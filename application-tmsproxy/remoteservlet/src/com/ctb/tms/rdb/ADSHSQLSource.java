package com.ctb.tms.rdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ADSHSQLSource implements ADSRDBSource {
	public Connection getADSConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oas", "SA", "");
	}
	
	private static final String GET_SUBTEST_SQL = "select * from subtest where subtest_id = ?";
	private static final String GET_ITEM_SQL = "select * from item where item_id = ?";

	public String getSubtest(Connection conn, int subtestId, String hash) {
		String result = null;
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = conn.prepareStatement(GET_SUBTEST_SQL);
			stmt1.setInt(1, subtestId);
			ResultSet rs = stmt1.executeQuery();
			if(rs.next()) {
				result = rs.getString("subtest");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		return result;
	}
	
	public String getItem(Connection conn, int itemId, String hash) {
		String result = null;
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = conn.prepareStatement(GET_ITEM_SQL);
			stmt1.setInt(1, itemId);
			ResultSet rs = stmt1.executeQuery();
			if(rs.next()) {
				result = rs.getString("item");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		return result;
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
