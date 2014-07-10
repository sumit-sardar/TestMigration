package com.ctb.control;

import java.sql.SQLException;
import java.util.Date;

import com.ctb.bean.Node;
import com.ctb.dao.IOrgNodeDAO;
import com.ctb.dao.OrgNodeDAO;
import com.ctb.utils.Constants;

public class OrganizationManagementControl {

	private IOrgNodeDAO orgNode = new OrgNodeDAO();
	

	public Node createOrganization(String userName, Node node)
			throws Exception {

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
