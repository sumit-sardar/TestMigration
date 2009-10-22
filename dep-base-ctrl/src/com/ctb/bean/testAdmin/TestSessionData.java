package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

/**
 * TestSessionData.java
 * @author Nate_Cohen
 *
 * Container for a filtered, sorted, paged list of test session beans
 */
public class TestSessionData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Gets the array of test session beans
	 * @return array of test sessions
	 */
	public TestSession[] getTestSessions() {
        CTBBean [] beans = this.getBeans();
		TestSession[] result = new TestSession[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (TestSession) beans[i];
        return result;
	}

    /**
     * Sets the array of test session beans
     * @param testSessions - the array of test session beans
     * @param pageSize - The number of beans to include in one page of data
     */
	public void setTestSessions(TestSession[] testSessions, Integer pageSize) {
		this.setBeans(testSessions, pageSize);
	}

} 
