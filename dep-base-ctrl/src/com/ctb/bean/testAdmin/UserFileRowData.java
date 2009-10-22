package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a  sorted list of userFileRow beans
 * 
 * @author Tata Consultancy Services 
 */

public class UserFileRowData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of userFileRow beans
	 * @return array of userFileRow
	 */
    
    public UserFileRow[] getUserFileRows() {
         CTBBean [] beans = this.getBeans();
         UserFileRow[] result = new UserFileRow[beans.length];
         for(int i=0;i<beans.length;i++)
            result[i] = (UserFileRow) beans[i];
         return result;
    }
    
    /**
     * Sets the array of userFileRows beans
     * @param userFileRows - the array of userFileRows beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setUserFileRows(UserFileRow [] userFileRows, Integer pageSize) {
        this.setBeans(userFileRows, pageSize);
    }
} 
