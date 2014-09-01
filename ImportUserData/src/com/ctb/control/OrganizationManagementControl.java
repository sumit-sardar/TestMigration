package com.ctb.control;

import java.util.Date;

import org.apache.log4j.Logger;

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
	private static Logger logger = Logger.getLogger(OrganizationManagementControl.class
			.getName());
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
		}  catch (Exception e) {
			logger.error("Exception in createOrganization" + e.getMessage());
			throw e;
		}
	}

}
