package com.ctb.utils;

public class Configuration {

	static String localFilePath = "";
	static String ftphost = "";
	static String ftpuser = "";
	static String ftppassword = "";
	static String ftpDataFilepath = "";
	static String ftpOrderFilepath = "";
	static Integer ftpFilePort = 0;
	
	static String devloperEmailId = "";
	static String devloperEmailSubject = "";
	static String devloperEmailBody = "";
	static String devloperEmailReplyTo = "";
	
	static String userEmailSubject = "";
	static String userEmailBody = "";
	static String userEmailReplyTo = "";
	
	static String localFilePathEngrade = "";
	static String ftphostLAUSD = "";
	static String ftpuserLAUSD = "";
	static String ftppasswordLAUSD = "";
	static String ftpDataFilepathLAUSD = "";
	static String ftpOrderFilepathLAUSD = "";
	static Integer ftpFilePortLAUSD = 0;
	static String clientPrivateKeyPath = "";
	
	static String customerConfigurationName="";

	static {

		localFilePath = ExtractUtil.getDetail("oas.exportdata.local.filestore"); 
		ftphost = ExtractUtil.getDetail("oas.exportdata.ftphost");
		ftpuser = ExtractUtil.getDetail("oas.exportdata.ftpuser");
		ftppassword = ExtractUtil.getDetail("oas.exportdata.ftppassword");
		ftpDataFilepath = ExtractUtil.getDetail("oas.exportdata.remote.ftp.datafile.filestore");
		ftpOrderFilepath = ExtractUtil.getDetail("oas.exportdata.remote.ftp.orderfile.filestore");
		ftpFilePort = new Integer(ExtractUtil.getDetail("oas.exportdata.remote.ftp.port"));
		
		devloperEmailId = ExtractUtil.getDetail("oas.exportdata.ftp.failure.email.to");
		devloperEmailSubject = ExtractUtil.getDetail("oas.exportdata.ftp.failure.email.subject");
		devloperEmailBody = ExtractUtil.getDetail("oas.exportdata.ftp.failure.email.body");
		devloperEmailReplyTo = ExtractUtil.getDetail("oas.exportdata.ftp.failure.email.replyTo");

		userEmailSubject = ExtractUtil.getDetail("oas.exportdata.user.email.subject");
		userEmailBody = ExtractUtil.getDetail("oas.exportdata.user.email.body");
		userEmailReplyTo = ExtractUtil.getDetail("oas.exportdata.user.email.replyTo");
		
		ftphostLAUSD = ExtractUtil.getDetail("engrade.exportdata.ftphost");
		ftpuserLAUSD = ExtractUtil.getDetail("engrade.exportdata.ftpuser");
		ftppasswordLAUSD = ExtractUtil.getDetail("engrade.exportdata.ftppassword");
		ftpDataFilepathLAUSD = ExtractUtil.getDetail("engrade.exportdata.remote.ftp.datafile.filestore");
		ftpOrderFilepathLAUSD = ExtractUtil.getDetail("engrade.exportdata.remote.ftp.orderfile.filestore");
		ftpFilePortLAUSD = new Integer(ExtractUtil.getDetail("engrade.exportdata.remote.ftp.port"));
		localFilePathEngrade = ExtractUtil.getDetail("engrade.exportdata.local.filestore");
		customerConfigurationName = ExtractUtil.getDetail("engrade.customer.configuration.name");
		clientPrivateKeyPath = ExtractUtil.getDetail("engrade.oas.private.keyPath");
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

	/**
	 * @return the ftpDataFilepath
	 */
	public static String getFtpDataFilepath() {
		return ftpDataFilepath;
	}

	/**
	 * @return the ftpOrderFilepath
	 */
	public static String getFtpOrderFilepath() {
		return ftpOrderFilepath;
	}

	/**
	 * @return the ftpFilePort
	 */
	public static Integer getFtpFilePort() {
		return ftpFilePort;
	}

	/**
	 * @return the ftphostLAUSD
	 */
	public static String getFtphostLAUSD() {
		return ftphostLAUSD;
	}

	/**
	 * @return the ftpuserLAUSD
	 */
	public static String getFtpuserLAUSD() {
		return ftpuserLAUSD;
	}

	/**
	 * @return the ftppasswordLAUSD
	 */
	public static String getFtppasswordLAUSD() {
		return ftppasswordLAUSD;
	}

	/**
	 * @return the ftpDataFilepathLAUSD
	 */
	public static String getFtpDataFilepathLAUSD() {
		return ftpDataFilepathLAUSD;
	}

	/**
	 * @return the ftpOrderFilepathLAUSD
	 */
	public static String getFtpOrderFilepathLAUSD() {
		return ftpOrderFilepathLAUSD;
	}

	/**
	 * @return the ftpFilePortLAUSD
	 */
	public static Integer getFtpFilePortLAUSD() {
		return ftpFilePortLAUSD;
	}

	/**
	 * @return the customerConfigurationName
	 */
	public static String getCustomerConfigurationName() {
		return customerConfigurationName;
	}

	/**
	 * @return the localFilePathEngrade
	 */
	public static String getLocalFilePathEngrade() {
		return localFilePathEngrade;
	}

	/**
	 * @return the clientPrivateKeyPath
	 */
	public static String getClientPrivateKeyPath() {
		return clientPrivateKeyPath;
	}
		
}
