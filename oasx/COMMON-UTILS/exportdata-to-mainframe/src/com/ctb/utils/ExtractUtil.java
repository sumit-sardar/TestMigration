package com.ctb.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

	public class ExtractUtil extends ResourceBundle {

		private static ResourceBundle rb;
		
		/*static {
			rb = ResourceBundle.getBundle("config");
		}*/
		
		public static void loadExternalProperties(String propertyFileName, String externalFilePath){			
			File infile = new File(externalFilePath);
			ClassLoader loader = null;			
			try {				
				URL[] urls = {infile.toURI().toURL()};
				loader = new URLClassLoader(urls);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}			
			rb = ResourceBundle.getBundle(propertyFileName, Locale.getDefault(), loader);
		}
		
				
		public static String getDetail(String key){
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

