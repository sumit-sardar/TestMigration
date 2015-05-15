package com.ctb.bean.testAdmin;

import java.util.ArrayList;
import java.util.List;

import com.ctb.bean.CTBBean;

public class AncestorOrgDetails extends CTBBean {

	static final long serialVersionUID = 1L;
	private Integer orgNodeId;
	private Integer parentOrgNodeId;
	private Integer orgNodeCategoryId;
	private String orgNodeName;
	private Integer categoryLevel;
	private String orgNodeCategoryName;
	private List<AncestorOrgDetails> childrenNodes = new ArrayList<AncestorOrgDetails>();

	public Integer getOrgNodeId() {
		return orgNodeId;
	}

	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}

	public Integer getParentOrgNodeId() {
		return parentOrgNodeId;
	}

	public void setParentOrgNodeId(Integer parentOrgNodeId) {
		this.parentOrgNodeId = parentOrgNodeId;
	}

	public Integer getOrgNodeCategoryId() {
		return orgNodeCategoryId;
	}

	public void setOrgNodeCategoryId(Integer orgNodeCategoryId) {
		this.orgNodeCategoryId = orgNodeCategoryId;
	}

	public String getOrgNodeName() {
		return orgNodeName;
	}

	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}

	public Integer getCategoryLevel() {
		return categoryLevel;
	}

	public void setCategoryLevel(Integer categoryLevel) {
		this.categoryLevel = categoryLevel;
	}

	public String getOrgNodeCategoryName() {
		return orgNodeCategoryName;
	}

	public void setOrgNodeCategoryName(String orgNodeCategoryName) {
		this.orgNodeCategoryName = orgNodeCategoryName;
	}

	public List<AncestorOrgDetails> getChildrenNodes() {
		return childrenNodes;
	}

	public void setChildrenNodes(List<AncestorOrgDetails> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}

}
