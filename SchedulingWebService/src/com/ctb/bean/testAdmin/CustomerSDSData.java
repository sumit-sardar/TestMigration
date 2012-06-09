package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of Customer SDS beans
 * 
 * @author Nate_Cohen
 */
public class CustomerSDSData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of Customer SDS beans
	 * @return array of Customer SDSs
	 */
	public CustomerSDS[] getCustomerSDSs() {
        CTBBean [] beans = this.getBeans();
		CustomerSDS[] result = new CustomerSDS[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (CustomerSDS) beans[i];
        return result;
	}
	
    /**
     * Sets the array of Customer SDS beans
     * @param sdss - the array of Customer SDS beans
     * @param pageSize - The number of beans to include in one page of data
     */
	public void setCustomerSDSs(CustomerSDS[] sdss, Integer pageSize) {
		this.setBeans(sdss, pageSize);
	}
} 
