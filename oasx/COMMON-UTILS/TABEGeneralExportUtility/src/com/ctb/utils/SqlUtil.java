package com.ctb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.DataExportTABE;

public class SqlUtil {

	private static String oasDtataSourceJndiName= "oasDataSource";
	private static String irsDtataSourceJndiName= "irsDataSource";
	
	private static DataExportTABE dataExport = new DataExportTABE();

	public static Connection openOASDBcon() throws Exception {
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

	
	public static Connection openOASDBconnectionForResearch() {
		String dbip = dataExport.getPropertyValue("oas.db.host.address").trim();
		String sid = dataExport.getPropertyValue("oas.db.sid.address").trim();
		String user = dataExport.getPropertyValue("oas.db.user.name").trim();
		String password = dataExport.getPropertyValue("oas.db.user.password").trim();
		Connection conn = null;
		
		if(dbip != null && sid != null && user != null && password != null
				&& dbip.length() > 0 && sid.length() > 0 && user.length() > 0 && password.length() > 0) {
			
			String connURL = "jdbc:oracle:thin:@" + dbip + ":1521:"	+ sid;
			try {
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				conn = DriverManager.getConnection(connURL, user, password);
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Mandatory Input OASDBAddress/OASDBSID/OASDBUserName/OASDBPassword are missing in Resource Bundle.");
		}
		return conn;
	}

	public static Connection openIRSDBconnectionForResearch() {
		
		String dbip = dataExport.getPropertyValue("irs.db.host.address").trim();
		String sid = dataExport.getPropertyValue("irs.db.sid.address").trim();
		String user = dataExport.getPropertyValue("irs.db.user.name").trim();
		String password = dataExport.getPropertyValue("irs.db.user.password").trim();
		Connection conn = null;
		
		if(dbip != null && sid != null && user != null && password != null
				&& dbip.length() > 0 && sid.length() > 0 && user.length() > 0 && password.length() > 0) {
			
			String connURL = "jdbc:oracle:thin:@" + dbip + ":1521:" + sid;
			try {
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				conn = DriverManager.getConnection(connURL, user, password);
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Mandatory Input IRSDBAddress/IRSDBSID/IRSDBUserName/IRSDBPassword are missing in Resource Bundle.");
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
