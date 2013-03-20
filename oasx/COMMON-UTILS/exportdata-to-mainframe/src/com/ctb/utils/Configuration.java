package com.ctb.utils;

public class Configuration {

	static String localFilePath = "";
	static String ftphost = "";
	static String ftpuser = "";
	static String ftppassword = "";
	static String ftpFilepath = "";
	static String ftpPort = "";
	static String isClassNodeRequired = "";

	static {

		localFilePath = ExtractUtil.getDetail("oas.exportdata.filepath");
		ftphost = ExtractUtil.getDetail("oas.exportdata.ftphost");
		ftpuser = ExtractUtil.getDetail("oas.exportdata.ftpuser");
		ftppassword = ExtractUtil.getDetail("oas.exportdata.ftppassword");
		ftpFilepath = ExtractUtil.getDetail("oas.exportdata.ftp.filepath");
		ftpPort = ExtractUtil.getDetail("oas.exportdata.ftp.port");
		isClassNodeRequired = ExtractUtil.getDetail("isClassNodeRequired");
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
	public static String getIsClassNodeRequired() {
		return isClassNodeRequired;
	}

}
