package com.ctb.lexington.db.irsdata.irstvdata;

public class ItemScore {
	
	private String itemId;
	private double nationalAverage;
	private Long pointsObtained;
	private Long pointsPossible;
	
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public double getNationalAverage() {
		return nationalAverage;
	}
	public void setNationalAverage(double nationalAverage) {
		this.nationalAverage = nationalAverage;
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

}
