package com.ctb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class provides JDBC utility functions.
 * 
 * @author TCS
 * 
 */
public class SQLUtil {
	
	public static Connection getConnection()  
	{
		String dbip = ExtractUtil.getDetail("oas.db.host.address").trim();
		String sid = ExtractUtil.getDetail("oas.db.sid.address").trim();
		String user = ExtractUtil.getDetail("oas.db.user.name").trim();
		String password = ExtractUtil.getDetail("oas.db.user.password").trim();
		String port = ":"+ExtractUtil.getDetail("oas.db.port.address")+":";
		
		String connURL = "jdbc:oracle:thin:@" + dbip + port	+ sid;

		Connection conn = null;
		
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(connURL, user, password);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception occurred while getting connection.");
			e.printStackTrace();
		}

		return conn;
	}
	
	public static void closeDbObjects(Connection conn, Statement st, ResultSet rs) 
	{
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.err.println("Exception occurred while closing the Connection.");
				e.printStackTrace();
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				System.err.println("Exception occurred while closing the Statement.");
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.err.println("Exception occurred while closing the ResultSet.");
				e.printStackTrace();
			}
		}

	}

}
