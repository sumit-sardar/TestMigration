package com.mhe.ctb.oas.BMTSync.rest;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.model.ItemResponse;

/**
 * Request to BMT to fetch item responses for an opaque (assignment) ID.
 * @author oas
 */
public class CreateItemResponsesResponse {
	// This isn't a property so there's no getter or setter.
	private static final Logger logger = Logger.getLogger(CreateItemResponsesResponse.class);
	
	private Long assignmentId;
	private List<ItemResponse> itemResponses;

	public Long getAssignmentId() {
		return assignmentId;
	}
	@JsonProperty(value="assignmentId", required=false)
	public void setAssignmentId(final Long assignmentId) {
		this.assignmentId = assignmentId;
	}
	
	public List<ItemResponse> getItemResponses() {
		return itemResponses;
	}
	@JsonProperty(value="itemResponses", required=true)
	public void setItemResponses(final List<ItemResponse> itemResponses) {
		this.itemResponses = itemResponses;
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

/*

{
	"assignmentId": 101,
	"itemResponses": [
		{
    		"itemCode": "BMTTESTNOTREAL12345",
    		"itemType": "MC",
    		"itemResponse": "A",
    		"itemResponseTime": 4
		},
		{
    		"itemCode": "BMTTESTNOTREAL12345",
    		"itemType": "CR",
    		"itemResponse": "The quick brown fox jumped over the lazy dog.",
    		"itemResponseTime": 19
		},
		{
    		"itemCode": "BMTTESTNOTREAL12345",
    		"itemType": "AU",
    		"itemResponse": "https://us-west-2.s3.amazonaws.com/bmt-buckets/bmttestnotreal12345/student-response-67890.mp3",
    		"itemResponseTime": 37
		}
	]
}

*/
