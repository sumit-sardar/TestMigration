package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * CustomerReportData.java
 * @author Nate_Cohen
 *
 * Container for a filtered, sorted, paged list of customer report beans
 */
public class CustomerReportData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of customer report beans
	 * 
	 * @return array of customer reports
	 */
	public CustomerReport[] getCustomerReports() {
        CTBBean [] beans = this.getBeans();
		CustomerReport[] result = new CustomerReport[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (CustomerReport) beans[i];
        return result;
	}
	
    /**
     * Sets the array of customer report beans
     * @param customerReports - the array of customer report beans
     * @param pageSize - The number of beans to include in one page of data
     */
	public void setCustomerReports(CustomerReport[] customerReports, Integer pageSize) {
		this.setBeans(customerReports, pageSize);
	}
} 
