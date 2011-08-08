package com.ctb.tms.rdb;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ctb.tms.bean.login.AuthenticationData;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist;
import noNamespace.BaseType;

public class OASOracleSink implements OASRDBSink {
	private static volatile boolean haveDataSource = true;
	private static String OASDatabaseURL = "jdbc:oracle:thin:@nj09mhe0393-vip.mhe.mhc:1521:oasr5t1";
	private static String OASDatabaseUser = "oas";
	private static String OASDatabaseUserPassword = "oaspr5r";
	private static String OASDatabaseJDBCDriver = "oracle.jdbc.driver.OracleDriver";
	
	private static final String STORE_RESPONSE_SQL = "insert into item_response (  item_response_id,  test_roster_id,  \t\titem_set_id,  \t\titem_id,  \t\tresponse,  \t\tresponse_method,  \t\tresponse_elapsed_time,  \t\tresponse_seq_num,  \t\text_answer_choice_id,  \tstudent_marked,  \t\tcreated_by) \tvalues  (SEQ_ITEM_RESPONSE_ID.NEXTVAL,  ?,  ?,  ?,  ?,  'M',  ?,  ?,  ?,  ?,  6)";
	
	{
		try {
			ResourceBundle rb = ResourceBundle.getBundle("env");
			OASDatabaseJDBCDriver = rb.getString("oas.db.driver");
			OASDatabaseURL = rb.getString("oas.db.url");
			OASDatabaseUser = rb.getString("oas.db.user");
			OASDatabaseUserPassword = rb.getString("oas.db.password");
			haveDataSource = true;
		} catch (Exception e) {
			System.out.println("***** No OAS DB connection info specified in env.properties, using static defaults");
			//e.printStackTrace();
		}
	}
	
	public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection newConn = null;
		try {    
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource)envContext.lookup("jdbc/OASDataSource");
			newConn = ds.getConnection(); 
			haveDataSource = true;
			//System.out.println("*****  Using OASDataSource for DB connection");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			haveDataSource = false;
		}

		if(!haveDataSource) {
			// no OASDataSource available, falling back on local properties
			Properties props = new Properties();
			props.put("user", OASDatabaseUser);
			props.put("password", OASDatabaseUserPassword);
			Driver driver = (Driver) Class.forName(OASDatabaseJDBCDriver).newInstance();
			newConn = driver.connect(OASDatabaseURL, props);
			//System.out.println("*****  Using local properties for OAS DB connection");
		}

		return newConn;
	}
	
	public void putItemResponse(Connection conn, String testRosterId, Tsd tsd) throws NumberFormatException, Exception {
		Ist[] ista = tsd.getIstArray();
		for(int j=0;j<ista.length;j++) {
	        Ist ist = ista[j];
	     //   if(ist != null && ist.getRvArray(0) != null && ist.getRvArray(0).getVArray(0) != null) {
	        if(ist != null && ist.getRvArray() != null && ist.getRvArray().length >0 ) {
	            if( ist.getRvArray(0).getVArray() != null && ist.getRvArray(0).getVArray().length >0){
	                if(ist.getRvArray(0).getVArray(0) != null){
	                    BaseType.Enum responseType = ist.getRvArray(0).getT();
	                    String xmlResponse = ist.getRvArray(0).getVArray(0).xmlText();
	                    String response = "";
	                    String studentMarked = ist.getMrk() ? "T" : "F";
	                    if(xmlResponse != null && xmlResponse.length() > 0) {
	                        // strip xml
	                        int start = xmlResponse.indexOf(">");
	                        if(start >= 0) {
	                            response = xmlResponse.substring(start + 1);
	                            int end = response.lastIndexOf("</");
	                            if(end != -1)
	                                response = response.substring(0, end);
	                        } else {
	                            response = xmlResponse;
	                        }
	                        // strip CDATA
	                        start = response.indexOf("[CDATA[");
	                        if(start >= 0) {
	                            response = response.substring(start + 7);
	                            int end = response.lastIndexOf("]]");
	                            if(end != -1)
	                                response = response.substring(0, end);
	                        }
	                    }
	                    if(responseType.equals(BaseType.IDENTIFIER)) {
	                        storeResponse(conn, Integer.parseInt(testRosterId), Integer.parseInt(tsd.getScid()), ist.getIid(), response, ist.getDur(), tsd.getMseq(), studentMarked);
	                    } else if(responseType.equals(BaseType.STRING)) {
	                        storeCRResponse(conn, Integer.parseInt(testRosterId), Integer.parseInt(tsd.getScid()), ist.getIid(), response, ist.getDur(), tsd.getMseq(), studentMarked);
	                    }
	                 }
	            }else{ 
	                String response = "";                   
	                String studentMarked = ist.getMrk() ? "T" : "F";                    
	                storeResponse(conn, Integer.parseInt(testRosterId), Integer.parseInt(tsd.getScid()), ist.getIid(), response, ist.getDur(), tsd.getMseq(), studentMarked);                                          
	            }       
	        }
		}
	}

	private static void storeResponse(Connection con, int testRosterId, int itemSetId, String itemId, String response, float duration, BigInteger mseq, String studentMarked) throws Exception {
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(STORE_RESPONSE_SQL);
			stmt1.setInt(1, testRosterId);
			stmt1.setInt(2, itemSetId);
			stmt1.setString(3, itemId);
			stmt1.setString(4, response);
			stmt1.setFloat(5, duration);
			stmt1.setInt(6, mseq.intValue());
			stmt1.setString(7, null);
			stmt1.setString(8, studentMarked);

			stmt1.executeUpdate();
			//System.out.println("$$$$$ Stored response record in DB for roster " + testRosterId + ", mseq " + mseq);
		} catch (Exception e) {
			if(e.getMessage().indexOf("unique constraint") >= 0 ) {
				// do nothing, dupe response
			} else {
				e.printStackTrace();
				throw(e);
			}
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}
	
	private static void storeCRResponse(Connection conn, int testRosterId, int subtestId, String itemId, String response, float duration, BigInteger mseq, String studentMarked) {
		// TODO: implement CR response persistence
	}

	public void putActiveRosters(Connection con, StudentCredentials[] credsA) {
		// do nothing
		
	}

	public void putRosterData(Connection conn, StudentCredentials creds,
			RosterData rosterData) throws Exception {
		// do nothing
		
	}

	public void shutdown() {
		// do nothing
	}
}
