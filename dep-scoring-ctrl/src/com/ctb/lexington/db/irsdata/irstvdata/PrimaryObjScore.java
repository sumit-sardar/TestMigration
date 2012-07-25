package com.ctb.lexington.db.irsdata.irstvdata;

public class PrimaryObjScore {
	
	private String primaryObjectiveId;
	private Long pointsAttempted;
	private Long pointsObtained;
	private Long pointsPossible;
	private Float percentObtained;
	private int masteryLevel;
	private Float nationalAverage;
	private Long lowModMasteryRange;
	private Long highModMasteryRange;
	private String modMasteryRange;
	private SecondaryObjScore[] secondaryObjectiveFact;
	
	
	public String getPrimaryObjectiveId() {
		return primaryObjectiveId;
	}
	public void setPrimaryObjectiveId(String primaryObjectiveId) {
		this.primaryObjectiveId = primaryObjectiveId;
	}
	public Long getPointsAttempted() {
		return pointsAttempted;
	}
	public void setPointsAttempted(Long pointsAttempted) {
		this.pointsAttempted = pointsAttempted;
	}
	public Long getPointsObtained() {
		return pointsObtained;
	}
	public void setPointsObtained(Long pointsObtained) {
		this.pointsObtained = pointsObtained;
	}
	public Long getPointsPossible() {
		return pointsPossible;
	}
	public void setPointsPossible(Long pointsPossible) {
		this.pointsPossible = pointsPossible;
	}
	public Float getPercentObtained() {
		return percentObtained;
	}
	public void setPercentObtained(Float percentObtained) {
		this.percentObtained = percentObtained;
	}
	public int getMasteryLevel() {
		return masteryLevel;
	}
	public void setMasteryLevel(int masteryLevel) {
		this.masteryLevel = masteryLevel;
	}
	public Float getNationalAverage() {
		return nationalAverage;
	}
	public void setNationalAverage(Float nationalAverage) {
		this.nationalAverage = nationalAverage;
	}
	public SecondaryObjScore[] getSecondaryObjectiveFact() {
		return secondaryObjectiveFact;
	}
	public void setSecondaryObjectiveFact(
			SecondaryObjScore[] secondaryObjectiveFact) {
		this.secondaryObjectiveFact = secondaryObjectiveFact;
	}
	public Long getLowModMasteryRange() {
		return lowModMasteryRange;
	}
	public void setLowModMasteryRange(Long lowModMasteryRange) {
		this.lowModMasteryRange = lowModMasteryRange;
	}
	public Long getHighModMasteryRange() {
		return highModMasteryRange;
	}
	public void setHighModMasteryRange(Long highModMasteryRange) {
		this.highModMasteryRange = highModMasteryRange;
	}
	public String getModMasteryRange() {
		return modMasteryRange;
	}
	public void setModMasteryRange(String modMasteryRange) {
		this.modMasteryRange = modMasteryRange;
	}

}
