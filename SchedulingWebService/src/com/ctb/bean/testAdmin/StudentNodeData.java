package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Container for a filtered, sorted, paged list of student node beans
 * 
 * @author Nate_Cohen
 */
public class StudentNodeData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of org node beans
	 * @return array of org nodes
	 */
	public StudentNode[] getStudentNodes() {
        CTBBean [] beans = this.getBeans();
		StudentNode[] result = new StudentNode[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (StudentNode) beans[i];
        return result;
	}
	
    /**
     * Sets the array of student node beans
     * @param studentNodes - the array of student node beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setStudentNodes(StudentNode [] studentNodes, Integer pageSize) {
        this.setBeans(studentNodes, pageSize);
    }
} 
