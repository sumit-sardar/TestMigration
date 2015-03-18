package com.mhe.ctb.oas.BMTSync.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to describe a test time limit.
 * @author oas
 */
public class EnforceTimeLimit {
	private String isRequired;   // Time_Limit required
	private String timeLimitInMins;

	public String getIsRequired() {
		return isRequired;
	}
	@JsonProperty(value="IsRequiured", required=true)
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public String getTimeLimitInMins() {
		return timeLimitInMins;
	}
	@JsonProperty(value="TimeLimitInMins", required=true)
	public void setTimeLimitInMins(String timeLimitInMins) {
		this.timeLimitInMins = timeLimitInMins;
	}
}
