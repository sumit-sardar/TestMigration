package com.ctb.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ctb.bean.LicenseMergedReportData;




/**
 * This class provides data access functions for LM Merged Report Data export
 * 
 * @author TCS
 * 
 */
public class DBUtil {	
	static Logger logger = Logger.getLogger(DBUtil.class.getName());
	
	private static String sqlStr1 = "SELECT    SF_CUSTOMER_ID, "+
										    "OAS_CUSTOMER_ID, "+
										    "OAS_CONTACT_NAME, "+
										    "OAS_CONTACT_PHONE, "+
										    "OAS_CONTACT_EMAIL, "+
										    "OAS_STATE_PR_DESC, "+
										    "OAS_STATE_PR, "+
										    "OAS_EXT_CUSTOMER_ID, "+
										    "OAS_PRODUCT_NAME, "+
										    "SF_CONTACT, "+
										    "SF_CONTACT_PHONE, "+
										    "SF_CONTACT_EMAIL, "+
										    "SF_CUSTOMER_ACCOUNT_NAME, "+
										    "SF_ACCOUNT_STATE, "+
										    "SF_ORG_NODE_ID, "+
										    "SF_ORG_NODE_NAME, "+
										    "SF_CATEGORY_NAME, "+
										    "SF_CATEGORY_LEVEL, "+
										    "OAS_ORG_NODE_ID, "+
										    "OAS_ORG_NODE_NAME, "+
										    "OAS_ORG_NODE_CATEGORY_NAME, "+
										    "OAS_ORG_NODE_CATEGORY_LEVEL, "+
										    "OAS_PARENT_ORG_NODE_ID, "+
										    "OAS_PARENT_ORG_NODE_NAME, "+
										    "SF_LICENSE_MODEL, "+
										    "DECODE(OAS_SUBTEST_MODEL, 'T','Sub-Tests','F', 'Full') AS OAS_LICENSE_MODEL, "+
										    "SF_LICENSE_COUNT, "+
										    "SF_ORDER_QUANTITY, "+
										    "SF_LICENSE_DISTRIBUTED_TO, "+
										    "TO_CHAR(SF_CREATED_DATE, 'MM-DD-YYYY HH24:MI:SS') AS SF_CREATED_DATE, "+
										    "SF_INTERVAL_NAME, "+
										    "TO_CHAR(SF_LOAD_DATE_TIME, 'MM-DD-YYYY HH24:MI:SS') AS SF_LOAD_DATE_TIME, "+
										    "SF_LOAD_SEQ_NUM, "+
										    "OAS_LIC_AVL_IN_DB, "+
										    "OAS_CUM_LIC_AVL_IN_DB, "+
										    "OAS_LIC_TO_BE_RELEASED, "+
										    "OAS_CUM_LIC_TO_BE_RELEASED, "+
										    "OAS_LIC_RESERVED_AT_NODE, "+
										    "OAS_CUM_LIC_RESERVED, "+
										    "OAS_LIC_CONSUMED_AT_NODE, "+
										    "OAS_CUM_LIC_CONSUMED, "+
										    "OAS_NODE_LVL_AVAILABLE, "+
										    "OAS_CUM_AVAILABLE, "+
										    "OAS_NODE_NET_AVAILABLE, "+
										    "OAS_CUM_NET_AVAILABLE, "+
										    "OAS_LIC_MOD_CONSUMED_AT_NODE, "+
										    "OAS_CUM_LIC_MOD_CONSUMED, "+
										    "OAS_LIC_MOD_RESERVED_AT_NODE, "+
										    "OAS_CUM_LIC_MOD_RESERVED, "+
										    "OAS_ACTIVATION_STATUS, "+
										    "TO_CHAR(OAS_EXTRACT_DATE_TIME, 'MM-DD-YYYY HH24:MI:SS')  AS OAS_EXTRACT_DATE_TIME, "+
										    "TO_CHAR(MERGED_DATE_TIME, 'MM-DD-YYYY HH24:MI:SS') AS MERGED_DATE_TIME, "+
										    "MERGED_SEQ_NUM, "+
										    "TO_CHAR(SYSDATE, 'MM-DD-YYYY HH24:MI:SS') AS LT_EXPORT_DATE_TIME, "+
										    "OAS_EXTRACT_SEQ_NUM "+
										"FROM LM_MERGED_REPORT_DATA "+
										"WHERE MERGED_SEQ_NUM = "+
										"(SELECT MAX(MERGED_SEQ_NUM) FROM LM_MERGED_REPORT_DATA) "+
										"ORDER BY OAS_CUSTOMER_ID, SF_CUSTOMER_ID, OAS_ORG_NODE_ID, SF_ORG_NODE_ID";
	
	private static String sqlStr2 = "SELECT    SF_CUSTOMER_ID, "+
											    "OAS_CUSTOMER_ID, "+
											    "OAS_CONTACT_NAME, "+
											    "OAS_CONTACT_PHONE, "+
											    "OAS_CONTACT_EMAIL, "+
											    "OAS_STATE_PR_DESC, "+
											    "OAS_STATE_PR, "+
											    "OAS_EXT_CUSTOMER_ID, "+
											    "OAS_PRODUCT_NAME, "+
											    "SF_CONTACT, "+
											    "SF_CONTACT_PHONE, "+
											    "SF_CONTACT_EMAIL, "+
											    "SF_CUSTOMER_ACCOUNT_NAME, "+
											    "SF_ACCOUNT_STATE, "+
											    "SF_ORG_NODE_ID, "+
											    "SF_ORG_NODE_NAME, "+
											    "SF_CATEGORY_NAME, "+
											    "SF_CATEGORY_LEVEL, "+
											    "OAS_ORG_NODE_ID, "+
											    "OAS_ORG_NODE_NAME, "+
											    "OAS_ORG_NODE_CATEGORY_NAME, "+
											    "OAS_ORG_NODE_CATEGORY_LEVEL, "+
											    "OAS_PARENT_ORG_NODE_ID, "+
											    "OAS_PARENT_ORG_NODE_NAME, "+
											    "SF_LICENSE_MODEL, "+
											    "DECODE(OAS_SUBTEST_MODEL, 'T','Sub-Tests','F', 'Full') AS OAS_LICENSE_MODEL, "+
											    "SF_LICENSE_COUNT, "+
											    "SF_ORDER_QUANTITY, "+
											    "SF_LICENSE_DISTRIBUTED_TO, "+
											    "TO_CHAR(SF_CREATED_DATE, 'MM-DD-YYYY HH24:MI:SS') AS SF_CREATED_DATE, "+
											    "SF_INTERVAL_NAME, "+
											    "TO_CHAR(SF_LOAD_DATE_TIME, 'MM-DD-YYYY HH24:MI:SS') AS SF_LOAD_DATE_TIME, "+
											    "SF_LOAD_SEQ_NUM, "+
											    "OAS_LIC_AVL_IN_DB, "+
											    "OAS_CUM_LIC_AVL_IN_DB, "+
											    "OAS_LIC_TO_BE_RELEASED, "+
											    "OAS_CUM_LIC_TO_BE_RELEASED, "+
											    "OAS_LIC_RESERVED_AT_NODE, "+
											    "OAS_CUM_LIC_RESERVED, "+
											    "OAS_LIC_CONSUMED_AT_NODE, "+
											    "OAS_CUM_LIC_CONSUMED, "+
											    "OAS_NODE_LVL_AVAILABLE, "+
											    "OAS_CUM_AVAILABLE, "+
											    "OAS_NODE_NET_AVAILABLE, "+
											    "OAS_CUM_NET_AVAILABLE, "+
											    "OAS_LIC_MOD_CONSUMED_AT_NODE, "+
											    "OAS_CUM_LIC_MOD_CONSUMED, "+
											    "OAS_LIC_MOD_RESERVED_AT_NODE, "+
											    "OAS_CUM_LIC_MOD_RESERVED, "+
											    "OAS_ACTIVATION_STATUS, "+
											    "TO_CHAR(OAS_EXTRACT_DATE_TIME, 'MM-DD-YYYY HH24:MI:SS')  AS OAS_EXTRACT_DATE_TIME, "+
											    "TO_CHAR(MERGED_DATE_TIME, 'MM-DD-YYYY HH24:MI:SS') AS MERGED_DATE_TIME, "+
											    "MERGED_SEQ_NUM, "+
											    "TO_CHAR(SYSDATE, 'MM-DD-YYYY HH24:MI:SS') AS LT_EXPORT_DATE_TIME, "+
											    "OAS_EXTRACT_SEQ_NUM "+
											"FROM LM_MERGED_REPORT_DATA "+
											"WHERE MERGED_SEQ_NUM = "+
											"(SELECT MAX(MERGED_SEQ_NUM) FROM LM_MERGED_REPORT_DATA) "+
											"AND OAS_PRODUCT_NAME = ? "+
											"ORDER BY OAS_CUSTOMER_ID, SF_CUSTOMER_ID, OAS_ORG_NODE_ID, SF_ORG_NODE_ID";
												
	/**
	 * Fetch LM merged report data for the latest data merge between SF & OAS
	 * @param productName
	 * @return ArrayList<LicenseMergedReportData>
	 */
	public static ArrayList<LicenseMergedReportData> fetchLMReportData(){
		Connection conn = null;
	   	PreparedStatement pstmt = null;
	   	ResultSet rs = null;
	   	ArrayList<LicenseMergedReportData> lmrdList = new ArrayList<LicenseMergedReportData>();
	    try{
   		 	 conn = SQLUtil.getConnection();
   		 	 pstmt = conn.prepareStatement(sqlStr1);
   		 	 //System.out.println(sqlStr1);
   		 	 rs = pstmt.executeQuery();
   		 	 while (rs.next()) {
   		 		LicenseMergedReportData lmrdBean = new LicenseMergedReportData();
   		 		//System.out.println(rs.getString("SF_CUSTOMER_ID"));
   		 		//System.out.println(rs.getString("OAS_CUSTOMER_ID"));
   		 		lmrdBean.setSfCustomerId(rs.getString("SF_CUSTOMER_ID"));
   		 		lmrdBean.setOasCustomerId(rs.getString("OAS_CUSTOMER_ID"));
   		 		lmrdBean.setOasContactName(rs.getString("OAS_CONTACT_NAME"));
   		 		lmrdBean.setOasContactPhone(rs.getString("OAS_CONTACT_PHONE"));
   		 		lmrdBean.setOasContactEmail(rs.getString("OAS_CONTACT_EMAIL"));
   		 		lmrdBean.setOasStatePRDesc(rs.getString("OAS_STATE_PR_DESC"));
   		 		lmrdBean.setOasStatePR(rs.getString("OAS_STATE_PR"));
   		 		lmrdBean.setOasExtCustomerId(rs.getString("OAS_EXT_CUSTOMER_ID"));
   		 		lmrdBean.setOasProductName(rs.getString("OAS_PRODUCT_NAME"));
   		 		lmrdBean.setSfContactName(rs.getString("SF_CONTACT"));
   		 		lmrdBean.setSfContactPhone(rs.getString("SF_CONTACT_PHONE"));
   		 		lmrdBean.setSfContactEmail(rs.getString("SF_CONTACT_EMAIL"));
   		 		lmrdBean.setSfCustomerActName(rs.getString("SF_CUSTOMER_ACCOUNT_NAME"));
   		 		lmrdBean.setSfActState(rs.getString("SF_ACCOUNT_STATE"));
   		 		lmrdBean.setSfOrgNodeId(rs.getString("SF_ORG_NODE_ID"));
   		 		lmrdBean.setSfOrgNodeName(rs.getString("SF_ORG_NODE_NAME"));
   		 		lmrdBean.setSfCategoryName(rs.getString("SF_CATEGORY_NAME"));
   		 		lmrdBean.setSfCategoryLevel(rs.getString("SF_CATEGORY_LEVEL"));
   		 		lmrdBean.setOasOrgNodeId(rs.getString("OAS_ORG_NODE_ID"));
   		 		lmrdBean.setOasOrgNodeName(rs.getString("OAS_ORG_NODE_NAME"));
   		 		lmrdBean.setOasOrgNodeCategoryName(rs.getString("OAS_ORG_NODE_CATEGORY_NAME"));
   		 		lmrdBean.setOasOrgNodeCategoryLevel(rs.getString("OAS_ORG_NODE_CATEGORY_LEVEL"));
   		 		lmrdBean.setOasParentOrgNodeId(rs.getString("OAS_PARENT_ORG_NODE_ID"));
   		 		lmrdBean.setOasParentOrgNodeName(rs.getString("OAS_PARENT_ORG_NODE_NAME"));
   		 		lmrdBean.setSfLicenseModel(rs.getString("SF_LICENSE_MODEL"));
   		 		lmrdBean.setOasLicenseModel(rs.getString("OAS_LICENSE_MODEL"));
   		 		lmrdBean.setSfLicenseCount(rs.getString("SF_LICENSE_COUNT"));
   		 		lmrdBean.setSfOrderQuantity(rs.getString("SF_ORDER_QUANTITY"));
   		 		lmrdBean.setSfLicenseDistributedTo(rs.getString("SF_LICENSE_DISTRIBUTED_TO"));
   		 		lmrdBean.setSfCreatedDate(rs.getString("SF_CREATED_DATE"));
   		 		lmrdBean.setSfIntervalName(rs.getString("SF_INTERVAL_NAME"));
   		 		lmrdBean.setSfLoadDateTime(rs.getString("SF_LOAD_DATE_TIME"));
   		 		lmrdBean.setSfLoadSeqNum(rs.getString("SF_LOAD_SEQ_NUM"));
   		 		lmrdBean.setOasLicAvlInDb(rs.getString("OAS_LIC_AVL_IN_DB"));
   		 		lmrdBean.setOasCumLicAvlInDb(rs.getString("OAS_CUM_LIC_AVL_IN_DB"));
   		 		lmrdBean.setOasLicToBeReleased(rs.getString("OAS_LIC_TO_BE_RELEASED"));
   		 		lmrdBean.setOasCumLicToBeReleased(rs.getString("OAS_CUM_LIC_TO_BE_RELEASED"));
   		 		lmrdBean.setOasLicReservedAtNode(rs.getString("OAS_LIC_RESERVED_AT_NODE"));
   		 		lmrdBean.setOasCumLicReserved(rs.getString("OAS_CUM_LIC_RESERVED"));
   		 		lmrdBean.setOasLicConsumedAtNode(rs.getString("OAS_LIC_CONSUMED_AT_NODE"));
   		 		lmrdBean.setOasCumLicConsumed(rs.getString("OAS_CUM_LIC_CONSUMED"));
   		 		lmrdBean.setOasNodeLvlAvailable(rs.getString("OAS_NODE_LVL_AVAILABLE"));
   		 		lmrdBean.setOasCumAvailable(rs.getString("OAS_CUM_AVAILABLE"));
   		 		lmrdBean.setOasNodeNetAvailable(rs.getString("OAS_NODE_NET_AVAILABLE"));
   		 		lmrdBean.setOasCumNetAvailable(rs.getString("OAS_CUM_NET_AVAILABLE"));
   		 		lmrdBean.setOasLicModConsumedAtNode(rs.getString("OAS_LIC_MOD_CONSUMED_AT_NODE"));
   		 		lmrdBean.setOasCumLicModConsumed(rs.getString("OAS_CUM_LIC_MOD_CONSUMED"));
   		 		lmrdBean.setOasLicModReservedAtNode(rs.getString("OAS_LIC_MOD_RESERVED_AT_NODE"));
   		 		lmrdBean.setOasCumLicModReserved(rs.getString("OAS_CUM_LIC_MOD_RESERVED"));
   		 		lmrdBean.setOasActivationStatus(rs.getString("OAS_ACTIVATION_STATUS"));
   		 		lmrdBean.setOasExtractDateTime(rs.getString("OAS_EXTRACT_DATE_TIME"));
   		 		lmrdBean.setMergedDateTime(rs.getString("MERGED_DATE_TIME"));
   		 		lmrdBean.setMergedSeqNum(rs.getString("MERGED_SEQ_NUM"));
   		 		lmrdBean.setLtExportDateTime(rs.getString("LT_EXPORT_DATE_TIME"));
   		 		lmrdBean.setOasExtractSeqNum(rs.getString("OAS_EXTRACT_SEQ_NUM"));
				lmrdList.add(lmrdBean);
   		 	 }
	    	 
   	 	} catch(SQLException se){
   	 		logger.error("SQLException : occurred while fetching data.");
   	 		se.printStackTrace();
   	 	}finally {
   	 		SQLUtil.closeDbObjects(conn, pstmt, null);
        }
   	 	return lmrdList;
	}
	
	/**
	 * Fetch LM merged report data for the latest data merge between SF & OAS - On product basis
	 * @param productName
	 * @return ArrayList<LicenseMergedReportData>
	 */
	public static ArrayList<LicenseMergedReportData> fetchLMReportDataByProduct(String productName){
		Connection conn = null;
	   	PreparedStatement pstmt = null;
	   	ResultSet rs = null;
	   	ArrayList<LicenseMergedReportData> lmrdList = new ArrayList<LicenseMergedReportData>();
	    try{
   		 	 conn = SQLUtil.getConnection();
   		 	 pstmt = conn.prepareStatement(sqlStr2);
   		 	 pstmt.setString(1, productName);
   		 	 //System.out.println(sqlStr2);
   		 	 rs = pstmt.executeQuery();
   		 	 while (rs.next()) {
   		 		LicenseMergedReportData lmrdBean = new LicenseMergedReportData();
   		 		//System.out.println(rs.getString("SF_CUSTOMER_ID"));
   		 		//System.out.println(rs.getString("OAS_CUSTOMER_ID"));
   		 		lmrdBean.setSfCustomerId(rs.getString("SF_CUSTOMER_ID"));
   		 		lmrdBean.setOasCustomerId(rs.getString("OAS_CUSTOMER_ID"));
   		 		lmrdBean.setOasContactName(rs.getString("OAS_CONTACT_NAME"));
   		 		lmrdBean.setOasContactPhone(rs.getString("OAS_CONTACT_PHONE"));
   		 		lmrdBean.setOasContactEmail(rs.getString("OAS_CONTACT_EMAIL"));
   		 		lmrdBean.setOasStatePRDesc(rs.getString("OAS_STATE_PR_DESC"));
   		 		lmrdBean.setOasStatePR(rs.getString("OAS_STATE_PR"));
   		 		lmrdBean.setOasExtCustomerId(rs.getString("OAS_EXT_CUSTOMER_ID"));
   		 		lmrdBean.setOasProductName(rs.getString("OAS_PRODUCT_NAME"));
   		 		//System.out.println("rs.getString(\"SF_CONTACT\") >> "+rs.getString("SF_CONTACT"));
   		 		lmrdBean.setSfContactName(rs.getString("SF_CONTACT"));
   		 		lmrdBean.setSfContactPhone(rs.getString("SF_CONTACT_PHONE"));
   		 		lmrdBean.setSfContactEmail(rs.getString("SF_CONTACT_EMAIL"));
   		 		lmrdBean.setSfCustomerActName(rs.getString("SF_CUSTOMER_ACCOUNT_NAME"));
   		 		lmrdBean.setSfActState(rs.getString("SF_ACCOUNT_STATE"));
   		 		lmrdBean.setSfOrgNodeId(rs.getString("SF_ORG_NODE_ID"));
   		 		lmrdBean.setSfOrgNodeName(rs.getString("SF_ORG_NODE_NAME"));
   		 		lmrdBean.setSfCategoryName(rs.getString("SF_CATEGORY_NAME"));
   		 		lmrdBean.setSfCategoryLevel(rs.getString("SF_CATEGORY_LEVEL"));
   		 		lmrdBean.setOasOrgNodeId(rs.getString("OAS_ORG_NODE_ID"));
   		 		lmrdBean.setOasOrgNodeName(rs.getString("OAS_ORG_NODE_NAME"));
   		 		lmrdBean.setOasOrgNodeCategoryName(rs.getString("OAS_ORG_NODE_CATEGORY_NAME"));
   		 		lmrdBean.setOasOrgNodeCategoryLevel(rs.getString("OAS_ORG_NODE_CATEGORY_LEVEL"));
   		 		lmrdBean.setOasParentOrgNodeId(rs.getString("OAS_PARENT_ORG_NODE_ID"));
   		 		lmrdBean.setOasParentOrgNodeName(rs.getString("OAS_PARENT_ORG_NODE_NAME"));
   		 		lmrdBean.setSfLicenseModel(rs.getString("SF_LICENSE_MODEL"));
   		 		lmrdBean.setOasLicenseModel(rs.getString("OAS_LICENSE_MODEL"));
   		 		lmrdBean.setSfLicenseCount(rs.getString("SF_LICENSE_COUNT"));
   		 		lmrdBean.setSfOrderQuantity(rs.getString("SF_ORDER_QUANTITY"));
   		 		lmrdBean.setSfLicenseDistributedTo(rs.getString("SF_LICENSE_DISTRIBUTED_TO"));
   		 		lmrdBean.setSfCreatedDate(rs.getString("SF_CREATED_DATE"));
   		 		lmrdBean.setSfIntervalName(rs.getString("SF_INTERVAL_NAME"));
   		 		lmrdBean.setSfLoadDateTime(rs.getString("SF_LOAD_DATE_TIME"));
   		 		lmrdBean.setSfLoadSeqNum(rs.getString("SF_LOAD_SEQ_NUM"));
   		 		lmrdBean.setOasLicAvlInDb(rs.getString("OAS_LIC_AVL_IN_DB"));
   		 		lmrdBean.setOasCumLicAvlInDb(rs.getString("OAS_CUM_LIC_AVL_IN_DB"));
   		 		lmrdBean.setOasLicToBeReleased(rs.getString("OAS_LIC_TO_BE_RELEASED"));
   		 		lmrdBean.setOasCumLicToBeReleased(rs.getString("OAS_CUM_LIC_TO_BE_RELEASED"));
   		 		lmrdBean.setOasLicReservedAtNode(rs.getString("OAS_LIC_RESERVED_AT_NODE"));
   		 		lmrdBean.setOasCumLicReserved(rs.getString("OAS_CUM_LIC_RESERVED"));
   		 		lmrdBean.setOasLicConsumedAtNode(rs.getString("OAS_LIC_CONSUMED_AT_NODE"));
   		 		lmrdBean.setOasCumLicConsumed(rs.getString("OAS_CUM_LIC_CONSUMED"));
   		 		lmrdBean.setOasNodeLvlAvailable(rs.getString("OAS_NODE_LVL_AVAILABLE"));
   		 		lmrdBean.setOasCumAvailable(rs.getString("OAS_CUM_AVAILABLE"));
   		 		lmrdBean.setOasNodeNetAvailable(rs.getString("OAS_NODE_NET_AVAILABLE"));
   		 		lmrdBean.setOasCumNetAvailable(rs.getString("OAS_CUM_NET_AVAILABLE"));
   		 		lmrdBean.setOasLicModConsumedAtNode(rs.getString("OAS_LIC_MOD_CONSUMED_AT_NODE"));
   		 		lmrdBean.setOasCumLicModConsumed(rs.getString("OAS_CUM_LIC_MOD_CONSUMED"));
   		 		lmrdBean.setOasLicModReservedAtNode(rs.getString("OAS_LIC_MOD_RESERVED_AT_NODE"));
   		 		lmrdBean.setOasCumLicModReserved(rs.getString("OAS_CUM_LIC_MOD_RESERVED"));
   		 		lmrdBean.setOasActivationStatus(rs.getString("OAS_ACTIVATION_STATUS"));
   		 		lmrdBean.setOasExtractDateTime(rs.getString("OAS_EXTRACT_DATE_TIME"));
   		 		lmrdBean.setMergedDateTime(rs.getString("MERGED_DATE_TIME"));
   		 		lmrdBean.setMergedSeqNum(rs.getString("MERGED_SEQ_NUM"));
   		 		lmrdBean.setLtExportDateTime(rs.getString("LT_EXPORT_DATE_TIME"));
   		 		lmrdBean.setOasExtractSeqNum(rs.getString("OAS_EXTRACT_SEQ_NUM"));
   		 		   		 				
				lmrdList.add(lmrdBean);
   		 	 }
	    	 
   	 	} catch(SQLException se){
   	 		logger.error("SQLException : occurred while fetching data.");
   	 		se.printStackTrace();
   	 	}finally {
   	 		SQLUtil.closeDbObjects(conn, pstmt, null);
        }
   	 	return lmrdList;
	}
}
