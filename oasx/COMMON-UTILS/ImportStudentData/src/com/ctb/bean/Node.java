package com.ctb.bean;

import java.util.Date;

/**
 * This class is Used to store the data of Org-Node table with modified fields.
 * @author TCS
 * 
 */
public class Node extends CTBBean {
	static final long serialVersionUID = 1L;
	private Integer orgNodeId;
	private Integer customerId;
	private Integer orgNodeCategoryId;
	private String orgNodeName;
	private String extQedPin;
	private String extElmId;
	private String extOrgNodeType;
	private String orgNodeDescription;
	private Integer createdBy;
	private Date createdDateTime;
	private Integer updatedBy;
	private Date updatedDateTime;
	private String activationStatus;
	private Integer dataImportHistoryId;
	private String parentState;
	private String parentRegion;
	private String parentCounty;
	private String parentDistrict;
	private String orgNodeCode;
	private String orgNodeCategoryName;
	private Integer childNodeCount;
	private Integer parentOrgNodeId;
	private String parentOrgNodeName;
	private OrgNodeCategory orgNodeCategory;
	private String editable;
	private Integer categoryLevel;
	private String mdrNumber;
	private String leafNodePath;

	/**
	 * @return the orgNodeId
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}

	/**
	 * @param orgNodeId
	 *            the orgNodeId to set
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the orgNodeCategoryId
	 */
	public Integer getOrgNodeCategoryId() {
		return orgNodeCategoryId;
	}

	/**
	 * @param orgNodeCategoryId
	 *            the orgNodeCategoryId to set
	 */
	public void setOrgNodeCategoryId(Integer orgNodeCategoryId) {
		this.orgNodeCategoryId = orgNodeCategoryId;
	}

	/**
	 * @return the orgNodeName
	 */
	public String getOrgNodeName() {
		return orgNodeName;
	}

	/**
	 * @param orgNodeName
	 *            the orgNodeName to set
	 */
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}

	/**
	 * @return the extQedPin
	 */
	public String getExtQedPin() {
		return extQedPin;
	}

	/**
	 * @param extQedPin
	 *            the extQedPin to set
	 */
	public void setExtQedPin(String extQedPin) {
		this.extQedPin = extQedPin;
	}

	/**
	 * @return the extElmId
	 */
	public String getExtElmId() {
		return extElmId;
	}

	/**
	 * @param extElmId
	 *            the extElmId to set
	 */
	public void setExtElmId(String extElmId) {
		this.extElmId = extElmId;
	}

	/**
	 * @return the extOrgNodeType
	 */
	public String getExtOrgNodeType() {
		return extOrgNodeType;
	}

	/**
	 * @param extOrgNodeType
	 *            the extOrgNodeType to set
	 */
	public void setExtOrgNodeType(String extOrgNodeType) {
		this.extOrgNodeType = extOrgNodeType;
	}

	/**
	 * @return the orgNodeDescription
	 */
	public String getOrgNodeDescription() {
		return orgNodeDescription;
	}

	/**
	 * @param orgNodeDescription
	 *            the orgNodeDescription to set
	 */
	public void setOrgNodeDescription(String orgNodeDescription) {
		this.orgNodeDescription = orgNodeDescription;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
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
	 * @param createdDateTime
	 *            the createdDateTime to set
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
	 * @param updatedBy
	 *            the updatedBy to set
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
	 * @param updatedDateTime
	 *            the updatedDateTime to set
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
	 * @param activationStatus
	 *            the activationStatus to set
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}

	/**
	 * @return the dataImportHistoryId
	 */
	public Integer getDataImportHistoryId() {
		return dataImportHistoryId;
	}

	/**
	 * @param dataImportHistoryId
	 *            the dataImportHistoryId to set
	 */
	public void setDataImportHistoryId(Integer dataImportHistoryId) {
		this.dataImportHistoryId = dataImportHistoryId;
	}

	/**
	 * @return the parentState
	 */
	public String getParentState() {
		return parentState;
	}

	/**
	 * @param parentState
	 *            the parentState to set
	 */
	public void setParentState(String parentState) {
		this.parentState = parentState;
	}

	/**
	 * @return the parentRegion
	 */
	public String getParentRegion() {
		return parentRegion;
	}

	/**
	 * @param parentRegion
	 *            the parentRegion to set
	 */
	public void setParentRegion(String parentRegion) {
		this.parentRegion = parentRegion;
	}

	/**
	 * @return the parentCounty
	 */
	public String getParentCounty() {
		return parentCounty;
	}

	/**
	 * @param parentCounty
	 *            the parentCounty to set
	 */
	public void setParentCounty(String parentCounty) {
		this.parentCounty = parentCounty;
	}

	/**
	 * @return the parentDistrict
	 */
	public String getParentDistrict() {
		return parentDistrict;
	}

	/**
	 * @param parentDistrict
	 *            the parentDistrict to set
	 */
	public void setParentDistrict(String parentDistrict) {
		this.parentDistrict = parentDistrict;
	}

	/**
	 * @return the orgNodeCode
	 */
	public String getOrgNodeCode() {
		return orgNodeCode;
	}

	/**
	 * @param orgNodeCode
	 *            the orgNodeCode to set
	 */
	public void setOrgNodeCode(String orgNodeCode) {
		this.orgNodeCode = orgNodeCode;
	}

	/**
	 * @return the orgNodeCategoryName
	 */
	public String getOrgNodeCategoryName() {
		return orgNodeCategoryName;
	}

	/**
	 * @param orgNodeCategoryName
	 *            the orgNodeCategoryName to set
	 */
	public void setOrgNodeCategoryName(String orgNodeCategoryName) {
		this.orgNodeCategoryName = orgNodeCategoryName;
	}

	/**
	 * @return the childNodeCount
	 */
	public Integer getChildNodeCount() {
		return childNodeCount;
	}

	/**
	 * @param childNodeCount
	 *            the childNodeCount to set
	 */
	public void setChildNodeCount(Integer childNodeCount) {
		this.childNodeCount = childNodeCount;
	}

	/**
	 * @return the parentOrgNodeId
	 */
	public Integer getParentOrgNodeId() {
		return parentOrgNodeId;
	}

	/**
	 * @param parentOrgNodeId
	 *            the parentOrgNodeId to set
	 */
	public void setParentOrgNodeId(Integer parentOrgNodeId) {
		this.parentOrgNodeId = parentOrgNodeId;
	}

	/**
	 * @return the parentOrgNodeName
	 */
	public String getParentOrgNodeName() {
		return parentOrgNodeName;
	}

	/**
	 * @param parentOrgNodeName
	 *            the parentOrgNodeName to set
	 */
	public void setParentOrgNodeName(String parentOrgNodeName) {
		this.parentOrgNodeName = parentOrgNodeName;
	}

	/**
	 * @return the orgNodeCategory
	 */
	public OrgNodeCategory getOrgNodeCategory() {
		return orgNodeCategory;
	}

	/**
	 * @param orgNodeCategory
	 *            the orgNodeCategory to set
	 */
	public void setOrgNodeCategory(OrgNodeCategory orgNodeCategory) {
		this.orgNodeCategory = orgNodeCategory;
	}

	/**
	 * @return the editable
	 */
	public String getEditable() {
		return editable;
	}

	/**
	 * @param editable
	 *            the editable to set
	 */
	public void setEditable(String editable) {
		this.editable = editable;
	}

	/**
	 * @return the categoryLevel
	 */
	public Integer getCategoryLevel() {
		return categoryLevel;
	}

	/**
	 * @param categoryLevel
	 *            the categoryLevel to set
	 */
	public void setCategoryLevel(Integer categoryLevel) {
		this.categoryLevel = categoryLevel;
	}

	/**
	 * @return the mdrNumber
	 */
	public String getMdrNumber() {
		return mdrNumber;
	}

	/**
	 * @param mdrNumber
	 *            the mdrNumber to set
	 */
	public void setMdrNumber(String mdrNumber) {
		this.mdrNumber = mdrNumber;
	}

	/**
	 * @return the leafNodePath
	 */
	public String getLeafNodePath() {
		return leafNodePath;
	}

	/**
	 * @param leafNodePath
	 *            the leafNodePath to set
	 */
	public void setLeafNodePath(String leafNodePath) {
		this.leafNodePath = leafNodePath;
	}

}
