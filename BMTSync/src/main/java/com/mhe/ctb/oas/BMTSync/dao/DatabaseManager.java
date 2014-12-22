package com.mhe.ctb.oas.BMTSync.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mhe.ctb.oas.BMTSync.util.ReadPropertyFile;

public class DatabaseManager {
	//private static DataSource datasource = null; // holds the database object
	//private static Context initContext = null;  // used to lookup database connection
	
	protected static Connection dbConnection_JNDI() {
		//Tomcat tomcat = new Tomcat();
		//tomcat.enableNaming();
		
		Connection conn = null;
		try {
			//if (initContext == null) {
				
			//}
			//serverName = (String) initContext.lookup("java:/comp/env/oasDataSource");
			//System.out.println(serverName);
			Context initContext = new InitialContext();
			initContext.addToEnvironment("enablenaming", true);
			Context envContext = (Context)initContext.lookup("java:comp/env");
			DataSource datasource = (DataSource) envContext.lookup("jdbc/oasDataSource");			
			conn = datasource.getConnection();
		}  catch (NamingException ne) {
			ne.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		} 
		
		return conn;
	}
	
	protected static Connection dbConnection() throws IOException {
		//URL of Oracle database server
        //String urlPems = "jdbc:oracle:thin:@10.160.23.34:1521:pemsdev";
        //String urlOAS = "jdbc:oracle:thin:@168.116.29.112:1521:OASR51D1";
        //String dbUserName = "oas"; // "phglobal"
        //String dbPwd = "oasr5d";  //"phglobal"

		ReadPropertyFile readConfigPropertyFile = new ReadPropertyFile(); 
        String dbDriverName = readConfigPropertyFile.getDbDriverName();
        String dbURL = readConfigPropertyFile.getDbURL();
        String dbUserName = readConfigPropertyFile.getDbUserName();
        String dbPwd = readConfigPropertyFile.getDbPwd();
        
        //properties for creating connection to Oracle database
        Properties props = new Properties();
        props.setProperty("user", dbUserName);
        props.setProperty("password", dbPwd);

    
		Connection conn = null;
		try {
			//Class.forName("oracle.jdbc.OracleDriver");
			Class.forName(dbDriverName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			// Create connection to oracle database using JDBC
			conn = DriverManager.getConnection(dbURL,props);
			
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}

}
