package com.ctb.lexington.domain.score.event;

import java.math.BigDecimal;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class PrimaryObjectiveDerivedScoreEvent extends ObjectiveEvent {
    private final BigDecimal pValue;
    private final Integer highMasteryRange;
    private final Integer lowMasteryRange;

    public PrimaryObjectiveDerivedScoreEvent(final Long testRosterId, final Long objectiveId,
            final BigDecimal pValue, final Long subtestId, final Integer highMasteryRange, 
            final Integer lowMasteryRange) {
        super(testRosterId, objectiveId, subtestId);
        this.pValue = pValue;
        this.highMasteryRange = highMasteryRange;
        this.lowMasteryRange = lowMasteryRange;
    }
    

    public BigDecimal getPValue() {
        return pValue;
    }

	public Integer getHighMasteryRange() {
		return highMasteryRange;
	}

	public Integer getLowMasteryRange() {
		return lowMasteryRange;
	}
}