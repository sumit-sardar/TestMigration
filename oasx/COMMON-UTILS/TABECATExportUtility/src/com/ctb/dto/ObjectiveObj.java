package com.ctb.dto;

import java.io.Serializable;

public class ObjectiveObj implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String objective;
	private String objectiveValue;
	
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	public String getObjectiveValue() {
		return objectiveValue;
	}
	public void setObjectiveValue(String objectiveValue) {
		this.objectiveValue = objectiveValue;
	}
}