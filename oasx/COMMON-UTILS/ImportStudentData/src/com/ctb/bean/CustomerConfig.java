package com.ctb.bean;

import java.util.Date;

/**
 * This class represents the Data of Customer-Configuration Table
 * 
 * @author TCS
 * 
 */
public class CustomerConfig extends CTBBean {
	static final long serialVersionUID = 1L;
	private Integer customerConfigurationId;
	private String customerConfigurationName;
	private Integer customerId;
	private String editable;
	private String defaultValue;
	private Integer createdBy;
	private Date createdDateTime;

	/**
	 * @return the customerConfigurationId
	 */
	public Integer getCustomerConfigurationId() {
		return customerConfigurationId;
	}

	/**
	 * @param customerConfigurationId
	 *            the customerConfigurationId to set
	 */
	public void setCustomerConfigurationId(Integer customerConfigurationId) {
		this.customerConfigurationId = customerConfigurationId;
	}

	/**
	 * @return the customerConfigurationName
	 */
	public String getCustomerConfigurationName() {
		return customerConfigurationName;
	}

	/**
	 * @param customerConfigurationName
	 *            the customerConfigurationName to set
	 */
	public void setCustomerConfigurationName(String customerConfigurationName) {
		this.customerConfigurationName = customerConfigurationName;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the editable
	 */
	public String getEditable() {
		return editable;
	}

	/**
	 * @param editable
	 *            the editable to set
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
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDateTime
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	/**
	 * @param createdDateTime
	 *            the createdDateTime to set
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

}
