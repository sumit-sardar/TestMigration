package com.ctb.testSessionInfo.utils;

import java.util.ArrayList;
import java.util.List;

public class BaseTree {
	
	private List<TreeData> data = new ArrayList<TreeData> ();
	private Integer leafNodeCategoryId;
	private String isStudentExist;
	private boolean showAccessCode;
	private boolean hasPrintClassName;
	private boolean isWVCustomer;
	private boolean showMultipleAccessCode;
	private boolean hasPrintSessionName;
	
	

	public boolean isShowMultipleAccessCode() {
		return showMultipleAccessCode;
	}

	public void setShowMultipleAccessCode(boolean showMultipleAccessCode) {
		this.showMultipleAccessCode = showMultipleAccessCode;
	}

	public boolean isWVCustomer() {
		return isWVCustomer;
	}

	public void setWVCustomer(boolean isWVCustomer) {
		this.isWVCustomer = isWVCustomer;
	}

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

	/**
	 * @return the hasPrintClassName
	 */
	public boolean isHasPrintClassName() {
		return hasPrintClassName;
	}

	/**
	 * @param hasPrintClassName the hasPrintClassName to set
	 */
	public void setHasPrintClassName(boolean hasPrintClassName) {
		this.hasPrintClassName = hasPrintClassName;
	}

	public boolean isHasPrintSessionName() {
		return hasPrintSessionName;
	}

	public void setHasPrintSessionName(boolean hasPrintSessionName) {
		this.hasPrintSessionName = hasPrintSessionName;
	}

	
	

}
