package com.ctb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.ctb.bean.Node;
import com.ctb.utils.SQLUtil;

/**
 * Implementation of IOrgNodeDAO Interface
 * 
 * @author TCS
 * 
 */
public class OrgNodeDAO implements IOrgNodeDAO {

	private static Logger logger = Logger.getLogger(OrgNodeDAO.class.getName());

	/**
	 * Used for Inserting Node data in Database after creating Org-node-id from
	 * Sequence generator
	 * 
	 * @param orgNode
	 *            - Node data to be inserted.
	 * @return - Node - Created Node returned.
	 * @throws Exception
	 */

	public Node createOrganization(Node orgNode) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String keyGeneration = " select SEQ_ORG_NODE_ID.Nextval as orgId from dual ";
		Integer orgNodeId = 0;
		ResultSet rs = null;
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(keyGeneration);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				orgNodeId = rs.getInt("orgId");
			}

		} catch (Exception e) {
			throw e;
		}

		SQLUtil.closeDbObjects(null, pstmt, rs);

		String queryString = "Insert into  org_node (  org_node_id,  org_node_category_id,  org_node_name,  customer_id,  created_by,  org_node_code,  activation_status, org_node_mdr_number )  values (  ? , ?,  ?,  ?, ?, ?,  ?, ? )  ";
		try {
			orgNode.setOrgNodeId(orgNodeId);
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, orgNodeId);
			pstmt.setInt(2, orgNode.getOrgNodeCategoryId());
			pstmt.setString(3, orgNode.getOrgNodeName());
			pstmt.setInt(4, orgNode.getCustomerId());
			pstmt.setInt(5, 1);
			pstmt.setString(6, orgNode.getOrgNodeCode());
			pstmt.setString(7, orgNode.getActivationStatus());
			pstmt.setString(8, orgNode.getMdrNumber());

			// pstmt.registerReturnParameter(9, Types.NUMERIC);
			pstmt.execute();

			return orgNode;
		} catch (Exception e) {
			logger.error("Exception in createOrganization");
			e.printStackTrace();
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
	}

	/**
	 * Used for insertion of Data in Org-node-Parent table
	 * 
	 * @param orgNode
	 *            - Node data representing OrgNode
	 * @throws Exception
	 */
	public void insertOrgNodeForParent(Node orgNode) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String queryString = "Insert into  org_node_parent (  parent_org_node_id,  org_node_id,  customer_id,  created_by  )  values ( ?, ?, ?, ? ) ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, orgNode.getParentOrgNodeId());
			pstmt.setInt(2, orgNode.getOrgNodeId());
			pstmt.setInt(3, orgNode.getCustomerId());
			pstmt.setInt(4, 1);

			pstmt.executeQuery();

		} catch (Exception e) {
			logger.error("Exception in insertOrgNodeForParent");
			e.printStackTrace();
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
	}
	
	/**
	 * Used for Updating Node data in Database
	 * 
	 * @param orgNode - Node data to be updated.
	 * @return - Node - Updated Node returned.
	 * @throws Exception
	 */
	public Node updateOrganization(Node orgNode) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLUtil.getConnection();
			String queryString = " Update  org_node set  org_node_name = ? ,   org_node_code = ? ,  activation_status = ? , "
					+ " updated_by = 1 , updated_date_time = sysdate where org_node_id = ?  ";
			pstmt = conn.prepareStatement(queryString);
			pstmt.setString(1, orgNode.getOrgNodeName());
			pstmt.setString(2, orgNode.getOrgNodeCode());
			pstmt.setString(3, orgNode.getActivationStatus());
			pstmt.setInt(4, orgNode.getOrgNodeId());
			pstmt.executeUpdate();
			return orgNode;
		} catch (Exception e) {
			logger.error("Exception in updateOrganization");
			e.printStackTrace();
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

	}
}
