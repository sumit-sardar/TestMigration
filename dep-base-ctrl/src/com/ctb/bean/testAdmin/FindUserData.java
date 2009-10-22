package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of ManageStudent beans
 * 
 * @author Tata Consultency Services
 */
public class FindUserData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of ManageUser beans
	 * @return array of ManageUsers
	 */
	public FindUser[] getUsers() {
        CTBBean [] beans = this.getBeans();
		FindUser[] result = new FindUser[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (FindUser) beans[i];
        return result;
	}
	
    /**
     * Sets the array of ManageUsers beans
     * @param users - the array of ManageUser beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setUsers(FindUser [] users, Integer pageSize) {
        this.setBeans(users, pageSize);
    }
} 

