package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

public class TestletLevelFormData extends CTBBeanData
{ 
     static final long serialVersionUID = 1L;
	/**
	 * Gets the array of Testlet level form beans
	 * @return array of TestletLevelForm
	 */
	public TestletLevelForm[] getTestletLevelForms() {
        CTBBean [] beans = this.getBeans();
        TestletLevelForm [] result = new TestletLevelForm[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (TestletLevelForm) beans[i];
        return result;
	}
	
    /**
     * Sets the array of testlet level form beans
     * @param TestletLevelForm - the array of student testlet info beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setTestletLevelForms(TestletLevelForm [] testletLevelForm, Integer pageSize) {
        this.setBeans(testletLevelForm, pageSize); 
    }
} 
