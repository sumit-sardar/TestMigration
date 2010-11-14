package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of test product beans
 * 
 * @author Nate_Cohen
 */
public class TestProductData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of test product beans
	 * @return array of test products
	 */
	public TestProduct[] getTestProducts() {
        CTBBean [] beans = this.getBeans();
		TestProduct[] result = new TestProduct[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (TestProduct) beans[i];
        return result;
	}
	
    /**
     * Sets the array of test product beans
     * @param testProducts - the array of test product beans
     * @param pageSize - The number of beans to include in one page of data
     */
	public void setTestProducts(TestProduct[] testProducts, Integer pageSize) {
		this.setBeans(testProducts, pageSize);
	}
} 
