package com.ctb.dao;

import java.sql.SQLException;

import com.ctb.bean.CustomerEmail;
import com.ctb.bean.User;

public interface IUsersDAO {
	
	User getUserDetails(String userName) throws SQLException;
	
	CustomerEmail getCustomerEmailByUserName(String userName, Integer emailType) throws SQLException;
	User [] getUsersForOrgNode(Integer orgNodeId) throws SQLException;

}
