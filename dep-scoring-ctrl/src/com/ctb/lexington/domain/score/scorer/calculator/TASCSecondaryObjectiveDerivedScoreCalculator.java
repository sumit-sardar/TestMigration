package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Map;

import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.Objective;
import com.ctb.lexington.domain.score.event.ObjectiveRawScoreEvent;
import com.ctb.lexington.domain.score.event.SecondaryObjectiveDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.SafeHashMap;

public class TASCSecondaryObjectiveDerivedScoreCalculator extends
		AbstractDerivedScoreCalculator {

	private final Map secondaryObjectiveRawScoreEventsList = new SafeHashMap(String.class, ObjectiveRawScoreEvent.class);
	private static Map secondaryObjectivePointObtained = new SafeHashMap(Long.class, Integer.class);
	public TASCSecondaryObjectiveDerivedScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, AssessmentStartedEvent.class);
        channel.subscribe(this, SubtestStartedEvent.class);
        channel.subscribe(this, ObjectiveRawScoreEvent.class);
//        mustPrecede(SubtestEndedEvent.class, ObjectiveRawScoreEvent.class);
        //channel.subscribe(this, ObjectivePrimaryCumulativeNumberCorrectEvent.class);
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
    
    /*public void onEvent(ObjectiveRawScoreEvent event) {
    	secondaryObjectiveRawScoreEventsList.put(event.getObjectiveId().toString(), event);
    }*/
    
    public void onEvent(ObjectiveRawScoreEvent event) {

        if(!secondaryObjectivePointObtained.containsKey(event.getObjectiveId())) {
        	secondaryObjectivePointObtained.put(event.getObjectiveId(), new Integer(event.getPointsObtained()));
        } else {
            Integer pointObtained = (Integer)secondaryObjectivePointObtained.get(event.getObjectiveId());
            pointObtained = pointObtained.intValue() + event.getPointsObtained();
            secondaryObjectivePointObtained.put(event.getObjectiveId(), pointObtained);
        }
        
    	if(Objective.SECONDARY.equals(event.getReportingLevel())){
	    	String objectiveName = getObjectiveName(event.getObjectiveId());
	    	Integer pointObtained = (Integer)secondaryObjectivePointObtained.get(event.getObjectiveId());
	    	
        	final BigDecimal scaleScore = getObjectiveScore(
        			event.getObjectiveId(),
        			objectiveName,
    				null,
    				pTestForm,
    				pTestLevel,
    				(pGrade != null)?pGrade:null,
    				ScoreLookupCode.SUBTEST_NUMBER_CORRECT,
    				//getSecondaryObjecrivePointsObtained(event.getObjectiveId()), // If it is wrong then we can use [new BigDecimal(event.getNumberCorrect())],
    				new BigDecimal(pointObtained.intValue()),
    				ScoreLookupCode.SCALED_SCORE,
    				pAgeCategory );
	        
	        
	        channel.send(new SecondaryObjectiveDerivedScoreEvent(
	                event.getTestRosterId(),
	        		event.getSubtestId(), 
					event.getObjectiveId(), 
					objectiveName, 
					scaleScore,
					null));
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
    
    /*private BigDecimal getSecondaryObjecrivePointsObtained(Long objectiveId) {
    	ObjectiveRawScoreEvent objRaw = (ObjectiveRawScoreEvent) secondaryObjectiveRawScoreEventsList.get(objectiveId.toString());
    	return new BigDecimal(objRaw.getPointsObtained());
    }*/
}
