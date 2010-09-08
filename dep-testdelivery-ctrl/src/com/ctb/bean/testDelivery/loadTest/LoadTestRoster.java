 package com.ctb.bean.testDelivery.loadTest;

public class LoadTestRoster {
	private String testRosterId;
	private String password;
	private String accessCode;
	private String loginId;
	
	/**
	 * @return Returns the accessCode.
	 */
	public String getAccessCode() {
		return accessCode;
	}

	/**
	 * @param accessCode The accessCode to set.
	 */
	public void setAccessCode(String accessCode){
		this.accessCode = accessCode;
	}
	/**
	 * @return Returns the testRosterId.
	 */
	public String getTestRosterId() {
		return testRosterId;
	}
	
	/**
	 * @param testRosterId The testRosterId to set.
	 */
	public void setTestrosterId(String testRosterId){
		this.testRosterId= testRosterId;
	}
	
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password){
		this.password = password;
	}
	
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @return Returns the loginId.
	 */
	public String getLoginId() {
		return loginId;
	}
	
	/**
	 * @param loginId The loginId to set.
	 */
	public void setLoginId(String loginId){
		this.loginId = loginId;
	}
}
