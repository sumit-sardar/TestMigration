package com.ctb.dto;

public class RostersItem {
	private String itemId = "";
	private String itemSetIdTD = "";
	private String itemType = "";
	private String itemIndx = "";
	private String itemCorrectResponse = "";
	private String studentResponse = "";
	private String itemDescriptio = "";
	private Boolean isItemValidateForScoring = false;
	private Boolean isSubtestCompleted = false;
	
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
	
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return the itemIndx
	 */
	public String getItemIndx() {
		return itemIndx;
	}
	/**
	 * @param itemIndx the itemIndx to set
	 */
	public void setItemIndx(String itemIndx) {
		this.itemIndx = itemIndx;
	}
	/**
	 * @return the itemDescriptio
	 */
	public String getItemDescriptio() {
		return itemDescriptio;
	}
	/**
	 * @param itemDescriptio the itemDescriptio to set
	 */
	public void setItemDescriptio(String itemDescriptio) {
		this.itemDescriptio = itemDescriptio;
	}
	/**
	 * @return the itemSetIdTD
	 */
	public String getItemSetIdTD() {
		return itemSetIdTD;
	}
	/**
	 * @param itemSetIdTD the itemSetIdTD to set
	 */
	public void setItemSetIdTD(String itemSetIdTD) {
		this.itemSetIdTD = itemSetIdTD;
	}
	/**
	 * @return the itemCorrectResponse
	 */
	public String getItemCorrectResponse() {
		return itemCorrectResponse;
	}
	/**
	 * @param itemCorrectResponse the itemCorrectResponse to set
	 */
	public void setItemCorrectResponse(String itemCorrectResponse) {
		this.itemCorrectResponse = itemCorrectResponse;
	}
	/**
	 * @return the studentResponse
	 */
	public String getStudentResponse() {
		return studentResponse;
	}
	/**
	 * @param studentResponse the studentResponse to set
	 */
	public void setStudentResponse(String studentResponse) {
		this.studentResponse = studentResponse;
	}
	/**
	 * @return the itemValidationStatus
	 */
	public boolean isItemValidateForScoring() {
		return isItemValidateForScoring;
	}
	/**
	 * @param itemValidationStatus the itemValidationStatus to set
	 */
	public void setItemValidationStatusForScoring(String itemValidationStatus) {
		if(itemValidationStatus.equalsIgnoreCase("NC"))
			this.isItemValidateForScoring = false;
		else
			this.isItemValidateForScoring = true;
	}
	
	public boolean isSubtestCompleted() {
		return isSubtestCompleted;
	}
	
	public void setIsSubtestCompleted(String  isSubtestCompleted) {
		if(isSubtestCompleted.equalsIgnoreCase("NC"))
			this.isSubtestCompleted = false;
		else
			this.isSubtestCompleted = true;
	}
	
	@Override
	public String toString() {
		
		String val = "itemId["+itemId+"] itemSetIdTD:["+itemSetIdTD+"]itemType:["
		+itemType+"] itemCorrectResponse:["+itemCorrectResponse+"] studentResponse:["+studentResponse
		+"] itemDescription["+ itemDescriptio+"] isItemValidateForScoring:["+isItemValidateForScoring;
		
		
		return val;
	}

}
