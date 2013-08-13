package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

public class OrgNodeCategory extends CTBBean
{
    /**
	 * @param args
     * Data bean representing the contents of the OAS.ORG_NODE_Category table
     * @author Tata Consulency Services
	 */
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
    private Boolean deletable;
    private Boolean beforeInsertable;
    private Boolean afterInsertable;
    
	/**
	 * @return Returns the activationStatus.
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus The activationStatus to set.
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	/**
	 * @return Returns the categoryLevel.
	 */
	public Integer getCategoryLevel() {
		return categoryLevel;
	}
	/**
	 * @param categoryLevel The categoryLevel to set.
	 */
	public void setCategoryLevel(Integer categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
	/**
	 * @return Returns the categoryName.
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param categoryName The categoryName to set.
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
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
	 * @return Returns the isGroup.
	 */
	public String getIsGroup() {
		return isGroup;
	}
	/**
	 * @param isGroup The isGroup to set.
	 */
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}
	/**
	 * @return Returns the orgNodeCategoryId.
	 */
	public Integer getOrgNodeCategoryId() {
		return orgNodeCategoryId;
	}
	/**
	 * @param orgNodeCategoryId The orgNodeCategoryId to set.
	 */
	public void setOrgNodeCategoryId(Integer orgNodeCategoryId) {
		this.orgNodeCategoryId = orgNodeCategoryId;
	}
	/**
	 * @return Returns the updatedBy.
	 */
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy The updatedBy to set.
	 */
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return Returns the updatedDateTime.
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime The updatedDateTime to set.
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	} 
    
    /**
	 * @return Returns the deletable.
	 */
	public Boolean getDeletable() {
		return deletable;
	}
	/**
	 * @param deletable The deletable to set.
	 */
	public void setDeletable(Boolean deletable) {
		this.deletable = deletable;
	} 
    
    /**
	 * @return Returns the deletable.
	 */
	public Boolean getBeforeInsertable() {
		return beforeInsertable;
	}
	/**
	 * @param deletable The deletable to set.
	 */
	public void setBeforeInsertable(Boolean beforeInsertable) {
		this.beforeInsertable = beforeInsertable;
	} 
    
    /**
	 * @return Returns the deletable.
	 */
	public Boolean getAfterInsertable() {
		return afterInsertable;
	}
	/**
	 * @param deletable The deletable to set.
	 */
	public void setAfterInsertable(Boolean afterInsertable) {
		this.afterInsertable = afterInsertable;
	} 
} 
