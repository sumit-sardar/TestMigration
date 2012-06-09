package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * ProgramStatusSessionData.java
 * @author John_Wang
 *
 */

public class ProgramStatusSessionData extends CTBBeanData
{ 
     static final long serialVersionUID = 1L;
	/**
	 * Gets the array of Program Status Session beans
	 * @return array of ProgramStatusSession
	 */
	public ProgramStatusSession [] getProgramStatusSessions() {
        CTBBean [] beans = this.getBeans();
		ProgramStatusSession [] result = new ProgramStatusSession[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (ProgramStatusSession) beans[i];
        return result;
	}
	
    /**
     * Sets the array of tudent Program Status Session beans
     * @param sessionNodes - the array of Program Status Session beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setProgramStatusSessions(ProgramStatusSession [] programStatusSession, Integer pageSize) {
        this.setBeans(programStatusSession, pageSize); 
    }
} 
