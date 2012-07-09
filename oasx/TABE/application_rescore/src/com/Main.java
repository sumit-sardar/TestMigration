/**
 * 
 */
package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.ctb.tdc.web.db.OASRescore;
import com.ctb.tdc.web.utils.ExtractUtil;


public class Main {
	static Connection conn = null;
	private static String userName = "";
	private static String password = "";
	private static String[] rosterIdList = {};
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		try{
			
			System.out.println("rosterId*** ");
			String rosterID = ExtractUtil.getDetail("rosterId");
			if(rosterID.length()>0 && rosterID.indexOf(",")!=-1){
				rosterIdList = rosterID.split(",");
			}
			else{
				rosterIdList = new String[1];
				rosterIdList[0] = ExtractUtil.getDetail("rosterId");
			}
						
			System.out.println("rosterId*** "+rosterID);
			conn = getConnection();
			
			System.out.println("conn*** "+conn);
			OASRescore oRescore = new OASRescore();
			for(int i=0;i<rosterIdList.length;i++){
				System.out.println("rosterIdList********     "+rosterIdList[i]);
				oRescore.getRoster(Integer.parseInt(rosterIdList[i]),conn);
			}
		}
		catch (Exception e) {
			System.out.println("exception in getting rescoring "+e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	
	public static Connection getConnection() throws SQLException {

		Connection connection = null;
		try {
		    // Load the JDBC driver
		    
		    Class.forName ("oracle.jdbc.driver.OracleDriver");
		    userName = ExtractUtil.getDetail("userName");
			password = ExtractUtil.getDetail("password");

		    // Create a connection to the database
		    String serverName = ExtractUtil.getDetail("serverName");
		    String portNumber = ExtractUtil.getDetail("portNumber");
		    String sid = ExtractUtil.getDetail("sid");
		    String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
		    connection = DriverManager.getConnection(url, userName, password);
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException in getConnection "+e);
		    // Could not find the database driver
		} catch (SQLException e) {
			System.err.println("SQLException in getConnection "+e);
		    // Could not connect to the database
		}
		catch (Exception e) {
			System.err.println("Exception in getConnection "+e);
		    // Could not connect to the database
		}
		return connection;

	}


}
