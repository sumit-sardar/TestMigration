package com.ctb.xlsread;

import java.io.Serializable;



/**
 * Bean for CR Item Content
 * 
 * @author TCS
 */
public class RubricViewData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String itemId;
	private Integer score;
	private String rubricDescription;
	private String sampleResponse;
	private String rubricExplanation;
	
	/**
	 * @return Return the item ID
	 */
	public String getItemId() {
		return itemId;
	}
	
	/**
	 * @param itemId -
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * @return Return the score
	 */
	public Integer getScore() {
		return score;
	}
	
	/**
	 * @param score -
	 */
	public void setScore(Integer score) {
		this.score = score;
	}
	
	/**
	 * @return Return the rubric description as per the score
	 */
	public String getRubricDescription() {
		return rubricDescription;
	}
	
	/**
	 * @param rubricDescription -
	 */
	public void setRubricDescription(String rubricDescription) {
		this.rubricDescription = rubricDescription;
	}
	
	/**
	 * @return Return a sample response for the item
	 */
	public String getSampleResponse() {
		return sampleResponse;
	}
	
	/**
	 * @param sampleResponse -
	 */
	public void setSampleResponse(String sampleResponse) {
		this.sampleResponse = sampleResponse;
	}
	
	/**
	 * @return Return the rubric explanation as per the score
	 */
	public String getRubricExplanation() {
		return rubricExplanation;
	}
	
	/**
	 * @param rubricExplanation -
	 */
	public void setRubricExplanation(String rubricExplanation) {
		this.rubricExplanation = rubricExplanation;
	}

}
