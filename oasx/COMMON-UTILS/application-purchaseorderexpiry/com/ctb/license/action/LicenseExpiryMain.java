package com.ctb.license.action;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ctb.license.util.DBAccess;

public class LicenseExpiryMain {

	String env = "";
	private static DBAccess dbConnection = null;
	Properties properties = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LicenseExpiryMain lem = new LicenseExpiryMain();
		lem.updateAvailableLicenseOnLicenseExpiry(args);
	}
	
	private void updateAvailableLicenseOnLicenseExpiry(String[] args)
	{
		try {
			getCommandLine(args);
			dbConnection = DBAccess.createConnection(this.env);
			this.properties = loadProperties(this.env);
			DBAccess dbAccess = new DBAccess();
			dbAccess.callStoredProcedure("SP_LAS_LM_PO_EXPIRY_UPDATE");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void getCommandLine(String[] args)
	{
		if ((args.length < 1) || (args[0].indexOf('=') >= 0)) {
			System.out.println("Cannot parse command line. No command specified.");
		}
		else {
			this.env = args[0].toLowerCase();
		}
	}

	private static Properties loadProperties(String env)
	{
		InputStream in = null;
		Properties prop = new Properties();
		try {
			in = new FileInputStream(env);
			prop.load(in);
			return prop;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
