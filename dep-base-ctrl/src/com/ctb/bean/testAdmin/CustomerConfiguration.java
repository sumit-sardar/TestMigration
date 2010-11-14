package com.ctb.bean.testAdmin; 

import java.util.Date;

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
    private Integer createdBy;
    private Date createdDateTime;
    
    /**
	 * @return Returns the createdBy.
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return Returns the createdDateTime.
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime The createdDateTime to set.
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
    
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
	 * @param customer Id. The customerId to set.
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


