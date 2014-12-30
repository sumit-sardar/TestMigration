package com.mhe.ctb.oas.BMTSync.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveryWindow {
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
