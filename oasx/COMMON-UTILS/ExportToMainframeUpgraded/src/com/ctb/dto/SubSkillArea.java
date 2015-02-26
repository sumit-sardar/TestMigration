package com.ctb.dto;

import java.io.Serializable;

public class SubSkillArea implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String subSkillId;
	private String subSkillName;
	private String subSkillCategory;
	private Integer prouductId;
	private String pointsObtained;
	private String percentObtained;
	
	
	
	/**
	 * @return the subSkillId
	 */
	public String getSubSkillId() {
		return subSkillId;
	}
	/**
	 * @param subSkillId the subSkillId to set
	 */
	public void setSubSkillId(String subSkillId) {
		this.subSkillId = subSkillId;
	}
	/**
	 * @return the subSkillName
	 */
	public String getSubSkillName() {
		return subSkillName;
	}
	/**
	 * @param subSkillName the subSkillName to set
	 */
	public void setSubSkillName(String subSkillName) {
		this.subSkillName = subSkillName;
	}
	/**
	 * @return the subSkillCategory
	 */
	public String getSubSkillCategory() {
		return subSkillCategory;
	}
	/**
	 * @param subSkillCategory the subSkillCategory to set
	 */
	public void setSubSkillCategory(String subSkillCategory) {
		this.subSkillCategory = subSkillCategory;
	}
	/**
	 * @return the prouductId
	 */
	public Integer getProuductId() {
		return prouductId;
	}
	/**
	 * @param prouductId the prouductId to set
	 */
	public void setProuductId(Integer prouductId) {
		this.prouductId = prouductId;
	}
	/**
	 * @return the pointsObtained
	 */
	public String getPointsObtained() {
		return pointsObtained;
	}
	/**
	 * @param pointsObtained the pointsObtained to set
	 */
	public void setPointsObtained(String pointsObtained) {
		this.pointsObtained = pointsObtained;
	}
	/**
	 * @return the percentObtained
	 */
	public String getPercentObtained() {
		return percentObtained;
	}
	/**
	 * @param percentObtained the percentObtained to set
	 */
	public void setPercentObtained(String percentObtained) {
		this.percentObtained = percentObtained;
	}
}