package com.ctb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jdbc.OraclePreparedStatement;

import org.apache.log4j.Logger;

import com.ctb.bean.Node;
import com.ctb.bean.OrgNodeCategory;
import com.ctb.utils.SQLUtil;

public class OrgNodeDAO implements IOrgNodeDAO {

	private static Logger logger = Logger.getLogger(OrgNodeDAO.class.getName());
	
	public Node getOrgNodeById(Integer orgNodeId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Node createOrganization(Node orgNode) throws SQLException {
		Connection conn = null;
		OraclePreparedStatement pstmt = null;
	   	ResultSet rSet = null;
	   	String queryString = "Insert into  org_node(  org_node_id,  org_node_category_id,  org_node_name,  customer_id,  created_by,  org_node_code,  activation_status, org_node_mdr_number)  values (  ? , ?,  ?,  ?, ?, ?,  ?, ? ) RETURN ORG_NODE_ID INTO ? ";
	   	try{
	   		conn = SQLUtil.getConnection();
	   		pstmt = (OraclePreparedStatement) conn.prepareStatement(queryString);
	   		pstmt.setString(1, "SEQ_ORG_NODE_ID.Nextval");
	   		pstmt.setInt(2, orgNode.getOrgNodeCategoryId());
	   		pstmt.setString(3, orgNode.getOrgNodeName());
	   		pstmt.setInt(4, orgNode.getCustomerId());
	   		pstmt.setInt(5, 1);
	   		pstmt.setString(6, orgNode.getOrgNodeCode());
	   		pstmt.setString(7, orgNode.getActivationStatus());
	   		pstmt.setString(8, orgNode.getMdrNumber());
	   		
	   		pstmt.registerReturnParameter(9, Types.INTEGER);
	   		pstmt.execute();
	   		rSet = pstmt.getReturnResultSet();
	   		rSet.next();
	   		orgNode.setOrgNodeId(rSet.getInt(1));
	   		
	   		return orgNode;
	   	}catch(SQLException e){
	   		logger.error("SQL Exception in createDataFileTemp-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in createDataFileTemp");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);
        }
	   	return null;
	}

	public void insertOrgNodeForParent(Node orgNode) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	String queryString = "Insert into  org_node_parent (  parent_org_node_id,  org_node_id,  customer_id,  created_by  )  values ( ?, ?, ?, ? ) ";
	   	try{
	   		conn = SQLUtil.getConnection();
	   		pstmt = conn.prepareStatement(queryString);
	   		pstmt.setInt(1, orgNode.getParentOrgNodeId());
	   		pstmt.setInt(2, orgNode.getOrgNodeId());
	   		pstmt.setInt(3, orgNode.getCustomerId());
	   		pstmt.setInt(4, 1);
	   			   		
	   		pstmt.executeQuery();
	   		
	   	}catch(SQLException e){
	   		logger.error("SQL Exception in insertOrgNodeForParent-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in insertOrgNodeForParent");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);
        }
	}

	public OrgNodeCategory getOrgNodeCategories(Integer orgNodeId)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getParentOrgNode(Integer childOrgNodeId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer[] getTopOrgNodeIdsForUser(String username)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getCustomerIdbyOrgNode(Integer orgNodeId)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getTopOrgNodeForCustomer(Integer customerId)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Node[] getOrgNodesByParent(Integer parentOrgNodeId)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
