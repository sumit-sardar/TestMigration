package com.tcs.dataaccess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;





/**
 * This class is used to (a) create a connection (b) open or close a connection
 * (c) close resultset, connection and different types of statements.
 * 
 * @author Tata Consultancy Services
 * @version 1.0
 */

public class ConnectionManager extends AbstractConnectionManager{

	//private static final Logger LOG = Logger.getLogger(ConnectionManager.class); 
	
	//private static DataSource ds;

    static
    {
    	try {
    		
    		/*DriverAdapterCPDS cpds = new DriverAdapterCPDS();
            cpds.setDriver(driver);
            cpds.setUrl(url);
            cpds.setUser(userName);
            cpds.setPassword(pwd);

            SharedPoolDataSource tds = new SharedPoolDataSource();
            tds.setConnectionPoolDataSource(cpds);
            tds.setMaxActive(10);
            tds.setMaxWait(50);

            ds = tds;*/
    		
    		
    		
    		
    	} catch (Exception e) {
    		
    		e.printStackTrace();
    	}
        
    }

    public static Connection getConnection () throws Exception {
    	Class.forName(driver);
		 Connection con = DriverManager.getConnection(url, userName, pwd);
    	return con;
    }

	
	/**
	 * This method is used to create a connection
	 * @param dataSource
	 * @return
	 */
	public static Connection createConnection(String dataSource) 
			throws SQLException, NamingException {
		//LOG.info("Entering of "+ConnectionManager.class.getName()+" in createConnection Method");
		Connection conn = null;
		
		try {
			Context ctx = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup(dataSource);
			conn = ds.getConnection();
		} catch (SQLException exceptionObject) {
			throw exceptionObject;
		} catch (NamingException exceptionObject) {
			throw exceptionObject;
		}
		//LOG.info("Exiting from "+ConnectionManager.class.getName()+" in createConnection Method");
		return conn;
	}

	
	/**
	 * This method commits a transaction
	 * @param conn
	 */
	public static void commitTransaction(Connection conn) 
			throws SQLException {
		//LOG.info("Entering of "+ConnectionManager.class.getName()+" in commitTransaction Method");
		try {
			if (conn != null) {
				conn.commit();
			}
		} catch (SQLException exceptionObject) {
			throw exceptionObject;
		}
		//LOG.info("Exiting from "+ConnectionManager.class.getName()+" in commitTransaction Method");
	}

	
	/**
	 * This method is used to roll back a transaction
	 * @param conn
	 */
	public static void rollBackTransaction(Connection conn) 
			throws SQLException {
		//LOG.info("Entering of "+ConnectionManager.class.getName()+" in rollBackTransaction Method");
		try {
			if (conn != null) {
				conn.rollback();
			}
		} catch (SQLException exceptionObject) {
			throw exceptionObject;
		}
		//LOG.info("Exiting from "+ConnectionManager.class.getName()+" in rollBackTransaction Method");
	}
	
	/**
	 * This method closes an open Connection
	 * @param conn
	 */
	public static void close(Connection conn) 
			throws SQLException {
		//LOG.info("Entering of "+ConnectionManager.class.getName()+" in close Method");
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException exceptionObject) {
			throw exceptionObject;
		}
		//LOG.info("Exiting from "+ConnectionManager.class.getName()+" in close Method");
	}
	
	
	/**
	 * This method closes an open CallableStatement
	 * @param callStatement
	 */
	public static void close(CallableStatement callStatement)
			throws SQLException {
		//LOG.info("Entering of "+ConnectionManager.class.getName()+" in close Method(CallableStatement)");
		try {
			if (callStatement != null) {
				callStatement.close();
			}
		} catch (SQLException exceptionObject) {
			throw exceptionObject;
		}
		//LOG.info("Exiting from "+ConnectionManager.class.getName()+" in close Method(CallableStatement)");
	}	
}
