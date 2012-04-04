package com.ctb.dto;


public class ItemResponses
{
	private String itemId;
	private String itemOrder;
	private String responseValue;
	private String firstVisitTime;
	private String totalTime;
	
	
	public String getItemOrder() {
		return itemOrder;
	}
	public void setItemOrder(String itemOrder) {
		this.itemOrder = itemOrder;
	}
	public String getFirstVisitTime() {
		return firstVisitTime;
	}
	public void setFirstVisitTime(String firstVisitTime) {
		this.firstVisitTime = firstVisitTime;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	/**
	 * @return the responseValue
	 */
	public String getResponseValue() {
		return responseValue;
	}
	/**
	 * @param responseValue the responseValue to set
	 */
	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
}
