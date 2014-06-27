package com.ctb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ctb.bean.ManageStudent;
import com.ctb.bean.OrgNodeStudent;
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
			Iterator<UploadStudent> it = finalStudentList.iterator();
			while(it.hasNext()){
					UploadStudent uploadStudent = it.next();
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
						if(count % BATCH_SIZE == 0){
							pstmt.executeBatch();
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
	
	public void populateStudentOrgNodes(String inClause, Map<Integer, ArrayList<OrganizationNode>> studentOrgMap, Integer customerId) throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "select distinct stu.student_id as studentId, node.org_node_id as orgNodeId, node.customer_id as customerId, node.org_node_category_id as orgNodeCategoryId, node.org_node_name as orgNodeName, node.ext_qed_pin as extQedPin, node.ext_elm_id as extElmId, node.ext_org_node_type as extOrgNodeType, node.org_node_description as orgNodeDescription, node.created_by as createdBy, node.created_date_time as createdDateTime, node.updated_by as updatedBy, node.updated_date_time as updatedDateTime, node.activation_status as activationStatus,     node.parent_state as parentState, node.parent_region as parentRegion, node.parent_county as parentCounty, node.parent_district as parentDistrict, node.org_node_code as orgNodeCode, getOrghierarchy(node.org_node_id) as leafNodePath, (select decode(count(1), 0, 'false', 'true') from test_roster where student_id = stu.student_id and org_node_id = node.org_node_id) as hasRoster from org_node_student ons, student stu, org_node node, org_node_category onc where ons.student_id = stu.student_id and stu.student_id IN ("+inClause+") and ons.org_node_id = node.org_node_id and ons.activation_status = 'AC' and onc.org_node_category_id = node.org_node_category_id AND node.customer_id = "+customerId+" order by stu.student_id, node.org_node_name asc";
		ResultSet rs = null;
		
		Integer studentId = null;
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(query);
			pstmt.setFetchSize(20);
			rs = pstmt.executeQuery();
			while(rs.next()){
				studentId = rs.getInt("studentId");
				OrganizationNode newOrg = new OrganizationNode();
				newOrg.setOrgNodeId(rs.getInt("orgNodeId"));
				newOrg.setCustomerId(rs.getInt("customerId"));
				newOrg.setOrgNodeCategoryId(rs.getInt("orgNodeCategoryId"));
				newOrg.setOrgNodeName(rs.getString("orgNodeName"));
				newOrg.setExtQedPin(rs.getString("extQedPin"));
				newOrg.setExtElmId(rs.getString("extElmId"));
				newOrg.setExtOrgNodeType(rs.getString("extOrgNodeType"));
				newOrg.setOrgNodeDescription(rs.getString("orgNodeDescription"));
				newOrg.setCreatedBy(rs.getInt("createdBy"));
				newOrg.setCreatedDateTime(rs.getDate("createdDateTime"));
				newOrg.setUpdatedBy(rs.getInt("updatedBy"));
				newOrg.setUpdatedDateTime(rs.getDate("updatedDateTime"));
				newOrg.setActivationStatus(rs.getString("activationStatus"));
				newOrg.setParentState(rs.getString("parentState"));
				newOrg.setParentRegion(rs.getString("parentRegion"));
				newOrg.setParentCounty(rs.getString("parentCounty"));
				newOrg.setParentDistrict(rs.getString("parentDistrict"));
				newOrg.setOrgNodeCode(rs.getString("orgNodeCode"));
				newOrg.setLeafNodePath(rs.getString("leafNodePath"));
				newOrg.setHasRoster(rs.getString("hasRoster"));
				if(!studentOrgMap.containsKey(studentId)){
					ArrayList<OrganizationNode> orgList = new ArrayList<OrganizationNode>();
					orgList.add(newOrg);
					studentOrgMap.put(studentId, orgList);
				}else{
					studentOrgMap.get(studentId).add(newOrg);
				}
			}
			
		}catch(SQLException e){
	   		logger.error("SQL Exception in populateStudentOrgNodes-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in populateStudentOrgNodes");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, rs);
        }
	}

	public void updateStudent(List<UploadStudent> finalStudentList,	Map<Integer, ArrayList<OrganizationNode>> studentOrgMap) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "update student set user_name = ?, first_name = ?, middle_name = ?, last_name  = ?, gender  = ?, birthdate = ?, grade = ?, ext_pin1  = ?, ext_pin2 = ?, test_purpose = ?, updated_By = ?, updated_Date_Time = SYSDATE , out_of_school = ? where student_id = ?";
		int counter = 0;
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(query);
			Iterator<UploadStudent> it = finalStudentList.iterator();
			while(it.hasNext()){
				//set student organization 
				UploadStudent upload = it.next();
				setOrganizationNodeForStudent(upload.getManageStudent(), studentOrgMap);
				
				ManageStudent mStud = upload.getManageStudent();
				pstmt.setString(1, mStud.getLoginId());
				pstmt.setString(2, mStud.getFirstName());
				pstmt.setString(3, mStud.getMiddleName());
				pstmt.setString(4, mStud.getLastName());
				pstmt.setString(5, mStud.getGender());
				pstmt.setDate(6, new java.sql.Date(mStud.getBirthDate().getTime()));
				pstmt.setString(7, mStud.getGrade());
				pstmt.setString(8, mStud.getStudentIdNumber());
				pstmt.setString(9, mStud.getStudentIdNumber2());
				pstmt.setString(10, mStud.getTestPurpose());
				pstmt.setInt(11, 1);
				pstmt.setString(12, mStud.getOutOfSchool());
				pstmt.setInt(13, mStud.getId());
				pstmt.addBatch();
				counter++;
				if(counter % BATCH_SIZE == 0){
					pstmt.executeBatch();
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
	

	public void updateAccommodation(List<UploadStudent> finalStudentList) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		String query = "update  student_accommodation  set SCREEN_MAGNIFIER=?, SCREEN_READER=?, CALCULATOR=?, TEST_PAUSE=?, UNTIMED_TEST=?, HIGHLIGHTER=?, QUESTION_BACKGROUND_COLOR=?, QUESTION_FONT_COLOR=?, QUESTION_FONT_SIZE=?, ANSWER_BACKGROUND_COLOR=?, ANSWER_FONT_COLOR=?, ANSWER_FONT_SIZE=?, MASKING_RULER = ?, MUSIC_FILE_ID =?, MAGNIFYING_GLASS = ?, EXTENDED_TIME = ?, MASKING_TOOL = ?, MICROPHONE_HEADPHONE = ? where STUDENT_ID = ?";
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(query);
			Iterator<UploadStudent> it = finalStudentList.iterator();
			while(it.hasNext()){
				UploadStudent uploadStudent = it.next();
				StudentAccommodations std = uploadStudent.getStudentAccommodations();
				pstmt.setString(1, std.getScreenMagnifier());
				pstmt.setString(2, std.getScreenReader());
				pstmt.setString(3, std.getCalculator());
				pstmt.setString(4, std.getTestPause());
				pstmt.setString(5, std.getUntimedTest());
				pstmt.setString(6, std.getHighlighter());
				pstmt.setString(7, std.getQuestionBackgroundColor());
				pstmt.setString(8, std.getQuestionFontColor());
				pstmt.setString(9, std.getQuestionFontSize());
				pstmt.setString(10, std.getAnswerBackgroundColor());
				pstmt.setString(11, std.getAnswerFontColor());
				pstmt.setString(12, std.getAnswerFontSize());
				pstmt.setString(13, std.getMaskingRuler());
				pstmt.setString(14, std.getMusicFile());
				pstmt.setString(15, std.getMagnifyingGlass());
				pstmt.setString(16, std.getExtendedTime());
				pstmt.setString(17, std.getMaskingTool());
				pstmt.setString(18, std.getMicrophoneHeadphone());	
				pstmt.setInt(19, std.getStudentId());
			
				pstmt.addBatch();
				count++;
				if(count % BATCH_SIZE == 0){
					pstmt.executeBatch();
				}
			}
			pstmt.executeBatch();
			
		}catch(SQLException e){
	   		logger.error("SQL Exception in updateAccommodation-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in updateAccommodation");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);
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
        }

		
		
	}
	
	public void createStudentDemographicDataDuringUpdate(List<StudentDemographicData> insertStudentDemoList) throws Exception {
		int counter = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "insert into student_demographic_data (student_demographic_data_id, student_id, customer_demographic_id, value_name, value, created_by, created_date_time ) values ( SEQ_STUDENT_DEMOGRAPHIC_ID.nextval , ?, ?,  ?,  ?,  ?,  SYSDATE )";
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(query);
			Iterator<StudentDemographicData> it =  insertStudentDemoList.iterator();
			while(it.hasNext()){
				StudentDemographicData studDemoData = it.next();
				pstmt.setInt(1, studDemoData.getStudentId());
				pstmt.setInt(2, studDemoData.getCustomerDemographicId());
				pstmt.setString(3, studDemoData.getValueName());
				pstmt.setString(4, studDemoData.getValue());
				pstmt.setInt(5, 1);
				
				pstmt.addBatch();
				counter++;
				if(counter % BATCH_SIZE == 0){
					pstmt.executeBatch();
				}
			}
			pstmt.executeBatch();
		}catch(SQLException e){
	   		logger.error("SQL Exception in createStudentDemographicDataDuringUpdate-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in createStudentDemographicDataDuringUpdate");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);
	   	}		
	}

	public void deleteStudentDemographicDataForStudentAndCustomerDemographic(String studentAndDemoIds) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "delete from student_demographic_data where (student_id, customer_demographic_id) IN (";
		try{
			conn = SQLUtil.getConnection();
			if(!studentAndDemoIds.contains("#")){
				query += studentAndDemoIds+")";
				pstmt  = conn.prepareStatement(query);
				pstmt.execute();
			}else{
				String [] splitedstr = studentAndDemoIds.split("#");
				for(int indx=0; indx<splitedstr.length;indx++){
					if(splitedstr[indx] == null)
						continue;
					String splitquery = query + splitedstr[indx].toString()+")";
			   		pstmt  = conn.prepareStatement(splitquery);
					pstmt.execute();
					SQLUtil.closeDbObjects(null, pstmt, null);
				}
			}
		}catch(SQLException e){
	   		logger.error("SQL Exception in deleteStudentDemographicDataForStudentAndCustomerDemographic-- >"+ e.getErrorCode());
	   		e.printStackTrace();
	   	}catch(Exception e){
	   		logger.error("Exception in deleteStudentDemographicDataForStudentAndCustomerDemographic");
	   		e.printStackTrace();
	   	}
	   	finally {
        	SQLUtil.closeDbObjects(conn, pstmt, null);
        }
	}

	public void deleteVisibleStudentDemographicDataForStudent(String studentIds) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "delete  from  student_demographic_data  where student_demographic_data_id in (  select sdd.student_demographic_data_id from student_demographic_data sdd, customer_demographic cd, customer_demographic_value cdv  where sdd.customer_demographic_id = cd.customer_demographic_id  and cd.customer_demographic_id = cdv.customer_demographic_id  and cd.visible = 'T' and cdv.visible = 'T'  and sdd.student_id in ( ";
		try{
			conn = SQLUtil.getConnection();
			if(!studentIds.contains("#")){
				query += studentIds + "))";
				pstmt  = conn.prepareStatement(query);
				pstmt.execute();
			}else{
				String [] splitedstr = studentIds.split("#");
				for(int indx=0; indx<splitedstr.length;indx++){
					if(splitedstr[indx] == null)
						continue;
					String splitQuery = query + splitedstr[indx].toString()+"))";
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
        }
	}
	
	public void createOrgnodeStudentDuringUpdate( List<OrgNodeStudent> newOrgStudentList) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		String query = "insert into  org_node_student ( student_id, org_node_id, created_date_time, created_by, customer_id,  activation_status) values ( ?, ?, SYSDATE , ?, ?, ? )";
		try{
			conn = SQLUtil.getConnection();
			pstmt  = conn.prepareStatement(query);
			Iterator<OrgNodeStudent> it = newOrgStudentList.iterator();
			while(it.hasNext()) {
				OrgNodeStudent ons = it.next();
				pstmt.setInt(1, ons.getStudentId());
				pstmt.setInt(2, ons.getOrgNodeId());
				pstmt.setInt(3, Constants.USER_ID);
				pstmt.setInt(4, ons.getCustomerId());
				pstmt.setString(5, "AC");
				
				pstmt.addBatch();
				count++;
				if(count % BATCH_SIZE == 0){
					pstmt.executeBatch();
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

	private void setOrganizationNodeForStudent(ManageStudent manageStudent, Map<Integer, ArrayList<OrganizationNode>> studentOrgMap) {
		OrganizationNode[] studentOrgNodes = studentOrgMap.get(
				manageStudent.getId()).toArray(new OrganizationNode[studentOrgMap.get(manageStudent.getId()).size()]);
		if ( !isOrganizationPresent(manageStudent.getOrganizationNodes()[0].getOrgNodeId(), studentOrgNodes) ) {
			int size = studentOrgNodes.length;
			OrganizationNode []updateNodes = new OrganizationNode[size + 1];
			for (int i = 0; i < studentOrgNodes.length; i++) {
				updateNodes[i] = studentOrgNodes[i];
			}
			updateNodes[size] = manageStudent.getOrganizationNodes()[0];
			manageStudent.setOrganizationNodes(updateNodes);
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
