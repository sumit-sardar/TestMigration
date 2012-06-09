package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

public class StudentManifestData extends CTBBeanData {

	 static final long serialVersionUID = 1L;
		/**
		 * Gets the array of Student Manifest beans
		 * @return array of StudentMnifest
		 */
		public StudentManifest[] getStudentManifests() {
	        CTBBean [] beans = this.getBeans();
			StudentManifest[] result = new StudentManifest[beans.length];			
	        for(int i=0;i<beans.length;i++)
	            result[i] = (StudentManifest) beans[i];
	       return result;
		}
		
	    /**
	     * Sets the array of Student Manifest beans
	     * @param sessionNodes - the array of Student Manifest beans
	     * @param pageSize - The number of beans to include in one page of data
	     */
	    public void setStudentManifests(StudentManifest [] studentManifest, Integer pageSize) {
	        this.setBeans(studentManifest, pageSize); 
	    }	
}
