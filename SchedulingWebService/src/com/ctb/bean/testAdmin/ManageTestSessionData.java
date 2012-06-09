package com.ctb.bean.testAdmin;

import java.util.List; //LLO- 118 - Change for Ematrix UI

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;


public class ManageTestSessionData extends CTBBeanData
{ 
	
    static final long serialVersionUID = 1L;
    private Integer totalExportedStudentCount = 0;
    private Integer scheduledStudentCount = 0;
	private Integer notTakenStudentCount = 0;
	private Integer notCompletedStudentCount = 0;
    private List<Integer> toBeExportedStudentRosterList; //LLO- 118 - Change for Ematrix UI
	
    /**
	 * @return the toBeExportedStudentRosterList
	 */
	public List<Integer> getToBeExportedStudentRosterList() {
		return toBeExportedStudentRosterList;
	}

	/**
	 * @param toBeExportedStudentRosterList the toBeExportedStudentRosterList to set
	 */
	public void setToBeExportedStudentRosterList(List<Integer> toBeExportedStudentRosterList) {
		this.toBeExportedStudentRosterList = toBeExportedStudentRosterList;
	}

	/**
	 * @return the totalExportedStudentCount
	 */
	public Integer getTotalExportedStudentCount() {
		return totalExportedStudentCount;
	}

	/**
	 * @param totalExportedStudentCount the totalExportedStudentCount to set
	 */
	public void setTotalExportedStudentCount(Integer totalExportedStudentCount) {
		this.totalExportedStudentCount = totalExportedStudentCount;
	}

	/**
	 * Gets the array of ManageStudent beans
	 * @return array of ManageStudents
	 */
	public ManageTestSession[] getManageTestSessions() {
        CTBBean [] beans = this.getBeans();
        ManageTestSession[] result = new ManageTestSession[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (ManageTestSession) beans[i];
        return result;
	}
	
    /**
     * Sets the array of ManageStudent beans
     * @param students - the array of ManageStudent beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setManageTestSessions(ManageTestSession [] testSessions, Integer pageSize) {
        this.setBeans(testSessions, pageSize);
   
   }

	/**
	 * @return the scheduledStudentCount
	 */
	public Integer getScheduledStudentCount() {
		return scheduledStudentCount;
	}

	/**
	 * @param scheduledStudentCount the scheduledStudentCount to set
	 */
	public void setScheduledStudentCount(Integer scheduledStudentCount) {
		this.scheduledStudentCount = scheduledStudentCount;
	}

	/**
	 * @return the notTakenStudentCount
	 */
	public Integer getNotTakenStudentCount() {
		return notTakenStudentCount;
	}

	/**
	 * @param notTakenStudentCount the notTakenStudentCount to set
	 */
	public void setNotTakenStudentCount(Integer notTakenStudentCount) {
		this.notTakenStudentCount = notTakenStudentCount;
	}

	/**
	 * @return the notCompletedStudentCount
	 */
	public Integer getNotCompletedStudentCount() {
		return notCompletedStudentCount;
	}

	/**
	 * @param notCompletedStudentCount the notCompletedStudentCount to set
	 */
	public void setNotCompletedStudentCount(Integer notCompletedStudentCount) {
		this.notCompletedStudentCount = notCompletedStudentCount;
	}
}