package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;
/**
 * Container for a filtered, sorted, paged list of Program beans
 * 
 * @author Mallik_Korivi
 */
public class ProgramData extends CTBBeanData
{ 
     static final long serialVersionUID = 1L;
     /**
	 * Gets the array of Program beans
	 * @return array of filtered, sorted and paged list of Program beans.
	 */
     public Program [] getPrograms(){
        CTBBean [] beans = this.getBeans();
        Program [] result = new Program[beans.length];
        for(int i = 0;i<beans.length;i++)
            result[i]= (Program)beans[i];
        return result;
     }
     
     /**
     * Sets the array of Program beans
     * @param programs - the array of Program beans
     * @param pageSize - The number of beans to include in one page of data
     */
     public void setPrograms(Program [] programs, Integer pageSize){
        this.setBeans(programs,pageSize);
     }
} 
