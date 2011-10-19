package com.ctb.bean.studentManagement; 

import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.StudentNode;
import com.ctb.bean.testAdmin.UserNode;

/**
 * Data bean representing the contents of the OAS.ORG_NODE table
 * with additional fields to indicate the student count at/below
 * the node.
 * 
 * @author John_Wang
 */

public class OrganizationNode extends Node 
{ 
    static final long serialVersionUID = 1L;
    private Integer studentCount;
    private String bottomLevelNodeFlag;
    private Integer numberOfLevels;
    
    public OrganizationNode() {
        super();
    }
    
    public OrganizationNode (StudentNode node) {
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
        this.setStudentCount(node.getStudentCount());
        this.setLeafNodePath(node.getLeafNodePath());
    }
    
    
    public OrganizationNode (UserNode node) {
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
        this.setLeafNodePath(node.getLeafNodePath());
    }
    
	/**
	 * @return Returns the studentCount.
	 */
	public Integer getStudentCount() {
		return studentCount;
	}
	/**
	 * @param studentCount The studentCount to set.
	 */
	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}

	/**
	 * @return the bottomLevelNodeFlag
	 */
	public String getBottomLevelNodeFlag() {
		return bottomLevelNodeFlag;
	}

	/**
	 * @param bottomLevelNodeFlag the bottomLevelNodeFlag to set
	 */
	public void setBottomLevelNodeFlag(String bottomLevelNodeFlag) {
		this.bottomLevelNodeFlag = bottomLevelNodeFlag;
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
} 

