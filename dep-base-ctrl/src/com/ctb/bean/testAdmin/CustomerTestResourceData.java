package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/*
*ISTEP CR032:Download Test
*/

public class CustomerTestResourceData  extends CTBBeanData{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of CustomerTestResource beans
	 * @return array of active tests
	 */
	public CustomerTestResource[] getCustomerTestResource() {
        CTBBean [] beans = this.getBeans();
        CustomerTestResource[] result = new CustomerTestResource[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (CustomerTestResource) beans[i];
        return result;
	}
	
    /**
     * Sets the array of CustomerTestResource beans
     * @param customerTestResource - the array ofcustomerTestResource beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setCustomerTestResources(CustomerTestResource [] customerTestResource, Integer pageSize) {
        this.setBeans(customerTestResource, pageSize);
    }
}
