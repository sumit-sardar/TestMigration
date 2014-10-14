package com.ctb.utils;

/**
 * This Class is used to Load the values from the Loaded Properties File
 * 
 * @author TCS
 * 
 */
public class Configuration {

	private static String localFilePath = "";
	private static String ftpHost = "";
	private static String ftpUser = "";
	private static String ftpFilePath = "";
	private static String ftpPort = "";
	private static String archivePath = "";
	private static String customerId = "";
	private static String errorPath = "";
	private static String clientPrivateKeyPath = "";
	private static String finalErrorPath = "";
	static String log4jFile = "";

	/**
	 * Email related key-pair configurations
	 */
	private static String emailAlerts = "";
	
	private static String emailSender = "";
	private static String emailAlias = "";
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
		finalErrorPath = ExtractUtil.getDetail("oas.importdata.ftp.finalerrorpath");
		customerId = ExtractUtil.getDetail("oas.customerId");
		clientPrivateKeyPath = ExtractUtil.getDetail("oas.private.keyPath");
		log4jFile = ExtractUtil.getDetail("oas.log4j.file");
		
		emailAlerts = ExtractUtil.getDetail("email.alerts");
		emailSender = ExtractUtil.getDetail("email.sender");
		emailAlias = ExtractUtil.getDetail("email.sender.alias");
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

	public static String getLocalFilePath() {
		return localFilePath;
	}

	public static String getFtpHost() {
		return ftpHost;
	}

	public static String getFtpUser() {
		return ftpUser;
	}

	public static String getFtpFilePath() {
		return ftpFilePath;
	}

	public static String getFtpPort() {
		return ftpPort;
	}

	public static String getArchivePath() {
		return archivePath;
	}

	public static String getCustomerId() {
		return customerId;
	}

	public static String getErrorPath() {
		return errorPath;
	}

	public static String getClientPrivateKeyPath() {
		return clientPrivateKeyPath;
	}

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

	public static String getEmailAlias() {
		return emailAlias;
	}

	public static String getFinalErrorPath() {
		return finalErrorPath;
	}
	
	
}
