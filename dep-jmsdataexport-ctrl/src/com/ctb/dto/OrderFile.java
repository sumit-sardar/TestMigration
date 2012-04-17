package com.ctb.dto;

public class OrderFile {

	private String customerId;
	private String customerName;
	private String customerStateAbbrevation;
	private String orgTestingProgram;
	private String scoringOrderNumber = "";
	private String tagNumber = "OAS";
	private String testName1;
	private String testName2 = "";
	private String testName3 = "";
	private String testDate;
	private String caseCount= "";
	private String reRunFlag = "";
	private String longitudinalFlag= "";
	private String reRosterFlag = "";
	private String dataFileName;
	private String customerContact;
	private String customerEmail;
	private String customerPhone;
	
	
	private String TB = "";
	private String hierarchyModeLocation= "519";
	private String specialCodeSelect = "";
	private String expectedTitles = "Big Shoulders";
	private String hierarchyModeLocation2= "520";
	private String specialCodeSelect2= "";
	private String expectedTitles2 = "City of Chicago";
	private String submittersEmail;
	private String maxSubtests= "";
	
	
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the customerStateAbbrevation
	 */
	public String getCustomerStateAbbrevation() {
		return customerStateAbbrevation;
	}
	/**
	 * @param customerStateAbbrevation the customerStateAbbrevation to set
	 */
	public void setCustomerStateAbbrevation(String customerStateAbbrevation) {
		this.customerStateAbbrevation = customerStateAbbrevation;
	}
	/**
	 * @return the orgTestingProgram
	 */
	public String getOrgTestingProgram() {
		return orgTestingProgram;
	}
	/**
	 * @param orgTestingProgram the orgTestingProgram to set
	 */
	public void setOrgTestingProgram(String orgTestingProgram) {
		this.orgTestingProgram = orgTestingProgram;
	}
	/**
	 * @return the scoringOrderNumber
	 */
	public String getScoringOrderNumber() {
		return scoringOrderNumber;
	}
	/**
	 * @param scoringOrderNumber the scoringOrderNumber to set
	 */
	public void setScoringOrderNumber(String scoringOrderNumber) {
		this.scoringOrderNumber = scoringOrderNumber;
	}
	/**
	 * @return the tagNumber
	 */
	public String getTagNumber() {
		return tagNumber;
	}
	/**
	 * @param tagNumber the tagNumber to set
	 */
	public void setTagNumber(String tagNumber) {
		this.tagNumber = tagNumber;
	}
	/**
	 * @return the testName1
	 */
	public String getTestName1() {
		return testName1;
	}
	/**
	 * @param testName1 the testName1 to set
	 */
	public void setTestName1(String testName1) {
		this.testName1 = testName1;
	}
	/**
	 * @return the testName2
	 */
	public String getTestName2() {
		return testName2;
	}
	/**
	 * @param testName2 the testName2 to set
	 */
	public void setTestName2(String testName2) {
		this.testName2 = testName2;
	}
	/**
	 * @return the testName3
	 */
	public String getTestName3() {
		return testName3;
	}
	/**
	 * @param testName3 the testName3 to set
	 */
	public void setTestName3(String testName3) {
		this.testName3 = testName3;
	}
	/**
	 * @return the testDate
	 */
	public String getTestDate() {
		return testDate;
	}
	/**
	 * @param testDate the testDate to set
	 */
	public void setTestDate(String testDate) {
		if(testDate.length()>6){
			testDate = testDate.substring(0,6);
		}
		testDate=String.format("%6s", testDate).replace(' ', '0');
		this.testDate = testDate;
	}
	/**
	 * @return the caseCount
	 */
	public String getCaseCount() {
		return caseCount;
	}
	/**
	 * @param caseCount the caseCount to set
	 */
	public void setCaseCount(String caseCount) {
		caseCount=String.format("%7s", caseCount).replace(' ', '0');
		this.caseCount = caseCount;
	}
	/**
	 * @return the reRunFlag
	 */
	public String getReRunFlag() {
		return reRunFlag;
	}
	/**
	 * @param reRunFlag the reRunFlag to set
	 */
	public void setReRunFlag(String reRunFlag) {
		this.reRunFlag = reRunFlag;
	}
	/**
	 * @return the longitudinalFlag
	 */
	public String getLongitudinalFlag() {
		return longitudinalFlag;
	}
	/**
	 * @param longitudinalFlag the longitudinalFlag to set
	 */
	public void setLongitudinalFlag(String longitudinalFlag) {
		this.longitudinalFlag = longitudinalFlag;
	}
	/**
	 * @return the reRosterFlag
	 */
	public String getReRosterFlag() {
		return reRosterFlag;
	}
	/**
	 * @param reRosterFlag the reRosterFlag to set
	 */
	public void setReRosterFlag(String reRosterFlag) {
		this.reRosterFlag = reRosterFlag;
	}
	/**
	 * @return the dataFileName
	 */
	public String getDataFileName() {
		return dataFileName;
	}
	/**
	 * @param dataFileName the dataFileName to set
	 */
	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}
	/**
	 * @return the customerContact
	 */
	public String getCustomerContact() {
		return customerContact;
	}
	/**
	 * @param customerContact the customerContact to set
	 */
	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}
	/**
	 * @return the customerEmail
	 */
	public String getCustomerEmail() {
		return customerEmail;
	}
	/**
	 * @param customerEmail the customerEmail to set
	 */
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	/**
	 * @return the customerPhone
	 */
	public String getCustomerPhone() {
		return customerPhone;
	}
	/**
	 * @param customerPhone the customerPhone to set
	 */
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	
	/**
	 * @return the tB
	 */
	public String getTB() {
		if(TB==null){
			return "";
		}
		if(TB.length()>2){
			return TB.substring(0,2);
		}else {
			//return String.format("%2s", TB);
			return TB;
		}
		
	}
	/**
	 * @param tb the tB to set
	 */
	public void setTB(String tb) {
		TB = tb;
	}
	/**
	 * @return the hierarchyModeLocation
	 */
	public String getHierarchyModeLocation() {
		if(hierarchyModeLocation==null){
			return "519";
		}
		if(hierarchyModeLocation.trim().length()>3){
			return hierarchyModeLocation.trim().substring(0,3);
		}else {
			//return String.format("%3s", hierarchyModeLocation);
			return hierarchyModeLocation.trim();
		}
	}
	/**
	 * @param hierarchyModeLocation the hierarchyModeLocation to set
	 */
	public void setHierarchyModeLocation(String hierarchyModeLocation) {
		this.hierarchyModeLocation = hierarchyModeLocation;
	}
	/**
	 * @return the specialCodeSelect
	 */
	public String getSpecialCodeSelect() {
		if(specialCodeSelect==null){
			return "";
		}
		if(specialCodeSelect.trim().length()>2){
			return specialCodeSelect.trim().substring(0,2);
		}else {
			//return String.format("%2s", specialCodeSelect);
			return specialCodeSelect.trim();
		}
	}
	/**
	 * @param specialCodeSelect the specialCodeSelect to set
	 */
	public void setSpecialCodeSelect(String specialCodeSelect) {
		this.specialCodeSelect = specialCodeSelect;
	}
	/**
	 * @return the expectedTitles
	 */
	public String getExpectedTitles() {
		if(expectedTitles==null){
			return "";
		}
		if(expectedTitles.trim().length()>50){
			return expectedTitles.trim().substring(0,50);
		}else {
			//return String.format("%50s", expectedTitles);
			return expectedTitles.trim();
		}
	}
	/**
	 * @param expectedTitles the expectedTitles to set
	 */
	public void setExpectedTitles(String expectedTitles) {
		this.expectedTitles = expectedTitles;
	}
	/**
	 * @return the hierarchyModeLocation2
	 */
	public String getHierarchyModeLocation2() {
		//return hierarchyModeLocation2;
		if(hierarchyModeLocation2 == null){
			return "520";
		}
		if(hierarchyModeLocation2.trim().length()>3){
			return hierarchyModeLocation2.trim().substring(0,3);
		}else {
			//return String.format("%3s", hierarchyModeLocation2);
			return hierarchyModeLocation2.trim();
		}
		
	}
	/**
	 * @param hierarchyModeLocation2 the hierarchyModeLocation2 to set
	 */
	public void setHierarchyModeLocation2(String hierarchyModeLocation2) {
		this.hierarchyModeLocation2 = hierarchyModeLocation2;
	}
	/**
	 * @return the specialCodeSelect2
	 */
	public String getSpecialCodeSelect2() {
		if(specialCodeSelect2==null){
			return "";
		}
		if(specialCodeSelect2.trim().length()>2){
			return specialCodeSelect2.trim().substring(0,2);
		}else {
			//return String.format("%2s", specialCodeSelect2);
			return specialCodeSelect2.trim();
		}
	}
	/**
	 * @param specialCodeSelect2 the specialCodeSelect2 to set
	 */
	public void setSpecialCodeSelect2(String specialCodeSelect2) {
		this.specialCodeSelect2 = specialCodeSelect2;
	}
	/**
	 * @return the expectedTitles2
	 */
	public String getExpectedTitles2() {
		if(expectedTitles2==null){
			return "";
		}
		if(expectedTitles2.trim().length()>50){
			return expectedTitles2.trim().substring(0,50);
		}else {
			//return String.format("%50s", expectedTitles2);
			return expectedTitles2.trim();
		}
	}
	/**
	 * @param expectedTitles2 the expectedTitles2 to set
	 */
	public void setExpectedTitles2(String expectedTitles2) {
		this.expectedTitles2 = expectedTitles2;
	}
	/**
	 * @return the submittersEmail
	 */
	public String getSubmittersEmail() {
		//return submittersEmail;
		if(submittersEmail==null){
			return "";
		}
		if(submittersEmail.trim().length()>64){
			return submittersEmail.trim().substring(0,64);
		}else {
			//return String.format("%64s", submittersEmail);
			return submittersEmail.trim();
		}
	}
	/**
	 * @param submittersEmail the submittersEmail to set
	 */
	public void setSubmittersEmail(String submittersEmail) {
		this.submittersEmail = submittersEmail;
	}
	/**
	 * @return the maxSubtests
	 */
	public String getMaxSubtests() {
		if(maxSubtests==null){
			return "";
		}
		if(maxSubtests.length()>2){
			return maxSubtests.substring(0,2);
		}else {
			//return String.format("%-2s", maxSubtests);
			return  maxSubtests;
		}
	}
	/**
	 * @param maxSubtests the maxSubtests to set
	 */
	public void setMaxSubtests(String maxSubtests) {
		this.maxSubtests = maxSubtests;
	}
}
