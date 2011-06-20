package com.ctb.lexington.db.data;

import java.math.BigDecimal;

import com.ctb.lexington.db.record.Persistent;

public class StsTotalStudentScoreDetail implements Persistent, ValidatedScoreRecord {
    private Long adminDimId;
    private Long studentDimId;
    private BigDecimal scaleScore;
    private String gradeEquivalent;
    private BigDecimal nationalStanine;
    private BigDecimal nationalPercentile;
    private Long studentDimVersionId;
    private Long orgNodeDimId;
    private Long orgNodeDimVersionId;
    private Long testDimId;
    private Long numberAttempted;
    private BigDecimal normalCurveEquivalent;
    private Long percentObjectiveMastery;
    private String teacherName;
    private String studentGradeLevel;
    private String normGroup;
    private String normYear;
    private String type;
    private Long averageGedPredictedScore;
    private String recommendedActivity;
    private Long literacyLevelValue;
    private String literacyLevelDescription;
    private Long pointsObtained;
    private Long pointsAttempted;
    private Long pointsPossible;
    private String validScore;
    private Long recommendedLevelId;
    private BigDecimal proficencyLevel;   //Added for LASLINK Scoring

    public Long getRecommendedLevelId() {
        return recommendedLevelId;
    }

    public void setRecommendedLevelId(Long recommendedLevelId) {
        this.recommendedLevelId = recommendedLevelId;
    }

    public Long getAdminDimId() {
        return adminDimId;
    }

    public void setAdminDimId(Long adminDimId) {
        this.adminDimId = adminDimId;
    }

    public Long getStudentDimId() {
        return studentDimId;
    }

    public void setStudentDimId(Long studentDimId) {
        this.studentDimId = studentDimId;
    }

    public BigDecimal getScaleScore() {
        return scaleScore;
    }

    public void setScaleScore(BigDecimal scaleScore) {
        this.scaleScore = scaleScore;
    }

    public String getGradeEquivalent() {
        return gradeEquivalent;
    }

    public void setGradeEquivalent(String gradeEquivalent) {
        this.gradeEquivalent = gradeEquivalent;
    }

    public BigDecimal getNationalStanine() {
        return nationalStanine;
    }

    public void setNationalStanine(BigDecimal nationalStanine) {
        this.nationalStanine = nationalStanine;
    }

    public BigDecimal getNationalPercentile() {
        return nationalPercentile;
    }

    public void setNationalPercentile(BigDecimal nationalPercentile) {
        this.nationalPercentile = nationalPercentile;
    }

    public Long getStudentDimVersionId() {
        return studentDimVersionId;
    }

    public void setStudentDimVersionId(Long studentDimVersionId) {
        this.studentDimVersionId = studentDimVersionId;
    }

    public Long getOrgNodeDimId() {
        return orgNodeDimId;
    }

    public void setOrgNodeDimId(Long orgNodeDimId) {
        this.orgNodeDimId = orgNodeDimId;
    }

    public Long getOrgNodeDimVersionId() {
        return orgNodeDimVersionId;
    }

    public void setOrgNodeDimVersionId(Long orgNodeDimVersionId) {
        this.orgNodeDimVersionId = orgNodeDimVersionId;
    }

    public Long getTestDimId() {
        return testDimId;
    }

    public void setTestDimId(Long testDimId) {
        this.testDimId = testDimId;
    }

    public Long getNumberAttempted() {
        return numberAttempted;
    }

    public void setNumberAttempted(Long numberAttempted) {
        this.numberAttempted = numberAttempted;
    }

    public BigDecimal getNormalCurveEquivalent() {
        return normalCurveEquivalent;
    }

    public void setNormalCurveEquivalent(BigDecimal normalCurveEquivalent) {
        this.normalCurveEquivalent = normalCurveEquivalent;
    }

    public Long getPercentObjectiveMastery() {
        return percentObjectiveMastery;
    }

    public void setPercentObjectiveMastery(Long percentObjectiveMastery) {
        this.percentObjectiveMastery = percentObjectiveMastery;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStudentGradeLevel() {
        return studentGradeLevel;
    }

    public void setStudentGradeLevel(String studentGradeLevel) {
        this.studentGradeLevel = studentGradeLevel;
    }

    public String getNormGroup() {
        return normGroup;
    }

    public void setNormGroup(String normGroup) {
        this.normGroup = normGroup;
    }

    public String getNormYear() {
        return normYear;
    }

    public void setNormYear(String normYear) {
        this.normYear = normYear;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAverageGedPredictedScore() {
        return averageGedPredictedScore;
    }

    public void setAverageGedPredictedScore(Long averageGedPredictedScore) {
        this.averageGedPredictedScore = averageGedPredictedScore;
    }

    public String getRecommendedActivity() {
        return recommendedActivity;
    }

    public void setRecommendedActivity(String recommendedActivity) {
        this.recommendedActivity = recommendedActivity;
    }

    public Long getLiteracyLevelValue() {
        return literacyLevelValue;
    }

    public void setLiteracyLevelValue(Long literacyLevelValue) {
        this.literacyLevelValue = literacyLevelValue;
    }

    public String getLiteracyLevelDescription() {
        return literacyLevelDescription;
    }

    public void setLiteracyLevelDescription(String literacyLevelDescription) {
        this.literacyLevelDescription = literacyLevelDescription;
    }

    public Long getPointsObtained() {
        return pointsObtained;
    }

    public void setPointsObtained(Long pointsObtained) {
        this.pointsObtained = pointsObtained;
    }

    public Long getPointsAttempted() {
        return pointsAttempted;
    }

    public void setPointsAttempted(Long pointsAttempted) {
        this.pointsAttempted = pointsAttempted;
    }

    public String getValidScore() {
        return validScore;
    }

    public void setValidScore(String validScore) {
        this.validScore = validScore;
    }

    public Long getPointsPossible() {
        return pointsPossible;
    }

    public void setPointsPossible(Long pointsPossible) {
        this.pointsPossible = pointsPossible;
    }

	/**
	 * @return the proficencyLevel
	 */
	public BigDecimal getProficencyLevel() {
		return proficencyLevel;
	}

	/**
	 * @param proficencyLevel the proficencyLevel to set
	 */
	public void setProficencyLevel(BigDecimal proficencyLevel) {
		this.proficencyLevel = proficencyLevel;
	}

}