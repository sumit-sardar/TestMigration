package com.ctb.bean.studentManagement; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the contents of the OAS.CUSTOMER_DEMOGRAPHIC table
 * 
 * @author John_Wang
 */


public class CustomerProgramGoal extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer customerPrgGoalId;
    private Integer customerId;
    private String labelName;
    private String labelCode;
    private String valueCardinality;
    private Integer sortOrder;
    private String visible;
    private String sectionName;
    private String importEditable;
    
    private CustomerProgramGoalValue [] customerProgramGoalValues;
    
	
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

	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	/**
	 * @return the customerProgramGoalValues
	 */
	public CustomerProgramGoalValue[] getCustomerProgramGoalValues() {
		return customerProgramGoalValues;
	}
	/**
	 * @param customerProgramGoalValues the customerProgramGoalValues to set
	 */
	public void setCustomerProgramGoalValues(
			CustomerProgramGoalValue[] customerProgramGoalValues) {
		this.customerProgramGoalValues = customerProgramGoalValues;
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
	 * @return the customerPrgGoalId
	 */
	public Integer getCustomerPrgGoalId() {
		return customerPrgGoalId;
	}
	/**
	 * @param customerPrgGoalId the customerPrgGoalId to set
	 */
	public void setCustomerPrgGoalId(Integer customerPrgGoalId) {
		this.customerPrgGoalId = customerPrgGoalId;
	}
    
} 

