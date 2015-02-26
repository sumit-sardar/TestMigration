package com.ctb.utils;

public class Configuration {

	static String customerId = "";
	static String frameWorkProductId = "";
	static String sessionStartDate = "";
	static String sessionEndDate= "";
	static String mfID = "";
	static String localFilePath = "";
	static String ftphost = "";
	static String ftpuser = "";
	static String ftppassword = "";
	static String ftpFilepath = "";
	static String ftpPort = "";
	static String requiredClassNodeDummyName = "";
	static String threadCount = "";
	static String log4jFile = "";
	static String maxElementsInMemory="";
	
	static {
		
		customerId = ExtractUtil.getDetail("oas.customerId");
		frameWorkProductId = ExtractUtil.getDetail("oas.frameworkProductId");
		sessionStartDate = ExtractUtil.getDetail("oas.extractSpanStartDate");
		sessionEndDate = ExtractUtil.getDetail("oas.extractSpanEndDate");
		mfID = ExtractUtil.getDetail("oas.mainFrameId");
		localFilePath = ExtractUtil.getDetail("oas.exportdata.filepath");
		ftphost = ExtractUtil.getDetail("oas.exportdata.ftphost");
		ftpuser = ExtractUtil.getDetail("oas.exportdata.ftpuser");
		ftppassword = ExtractUtil.getDetail("oas.exportdata.ftppassword");
		ftpFilepath = ExtractUtil.getDetail("oas.exportdata.ftp.filepath");
		ftpPort = ExtractUtil.getDetail("oas.exportdata.ftp.port");
		requiredClassNodeDummyName = ExtractUtil.getDetail("requiredClassNodeDummyName");
		log4jFile = ExtractUtil.getDetail("oas.log4j.file");
		maxElementsInMemory = ExtractUtil.getDetail("cache.inmemory.maxlimit");
		threadCount = ExtractUtil.getDetail("thread.connection.number");
	}

	
	
	/**
	 * @return the customerId
	 */
	public static String getCustomerId() {
		return customerId;
	}

	/**
	 * @return the frameWorkProductId
	 */
	public static String getFrameWorkProductId() {
		return frameWorkProductId;
	}

	/**
	 * @return the sessionStartDate
	 */
	public static String getSessionStartDate() {
		return sessionStartDate;
	}

	/**
	 * @return the sessionEndDate
	 */
	public static String getSessionEndDate() {
		return sessionEndDate;
	}

	/**
	 * @return the mfID
	 */
	public static String getMfID() {
		return mfID;
	}

	/**
	 * @return the localFilePath
	 */
	public static String getLocalFilePath() {
		return localFilePath;
	}

	/**
	 * @return the ftphost
	 */
	public static String getFtpHost() {
		return ftphost;
	}

	/**
	 * @return the ftpuser
	 */
	public static String getFtpUser() {
		return ftpuser;
	}

	/**
	 * @return the ftppassword
	 */
	public static String getFtpPassword() {
		return ftppassword;
	}

	/**
	 * @return the filepath
	 */
	public static String getFtpFilepath() {
		return ftpFilepath;
	}

	/**
	 * @return the ftpPort
	 */
	public static String getFtpPort() {
		return ftpPort;
	}

	/**
	 * @return the isClassNodeRequired
	 */
	public static String getIsClassNodeRequiredDummyName() {
		return requiredClassNodeDummyName;
	}

	/**
	 * @return the log4jFile
	 */
	public static String getLog4jFile() {
		return log4jFile;
	}

	/**
	 * @return the maxElementsInMemory
	 */
	public static String getMaxElementsInMemory() {
		return maxElementsInMemory;
	}

	/**
	 * @return the threadCount
	 */
	public static String getThreadCount() {
		return threadCount;
	}

	
}
