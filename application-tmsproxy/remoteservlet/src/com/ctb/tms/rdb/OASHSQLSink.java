package com.ctb.tms.rdb;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;
import sun.misc.BASE64Encoder;

import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public class OASHSQLSink implements OASRDBSink {
	public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oas", "SA", "");
	}
	
	{
		Connection conn = null;
		try {
			conn = getOASConnection();
			PreparedStatement ps = conn.prepareStatement("CREATE TEXT TABLE STUDENT (user_name VARCHAR(32), password VARCHAR(32), access_code VARCHAR(32))");
			ps.executeUpdate();
			ps = conn.prepareStatement("SET TABLE STUDENT SOURCE 'student;fs=|;cache_rows=0'");
			ps.executeUpdate();
			ps = conn.prepareStatement("CREATE TEXT TABLE ROSTER (user_name VARCHAR(32), password VARCHAR(32), access_code VARCHAR(32), roster VARCHAR(65535))");
			ps.executeUpdate();
			ps = conn.prepareStatement("SET TABLE ROSTER SOURCE 'roster;fs=|;cache_rows=0'");
			ps.executeUpdate();
			ps = conn.prepareStatement("CREATE TEXT TABLE RESPONSE (test_roster_id VARCHAR(32), item_id VARCHAR(32), seq_num VARCHAR(32), response VARCHAR(32))");
			ps.executeUpdate();
			ps = conn.prepareStatement("SET TABLE RESPONSE SOURCE 'response;fs=|;cache_rows=0'");
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
	
	public void shutdown() {
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
	
	private static final String ITEM_RESPONSE_SQL = "insert into response (test_roster_id, item_id, seq_num, response) values (?, ?, ?, ?)";
	private static final String ACTIVE_ROSTERS_SQL = "insert into student (user_name, password, access_code) values (?, ?, ?)";
	private static final String ROSTER_DATA_SQL = "insert into roster (user_name, password, access_code, roster) values (?, ?, ?, ?)";
	
	public void putItemResponse(Connection conn, String testRosterId, Tsd tsd) throws NumberFormatException, Exception {
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = conn.prepareStatement(ITEM_RESPONSE_SQL);
			stmt1.setString(1, testRosterId);
			stmt1.setString(2, tsd.getIstArray()[0].getIid());
			stmt1.setInt(3, tsd.getMseq().intValue());
			stmt1.setString(4, tsd.getIstArray()[0].getRvArray()[0].getVArray()[0].xmlText());
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

	public void putActiveRosters(Connection con, StudentCredentials[] credsA) {
		PreparedStatement stmt1 = null;
		for(int i=0;i<credsA.length;i++) {
			StudentCredentials creds = credsA[i];
	    	try {
				stmt1 = con.prepareStatement(ACTIVE_ROSTERS_SQL);
				stmt1.setString(1, creds.getUsername());
				stmt1.setString(2, creds.getPassword());
				stmt1.setString(3, creds.getAccesscode());
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

	public void putRosterData(Connection conn, StudentCredentials creds, RosterData rosterData) throws Exception {
		PreparedStatement stmt1 = null;
		try {
			stmt1 = conn.prepareStatement(ROSTER_DATA_SQL);
			stmt1.setString(1, creds.getUsername());
			stmt1.setString(2, creds.getPassword());
			stmt1.setString(3, creds.getAccesscode());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(rosterData);
			byte [] bytes = baos.toByteArray();
			String rdString = new BASE64Encoder().encode(bytes);
			stmt1.setString(4, rdString);
			stmt1.executeUpdate();
			System.out.println("*****  Stored to HSQL DB: " + creds.getUsername());
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
