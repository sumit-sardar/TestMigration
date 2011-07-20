package com.ctb.bean.dataExportManagement; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of ManageStudent beans
 * 
 * @author John_Wang
 */
public class ManageStudentData extends CTBBeanData
{ 
	private Integer scheduledStudentCount = 0;
	private Integer notTakenStudentCount = 0;
	private Integer notCompletedStudentCount = 0;
	
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of ManageStudent beans
	 * @return array of ManageStudents
	 */
	public ManageStudent[] getManageStudents() {
        CTBBean [] beans = this.getBeans();
		ManageStudent[] result = new ManageStudent[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (ManageStudent) beans[i];
        return result;
	}
	
    /**
     * Sets the array of ManageStudent beans
     * @param students - the array of ManageStudent beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setManageStudents(ManageStudent [] students, Integer pageSize) {
        this.setBeans(students, pageSize);
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

