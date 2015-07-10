package com.mhe.ctb.oas.BMTSync.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.model.Student;
import com.mhe.ctb.oas.BMTSync.model.TestStatus;

/**
 * Request from BMT for a test status update.
 * @author oas
 */
public class CreateTestStatusRequest {
	// This isn't a property so there's no getter or setter.
	private static final Logger logger = Logger.getLogger(Student.class);

	private List<TestStatus> _testStatus;
	
	public CreateTestStatusRequest() {
		_testStatus = new ArrayList<TestStatus>();
	}	

	public List<TestStatus> getTestStatus() {
		return _testStatus;
	}
	
	@JsonProperty(value="teststatus", required=true)
	public void setTestStatus(List<TestStatus> testStatus) {
		_testStatus = testStatus;
	}

	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("Failure to serialize Test Status object!");
			return null;
		}
	}
	

}
