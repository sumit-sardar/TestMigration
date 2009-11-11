package com.ctb.lexington.domain.score.event;

public class SubtestRawScoreEvent extends SubtestEvent {
    private final int pointsPossible;
    private final int pointsAttempted;
    private final int pointsObtained;
    private final int percentObtained;

    public SubtestRawScoreEvent(final SubtestEvent event, final int pointsPossible,
            final int pointsAttempted, final int pointsObtained, final int percentObtained) {
        super(event.getTestRosterId(), event.getItemSetId());
        this.pointsAttempted = pointsAttempted;
        this.pointsObtained = pointsObtained;
        this.pointsPossible = pointsPossible;
        this.percentObtained = percentObtained;
    }

    public int getPointsPossible() {
        return pointsPossible;
    }

    public int getPointsAttempted() {
        return pointsAttempted;
    }

    public int getPointsObtained() {
        return pointsObtained;
    }

    public int getPercentObtained() {
        return percentObtained;
    }
}