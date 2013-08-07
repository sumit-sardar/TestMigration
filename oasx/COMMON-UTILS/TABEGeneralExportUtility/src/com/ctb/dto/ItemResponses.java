package com.ctb.dto;

public class ItemResponses
{
	private String itemId;
	private String responseValue;
	private String originalResponse;
	private String responseTime;
	private int index;
	private int sequenceNo;
	private String oasItemId;
	
	
	public int getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getOriginalResponse() {
		return (originalResponse == null ? " ": originalResponse);
	}
	public void setOriginalResponse(String originalResponse) {
		this.originalResponse = originalResponse;
	}
	public String getResponseTime() {
		return (responseTime == null ? " ": responseTime);
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	/**
	 * @return the responseValue
	 */
	public String getResponseValue() {
		return (responseValue == null ? " ": responseValue);
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
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ItemResponses)) {
			return false;
		}
		if(this.itemId.equals(((ItemResponses)obj).itemId)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.itemId.hashCode();
	}
	/**
	 * @return the oasItemId
	 */
	public String getOasItemId() {
		return oasItemId;
	}
	/**
	 * @param oasItemId the oasItemId to set
	 */
	public void setOasItemId(String oasItemId) {
		this.oasItemId = oasItemId;
	}
}
