package com.ctb.lexington.domain.score.event;

public class ObjectiveRawScoreEvent extends ObjectiveEvent {
	private final int pointsPossible;
    private final int pointsObtained;
    private final int pointsAttempted;
    private final int percentObtained;
    private final String reportingLevel;

    public ObjectiveRawScoreEvent(final Long testRosterId, final Long objectiveId,
            final String reportingLevel, final int pointsPossible, final int pointsObtained, final int pointsAttempted,
            final int percentObtained, final Long subtestId) {
        super(testRosterId, objectiveId, subtestId);
        this.pointsPossible = pointsPossible;
        this.pointsObtained = pointsObtained;
        this.pointsAttempted = pointsAttempted;
        this.reportingLevel = reportingLevel;
        this.percentObtained = percentObtained;
    }

    /**
     * @return Returns the pointsPossible.
     */
    public int getPointsPossible() {
        return pointsPossible;
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
    public String getReportingLevel() {
        return reportingLevel;
    }

    public int getPercentObtained() {
        return percentObtained;
    }
}