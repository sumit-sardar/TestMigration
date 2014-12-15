package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.mhe.ctb.oas.BMTSync.exception.UnknownStudentException;
import com.mhe.ctb.oas.BMTSync.model.Student;

@Repository
public class SpringStudentDAO {

	// Return map names
	private static final String OUTPUT_STUDENT = "PRESULTCURSOR";
	
	// The data source
	private DataSource _dataSource;

	// The JDBC template
	private JdbcTemplate _jdbcTemplate;

	// The hierarchy reader
	private SimpleJdbcCall _studentDetailsCall;
	
	// The hierarchy reader
	private SimpleJdbcCall _uuustudentCall;
	
	private SpringOrgNodeDAO _orgNodeDao;
	
	
	/**
	 * Returns a student based on the student ID
	 * 
	 * @param studentId
	 * @return
	 * @throws UnknownStudentException
	 */
	public Student getStudent(long studentId) throws UnknownStudentException
	{
		// call the sproc
		Map<String, Object> result = _studentDetailsCall.execute(studentId);
		
		// See if we got a response
		if ((result == null) || (!result.containsKey(OUTPUT_STUDENT)))
		{
			throw new UnknownStudentException(studentId);
		}
		
		// Get the response
		Collection<Student> returnList = (Collection<Student>) result.get(OUTPUT_STUDENT);

		// Check if the list has a student (we will ignore the multiple)
		if (returnList.size() == 0)
		{
			throw new UnknownStudentException(studentId);
		}
		
		// TODO: fix this
		Student student = returnList.iterator().next();
		
		student.setHeirarchySet(_orgNodeDao.getStudentHeirarchy(studentId));
		// TODO Get accommodations
		
		return student;
	}
	
	public void updateStudentAPIStatus(final Integer studentId, final boolean success, final String errorMessage) throws SQLException {
		final SqlParameterSource paramMap = new MapSqlParameterSource()
			.addValue("pStudentID", studentId.toString())
			.addValue("pAppName", "BMT")
			.addValue("pExportStatus", success ? "Success" : "Failed")
			.addValue("pErrorCode",  success ? "" : "999")
			.addValue("pErrorMessage", success ? "" : errorMessage);
			
		_uuustudentCall.compile();
		
		int rowsUpdated = _uuustudentCall.executeFunction(int.class, paramMap);
		if (rowsUpdated != 1) {
			throw new SQLException("One row expected to be updated! Rows updated: " + rowsUpdated);
		}
	}
	
	/**
	 * Setup the datasource, autowired if context is applied
	 * 
	 * @param ds
	 */
	@Autowired
	public void setDataSource(DataSource ds)
	{
		_dataSource = ds;
		_jdbcTemplate = new JdbcTemplate(_dataSource);
		
		_studentDetailsCall = new SimpleJdbcCall(_jdbcTemplate)
				.withCatalogName("PK_Students")
				.withProcedureName("StudentDetails")
				.useInParameterNames("pStudentId", "pResultCursor")
				.declareParameters(
						new SqlParameter("pStudentID", Types.BIGINT),
						new SqlOutParameter(OUTPUT_STUDENT,  OracleTypes.CURSOR, new StudentDetailsRowMapper())
				);

		_uuustudentCall = new SimpleJdbcCall(_jdbcTemplate)
				.withCatalogName("PK_Students")
				.withProcedureName("updateStudentAPIStatus")
				.useInParameterNames("pStudentID", "pAppName", "pExportStatus", "pErrorCode", "pErrorMessage")
				.declareParameters(
						new SqlParameter("pStudentID", Types.VARCHAR),
						new SqlParameter("pAppName", Types.VARCHAR),
						new SqlParameter("pExportStatus", Types.VARCHAR),
						new SqlParameter("pErrorCode", Types.VARCHAR),
						new SqlParameter("pErrorMessage", Types.VARCHAR)
				);
	}

	/**
	 * Maps a response from the PK_Students.HeirarchyParents stored procedure
	 * 
	 * @author cparis
	 */
	private class StudentDetailsRowMapper implements RowMapper<Student>
	{

		public Student mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			Student student = new Student();
			
			student.setOasStudentId(rs.getInt("oasStudentID"));
	    	student.setOasCustomerId(rs.getInt("oasCustomerId"));
	    	student.setStudentusername(rs.getString("StudentUserName"));
	    	student.setFirstName(rs.getString("FirstName"));
	    	student.setMiddleName(rs.getString("MiddleName"));
	    	student.setLastName(rs.getString("LastName"));
	    	student.setBirthdate(rs.getString("BirthDate"));
	    	student.setGender(rs.getString("Gender"));
	    	student.setGrade(rs.getString("Grade"));
	    	student.setCustomerStudentId(rs.getString("Ext_Pin1"));
	    	
	    	return student;
		}

	}
	
	

	public SpringOrgNodeDAO getOrgNodeDao() {
		return _orgNodeDao;
	}

	@Autowired
	public void setOrgNodeDao(SpringOrgNodeDAO orgNodeDao) {
		_orgNodeDao = orgNodeDao;
	}

}
