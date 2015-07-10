package com.mhe.ctb.oas.BMTSync.model;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to describe a test assignment.
 * @author oas
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ItemResponse {
	private static final Logger logger = Logger.getLogger(ItemResponse.class);
	private Integer itemCode;
	private String itemType;
	private String itemResponse;
	private Integer itemResponseTime;
	
    /* Getter and Setter Methods */
	public Integer getItemCode() {
		return itemCode;
	}
	@JsonProperty(value="itemCode", required=true)
	public void setItemCode(final Integer itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemType() {
		return itemType;
	}
	@JsonProperty(value="itemType", required=true)
	public void setItemType(final String itemType) {
		this.itemType = itemType;
	}

	public String getItemResponse() {
		return itemResponse;
	}
	@JsonProperty(value="itemResponse", required=true)
	public void setItemResponse(final String itemResponse) {
		this.itemResponse = itemResponse;
	}

	public Integer getItemResponseTime() {
		return itemResponseTime;
	}
	@JsonProperty(value="itemResponseTime", required=true)
	public void setItemResponseTime(final Integer itemResponseTime) {
		this.itemResponseTime = itemResponseTime;
	}
	
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("Failure to serialize ItemResponse object! [itemCode=" + itemCode + "]");
			return null;
		}
	}
	
	
	/*
    Examples of Item Response JSON
    {
	    "itemCode": "BMTTESTNOTREAL12345,
	    "itemType": "MC",
	    "itemResponse": "A",
	    "itemResponseTime": 4,
	}

    {
	    "itemCode": "BMTTESTNOTREAL12345,
	    "itemType": "CR",
	    "itemResponse": "The quick brown fox jumped over the lazy dog.",
	    "itemResponseTime": 19,
	}

    {
	    "itemCode": "BMTTESTNOTREAL12345,
	    "itemType": "AU",
	    "itemResponse": "https://us-west-2.s3.amazonaws.com/bmt-buckets/bmttestnotreal12345/student-response-67890.mp3",
	    "itemResponseTime": 37,
	}
*/	 
	 
	 
	
	
}
