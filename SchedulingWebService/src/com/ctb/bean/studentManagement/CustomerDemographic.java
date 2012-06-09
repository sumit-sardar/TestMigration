package com.ctb.bean.studentManagement; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the contents of the OAS.CUSTOMER_DEMOGRAPHIC table
 * 
 * @author John_Wang
 */


public class CustomerDemographic extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer id;
    private Integer customerId;
    private String labelName;
    private String labelCode;
    private String valueCardinality;
    private Integer sortOrder;
    private String importEditable;
    private String visible;
    private CustomerDemographicValue [] customerDemographicValues;
    
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
	 * @return the labelCode
	 */
	public String getLabelCode() {
		return labelCode;
	}
	/**
	 * @param labelCode the labelCode to set
	 */
	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}
	/**
	 * @return the valueCardinality
	 */
	public String getValueCardinality() {
		return valueCardinality;
	}
	/**
	 * @param valueCardinality the valueCardinality to set
	 */
	public void setValueCardinality(String valueCardinality) {
		this.valueCardinality = valueCardinality;
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
	/**
	 * @return the importEditable
	 */
	public String getImportEditable() {
		return importEditable;
	}
	/**
	 * @param importEditable the importEditable to set
	 */
	public void setImportEditable(String importEditable) {
		this.importEditable = importEditable;
	}
	/**
	 * @return the visible
	 */
	public String getVisible() {
		return visible;
	}
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(String visible) {
		this.visible = visible;
	}
	/**
	 * @return the customerDemographicValues
	 */
	public CustomerDemographicValue[] getCustomerDemographicValues() {
		return customerDemographicValues;
	}
	/**
	 * @param customerDemographicValues the customerDemographicValues to set
	 */
	public void setCustomerDemographicValues(
			CustomerDemographicValue[] customerDemographicValues) {
		this.customerDemographicValues = customerDemographicValues;
	}
    
	/**
	 * @return the multipleAllowedFlag
	 */
    public String getMultipleAllowedFlag() {
        if (this.valueCardinality.equalsIgnoreCase("MULTIPLE"))
            return "true";
        else
            return "false";
        
    }
    
	/**
	 * @param multipleAllowedFlag the multipleAllowedFlag to set
	 */
    public void setMultipleAllowedFlag(String multipleAllowedFlag) {
        //have settter just to avoid compile error (marshall/unmarshall XML)
    }
    
} 

