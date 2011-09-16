package com.ctb.tms.rdb.hsql;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestData;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.rdb.OASRDBSink;

public class OASHSQLSink implements OASRDBSink {
	
	static Logger logger = Logger.getLogger(OASHSQLSink.class);
	
	public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return HSQLSetup.getOASConnection();
	}
	
	public void shutdown() {
		HSQLSetup.shutdown();
	}
	
	private static final String ITEM_RESPONSE_SQL = "insert into response (test_roster_id, item_id, seq_num, response) values (?, ?, ?, ?)";
	private static final String ACTIVE_ROSTERS_SQL = "insert into student (user_name, password, access_code) values (?, ?, ?)";
	private static final String ROSTER_DATA_SQL = "insert into roster (user_name, password, access_code, roster) values (?, ?, ?, ?)";
	private static final String SUBTEST_STATUS_SQL = "insert into manifest (test_roster_id, item_set_id, completion_status) values (?, ?, ?)";
	
	public void putItemResponse(Connection conn, String testRosterId, Tsd tsd) throws NumberFormatException, Exception {
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = conn.prepareStatement(ITEM_RESPONSE_SQL);
			stmt1.setString(1, testRosterId);
			stmt1.setString(2, tsd.getIstArray()[0].getIid());
			stmt1.setInt(3, tsd.getMseq().intValue());
			stmt1.setString(4, tsd.getIstArray()[0].getRvArray()[0].getVArray()[0]);
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
			logger.info("*****  Stored to HSQL DB: " + creds.getUsername());
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
	
	public void putManifest(Connection conn, String testRosterId, Manifest manifest) throws Exception {
		PreparedStatement stmt1 = null;
    	try {
    		ManifestData [] subtests = manifest.getManifest();
    		for(int i=0;i<subtests.length;i++) {
    			ManifestData subtest = subtests[i];
    			stmt1 = conn.prepareStatement(SUBTEST_STATUS_SQL);
    			stmt1.setString(1, testRosterId);
    			stmt1.setInt(2, subtest.getId());
    			stmt1.setString(3, subtest.getCompletionStatus());
    		}
			stmt1.executeUpdate();
			// TODO: update roster status as well
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
