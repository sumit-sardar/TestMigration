package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of customer beans
 * 
 * @author Tata consultency Services
 */

public class FindCustomerData extends CTBBeanData
{ 
      static final long serialVersionUID = 1L;
    /**
	 * Gets the array of customer beans
	 * @return array of customers
	 */
	public FindCustomer[] getCustomers() {
        CTBBean [] beans = this.getBeans();
		FindCustomer[] result = new FindCustomer[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (FindCustomer) beans[i];
        return result;
	}
	
    /**
     * Sets the array of customer beans
     * @param customers - the array of user beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setCustomers(FindCustomer [] customer, Integer pageSize) {
        this.setBeans(customer, pageSize);
    }
} 
