package com.ctb.utils;

public class Configuration {

	static String csvFilePath = "";
	static String jndiFactory = "";
    static String jmsFactory = "";
    static String jmsURL = "";
    static String jmsQueue = "";
    static String jmsPrincipal = "";
    static String jmsCredentials = "";
    
    static {
    	csvFilePath = ResourceUtils.getDetail("oas.rosterListFilePath");
    	jndiFactory = ResourceUtils.getDetail("oas.jndiFactory");
    	jmsFactory = ResourceUtils.getDetail("oas.jmsFactory");
    	jmsURL = ResourceUtils.getDetail("oas.jmsURL");
    	jmsQueue = ResourceUtils.getDetail("oas.jmsQueue");
    	jmsPrincipal = ResourceUtils.getDetail("oas.jmsPrincipal");
    	jmsCredentials = ResourceUtils.getDetail("oas.jmsCredentials");
    }

	/**
	 * @return the csvFilePath
	 */
	public static String getCsvFilePath() {
		return csvFilePath;
	}

	/**
	 * @return the jndiFactory
	 */
	public static String getJndiFactory() {
		return jndiFactory;
	}

	/**
	 * @return the jmsFactory
	 */
	public static String getJmsFactory() {
		return jmsFactory;
	}

	/**
	 * @return the jmsURL
	 */
	public static String getJmsURL() {
		return jmsURL;
	}

	/**
	 * @return the jmsQueue
	 */
	public static String getJmsQueue() {
		return jmsQueue;
	}

	/**
	 * @return the jmsPrincipal
	 */
	public static String getJmsPrincipal() {
		return jmsPrincipal;
	}

	/**
	 * @return the jmsCredentials
	 */
	public static String getJmsCredentials() {
		return jmsCredentials;
	}
    
    
}
