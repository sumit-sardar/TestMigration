package com.mhe.ctb.oas.BMTSync.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;

public class CreateAssignmentRequest {
	private static final Logger logger = Logger.getLogger(CreateAssignmentRequest.class);
	
	private List<TestAssignment> _testAssignments;
	
	public CreateAssignmentRequest() {
		_testAssignments = new ArrayList<TestAssignment>();
	}
	
	public List<TestAssignment> getTestAssignments() {
		return _testAssignments;
	}
	
	@JsonProperty(value="testAssignments", required=true)
	public void addTestAssignment(TestAssignment testAssignment) {
		_testAssignments.add(testAssignment);
	}

	@JsonProperty(value="testAssignments", required=true)
	public void addTestAssignments(List<TestAssignment> testAssignments) {
		_testAssignments.addAll(testAssignments);
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
