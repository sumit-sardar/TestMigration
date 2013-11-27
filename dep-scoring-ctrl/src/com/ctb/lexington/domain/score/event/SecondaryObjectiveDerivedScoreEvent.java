package com.ctb.lexington.domain.score.event;

import java.math.BigDecimal;

import com.ctb.lexington.domain.teststructure.PerformanceLevel;

/**
 * @author TCS
 * @version $Id$
 */
public class SecondaryObjectiveDerivedScoreEvent extends ObjectiveEvent {
    private final String objectiveName;
    private final BigDecimal scaleScore;
	private final BigDecimal masteryLevel;
	private final String scaleScoreRangeForMastery;
    
    public SecondaryObjectiveDerivedScoreEvent(final Long testRosterId, final Long subtestId, final Long objectiveId,
    		final String objectiveName, final BigDecimal scaleScore, final BigDecimal masteryLevel, final String scaleScoreRangeForMastery)
    {
    	super(testRosterId, objectiveId, subtestId);
    	
    	this.scaleScore = scaleScore;
    	this.objectiveName = objectiveName;
    	this.masteryLevel = masteryLevel;
    	this.scaleScoreRangeForMastery = scaleScoreRangeForMastery;
    	
    }

	public String getObjectiveName() {
		return objectiveName;
	}


	public BigDecimal getScaleScore() {
		return scaleScore;
	}


	public BigDecimal getMasteryLevel() {
		return masteryLevel;
	}
	
	public String getScaleScoreRangeForMastery(){
		return scaleScoreRangeForMastery;
	}
}
