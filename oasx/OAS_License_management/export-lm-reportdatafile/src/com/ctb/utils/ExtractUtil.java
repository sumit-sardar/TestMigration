package com.ctb.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * This class provides properties file handling utility functions.
 * 
 * @author TCS
 * 
 */
public class ExtractUtil extends ResourceBundle {

	private static ResourceBundle rb;
	static Logger logger = Logger.getLogger(ExtractUtil.class.getName());

	public static void loadPropetiesFile(String fileName) {
		rb = ResourceBundle.getBundle(fileName);
	}

	/**
	 * Loads the properties file from the path specified.
	 * 
	 * @param baseName
	 *            - Properties File Name
	 * @param externalPropertiesFilePAth
	 *            - Properties File Path
	 */
	public static void loadExternalPropetiesFile(String baseName,
			String externalPropertiesFilePath) {
		File file = new File(externalPropertiesFilePath);
		ClassLoader loader = null;
		try {
			URL[] urls = { file.toURI().toURL() };
			loader = new URLClassLoader(urls);
		} catch (MalformedURLException e) {
			System.err.println("Properties File Loading Error. System exiting..");
			logger.error("Properties File Loading Error. System exiting..");
			e.printStackTrace();
			System.exit(1);
		}
		rb = ResourceBundle.getBundle(baseName, Locale.getDefault(), loader);
		// logger.info("Properties File Successfully Loaded of Environment :: "
		// + baseName);
	}

	/**
	 * Returns the Value mapped to the key
	 * 
	 * @param key
	 *            - Key of Properties File
	 * @return Value of the Key
	 */
	public static String getDetail(String key) {
		return (rb.containsKey(key))?rb.getString(key):"";
	}

	@Override
	public Enumeration<String> getKeys() {
		return rb.getKeys();
	}

	@Override
	protected Object handleGetObject(String key) {
		return null;
	}

}
