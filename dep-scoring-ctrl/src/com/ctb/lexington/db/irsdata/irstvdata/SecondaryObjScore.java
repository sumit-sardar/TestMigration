package com.ctb.lexington.db.irsdata.irstvdata;

public class SecondaryObjScore {
	
	private String secondaryObjId;
	private Long pointsPossible;
	private Long pointsObtained;
	private Long pointsAttempted;
	private Float percentObtained;
	private int masteryLevel;
	private Long secId;
	private ItemScore[] itemScores;
	
	
	public String getSecondaryObjId() {
		return secondaryObjId;
	}
	public void setSecondaryObjId(String secondaryObjId) {
		this.secondaryObjId = secondaryObjId;
	}
	public Long getPointsPossible() {
		return pointsPossible;
	}
	public void setPointsPossible(Long pointsPossible) {
		this.pointsPossible = pointsPossible;
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
	public Long getSecId() {
		return secId;
	}
	public void setSecId(Long secId) {
		this.secId = secId;
	}
	public ItemScore[] getItemScores() {
		return itemScores;
	}
	public void setItemScores(ItemScore[] itemScores) {
		this.itemScores = itemScores;
	}

}
