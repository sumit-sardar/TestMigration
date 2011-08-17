package com.ctb.tms.rdb.hsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class HSQLSetup {
	
	static Logger logger = Logger.getLogger(HSQLSetup.class);
	
	public static Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		Connection connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oas", "SA", "");
		return connection;
	}
	
	public static Connection getADSConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return getOASConnection();
	}
	
	static {
		Connection conn = null;
		try {
			conn = getOASConnection();
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("CREATE TEXT TABLE STUDENT (user_name VARCHAR(32), password VARCHAR(32), access_code VARCHAR(32))");
				ps.executeUpdate();
			} catch (Exception e) {
				logger.info("***** HSQLDB - STUDENT table exists");
			}
			try {
				ps = conn.prepareStatement("CREATE TEXT TABLE ROSTER (user_name VARCHAR(32), password VARCHAR(32), access_code VARCHAR(32), roster VARCHAR(65535))");
				ps.executeUpdate();
			} catch (Exception e) {
				logger.info("***** HSQLDB - ROSTER table exists");
			}
			try {
				ps = conn.prepareStatement("CREATE TEXT TABLE RESPONSE (test_roster_id VARCHAR(32), item_id VARCHAR(32), seq_num VARCHAR(32), response VARCHAR(32))");
				ps.executeUpdate();
			} catch (Exception e) {
				logger.info("***** HSQLDB - RESPONSE table exists");
			}
			try {
				ps = conn.prepareStatement("CREATE TEXT TABLE MANIFEST (test_roster_id VARCHAR(32), item_set_id VARCHAR(32), completion_status VARCHAR(32))");
				ps.executeUpdate();
			} catch (Exception e) {
				logger.info("***** HSQLDB - MANIFEST table exists");
			}
			try {
				ps = conn.prepareStatement("SET TABLE STUDENT SOURCE 'student;fs=|'");
				ps.executeUpdate();
			} catch (Exception e) {
				// do nothing
			}
			try {
				ps = conn.prepareStatement("SET TABLE ROSTER SOURCE 'roster;fs=|'");
				ps.executeUpdate();
			} catch (Exception e) {
				// do nothing
			}
			try {
				ps = conn.prepareStatement("SET TABLE RESPONSE SOURCE 'response;fs=|'");
				ps.executeUpdate();
			} catch (Exception e) {
				// do nothing
			}
			try {
				ps = conn.prepareStatement("SET TABLE MANIFEST SOURCE 'manifest;fs=|'");
				ps.executeUpdate();
			} catch (Exception e) {
				// do nothing
			}
			try {
				ps = conn.prepareStatement("CREATE TEXT TABLE SUBTEST (subtest_id VARCHAR(32), subtest VARCHAR(65535))");
				ps.executeUpdate();
			} catch (Exception e) {
				logger.info("***** HSQLDB - SUBTEST table exists");
			}
			try {
				ps = conn.prepareStatement("CREATE TEXT TABLE ITEM (item_id VARCHAR(32), item VARCHAR(65535))");
				ps.executeUpdate();
			} catch (Exception e) {
				logger.info("***** HSQLDB - ITEM table exists");
			}
			try {
				ps = conn.prepareStatement("SET TABLE SUBTEST SOURCE 'subtest;fs=|'");
				ps.executeUpdate();
			} catch (Exception e) {
				// do nothing
			}
			try {
				ps = conn.prepareStatement("SET TABLE ITEM SOURCE 'item;fs=|'");
				ps.executeUpdate();
			} catch (Exception e) {
				// do nothing
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) conn.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}
	
	public static void shutdown() {
		Connection conn = null;
		try {
			conn = getOASConnection();
			PreparedStatement ps = conn.prepareStatement("SHUTDOWN");
			ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) conn.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}

}
