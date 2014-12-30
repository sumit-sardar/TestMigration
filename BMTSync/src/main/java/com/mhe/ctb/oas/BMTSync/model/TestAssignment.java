package com.mhe.ctb.oas.BMTSync.model;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TestAssignment {
	private static final Logger logger = Logger.getLogger(TestAssignment.class);
	
	private Integer oasTestAdministrationID; 
	private Integer oasCustomerId;
	private Integer oasTestCatalogId;
	private String name;
	private String productName;
	private DeliveryWindow deliveryWindow;
	private Parameters parameters;
	private List<StudentRoster> roster;
	private Integer _errorCode;
	private String _errorMessage; 	
	
    /* Getter and Setter Methods */
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
	
	public List<StudentRoster> getRoster() {
		return roster;
	}
	
	@JsonProperty(value="roaster", required=false)
	public void setRoster(List<StudentRoster> rosterList) {
		this.roster = rosterList;
	}
	
	public Integer getErrorCode() {
		return _errorCode;
	}
	
	@JsonProperty(value="errorCode", required=false)
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
			logger.error("Failure to serialize Test Assignment object! [Test Admin ID=" + oasTestAdministrationID + "]");
			return null;
		}
	}
	
	
	/*
    Example of Assignment JSON
    {
	    "oasTestAdministrationID": 12345,
	    "oasCustomerId": 12345,
	    "oasTestCatalogId": 1234324,
	    "name": "Spring 2015 General 4th Grade Math",
	    "productName": "ISTEP Applied Skills ELA and Math Book IIs",
	    "deliveryWindow": {
	        "startDate": "2014-08-22",
	        "startHour": "08:00",
	        "endDate": "2014-08-22",
	        "endHour": "17:00"
	    },
	    "parameters": {
	        "enforceBreak": false,
	        "enforceTutorial": false
	    },
	    "roaster": [
	        {
	            "oasRoasterId": "56678",
	            "oasStudentId": "1234",
	            "studentpassword": "c6ee37720236bfb0f6c69712cb5d5f48",
	            "parts": [
	                {
	                    "deliverystatus": "SC",
	                    "accessCode": "ALPHABRAVO",
	                    "oasItemSetId": "13245",
	                    "oasTestId": "BMT_OK_A1_S14_AlgI_SU1_DU1",
	                    "oasTestName": "ACE Algebra I Oklahoma Item Tryout Test",
	                    "enforceTimeLimit": {
	                        "IsRequiured": "True",
	                        "TimeLimitInMins": "30"
	                    },
	                    "order": 1
	                },
	                {
	                    "deliverystatus": "SC",
	                    "accessCode": "ALPHABRAVO",
	                    "oasItemSetId": "132456",
	                    "oasTestId": "BMT_OK_A1_S14_AlgII_SU1_DU1",
	                    "oasTestName": "ACE Algebra II Oklahoma Item Tryout Test",
	                    "enforceTimeLimit": {
	                        "IsRequiured": "True",
	                        "TimeLimitInMins": "30"
	                    },
	                    "order": 2
	                }
	            ]
	        }
	    ]
	}
*/	 
	 
	 
	
	
}
