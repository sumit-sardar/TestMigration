package com.ctb.schedular;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ctb.lexington.exception.CTBBusinessException;

/**
 * @author TCS Kolkata Offshore
 * @version 05/09/2012
 */
public class SqlUtil {
 
	private static String oasDtataSourceJndiName = "oasDataSource";
	private static String irsDtataSourceJndiName = "irsDataSource";

	public static Connection openOASDBcon(boolean isCommitable)
			throws CTBBusinessException {
		Connection conn = null;
		try {
			DataSource ds = ServiceLocator
					.locateDataSource(oasDtataSourceJndiName);
			conn = ds.getConnection();
			if (isCommitable) {
				conn.setAutoCommit(false);
			}
		} catch (NamingException e) {
			System.err.println("NamingException:"
					+ "JNDI name for oas datasource does not exists.");
			throw new CTBBusinessException("NamingException:"
					+ "JNDI name for oas datasource does not exists.");
		} catch (SQLException e) {
			System.err.println("SQLException:"
					+ "while getting oas database connection.");
			throw new CTBBusinessException("SQLException:"
					+ "while getting oas database connection.");
		} catch (Exception e) {
			System.err.println("Exception:"
					+ "while getting oas database connection.");
			throw new CTBBusinessException("Exception:"
					+ "while getting oas database connection.");
		}

		return conn;

	}

	public static Connection openIRSDBcon(boolean isCommitable)
			throws CTBBusinessException {

		Connection conn = null;
		try {
			DataSource ds = ServiceLocator
					.locateDataSource(irsDtataSourceJndiName);
			conn = ds.getConnection();
			if (isCommitable) {
				conn.setAutoCommit(false);
			}
		} catch (NamingException e) {
			System.err.println("NamingException:"
					+ "JNDI name for irs datasource does not found.");
			throw new CTBBusinessException("NamingException:"
					+ "JNDI name for irs datasource does not found.");
		} catch (SQLException e) {
			System.err.println("SQLException:"
					+ "while getting irs database connection.");
			throw new CTBBusinessException("NamingException:"
					+ "while getting irs database connection.");
		} catch (Exception e) {
			System.err.println("Exception:"
					+ "while getting irs database connection.");
			throw new CTBBusinessException("Exception:"
					+ "while getting  irs database connection.");
		}

		return conn;
	}

}
