package com.ctb.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class ExtractUtils extends ResourceBundle{

	private static ResourceBundle rb;
	
	// static {
	// rb = ResourceBundle.getBundle("conf");
	// }
	
	/**
	 * read the external properties file
	 * @param propertyFileName
	 * @param externalFilePath
	 */
	public static void loadExternalProperties(String externalFilePath,String propertyFileName){			
		try {
			File infile = new File(externalFilePath);
			ClassLoader loader = null;			
			URL[] urls = {infile.toURI().toURL()};
			loader = new URLClassLoader(urls);
			rb = ResourceBundle.getBundle(propertyFileName, Locale.getDefault(), loader);
		} catch (MalformedURLException me) {
			me.printStackTrace();
			System.exit(1);
		} catch (Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * get the value against the key from resource bundle		
	 * @param key
	 * @return value in String
	 */
	public static String get(String key){
		if (rb.containsKey(key))
			return rb.getString(key);
		else return "";
	}

	@Override
	public Enumeration<String> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object handleGetObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
