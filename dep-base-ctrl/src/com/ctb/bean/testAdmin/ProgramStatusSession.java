package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * ProgramStatusSession.java
 * @author John_Wang
 *
 * Data bean representing sessions for program status page.
 * 
 */

public class ProgramStatusSession extends CTBBean
{ 

    static final long serialVersionUID = 1L;
    
    private Integer testAdminId;
    private String sessionName;
    private String sessionNumber;
    private Integer testRosterId;
    private String loginId;
    private String password;
    private String accessCode;
    private String visible;
    
	public Integer getTestAdminId() {
		return testAdminId;
	}
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	public String getSessionNumber() {
		return sessionNumber;
	}
	public void setSessionNumber(String sessionNumber) {
		this.sessionNumber = sessionNumber;
	}
	public Integer getTestRosterId() {
		return testRosterId;
	}
	public void setTestRosterId(Integer testRosterId) {
		this.testRosterId = testRosterId;
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
	public String getAccessCode() {
		return accessCode;
	}
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}


} 
