package com.ctb.bean.testAdmin; 

import java.util.Date;

public class UserRole 
{ 
    private Long userId; 
	private Long roleId;
	private Long orgNodeId;
	private Long createdBy;
	private Date createdDateTime;
	private Long updatedBy;
	private Date updatedDateTime;
	private String activationStatus;
	private Long dataImportHistoryId;
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/**
	 * @return the orgNodeId
	 */
	public Long getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(Long orgNodeId) {
		this.orgNodeId = orgNodeId;   // 'this' keyword was missing ,Changed for Deferred Defect 60566
	}
	/**
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the createdDateTime
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	/**
	 * @return the updatedBy
	 */
	public Long getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return the updatedDateTime
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime the updatedDateTime to set
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	/**
	 * @return the activationStatus
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus the activationStatus to set
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	/**
	 * @return the dataImportHistoryId
	 */
	public Long getDataImportHistoryId() {
		return dataImportHistoryId;
	}
	/**
	 * @param dataImportHistoryId the dataImportHistoryId to set
	 */
	public void setDataImportHistoryId(Long dataImportHistoryId) {
		this.dataImportHistoryId = dataImportHistoryId;
	}
} 
