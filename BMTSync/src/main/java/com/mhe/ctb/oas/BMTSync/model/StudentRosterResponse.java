package com.mhe.ctb.oas.BMTSync.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentRosterResponse extends StudentRoster {
	private String errorCode;
	private String errorMessage;
	
	public String getErrorCode() {
		return errorCode;
	}
	@JsonProperty(value="errorCode", required=false)
	public void setErrorCode(final String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	@JsonProperty(value="errorMessage", required=false)
	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
