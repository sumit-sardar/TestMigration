package com.ctb.testSessionInfo.utils;

import java.util.ArrayList;
import java.util.List;

public class BaseTree {
	
	private List<TreeData> data = new ArrayList<TreeData> ();
	private Integer leafNodeCategoryId;
	private String isStudentExist;
	private boolean showAccessCode;
	

	public String getIsStudentExist() {
		return isStudentExist;
	}

	public void setIsStudentExist(String isStudentExist) {
		this.isStudentExist = isStudentExist;
	}

	/**
	 * @return the leafNodeCategoryId
	 */
	public Integer getLeafNodeCategoryId() {
		return leafNodeCategoryId;
	}

	/**
	 * @param leafNodeCategoryId the leafNodeCategoryId to set
	 */
	public void setLeafNodeCategoryId(Integer leafNodeCategoryId) {
		this.leafNodeCategoryId = leafNodeCategoryId;
	}

	public List<TreeData> getData() {
		return data;
	}

	public void setData(List<TreeData> data) {
		this.data = data;
	}

	public boolean isShowAccessCode() {
		return showAccessCode;
	}

	public void setShowAccessCode(boolean showAccessCode) {
		this.showAccessCode = showAccessCode;
	}

	
	

}
