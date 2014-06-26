package com.ctb.bean;

import java.util.Date;

public class StudentFileRow extends CTBBean{
	static final long serialVersionUID = 1L;
	private String key;
    private Integer studentId;
    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String displayStudentName;
    private String preferredName;
    private String prefix;
    private String suffix;
    private Date birthdate;
    private String headerDateOfBirth;
    private String gender;
    private String ethnicity;
    private String email;
    private String grade;
    private String extElmId;
    private String extPin1;
    private String extPin2;
    private String extPin3;
    private String extSchoolId;
    private String activeSession;
    private String potentialDuplicatedStudent;
    private Integer createdBy;
    private Date createdDateTime;
    private Integer updatedBy;
    private Date updatedDateTime;
    private String activationStatus;
    private Integer dataImportHistoryId;
    private Integer customerId;
    private Integer precodeId;
    private String barcode;
    private String udf;
    private String udf1;
    private String udf2;
    private String studentGrade;
    private String screenMagnifier;
    private String screenReader;
    private String calculator; 
    private String testPause;
    private String untimedTest;
    private String questionBackgroundColor;
    private String questionFontColor;
    private String questionFontSize;
    private String answerBackgroundColor;
    private String answerFontColor;
    private String answerFontSize;
    private String colorFontAccommodation;
    private String highlighter;
    private String contactName;
    private String address1;
    private String address2;
    private String address3;
    private String timeZone;
    private String city;
    private String state;
    private String zip;
    private String primaryPhone;
    private String secondaryPhone ;
    private String faxNumber;
    private Integer orgNodeId;
    private Node[] organizationNodes;
    private StudentDemoGraphics []studentDemoGraphics;
    private StudentDemoGraphicsData []studentDemoGraphicsData;
    private CustomerConfig []customerConfig ;
    private String primarySort;
    private String secondarySort;    
    private DataFileRowError[] dataFilerowError;
	/**
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the displayStudentName
	 */
	public String getDisplayStudentName() {
		return displayStudentName;
	}
	/**
	 * @param displayStudentName the displayStudentName to set
	 */
	public void setDisplayStudentName(String displayStudentName) {
		this.displayStudentName = displayStudentName;
	}
	/**
	 * @return the preferredName
	 */
	public String getPreferredName() {
		return preferredName;
	}
	/**
	 * @param preferredName the preferredName to set
	 */
	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}
	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}
	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	/**
	 * @return the birthdate
	 */
	public Date getBirthdate() {
		return birthdate;
	}
	/**
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	/**
	 * @return the headerDateOfBirth
	 */
	public String getHeaderDateOfBirth() {
		return headerDateOfBirth;
	}
	/**
	 * @param headerDateOfBirth the headerDateOfBirth to set
	 */
	public void setHeaderDateOfBirth(String headerDateOfBirth) {
		this.headerDateOfBirth = headerDateOfBirth;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the ethnicity
	 */
	public String getEthnicity() {
		return ethnicity;
	}
	/**
	 * @param ethnicity the ethnicity to set
	 */
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * @return the extElmId
	 */
	public String getExtElmId() {
		return extElmId;
	}
	/**
	 * @param extElmId the extElmId to set
	 */
	public void setExtElmId(String extElmId) {
		this.extElmId = extElmId;
	}
	/**
	 * @return the extPin1
	 */
	public String getExtPin1() {
		return extPin1;
	}
	/**
	 * @param extPin1 the extPin1 to set
	 */
	public void setExtPin1(String extPin1) {
		this.extPin1 = extPin1;
	}
	/**
	 * @return the extPin2
	 */
	public String getExtPin2() {
		return extPin2;
	}
	/**
	 * @param extPin2 the extPin2 to set
	 */
	public void setExtPin2(String extPin2) {
		this.extPin2 = extPin2;
	}
	/**
	 * @return the extPin3
	 */
	public String getExtPin3() {
		return extPin3;
	}
	/**
	 * @param extPin3 the extPin3 to set
	 */
	public void setExtPin3(String extPin3) {
		this.extPin3 = extPin3;
	}
	/**
	 * @return the extSchoolId
	 */
	public String getExtSchoolId() {
		return extSchoolId;
	}
	/**
	 * @param extSchoolId the extSchoolId to set
	 */
	public void setExtSchoolId(String extSchoolId) {
		this.extSchoolId = extSchoolId;
	}
	/**
	 * @return the activeSession
	 */
	public String getActiveSession() {
		return activeSession;
	}
	/**
	 * @param activeSession the activeSession to set
	 */
	public void setActiveSession(String activeSession) {
		this.activeSession = activeSession;
	}
	/**
	 * @return the potentialDuplicatedStudent
	 */
	public String getPotentialDuplicatedStudent() {
		return potentialDuplicatedStudent;
	}
	/**
	 * @param potentialDuplicatedStudent the potentialDuplicatedStudent to set
	 */
	public void setPotentialDuplicatedStudent(String potentialDuplicatedStudent) {
		this.potentialDuplicatedStudent = potentialDuplicatedStudent;
	}
	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the createdDateTime
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	/**
	 * @return the updatedBy
	 */
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return the updatedDateTime
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime the updatedDateTime to set
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	/**
	 * @return the activationStatus
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus the activationStatus to set
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	/**
	 * @return the dataImportHistoryId
	 */
	public Integer getDataImportHistoryId() {
		return dataImportHistoryId;
	}
	/**
	 * @param dataImportHistoryId the dataImportHistoryId to set
	 */
	public void setDataImportHistoryId(Integer dataImportHistoryId) {
		this.dataImportHistoryId = dataImportHistoryId;
	}
	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the precodeId
	 */
	public Integer getPrecodeId() {
		return precodeId;
	}
	/**
	 * @param precodeId the precodeId to set
	 */
	public void setPrecodeId(Integer precodeId) {
		this.precodeId = precodeId;
	}
	/**
	 * @return the barcode
	 */
	public String getBarcode() {
		return barcode;
	}
	/**
	 * @param barcode the barcode to set
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	/**
	 * @return the udf
	 */
	public String getUdf() {
		return udf;
	}
	/**
	 * @param udf the udf to set
	 */
	public void setUdf(String udf) {
		this.udf = udf;
	}
	/**
	 * @return the udf1
	 */
	public String getUdf1() {
		return udf1;
	}
	/**
	 * @param udf1 the udf1 to set
	 */
	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}
	/**
	 * @return the udf2
	 */
	public String getUdf2() {
		return udf2;
	}
	/**
	 * @param udf2 the udf2 to set
	 */
	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}
	/**
	 * @return the studentGrade
	 */
	public String getStudentGrade() {
		return studentGrade;
	}
	/**
	 * @param studentGrade the studentGrade to set
	 */
	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}
	/**
	 * @return the screenMagnifier
	 */
	public String getScreenMagnifier() {
		return screenMagnifier;
	}
	/**
	 * @param screenMagnifier the screenMagnifier to set
	 */
	public void setScreenMagnifier(String screenMagnifier) {
		this.screenMagnifier = screenMagnifier;
	}
	/**
	 * @return the screenReader
	 */
	public String getScreenReader() {
		return screenReader;
	}
	/**
	 * @param screenReader the screenReader to set
	 */
	public void setScreenReader(String screenReader) {
		this.screenReader = screenReader;
	}
	/**
	 * @return the calculator
	 */
	public String getCalculator() {
		return calculator;
	}
	/**
	 * @param calculator the calculator to set
	 */
	public void setCalculator(String calculator) {
		this.calculator = calculator;
	}
	/**
	 * @return the testPause
	 */
	public String getTestPause() {
		return testPause;
	}
	/**
	 * @param testPause the testPause to set
	 */
	public void setTestPause(String testPause) {
		this.testPause = testPause;
	}
	/**
	 * @return the untimedTest
	 */
	public String getUntimedTest() {
		return untimedTest;
	}
	/**
	 * @param untimedTest the untimedTest to set
	 */
	public void setUntimedTest(String untimedTest) {
		this.untimedTest = untimedTest;
	}
	/**
	 * @return the questionBackgroundColor
	 */
	public String getQuestionBackgroundColor() {
		return questionBackgroundColor;
	}
	/**
	 * @param questionBackgroundColor the questionBackgroundColor to set
	 */
	public void setQuestionBackgroundColor(String questionBackgroundColor) {
		this.questionBackgroundColor = questionBackgroundColor;
	}
	/**
	 * @return the questionFontColor
	 */
	public String getQuestionFontColor() {
		return questionFontColor;
	}
	/**
	 * @param questionFontColor the questionFontColor to set
	 */
	public void setQuestionFontColor(String questionFontColor) {
		this.questionFontColor = questionFontColor;
	}
	/**
	 * @return the questionFontSize
	 */
	public String getQuestionFontSize() {
		return questionFontSize;
	}
	/**
	 * @param questionFontSize the questionFontSize to set
	 */
	public void setQuestionFontSize(String questionFontSize) {
		this.questionFontSize = questionFontSize;
	}
	/**
	 * @return the answerBackgroundColor
	 */
	public String getAnswerBackgroundColor() {
		return answerBackgroundColor;
	}
	/**
	 * @param answerBackgroundColor the answerBackgroundColor to set
	 */
	public void setAnswerBackgroundColor(String answerBackgroundColor) {
		this.answerBackgroundColor = answerBackgroundColor;
	}
	/**
	 * @return the answerFontColor
	 */
	public String getAnswerFontColor() {
		return answerFontColor;
	}
	/**
	 * @param answerFontColor the answerFontColor to set
	 */
	public void setAnswerFontColor(String answerFontColor) {
		this.answerFontColor = answerFontColor;
	}
	/**
	 * @return the answerFontSize
	 */
	public String getAnswerFontSize() {
		return answerFontSize;
	}
	/**
	 * @param answerFontSize the answerFontSize to set
	 */
	public void setAnswerFontSize(String answerFontSize) {
		this.answerFontSize = answerFontSize;
	}
	/**
	 * @return the colorFontAccommodation
	 */
	public String getColorFontAccommodation() {
		return colorFontAccommodation;
	}
	/**
	 * @param colorFontAccommodation the colorFontAccommodation to set
	 */
	public void setColorFontAccommodation(String colorFontAccommodation) {
		this.colorFontAccommodation = colorFontAccommodation;
	}
	/**
	 * @return the highlighter
	 */
	public String getHighlighter() {
		return highlighter;
	}
	/**
	 * @param highlighter the highlighter to set
	 */
	public void setHighlighter(String highlighter) {
		this.highlighter = highlighter;
	}
	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}
	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}
	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}
	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	/**
	 * @return the address3
	 */
	public String getAddress3() {
		return address3;
	}
	/**
	 * @param address3 the address3 to set
	 */
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the primaryPhone
	 */
	public String getPrimaryPhone() {
		return primaryPhone;
	}
	/**
	 * @param primaryPhone the primaryPhone to set
	 */
	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}
	/**
	 * @return the secondaryPhone
	 */
	public String getSecondaryPhone() {
		return secondaryPhone;
	}
	/**
	 * @param secondaryPhone the secondaryPhone to set
	 */
	public void setSecondaryPhone(String secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}
	/**
	 * @return the faxNumber
	 */
	public String getFaxNumber() {
		return faxNumber;
	}
	/**
	 * @param faxNumber the faxNumber to set
	 */
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	/**
	 * @return the orgNodeId
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	/**
	 * @return the organizationNodes
	 */
	public Node[] getOrganizationNodes() {
		return organizationNodes;
	}
	/**
	 * @param organizationNodes the organizationNodes to set
	 */
	public void setOrganizationNodes(Node[] organizationNodes) {
		this.organizationNodes = organizationNodes;
	}
	/**
	 * @return the studentDemoGraphics
	 */
	public StudentDemoGraphics[] getStudentDemoGraphics() {
		return studentDemoGraphics;
	}
	/**
	 * @param studentDemoGraphics the studentDemoGraphics to set
	 */
	public void setStudentDemoGraphics(StudentDemoGraphics[] studentDemoGraphics) {
		this.studentDemoGraphics = studentDemoGraphics;
	}
	/**
	 * @return the studentDemoGraphicsData
	 */
	public StudentDemoGraphicsData[] getStudentDemoGraphicsData() {
		return studentDemoGraphicsData;
	}
	/**
	 * @param studentDemoGraphicsData the studentDemoGraphicsData to set
	 */
	public void setStudentDemoGraphicsData(
			StudentDemoGraphicsData[] studentDemoGraphicsData) {
		this.studentDemoGraphicsData = studentDemoGraphicsData;
	}
	/**
	 * @return the customerConfig
	 */
	public CustomerConfig[] getCustomerConfig() {
		return customerConfig;
	}
	/**
	 * @param customerConfig the customerConfig to set
	 */
	public void setCustomerConfig(CustomerConfig[] customerConfig) {
		this.customerConfig = customerConfig;
	}
	/**
	 * @return the primarySort
	 */
	public String getPrimarySort() {
		return primarySort;
	}
	/**
	 * @param primarySort the primarySort to set
	 */
	public void setPrimarySort(String primarySort) {
		this.primarySort = primarySort;
	}
	/**
	 * @return the secondarySort
	 */
	public String getSecondarySort() {
		return secondarySort;
	}
	/**
	 * @param secondarySort the secondarySort to set
	 */
	public void setSecondarySort(String secondarySort) {
		this.secondarySort = secondarySort;
	}
	/**
	 * @return the dataFilerowError
	 */
	public DataFileRowError[] getDataFilerowError() {
		return dataFilerowError;
	}
	/**
	 * @param dataFilerowError the dataFilerowError to set
	 */
	public void setDataFilerowError(DataFileRowError[] dataFilerowError) {
		this.dataFilerowError = dataFilerowError;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
    
    
}
