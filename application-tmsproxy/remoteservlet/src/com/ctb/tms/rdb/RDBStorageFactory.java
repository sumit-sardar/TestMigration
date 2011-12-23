package com.ctb.tms.rdb;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.ctb.tms.rdb.oracle.ADSOracleSink;
import com.ctb.tms.rdb.oracle.ADSOracleSource;
import com.ctb.tms.rdb.oracle.OASOracleSink;
import com.ctb.tms.rdb.oracle.OASOracleSource;

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
	
	static Logger logger = Logger.getLogger(RDBStorageFactory.class);
	
	static {
		//synchronized(RDBStorageFactory.class) {
			ResourceBundle rb = ResourceBundle.getBundle("storage");
			oassource = rb.getString("storage.rdb.source");
			oassink = rb.getString("storage.rdb.sink");
			adssource = rb.getString("storage.rdb.source");
			adssink = rb.getString("storage.rdb.sink");
			
			copytosink = rb.getString("storage.rdb.copytosink");
		//}
	}
	
	public static OASRDBSource getOASSource() {
		if(oasSource == null) {
			if (ORACLE.equals(oassource)) {
				logger.info("*****  Using OASOracleSource");
				oasSource = new OASOracleSource();
			} else {
				// default to Oracle
				logger.info("*****  Using OASOracleSource");
				oasSource = new OASOracleSource();
			}
		}
		return oasSource;
	}
	
	public static OASRDBSink getOASSink() {
		if(oasSink == null) {
			if (ORACLE.equals(oassink)) {
				logger.info("*****  Using OASOracleSink");
				oasSink = new OASOracleSink();
			} else {
				// default to Oracle
				logger.info("*****  Using OASOracleSink");
				oasSink = new OASOracleSink();
			}
		}
		return oasSink;
	}
	
	public static ADSRDBSource getADSSource() {
		if(adsSource == null) {
			if (ORACLE.equals(adssource)) {
				logger.info("*****  Using ADSOracleSource");
				adsSource = new ADSOracleSource();
			} else {
				// default to Oracle
				logger.info("*****  Using ADSOracleSource");
				adsSource = new ADSOracleSource();
			}
		}
		return adsSource;
	}
	
	public static ADSRDBSink getADSSink() {
		if(adsSink == null) {
			if (ORACLE.equals(adssink)) {
				logger.info("*****  Using ADSOracleSink");
				adsSink = new ADSOracleSink();
			} else {
				// default to Oracle
				logger.info("*****  Using ADSOracleSink");
				adsSink = new ADSOracleSink();
			}
		}
		return adsSink;
	}
}
