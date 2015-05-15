package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

public class BulkReportData extends CTBBean {
	static final long serialVersionUID = 1L;
	private Node[] topLevelNodes;
	private Node[] parentHierarchyDetails;
	private OrgNodeCategory[] orgNodeCategoryList;
	private AncestorOrgDetails childLevelNodes;

	public Node[] getTopLevelNodes() {
		return topLevelNodes;
	}

	public void setTopLevelNodes(Node[] topLevelNodes) {
		this.topLevelNodes = topLevelNodes;
	}

	public Node[] getParentHierarchyDetails() {
		return parentHierarchyDetails;
	}

	public void setParentHierarchyDetails(Node[] parentHierarchyDetails) {
		this.parentHierarchyDetails = parentHierarchyDetails;
	}

	public OrgNodeCategory[] getOrgNodeCategoryList() {
		return orgNodeCategoryList;
	}

	public void setOrgNodeCategoryList(OrgNodeCategory[] orgNodeCategoryList) {
		this.orgNodeCategoryList = orgNodeCategoryList;
	}

	public AncestorOrgDetails getChildLevelNodes() {
		return childLevelNodes;
	}

	public void setChildLevelNodes(AncestorOrgDetails childLevelNodes) {
		this.childLevelNodes = childLevelNodes;
	}

}
