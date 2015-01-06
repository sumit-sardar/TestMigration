package com.mhe.ctb.oas.BMTSync.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import com.mhe.ctb.oas.BMTSync.model.TestStatus;

import oracle.jdbc.OracleTypes;


public class TestStatusDao  extends DatabaseManager {
	static private Logger logger = Logger.getLogger(TestStatusDao.class);
	
	public TestStatus validateSaveData(int pRosterId, String pOasTestID, 
			String pDeliveryStatus, String pStartDate, String pCompletedDate) throws SQLException {
		
		//SimpleDateFormat formatter = new SimpleDateFormat("MM/DD/YYYY hh:mi:ss a");
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		TestStatus testStatus = new TestStatus();
		
		try {
		    //Date startDate = formatter.parse(pStartDate);
			//Date completionDate = formatter.parse(pCompletedDate);
			
			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PKG_BMTSYNC_TESTSTATUS.ValidateSaveData(?,?,?,?,?,?); END;");
			cstmt.setLong(1, pRosterId);
			cstmt.setString(2, pOasTestID);
			cstmt.setString(3, pDeliveryStatus);
			cstmt.setString(4, pStartDate);
			cstmt.setString(5, pCompletedDate);
			
			//cstmt.setDate(4, new java.sql.Date(startDate.getTime()));
			//cstmt.setDate(5, new java.sql.Date(completionDate.getTime()));
			cstmt.registerOutParameter(6,OracleTypes.CURSOR);
			
			cstmt.execute();
			
			rs = (ResultSet) cstmt.getObject(6);
			
		    while (rs.next()) {
			    
			    testStatus.setOasRosterId(pRosterId);
			    testStatus.setOasTestId(pOasTestID);
			    testStatus.setDeliveryStatus(pDeliveryStatus);
			    testStatus.setStartedDate(pStartDate);
			    testStatus.setCompletedDate(pCompletedDate);
			    testStatus.setErrorCode(rs.getInt("errorCode"));
			    testStatus.setErrorMessage(rs.getString("errorMsg"));
			    logger.info("Return JSON:"+testStatus.toJson());
		    }
		    
		    rs.close();
		    cstmt.close();
		    conn.close();
		    
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.info("SQL Exception:"+sqle.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception:"+e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (cstmt != null) {
				cstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
			
		}

	    return testStatus;
		
	}
	
}
