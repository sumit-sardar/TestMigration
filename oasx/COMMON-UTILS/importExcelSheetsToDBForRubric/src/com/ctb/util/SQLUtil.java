package com.ctb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQLUtil {

	public static Connection getConnection(String ConnectionUrl, String user, String password) throws Exception {
		Connection connection = null;
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			connection = DriverManager.getConnection(ConnectionUrl, user, password);
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			System.err.println("SQLException occurred while getting connection.");
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("Exception occurred while getting connection.");
			e.printStackTrace();
			throw e;
		}

		return connection;
	}
	
	public static void close(Connection con)  {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("Exception occurred while closing connection.");
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Statement stmt)  {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Exception occurred while closing statement.");
				e.printStackTrace();
			}
		}
	}
	
	public static void close(ResultSet rs)  {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println("Exception occurred while closing ResultSet.");
				e.printStackTrace();
			}
		}
	}
	
}
