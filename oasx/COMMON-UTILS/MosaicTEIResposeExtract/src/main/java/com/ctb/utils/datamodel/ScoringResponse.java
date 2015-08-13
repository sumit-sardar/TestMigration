package com.ctb.utils.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScoringResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<ScoreData> scoring;
	private float totalScoreObtained;
	private float totalMaxScore;
	
	public ScoringResponse() {
		
	}

	public List<ScoreData> getScoring() {
		return scoring;
	}

	public void setScoring(List<ScoreData> scoring) {
		this.scoring = scoring;
	}

	public float getTotalScoreObtained() {
		return totalScoreObtained;
	}

	public void setTotalScoreObtained(float totalScoreObtained) {
		this.totalScoreObtained = totalScoreObtained;
	}

	public float getTotalMaxScore() {
		return totalMaxScore;
	}

	public void setTotalMaxScore(float totalMaxScore) {
		this.totalMaxScore = totalMaxScore;
	}
	
	public void addScore(ScoreData score) {
		if(this.scoring == null)
			this.scoring = new ArrayList<ScoreData>();
		this.scoring.add(score);
	}

	@Override
	public String toString() {
		return "ScoringResponse [scoring=" + scoring + ", totalScoreObtained="
				+ totalScoreObtained + ", totalMaxScore=" + totalMaxScore + "]";
	}
}
