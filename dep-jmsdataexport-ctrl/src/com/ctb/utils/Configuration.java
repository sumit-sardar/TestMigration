package com.ctb.utils;

public class Configuration {

	static String localFilePath = "";
	static String ftphost = "";
	static String ftpuser = "";
	static String ftppassword = "";
	static String ftpFilepath = "";
	
	static String devloperEmailId = "";
	static String devloperEmailSubject = "";
	static String devloperEmailBody = "";
	static String devloperEmailReplyTo = "";
	
	static String userEmailSubject = "";
	static String userEmailBody = "";
	static String userEmailReplyTo = "";
	

	static {

		localFilePath = ExtractUtil.getDetail("oas.exportdata.local.filestore"); 
		ftphost = ExtractUtil.getDetail("oas.exportdata.ftphost");
		ftpuser = ExtractUtil.getDetail("oas.exportdata.ftpuser");
		ftppassword = ExtractUtil.getDetail("oas.exportdata.ftppassword");
		ftpFilepath = ExtractUtil.getDetail("oas.exportdata.remote.ftp.filestore");
		
		devloperEmailId = ExtractUtil.getDetail("oas.exportdata.ftp.failure.emil.to");
		devloperEmailSubject = ExtractUtil.getDetail("oas.exportdata.ftp.failure.emil.subject");
		devloperEmailBody = ExtractUtil.getDetail("oas.exportdata.ftp.failure.emil.body");
		devloperEmailReplyTo = ExtractUtil.getDetail("oas.exportdata.ftp.failure.emil.replyTo");

		userEmailSubject = ExtractUtil.getDetail("oas.exportdata.user.emil.subject");
		userEmailBody = ExtractUtil.getDetail("oas.exportdata.user.emil.body");
		userEmailReplyTo = ExtractUtil.getDetail("oas.exportdata.user.emil.replyTo");

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
	public static String getFtphost() {
		return ftphost;
	}

	/**
	 * @return the ftpuser
	 */
	public static String getFtpuser() {
		return ftpuser;
	}

	/**
	 * @return the ftppassword
	 */
	public static String getFtppassword() {
		return ftppassword;
	}

	/**
	 * @return the filepath
	 */
	public static String getFtpFilepath() {
		return ftpFilepath;
	}

	/**
	 * @return the devloperEmailId
	 */
	public static String getDevloperEmailId() {
		return devloperEmailId;
	}

	/**
	 * @return the devloperEmailSubject
	 */
	public static String getDevloperEmailSubject() {
		return devloperEmailSubject;
	}

	/**
	 * @return the devloperEmailBody
	 */
	public static String getDevloperEmailBody() {
		return devloperEmailBody;
	}

	/**
	 * @return the devloperEmailReplyTo
	 */
	public static String getDevloperEmailReplyTo() {
		return devloperEmailReplyTo;
	}

	/**
	 * @return the userEmailSubject
	 */
	public static String getUserEmailSubject() {
		return userEmailSubject;
	}

	/**
	 * @return the userEmailBody
	 */
	public static String getUserEmailBody() {
		return userEmailBody;
	}

	/**
	 * @return the userEmailReplyTo
	 */
	public static String getUserEmailReplyTo() {
		return userEmailReplyTo;
	}

}
