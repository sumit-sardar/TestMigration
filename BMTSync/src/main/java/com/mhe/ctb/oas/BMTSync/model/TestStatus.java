package com.mhe.ctb.oas.BMTSync.model;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"oasRosterId", "oasTestId", "deliveryStatus",
	"startedDate","completedDate"})

public class TestStatus {
	static private Logger logger = Logger.getLogger(TestStatus.class);
	
	private int oasRosterId;
	private String oasTestId;
	private String deliveryStatus;
	private String startedDate;
	private String completedDate;
	private Integer _errorCode;
	private String _errorMessage; 	
	
	public int getOasRosterId() {
		return oasRosterId;
	}
	@JsonProperty(value="oasRosterId")
	public void setOasRosterId(int oasRosterId) {
		this.oasRosterId = oasRosterId;
	}
	public String getOasTestId() {
		return oasTestId;
	}
	@JsonProperty(value="oasTestId")
	public void setOasTestId(String oasTestId) {
		this.oasTestId = oasTestId;
	}
	
	
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	@JsonProperty(value="deliverystatus")
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	
	public String getStartedDate() {
		return startedDate;
	}
	@JsonProperty(value="startedDate")
	public void setStartedDate(String startedDate) {
		this.startedDate = startedDate;
	}
	
	
	public String getCompletedDate() {
		return completedDate;
	}
	@JsonProperty(value="completedDate")
	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}
	
	public Integer getErrorCode() {
		return _errorCode;
	}
	
	@JsonProperty(value="errorCode")
	public void setErrorCode(Integer errorCode) {
		_errorCode = errorCode;
	}
	
	public String getErrorMessage() {
		return _errorMessage;
	}

	@JsonProperty("errorMessage")
	public void setErrorMessage(String errorMessage) {
		_errorMessage = errorMessage;
	}
	
	
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("Failure to serialize Test Status object! [Test Roster ID=" + oasRosterId + "]");
			return null;
		}
	}
	
	public String toString() {
		
		return "oasRosterId: "+this.oasRosterId+
			", oasTestId: "+this.oasTestId+
			", deliveryStatus: "+this.deliveryStatus+
			", startedDate: "+this.startedDate+
			", completedDate: "+this.completedDate;
	}
	

}
