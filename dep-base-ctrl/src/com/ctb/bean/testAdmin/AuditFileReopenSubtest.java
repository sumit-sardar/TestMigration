package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;
import java.sql.Blob;

public class AuditFileReopenSubtest extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer AuditId;
    private Integer customerId;
    private Integer orgNodeId;
    private Integer testAdminId;
    private Integer studentId;
    private Integer testRosterId;
    private Integer itemSetTSId;
    private Integer itemSetTDId;
    private String oldSRosterCompStatus;
    private String newRosterCompStatus;
    private String oldSubtestCompStatus;
    private String newSubtestCompStatus;
    private Integer createdBy;
    private Date createdDateTime;
    private String ticketId;
    private String requestorName;
    private String reasonForRequest;
    
	/**
	 * @return Returns the createdBy.
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return Returns the createdBy.
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	/**
	 * @return Returns the customerId.
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId The customerId to set.
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the oldTestCompletionStatus
	 */
	public String getOldSRosterCompStatus() {
		return oldSRosterCompStatus;
	}
	/**
	 * @param oldTestCompletionStatus the oldTestCompletionStatus to set
	 */
	public void setOldSRosterCompStatus(String oldSRosterCompStatus) {
		this.oldSRosterCompStatus = oldSRosterCompStatus;
	}
	/**
	 * @return the newTestCompletionStatus
	 */
	public String getNewRosterCompStatus() {
		return newRosterCompStatus;
	}
	/**
	 * @param newTestCompletionStatus the newTestCompletionStatus to set
	 */
	public void setNewRosterCompStatus(String newRosterCompStatus) {
		this.newRosterCompStatus = newRosterCompStatus;
	}
	/**
	 * @return the oldTestCompletionStatus
	 */
	public String getOldSubtestCompStatus() {
		return oldSubtestCompStatus;
	}
	/**
	 * @param oldTestCompletionStatus the oldTestCompletionStatus to set
	 */
	public void setOldSubtestCompStatus(String oldSubtestCompStatus) {
		this.oldSubtestCompStatus = oldSubtestCompStatus;
	}
	/**
	 * @return the newTestCompletionStatus
	 */
	public String getNewSubtestCompStatus() {
		return newSubtestCompStatus;
	}
	/**
	 * @param newTestCompletionStatus the newTestCompletionStatus to set
	 */
	public void setNewSubtestCompStatus(String newSubtestCompStatus) {
		this.newSubtestCompStatus = newSubtestCompStatus;
	}
	/**
	 * @return the orgNodeId
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	/**
	 * @return the testAdminId
	 */
	public Integer getTestAdminId() {
		return testAdminId;
	}
	/**
	 * @param testAdminId the testAdminId to set
	 */
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}
	/**
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
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
	 * @return the itemSetTSId
	 */
	public Integer getItemSetTSId() {
		return itemSetTSId;
	}
	/**
	 * @param itemSetTSId the itemSetTSId to set
	 */
	public void setItemSetTSId(Integer itemSetTSId) {
		this.itemSetTSId = itemSetTSId;
	}
	/**
	 * @return the itemSetTDId
	 */
	public Integer getItemSetTDId() {
		return itemSetTDId;
	}
	/**
	 * @param itemSetTDId the itemSetTDId to set
	 */
	public void setItemSetTDId(Integer itemSetTDId) {
		this.itemSetTDId = itemSetTDId;
	}
	/**
	 * @return the auditId
	 */
	public Integer getAuditId() {
		return AuditId;
	}
	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(Integer auditId) {
		AuditId = auditId;
	}
	/**
	 * @return the ticketId
	 */
	public String getTicketId() {
		return ticketId;
	}
	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	/**
	 * @return the requestorName
	 */
	public String getRequestorName() {
		return requestorName;
	}
	/**
	 * @param requestorName the requestorName to set
	 */
	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}
	
	/**
	 * @return the reasonForRequest
	 */
	public String getReasonForRequest() {
		return reasonForRequest;
	}
	/**
	 * @param reasonForRequest the reasonForRequest to set
	 */
	public void setReasonForRequest(String reasonForRequest) {
		this.reasonForRequest = reasonForRequest;
	}
}
