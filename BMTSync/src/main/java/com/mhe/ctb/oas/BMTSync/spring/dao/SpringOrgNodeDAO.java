package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.List;
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

import com.mhe.ctb.oas.BMTSync.exception.UnknownStudentException;
import com.mhe.ctb.oas.BMTSync.model.HierarchyNode;

/**
 * A DAO to fetch organization information for a student.
 * @author oas
 */
@Repository
public class SpringOrgNodeDAO {
	private static final Logger logger = Logger.getLogger(SpringOrgNodeDAO.class);
	
	// Return map names
	private static final String OUTPUT_HEIRARCHY_LIST = "PRESULTCURSOR";

	// The data source
	private final DataSource _dataSource;

	// The JDBC template
	private final JdbcTemplate _jdbcTemplate;

	// The hierarchy reader
	private final SimpleJdbcCall _hierarchyReader;
	
	/**
	 * Constructor
	 * @param ds Data source.
	 */
	public SpringOrgNodeDAO(final DataSource ds) {
		_dataSource = ds;
		_jdbcTemplate = new JdbcTemplate(_dataSource);

		_hierarchyReader = new SimpleJdbcCall(_jdbcTemplate)
				.withCatalogName("PKG_BMTSYNC_STUDENTS")
				.withProcedureName("Heirarchy")
				.useInParameterNames("pStudentID", "pResultCursor")
				.declareParameters(
						new SqlParameter("pStudentID", Types.BIGINT),
						new SqlOutParameter(OUTPUT_HEIRARCHY_LIST,
								OracleTypes.CURSOR,
								new HeirarchyParentsRowMapper()));
		_hierarchyReader.compile();
	}

	/**
	 * Returns a list of the student hierarchies
	 * @param studentId Student ID
	 * @return The hierarchy information for the student.
	 * @throws UnknownStudentException If the student is unknown.
	 */
	public List<HierarchyNode> getStudentHeirarchy(long studentId)
			throws UnknownStudentException {
		final Calendar startDBTime = Calendar.getInstance();
		Map<String, Object> result = _hierarchyReader.execute(studentId);
		final Calendar endDBTime = Calendar.getInstance();
		final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
        logger.info("SyncCallTime " + callDBTime + " SyncCallType StoredProcedure SyncCallDest PKG_BMTSYNC_STUDENTS.Hierarchy");
        
        //BMTOAS-2042 - logging for CloudWatch
        logger.info("{\"Name\":\"CloudWatchLog\""
        		+",\"Application\":\"BMTSyncClient\""
        		+",\"IsError\":false,\"ErrorCode\":0"
        		+",\"CallType\":\"StoredProcedure\""
        		+",\"CallDest\":\"PKG_BMTSYNC_STUDENTS.Hierarchy\""
        		+",\"APICallDuration\":"+callDBTime+"}");

		if (result == null || !result.containsKey(OUTPUT_HEIRARCHY_LIST)) {
			throw new UnknownStudentException(studentId);
		}

		@SuppressWarnings("unchecked")
		List<HierarchyNode> returnList = (List<HierarchyNode>) result.get(OUTPUT_HEIRARCHY_LIST);
		return returnList;
	}

	/**
	 * Maps a response from the PKG_BMTSYNC_STUDENTS.HeirarchyParents stored procedure
	 * 
	 * @author cparis
	 */
	private class HeirarchyParentsRowMapper implements RowMapper<HierarchyNode> {

		public HierarchyNode mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			HierarchyNode heirarchy = new HierarchyNode();

			// TODO Fix this. We likely also will want to sort the list based on
			// this laster
			// hierarchy,setHeirarchyCategoryLevel(rs.getString("CATEGORY_LEVEL"));
			heirarchy.setHeirarchyCategoryName(rs.getString("CATEGORY_NAME"));
			heirarchy.setOasHeirarchyId(rs.getInt("OAS_Heirarchy_ID"));
			heirarchy.setCode(rs.getString("ORG_NODE_CODE"));
			heirarchy.setName(rs.getString("ORG_NODE_NAME"));

			return heirarchy;
		}

	}

}
