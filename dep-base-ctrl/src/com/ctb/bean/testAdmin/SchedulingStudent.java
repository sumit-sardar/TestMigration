package com.ctb.bean.testAdmin; 

/**
 * Data bean representing the contents of the OAS.STUDENT table
 * with additional fields for current grade and accommodations
 * information, and an EditCopyStatus object used to indicate whether
 * the student can be scheduled for the selected test or copied into
 * a new session. If the editCopyStatus.editable flag is "F", 
 * the editCopyStatus.code field indicates the reason the student 
 * is non-editable.
 * 
 * @author Nate_Cohen
 */
public class SchedulingStudent extends Student
{ 
    static final long serialVersionUID = 1L;
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
    private String hasAccommodations;
    private String hasColorFontAccommodations;
    private String orgNodeName;
    private Integer orgNodeId;
    private String orgNodeCategoryName;
    private EditCopyStatus status;
    private Integer priorAdmin;
    private String highLighter; /* 51931 Deferred Defect For HighLighter*/
    
   	public Integer getPriorAdmin() {
        return this.priorAdmin;
    }
    
    public void setPriorAdmin(Integer priorAdmin) {
        this.priorAdmin = priorAdmin;
    }
  
    public String getHasAccommodations() {
        String result = "false";
        if( "T".equals(this.screenMagnifier) ||
            "T".equals(this.screenReader) ||
            "T".equals(this.calculator) ||
            "T".equals(this.testPause) ||
            "T".equals(this.untimedTest) ||
            "T".equals(this.highLighter) ||
            this.questionBackgroundColor != null ||
            this.questionFontColor != null ||
            this.questionFontSize != null ||
            this.answerBackgroundColor != null ||
            this.answerFontColor != null ||
            this.answerFontSize != null)
            result = "true";
        return result;
    }
    
    public String getHasColorFontAccommodations() {
        String result = "false";
        if( this.questionBackgroundColor != null ||
            this.questionFontColor != null ||
            this.questionFontSize != null ||
            this.answerBackgroundColor != null ||
            this.answerFontColor != null ||
            this.answerFontSize != null)
            result = "true";
        return result;
    }
    
    public void setHasAccommodations(String hasAccommodations) {
        this.hasAccommodations = hasAccommodations;
    }
    
    public void setHasColorFontAccommodations(String hasColorFontAccommodations) {
        this.hasColorFontAccommodations = hasColorFontAccommodations;
    }
  
  
    /**
	 * @return Returns the status.
	 */
	public EditCopyStatus getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(EditCopyStatus status) {
		this.status = status;
	}
    /**
	 * @return Returns the orgNodeCategoryName.
	 */
	public String getOrgNodeCategoryName() {
		return orgNodeCategoryName;
	}
	/**
	 * @param orgNodeCategoryName The orgNodeCategoryName to set.
	 */
	public void setOrgNodeCategoryName(String orgNodeCategoryName) {
		this.orgNodeCategoryName = orgNodeCategoryName;
	}
  
    public SchedulingStudent() {
        super();
        this.setStatus(new EditCopyStatus());
    }
  
    public SchedulingStudent (Student student) {
        setStudentId(student.getStudentId());
        setUserName(student.getUserName());
        setPassword(student.getPassword());
        setFirstName(student.getFirstName());
        setMiddleName(student.getMiddleName());
        setLastName(student.getLastName());
        setPreferredName(student.getPreferredName());
        setPrefix(student.getPrefix());
        setSuffix(student.getSuffix());
        setBirthdate(student.getBirthdate());
        setGender(student.getGender());
        setEthnicity(student.getEthnicity());
        setEmail(student.getEmail());
        setGrade(student.getGrade());
        setExtElmId(student.getExtElmId());
        setExtPin1(student.getExtPin1());
        setExtPin2(student.getExtPin2());
        setExtPin3(student.getExtPin3());
        setExtSchoolId(student.getExtSchoolId());
        setActiveSession(student.getActiveSession());
        setPotentialDuplicatedStudent(student.getPotentialDuplicatedStudent());
        setCreatedBy(student.getCreatedBy());
        setCreatedDateTime(student.getCreatedDateTime());
        setUpdatedBy(student.getUpdatedBy());
        setUpdatedDateTime(student.getUpdatedDateTime());
        setActivationStatus(student.getActivationStatus());
        setDataImportHistoryId(student.getDataImportHistoryId());
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
	 * @return Returns the orgNodeName.
	 */
	public String getOrgNodeName() {
		return orgNodeName;
	}
	/**
	 * @param orgNodeName The orgNodeName to set.
	 */
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}
	/**
	 * @return Returns the answerBackgroundColor.
	 */
	public String getAnswerBackgroundColor() {
		return answerBackgroundColor;
	}
	/**
	 * @param answerBackgroundColor The answerBackgroundColor to set.
	 */
	public void setAnswerBackgroundColor(String answerBackgroundColor) {
		this.answerBackgroundColor = answerBackgroundColor;
	}
	/**
	 * @return Returns the answerFontColor.
	 */
	public String getAnswerFontColor() {
		return answerFontColor;
	}
	/**
	 * @param answerFontColor The answerFontColor to set.
	 */
	public void setAnswerFontColor(String answerFontColor) {
		this.answerFontColor = answerFontColor;
	}
	/**
	 * @return Returns the answerFontSize.
	 */
	public String getAnswerFontSize() {
		return answerFontSize;
	}
	/**
	 * @param answerFontSize The answerFontSize to set.
	 */
	public void setAnswerFontSize(String answerFontSize) {
		this.answerFontSize = answerFontSize;
	}
	/**
	 * @return Returns the calculator.
	 */
	public String getCalculator() {
		return calculator;
	}
	/**
	 * @param calculator The calculator to set.
	 */
	public void setCalculator(String calculator) {
		this.calculator = calculator;
	}
	/**
	 * @return Returns the questionBackgroundColor.
	 */
	public String getQuestionBackgroundColor() {
		return questionBackgroundColor;
	}
	/**
	 * @param questionBackgroundColor The questionBackgroundColor to set.
	 */
	public void setQuestionBackgroundColor(String questionBackgroundColor) {
		this.questionBackgroundColor = questionBackgroundColor;
	}
	/**
	 * @return Returns the questionFontColor.
	 */
	public String getQuestionFontColor() {
		return questionFontColor;
	}
	/**
	 * @param questionFontColor The questionFontColor to set.
	 */
	public void setQuestionFontColor(String questionFontColor) {
		this.questionFontColor = questionFontColor;
	}
	/**
	 * @return Returns the questionFontSize.
	 */
	public String getQuestionFontSize() {
		return questionFontSize;
	}
	/**
	 * @param questionFontSize The questionFontSize to set.
	 */
	public void setQuestionFontSize(String questionFontSize) {
		this.questionFontSize = questionFontSize;
	}
	/**
	 * @return Returns the screenMagnifier.
	 */
	public String getScreenMagnifier() {
		return screenMagnifier;
	}
	/**
	 * @param screenMagnifier The screenMagnifier to set.
	 */
	public void setScreenMagnifier(String screenMagnifier) {
		this.screenMagnifier = screenMagnifier;
	}
	/**
	 * @return Returns the screenReader.
	 */
	public String getScreenReader() {
		return screenReader;
	}
	/**
	 * @param screenReader The screenReader to set.
	 */
	public void setScreenReader(String screenReader) {
		this.screenReader = screenReader;
	}
	/**
	 * @return Returns the studentGrade.
	 */
	public String getStudentGrade() {
		return studentGrade;
	}
	/**
	 * @param studentGrade The studentGrade to set.
	 */
	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}
	/**
	 * @return Returns the testPause.
	 */
	public String getTestPause() {
		return testPause;
	}
	/**
	 * @param testPause The testPause to set.
	 */
	public void setTestPause(String testPause) {
		this.testPause = testPause;
	}
	/**
	 * @return Returns the untimedTest.
	 */
	public String getUntimedTest() {
		return untimedTest;
	}
	/**
	 * @param untimedTest The untimedTest to set.
	 */
	public void setUntimedTest(String untimedTest) {
		this.untimedTest = untimedTest;
	}
    
    /**
	 * @return Returns the highLighter.
	 */
	public String getHighLighter() {
		return this.highLighter;
	}
	/**
	 * @param highLighter The highLighter to set.
	 */
	public void setHighLighter(String highLighter) {
		this.highLighter = highLighter;
	}
} 
