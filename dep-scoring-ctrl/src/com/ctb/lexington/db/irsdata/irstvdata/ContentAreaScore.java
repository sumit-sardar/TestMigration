package com.ctb.lexington.db.irsdata.irstvdata;

public class ContentAreaScore {
	
	private Long contentAreaId;
	private String contentAreaName;
	private Long scaleScore;
	private Long sem;
	private Long minScaleScore;
	private Long maxScaleScore;
	private Float gradeEquivalent;
	private int nationalStanine;
	private int nationalPercentile;
	private Long normCurveEquivalent;
	private Long percentMastery;
	private Long pointsObtained;
	private Long percentObtained;
	private Long pointsPossible;
	private Long pointsAttempted;
	private PrimaryObjScore[] primaryObjScores;
	
	
	public Long getContentAreaId() {
		return contentAreaId;
	}
	public void setContentAreaId(Long contentAreaId) {
		this.contentAreaId = contentAreaId;
	}
	public String getContentAreaName() {
		return contentAreaName;
	}
	public void setContentAreaName(String contentAreaName) {
		this.contentAreaName = contentAreaName;
	}
	public Long getScaleScore() {
		return scaleScore;
	}
	public void setScaleScore(Long scaleScore) {
		this.scaleScore = scaleScore;
	}
	public Long getSem() {
		return sem;
	}
	public void setSem(Long sem) {
		this.sem = sem;
	}
	public Long getMinScaleScore() {
		return minScaleScore;
	}
	public void setMinScaleScore(Long minScaleScore) {
		this.minScaleScore = minScaleScore;
	}
	public Long getMaxScaleScore() {
		return maxScaleScore;
	}
	public void setMaxScaleScore(Long maxScaleScore) {
		this.maxScaleScore = maxScaleScore;
	}
	public Float getGradeEquivalent() {
		return gradeEquivalent;
	}
	public void setGradeEquivalent(Float gradeEquivalent) {
		this.gradeEquivalent = gradeEquivalent;
	}
	public int getNationalStanine() {
		return nationalStanine;
	}
	public void setNationalStanine(int nationalStanine) {
		this.nationalStanine = nationalStanine;
	}
	public int getNationalPercentile() {
		return nationalPercentile;
	}
	public void setNationalPercentile(int nationalPercentile) {
		this.nationalPercentile = nationalPercentile;
	}
	public Long getPointsObtained() {
		return pointsObtained;
	}
	public void setPointsObtained(Long pointsObtained) {
		this.pointsObtained = pointsObtained;
	}
	public Long getPercentObtained() {
		return percentObtained;
	}
	public void setPercentObtained(Long percentObtained) {
		this.percentObtained = percentObtained;
	}
	public Long getPointsPossible() {
		return pointsPossible;
	}
	public void setPointsPossible(Long pointsPossible) {
		this.pointsPossible = pointsPossible;
	}
	public Long getPointsAttempted() {
		return pointsAttempted;
	}
	public void setPointsAttempted(Long pointsAttempted) {
		this.pointsAttempted = pointsAttempted;
	}
	public Long getNormCurveEquivalent() {
		return normCurveEquivalent;
	}
	public void setNormCurveEquivalent(Long normCurveEquivalent) {
		this.normCurveEquivalent = normCurveEquivalent;
	}
	public Long getPercentMastery() {
		return percentMastery;
	}
	public void setPercentMastery(Long percentMastery) {
		this.percentMastery = percentMastery;
	}
	public PrimaryObjScore[] getPrimaryObjScores() {
		return primaryObjScores;
	}
	public void setPrimaryObjScores(PrimaryObjScore[] primaryObjScores) {
		this.primaryObjScores = primaryObjScores;
	}

}
