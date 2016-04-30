package com.ctb.utils;

/**
 * This Class is used to load the values from the external Properties File
 * 
 * @author TCS
 * 
 */
public class Configuration {

	static String localFilePath = "";
	static String log4jFile = "";
	static {
		localFilePath = ExtractUtil.getDetail("oas.import.sfdata.filepath");
		log4jFile = ExtractUtil.getDetail("oas.log4j.file");		
	}
	
	/**
	 * @return the localFilePath
	 */
	public static String getLocalFilePath() {
		return localFilePath;
	}
	/**
	 * @param localFilePath the localFilePath to set
	 */
	public static void setLocalFilePath(String localFilePath) {
		Configuration.localFilePath = localFilePath;
	}
	/**
	 * @return the log4jFile
	 */
	public static String getLog4jFile() {
		return log4jFile;
	}
	/**
	 * @param log4jFile the log4jFile to set
	 */
	public static void setLog4jFile(String log4jFile) {
		Configuration.log4jFile = log4jFile;
	}
	
}
