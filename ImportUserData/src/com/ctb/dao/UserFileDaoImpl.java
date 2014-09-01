package com.ctb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ctb.bean.CustomerEmail;
import com.ctb.bean.DataFileAudit;
import com.ctb.bean.Node;
import com.ctb.bean.TimeZones;
import com.ctb.bean.USState;
import com.ctb.bean.UserFileRow;
import com.ctb.utils.Constants;
import com.ctb.utils.SQLUtil;
import com.ctb.utils.UserUtils;
import com.ctb.utils.cache.OrgMDRDBCacheImpl;
import com.ctb.utils.cache.UserDBCacheImpl;
import com.ctb.utils.cache.UserNewRecordCacheImpl;
import com.ctb.utils.cache.UserUpdateRecordCacheImpl;

/**
 * This Class implements UserFileDao interface
 * 
 * @author TCS
 * 
 */
public class UserFileDaoImpl implements UserFileDao {

	static Logger logger = Logger.getLogger(UserFileDaoImpl.class.getName());
	final int BATCH_SIZE_LARGE = 4000;

	/**
	 * This Map is of RoleName and Role-ids
	 * 
	 * @return Map<String, Integer>
	 * @throws Exception
	 */
	public Map<String, Integer> getRoles() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		Map<String, Integer> roleMap = new HashMap<String, Integer>();
		String queryString = "SELECT role.role_id as roleId, INITCAP(role.role_name) as roleName, role.activation_status as activationStatus FROM Role role WHERE role.activation_status = 'AC' AND  role.role_name in ('ADMINISTRATOR','ADMINISTRATIVE COORDINATOR','COORDINATOR','PROCTOR') order by role.role_name";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				Integer roleId = new Integer(rSet.getInt("roleId"));
				String roleName = rSet.getString("roleName");

				roleMap.put(roleName, roleId);
			}
		} catch (Exception e) {
			logger.error("Exception in getRoles" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
		}

		return roleMap;
	}

	/**
	 * Returns all the Timezones
	 * 
	 * @return TimeZones[]
	 * @throws Exception
	 */
	public TimeZones[] getTimeZones() throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		List<TimeZones> timeZoneList = new ArrayList<TimeZones>();
		String queryString = "select tz.time_zone as timeZone, INITCAP(tz.time_zone_desc) as timeZoneDesc from time_zone_code tz";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				TimeZones timeZone = new TimeZones();
				timeZone.setTimeZone(rSet.getString("timeZone"));
				timeZone.setTimeZoneDesc(rSet.getString("timeZoneDesc"));
				timeZoneList.add(timeZone);
			}
		} catch (Exception e) {
			logger.error("Exception in getTimeZones" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
		}

		return timeZoneList.toArray(new TimeZones[timeZoneList.size()]);
	}

	/**
	 * Returns all present USState
	 * 
	 * @return USState[]
	 * @throws Exception
	 */
	public USState[] getStates() throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		List<USState> usStateList = new ArrayList<USState>();
		String queryString = "select  s.statepr as statePr, s.statepr_desc as statePrDesc from statepr_code s  order by  s.statepr_desc ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				USState usState = new USState();
				usState.setStatePr(rSet.getString("statePr"));
				usState.setStatePrDesc(rSet.getString("statePrDesc"));
				usStateList.add(usState);
			}
		} catch (Exception e) {
			logger.error("Exception in getStates" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
		}

		return usStateList.toArray(new USState[usStateList.size()]);

	}

	/**
	 * This method will return the set of Node data for a customer.
	 * 
	 * @param customerId
	 * @return Node[]
	 * @throws Exception
	 */
	public Node[] getUserDataTemplate(Integer customerId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<Node> nodeList = new ArrayList<Node>();
		String queryString = "select distinct parent.parent_org_node_id as parentorgnodeid, descendant_node.org_node_id as orgnodeid, descendant_node.customer_id as customerid, descendant_node.org_node_category_id as orgnodecategoryid, descendant_node.org_node_name as orgnodename, descendant_node.activation_status as activationstatus, descendant_node.org_node_code as orgnodecode, cat.category_name as orgnodecategoryname, cat.category_level as categorylevel, nvl(descendant_node.org_node_mdr_number, ' ') as mdrnumber from org_node node, org_node_category cat, org_node_ancestor descendants, org_node descendant_node, org_node_parent PARENT where cat.org_node_category_id = descendant_node.org_node_category_id and node.org_node_id = descendants.ancestor_org_node_id and descendants.org_node_id = descendant_node.org_node_id and parent.org_node_id = descendant_node.org_node_id and descendant_node.activation_status = 'AC' AND node.customer_id = ? order by cat.category_level, parent.parent_org_node_id";

		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				Node node = new Node();
				node.setParentOrgNodeId(rSet.getInt("parentorgnodeid"));
				node.setOrgNodeId(rSet.getInt("orgnodeid"));
				node.setCustomerId(rSet.getInt("customerId"));
				node.setOrgNodeCategoryId(rSet.getInt("orgNodeCategoryId"));
				node.setOrgNodeName(rSet.getString("orgNodeName"));
				node.setActivationStatus(rSet.getString("activationStatus"));
				node.setOrgNodeCode(rSet.getString("orgNodeCode"));
				node.setMdrNumber(rSet.getString("mdrNumber"));
				node.setOrgNodeCategoryName(rSet
						.getString("orgNodeCategoryName"));
				node.setCategoryLevel(rSet.getInt("categoryLevel"));
				nodeList.add(node);
			}
		} catch (Exception e) {
			logger.error("Exception in getUserDataTemplate" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return nodeList.toArray(new Node[nodeList.size()]);
	}

	/**
	 * This method populates Cache with all existing user data in Database
	 * 
	 * @param customerId
	 * @param dbCache
	 * @throws Exception
	 */
	/*
	 * private void getExistUserData_Pro(Integer customerId, UserDBCacheImpl
	 * dbCache) throws Exception { Connection conn = null; PreparedStatement
	 * pstmt = null; ResultSet rSet = null;
	 *//**
	 * This query is having order-by Clause. Do not remove the order-by
	 * clause for any performance tuning.This will cause the code to break.
	 */
	/*
	 * String queryString =
	 * "select distinct u.user_id as userId, u.user_name as userName, u.first_name as firstName, "
	 * +
	 * " u.middle_name as middleName, u.last_name as lastName,  u.email as email, INITCAP(r.role_name) as roleName, "
	 * +
	 * " r.role_id as roleId, u.ext_school_id as extSchoolId, u.address_id as addressId , "
	 * +
	 * " ur.org_node_id as orgNodeId from users u, user_role ur, role r, org_node node "
	 * +
	 * " where u.user_id = ur.user_id and ur.org_node_id = node.org_node_id and ur.role_id = r.role_id "
	 * +
	 * " and node.activation_status = 'AC' and u.activation_status = 'AC' and ur.activation_status = 'AC' "
	 * + " and node.customer_id = ?  order by u.user_id ";
	 * 
	 * int prevUserId = -1, newUserId = 0; try { conn = SQLUtil.getConnection();
	 * pstmt = conn.prepareStatement(queryString); pstmt.setInt(1, customerId);
	 * rSet = pstmt.executeQuery(); UserFileRow userFileRow = new UserFileRow();
	 * List<Node> orgList = new ArrayList<Node>(); while (rSet.next()) {
	 * newUserId = rSet.getInt("userId");
	 *//**
	 * An user can be linked to more than 1 orgs.So each user bean will be
	 * having the Array of Org-nodes associated with that user.This block is
	 * used to populate the User-bean having Node[] for each user.
	 */
	/*
	 * if (newUserId != prevUserId) { userFileRow.setOrganizationNodes(orgList
	 * .toArray(new Node[orgList.size()]));
	 * dbCache.addUserFileRow(userFileRow.getKey(), userFileRow); userFileRow =
	 * new UserFileRow(); orgList = new ArrayList<Node>(); }
	 * 
	 * Node orgNode = new Node(); String extSchoolId = "";
	 * userFileRow.setUserId(newUserId);
	 * userFileRow.setUserName(rSet.getString("userName"));
	 * userFileRow.setFirstName(rSet.getString("firstName"));
	 * userFileRow.setMiddleName(rSet.getString("middleName"));
	 * userFileRow.setLastName(rSet.getString("lastName"));
	 * userFileRow.setEmail(rSet.getString("email"));
	 * userFileRow.setRoleName(rSet.getString("roleName"));
	 * userFileRow.setRoleId(rSet.getInt("roleId"));
	 * 
	 * extSchoolId = rSet.getString("extSchoolId");
	 * userFileRow.setExtSchoolId(extSchoolId);
	 * userFileRow.setAddressId(rSet.getInt("addressId"));
	 * 
	 * orgNode.setOrgNodeId(rSet.getInt("orgNodeId")); orgList.add(orgNode);
	 * 
	 * prevUserId = newUserId;
	 *//**
	 * ExtSchoolId is Unique for each user and used for identifying Users. If
	 * ExtSchoolId is not present for any User then Basic-Username will be used
	 * for identifying purpose.
	 */
	/*
	 * userFileRow.setKey(!("".equals(extSchoolId)) ? extSchoolId :
	 * generateKey(userFileRow));
	 * 
	 * 
	 * } userFileRow.setOrganizationNodes(orgList.toArray(new Node[orgList
	 * .size()])); dbCache.addUserFileRow(userFileRow.getKey(), userFileRow); }
	 * catch (Exception e) { logger.error("Exception in getExistUserData " +
	 * e.getMessage()); throw e; } finally { SQLUtil.closeDbObjects(conn, pstmt,
	 * null); } }
	 */

	/**
	 * This method populates Cache with all existing user data in Database
	 * 
	 * @param customerId
	 * @param dbCache
	 * @throws Exception
	 */
	public void getExistUserData(Integer customerId, UserDBCacheImpl dbCache)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		/**
		 * This query is having order-by Clause. Do not remove the order-by
		 * clause for any performance tuning.This will cause the code to break.
		 */
		String queryString = " select u.user_id as userId, "
				+ "        u.user_name as userName, "
				+ "        u.first_name as firstName, "
				+ "        u.middle_name as middleName, "
				+ "        u.last_name as lastName, "
				+ "        u.email as email, "
				+ "        INITCAP(r.role_name) as roleName, "
				+ "        r.role_id as roleId, "
				+ "        u.ext_school_id as extSchoolId, "
				+ "        u.address_id as addressId, "
				+ "        RTRIM(xmlagg(xmlelement(d, ur.org_node_id || ',')) "
				+ "              .extract('//text()'), "
				+ "              ',') AS orgNodeIds "
				+ "   from users u, user_role ur, role r, org_node node "
				+ "  where u.user_id = ur.user_id "
				+ "    and ur.org_node_id = node.org_node_id "
				+ "    and ur.role_id = r.role_id "
				+ "    and node.activation_status = 'AC' "
				+ "    and u.activation_status = 'AC' "
				+ "    and ur.activation_status = 'AC' "
				+ "    and node.customer_id = ? " + "  group by u.user_id, "
				+ "           u.user_name, " + "           u.first_name, "
				+ "           u.middle_name, " + "           u.last_name, "
				+ "           u.email, " + "           r.role_name, "
				+ "           r.role_id, " + "           u.ext_school_id, "
				+ "           u.address_id ";

		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			rSet = pstmt.executeQuery();
			UserFileRow userFileRow = new UserFileRow();
			while (rSet.next()) {
				List<Node> orgList = new ArrayList<Node>();

				userFileRow.setUserId(rSet.getInt("userId"));
				userFileRow.setUserName(rSet.getString("userName"));
				userFileRow.setFirstName(rSet.getString("firstName"));
				userFileRow.setMiddleName(rSet.getString("middleName"));
				userFileRow.setLastName(rSet.getString("lastName"));
				userFileRow.setEmail(rSet.getString("email"));
				userFileRow.setRoleName(rSet.getString("roleName"));
				userFileRow.setRoleId(rSet.getInt("roleId"));
				String extSchoolId = "";
				extSchoolId = rSet.getString("extSchoolId");
				userFileRow.setExtSchoolId(extSchoolId);
				userFileRow.setAddressId(rSet.getInt("addressId"));

				/**
				 * ExtSchoolId is Unique for each user and used for identifying
				 * Users. If ExtSchoolId is not present for any User then
				 * Basic-Username will be used for identifying purpose.
				 */
				userFileRow.setKey(!("".equals(extSchoolId)) ? extSchoolId
						: generateKey(userFileRow));

				String orgNodeIds = rSet.getString("orgNodeIds");
				String[] orgArr = orgNodeIds.split(",");
				for (String org : orgArr) {
					orgList.add(new Node(Integer.valueOf(org)));
				}
				userFileRow.setOrganizationNodes(orgList
						.toArray(new Node[orgList.size()]));
				dbCache.addUserFileRow(userFileRow.getKey(), userFileRow);
			}

		} catch (Exception e) {
			logger.error("Exception in getExistUserData " + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
	}

	/**
	 * Generates the user Key which is basic user-name
	 * 
	 * @param student
	 * @return String
	 */
	private String generateKey(UserFileRow userInfo) {
		String key = UserUtils.generateEscapeUsername(userInfo);
		return key;
	}

	/**
	 * This method returns the TopNode Details for a customer
	 * 
	 * @param customerId
	 * @return Node[]
	 * @throws Exception
	 */
	public Node[] getTopNodeDetails(Integer customerId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<Node> nodeList = new ArrayList<Node>();
		String queryString = "select distinct org.org_node_id as orgnodeid, org.customer_id as customerid, org.org_node_category_id as orgnodecategoryid, org.org_node_name as orgnodename, org.ext_qed_pin as extqedpin, org.ext_elm_id as extelmid, org.ext_org_node_type as extorgnodetype, org.org_node_description as orgnodedescription, org.created_by as createdby, org.created_date_time as createddatetime, org.updated_by as updatedby, org.updated_date_time as updateddatetime, org.activation_status as activationstatus, org.data_import_history_id as dataimporthistoryid, org.parent_state as parentstate, org.parent_region as parentregion, org.parent_county as parentcounty, org.parent_district as parentdistrict, org.org_node_code as orgnodecode, org.org_node_mdr_number as mdrnumber, onc.category_name as orgnodecategoryname, count(distinct onp.org_node_id) as childnodecount from org_node org, org_node_category onc, org_node_parent onp where org.customer_id = onc.customer_id and onc.org_node_category_id = org.org_node_category_id and onp.parent_org_node_id (+) = org.org_node_id and onc.customer_id = ? and onc.activation_status = 'AC' and org.activation_status = 'AC' and onc.category_level = (select min(onc1.category_level) from org_node_category onc1 where onc1.customer_id = ? and onc1.is_group = 'F' and onc1.activation_status = 'AC') group by org.org_node_id, org.customer_id, org.org_node_category_id, org.org_node_name, org.ext_qed_pin, org.ext_elm_id, org.ext_org_node_type, org.org_node_description, org.created_by, org.created_date_time, org.updated_by, org.updated_date_time, org.activation_status, org.data_import_history_id, org.parent_state, org.parent_region, org.parent_county, org.parent_district, org.org_node_code, org.org_node_mdr_number, onc.category_name";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			pstmt.setInt(2, customerId);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				Node node = new Node();
				node.setOrgNodeId(rSet.getInt("orgnodeid"));
				node.setCustomerId(rSet.getInt("customerid"));
				node.setOrgNodeCategoryId(rSet.getInt("orgnodecategoryid"));
				node.setOrgNodeName(rSet.getString("orgnodename"));
				node.setActivationStatus(rSet.getString("activationstatus"));
				node.setParentDistrict(rSet.getString("parentdistrict"));
				node.setOrgNodeCode(rSet.getString("orgnodecode"));
				node.setMdrNumber(rSet.getString("mdrnumber"));
				node.setOrgNodeCategoryName(rSet
						.getString("orgnodecategoryname"));
				node.setChildNodeCount(rSet.getInt("childnodecount"));
				nodeList.add(node);
			}
		} catch (Exception e) {
			logger.error("Exception in getTopNodeDetails" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return nodeList.toArray(new Node[nodeList.size()]);
	}

	/**
	 * Returns Details of DATA_FILE_AUDIT table for a particular
	 * data_file_audit_id
	 * 
	 * @param uploadDataFileId
	 * @return DataFileAudit
	 */
	public DataFileAudit getUploadFile(Integer uploadDataFileId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DataFileAudit dataFileAudit = null;
		String queryString = "select dfa.DATA_FILE_AUDIT_ID  as dataFileAuditId,  dfa.user_id  as userId,  dfa.customer_id  as customerId,  dfa.upload_file_name  as uploadFileName,  dfa.uploaded_record_count as uploadFileRecordCount,  dfa.failed_record_count  as failedRecordCount,  dfa.status  as status,  dfa.created_by  as createdBy,  dfa.created_date_time  as createdDateTime  from DATA_FILE_AUDIT dfa  where dfa.data_file_audit_id = ?";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, uploadDataFileId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dataFileAudit = new DataFileAudit();
				dataFileAudit.setDataFileAuditId(rs.getInt("datafileauditid"));
				dataFileAudit.setUserId(rs.getInt("userid"));
				dataFileAudit.setCustomerId(rs.getInt("customerid"));
				dataFileAudit.setUploadFileName(rs.getString("uploadfilename"));
				dataFileAudit.setUploadFileRecordCount(rs
						.getInt("uploadfilerecordcount"));
				dataFileAudit.setFailedRecordCount(rs
						.getInt("failedrecordcount"));
				dataFileAudit.setStatus(rs.getString("status"));
				dataFileAudit.setCreatedBy(rs.getInt("createdby"));
				dataFileAudit.setCreatedDateTime(rs.getDate("createddatetime"));
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in getUploadFile-- >"
					+ e.getErrorCode());
			logger.error("Exception in getUploadFile" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getUploadFile" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rs);
		}
		return dataFileAudit;
	}

	/**
	 * Fetches Email details for a customer
	 * 
	 * @param userName
	 * @param emailType
	 * @return CustomerEmail
	 * @throws Exception
	 */
	public CustomerEmail getCustomerEmailByUserName(String userName,
			Integer emailType) throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CustomerEmail customerEmail = null;
		String queryString = "select c.customer_id as customerId , c.email_type as emailType , c.reply_to as replyTo , c.subject as subject , c.email_body as emailBody from customer_email_config c where c.customer_id = (select distinct node.customer_id  from users u  , user_role role  , org_node node  where u.user_name = ? and u.user_id = role.user_id  and role.org_node_id = node.org_node_id) and c.email_type = ? ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setString(1, userName);
			pstmt.setInt(2, emailType);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				customerEmail = new CustomerEmail();
				customerEmail.setCustomerId(rs.getInt("customerId"));
				customerEmail.setEmailType(rs.getInt("emailType"));
				customerEmail.setReplyTo(rs.getString("replyTo"));
				customerEmail.setSubject(rs.getString("subject"));
				customerEmail.setEmailBody(rs.getClob("emailBody"));
			}
		} catch (Exception e) {
			logger.error("Exception in getUploadFile" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rs);
		}
		return customerEmail;

	}

	/**
	 * This method checks if the mdrNumber is Unique or not
	 * 
	 * @param selectedMdrNumber
	 * @return String
	 * @throws Exception
	 *//*
	public String checkUniqueMdrNumberForOrgNodes(String selectedMdrNumber)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		String uniqueNumber = null;
		String queryString = "SELECT DECODE(COUNT(1), 0, 'T', 'F') AS UNIQUEMDRNUMBER  FROM ORG_NODE ORG WHERE ORG.ORG_NODE_MDR_NUMBER = ? AND ORG.ACTIVATION_STATUS = 'AC'";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setString(1, selectedMdrNumber);
			rSet = pstmt.executeQuery();
			if (rSet.next()) {
				uniqueNumber = rSet.getString("UNIQUEMDRNUMBER");
			}
		} catch (Exception e) {
			logger.error("Exception in checkUniqueMdrNumberForOrgNodes"
					+ e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
		}
		return uniqueNumber;
	}*/

	/**
	 * Username existence Count retrieval.
	 * 
	 * @param userName
	 * @param userNameescape
	 * @param whereRegExp
	 * @param selectRegExp
	 * @param replaceStr
	 * @return Integer
	 * @throws Exception
	 */
	public Integer findExistingUserName(String userName, String userNameescape,
			String whereRegExp, String selectRegExp, String replaceStr)
			throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		Integer count = 0;
		String queryString = "select max(to_number(nvl(regexp_replace(u.user_name, ?, ?), 0))) as count from (SELECT USER_NAME FROM users WHERE  user_name LIKE ? || '%' ESCAPE '\\') u where regexp_like(u.user_name,?) or u.user_name = ?";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setString(1, selectRegExp);
			pstmt.setString(2, replaceStr);
			pstmt.setString(3, userNameescape);
			pstmt.setString(4, whereRegExp);
			pstmt.setString(5, userName);
			rSet = pstmt.executeQuery();
			if (rSet.next()) {
				count = rSet.getInt("count");
			}
		} catch (Exception e) {
			logger.error("Exception in findExistingUserName" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
		}
		return count;

	}

	/**
	 * Populates Username for each user which is unique
	 * 
	 * @param userNewCacheImpl
	 * @param userNames
	 * @param userCount
	 * @throws Exception
	 */
	public void populateActualUserName(UserNewRecordCacheImpl userNewCacheImpl,
			String userNames, Integer userCount) throws Exception {
		logger.info("executing populateActualUserName()");
		Set<String> newSet = new HashSet<String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		String usernameQuery = "SELECT USER_NAME FROM USERS WHERE USER_NAME ";
		try {
			if (null != userNames) {
				conn = SQLUtil.getConnection();
				if (!userNames.contains("#")) {
					usernameQuery += userNames;
					pstmt = conn.prepareStatement(usernameQuery);
					pstmt.setFetchSize(20);
					rSet = pstmt.executeQuery();
					while (rSet.next()) {
						newSet.add(rSet.getString("user_name"));
					}
				} else {
					String[] splitedStr = userNames.split("#");
					for (int i = 0; i < splitedStr.length; i++) {
						if (null == splitedStr[i])
							break;
						String query = usernameQuery + splitedStr[i].toString();
						pstmt = conn.prepareStatement(query);

						pstmt.setFetchSize(20);
						rSet = pstmt.executeQuery();
						while (rSet.next()) {
							newSet.add(rSet.getString("user_name"));
						}
						SQLUtil.closeDbObjects(null, pstmt, rSet);
					}
				}
			}
			List<String> keys = userNewCacheImpl.getKeys();
			for (String key : keys) {
				if (userNewCacheImpl.getNewUser(key) != null) {
					UserFileRow user = ((UserFileRow) userNewCacheImpl
							.getNewUser(key));
					user.setUserName(UserUtils.generateUniqueUserName(newSet,
							user));
					userNewCacheImpl.addNewUser(key, user);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in populateActualUserName" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
			logger.info("Username population completed");
		}

	}

	/**
	 * UserIds and AddressIds populations
	 * 
	 * @param userNewCacheImpl
	 * @param userCount
	 * @param addressCount
	 * @param keyUserIdMap
	 * @throws Exception
	 */
	public void populateActualUserAndAddressIds(
			UserNewRecordCacheImpl userNewCacheImpl, Integer userCount,
			Integer addressCount, Map<String, Integer> keyUserIdMap,
			Map<String, Integer> keyAddressIdMap) throws Exception {

		logger.info("executing populateActualUserIds");
		Integer[] userIds = null;
		Integer[] addressIds = null;
		ArrayList<Integer> idList = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		String seqQuery = "select seq_user_id.nextval from dual connect by level<= ?";
		String seqAddressQuery = "select seq_address_id.nextval from dual connect by level<= ?";
		try {
			conn = SQLUtil.getConnection();

			/**
			 * Populating UserIds
			 */
			pstmt = conn.prepareStatement(seqQuery);
			pstmt.setInt(1, userCount.intValue());
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				idList.add(rSet.getInt(1));
			}
			SQLUtil.closeDbObjects(null, pstmt, rSet);
			userIds = idList.toArray(new Integer[idList.size()]);

			idList.clear();

			/**
			 * Populating AddressIds
			 */
			if (addressCount > 0) {
				pstmt = conn.prepareStatement(seqAddressQuery);
				pstmt.setInt(1, addressCount.intValue());
				rSet = pstmt.executeQuery();
				while (rSet.next()) {
					idList.add(rSet.getInt(1));
				}
				addressIds = idList.toArray(new Integer[idList.size()]);
			}
			int count = 0, addCount = 0;
			List<String> keys = userNewCacheImpl.getKeys();
			for (String key : keys) {
				UserFileRow user = userNewCacheImpl.getNewUser(key);
				if (user != null) {
					user.setUserId(userIds[count++]);
					if (user.isAddressPresent() && addressCount > 0) {
						user.setAddressId(addressIds[addCount++]);
						keyAddressIdMap.put(key, user.getAddressId());
					}
					userNewCacheImpl.addNewUser(key, user);
					/**
					 * Map used for tracking User-id in case where same file
					 * contains an user record for Insertion and Update.
					 */
					keyUserIdMap.put(key, user.getUserId());
				}

			}

		} catch (Exception e) {
			logger.error("Exception in populateActualUserIds" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
			logger.info("User Ids population completed");

		}

	}

	/**
	 * Inserts AddressInformation for an User
	 * 
	 * @param userNewCacheImpl
	 * @throws Exception
	 */
	public void insertAddressForUser(UserNewRecordCacheImpl userNewCacheImpl)
			throws Exception {

		logger.info("executing insertAdsressForUser()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		String insertQuery = "insert into address (address_id, street_line1, street_line2, city, statepr, zipcode, zipcode_ext, primary_phone, primary_phone_ext, created_by, created_date_time, fax, secondary_phone) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, ?, ?)";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(insertQuery);
			List<String> keys = userNewCacheImpl.getKeys();
			for (String key : keys) {
				UserFileRow user = userNewCacheImpl.getNewUser(key);

				if (!user.isAddressPresent()) {
					continue;
				}

				pstmt.setInt(1, user.getAddressId());
				pstmt.setString(2, user.getAddress1());
				pstmt.setString(3, user.getAddress2());
				pstmt.setString(4, user.getCity());
				pstmt.setString(5, user.getState());
				pstmt.setString(6, user.getZip());
				pstmt.setString(7, user.getZipCodeExt());
				pstmt.setString(8, user.getPrimaryPhone());
				pstmt.setString(9, user.getPrimaryPhoneExt());
				pstmt.setInt(10, Constants.USER_ID);
				pstmt.setString(11, user.getFaxNumber());
				pstmt.setString(12, user.getSecondaryPhone());

				pstmt.addBatch();
				count++;
				if (count % BATCH_SIZE_LARGE == 0) {
					pstmt.executeBatch();
					logger.info("User address row count" + count);
				}
			}
			pstmt.executeBatch();

		} catch (Exception e) {
			logger.error("Exception in insertAdsressForUser" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
			logger.info("done executing insertAdsressForUser()");
		}

	}

	/**
	 * Inserts User Profile Information for an User
	 * 
	 * @param userNewCacheImpl
	 * @throws Exception
	 */
	public void insertUserProfile(UserNewRecordCacheImpl userNewCacheImpl)
			throws Exception {
		logger.info("executing insertUserProfile()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		String insertQuery = "insert into users (user_id, user_name, password, first_name, middle_name, last_name, email, password_expiration_date, reset_password, address_id, time_zone, created_date_time, created_by, activation_status, display_user_name, ext_school_id, display_new_message ) values (?, ?, ?, ?, ?, ?, ?, (sysdate+90), ? , ? , ?, sysdate, ?, 'AC', ?, ?, ?)";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(insertQuery);
			List<String> keys = userNewCacheImpl.getKeys();
			for (String key : keys) {
				UserFileRow user = userNewCacheImpl.getNewUser(key);

				pstmt.setInt(1, user.getUserId());
				pstmt.setString(2, user.getUserName());
				pstmt.setString(3, user.getPassword());
				pstmt.setString(4, user.getFirstName());
				pstmt.setString(5, user.getMiddleName());
				pstmt.setString(6, user.getLastName());
				pstmt.setString(7, user.getEmail());
				pstmt.setString(8, Constants.RESET_PASSWORD);
				if (null != user.getAddressId())
					pstmt.setInt(9, user.getAddressId());
				else
					pstmt.setNull(9, Types.INTEGER);
				pstmt.setString(10, user.getTimeZone());
				pstmt.setInt(11, Constants.USER_ID);
				pstmt.setString(12, user.getUserName());
				pstmt.setString(13, user.getExtSchoolId());
				pstmt.setString(14, Constants.TRUE);

				pstmt.addBatch();
				count++;
				if (count % BATCH_SIZE_LARGE == 0) {
					pstmt.executeBatch();
					logger.info("User row count" + count);
				}
			}
			pstmt.executeBatch();

		} catch (Exception e) {
			logger.error("Exception in insertUserProfile" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
			logger.info("done executing insertUserProfile()");
		}

	}

	/**
	 * User-role Population
	 * 
	 * @param userNewCacheImpl
	 * @throws Exception
	 */
	public void insertUserRole(UserNewRecordCacheImpl userNewCacheImpl)
			throws Exception {

		logger.info("executing insertUserRole()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		String insertQuery = "insert into user_role (user_id, role_id, org_node_id, activation_status, created_by, created_date_time) values (?, ?, ?, 'AC', ?, sysdate)";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(insertQuery);
			List<String> keys = userNewCacheImpl.getKeys();
			for (String key : keys) {
				UserFileRow user = userNewCacheImpl.getNewUser(key);
				Node[] orgNodes = user.getOrganizationNodes();
				for (Node node : orgNodes) {

					pstmt.setInt(1, user.getUserId());
					pstmt.setInt(2, user.getRoleId());
					pstmt.setInt(3, node.getOrgNodeId());
					pstmt.setInt(4, Constants.USER_ID);

					pstmt.addBatch();
					count++;
					if (count % BATCH_SIZE_LARGE == 0) {
						pstmt.executeBatch();
						logger.info("User role row count" + count);
					}

				}
			}
			pstmt.executeBatch();

		} catch (Exception e) {
			logger.error("Exception in insertUserRole" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
			logger.info("done executing insertUserRole()");
		}

	}

	/**
	 * Generation of Address-ids for Users
	 * 
	 * @param userUpdateCacheImpl
	 * @param addressCount
	 * @throws Exception
	 */
	public void populateActualAddressIds(
			UserUpdateRecordCacheImpl userUpdateCacheImpl, Integer addressCount)
			throws Exception {
		logger.info("executing populateActualAddressIds");
		Integer[] addressIds = null;
		ArrayList<Integer> idList = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		String seqAddressQuery = "select seq_address_id.nextval from dual connect by level<= ?";
		try {
			conn = SQLUtil.getConnection();
			/**
			 * Populating AddressIds
			 */
			pstmt = conn.prepareStatement(seqAddressQuery);
			pstmt.setInt(1, addressCount.intValue());
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				idList.add(rSet.getInt(1));
			}
			addressIds = idList.toArray(new Integer[idList.size()]);

			int addCount = 0;
			List<String> keys = userUpdateCacheImpl.getKeys();
			for (String key : keys) {
				UserFileRow user = userUpdateCacheImpl.getUpdatedUser(key);
				if (user != null) {
					if (user.isAddressPresent() && addressCount > 0
							&& user.getAddressId() == null) {
						user.setAddressId(addressIds[addCount++]);
					}
					userUpdateCacheImpl.addUpdatedUser(key, user);
				}

			}

		} catch (Exception e) {
			logger.error("Exception in populateActualAddressIds"
					+ e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
			logger.info("done executing populateActualAddressIds()");
		}

	}

	/**
	 * Address Update Operation for users. Address present will be updated
	 * otherwise inserted.
	 * 
	 * @param userUpdateCacheImpl
	 * @throws Exception
	 */
	public void updateAddressForUser(
			UserUpdateRecordCacheImpl userUpdateCacheImpl) throws Exception {

		logger.info("executing updateAddressForUser()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		String mergeQuery = " merge into address a "
				+ " using (select ? as address_id from dual) s "
				+ " on (s.address_id = a.address_id)  when matched then "
				+ "  update   set a.street_line1      = ?, "
				+ "         a.street_line2      = ?, "
				+ "         a.city              = ?, "
				+ "         a.statepr           = ?, "
				+ "         a.zipcode           = ?, "
				+ "         a.zipcode_ext       = ?, "
				+ "         a.primary_phone     = ?, "
				+ "         a.primary_phone_ext = ?, "
				+ "         a.created_by        = ?, "
				+ "         a.created_date_time = sysdate, "
				+ "         a.fax               = ?, "
				+ "         a.secondary_phone   = ? "
				+ "  when not matched then   insert "
				+ "    (a.address_id,    a.street_line1, "
				+ "     a.street_line2,   a.city, "
				+ "     a.statepr,    a.zipcode, "
				+ "     a.zipcode_ext,   a.primary_phone, "
				+ "     a.primary_phone_ext,   a.created_by, "
				+ "     a.created_date_time,     a.fax, "
				+ "     a.secondary_phone)  values "
				+ "    (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate,  ?, ?)  ";

		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(mergeQuery);
			List<String> keys = userUpdateCacheImpl.getKeys();
			for (String key : keys) {

				UserFileRow user = userUpdateCacheImpl.getUpdatedUser(key);
				if (!user.isAddressPresent()) {
					continue;
				}
				pstmt.setInt(1, user.getAddressId());
				pstmt.setString(2, user.getAddress1());
				pstmt.setString(3, user.getAddress2());
				pstmt.setString(4, user.getCity());
				pstmt.setString(5, user.getState());
				pstmt.setString(6, user.getZip());
				pstmt.setString(7, user.getZipCodeExt());
				pstmt.setString(8, user.getPrimaryPhone());
				pstmt.setString(9, user.getPrimaryPhoneExt());
				pstmt.setInt(10, Constants.USER_ID);
				pstmt.setString(11, user.getFaxNumber());
				pstmt.setString(12, user.getSecondaryPhone());
				pstmt.setInt(13, user.getAddressId());
				pstmt.setString(14, user.getAddress1());
				pstmt.setString(15, user.getAddress2());
				pstmt.setString(16, user.getCity());
				pstmt.setString(17, user.getState());
				pstmt.setString(18, user.getZip());
				pstmt.setString(19, user.getZipCodeExt());
				pstmt.setString(20, user.getPrimaryPhone());
				pstmt.setString(21, user.getPrimaryPhoneExt());
				pstmt.setInt(22, Constants.USER_ID);
				pstmt.setString(23, user.getFaxNumber());
				pstmt.setString(24, user.getSecondaryPhone());

				pstmt.addBatch();
				count++;
				if (count % BATCH_SIZE_LARGE == 0) {
					pstmt.executeBatch();
					logger.info("User address row count" + count);
				}
			}
			pstmt.executeBatch();

		} catch (Exception e) {
			logger.error("Exception in updateAddressForUser" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
			logger.info("done executing updateAddressForUser()");
		}

	}

	/**
	 * User-profile will be updated.
	 * 
	 * @param userUpdateCacheImpl
	 * @param keyUserIdMap
	 * @throws Exception
	 */
	public void updateUserProfile(
			UserUpdateRecordCacheImpl userUpdateCacheImpl,
			Map<String, Integer> keyUserIdMap) throws Exception {

		logger.info("executing updateUserProfile()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		String updateQuery = " update users  set first_name = ?, middle_name = ?, last_name = ?, email = ?,  "
				+ " password_expiration_date = (sysdate + 90),  reset_password = ?,  address_id = ?,  time_zone = ?, "
				+ " updated_date_time = sysdate,  updated_by = ?,  activation_status = 'AC',  ext_school_id = ?  "
				+ " where user_id = ? ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(updateQuery);
			List<String> keys = userUpdateCacheImpl.getKeys();
			for (String key : keys) {
				UserFileRow user = userUpdateCacheImpl.getUpdatedUser(key);
				pstmt.setString(1, user.getFirstName());
				pstmt.setString(2, user.getMiddleName());
				pstmt.setString(3, user.getLastName());
				pstmt.setString(4, user.getEmail());
				pstmt.setString(5, Constants.RESET_PASSWORD);
				if (null != user.getAddressId())
					pstmt.setInt(6, user.getAddressId());
				else
					pstmt.setNull(6, Types.INTEGER);
				pstmt.setString(7, user.getTimeZone());
				pstmt.setInt(8, Constants.USER_ID);
				pstmt.setString(9, user.getExtSchoolId());

				/**
				 * This block will be executed when Single file is having more
				 * than 1 records of same user.Then 1st record will be for
				 * insert and rest for update. Hence in these update scenarios
				 * we have to get the User-id generated during insertion
				 * process.
				 */
				if (user.getUserId() == null) {
					user.setUserId(keyUserIdMap.get(user.getExtSchoolId()));
				}

				pstmt.setInt(10, user.getUserId());
				pstmt.addBatch();
				count++;
				if (count % BATCH_SIZE_LARGE == 0) {
					pstmt.executeBatch();
					logger.info("User row count" + count);
				}
			}
			pstmt.executeBatch();

		} catch (Exception e) {
			logger.error("Exception in updateUserProfile" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
			logger.info("done executing updateUserProfile()");
		}

	}

	/**
	 * User-Role will be updated.
	 * 
	 * @param userUpdateCacheImpl
	 * @throws Exception
	 */
	public void updateUserRole(UserUpdateRecordCacheImpl userUpdateCacheImpl)
			throws Exception {
		logger.info("executing updateUserRole()");
		Connection conn = null;
		Connection connNew = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtNew = null;
		int count = 0, countNew = 0;
		String mergeQuery = " merge into user_role ur "
				+ " using (select ? as user_id, ? as org_node_id from dual) d "
				+ " on (ur.user_id = d.user_id and ur.org_node_id = d.org_node_id) "
				+ " when matched then  update"
				+ "     set activation_status = 'AC',"
				+ "         updated_by        = ?,"
				+ "         updated_date_time = sysdate"
				+ "   where user_id = ?" + "     and org_node_id = ? "
				+ " when not matched then" + "  insert" + "    (user_id, "
				+ "     role_id," + "     org_node_id,"
				+ "     activation_status," + "     created_by,"
				+ "     created_date_time)" + "  values"
				+ "    (?, ?, ?, 'AC', ?, sysdate)";

		String roleUpdateQuery = " update user_role set role_id = ? ,updated_by  = ?, "
				+ "   updated_date_time = sysdate where user_id = ? and activation_status = 'AC' ";

		try {
			conn = SQLUtil.getConnection();
			connNew = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(mergeQuery);
			pstmtNew = connNew.prepareStatement(roleUpdateQuery);

			List<String> keys = userUpdateCacheImpl.getKeys();
			for (String key : keys) {
				UserFileRow user = userUpdateCacheImpl.getUpdatedUser(key);
				Node[] orgNodes = user.getOrganizationNodes();
				for (Node node : orgNodes) {
					/**
					 * Each User can be associated with more than 1 org-nodes
					 */
					pstmt.setInt(1, user.getUserId());
					pstmt.setInt(2, node.getOrgNodeId());
					pstmt.setInt(3, Constants.USER_ID);
					pstmt.setInt(4, user.getUserId());
					pstmt.setInt(5, node.getOrgNodeId());
					pstmt.setInt(6, user.getUserId());
					pstmt.setInt(7, user.getRoleId());
					pstmt.setInt(8, node.getOrgNodeId());
					pstmt.setInt(9, Constants.USER_ID);

					pstmt.addBatch();
					count++;
					if (count % BATCH_SIZE_LARGE == 0) {
						pstmt.executeBatch();
						logger.info("User role row count" + count);
					}
				}

				/**
				 * Each User can have only 1 Role.Hence updating the user with
				 * the latest role for all org-association
				 */
				pstmtNew.setInt(1, user.getRoleId());
				pstmtNew.setInt(2, Constants.USER_ID);
				pstmtNew.setInt(3, user.getUserId());
				pstmtNew.addBatch();
				countNew++;
				if (countNew % BATCH_SIZE_LARGE == 0) {
					pstmtNew.executeBatch();
					logger.info("User role-Unique Role row count" + countNew);
				}
			}
			pstmt.executeBatch();
			pstmtNew.executeBatch();
		} catch (Exception e) {
			logger.error("Exception in updateUserRole" + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
			SQLUtil.closeDbObjects(connNew, pstmtNew, null);
			logger.info("done executing updateUserRole()");
		}
	}

	public void getExistOrgData(Integer customerId,
			OrgMDRDBCacheImpl dbCacheOrgImpl) throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		String queryString = " select org_node_mdr_number as org_node_mdr_number from org_node where  activation_status = 'AC' and org_node_mdr_number is not null ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				String orgNodeMdrNumber = rSet.getString("org_node_mdr_number");
				dbCacheOrgImpl
						.addOrgFileRow(orgNodeMdrNumber.trim(), orgNodeMdrNumber);
			}

		} catch (Exception e) {
			logger.error("Exception in getExistOrgData " + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

	}

}
