package com.ctb.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceUtils extends ResourceBundle{

	
	private static ResourceBundle rb = null;
	
	public static void loadExternalProperties (String propertiesFileName, String propertiesFilePath){
		File inputFile = new File(propertiesFilePath);
		ClassLoader loader = null;
		
		try {
			URL[] urls = {inputFile.toURI().toURL()};
			loader = new URLClassLoader(urls);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		rb = ResourceBundle.getBundle(propertiesFileName, Locale.getDefault(), loader);
		
	}
	
	public static String getDetail(String key){
			return rb.getString(key);
	}
	
	public Enumeration getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	protected Object handleGetObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
