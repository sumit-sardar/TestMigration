package com.ctb.bean.testDelivery.login; 

import java.math.BigInteger;

public class AccomodationsData 
{
    private int studentId;
	private String screenMagnifier;
  	private String screenReader;
	private String calculator;
	private String testPause;
	private String untimedTest;
	private String questionBackgroundColor;
	private String questionFontColor;
	private float questionFontSize;
	private String answerBackgroundColor;
	private String answerFontColor;
	private float answerFontSize;
    private String highlighter;
    private String maskingRuler;
    private String auditoryCalming;
    private byte[] musicFileData;
    private Integer musicFileId;
    private String magnifyingGlass;//Added for magnifier
    private String extendedTime;
    private String maskingTool;//Added for masking Answers
    
  
	/**
	 * @return the musicFileId
	 */
	public Integer getMusicFileId() {
		return musicFileId;
	}
	/**
	 * @param musicFileId the musicFileId to set
	 */
	public void setMusicFileId(Integer musicFileId) {
		this.musicFileId = musicFileId;
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
	public float getAnswerFontSize() {
		return answerFontSize;
	}
	/**
	 * @param answerFontSize The answerFontSize to set.
	 */
	public void setAnswerFontSize(float answerFontSize) {
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
	public float getQuestionFontSize() {
		return questionFontSize;
	}
	/**
	 * @param questionFontSize The questionFontSize to set.
	 */
	public void setQuestionFontSize(float questionFontSize) {
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
	 * @return Returns the studentId.
	 */
	public int getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId The studentId to set.
	 */
	public void setStudentId(int studentId) {
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
	 * @return the musicFileData
	 */
	public byte[] getMusicFileData() {
		return musicFileData;
	}
	/**
	 * @param musicFileData the musicFileData to set
	 */
	public void setMusicFileData(byte[] musicFileData) {
		this.musicFileData = musicFileData;
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
	
} 
