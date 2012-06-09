package com.ctb.bean.studentManagement; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Container for a filtered, sorted, paged list of org node beans
 * 
 * @author John_Wang
 */

public class OrganizationNodeData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of org node beans
	 * @return array of org nodes
	 */
	public OrganizationNode[] getOrganizationNodes() {
        CTBBean [] beans = this.getBeans();
		OrganizationNode[] result = new OrganizationNode[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (OrganizationNode) beans[i];
        return result;
	}
	
    /**
     * Sets the array of organization node beans
     * @param organizationNodes - the array of organization node beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setOrganizationNodes(OrganizationNode [] organizationNodes, Integer pageSize) {
        this.setBeans(organizationNodes, pageSize);
    }
} 

