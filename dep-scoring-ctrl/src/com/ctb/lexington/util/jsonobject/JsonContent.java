package com.ctb.lexington.util.jsonobject;

import java.io.Serializable;
import java.util.List;

public class JsonContent implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<List<ScoreData>> scoring;
	private String itemId;
	private Integer totalScoreObtained;
	private Integer totalMaxScore;
	
	public JsonContent(){
		
	}

	

	public List<List<ScoreData>> getScoring() {
		return scoring;
	}



	public void setScoring(List<List<ScoreData>> scoring) {
		this.scoring = scoring;
	}



	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Integer getTotalScoreObtained() {
		return totalScoreObtained;
	}

	public void setTotalScoreObtained(Integer totalScoreObtained) {
		this.totalScoreObtained = totalScoreObtained;
	}

	public Integer getTotalMaxScore() {
		return totalMaxScore;
	}

	public void setTotalMaxScore(Integer totalMaxScore) {
		this.totalMaxScore = totalMaxScore;
	}

	
	
}
