package com.mhe.ctb.oas.BMTSync.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to describe test parameters.
 * @author oas
 */
public class Parameters {
	private String enforceBreak;
	private String enforceTutorial; 

	public String getEnforceBreak() {
		return enforceBreak;
	}
	@JsonProperty(value="enforceBreak", required=true)
	public void setEnforceBreak(String enforceBreak) {
		this.enforceBreak = enforceBreak;
	}

	public String getEnforceTutorial() {
		return enforceTutorial;
	}
	@JsonProperty(value="enforceTutorial", required=true)
	public void setEnforceTutorial(String enforceTutorial) {
		this.enforceTutorial = enforceTutorial;
	}
}
