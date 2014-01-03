package com.ctb.csvread;

import java.io.Serializable;

public class GRItemRuleData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String monarchItemId ;
	private String itemRules;
	private String correctAnswer;
	
	/**
	 * @return the monarchItemId
	 */
	public String getMonarchItemId() {
		return monarchItemId;
	}
	/**
	 * @param monarchItemId the monarchItemId to set
	 */
	public void setMonarchItemId(String monarchItemId) {
		this.monarchItemId = monarchItemId;
	}
	/**
	 * @return the itemRules
	 */
	public String getItemRules() {
		return itemRules;
	}
	/**
	 * @param itemRules the itemRules to set
	 */
	public void setItemRules(String itemRules) {
		this.itemRules = itemRules;
	}
	/**
	 * @return the correctAnswer
	 */
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	/**
	 * @param correctAnswer the correctAnswer to set
	 */
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	
}
