package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    private Boolean hasLocator = false;
   
    private Map<Integer,String> locatorSubtestTD = null;	 //added for locator manifest
    private TestElement [] locatorDeliverableUnit;		 //added for locator manifest
    private TestElement [] hasLocatorSubtestList;
    private boolean isTestSessionHasStudents;
    
    
    public Boolean getHasLocator() {
		return hasLocator;
	}
	public void setHasLocator(Boolean hasLocator) {
		this.hasLocator = hasLocator;
	}
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
	/**
	 * @return the locatorSubtestTD
	 */
	public Map<Integer,String> getLocatorSubtestTD() {
		return locatorSubtestTD;
	}
	/**
	 * @param locatorSubtestTD the locatorSubtestTD to set
	 */
	public void setLocatorSubtestTD(Map<Integer,String> locatorSubtestTD) {
		this.locatorSubtestTD = locatorSubtestTD;
	}
	/**
	 * @return the locatorDeliverableUnit
	 */
	public TestElement[] getLocatorDeliverableUnit() {
		return locatorDeliverableUnit;
	}
	/**
	 * @param locatorDeliverableUnit the locatorDeliverableUnit to set
	 */
	public void setLocatorDeliverableUnit(TestElement[] locatorDeliverableUnit) {
		this.locatorDeliverableUnit = locatorDeliverableUnit;
	}
	/**
	 * @return the hasLocatorSubtestList
	 */
	public TestElement[] getHasLocatorSubtestList() {
		return hasLocatorSubtestList;
	}
	/**
	 * @param hasLocatorSubtestList the hasLocatorSubtestList to set
	 */
	public void setHasLocatorSubtestList(TestElement[] hasLocatorSubtestList) {
		this.hasLocatorSubtestList = hasLocatorSubtestList;
	}
	/**
	 * @return the isTestSessionHasStudents
	 */
	public boolean isTestSessionHasStudents() {
		return isTestSessionHasStudents;
	}
	/**
	 * @param isTestSessionHasStudents the isTestSessionHasStudents to set
	 */
	public void setTestSessionHasStudents(boolean isTestSessionHasStudents) {
		this.isTestSessionHasStudents = isTestSessionHasStudents;
	}
	
	public Map<Integer,Integer> getSessionStudentMap(){
		Map<Integer,Integer> sessionStudentMap = new HashMap<Integer, Integer>();
		if(this.students != null && this.students.length > 0){
			for(SessionStudent st : this.students){
				sessionStudentMap.put(st.getStudentId(), st.getOrgNodeId());
			}
		}
		return sessionStudentMap;
	}
	
	
} 
