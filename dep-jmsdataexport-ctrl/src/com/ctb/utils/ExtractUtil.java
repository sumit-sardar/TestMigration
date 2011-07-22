package com.ctb.utils;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

	public class ExtractUtil extends ResourceBundle {

		private static ResourceBundle rb;
		
		static {
			rb = ResourceBundle.getBundle("irs-security");
		}
		
				
		public static String getDetail(String key){
			String val = "";
			try{
				val = rb.getString(key);
			} catch (MissingResourceException e){
				System.err.println("No value for the given key ["+key+"] is found");
			} catch (Exception e){
				System.err.println("Unexpected exception while reading property file");
				e.printStackTrace();
			}
			return val.trim();
			
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

