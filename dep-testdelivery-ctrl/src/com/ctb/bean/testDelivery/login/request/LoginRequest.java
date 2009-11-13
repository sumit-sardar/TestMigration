package com.ctb.bean.testDelivery.login.request; 

public class LoginRequest 
{ 
    private String username;
    private String password;
    private String accessCode;
    private String os_enum;
    private String browser_agent_enum;
    private String user_agent_string;
    private String sds_id;
    private String sds_date_time;
    private String token;
    
    /**
	 * @return Returns the accessCode.
	 */
	public String getAccessCode() {
		return accessCode;
	}
	/**
	 * @param accessCode The accessCode to set.
	 */
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	/**
	 * @return Returns the browser_agent_enum.
	 */
	public String getBrowser_agent_enum() {
		return browser_agent_enum;
	}
	/**
	 * @param browser_agent_enum The browser_agent_enum to set.
	 */
	public void setBrowser_agent_enum(String browser_agent_enum) {
		this.browser_agent_enum = browser_agent_enum;
	}
	/**
	 * @return Returns the os_enum.
	 */
	public String getOs_enum() {
		return os_enum;
	}
	/**
	 * @param os_enum The os_enum to set.
	 */
	public void setOs_enum(String os_enum) {
		this.os_enum = os_enum;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the sds_date_time.
	 */
	public String getSds_date_time() {
		return sds_date_time;
	}
	/**
	 * @param sds_date_time The sds_date_time to set.
	 */
	public void setSds_date_time(String sds_date_time) {
		this.sds_date_time = sds_date_time;
	}
	/**
	 * @return Returns the sds_id.
	 */
	public String getSds_id() {
		return sds_id;
	}
	/**
	 * @param sds_id The sds_id to set.
	 */
	public void setSds_id(String sds_id) {
		this.sds_id = sds_id;
	}
	/**
	 * @return Returns the token.
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token The token to set.
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return Returns the user_agent_string.
	 */
	public String getUser_agent_string() {
		return user_agent_string;
	}
	/**
	 * @param user_agent_string The user_agent_string to set.
	 */
	public void setUser_agent_string(String user_agent_string) {
		this.user_agent_string = user_agent_string;
	}
	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
} 
