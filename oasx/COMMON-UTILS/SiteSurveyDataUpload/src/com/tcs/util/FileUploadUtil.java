package com.tcs.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;


import com.tcs.dataaccess.ConnectionManager;





public class FileUploadUtil {
	
	public static String getSiteServey (String siteId,String surveyType) {
		
		return "SELECT count(*) isExist FROM site_survey WHERE site_id ='"+siteId+"' and site_type='"+surveyType+"'";
	}
	
	public static String getSiteServeypath (String siteId,String path) {
		
		return "SELECT count(*) isExist FROM site_survey WHERE site_id ='"+siteId+"' and site_path ='"+path+"'";
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
                 
                 //Change for updating contact_status in database
                 String val = "T";

    		 if(rowData[7] == null || rowData[8] == null || rowData[3] == null || rowData[4] == null  
    				 || rowData[5] == null || rowData[9] == null || rowData[6] == null || rowData[10] == null 
    				 || rowData[7].equals("") || rowData[8].equals("") || rowData[3].equals("") || rowData[4].equals("")
    				 || rowData[5].equals("") || rowData[9].equals("") || rowData[6].equals("") || rowData[10].equals("")){
    			 
    			  val = "F";
    		 }
    			 stmt += "CONTACT_STATUS ='"+val+"',";
    			 
    		 
    		 //remove the last comma
    		 stmt= stmt.substring(0,stmt.length()-1);
    		 
    		 stmt = "update site_survey set " + stmt + " WHERE SITE_ID='"+rowData[1]+"' AND SITE_TYPE='"+rowData[2]+"'";
    	 }
    			 
    return stmt;
    
	}
     
     
     public static String insertSiteSurveyDataSQL (String rowData[], SiteDataDetail siteDataDetail) {
 		
    	 String stmt = "";
    	 String value = "";
    	 if (rowData[0] != null && rowData[0].length() > 0) {
    		 stmt += "CUSTOMER_ID ,";
    		 value+= "'" + rowData[0] + "',";
    	 }
    	 if (rowData[1] != null && rowData[1].length() > 0) {
    		 stmt += "SITE_ID ,";
    		 value+= "'" + rowData[1] + "',";
    	 } 
    	 if (rowData[2] != null && rowData[2].length() > 0) {
    		 stmt += "SITE_TYPE ,";
    		 value+= "'" + rowData[2] + "',";
    	 }
    	 if (rowData[3] != null && rowData[3].length() > 0) {
    		 stmt += "TEST_COORD_FIRST ,";
    		 value+= "'" + rowData[3] + "',";
    	 }
    	 if (rowData[4] != null && rowData[4].length() > 0) {
    		 stmt += "TEST_COORD_LAST ,";
    		 value+= "'" + rowData[4] + "',";
    	 }
    	 if (rowData[5] != null && rowData[5].length() > 0) {
    		 stmt += "TEST_COORD_EMAIL ,";
    		 value+= "'" + rowData[5] + "',";
    	 }
    	 if (rowData[6] != null && rowData[6].length() > 0) {
    		 stmt += "TEST_COORD_PHONE ,";
    		 value+= "'" + rowData[6] + "',";
    	 }
    	 if (rowData[7] != null && rowData[7].length() > 0) {
    		 stmt += "TECH_COORD_FIRST ,";
    		 value+= "'" + rowData[7] + "',";
    	 }
    	 if (rowData[8] != null && rowData[8].length() > 0) {
    		 stmt += "TECH_COORD_LAST ,";
    		 value+= "'" + rowData[8] + "',";
    	 }
    	 if (rowData[9] != null && rowData[9].length() > 0) {
    		 stmt += "TECH_COORD_EMAIL ,";
    		 value+= "'" + rowData[9] + "',";
    	 }
    	 if (rowData[10] != null && rowData[10].length() > 0) {
    		 stmt += "TECH_COORD_PHONE ,";
    		 value+= "'" + rowData[10] + "',";
    	 }
    	 if (siteDataDetail != null && siteDataDetail.getName()!= null) {
    		 stmt += "SITE_NAME ,";
    		 value+= "'" + siteDataDetail.getName() + "',";
    	 }
    	 if (siteDataDetail != null && siteDataDetail.getPath()!= null) {
    		 stmt += "SITE_PATH ,";
    		 value+= "'" + siteDataDetail.getPath() + "',";
    	 }
    	 
					
    	 if (!stmt.equals("") && !value.equals("")){
    		 
                 //Change for inserting contact_status along with other field data as false in database

    		 String val = "T";
    		 if(rowData[7] == null || rowData[8] == null || rowData[3] == null || rowData[4] == null  
    				 || rowData[5] == null || rowData[9] == null || rowData[6] == null || rowData[10] == null 
    				 || rowData[7].equals("") || rowData[8].equals("") || rowData[3].equals("") || rowData[4].equals("")
    				 || rowData[5].equals("") || rowData[9].equals("") || rowData[6].equals("") || rowData[10].equals("")){
    			 
    			  val = "F";
    		 }
    			 stmt += "CONTACT_STATUS , ENROLLMENT_STATUS, NETWORK_STATUS, WORKSTATION_STATUS, READINESS_STATUS,";
    			 value+= "'" + val + "','F' ,'F' ,'F' ,'F',";
    			 
    		 //remove the last comma
    		 stmt= stmt.substring(0,stmt.length()-1);
    		 value= value.substring(0,value.length()-1);
    		 
    		 stmt = "insert into site_survey  (SITE_SURVEY_ID ," + stmt + ") values (SEQ_SITE_SURVEY_ID.nextval ,"+ value +")";
    		 System.out.println(stmt);
    	 }
    			 
    return stmt;
    
	}
     public static String UpdatepthBasedSiteSurveyDataSQL (String rowData[], SiteDataDetail siteDataDetail) {
 		
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
    		 if (rowData[2] == null || rowData[2].equals(""))
    			 stmt = "update site_survey set " + stmt + " WHERE SITE_ID='"+rowData[1]+"' AND SITE_TYPE IS NULL AND SITE_PATH='"+siteDataDetail.getPath()+"'";
    		 else {
    			 stmt = "update site_survey set " + stmt + " WHERE SITE_ID='"+rowData[1]+"' AND SITE_TYPE ='"+rowData[2]+"'AND SITE_PATH='"+siteDataDetail.getPath()+"'";
    		 }
    	 }
    			 
    return stmt;
    
	}
	
     
     public static String getSiteDetailSQL (String siteId) {
  		
    	 String stmt = "SELECT PTG.PARTY_ID AS ID, PTG.GROUP_NAME AS NAME, PTG.GROUP_NUMBER AS SITEID,  COM_MISC_SQL.GETPARTYPATH(PTY.PARTY_ID) AS PATH  FROM PARTY_GROUP PTG, PARTY PTY WHERE PTG.GROUP_NUMBER = '"+ siteId +"'  AND PTY.PARTY_ID = PTG.PARTY_ID  AND PTY.STATUS_ID = 'ORGANIZATION_ACTIVE' AND PTY.PARTY_TYPE_ID = 'ORGANIZATION'";
    	
    			 
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
