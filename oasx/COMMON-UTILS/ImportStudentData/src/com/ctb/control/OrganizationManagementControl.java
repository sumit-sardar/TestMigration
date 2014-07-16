package com.ctb.control;

import java.sql.SQLException;
import java.util.Date;

import com.ctb.bean.Node;
import com.ctb.dao.IOrgNodeDAO;
import com.ctb.dao.OrgNodeDAO;
import com.ctb.utils.Constants;

/**
 * Class used for Organization related DB transactions control handling
 * 
 * @author TCS
 * 
 */
public class OrganizationManagementControl {

	private IOrgNodeDAO orgNode = new OrgNodeDAO();

	/**
	 * Creates the Organization in the Database and returns the Node Object.
	 * This Node Object will have the Sequence generated Id which will used
	 * Later for student association
	 * 
	 * @param userName
	 *            - User creating the Org-Node
	 * @param node
	 *            - Bean having Org-node Data
	 * @return Node - The Created Node having Information such as Org-node-Id
	 * @throws Exception
	 */
	public Node createOrganization(String userName, Node node) throws Exception {

		try {
			node.setCreatedBy(new Integer(1));
			node.setCreatedDateTime(new Date());
			node.setActivationStatus(Constants.ACTIVATION_STATUS_ACTIVE);
			node = orgNode.createOrganization(node);
			orgNode.insertOrgNodeForParent(node);
			return node;
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
