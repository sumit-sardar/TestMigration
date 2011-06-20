package com.ctb.lexington.domain.score.event;

import java.math.BigDecimal;

import com.ctb.lexington.domain.score.event.common.Event;

public class SubtestContentAreaCompositeScoreEvent extends Event {
    private final BigDecimal scaleScore;
    private final String type;
    private final BigDecimal normalCurveEquivalent;
    private final String gradeEquivalent;
    private final BigDecimal nationalStanine;
    private final BigDecimal nationalPercentile;
    private final BigDecimal expectedMathGed;
    private final BigDecimal expectedReadingGed;
    private final BigDecimal expectedWritingGed;
    private final BigDecimal expectedSocialStudiesGed;
    private final BigDecimal expectedScienceGed;
    private final BigDecimal expectedAverageGed;
    private final String normGroup;
    private final String normYear;
    private final Long pointsObtained;
    private final Long pointsAttempted;
    private final Long percentObtained;
    private final Long pointsPossible;
    private final String validScore;
    private final BigDecimal proficencyLevel; // For Laslink Scoring

    public SubtestContentAreaCompositeScoreEvent(final Long testRosterId, final String type,
            final BigDecimal scaleScore, final BigDecimal normalCurveEquivalent,
            final String gradeEquivalent, final BigDecimal nationalStanine,
            final BigDecimal nationalPercentile, final String normGroup, final String normYear,
            final BigDecimal expectedMathGed, final BigDecimal expectedReadingGed,
            final BigDecimal expectedWritingGed, final BigDecimal expectedSocialStudiesGed,
            final BigDecimal expectedScienceGed, final BigDecimal expectedAverageGed,
            final Long pointsObtained, final Long pointsAttempted, final Long pointsPossible,
            final Long percentObtained, final String validScore, final BigDecimal proficencyLevel) {
        super(testRosterId);
        this.scaleScore = scaleScore;
        this.type = type;
        this.normalCurveEquivalent = normalCurveEquivalent;
        this.gradeEquivalent = gradeEquivalent;
        this.nationalStanine = nationalStanine;
        this.nationalPercentile = nationalPercentile;
        this.normGroup = normGroup;
        this.normYear = normYear;
        this.expectedMathGed = expectedMathGed;
        this.expectedReadingGed = expectedReadingGed;
        this.expectedWritingGed = expectedWritingGed;
        this.expectedSocialStudiesGed = expectedSocialStudiesGed;
        this.expectedScienceGed = expectedScienceGed;
        this.expectedAverageGed = expectedAverageGed;
        this.pointsObtained = pointsObtained;
        this.pointsAttempted = pointsAttempted;
        this.pointsPossible = pointsPossible;
        this.percentObtained = percentObtained;
        this.validScore = validScore;
        this.proficencyLevel = proficencyLevel;  // For Laslink Scoring
    }

    public BigDecimal getScaleScore() {
        return scaleScore;
    }

    public String getType() {
        return type;
    }

    public String getGradeEquivalent() {
        return gradeEquivalent;
    }

    public BigDecimal getNationalPercentile() {
        return nationalPercentile;
    }

    public BigDecimal getNationalStanine() {
        return nationalStanine;
    }

    public BigDecimal getNormalCurveEquivalent() {
        return normalCurveEquivalent;
    }

    public BigDecimal getExpectedMathGed() {
        return expectedMathGed;
    }

    public BigDecimal getExpectedReadingGed() {
        return expectedReadingGed;
    }

    public BigDecimal getExpectedWritingGed() {
        return expectedWritingGed;
    }

    public BigDecimal getExpectedSocialStudiesGed() {
        return expectedSocialStudiesGed;
    }

    public BigDecimal getExpectedScienceGed() {
        return expectedScienceGed;
    }

    public BigDecimal getExpectedAverageGed() {
        return expectedAverageGed;
    }

    /**
     * @return Returns the normGroup.
     */
    public String getNormGroup() {
        return normGroup;
    }

    /**
     * @return Returns the normYear.
     */
    public String getNormYear() {
        return normYear;
    }

    public Long getPointsObtained() {
        return pointsObtained;
    }

    public Long getPointsAttempted() {
        return pointsAttempted;
    }

    public Long getPercentObtained() {
        return percentObtained;
    }

    public Long getPointsPossible() {
        return pointsPossible;
    }

    public String getValidScore() {
        return validScore;
    }

    public boolean hasExpectedGedScores() {
        return (expectedAverageGed != null || expectedMathGed != null || expectedReadingGed != null
                || expectedScienceGed != null || expectedSocialStudiesGed != null || expectedWritingGed != null);
    }

	/**
	 * @return the proficencyLevel
	 */
	public BigDecimal getProficencyLevel() {
		return proficencyLevel;
	}
}