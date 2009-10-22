package com.ctb.bean.testAdmin; 

/**
 * Data bean representing the contents of the OAS.ORG_NODE table
 * with additional fields to indicate the user count at/below
 * the node.
 * 
 * @author Nate_Cohen
 */
public class UserNode extends Node 
{ 
    static final long serialVersionUID = 1L;
    private Integer userCount;
    private Integer proctorCount;
    private Integer numberOfLevels;
    private String editable;
    public UserNode() {
    }    
    public UserNode(Node node) {
        this.setActivationStatus(node.getActivationStatus());
        this.setCreatedBy(node.getCreatedBy());
        this.setCreatedDateTime(node.getCreatedDateTime());
        this.setCustomerId(node.getCustomerId());
        this.setDataImportHistoryId(node.getDataImportHistoryId());
        this.setExtElmId(node.getExtElmId());
        this.setExtOrgNodeType(node.getExtOrgNodeType());
        this.setExtQedPin(node.getExtQedPin());
        this.setOrgNodeCategoryId(node.getOrgNodeCategoryId());
        this.setOrgNodeCode(node.getOrgNodeCode());
        this.setOrgNodeDescription(node.getOrgNodeDescription());
        this.setOrgNodeId(node.getOrgNodeId());
        this.setOrgNodeName(node.getOrgNodeName());
        this.setParentCounty(node.getParentCounty());
        this.setParentDistrict(node.getParentDistrict());
        this.setParentRegion(node.getParentRegion());
        this.setParentState(node.getParentState());
        this.setUpdatedBy(node.getUpdatedBy());
        this.setUpdatedDateTime(node.getUpdatedDateTime());
        this.setChildNodeCount(node.getChildNodeCount());
        this.setOrgNodeCategoryName(node.getOrgNodeCategoryName());
        this.setCategoryLevel(node.getCategoryLevel());
        this.setParentOrgNodeId(node.getParentOrgNodeId());
        this.setParentOrgNodeName(node.getParentOrgNodeName());
    }
    
    /**
	 * @return Returns the proctorCount.
	 */
	public Integer getProctorCount() {
		return proctorCount;
	}
	/**
	 * @param proctorCount The proctorCount to set.
	 */
	public void setProctorCount(Integer proctorCount) {
		this.proctorCount = proctorCount;
	}
	/**
	 * @return Returns the userCount.
	 */
	public Integer getUserCount() {
		return userCount;
	}
	/**
	 * @param userCount The userCount to set.
	 */
	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
    
    /**
	 * @return the numberOfLevels
	 */
	public Integer getNumberOfLevels() {
		return numberOfLevels;
	}

	/**
	 * @param numberOfLevels the numberOfLevels to set
	 */
	public void setNumberOfLevels(Integer numberOfLevels) {
		this.numberOfLevels = numberOfLevels;
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
    
} 
