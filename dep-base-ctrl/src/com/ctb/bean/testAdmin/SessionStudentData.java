package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of session student beans
 * 
 * @author Nate_Cohen
 */
public class SessionStudentData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of session student beans
	 * @return array of session students
	 */
	public SessionStudent[] getSessionStudents() {
        CTBBean [] beans = this.getBeans();
		SessionStudent[] result = new SessionStudent[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (SessionStudent) beans[i];
        return result;
	}
	
    /**
     * Sets the array of session student beans
     * @param students - the array of session student beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setSessionStudents(SessionStudent [] students, Integer pageSize) {
        this.setBeans(students, pageSize);
    }
} 
