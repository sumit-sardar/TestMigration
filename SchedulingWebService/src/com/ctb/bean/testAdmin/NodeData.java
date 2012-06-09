package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of org node beans
 * 
 * @author Nate_Cohen
 */
public class NodeData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of org node beans
	 * @return array of org nodes
	 */
	public Node[] getNodes() {
        CTBBean [] beans = this.getBeans();
		Node[] result = new Node[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (Node) beans[i];
        return result;
	}
	
    /**
     * Sets the array of org node beans
     * @param nodes - the array of org node beans
     * @param pageSize - The number of beans to include in one page of data
     */
	public void setNodes(Node[] nodes, Integer pageSize) {
		this.setBeans(nodes, pageSize);
	}
} 
