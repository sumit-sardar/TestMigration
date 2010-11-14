package com.ctb.bean.studentManagement; 

/**
 * Data bean representing the contents of the OAS.CUSTOMER_DEMOGRAPHIC table 
 * and student selection on demographic values
 * 
 * @author John_Wang
 */


public class StudentProgramGoal extends CustomerProgramGoal
{ 
    private Integer studentId;
    private StudentProgramGoalValue [] studentProgramGoalValues;
    
     public StudentProgramGoal() {
        super();
     }
    
    public StudentProgramGoal(CustomerProgramGoal cd) {
        this.setCustomerPrgGoalId(cd.getCustomerPrgGoalId());
        this.setCustomerId(cd.getCustomerId());
        this.setImportEditable(cd.getImportEditable());
        this.setLabelCode(cd.getLabelCode());
        this.setLabelName(cd.getLabelName());
        this.setSortOrder(cd.getSortOrder());
        this.setValueCardinality(cd.getValueCardinality());
        this.setVisible(cd.getVisible());
        this.setCustomerProgramGoalValues(cd.getCustomerProgramGoalValues());
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
	 * @return the studentProgramGoalValues
	 */
	public StudentProgramGoalValue[] getStudentProgramGoalValues() {
		return studentProgramGoalValues;
	}

	/**
	 * @param studentProgramGoalValues the studentProgramGoalValues to set
	 */
	public void setStudentProgramGoalValues(
			StudentProgramGoalValue[] studentProgramGoalValues) {
		this.studentProgramGoalValues = studentProgramGoalValues;
	}
    
    
} 
