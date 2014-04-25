package com.ctb.util;

import java.io.Serializable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertiesLoader implements Serializable {
	
	private static final long serialVersionUID = 1L;	
	private static ResourceBundle rb;	
	static {
		rb = ResourceBundle.getBundle("config");
	}
	public static String getDetail(String key){
		try {
			return rb.getString(key);
		} catch (MissingResourceException mre) {
			return null;
		}
	}
}
