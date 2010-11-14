package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Container for a filtered, sorted, paged list of active test beans
 * 
 * @author Nate_Cohen
 */
public class ActiveTestData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of active test beans
	 * @return array of active tests
	 */
	public ActiveTest[] getActiveTests() {
        CTBBean [] beans = this.getBeans();
		ActiveTest[] result = new ActiveTest[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (ActiveTest) beans[i];
        return result;
	}
	
    /**
     * Sets the array of active test beans
     * @param activeTests - the array of active test beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setActiveTests(ActiveTest [] activeTests, Integer pageSize) {
        this.setBeans(activeTests, pageSize);
    }
} 
