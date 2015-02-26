package com.ctb.dto;

import java.io.Serializable;

public class Organization implements Serializable{

	private static final long serialVersionUID = 1L;

	
	private Integer orgNodeId;
	private String orgNodeName;
	private String orgCategoryName;
	private String orgNodeMdrNumber;
	private String orgNodeCode;
	private String categoryLevel;
	

	/**
	 * @return the orgNodeId
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	/**
	 * @return the orgNodeName
	 */
	public String getOrgNodeName() {
		return orgNodeName;
	}
	/**
	 * @param orgNodeName the orgNodeName to set
	 */
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}
	/**
	 * @return the orgNodeMdrNumber
	 */
	public String getOrgNodeMdrNumber() {
		return orgNodeMdrNumber;
	}
	/**
	 * @param orgNodeMdrNumber the orgNodeMdrNumber to set
	 */
	public void setOrgNodeMdrNumber(String orgNodeMdrNumber) {
		this.orgNodeMdrNumber = orgNodeMdrNumber;
	}
	/**
	 * @return the orgNodeCode
	 */
	public String getOrgNodeCode() {
		return orgNodeCode;
	}
	/**
	 * @param orgNodeCode the orgNodeCode to set
	 */
	public void setOrgNodeCode(String orgNodeCode) {
		this.orgNodeCode = orgNodeCode;
	}
	/**
	 * @return the categoryLevel
	 */
	public String getCategoryLevel() {
		return categoryLevel;
	}
	/**
	 * @param categoryLevel the categoryLevel to set
	 */
	public void setCategoryLevel(String categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
	/**
	 * @return the orgCategoryName
	 */
	public String getOrgCategoryName() {
		return orgCategoryName;
	}
	/**
	 * @param orgCategoryName the orgCategoryName to set
	 */
	public void setOrgCategoryName(String orgCategoryName) {
		this.orgCategoryName = orgCategoryName;
	}
	
}
