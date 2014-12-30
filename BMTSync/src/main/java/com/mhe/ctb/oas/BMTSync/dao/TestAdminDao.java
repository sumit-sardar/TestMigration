package com.mhe.ctb.oas.BMTSync.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;

import com.mhe.ctb.oas.BMTSync.model.DeliveryWindow;
import com.mhe.ctb.oas.BMTSync.model.TestAdmin;


public class TestAdminDao extends DatabaseManager {

	/*
	 * Method to the get the student test assignment details
	 * From the test_Admin, Test_Admin_Item_Set, 
	 * Test_Rosster and Student_Item_set_Status
	 */
	public TestAdmin getTestAdmin(long testAdminId) throws SQLException {
		
		TestAdmin testAdmin = new TestAdmin();
		DeliveryWindow deliveryWindow = new  DeliveryWindow();
		//TestAssignment.Parameters parameters = new  TestAssignment.Parameters();

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PKG_BMTSYNC_TESTADMIN.getTestAdmin(?,?); END;");
			cstmt.setLong(1, testAdminId);
			cstmt.registerOutParameter(2,OracleTypes.CURSOR);
			
			cstmt.execute();
			
			rs = (ResultSet) cstmt.getObject(2);
			
		    while (rs.next()) {
		    	if (rs.getInt("oasTestAdministrationID") > 0) {
	    		
		    			testAdmin.setOasTestAdministrationID(rs.getInt("oasTestAdministrationID"));
		    			testAdmin.setOasCustomerId(rs.getInt("oasCustomerId"));
		    			testAdmin.setOasTestCatalogId(rs.getInt("oasTestCatalogId"));
		    			testAdmin.setName(rs.getString("name"));
		    			testAdmin.setProductName(rs.getString("productName"));
			    		
			    		deliveryWindow.setStartDate(rs.getString("startDate"));
			    		deliveryWindow.setStartHour(rs.getString("startHour"));
			    		deliveryWindow.setEndDate(rs.getString("endDate"));
			    		deliveryWindow.setEndHour(rs.getString("endHour"));
			    		testAdmin.setDeliveryWindow(deliveryWindow);
			    		
		    		}
			    	
		    	}
		    
		    rs.close();
		    cstmt.close();
		    conn.close();
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
		
		return testAdmin;
	}
	
	
	
	/*
	 * Method to update the Assignment API Status table
	 */
	public boolean updateTestAdminStatus(Integer pTestAdminId, String pAppName, String pExportStatus, String pErrorCode, String pErrorMessage) throws Exception {
		
		Connection conn = null;
		CallableStatement cstmt = null;
		//ResultSet rs = null;
		
		try {
			if (pErrorMessage.length() > 200 )
				pErrorMessage = pErrorMessage.substring(1, 200);
			
			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PKG_BMTSYNC_TESTADMIN.updateTestAdminAPIStatus(?, ?, ?, ?, ?); END;");
			cstmt.setInt(1, pTestAdminId);
			cstmt.setString(2, pAppName);
			cstmt.setString(3, pExportStatus);			
			cstmt.setString(4, pErrorCode);
			cstmt.setString(5, pErrorMessage);
			
			cstmt.execute();
		    
			
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			//rs.close();
			cstmt.close();
			if (conn!= null) conn.close();				
		}
		return true;
	}
	
}
