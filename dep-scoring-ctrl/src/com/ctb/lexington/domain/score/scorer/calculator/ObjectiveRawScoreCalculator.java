package com.ctb.lexington.domain.score.scorer.calculator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.domain.score.event.Objective;
import com.ctb.lexington.domain.score.event.ObjectiveRawScoreEvent;
import com.ctb.lexington.domain.score.event.PointEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestObjectiveCollectionEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.SafeHashMap;

/**
 * This class associates incoming point events with the objective node that the item rolls up into
 * (at the primary and secondary reporting levels). For each objective, the class keeps a tally of
 * points obtained. When the subtest is complete, the class publishes an ObjectiveRawScoresEvent for
 * each roll-up objective, including the points obtained for each.
 */
public class ObjectiveRawScoreCalculator extends Calculator {
    private final Map primaryObjectives = new SafeHashMap(Long.class, Map.class);
    private final Map secondaryObjectives = new SafeHashMap(Long.class, Map.class);
    private final Map subtestObjectiveMap = new SafeHashMap(Long.class, Objective.class);
    private SubtestObjectiveCollectionEvent subtestObjectives = null;

    /**
     * Constructor for ObjectiveNumberCorrectCalculator.
     * 
     * @param channel
     * @param scorer
     */
    public ObjectiveRawScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, SubtestObjectiveCollectionEvent.class);
        channel.subscribe(this, PointEvent.class);
        mustPrecede(SubtestObjectiveCollectionEvent.class, PointEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
        mustPrecede(SubtestObjectiveCollectionEvent.class, SubtestEndedEvent.class);
    }

    public void onEvent(SubtestObjectiveCollectionEvent event) {
        subtestObjectives = event;

        Iterator iter = event.primaryReportingLevelObjectiveIterator();
        while (iter.hasNext()) {
            Objective element = (Objective) iter.next();
            primaryObjectives.put(element.getId(), new HashMap()); // null values ok?
            subtestObjectiveMap.put(element.getId(), element);
        }
        iter = event.secondaryReportingLevelObjectiveIterator();
        while (iter.hasNext()) {
            Objective element = (Objective) iter.next();
            secondaryObjectives.put(element.getId(), new HashMap()); // null values ok?
            subtestObjectiveMap.put(element.getId(), element);
        }
    }

    public void onEvent(PointEvent event) throws CTBSystemException {
        validateItemSetBeingProcessed(event.getItemSetId(), "Response received for wrong itemset.");

        List<Objective> primaryObjectiveList = subtestObjectives.getPrimaryReportingLevelObjective(event
                .getItemId());
        
        for (Objective primaryObjective : primaryObjectiveList) {
        	
        	setItemPointsForObjectiveSet(primaryObjectives, primaryObjective, event);
        }
        
        

        List<Objective> secondaryObjectiveList = subtestObjectives.getSecondaryReportingLevelObjective(event
                .getItemId());
        if (!secondaryObjectiveList.isEmpty())
        	
        	for (Objective secondaryObjective : secondaryObjectiveList) {
        		
        		setItemPointsForObjectiveSet(secondaryObjectives, secondaryObjective, event);
        	}
            
    
        final Long testRosterId = event.getTestRosterId();
        //calculateAndSendRawScoreEvent(testRosterId, primaryObjectives, Objective.PRIMARY, new Long(event.getItemSetId().longValue()));
        //calculateAndSendRawScoreEvent(testRosterId, secondaryObjectives, Objective.SECONDARY, new Long(event.getItemSetId().longValue()));
    }

    public void onEvent(SubtestEndedEvent event) {
        validateItemSetBeingProcessed(event.getItemSetId(), "Response received for wrong itemset.");

        final Long testRosterId = event.getTestRosterId();
        calculateAndSendRawScoreEvent(testRosterId, primaryObjectives, Objective.PRIMARY, new Long(event.getItemSetId().longValue()));
        calculateAndSendRawScoreEvent(testRosterId, secondaryObjectives, Objective.SECONDARY, new Long(event.getItemSetId().longValue()));
    
        Iterator primIter = primaryObjectives.keySet().iterator();
        while(primIter.hasNext()) {
            primaryObjectives.put(primIter.next(), new HashMap());
        }
        Iterator secIter = secondaryObjectives.keySet().iterator();
        while(secIter.hasNext()) {
            secondaryObjectives.put(secIter.next(), new HashMap());
        }    
    }
    
    private void validateItemSetBeingProcessed(Integer evtItemSetId, String message) {
        if (!subtestObjectives.getItemSetId().equals(evtItemSetId))
            throw new IllegalArgumentException(message + " expected "
                    + subtestObjectives.getItemSetId() + " but got " + evtItemSetId);
    }

    private void setItemPointsForObjectiveSet(Map map, Objective objective, PointEvent event) {
        final Map itemPointsForObjective = (Map) (map.get(objective.getId()));
        itemPointsForObjective.put(event.getItemId(), new ItemScore(event.getPointsObtained(),
                event.getPointsAttempted()));
    }

    private void calculateAndSendRawScoreEvent(final Long testRosterId, final Map objectiveMap,
            final String objectiveLevel, Long subtestId) {
        for (final Iterator it = objectiveMap.entrySet().iterator(); it.hasNext();) {
            final Map.Entry entry = (Map.Entry) it.next();
            final Long objectiveId = (Long) entry.getKey();
            final Map itemMap = (Map) entry.getValue();

            int pointsObtained = 0;
            int pointsAttempted = 0;

            for (final Iterator scoreIt = itemMap.values().iterator(); scoreIt.hasNext();) {
                final ItemScore score = (ItemScore) scoreIt.next();

                if (score != null && score.getPointsObtained() != null) {
                    pointsObtained += score.getPointsObtained().intValue();
                }
                if (score != null && score.getPointsAttempted() != null) {
                    pointsAttempted += score.getPointsAttempted().intValue();
                }
            }

            int pointsPossible = getPointsPossibleFor(objectiveId);
            
            if(isLessItemCountForTV(objectiveId) &&  objectiveLevel.equals(Objective.PRIMARY)){ //Added for TN Online 
            	pointsObtained = 0;
            }
            //System.out.println("objectiveId==>"+objectiveId);
            channel.send(new ObjectiveRawScoreEvent(testRosterId, objectiveId, objectiveLevel,
                    pointsPossible, pointsObtained, pointsAttempted, ScorerHelper.calculatePercentage(
                            pointsObtained, pointsPossible), subtestId));
        }
    }
    
    //Added to for story : Suppress OPI, Raw and Master scores for objectives for less than 4 items
    private boolean isLessItemCountForTV(Long objectiveId){
    	if(scorer.getResultHolder().getAdminData().getProductId().intValue() == 3500 && 
    			((Objective)subtestObjectiveMap.get(objectiveId)).getNumberOfItems().intValue()<4)
    		return true;
    	else
    		return false;
    }
    
    private int getPointsPossibleFor(final Long objectiveID) {
        return ((Objective) subtestObjectiveMap.get(objectiveID)).getPointsPossible().intValue();
    }

    private class ItemScore {
        private Integer pointsObtained;
        private Integer pointsAttempted;

        public ItemScore(Integer pointsObtained, Integer pointsAttempted) {
            this.pointsObtained = pointsObtained;
            this.pointsAttempted = pointsAttempted;
        }

        public Integer getPointsAttempted() {
            return pointsAttempted;
        }

        public Integer getPointsObtained() {
            return pointsObtained;
        }
    }
}