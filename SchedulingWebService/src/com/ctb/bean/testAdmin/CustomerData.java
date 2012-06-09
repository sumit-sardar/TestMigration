package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of customer beans
 * 
 * @author Tata consultency Services
 */

public class CustomerData extends CTBBeanData
{ 
      static final long serialVersionUID = 1L;
    /**
	 * Gets the array of Customer beans
	 * @return array of Customer
	 */
	public Customer[] getCustomers() {
        CTBBean [] beans = this.getBeans();
		Customer[] result = new Customer[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (Customer) beans[i];
        return result;
	}
	
    /**
     * Sets the array of Customer beans
     * @param customer - the array of Customer beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setCustomers(Customer [] customer, Integer pageSize) {
        this.setBeans(customer, pageSize);
    }
} 
