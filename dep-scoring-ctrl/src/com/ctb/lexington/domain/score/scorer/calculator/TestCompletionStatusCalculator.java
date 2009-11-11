package com.ctb.lexington.domain.score.scorer.calculator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import com.ctb.lexington.data.StudentItemSetStatusRecord;
import com.ctb.lexington.db.data.TestRosterRecord;
import com.ctb.lexington.db.mapper.ItemResponseMapper;
import com.ctb.lexington.db.mapper.StudentItemSetStatusMapper;
import com.ctb.lexington.db.mapper.TestRosterMapper;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.TestCompletionStatusEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.CompletionStatus;
import com.ctb.lexington.domain.teststructure.RosterCaptureMethod;
import com.ctb.lexington.exception.CTBSystemException;

public class TestCompletionStatusCalculator extends Calculator {
    private List itemSetIdsForEndedSubTests;
    private Collection sicEvents = new ArrayList();
    
    private static final String decisionNodeProps = "completionstatus";
    private static DecisionNodeTree decisionTree = null;
    
    /**
     * Constructor for TestCompletionStatusCalculator.
     *
     * @param channel
     * @param scorer
     * @throws IOException
     * @throws CTBSystemException
     */
    public TestCompletionStatusCalculator(Channel channel, Scorer scorer) throws CTBSystemException, IOException {
        super(channel, scorer);

        channel.subscribe(this, SubtestItemCollectionEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
        channel.subscribe(this, AssessmentEndedEvent.class);
        mustPrecede(AssessmentStartedEvent.class, SubtestItemCollectionEvent.class);
        mustPrecede(SubtestItemCollectionEvent.class, SubtestEndedEvent.class);
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

    public void onEvent(SubtestItemCollectionEvent sicEvent){
        sicEvents.add(sicEvent);
    }

    public void onEvent(SubtestEndedEvent event) {
        if (itemSetIdsForEndedSubTests.size() + 1 != sicEvents.size()){
            throw new IllegalStateException("A SubtestEndedEvent was received without a matching SubtestItemCollection event");
        }

        Long itemSetId = DatabaseHelper.asLong(event.getItemSetId());
        if (!itemSetIdsForEndedSubTests.contains(itemSetId)) {
            itemSetIdsForEndedSubTests.add(itemSetId);
        }
    }

    public void onEvent(AssessmentEndedEvent event) {
    	CompletionStatus status = getCompletionStatus(event.getTestRosterId());
        if(status != null) {
        	channel.send(createEvent(event.getTestRosterId(), status));
        }
    }
    
    private CompletionStatus getCompletionStatus(Long testRosterId) {
        final CompletionStatus NO_CHANGE = null;
        
        final boolean allTestsFinished = checkWhetherAllTestsHaveBeenTaken(testRosterId);
        
        final String rosterCaptureMethod = retrieveRosterCaptureMethod(testRosterId);
        
        boolean isOnlineCapture = RosterCaptureMethod.ONLINE.getCode().equals(rosterCaptureMethod);
        boolean isOfflineCapture = RosterCaptureMethod.OFFLINE.getCode().equals(rosterCaptureMethod);
        boolean isMixedCapture = RosterCaptureMethod.MIXED.getCode().equals(rosterCaptureMethod);

        final int countItems = getTotalItems();
        final int countScoredItems = getCountOfItemsThatAreScored(testRosterId);
        final int countScoredOfflineItems = getCountOfOfflineItemsThatAreScored(testRosterId);
        final int countOfflineItems = getTotalOfflineItems();
        final int countOnlineItems = countItems - countOfflineItems;
        final int countScoredOnlineItems = countScoredItems - countScoredOfflineItems;
        
        final boolean unscoredOnlineItems = countScoredOnlineItems < countOnlineItems;
        final boolean scoredOnlineItems = countScoredOnlineItems > 0;
        
        final boolean offlineItemsExist = countOfflineItems > 0;
        final boolean allItemsOffline = countOfflineItems == countItems;
        final boolean unscoredOfflineItems = countOfflineItems!=countScoredOfflineItems;
        final boolean scoredOfflineItems = countScoredOfflineItems > 0;
        
        List path = new ArrayList();
        path.add(new Boolean(allTestsFinished));
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
        if(status != null && !"NC".equals(status)) {
        	return CompletionStatus.getByCode(status);
        }else {
        	return NO_CHANGE;
        }
    }

    protected String retrieveRosterCaptureMethod(Long testRosterId) {
        String method = null;
        Connection oasConnection = null;

        try {
            oasConnection = scorer.getOASConnection();
            TestRosterMapper mapper = getRosterMapper(oasConnection);
            TestRosterRecord record = mapper.findTestRoster(testRosterId);
            method = record.getCaptureMethod();
        } catch (Exception e) {
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
    
    private boolean offlineItemsExist(){
        boolean result = false;

        for(Iterator iterator = sicEvents.iterator(); iterator.hasNext();){
            SubtestItemCollectionEvent event = (SubtestItemCollectionEvent)iterator.next();

            if( event.hasOfflineItems()){
                result = true;
                break;
            }
        }

        return result;
    }

    private int getTotalOfflineItems() {
        int count=0;
        for(Iterator iterator = sicEvents.iterator(); iterator.hasNext();){
            SubtestItemCollectionEvent event = (SubtestItemCollectionEvent)iterator.next();
            count += event.countOfflineItems();
        }
        return count;
    }
    
    private int getTotalOnlineItems() {
        int count=0;
        for(Iterator iterator = sicEvents.iterator(); iterator.hasNext();){
            SubtestItemCollectionEvent event = (SubtestItemCollectionEvent)iterator.next();
            count += event.getItems().size() - event.countOfflineItems();
        }
        return count;
    }
    
    private int getTotalItems() {
        int count=0;
        for(Iterator iterator = sicEvents.iterator(); iterator.hasNext();){
            SubtestItemCollectionEvent event = (SubtestItemCollectionEvent)iterator.next();
            count += event.getItems().size();
        }
        return count;
    }

    private boolean checkWhetherAllTestsHaveBeenTaken(Long testRosterId) {
        List expectedSubtests = getExpectedSubtests(testRosterId);
        return (expectedSubtests.size() == itemSetIdsForEndedSubTests.size() &&
        		itemSetIdsForEndedSubTests.containsAll(expectedSubtests));
    }
    
    private boolean checkWhetherAnyTestsHaveBeenTaken() {
        return (itemSetIdsForEndedSubTests.size() > 0);
    }

    private List getExpectedSubtests(Long testRosterId) {
        List expectedItemSetIds = new ArrayList();
        Connection oasConnection = null;
        try {
            oasConnection = scorer.getOASConnection();
            StudentItemSetStatusMapper mapper = new StudentItemSetStatusMapper(oasConnection);
            List studentItemSetStatuses = mapper.findStudentItemSetStatusesForRoster(testRosterId);
            for (Iterator iter = studentItemSetStatuses.iterator(); iter.hasNext();) {
                StudentItemSetStatusRecord record = (StudentItemSetStatusRecord) iter.next();
                expectedItemSetIds.add(record.getItemSetId());
            }
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        } finally {
            try {
                scorer.close(true, oasConnection);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return expectedItemSetIds;
    }

    private TestCompletionStatusEvent createEvent(Long testRosterId, CompletionStatus status) {
        return new TestCompletionStatusEvent(testRosterId, status,
                new Timestamp(System.currentTimeMillis()));
    }

    private int getCountOfOfflineItemsThatAreScored(Long testRosterId) {
        Connection oasConnection = null;
        try {
            oasConnection = scorer.getOASConnection();
            ItemResponseMapper mapper = getResponseMapper(oasConnection);
            return mapper.getCountOfOfflineItemsThatAreScored(testRosterId);
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
    
    private int getCountOfItemsThatAreScored(Long testRosterId) {
        Connection oasConnection = null;
        try {
            oasConnection = scorer.getOASConnection();
            ItemResponseMapper mapper = getResponseMapper(oasConnection);
            return mapper.getCountOfItemsThatAreScored(testRosterId);
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

    protected TestRosterMapper getRosterMapper(Connection oasConnection) {
        return new TestRosterMapper(oasConnection);
    }

    protected ItemResponseMapper getResponseMapper(Connection oasConnection) {
        return new ItemResponseMapper(oasConnection);
    }
}