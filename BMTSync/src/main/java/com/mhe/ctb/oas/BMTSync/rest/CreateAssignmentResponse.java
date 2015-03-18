package com.mhe.ctb.oas.BMTSync.rest;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.model.DeliveryWindow;
import com.mhe.ctb.oas.BMTSync.model.Parameters;
import com.mhe.ctb.oas.BMTSync.model.StudentRosterResponse;

/**
 * Response from BMT to synch assignments.
 * @author oas
 */
@JsonIgnoreProperties(ignoreUnknown=true)
//@JsonPropertyOrder({"isSuccessful", "successCount", "failureCount", "errorCode", "errorMessage", "failures"})
@JsonInclude(Include.NON_EMPTY)
public class CreateAssignmentResponse extends BaseResponse {
	private static final Logger logger = Logger.getLogger(CreateAssignmentResponse.class);
	
	private Integer oasTestAdministrationID; 
	private Integer oasCustomerId;
	private Integer oasTestCatalogId;
	private String name;
	private String productName;
	private DeliveryWindow deliveryWindow;
	private Parameters parameters;
	private int _successCount;
	private int _failureCount;
	private List<StudentRosterResponse> _failures;

	public Integer getOasTestAdministrationID() {
		return oasTestAdministrationID;
	}

	@JsonProperty(value="oasTestAdministrationID", required=false)
	public void setOasTestAdministrationID(Integer oasTestAdministrationID) {
		this.oasTestAdministrationID = oasTestAdministrationID;
	}

	public Integer getOasCustomerId() {
		return oasCustomerId;
	}

	@JsonProperty(value="oasCustomerId", required=false)
	public void setOasCustomerId(Integer oasCustomerId) {
		this.oasCustomerId = oasCustomerId;
	}

	public Integer getOasTestCatalogId() {
		return oasTestCatalogId;
	}

	@JsonProperty(value="oasTestCatalogId", required=false)
	public void setOasTestCatalogId(Integer oasTestCatalogId) {
		this.oasTestCatalogId = oasTestCatalogId;
	}

	public String getName() {
		return name;
	}

	@JsonProperty(value="name", required=false)
	public void setName(String name) {
		this.name = name;
	}

	public String getProductName() {
		return productName;
	}

	@JsonProperty(value="productName", required=false)
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public DeliveryWindow getDeliveryWindow() {
		return deliveryWindow;
	}

	@JsonProperty(value="deliveryWindow", required=false)
	public void setDeliveryWindow(DeliveryWindow deliveryWindow) {
		this.deliveryWindow = deliveryWindow;
	}

	public Parameters getParameters() {
		return parameters;
	}

	@JsonProperty(value="parameters", required=false)	
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public int getSuccessCount() {
		return _successCount;
	}

	@JsonProperty(value="successCount", required=true)
	public void setSuccessCount(int successCount) {
		_successCount = successCount;
	}

	public int getFailureCount() {
		return _failureCount;
	}

	@JsonProperty(value="failureCount", required=true)
	public void setFailureCount(int failureCount) {
		_failureCount = failureCount;
	}

	public List<StudentRosterResponse> getFailures() {
		return _failures;
	}
	
	@JsonProperty(value="failures", required=false)
	public void setFailures(List<StudentRosterResponse> failures) {
		_failures = failures;
	}

	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("Failure to serialize Test Assignment response object");
			return null;
		}
	}	
	

}
