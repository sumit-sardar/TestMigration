package com.ctb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import oracle.jdbc.driver.OraclePreparedStatement;

import org.apache.log4j.Logger;

import com.ctb.bean.ManageStudent;
import com.ctb.bean.OrganizationNode;
import com.ctb.bean.Student;
import com.ctb.bean.StudentAccommodations;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.bean.StudentDemographicData;
import com.ctb.bean.StudentDemographicValue;
import com.ctb.bean.UploadStudent;
import com.ctb.utils.Constants;
import com.ctb.utils.SQLUtil;
import com.ctb.utils.StudentUtils;

public class StudentManagementDAO implements IStudentManagementDAO {

	private static Logger logger = Logger.getLogger(StudentManagementDAO.class.getName());
	final int BATCH_SIZE = 998;
	
	public OrganizationNode[] getAssignedOrganizationNodesForStudentAtAndBelowUserTopNodes(
			int studentId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public StudentAccommodations getStudentAccommodations(Integer studentId)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setRosterUpdateFlag(Integer studentId) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void deleteVisibleStudentDemographicDataForStudent(Integer studentId)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public Integer getNextPKForStudentDemographicData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void createStudentDemographicData(StudentDemographicData sdd)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public Integer[] getTopOrgNodeIdsForUser(String username)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateStudent(ManageStudent student,
			Date updatedDateTime) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void deleteStudentDemographicDataForStudentAndCustomerDemographic(
			Integer studentId, Integer customerDemographicId)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public StudentDemographicValue[] getStudentDemographicValues(
			int customerDemographicId, int studentId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void populateActualStudentUserName(List<UploadStudent> finalStudentList,
			String userNames, Integer newStudentCount) throws Exception {
		
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
			
	   		for(UploadStudent uStud : finalStudentList){
	   			uStud.getStudent().setUserName(StudentUtils.generateUniqueStudentUserName(newSet, uStud.getStudent()));
	   		}	   	
	   	}catch(Exception e){
	   		logger.error("Exception in createDataFileTemp");
	   		e.printStackTrace();
	   		throw e;
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, rSet);
        }
		
	}
	
	public List<UploadStudent> populateActualStudentIds(List<UploadStudent> finalStudentList, Integer newStudentCount) throws Exception {
		
		Integer [] studentIds = new Integer[finalStudentList.size()];
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
	   			studentIds[count] = rSet.getInt(1);
	   			count++;
	   		}
	   		count = 0;
	   		for(UploadStudent uStud : finalStudentList){
	   			Student student = uStud.getStudent();
	   			student.setStudentId(studentIds[count]);
	   			uStud.getStudentAccommodations().setStudentId(student.getStudentId());
	   			uStud.getManageStudent().setId(student.getStudentId());
	   			count++;
	   		}
	   		
	   	}catch(Exception e){
	   		logger.error("Exception in populateActualStudentIds");
	   		e.printStackTrace();
	   		throw e;
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, rSet);
        	
        }
	   	return finalStudentList;
		
	}

	public void insertStudentDetails(List<UploadStudent> finalStudentList) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	int count = 0;
		String insertQuery = " insert into  student ( STUDENT_ID, USER_NAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, PREFERRED_NAME, LAST_NAME, BIRTHDATE, ETHNICITY, GENDER, EMAIL, GRADE, EXT_PIN1, EXT_ELM_ID, EXT_PIN2, EXT_PIN3, CREATED_DATE_TIME, ACTIVE_SESSION, ACTIVATION_STATUS, POTENTIAL_DUPLICATED_STUDENT, CREATED_BY , EXT_SCHOOL_ID, PREFIX, SUFFIX,  CUSTOMER_ID, UDF, UDF_1, UDF_2, TEST_PURPOSE, OUT_OF_SCHOOL) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		try{
			conn = SQLUtil.getConnection();
			//conn.setAutoCommit(false);
			pstmt  = conn.prepareStatement(insertQuery);
			for(UploadStudent uploadStudent : finalStudentList){
					Student std = uploadStudent.getStudent();
					pstmt.setInt(1, std.getStudentId());
					pstmt.setString(2,std.getUserName());
					pstmt.setString(3,std.getPassword());
					pstmt.setString(4,std.getFirstName());
					pstmt.setString(5,std.getMiddleName());
					pstmt.setString(6,std.getPreferredName());
					pstmt.setString(7,std.getLastName());
					pstmt.setDate(8, new java.sql.Date(std.getBirthdate().getTime()));
					pstmt.setString(9,std.getEthnicity());
					pstmt.setString(10,std.getGender());
					pstmt.setString(11,std.getEmail());
					pstmt.setString(12,std.getGrade());
					pstmt.setString(13,std.getExtPin1());
					pstmt.setString(14,std.getExtElmId());
					pstmt.setString(15,std.getExtPin2());
					pstmt.setString(16,std.getExtPin3());
					pstmt.setDate(17,new java.sql.Date(System.currentTimeMillis()));
					pstmt.setString(18,std.getActiveSession());
					pstmt.setString(19,std.getActivationStatus());
					pstmt.setString(20,std.getPotentialDuplicatedStudent());
					pstmt.setInt(21,Constants.USER_ID);				
					pstmt.setString(22,std.getExtSchoolId());
					pstmt.setString(23,std.getPrefix());
					pstmt.setString(24,std.getSuffix());					
					pstmt.setInt(25,std.getCustomerId());					
					pstmt.setString(26,std.getUdf());
					pstmt.setString(27,std.getUdf1());
					pstmt.setString(28,std.getUdf2());
					pstmt.setString(29,std.getTestPurpose());
					pstmt.setString(30,std.getOutOfSchool());				
					
					pstmt.addBatch();
					count++;
					if(count % BATCH_SIZE == 0){
						pstmt.executeBatch();
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
        }
		
	}

	public void createStudentAccommodations(List<UploadStudent> finalStudentList)
			throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	int count = 0;
	   	String insertQuery = "insert into  student_accommodation ( STUDENT_ID, SCREEN_MAGNIFIER, SCREEN_READER, CALCULATOR, TEST_PAUSE, UNTIMED_TEST, HIGHLIGHTER, QUESTION_BACKGROUND_COLOR, QUESTION_FONT_COLOR, QUESTION_FONT_SIZE, ANSWER_BACKGROUND_COLOR, ANSWER_FONT_COLOR, ANSWER_FONT_SIZE, MASKING_RULER, MUSIC_FILE_ID, MAGNIFYING_GLASS, EXTENDED_TIME, MASKING_TOOL, MICROPHONE_HEADPHONE ) values (  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"; 
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(insertQuery);
			for(UploadStudent uploadStudent : finalStudentList){
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
					if(count % BATCH_SIZE == 0){
						pstmt.executeBatch();
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
        }
	}

	public void createStudentDemographicData(List<UploadStudent> finalStudentList) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	int count = 0;
	   	String insertQuery = "insert into  student_demographic_data (  student_demographic_data_id, student_id, customer_demographic_id, value_name, value, created_by, created_date_time ) values ( SEQ_STUDENT_DEMOGRAPHIC_ID.nextval , ?, ?,  ?,  ?,  ?,  SYSDATE )";
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(insertQuery);
			for(UploadStudent uploadStudent : finalStudentList){
					Student st = uploadStudent.getStudent();
					StudentDemoGraphics[] studentDemoGraphics = uploadStudent.getStudentDemographic();
					for(StudentDemoGraphics std:studentDemoGraphics){
						StudentDemographicValue [] studentDemographicValues = std.getStudentDemographicValues();
						for(StudentDemographicValue sdv:studentDemographicValues){
							pstmt.setInt(1, st.getStudentId());
							pstmt.setInt(2, std.getId());
							pstmt.setString(3, sdv.getValueName());
							pstmt.setString(4,sdv.getValueCode());
							pstmt.setInt(5, Constants.USER_ID);
							
							pstmt.addBatch();
							count++;
							if(count % BATCH_SIZE == 0){
								pstmt.executeBatch();
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
        }
	}

	public void createOrgnodeStudent(List<UploadStudent> finalStudentList)
			throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
	   	int count = 0;
	   	String insertQuery = "insert into  org_node_student ( student_id, org_node_id, created_date_time, created_by, customer_id,  activation_status) values ( ?, ?, SYSDATE , ?, ?, ? )"; 
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(insertQuery);
			for(UploadStudent uploadStudent : finalStudentList){
					ManageStudent ms = uploadStudent.getManageStudent();
					OrganizationNode [] organizationNodes = ms.getOrganizationNodes();
					
					for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) {
						pstmt.setInt(1, ms.getId());
						pstmt.setInt(2, organizationNodes[i].getOrgNodeId());
						pstmt.setInt(3, Constants.USER_ID);
						pstmt.setInt(4, uploadStudent.getStudent().getCustomerId());
						pstmt.setString(5, "AC");
						
						pstmt.addBatch();
						count++;
					}
					if(count % BATCH_SIZE == 0){
						pstmt.executeBatch();
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
        }
	}
	
}
