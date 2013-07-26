package com.ctb.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseConnection {

	private  String user /*= "oas"*/;
	private  String password /*= "oasr5d"*/;
	private  String host /*= "168.116.29.112"*/;

	private  String sid /*= "OASR51D1"*/;
	// private static boolean useThin = true;
	//private  Connection connection = null;
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
		url = driverName + host + ":1521/" + sid;

		return url;
	}

	/*
	 * public boolean updateBatchData (String[] fName) throws SQLException {
	 * 
	 * Connection con = null; Statement stml = null; boolean isUpdate = false;
	 * try {
	 * 
	 * con = getConnection(); con.setAutoCommit(false); stml =
	 * con.createStatement(); for(int i=0;i<fName.length;i++) {
	 * if(fName[i].matches("|")) {
	 * 
	 * String []querys =fName[i].split("\\|"); for(int j=0;j<querys.length;j++) {
	 * 
	 * stml.addBatch(querys[j]) ; } } else {
	 * 
	 * stml.addBatch(getQueryString (fName[i])); } } stml.executeBatch();
	 * con.commit(); isUpdate = true; } catch (SQLException se) {
	 * 
	 * con.rollback(); se.printStackTrace(); isUpdate = false; } catch (
	 * Exception e) { con.rollback(); e.printStackTrace(); isUpdate = false; }
	 * finally {
	 * 
	 * con.close(); stml.close(); }
	 * 
	 * return isUpdate; }
	 */
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

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	/*
	 * public boolean insertData (String fName) throws SQLException {
	 * 
	 * Connection con = null; Statement stml = null; boolean isInsert = false;
	 * try {
	 * 
	 * con = getConnection(); stml = con.createStatement(); if
	 * (stml.executeUpdate(getQueryString (fName)) > 0) {
	 * 
	 * isInsert = true; } } catch (SQLException se) {
	 * 
	 * se.printStackTrace(); } catch ( Exception e) {
	 * 
	 * e.printStackTrace(); } finally {
	 * 
	 * con.close(); stml.close(); }
	 * 
	 * return isInsert; }
	 */

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	/*
	 * public boolean updateData (String fName) throws SQLException{
	 * 
	 * Connection con = null; Statement stml = null; boolean isUpdate = false;
	 * try {
	 * 
	 * con = getConnection(); stml = con.createStatement(); if
	 * (stml.executeUpdate(getQueryString (fName)) > 0) {
	 * 
	 * isUpdate = true; } } catch (SQLException se) {
	 * 
	 * se.printStackTrace(); } catch ( Exception e) {
	 * 
	 * e.printStackTrace(); } finally {
	 * 
	 * con.close(); stml.close(); }
	 * 
	 * return isUpdate; }
	 */

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	/*
	 * public HashMap retriveData (String fName) {
	 * 
	 * Connection con = null; Statement stml = null; ResultSet rs = null;
	 * HashMap hMap = new HashMap ();
	 * 
	 * try {
	 * 
	 * con = getConnection(); stml = con.createStatement();
	 * 
	 * rs = stml.executeQuery(getQueryString (fName)); //rs = stml.executeQuery(
	 * fName); hMap.put("con",con); hMap.put("stml",stml); hMap.put("rs", rs); }
	 * catch (SQLException se) {
	 * 
	 * se.printStackTrace(); } catch ( Exception e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * return hMap; }
	 */

	protected String convertDateToString(Date dateColumn) {

		if (dateColumn != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dateValue = "'" + sdf.format(dateColumn) + "'";
			return dateValue;
		} else {
			return null;
		}
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

	public abstract String[] getQueryString(String fName);

}
