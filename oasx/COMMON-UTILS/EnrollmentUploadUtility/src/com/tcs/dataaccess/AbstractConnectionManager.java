package com.tcs.dataaccess;

import java.io.FileInputStream;
import java.util.Properties;

public class AbstractConnectionManager {

	static String url;
	static String driver;
	static String userName;
	static String pwd;
	static String customerId;

	public static void processProperties(String fileName) throws Exception {

		Properties prop = new Properties();
		prop.load(new FileInputStream(fileName));
		url = prop.getProperty("URL");
		if (url == null) {

			throw new Exception();
		}

		driver = prop.getProperty("DRIVER");
		if (driver == null) {

			throw new Exception();
		}

		userName = prop.getProperty("USERNAME");
		if (userName == null) {

			throw new Exception();
		}

		pwd = prop.getProperty("PWD");
		if (pwd == null) {

			throw new Exception();
		}

		customerId = prop.getProperty("rts.customerId");
		if (customerId == null)
			throw new Exception(
					"rts.customerId is not present in the properties file...");

	}// end of method

	public static String getUrl() {
		return url;
	}

	public static String getDriver() {
		return driver;
	}

	public static String getUserName() {
		return userName;
	}

	public static String getPwd() {
		return pwd;
	}

	public static String getCustomerId() {
		return customerId;
	}

}// end of class
