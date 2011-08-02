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
	
}
