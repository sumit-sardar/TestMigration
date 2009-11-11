/*
 * Created on May 4, 2005
 *
 * @author ncohen
 *
 */
package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

public class ScoreTypeRawScoreEvent extends ScoreTypeEvent {
    private final int pointsObtained;
    private final Integer itemSetId;

    public ScoreTypeRawScoreEvent(final Long testRosterId, final ScoreLookupCode scoreType,
            final Integer itemSetId, final int pointsObtained) {
        super(testRosterId, scoreType);
        this.itemSetId = itemSetId;
        this.pointsObtained = pointsObtained;
    }

    public Integer getItemSetId() {
        return itemSetId;
    }

    public int getPointsObtained() {
        return pointsObtained;
    }
}