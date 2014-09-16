package com.ctb.utils;

/**
 * This Class is used to Load the values from the Loaded Properties File
 * 
 * @author TCS
 * 
 */
public class Configuration {

	static String localFilePath = "";
	static String ftpHost = "";
	static String ftpUser = "";
	static String ftpFilePath = "";
	static String ftpPort = "";
	static String archivePath = "";
	static String customerId = "";
	static String errorPath = "";
	static String clientPrivateKeyPath = "";
	static String demographics = "";
	static String log4jFile = "";
	
	/**
	 * Email related key-pair configurations
	 */
	private static String emailAlerts = "";
	
	private static String emailSender = "";
	private static String emailRecipient = "";
	private static String emailCC = "";
	private static String emailBCC = "";
	private static String emailSubjectFtpIssue = "";
	private static String emailBodyFtpIsuue = "";
	private static String emailSubjectDownloadFileIssue = "";
	private static String emailBodyDownloadFileIssue = "";
	private static String emailSubjectNoFileIssue = "";
	private static String emailBodyNoFileIssue = "";
	private static String emailSubjectMoreFilesIssue = "";
	private static String emailBodyMoreFilesIssue = "";
	private static String emailSubjectFileEmptyIssue = "";
	private static String emailBodyFileEmptyIssue = "";
	private static String emailSubjectFileHeaderValidationIssue = "";
	private static String emailBodyFileHeaderValidationIssue = "";
	private static String emailSubjectImportSuccess = "";
	private static String emailBodyImportSuccess = "";
	private static String emailSubjectArchiveFTPIssue = "";
	private static String emailBodyArchiveFTPIssue = "";
	private static String emailSubjectErrorFileFTPIssue = "";
	private static String emailBodyErrorFileFTPIssue = "";

	static {
		localFilePath = ExtractUtil.getDetail("oas.importdata.local.filepath");
		ftpHost = ExtractUtil.getDetail("oas.importdata.ftpHost");
		ftpUser = ExtractUtil.getDetail("oas.importdata.ftpUser");
		ftpFilePath = ExtractUtil.getDetail("oas.importdata.ftp.filepath");
		ftpPort = ExtractUtil.getDetail("oas.importdata.ftp.port");
		archivePath = ExtractUtil.getDetail("oas.importdata.ftp.archivepath");
		errorPath = ExtractUtil.getDetail("oas.importdata.ftp.errorpath");
		customerId = ExtractUtil.getDetail("oas.customerId");
		clientPrivateKeyPath = ExtractUtil.getDetail("oas.private.keyPath");
		demographics = ExtractUtil.getDetail("oas.manual.demographics");
		log4jFile = ExtractUtil.getDetail("oas.log4j.file");
		
		emailAlerts = ExtractUtil.getDetail("email.alerts");
		emailSender = ExtractUtil.getDetail("email.sender");
		emailRecipient = ExtractUtil.getDetail("email.recipient");
		emailCC = ExtractUtil.getDetail("email.cc");
		emailBCC = ExtractUtil.getDetail("email.bcc");
		emailSubjectFtpIssue = ExtractUtil.getDetail("email.subject.ftpIssue");
		emailBodyFtpIsuue = ExtractUtil.getDetail("email.body.ftpIssue");
		emailSubjectDownloadFileIssue = ExtractUtil.getDetail("email.subject.downloadFileIssue");
		emailBodyDownloadFileIssue = ExtractUtil.getDetail("email.body.downloadFileIssue");
		emailSubjectNoFileIssue = ExtractUtil.getDetail("email.subject.noFileIssue");
		emailBodyNoFileIssue = ExtractUtil.getDetail("email.body.noFileIssue");
		emailSubjectMoreFilesIssue = ExtractUtil.getDetail("email.subject.moreFilesPresentIssue");
		emailBodyMoreFilesIssue = ExtractUtil.getDetail("email.body.moreFilesPresentIssue");
		emailSubjectFileEmptyIssue = ExtractUtil.getDetail("email.subject.fileEmptyIssue");
		emailBodyFileEmptyIssue = ExtractUtil.getDetail("email.body.fileEmptyIssue");
		emailSubjectFileHeaderValidationIssue = ExtractUtil.getDetail("email.subject.fileHeaderValidationIssue");
		emailBodyFileHeaderValidationIssue = ExtractUtil.getDetail("email.body.fileHeaderValidationIssue");
		emailSubjectImportSuccess = ExtractUtil.getDetail("email.subject.importSuccess");
		emailBodyImportSuccess = ExtractUtil.getDetail("email.body.importSuccess");
		emailSubjectArchiveFTPIssue = ExtractUtil.getDetail("email.subject.archiveFTPIssue");
		emailBodyArchiveFTPIssue = ExtractUtil.getDetail("email.body.archiveFTPIssue");
		emailSubjectErrorFileFTPIssue = ExtractUtil.getDetail("email.subject.errorFileFTPIssue");
		emailBodyErrorFileFTPIssue = ExtractUtil.getDetail("email.body.errorFileFTPIssue");
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
	 */
	/*
	 * public static String getFtpPassword() { return ftpPassword; }
	 */

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

	/**
	 * @return the demographics
	 */
	public static String getDemographics() {
		return demographics;
	}

	/**
	 * @return the log4jFile
	 */
	public static String getLog4jFile() {
		return log4jFile;
	}

	public static String getEmailAlerts() {
		return emailAlerts;
	}

	public static String getEmailSender() {
		return emailSender;
	}

	public static String getEmailRecipient() {
		return emailRecipient;
	}

	public static String getEmailCC() {
		return emailCC;
	}

	public static String getEmailBCC() {
		return emailBCC;
	}

	public static String getEmailSubjectFtpIssue() {
		return emailSubjectFtpIssue;
	}

	public static String getEmailBodyFtpIsuue() {
		return emailBodyFtpIsuue;
	}

	public static String getEmailSubjectDownloadFileIssue() {
		return emailSubjectDownloadFileIssue;
	}

	public static String getEmailBodyDownloadFileIssue() {
		return emailBodyDownloadFileIssue;
	}

	public static String getEmailSubjectNoFileIssue() {
		return emailSubjectNoFileIssue;
	}

	public static String getEmailBodyNoFileIssue() {
		return emailBodyNoFileIssue;
	}

	public static String getEmailSubjectMoreFilesIssue() {
		return emailSubjectMoreFilesIssue;
	}

	public static String getEmailBodyMoreFilesIssue() {
		return emailBodyMoreFilesIssue;
	}

	public static String getEmailSubjectFileEmptyIssue() {
		return emailSubjectFileEmptyIssue;
	}

	public static String getEmailBodyFileEmptyIssue() {
		return emailBodyFileEmptyIssue;
	}

	public static String getEmailSubjectFileHeaderValidationIssue() {
		return emailSubjectFileHeaderValidationIssue;
	}

	public static String getEmailBodyFileHeaderValidationIssue() {
		return emailBodyFileHeaderValidationIssue;
	}

	public static String getEmailSubjectImportSuccess() {
		return emailSubjectImportSuccess;
	}

	public static String getEmailBodyImportSuccess() {
		return emailBodyImportSuccess;
	}

	public static String getEmailSubjectArchiveFTPIssue() {
		return emailSubjectArchiveFTPIssue;
	}

	public static String getEmailBodyArchiveFTPIssue() {
		return emailBodyArchiveFTPIssue;
	}

	public static String getEmailSubjectErrorFileFTPIssue() {
		return emailSubjectErrorFileFTPIssue;
	}

	public static String getEmailBodyErrorFileFTPIssue() {
		return emailBodyErrorFileFTPIssue;
	}
	
	

}
