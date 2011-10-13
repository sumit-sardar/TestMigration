package com.ctb.contentcreator.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.ctb.common.tools.SystemException;
import com.ctb.contentcreator.bin.Configuration;


public class ConnectionUtil {

	private static ThreadLocal oasLocal = new ThreadLocal();
	private static ThreadLocal adsLocal = new ThreadLocal();

	public static Connection getOASConnection(Configuration conf) {
		Connection oasconn = (Connection) oasLocal.get();
		if (oasconn == null) {
			oasconn = establishOASConnection(conf);
			oasLocal.set(oasconn);
			System.out.println("new OAS Connection created for thread: [" + Thread.currentThread().getName()+"]");
		}
		return oasconn;
	}

	public static Connection getADSConnection(Configuration conf) {
		Connection adsconn = (Connection) adsLocal.get();
		if (adsconn == null) {
			adsconn = establishADSConnection(conf);
			adsLocal.set(adsconn);
			System.out.println("new ADS Connection created for thread: ["+Thread.currentThread().getName()+"]");
		}
		return adsconn;
	}

	private static Connection establishOASConnection(Configuration conf) {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			String url = getOASConnectionUrl(conf);
			Connection conn = DriverManager.getConnection(url, conf.getUser(),
					conf.getPassword());
			return conn;
		} catch (SQLException e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	private static Connection establishADSConnection(Configuration conf) {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			String url = getADSConnectionUrl(conf);
			Connection conn = DriverManager.getConnection(url, conf
					.getAdsDbUser(), conf.getAdsDbPassword());
			return conn;
		} catch (SQLException e) {
			throw new SystemException(e.getMessage(), e);
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
					+ conf.getSid();
		} else {
			url = "jdbc:oracle:oci:@" + conf.getAdsDbSid();
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
}
