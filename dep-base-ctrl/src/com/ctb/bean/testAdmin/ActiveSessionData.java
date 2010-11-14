package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Container for a filtered, sorted, paged list of active session beans
 * 
 * @author Nate_Cohen
 */
public class ActiveSessionData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of active session beans
	 * @return array of active tests
	 */
	public ActiveSession[] getActiveSessions() {
        CTBBean [] beans = this.getBeans();
		ActiveSession[] result = new ActiveSession[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (ActiveSession) beans[i];
        return result;
	}
	
    /**
     * Sets the array of active session beans
     * @param activeSessions - the array of active session beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setActiveSessions(ActiveSession [] activeSessions, Integer pageSize) {
        this.setBeans(activeSessions, pageSize);
    }
} 
