package com.ctb.tms.bean.login; 

import java.io.Serializable;
import java.util.Date;

public class AuthenticationData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int testRosterId; 
	private int studentId;
	private String studentLastName;
	private String studentFirstName;
	private String studentMiddleName;
	private String rosterTestCompletionStatus;
	private Date windowStartDate;
	private Date windowEndDate;
	private Date dailyStartTime;
	private Date dailyEndTime;
	private String captureMethod;
	private int restartNumber;
	private String testAdminStatus;
	private int testAdminId;
	private String timeZone;

	//Changes for RD 

	private Integer randomDistractorSeedNumber;

	//Change for TTS-Sppech controller
    
    private String ttsSpeedStatus;
    
    /**
	 * @return Returns the ttsSpeedStatus.
	 */
	public String getTtsSpeedStatus() {
		return ttsSpeedStatus;
	}
	/**
	 * @param ttsSpeedStatus The ttsSpeedStatus to set.
	 */
	public void setTtsSpeedStatus(String ttsSpeedStatus) {
		this.ttsSpeedStatus = ttsSpeedStatus;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getRandomDistractorSeedNumber() {
		return randomDistractorSeedNumber;
	}

	/**
	 * 
	 * @param randomDistractorSeedNumber
	 */
	public void setRandomDistractorSeedNumber(Integer randomDistractorSeedNumber) {
		this.randomDistractorSeedNumber = randomDistractorSeedNumber;
	}

	/**
	 * @return Returns the testAdminStatus.
	 */
	public String getTestAdminStatus() {
		return testAdminStatus;
	}
	/**
	 * @param testAdminStatus The testAdminStatus to set.
	 */
	public void setTestAdminStatus(String testAdminStatus) {
		this.testAdminStatus = testAdminStatus;
	}
	/**
	 * @return Returns the restartNumber.
	 */
	public int getRestartNumber() {
		return restartNumber;
	}
	/**
	 * @param restart The restartNumber to set.
	 */
	public void setRestartNumber(int restartNumber) {
		this.restartNumber = restartNumber;
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
	 * @return Returns the dailyEndTime.
	 */
	public Date getDailyEndTime() {
		return dailyEndTime;
	}
	/**
	 * @param dailyEndTime The dailyEndTime to set.
	 */
	public void setDailyEndTime(Date dailyEndTime) {
		this.dailyEndTime = dailyEndTime;
	}
	/**
	 * @return Returns the dailyStartTime.
	 */
	public Date getDailyStartTime() {
		return dailyStartTime;
	}
	/**
	 * @param dailyStartTime The dailyStartTime to set.
	 */
	public void setDailyStartTime(Date dailyStartTime) {
		this.dailyStartTime = dailyStartTime;
	}
	/**
	 * @return Returns the rosterTestCompletionStatus.
	 */
	public String getRosterTestCompletionStatus() {
		return rosterTestCompletionStatus;
	}
	/**
	 * @param rosterTestCompletionStatus The rosterTestCompletionStatus to set.
	 */
	public void setRosterTestCompletionStatus(String rosterTestCompletionStatus) {
		this.rosterTestCompletionStatus = rosterTestCompletionStatus;
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
	 * @return Returns the studentLastName.
	 */
	public String getStudentLastName() {
		return studentLastName;
	}
	/**
	 * @param studentLastName The studentLastName to set.
	 */
	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}
	/**
	 * @return Returns the studentFirstName.
	 */
	public String getStudentFirstName() {
		return studentFirstName;
	}
	/**
	 * @param studentFirstName The studentFirstName to set.
	 */
	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}
	/**
	 * @return Returns the studentMiddleName.
	 */
	public String getStudentMiddleName() {
		return studentMiddleName;
	}
	/**
	 * @param studentMiddleName The studentMiddleName to set.
	 */
	public void setStudentMiddleName(String studentMiddleName) {
		this.studentMiddleName = studentMiddleName;
	}
	/**
	 * @return Returns the testRosterId.
	 */
	public int getTestRosterId() {
		return testRosterId;
	}
	/**
	 * @param testRosterId The testRosterId to set.
	 */
	public void setTestRosterId(int testRosterId) {
		this.testRosterId = testRosterId;
	}
	/**
	 * @return Returns the testAdminId.
	 */
	public int getTestAdminId() {
		return testAdminId;
	}
	/**
	 * @param testAdminId The testAdminId to set.
	 */
	public void setTestAdminId(int testAdminId) {
		this.testAdminId = testAdminId;
	}
	/**
	 * @return Returns the windowEndDate.
	 */
	public Date getWindowEndDate() {
		return windowEndDate;
	}
	/**
	 * @param windowEndDate The windowEndDate to set.
	 */
	public void setWindowEndDate(Date windowEndDate) {
		this.windowEndDate = windowEndDate;
	}
	/**
	 * @return Returns the windowStartDate.
	 */
	public Date getWindowStartDate() {
		return windowStartDate;
	}
	/**
	 * @param windowStartDate The windowStartDate to set.
	 */
	public void setWindowStartDate(Date windowStartDate) {
		this.windowStartDate = windowStartDate;
	}

	/**
	 * @return Returns the timeZone.
	 */
	public String getTimeZone() {
		return timeZone;
	}
	/**
	 * @param timeZone The timeZone to set.
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
} 
