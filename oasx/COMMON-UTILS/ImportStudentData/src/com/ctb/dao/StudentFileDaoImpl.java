package com.ctb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ctb.bean.CustomerConfig;
import com.ctb.bean.CustomerConfigurationValue;
import com.ctb.bean.CustomerDemographicValue;
import com.ctb.bean.Node;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.bean.StudentFileRow;
import com.ctb.utils.SQLUtil;

public class StudentFileDaoImpl implements StudentFileDao {

	private static Logger logger = Logger.getLogger(StudentFileDaoImpl.class
			.getName());

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
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in getCustomerConfigurationValuesForGrades");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

		return customerConfigurationValueList
				.toArray(new CustomerConfigurationValue[customerConfigurationValueList
						.size()]);
	}

	public StudentDemoGraphics[] getStudentDemoGraphics(Integer customerId)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<StudentDemoGraphics> studentDemographicsList = new ArrayList<StudentDemoGraphics>();
		String queryString = "select  cusDemo.Customer_Demographic_Id as customerDemographicId,  cusDemo.Customer_Id  as customerId,  cusDemo.Label_Name  as labelName,  cusDemo.Label_Code  as labelCode,  cusDemo.Value_Cardinality  as valueCardinality,  cusDemo.Sort_Order  as sortOrder,  cusDemo.Import_Editable  as importEditable,  cusDemo.Visible  as visible,  cusDemo.Created_Date_Time  as createdDateTime  from customer_demographic cusDemo  where cusDemo.customer_id = ?";
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
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in getStudentDemoGraphics");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

		return studentDemographicsList
				.toArray(new StudentDemoGraphics[studentDemographicsList.size()]);
	}

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
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in getCustomerDemographicValue");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

		return customerDemographicsList
				.toArray(new CustomerDemographicValue[customerDemographicsList
						.size()]);

	}

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
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in getCustomerConfigurationForAccommodation");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

		return customerConfigList.toArray(new CustomerConfig[customerConfigList
				.size()]);

	}

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
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in checkCustomerConfigurationEntries");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return exist;
	}

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
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in checkCustomerConfiguration");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return ccDefaultValue;
	}

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
			logger.error("SQL Exception in getCustomerConfigurationForAccommodation-- >"
					+ e.getErrorCode());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in getCustomerConfigurationForAccommodation");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return nodeList.toArray(new Node[nodeList.size()]);
	}

	public StudentFileRow[] getExistStudentData(Integer customerId)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<StudentFileRow> studentFileRowList = new ArrayList<StudentFileRow>();
		String queryString = " select distinct stu.student_id  as studentId, stu.user_Name   as userName, stu.first_Name  as firstName, stu.middle_Name as middleName, stu.last_Name   as lastName, stu.birthdate   as birthdate, stu.gender      as gender, stu.grade       as grade, stu.ext_Elm_Id  as extElmId, stu.ext_Pin1    as extPin1, stu.ext_Pin2    as extPin2 from student stu , org_node_student ons , org_node org where stu.activation_status = 'AC' and stu.customer_id = org.customer_id and org.org_node_id = ons.org_node_id and ons.student_id = stu.student_id and org.customer_id = ? and org.activation_status = 'AC' and ons.activation_status = 'AC'  ";

		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				StudentFileRow studentFileRow = new StudentFileRow();

				studentFileRow.setStudentId(rSet.getInt("studentId"));
				studentFileRow.setUserName(rSet.getString("userName"));
				studentFileRow.setFirstName(rSet.getString("firstName"));
				studentFileRow.setMiddleName(rSet.getString("middleName"));
				studentFileRow.setLastName(rSet.getString("lastName"));
				studentFileRow.setBirthdate(rSet.getDate("birthdate"));
				studentFileRow.setGender(rSet.getString("gender"));
				studentFileRow.setGrade(rSet.getString("grade"));
				studentFileRow.setExtElmId(rSet.getString("extElmId"));
				studentFileRow.setExtPin1(rSet.getString("extPin1"));
				studentFileRow.setExtPin2(rSet.getString("extPin2"));
				studentFileRow.setKey((studentFileRow.getExtPin1()!= null )? studentFileRow.getExtPin1().trim() : generateKey(studentFileRow) );
				studentFileRowList.add(studentFileRow);
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in getCustomerConfigurationForAccommodation-- >"
					+ e.getErrorCode());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in getCustomerConfigurationForAccommodation");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return studentFileRowList.toArray(new StudentFileRow[studentFileRowList
				.size()]);
	}

	private String generateKey(StudentFileRow studentFileRow) {

		String middleName = "";
		if(null!=studentFileRow.getMiddleName())
			middleName = studentFileRow.getMiddleName().toUpperCase();
		String key = studentFileRow.getFirstName().toUpperCase()
				+ middleName
				+ studentFileRow.getLastName().toUpperCase()
				+ studentFileRow.getGender();

		String datefromDB = "";
		Date dbDate = studentFileRow.getBirthdate();
		if (dbDate != null && !dbDate.equals("")) {
			// Update Student
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			datefromDB = sdf.format(dbDate);
		}
		return key + datefromDB;
	}

	public Node[] getTopNodeDetails(Integer customerId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<Node> nodeList = new ArrayList<Node>();
		String queryString = "select distinct org.org_node_id as orgnodeid, org.customer_id as customerid, org.org_node_category_id as orgnodecategoryid, org.org_node_name as orgnodename, org.ext_qed_pin as extqedpin, org.ext_elm_id as extelmid, org.ext_org_node_type as extorgnodetype, org.org_node_description as orgnodedescription, org.created_by as createdby, org.created_date_time as createddatetime, org.updated_by as updatedby, org.updated_date_time as updateddatetime, org.activation_status as activationstatus, org.data_import_history_id as dataimporthistoryid, org.parent_state as parentstate, org.parent_region as parentregion, org.parent_county as parentcounty, org.parent_district as parentdistrict, org.org_node_code as orgnodecode, org.org_node_mdr_number as mdrnumber, onc.category_name as orgnodecategoryname, count(distinct onp.org_node_id) as childnodecount from org_node org, org_node_category onc, org_node_parent onp where org.customer_id = onc.customer_id and onc.org_node_category_id = org.org_node_category_id and onp.parent_org_node_id = org.org_node_id and onc.customer_id = ? and onc.activation_status = 'AC' and org.activation_status = 'AC' and onc.category_level = (select min(onc1.category_level) from org_node_category onc1 where onc1.customer_id = ? and onc1.is_group = 'F' and onc1.activation_status = 'AC') group by org.org_node_id, org.customer_id, org.org_node_category_id, org.org_node_name, org.ext_qed_pin, org.ext_elm_id, org.ext_org_node_type, org.org_node_description, org.created_by, org.created_date_time, org.updated_by, org.updated_date_time, org.activation_status, org.data_import_history_id, org.parent_state, org.parent_region, org.parent_county, org.parent_district, org.org_node_code, org.org_node_mdr_number, onc.category_name";
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
				node.setExtQedPin(rSet.getString("extqedpin"));
				node.setExtElmId(rSet.getString("extelmid"));
				node.setExtOrgNodeType(rSet.getString("extorgnodetype"));
				node.setOrgNodeDescription(rSet.getString("orgnodedescription"));
				node.setCreatedBy(rSet.getInt("createdby"));
				node.setCreatedDateTime(rSet.getDate("createddatetime"));
				node.setUpdatedBy(rSet.getInt("updatedby"));
				node.setUpdatedDateTime(rSet.getDate("updateddatetime"));
				node.setActivationStatus(rSet.getString("activationstatus"));
				node.setDataImportHistoryId(rSet.getInt("dataimporthistoryid"));
				node.setParentState(rSet.getString("parentstate"));
				node.setParentRegion(rSet.getString("parentregion"));
				node.setParentCounty(rSet.getString("parentcounty"));
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
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in getTopNodeDetails");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return nodeList.toArray(new Node[nodeList.size()]);
	}

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
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in checkUniqueMdrNumberForOrgNodes");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return uniqueNumber;
	}

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
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in checkUniqueStudentId");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
		return uniqueStudentId;
	}

}
