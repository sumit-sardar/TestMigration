package com.ctb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ctb.bean.CustomerConfiguration;
import com.ctb.bean.CustomerConfigurationValue;
import com.ctb.bean.DataFileAudit;
import com.ctb.bean.DataFileTemp;
import com.ctb.bean.OrgNodeCategory;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.utils.Configuration;
import com.ctb.utils.Constants;
import com.ctb.utils.SQLUtil;

/**
 * This Class implements the UploadFileDao Interface
 * 
 */
public class UploadFileDaoImpl  implements UploadFileDao {
	private static Logger logger = Logger.getLogger(UploadFileDaoImpl.class
			.getName());

	/**
	 * Generates the Id from Sequence generator.
	 * 
	 * @return Integer
	 * @throws Exception
	 */
	public Integer getNextPKForTempFile() throws Exception {
		Integer uploadDataFileId = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		String queryString = " select  SEQ_DATA_FILE_AUDIT_ID.NEXTVAL as PKKey  from  dual ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				uploadDataFileId = rSet.getInt("PKKey");
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in getNextPKForTempFile-- >"
					+ e.getErrorCode());
			logger.error("Exception in getNextPKForTempFile" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getNextPKForTempFile" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
		}
		return (null != uploadDataFileId) ? uploadDataFileId : 0;
	}

	/**
	 * Inserts data in DATA_FILE_TEMP table
	 * 
	 * @param tempFile
	 * @throws Exception
	 */
	public void createDataFileTemp(DataFileTemp tempFile) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String queryString = "insert into DATA_FILE_TEMP  ( DATA_FILE_AUDIT_ID, DATA_FILE )  values ( ?, ? )";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, tempFile.getDataFileAuditId());
			pstmt.setBytes(2, tempFile.getDataFile());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("SQL Exception in createDataFileTemp-- >"
					+ e.getErrorCode());
			logger.error("Exception in createDataFileTemp" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in createDataFileTemp" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
	}

	/**
	 * Checks if a Configuration is present or not for a customer
	 * 
	 * @param customerId
	 * @param configuration
	 * @return
	 * @throws Exception
	 */
	public boolean checkCustomerConfiguration(Integer customerId,
			String configuration) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		boolean isPresent = false;
		String queryString = " select  decode( count(cc.customer_id),0,0,1) as configPresntCount  from customer_configuration cc  where cc.customer_id = ?  and cc.customer_configuration_name = ? and cc.default_value='T' ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			pstmt.setString(2, configuration);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				isPresent = rSet.getInt("configPresntCount") != 0 ? true
						: false;
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in getNextPKForTempFile-- >"
					+ e.getErrorCode());
			logger.error("Exception in getNextPKForTempFile" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getNextPKForTempFile" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
		}
		return isPresent;
	}

	/**
	 * Returns the OrgNodeCategories of a Customer
	 * 
	 * @param customerId
	 * @return OrgNodeCategory[]
	 * @throws Exception
	 */
	public OrgNodeCategory[] getOrgNodeCategories(Integer customerId)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<OrgNodeCategory> orgNodeCategoryList = new ArrayList<OrgNodeCategory>();
		String queryString = " select orgNodeCat.org_node_category_id as orgNodeCategoryId,  orgNodeCat.customer_id as customerId,  orgNodeCat.is_group as isGroup,  orgNodeCat.category_name as categoryName,  orgNodeCat.category_level as categoryLevel,  orgNodeCat.activation_status as activationStatus,  orgNodeCat.created_by as createdBy,  orgNodeCat.created_date_time as createdDateTime,  orgNodeCat.updated_by as updatedBy,  orgNodeCat.updated_date_time as updatedDateTime  from org_node_category orgNodeCat  where orgNodeCat.customer_id = ?  and orgNodeCat.Activation_Status = 'AC'  and orgNodeCat.category_level > 0  order by orgNodeCat.category_level ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				OrgNodeCategory orgNodeCategory = new OrgNodeCategory();
				orgNodeCategory.setOrgNodeCategoryId(rSet
						.getInt("orgNodeCategoryId"));
				orgNodeCategory.setCustomerId(rSet.getInt("customerId"));
				orgNodeCategory.setIsGroup(rSet.getString("isGroup"));
				orgNodeCategory.setCategoryName(rSet.getString("categoryName"));
				orgNodeCategory.setCategoryLevel(rSet.getInt("categoryLevel"));
				orgNodeCategory.setActivationStatus(rSet
						.getString("activationStatus"));
				orgNodeCategory.setCreatedBy(rSet.getInt("createdBy"));
				orgNodeCategory.setCreatedDateTime(rSet
						.getDate("createdDateTime"));
				orgNodeCategory.setUpdatedBy(rSet.getInt("updatedBy"));
				orgNodeCategory.setUpdatedDateTime(rSet
						.getDate("updatedDateTime"));
				orgNodeCategoryList.add(orgNodeCategory);
			}

		} catch (SQLException e) {
			logger.error("SQL Exception in getOrgNodeCategories-- >"
					+ e.getErrorCode());
			logger.error("Exception in getOrgNodeCategories" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getOrgNodeCategories" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
		}
		return orgNodeCategoryList
				.toArray(new OrgNodeCategory[orgNodeCategoryList.size()]);
	}

	/**
	 * Returns the Demographics for a customer
	 * 
	 * @param customerId
	 * @return StudentDemoGraphics[]
	 * @throws Exception
	 */
	public StudentDemoGraphics[] getStudentDemoGraphics(Integer customerId)
			throws Exception {
		String definedDemos = ("".equals(Configuration.getDemographics()))?null:Configuration.getDemographics();
		int totalDemosGiven = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<StudentDemoGraphics> studentDemographicsList = new ArrayList<StudentDemoGraphics>();
		String queryString = " select  cusDemo.Customer_Demographic_Id as customerDemographicId,  cusDemo.Customer_Id  as customerId,  cusDemo.Label_Name  as labelName,  cusDemo.Label_Code  as labelCode,  cusDemo.Value_Cardinality  as valueCardinality,  cusDemo.Sort_Order  as sortOrder,  cusDemo.Import_Editable  as importEditable,  cusDemo.Visible  as visible,  cusDemo.Created_Date_Time  as createdDateTime  from customer_demographic cusDemo  where cusDemo.customer_id = ? ";
		
		/**
		 * Changes for user story : OAS-636 & OAS-637
		 */
		if(definedDemos != null){
			String[] demoArr = definedDemos.split(Constants.DEMOGRAPHIC_VALUSE_SEPARATOR);
			totalDemosGiven = demoArr.length; 
			queryString = getManualDemographicString(demoArr, queryString);
		}
		
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			rSet = pstmt.executeQuery();
			int rSetCount =0;
			while (rSet.next()) {
				StudentDemoGraphics studentDemoGraphics = new StudentDemoGraphics();
				studentDemoGraphics.setCustomerDemographicId(rSet
						.getInt("customerDemographicId"));
				studentDemoGraphics.setCustomerId(rSet.getInt("customerId"));
				studentDemoGraphics.setLabelName(rSet.getString("labelName"));
				studentDemoGraphics.setValueCardinality(rSet
						.getString("valueCardinality"));
				studentDemoGraphics.setSortOrder(rSet.getInt("sortOrder"));
				studentDemoGraphics.setImportEditable(rSet
						.getString("importEditable"));
				studentDemoGraphics.setVisible(rSet.getString("visible"));
				studentDemoGraphics.setCreatedDateTime(rSet
						.getDate("createdDateTime"));
				studentDemographicsList.add(studentDemoGraphics);
				rSetCount++;
			}
			
			if(definedDemos != null && rSetCount != totalDemosGiven){
				throw new Exception(" :: Given manual demographic names are not match with the database...");
			}
			
		} catch (SQLException e) {
			logger.error("SQL Exception in getStudentDemoGraphics-- >"
					+ e.getErrorCode());
			logger.error("Exception in getStudentDemoGraphics" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getStudentDemoGraphics" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
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
	 * Returns CustomerConfigurations for a customer
	 * 
	 * @param customerId
	 * @return CustomerConfiguration[]
	 * @throws Exception
	 */
	public CustomerConfiguration[] getCustomerConfigurations(Integer customerId)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<CustomerConfiguration> customerConfigurationList = new ArrayList<CustomerConfiguration>();
		String queryString = " select  customer_configuration_id as id,  customer_configuration_name as customerConfigurationName,  customer_id as customerId,  editable as editable,  default_value as defaultValue from customer_configuration where customer_id = ? ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, customerId);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				CustomerConfiguration customerConfiguration = new CustomerConfiguration();
				customerConfiguration.setId(rSet.getInt("id"));
				customerConfiguration.setCustomerConfigurationName(rSet
						.getString("customerConfigurationName"));
				customerConfiguration.setCustomerId(customerId);
				customerConfiguration.setEditable(rSet.getString("editable"));
				customerConfiguration.setDefaultValue(rSet
						.getString("defaultValue"));
				customerConfigurationList.add(customerConfiguration);
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in getCustomerConfigurations-- >"
					+ e.getErrorCode());
		} catch (Exception e) {
			logger.error("Exception in getCustomerConfigurations"
					+ e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
		}

		return customerConfigurationList
				.toArray(new CustomerConfiguration[customerConfigurationList
						.size()]);

	}

	/**
	 * Returns CustomerConfigurationValue for a customerConfiguration
	 * 
	 * @param configId
	 * @return CustomerConfigurationValue[]
	 * @throws Exception
	 */
	public CustomerConfigurationValue[] getCustomerConfigurationsValue(
			Integer configId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		ArrayList<CustomerConfigurationValue> customerConfigurationValueList = new ArrayList<CustomerConfigurationValue>();
		String queryString = " select  customer_configuration_value as customerConfigurationValue,  customer_configuration_id as customerConfigurationId,  sort_order as sortOrder from customer_configuration_value where customer_configuration_id = ? order by sort_order, customer_configuration_value ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, configId);
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
			logger.error("SQL Exception in getCustomerConfigurationsValue-- >"
					+ e.getErrorCode());
		} catch (Exception e) {
			logger.error("Exception in getCustomerConfigurationsValue"
					+ e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, rSet);
		}

		return customerConfigurationValueList
				.toArray(new CustomerConfigurationValue[customerConfigurationValueList
						.size()]);
	}

	/**
	 * Inserts data in DATA_FILE_AUDIT table
	 * 
	 * @param dataFileAudit
	 * @throws Exception
	 */
	public void createDataFileAudit(DataFileAudit dataFileAudit)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String queryString = " insert into DATA_FILE_AUDIT  ( DATA_FILE_AUDIT_ID, USER_ID, CUSTOMER_ID, UPLOAD_FILE_NAME,  CREATED_BY, CREATED_DATE_TIME, STATUS )  values ( ?,?, ?, ?, ?, sysdate , ? ) ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setInt(1, dataFileAudit.getDataFileAuditId());
			pstmt.setInt(2, Constants.USER_ID);
			pstmt.setInt(3, dataFileAudit.getCustomerId());
			pstmt.setString(4, dataFileAudit.getUploadFileName());
			pstmt.setInt(5, Constants.USER_ID);
			pstmt.setString(6, dataFileAudit.getStatus());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			logger.error("SQL Exception in createDataFileAudit-- >"
					+ e.getErrorCode());
			logger.error("Exception in createDataFileAudit" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in createDataFileAudit" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}

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
	 * Update data in DATA_FILE_AUDIT table
	 * 
	 * @param dataFileAudit
	 * @throws Exception
	 */
	public void upDateAuditTable(DataFileAudit dataFileAudit) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String queryString = "update DATA_FILE_AUDIT set error_file = ?, FAILED_RECORD_COUNT = ?, UPLOADED_RECORD_COUNT = ?, STATUS = ? where DATA_FILE_AUDIT_ID = ?";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(queryString);
			pstmt.setBytes(1, dataFileAudit.getFaildRec());
			pstmt.setInt(2, dataFileAudit.getFailedRecordCount());
			pstmt.setInt(3, dataFileAudit.getUploadFileRecordCount());
			pstmt.setString(4, dataFileAudit.getStatus());
			pstmt.setInt(5, dataFileAudit.getDataFileAuditId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("SQL Exception in upDateAuditTable-- >"
					+ e.getErrorCode());
			logger.error("Exception in upDateAuditTable" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in upDateAuditTable" + e.getMessage());
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
		}
	}

}
