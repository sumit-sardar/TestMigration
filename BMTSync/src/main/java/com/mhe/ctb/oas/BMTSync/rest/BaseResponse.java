package com.mhe.ctb.oas.BMTSync.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic response data for OAS from BMT.
 * @author oas
 */
public class BaseResponse {
	private boolean _successful;
	private String _serviceErrorCode;
	private String _serviceErrorMessage;

	public boolean isSuccessful() {
		return _successful;
	}

	@JsonProperty("isSuccessful")
	public void setSuccessful(boolean successful) {
		_successful = successful;
	}

	public String getServiceErrorCode() {
		return _serviceErrorCode;
	}

	@JsonProperty("serviceErrorCode")
	public void setErrorCode(String serviceErrorCode) {
		_serviceErrorCode = serviceErrorCode;
	}

	public String getServiceErrorMessage() {
		return _serviceErrorMessage;
	}

	@JsonProperty("serviceErrorMessage")
	public void setErrorMessage(String serviceErrorMessage) {
		_serviceErrorMessage = serviceErrorMessage;
	}

}
