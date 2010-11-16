package com.ctb.bean.studentManagement; 

/**
 * Data bean representing the contents of the OAS.CUSTOMER_DEMOGRAPHIC_VALUE table 
 * and student selection on demographic values
 * 
 * @author John_Wang
 */



public class StudentOtherDetailValue 
{ 
    static final long serialVersionUID = 1L;
    private String selectedFlag;
    private String valueName;
    private String valueCode;
    private Integer sortOrder;
    private String visible;
    private String sectionName;
    private String labelName;

     public StudentOtherDetailValue() {
        
     }
    
	/**
	 * @return the selectedFlag
	 */
	public String getSelectedFlag() {
		return selectedFlag;
	}
	/**
	 * @param selectedFlag the selectedFlag to set
	 */
	public void setSelectedFlag(String selectedFlag) {
		this.selectedFlag = selectedFlag;
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
	 * @return the sectionName
	 */
	public String getSectionName() {
		return sectionName;
	}

	/**
	 * @param sectionName the sectionName to set
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
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
    
    
    
} 
