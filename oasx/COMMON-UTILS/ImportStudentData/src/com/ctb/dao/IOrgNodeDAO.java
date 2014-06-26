package com.ctb.dao;

import java.sql.SQLException;

import com.ctb.bean.Node;
import com.ctb.bean.OrgNodeCategory;

public interface IOrgNodeDAO {

	Node getOrgNodeById(Integer orgNodeId) throws SQLException;
	Node createOrganization(Node orgNode) throws SQLException ;
	void insertOrgNodeForParent(Node orgNode) throws SQLException;
	OrgNodeCategory getOrgNodeCategories(Integer orgNodeId) throws SQLException;
	Node getParentOrgNode(Integer childOrgNodeId) throws SQLException;
	Integer [] getTopOrgNodeIdsForUser(String username) throws SQLException;
	Integer getCustomerIdbyOrgNode(Integer orgNodeId) throws SQLException;
	Node getTopOrgNodeForCustomer(Integer customerId) throws SQLException;
	Node [] getOrgNodesByParent(Integer parentOrgNodeId) throws SQLException;
}
