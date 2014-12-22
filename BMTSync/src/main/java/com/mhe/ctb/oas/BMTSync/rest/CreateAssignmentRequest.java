package com.mhe.ctb.oas.BMTSync.rest;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;

public class CreateAssignmentRequest {
	private static final Logger logger = Logger.getLogger(CreateAssignmentRequest.class);
	
	private TestAssignment testAssignment;
	
	
	public TestAssignment getTestAssignment() {
		return testAssignment;
	}
	@JsonProperty(value="testAssignment", required=true)
	public void setTestAssignment(TestAssignment testAssignment) {
		this.testAssignment = testAssignment;
	}

	
	
	
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("Failure to serialize Test Assignment request object");
			return null;
		}
	}	
	
	
}
