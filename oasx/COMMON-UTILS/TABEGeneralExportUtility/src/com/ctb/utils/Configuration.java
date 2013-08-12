package com.ctb.utils;

public class Configuration {

	static String localFilePath = "";
	static String ftphost = "";
	static String ftpuser = "";
	static String ftppassword = "";
	static String ftpFilepath = "";

	static {
		/*localFilePath = ExtractUtil.getDetail("oas.exportdata.filepath");
		ftphost = ExtractUtil.getDetail("oas.exportdata.ftphost");
		ftpuser = ExtractUtil.getDetail("oas.exportdata.ftpuser");
		ftppassword = ExtractUtil.getDetail("oas.exportdata.ftppassword");
		ftpFilepath = ExtractUtil.getDetail("oas.exportdata.ftp.filepath");*/
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

}
