package com.ctb.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ctb.exception.CTBBusinessException;

public class SqlUtil {

	private static String oasDtataSourceJndiName= "oasDataSource";
	private static String irsDtataSourceJndiName= "irsDataSource";

	public static Connection openOASDBcon() throws CTBBusinessException {
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
			conn.setAutoCommit(false);
		} catch (NamingException e) {
			System.err.println("NamingException:"+ "JNDI name for oas datasource does not exists.");
			//e.printStackTrace();
			throw new CTBBusinessException("NamingException:"+ "JNDI name for oas datasource does not exists.");
		} catch (SQLException e) {
			System.err.println("SQLException:"+ "while getting oas database connection.");
			//e.printStackTrace();
			throw new CTBBusinessException("SQLException:"+ "while getting oas database connection.");
		} catch (Exception e) {
			System.err.println("Exception:"+ "while getting oas database connection.");
			//e.printStackTrace();
			throw new CTBBusinessException("Exception:"+ "while getting oas database connection.");
		}
		
		return conn;
		
	}

	public static Connection openIRSDBcon() throws CTBBusinessException {
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
			conn.setAutoCommit(false);
		} catch (NamingException e) {
			System.err.println("NamingException:"+ "JNDI name for irs datasource does not found.");
			//e.printStackTrace();
			throw new CTBBusinessException("NamingException:"+ "JNDI name for irs datasource does not found.");
		} catch (SQLException e) {
			System.err.println("SQLException:"+ "while getting irs database connection.");
			//e.printStackTrace();
			throw new CTBBusinessException("NamingException:"+ "while getting irs database connection.");
		} catch (Exception e) {
			System.err.println("Exception:"+ "while getting irs database connection.");
			//e.printStackTrace();
			throw new CTBBusinessException("Exception:"+ "while getting  irs database connection.");
		}
		
		return conn;
	}

	public static void close(Connection con) {
		if (con != null) {
			try {
				con.rollback();
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
		close(rs);
		close(st);

	}
	public static void close(Connection con, Statement st, ResultSet rs) {
		close(rs);
		close(st);
		close(con);

	}

	public static void close(Connection con, Statement st) {
		close(st);
		close(con);
		
	}

}
