package com.ctb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBUtils {

	public static Connection getOASConnection()  
	{
		String dbip = ExtractUtils.get("oas.dbip");
		String sid = ExtractUtils.get("oas.sid");
		String user = ExtractUtils.get("oas.user");
		String password = ExtractUtils.get("oas.password");
		
		String connURL = "jdbc:oracle:thin:@" + dbip + ":1521:"	+ sid;
		Connection conn = null;
		try {

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(connURL, user, password);
			conn.setAutoCommit(false);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		return conn;
	}
	
	public static void close(Statement ps,ResultSet rs) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();// do nothing
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();// do nothing
			}
		}

	}
	
	public static void close(Connection conn){
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();// do nothing
			}
		}
	}
	
}
