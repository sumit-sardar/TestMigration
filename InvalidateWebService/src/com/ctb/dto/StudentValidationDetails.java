package com.ctb.dto;

import java.io.Serializable;

public class StudentValidationDetails implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long studentId;
	private long sessionId;
	private String[] subtest;
	
	
	public long getStudentId() {
		return studentId;
	}
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public String[] getSubtest() {
		return subtest;
	}
	public void setSubtest(String[] subtest) {
		this.subtest = subtest;
	}

}
