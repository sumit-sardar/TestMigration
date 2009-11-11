/*
 * Created on Aug 3, 2004
 */
package com.ctb.lexington.domain.score.event;

import java.math.BigDecimal;

public class SubtestDerivedScoreEvent extends SubtestEvent {
    private final BigDecimal score;
    private final String scoreTypeCode;

    public SubtestDerivedScoreEvent(final Long testRosterId, final Integer itemSetId,
            final String scoreTypeCode, final BigDecimal score) {
        super(testRosterId, itemSetId);
        this.scoreTypeCode = scoreTypeCode;
        this.score = score;
    }

    // REDTAG: Rename this method to getScore()
    public BigDecimal getScaleScore() {
        return score;
    }

    public String getScoreTypeCode() {
        return scoreTypeCode;
    }
}