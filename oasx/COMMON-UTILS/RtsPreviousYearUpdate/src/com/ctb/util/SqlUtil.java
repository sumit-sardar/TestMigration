package com.ctb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUtil {
	public static Connection openAMSDBcon()  
	{
		String dbip = ExtractUtil.getDetail("ams.db.host.address").trim();
		String sid = ExtractUtil.getDetail("ams.db.sid.address").trim();
		String user = ExtractUtil.getDetail("ams.db.user.name").trim();
		String password = ExtractUtil.getDetail("ams.db.user.password").trim();
		
		String connURL = "jdbc:oracle:thin:@" + dbip + ":1521/"	+ sid;

		Connection conn = null;
		try {

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(connURL, user, password);
			conn.setAutoCommit(false);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();

		}

		return conn;
	}
	public static Connection openOASDBcon()  
	{
		String dbip = ExtractUtil.getDetail("oas.db.host.address").trim();
		String sid = ExtractUtil.getDetail("oas.db.sid.address").trim();
		String user = ExtractUtil.getDetail("oas.db.user.name").trim();
		String password = ExtractUtil.getDetail("oas.db.user.password").trim();
		
		String connURL = "jdbc:oracle:thin:@" + dbip + ":1521/"	+ sid;

		Connection conn = null;
		try {

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(connURL, user, password);
			conn.setAutoCommit(false);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();

		}

		return conn;
	}
	public static void close(Connection con,PreparedStatement ps,ResultSet rs) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();// do nothing
			}
		}
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

}
