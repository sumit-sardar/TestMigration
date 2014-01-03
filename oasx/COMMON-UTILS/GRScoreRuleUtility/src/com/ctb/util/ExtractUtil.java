package com.ctb.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

	public class ExtractUtil extends ResourceBundle {

		private static ResourceBundle rb;
		
		/*static {
			rb = ResourceBundle.getBundle("config");
		}*/
		
		public static void loadPropertiesExternally(String propertiesFileName, String propertiesFilePath){
			File inFile = new File (propertiesFilePath);
			ClassLoader loader = null;			
			try {
				URL [] urls = {inFile.toURI().toURL()};
				loader = new URLClassLoader(urls);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rb = ResourceBundle.getBundle(propertiesFileName, Locale.getDefault(), loader);						
		}
		
		public static String getDetail(String key){
			try {
				return rb.getString(key);
			} catch (MissingResourceException mre) {
				return null;
			}
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