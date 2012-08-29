package dto;

import java.util.List;

import com.ctb.bean.testAdmin.TestElement;
import com.ctb.util.OperationStatus;

public class RapidRegistrationVO {
	
	private OperationStatus status ;
	private String studentId;
	private String studentName;
	private String studentOrgId;
	private String password;
	private String loginName;
	
	private String testAdminId;
	private String testName; 
	private String testAdminName;
	private String testAccessCode;
	private String sessionNumber;
	private String creatorOrgNodeName;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private boolean showAccessCode;
	private Boolean hasLocatorSubtest ;
	private boolean isLocatorTest;
	private String enforceBreak;
	private List selectedProctors;
	private List selectedSubtests = null;
	private boolean  autoLocator;
	private TestElement locatorSubtest;
	private String autoLocatorDisplay;
	private boolean assessmentHasLocator;
	//private String ;
	

	/**
	 * @return the status
	 */
	public OperationStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(OperationStatus status) {
		this.status = status;
	}

	/**
	 * @return the studentId
	 */
	public String getStudentId() {
		return studentId;
	}

	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	/**
	 * @return the studentName
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * @param studentName the studentName to set
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	/**
	 * @return the studentOrgId
	 */
	public String getStudentOrgId() {
		return studentOrgId;
	}

	/**
	 * @param studentOrgId the studentOrgId to set
	 */
	public void setStudentOrgId(String studentOrgId) {
		this.studentOrgId = studentOrgId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return the testAdminId
	 */
	public String getTestAdminId() {
		return testAdminId;
	}

	/**
	 * @param testAdminId the testAdminId to set
	 */
	public void setTestAdminId(String testAdminId) {
		this.testAdminId = testAdminId;
	}

	/**
	 * @return the testName
	 */
	public String getTestName() {
		return testName;
	}

	/**
	 * @param testName the testName to set
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}

	/**
	 * @return the testAdminName
	 */
	public String getTestAdminName() {
		return testAdminName;
	}

	/**
	 * @param testAdminName the testAdminName to set
	 */
	public void setTestAdminName(String testAdminName) {
		this.testAdminName = testAdminName;
	}

	/**
	 * @return the testAccessCode
	 */
	public String getTestAccessCode() {
		return testAccessCode;
	}

	/**
	 * @param testAccessCode the testAccessCode to set
	 */
	public void setTestAccessCode(String testAccessCode) {
		this.testAccessCode = testAccessCode;
	}

	/**
	 * @return the sessionNumber
	 */
	public String getSessionNumber() {
		return sessionNumber;
	}

	/**
	 * @param sessionNumber the sessionNumber to set
	 */
	public void setSessionNumber(String sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

	/**
	 * @return the creatorOrgNodeName
	 */
	public String getCreatorOrgNodeName() {
		return creatorOrgNodeName;
	}

	/**
	 * @param creatorOrgNodeName the creatorOrgNodeName to set
	 */
	public void setCreatorOrgNodeName(String creatorOrgNodeName) {
		this.creatorOrgNodeName = creatorOrgNodeName;
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
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the showAccessCode
	 */
	public boolean getShowAccessCode() {
		return showAccessCode;
	}

	/**
	 * @param showAccessCode the showAccessCode to set
	 */
	public void setShowAccessCode(boolean showAccessCode) {
		this.showAccessCode = showAccessCode;
	}

	/**
	 * @return the hasLocatorSubtest
	 */
	public Boolean getHasLocatorSubtest() {
		return hasLocatorSubtest;
	}

	/**
	 * @param hasLocatorSubtest the hasLocatorSubtest to set
	 */
	public void setHasLocatorSubtest(Boolean hasLocatorSubtest) {
		this.hasLocatorSubtest = hasLocatorSubtest;
	}

	/**
	 * @return the isLocatorTest
	 */
	public boolean isLocatorTest() {
		return isLocatorTest;
	}

	/**
	 * @param isLocatorTest the isLocatorTest to set
	 */
	public void setLocatorTest(boolean isLocatorTest) {
		this.isLocatorTest = isLocatorTest;
	}

	/**
	 * @return the enforceBreak
	 */
	public String getEnforceBreak() {
		return enforceBreak;
	}

	/**
	 * @param enforceBreak the enforceBreak to set
	 */
	public void setEnforceBreak(String enforceBreak) {
		this.enforceBreak = enforceBreak;
	}

	/**
	 * @return the selectedProctors
	 */
	public List getSelectedProctors() {
		return selectedProctors;
	}

	/**
	 * @param selectedProctors the selectedProctors to set
	 */
	public void setSelectedProctors(List selectedProctors) {
		this.selectedProctors = selectedProctors;
	}

	/**
	 * @return the selectedSubtests
	 */
	public List getSelectedSubtests() {
		return selectedSubtests;
	}

	/**
	 * @param selectedSubtests the selectedSubtests to set
	 */
	public void setSelectedSubtests(List selectedSubtests) {
		this.selectedSubtests = selectedSubtests;
	}

	/**
	 * @return the autoLocator
	 */
	public boolean isAutoLocator() {
		return autoLocator;
	}

	/**
	 * @param autoLocator the autoLocator to set
	 */
	public void setAutoLocator(boolean autoLocator) {
		this.autoLocator = autoLocator;
	}

	/**
	 * @return the locatorSubtest
	 */
	public TestElement getLocatorSubtest() {
		return locatorSubtest;
	}

	/**
	 * @param locatorSubtest the locatorSubtest to set
	 */
	public void setLocatorSubtest(TestElement locatorSubtest) {
		this.locatorSubtest = locatorSubtest;
	}

	/**
	 * @return the autoLocatorDisplay
	 */
	public String getAutoLocatorDisplay() {
		return autoLocatorDisplay;
	}

	/**
	 * @param autoLocatorDisplay the autoLocatorDisplay to set
	 */
	public void setAutoLocatorDisplay(String autoLocatorDisplay) {
		this.autoLocatorDisplay = autoLocatorDisplay;
	}

	/**
	 * @return the assessmentHasLocator
	 */
	public boolean isAssessmentHasLocator() {
		return assessmentHasLocator;
	}

	/**
	 * @param assessmentHasLocator the assessmentHasLocator to set
	 */
	public void setAssessmentHasLocator(boolean assessmentHasLocator) {
		this.assessmentHasLocator = assessmentHasLocator;
	}
	

}
