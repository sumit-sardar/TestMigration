package com.ctb.dao;

import java.sql.SQLException;

import com.ctb.bean.OrgNodeStudent;

public interface IOrgNodeStudentDAO {

	OrgNodeStudent [] getOrgNodeStudentForStudentAtAndBelowOrgNodes(Integer studentId, String searchCriteria) throws SQLException;
	void activateOrgNodeStudentForStudentAndOrgNode(Integer studentId, Integer orgNodeId) throws SQLException;
	void deactivateOrgNodeStudentForStudentAndOrgNode(Integer studentId, Integer orgNodeId) throws SQLException;
	void deleteOrgNodeStudentForStudentAndOrgNode(Integer studentId, Integer orgNodeId) throws SQLException;
	void createOrgNodeStudent(OrgNodeStudent ons) throws SQLException;
}
