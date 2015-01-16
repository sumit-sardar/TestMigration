package com.ctb.control;

import java.util.Date;

import org.apache.log4j.Logger;

import com.ctb.bean.Node;
import com.ctb.dao.IOrgNodeDAO;
import com.ctb.dao.OrgNodeDAO;
import com.ctb.utils.Constants;
import com.ctb.utils.cache.OrgMDRDBCacheImpl;

/**
 * Class used for Organization related DB transactions control handling
 * 
 * @author TCS
 * 
 */
public class OrganizationManagementControl {

	private IOrgNodeDAO orgNode = new OrgNodeDAO();
	private static Logger logger = Logger
	.getLogger(OrganizationManagementControl.class.getName());

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
	public Node createOrganization(String userName, Node node,
			OrgMDRDBCacheImpl orgMDRImpl) throws Exception {

		try {
			node.setCreatedBy(new Integer(1));
			node.setCreatedDateTime(new Date());
			node.setActivationStatus(Constants.ACTIVATION_STATUS_ACTIVE);
			String mdrNumber = node.getMdrNumber();
			if (orgMDRImpl.getOrgMDRNumber(mdrNumber) != null) {
				/**
				 * If the organization that is to be created is having same
				 * MDRNumebr as of another org then skip creation process and
				 * return the node with Id -99.This will be used to track this
				 * skipped process.
				 */
				node.setOrgNodeId(-99);
				return node;
			}

			node = orgNode.createOrganization(node);
			orgNode.insertOrgNodeForParent(node);
			orgMDRImpl.addOrgFileRow(mdrNumber, mdrNumber);
			return node;
		} catch (Exception e) {
			logger.error("Exception in createOrganization" + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Updates the Organization in the Database and returns the Node Object.
	 * 
	 * @param node - Bean having Org-node Data
	 * @throws Exception
	 */
	public void updateOrganization(Node node,
			OrgMDRDBCacheImpl orgMDRImpl) throws Exception {

		try {
			node.setUpdatedDateTime(new Date());
			node.setActivationStatus(Constants.ACTIVATION_STATUS_ACTIVE);
			String mdrNumber = node.getMdrNumber();
			if (orgMDRImpl.getOrgMDRNumber(mdrNumber) != null) {
				orgNode.updateOrganization(node);
			}
		} catch (Exception e) {
			logger.error("Exception in createOrganization" + e.getMessage());
			throw e;
		}
	}

}
