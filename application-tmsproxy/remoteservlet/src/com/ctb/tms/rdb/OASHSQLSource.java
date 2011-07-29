package com.ctb.tms.rdb; 

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import sun.misc.BASE64Decoder;

import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public class OASHSQLSource implements OASRDBSource
{ 
	public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return HSQLSetup.getOASConnection();
	}
	
	public void shutdown() {
		HSQLSetup.shutdown();
	}
	
	private static final String GET_STUDENTS_SQL = "select * from roster";
	private static final String GET_ROSTER_SQL = "select * from roster where user_name = ? and password = ? and access_code = ?";
	
	private static HashMap rosterMap = new HashMap(100000);
	
	public StudentCredentials [] getActiveRosters(Connection conn) {
		ArrayList results = new ArrayList();
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = conn.prepareStatement(GET_STUDENTS_SQL);
			ResultSet rs = stmt1.executeQuery();
			while(rs.next()) {
				StudentCredentials creds = new StudentCredentials();
				creds.setUsername(rs.getString("user_name"));
				creds.setPassword(rs.getString("password"));
				creds.setAccesscode(rs.getString("access_code"));
				String rosterString = rs.getString("roster");
		    	byte [] bytes = new BASE64Decoder().decodeBuffer(rosterString);
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				RosterData roster = (RosterData) ois.readObject();
				rosterMap.put(creds, roster);
				results.add(creds);
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
		return (StudentCredentials[]) results.toArray(new StudentCredentials[0]);
	}
	
    public RosterData getRosterData(Connection conn, StudentCredentials creds)  throws Exception {
    	return (RosterData) rosterMap.get(creds);
    }
} 
