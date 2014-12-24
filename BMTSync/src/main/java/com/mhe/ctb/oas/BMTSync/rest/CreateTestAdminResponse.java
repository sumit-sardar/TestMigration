package com.mhe.ctb.oas.BMTSync.rest;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.model.TestAdmin;


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"isSuccessful", "successCount", "failureCount", "failures"})
@JsonInclude(Include.NON_EMPTY)

public class CreateTestAdminResponse {
	
	private static final Logger logger = Logger.getLogger(CreateTestAdminResponse.class);
	
	int _successCount;
	int _failureCount;
	List<TestAdmin> _failures;
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

	public List<TestAdmin> getFailures() {
		return _failures;
	}

	
	@JsonProperty("failures")
	public void setFailures(List<TestAdmin> failures) {
		_failures = failures;
	}
	
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("Failure to serialize Test Assignment response object");
			return null;
		}
	}	
	

}
