package com.ctb.bean.testAdmin; 

import java.util.Date;

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the contents of the OAS.ORG_NODE table
 * 
 * @author Nate_Cohen
 */
public class Node extends CTBBean
{ 
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
    //START - Changes for MDR Number
    private String mdrNumber;
    //END - Changes for MDR Number
    //private Integer numberOfLevels;
	//private boolean hasFramework;
    private String leafNodePath;
    
    /**
	 * @return the mdrNumber
	 */
	public String getMdrNumber() {
		return mdrNumber;
	}

	/**
	 * @param mdrNumber the mdrNumber to set
	 */
	public void setMdrNumber(String mdrNumber) {
		this.mdrNumber = mdrNumber;
	}

	public Node() {}
    
	/**
	 * @return Returns the childNodeCount.
	 */
	public Integer getChildNodeCount() {
		return childNodeCount;
	}
	/**
	 * @param childNodeCount The childNodeCount to set.
	 */
	public void setChildNodeCount(Integer childNodeCount) {
		this.childNodeCount = childNodeCount;
	}
	/**
	 * @return Returns the orgNodeCategoryName.
	 */
	public String getOrgNodeCategoryName() {
		return orgNodeCategoryName;
	}
	/**
	 * @param orgNodeCategoryName The orgNodeCategoryName to set.
	 */
	public void setOrgNodeCategoryName(String orgNodeCategoryName) {
		this.orgNodeCategoryName = orgNodeCategoryName;
	}
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
	 * @return Returns the dataImportHistoryId.
	 */
	public Integer getDataImportHistoryId() {
		return dataImportHistoryId;
	}
	/**
	 * @param dataImportHistoryId The dataImportHistoryId to set.
	 */
	public void setDataImportHistoryId(Integer dataImportHistoryId) {
		this.dataImportHistoryId = dataImportHistoryId;
	}
	/**
	 * @return Returns the extElmId.
	 */
	public String getExtElmId() {
		return extElmId;
	}
	/**
	 * @param extElmId The extElmId to set.
	 */
	public void setExtElmId(String extElmId) {
		this.extElmId = extElmId;
	}
	/**
	 * @return Returns the extOrgNodeType.
	 */
	public String getExtOrgNodeType() {
		return extOrgNodeType;
	}
	/**
	 * @param extOrgNodeType The extOrgNodeType to set.
	 */
	public void setExtOrgNodeType(String extOrgNodeType) {
		this.extOrgNodeType = extOrgNodeType;
	}
	/**
	 * @return Returns the extQedPin.
	 */
	public String getExtQedPin() {
		return extQedPin;
	}
	/**
	 * @param extQedPin The extQedPin to set.
	 */
	public void setExtQedPin(String extQedPin) {
		this.extQedPin = extQedPin;
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
	 * @return Returns the orgNodeCode.
	 */
	public String getOrgNodeCode() {
		return orgNodeCode;
	}
	/**
	 * @param orgNodeCode The orgNodeCode to set.
	 */
	public void setOrgNodeCode(String orgNodeCode) {
		this.orgNodeCode = orgNodeCode;
	}
	/**
	 * @return Returns the orgNodeDescription.
	 */
	public String getOrgNodeDescription() {
		return orgNodeDescription;
	}
	/**
	 * @param orgNodeDescription The orgNodeDescription to set.
	 */
	public void setOrgNodeDescription(String orgNodeDescription) {
		this.orgNodeDescription = orgNodeDescription;
	}
	/**
	 * @return Returns the orgNodeId.
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId The orgNodeId to set.
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	/**
	 * @return Returns the orgNodeName.
	 */
	public String getOrgNodeName() {
		return orgNodeName;
	}
	/**
	 * @param orgNodeName The orgNodeName to set.
	 */
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}
	/**
	 * @return Returns the parentCounty.
	 */
	public String getParentCounty() {
		return parentCounty;
	}
	/**
	 * @param parentCounty The parentCounty to set.
	 */
	public void setParentCounty(String parentCounty) {
		this.parentCounty = parentCounty;
	}
	/**
	 * @return Returns the parentDistrict.
	 */
	public String getParentDistrict() {
		return parentDistrict;
	}
	/**
	 * @param parentDistrict The parentDistrict to set.
	 */
	public void setParentDistrict(String parentDistrict) {
		this.parentDistrict = parentDistrict;
	}
	/**
	 * @return Returns the parentRegion.
	 */
	public String getParentRegion() {
		return parentRegion;
	}
	/**
	 * @param parentRegion The parentRegion to set.
	 */
	public void setParentRegion(String parentRegion) {
		this.parentRegion = parentRegion;
	}
	/**
	 * @return Returns the parentState.
	 */
	public String getParentState() {
		return parentState;
	}
	/**
	 * @param parentState The parentState to set.
	 */
	public void setParentState(String parentState) {
		this.parentState = parentState;
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
	 * @return Returns the parentOrgNodeId.
	 */
	public Integer getParentOrgNodeId() {
		return parentOrgNodeId;
	}
	/**
	 * @param parentOrgNodeId The parentOrgNodeId to set.
	 */
	public void setParentOrgNodeId(Integer parentOrgNodeId) {
		this.parentOrgNodeId = parentOrgNodeId;
	}
    
    /**
	 * @return Returns the parentOrgNodeName.
	 */
	public String getParentOrgNodeName() {
		return parentOrgNodeName;
	}
	/**
	 * @param parentOrgNodeName The parentOrgNodeName to set.
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
	 * @param orgNodeCategory to set
	 */
	public void setOrgNodeCategory(OrgNodeCategory orgNodeCategory) {
		this.orgNodeCategory = orgNodeCategory;
	}
    
    /**
	 * @return Returns the editable.
	 */
	public String getEditable() {
		return editable;
	}
	/**
	 * @param editable The editable to set.
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
	 * @param categoryLevel the categoryLevel to set
	 */
	public void setCategoryLevel(Integer categoryLevel) {
		this.categoryLevel = categoryLevel;
	}

	/**
	 * @return the leafNodePath
	 */
	public String getLeafNodePath() {
		return leafNodePath;
	}

	/**
	 * @param leafNodePath the leafNodePath to set
	 */
	public void setLeafNodePath(String leafNodePath) {
		this.leafNodePath = leafNodePath;
	}
    
   
    /**
	 * @return Returns the hasFramework.
	 */
/*	public boolean getHasFramework() {
		return hasFramework;
	}*/
	/**
	 * @param hasFramework The hasFramework to set.
	 */
/*	public void setHasFramework(boolean hasFramework) {
		this.hasFramework = hasFramework;
	}*/
    /**
	 * @return the numberOfLevels
	 */
/*	public Integer getNumberOfLevels() {
		return numberOfLevels;
	}*/

	/**
	 * @param numberOfLevels the numberOfLevels to set
	 */
	/*public void setNumberOfLevels(Integer numberOfLevels) {
		this.numberOfLevels = numberOfLevels;
	}*/
	
   
    
} 
