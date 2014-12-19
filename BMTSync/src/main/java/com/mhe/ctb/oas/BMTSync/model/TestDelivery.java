package com.mhe.ctb.oas.BMTSync.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TestDelivery {
	private String deliverystatus;
	private String accessCode;
	private Integer oasItemSetId; 
	private String oasTestId;
	private String oasSubTestName;
	//private String isRequired;   // Time_Limit required
	//private Integer TimeLimitInMins;
	private Integer order;
	private EnforceTimeLimit enforceTimeLimit;
	
	
	public static class EnforceTimeLimit {
		private String isRequired;   // Time_Limit required
		private Integer timeLimitInMins;

		public String getIsRequired() {
			return isRequired;
		}
		@JsonProperty(value="isRequired", required=true)
		public void setIsRequired(String isRequired) {
			this.isRequired = isRequired;
		}

		public Integer getTimeLimitInMins() {
			return timeLimitInMins;
		}
		@JsonProperty(value="timeLimitInMins", required=true)
		public void setTimeLimitInMins(Integer timeLimitInMins) {
			this.timeLimitInMins = timeLimitInMins;
		}
		
	}



	
	public String getDeliverystatus() {
		return deliverystatus;
	}
	@JsonProperty(value="deliverystatus", required=true)
	public void setDeliverystatus(String deliverystatus) {
		this.deliverystatus = deliverystatus;
	}
	
	
	public String getAccessCode() {
		return accessCode;
	}
	@JsonProperty(value="accessCode", required=true)
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	
	
	public Integer getOasItemSetId() {
		return oasItemSetId;
	}
	@JsonProperty(value="oasItemSetId", required=true)
	public void setOasItemSetId(Integer oasItemSetId) {
		this.oasItemSetId = oasItemSetId;
	}
	
	
	public String getOasTestId() {
		return oasTestId;
	}
	@JsonProperty(value="oasTestId", required=true)
	public void setOasTestId(String oasTestId) {
		this.oasTestId = oasTestId;
	}
	
	
	public String getOasSubTestName() {
		return oasSubTestName;
	}
	@JsonProperty(value="oasTestName", required=true)
	public void setOasSubTestName(String oasSubTestName) {
		this.oasSubTestName = oasSubTestName;
	}
	
	public Integer getOrder() {
		return order;
	}
	@JsonProperty(value="order", required=true)
	public void setOrder(Integer order) {
		this.order = order;
	}
	

	public EnforceTimeLimit getEnforceTimeLimit() {
		return enforceTimeLimit;
	}
	@JsonProperty(value="enforceTimeLimit", required=true)	
	public void setEnforceTimeLimit(EnforceTimeLimit enforceTimeLimit) {
		this.enforceTimeLimit = enforceTimeLimit;
	}
	
	
	
}
