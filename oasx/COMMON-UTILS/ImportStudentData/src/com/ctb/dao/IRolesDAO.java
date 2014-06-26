package com.ctb.dao;

import java.sql.SQLException;

import com.ctb.bean.Role;



public interface IRolesDAO {

	Role getActiveRoleForUser(String userName) throws SQLException;
}
