package com.tcs.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

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
	
	
	
	public static String isSiteSurveyIdExist (Integer siteSurveyID) {
		
		String sql = "SELECT count(*) as isExist FROM site_survey_enrollment WHERE site_survey_id ="+siteSurveyID;
		System.out.println(sql);
		
		return sql;
	}

	public static String getSiteServeyIdSQL(String customerId, String siteId,
			String sitePath) {

		String sql =  "select site_survey_id from site_survey where " +
				"customer_id = '"+customerId+"' " +
				"and site_id = '"+siteId+"' " +
				"and site_path='"+sitePath+"'";
		return sql;
	}

	/*public static String getsaveOrUpdateSiteSurveyEnrollMentMergeSQL(
			String[] rowData) {
		String sql =  " MERGE INTO site_survey_enrollment ssenrollment USING (select ? AS SITE_SURVEY_ID, ? AS THIRD_GRADE, ? AS  FOURTH_GRADE, ? AS FIFTH_GRADE, ? AS SIXTH_GRADE, ? AS SEVENTH_GRADE,  ? AS EIGHTH_GRADE, ? AS YEAR, ? AS TESTSESSION_PER_DAY, ? AS WORKSTATION_UP_TIME, ? AS TOTAL_TESTING_DAYS, ? AS SPARE_WORKSTATION_PERCENT, ? AS  MAKEUP_PERCENT  from dual) temp ON (ssenrollment.site_survey_id = temp.site_survey_id) WHEN MATCHED THEN   UPDATE  SET THIRD_GRADE  = temp.THIRD_GRADE,  FOURTH_GRADE  = temp.FOURTH_GRADE,  FIFTH_GRADE   = temp.FIFTH_GRADE,   SIXTH_GRADE   = temp.SIXTH_GRADE,  SEVENTH_GRADE  = temp.SEVENTH_GRADE, EIGHTH_GRADE  = temp.EIGHTH_GRADE,  YEAR   = temp.YEAR,  TESTSESSION_PER_DAY = temp.TESTSESSION_PER_DAY, WORKSTATION_UP_TIME       = temp.WORKSTATION_UP_TIME,  TOTAL_TESTING_DAYS  = temp.TOTAL_TESTING_DAYS, SPARE_WORKSTATION_PERCENT = temp.SPARE_WORKSTATION_PERCENT,  MAKEUP_PERCENT  = temp.MAKEUP_PERCENT , UPDATED_DATE_TIME = SYSDATE WHEN NOT MATCHED  THEN   INSERT     (SITE_SURVEY_ID,      THIRD_GRADE,      FOURTH_GRADE,      FIFTH_GRADE,      SIXTH_GRADE,      SEVENTH_GRADE,      EIGHTH_GRADE,   YEAR,   TESTSESSION_PER_DAY,   WORKSTATION_UP_TIME,   TOTAL_TESTING_DAYS,   SPARE_WORKSTATION_PERCENT,  MAKEUP_PERCENT) VALUES   ( temp. SITE_SURVEY_ID, temp.THIRD_GRADE,   temp.FOURTH_GRADE,   temp.FIFTH_GRADE,  temp.SIXTH_GRADE, temp.SEVENTH_GRADE,  temp.EIGHTH_GRADE,  temp.YEAR,  temp.TESTSESSION_PER_DAY, temp.WORKSTATION_UP_TIME, temp.TOTAL_TESTING_DAYS, temp.SPARE_WORKSTATION_PERCENT, temp.MAKEUP_PERCENT)";

		return sql;
	}*/
	
	public static String getsaveOrUpdateSiteSurveyEnrollMentMergeSQL(
			String[] rowData,String enrollmentTableName, boolean isSubject) {
		if(!isSubject)
			return getSaveOrUpdateEnrollmentByGrade(enrollmentTableName);
		else
			return getSaveOrUpdateEnrollmentBySubject(enrollmentTableName);
	}
	public static String getSaveOrUpdateEnrollmentByGrade(String enrollmentTableName){
		String sql = " MERGE INTO "+enrollmentTableName+" ssenrollment USING (select ? AS SITE_SURVEY_ID,  ? AS THIRD_GRADE,? AS FOURTH_GRADE, ? AS FIFTH_GRADE,"+
               "? AS SIXTH_GRADE, ? AS SEVENTH_GRADE, ? AS EIGHTH_GRADE, ? AS THIRD_GRADE_CHK, "+                                                                                                                                            
"? AS FOURTH_GRADE_CHK ,"+                                                                                                                                                                                              
"? AS FIFTH_GRADE_CHK ,  "+                                                                                                                                                                                         
"? AS SIXTH_GRADE_CHK ,    "+                                                                                                                                                                                       
"? AS SEVENTH_GRADE_CHK ,    "+                                                                                                                                                                                      
"? AS EIGHTH_GRADE_CHK "+
 "         from dual) temp "+
 "ON (ssenrollment.site_survey_id = temp.site_survey_id)"+
 " WHEN MATCHED THEN"+
  " UPDATE"+
   "   SET THIRD_GRADE         = temp.THIRD_GRADE,"+
    "      FOURTH_GRADE        = temp.FOURTH_GRADE,"+
     "     FIFTH_GRADE         = temp.FIFTH_GRADE,"+
      "    SIXTH_GRADE         = temp.SIXTH_GRADE,"+
       "   SEVENTH_GRADE       = temp.SEVENTH_GRADE,"+
        "  EIGHTH_GRADE        = temp.EIGHTH_GRADE,"+
        
	  "THIRD_GRADE_CHK  = temp.THIRD_GRADE_CHK,"+                                                                                                                                                                                                           
"FOURTH_GRADE_CHK   = temp.FOURTH_GRADE_CHK,     "+                                                                                                                                                                                           
"FIFTH_GRADE_CHK   = temp.FIFTH_GRADE_CHK,         "+                                                                                                                                                                                          
"SIXTH_GRADE_CHK   = temp.SIXTH_GRADE_CHK,           "+                                                                                                                                                                                              
"SEVENTH_GRADE_CHK   = temp.SEVENTH_GRADE_CHK,         "+                                                                                                                                                                                               
"EIGHTH_GRADE_CHK  = temp.EIGHTH_GRADE_CHK ,"+

" UPDATED_DATE_TIME   = SYSDATE,"+
"TESTSESSION_PER_DAY = ?,"+
"TOTAL_TESTING_DAYS  = ?"+

" WHEN NOT MATCHED THEN"+
 "  INSERT"+
  "   (SITE_SURVEY_ID,"+
   "   THIRD_GRADE,"+
    "  FOURTH_GRADE,"+
     " FIFTH_GRADE,"+
      "SIXTH_GRADE,"+
      "SEVENTH_GRADE,"+
    "  EIGHTH_GRADE,"+
    
    "  THIRD_GRADE_CHK   ,  "+                                                                                                                                                                                                 
"FOURTH_GRADE_CHK         ,   "+                                                                                                                                                                                             
"FIFTH_GRADE_CHK           ,    "+                                                                                                                                                                                      
"SIXTH_GRADE_CHK            ,     "+                                                                                                                                                                                          
"SEVENTH_GRADE_CHK           ,      "+                                                                                                                                                                                         
"EIGHTH_GRADE_CHK ," +
"TESTSESSION_PER_DAY,"+
    "  TOTAL_TESTING_DAYS )"+
 "  VALUES"+
  "   (temp. SITE_SURVEY_ID,"+
   "   temp.THIRD_GRADE,"+
    "  temp.FOURTH_GRADE,"+
     " temp.FIFTH_GRADE,"+
      "temp.SIXTH_GRADE,"+
  "    temp.SEVENTH_GRADE,"+
   "   temp.EIGHTH_GRADE,"+
    
  "    temp. THIRD_GRADE_CHK   ,"+                                                                                                                                                                                                    
"temp. FOURTH_GRADE_CHK         , "+                                                                                                                                                                                              
"temp. FIFTH_GRADE_CHK           ,  "+                                                                                                                                                                                          
"temp. SIXTH_GRADE_CHK            ,   "+                                                                                                                                                                                         
"temp. SEVENTH_GRADE_CHK           ,    "+                                                                                                                                                                                       
"temp. EIGHTH_GRADE_CHK         ," +
"  ?,"+
     " ?)";
		return sql;
	}
	
	public static String getSaveOrUpdateEnrollmentByGrade_wv(String enrollmentTableName){
		String sql = " MERGE INTO "+enrollmentTableName+" ssenrollment USING (select ? AS SITE_SURVEY_ID,  ? AS THIRD_GRADE,? AS FOURTH_GRADE, ? AS FIFTH_GRADE,"+
        "? AS SIXTH_GRADE, ? AS SEVENTH_GRADE, ? AS EIGHTH_GRADE, ? AS NINETH_GRADE, ? AS TENTH_GRADE, ? AS ELEVENTH_GRADE,? AS THIRD_GRADE_CHK, "+                                                                                                                                            
"? AS FOURTH_GRADE_CHK ,"+                                                                                                                                                                                              
"? AS FIFTH_GRADE_CHK ,  "+                                                                                                                                                                                         
"? AS SIXTH_GRADE_CHK ,    "+                                                                                                                                                                                       
"? AS SEVENTH_GRADE_CHK ,    "+                                                                                                                                                                                      
"? AS EIGHTH_GRADE_CHK ,   "+
"? AS NINETH_GRADE_CHK ,    "+                                                                                                                                                                                       
"? AS TENTH_GRADE_CHK ,    "+                                                                                                                                                                                      
"? AS ELEVENTH_GRADE_CHK   "+
"         from dual) temp "+
"ON (ssenrollment.site_survey_id = temp.site_survey_id)"+
" WHEN MATCHED THEN"+
" UPDATE"+
"   SET THIRD_GRADE         = temp.THIRD_GRADE,"+
"      FOURTH_GRADE        = temp.FOURTH_GRADE,"+
"     FIFTH_GRADE         = temp.FIFTH_GRADE,"+
"    SIXTH_GRADE         = temp.SIXTH_GRADE,"+
"   SEVENTH_GRADE       = temp.SEVENTH_GRADE,"+
 "  EIGHTH_GRADE        = temp.EIGHTH_GRADE,"+
 //////////////////start///////////////////////
 "    NINETH_GRADE         = temp.NINETH_GRADE,"+
 "   TENTH_GRADE       = temp.TENTH_GRADE,"+
  "  ELEVENTH_GRADE        = temp.ELEVENTH_GRADE,"+
 ////////////////END/////////////////
"THIRD_GRADE_CHK  = temp.THIRD_GRADE_CHK,"+                                                                                                                                                                                                           
"FOURTH_GRADE_CHK   = temp.FOURTH_GRADE_CHK,     "+                                                                                                                                                                                           
"FIFTH_GRADE_CHK   = temp.FIFTH_GRADE_CHK,         "+                                                                                                                                                                                          
"SIXTH_GRADE_CHK   = temp.SIXTH_GRADE_CHK,           "+                                                                                                                                                                                              
"SEVENTH_GRADE_CHK   = temp.SEVENTH_GRADE_CHK,         "+                                                                                                                                                                                               
"EIGHTH_GRADE_CHK  = temp.EIGHTH_GRADE_CHK ,"+
///////////////////////start///////////////////////////////
"NINETH_GRADE_CHK   = temp.NINETH_GRADE_CHK,           "+                                                                                                                                                                                              
"TENTH_GRADE_CHK   = temp.TENTH_GRADE_CHK,         "+                                                                                                                                                                                               
"ELEVENTH_GRADE_CHK  = temp.ELEVENTH_GRADE_CHK ,"+

////////////////////////end//////////////////////

" UPDATED_DATE_TIME   = SYSDATE,"+
"TESTSESSION_PER_DAY = ?,"+
"TOTAL_TESTING_DAYS  = ?"+

" WHEN NOT MATCHED THEN"+
"  INSERT"+
"   (SITE_SURVEY_ID,"+
"   THIRD_GRADE,"+
"  FOURTH_GRADE,"+
" FIFTH_GRADE,"+
"SIXTH_GRADE,"+
"SEVENTH_GRADE,"+
"  EIGHTH_GRADE,"+
//////////////start////////
"NINETH_GRADE,"+
"TENTH_GRADE,"+
"  ELEVENTH_GRADE,"+
///////////end///////////
"  THIRD_GRADE_CHK   ,  "+                                                                                                                                                                                                 
"FOURTH_GRADE_CHK         ,   "+                                                                                                                                                                                             
"FIFTH_GRADE_CHK           ,    "+                                                                                                                                                                                      
"SIXTH_GRADE_CHK            ,     "+                                                                                                                                                                                          
"SEVENTH_GRADE_CHK           ,      "+                                                                                                                                                                                         
"EIGHTH_GRADE_CHK ," +
/////////////start/////////
"NINETH_GRADE_CHK            ,     "+                                                                                                                                                                                          
"TENTH_GRADE_CHK           ,      "+                                                                                                                                                                                         
"ELEVENTH_GRADE_CHK ," +
////////end/////////
"TESTSESSION_PER_DAY,"+
"  TOTAL_TESTING_DAYS )"+
"  VALUES"+
"   (temp. SITE_SURVEY_ID,"+
"   temp.THIRD_GRADE,"+
"  temp.FOURTH_GRADE,"+
" temp.FIFTH_GRADE,"+
"temp.SIXTH_GRADE,"+
"    temp.SEVENTH_GRADE,"+
"   temp.EIGHTH_GRADE,"+

//////////////////start///////////////////////
"   temp.NINETH_GRADE,"+
"   temp.TENTH_GRADE,"+
"   temp.ELEVENTH_GRADE,"+
////////////////END/////////////////

"temp. THIRD_GRADE_CHK   ,"+                                                                                                                                                                                                    
"temp. FOURTH_GRADE_CHK         , "+                                                                                                                                                                                              
"temp. FIFTH_GRADE_CHK           ,  "+                                                                                                                                                                                          
"temp. SIXTH_GRADE_CHK            ,   "+                                                                                                                                                                                         
"temp. SEVENTH_GRADE_CHK           ,    "+                                                                                                                                                                                       
"temp. EIGHTH_GRADE_CHK         ," +

//////////////////////////////////start/////////////////////////
"temp.NINETH_GRADE_CHK,           "+                                                                                                                                                                                              
"temp.TENTH_GRADE_CHK,         "+                                                                                                                                                                                               
"temp.ELEVENTH_GRADE_CHK ,"+

///////////////////////////end/////////////////////////////////////////
"  ?,"+
" ?)";
		return sql;
	}
	
public static String getSaveOrUpdateEnrollmentBySubject(String enrollmentTableName){
		
		String sql = "MERGE INTO "+enrollmentTableName+" ssenrollment  "+
		"  USING (select ? AS SITE_SURVEY_ID,"+
	              "? AS SUB_ALG1,"+
	             " ? AS SUB_ALG2,"+
	             " ? AS SUB_USHIS,"+
	             " ? AS SUB_BIO,"+
	              "? AS SUB_GEO,"+
	              "? AS SUB_ENG2,"+
	              "? AS SUB_ENG3,"+
	             " ? AS SUB_ALG1_CHK ,  "+                                                                                                                                                                                                            
	"? AS SUB_ALG2_CHK,    "+                                                                                                                                                                                                           
	"? AS SUB_USHIS_CHK ,   "+                                                                                                                                                                                                          
	"? AS SUB_BIO_CHK ,     "+                                                                                                                                                                                                          
	"? AS SUB_GEO_CHK ,     "+                                                                                                                                                                                                          
	"? AS SUB_ENG2_CHK ,    "+                                                                                                                                                                                                         
	"? AS SUB_ENG3_CHK "+
	        " from dual) temp "+
	" ON (ssenrollment.site_survey_id = temp.site_survey_id)"+
	" WHEN MATCHED THEN "+
	 " UPDATE "+
	     " SET SUB_ALG1            = temp.SUB_ALG1,"+
	      "   SUB_ALG2            = temp.SUB_ALG2,"+
	      "   SUB_USHIS           = temp.SUB_USHIS,"+
	      "   SUB_BIO             = temp.SUB_BIO,"+
	       "  SUB_GEO             = temp.SUB_GEO,"+
	      "   SUB_ENG2            = temp.SUB_ENG2,"+
	      "   SUB_ENG3            = temp.SUB_ENG3,"+
	      
	        " SUB_ALG1_CHK  = temp.SUB_ALG1_CHK, "+                                                                                                                                                                                                            
		"SUB_ALG2_CHK = temp.SUB_ALG2_CHK,      "+                                                                                                                                                                                                        
		"SUB_USHIS_CHK  = temp.SUB_USHIS_CHK, "+                                                                                                                                                                                                           
		"SUB_BIO_CHK   = temp.SUB_BIO_CHK,    "+                                                                                                                                                                                                         
		"SUB_GEO_CHK  = temp.SUB_GEO_CHK,    "+                                                                                                                                                                                                          
		"SUB_ENG2_CHK = temp.SUB_ENG2_CHK,   "+                                                                                                                                                                                                          
		" SUB_ENG3_CHK = temp.SUB_ENG3_CHK,"+
		
		  " UPDATED_DATE_TIME   = SYSDATE,"+
	        " TESTSESSION_PER_DAY = ?,"+
	        " TOTAL_TESTING_DAYS  = ? "+
	        
	" WHEN NOT MATCHED THEN"+
	 " INSERT"+
	   " (SITE_SURVEY_ID,"+
	     "SUB_ALG1,"+
	     "SUB_ALG2,"+
	     "SUB_USHIS,"+
	     "SUB_BIO,"+
	    " SUB_GEO,"+
	    " SUB_ENG2,"+
	    " SUB_ENG3,"+
	    
		"SUB_ALG1_CHK , "+                                                                                                                                                                                                             
		"SUB_ALG2_CHK ,  "+                                                                                                                                                                                                            
		"SUB_USHIS_CHK ,   "+                                                                                                                                                                                                          
		"SUB_BIO_CHK ,       "+                                                                                                                                                                                                        
		"SUB_GEO_CHK ,         "+                                                                                                                                                                                                      
		"SUB_ENG2_CHK,           "+                                                                                                                                                                                                   
		"SUB_ENG3_CHK, "+
		
		" TESTSESSION_PER_DAY,"+
	    " TOTAL_TESTING_DAYS"+
	    " )"+
	 " VALUES "+
	    " (temp. SITE_SURVEY_ID,"+
	    " temp.SUB_ALG1,"+
	    " temp.SUB_ALG2,"+
	    " temp.SUB_USHIS,"+
	     "temp.SUB_BIO,"+
	    " temp.SUB_GEO,"+
	    " temp.SUB_ENG2,"+
	    " temp.SUB_ENG3,"+
	
	"temp.SUB_ALG1_CHK ,"+                                                                                                                                                                                                              
	"temp.SUB_ALG2_CHK ,  "+                                                                                                                                                                                                            
	"temp.SUB_USHIS_CHK ,   "+                                                                                                                                                                                                          
	"temp.SUB_BIO_CHK ,       "+                                                                                                                                                                                                        
	"temp.SUB_GEO_CHK ,         "+                                                                                                                                                                                                      
	"temp.SUB_ENG2_CHK,           "+                                                                                                                                                                                                   
	"temp.SUB_ENG3_CHK, "+
	
	" ?,"+
	" ?"+
	")";
		
		return sql;
	}

	/*public static String getSaveOrUpdateEnrollmentByGrade(String enrollmentTableName){
		String sql = " MERGE INTO "+enrollmentTableName+" ssenrollment USING (select ? AS SITE_SURVEY_ID, ? AS THIRD_GRADE, ? AS  FOURTH_GRADE, ? AS FIFTH_GRADE, ? AS SIXTH_GRADE, ? AS SEVENTH_GRADE,  ? AS EIGHTH_GRADE  from dual) temp ON (ssenrollment.site_survey_id = temp.site_survey_id ) WHEN MATCHED THEN   UPDATE  SET THIRD_GRADE  = temp.THIRD_GRADE,  FOURTH_GRADE  = temp.FOURTH_GRADE,  FIFTH_GRADE   = temp.FIFTH_GRADE,   SIXTH_GRADE   = temp.SIXTH_GRADE,  SEVENTH_GRADE  = temp.SEVENTH_GRADE, EIGHTH_GRADE  = temp.EIGHTH_GRADE, UPDATED_DATE_TIME = SYSDATE,TESTSESSION_PER_DAY=?,TOTAL_TESTING_DAYS=? WHEN NOT MATCHED  THEN   INSERT     (SITE_SURVEY_ID,      THIRD_GRADE,      FOURTH_GRADE,      FIFTH_GRADE,      SIXTH_GRADE,      SEVENTH_GRADE,      EIGHTH_GRADE, TESTSESSION_PER_DAY, TOTAL_TESTING_DAYS) VALUES   ( temp. SITE_SURVEY_ID, temp.THIRD_GRADE,   temp.FOURTH_GRADE,   temp.FIFTH_GRADE,  temp.SIXTH_GRADE, temp.SEVENTH_GRADE,  temp.EIGHTH_GRADE,?,?)";

		return sql;
	}
	public static String getSaveOrUpdateEnrollmentBySubject(String enrollmentTableName){
		String sql = " MERGE INTO "+enrollmentTableName+" ssenrollment USING (select ? AS SITE_SURVEY_ID, ? AS SUB_ALG1, ? AS  SUB_ALG2, ? AS SUB_USHIS, ? AS SUB_BIO, ? AS SUB_GEO,  ? AS SUB_ENG2, ? AS SUB_ENG3  from dual) temp ON (ssenrollment.site_survey_id = temp.site_survey_id ) WHEN MATCHED THEN   UPDATE  SET SUB_ALG1  = temp.SUB_ALG1,  SUB_ALG2  = temp.SUB_ALG2,  SUB_USHIS   = temp.SUB_USHIS,   SUB_BIO   = temp.SUB_BIO,  SUB_GEO  = temp.SUB_GEO, SUB_ENG2  = temp.SUB_ENG2, SUB_ENG3  = temp.SUB_ENG3, UPDATED_DATE_TIME = SYSDATE,TESTSESSION_PER_DAY=?,TOTAL_TESTING_DAYS=? WHEN NOT MATCHED  THEN   INSERT     (SITE_SURVEY_ID,      SUB_ALG1,      SUB_ALG2,      SUB_USHIS,      SUB_BIO,      SUB_GEO,      SUB_ENG2, SUB_ENG3, TESTSESSION_PER_DAY, TOTAL_TESTING_DAYS) VALUES   ( temp. SITE_SURVEY_ID, temp.SUB_ALG1,   temp.SUB_ALG2,   temp.SUB_USHIS,  temp.SUB_BIO, temp.SUB_GEO,  temp.SUB_ENG2, temp.SUB_ENG3,?,?)";

		return sql;
	}*/
	public static String getSiteSurveyIDBySchoolAndDistrictNo( ) {
		String sql =  "SELECT DISTINCT S_SITE.SITE_SURVEY_ID  FROM SITE_SURVEY S_SITE, SITE_SURVEY D_SITE WHERE  D_SITE.CUSTOMER_ID= ? AND D_SITE.SITE_ID = ? AND UPPER(D_SITE.SITE_TYPE) = UPPER ('CORPORATION') AND   S_SITE.SITE_ID = ?  AND  UPPER(S_SITE.SITE_TYPE) = UPPER ('SCHOOL')  AND TRIM(S_SITE.SITE_PATH) = (D_SITE.SITE_PATH||'/'||TRIM(S_SITE.SITE_NAME)) ";
		return sql;   
	}

	public static String getCustomerIDBySchoolAndDistrictNo( ) {
		String sql =  "SELECT DISTINCT S_SITE.CUSTOMER_ID  FROM SITE_SURVEY S_SITE, SITE_SURVEY D_SITE WHERE D_SITE.SITE_ID = ? AND UPPER(D_SITE.SITE_TYPE) = UPPER ('CORPORATION') AND   S_SITE.SITE_ID = ?  AND  UPPER(S_SITE.SITE_TYPE) = UPPER ('SCHOOL')  AND TRIM(S_SITE.SITE_PATH) = (D_SITE.SITE_PATH||'/'||TRIM(S_SITE.SITE_NAME)) ";
		return sql;
	}
		
	public static String getCustomerProperties(){

		return "SELECT CUSTOMER_TYPE CTYPE,PROPERTY_NAME NAME,PROPERTY_VALUE VALUE FROM SITE_SURVEY_CLIENT_TYPE CTYPE,SITE_SURVEY_CLIENT_PROP_VAL VALS WHERE CTYPE.CUSTOMER_ID = VALS.CUSTOMER_ID AND CTYPE.CUSTOMER_ID=?";
	}
	
	
	
}
