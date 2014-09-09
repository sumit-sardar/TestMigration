package com.ctb.bean;

/**
 * This Class represents the Data of Top-Node with The student Count and the
 * Child Node counts.
 * 
 * @author TCS
 * 
 */
public class OrganizationNode extends Node {
	static final long serialVersionUID = 1L;
	private Integer studentCount;
	private String bottomLevelNodeFlag;
	private Integer numberOfLevels;

	public OrganizationNode() {
		super();
	}

	/**
	 * @return Returns the studentCount.
	 */
	public Integer getStudentCount() {
		return studentCount;
	}

	/**
	 * @param studentCount
	 *            The studentCount to set.
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
	 * @param bottomLevelNodeFlag
	 *            the bottomLevelNodeFlag to set
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
	 * @param numberOfLevels
	 *            the numberOfLevels to set
	 */
	public void setNumberOfLevels(Integer numberOfLevels) {
		this.numberOfLevels = numberOfLevels;
	}

}
