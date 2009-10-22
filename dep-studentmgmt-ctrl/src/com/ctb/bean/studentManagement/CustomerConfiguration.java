package com.ctb.bean.studentManagement; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the contents of the OAS.CUSTOMER_CONFIGURATION table
 * 
 * @author John_Wang
 */


public class CustomerConfiguration extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer id;
    private Integer customerId;
    private String customerConfigurationName;
    private String editable;
    private String defaultValue;
    private CustomerConfigurationValue [] customerConfigurationValues;
    
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
    /**
	 * @return Returns the customerId.
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId The customerId to set.
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the customerConfigurationName
	 */
	public String getCustomerConfigurationName() {
		return customerConfigurationName;
	}
	/**
	 * @param customerConfigurationName the customerConfigurationName to set
	 */
	public void setCustomerConfigurationName(String customerConfigurationName) {
		this.customerConfigurationName = customerConfigurationName;
	}
	/**
	 * @return the editable
	 */
	public String getEditable() {
		return editable;
	}
	/**
	 * @param editable the editable to set
	 */
	public void setEditable(String editable) {
		this.editable = editable;
	}
	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return the customerConfigurationValues
	 */
	public CustomerConfigurationValue[] getCustomerConfigurationValues() {
		return customerConfigurationValues;
	}
	/**
	 * @param customerConfigurationValues the customerConfigurationValues to set
	 */
	public void setCustomerConfigurationValues(
			CustomerConfigurationValue[] customerConfigurationValues) {
		this.customerConfigurationValues = customerConfigurationValues;
	}
} 

