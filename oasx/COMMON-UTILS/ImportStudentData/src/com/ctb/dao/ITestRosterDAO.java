package com.ctb.dao;

import java.sql.SQLException;

public interface ITestRosterDAO {
	
	Integer getRosterCountForStudentAndOrgNode(Integer studentId, Integer orgNodeId) throws SQLException;

}
