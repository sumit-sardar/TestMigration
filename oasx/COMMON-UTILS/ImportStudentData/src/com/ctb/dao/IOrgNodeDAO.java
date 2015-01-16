package com.ctb.dao;

import com.ctb.bean.Node;

/**
 * Interface used for Organization Data Execution Controls
 * 
 * @author TCS
 * 
 */
public interface IOrgNodeDAO {
	
	/**
	 * Used for Inserting Node data in Database after creating Org-node-id from
	 * Sequence generator
	 * 
	 * @param orgNode
	 *            - Node data to be inserted.
	 * @return - Node - Created Node returned.
	 * @throws Exception
	 */
	Node createOrganization(Node orgNode) throws Exception;
	
	/**
	 * Used for insertion of Data in Org-node-Parent table
	 * 
	 * @param orgNode
	 *            - Node data representing OrgNode
	 * @throws Exception
	 */
	void insertOrgNodeForParent(Node orgNode) throws Exception;
	
	/**
	 * Used for Updating Node data in Database
	 * 
	 * @param orgNode - Node data to be updated.
	 * @return - Node - Updated Node returned.
	 * @throws Exception
	 */
	void updateOrganization(Node orgNode) throws Exception;
}
