package com.ctb.testSessionInfo.utils; 

import java.util.List;

import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.testSessionInfo.dto.TestSessionVO;
import com.ctb.testSessionInfo.dto.UserProfileInformation;



public class Base {
	
	private String page;
	private String total;
	private String records;
	private List<Row> rows;
	private List<TestSessionVO> testSessionCUFU;
	private List<TestSessionVO> testSessionPA;
	private List studentNode;
	private OrgNodeCategory orgNodeCategory;
	private List<com.ctb.testSessionInfo.dto.UserProfileInformation> userProfileInformation;
	private List gradeList;
	/**
	 * @return the orgNodeCategory
	 */
	public OrgNodeCategory getOrgNodeCategory() {
		return orgNodeCategory;
	}
	/**
	 * @param orgNodeCategory the orgNodeCategory to set
	 */
	public void setOrgNodeCategory(OrgNodeCategory orgNodeCategory) {
		this.orgNodeCategory = orgNodeCategory;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getRecords() {
		return records;
	}
	public void setRecords(String records) {
		this.records = records;
	}
	public List<Row> getRows() {
		return rows;
	}
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	/**
	 * @return the testSessionCUFU
	 */
	public List<TestSessionVO> getTestSessionCUFU() {
		return testSessionCUFU;
	}
	/**
	 * @param testSessionCUFU the testSessionCUFU to set
	 */
	public void setTestSessionCUFU(List<TestSessionVO> testSessionCUFU) {
		this.testSessionCUFU = testSessionCUFU;
	}
	/**
	 * @return the testSessionPA
	 */
	public List<TestSessionVO> getTestSessionPA() {
		return testSessionPA;
	}
	/**
	 * @param testSessionPA the testSessionPA to set
	 */
	public void setTestSessionPA(List<TestSessionVO> testSessionPA) {
		this.testSessionPA = testSessionPA;
	}
	/**
	 * @return the studentNode
	 */
	public List getStudentNode() {
		return studentNode;
	}
	/**
	 * @param studentNode the studentNode to set
	 */
	public void setStudentNode(List studentNode) {
		this.studentNode = studentNode;
	}
	/**
	 * @return the userProfileInformation
	 */
	public List<UserProfileInformation> getUserProfileInformation() {
		return userProfileInformation;
	}
	/**
	 * @param userProfileInformation the userProfileInformation to set
	 */
	public void setUserProfileInformation(
			List<UserProfileInformation> userProfileInformation) {
		this.userProfileInformation = userProfileInformation;
	}
	/**
	 * @param gradeList the gradeList to set
	 */
	public void setGradeList(List gradeList) {
		this.gradeList = gradeList;
	}
	/**
	 * @return the gradeList
	 */
	public List getGradeList() {
		return gradeList;
	}

}
