package com.ctb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ctb.bean.ManageStudent;
import com.ctb.bean.OrganizationNode;
import com.ctb.bean.StudentAccommodations;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.bean.StudentDemographicValue;
import com.ctb.bean.UploadStudent;
import com.ctb.utils.Constants;
import com.ctb.utils.SQLUtil;
import com.ctb.utils.StudentUtils;
import com.ctb.utils.cache.StudentNewRecordCacheImpl;
import com.ctb.utils.cache.StudentUpdateRecordCacheImpl;

public class StudentManagementDAO implements IStudentManagementDAO {

	private static Logger logger = Logger.getLogger(StudentManagementDAO.class.getName());
	//final int BATCH_SIZE = 998;
	final int BATCH_SIZE_LARGE = 4000;
	
	public void populateActualStudentUserName(StudentNewRecordCacheImpl newStdRecordCacheImpl,
			String userNames, Integer newStudentCount) throws Exception {
		logger.info("executing populateActualStudentUserName()");
		Set<String> newSet = new HashSet<String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	ResultSet rSet = null;
	   	String usernameQuery = "SELECT USER_NAME FROM STUDENT WHERE USER_NAME ";
		try{
			if(null != userNames){
		   		conn = SQLUtil.getConnection();
		   		if(!userNames.contains("#")){
		   			usernameQuery +=  userNames;
		   			pstmt = conn.prepareStatement(usernameQuery);
			   		pstmt.setFetchSize(20);
			   		rSet = pstmt.executeQuery();
			   		while (rSet.next()){
			   			newSet.add(rSet.getString("user_name"));
			   		}
		   		}else {
		   			String [] splitedStr = userNames.split("#");
			   		for(int i=0; i<splitedStr.length;i++){
			   			if(null==splitedStr[i])
			   				break;
			   			String query = usernameQuery + splitedStr[i].toString();
			   			pstmt = conn.prepareStatement(query);
				   		
				   		pstmt.setFetchSize(20);
				   		rSet = pstmt.executeQuery();
				   		while (rSet.next()){
				   			newSet.add(rSet.getString("user_name"));
				   		}
				   		SQLUtil.closeDbObjects(null, pstmt, rSet);
			   		}
				}
			}
			List<String> keys = newStdRecordCacheImpl.getKeys();
	   		for(String key : keys){
	   			if(newStdRecordCacheImpl.getNewStudent(key) != null){
	   				UploadStudent uploadStudent = ((UploadStudent)newStdRecordCacheImpl.getNewStudent(key));
	   				uploadStudent.getManageStudent().setLoginId(StudentUtils.generateUniqueStudentUserName(newSet, uploadStudent.getManageStudent()));
	   				newStdRecordCacheImpl.addNewStudent(key, uploadStudent);
	   			}
	   		}	   	
	   	}catch(Exception e){
	   		logger.error("Exception in createDataFileTemp");
	   		e.printStackTrace();
	   		throw e;
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, rSet);
        	logger.info("Username population completed");
        }
		
	}
	
	public void populateActualStudentIds(StudentNewRecordCacheImpl newStdRecordCacheImpl, Integer newStudentCount , 
														Map<String,Integer> studentIdExtPinMap) throws Exception {
		

		logger.info("executing populateActualStudentIds");
		Integer [] studentIds = null;//TODO 
		ArrayList<Integer> stdIds = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	ResultSet rSet = null;	   	
	   	String seqQuery = "select seq_student_id.nextval from dual  connect by level<= ?"; 
		try{
			conn = SQLUtil.getConnection();
	   		pstmt  =  conn.prepareStatement(seqQuery);
	   		pstmt.setInt(1, newStudentCount.intValue());
	   		rSet = pstmt.executeQuery();
	   		int count = 0;
	   		while(rSet.next()){
	   			stdIds.add(rSet.getInt(1));
	   			count++;
	   		}
	   		
	   		studentIds = stdIds.toArray(new Integer[stdIds.size()]);
	   		count = 0;
	   		List<String> keys = newStdRecordCacheImpl.getKeys();
	   		for(String key : keys){
	   			if(newStdRecordCacheImpl.getNewStudent(key) != null){
	   				Integer studentId = studentIds[count];
	   				UploadStudent uploadStudent = ((UploadStudent)newStdRecordCacheImpl.getNewStudent(key));
	   				uploadStudent.getManageStudent().setId(studentId);
	   				uploadStudent.getStudentAccommodations().setStudentId(studentId);
	   				studentIdExtPinMap.put(uploadStudent.getManageStudent().getStudentIdNumber().trim(), studentId);
	   				newStdRecordCacheImpl.addNewStudent(key, uploadStudent);
	   				count++;
	   			}
	   			
	   		}	
	   		
	   	}catch(Exception e){
	   		logger.error("Exception in populateActualStudentIds");
	   		e.printStackTrace();
	   		throw e;
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, rSet);
        	logger.info("Student Ids population completed");
        	
        }
	   
		
	}

	public void insertStudentDetails(StudentNewRecordCacheImpl newStdRecordCacheImpl) throws Exception {

		logger.info("executing insertStudentDetails()");
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	int count = 0;
		String insertQuery = " insert into student (STUDENT_ID, USER_NAME, FIRST_NAME, MIDDLE_NAME,   LAST_NAME, BIRTHDATE,   GENDER, GRADE, EXT_PIN1, EXT_PIN2, CREATED_DATE_TIME, ACTIVATION_STATUS, CREATED_BY, CUSTOMER_ID ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ? )";
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(insertQuery);
			List<String> keys = newStdRecordCacheImpl.getKeys();
	   		for(String key : keys){
	   			UploadStudent uploadStudent = ((UploadStudent)newStdRecordCacheImpl.getNewStudent(key));
	   				ManageStudent std = uploadStudent.getManageStudent();
	   				pstmt.setInt(1, std.getId());
					pstmt.setString(2,std.getLoginId());
					pstmt.setString(3,std.getFirstName());
					pstmt.setString(4,std.getMiddleName());
					pstmt.setString(5,std.getLastName());
					pstmt.setDate(6, new java.sql.Date(std.getBirthDate().getTime()));
					pstmt.setString(7,std.getGender());
					pstmt.setString(8,std.getGrade());
					pstmt.setString(9,std.getStudentIdNumber());
					pstmt.setString(10,std.getStudentIdNumber2());
					pstmt.setString(11,"AC");
					pstmt.setInt(12,Constants.USER_ID);				
					pstmt.setInt(13,std.getCustomerId());					
					pstmt.addBatch();
					count++;
					if(count % BATCH_SIZE_LARGE == 0){
						pstmt.executeBatch();
						logger.info("Student Inserted count->"+count);
					}
			}
			pstmt.executeBatch();
			
			   		
	   	}catch(Exception e){	   		
	   		logger.error("Exception in insertStudentDetails");
	   		e.printStackTrace();	
	   		throw e;
	   	}
	   	finally {	   		
        	SQLUtil.closeDbObjects(conn, pstmt, null);

    		logger.info("done executing insertStudentDetails()");
        }
		
	}

	public void createStudentAccommodations(StudentNewRecordCacheImpl newStdRecordCacheImpl)
			throws Exception {

		logger.info(" executing createStudentAccommodations()");
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	int count = 0;
	   	String insertQuery = "insert into  student_accommodation ( STUDENT_ID, SCREEN_MAGNIFIER, SCREEN_READER, CALCULATOR, TEST_PAUSE, UNTIMED_TEST, HIGHLIGHTER, QUESTION_BACKGROUND_COLOR, QUESTION_FONT_COLOR, QUESTION_FONT_SIZE, ANSWER_BACKGROUND_COLOR, ANSWER_FONT_COLOR, ANSWER_FONT_SIZE, MASKING_RULER, MUSIC_FILE_ID, MAGNIFYING_GLASS, EXTENDED_TIME, MASKING_TOOL, MICROPHONE_HEADPHONE ) values (  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"; 
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(insertQuery);
			List<String> keys = newStdRecordCacheImpl.getKeys();
	   		for(String key : keys){
	   			UploadStudent uploadStudent = ((UploadStudent)newStdRecordCacheImpl.getNewStudent(key));
	   				
					StudentAccommodations std = uploadStudent.getStudentAccommodations();
					pstmt.setInt(1, std.getStudentId());
					pstmt.setString(2, std.getScreenMagnifier());
					pstmt.setString(3, std.getScreenReader());
					pstmt.setString(4, std.getCalculator());
					pstmt.setString(5, std.getTestPause());
					pstmt.setString(6, std.getUntimedTest());
					pstmt.setString(7, std.getHighlighter());
					pstmt.setString(8, std.getQuestionBackgroundColor());
					pstmt.setString(9, std.getQuestionFontColor());
					pstmt.setString(10, std.getQuestionFontSize());
					pstmt.setString(11, std.getAnswerBackgroundColor());
					pstmt.setString(12, std.getAnswerFontColor());
					pstmt.setString(13, std.getAnswerFontSize());
					pstmt.setString(14, std.getMaskingRuler());
					pstmt.setString(15, std.getMusicFile());
					pstmt.setString(16, std.getMagnifyingGlass());
					pstmt.setString(17, std.getExtendedTime());
					pstmt.setString(18, std.getMaskingTool());
					pstmt.setString(19, std.getMicrophoneHeadphone());							
				
					pstmt.addBatch();
					count++;
					if(count % BATCH_SIZE_LARGE == 0){
						pstmt.executeBatch();
						logger.info("Student_accommodation Inserted count->"+count);
					}
			}
			pstmt.executeBatch();
		}catch(Exception e){
	   		logger.error("Exception in createStudentAccommodations");
	   		e.printStackTrace();
	   		throw e;
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);

			logger.info("done executing createStudentAccommodations()");
        }
	}

	public void createStudentDemographicData(StudentNewRecordCacheImpl newStdRecordCacheImpl) throws Exception {

		logger.info(" executing createStudentDemographicData");
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	int count = 0;
	   	String insertQuery = "insert into  student_demographic_data (  student_demographic_data_id, student_id, customer_demographic_id, value_name, value, created_by, created_date_time ) values ( SEQ_STUDENT_DEMOGRAPHIC_ID.nextval , ?, ?,  ?,  ?,  ?,  SYSDATE )";
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(insertQuery);
			List<String> keys = newStdRecordCacheImpl.getKeys();
	   		for(String key : keys){
	   			UploadStudent uploadStudent = ((UploadStudent)newStdRecordCacheImpl.getNewStudent(key));
	   				ManageStudent st = uploadStudent.getManageStudent();				
					StudentDemoGraphics[] studentDemoGraphics = uploadStudent.getStudentDemographic();
					for(StudentDemoGraphics std:studentDemoGraphics){
						StudentDemographicValue [] studentDemographicValues = std.getStudentDemographicValues();
						for(StudentDemographicValue sdv:studentDemographicValues){
							pstmt.setInt(1, st.getId());
							pstmt.setInt(2, std.getId());
							pstmt.setString(3, sdv.getValueName());
							pstmt.setString(4,sdv.getValueCode());
							pstmt.setInt(5, Constants.USER_ID);
							
							pstmt.addBatch();
							count++;
							if(count % BATCH_SIZE_LARGE == 0){
								pstmt.executeBatch();
								logger.info("Student_demographic_data Inserted count->"+count);
							}
						}
			   }
			}
			pstmt.executeBatch();
		}catch(Exception e){
	   		logger.error("Exception in createStudentAccommodations");
	   		e.printStackTrace();
	   		throw e;
	   	}
	   	finally {
	   		SQLUtil.closeDbObjects(conn, pstmt, null);
			logger.info(" done executing createStudentDemographicData");
        }
	}

	public void createOrgnodeStudent(StudentNewRecordCacheImpl newStdRecordCacheImpl)
			throws Exception {

		logger.info("executing createOrgnodeStudent()");
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	int count = 0;
	   	String insertQuery = "insert into  org_node_student ( student_id, org_node_id, created_date_time, created_by, customer_id,  activation_status) values ( ?, ?, SYSDATE , ?, ?, ? )"; 
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(insertQuery);
			List<String> keys = newStdRecordCacheImpl.getKeys();
	   		for(String key : keys){
	   			UploadStudent uploadStudent = ((UploadStudent)newStdRecordCacheImpl.getNewStudent(key));
	   				ManageStudent ms = uploadStudent.getManageStudent();
					OrganizationNode [] organizationNodes = ms.getOrganizationNodes();
					
					for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) {
						pstmt.setInt(1, ms.getId());
						pstmt.setInt(2, organizationNodes[i].getOrgNodeId());
						pstmt.setInt(3, Constants.USER_ID);
						pstmt.setInt(4, ms.getCustomerId());
						pstmt.setString(5, "AC");
						
						pstmt.addBatch();
						count++;
						if(count % BATCH_SIZE_LARGE == 0){
							pstmt.executeBatch();
							logger.info("Org_node_student Inserted count->"+count);
						}
					}
			}
			pstmt.executeBatch();
		}catch(Exception e){
	   		logger.error("Exception in createStudentAccommodations");
	   		e.printStackTrace();
	   		throw e;
	   	}
	   	finally {
	   		SQLUtil.closeDbObjects(conn, pstmt, null);

			logger.info("done executing createOrgnodeStudent()");
        }
	}
	

	public void updateStudent(StudentUpdateRecordCacheImpl updateStdRecordCacheImpl, Map<String,Integer> studentIdExtPinMap ) throws Exception {
		logger.info("executing updateStudent");
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "update student set  first_name = ?, middle_name = ?, last_name  = ?, gender  = ?, birthdate = ?, grade = ?, ext_pin1  = ?, ext_pin2 = ?,  updated_By = ?, updated_Date_Time = SYSDATE  where student_id = ?";
		int counter = 0;
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(query);
			List<String> keys = updateStdRecordCacheImpl.getKeys();
	   		for(String key : keys){
	   			UploadStudent upload = ((UploadStudent)updateStdRecordCacheImpl.getUpdatedStudent(key));
	   			ManageStudent mStud = upload.getManageStudent();
	   			
	   			Integer studentId = 0;
				if (mStud.getId()==null){
					/*
					 * For same students in a single file.
					 * */
					studentId = studentIdExtPinMap.get(mStud.getStudentIdNumber().trim());
					mStud.setId(studentId);
					upload.getStudentAccommodations().setStudentId(studentId);
					updateStdRecordCacheImpl.addUpdatedStudent(key, upload);
					//upStd.getStudent().setStudentId(studentId);
				}else{
					studentId =mStud.getId();
				}
	   			
				//setOrganizationNodeForStudent(mStud, studentOrgMap);
				
				//pstmt.setString(1, mStud.getLoginId());
				pstmt.setString(1, mStud.getFirstName());
				pstmt.setString(2, mStud.getMiddleName());
				pstmt.setString(3, mStud.getLastName());
				pstmt.setString(4, mStud.getGender());
				pstmt.setDate(5, new java.sql.Date(mStud.getBirthDate().getTime()));
				pstmt.setString(6, mStud.getGrade());
				pstmt.setString(7, mStud.getStudentIdNumber());
				pstmt.setString(8, mStud.getStudentIdNumber2());
				pstmt.setInt(9, 1);
				pstmt.setInt(10, studentId);
				pstmt.addBatch();
				counter++;
				if(counter % BATCH_SIZE_LARGE == 0){
					pstmt.executeBatch();
					logger.info("Student updated count->"+counter);
				}
			}
			pstmt.executeBatch();
		}catch(SQLException e){
	   		logger.error("SQL Exception in updateStudent-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in updateStudent");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);
        	logger.info("done executing updateStudent");
        }
	}
	
	public void populateStudentAccommodation(String studentIds,	Map<Integer, StudentAccommodations> studentAccomMap)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "select  distinct accom.STUDENT_ID as studentId,  accom.SCREEN_MAGNIFIER as screenMagnifier,  accom.SCREEN_READER as screenReader,  accom.CALCULATOR as calculator,  accom.TEST_PAUSE as testPause,  accom.UNTIMED_TEST as untimedTest,  accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,  accom.QUESTION_FONT_COLOR as questionFontColor,  accom.QUESTION_FONT_SIZE as questionFontSize,  accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,  accom.ANSWER_FONT_COLOR as answerFontColor,  accom.ANSWER_FONT_SIZE as answerFontSize from  student_accommodation accom,  student stu where  accom.student_id = stu.student_id  and stu.activation_status = 'AC' and stu.student_id in ("+studentIds+")";
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(query);
//			pstmt.setString(1, studentIds);
			rs = pstmt.executeQuery();
			while(rs.next()){
				StudentAccommodations oldAccom = new StudentAccommodations();
				oldAccom.setStudentId(rs.getInt(1));
				oldAccom.setScreenMagnifier(rs.getString(2));
				oldAccom.setScreenReader(rs.getString(3));
				oldAccom.setCalculator(rs.getString(4));
				oldAccom.setTestPause(rs.getString(5));
				oldAccom.setUntimedTest(rs.getString(6));
				oldAccom.setQuestionBackgroundColor(rs.getString(7));
				oldAccom.setQuestionFontColor(rs.getString(8));
				oldAccom.setQuestionFontSize(rs.getString(9));
				oldAccom.setAnswerBackgroundColor(rs.getString(10));
				oldAccom.setAnswerFontColor(rs.getString(11));
				oldAccom.setAnswerFontSize(rs.getString(12));
				
				studentAccomMap.put(rs.getInt(1), oldAccom);
			}
			
		}catch(SQLException e){
	   		logger.error("SQL Exception in populateStudentAccommodation-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in populateStudentAccommodation");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, rs);
        	logger.info("Student accomidation population ended...");
        }
		
	}
	
	public void populateStudentDemoValue(String studentIds, String demographicIds, Map<Integer, HashMap<Integer, ArrayList<StudentDemographicValue>>> studentDemoMap) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<StudentDemographicValue> demoValueList = null;
		HashMap<Integer, ArrayList<StudentDemographicValue>> newMap = null;
		String query = "select sdda.student_Id as studentId, sdv.customer_demographic_id as customerDemographicId, sdv.value_name as valueName, sdv.value_code as valueCode, sdv.sort_order as sortOrder, sdv.visible as visible, (select decode(count(*), 0, 'false', 'true') as sddcount from student_demographic_data sdd where sdd.CUSTOMER_DEMOGRAPHIC_ID = sdv.CUSTOMER_DEMOGRAPHIC_ID and sdd.VALUE_NAME = sdv.value_name and sdd.STUDENT_ID = sdda.student_id) as selectedFlag from customer_demographic_value sdv, student_demographic_data sdda where sdda.customer_demographic_id = sdv.customer_demographic_id ";
		try{
			if ((null != demographicIds && !"".equals(demographicIds)) && (null != studentIds && !"".equals(studentIds))) {
				conn = SQLUtil.getConnection();
				query += "AND sdv.customer_demographic_id IN ("+demographicIds+") AND sdda.student_Id IN ( "+studentIds+") order by sdv.sort_order, sdv.value_name";
				pstmt = conn.prepareStatement(query);
				pstmt.setFetchSize(100);
				rs = pstmt.executeQuery();
				while(rs.next()){
					demoValueList = new ArrayList<StudentDemographicValue>(); 
					newMap = new HashMap<Integer, ArrayList<StudentDemographicValue>>();
					Integer studentId = rs.getInt(1);
					Integer customerDemographicId = rs.getInt(2);
					StudentDemographicValue demoValue = new StudentDemographicValue();
					demoValue.setCustomerDemographicId(customerDemographicId);
					demoValue.setValueName(rs.getString(3));
					demoValue.setValueCode(rs.getString(4));
					demoValue.setSortOrder(rs.getInt(5));
					demoValue.setVisible(rs.getString(6));
					demoValue.setSelectedFlag(rs.getString(7));
					demoValueList.add(demoValue);
					
					if (studentDemoMap.containsKey(studentId)) {
						if (studentDemoMap.get(studentId).containsKey(
								customerDemographicId)) {
							studentDemoMap.get(studentId)
									.get(customerDemographicId).add(demoValue);
						} else {
							studentDemoMap.get(studentId).put(
									customerDemographicId, demoValueList);
						}
					} else {
						newMap.put(customerDemographicId, demoValueList);
						studentDemoMap.put(studentId, newMap);
					}
				}
			}
		}catch(SQLException e){
	   		logger.error("SQL Exception in populateStudentDemoValue-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in populateStudentDemoValue");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, rs);
        }
		
	}
	

	public void updateAccommodation(
			StudentUpdateRecordCacheImpl updateStdRecordCacheImpl)
			throws Exception {
		logger.info("UpdateAccommodation Executing...");
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		String query = "MERGE INTO STUDENT_ACCOMMODATION D "
				+ "USING ( SELECT sa.student_id FROM STUDENT sa WHERE sa.student_id = ? ) S "
				+ "ON (D.student_id = S.student_id)" + "WHEN MATCHED THEN "
				+ "UPDATE " + "SET D.SCREEN_MAGNIFIER   = ? ,"
				+ "D.SCREEN_READER             = ?, "
				+ "D.CALCULATOR                = ?, "
				+ "D.TEST_PAUSE                = ?, "
				+ "D.UNTIMED_TEST              = ?, "
				+ "D.HIGHLIGHTER			   = ?, "
				+ "D.QUESTION_BACKGROUND_COLOR = ?, "
				+ "D.QUESTION_FONT_COLOR       = ?, "
				+ "D.QUESTION_FONT_SIZE        = ?, "
				+ "D.ANSWER_BACKGROUND_COLOR   = ?, "
				+ "D.ANSWER_FONT_COLOR         = ?, "
				+ "D.ANSWER_FONT_SIZE          = ?, "
				+ "D.MASKING_RULER             = ?, "
				+ "D.MUSIC_FILE_ID             = ?, "
				+ "D.MAGNIFYING_GLASS          = ?, "
				+ "D.EXTENDED_TIME             = ?, "
				+ "D.MASKING_TOOL              = ?, "
				+ "D.MICROPHONE_HEADPHONE      = ? " + " WHEN NOT MATCHED THEN "
				+ "INSERT " + "(D.student_id, " + "D.SCREEN_MAGNIFIER, "
				+ "D.SCREEN_READER, " + "D.CALCULATOR, " + "D.TEST_PAUSE, "
				+ "D.UNTIMED_TEST, " + "D.HIGHLIGHTER, " +"D.QUESTION_BACKGROUND_COLOR, "
				+ "D.QUESTION_FONT_COLOR, " + "D.QUESTION_FONT_SIZE, "
				+ "D.ANSWER_BACKGROUND_COLOR, " + "D.ANSWER_FONT_COLOR, "
				+ "D.ANSWER_FONT_SIZE, "
				+ "D.MASKING_RULER, "  +  "D.MUSIC_FILE_ID, " + "D.MAGNIFYING_GLASS, "
				+ "D.EXTENDED_TIME, " + "D.MASKING_TOOL, "
				+ "D.MICROPHONE_HEADPHONE) " + "VALUES "
				+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		try {
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(query);
			List<String> keys = updateStdRecordCacheImpl.getKeys();
			for (String key : keys) {
				UploadStudent uploadStudent = ((UploadStudent) updateStdRecordCacheImpl
						.getUpdatedStudent(key));
				StudentAccommodations std = uploadStudent
						.getStudentAccommodations();
				
				pstmt.setInt(1, std.getStudentId());
				pstmt.setString(2, std.getScreenMagnifier());
				pstmt.setString(3, std.getScreenReader());
				pstmt.setString(4, std.getCalculator());
				pstmt.setString(5, std.getTestPause());
				pstmt.setString(6, std.getUntimedTest());
				pstmt.setString(7, std.getHighlighter());
				pstmt.setString(8, std.getQuestionBackgroundColor());
				pstmt.setString(9, std.getQuestionFontColor());
				pstmt.setString(10, std.getQuestionFontSize());
				pstmt.setString(11, std.getAnswerBackgroundColor());
				pstmt.setString(12, std.getAnswerFontColor());
				pstmt.setString(13, std.getAnswerFontSize());
				pstmt.setString(14, std.getMaskingRuler());
				pstmt.setString(15, std.getMusicFile());
				pstmt.setString(16, std.getMagnifyingGlass());
				pstmt.setString(17, std.getExtendedTime());
				pstmt.setString(18, std.getMaskingTool());
				pstmt.setString(19, std.getMicrophoneHeadphone());

				pstmt.setInt(20, std.getStudentId());
				pstmt.setString(21, std.getScreenMagnifier());
				pstmt.setString(22, std.getScreenReader());
				pstmt.setString(23, std.getCalculator());
				pstmt.setString(24, std.getTestPause());
				pstmt.setString(25, std.getUntimedTest());
				pstmt.setString(26, std.getHighlighter());
				pstmt.setString(27, std.getQuestionBackgroundColor());
				pstmt.setString(28, std.getQuestionFontColor());
				pstmt.setString(29, std.getQuestionFontSize());
				pstmt.setString(30, std.getAnswerBackgroundColor());
				pstmt.setString(31, std.getAnswerFontColor());
				pstmt.setString(32, std.getAnswerFontSize());
				pstmt.setString(33, std.getMaskingRuler());
				pstmt.setString(34, std.getMusicFile());
				pstmt.setString(35, std.getMagnifyingGlass());
				pstmt.setString(36, std.getExtendedTime());
				pstmt.setString(37, std.getMaskingTool());
				pstmt.setString(38, std.getMicrophoneHeadphone());
				
				pstmt.addBatch();
				count++;
				if (count % BATCH_SIZE_LARGE == 0) {
					pstmt.executeBatch();
					logger.info("Update Accommodation count ->" + count);
				}
			}
			pstmt.executeBatch();

		} catch (SQLException e) {
			logger.error("SQL Exception in updateAccommodation-- >"
					+ e.getErrorCode());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in updateAccommodation");
			e.printStackTrace();
		} finally {
			SQLUtil.closeDbObjects(conn, pstmt, null);
			logger.info("UpdateAccommodation Complete...");
		}

	}
	
	public void setRosterUpdateFlag(String studentIds) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "update test_roster set updated_date_time = updated_date_time where student_id in ("+studentIds+") and test_completion_status not in ('CO','NT')";
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(query);
//			pstmt.setString(1, studentIds);
			pstmt.executeUpdate();
		}catch(SQLException e){
	   		logger.error("SQL Exception in setRosterUpdateFlag-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in setRosterUpdateFlag");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);
        	logger.info("Roster Update Flag update completed...");
        }

		
		
	}
	
	//public void createStudentDemographicDataDuringUpdate(List<StudentDemographicData> insertStudentDemoList) throws Exception {
	public void createStudentDemographicDataDuringUpdate(StudentUpdateRecordCacheImpl updateStdRecordCacheImpl) throws Exception {
		logger.info("Demographic Inserted during update executing ");
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "insert into student_demographic_data (student_demographic_data_id, student_id, customer_demographic_id, value_name, value, created_by, created_date_time , updated_by , updated_date_time  ) values ( SEQ_STUDENT_DEMOGRAPHIC_ID.nextval , ?, ?,  ?,  ?,  ?,  SYSDATE , ? , SYSDATE)";
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(query);
			List<String> keys = updateStdRecordCacheImpl.getKeys();
	   		for(String key : keys){
	   			UploadStudent uploadStudent = ((UploadStudent)updateStdRecordCacheImpl.getUpdatedStudent(key));
	   				ManageStudent st = uploadStudent.getManageStudent();				
					StudentDemoGraphics[] studentDemoGraphics = uploadStudent.getStudentDemographic();
					for(StudentDemoGraphics std:studentDemoGraphics){
						StudentDemographicValue [] studentDemographicValues = std.getStudentDemographicValues();
						for(StudentDemographicValue sdv:studentDemographicValues){
							pstmt.setInt(1, st.getId());
							pstmt.setInt(2, std.getId());
							pstmt.setString(3, sdv.getValueName());
							pstmt.setString(4,sdv.getValueCode());
							pstmt.setInt(5, Constants.USER_ID);
							pstmt.setInt(6, Constants.USER_ID);
							pstmt.addBatch();
							count++;
							if(count % BATCH_SIZE_LARGE == 0){
								pstmt.executeBatch();
								logger.info("Demographic Inserted during update : --> "+count);
							}
						}
			   }
			}
			pstmt.executeBatch();
			
		}catch(Exception e){
	   		logger.error("Exception in createStudentDemographicDataDuringUpdate");
	   		e.printStackTrace();
	   		throw e;
	   	}
	   	finally {
	   		SQLUtil.closeDbObjects(conn, pstmt, null);
	   		logger.info("Demographic Inserted during update completed ");
        }
	}


	
	
	
	public void deleteVisibleStudentDemographicDataForStudent(String studentIds) throws Exception {
		logger.info("Demographic Update : Deletion exceuting ");
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "delete  from  student_demographic_data  where student_id in ( ";
		try{
			conn = SQLUtil.getConnection();
			if(!studentIds.contains("#")){
				query += studentIds + ")";
				pstmt  = conn.prepareStatement(query);
				pstmt.execute();
			}else{
				String [] splitedstr = studentIds.split("#");
				for(int indx=0; indx<splitedstr.length;indx++){
					if(splitedstr[indx] == null)
						continue;
					String splitQuery = query + splitedstr[indx].toString()+")";
					pstmt  = conn.prepareStatement(splitQuery);
					pstmt.execute();
					SQLUtil.closeDbObjects(null, pstmt, null);
				}
			}
		}catch(SQLException e){
	   		logger.error("SQL Exception in deleteVisibleStudentDemographicDataForStudent-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in deleteVisibleStudentDemographicDataForStudent");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);
        	logger.info("Done Demographic Update : Deletion exceuting ");
        }
	}
	
	public void createOrgnodeStudentDuringUpdate( StudentUpdateRecordCacheImpl updateStdRecordCacheImpl) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		String query = " Merge into org_node_student ons using (select ?  as Student_id, ?  as Org_node_id from dual) s on (ons.student_id = s.Student_id and ons.org_node_id = s.org_node_id) when matched then update set activation_status = 'AC' , updated_date_time = sysdate , updated_by = 1 when not matched then insert (student_id, org_node_id, created_date_time, created_by, customer_id, activation_status) values ( ? , ? , sysdate , 1 , ? , 'AC' )";
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(query);
			
			List<String> keys = updateStdRecordCacheImpl.getKeys();
	   		for(String key : keys){
	   			UploadStudent updateStudent = ((UploadStudent)updateStdRecordCacheImpl.getUpdatedStudent(key));
	   			
				Integer studentId = updateStudent.getManageStudent().getId();
				OrganizationNode [] organizationNodes = updateStudent.getManageStudent().getOrganizationNodes();
				for(OrganizationNode org:organizationNodes){
					Integer orgNodeId = org.getOrgNodeId();
					pstmt.setInt(1, studentId);
					pstmt.setInt(2, orgNodeId);
					pstmt.setInt(3, studentId);
					pstmt.setInt(4, orgNodeId);
					pstmt.setInt(5, updateStudent.getManageStudent().getCustomerId());
					pstmt.addBatch();
					
					count++;
					if(count % BATCH_SIZE_LARGE == 0){
						pstmt.executeBatch();
						//logger.info("Org_node_Student Inserted/Updated : --> "+count);
					}
				}
	   		}
			
			pstmt.executeBatch();
		}catch(SQLException e){
	   		logger.error("SQL Exception in createOrgnodeStudentDuringUpdate-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in createOrgnodeStudentDuringUpdate");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);
        }
		
	}

	private static boolean isOrganizationPresent (Integer orgNodeId, OrganizationNode []organizationNode) {

		for (int i = 0; i < organizationNode.length ; i++) {
			OrganizationNode tempNode = organizationNode[i];
			if (orgNodeId.intValue() == tempNode.getOrgNodeId().intValue()) {
				return true;
			}
		}
		return false;

	}

}
