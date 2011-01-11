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
    private String highlighter;
    
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
        return this.colorFontAccommodation;
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
} 
