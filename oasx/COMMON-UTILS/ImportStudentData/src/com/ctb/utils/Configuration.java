package com.ctb.utils;

public class Configuration {

	static String localFilePath = "";
	static String ftpHost = "";
	static String ftpUser = "";
	static String ftpPassword = "";
	static String ftpFilePath = "";
	static String ftpPort = "";
	static String archivePath = "";
	static String customerId = "";
	static String errorPath = "";
	static String clientPrivateKeyPath = "";
	
	static {
		localFilePath = ExtractUtil.getDetail("oas.importdata.local.filepath");
		ftpHost = ExtractUtil.getDetail("oas.importdata.ftpHost");
		ftpUser = ExtractUtil.getDetail("oas.importdata.ftpUser");
		ftpPassword = ExtractUtil.getDetail("oas.importdata.ftpPassword");
		ftpFilePath = ExtractUtil.getDetail("oas.importdata.ftp.filepath");
		ftpPort = ExtractUtil.getDetail("oas.importdata.ftp.port");
		archivePath = ExtractUtil.getDetail("oas.importdata.ftp.archivepath");
		errorPath = ExtractUtil.getDetail("oas.importdata.ftp.errorpath");
		customerId = ExtractUtil.getDetail("oas.customerId");
		clientPrivateKeyPath = ExtractUtil.getDetail("oas.private.keyPath");
	}

	/**
	 * @return the localFilePath
	 */
	public static String getLocalFilePath() {
		return localFilePath;
	}



	/**
	 * @return the ftpHost
	 */
	public static String getFtpHost() {
		return ftpHost;
	}



	public static String getClientPrivateKeyPath() {
		return clientPrivateKeyPath;
	}



	/**
	 * @return the ftpUser
	 */
	public static String getFtpUser() {
		return ftpUser;
	}



	/**
	 * @return the ftpPassword
	 *//*
	public static String getFtpPassword() {
		return ftpPassword;
	}*/



	/**
	 * @return the ftpFilePath
	 */
	public static String getFtpFilePath() {
		return ftpFilePath;
	}



	/**
	 * @return the ftpPort
	 */
	public static String getFtpPort() {
		return ftpPort;
	}



	/**
	 * @return the archivePath
	 */
	public static String getArchivePath() {
		return archivePath;
	}



	/**
	 * @return the customerId
	 */
	public static String getCustomerId() {
		return customerId;
	}



	/**
	 * @return the errorPath
	 */
	public static String getErrorPath() {
		return errorPath;
	}

	
	
	
}
