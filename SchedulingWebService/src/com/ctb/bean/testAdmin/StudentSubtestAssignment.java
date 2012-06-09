package com.ctb.bean.testAdmin; 

import java.util.ArrayList;

public class StudentSubtestAssignment 
{ 
    private ArrayList subtests;
    private String form;
    private Integer testRosterId;
    private Integer testAdminId;
    private Integer studentId;
    
	/**
	 * @return Returns the testRosterId.
	 */
	public Integer getTestRosterId() {
		return testRosterId;
	}
	/**
	 * @param testRosterId The testRosterId to set.
	 */
	public void setTestRosterId(Integer testRosterId) {
		this.testRosterId = testRosterId;
	}
    /**
	 * @return Returns the testAdminId.
	 */
	public Integer getTestAdminId() {
		return testAdminId;
	}
	/**
	 * @param testAdminId The testAdminId to set.
	 */
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}
    /**
	 * @return Returns the studentId.
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId The studentId to set.
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return Returns the form.
	 */
	public String getForm() {
		return form;
	}
	/**
	 * @param form The form to set.
	 */
	public void setForm(String form) {
		this.form = form;
	}
	/**
	 * @return Returns the subtests.
	 */
	public ArrayList getSubtests() {
		return subtests;
	}
	/**
	 * @param subtests The subtests to set.
	 */
	public void setSubtests(ArrayList subtests) {
		this.subtests = subtests;
	}
    
    
} 
