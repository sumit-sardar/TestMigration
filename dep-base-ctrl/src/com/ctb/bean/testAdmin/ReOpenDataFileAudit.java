package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;
import java.sql.Blob;

public class ReOpenDataFileAudit extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private  String oldTestCompletionStatus = null;
    private  String newTestCompletionStatus = null;
    private  Integer createdBy;
    private  Integer customerId;
    private  Integer orgNodeId;
    private  Integer testAdminId;
    private  Integer studentId;
    private  Integer testRosterId;
    private  Integer itemSetId;
    private  Integer AuditId;
    private  Date createdDateTime;
    private Integer serviceRequestingId;
    private String requestDescription;
    
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
	 * @return Returns the createdDateTime.
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime The createdDateTime to set.
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
	public String getOldTestCompletionStatus() {
		return oldTestCompletionStatus;
	}
	/**
	 * @param oldTestCompletionStatus the oldTestCompletionStatus to set
	 */
	public void setOldTestCompletionStatus(String oldTestCompletionStatus) {
		this.oldTestCompletionStatus = oldTestCompletionStatus;
	}
	/**
	 * @return the newTestCompletionStatus
	 */
	public String getNewTestCompletionStatus() {
		return newTestCompletionStatus;
	}
	/**
	 * @param newTestCompletionStatus the newTestCompletionStatus to set
	 */
	public void setNewTestCompletionStatus(String newTestCompletionStatus) {
		this.newTestCompletionStatus = newTestCompletionStatus;
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
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	/**
	 * @return the serviceRequestingId
	 */
	public Integer getServiceRequestingId() {
		return serviceRequestingId;
	}
	/**
	 * @param serviceRequestingId the serviceRequestingId to set
	 */
	public void setServiceRequestingId(Integer serviceRequestingId) {
		this.serviceRequestingId = serviceRequestingId;
	}
	/**
	 * @return the requestDescription
	 */
	public String getRequestDescription() {
		return requestDescription;
	}
	/**
	 * @param requestDescription the requestDescription to set
	 */
	public void setRequestDescription(String requestDescription) {
		this.requestDescription = requestDescription;
	}
}
