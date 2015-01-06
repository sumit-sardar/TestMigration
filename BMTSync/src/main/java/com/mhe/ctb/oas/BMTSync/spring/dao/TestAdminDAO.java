package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.mhe.ctb.oas.BMTSync.exception.UnknownTestAdminException;
import com.mhe.ctb.oas.BMTSync.model.DeliveryWindow;
import com.mhe.ctb.oas.BMTSync.model.TestAdmin;


@Repository
public class TestAdminDAO {
	private static final Logger logger = Logger.getLogger(TestAdminDAO.class);
	
	// Return map names
	private static final String OUTPUT_ASSIGNMENT = "PRESULTCURSOR";
	
	// The data source
	private final DataSource _dataSource;

	// The JDBC template
	private final JdbcTemplate _jdbcTemplate;

	// The test assignment
	private final SimpleJdbcCall _getTestAdminCall;	
	
	// Update status in BMTSYNC_ASSIGNMENT_STATUS
	private SimpleJdbcCall _updateAdminStatusCall;

	// Constructor
	public TestAdminDAO(final DataSource ds) {
		_dataSource = ds;
		_jdbcTemplate = new JdbcTemplate(_dataSource);

		_getTestAdminCall = new SimpleJdbcCall(_jdbcTemplate)
				.withCatalogName("PKG_BMTSYNC_TESTADMIN")
				.withProcedureName("getTestAdmin")
				.useInParameterNames("pTestAdminId", "pResultCursor")
				.declareParameters(
						new SqlParameter("pTestAdminId", Types.BIGINT),
						new SqlOutParameter(OUTPUT_ASSIGNMENT, OracleTypes.CURSOR,
								new TestAdminRowMapper()));
		_getTestAdminCall.compile();
		
		_updateAdminStatusCall = new SimpleJdbcCall(_jdbcTemplate)
		.withCatalogName("PKG_BMTSYNC_TESTADMIN")
		.withProcedureName("updateTestAdminAPIStatus")
		.useInParameterNames("pTestAdminID", "pAppName", "pExportStatus", "pErrorCode", "pErrorMessage")
		.declareParameters(
				new SqlParameter("pTestAdminID", Types.INTEGER),
				new SqlParameter("pAppName", Types.VARCHAR),
				new SqlParameter("pExportStatus", Types.VARCHAR),
				new SqlParameter("pErrorCode", Types.VARCHAR),
				new SqlParameter("pErrorMessage", Types.VARCHAR));
		_updateAdminStatusCall.compile();		
	}
	
	// returns student test assignments
	public TestAdmin getTestAdmin(final long testAdminId) throws UnknownTestAdminException {

		// call the sproc
		Map<String, Object> result = _getTestAdminCall.execute(testAdminId);

		// See if we got a response
		if (result == null || !result.containsKey(OUTPUT_ASSIGNMENT)) {
			throw new UnknownTestAdminException(testAdminId);
		}

		// Get the response
		@SuppressWarnings("unchecked")
		Collection<TestAdmin> returnList = (Collection<TestAdmin>) result.get(OUTPUT_ASSIGNMENT);
		
		// Check if the list has a student (we will ignore the multiple)
		if (returnList.size() == 0) {
			throw new UnknownTestAdminException(testAdminId);
		}

		return returnList.iterator().next();
	}

	/**
	 * Maps a response from the PK_testAdmin.HeirarchyParents stored procedure
	 * 
	 * @author ktracer
	 */
	private class TestAdminRowMapper implements RowMapper<TestAdmin> {
        
		public TestAdmin mapRow(ResultSet rs, int rowNum) throws SQLException {
			final TestAdmin testAdmin = new TestAdmin();
			final DeliveryWindow window = new DeliveryWindow();

			testAdmin.setOasTestAdministrationID(rs.getInt("oasTestAdministrationID"));
    		testAdmin.setOasCustomerId(rs.getInt("oasCustomerId"));
    		testAdmin.setOasTestCatalogId(rs.getInt("oasTestCatalogId"));
    		testAdmin.setName(rs.getString("name"));
    		testAdmin.setProductName(rs.getString("productName"));
	    		
    		window.setStartDate(rs.getString("startDate"));
    		window.setStartHour(rs.getString("startHour"));
    		window.setEndDate(rs.getString("endDate"));
    		window.setEndHour(rs.getString("endHour"));
    		testAdmin.setDeliveryWindow(window);
	    	
			return testAdmin;		
		}

	}
	
	public void updateTestAdminStatus(final Integer testAdminId, final boolean success,
			final String errorCode, final String errorMessage) throws SQLException {
			
		logger.debug(String.format("DB CALL: [testAdminID=%d][updateSuccess=%b][updateStatus=%s][updateMessage=%s]",
				testAdminId,
				"BMT",
				Boolean.valueOf(success),
				errorCode,
				errorMessage));	
		_updateAdminStatusCall.execute(
				testAdminId,
				"BMT",
				success ? "Success" : "Failed",
				success ? "" : errorCode,
				success ? "" : errorMessage);
	}
	
}
