package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * BroadcastMessageData.java
 * @author Nate_Cohen
 *
 * Container for a filtered, sorted, paged list of broadcast message beans
 */
public class BroadcastMessageData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of broadcast message beans
	 * 
	 * @return array of brodcast messages
	 */
    public BroadcastMessage[] getBroadcastMessages() {
        CTBBean [] beans = this.getBeans();
		BroadcastMessage[] result = new BroadcastMessage[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (BroadcastMessage) beans[i];
        return result;
	}
	
    /**
     * Sets the array of broadcast message beans
     * 
     * @param broadcastMessages - the array of broadcast message beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setBroadcastMessages(BroadcastMessage[] broadcastMessages, Integer pageSize) {
		this.setBeans(broadcastMessages, pageSize);
	}
} 
