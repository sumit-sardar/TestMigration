package com.ctb.lexington.domain.score.scorer.calculator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ctb.lexington.domain.score.event.CorrectResponseEvent;
import com.ctb.lexington.domain.score.event.IncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.Objective;
import com.ctb.lexington.domain.score.event.ObjectivePrimaryNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ObjectiveSecondaryNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestObjectiveCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestScoreReceivedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;
import com.ctb.lexington.domain.teststructure.MasteryLevel;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.AutoHashMap;
import com.ctb.lexington.util.SafeHashSet;

/**
 * This class associates incoming item response events (correct / incorrect) with the objective node
 * that the item rolls up into (at the primary and secondary reporting levels). For each objective,
 * the class keeps a set of correctly answered item IDs. When the subtest is complete, the class
 * publishes an ObjectiveNumberCorrectEvent for each roll-up objective, including the number of
 * correct answers for each.
 */
public class ObjectiveNumberCorrectCalculator extends Calculator {
    private final static class ItemIdSet extends SafeHashSet {
        public ItemIdSet() {
            super(String.class);
        }

        public ItemIdSet(final Collection ts) {
            super(String.class, ts);
        }
    }

    private final static class ObjectiveSetMap extends AutoHashMap {
        public ObjectiveSetMap() {
            super(Long.class, Set.class, ItemIdSet.class);
        }
    }

    private final Map primaryObjectivesCorrect = new ObjectiveSetMap();
    private final Map primaryObjectivesIncorrect = new ObjectiveSetMap();
    private final Map primaryObjectivesAttempted = new ObjectiveSetMap();
    private final Map primaryObjectivesUnattempted = new ObjectiveSetMap();

    private final Map secondaryObjectivesCorrect = new ObjectiveSetMap();
    private final Map secondaryObjectivesIncorrect = new ObjectiveSetMap();
    private final Map secondaryObjectivesAttempted = new ObjectiveSetMap();
    private final Map secondaryObjectivesUnattempted = new ObjectiveSetMap();
    
    private final Map<Integer, Integer> tabeAdaptiveObjectiveMap = new HashMap<Integer, Integer>();

    private SubtestObjectiveCollectionEvent subtestObjectives = null;

    private SubtestItemCollectionEvent sicEvent;
    
    //Added for TABE Adaptive
    private boolean isTabeAdaptive = false;
    private Integer objectiveId = null;
   // private Double objectiveRawScore = null;
   // private Double totalObjectiveRawScore = null;
   // private Double objectiveScore = null;
    private Integer objectiveMasteryLevel = 0;

    /**
     * Constructor for ObjectiveNumberCorrectCalculator.
     * 
     * @param channel
     * @param scorer
     */
    public ObjectiveNumberCorrectCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, SubtestItemCollectionEvent.class);
        // HACKTAG: Since we are handling events depth-first, we cannot validate state here
        // since the sicEvent has not reached this instance yet!
        
        channel.subscribe(this, SubtestObjectiveCollectionEvent.class);
        //mustPrecede(SubtestItemCollectionEvent.class, SubtestObjectiveCollectionEvent.class);
        channel.subscribe(this, ResponseReceivedEvent.class);
        // HACKTAG: Since we are handling events depth-first, we cannot validate state here
        // since the sicEvent has not reached this instance yet!
        //mustPrecede(SubtestObjectiveCollectionEvent.class, ResponseReceivedEvent.class);
        channel.subscribe(this, CorrectResponseEvent.class);
        mustPrecede(SubtestObjectiveCollectionEvent.class, CorrectResponseEvent.class);
        channel.subscribe(this, IncorrectResponseEvent.class);
        mustPrecede(SubtestObjectiveCollectionEvent.class, IncorrectResponseEvent.class);
        channel.subscribe(this, SubtestScoreReceivedEvent.class); // changes for TABE Adaptive
        channel.subscribe(this, SubtestEndedEvent.class);
        mustPrecede(SubtestObjectiveCollectionEvent.class, SubtestEndedEvent.class);
        
    }
    
    //This method is added for TABE Adaptive
    public void onEvent(SubtestScoreReceivedEvent event) {
    	isTabeAdaptive = true;
    	 this.objectiveId = event.getObjectiveId();
    	 //objectiveRawScore = event.getObjectiveRawScore();
    	// totalObjectiveRawScore = event.getTotalObjectiveRawScore();
    	 //objectiveScore = event.getObjectiveScore();
    	 this.objectiveMasteryLevel = event.getObjectiveMasteryLevel();
    //	 System.out.println("SubtestScoreReceivedEvent of ObjectiveNumberCorrectCalculator");
    	 tabeAdaptiveObjectiveMap.put(event.getObjectiveId(), event.getObjectiveMasteryLevel());
    //	 System.out.println(event.getObjectiveId()+"-"+event.getObjectiveMasteryLevel());
    	
    }

    public void onEvent(SubtestItemCollectionEvent event) {
        this.sicEvent = event;
    }

    public void onEvent(SubtestObjectiveCollectionEvent event) {
        subtestObjectives = event;

        Iterator iter = event.primaryReportingLevelObjectiveIterator();
        while (iter.hasNext()) {
            Objective objective = (Objective) iter.next();
            primaryObjectivesCorrect.put(objective.getId(), new ItemIdSet());
            primaryObjectivesIncorrect.put(objective.getId(), new ItemIdSet());
            primaryObjectivesAttempted.put(objective.getId(), new ItemIdSet());
            // Prepopulate the unattempteds to handle items without any response received event
            primaryObjectivesUnattempted.put(objective.getId(), new ItemIdSet(event
                    .getItemIdsForPrimaryObjectiveId(objective.getId())));
        }
        iter = event.secondaryReportingLevelObjectiveIterator();
        while (iter.hasNext()) {
            Objective objective = (Objective) iter.next();
            secondaryObjectivesCorrect.put(objective.getId(), new ItemIdSet());
            secondaryObjectivesIncorrect.put(objective.getId(), new ItemIdSet());
            secondaryObjectivesAttempted.put(objective.getId(), new ItemIdSet());
            // TODO: consider this more -- secondary objectives do not contribute to mastery level
            // Prepopulate the unattempteds to handle items without any response received event
            secondaryObjectivesUnattempted.put(objective.getId(), new ItemIdSet(event
                    .getItemIdsForSecondaryObjectiveId(objective.getId())));
        }
    }

    public void onEvent(ResponseReceivedEvent event) throws CTBSystemException {
        validateItemSetBeingProcessed(event.getItemSetId(), "Response received for wrong itemset.");

        List<Objective> primaryReportingLevelObjectiveList = subtestObjectives
                .getPrimaryReportingLevelObjective(event.getItemId());
        List<Objective> secondaryReportingLevelObjectiveList = subtestObjectives
                .getSecondaryReportingLevelObjective(event.getItemId());

        if (sicEvent.isAttempted(event)) {
        	
        	for (Objective primaryReportingLevelObjective :primaryReportingLevelObjectiveList) {
        		
        		addItemToObjectiveSet(primaryObjectivesAttempted, primaryReportingLevelObjective
                        .getId(), event.getItemId());
                removeItemFromObjectiveSet(primaryObjectivesUnattempted, primaryReportingLevelObjective
                        .getId(), event.getItemId());
        	}
            
            if (!secondaryReportingLevelObjectiveList.isEmpty()) {
            	
            	for (Objective secondaryReportingLevelObjective :secondaryReportingLevelObjectiveList) {
            		
            		 addItemToObjectiveSet(secondaryObjectivesAttempted,
                             secondaryReportingLevelObjective.getId(), event.getItemId());
                     removeItemFromObjectiveSet(secondaryObjectivesUnattempted,
                             secondaryReportingLevelObjective.getId(), event.getItemId());
            		
            	}
               
            }
        } else {
        	
        	for (Objective primaryReportingLevelObjective :primaryReportingLevelObjectiveList) {
        		
        		addItemToObjectiveSet(primaryObjectivesUnattempted, primaryReportingLevelObjective
                        .getId(), event.getItemId());
                removeItemFromObjectiveSet(primaryObjectivesAttempted, primaryReportingLevelObjective
                        .getId(), event.getItemId());
        	}
            
            if (!secondaryReportingLevelObjectiveList.isEmpty()) {
            	
            	for (Objective secondaryReportingLevelObjective :secondaryReportingLevelObjectiveList) {
            		
            		addItemToObjectiveSet(secondaryObjectivesUnattempted,
                            secondaryReportingLevelObjective.getId(), event.getItemId());
                    removeItemFromObjectiveSet(secondaryObjectivesAttempted,
                            secondaryReportingLevelObjective.getId(), event.getItemId());
            	}
                
            }
        }
    }

    public void onEvent(CorrectResponseEvent event) throws CTBSystemException {
        validateItemSetBeingProcessed(event.getItemSetId(), "Response received for wrong itemset.");

        List<Objective> primaryReportingLevelObjectiveList = subtestObjectives
        .getPrimaryReportingLevelObjective(event.getItemId());
        
        List<Objective> secondaryReportingLevelObjectiveList = subtestObjectives
        .getSecondaryReportingLevelObjective(event.getItemId());
        
        for (Objective primaryReportingLevelObjective :primaryReportingLevelObjectiveList) {
        	
        	 addItemToObjectiveSet(primaryObjectivesCorrect, primaryReportingLevelObjective.getId(), event.getItemId());
             removeItemFromObjectiveSet(primaryObjectivesIncorrect, primaryReportingLevelObjective.getId(), event.getItemId());
        }
       

        
        if (!secondaryReportingLevelObjectiveList.isEmpty()) {
        	
        	for (Objective secondaryReportingLevelObjective :secondaryReportingLevelObjectiveList) {
	            addItemToObjectiveSet(secondaryObjectivesCorrect, secondaryReportingLevelObjective
	                    .getId(), event.getItemId());
	            removeItemFromObjectiveSet(secondaryObjectivesIncorrect,
	                    secondaryReportingLevelObjective.getId(), event.getItemId());
        	}
        }
    }

    public void onEvent(IncorrectResponseEvent event) throws CTBSystemException {
        validateItemSetBeingProcessed(event.getItemSetId(), "Response received for wrong itemset.");

        List<Objective> primaryReportingLevelObjectiveList = subtestObjectives
                .getPrimaryReportingLevelObjective(event.getItemId());
        for (Objective primaryReportingLevelObjective :primaryReportingLevelObjectiveList) {
        	
        	 addItemToObjectiveSet(primaryObjectivesIncorrect, primaryReportingLevelObjective.getId(),
                     event.getItemId());
             removeItemFromObjectiveSet(primaryObjectivesCorrect,
                     primaryReportingLevelObjective.getId(), event.getItemId());
        }
       

        List<Objective> secondaryReportingLevelObjectiveList = subtestObjectives
                .getSecondaryReportingLevelObjective(event.getItemId());
        if (!secondaryReportingLevelObjectiveList.isEmpty()) {
        	
        	for (Objective secondaryReportingLevelObjective :secondaryReportingLevelObjectiveList) {
        		
        		addItemToObjectiveSet(secondaryObjectivesIncorrect, secondaryReportingLevelObjective
                        .getId(), event.getItemId());
                removeItemFromObjectiveSet(secondaryObjectivesCorrect, secondaryReportingLevelObjective
                        .getId(), event.getItemId());
        	}
            
        }
    }

    public void onEvent(SubtestEndedEvent event) {
    	if(!isTabeAdaptive) {
	        validateItemSetBeingProcessed(event.getItemSetId(), "Response received for wrong itemset.");
	
	        for (Iterator iter = primaryObjectivesCorrect.keySet().iterator(); iter.hasNext();) {
	            Long objectiveId = (Long) iter.next();
	            int numItemsCorrectForObjective = ((Set) primaryObjectivesCorrect.get(objectiveId))
	                    .size();
	            int numItemsIncorrectForObjective = ((Set) primaryObjectivesIncorrect.get(objectiveId))
	                    .size();
	            int numItemsAttemptedForObjective = ((Set) primaryObjectivesAttempted.get(objectiveId))
	                    .size();
	            int numItemsUnattemptedForObjective = ((Set) primaryObjectivesUnattempted
	                    .get(objectiveId)).size();
	
	            MasteryLevel masteryLevel = calculateMasteryLevel(numItemsCorrectForObjective,
	                    numItemsAttemptedForObjective + numItemsUnattemptedForObjective);
	            ObjectivePrimaryNumberCorrectEvent numberCorrectEvent = new ObjectivePrimaryNumberCorrectEvent(
	                    event.getTestRosterId(), objectiveId, sicEvent.getItems().size(),
	                    numItemsCorrectForObjective, numItemsIncorrectForObjective,
	                    numItemsAttemptedForObjective, numItemsUnattemptedForObjective, masteryLevel, new Long(event.getItemSetId().longValue()));
	            channel.send(numberCorrectEvent);
	        }
	        primaryObjectivesCorrect.clear();
	
	        for (Iterator iter = secondaryObjectivesCorrect.keySet().iterator(); iter.hasNext();) {
	            Long objectiveId = (Long) iter.next();
	            int numItemsCorrectForObjective = ((Set) secondaryObjectivesCorrect.get(objectiveId))
	                    .size();
	            int numItemsIncorrectForObjective = ((Set) secondaryObjectivesIncorrect
	                    .get(objectiveId)).size();
	            int numItemsAttemptedForObjective = ((Set) secondaryObjectivesAttempted
	                    .get(objectiveId)).size();
	            int numItemsUnattemptedForObjective = ((Set) secondaryObjectivesUnattempted
	                    .get(objectiveId)).size();
	            ObjectiveSecondaryNumberCorrectEvent numberCorrectEvent = new ObjectiveSecondaryNumberCorrectEvent(
	                    event.getTestRosterId(), objectiveId, sicEvent.getItems().size(),
	                    numItemsCorrectForObjective, numItemsIncorrectForObjective,
	                    numItemsAttemptedForObjective, numItemsUnattemptedForObjective, new Long(event.getItemSetId().longValue()));
	            channel.send(numberCorrectEvent);
	        }
	        secondaryObjectivesCorrect.clear();
	    } else {  	
	    	for (Map.Entry<Integer, Integer> entry : tabeAdaptiveObjectiveMap.entrySet()) {
	            Long objectiveId =  new Long (entry.getKey());
	          //  System.out.println("objectiveId ==--" + objectiveId);
	            if(objectiveId > 0) {
		            int masteryValue = entry.getValue();
		            MasteryLevel masteryLevel;
		            if(masteryValue == 0)
			    		masteryLevel = MasteryLevel.masteryLevelForPercentage(10);
			    	else if (masteryValue == 1)
			    		masteryLevel = MasteryLevel.masteryLevelForPercentage(60);
			    	else
			    		masteryLevel = MasteryLevel.masteryLevelForPercentage(80);
			    //	System.out.println("******objectiveMasteryLevel -> masteryLevel.getCode() -> " + masteryLevel.getCode());
			    //	System.out.println("event.getItemSetId().longValue() -> " + event.getItemSetId().longValue());
	
		            ObjectivePrimaryNumberCorrectEvent numberCorrectEvent = new ObjectivePrimaryNumberCorrectEvent(
		                    event.getTestRosterId(), objectiveId, sicEvent.getItems().size(),
		                    0, 0,
		                    0, 0, masteryLevel, new Long(event.getItemSetId().longValue()));
		            channel.send(numberCorrectEvent);
	            }
	        }
	    	tabeAdaptiveObjectiveMap.clear();	    	
	    }
    }

    public static MasteryLevel calculateMasteryLevel(int numItemsCorrectForObjective,
            int totalItemsForObjective) {
        if (numItemsCorrectForObjective < 0) {
            throw new IllegalArgumentException("Negative item count: "
                    + numItemsCorrectForObjective);
        }
        if (totalItemsForObjective < 1) {
            throw new IllegalArgumentException("Non-positive item count: " + totalItemsForObjective);
        }
        if (numItemsCorrectForObjective > totalItemsForObjective) {
            throw new IllegalArgumentException("Too many items: " + numItemsCorrectForObjective
                    + " v. " + totalItemsForObjective);
        }

        return MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage(
                numItemsCorrectForObjective, totalItemsForObjective));
    }

    private void validateItemSetBeingProcessed(Integer evtItemSetId, String message) {
        if (!subtestObjectives.getItemSetId().equals(evtItemSetId))
            throw new IllegalArgumentException(message + " expected "
                    + subtestObjectives.getItemSetId() + " but got " + evtItemSetId);
    }

    /**
     * Beware &mdash; <var>subtestObjectives </var> may be uninitialized if the events come in the
     * wrong order.
     * 
     * @param map
     * @param itemId
     * @todo Refactor if possible to remove NPE potential
     */
    private void addItemToObjectiveSet(Map map, Long objectiveID, String itemId) {
        ((Set) (map.get(objectiveID))).add(itemId);
    }

    /**
     * Beware &mdash; <var>subtestObjectives </var> may be uninitialized if the events come in the
     * wrong order.
     * 
     * @param map
     * @param itemId
     * @todo Refactor if possible to remove NPE potential
     */
    private void removeItemFromObjectiveSet(Map map, Long objectiveID, String itemId) {
        ((Set) (map.get(objectiveID))).remove(itemId);
    }
}