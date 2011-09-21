package com.ctb.tms.rdb.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class OracleSetup {
	/* private static String ADSDatabaseURL = "jdbc:oracle:thin:@nj09mhe0393-vip.mhe.mhc:1521:oasr5t1";
	private static String ADSDatabaseUser = "ads";
	private static String ADSDatabaseUserPassword = "adspr5r";
	private static String ADSDatabaseJDBCDriver = "oracle.jdbc.driver.OracleDriver"; */
	/* private static String OASDatabaseURL = "jdbc:oracle:thin:@nj09mhe0393-vip.mhe.mhc:1521:oasr5t1";
	private static String OASDatabaseUser = "oas";
	private static String OASDatabaseUserPassword = "oaspr5r";
	private static String OASDatabaseJDBCDriver = "oracle.jdbc.driver.OracleDriver"; */
	
	public static final String TEST_SQL = "select 1 from dual";
	
	public static boolean useWeblogicJNDI = false;
	public static boolean useTomcatJNDI = false;
	
	static Logger logger = Logger.getLogger(OracleSetup.class);
	
	public static Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection newConn = null;
		try {  
			Context envContext = null;
			if(useWeblogicJNDI) {
				 envContext = new InitialContext();
			} else if(useTomcatJNDI){
				Context initContext = new InitialContext();
				envContext  = (Context)initContext.lookup("java:/comp/env");
			} else {
				return null;
			}
			DataSource ds = (DataSource)envContext.lookup("oasDataSource");
			newConn = ds.getConnection(); 
			//logger.info("*****  Using OASDataSource for DB connection");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return newConn;
	}
	
	public static Connection getADSConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection newConn = null;
		try {    
			Context envContext = null;
			if(useWeblogicJNDI) {
				 envContext = new InitialContext();
			} else if(useTomcatJNDI){
				Context initContext = new InitialContext();
				envContext  = (Context)initContext.lookup("java:/comp/env");
			} else {
				return null;
			}
			DataSource ds = (DataSource)envContext.lookup("adsDataSource");
			newConn = ds.getConnection();  
			//logger.info("*****  Using ADSDataSource for DB connection");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return newConn;
	}
	
	static {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			useTomcatJNDI = true;
			conn = getOASConnection();
			ps = conn.prepareStatement(TEST_SQL);
			ps.executeQuery();
			logger.info("***** Using Tomcat JNDI syntax");
		} catch (Exception e) {
			logger.error("Couldn't lookup oasDataSource or execute test SQL using Tomcat JNDI syntax: " + e.getMessage());
			useTomcatJNDI = false;
		} finally {
			try {
				if (ps != null) ps.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		if(!useTomcatJNDI) {
			try {
				useWeblogicJNDI = true;
				conn = getOASConnection();
				ps = conn.prepareStatement(TEST_SQL);
				ps.executeQuery();
				logger.info("***** Using Weblogic JNDI syntax");
			} catch (Exception e) {
				logger.error("Couldn't lookup oasDataSource or execute test SQL using Weblogic JNDI syntax: " + e.getMessage());
				useWeblogicJNDI = false;
			} finally {
				try {
					if (ps != null) ps.close();
					if (conn != null) conn.close();
				} catch (Exception e) {
					// do nothing
				}
			}
		}
	}
	
	public static void shutdown() {
		Connection conn = null;
		try {
			conn = getOASConnection();
			PreparedStatement ps = conn.prepareStatement("SHUTDOWN");
			ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) conn.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}

}
