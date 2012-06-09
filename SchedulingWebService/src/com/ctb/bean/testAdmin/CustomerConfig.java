package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the contents of the OAS.CUSTOMER_CONFIGURATION table
 * 
 * @author Tata Consulency Services
 */


public class CustomerConfig extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer customerConfigurationId;
    private String customerConfigurationName;
    private Integer customerId;
    private String editable;
    private String defaultValue;
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
	 * @return Returns the customerConfigurationId.
	 */
	public Integer getCustomerConfigurationId() {
		return customerConfigurationId;
	}
	/**
	 * @param customerConfigurationId The customerConfigurationId to set.
	 */
	public void setCustomerConfigurationId(Integer customerConfigurationId) {
		this.customerConfigurationId = customerConfigurationId;
	}
	/**
	 * @return Returns the customerConfigurationName.
	 */
	public String getCustomerConfigurationName() {
		return customerConfigurationName;
	}
	/**
	 * @param customerConfigurationName The customerConfigurationName to set.
	 */
	public void setCustomerConfigurationName(String customerConfigurationName) {
		this.customerConfigurationName = customerConfigurationName;
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
	 * @return Returns the defaultValue.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue The defaultValue to set.
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return Returns the editable.
	 */
	public String getEditable() {
		return editable;
	}
	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(String editable) {
		this.editable = editable;
	}
} 
