package com.mhe.ctb.oas.BMTSync.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"isSuccessful", "successCount", "failureCount", "failures"})
@JsonInclude(Include.NON_EMPTY)
public class CreateAssignmentResponse extends BaseResponse {
	
	int _successCount;
	int _failureCount;
	List<TestAssignment> _failures;
	//int _serviceErrorCode;
	//String _serviceErrorMessage;

	public int getSuccessCount() {
		return _successCount;
	}

	@JsonProperty("successCount")
	public void setSuccessCount(int successCount) {
		_successCount = successCount;
	}

	public int getFailureCount() {
		return _failureCount;
	}

	@JsonProperty("failureCount")
	public void setFailureCount(int failureCount) {
		_failureCount = failureCount;
	}

	public List<TestAssignment> getFailures() {
		return _failures;
	}

	
	@JsonProperty("failures")
	public void setFailures(List<TestAssignment> failures) {
		_failures = failures;
	}
	

}
