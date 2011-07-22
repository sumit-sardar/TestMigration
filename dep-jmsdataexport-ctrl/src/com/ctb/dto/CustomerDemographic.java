package com.ctb.dto;

import java.util.HashSet;
import java.util.Set;

public class CustomerDemographic {

	private Integer customerId;
	private Integer customerDemographicId;
	private String labelName;
	private Set<CustomerDemographicValue> customerDemographicValue = new HashSet<CustomerDemographicValue>();
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(labelName);
		return sb.toString();
		
	}
	
	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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
	 * @return the labelName
	 */
	public String getLabelName() {
		return labelName;
	}
	/**
	 * @param labelName the labelName to set
	 */
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	/**
	 * @return the customerDemographicValue
	 */
	public Set<CustomerDemographicValue> getCustomerDemographicValue() {
		return customerDemographicValue;
	}
	/**
	 * @param customerDemographicValue the customerDemographicValue to set
	 */
	public void setCustomerDemographicValue(
			Set<CustomerDemographicValue> customerDemographicValue) {
		this.customerDemographicValue = customerDemographicValue;
	}
	
	public boolean equals( Object obj )	{
		if(obj==null)
			return false;
		else {
			CustomerDemographic cusDemo = (CustomerDemographic)obj;
			return this.getCustomerDemographicId().equals(cusDemo.getCustomerDemographicId()) ;
		}
		
	}
	
	public int hashCode()	{		
		return this.getCustomerDemographicId();	
		}
	
	
}
