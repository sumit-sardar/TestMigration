package com.ctb.prism.web.bean;

import com.ctb.bean.CTBBean;

/**
 * This class will hold data for each prism call.The data will be used for audit
 * purpose.
 * 
 * @author TCS
 * 
 */
public class PrismAuditLog extends CTBBean {
	
	static final long serialVersionUID = 1L;

	private Long invokeId;
	private Long rosterId;
	private Long studentId;
	private Long sessionId;
	private Long wsInvokeTimestamp;
	private Long wsResponseTimestamp;
	private String retryProcess;
	private Long retryErrorLogId;
	private String prismProcessId;
	private String prismPartitionId;
	private String statusCode;
	private String message;
	private String wsType;
	private Long errorLogKey;
	private String oasXmlSent;
	private String prismResponse;
	private long updatedDateTime;
	private byte[] oasXmlBytes;
	private byte[] prismResponseBytes;

	/**
	 * Default constructor
	 */
	public PrismAuditLog() {
		super();
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
	 * @return the invokeId
	 */
	public Long getInvokeId() {
		return invokeId;
	}

	/**
	 * @param invokeId
	 *            the invokeId to set
	 */
	public void setInvokeId(Long invokeId) {
		this.invokeId = invokeId;
	}

	/**
	 * @return the studentId
	 */
	public Long getStudentId() {
		return studentId;
	}

	/**
	 * @param studentId
	 *            the studentId to set
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
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the wsInvokeTimestamp
	 */
	public Long getWsInvokeTimestamp() {
		return wsInvokeTimestamp;
	}

	/**
	 * @param wsInvokeTimestamp
	 *            the wsInvokeTimestamp to set
	 */
	public void setWsInvokeTimestamp(Long wsInvokeTimestamp) {
		this.wsInvokeTimestamp = wsInvokeTimestamp;
	}

	/**
	 * @return the wsResponseTimestamp
	 */
	public Long getWsResponseTimestamp() {
		return wsResponseTimestamp;
	}

	/**
	 * @param wsResponseTimestamp
	 *            the wsResponseTimestamp to set
	 */
	public void setWsResponseTimestamp(Long wsResponseTimestamp) {
		this.wsResponseTimestamp = wsResponseTimestamp;
	}

	/**
	 * @return the prismProcessId
	 */
	public String getPrismProcessId() {
		return prismProcessId;
	}

	/**
	 * @param prismProcessId
	 *            the prismProcessId to set
	 */
	public void setPrismProcessId(String prismProcessId) {
		this.prismProcessId = prismProcessId;
	}

	/**
	 * @return the prismPartitionId
	 */
	public String getPrismPartitionId() {
		return prismPartitionId;
	}

	/**
	 * @param prismPartitionId
	 *            the prismPartitionId to set
	 */
	public void setPrismPartitionId(String prismPartitionId) {
		this.prismPartitionId = prismPartitionId;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the wsType
	 */
	public String getWsType() {
		return wsType;
	}

	/**
	 * @param wsType
	 *            the wsType to set
	 */
	public void setWsType(String wsType) {
		this.wsType = wsType;
	}

	/**
	 * @return the errorLogKey
	 */
	public Long getErrorLogKey() {
		return errorLogKey;
	}

	/**
	 * @param errorLogKey
	 *            the errorLogKey to set
	 */
	public void setErrorLogKey(Long errorLogKey) {
		this.errorLogKey = errorLogKey;
	}

	/**
	 * @return the oasXmlSent
	 */
	public String getOasXmlSent() {
		return oasXmlSent;
	}

	/**
	 * @param oasXmlSent
	 *            the oasXmlSent to set
	 */
	public void setOasXmlSent(String oasXmlSent) {
		this.oasXmlSent = oasXmlSent;
	}

	/**
	 * @return the prismResponse
	 */
	public String getPrismResponse() {
		return prismResponse;
	}

	/**
	 * @param prismResponse
	 *            the prismResponse to set
	 */
	public void setPrismResponse(String prismResponse) {
		this.prismResponse = prismResponse;
	}

	/**
	 * @return the updatedDateTime
	 */
	public Long getUpdatedDateTime() {
		return updatedDateTime;
	}

	/**
	 * @param updatedDateTime
	 *            the updatedDateTime to set
	 */
	public void setUpdatedDateTime(Long updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
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
	 * @return the oasXmlBytes
	 */
	public byte[] getOasXmlBytes() {
		return oasXmlBytes;
	}


	/**
	 * @param oasXmlBytes the oasXmlBytes to set
	 */
	public void setOasXmlBytes(byte[] oasXmlBytes) {
		this.oasXmlBytes = oasXmlBytes;
	}


	/**
	 * @return the prismResponseBytes
	 */
	public byte[] getPrismResponseBytes() {
		return prismResponseBytes;
	}


	/**
	 * @param prismResponseBytes the prismResponseBytes to set
	 */
	public void setPrismResponseBytes(byte[] prismResponseBytes) {
		this.prismResponseBytes = prismResponseBytes;
	}
	
}
