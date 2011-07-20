
package com.ctb.bean.dataExportManagement; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;


public class ManageTestSessionData extends CTBBeanData
{ 
	
    static final long serialVersionUID = 1L;
    /**
	 * Gets the array of ManageStudent beans
	 * @return array of ManageStudents
	 */
	public ManageTestSession[] getManageTestSessions() {
        CTBBean [] beans = this.getBeans();
        ManageTestSession[] result = new ManageTestSession[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (ManageTestSession) beans[i];
        return result;
	}
	
    /**
     * Sets the array of ManageStudent beans
     * @param students - the array of ManageStudent beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setManageTestSessions(ManageTestSession [] testSessions, Integer pageSize) {
        this.setBeans(testSessions, pageSize);
   
   }
}