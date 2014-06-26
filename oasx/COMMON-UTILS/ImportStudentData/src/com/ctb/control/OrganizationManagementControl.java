package com.ctb.control;

import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;

import com.ctb.bean.Node;
import com.ctb.bean.OrgNodeCategory;
import com.ctb.dao.IOrgNodeCategoryDAO;
import com.ctb.dao.IOrgNodeDAO;
import com.ctb.dao.IRolesDAO;
import com.ctb.dao.ITestAdminDAO;
import com.ctb.dao.IUsersDAO;
import com.ctb.dao.OrgNodeCategoryDAO;
import com.ctb.dao.OrgNodeDAO;
import com.ctb.dao.RolesDAO;
import com.ctb.dao.TestAdminDAO;
import com.ctb.dao.UsersDAO;
import com.ctb.utils.Constants;

public class OrganizationManagementControl {

	private IOrgNodeDAO orgNode = new OrgNodeDAO();
	private IUsersDAO users=new UsersDAO();
	private ITestAdminDAO testAdmin=new TestAdminDAO();
	private IOrgNodeCategoryDAO orgNodeCategory=new OrgNodeCategoryDAO();
	private IRolesDAO roles=new RolesDAO();
	

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


	private boolean isLeafNode(Integer selectedOrgNodeId)
			throws Exception {
		try {
			TreeMap map = new TreeMap();
			OrgNodeCategory categoryDetails = orgNode
					.getOrgNodeCategories(selectedOrgNodeId);
			OrgNodeCategory[] customerCategory = orgNodeCategory
					.getOrgNodeCategories(categoryDetails.getCustomerId());
			for (int i = 0; i < customerCategory.length; i++) {
				map.put(customerCategory[i].getCategoryLevel(),
						customerCategory[i]);
			}

			Integer customerLevel = customerCategory[customerCategory.length - 1]
					.getCategoryLevel();

			OrgNodeCategory orgCategory = (OrgNodeCategory) map
					.get((Integer) map.lastKey());

			if (categoryDetails.getOrgNodeCategoryId().intValue() == orgCategory
					.getOrgNodeCategoryId().intValue()) {

				return true;

			}

			return false;

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

}
