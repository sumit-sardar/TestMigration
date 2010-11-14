package com.ctb.bean.testAdmin; 

/**
 * Data bean representing the contents of the OAS.ORG_NODE table
 * with additional fields to indicate the student count at/below
 * the node, and the rostered student count at/below the node.
 * 
 * @author Nate_Cohen
 */
public class StudentNode extends Node 
{ 
    static final long serialVersionUID = 1L;
    private Integer studentCount;
    private Integer rosterCount;
    
    public StudentNode () {
    }
    
    public StudentNode (Node node) {
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
    }
    
	/**
	 * @return Returns the rosterCount.
	 */
	public Integer getRosterCount() {
		return rosterCount;
	}
	/**
	 * @param rosterCount The rosterCount to set.
	 */
	public void setRosterCount(Integer rosterCount) {
		this.rosterCount = rosterCount;
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
} 
