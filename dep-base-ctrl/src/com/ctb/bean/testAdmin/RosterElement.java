package com.ctb.bean.testAdmin; 

import java.util.ArrayList;
import java.util.Date;

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the contents of the OAS.TEST_ROSTER table
 * 
 * @author Nate_Cohen
 */
public class RosterElement extends CTBBean
{
  static final long serialVersionUID = 1L;
  private Integer testRosterId;
  private Integer testAdminId;
  private Date createdDateTime;
  private Date startDateTime;
  private Date completionDateTime;
  private String testCompletionStatus;
  private String validationStatus;
  private Integer validationUpdatedBy;
  private Date validationUpdatedDateTime;
  private String validationUpdatedNote;
  private String overrideTestWindow;
  private String password;
  private Integer studentId;
  private Integer createdBy;
  private Integer updatedBy;
  private String activationStatus;
  private Date updatedDateTime;
  private Integer customerId;
  private Date tutorialTakenDateTime;
  private String captureMethod;
  private String scoringStatus;
  private Integer orgNodeId;
  private String formAssignment;
  private String firstName;
  private String middleName;
  private String lastName;
  private String extPin1;
  private String userName;
  private String customerFlagStatus;
  private String testCompletionStatusDesc; // For CR Soring 
  private String grade; // For CR Soring 
  private Integer itemSetIdTC; // For CR Soring 
  private Integer scorePoint;
  private Double extendedTime; // Added for Student Pacing
  private String dnsStatus;
  private String studentName;
  private String studentUserName;
  private String className;
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
  private String hasAccommodations;
  private String orgNodeName;
  private String highLighter; 
  private String extendedTimeAccom; 
  private String  maskingRular;
  private String  maskingTool;
  private String  magnifyingGlass;
  private String  musicFileId;
  private String auditoryCalming;
  private String assignedForm;
  private ArrayList<Node> orgList;
  private String hasColorFontAccommodations;
  private String extItemSetId;		//Added for LLO RP Item Rendering story
	public String getDnsStatus() {
		return dnsStatus;
	}
	public void setDnsStatus(String dnsStatus) {
		this.dnsStatus = dnsStatus;
	}
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}  
    /**
     * @return Returns the extPin1.
     */
    public String getExtPin1() {
        return extPin1;
    }
    /**
     * @param extPin1 The extPin1 to set.
     */
    public void setExtPin1(String extPin1) {
        this.extPin1 = extPin1;
    }
    /**
     * @return Returns the firstName.
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * @param firstName The firstName to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * @return Returns the lastName.
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * @param lastName The lastName to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * @return Returns the middleName.
     */
    public String getMiddleName() {
        return middleName;
    }
    /**
     * @param middleName The middleName to set.
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    /**
     * @return Returns the activationStatus.
     */
    public String getActivationStatus() {
        return activationStatus;
    }
    /**
     * @param activationStatus The activationStatus to set.
     */
    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }
    /**
     * @return Returns the captureMethod.
     */
    public String getCaptureMethod() {
        return captureMethod;
    }
    /**
     * @param captureMethod The captureMethod to set.
     */
    public void setCaptureMethod(String captureMethod) {
        this.captureMethod = captureMethod;
    }
    /**
     * @return Returns the completionDateTime.
     */
    public Date getCompletionDateTime() {
        return completionDateTime;
    }
    /**
     * @param completionDateTime The completionDateTime to set.
     */
    public void setCompletionDateTime(Date completionDateTime) {
        this.completionDateTime = completionDateTime;
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
     * @return Returns the createdDateTime.
     */
    public Date getCreatedDateTime() {
        return createdDateTime;
    }
    /**
     * @param createdDateTime The createdDateTime to set.
     */
    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
    /**
     * @return Returns the customerId.
     */
    public Integer getCustomerId() {
        return customerId;
    }
    /**
     * @param customerId The customerId to set.
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    /**
     * @return Returns the formAssignment.
     */
    public String getFormAssignment() {
        return formAssignment;
    }
    /**
     * @param formAssignment The formAssignment to set.
     */
    public void setFormAssignment(String formAssignment) {
        this.formAssignment = formAssignment;
    }
    /**
     * @return Returns the orgNodeId.
     */
    public Integer getOrgNodeId() {
        return orgNodeId;
    }
    /**
     * @param orgNodeId The orgNodeId to set.
     */
    public void setOrgNodeId(Integer orgNodeId) {
        this.orgNodeId = orgNodeId;
    }
    /**
     * @return Returns the overrideTestWindow.
     */
    public String getOverrideTestWindow() {
        return overrideTestWindow;
    }
    /**
     * @param overrideTestWindow The overrideTestWindow to set.
     */
    public void setOverrideTestWindow(String overrideTestWindow) {
        this.overrideTestWindow = overrideTestWindow;
    }
    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return Returns the scoringStatus.
     */
    public String getScoringStatus() {
        return scoringStatus;
    }
    /**
     * @param scoringStatus The scoringStatus to set.
     */
    public void setScoringStatus(String scoringStatus) {
        this.scoringStatus = scoringStatus;
    }
    /**
     * @return Returns the startDateTime.
     */
    public Date getStartDateTime() {
        return startDateTime;
    }
    /**
     * @param startDateTime The startDateTime to set.
     */
    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }
    /**
     * @return Returns the studentId.
     */
    public Integer getStudentId() {
        return studentId;
    }
    /**
     * @param studentId The studentId to set.
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
    /**
     * @return Returns the testAdminId.
     */
    public Integer getTestAdminId() {
        return testAdminId;
    }
    /**
     * @param testAdminId The testAdminId to set.
     */
    public void setTestAdminId(Integer testAdminId) {
        this.testAdminId = testAdminId;
    }
    /**
     * @return Returns the testCompletionStatus.
     */
    public String getTestCompletionStatus() {
        return testCompletionStatus;
    }
    /**
     * @param testCompletionStatus The testCompletionStatus to set.
     */
    public void setTestCompletionStatus(String testCompletionStatus) {
        this.testCompletionStatus = testCompletionStatus;
    }
    /**
     * @return Returns the testRosterId.
     */
    public Integer getTestRosterId() {
        return testRosterId;
    }
    /**
     * @param testRosterId The testRosterId to set.
     */
    public void setTestRosterId(Integer testRosterId) {
        this.testRosterId = testRosterId;
    }
    /**
     * @return Returns the tutorialTakenDateTime.
     */
    public Date getTutorialTakenDateTime() {
        return tutorialTakenDateTime;
    }
    /**
     * @param tutorialTakenDateTime The tutorialTakenDateTime to set.
     */
    public void setTutorialTakenDateTime(Date tutorialTakenDateTime) {
        this.tutorialTakenDateTime = tutorialTakenDateTime;
    }
    /**
     * @return Returns the updatedBy.
     */
    public Integer getUpdatedBy() {
        return updatedBy;
    }
    /**
     * @param updatedBy The updatedBy to set.
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
    /**
     * @return Returns the updatedDateTime.
     */
    public Date getUpdatedDateTime() {
        return updatedDateTime;
    }
    /**
     * @param updatedDateTime The updatedDateTime to set.
     */
    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }
    /**
     * @return Returns the validationStatus.
     */
    public String getValidationStatus() {
        return validationStatus;
    }
    /**
     * @param validationStatus The validationStatus to set.
     */
    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }
    /**
     * @return Returns the validationUpdatedBy.
     */
    public Integer getValidationUpdatedBy() {
        return validationUpdatedBy;
    }
    /**
     * @param validationUpdatedBy The validationUpdatedBy to set.
     */
    public void setValidationUpdatedBy(Integer validationUpdatedBy) {
        this.validationUpdatedBy = validationUpdatedBy;
    }
    /**
     * @return Returns the validationUpdatedDateTime.
     */
    public Date getValidationUpdatedDateTime() {
        return validationUpdatedDateTime;
    }
    /**
     * @param validationUpdatedDateTime The validationUpdatedDateTime to set.
     */
    public void setValidationUpdatedDateTime(Date validationUpdatedDateTime) {
        this.validationUpdatedDateTime = validationUpdatedDateTime;
    }
    /**
     * @return Returns the validationUpdatedNote.
     */
    public String getValidationUpdatedNote() {
        return validationUpdatedNote;
    }
    /**
     * @param validationUpdatedNote The validationUpdatedNote to set.
     */
    public void setValidationUpdatedNote(String validationUpdatedNote) {
        this.validationUpdatedNote = validationUpdatedNote;
    }
     /**
     * @return Returns The customerFlagStatus .
     */
    public String getCustomerFlagStatus() {
            return customerFlagStatus;
    }
     /**
     * @param customerFlagStatus The customerFlagStatus to set.
     */
    public void setCustomerFlagStatus(String customerFlagStatus) {
            this.customerFlagStatus = customerFlagStatus;
    }
    
    
	/**
	 * @return Returns testCompletionStatusDesccription of a student
	 */
	public String getTestCompletionStatusDesc() {
		return testCompletionStatusDesc;
	}
	
	/**
	 * @param testCompletionStatusDesc The testCompletionStatusDesccription to set.
	 */
	public void setTestCompletionStatusDesc(String testCompletionStatusDesc) {
		this.testCompletionStatusDesc = testCompletionStatusDesc;
	}
	
	/**
	 * @return Returns grade of a student
	 */
	public String getGrade() {
		return grade;
	}
	
	/**
	 * @param grade The Grade to set.
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * @return the itemSetIDTC
	 */
	public Integer getItemSetIdTC() {
		return itemSetIdTC;
	}
	/**
	 * @param itemSetIDTC the itemSetIDTC to set
	 */
	public void setItemSetIdTC(Integer itemSetIdTC) {
		this.itemSetIdTC = itemSetIdTC;
	}
	/**
	 * @return the scorePoint
	 */
	public Integer getScorePoint() {
		return scorePoint;
	}
	/**
	 * @param scorePoint the scorePoint to set
	 */
	public void setScorePoint(Integer scorePoint) {
		this.scorePoint = scorePoint;
	}
	
	// Start changes for Student Pacing
	/**
	 * @return the extendedTime
	 */
	public Double getExtendedTime() {
		return extendedTime;
	}
	/**
	 * @param extendedTime the extendedTime to set
	 */
	public void setExtendedTime(Double extendedTime) {
		this.extendedTime = extendedTime;
	}
	// End changes for Student Pacing
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentUserName() {
		return studentUserName;
	}
	public void setStudentUserName(String studentUserName) {
		this.studentUserName = studentUserName;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the assignedForm
	 */
	public String getAssignedForm() {
		return assignedForm;
	}
	/**
	 * @param assignedForm the assignedForm to set
	 */
	public void setAssignedForm(String assignedForm) {
		this.assignedForm = assignedForm;
	}
	public String getScreenMagnifier() {
		return screenMagnifier;
	}
	public void setScreenMagnifier(String screenMagnifier) {
		this.screenMagnifier = screenMagnifier;
	}
	public String getScreenReader() {
		return screenReader;
	}
	public void setScreenReader(String screenReader) {
		this.screenReader = screenReader;
	}
	public String getCalculator() {
		return calculator;
	}
	public void setCalculator(String calculator) {
		this.calculator = calculator;
	}
	public String getTestPause() {
		return testPause;
	}
	public void setTestPause(String testPause) {
		this.testPause = testPause;
	}
	public String getUntimedTest() {
		return untimedTest;
	}
	public void setUntimedTest(String untimedTest) {
		this.untimedTest = untimedTest;
	}
	public String getQuestionBackgroundColor() {
		return questionBackgroundColor;
	}
	public void setQuestionBackgroundColor(String questionBackgroundColor) {
		this.questionBackgroundColor = questionBackgroundColor;
	}
	public String getQuestionFontColor() {
		return questionFontColor;
	}
	public void setQuestionFontColor(String questionFontColor) {
		this.questionFontColor = questionFontColor;
	}
	public String getQuestionFontSize() {
		return questionFontSize;
	}
	public void setQuestionFontSize(String questionFontSize) {
		this.questionFontSize = questionFontSize;
	}
	public String getAnswerBackgroundColor() {
		return answerBackgroundColor;
	}
	public void setAnswerBackgroundColor(String answerBackgroundColor) {
		this.answerBackgroundColor = answerBackgroundColor;
	}
	public String getAnswerFontColor() {
		return answerFontColor;
	}
	public void setAnswerFontColor(String answerFontColor) {
		this.answerFontColor = answerFontColor;
	}
	public String getAnswerFontSize() {
		return answerFontSize;
	}
	public void setAnswerFontSize(String answerFontSize) {
		this.answerFontSize = answerFontSize;
	}
	public String getHasAccommodations() {
		   String result = "No";
	        if( "T".equals(this.screenMagnifier) ||
	            "T".equals(this.screenReader) ||
	            "T".equals(this.calculator) ||
	            "T".equals(this.testPause) ||
	            "T".equals(this.untimedTest) ||
	            "T".equals(this.highLighter) ||
	            "T".equals(this.extendedTimeAccom) ||
	            "T".equals(this.maskingRular) ||
	            "T".equals(this.maskingTool) ||
	            "T".equals(this.magnifyingGlass) ||
	            (this.musicFileId!=null && this.musicFileId.trim().length()!=0)||
	            (this.extendedTimeAccom != null &&
	            !this.extendedTimeAccom.equals("") &&
	            !this.extendedTimeAccom.equals("F")) || // change for defect #66723
	            this.questionBackgroundColor != null ||
	            this.questionFontColor != null ||
	            this.questionFontSize != null ||
	            this.answerBackgroundColor != null ||
	            this.answerFontColor != null ||
	            this.answerFontSize != null)
	            result = "Yes";
	        return result;
	}
	public void setHasAccommodations(String hasAccommodations) {
		this.hasAccommodations = hasAccommodations;
	}
	public String getOrgNodeName() {
		return orgNodeName;
	}
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}
	public String getHighLighter() {
		return highLighter;
	}
	public void setHighLighter(String highLighter) {
		this.highLighter = highLighter;
	}
	public String getExtendedTimeAccom() {
		return extendedTimeAccom;
	}
	public void setExtendedTimeAccom(String extendedTimeAccom) {
		this.extendedTimeAccom = extendedTimeAccom;
	}
	public String getMaskingRular() {
		return maskingRular;
	}
	public void setMaskingRular(String maskingRular) {
		this.maskingRular = maskingRular;
	}
	public String getMaskingTool() {
		return maskingTool;
	}
	public void setMaskingTool(String maskingTool) {
		this.maskingTool = maskingTool;
	}
	public String getMagnifyingGlass() {
		return magnifyingGlass;
	}
	public void setMagnifyingGlass(String magnifyingGlass) {
		this.magnifyingGlass = magnifyingGlass;
	}
	public String getMusicFileId() {
		return musicFileId;
	}
	public void setMusicFileId(String musicFileId) {
		this.musicFileId = musicFileId;
	}
	public String getAuditoryCalming() {
		return auditoryCalming;
	}
	public void setAuditoryCalming(String auditoryCalming) {
		this.auditoryCalming = auditoryCalming;
	}
	public ArrayList<Node> getOrgList() {
		return orgList;
	}
	public void setOrgList(ArrayList<Node> orgList) {
		this.orgList = orgList;
	}
	
	public String getHasColorFontAccommodations() {
        String result = "F";
        if( this.getQuestionBackgroundColor() != null ||
        	this.getQuestionFontColor() != null ||
        	this.getQuestionFontSize() != null ||
        	this.getAnswerBackgroundColor() != null ||
        	this.getAnswerFontColor() != null ||
        	this.getAnswerFontSize() != null)
            result = "T";
        return result;
    }
	
	public void setHasColorFontAccommodations(String hasColorFontAccommodations) {
        this.hasColorFontAccommodations = hasColorFontAccommodations;
    }
	public String getExtItemSetId() {
		return extItemSetId;
	}
	public void setExtItemSetId(String extItemSetId) {
		this.extItemSetId = extItemSetId;
	}
	  
} 
