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

import com.mhe.ctb.oas.BMTSync.exception.UnknownTestStatusException;
import com.mhe.ctb.oas.BMTSync.model.TestStatus;

@Repository
public class TestStatusDAO {
	private static final Logger logger = Logger.getLogger(TestStatusDAO.class);
	
	// Return map names
	private static final String OUTPUT_TESTSTATUS = "PRESULTCURSOR";
	
	// The data source
	private final DataSource _dataSource;

	// The JDBC template
	private final JdbcTemplate _jdbcTemplate;

	// The test assignment
	private final SimpleJdbcCall _valiDateSaveTestStatusCall;	
	
	
	public TestStatusDAO(final DataSource ds) {

		_dataSource = ds;
		_jdbcTemplate = new JdbcTemplate(_dataSource);

		_valiDateSaveTestStatusCall = new SimpleJdbcCall(_jdbcTemplate)
				.withCatalogName("PKG_BMTSYNC_TESTSTATUS")
				.withProcedureName("ValidateSaveTestStatus")
				.useInParameterNames("pRosterId","pOasTestID","pDeliveryStatus",
						"pStartedDate", "pCompletedDate", "pResultCursor")
				.declareParameters(
						new SqlParameter("pRosterId", Types.INTEGER),
						new SqlParameter("pOasTestID", Types.VARCHAR),
						new SqlParameter("pDeliveryStatus", Types.VARCHAR),
						new SqlParameter("pStartedDate", Types.VARCHAR),
						new SqlParameter("pCompletedDate", Types.VARCHAR),
						new SqlOutParameter(OUTPUT_TESTSTATUS, OracleTypes.CURSOR,
								new TestStatusRowMapper())
						);
		_valiDateSaveTestStatusCall.compile();
	}
	
	
	/**
	 * Maps a response 
	 * 
	 * @author sbasa
	 */
	private class TestStatusRowMapper implements RowMapper<TestStatus> {
        
		public TestStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
			final TestStatus testStatus = new TestStatus();

		    testStatus.setOasRosterId(rs.getInt("RosterId"));
		    testStatus.setOasTestId(rs.getString("OasTestId"));
		    testStatus.setDeliveryStatus(rs.getString("DeliveryStatus"));
		    testStatus.setStartedDate(rs.getString("Started_Date"));
		    testStatus.setCompletedDate(rs.getString("Completed_Date"));
		    testStatus.setErrorCode(rs.getInt("errorCode"));
		    testStatus.setErrorMessage(rs.getString("errorMsg"));

			return testStatus;		
		}

	}
	

	// returns roaster test Status that failed to update 
	public TestStatus validateSaveData(final Integer rosterId,
			final String pOasTestID, 
			final String pDeliveryStatus, 
			final String pStartedDate, 
			final String pCompletedDate) throws UnknownTestStatusException {

		// call the sproc
		Map<String, Object> result = _valiDateSaveTestStatusCall.execute(rosterId, pOasTestID, 
				pDeliveryStatus, pStartedDate, pCompletedDate);

		// See if we got a response
		if (result == null || !result.containsKey(OUTPUT_TESTSTATUS)) {
			throw new UnknownTestStatusException(rosterId);
		}

		// Get the response
		@SuppressWarnings("unchecked")
		Collection<TestStatus> returnList = (Collection<TestStatus>) result.get(OUTPUT_TESTSTATUS);
		
		// Check if the list has a student (we will ignore the multiple)
		if (returnList.size() == 0) {
			logger.info("Error on roaster id :"+rosterId);
			throw new UnknownTestStatusException(rosterId);
		}

		return returnList.iterator().next();
	}
	
	
}
