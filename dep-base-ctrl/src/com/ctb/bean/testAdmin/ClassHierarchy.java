package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

public class ClassHierarchy extends CTBBean {

	private Integer orgNodeId;
	private String orgNodeName;
	private String parentOrgNodeId;
	private String parentOrgNodeName;
	private String categoryLevel;
	
	static final long serialVersionUID = 1L;
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	public String getOrgNodeName() {
		return orgNodeName;
	}
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}
	public String getParentOrgNodeId() {
		return parentOrgNodeId;
	}
	public void setParentOrgNodeId(String parentOrgNodeId) {
		this.parentOrgNodeId = parentOrgNodeId;
	}
	public String getParentOrgNodeName() {
		return parentOrgNodeName;
	}
	public void setParentOrgNodeName(String parentOrgNodeName) {
		this.parentOrgNodeName = parentOrgNodeName;
	}
	public String getCategoryLevel() {
		return categoryLevel;
	}
	public void setCategoryLevel(String categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
	
}
