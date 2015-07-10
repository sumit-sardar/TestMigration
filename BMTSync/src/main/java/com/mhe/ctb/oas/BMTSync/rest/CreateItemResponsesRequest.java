package com.mhe.ctb.oas.BMTSync.rest;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Request to BMT to fetch item responses for an opaque (assignment) ID.
 * @author oas
 */
public class CreateItemResponsesRequest {
	// This isn't a property so there's no getter or setter.
	private static final Logger logger = Logger.getLogger(CreateItemResponsesRequest.class);
	
	private Long assignmentId;

	public Long getAssignmentId() {
		return assignmentId;
	}
	@JsonProperty(value="assignmentId", required=true)
	public void setAssignmentId(final Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("Failure to serialize CreateItemResponseRequest object!");
			return null;
		}
	}
}
