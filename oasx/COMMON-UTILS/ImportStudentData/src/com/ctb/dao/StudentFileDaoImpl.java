package com.ctb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ctb.bean.CustomerConfig;
import com.ctb.bean.CustomerConfigurationValue;
import com.ctb.bean.CustomerDemographicValue;
import com.ctb.bean.Node;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.bean.StudentFileRow;
import com.ctb.utils.Configuration;
import com.ctb.utils.Constants;
import com.ctb.utils.SQLUtil;
import com.ctb.utils.cache.OrgMDRDBCacheImpl;
import com.ctb.utils.cache.StudentDBCacheImpl;

/**
 * Implementation class for StudentFileDao Interface
 * @author TCS
 */
public class StudentFileDaoImpl implements StudentFileDao {

	private static Logger logger = Logger.getLogger(StudentFileDaoImpl.class
			.getName());

	/**
	 * Returns CustomerConfigurationValues for GRADE customerConfiguration of a
	 * Particular Customer
	 * 
	 * @param customerId
	 * @return CustomerConfigurationValue
	 * @throws Exception
	 */
	public CustomerConfigurationValue[] getCustomerConfigurationValuesForGrades(
			int customerId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<CustomerConfigurationValue> customerConfigurationValueList = new ArrayList<CustomerConfigurationValue>();
		String queryString = "select distinct customer_configuration_value as customerConfigurationValue,  customer_configuration_id as customerConfigurationId, sort_order as sortOrder from customer_configuration_value where customer_configuration_id in ( select customer_configuration_id from customer_configuration where customer_id = ? and upper(customer_configuration_name) = upper( ? )) order by sort_order, customer_configuration_value";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			pstmt.setString(2, "Grade");
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				CustomerConfigurationValue customerConfigurationValue = new CustomerConfigurationValue();
				customerConfigurationValue.setCustomerConfigurationId(rSet
						.getInt("customerConfigurationId"));
				customerConfigurationValue.setCustomerConfigurationValue(rSet
						.getString("customerConfigurationValue"));
				customerConfigurationValue.setSortOrder(rSet
						.getInt("sortOrder"));
				customerConfigurationValueList.add(customerConfigurationValue);
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in getCustomerConfigurationValuesForGrades-- >"
					+ e.getErrorCode());
		} catch (Exception e) {
			logger.error("Exception in getCustomerConfigurationValuesForGrades"
					+ e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

		return customerConfigurationValueList
				.toArray(new CustomerConfigurationValue[customerConfigurationValueList
						.size()]);
	}

	/**
	 * Used for Collecting the Set of StudentDemoGraphics for a Customer
	 * 
	 * @param customerId
	 * @return StudentDemoGraphics[]
	 * @throws Exception
	 */
	public StudentDemoGraphics[] getStudentDemoGraphics(Integer customerId)
			throws Exception {
		String definedDemos = ("".equals(Configuration.getDemographics()))?null:Configuration.getDemographics();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<StudentDemoGraphics> studentDemographicsList = new ArrayList<StudentDemoGraphics>();
		String queryString = "select  cusDemo.Customer_Demographic_Id as customerDemographicId,  cusDemo.Customer_Id  as customerId,  cusDemo.Label_Name  as labelName,  cusDemo.Label_Code  as labelCode,  cusDemo.Value_Cardinality  as valueCardinality,  cusDemo.Sort_Order  as sortOrder,  cusDemo.Import_Editable  as importEditable,  cusDemo.Visible  as visible,  cusDemo.Created_Date_Time  as createdDateTime  from customer_demographic cusDemo  where cusDemo.customer_id = ?";
		
		if(definedDemos != null){
			String[] demoArr = definedDemos.split(Constants.DEMOGRAPHIC_VALUSE_SEPARATOR);
			queryString = getManualDemographicString(demoArr, queryString);
		}
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				StudentDemoGraphics studentDemoGraphics = new StudentDemoGraphics();
				studentDemoGraphics.setCustomerDemographicId(rSet
						.getInt("customerDemographicId"));
				studentDemoGraphics.setCustomerId(rSet.getInt("customerId"));
				studentDemoGraphics.setLabelName(rSet.getString("labelName"));
				studentDemoGraphics.setLabelCode(rSet.getString("labelCode"));
				studentDemoGraphics.setValueCardinality(rSet
						.getString("valueCardinality"));
				studentDemoGraphics.setSortOrder(rSet.getInt("sortOrder"));
				studentDemoGraphics.setImportEditable(rSet
						.getString("importEditable"));
				studentDemoGraphics.setVisible(rSet.getString("visible"));
				studentDemoGraphics.setCreatedDateTime(rSet
						.getDate("createdDateTime"));
				studentDemographicsList.add(studentDemoGraphics);
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in getStudentDemoGraphics-- >"
					+ e.getErrorCode());
		} catch (Exception e) {
			logger.error("Exception in getStudentDemoGraphics" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

		return studentDemographicsList
				.toArray(new StudentDemoGraphics[studentDemographicsList.size()]);
	}
	
	/**
	 * Returns manual customer demographic name query string
	 * 
	 * @param demoArr
	 * @param queryString
	 * @return queryString
	 * @throws Exception
	 */
	private String getManualDemographicString(String[] demoArr, String queryString) throws Exception{
		boolean isFistValue = true;
		for(String demo : demoArr){
			if(isFistValue){
				queryString += "and cusDemo.Label_Name in ('"+demo.trim()+"'";
				isFistValue = false;
			}else{
				queryString += ",'"+demo.trim()+"'";
			}
		}
		queryString += ")";
		
		return queryString;
	}

	/**
	 * This method will return the CustomerDemographicValue for a particular
	 * CustomerDemographicId
	 * 
	 * @param customerDemographicId
	 * @return CustomerDemographicValue[]
	 * @throws Exception
	 */
	public CustomerDemographicValue[] getCustomerDemographicValue(
			Integer customerDemographicId) throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<CustomerDemographicValue> customerDemographicsList = new ArrayList<CustomerDemographicValue>();
		String queryString = "SELECT  cdv.customer_demographic_id as customerDemographicId,  cdv.value_name  as valueName,  cdv.value_code  as valueCode,  cdv.sort_order  as sortOrder,  cdv.visible  as visible,  cdv.created_by  as createdBy,  cdv.created_date_time  as createdDateTime  FROM customer_demographic_value cdv  WHERE cdv.customer_demographic_id= ?";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerDemographicId);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				CustomerDemographicValue customerDemographicValue = new CustomerDemographicValue();
				customerDemographicValue.setCustomerDemographicId(rSet
						.getInt("customerDemographicId"));
				customerDemographicValue.setValueName(rSet
						.getString("valueName"));
				customerDemographicValue.setValueCode(rSet
						.getString("valueCode"));
				customerDemographicValue.setSortOrder(rSet.getInt("sortOrder"));
				customerDemographicValue.setVisible(rSet.getString("visible"));
				customerDemographicValue.setCreatedBy(rSet.getInt("createdBy"));
				customerDemographicValue.setCreatedDateTime(rSet
						.getDate("createdDateTime"));
				customerDemographicsList.add(customerDemographicValue);
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in getCustomerDemographicValue-- >"
					+ e.getErrorCode());
		} catch (Exception e) {
			logger.error("Exception in getCustomerDemographicValue"
					+ e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

		return customerDemographicsList
				.toArray(new CustomerDemographicValue[customerDemographicsList
						.size()]);

	}

	/**
	 * This method will return the CustomerConfiguration of particular set of
	 * Accommodation for a customer.
	 * 
	 * @param customerId
	 * @return CustomerConfig[]
	 * @throws Exception
	 */
	public CustomerConfig[] getCustomerConfigurationForAccommodation(
			Integer customerId) throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<CustomerConfig> customerConfigList = new ArrayList<CustomerConfig>();
		String queryString = "select  cc.customer_configuration_id  as customerConfigurationId,  cc.customer_configuration_name as customerConfigurationName,  cc.editable  as editable,  cc.default_value  as defaultValue,  cc.created_by  as createdBy,  cc.created_date_time  as createdDateTime  from customer_configuration cc  where cc.customer_configuration_name in ('Screen_Reader'  ,'Calculator','Test_Pause'  ,'Untimed_Test','Highlighter')  and cc.customer_id = ?";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				CustomerConfig customerConfig = new CustomerConfig();
				customerConfig.setCustomerConfigurationId(rSet
						.getInt("customerConfigurationId"));
				customerConfig.setCustomerConfigurationName(rSet
						.getString("customerConfigurationName"));
				customerConfig.setEditable(rSet.getString("editable"));
				customerConfig.setDefaultValue(rSet.getString("defaultValue"));
				customerConfig.setCreatedBy(rSet.getInt("createdBy"));
				customerConfig.setCreatedDateTime(rSet
						.getDate("createdDateTime"));
				customerConfigList.add(customerConfig);
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in getCustomerConfigurationForAccommodation-- >"
					+ e.getErrorCode());
		} catch (Exception e) {
			logger.error("Exception in getCustomerConfigurationForAccommodation"
					+ e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

		return customerConfigList.toArray(new CustomerConfig[customerConfigList
				.size()]);

	}

	/**
	 * This method will check if a particular customerConfiguration is present
	 * or not for a customer
	 * 
	 * @param customerId
	 * @param customerConfigurationName
	 * @return boolean
	 * @throws Exception
	 */
	public boolean checkCustomerConfigurationEntries(Integer customerId,
			String customerConfigurationName) throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		boolean exist = false;
		String queryString = "select  decode( count(cc.customer_id),0,0,1) as exist  from customer_configuration cc  where cc.customer_id = ?  and cc.customer_configuration_name = ? and cc.default_value='T'";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			pstmt.setString(2, customerConfigurationName);
			rSet = pstmt.executeQuery();
			if (rSet.next()) {
				exist = rSet.getInt("exist") == 1 ? true : false;
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in checkCustomerConfigurationEntries-- >"
					+ e.getErrorCode());
		} catch (Exception e) {
			logger.error("Exception in checkCustomerConfigurationEntries"
					+ e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return exist;
	}

	/**
	 * This method will return the default value of a customerConfiguration for
	 * a customer
	 * 
	 * @param customerId
	 * @param customerConfigurationName
	 * @return String
	 * @throws Exception
	 */
	public String checkCustomerConfiguration(Integer customerId,
			String customerConfigurationName) throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		String ccDefaultValue = null;
		String queryString = "select cc.default_value from customer_configuration cc  where cc.customer_id = ?  and cc.customer_configuration_name = ?";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			pstmt.setString(2, customerConfigurationName);
			rSet = pstmt.executeQuery();
			if (rSet.next()) {
				ccDefaultValue = rSet.getString("default_value");
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in checkCustomerConfiguration-- >"
					+ e.getErrorCode());
		} catch (Exception e) {
			logger.error("Exception in checkCustomerConfiguration --> "
					+ e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return ccDefaultValue;
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
		} catch (SQLException e) {
			logger.error("SQL Exception in getUserDataTemplate-- >"
					+ e.getErrorCode());
			logger.error("Exception in getUserDataTemplate" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getUserDataTemplate" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return nodeList.toArray(new Node[nodeList.size()]);
	}

	/**
	 * This method populates Cache with all existing student data in Database
	 * 
	 * @param customerId
	 * @param dbCache
	 * @throws Exception
	 */
	public void getExistStudentData(Integer customerId,
			StudentDBCacheImpl dbCache) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		String queryString = " select distinct stu.student_id  as studentId, stu.user_Name   as userName , stu.ext_Pin1    as extPin1 , stu.ext_Pin2   as extPin2 from student stu , org_node_student ons , org_node org where stu.activation_status = 'AC' and stu.customer_id = org.customer_id and org.org_node_id = ons.org_node_id and ons.student_id = stu.student_id and org.customer_id = ? and org.activation_status = 'AC' and ons.activation_status = 'AC'  ";

		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				StudentFileRow studentFileRow = new StudentFileRow();

				studentFileRow.setStudentId(rSet.getInt("studentId"));
				studentFileRow.setUserName(rSet.getString("userName"));
				studentFileRow.setExtPin1(rSet.getString("extPin1"));
				studentFileRow.setExtPin2(rSet.getString("extPin2"));
				studentFileRow.setCustomerId(customerId);
				studentFileRow.setKey(studentFileRow.getExtPin1());
				dbCache.addStudentFileRow(studentFileRow.getKey(),
						studentFileRow);
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in getCustomerConfigurationForAccommodation-- >"
					+ e.getErrorCode());
			logger.error("Exception in getCustomerConfigurationForAccommodation "
					+ e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getCustomerConfigurationForAccommodation "
					+ e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
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
		} catch (SQLException e) {
			logger.error("SQL Exception in getTopNodeDetails-- >"
					+ e.getErrorCode());
		} catch (Exception e) {
			logger.error("Exception in getTopNodeDetails" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return nodeList.toArray(new Node[nodeList.size()]);
	}

	/**
	 * This method checks if the mdrNumber is Unique or not
	 * 
	 * @param selectedMdrNumber
	 * @return String
	 * @throws Exception
	 */
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
		} catch (SQLException e) {
			logger.error("SQL Exception in checkUniqueMdrNumberForOrgNodes-- >"
					+ e.getErrorCode());
			logger.error("Exception in checkUniqueMdrNumberForOrgNodes"
					+ e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in checkUniqueMdrNumberForOrgNodes"
					+ e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return uniqueNumber;
	}

	/**
	 * This method checks if the StudentId is unique or not for a Customer.
	 * 
	 * @param studentId
	 * @param customerId
	 * @return String
	 * @throws Exception
	 */
	public String checkUniqueStudentId(String studentId, Integer customerId)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		String uniqueStudentId = null;
		String queryString = "SELECT DECODE(COUNT(1), 0, 'T', 'F') AS UNIQUESTUDENTID  FROM STUDENT STU WHERE trim(STU.EXT_PIN1) = ? AND STU.CUSTOMER_ID = ? AND STU.ACTIVATION_STATUS = 'AC'";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setString(1, studentId);
			pstmt.setInt(1, customerId);
			rSet = pstmt.executeQuery();
			if (rSet.next()) {
				uniqueStudentId = rSet.getString("UNIQUESTUDENTID");
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in checkUniqueStudentId-- >"
					+ e.getErrorCode());
			logger.error("Exception in checkUniqueStudentId" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in checkUniqueStudentId" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return uniqueStudentId;
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
			pstmt.setFetchSize(1000);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				String orgNodeMdrNumber = rSet.getString("org_node_mdr_number");
				dbCacheOrgImpl.addOrgFileRow(orgNodeMdrNumber.trim(),
						orgNodeMdrNumber);
			}

		} catch (Exception e) {
			logger.error("Exception in getExistOrgData " + e.getMessage());
			throw e;
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

	}

}
