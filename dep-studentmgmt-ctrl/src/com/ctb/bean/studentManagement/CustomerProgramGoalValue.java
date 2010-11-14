package com.ctb.bean.studentManagement; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the contents of the OAS.CUSTOMER_DEMOGRAPHIC_VALUE table
 * 
 * @author John_Wang
 */


public class CustomerProgramGoalValue extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer customerPrgGoalId;
    private String valueName;
    private String valueCode;
    private Integer sortOrder;
    private String visible;
    private String sectionName;

	
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
	
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
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


