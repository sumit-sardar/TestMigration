package com.ctb.bean.testAdmin;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ctb.bean.CTBBean;

public class BMTResetSessionResponseBean extends CTBBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String externalAdminUserId;
	private String testSessionId;
	private ResetRosters[] rosters;
	private Map<String, String> studentLogindNameMap = null;

	public BMTResetSessionResponseBean() {

	}

	/**
	 * @return the externalAdminUserId
	 */
	public String getExternalAdminUserId() {
		return externalAdminUserId;
	}

	/**
	 * @param externalAdminUserId
	 *            the externalAdminUserId to set
	 */
	public void setExternalAdminUserId(String externalAdminUserId) {
		this.externalAdminUserId = externalAdminUserId;
	}

	/**
	 * @return the testSessionId
	 */
	public String getTestSessionId() {
		return testSessionId;
	}

	/**
	 * @param testSessionId
	 *            the testSessionId to set
	 */
	public void setTestSessionId(String testSessionId) {
		this.testSessionId = testSessionId;
	}

	/**
	 * @return the rosters
	 */
	public ResetRosters[] getRosters() {
		return rosters;
	}

	/**
	 * @param rosters
	 *            the rosters to set
	 */
	public void setRosters(List<ResetRosters> rosters) {
		this.rosters = rosters.toArray(new ResetRosters[rosters.size()]);
	}
	
	/**
	 * @return the studentLogindNameMap
	 */
	public Map<String, String> getStudentLogindNameMap() {
		return studentLogindNameMap;
	}

	/**
	 * @param studentLogindNameMap the studentLogindNameMap to set
	 */
	public void setStudentLogindNameMap(Map<String, String> studentLogindNameMap) {
		this.studentLogindNameMap = studentLogindNameMap;
	}

	public static class ResetRosters implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String rosterId;
		private String subtestOrder;
		private String subtestId;
		private String errorDesc;
		private String isError;

		public ResetRosters() {

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
		 * @return the subtestOrder
		 */
		public String getSubtestOrder() {
			return subtestOrder;
		}

		/**
		 * @param subtestOrder
		 *            the subtestOrder to set
		 */
		public void setSubtestOrder(String subtestOrder) {
			this.subtestOrder = subtestOrder;
		}

		/**
		 * @return the subtestId
		 */
		public String getSubtestId() {
			return subtestId;
		}

		/**
		 * @param subtestId
		 *            the subtestId to set
		 */
		public void setSubtestId(String subtestId) {
			this.subtestId = subtestId;
		}

		/**
		 * @return the errorDesc
		 */
		public String getErrorDesc() {
			return errorDesc;
		}

		/**
		 * @param errorDesc
		 *            the errorDesc to set
		 */
		public void setErrorDesc(String errorDesc) {
			this.errorDesc = errorDesc;
		}

		/**
		 * @return the isError
		 */
		public String getIsError() {
			return isError;
		}

		/**
		 * @param isError
		 *            the isError to set
		 */
		public void setIsError(String isError) {
			this.isError = isError;
		}
		
		/**
		 * 
		 * @return the isError
		 */
		public boolean isError() {
			if (isError != null && "true".equals(isError)) {
				return true;
			} else
				return false;
		}

	}

}