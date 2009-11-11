package com.ctb.lexington.domain.score.event;

public class PointEvent extends ResponseEvent {
    private final Integer pointsAttempted;
    private final Integer pointsObtained;

    public PointEvent(final Long testRosterId, final String itemId, final Integer itemSetId,
            final Integer pointsAttempted, final Integer pointsObtained) {
        super(testRosterId, itemId, itemSetId);
        this.pointsAttempted = pointsAttempted;
        this.pointsObtained = pointsObtained;
    }

    public Integer getPointsAttempted() {
        return pointsAttempted;
    }

    public Integer getPointsObtained() {
        return pointsObtained;
    }
}