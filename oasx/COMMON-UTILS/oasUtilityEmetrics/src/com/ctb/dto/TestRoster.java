package com.ctb.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.ffpojo.metadata.positional.annotation.PositionalRecord;

@PositionalRecord
public class TestRoster implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer testRosterId ;
	private String activationStatus ;
	private String testCompletionStatus;
	private Student student;
	private Integer customerId;
	private Integer studentId;
	//private Integer orgNodeId;
	private Set<StudentItemSetStatus> studentItemSetStatus = new HashSet<StudentItemSetStatus> ();
	

	
	
	
	/**
	 * @return the studentItemSetStatus
	 */
	public Set<StudentItemSetStatus> getStudentItemSetStatus() {
		return studentItemSetStatus;
	}
	/**
	 * @param studentItemSetStatus the studentItemSetStatus to set
	 */
	public void setStudentItemSetStatus(
			Set<StudentItemSetStatus> studentItemSetStatus) {
		this.studentItemSetStatus = studentItemSetStatus;
	}
	/**
	 * @return the student
	 */
	public Student getStudent() {
		return student;
	}
	/**
	 * @param student the student to set
	 */
	public void setStudent(Student student) {
		this.student = student;
	}
	/**
	 * @return the testRosterId
	 */
	public Integer getTestRosterId() {
		return testRosterId;
	}
	/**
	 * @param testRosterId the testRosterId to set
	 */
	public void setTestRosterId(Integer testRosterId) {
		this.testRosterId = testRosterId;
	}
	
	/**
	 * @return the activationStatus
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus the activationStatus to set
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	/**
	 * @return the testCompletionStatus
	 */
	public String getTestCompletionStatus() {
		return testCompletionStatus;
	}
	/**
	 * @param testCompletionStatus the testCompletionStatus to set
	 */
	public void setTestCompletionStatus(String testCompletionStatus) {
		this.testCompletionStatus = testCompletionStatus;
	}
	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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
	
	

}
