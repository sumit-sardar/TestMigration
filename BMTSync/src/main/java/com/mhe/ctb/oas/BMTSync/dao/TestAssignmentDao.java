package com.mhe.ctb.oas.BMTSync.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import oracle.jdbc.OracleTypes;

import com.mhe.ctb.oas.BMTSync.model.StudentRoster;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;
import com.mhe.ctb.oas.BMTSync.model.TestDelivery;

public class TestAssignmentDao extends DatabaseManager {
	
	/*
	 * Method to the get the student test assignment details
	 * From the test_Admin, Test_Admin_Item_Set, 
	 * Test_Rosster and Student_Item_set_Status
	 */
	public TestAssignment getStudentAssignment(long testAdminId, long studentId) {
		
		TestAssignment testAssignment = new TestAssignment();
		TestAssignment.DeliveryWindow deliveryWindow = new  TestAssignment.DeliveryWindow();
		TestAssignment.Parameters parameters = new  TestAssignment.Parameters();
		TestDelivery.EnforceTimeLimit enforceTimeLimit = new TestDelivery.EnforceTimeLimit();

	    List<TestDelivery> testDeliveryList = new ArrayList<TestDelivery>();
	    StudentRoster studentRoster = new StudentRoster();
        List<StudentRoster> studentRoasterList = new ArrayList<StudentRoster>();
	    
	    int recordCtr = 0;
		
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PKG_BMTSYNC_ASSIGNMENT.getTestAssignment(?,?,?); END;");
			cstmt.setLong(1, testAdminId);
			cstmt.setLong(2, studentId);
			cstmt.registerOutParameter(3,OracleTypes.CURSOR);
			
			cstmt.execute();
			
			rs = (ResultSet) cstmt.getObject(3);
			
		    while (rs.next()) {
		    	if (rs.getInt("oasTestAdministrationID") > 0) {
		    		TestDelivery testDelivery  = new TestDelivery();
		    		
		    		recordCtr++;
		    		
		    		if (recordCtr == 1) {
			    		testAssignment.setOasTestAdministrationID(rs.getInt("oasTestAdministrationID"));
			    		testAssignment.setOasCustomerId(rs.getInt("oasCustomerId"));
			    		testAssignment.setOasTestCatalogId(rs.getInt("oasTestCatalogId"));
			    		testAssignment.setName(rs.getString("name"));
			    		testAssignment.setProductName(rs.getString("productName"));
			    		
			    		deliveryWindow.setStartDate(rs.getString("startDate"));
			    		deliveryWindow.setStartHour(rs.getString("startHour"));
			    		deliveryWindow.setEndDate(rs.getString("endDate"));
			    		deliveryWindow.setEndHour(rs.getString("endHour"));
			    		testAssignment.setDeliveryWindow(deliveryWindow);
			    		
			    		parameters.setEnforceBreak(rs.getString("enforceBreak"));
			    		parameters.setEnforceTutorial(rs.getString("enForceTutorial"));
			    		testAssignment.setParameters(parameters);
			    		
			    		
			    		studentRoster.setOasRosterId(rs.getString("oasRosterId"));
			    		studentRoster.setOasStudentid(rs.getString("oasStudentid"));
			    		studentRoster.setStudentpassword(rs.getString("password"));
		    		}
			    	testDelivery.setOasItemSetId(rs.getString("ITEM_SET_ID"));
			    	testDelivery.setDeliverystatus(rs.getString("Delivery_Status"));
			    	testDelivery.setAccessCode(rs.getString("Access_Code"));
			    	testDelivery.setOasTestId(rs.getString("OasTestId"));
			    	testDelivery.setOasSubTestName(rs.getString("oasSubTestName"));
			    	enforceTimeLimit.setIsRequired(rs.getString("Enforce_Time_Limit"));
			    	enforceTimeLimit.setTimeLimitInMins(rs.getString("TimeLimitInMins"));
			    	testDelivery.setEnforceTimeLimit(enforceTimeLimit);

			    	testDelivery.setOrder(rs.getInt("Item_Order"));
			    	
			    	System.out.println(rs.getString("OasTestId"));
			    	testDeliveryList.add(testDelivery);
			    	
		    	}
		    }			
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		studentRoster.setTestDelivery(testDeliveryList);
		studentRoasterList.add(studentRoster);
		
        testAssignment.setRoster(studentRoasterList);
		return testAssignment;
	}

	
	/*
	 * Method to update the Assignment API Status table
	 */
	public boolean updateAssignmentAPIstatus(Integer pTestAdminId, String pStudentID, String pAppName, String pExportStatus, String pErrorCode, String pErrorMessage) throws Exception {
		
		Connection conn = null;
		CallableStatement cstmt = null;
		//ResultSet rs = null;
		
		try {
			if (pErrorMessage.length() > 200 )
				pErrorMessage = pErrorMessage.substring(1, 200);
			
			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PKG_BMTSYNC_ASSIGNMENT.updateAssignmentAPIStatus(?, ?, ?, ?, ?, ?); END;");
			cstmt.setInt(1, pTestAdminId);
			cstmt.setString(2, pStudentID);
			cstmt.setString(3, pAppName);
			cstmt.setString(4, pExportStatus);			
			cstmt.setString(5, pErrorCode);
			cstmt.setString(6, pErrorMessage);
			
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
