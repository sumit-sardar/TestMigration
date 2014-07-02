package com.propertiesFileLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesFileReader {
	
	static Properties properties = new Properties();
	static{
		try{
			//String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
			String sPropFilePath = System.getProperty("OCPS_PROPERTIES_FILE");
			File file = new File(sPropFilePath);
			//File file = new File("D:\\OCPS_Local_File\\config.properties");
			FileInputStream fileInput = new FileInputStream(file);
			//Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String getValue(String key)
	{
		String propertyValue = "";
		//Properties properties;
		propertyValue = properties.getProperty(key);
		System.out.println("propertyValue-->"+propertyValue);
		return propertyValue;
	}
}


