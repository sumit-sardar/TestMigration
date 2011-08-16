package com.ctb.tms.rdb.hsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ctb.tms.rdb.ADSRDBSink;

public class ADSHSQLSink implements ADSRDBSink {
	
	public Connection getADSConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return HSQLSetup.getADSConnection();
	}
	
	public void shutdown() {
		HSQLSetup.shutdown();
	}
	
	private static final String PUT_SUBTEST_SQL = "insert into subtest (subtest_id, subtest) values (?, ?)";
	private static final String PUT_ITEM_SQL = "insert into item (item_id, item) values (?, ?)";

	
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
}
