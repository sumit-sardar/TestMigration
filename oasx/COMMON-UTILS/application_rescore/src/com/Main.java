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
import java.util.ArrayList;
import java.util.Iterator;

import com.ctb.tdc.web.db.OASRescore;
import com.ctb.tdc.web.to.TestData;
import com.ctb.tdc.web.utils.ExtractUtil;


public class Main {
	static Connection conn = null;
	private static String userName = "";
	private static String password = "";
	private static String[] rosterIdList = {};
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		try{
			ArrayList<TestData> testData = new ArrayList<TestData>();
			String rosterID = ExtractUtil.getDetail("rosterId");
			if(rosterID.length()>0 && rosterID.indexOf(",")!=-1){
				rosterIdList = rosterID.split(",");
			}
			else{
				rosterIdList = new String[1];
				rosterIdList[0] = ExtractUtil.getDetail("rosterId");
			}
						
			OASRescore oRescore = new OASRescore();
			for(int i=0;i<rosterIdList.length;i++){
				conn = getConnection();
				testData = oRescore.getRoster(Integer.parseInt(rosterIdList[i]),conn);
			}
			oRescore.updateScore(conn,testData);
			oRescore.updateRescoredStatus(conn,testData);
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
		    userName = ExtractUtil.getDetail("oas.db.user.name");
			password = ExtractUtil.getDetail("oas.db.user.password");

		    // Create a connection to the database
		    String serverName = ExtractUtil.getDetail("oas.db.host.address");
		    String portNumber = ExtractUtil.getDetail("oas.db.portNumber");
		    String sid = ExtractUtil.getDetail("oas.db.sid.address");
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
