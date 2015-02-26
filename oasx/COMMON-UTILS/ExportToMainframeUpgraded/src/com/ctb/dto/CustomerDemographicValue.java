package com.ctb.dto;

public class CustomerDemographicValue {

	private Integer customerDemographicId;
	private String valueName;
	private String valueCode;
	private CustomerDemographic customerDemographic;
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(valueName);
		return sb.toString();
		
	}
	/**
	 * @return the valueName
	 */
	public String getValueName() {
		return valueName;
	}
	/**
	 * @param valueName the valueName to set
	 */
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	/**
	 * @return the valueCode
	 */
	public String getValueCode() {
		return valueCode;
	}
	/**
	 * @param valueCode the valueCode to set
	 */
	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}
	/**
	 * @return the customerDemographicId
	 */
	public Integer getCustomerDemographicId() {
		return customerDemographicId;
	}
	/**
	 * @param customerDemographicId the customerDemographicId to set
	 */
	public void setCustomerDemographicId(Integer customerDemographicId) {
		this.customerDemographicId = customerDemographicId;
	}
	/**
	 * @return the customerDemographic
	 */
	public CustomerDemographic getCustomerDemographic() {
		return customerDemographic;
	}
	/**
	 * @param customerDemographic the customerDemographic to set
	 */
	public void setCustomerDemographic(CustomerDemographic customerDemographic) {
		this.customerDemographic = customerDemographic;
	}
	
	
}
