package com.ctb.dao;

import java.sql.SQLException;

import com.ctb.bean.TestSession;



public interface ITestAdminDAO {

	TestSession [] getTestAdminsForOrgNode(Integer orgNodeId) throws SQLException;
}
