package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.score.event.common.Event;

public class ContentAreaRawScoreEvent extends Event {
    private final int pointsObtained;
    private final int pointsAttempted;
    private final int percentObtained;
    private final int pointsPossible;
    private final String contentAreaName;

    public ContentAreaRawScoreEvent(final Long testRosterId, final String contentAreaName, final int pointsObtained,
            final int pointsAttempted, final int percentObtained, final int pointsPossible) {
        super(testRosterId);
        this.pointsObtained = pointsObtained;
        this.pointsAttempted = pointsAttempted;
        this.contentAreaName = contentAreaName;
        this.percentObtained = percentObtained;
        this.pointsPossible = pointsPossible;
    }

    /**
     * @return Returns the pointsAttempted.
     */
    public int getPointsAttempted() {
        return pointsAttempted;
    }

    /**
     * @return Returns the pointsObtained.
     */
    public int getPointsObtained() {
        return pointsObtained;
    }

    /**
     * @return Returns the reportingLevel.
     */
    public String getContentAreaName() {
        return contentAreaName;
    }

    public int getPercentObtained() {
        return percentObtained;
    }

    public int getPointsPossible() {
        return pointsPossible;
    }
}