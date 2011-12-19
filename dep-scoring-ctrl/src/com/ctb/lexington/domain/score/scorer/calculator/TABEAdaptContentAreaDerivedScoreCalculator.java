package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.util.Map;

import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.SubtestScoreReceivedEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.SafeHashMap;

public class TABEAdaptContentAreaDerivedScoreCalculator extends AbstractDerivedScoreCalculator {
	
	protected Integer objectiveId;
	protected Double objectiveRawScore;
	protected Double totalObjectiveRawScore;
	protected Double objectiveScaleScore;
	protected Double objectiveSSsem; // Standard Error of Measurement
	protected Integer objectiveMasteryLevel;
	protected Map adaptiveContentDetails  = new SafeHashMap(String.class, ContentAreaNumberCorrectEvent.class);
	
	 public TABEAdaptContentAreaDerivedScoreCalculator(Channel channel, Scorer scorer) {
	        super(channel, scorer);
	        channel.subscribe(this, SubtestScoreReceivedEvent.class); // changes for TABE Adaptive
	        mustPrecede(SubtestStartedEvent.class, SubtestScoreReceivedEvent.class);
	        channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
	        mustPrecede(SubtestStartedEvent.class, ContentAreaNumberCorrectEvent.class);
	    }
	 
	//This method is added for TABE Adaptive
	    public void onEvent(SubtestScoreReceivedEvent event) {
	    	
	    	 this.objectiveId = event.getObjectiveId();
	    	 this.objectiveRawScore = event.getObjectiveRawScore();
	    	 this.totalObjectiveRawScore = event.getTotalObjectiveRawScore();
	    	 this.objectiveScaleScore = event.getObjectiveScore();
	    	 this.objectiveSSsem = event.getObjectiveSSsem();
	    	 this.objectiveMasteryLevel = event.getObjectiveMasteryLevel();
	    	// System.out.println("objectiveId ->" + this.objectiveId);
	    	
	    }
	    
	    
	    public void onEvent(ContentAreaNumberCorrectEvent event) {
	    	if (!adaptiveContentDetails.containsKey(event.getContentAreaName())) {
	    		adaptiveContentDetails.put(event.getContentAreaName(), event);
		        final Integer subtestId = DatabaseHelper.asInteger(event.getSubtestId());
		        final BigDecimal scaleScore = abilityScore != null ? new BigDecimal(Math.floor(abilityScore)) : null;
		     //   System.out.println("-------event.getContentAreaName() ->" + event.getContentAreaName());
		     //   System.out.println("-------scaleScore ->" + scaleScore);
		        
		        final BigDecimal standardErrorMeasurement = abilityScore != null ? new BigDecimal(semScore) : null;
		        
		        final String gradeEquivalent = getGradeEquivalent(
		        		subtestId,
		        		event.getContentAreaName(),
						null,
						null,
						null,
						null,
						ScoreLookupCode.SCALED_SCORE,
						scaleScore,
						ScoreLookupCode.GRADE_EQUIVALENT,
						null);
	
		        channel.send(new ContentAreaDerivedScoreEvent(
		                event.getTestRosterId(),
		        		event.getSubtestId(), 
						event.getContentAreaId(), 
						event.getContentAreaName(), 
						scaleScore, 
						standardErrorMeasurement,
						null,
		                gradeEquivalent,
						null,
						null, 
						null, 
						null,
						null,
						null,
						pNormGroup,
		                pNormYear,
						pAgeCategory,
		                pTestLevel,
		                pRecommendedLevel));
		    }
	    }

}
