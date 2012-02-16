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
	private Integer  testAdminId;
	private String dateTestingCompleted;
	private Integer lastMseq;
	private Integer restartNumber;
	//private Integer orgNodeId;

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
	/**
	 * @return the testAdminId
	 */
	public Integer getTestAdminId() {
		return testAdminId;
	}
	/**
	 * @param testAdminId the testAdminId to set
	 */
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}
	/**
	 * @return the dateTestingCompleted
	 */
	public String getDateTestingCompleted() {
		return dateTestingCompleted;
	}
	/**
	 * @param dateTestingCompleted the dateTestingCompleted to set
	 */
	public void setDateTestingCompleted(String dateTestingCompleted) {
		this.dateTestingCompleted = dateTestingCompleted;
	}
	/**
	 * @return the lastMseq
	 */
	public Integer getLastMseq() {
		return lastMseq;
	}
	/**
	 * @param lastMseq the lastMseq to set
	 */
	public void setLastMseq(Integer lastMseq) {
		this.lastMseq = lastMseq;
	}
	/**
	 * @return the restartNumber
	 */
	public Integer getRestartNumber() {
		return restartNumber;
	}
	/**
	 * @param restartNumber the restartNumber to set
	 */
	public void setRestartNumber(Integer restartNumber) {
		this.restartNumber = restartNumber;
	}
	
	

}
