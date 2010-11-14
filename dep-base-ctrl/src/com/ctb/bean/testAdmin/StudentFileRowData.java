package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a  sorted list of StudentFileRow beans
 * 
 *@ author Tata Consultancy Services 
 */
 
public class StudentFileRowData extends CTBBeanData
{ 
      static final long serialVersionUID = 1L;
   
    /**
	 * Gets the array of StudentFileRow beans
	 * @return array of StudentFileRow
	 */
    
     public StudentFileRow[] getStudentFileRows() {
         CTBBean [] beans = this.getBeans();
         StudentFileRow[] result = new StudentFileRow[beans.length];
         for(int i=0;i<beans.length;i++)
            result[i] = (StudentFileRow) beans[i];
         return result;
    }
    
    /**
     * Sets the array of studentFileRows beans
     * @param studentFileRows - the array of studentFileRows beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setStudentFileRows(StudentFileRow []studentFileRows, Integer pageSize) {
        this.setBeans(studentFileRows, pageSize);
    }
} 
