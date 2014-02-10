package com.ctb.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public  class BaseConnection {

	private  String user ;
	private  String password ;
	private  String host;
	private  String sid ;
	private  String driverName = "jdbc:oracle:thin:@";
	
	public BaseConnection(String host,String sid, String user, String password ){
		this.user= user;
		this.password = password;
		this.sid = sid;
		this.host = host;
	}

	public Connection getConnection() throws Exception {
		return SQLUtil.getConnection(connectionUrl(), user, password);
	}

	public String connectionUrl() {

		String url;
		url = driverName + host + ":1521:" + sid;

		return url;
	}

	/**
	 * 
	 * @param fName
	 * @return
	 * @throws SQLException
	 */
	public boolean insertBatchData(String[] fName) throws SQLException {

		Connection con = null;
		Statement stml = null;
		boolean isInsert = false;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			stml = con.createStatement();
			for (int i = 0; i < fName.length; i++) {
				stml.addBatch(fName[i]);
			}
			stml.executeBatch();
			con.commit();
			isInsert = true;

		} catch (Exception e) {
			con.rollback();
			System.err.println("Exception occurred while inserting using batch.");
			e.printStackTrace();
			isInsert = false;
		} finally {
          SQLUtil.close(stml);
          SQLUtil.close(con);
		}

		return isInsert;

	}

	

	protected String getSQLString(String colValue) {
		if (colValue != null) {
			return "'" + colValue.trim() + "'";
		} else {
			return colValue;

		}

	}

	public void closeConnection(Connection con) throws SQLException {
		if (con != null) {
			con.close();
		}
	}


}
