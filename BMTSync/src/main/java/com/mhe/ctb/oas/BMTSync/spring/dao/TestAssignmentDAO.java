package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;





import com.mhe.ctb.oas.BMTSync.exception.UnknownTestAssignmentException;
import com.mhe.ctb.oas.BMTSync.model.StudentRoster;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;
import com.mhe.ctb.oas.BMTSync.model.TestDelivery;


@Repository
public class TestAssignmentDAO {
	// Return map names
	private static final String OUTPUT_ASSIGNMENT = "PRESULTCURSOR";
	
	// The data source
	@Autowired
	private DataSource _dataSource;

	// The JDBC template
	private JdbcTemplate _jdbcTemplate;

	// The test assignment
	private SimpleJdbcCall _getTestAssignmentCall;	

	
	/**
	 * Setup the datasource, autowired if context is applied
	 * 
	 * @param ds
	 */
	
	public TestAssignmentDAO(DataSource ds) {
		_dataSource = ds;
		_jdbcTemplate = new JdbcTemplate(_dataSource);
		System.out.println(_jdbcTemplate);

		_getTestAssignmentCall = new SimpleJdbcCall(_jdbcTemplate)
				.withCatalogName("PKG_BMTSYNC_ASSIGNMENT")
				.withProcedureName("getTestAssignment")
				.useInParameterNames("pTestAdminId", "pStudentId", "pResultCursor")
				.declareParameters(
						new SqlParameter("pTestAdminId", Types.BIGINT),
						new SqlParameter("pStudentID", Types.BIGINT),
						new SqlOutParameter(OUTPUT_ASSIGNMENT, OracleTypes.CURSOR,
								new TestAssignmentRowMapper()));
		
	}

	
	/*
	 * returns studest test assignments
	 * 
	 * 
	 */
	
	public TestAssignment getTestAssignment(long testAdminId, long studentId) throws UnknownTestAssignmentException {

		TestAssignment testAssignment = new TestAssignment();
		
		// call the sproc
		Map<String, Object> result = _getTestAssignmentCall.execute(testAdminId, studentId);

		// See if we got a response
		if ((result == null) || (!result.containsKey(OUTPUT_ASSIGNMENT))) {
			throw new UnknownTestAssignmentException(testAdminId, studentId);
		}

		// Get the response
		Collection<TestAssignment> returnList = (Collection<TestAssignment>) result.get(OUTPUT_ASSIGNMENT);
		
		// Check if the list has a student (we will ignore the multiple)
		if (returnList.size() == 0) {
			throw new UnknownTestAssignmentException(testAdminId, studentId);
		}

		testAssignment = returnList.iterator().next();

		
		return testAssignment;
	}

	
	
	
	/**
	 * Maps a response from the PK_Students.HeirarchyParents stored procedure
	 * 
	 * @author cparis
	 */
	private class TestAssignmentRowMapper implements RowMapper<TestAssignment> {
		TestAssignment testAssignment = new TestAssignment();
		TestAssignment.DeliveryWindow deliveryWindow = new  TestAssignment.DeliveryWindow();
		TestAssignment.Parameters parameters = new  TestAssignment.Parameters();

	    List<TestDelivery> testDeliveryList = new ArrayList<TestDelivery>();
	    StudentRoster studentRoster = new StudentRoster();
        List<StudentRoster> studentRoasterList = new ArrayList<StudentRoster>();

		int recordCtr;
		
		public TestAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {

    		TestDelivery testDelivery  = new TestDelivery();
    		
    		if (rowNum == 1) {
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
	    		
	    		
	    		studentRoster.setOasRosterId(rs.getInt("oasRosterId"));
	    		studentRoster.setOasStudentid(rs.getInt("oasStudentid"));
	    		studentRoster.setStudentpassword(rs.getString("password"));
    		}
	    	testDelivery.setOasItemSetId(rs.getInt("ITEM_SET_ID"));
	    	testDelivery.setDeliverystatus(rs.getString("Delivery_Status"));
	    	testDelivery.setAccessCode(rs.getString("Access_Code"));
	    	testDelivery.setOasTestId(rs.getString("OasTestId"));
	    	testDelivery.setOasSubTestName(rs.getString("oasSubTestName"));
	    	testDelivery.setIsRequired(rs.getString("Enforce_Time_Limit"));
	    	testDelivery.setTimeLimitInMins(rs.getInt("TimeLimitInMins"));
	    	testDelivery.setOrder(rs.getInt("Item_Order"));
	    	
	    	System.out.println(rs.getString("OasTestId"));
	    	testDeliveryList.add(testDelivery);

			studentRoster.setTestDelivery(testDeliveryList);
			studentRoasterList.add(studentRoster);
			
	        testAssignment.setRoster(studentRoasterList);
			return testAssignment;		
		}
		

	}
	
	
}
