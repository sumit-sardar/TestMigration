package model;

import java.io.*;

public class SchedulingData implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer sessionId = 0;
	private String sessionName = "";
	private String accessCode = "";
	private Integer studentId = 0;
	private String loginId = "";
	private String password = "";
	

	public Integer getSessionId() {
		return sessionId;
	}



	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}



	public String getSessionName() {
		return sessionName;
	}



	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}



	public String getAccessCode() {
		return accessCode;
	}



	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}



	public Integer getStudentId() {
		return studentId;
	}



	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}



	public String getLoginId() {
		return loginId;
	}



	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public SchedulingData() {
		super();
	}


}
