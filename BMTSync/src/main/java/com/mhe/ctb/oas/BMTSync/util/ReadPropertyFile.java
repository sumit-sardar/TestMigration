package com.mhe.ctb.oas.BMTSync.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadPropertyFile {
	
	protected Properties prop = null;
	//protected InputStream input = ReadPropertyFile.class.getClassLoader().getResourceAsStream("com/mhe/ctb/oas/BMTSync/util/config.properties");
	protected InputStream input = getClass().getClassLoader().getResourceAsStream("bmtsync.properties");
	public ReadPropertyFile() throws IOException {
		prop = new Properties();
		prop.load(input);
	}
	
	public String getDbDriverName() {
		return prop.getProperty("dbDriverName");
	}

	public String getDbURL(){
		return prop.getProperty("dbURL");
	}
	

    public String getDbUserName() {
		return prop.getProperty("dbUserName");
	}
	
	public String getDbPwd() {
		return prop.getProperty("dbPwd");
	}
	
	
	public int getNofRecordToBeFetched() {
		return Integer.parseInt(prop.getProperty("noOfRecordToFetch"));
	}

}
