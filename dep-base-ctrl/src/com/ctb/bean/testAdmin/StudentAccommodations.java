package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the contents of the OAS.STUDENT_ACCOMMODATIONS table
 * 
 * @author Nate_Cohen
 */
public class StudentAccommodations extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer orgNodeId;
    private Integer studentId;
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
    private String colorFont;//bulk accommodation new field
    private String highlighter;
    private String maskingRuler;//Added for masking
    private String auditoryCalming;//Added for auditory calming
    private String musicFile;//Added for auditory calming
    private String magnifyingGlass;//Added for magnifier
    private String extendedTime; // Added for student pacing
    private String maskingTool; // Added for masking answers
    private String microphoneHeadphone; // Added for Microphone and Headphone
    
    // Start changes for student pacing
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
	// End changes for student pacing
	
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
	 * @return Returns the highlighter.
	 */
	public String getHighlighter() {
		return highlighter;
	}
	/**
	 * @param highlighter The highlighter to set.
	 */
	public void setHighlighter(String highlighter) {
		this.highlighter = highlighter;
	}
    
  
    public String getColorFontAccommodation() {
        return 
            this.getAnswerBackgroundColor() != null ||
            this.getAnswerFontColor() != null ||
            this.getAnswerFontSize() != null ||
            this.getQuestionBackgroundColor() != null ||
            this.getQuestionFontColor() != null ||
            this.getQuestionFontSize() != null ? "T" : "F";
    }
    
    public void setColorFontAccommodation(String colorFontAccommodation) {
        this.colorFontAccommodation = colorFontAccommodation;
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
	 * @return Returns the colorFont.
	 */
	public String getColorFont() {
		return colorFont;
	}
	/**
	 * @param colorFont The colorFont to set.
	 */
	public void setColorFont(String colorFont) {
		this.colorFont = colorFont;
	}
	/**
	 * @return Returns the maskingRuler.
	 */
	public String getMaskingRuler() {
		return maskingRuler;
	}
	/**
	 * @param maskingRuler The maskingRuler to set.
	 */
	public void setMaskingRuler(String maskingRuler) {
		this.maskingRuler = maskingRuler;
	}
	/**
	 * @return Returns the auditoryCalming.
	 */
	public String getAuditoryCalming() {
		return auditoryCalming;
	}
	/**
	 * @param auditoryCalming The auditoryCalming to set.
	 */
	public void setAuditoryCalming(String auditoryCalming) {
		this.auditoryCalming = auditoryCalming;
	}
	/**
	 * @return Returns the musicFile.
	 */
	public String getMusicFile() {
		return musicFile;
	}
	/**
	 * @param musicFile The musicFile to set.
	 */
	public void setMusicFile(String musicFile) {
		this.musicFile = musicFile;
	}
	/**
	 * @return Returns the magnifyingGlass.
	 */
	public String getMagnifyingGlass() {
		return magnifyingGlass;
	}
	/**
	 * @param magnifyingGlass The magnifyingGlass to set.
	 */
	public void setMagnifyingGlass(String magnifyingGlass) {
		this.magnifyingGlass = magnifyingGlass;
	}
	/**
	 * @return Returns the maskingTool.
	 */
	public String getMaskingTool() {
		return maskingTool;
	}
	/**
	 * @param maskingTool The maskingTool to set.
	 */
	public void setMaskingTool(String maskingTool) {
		this.maskingTool = maskingTool;
	}
	public String getMicrophoneHeadphone() {
		return microphoneHeadphone;
	}
	public void setMicrophoneHeadphone(String microphoneHeadphone) {
		this.microphoneHeadphone = microphoneHeadphone;
	}
	
} 
