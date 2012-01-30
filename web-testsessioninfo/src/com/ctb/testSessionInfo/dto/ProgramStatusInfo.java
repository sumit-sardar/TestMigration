package com.ctb.testSessionInfo.dto;

import java.util.LinkedHashMap;
import java.util.List;

import com.ctb.bean.testAdmin.TestElement;

public class ProgramStatusInfo implements java.io.Serializable {
	static final long serialVersionUID = 1L;
	 
	Boolean hasClickableSubtest = Boolean.FALSE;
	private TestElement[] testList =null;
	private List subtestList = null;
	private List statusList = null;
	private SubtestStatus testStatus =null;
	private String programName = null;
	private String testName = null;
	private Integer selectedTestId = null;
	private Integer selectedProgramId = null;
	private boolean allowExport;
	private boolean noPrograms;
	private boolean multiplePrograms;
	private boolean noTest;
	private boolean singleTest;
	private boolean multipleTests;
	private String testStatusTitle;
	private String sessionsForTitle;
	private String viewSubtestStatus;
	private String page;
	private String total;
	private String records;
	 
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
	public boolean isNoPrograms() {
		return noPrograms;
	}
	public void setNoPrograms(boolean noPrograms) {
		this.noPrograms = noPrograms;
	}
	public boolean isMultiplePrograms() {
		return multiplePrograms;
	}
	public void setMultiplePrograms(boolean multiplePrograms) {
		this.multiplePrograms = multiplePrograms;
	}
	public boolean isNoTest() {
		return noTest;
	}
	public void setNoTest(boolean noTest) {
		this.noTest = noTest;
	}
	public boolean isSingleTest() {
		return singleTest;
	}
	public void setSingleTest(boolean singleTest) {
		this.singleTest = singleTest;
	}
	public boolean isMultipleTests() {
		return multipleTests;
	}
	public void setMultipleTests(boolean multipleTests) {
		this.multipleTests = multipleTests;
	}
	public String getTestStatusTitle() {
		return testStatusTitle;
	}
	public void setTestStatusTitle(String testStatusTitle) {
		this.testStatusTitle = testStatusTitle;
	}
	public String getSessionsForTitle() {
		return sessionsForTitle;
	}
	public void setSessionsForTitle(String sessionsForTitle) {
		this.sessionsForTitle = sessionsForTitle;
	}
	public String getViewSubtestStatus() {
		return viewSubtestStatus;
	}
	public void setViewSubtestStatus(String viewSubtestStatus) {
		this.viewSubtestStatus = viewSubtestStatus;
	}
	public boolean isAllowExport() {
		return allowExport;
	}
	public void setAllowExport(boolean allowExport) {
		this.allowExport = allowExport;
	}
	public Integer getSelectedProgramId() {
		return selectedProgramId;
	}
	public void setSelectedProgramId(Integer selectedProgramId) {
		this.selectedProgramId = selectedProgramId;
	}
	public Integer getSelectedTestId() {
		return selectedTestId;
	}
	public void setSelectedTestId(Integer selectedTestId) {
		this.selectedTestId = selectedTestId;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public SubtestStatus getTestStatus() {
		return testStatus;
	}
	public void setTestStatus(SubtestStatus testStatus) {
		this.testStatus = testStatus;
	}
	public Boolean getHasClickableSubtest() {
		return hasClickableSubtest;
	}
	public void setHasClickableSubtest(Boolean hasClickableSubtest) {
		this.hasClickableSubtest = hasClickableSubtest;
	}
	public List getSubtestList() {
		return subtestList;
	}
	public void setSubtestList(List subtestList) {
		this.subtestList = subtestList;
	}
	public List getStatusList() {
		return statusList;
	}
	public void setStatusList(List statusList) {
		this.statusList = statusList;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public TestElement[] getTestList() {
		return testList;
	}
	public void setTestList(TestElement[] testList) {
		this.testList = testList;
	}

}
