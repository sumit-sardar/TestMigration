package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of scheduling student beans
 * 
 * @author Nate_Cohen
 */
public class SchedulingStudentData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of scheduling student beans
	 * @return array of scheduling students
	 */
	public SchedulingStudent[] getStudents() {
        CTBBean [] beans = this.getBeans();
		SchedulingStudent[] result = new SchedulingStudent[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (SchedulingStudent) beans[i];
        return result;
	}
	
    /**
     * Sets the array of scheduling student beans
     * @param students - the array of scheduling student beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setSchedulingStudents(SchedulingStudent [] students, Integer pageSize) {
        this.setBeans(students, pageSize);
    }
} 
