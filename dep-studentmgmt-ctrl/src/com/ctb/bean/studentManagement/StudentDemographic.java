package com.ctb.bean.studentManagement; 

/**
 * Data bean representing the contents of the OAS.CUSTOMER_DEMOGRAPHIC table 
 * and student selection on demographic values
 * 
 * @author John_Wang
 */


public class StudentDemographic extends CustomerDemographic
{ 
    private Integer studentId;
    private StudentDemographicValue [] studentDemographicValues;
    private Integer orgNodeId;//bulk accommodation
    private String importEditable;
    
     public StudentDemographic() {
        super();
     }
    
    public StudentDemographic(CustomerDemographic cd) {
        this.setId(cd.getId());
        this.setCustomerId(cd.getCustomerId());
        this.setImportEditable(cd.getImportEditable());
        this.setLabelCode(cd.getLabelCode());
        this.setLabelName(cd.getLabelName());
        this.setSortOrder(cd.getSortOrder());
        this.setValueCardinality(cd.getValueCardinality());
        this.setVisible(cd.getVisible());
        this.setCustomerDemographicValues(cd.getCustomerDemographicValues());
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
	 * @return the studentDemographicValues
	 */
	public StudentDemographicValue[] getStudentDemographicValues() {
		return studentDemographicValues;
	}
	/**
	 * @param studentDemographicValues the studentDemographicValues to set
	 */
	public void setStudentDemographicValues(
			StudentDemographicValue[] studentDemographicValues) {
		this.studentDemographicValues = studentDemographicValues;
	}

	/**
	 * @return the orgNodeId
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}

	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
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
    
    
} 
