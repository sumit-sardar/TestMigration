package com.ctb.utils.datamodel;

import java.io.Serializable;
import java.util.List;


public class ScoreData implements Serializable{

	private static final long serialVersionUID = 1L;
	private String interactionType;
	private Integer subquestionIndex;
	private String scoringMethod;
	private float scoreObtained;	
	private float maxScore;
	private String screenName;
	private List<CorrectAnswer> correctAnswers;
	private WeightedScore weightedScoring;
	private List<PossibleOption> possibleOptions;
	private List<Answer> answered;
	
	public ScoreData() {
		
	}

	public String getInteractionType() {
		return interactionType;
	}

	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
	}

	public String getScoringMethod() {
		return scoringMethod;
	}

	public void setScoringMethod(String scoringMethod) {
		this.scoringMethod = scoringMethod;
	}

	public float getScoreObtained() {
		return scoreObtained;
	}

	public void setScoreObtained(float scoreObtained) {
		this.scoreObtained = scoreObtained;
	}


	public float getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(float maxScore) {
		this.maxScore = maxScore;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	
	public List<CorrectAnswer> getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(List<CorrectAnswer> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	public WeightedScore getWeightedScoring() {
		return weightedScoring;
	}

	public void setWeightedScoring(WeightedScore weightedScoring) {
		this.weightedScoring = weightedScoring;
	}
	
	
	
		
	@Override
	public String toString() {
		return "ScoreData [interactionType=" + interactionType
				+ ", scoringMethod=" + scoringMethod + ", scoreObtained="
				+ scoreObtained + ", answeredText=" 
				+ ", maxScore=" + maxScore + ", screenName=" + screenName
				+ ", correctAnswers=" + correctAnswers + ", weightedScoring="
				+ weightedScoring + "]";
	}

	public Integer getSubquestionIndex() {
		return subquestionIndex;
	}

	public void setSubquestionIndex(Integer subquestionIndex) {
		this.subquestionIndex = subquestionIndex;
	}

	public List<PossibleOption> getPossibleOptions() {
		return possibleOptions;
	}

	public void setPossibleOptions(List<PossibleOption> possibleOptions) {
		this.possibleOptions = possibleOptions;
	}

	public List<Answer> getAnswered() {
		return answered;
	}

	public void setAnswered(List<Answer> answered) {
		this.answered = answered;
	}
	
}
