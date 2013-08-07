package com.ctb.dto;

public class Scores {

	private String itemSetId;
	private String contentAreaId;
	private String scaleScore;
	private String objecttiveScore;
	private String level;
	private String itemSetName;
	private String rawScore;
	
	
	public String getItemSetName() {
		return itemSetName;
	}
	public void setItemSetName(String itemSetName) {
		this.itemSetName = itemSetName;
	}
	public String getContentAreaId() {
		return contentAreaId;
	}
	public void setContentAreaId(String contentAreaId) {
		this.contentAreaId = contentAreaId;
	}
	public String getRawScore() {
		return rawScore;
	}
	public void setRawScore(String rawScore) {
		this.rawScore = rawScore;
	}
	public String getItemSetId() {
		return itemSetId;
	}
	public void setItemSetId(String itemSetId) {
		this.itemSetId = itemSetId;
	}
	public String getScaleScore() {
		return scaleScore;
	}
	public void setScaleScore(String scaleScore) {
		this.scaleScore = scaleScore;
	}
	public String getObjecttiveScore() {
		return objecttiveScore;
	}
	public void setObjecttiveScore(String objecttiveScore) {
		this.objecttiveScore = objecttiveScore;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
}