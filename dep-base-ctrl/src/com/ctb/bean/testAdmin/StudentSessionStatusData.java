package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

public class StudentSessionStatusData extends CTBBeanData
{ 
     static final long serialVersionUID = 1L;
	/**
	 * Gets the array of Student Session Status beans
	 * @return array of StudentSessionStatus
	 */
	public StudentSessionStatus[] getStudentSessionStatuses() {
        CTBBean [] beans = this.getBeans();
		StudentSessionStatus [] result = new StudentSessionStatus[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (StudentSessionStatus) beans[i];
        return result;
	}
	
    /**
     * Sets the array of tudent Session Status beans
     * @param sessionNodes - the array of tudent Session Status beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setStudentSessionStatuses(StudentSessionStatus [] studentSessionStatus, Integer pageSize) {
        this.setBeans(studentSessionStatus, pageSize); 
    }
} 
