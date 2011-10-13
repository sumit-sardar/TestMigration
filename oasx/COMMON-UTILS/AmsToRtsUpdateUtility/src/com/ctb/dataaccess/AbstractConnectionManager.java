package com.ctb.dataaccess;

import java.io.FileInputStream;
import java.util.Properties;

public class AbstractConnectionManager {
	
	 
	static String url;
	static String driver;
	static String userName;
	static String pwd;
	
	
	
	public static void processProperties (String fileName) throws Exception{
		
		Properties prop = new Properties ();
		prop.load(new FileInputStream (fileName));
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
		
		
	}

}
