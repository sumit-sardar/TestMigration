package com.ctb.dummy.bean;

public class RosterDetails {
	
	Integer studentId;
	Integer sessionId;
	Integer testRosterId;
	boolean testCompletionStatus;
	String testcompletionStatusStr;

	
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public Integer getSessionId() {
		return sessionId;
	}
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}
	public Integer getTestRosterId() {
		return testRosterId;
	}
	public void setTestRosterId(Integer testRosterId) {
		this.testRosterId = testRosterId;
	}
	public boolean isTestCompletionStatus() {
		return testCompletionStatus;
	}
	public void setTestCompletionStatus(boolean testCompletionStatus) {
		this.testCompletionStatus = testCompletionStatus;
	}
	public String getTestcompletionStatusStr() {
		return testcompletionStatusStr;
	}
	public void setTestcompletionStatusStr(String testcompletionStatusStr) {
		this.testcompletionStatusStr = testcompletionStatusStr;
	}
	
}
