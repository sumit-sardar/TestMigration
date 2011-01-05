package com.ctb.bean.studentManagement;


import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;
import com.ctb.bean.testAdmin.SessionStudent;

public class ManageBulkStudentData extends CTBBeanData {
	


	 
	    static final long serialVersionUID = 1L;
	    /**
		 * Gets the array of ManageStudent beans
		 * @return array of ManageStudents
		 */
		public SessionStudent[] getManageStudents() {
	        CTBBean [] beans = this.getBeans();
	        SessionStudent[] result = new SessionStudent[beans.length];
	        for(int i=0;i<beans.length;i++)
	            result[i] = (SessionStudent) beans[i];
	        return result;
		}
		
	    /**
	     * Sets the array of ManageStudent beans
	     * @param students - the array of ManageStudent beans
	     * @param pageSize - The number of beans to include in one page of data
	     */
	    public void setManageStudents(SessionStudent [] students, Integer pageSize) {
	        this.setBeans(students, pageSize);
	    }
	} 



