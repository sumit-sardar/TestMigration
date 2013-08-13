package com.ctb.bean.testAdmin;

import java.util.Date;

import com.ctb.bean.CTBBean;

/**
 *  Data bean representing the contents of the item_response
 * @author TCS
 * 
 *
 */
public class ItemResponseData extends CTBBean {

	private static final long serialVersionUID = 1L;
	
	private Integer itemResponseId;
	private Integer itemSetId;
	private Integer testRosterId;
	private String response;
	private String responseMethod;
	private Integer responseElapsedTime;
	private Integer responseSeqNum;
	private Date created_date_time;
	private String itemId;
	private String extAnswerChoiceId;
	private String studentMarked;
	private Integer createdBy;
	private Date startDateTime;
    private Date completionDateTime;
	/**
	 * @return the itemResponseId
	 */
	public Integer getItemResponseId() {
		return itemResponseId;
	}
	/**
	 * @param itemResponseId the itemResponseId to set
	 */
	public void setItemResponseId(Integer itemResponseId) {
		this.itemResponseId = itemResponseId;
	}
	/**
	 * @return the itemSetId
	 */
	public Integer getItemSetId() {
		return itemSetId;
	}
	/**
	 * @param itemSetId the itemSetId to set
	 */
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}
	/**
	 * @return the testRosterId
	 */
	public Integer getTestRosterId() {
		return testRosterId;
	}
	/**
	 * @param testRosterId the testRosterId to set
	 */
	public void setTestRosterId(Integer testRosterId) {
		this.testRosterId = testRosterId;
	}
	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	/**
	 * @return the responseMethod
	 */
	public String getResponseMethod() {
		return responseMethod;
	}
	/**
	 * @param responseMethod the responseMethod to set
	 */
	public void setResponseMethod(String responseMethod) {
		this.responseMethod = responseMethod;
	}
	/**
	 * @return the responseElapsedTime
	 */
	public Integer getResponseElapsedTime() {
		return responseElapsedTime;
	}
	/**
	 * @param responseElapsedTime the responseElapsedTime to set
	 */
	public void setResponseElapsedTime(Integer responseElapsedTime) {
		this.responseElapsedTime = responseElapsedTime;
	}
	/**
	 * @return the responseSeqNum
	 */
	public Integer getResponseSeqNum() {
		return responseSeqNum;
	}
	/**
	 * @param responseSeqNum the responseSeqNum to set
	 */
	public void setResponseSeqNum(Integer responseSeqNum) {
		this.responseSeqNum = responseSeqNum;
	}
	/**
	 * @return the created_date_time
	 */
	public Date getCreated_date_time() {
		return created_date_time;
	}
	/**
	 * @param created_date_time the created_date_time to set
	 */
	public void setCreated_date_time(Date created_date_time) {
		this.created_date_time = created_date_time;
	}
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the extAnswerChoiceId
	 */
	public String getExtAnswerChoiceId() {
		return extAnswerChoiceId;
	}
	/**
	 * @param extAnswerChoiceId the extAnswerChoiceId to set
	 */
	public void setExtAnswerChoiceId(String extAnswerChoiceId) {
		this.extAnswerChoiceId = extAnswerChoiceId;
	}
	/**
	 * @return the studentMarked
	 */
	public String getStudentMarked() {
		return studentMarked;
	}
	/**
	 * @param studentMarked the studentMarked to set
	 */
	public void setStudentMarked(String studentMarked) {
		this.studentMarked = studentMarked;
	}
	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the startDateTime
	 */
	public Date getStartDateTime() {
		return startDateTime;
	}
	/**
	 * @param startDateTime the startDateTime to set
	 */
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	/**
	 * @return the completionDateTime
	 */
	public Date getCompletionDateTime() {
		return completionDateTime;
	}
	/**
	 * @param completionDateTime the completionDateTime to set
	 */
	public void setCompletionDateTime(Date completionDateTime) {
		this.completionDateTime = completionDateTime;
	}
	
	
}
