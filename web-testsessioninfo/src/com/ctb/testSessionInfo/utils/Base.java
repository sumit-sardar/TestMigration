package com.ctb.testSessionInfo.utils; 

import java.util.List;
import java.util.Map;

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
	private List rosterElement;
	private List testElement;
	private String hasBreak;
	private TestSessionVO testSession;
	private String loginName;
	private String studentName;
	private String password;
	private String testSessionName;
	private String testName;
	private String testStatus;
	private String testGrade;
	private String testLevel;
	private String response;
	private boolean isShowScores;
	private boolean isTabeSession;
	private int numberColumn;
	private boolean subtestValidationAllowed;
	private boolean isLaslinkSession;
	private boolean donotScoreAllowed;
	Map<Integer,Map> sessionListPAMap;
	Map<Integer,Map> sessionListCUFUMap;
	Map<Integer,Map> accomodationMap;
	
	public boolean isDonotScoreAllowed() {
		return donotScoreAllowed;
	}
	public void setDonotScoreAllowed(boolean donotScoreAllowed) {
		this.donotScoreAllowed = donotScoreAllowed;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public List getRosterElement() {
		return rosterElement;
	}
	public void setRosterElement(List rosterElement) {
		this.rosterElement = rosterElement;
	}
	public List getTestElement() {
		return testElement;
	}
	public void setTestElement(List testElement) {
		this.testElement = testElement;
	}
	public String getHasBreak() {
		return hasBreak;
	}
	public void setHasBreak(String hasBreak) {
		this.hasBreak = hasBreak;
	}
	public TestSessionVO getTestSession() {
		return testSession;
	}
	public void setTestSession(TestSessionVO testSession) {
		this.testSession = testSession;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTestSessionName() {
		return testSessionName;
	}
	public void setTestSessionName(String testSessionName) {
		this.testSessionName = testSessionName;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getTestStatus() {
		return testStatus;
	}
	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}
	public String getTestGrade() {
		return testGrade;
	}
	public void setTestGrade(String testGrade) {
		this.testGrade = testGrade;
	}
	public String getTestLevel() {
		return testLevel;
	}
	public void setTestLevel(String testLevel) {
		this.testLevel = testLevel;
	}
	public boolean isShowScores() {
		return isShowScores;
	}
	public void setShowScores(boolean isShowScores) {
		this.isShowScores = isShowScores;
	}
	public boolean isTabeSession() {
		return isTabeSession;
	}
	public void setTabeSession(boolean isTabeSession) {
		this.isTabeSession = isTabeSession;
	}
	public int getNumberColumn() {
		return numberColumn;
	}
	public void setNumberColumn(int numberColumn) {
		this.numberColumn = numberColumn;
	}
	public boolean isSubtestValidationAllowed() {
		return subtestValidationAllowed;
	}
	public void setSubtestValidationAllowed(boolean subtestValidationAllowed) {
		this.subtestValidationAllowed = subtestValidationAllowed;
	}
	public boolean isLaslinkSession() {
		return isLaslinkSession;
	}
	public void setLaslinkSession(boolean isLaslinkSession) {
		this.isLaslinkSession = isLaslinkSession;
	}
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
	public Map<Integer, Map> getSessionListPAMap() {
		return sessionListPAMap;
	}
	public void setSessionListPAMap(Map<Integer, Map> sessionListPAMap) {
		this.sessionListPAMap = sessionListPAMap;
	}
	public Map<Integer, Map> getSessionListCUFUMap() {
		return sessionListCUFUMap;
	}
	public void setSessionListCUFUMap(Map<Integer, Map> sessionListCUFUMap) {
		this.sessionListCUFUMap = sessionListCUFUMap;
	}
	public Map<Integer, Map> getAccomodationMap() {
		return accomodationMap;
	}
	public void setAccomodationMap(Map<Integer, Map> accomodationMap) {
		this.accomodationMap = accomodationMap;
	}

}
