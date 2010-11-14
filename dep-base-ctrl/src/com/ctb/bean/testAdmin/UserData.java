package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of user beans
 * 
 * @author Nate_Cohen
 */
public class UserData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of user beans
	 * @return array of users
	 */
	public User[] getUsers() {
        CTBBean [] beans = this.getBeans();
		User[] result = new User[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (User) beans[i];
        return result;
	}
	
    /**
     * Sets the array of user beans
     * @param users - the array of user beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setUsers(User [] users, Integer pageSize) {
        this.setBeans(users, pageSize);
    }
} 
