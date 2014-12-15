package com.mhe.ctb.oas.BMTSync.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TestAssignment {
	
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
	

	public static class Parameters {
		private String enforceBreak;
		private String enforceTutorial; 

		public String getEnforceBreak() {
			return enforceBreak;
		}
		@JsonProperty(value="enforceBreak", required=true)
		public void setEnforceBreak(String enforceBreak) {
			this.enforceBreak = enforceBreak;
		}

		public String getEnforceTutorial() {
			return enforceTutorial;
		}
		@JsonProperty(value="enforceTutorial", required=true)
		public void setEnforceTutorial(String enforceTutorial) {
			this.enforceTutorial = enforceTutorial;
		}
		
	}


	
    /* Getter and Setter Methods */
	public DeliveryWindow getDeliveryWindow() {
		return deliveryWindow;
	}	
	@JsonProperty(value="deliveryWindow", required=true)
	public void setDeliveryWindow(DeliveryWindow deliveryWindow) {
		this.deliveryWindow = deliveryWindow;
	}	
	
	public Parameters getParameters() {
		return parameters;
	}
	@JsonProperty(value="parameters", required=true)	
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
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
	
	public List<StudentRoster> getRoster() {
		return roster;
	}
	@JsonProperty(value="roaster", required=true)
	public void setRoster(List<StudentRoster> rosterList) {
		this.roster = rosterList;
	}
	
	public Integer getErrorCode() {
		return _errorCode;
	}
	@JsonProperty(value="errorCode", required=true)
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
