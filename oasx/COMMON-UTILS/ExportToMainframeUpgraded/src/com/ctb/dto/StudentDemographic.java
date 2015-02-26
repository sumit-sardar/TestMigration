package com.ctb.dto;

import java.io.Serializable;

public class StudentDemographic implements Serializable {

	private static final long serialVersionUID = 1L;


	private Integer studentDemographicId;
	private Integer studentId;
	private Integer customerDemographicId;
	private String valueName;
	private String value;
	/**
	 * @return the studentDemographicId
	 */
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(valueName);
		
		return sb.toString();
		
	}
	public Integer getStudentDemographicId() {
		return studentDemographicId;
	}
	/**
	 * @param studentDemographicId the studentDemographicId to set
	 */
	public void setStudentDemographicId(Integer studentDemographicId) {
		this.studentDemographicId = studentDemographicId;
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
	 * @return the customerDemographicId
	 */
	public Integer getCustomerDemographicId() {
		return customerDemographicId;
	}
	/**
	 * @param customerDemographicId the customerDemographicId to set
	 */
	public void setCustomerDemographicId(Integer customerDemographicId) {
		this.customerDemographicId = customerDemographicId;
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
	
}
