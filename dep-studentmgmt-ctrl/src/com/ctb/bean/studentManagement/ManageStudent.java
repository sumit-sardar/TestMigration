package com.ctb.bean.studentManagement; 

import java.util.Date;

import com.ctb.bean.CTBBean;
import com.ctb.bean.testAdmin.StudentAccommodations;

/**
 * Data bean representing the partial contents of the OAS.STUDENT table 
 * and the student's assigned org nodes.
 * 
 * @author John_Wang
 */
public class ManageStudent extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer id;
    private String loginId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String studentName;
    private String studentIdNumber;
    private String studentIdNumber2;
    private String grade;
    private String productNameList;
    private String gender;
    private String scoringStatus;
    private Date birthDate;
    private Integer createdBy;
    private OrganizationNode [] organizationNodes;
    private String extElmId;//Bulk Accommodation
    //START-Added for HandScoring
    private Integer rosterId;
    private String testSessionName;
    private Integer  itemSetIdTC;
    private String accessCode;
    private String testAdminId;
    private Integer itemCountCRAI;
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
    private String colorFont;//bulk accommodation new field
    private String highlighter;
    private String maskingRuler;//Added for masking
    private String auditoryCalming;//Added for auditory calming
    private String musicFile;//Added for auditory calming
    private String magnifyingGlass;//Added for magnifier
    private String extendedTime; // Added for student pacing
    private String maskingTool;
    //END-Added for HandScoring  
    
    //Start- added for  Process Scores  button
    private Boolean isSuccess;
    private String completionStatus;
    //end - added for  Process Scores  button
   
    
    //START- (LLO82) StudentManagement Changes For LasLink product
    private String testPurpose;
    //END- (LLO82) StudentManagement Changes For LasLink product
    
    private String orgNodeName;
    private Integer orgNodeId;
    private String orgIdList;
    private String orgNameList;
    private String completionStatusTD;
    //Added for out of school
    private String outOfSchool;
    //Added for new UI hand scoring
    private String testCatalogName;
    
    
    private String completedContentArea;
    private Date testWindowOpenDate;
    private String form;
    private String proficiencyLevel;
    private String organizationNames;
    private String contentAreaString;
    private String defaultScheduler;
    private Date administrationDate; // added for Immediate Report
    
    private String isSessionStudent;
    private String orgIdNameList;
    
	/**
	 * @return the orgIdNameList
	 */
	public String getOrgIdNameList() {
		return orgIdNameList;
	}
	/**
	 * @param orgIdNameList the orgIdNameList to set
	 */
	public void setOrgIdNameList(String orgIdNameList) {
		this.orgIdNameList = orgIdNameList;
	}
	/**
	 * @return the testPurpose
	 */
	public String getTestPurpose() {
		return testPurpose;
	}
	/**
	 * @param testPurpose the testPurpose to set
	 */
	public void setTestPurpose(String testPurpose) {
		this.testPurpose = testPurpose;
	}
	/**
	 * @return the accessCode
	 */
	public String getAccessCode() {
		return accessCode;
	}
	/**
	 * @param accessCode the accessCode to set
	 */
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	
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
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}
	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
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
	 * @return the organizationNodes
	 */
	public OrganizationNode[] getOrganizationNodes() {
		return organizationNodes;
	}
	/**
	 * @param organizationNodes the organizationNodes to set
	 */
	public void setOrganizationNodes(OrganizationNode[] organizationNodes) {
		this.organizationNodes = organizationNodes;
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
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}
	
	public String getProductNameList() {
		return productNameList;
	}
	public void setProductNameList(String productNameList) {
		this.productNameList = productNameList;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	public String getScoringStatus() {
		return scoringStatus;
	}
	/**
	 * @param scoringStatus the scoringStatus to set
	 */
	public void setScoringStatus(String scoringStatus) {
		this.scoringStatus = scoringStatus;
	}
	/**
	 * @return the studentIdNumber
	 */
	public String getStudentIdNumber() {
		return studentIdNumber;
	}
	/**
	 * @param studentIdNumber the studentIdNumber to set
	 */
	public void setStudentIdNumber(String studentIdNumber) {
		this.studentIdNumber = studentIdNumber;
	}
	/**
	 * @return the studentIdNumber2
	 */
	public String getStudentIdNumber2() {
		return studentIdNumber2;
	}
	/**
	 * @param studentIdNumber2 the studentIdNumber2 to set
	 */
	public void setStudentIdNumber2(String studentIdNumber2) {
		this.studentIdNumber2 = studentIdNumber2;
	}
	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}    
	/**
	 * @return Returns the createdBy.
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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
	 * @return Return RosterId
	 */
	public Integer getRosterId() {
		return rosterId;
	}
	/**
	 * @param rosterId - rosterId
	 */
	public void setRosterId(Integer rosterId) {
		this.rosterId = rosterId;
	}
	/**
	 * @return the itemSetIdTC
	 */
	public Integer getItemSetIdTC() {
		return itemSetIdTC;
	}
	/**
	 * @param itemSetIdTC the itemSetIdTC to set
	 */
	public void setItemSetIdTC(Integer itemSetIdTC) {
		this.itemSetIdTC = itemSetIdTC;
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
	 * @return the itemCountCRAI
	 */
	public Integer getItemCountCRAI() {
		return itemCountCRAI;
	}
	/**
	 * @param itemCountCRAI the itemCountCRAI to set
	 */
	public void setItemCountCRAI(Integer itemCountCRAI) {
		this.itemCountCRAI = itemCountCRAI;
	}
	/**
	 * @return the isSuccess
	 */
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	/**
	 * @param isSuccess the isSuccess to set
	 */
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	/**
	 * @return the completionStatus
	 */
	public String getCompletionStatus() {
		return completionStatus;
	}
	/**
	 * @param completionStatus the completionStatus to set
	 */
	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
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
	 * @return the colorFont
	 */
	public String getColorFont() {
		return colorFont;
	}
	/**
	 * @param colorFont the colorFont to set
	 */
	public void setColorFont(String colorFont) {
		this.colorFont = colorFont;
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
	 * @return the maskingRuler
	 */
	public String getMaskingRuler() {
		return maskingRuler;
	}
	/**
	 * @param maskingRuler the maskingRuler to set
	 */
	public void setMaskingRuler(String maskingRuler) {
		this.maskingRuler = maskingRuler;
	}
	/**
	 * @return the auditoryCalming
	 */
	public String getAuditoryCalming() {
		return auditoryCalming;
	}
	/**
	 * @param auditoryCalming the auditoryCalming to set
	 */
	public void setAuditoryCalming(String auditoryCalming) {
		this.auditoryCalming = auditoryCalming;
	}
	/**
	 * @return the musicFile
	 */
	public String getMusicFile() {
		return musicFile;
	}
	/**
	 * @param musicFile the musicFile to set
	 */
	public void setMusicFile(String musicFile) {
		this.musicFile = musicFile;
	}
	/**
	 * @return the magnifyingGlass
	 */
	public String getMagnifyingGlass() {
		return magnifyingGlass;
	}
	/**
	 * @param magnifyingGlass the magnifyingGlass to set
	 */
	public void setMagnifyingGlass(String magnifyingGlass) {
		this.magnifyingGlass = magnifyingGlass;
	}
	/**
	 * @return the extendedTime
	 */
	public String getExtendedTime() {
		return extendedTime;
	}
	/**
	 * @param extendedTime the extendedTime to set
	 */
	public void setExtendedTime(String extendedTime) {
		this.extendedTime = extendedTime;
	}
	/**
	 * @return the maskingTool
	 */
	public String getMaskingTool() {
		return maskingTool;
	}
	/**
	 * @param maskingTool the maskingTool to set
	 */
	public void setMaskingTool(String maskingTool) {
		this.maskingTool = maskingTool;
	}
	public String getOrgNodeName() {
		return orgNodeName;
	}
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	
	/**
	 * @return the orgIdList
	 */
	public String getOrgIdList() {
		return orgIdList;
	}
	
	/**
	 * @param orgIdList the orgIdList to set
	 */
	public void setOrgIdList(String orgIdList) {
		this.orgIdList = orgIdList;
	}
	
	/**
	 * @return the orgNameList
	 */
	public String getOrgNameList() {
		return orgNameList;
	}
	
	/**
	 * @param orgNameList the orgNameList to set
	 */
	public void setOrgNameList(String orgNameList) {
		this.orgNameList = orgNameList;
	}
	public String getOutOfSchool() {
		return outOfSchool;
	}
	public void setOutOfSchool(String outOfSchool) {
		this.outOfSchool = outOfSchool;
	}
	public String getCompletionStatusTD() {
		return completionStatusTD;
	}
	public void setCompletionStatusTD(String completionStatusTD) {
		this.completionStatusTD = completionStatusTD;
	}
	public String getTestCatalogName() {
		return testCatalogName;
	}
	public void setTestCatalogName(String testCatalogName) {
		this.testCatalogName = testCatalogName;
	}
	/**
	 * @return the completedContentArea
	 */
	public String getCompletedContentArea() {
		return completedContentArea;
	}
	/**
	 * @param completedContentArea the completedContentArea to set
	 */
	public void setCompletedContentArea(String completedContentArea) {
		this.completedContentArea = completedContentArea;
	}
	/**
	 * @return the testWindowOpenDate
	 */
	public Date getTestWindowOpenDate() {
		return testWindowOpenDate;
	}
	/**
	 * @param testWindowOpenDate the testWindowOpenDate to set
	 */
	public void setTestWindowOpenDate(Date testWindowOpenDate) {
		this.testWindowOpenDate = testWindowOpenDate;
	}
	/**
	 * @return the form
	 */
	public String getForm() {
		return form;
	}
	/**
	 * @param form the form to set
	 */
	public void setForm(String form) {
		this.form = form;
	}
	/**
	 * @return the proficiencyLevel
	 */
	public String getProficiencyLevel() {
		return proficiencyLevel;
	}
	/**
	 * @param proficiencyLevel the proficiencyLevel to set
	 */
	public void setProficiencyLevel(String proficiencyLevel) {
		this.proficiencyLevel = proficiencyLevel;
	}
	/**
	 * @return the organizationNames
	 */
	public String getOrganizationNames() {
		return organizationNames;
	}
	/**
	 * @param organizationNames the organizationNames to set
	 */
	public void setOrganizationNames(String organizationNames) {
		this.organizationNames = organizationNames;
	}
	/**
	 * @return the contentAreaString
	 */
	public String getContentAreaString() {
		return contentAreaString;
	}
	/**
	 * @param contentAreaString the contentAreaString to set
	 */
	public void setContentAreaString(String contentAreaString) {
		this.contentAreaString = contentAreaString;
	}
	/**
	 * @return the defaultScheduler
	 */
	public String getDefaultScheduler() {
		return defaultScheduler;
	}
	/**
	 * @param defaultScheduler the defaultScheduler to set
	 */
	public void setDefaultScheduler(String defaultScheduler) {
		this.defaultScheduler = defaultScheduler;
	}
	/**
	 * @return the administrationDate
	 */
	public Date getAdministrationDate() {
		return administrationDate;
	}
	/**
	 * @param administrationDate the administrationDate to set
	 */
	public void setAdministrationDate(Date administrationDate) {
		this.administrationDate = administrationDate;
	}
	/**
	 * @return the isSessionStudent
	 */
	public String getIsSessionStudent() {
		return isSessionStudent;
	}
	/**
	 * @param isSessionStudent the isSessionStudent to set
	 */
	public void setIsSessionStudent(String isSessionStudent) {
		this.isSessionStudent = isSessionStudent;
	}
	
    
} 

