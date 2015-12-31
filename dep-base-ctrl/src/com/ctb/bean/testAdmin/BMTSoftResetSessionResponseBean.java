package com.ctb.bean.testAdmin;

import java.io.Serializable;

import com.ctb.bean.CTBBean;

public class BMTSoftResetSessionResponseBean extends CTBBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Assignments[] assignments;
	private String rosterId;
	private String rosterStatus;
	private String errorCode;
	private String studentLogindName = null;

	public BMTSoftResetSessionResponseBean() {
		super();
	}

	/**
	 * @return the assignments
	 */
	public Assignments[] getAssignments() {
		return assignments;
	}

	/**
	 * @param assignments
	 *            the assignments to set
	 */
	public void setAssignments(Assignments[] assignments) {
		this.assignments = assignments;
	}

	/**
	 * @return the rosterId
	 */
	public String getRosterId() {
		return rosterId;
	}

	/**
	 * @param rosterId
	 *            the rosterId to set
	 */
	public void setRosterId(String rosterId) {
		this.rosterId = rosterId;
	}

	/**
	 * @return the rosterStatus
	 */
	public String getRosterStatus() {
		return rosterStatus;
	}

	/**
	 * @param rosterStatus
	 *            the rosterStatus to set
	 */
	public void setRosterStatus(String rosterStatus) {
		this.rosterStatus = rosterStatus;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the studentLogindName
	 */
	public String getStudentLogindName() {
		return studentLogindName;
	}

	/**
	 * @param studentLogindName
	 *            the studentLogindName to set
	 */
	public void setStudentLogindName(String studentLogindName) {
		this.studentLogindName = studentLogindName;
	}

	public static class Assignments implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String assignmentid;
		private String loginOrder;
		private String status;
		private String started;
		private String completed;

		public Assignments() {

		}

		/**
		 * @return the assignmentid
		 */
		public String getAssignmentid() {
			return assignmentid;
		}

		/**
		 * @param assignmentid
		 *            the assignmentid to set
		 */
		public void setAssignmentid(String assignmentid) {
			this.assignmentid = assignmentid;
		}

		/**
		 * @return the loginOrder
		 */
		public String getLoginOrder() {
			return loginOrder;
		}

		/**
		 * @param loginOrder
		 *            the loginOrder to set
		 */
		public void setLoginOrder(String loginOrder) {
			this.loginOrder = loginOrder;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @param status
		 *            the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}

		/**
		 * @return the started
		 */
		public String getStarted() {
			return started;
		}

		/**
		 * @param started
		 *            the started to set
		 */
		public void setStarted(String started) {
			this.started = started;
		}

		/**
		 * @return the completed
		 */
		public String getCompleted() {
			return completed;
		}

		/**
		 * @param completed
		 *            the completed to set
		 */
		public void setCompleted(String completed) {
			this.completed = completed;
		}

		/**
		 * 
		 * @return
		 */
		public boolean isInProgress() {
			if (this.status != null && "IP".equalsIgnoreCase(status))
				return true;
			else
				return false;
		}

	}
}
