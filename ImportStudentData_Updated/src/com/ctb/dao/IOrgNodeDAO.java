package com.ctb.dao;

import com.ctb.bean.Node;

public interface IOrgNodeDAO {

	Node createOrganization(Node orgNode) throws Exception ;
	void insertOrgNodeForParent(Node orgNode) throws Exception;
}
