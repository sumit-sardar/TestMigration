package com.ctb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * This class provides JDBC utility functions.
 * 
 * @author TCS
 * 
 */
public class SQLUtil {
	
	static Logger logger = Logger.getLogger(SQLUtil.class.getName());
	/**
	 * Creates Connection with Database and returns Connection object
	 * 
	 * @return Connection
	 */
	public static Connection getConnection() {
		String dbip = ExtractUtil.getDetail("oas.db.host.address").trim();
		String sid = ExtractUtil.getDetail("oas.db.sid.address").trim();
		String user = ExtractUtil.getDetail("oas.db.user.name").trim();
		String password = ExtractUtil.getDetail("oas.db.user.password").trim();
		String port = ":" + ExtractUtil.getDetail("oas.db.port.address") + ":";

		String connURL = "jdbc:oracle:thin:@" + dbip + port + sid;
		logger.info("Connection URL >> "+connURL);
		Connection conn = null;

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(connURL, user, password);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			logger.error("SQLException: occurred while getting connection. "+e.getMessage());
		} catch (Exception e) {
			System.err.println("Exception occurred while getting connection. "+ e.getMessage());
			logger.error("SQLException: occurred while getting connection. "+e.getMessage());
		}

		return conn;
	}

	/**
	 * Closes the Objects
	 * 
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public static void closeDbObjects(Connection conn, Statement st,
			ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.err
						.println("Exception occurred while closing the Connection."
								+ e.getMessage());
				logger.error("Exception occurred while closing the Connection. "+e.getMessage());
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				System.err
						.println("Exception occurred while closing the Statement."
								+ e.getMessage());
				logger.error("Exception occurred while closing the Statement. "+e.getMessage());
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.err
						.println("Exception occurred while closing the ResultSet."
								+ e.getMessage());
				logger.error("Exception occurred while closing the ResultSet. "+e.getMessage());
			}
		}

	}

}
