package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of student beans
 * 
 * @author Nate_Cohen
 */
public class StudentData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of student beans
	 * @return array of students
	 */
	public Student[] getStudents() {
        CTBBean [] beans = this.getBeans();
		Student[] result = new Student[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (Student) beans[i];
        return result;
	}
	
    /**
     * Sets the array of student beans
     * @param students - the array of student beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setStudents(Student [] students, Integer pageSize) {
        this.setBeans(students, pageSize);
    }
} 
