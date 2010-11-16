package com.ctb.bean.studentManagement; 

/**
 * Data bean representing the contents of the OAS.CUSTOMER_DEMOGRAPHIC table 
 * and student selection on demographic values
 * 
 * @author John_Wang
 */


public class StudentOtherDetail 
{ 
	  static final long serialVersionUID = 1L;
	private Integer studentId;
    private StudentOtherDetailValue [] studentOtherDetailValues;
    private String valueName;
    private String valueCode;
    private Integer sortOrder;
    private String visible;
    private String sectionName;
    private String labelName;
    private String valueCardinality;
    private String multipleAllowedFlag;
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

	public StudentOtherDetail() {
      
     }
    
   

	/**
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
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
	 * @return the multipleAllowedFlag
	 */
	public String getMultipleAllowedFlag() {
		return multipleAllowedFlag;
	}

	/**
	 * @param multipleAllowedFlag the multipleAllowedFlag to set
	 */
	public void setMultipleAllowedFlag(String multipleAllowedFlag) {
		this.multipleAllowedFlag = multipleAllowedFlag;
	}

	/**
	 * @return the studentOtherDetailValues
	 */
	public StudentOtherDetailValue[] getStudentOtherDetailValues() {
		return studentOtherDetailValues;
	}

	/**
	 * @param studentOtherDetailValues the studentOtherDetailValues to set
	 */
	public void setStudentOtherDetailValues(
			StudentOtherDetailValue[] studentOtherDetailValues) {
		this.studentOtherDetailValues = studentOtherDetailValues;
	}
    
    
} 
