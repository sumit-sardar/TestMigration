package com.ctb.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ctb.bean.SalesForceLicenseData;
import com.ctb.importdata.ImportSFDataProcessor;


/**
 * This class provides data access functions for EHS import
 * 
 * @author TCS
 * 
 */
public class DBUtil {	
	static Logger logger = Logger.getLogger(DBUtil.class.getName());
	
	private static String sqlStr1 = "INSERT INTO LM_PURCHASE_DATA_SF_STG "+
									   "(CUSTOMER_ID, "+
									   	"OAS_IMPLEMENTATION_ID, "+
									    "IMPL_RECORD_TYPE, "+
										"CUSTOMER_ACCOUNT_NAME, "+
										"ACCOUNT_STATE, "+
										"ORG_NODE_ID, "+
										"ORG_NODE_NAME, "+
										"CONTACT_PHONE, "+
										"CONTACT, "+
										"CONTACT_EMAIL, "+
										"CATEGORY_NAME, "+
										"CATEGORY_LEVEL, "+
										"LICENSE_MODEL, "+
										"LICENSE_COUNT, "+
										"ORDER_QUANTITY, "+
										"LICENSE_DISTRIBUTED_TO, "+
										"CREATED_DATE, "+
										"INTERVAL_NAME, "+
										"LOAD_DATE_TIME, "+
										"LOAD_SEQ_NUM "+
										") "+
									"VALUES "+
									  "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)";
	
	private static String sqlStr2 = "select SEQ_LM_LOAD_SF_SEQ_NUM.nextval as loadSeqNum from dual";
	
	//private static Connection conn = null;
	//private static PreparedStatement pstmt= null;
	//private static ResultSet rs = null;
	
	public static boolean saveSFLicenseDataInBatch (ArrayList<SalesForceLicenseData> sfDataList) {
		 boolean result = true;
	   	 Connection conn = null;
	   	 PreparedStatement pstmt = null;
	   	 if(sfDataList != null && sfDataList.size() > 0){
		    	 try{
		    		 conn = SQLUtil.getConnection();
			    	 conn.setAutoCommit(false);
			    	 Integer loadSeqNum = getSFLoadSeqNum(conn);
			    	 logger.info("SF License Data will be loaded for LOAD_SEQ_NUM = ["+loadSeqNum+"]");
			    	 pstmt = conn.prepareStatement(sqlStr1);
			    	 for (Iterator<SalesForceLicenseData> iterator = sfDataList.iterator(); iterator.hasNext();) {
			    		
			    		SalesForceLicenseData sfld = (SalesForceLicenseData) iterator.next();
						//System.out.println("sfld.getCustomerId() >> "+sfld.getCustomerId());
						//System.out.println("sfld.getOrgNodeId() >> "+sfld.getOrgNodeId());
						//System.out.println("sfld.getCreatedDate() >> "+sfld.getCreatedDate());
						//System.out.println("sfld.getAccountState() >> "+sfld.getAccountState() + ":: length >> "+sfld.getAccountState().length());
			    		pstmt.setInt(1, sfld.getCustomerId());
						pstmt.setString(2, sfld.getOasImplementationId());
						pstmt.setString(3, sfld.getImplRecordType());
						pstmt.setString(4, sfld.getCustomerAccountName());
						pstmt.setString(5, sfld.getAccountState());
						pstmt.setInt(6, sfld.getOrgNodeId());
						pstmt.setString(7, sfld.getOrgNodeName());
						pstmt.setString(8, sfld.getContactPhone());
						pstmt.setString(9, sfld.getContact());
						pstmt.setString(10, sfld.getContactEmail());
						pstmt.setString(11, sfld.getCategoryName());
						if(sfld.getCategoryLevel() != null)
							pstmt.setInt(12, sfld.getCategoryLevel());
						else
							pstmt.setString(12, null);
						pstmt.setString(13, sfld.getLicenseModel());
						if(sfld.getLicenseCount() != null)
							pstmt.setInt(14, sfld.getLicenseCount());
						else
							pstmt.setString(14, null);
						if(sfld.getOrderQuantity() != null)
							pstmt.setInt(15, sfld.getOrderQuantity());
						else
							pstmt.setString(15, null);
						pstmt.setString(16, sfld.getLicenseDistributedTo());
						if(sfld.getCreatedDate() != null)
							pstmt.setDate(17, new java.sql.Date(sfld.getCreatedDate().getTime()));
						else
							pstmt.setDate(17, null);
						pstmt.setString(18, sfld.getIntervalName());
						pstmt.setInt(19, loadSeqNum);
						
						pstmt.addBatch();				

					}    	
			    	pstmt.executeBatch(); 
				    conn.commit();
		    	 } catch(SQLException se){
		    		 logger.error("SQLException : occurred while executing Batch Update");
		    		 result = false;
		    		 try {
		    			logger.error("Batch Update Rollback : Started :: Timestamp >> "+ new Date(System.currentTimeMillis()));
						conn.rollback();
						logger.error("Batch Update Rollback : Completed:: Timestamp >> "+ new Date(System.currentTimeMillis()));
					} catch (SQLException e) {
						logger.error("SQLException : occurred while Batch Update Rollback.");
						e.printStackTrace();
					}
		    		 se.printStackTrace();
		    	 }finally {
		        	SQLUtil.closeDbObjects(conn, pstmt, null);
		         }
	   	 }
	   	 
	   	 return result;
	}
	
	public static Integer getSFLoadSeqNum(Connection conn) throws SQLException{
		Integer loadSeqNum = null;
		//Connection conn = null;
	   	PreparedStatement pstmt = null;
	   	ResultSet rs = null;
		try{
			//conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(sqlStr2);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadSeqNum = new Integer(rs.getInt("loadSeqNum"));
			}
		}catch(SQLException se){
			//se.printStackTrace();
			logger.error("SQLException : occurred while generating LOAD_SEQ_NUM");
			throw new SQLException();
		}finally{
			//TODO
			SQLUtil.closeDbObjects(null, pstmt, rs);
		}		
		
		return loadSeqNum;
	}
			
}
