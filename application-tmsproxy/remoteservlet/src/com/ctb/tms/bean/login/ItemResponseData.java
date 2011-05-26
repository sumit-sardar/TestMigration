package com.ctb.tms.bean.login; 

import java.sql.Clob;

public class ItemResponseData 
{ 
    private String itemId;
    private int itemSortOrder;
    private int responseSeqNum;
    private String studentMarked;
    private String itemType;
    private String response;
    private Clob constructedResponse;
    private int responseElapsedTime;
    private int eid;
    private int score;
    
	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return Returns the itemId.
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param responseSeqNum The responseSeqNum to set.
	 */
	public void setResponseSeqNum(int responseSeqNum) {
		this.responseSeqNum = responseSeqNum;
	}
	/**
	 * @return Returns the responseSeqNum.
	 */
	public int getResponseSeqNum() {
		return responseSeqNum;
	}
	/**
	 * @param student_marked The student_marked to set.
	 */
	public void setStudentMarked(String studentMarked) {
		this.studentMarked = studentMarked;
	}
	/**
	 * @return Returns the student_marked.
	 */
	public String getStudentMarked() {
		return studentMarked;
	}
	/**
	 * @param itemType The itemType to set.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return Returns the itemType.
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param response The response to set.
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	/**
	 * @return Returns the response.
	 */
	public String getResponse() {
		return response;
	}
	/**
	 * @param responseElapsedTime The responseElapsedTime to set.
	 */
	public void setResponseElapsedTime(int responseElapsedTime) {
		this.responseElapsedTime = responseElapsedTime;
	}
	/**
	 * @return Returns the responseElapsedTime.
	 */
	public int getResponseElapsedTime() {
		return responseElapsedTime;
	}
	/**
	 * @param itemSortOrder The itemSortOrder to set.
	 */
	public void setItemSortOrder(int itemSortOrder) {
		this.itemSortOrder = itemSortOrder;
	}
	/**
	 * @return Returns the itemSortOrder.
	 */
	public int getItemSortOrder() {
		return itemSortOrder;
	}
    /**
	 * @return Returns the eid.
	 */
	public int getEid() {
		return this.eid;
	}
	/**
	 * @param eid The eid to set.
	 */
	public void setEid(int eid) {
		this.eid = eid;
	}
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * @return the constructedResponse
	 */
	public Clob getConstructedResponse() {
		return constructedResponse;
	}
	/**
	 * @param constructedResponse the constructedResponse to set
	 */
	public void setConstructedResponse(Clob constructedResponse) {
		this.constructedResponse = constructedResponse;
	}

} 
