package com.ctb.lexington.db.data;

import com.ctb.lexington.db.record.Persistent;

public class ScoringInvokeRecord implements Persistent {

	private Long testRosterId;
	private Long studentId;
	private Long sessionId;
	private String status;
	private Integer invokeCount;
	private String scorerType;
	private String additionalInfo;
	private String messages;
	private Long invokeKey;
	
	
	
	public ScoringInvokeRecord(Long testRosterId, Long studentId,
			Long sessionId, String status, Integer invokeCount,
			String scorerType, String additionalInfo, String messages, Long invokeKey) {
		this.testRosterId = testRosterId;
		this.studentId = studentId;
		this.sessionId = sessionId;
		this.status = status;
		this.invokeCount = invokeCount;
		this.scorerType = scorerType;
		this.additionalInfo = additionalInfo;
		this.messages = messages;
		this.invokeKey = invokeKey;
	}
	/**
	 * @return the testRosterId
	 */
	public Long getTestRosterId() {
		return testRosterId;
	}
	/**
	 * @param testRosterId the testRosterId to set
	 */
	public void setTestRosterId(Long testRosterId) {
		this.testRosterId = testRosterId;
	}
	/**
	 * @return the studentId
	 */
	public Long getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the sessionId
	 */
	public Long getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the invokeCount
	 */
	public Integer getInvokeCount() {
		return invokeCount;
	}
	/**
	 * @param invokeCount the invokeCount to set
	 */
	public void setInvokeCount(Integer invokeCount) {
		this.invokeCount = invokeCount;
	}
	/**
	 * @return the scorerType
	 */
	public String getScorerType() {
		return scorerType;
	}
	/**
	 * @param scorerType the scorerType to set
	 */
	public void setScorerType(String scorerType) {
		this.scorerType = scorerType;
	}
	/**
	 * @return the additionalInfo
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	/**
	 * @return the messages
	 */
	public String getMessages() {
		return messages;
	}
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(String messages) {
		this.messages = messages;
	}
	/**
	 * @return the invokeKey
	 */
	public Long getInvokeKey() {
		return invokeKey;
	}
	/**
	 * @param invokeKey the invokeKey to set
	 */
	public void setInvokeKey(Long invokeKey) {
		this.invokeKey = invokeKey;
	}
}
