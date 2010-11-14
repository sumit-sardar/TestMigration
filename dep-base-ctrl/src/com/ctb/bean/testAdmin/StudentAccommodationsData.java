package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of student accommodations beans
 * 
 * @author Nate_Cohen
 */
public class StudentAccommodationsData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of student accommodations beans
	 * @return array of student accommodations
	 */
    public StudentAccommodations[] getStudentAccommodations() {
        CTBBean [] beans = this.getBeans();
        StudentAccommodations[] result = null;
        if(beans != null) {
            result = new StudentAccommodations[beans.length];
            for(int i=0;i<beans.length;i++)
                result[i] = (StudentAccommodations) beans[i];
        } else {
            result = new StudentAccommodations[0];
        }
        return result;
    }
    
    /**
     * Sets the array of student accommodations beans
     * @param studentAccommodations - the array of student accommodations beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setStudentAccommodations(StudentAccommodations [] studentAccommodations, Integer pageSize) {
        this.setBeans(studentAccommodations, pageSize);
    }
} 
