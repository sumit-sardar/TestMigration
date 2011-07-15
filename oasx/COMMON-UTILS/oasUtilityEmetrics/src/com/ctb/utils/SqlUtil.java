package com.ctb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtil {

	public static Connection openOASDBcon() {
		String connURL = "jdbc:oracle:thin:@" + "168.116.29.112" + ":1521:"
				+ "OASR51D1";

		Connection conn = null;

		try {

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(connURL, "oas", "oasr5d");
			conn.setAutoCommit(false);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();

		}

		return conn;
	}

	public static Connection openIRSDBcon() {
		String connURL = "jdbc:oracle:thin:@" + "168.116.29.112" + ":1521:"
				+ "irsr5d1";

		Connection conn = null;

		try {

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(connURL, "irs", "irsr5d");
			conn.setAutoCommit(false);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();

		}

		return conn;
	}

	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

	public static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

	public static void close(Statement st, ResultSet rs) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

}
