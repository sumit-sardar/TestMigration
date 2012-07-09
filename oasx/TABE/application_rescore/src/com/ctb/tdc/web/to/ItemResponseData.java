package com.ctb.tdc.web.to;

import org.apache.log4j.Logger;


public class ItemResponseData extends ReplicationObject {
	
	static Logger logger = Logger.getLogger(ItemResponseData.class);
	
	public ItemResponseData() {
	
	}
	
	private int testRosterId;
	private int itemSetId;
    private String itemId;
    private int itemSortOrder;
    private String responseSeqNum;
    private String studentMarked;
    private String itemType;
    private String response;
    private String constructedResponse;
    private int responseElapsedTime;
    private int eid;
    private int score;
    private String answerArea;
    private boolean audioItem;
    private String responseType;
    private boolean sendCatSave;
    private String contentArea;
    
	public boolean isSendCatSave() {
		return sendCatSave;
	}

	public void setSendCatSave(boolean sendCatSave) {
		this.sendCatSave = sendCatSave;
	}
	
	public String getContentArea() {
		return this.contentArea;
	}

	public void setContentArea(String contentArea) {
		this.contentArea = contentArea;
	}

	public boolean isAudioItem() {
		return audioItem;
	}
	
	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public void setAudioItem(boolean audioItem) {
		this.audioItem = audioItem;
	}
	public int getTestRosterId() {
		return testRosterId;
	}
	public void setTestRosterId(int testRosterId) {
		this.testRosterId = testRosterId;
	}
	public int getItemSetId() {
		return itemSetId;
	}
	public void setItemSetId(int itemSetId) {
		this.itemSetId = itemSetId;
	}
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
	public void setResponseSeqNum(String responseSeqNum) {
		this.responseSeqNum = responseSeqNum;
	}
	/**
	 * @return Returns the responseSeqNum.
	 */
	public String getResponseSeqNum() {
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
	public String getConstructedResponse() {
		return constructedResponse;
	}
	/**
	 * @param constructedResponse the constructedResponse to set
	 */
	public void setConstructedResponse(String constructedResponse) {
		this.constructedResponse = constructedResponse;
	}
	
	/**
	 * @return the answerArea
	 */
	public String getAnswerArea() {
		return answerArea;
	}
	/**
	 * @param answerArea the answerArea to set
	 */
	public void setAnswerArea(String answerArea) {
		this.answerArea = answerArea;
	}
} 
