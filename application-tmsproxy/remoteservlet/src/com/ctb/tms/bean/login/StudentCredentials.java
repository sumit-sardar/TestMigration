package com.ctb.tms.bean.login;

public class StudentCredentials {
	private String username;
	private String password;
	private String accesscode;
	private String testRosterId;
	private String tmsUpdate;
	private String activationStatus;
	
	public String getTestRosterId() {
		return testRosterId;
	}
	public void setTestRosterId(String testRosterId) {
		this.testRosterId = testRosterId;
	}
	
	public String getTmsUpdate() {
		return tmsUpdate;
	}
	public void setTmsUpdate(String tmsUpdate) {
		this.tmsUpdate = tmsUpdate;
	}
	public String getUsername() {
		return username.toUpperCase();
	}
	public void setUsername(String username) {
		if(username != null) {
			this.username = username.toUpperCase();
		}
	}
	public String getPassword() {
		return password.toUpperCase();
	}
	public void setPassword(String password) {
		if(password != null) {
			this.password = password.toUpperCase();
		}
	}
	public String getAccesscode() {
		return accesscode.toUpperCase();
	}
	public void setAccesscode(String accesscode) {
		if(accesscode != null) {
			this.accesscode = accesscode.toUpperCase();
		}
	}
	/**
	 * @return the activationStatus
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus the activationStatus to set
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	
}
