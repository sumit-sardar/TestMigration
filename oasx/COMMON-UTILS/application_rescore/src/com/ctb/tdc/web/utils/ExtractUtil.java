package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ExtractUtil extends ResourceBundle {

	private static ResourceBundle rb;
	private static HashMap<String,String> prop = new HashMap<String,String>();

	static {
		rb = ResourceBundle.getBundle("resource/rescore");
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
	
	/*public static HashMap getFile () throws Exception {
		InputStream is = null;
		BufferedReader br = null;
		try {
			is = new FileInputStream (new File ( "./resource/rescore.properties" ));
			br = new BufferedReader(new InputStreamReader(is));
			String str;
			while((str = br.readLine()) != null){
				if(str.indexOf("=")!=-1){
					prop.put(str.substring(0,str.indexOf("=")), str.substring(str.indexOf("=")+1));
				}
			}
			System.out.println(prop.get("rosterId"));
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
		}
		finally{
			is.close();
			br.close();
		}
		return prop;
	}*/


}

