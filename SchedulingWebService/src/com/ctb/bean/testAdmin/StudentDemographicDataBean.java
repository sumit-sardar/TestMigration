package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Data bean representing the contents of the OAS.STUDENT_DEMOGRAPHIC_DATA table
 * 
 * @author John_Wang
 */


public class StudentDemographicDataBean extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
   
    
    public StudentDemoGraphics[] getStudentDemographics() {
        CTBBean [] beans = this.getBeans();
        StudentDemoGraphics[] result = null;
        if(beans != null) {
            result = new StudentDemoGraphics[beans.length];
            for(int i=0;i<beans.length;i++)
                result[i] = (StudentDemoGraphics) beans[i];
        } else {
            result = new StudentDemoGraphics[0];
        }
        return result;
    }
    
    /**
     * Sets the array of student accommodations beans
     * @param studentAccommodations - the array of student accommodations beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setStudentDemographics(StudentDemoGraphics [] studentDemographic, Integer pageSize) {
        this.setBeans(studentDemographic, pageSize);
    }
	
    
} 

