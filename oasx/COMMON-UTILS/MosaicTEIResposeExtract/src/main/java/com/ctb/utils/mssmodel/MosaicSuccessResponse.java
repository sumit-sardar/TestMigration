package com.ctb.utils.mssmodel;

import java.io.Serializable;

public class MosaicSuccessResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private String ScoringSignature;
	private String ItemScore;
	private String CleanCandidateItemResponse;
	private String RulesApplied;
	private String Timestamp;
	
	public MosaicSuccessResponse() {
		super();
	}
	public String getScoringSignature() {
		return ScoringSignature;
	}
	public void setScoringSignature(String scoringSignature) {
		ScoringSignature = scoringSignature;
	}
	public String getItemScore() {
		return ItemScore;
	}
	public void setItemScore(String itemScore) {
		ItemScore = itemScore;
	}
	public String getCleanCandidateItemResponse() {
		return CleanCandidateItemResponse;
	}
	public void setCleanCandidateItemResponse(String cleanCandidateItemResponse) {
		CleanCandidateItemResponse = cleanCandidateItemResponse;
	}
	public String getRulesApplied() {
		return RulesApplied;
	}
	public void setRulesApplied(String rulesApplied) {
		RulesApplied = rulesApplied;
	}
	public String getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

}
