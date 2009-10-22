package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the contents of the OAS.CUSTOMER_CONFIGURATION_VALUE table
 * 
 * @author John_Wang
 */


public class CustomerConfigurationValue extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer customerConfigurationId;
    private String customerConfigurationValue;
    private Integer sortOrder;
	/**
	 * @return the customerConfigurationId
	 */
	public Integer getCustomerConfigurationId() {
		return customerConfigurationId;
	}
	/**
	 * @param customerConfigurationId the customerConfigurationId to set
	 */
	public void setCustomerConfigurationId(Integer customerConfigurationId) {
		this.customerConfigurationId = customerConfigurationId;
	}
	/**
	 * @return the customerConfigurationValue
	 */
	public String getCustomerConfigurationValue() {
		return customerConfigurationValue;
	}
	/**
	 * @param customerConfigurationValue the customerConfigurationValue to set
	 */
	public void setCustomerConfigurationValue(String customerConfigurationValue) {
		this.customerConfigurationValue = customerConfigurationValue;
	}
	/**
	 * @return the sortOrder
	 */
	public Integer getSortOrder() {
		return sortOrder;
	}
	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
    
} 



