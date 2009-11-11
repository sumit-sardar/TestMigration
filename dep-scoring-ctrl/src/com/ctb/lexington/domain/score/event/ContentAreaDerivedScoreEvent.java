package com.ctb.lexington.domain.score.event;

import java.math.BigDecimal;

import com.ctb.lexington.domain.teststructure.PerformanceLevel;

public class ContentAreaDerivedScoreEvent extends ObjectiveEvent {
    private final Long subtestId;
    private final String contentAreaName;
    private final BigDecimal scaleScore;
    private final BigDecimal standardErrorMeasurement;
    private final BigDecimal normalCurveEquivalent;
    private final String gradeEquivalent;
    private final BigDecimal gradeMeanEquivalent;
    private final BigDecimal nationalStanine;
    private final BigDecimal nationalPercentile;
    private final PerformanceLevel performanceLevel;
    private final BigDecimal highNationalPercentile;
    private final BigDecimal lowNationalPercentile;
    private final String normGroup;
    private final String normYear;
    private final String ageCategory;
    private final String testLevel;
    private final String recommendedLevel;

    public ContentAreaDerivedScoreEvent(final Long testRosterId, final Long subtestId,
            final Long contentAreaId, final String contentAreaName, final BigDecimal scaleScore,
            final BigDecimal standardErrorMeasurement, final BigDecimal normalCurveEquivalent,
            final String gradeEquivalent, final BigDecimal gradeMeanEquivalent,
            final BigDecimal nationalStanine, final BigDecimal nationalPercentile,
            final BigDecimal highNationalPercentile, final BigDecimal lowNationalPercentile,
            final PerformanceLevel performanceLevel, final String normGroup, final String normYear,
            final String ageCategory, final String testLevel, final String recommendedLevel) {
        super(testRosterId, contentAreaId, subtestId);
        this.subtestId = subtestId;
        this.contentAreaName = contentAreaName;
        this.scaleScore = scaleScore;
        this.standardErrorMeasurement = standardErrorMeasurement;
        this.normalCurveEquivalent = normalCurveEquivalent;
        this.gradeEquivalent = gradeEquivalent;
        this.gradeMeanEquivalent = gradeMeanEquivalent;
        this.nationalStanine = nationalStanine;
        this.nationalPercentile = nationalPercentile;
        this.highNationalPercentile = highNationalPercentile;
        this.lowNationalPercentile = lowNationalPercentile;
        this.performanceLevel = performanceLevel;
        this.normGroup = normGroup;
        this.normYear = normYear;
        this.ageCategory = ageCategory;
        this.testLevel = testLevel;
        this.recommendedLevel = recommendedLevel;
    }

    /**
	 * @return Returns the recommendedLevel.
	 */
	public String getRecommendedLevel() {
		return recommendedLevel;
	}

	public Long getSubtestId() {
        return subtestId;
    }

    public Long getContentAreaId() {
        return getObjectiveId();
    }

    public String getContentAreaName() {
        return contentAreaName;
    }

    public BigDecimal getScaleScore() {
        return scaleScore;
    }

    public String getGradeEquivalent() {
        return gradeEquivalent;
    }

    public BigDecimal getNationalPercentile() {
        return nationalPercentile;
    }
    
    public BigDecimal getHighNationalPercentile() {
        return highNationalPercentile;
    }
    
    public BigDecimal getLowNationalPercentile() {
        return lowNationalPercentile;
    }

    public BigDecimal getNationalStanine() {
        return nationalStanine;
    }

    public BigDecimal getNormalCurveEquivalent() {
        return normalCurveEquivalent;
    }

    public String getNormGroup() {
        return normGroup;
    }

    public String getNormYear() {
        return normYear;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public PerformanceLevel getPerformanceLevel() {
        return performanceLevel;
    }

    /**
     * @return Returns the gradeMeanEquivalent.
     */
    public BigDecimal getGradeMeanEquivalent() {
        return gradeMeanEquivalent;
    }

    /**
     * @return Returns the standardErrorMeasurement.
     */
    public BigDecimal getStandardErrorMeasurement() {
        return standardErrorMeasurement;
    }

    public String getTestLevel() {
        return testLevel;
    }
}