package com.ctb.bean.studentManagement; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the contents of the OAS.STUDENT_DEMOGRAPHIC_DATA table
 * 
 * @author John_Wang
 */


public class StudentOtherDetailData extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer studentProgramGoalDataId;
    private Integer studentId;
    private Integer customerPrgGoalId;
    private String valueName;
    private String value;
    private Integer createdBy;
    private Date createdDateTime;
    private Integer updatedBy;
    private Date updatedDateTime;
	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
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
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
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
	 * @return the updatedBy
	 */
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return the updatedDateTime
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime the updatedDateTime to set
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the studentProgramGoalDataId
	 */
	public Integer getStudentProgramGoalDataId() {
		return studentProgramGoalDataId;
	}
	/**
	 * @param studentProgramGoalDataId the studentProgramGoalDataId to set
	 */
	public void setStudentProgramGoalDataId(Integer studentProgramGoalDataId) {
		this.studentProgramGoalDataId = studentProgramGoalDataId;
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

