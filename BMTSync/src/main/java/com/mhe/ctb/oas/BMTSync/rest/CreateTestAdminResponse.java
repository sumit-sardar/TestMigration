package com.mhe.ctb.oas.BMTSync.rest;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"isSuccessful", "successCount", "failureCount", "failures"})
@JsonInclude(Include.NON_EMPTY)

public class CreateTestAdminResponse {
	
	private static final Logger logger = Logger.getLogger(CreateTestAdminResponse.class);
	
	int _successCount;
	int _failureCount;
	private Integer oasTestAdministrationID; 
	private Integer oasCustomerId;
	private Integer oasTestCatalogId;
	private String name;
	private String productName;
	private DeliveryWindow deliveryWindow;
	private Integer _errorCode;
	private String _errorMessage;	

	
	/* Static Inner Classes */
	public static class DeliveryWindow {
		private String startDate;
		private String startHour;
		private String endDate;
		private String endHour;
		
		public String getStartDate() {
			return startDate;
		}
		@JsonProperty(value="startDate", required=true)
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		
		public String getStartHour() {
			return startHour;
		}
		@JsonProperty(value="startHour", required=true)
		public void setStartHour(String startHour) {
			this.startHour = startHour;
		}
		
		
		public String getEndDate() {
			return endDate;
		}
		@JsonProperty(value="endDate", required=true)
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		
		
		public String getEndHour() {
			return endHour;
		}
		@JsonProperty(value="endHour", required=true)
		public void setEndHour(String endHour) {
			this.endHour = endHour;
		}
	}

	

	public int getSuccessCount() {
		return _successCount;
	}

	@JsonProperty("successCount")
	public void setSuccessCount(int successCount) {
		_successCount = successCount;
	}

	public int getFailureCount() {
		return _failureCount;
	}

	@JsonProperty("failureCount")
	public void setFailureCount(int failureCount) {
		_failureCount = failureCount;
	}


    /* Getter and Setter Methods */
	public DeliveryWindow getDeliveryWindow() {
		return deliveryWindow;
	}	
	@JsonProperty(value="deliveryWindow", required=true)
	public void setDeliveryWindow(DeliveryWindow deliveryWindow) {
		this.deliveryWindow = deliveryWindow;
	}	

	public Integer getOasTestAdministrationID() {
		return oasTestAdministrationID;
	}
	@JsonProperty(value="oasTestAdministrationID", required=true)
	public void setOasTestAdministrationID(Integer oasTestAdministrationID) {
		this.oasTestAdministrationID = oasTestAdministrationID;
	}
	
	
	public Integer getOasCustomerId() {
		return oasCustomerId;
	}
	@JsonProperty(value="oasCustomerId", required=true)
	public void setOasCustomerId(Integer oasCustomerId) {
		this.oasCustomerId = oasCustomerId;
	}
	
	public Integer getOasTestCatalogId() {
		return oasTestCatalogId;
	}
	@JsonProperty(value="oasTestCatalogId", required=true)
	public void setOasTestCatalogId(Integer oasTestCatalogId) {
		this.oasTestCatalogId = oasTestCatalogId;
	}
	
	
	public String getName() {
		return name;
	}
	@JsonProperty(value="name", required=true)
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getProductName() {
		return productName;
	}
	@JsonProperty(value="productName", required=true)
	public void setProductName(String productName) {
		this.productName = productName;
	}
	

	public Integer getErrorCode() {
		return _errorCode;
	}
	@JsonProperty("errorCode")
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
			logger.error("Failure to serialize Test Assignment response object");
			return null;
		}
	}	
	

}
