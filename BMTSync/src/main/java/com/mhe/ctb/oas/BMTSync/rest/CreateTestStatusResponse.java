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
import com.mhe.ctb.oas.BMTSync.model.TestStatus;


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"isSuccessful", "successCount", "failureCount", "failures"})
@JsonInclude(Include.NON_EMPTY)

public class CreateTestStatusResponse extends BaseResponse {

	// The logger shouldn't be included in the JSON.
	private static final Logger logger = Logger.getLogger(CreateTestStatusResponse.class);

	int _successCount;
	int _failureCount;
	List<TestStatus> _failures;

	
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

	public List<TestStatus> getFailures() {
		return _failures;
	}

	
	@JsonProperty("failures")
	public void setFailures(List<TestStatus> failures) {
		_failures = failures;
	}
	
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			logger.info("Failure mapping CreateTestStatusResponse to JSON.");
			return null;
		}
	}
	
	
}
