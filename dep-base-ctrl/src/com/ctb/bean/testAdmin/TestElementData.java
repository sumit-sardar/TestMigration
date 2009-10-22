package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * Container for a filtered, sorted, paged list of test element beans
 * 
 * @author Nate_Cohen
 */
public class TestElementData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    private Integer breakCount;
    
    public Integer getBreakCount() {
        return this.breakCount;
    }
    
    public void setBreakCount(Integer breakCount) {
        this.breakCount = breakCount;
    }
    
    /**
	 * Gets the array of test element beans
	 * @return array of test elements
	 */
    public TestElement[] getTestElements() {
        CTBBean [] beans = this.getBeans();
		TestElement[] result = new TestElement[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (TestElement) beans[i];
        return result;
    }
    
    /**
     * Sets the array of test element beans
     * @param testElements - the array of test element beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setTestElements(TestElement [] testElements, Integer pageSize) {
        this.setBeans(testElements, pageSize);
        int result = 0;
        if(testElements != null && testElements.length > 0) {
            String accessCode = testElements[0].getAccessCode();
            for(int i=0;i<testElements.length;i++) {
                TestElement ts = testElements[i];
                if(accessCode != null && !accessCode.equals(ts.getAccessCode())) {
                    result++;
                    accessCode = ts.getAccessCode();
                }
            }
        }
        this.breakCount = new Integer(result);
    }
} 
