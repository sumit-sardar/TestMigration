package com.ctb.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ctb.utils.Constants;
import com.ctb.utils.Utility;

public class ObjectiveLevel implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, String> objectiveMap = new HashMap<String, String>();

	public Map<String, String> getObjectiveMap() {
		return objectiveMap;
	}

	public void setObjectiveMap(Map<String, String> objectiveMap) {
		this.objectiveMap = objectiveMap;
	}

	public String toString(int length){
		String val = "";
		for(String key: Constants.OBJECTIVES) {
			val += Utility.getFormatedString(objectiveMap.get(key), length);
		}
		return val;
	}
}