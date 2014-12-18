package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import org.springframework.stereotype.Repository;

import com.mhe.ctb.oas.BMTSync.exception.UnknownStudentException;
import com.mhe.ctb.oas.BMTSync.model.HierarchyNode;

@Repository
public class SpringOrgNodeDAO {
	// Return map names
	private static final String OUTPUT_HEIRARCHY_LIST = "PRESULTCURSOR";

	// The data source
	private DataSource _dataSource;

	// The JDBC template
	private JdbcTemplate _jdbcTemplate;

	// The hierarchy reader
	private SimpleJdbcCall _hierarchyReader;

	/**
	 * Returns a list of the student hierarchies
	 * 
	 * @param studentId
	 * @return
	 * @throws UnknownStudentException
	 */
	public List<HierarchyNode> getStudentHeirarchy(long studentId)
			throws UnknownStudentException {
		Map<String, Object> result = _hierarchyReader.execute(studentId);

		if ((result == null) || (!result.containsKey(OUTPUT_HEIRARCHY_LIST))) {
			throw new UnknownStudentException(studentId);
		}

		List<HierarchyNode> returnList = (List<HierarchyNode>) result.get(OUTPUT_HEIRARCHY_LIST);
		return returnList;
	}

	/**
	 * Setup the datasource, autowired if context is applied
	 * 
	 * @param ds
	 */
	@Autowired
	public void setDataSource(DataSource ds) {
		_dataSource = ds;
		_jdbcTemplate = new JdbcTemplate(_dataSource);

		_hierarchyReader = new SimpleJdbcCall(_jdbcTemplate)
				.withCatalogName("PK_Students")
				.withProcedureName("Heirarchy")
				.useInParameterNames("pStudentID", "pResultCursor")
				.declareParameters(
						new SqlParameter("pStudentID", Types.BIGINT),
						new SqlOutParameter(OUTPUT_HEIRARCHY_LIST,
								OracleTypes.CURSOR,
								new HeirarchyParentsRowMapper()));

	}

	/**
	 * Maps a response from the PK_Students.HeirarchyParents stored procedure
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
