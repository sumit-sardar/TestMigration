package com.ctb.bean;

import java.util.Date;

public class OrgNodeCategory extends CTBBean{
	static final long serialVersionUID = 1L;
	private Integer orgNodeCategoryId;
	private Integer customerId;
	private Integer categoryLevel;
	private String categoryName;
	private String isGroup;
	private Integer createdBy;
	private Date createdDateTime;
	private Integer updatedBy;
	private Date updatedDateTime;
	private String activationStatus;
	/**
	 * @return the orgNodeCategoryId
	 */
	public Integer getOrgNodeCategoryId() {
		return orgNodeCategoryId;
	}
	/**
	 * @param orgNodeCategoryId the orgNodeCategoryId to set
	 */
	public void setOrgNodeCategoryId(Integer orgNodeCategoryId) {
		this.orgNodeCategoryId = orgNodeCategoryId;
	}
	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the categoryLevel
	 */
	public Integer getCategoryLevel() {
		return categoryLevel;
	}
	/**
	 * @param categoryLevel the categoryLevel to set
	 */
	public void setCategoryLevel(Integer categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/**
	 * @return the isGroup
	 */
	public String getIsGroup() {
		return isGroup;
	}
	/**
	 * @param isGroup the isGroup to set
	 */
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
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
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(Integer updatedBy) {
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
	
	

}
