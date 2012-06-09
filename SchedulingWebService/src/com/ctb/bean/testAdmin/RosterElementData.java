package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of roster element beans
 * 
 * @author Nate_Cohen
 */
public class RosterElementData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of roster element beans
	 * @return array of roster elements
	 */
	public RosterElement[] getRosterElements() {
        CTBBean [] beans = this.getBeans();
		RosterElement[] result = new RosterElement[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (RosterElement) beans[i];
        return result;
	}
	
    /**
     * Sets the array of roster element beans
     * @param rosters - the array of roster element beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setRosterElements(RosterElement [] rosters, Integer pageSize) {
        this.setBeans(rosters, pageSize);
    }
} 
