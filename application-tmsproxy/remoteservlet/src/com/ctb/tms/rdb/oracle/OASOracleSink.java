package com.ctb.tms.rdb.oracle;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist;
import noNamespace.BaseType;

import org.apache.log4j.Logger;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestData;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.rdb.OASRDBSink;

public class OASOracleSink implements OASRDBSink {	
	private static final String STORE_RESPONSE_SQL = "insert into item_response (  item_response_id,  test_roster_id,  \t\titem_set_id,  \t\titem_id,  \t\tresponse,  \t\tresponse_method,  \t\tresponse_elapsed_time,  \t\tresponse_seq_num,  \t\text_answer_choice_id,  \tstudent_marked,  \t\tcreated_by) \tvalues  (SEQ_ITEM_RESPONSE_ID.NEXTVAL,  ?,  ?,  ?,  ?,  'M',  ?,  ?,  ?,  ?,  6)";
	private static final String DELETE_CR_RESPONSE_SQL = "delete from item_response_cr where test_roster_id = ? and item_set_id = ? and item_id = ?";
	private static final String STORE_CR_RESPONSE_SQL = "insert into item_response_cr (  test_roster_id,  item_set_id, item_id, constructed_response) values (?,  ?,  ?,  ?)";
	private static final String SUBTEST_STATUS_SQL = "update student_item_set_status set completion_status = ?, raw_score = ?, max_score = ?, unscored = ? where test_roster_id = ? and item_set_id = ?";
	private static final String ROSTER_STATUS_SQL = "update  test_roster set  test_completion_status = ?,  restart_number = ?,  start_date_time = nvl(start_date_time,?),  last_login_date_time = ?, updated_date_time = ?,  completion_date_time = ?, last_mseq = ?,  correlation_id = ?, random_distractor_seed = ? where  test_roster_id = ?";
	
	static Logger logger = Logger.getLogger(OASOracleSink.class);
	
	public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return OracleSetup.getOASConnection();
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
	                    String audioItem = ist.getAudioItem() ? "T" : "F";
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
	                    logger.debug("***** Persisted response for roster: " + testRosterId + ", item: " + ist.getIid() + ". Response was: " + response);
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
			//logger.info("$$$$$ Stored response record in DB for roster " + testRosterId + ", mseq " + mseq);
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
	
	public void putManifest(Connection conn, String testRosterId, Manifest manifest) throws Exception {
		PreparedStatement stmt1 = null;
    	try {
    		ManifestData [] subtests = manifest.getManifest();
    		for(int i=0;i<subtests.length;i++) {
    			ManifestData subtest = subtests[i];
    			stmt1 = conn.prepareStatement(SUBTEST_STATUS_SQL);
    			stmt1.setString(1, subtest.getCompletionStatus());
    			stmt1.setInt(2, subtest.getRawScore());
    			stmt1.setInt(3, subtest.getMaxScore());
    			stmt1.setInt(4, subtest.getUnscored());
    			stmt1.setString(5, testRosterId);
    			stmt1.setInt(6, subtest.getId());
    			stmt1.executeUpdate();
    			logger.debug("***** Updated subtest status for roster: " + testRosterId + ", subtest: " + subtest.getId() + ". Status is: " + subtest.getCompletionStatus());
    		}
    		stmt1.close();
    		stmt1 = conn.prepareStatement(ROSTER_STATUS_SQL);
    		stmt1.setString(1, manifest.getRosterCompletionStatus());
    		stmt1.setInt(2, manifest.getRosterRestartNumber());
    		stmt1.setDate(3, manifest.getRosterStartTime());
    		stmt1.setDate(4, manifest.getRosterStartTime());
    		stmt1.setDate(5, new Date(System.currentTimeMillis()));
    		stmt1.setDate(6, manifest.getRosterEndTime());
    		stmt1.setInt(7, manifest.getRosterLastMseq());
    		stmt1.setInt(8, manifest.getRosterCorrelationId());
    		stmt1.setInt(9, manifest.getRandomDistractorSeed());
    		stmt1.setString(10, testRosterId);
    		stmt1.executeUpdate();
			logger.debug("***** Updated roster status for roster: " + testRosterId + ". Status is: " + manifest.getRosterCompletionStatus());
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
	
	private static void storeCRResponse(Connection conn, int testRosterId, int itemSetId, String itemId, String response, float duration, BigInteger mseq, String studentMarked) throws Exception {
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = conn.prepareStatement(DELETE_CR_RESPONSE_SQL);
			stmt1.setInt(1, testRosterId);
			stmt1.setInt(2, itemSetId);
			stmt1.setString(3, itemId);
			
			stmt1.executeUpdate();
			
			stmt1 = conn.prepareStatement(STORE_CR_RESPONSE_SQL);
			stmt1.setInt(1, testRosterId);
			stmt1.setInt(2, itemSetId);
			stmt1.setString(3, itemId);
			stmt1.setString(4, response);

			stmt1.executeUpdate();
			//logger.info("$$$$$ Stored CR response record in DB for roster " + testRosterId + ", mseq " + mseq);
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
