package com.ctb.security.provider.authentication;

/**
* DExAuthOAMConnection.java
* @author Tata Consultancy Services 
* This Class is created for getting the user name by sending
* token to OAM after validating it for DEx Application 
*
*/

import java.util.*;
import java.io.*;
import java.util.*;
import java.text.*;
import javax.security.auth.Subject;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.net.URLConnection;

public final class DExAuthOAMConnection {

	private String token = null;
	private final static String RESOURCE_BUNDLE_FILE = "resource"; 
	private static ResourceBundle rb;
	
	static {
		rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_FILE);
	}
	
	public DExAuthOAMConnection() {

	}
	/*
	 * This method is really used to get the user name from OAM
	 * after authentication by providing token from ctb.com
	 * @param token
	 * @return username
	 * @exception NotFoundException
	 */
	public String getUserNameFromToken(String token) {	
		// Print debugging information
		OASAuthLogger.getLogger().debug("DExAuthOAMConnection.getUserNameFromToken");
		String userName = "";
		ObjectInputStream outputFromOAMProxy = null;
		
		try {

			if (token == null || "".equals(token)) {
				token = "";
			}
			this.token = token;


			URL url = new URL( getResource("ssoproxy.ip")
							   + token);

			// Temporary code if resource bundle does not work
	/*		URL url = new URL( "http://ca17dmhe0006:8080/ssoproxy?oamSessionID="
					   + token);*/
			URLConnection conn = url.openConnection();
			conn.setDoInput(true);
			outputFromOAMProxy =
						new ObjectInputStream(conn.getInputStream());
			
			userName = (String) outputFromOAMProxy.readObject();

		} catch(Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				outputFromOAMProxy.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	  return userName;
	}
	
	/*
	 * This method is used to get the resource bundle from
	 * properties file
	 * @return ResourceBundle
	 */
	private static ResourceBundle getBundle() {
        if (rb == null)
            rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_FILE);
        return rb;
    }
	
	/*
	 * This method will return the actual resource
	 * from key value pair in properies file
	 * by providing the key
	 * @param key
	 * @return resource
	 */
	public static String getResource(String key) {
		String resource = getBundle().getString(key);
		return resource;
	}
	

}
