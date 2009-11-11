package com.ctb.lexington.domain.score.event;

import java.math.BigDecimal;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class PrimaryObjectiveDerivedScoreEvent extends ObjectiveEvent {
    private final BigDecimal pValue;

    public PrimaryObjectiveDerivedScoreEvent(final Long testRosterId, final Long objectiveId,
            final BigDecimal pValue, final Long subtestId) {
        super(testRosterId, objectiveId, subtestId);
        this.pValue = pValue;
    }

    public BigDecimal getPValue() {
        return pValue;
    }
}