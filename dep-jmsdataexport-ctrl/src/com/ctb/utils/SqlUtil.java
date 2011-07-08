package com.ctb.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;
import javax.sql.DataSource;

public class SqlUtil {

	private static String oasDtataSourceJndiName= "oasDataSource";
	private static String irsDtataSourceJndiName= "irsDataSource";

	public static Connection openOASDBcon() throws Exception {
		/*String connURL = "jdbc:oracle:thin:@" + "168.116.29.112" + ":1521:"
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

		return conn;*/
		Connection conn = null;
		try {
			DataSource ds = ServiceLocator.locateDataSource(oasDtataSourceJndiName);
			conn = ds.getConnection();
		} catch (NamingException e) {
			System.err.println("NamingException:"+ "JNDI name does not found.");
			e.printStackTrace();
			throw new Exception("NamingException:"+ "JNDI name does not found.");
		} catch (SQLException e) {
			System.err.println("SQLException:"+ "while getting connection.");
			e.printStackTrace();
			throw new Exception("NamingException:"+ "while getting connection.");
		}
		
		return conn;
		
	}

	public static Connection openIRSDBcon() throws Exception {
		/*String connURL = "jdbc:oracle:thin:@" + "168.116.29.112" + ":1521:"
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

		return conn;*/
		Connection conn = null;
		try {
			DataSource ds = ServiceLocator.locateDataSource(irsDtataSourceJndiName);
			conn = ds.getConnection();
		} catch (NamingException e) {
			System.err.println("NamingException:"+ "JNDI name does not found.");
			e.printStackTrace();
			throw new Exception("NamingException:"+ "JNDI name does not found.");
		} catch (SQLException e) {
			System.err.println("SQLException:"+ "while getting connection.");
			e.printStackTrace();
			throw new Exception("NamingException:"+ "while getting connection.");
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
