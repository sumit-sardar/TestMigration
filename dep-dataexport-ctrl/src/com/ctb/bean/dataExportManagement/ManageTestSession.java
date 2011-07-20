package com.ctb.bean.dataExportManagement;

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

import java.util.Date;
import java.util.List;

public class ManageTestSession extends CTBBean{
	
	 static final long serialVersionUID = 1L;
	    private String testSessionName;
	    private String startDate;
	    private String endDate;
	    private String status;
	    private Integer testAdminId;
	    private Integer toBeExported;
	    private Integer complete;
	    private Integer scheduled;
	    private Integer studentStop;
	    private Integer systemStop;
	    private Integer notTaken;
	    private Integer incomplete;
	   
		/**
		 * @return the testSessionName
		 */
		public String getTestSessionName() {
			return testSessionName;
		}
		/**
		 * @param testSessionName the testSessionName to set
		 */
		public void setTestSessionName(String testSessionName) {
			this.testSessionName = testSessionName;
		}
		/**
		 * @return the startDate
		 */
		public String getStartDate() {
			return startDate;
		}
		/**
		 * @param startDate the startDate to set
		 */
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
	
		/**
		 * @return the endDate
		 */
		public String getEndDate() {
			return endDate;
		}
		/**
		 * @param endDate the endDate to set
		 */
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		/**
		 * @return the toBeExported
		 */
		public Integer getToBeExported() {
			return toBeExported;
		}
		/**
		 * @param toBeExported the toBeExported to set
		 */
		public void setToBeExported(Integer toBeExported) {
			this.toBeExported = toBeExported;
		}
		/**
		 * @return the complete
		 */
		public Integer getComplete() {
			return complete;
		}
		/**
		 * @param complete the complete to set
		 */
		public void setComplete(Integer complete) {
			this.complete = complete;
		}
		/**
		 * @return the scheduled
		 */
		public Integer getScheduled() {
			return scheduled;
		}
		/**
		 * @param scheduled the scheduled to set
		 */
		public void setScheduled(Integer scheduled) {
			this.scheduled = scheduled;
		}
		/**
		 * @return the studentStop
		 */
		public Integer getStudentStop() {
			return studentStop;
		}
		/**
		 * @param studentStop the studentStop to set
		 */
		public void setStudentStop(Integer studentStop) {
			this.studentStop = studentStop;
		}
		/**
		 * @return the systemStop
		 */
		public Integer getSystemStop() {
			return systemStop;
		}
		/**
		 * @param systemStop the systemStop to set
		 */
		public void setSystemStop(Integer systemStop) {
			this.systemStop = systemStop;
		}
		/**
		 * @return the notTaken
		 */
		public Integer getNotTaken() {
			return notTaken;
		}
		/**
		 * @param notTaken the notTaken to set
		 */
		public void setNotTaken(Integer notTaken) {
			this.notTaken = notTaken;
		}
		/**
		 * @return the incomplete
		 */
		public Integer getIncomplete() {
			return incomplete;
		}
		/**
		 * @param incomplete the incomplete to set
		 */
		public void setIncomplete(Integer incomplete) {
			this.incomplete = incomplete;
		}
		/**
		 * @return the testAdminId
		 */
		public Integer getTestAdminId() {
			return testAdminId;
		}
		/**
		 * @param testAdminId the testAdminId to set
		 */
		public void setTestAdminId(Integer testAdminId) {
			this.testAdminId = testAdminId;
		}
		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}
		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}
		
}
