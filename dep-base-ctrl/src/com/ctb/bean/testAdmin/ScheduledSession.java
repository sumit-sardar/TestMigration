package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import java.io.Serializable;

/**
 * input object for creating new test sessions.
 * The copyable flag will be set to 'T' if the requesting user
 * has a non-proctor role, the test is active and distributed
 * to the requesting user's top node, and the requesting user
 * has the scheduling node as one of their top nodes. Otherwise
 * the flag will be set to 'F'
 * 
 * @author Nate_Cohen, John_Wang
 */
public class ScheduledSession extends CTBBean
{   
    static final long serialVersionUID = 1L;
    
    private Integer studentsLoggedIn;
    private TestSession testSession;
    private SessionStudent [] students;
    private User [] proctors;
    private TestElement [] scheduledUnits;
    private String copyable;
    private Boolean fromTAS = true;
    
    /**
	 * @return Returns the copyable.
	 */
	public String getCopyable() {
		return copyable;
	}
	/**
	 * @param copyable The copyable to set.
	 */
	public void setCopyable(String copyable) {
		this.copyable = copyable;
	}
    /**
	 * @return Returns the studentsLoggedIn.
	 */
	public Integer getStudentsLoggedIn() {
		return studentsLoggedIn;
	}
	/**
	 * @param studentsLoggedIn The studentsLoggedIn to set.
	 */
	public void setStudentsLoggedIn(Integer studentsLoggedIn) {
		this.studentsLoggedIn = studentsLoggedIn;
	}
	/**
	 * @return Returns the proctors.
	 */
	public User[] getProctors() {
		return proctors;
	}
	/**
	 * @param proctors The proctors to set.
	 */
	public void setProctors(User[] proctors) {
		this.proctors = proctors;
	}
	/**
	 * @return Returns the scheduledUnits.
	 */
	public TestElement[] getScheduledUnits() {
		return scheduledUnits;
	}
	/**
	 * @param scheduledUnits The scheduledUnits to set.
	 */
	public void setScheduledUnits(TestElement[] scheduledUnits) {
		this.scheduledUnits = scheduledUnits;
	}
	/**
	 * @return Returns the students.
	 */
	public SessionStudent[] getStudents() {
		return students;
	}
	/**
	 * @param students The students to set.
	 */
	public void setStudents(SessionStudent[] students) {
		this.students = students;
	}
	/**
	 * @return Returns the testSession.
	 */
	public TestSession getTestSession() {
		return testSession;
	}
	/**
	 * @param testSession The testSession to set.
	 */
	public void setTestSession(TestSession testSession) {
		this.testSession = testSession;
	}
	/**
	 * @return the fromTAS
	 */
	public Boolean isFromTAS() {
		return fromTAS;
	}
	/**
	 * @param fromTAS the fromTAS to set
	 */
	public void setFromTAS(Boolean fromTAS) {
		this.fromTAS = fromTAS;
	}
	
} 
