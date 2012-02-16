package com.ctb.dto;

import java.io.Serializable;

public class ObjectiveObj implements Serializable{
	private static final long serialVersionUID = 1L;
	private String objectiveLevel;
	private String objectiveMastery;
	/**
	 * @return the objectiveLevel
	 */
	public String getObjectiveLevel() {
		return objectiveLevel;
	}
	/**
	 * @param objectiveLevel the objectiveLevel to set
	 */
	public void setObjectiveLevel(String objectiveLevel) {
		this.objectiveLevel = objectiveLevel;
	}
	/**
	 * @return the objectiveMastery
	 */
	public String getObjectiveMastery() {
		return objectiveMastery;
	}
	/**
	 * @param objectiveMastery the objectiveMastery to set
	 */
	public void setObjectiveMastery(String objectiveMastery) {
		this.objectiveMastery = objectiveMastery;
	}	
}
