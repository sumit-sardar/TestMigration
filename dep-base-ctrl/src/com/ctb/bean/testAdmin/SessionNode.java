package com.ctb.bean.testAdmin; 

/**
 * Data bean representing the contents of the OAS.ORG_NODE table
 * with additional fields to indicate the test session count at/below
 * the node, and the count of immediate children of the node.
 * 
 * @author Nate_Cohen
 */
public class SessionNode extends Node 
{ 
    static final long serialVersionUID = 1L;
    private Integer sessionCount;
    private Integer childNodeCount;
    
    public SessionNode(Node node) {
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
    }
    
	/**
	 * @return Returns the sessionCount.
	 */
	public Integer getSessionCount() {
		return sessionCount;
	}
	/**
	 * @param sessionCount The sessionCount to set.
	 */
	public void setSessionCount(Integer sessionCount) {
		this.sessionCount = sessionCount;
	}
    
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
} 
