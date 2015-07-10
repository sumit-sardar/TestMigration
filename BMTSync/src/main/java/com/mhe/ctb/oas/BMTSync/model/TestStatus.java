package com.mhe.ctb.oas.BMTSync.model;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to describe a test status
 * @author oas
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"oasRosterId", "oasTestId", "deliveryStatus",
	"startedDate","completedDate"})
public class TestStatus {
	static private Logger logger = Logger.getLogger(TestStatus.class);
	
	private Integer oasCustomerId;
	private Integer oasRosterId;
	private String oasTestId;
	private String deliveryStatus;
	private Long assignmentId;
	private String startedDate;
	private String completedDate;
	private Integer _errorCode;
	private String _errorMessage; 	
	
	public Integer getOasCustomerId() {
		return oasCustomerId;
	}
	@JsonProperty(value="oasCustomerId", required=false)
	public void setOasCustomerId(Integer oasCustomerId) {
		this.oasCustomerId = oasCustomerId;
	}
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
	
	
	public Long getAssignmentId() {
		return assignmentId;
	}
	
	@JsonProperty(value="assignmentId")
	public void setAssignmentId(final Long assignmentId) {
		this.assignmentId = assignmentId;
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
	
	public Integer getValidatedCustomerId() {
		// Per Prem, customer ID can be zero if there's an error in BMT; this is a validator function that will convert zero to null for
		// other code.
		if (oasCustomerId == null) {
			return null;
		}
		if (oasCustomerId == 0) {
			return null;
		}
		return oasCustomerId;
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
			", assignmentId: "+this.assignmentId+
			", startedDate: "+this.startedDate+
			", completedDate: "+this.completedDate;
	}
}
