package com.ctb.contentBridge.core.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.exception.ExceptionResolver;


public class ConnectionUtil {

	private static ThreadLocal oasLocal = new ThreadLocal();
	private static ThreadLocal adsLocal = new ThreadLocal();
	private static ThreadLocal cbLocal = new ThreadLocal();

	public static Connection getOASConnection(Configuration conf) throws Exception {
		Connection oasconn = (Connection) oasLocal.get();
		if (oasconn == null || oasconn.isClosed()) {
			oasconn = establishOASConnection(conf);
			oasLocal.set(oasconn);
			System.out.println("new OAS Connection created for thread: [" + Thread.currentThread().getName()+"]");
		}
		return oasconn;
	}

	public static Connection getADSConnection(Configuration conf) throws Exception {
		Connection adsconn = (Connection) adsLocal.get();
		if (adsconn == null || adsconn.isClosed()) {
			adsconn = establishADSConnection(conf);
			adsLocal.set(adsconn);
			adsconn.setAutoCommit(false);
			System.out.println("new ADS Connection created for thread: ["+Thread.currentThread().getName()+"]");
		}
		return adsconn;
	}
	
	public static Connection getContBrgConnection(Configuration conf) throws Exception {
		Connection cbConn = (Connection) cbLocal.get();
		if (cbConn == null || cbConn.isClosed()) {
			cbConn = establishContBrgConnection(conf);
			cbLocal.set(cbConn);
			System.out.println("new Content Bridge Connection created for thread: ["+Thread.currentThread().getName()+"]");
		}
		return cbConn;
	}

	private static Connection establishOASConnection(Configuration conf) throws Exception {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			String url = getOASConnectionUrl(conf);
			System.out.println("OAS USER-->>>>"+conf.getUser()+" "+url+" "+conf.getPassword());
			Connection conn = DriverManager.getConnection(url, conf.getUser(),
					conf.getPassword());
			return conn;
		} catch (SQLException e) {
			throw ExceptionResolver.resolve(e);
		}
	}

	private static Connection establishADSConnection(Configuration conf) throws Exception {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			String url = getADSConnectionUrl(conf);
			Connection conn = DriverManager.getConnection(url, conf
					.getAdsDbUser(), conf.getAdsDbPassword());
			return conn;
		} catch (SQLException e) {
			throw ExceptionResolver.resolve(e);
		}
	}
	
	private static Connection establishContBrgConnection(Configuration conf) throws Exception {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			String url = getContBrgConnectionUrl(conf);
			Connection conn = DriverManager.getConnection(url, conf
					.getCbDbUser(), conf.getCbDbPassword());
			return conn;
		} catch (SQLException e) {
			throw ExceptionResolver.resolve(e);
		}
	}

	private static String getOASConnectionUrl(Configuration conf) {
		String url;

		if (conf.isUseThin()) {
			url = "jdbc:oracle:thin:@" + conf.getHost() + ":1521:"
					+ conf.getSid();
		} else {
			url = "jdbc:oracle:oci:@" + conf.getSid();
		}
		return url;
	}

	private static String getADSConnectionUrl(Configuration conf) {
		String url;

		if (conf.isUseThin()) {
			url = "jdbc:oracle:thin:@" + conf.getAdsDbHost() + ":1521:"
					+ conf.getAdsDbSid();
		} else {
			url = "jdbc:oracle:oci:@" + conf.getAdsDbSid();
		}
		return url;
	}
	
	private static String getContBrgConnectionUrl(Configuration conf) {
		String url;

		if (conf.isUseThin()) {
			url = "jdbc:oracle:thin:@" + conf.getCbDbHost() + ":1521:"
					+ conf.getCbDbSid();
		} else {
			url = "jdbc:oracle:oci:@" + conf.getCbDbSid();
		}
		return url;
	}

	public static void closeADSConnection() {
		Connection oasconn = (Connection) adsLocal.get();
		if (oasconn != null) {
			try {
				oasconn.close();
				System.out.println(" ADS Connection closed for thread: ["+Thread.currentThread().getName()+"]");
			} catch (SQLException e) {
			}
			oasconn = null;
		}

	}

	public static void closeOASConnection() {
		Connection adsconn = (Connection) oasLocal.get();
		if (adsconn != null) {
			try {
				System.out.println(" OAS Connection closed for thread: ["+Thread.currentThread().getName()+"]");
				adsconn.close();
			} catch (SQLException e) {
			}
			adsconn = null;
		}

	}
	
	public static void closeContBrgConnection() {
		Connection cbConn = (Connection) cbLocal.get();
		if (cbConn != null) {
			try {
				System.out.println(" Content Bridge Connection closed for thread: ["+Thread.currentThread().getName()+"]");
				cbConn.close();
			} catch (SQLException e) {
			}
			cbConn = null;
		}

	}
}
