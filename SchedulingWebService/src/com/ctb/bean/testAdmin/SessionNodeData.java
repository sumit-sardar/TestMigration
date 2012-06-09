package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Container for a filtered, sorted, paged list of session node beans
 * 
 * @author Nate_Cohen
 */
public class SessionNodeData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of org node beans
	 * @return array of org nodes
	 */
	public SessionNode[] getSessionNodes() {
        CTBBean [] beans = this.getBeans();
		SessionNode[] result = new SessionNode[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (SessionNode) beans[i];
        return result;
	}
	
    /**
     * Sets the array of session node beans
     * @param sessionNodes - the array of session node beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setSessionNodes(SessionNode [] sessionNodes, Integer pageSize) {
        this.setBeans(sessionNodes, pageSize);
    }
} 
