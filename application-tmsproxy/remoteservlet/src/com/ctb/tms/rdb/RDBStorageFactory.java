package com.ctb.tms.rdb;

import java.util.ResourceBundle;

import com.ctb.tms.nosql.NoSQLStorageFactory;

public class RDBStorageFactory {

	private static final String ORACLE = "ORACLE";
	private static final String HSQL = "HSQL";
	
	private static String oassource = ORACLE;
	private static String oassink = HSQL;
	
	private static String adssource = ORACLE;
	private static String adssink = HSQL;
	
	public static String copytosink;
	
	private static OASRDBSource oasSource;
	private static OASRDBSink oasSink;
	private static ADSRDBSource adsSource;
	private static ADSRDBSink adsSink;
	
	static {
		synchronized(RDBStorageFactory.class) {
			ResourceBundle rb = ResourceBundle.getBundle("storage");
			oassource = rb.getString("storage.rdb.source");
			oassink = rb.getString("storage.rdb.sink");
			adssource = rb.getString("storage.rdb.source");
			adssink = rb.getString("storage.rdb.sink");
			
			copytosink = rb.getString("storage.rdb.copytosink");
		}
	}
	
	public static OASRDBSource getOASSource() {
		if(oasSource == null) {
			if(HSQL.equals(oassource)) {
				System.out.println("*****  Using OASHSQLSource");
				oasSource = new OASHSQLSource();
			} else if (ORACLE.equals(oassource)) {
				System.out.println("*****  Using OASOracleSource");
				oasSource = new OASOracleSource();
			} else {
				// default to Oracle
				oasSource = new OASOracleSource();
			}
		}
		return oasSource;
	}
	
	public static OASRDBSink getOASSink() {
		if(oasSink == null) {
			if(HSQL.equals(oassink)) {
				System.out.println("*****  Using OASHSQLSink");
				oasSink = new OASHSQLSink();
			} else if (ORACLE.equals(oassink)) {
				System.out.println("*****  Using OASOracleSink");
				oasSink = new OASOracleSink();
			} else {
				// default to Oracle
				oasSink = new OASOracleSink();
			}
		}
		return oasSink;
	}
	
	public static ADSRDBSource getADSSource() {
		if(adsSource == null) {
			if(HSQL.equals(adssource)) {
				System.out.println("*****  Using ADSHSQLSource");
				adsSource = new ADSHSQLSource();
			} else if (ORACLE.equals(adssource)) {
				System.out.println("*****  Using ADSOracleSource");
				adsSource = new ADSOracleSource();
			} else {
				// default to Oracle
				adsSource = new ADSOracleSource();
			}
		}
		return adsSource;
	}
	
	public static ADSRDBSink getADSSink() {
		if(adsSink == null) {
			if(HSQL.equals(adssink)) {
				System.out.println("*****  Using ADSHSQLSink");
				adsSink = new ADSHSQLSink();
			} else if (ORACLE.equals(adssink)) {
				System.out.println("*****  Using ADSOracleSink");
				adsSink = new ADSOracleSink();
			} else {
				// default to Oracle
				adsSink = new ADSOracleSink();
			}
		}
		return adsSink;
	}
}
