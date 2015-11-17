package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
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

/**
 * The Test status DAO for updating test statuses as sent from BMT.
 * @author oas
 */
@Repository
public class TestStatusDAO {
	private static final Logger logger = Logger.getLogger(TestStatusDAO.class);
	
	// Return map names
	private static final String OUTPUT_TESTSTATUS = "PRESULTCURSOR";
	
	// The data source
	private final DataSource _dataSource;

	// The JDBC template
	private final JdbcTemplate _jdbcTemplate;

	/**
	 * Constructor.
	 * @param ds Data source from the configuration bean.
	 */
	public TestStatusDAO(final DataSource ds) {
		_dataSource = ds;
		_jdbcTemplate = new JdbcTemplate(_dataSource);
	}
	
	
	/**
	 * Maps a response to a database column.
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
	
	/**
	 * Validate the data coming back from BMT.
	 * @param rosterId Roster ID
	 * @param pOasTestID Test ID
	 * @param pDeliveryStatus Test status
	 * @param pStartedDate Start date for the test
	 * @param pCompletedDate End date for the test
	 * @return Success of the update
	 * @throws UnknownTestStatusException If something goes wrong with the database.
	 */
	public TestStatus validateSaveData(final Integer rosterId,
			final String pOasTestID, 
			final String pDeliveryStatus, 
			final String pStartedDate, 
			final String pCompletedDate) throws UnknownTestStatusException {
		
		final SimpleJdbcCall _valiDateSaveTestStatusCall = new SimpleJdbcCall(_jdbcTemplate)
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
		

		// call the sproc
        final Calendar startDBTime = Calendar.getInstance();
        Map<String, Object> result = _valiDateSaveTestStatusCall.execute(rosterId, pOasTestID, 
				pDeliveryStatus, pStartedDate, pCompletedDate);
		final Calendar endDBTime = Calendar.getInstance();
		final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
        logger.info("SyncCallTime " + callDBTime + " SyncCallType StoredProcedure SyncCallDest PKG_BMTSYNC_TESTSTATUS.ValidateSaveTestStatus");
        
        //BMTOAS-2042 - logging for CloudWatch
        logger.info("{\"Name\":\"CloudWatchLog\""
        		+",\"Application\":\"BMTSyncClient\""
        		+",\"IsError\":false,\"ErrorCode\":0"
        		+",\"CallType\":\"StoredProcedure\""
        		+",\"CallDest\":\"PKG_BMTSYNC_TESTSTATUS.ValidateSaveTestStatus\""
        		+",\"APICallDuration\":"+callDBTime+"}");

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
