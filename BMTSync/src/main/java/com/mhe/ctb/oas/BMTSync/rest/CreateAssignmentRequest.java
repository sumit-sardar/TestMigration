package com.mhe.ctb.oas.BMTSync.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;

public class CreateAssignmentRequest {

	public TestAssignment getTestAssignment() {
		return testAssignment;
	}
	@JsonProperty(value="testAssignment", required=true)
	public void setTestAssignment(TestAssignment testAssignment) {
		this.testAssignment = testAssignment;
	}

	
	private TestAssignment testAssignment;
	
	
	
}
