package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
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
import com.mhe.ctb.oas.BMTSync.model.Student.Accommodations;

@Repository
public class SpringStudentDAO {

	// Return map names
	private static final String OUTPUT_STUDENT = "PRESULTCURSOR";

	// The data source
	private DataSource _dataSource;

	// The JDBC template
	private JdbcTemplate _jdbcTemplate;

	// The hierarchy reader
	private SimpleJdbcCall _getStudentDetailsCall;

	// The hierarchy reader
	private SimpleJdbcCall _updateStudentAPIStatusCall;

	private SpringOrgNodeDAO _orgNodeDao;
	
	private static final Logger logger = Logger.getLogger(SpringStudentDAO.class);

	public SpringStudentDAO(DataSource ds) {
		_dataSource = ds;
		_jdbcTemplate = new JdbcTemplate(_dataSource);

		_getStudentDetailsCall = new SimpleJdbcCall(_jdbcTemplate)
				.withCatalogName("PK_Students")
				.withProcedureName("StudentDetails")
				.useInParameterNames("pStudentId", "pResultCursor")
				.declareParameters(
						new SqlParameter("pStudentID", Types.BIGINT),
						new SqlOutParameter(OUTPUT_STUDENT, OracleTypes.CURSOR,
								new StudentDetailsRowMapper()));
		_getStudentDetailsCall.compile();

		_updateStudentAPIStatusCall = new SimpleJdbcCall(_jdbcTemplate)
				.withCatalogName("PK_Students")
				.withProcedureName("updateStudentAPIStatus")
				.useInParameterNames("pStudentID", "pAppName", "pExportStatus",	"pErrorCode", "pErrorMessage")
				.declareParameters(
						new SqlParameter("pStudentID", Types.VARCHAR),
						new SqlParameter("pAppName", Types.VARCHAR),
						new SqlParameter("pExportStatus", Types.VARCHAR),
						new SqlParameter("pErrorCode", Types.VARCHAR),
						new SqlParameter("pErrorMessage", Types.VARCHAR));
		_updateStudentAPIStatusCall.compile();
	}
	
	/**
	 * Returns a student based on the student ID
	 * 
	 * @param studentId
	 * @return
	 * @throws UnknownStudentException
	 */
	public Student getStudent(long studentId) throws UnknownStudentException {
		Map<String, Object> result = _getStudentDetailsCall.execute(studentId);

		// See if we got a response
		if ((result == null) || (!result.containsKey(OUTPUT_STUDENT))) {
			throw new UnknownStudentException(studentId);
		}

		// Get the response
		Collection<Student> returnList = (Collection<Student>) result.get(OUTPUT_STUDENT);

		// Check if the list has a student (we will ignore the multiple)
		if (returnList.size() == 0) {
			throw new UnknownStudentException(studentId);
		}

		// TODO: fix this
		Student student = returnList.iterator().next();

		student.setHeirarchySet(_orgNodeDao.getStudentHeirarchy(studentId));

		return student;
	}

	public void updateStudentAPIStatus(final Integer studentId,
			final boolean success, final String errorMessage)
			throws SQLException {

		_updateStudentAPIStatusCall.execute(studentId.toString(), "BMT", 
				success ? "Success" : "Failed",
				success ? "" : "999",
				success ? "" : errorMessage);
		
		/* int numRowsUpdated = Integer.valueOf((Integer) results.get("#update-count-1"));
		if (numRowsUpdated != 1) {
			throw new SQLException("One row expected to be updated! Rows updated: "	+ numRowsUpdated);
		} */
	}

	/**
	 * Maps a response from the PK_Students.HeirarchyParents stored procedure
	 * 
	 * @author cparis
	 */
	private class StudentDetailsRowMapper implements RowMapper<Student> {

		public Student mapRow(ResultSet rs, int rowNum) throws SQLException {

			Student student = new Student();
			Accommodations studentAccom = new Accommodations();

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

			// Set Student Accommodation
			studentAccom.setScreen_Magnifier(rs.getString("SCREEN_MAGNIFIER"));
			studentAccom.setScreen_Reader(rs.getString("Screen_Reader"));
			studentAccom.setCalculator(rs.getString("Calculator"));
			studentAccom.setTest_Pause(rs.getString("Test_Pause"));
			studentAccom.setUntimed_Test(rs.getString("Untimed_Test"));
			studentAccom.setQuestion_background_color(rs.getString("Question_Background_Color"));
			studentAccom.setQuestion_font_color(rs.getString("Question_Font_Color"));
			studentAccom.setQuestion_font_size(rs.getString("Question_Font_Size"));
			studentAccom.setAnswer_background_color(rs.getString("Answer_Background_Color"));
			studentAccom.setAnswer_font_color(rs.getString("Answer_Font_Color"));
			studentAccom.setAnswer_font_size(rs.getString("Answer_Font_Size"));
			studentAccom.setHighlighter(rs.getString("Highlighter"));
			studentAccom.setMusic_File_Id(rs.getString("Music_File_Id"));
			studentAccom.setMasking_ruler(rs.getString("Masking_Ruler"));
			studentAccom.setMagnifying_glass(rs.getString("Magnifying_Glass"));
			studentAccom.setExtended_time(rs.getString("Extended_Time"));
			studentAccom.setMasking_tool(rs.getString("Masking_Tool"));
			studentAccom.setMicrophone_headphone(rs.getString("Microphone_Headphone"));
			studentAccom.setExtended_time_factor(rs.getFloat("Extended_Time_Factor"));

			student.setAccomodation(studentAccom);

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
