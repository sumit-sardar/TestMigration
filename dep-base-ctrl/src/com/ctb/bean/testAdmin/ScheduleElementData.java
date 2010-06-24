package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of scheduling student beans
 * 
 * @author Nate_Cohen
 */
public class ScheduleElementData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of scheduling student beans
	 * @return array of scheduling students
	 */
	public ScheduleElement[] getElements() {
        CTBBean [] beans = this.getBeans();
        ScheduleElement[] result = new ScheduleElement[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (ScheduleElement) beans[i];
        return result;
	}
	
    /**
     * Sets the array of scheduling student beans
     * @param students - the array of scheduling student beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setScheduleElements(ScheduleElement [] elements, Integer pageSize) {
        this.setBeans(elements, pageSize);
    }
} 
