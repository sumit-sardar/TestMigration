package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

public class StudentTestletInfoData extends CTBBeanData
{ 
     static final long serialVersionUID = 1L;
	/**
	 * Gets the array of Student Testlet info beans
	 * @return array of StudentTestletInfo
	 */
	public StudentTestletInfo[] getStudentTestletInfos() {
        CTBBean [] beans = this.getBeans();
        StudentTestletInfo [] result = new StudentTestletInfo[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (StudentTestletInfo) beans[i];
        return result;
	}
	
    /**
     * Sets the array of student testlet info beans
     * @param studentTestletInfo - the array of student testlet info beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setStudentTestletInfos(StudentTestletInfo [] studentTestletInfo, Integer pageSize) {
        this.setBeans(studentTestletInfo, pageSize); 
    }
} 
