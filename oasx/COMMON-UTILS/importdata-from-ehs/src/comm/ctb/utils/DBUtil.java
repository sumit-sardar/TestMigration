package comm.ctb.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import comm.ctb.bean.ItemDataPointDetailsVO;
import comm.ctb.bean.ReadDetailsVO;
import comm.ctb.bean.StudentTestDetailsVO;

/**
 * This class provides data access functions for EHS import
 * 
 * @author TCS
 * 
 */
public class DBUtil {	
	
	private static String sqlStr1 = "INSERT INTO ITEM_DATAPOINT_SCORE "+
									  "(ITEM_READER_ID, "+
									   "TEST_ROSTER_ID, "+
									   "STUDENT_ID, "+
									   "ITEM_ID, "+
									   "DATA_POINT, "+
									   "ITEM_NO, "+
									   "FINAL_SCORE, "+
									   "CREATED_DATE_TIME) "+
									"VALUES "+
									  "(?, ?, ?, ?, ?, ?, ?, SYSDATE)";
	
	private static String sqlStr2 = "INSERT INTO ITEM_READ_DETAILS "+
									  "(ITEM_READER_ID, "+
									   "READ_NUMBER, "+
									   "SCORE_VALUE, "+
									   "READER_ID, "+
									   "DATE_TIME, "+
									   "ELAPSED_TIME) "+
									"VALUES "+
									  "(?, ?, ?, ?, ?, ?)";
	
	private static String sqlStr3 = "INSERT INTO EHS_IMPORT_LOG "+
									  "(STUDENT_TEST_ID, "+
									   "DOCUMENT_ID, "+
									   "VENDOR_STUDENT_ID, "+
									   "EHS_FILE_NAME, "+
									   "LOG_DATE_TIME, "+
									   "LOG_MESSAGE) "+
									"VALUES "+
									  "(?, ?, ?, ?, sysdate, ?)";
	
	//private static Connection conn = null;
	//private static PreparedStatement pstmt= null;
	//private static ResultSet rs = null;
	
	public static boolean saveItemScoreData(StudentTestDetailsVO stuTestDetails) {
		 List itemDataPointList = stuTestDetails.getItemDataPointList();
		 boolean result = true;
	   	 Connection conn = null;
	   	 PreparedStatement pstmt = null;
	   	 if(itemDataPointList != null && itemDataPointList.size() > 0){
		    	 try{
		    		 conn = SQLUtil.getConnection();
			    	 conn.setAutoCommit(false);
			    	 pstmt = conn.prepareStatement(sqlStr1);
			    	 for (Iterator iterator = itemDataPointList.iterator(); iterator.hasNext();) {
			    		
			    		ItemDataPointDetailsVO idpd = (ItemDataPointDetailsVO) iterator.next();			
						Integer itemReaderId = getSeqItemReaderId(conn);
			    		pstmt.setInt(1, itemReaderId.intValue());
						pstmt.setInt(2, Integer.parseInt(stuTestDetails.getStudentTestId().substring(10)));
						pstmt.setInt(3, Integer.parseInt(stuTestDetails.getVendorStudentId()));
						pstmt.setString(4, idpd.getItemId().substring(7));
						pstmt.setString(5, idpd.getDataPoint());
						pstmt.setInt(6, Integer.parseInt(idpd.getItemNo()));
						pstmt.setString(7, idpd.getFinalScore());
						
						pstmt.executeUpdate();
						
						saveReadDetails(itemReaderId, idpd.getReadDetailsList(), conn);						

					}    	 
				    conn.commit();
		    	 } catch(SQLException se){
		    		 result = false;
		    		 try {
						conn.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
		    		 se.printStackTrace();
		    		 stuTestDetails.setErrorMsg(se.getMessage());
		    	 }finally {
		        	SQLUtil.closeDbObjects(conn, pstmt, null);
		         }
	   	 }
	   	 
	   	 return result;
	}
	
	public static Integer getSeqItemReaderId(Connection conn) throws SQLException{
		Integer itemReaderId = null;
		//Connection conn = null;
	   	PreparedStatement pstmt = null;
	   	ResultSet rs = null;
		try{
			//conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement("select seq_item_reader_id.nextval as itemReaderId from dual");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				itemReaderId = new Integer(rs.getInt("itemReaderId"));
			}
		}catch(SQLException se){
			//se.printStackTrace();
			throw new SQLException();
		}finally{
			//TODO
			SQLUtil.closeDbObjects(null, pstmt, rs);
		}		
		
		return itemReaderId;
	}
	
	public static void saveReadDetails(Integer itemReaderId, List readDetailsList, Connection conn) throws SQLException{
		//Connection conn = null;
	   	PreparedStatement pstmt = null;
		if(readDetailsList != null && readDetailsList.size() > 0){
	    	 try{
	    		 //conn = SQLUtil.getConnection();
		    	 //conn.setAutoCommit(false);
		    	 pstmt = conn.prepareStatement(sqlStr2);
		    	 for (Iterator iterator = readDetailsList.iterator(); iterator.hasNext();) {
		    		
		    		ReadDetailsVO rdVO = (ReadDetailsVO) iterator.next();			
		    		pstmt.setInt(1,itemReaderId.intValue());
					pstmt.setInt(2, rdVO.getReadNumber().intValue());
					pstmt.setString(3, rdVO.getScoreValue());
					pstmt.setInt(4, rdVO.getReaderId().intValue());
					pstmt.setString(5, rdVO.getDataTime());
					pstmt.setInt(6, rdVO.getElapsedTime().intValue());
					
					pstmt.executeUpdate();										

				}    	 
			    
	    	 } catch(SQLException se){
	    		 //se.printStackTrace();	    		 
	    		 throw se;
	    	 }finally {
	        	SQLUtil.closeDbObjects(null, pstmt, null);
	         }
		}

	}
	
	public static boolean logForFailedRoster(StudentTestDetailsVO stuTestDetails, String fileName) {
		 boolean result = true;
	   	 Connection conn = null;
	   	 PreparedStatement pstmt = null;
	   	 String errMsg = stuTestDetails.getErrorMsg();
	   	 if(errMsg != null && errMsg.length() >= 4000) {
        	errMsg = errMsg.substring(0, 3999);
	   	 }
    	 try{
    		 conn = SQLUtil.getConnection();
	    	 conn.setAutoCommit(false);
	    	 pstmt = conn.prepareStatement(sqlStr3);
	    	 
    		 pstmt.setString(1, stuTestDetails.getStudentTestId());
			 pstmt.setLong(2, stuTestDetails.getDocumentId());
			 pstmt.setString(3, stuTestDetails.getVendorStudentId());
			 pstmt.setString(4, fileName);			 
			 pstmt.setString(5, errMsg);
			
			 pstmt.executeUpdate();						
				 	 
		    conn.commit();
    	 } catch(SQLException se){
    		 result = false;
    		 try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		 se.printStackTrace();
    	 }finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);
         }
	   	 
	   	 return result;
	}
}
