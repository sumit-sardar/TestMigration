package com.ctb.tms.rdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ADSHSQLSink implements ADSRDBSink {
	public Connection getADSConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oas", "SA", "");
	}
	
	private static final String PUT_SUBTEST_SQL = "insert into subtest (subtest_id, subtest) values (?, ?)";
	private static final String PUT_ITEM_SQL = "insert into item (item_id, item) values (?, ?)";

	{
		Connection conn = null;
		try {
			conn = getADSConnection();
			PreparedStatement ps = conn.prepareStatement("CREATE TEXT TABLE SUBTEST (subtest_id VARCHAR(32), subtest VARCHAR(65535))");
			ps.executeUpdate();
			ps = conn.prepareStatement("SET TABLE SUBTEST SOURCE 'subtest;fs=|;cache_rows=0'");
			ps.executeUpdate();
			ps = conn.prepareStatement("CREATE TEXT TABLE ITEM (item_id VARCHAR(32), item VARCHAR(65535))");
			ps.executeUpdate();
			ps = conn.prepareStatement("SET TABLE ITEM SOURCE 'item;fs=|;cache_rows=0'");
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
	
	public void putSubtest(Connection conn, int subtestId, String subtest) {
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = conn.prepareStatement(PUT_SUBTEST_SQL);
			stmt1.setInt(1, subtestId);
			stmt1.setString(2, subtest);
			stmt1.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}
	
	public void putItem(Connection conn, int itemId, String item) {
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = conn.prepareStatement(PUT_ITEM_SQL);
			stmt1.setInt(1, itemId);
			stmt1.setString(2, item);
			stmt1.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	public void shutdown() {
		Connection conn = null;
		try {
			conn = getADSConnection();
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
