package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Container for a filtered, sorted, paged list of user node beans
 * 
 * @author Nate_Cohen
 */
public class UserNodeData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of user node beans
	 * @return array of user nodes
	 */
	public UserNode[] getUserNodes() {
        CTBBean [] beans = this.getBeans();
		UserNode[] result = new UserNode[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (UserNode) beans[i];
        return result;
	}
	
    /**
     * Sets the array of user node beans
     * @param userNodes - the array of user node beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setUserNodes(UserNode [] userNodes, Integer pageSize) {
        this.setBeans(userNodes, pageSize);
    }
} 
