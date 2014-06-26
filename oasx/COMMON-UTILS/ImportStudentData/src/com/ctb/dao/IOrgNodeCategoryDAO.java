package com.ctb.dao;

import java.sql.SQLException;

import com.ctb.bean.OrgNodeCategory;

public interface IOrgNodeCategoryDAO {
	OrgNodeCategory[] getOrgNodeCategories(Integer customerId) throws SQLException;

}
