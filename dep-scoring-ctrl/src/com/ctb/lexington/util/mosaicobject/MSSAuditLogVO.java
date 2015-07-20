package com.ctb.lexington.util.mosaicobject;

import java.io.Serializable;
import java.sql.Timestamp;

import com.ctb.lexington.db.record.Persistent;

public class MSSAuditLogVO implements Persistent {

	private static final long serialVersionUID = 1L;

	private Long rosterId;
	private Long studentId;
	private Long sessionId;
	private Timestamp wsInvokeTimestamp;
	private Timestamp wsResponseTimestamp;
	private String retryProcess;
	private Long retryErrorLogId;
	private String oasItemId;
	private String dasItemId;
	private String wsRequest;
	private String wsResponse;
	
	
	public MSSAuditLogVO(Timestamp wsInvokeTimestamp,
			Timestamp wsResponseTimestamp, String oasItemId, String dasItemId, String wsRequest, String wsResponse) {
		super();
		this.wsInvokeTimestamp = wsInvokeTimestamp;
		this.wsResponseTimestamp = wsResponseTimestamp;
		this.oasItemId = oasItemId;
		this.dasItemId = dasItemId;
		this.wsRequest = wsRequest;
		this.wsResponse = wsResponse;
	}


	/**
	 * @return the rosterId
	 */
	public Long getRosterId() {
		return rosterId;
	}


	/**
	 * @param rosterId the rosterId to set
	 */
	public void setRosterId(Long rosterId) {
		this.rosterId = rosterId;
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
	 * @return the wsInvokeTimestamp
	 */
	public Timestamp getWsInvokeTimestamp() {
		return wsInvokeTimestamp;
	}


	/**
	 * @param wsInvokeTimestamp the wsInvokeTimestamp to set
	 */
	public void setWsInvokeTimestamp(Timestamp wsInvokeTimestamp) {
		this.wsInvokeTimestamp = wsInvokeTimestamp;
	}


	/**
	 * @return the wsResponseTimestamp
	 */
	public Timestamp getWsResponseTimestamp() {
		return wsResponseTimestamp;
	}


	/**
	 * @param wsResponseTimestamp the wsResponseTimestamp to set
	 */
	public void setWsResponseTimestamp(Timestamp wsResponseTimestamp) {
		this.wsResponseTimestamp = wsResponseTimestamp;
	}


	/**
	 * @return the retryProcess
	 */
	public String getRetryProcess() {
		return retryProcess;
	}


	/**
	 * @param retryProcess the retryProcess to set
	 */
	public void setRetryProcess(String retryProcess) {
		this.retryProcess = retryProcess;
	}


	/**
	 * @return the retryErrorLogId
	 */
	public Long getRetryErrorLogId() {
		return retryErrorLogId;
	}


	/**
	 * @param retryErrorLogId the retryErrorLogId to set
	 */
	public void setRetryErrorLogId(Long retryErrorLogId) {
		this.retryErrorLogId = retryErrorLogId;
	}


	/**
	 * @return the oasItemId
	 */
	public String getOasItemId() {
		return oasItemId;
	}


	/**
	 * @param oasItemId the oasItemId to set
	 */
	public void setOasItemId(String oasItemId) {
		this.oasItemId = oasItemId;
	}


	/**
	 * @return the dasItemId
	 */
	public String getDasItemId() {
		return dasItemId;
	}


	/**
	 * @param dasItemId the dasItemId to set
	 */
	public void setDasItemId(String dasItemId) {
		this.dasItemId = dasItemId;
	}


	/**
	 * @return the wsRequest
	 */
	public String getWsRequest() {
		return wsRequest;
	}


	/**
	 * @param wsRequest the wsRequest to set
	 */
	public void setWsRequest(String wsRequest) {
		this.wsRequest = wsRequest;
	}


	/**
	 * @return the wsResponse
	 */
	public String getWsResponse() {
		return wsResponse;
	}


	/**
	 * @param wsResponse the wsResponse to set
	 */
	public void setWsResponse(String wsResponse) {
		this.wsResponse = wsResponse;
	}
	
}
