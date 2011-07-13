package com.tcs.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;


import com.tcs.dataaccess.ConnectionManager;





public class FileUploadUtil {
	
	public static String getSiteServey (String siteId,String surveyType) {
		
		return "SELECT count(*) isExist FROM site_survey WHERE site_survey_id ='"+siteId+"' and site_type='"+surveyType+"'";
	}
	
     public static String UpdateSiteSurveyDataSQL (String rowData[]) {
		
    	 String stmt = "";
    	 
    	 if (rowData[3] != null && rowData[3].length() > 0)
    		 stmt += "TEST_COORD_FIRST ='"+rowData[3]+"',";
    	 if (rowData[4] != null && rowData[4].length() > 0)
    		 stmt += "TEST_COORD_LAST ='"+rowData[4]+"',";
    	 if (rowData[5] != null && rowData[5].length() > 0)
    		 stmt += "TEST_COORD_EMAIL ='"+rowData[5]+"',";
    	 if (rowData[6] != null && rowData[6].length() > 0)
    		 stmt += "TEST_COORD_PHONE ='"+rowData[6]+"',";
    	 if (rowData[7] != null && rowData[7].length() > 0)
    		 stmt += "TECH_COORD_FIRST ='"+rowData[7]+"',";
    	 if (rowData[8] != null && rowData[8].length() > 0)
    		 stmt += "TECH_COORD_LAST ='"+rowData[8]+"',";
    	 if (rowData[9] != null && rowData[9].length() > 0)
    		 stmt += "TECH_COORD_EMAIL ='"+rowData[9]+"',";
    	 if (rowData[10] != null && rowData[10].length() > 0)
    		 stmt += "TECH_COORD_PHONE ='"+rowData[10]+"',";
    	 
    	 if (!stmt.equals("")){
    		 
    		 //remove the last comma
    		 stmt= stmt.substring(0,stmt.length()-1);
    		 
    		 stmt = "update site_survey set " + stmt + " WHERE SITE_ID='"+rowData[1]+"' AND SITE_TYPE='"+rowData[2]+"'";
    	 }
    			 
    return stmt;
    
	}
	
	
	public static InputStream getFileInputStream (String filePath) throws Exception{
		
		return new FileInputStream (filePath);
	}
	
	/*public static void init (rowData[]) throws Exception {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement creStmtUpdateEmptyBlob = con.prepareStatement(FileUploadUtil.UpdateSiteSurveyDataSQL(rowData[]));
		
		creStmtUpdateEmptyBlob.executeUpdate();
		creStmtUpdateEmptyBlob.close();
		con.commit();
		ConnectionManager.close(con);
		
	}*/
	
	public static void updateBlob(Blob blob, InputStream inputStream) throws Exception {
		try {
			int i = 0;
			
			byte[] tempByte = new byte[Integer.valueOf("1024").intValue()];//new byte[Integer.MAX_VALUE];
	        long size = 1L;
	        if (inputStream != null) {
	        	
	        	while((i = inputStream.read(tempByte)) != -1) {
		        	blob.setBytes(size, tempByte);
		        	size = size + tempByte.length;
				}
	        	
	        }
	        
		} catch (Exception ex) {
			throw new Exception(ex);
		} catch(Error ex) {
			ex.printStackTrace();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
	
	
	
	
	
	
}
