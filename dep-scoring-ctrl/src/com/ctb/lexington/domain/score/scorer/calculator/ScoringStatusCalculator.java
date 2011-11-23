package com.ctb.lexington.domain.score.scorer.calculator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.db.data.TestRosterRecord;
import com.ctb.lexington.db.mapper.ItemResponseMapper;
import com.ctb.lexington.db.mapper.TestRosterMapper;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.ScoringStatusEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.RosterCaptureMethod;
import com.ctb.lexington.domain.teststructure.ScoringStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.SafeHashMap;
import com.ctb.lexington.util.SafeHashSet;

/**
 * Scoring status is calculated for an assessment. The rules are as follows:
 *
 * While scoring runs the assessment is IN_PROGRESS status. This is done by the scorer before the events
 * are sent to the channel.
 *
 * If the test is hand-scored, the test is PARTIALLY_SCORED if a response is not present for each item. If
 * a response is supplied for each item the test is SCORED.
 *
 * If the test is taken on-line missing the test is SCORED if the only missing item responses are SR items.
 * Otherwise the test is PARTIALLY_SCORED.
 */
public class ScoringStatusCalculator extends Calculator {
    private Long testRosterId;
    private List itemSetIdsForEndedSubTests;

    private static final String decisionNodeProps = "scoringstatus";
    private static DecisionNodeTree decisionTree = null;

    // This calculator works across a whole assessment so hold on to multiple sicEvents keyed by itemSetId
    private final Map sicEvents = new SafeHashMap(Integer.class, SubtestItemCollectionEvent.class);

    // keep a list of all items in a subtest that have not been attempted yet (A Set for each itemSetId)
    private final Map itemsLeftInSubtest = new SafeHashMap(Integer.class, Set.class);

    // On-line tests and key entry set the capture method to a value from RosterCaptureMethod
    private String rosterCaptureMethod = null;

    public ScoringStatusCalculator(Channel channel, Scorer scorer) throws CTBSystemException, IOException {
        super(channel, scorer);

        channel.subscribe(this, AssessmentStartedEvent.class);
        channel.subscribe(this, SubtestItemCollectionEvent.class);
        channel.subscribe(this, ResponseReceivedEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
        channel.subscribe(this, AssessmentEndedEvent.class);

        mustPrecede(AssessmentStartedEvent.class, SubtestItemCollectionEvent.class);
        mustPrecede(AssessmentStartedEvent.class, SubtestEndedEvent.class);
        mustPrecede(AssessmentStartedEvent.class, AssessmentEndedEvent.class);
        
        itemSetIdsForEndedSubTests = new ArrayList();
        
        if( decisionTree == null ) {
	        synchronized(getClass()) {
	        	if(decisionTree == null) {
		            	decisionTree = new DecisionNodeTree(decisionNodeProps);
		        }
	        }
        }
    }

    public void onEvent(AssessmentStartedEvent event) {
        testRosterId = event.getTestRosterId();
        rosterCaptureMethod = getRosterCaptureMethod(testRosterId);
    }

    public void onEvent(SubtestEndedEvent event) {
        if (itemSetIdsForEndedSubTests.size() + 1 != sicEvents.size()){
            throw new IllegalStateException("A SubtestEndedEvent was received without a matching SubtestItemCollection event");
        }

        Integer itemSetId = event.getItemSetId();
        if (!itemSetIdsForEndedSubTests.contains(itemSetId)) {
            itemSetIdsForEndedSubTests.add(itemSetId);
        }
    }
    
    protected String getRosterCaptureMethod(Long testRosterId) {
        String method = null;
        Connection oasConnection = null;

        try {
            oasConnection = scorer.getOASConnection();
            TestRosterMapper mapper = new TestRosterMapper(oasConnection);
            TestRosterRecord record = mapper.findTestRoster(testRosterId);
            method = record.getCaptureMethod();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                scorer.close(true, oasConnection);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return method;
    }

    public void onEvent(SubtestItemCollectionEvent sicEvent) {
        final Integer itemSetId = sicEvent.getItemSetId();

        if (sicEvents.containsKey(itemSetId))
            throw new IllegalStateException("SubtestItemCollection event " + itemSetId + " received twice within roster: " + testRosterId);

        sicEvents.put(itemSetId, sicEvent);

        itemsLeftInSubtest.put(itemSetId, new SafeHashSet(String.class, sicEvent.getItemIds()));
    }

    public void onEvent(ResponseReceivedEvent event) {
        // If remove blows here with IllegalArgumentException, it is because we
        // got a RR event for an item *not* in this subtest -- bad problem elsewhere
        ((Set)itemsLeftInSubtest.get(event.getItemSetId())).remove(event.getItemId());
    }

    public void onEvent(AssessmentEndedEvent event) {
        // determine status for each subtest first, then roll up into assessment status
        ScoringStatus assessmentStatus = determineAssessmentStatus(getAllSubtestStatuses());

        if(assessmentStatus != null) {
        	channel.send (new ScoringStatusEvent(testRosterId, assessmentStatus));
        } else {
        	channel.send (new ScoringStatusEvent(testRosterId, ScoringStatus.NOT_SCORED));
        }
    }

    private Set getAllSubtestStatuses() {
        Set subtestStatuses = new HashSet();

        for (Iterator iter = sicEvents.keySet().iterator(); iter.hasNext();) {
            Integer itemSetId = (Integer) iter.next();
            ScoringStatus status = determineSubtestStatus(itemSetId);
            subtestStatuses.add(status.getCode());
        }

        return subtestStatuses;
    }

    /**
     * We do not have to check the TestCompletionStatus here because the scorer assigns IN_PROGRESS
     * and SCORING_PROBLEM when an assessment starts and when it ends unexpectedly (e.g., by system stop)
     */
    private ScoringStatus determineSubtestStatus(Integer itemSetId) {
        SubtestItemCollectionEvent sicEvent = (SubtestItemCollectionEvent) sicEvents.get(itemSetId);
        if(sicEvent.getProductId() == 8001) {
        	//System.out.println("Making all scoring status as SCORED");
        	return ScoringStatus.SCORED;
        }
        final boolean subtestFinished = checkWhetherSubtestHasBeenTaken(itemSetId);
        
        boolean isOnlineCapture = RosterCaptureMethod.ONLINE.getCode().equals(rosterCaptureMethod);
        boolean isOfflineCapture = RosterCaptureMethod.OFFLINE.getCode().equals(rosterCaptureMethod);
        boolean isMixedCapture = RosterCaptureMethod.MIXED.getCode().equals(rosterCaptureMethod);

        final int countItems = sicEvent.getItems().size();
        final int countScoredItems = getCountOfItemsThatAreScored(testRosterId, itemSetId);
        final int countScoredOfflineItems = getCountOfOfflineItemsThatAreScored(testRosterId, itemSetId);
        final int countOfflineItems = getTotalOfflineItems(itemSetId);
        final int countOnlineItems = countItems - countOfflineItems;
        final int countScoredOnlineItems = countScoredItems - countScoredOfflineItems;
        
        final boolean unscoredOnlineItems = countScoredOnlineItems < countOnlineItems;
        final boolean scoredOnlineItems = countScoredOnlineItems > 0;
        
        final boolean offlineItemsExist = countOfflineItems > 0;
        final boolean allItemsOffline = countOfflineItems == countItems;
        final boolean unscoredOfflineItems = countOfflineItems!=countScoredOfflineItems;
        final boolean scoredOfflineItems = countScoredOfflineItems > 0;
        
        final int countOnlineCR = getCountOfOnlineCR(itemSetId);
        final int countScoredOnlineCR = getCountOfOnlineCrItemsThatAreScored(testRosterId, itemSetId);
        final boolean hasOnlineCRs = countOnlineCR > 0;
        final boolean allItemsOnlineCR = countOnlineCR == countItems;
        final boolean allOnlineCRItemsScored = countOnlineCR == countScoredOnlineCR;

        List path = new ArrayList();
        path.add(new Boolean(subtestFinished));
        path.add(new Boolean(isOnlineCapture));
        path.add(new Boolean(isOfflineCapture));
        path.add(new Boolean(isMixedCapture));
        path.add(new Boolean(unscoredOnlineItems));
        path.add(new Boolean(scoredOnlineItems));
        path.add(new Boolean(offlineItemsExist));
        path.add(new Boolean(allItemsOffline));
        path.add(new Boolean(unscoredOfflineItems));
        path.add(new Boolean(scoredOfflineItems));
        
        String status = (String) decisionTree.getValueForPath(path);
        if(status != null) {
        	if( isOnlineCapture && hasOnlineCRs && !allOnlineCRItemsScored && status.equalsIgnoreCase(ScoringStatus.SCORED.getCode()) ) {
        		// TODO: Offline vs Online CR madness
        		System.out.println("DecisionTree hack for online CR items... setting scoring status to PC");
            	return ScoringStatus.PARTIALLY_SCORED;
        	} else if( isMixedCapture && hasOnlineCRs && !allOnlineCRItemsScored && status.equalsIgnoreCase(ScoringStatus.SCORED.getCode()) ) {
        		// TODO: Offline vs Online CR madness, part deux
        		System.out.println("DecisionTree hack for online CR items... setting scoring status to PC");
            	return ScoringStatus.PARTIALLY_SCORED;
        	} else {
            	return ScoringStatus.getByCode(status);
        	}
        }else {
        	return ScoringStatus.SCORING_PROBLEM;
        }
    }
    
    /**
	 * @return Number of online CR items within the item set.
	 */
	private int getCountOfOnlineCR(Integer itemSetId) {
    	SubtestItemCollectionEvent sicEvent = (SubtestItemCollectionEvent) sicEvents.get(itemSetId);
    	Iterator iterator = sicEvent.getItems().iterator();
    	int count = 0;
    	while(iterator.hasNext()) {
    		ItemVO item = (ItemVO) iterator.next();
    		if( item.getOnlineCr() != null && item.getOnlineCr().equalsIgnoreCase("T") ) {
    			count++;
    		}
    	}
    	return count;
	}

	private boolean checkWhetherSubtestHasBeenTaken(Integer itemSetId) {
        return itemSetIdsForEndedSubTests.contains(itemSetId);
    }
    
    private boolean itemsLeftForSubtest(Integer itemSetId) {
        return !((Set)itemsLeftInSubtest.get(itemSetId)).isEmpty();
    }

    /**
     * @param subtestStatusList a set of ScoringStatus codes. Only use a set because having multiple
     *        subtests with the same status does not change the result.
     * @return status code for the assessment
     *
     * NOTE: this is public only for testability purposes
     */
    public static ScoringStatus determineAssessmentStatus (Set subtestStatusList) {
        if (subtestStatusList.contains(ScoringStatus.IN_PROGRESS.getCode()))
            return ScoringStatus.IN_PROGRESS;

        if (subtestStatusList.contains(ScoringStatus.PARTIALLY_SCORED.getCode()))
            return ScoringStatus.PARTIALLY_SCORED;

        if (subtestStatusList.contains(ScoringStatus.NOT_SCORED.getCode()))
            return ScoringStatus.PARTIALLY_SCORED;

        if (subtestStatusList.contains(ScoringStatus.SCORED.getCode()))
            return ScoringStatus.SCORED;

        // won't happen unless set is empty
        return null;
    }
    
    private int getCountOfOfflineItemsThatAreScored(Long testRosterId, Integer itemSetId) {
        Connection oasConnection = null;
        try {
            oasConnection = scorer.getOASConnection();
            ItemResponseMapper mapper = getResponseMapper(oasConnection);
            return mapper.getCountOfOfflineItemsThatAreScored(testRosterId, DatabaseHelper.asLong(itemSetId));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                scorer.close(true, oasConnection);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private int getTotalOfflineItems(Integer itemSetId) {
    	SubtestItemCollectionEvent sicEvent = (SubtestItemCollectionEvent) sicEvents.get(itemSetId);
        return sicEvent.countOfflineItems();
    }
    
    protected ItemResponseMapper getResponseMapper(Connection oasConnection) {
        return new ItemResponseMapper(oasConnection);
    }
    
    private int getCountOfItemsThatAreScored(Long testRosterId, Integer itemSetId) {
    	SubtestItemCollectionEvent sicEvent = (SubtestItemCollectionEvent) sicEvents.get(itemSetId);
    	Iterator iterator = sicEvent.getItems().iterator();
    	int count = 0;
    	while(iterator.hasNext()) {
    		ItemVO item = (ItemVO) iterator.next();
    		String itemId = item.getItemId();
    		if(!((Set)itemsLeftInSubtest.get(itemSetId)).contains(itemId)){
    			count++;
    		}
    	}
    	return count;
    }

    private int getCountOfOnlineCrItemsThatAreScored(Long testRosterId, Integer itemSetId) {
    	SubtestItemCollectionEvent sicEvent = (SubtestItemCollectionEvent) sicEvents.get(itemSetId);
    	Iterator iterator = sicEvent.getItems().iterator();
    	int count = 0;
    	while(iterator.hasNext()) {
    		ItemVO item = (ItemVO) iterator.next();
    		String itemId = item.getItemId();
    		if(!((Set)itemsLeftInSubtest.get(itemSetId)).contains(itemId) && item.getOnlineCr().equalsIgnoreCase("T") ){
    			count++;
    		}
    	}
    	return count;
    }
    
}