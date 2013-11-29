package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.util.Map;

import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.Objective;
import com.ctb.lexington.domain.score.event.ObjectiveRawScoreEvent;
import com.ctb.lexington.domain.score.event.SecondaryObjectiveDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.SafeHashMap;

public class TASCSecondaryObjectiveDerivedScoreCalculator extends
		AbstractDerivedScoreCalculator {

	private static Map<Long,ObjectiveRawScoreEvent> secondaryObjectivePointObtained = new SafeHashMap(Long.class, ObjectiveRawScoreEvent.class);
	private static final String TASC_FRAMEWORK_CODE = "TASC";
	
	public TASCSecondaryObjectiveDerivedScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
        channel.subscribe(this, AssessmentStartedEvent.class);
        channel.subscribe(this, SubtestStartedEvent.class);
        channel.subscribe(this, ObjectiveRawScoreEvent.class);
    }
	
	public void onEvent(AssessmentStartedEvent event) {
        testRosterId = event.getTestRosterId();
        pGrade = event.getGrade();
    }

    public void onEvent(SubtestStartedEvent event) {
//        pNormGroup = event.getNormGroup();
        pTestLevel = event.getItemSetLevel();
        pTestForm = event.getItemSetForm();
    }
    
    public void onEvent(ObjectiveRawScoreEvent event) {

    	if(Objective.SECONDARY.equals(event.getReportingLevel())){
    		
	        if(!secondaryObjectivePointObtained.containsKey(event.getObjectiveId())) {
	        	secondaryObjectivePointObtained.put(event.getObjectiveId(), new ObjectiveRawScoreEvent(event.getTestRosterId(), event.getObjectiveId(),
	                    event.getReportingLevel(), event.getPointsPossible(), event.getPointsObtained(), event.getPointsAttempted(),
	                    ScorerHelper.calculatePercentage(
	                    		event.getPointsObtained(),
	                    		event.getPointsPossible()), event.getSubtestId()));
	        } else {
	        	ObjectiveRawScoreEvent  objEvent = (ObjectiveRawScoreEvent) secondaryObjectivePointObtained.get(event.getObjectiveId());
	        	if(!objEvent.getSubtestId().equals(event.getSubtestId())){
	        		Integer pointObtained = new Integer(objEvent.getPointsObtained());
	        		pointObtained = pointObtained.intValue() + event.getPointsObtained();
	        		secondaryObjectivePointObtained.put(event.getObjectiveId(), new ObjectiveRawScoreEvent(event.getTestRosterId(), event.getObjectiveId(),
	                        event.getReportingLevel(), event.getPointsPossible(), pointObtained.intValue(), event.getPointsAttempted(),
	                        ScorerHelper.calculatePercentage(
	                        		pointObtained.intValue(),
	                        		event.getPointsPossible()), event.getSubtestId()));
	        	}
	        }
        
    	
	    	String objectiveName = getObjectiveName(event.getObjectiveId());
	    	ObjectiveRawScoreEvent  objEvent = (ObjectiveRawScoreEvent) secondaryObjectivePointObtained.get(event.getObjectiveId());
	    	Integer pointObtained = new Integer(objEvent.getPointsObtained()); 
	    	
        	final BigDecimal scaleScore = (objectiveName == null )? null :getScoreForTASC(
        			event.getObjectiveId(),
        			objectiveName,
    				null,
    				pTestForm,
    				pTestLevel,
    				pGrade,
    				ScoreLookupCode.SUBTEST_NUMBER_CORRECT,
    				new BigDecimal(pointObtained.intValue()),
    				ScoreLookupCode.SCALED_SCORE,
    				null );
	        
        	System.out.println( "Objective Name :: "+objectiveName+" || Raw Score :: "+new BigDecimal(pointObtained.intValue())+" || Scale Score :: "+scaleScore );
	        final BigDecimal masteryLevelValue = (scaleScore==null) ? null : getTASCObjectiveMasteryLevel(
	        		TASC_FRAMEWORK_CODE,
	        		objectiveName,
	        		null, 
	        		pTestForm,
	        	    pTestLevel,
	        	    pGrade,
	        	    scaleScore,
	        	    null);
	        
	        final String scaleScoreRangeForMastery = (objectiveName == null)? null : getTASCScaleScoreRangeForCutScore(
	        		TASC_FRAMEWORK_CODE,
	        		objectiveName,
	        		pTestLevel,
	        		null,
	        		pGrade,
	        		pTestForm);
	        
	        
	        channel.send(new SecondaryObjectiveDerivedScoreEvent(
	                event.getTestRosterId(),
	        		event.getSubtestId(), 
					event.getObjectiveId(), 
					objectiveName, 
					scaleScore,
					masteryLevelValue,
					scaleScoreRangeForMastery));
    	}
    }
    
    private String getObjectiveName(Long objectiveId) {
        for(int i=0;i<scorer.getResultHolder().getCurriculumData().getSecondaryObjectives().length;i++) {
        	SecondaryObjective sec = scorer.getResultHolder().getCurriculumData().getSecondaryObjectives()[i];
                if(objectiveId.equals(sec.getSecondaryObjectiveId())) {
                    return sec.getSecondaryObjectiveName();
                }
            }
        return null;
    }
}
