package com.mhe.ctb.oas.BMTSync.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;

import com.mhe.ctb.oas.BMTSync.model.TestAdmin;


public class TestAdminDao extends DatabaseManager {

	/*
	 * Method to the get the student test assignment details
	 * From the test_Admin, Test_Admin_Item_Set, 
	 * Test_Rosster and Student_Item_set_Status
	 */
	public TestAdmin getTestAdmin(long testAdminId) throws SQLException {
		
		TestAdmin testAdmin = new TestAdmin();
		TestAdmin.DeliveryWindow deliveryWindow = new  TestAdmin.DeliveryWindow();
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
}
