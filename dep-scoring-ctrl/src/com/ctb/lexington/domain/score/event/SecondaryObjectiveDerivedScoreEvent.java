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
	private final PerformanceLevel performanceLevel;

    
    public SecondaryObjectiveDerivedScoreEvent(final Long testRosterId, final Long subtestId, final Long objectiveId,
    		final String objectiveName, final BigDecimal scaleScore, final PerformanceLevel performanceLevel)
    {
    	super(testRosterId, objectiveId, subtestId);
    	
    	this.scaleScore = scaleScore;
    	this.objectiveName = objectiveName;
    	this.performanceLevel = performanceLevel;
    	
    }

	public String getObjectiveName() {
		return objectiveName;
	}


	public BigDecimal getScaleScore() {
		return scaleScore;
	}


	public PerformanceLevel getPerformanceLevel() {
		return performanceLevel;
	}
}
