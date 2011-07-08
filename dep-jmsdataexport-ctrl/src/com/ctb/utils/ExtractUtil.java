package com.ctb.utils;

import java.util.Enumeration;
import java.util.ResourceBundle;

	public class ExtractUtil extends ResourceBundle {

		private static ResourceBundle rb;
		static {
			rb = ResourceBundle.getBundle("config");
		}
		
		
		public static String getDetail(String key){
			return rb.getString(key);
			
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

