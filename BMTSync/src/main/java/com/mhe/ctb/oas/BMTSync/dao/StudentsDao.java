package com.mhe.ctb.oas.BMTSync.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;

import com.mhe.ctb.oas.BMTSync.controller.StudentController;
import com.mhe.ctb.oas.BMTSync.model.HierarchyNode;
import com.mhe.ctb.oas.BMTSync.model.Student;

//import com.mhe.ctb.oas.BMTSync.util.ReadPropertyFile;

public class StudentsDao  extends DatabaseManager {

	static private Logger LOGGER = Logger.getLogger(StudentController.class);
	
	public List<Student> getStudentDetails() throws SQLException, Exception {
		//String procName, List<String> procInputParams, List<String> procParamTypes		
		List<Student> studentList = new ArrayList<Student>();
		Student student = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		
		//int pCustomerID = 11100;
		int pStudentID = 0;
		int pNoOfRecordToFetch = 1;
		
		ResultSet rs = null;
		//SqlDML sqlDML = new SqlDML();
		
		try {
			//ReadPropertyFile readConfigPropertyFile = new ReadPropertyFile();
			//pNoOfRecordToFetch = Integer.parseInt(readConfigPropertyFile.getNofRecordToBeFetched());

			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PK_Students.FetchStudentList(?, ?); END;");
			cstmt.setInt(1, pNoOfRecordToFetch);
			cstmt.registerOutParameter(2,OracleTypes.CURSOR);

			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(2);
			
		    while (rs.next()) {
		    	if (rs.getInt("oasStudentID") > 0) {
			    	//pStudentID = Integer.toString(rs.getInt("oasStudentID"));
			    	pStudentID = rs.getInt("oasStudentID");
			    	 
			    	student = new Student();
	
					student = getStudenData(pStudentID);
			    	studentList.add(student);
		    	}
		    }
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			LOGGER.error("SQL Exception Error in StudentDao: "+sqle.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception Error in StudentDao: "+e.getMessage());
		} finally {
			if (rs!= null) rs.close();
			if (cstmt != null) cstmt.close();
			if (conn!= null) conn.close();		

		}
		return studentList;
	}
	


	public Student getStudenData(Integer pStudentID) throws Exception {
		Student student = new Student();
		//StudentAccomodation studentAccom = new StudentAccomodation();
		String[] extStudentIds = new String[2];
		
		
		List<HierarchyNode> heirarchyList = new ArrayList<HierarchyNode>();
		
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		try {

			heirarchyList = getHierarchy(pStudentID);

			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PK_Students.StudentDetails(?, ?); END;");
			cstmt.setInt(1, pStudentID);
			cstmt.registerOutParameter(2,OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(2);

		    
		    
		    while (rs.next()) {
		    	extStudentIds[0] = rs.getString("Ext_Pin1");
		    	extStudentIds[1] = rs.getString("Ext_Pin2");
		    	//System.out.println("Screen_Reader="+rs.getString("Screen_Reader"));
		    	
		    	student.setOasStudentId(rs.getInt("oasStudentID"));
		    	student.setOasCustomerId(rs.getInt("oasCustomerId"));
		    	student.setStudentusername(rs.getString("StudentUserName"));
		    	student.setFirstName(rs.getString("FirstName"));
		    	student.setMiddleName(rs.getString("MiddleName"));
		    	student.setLastName(rs.getString("LastName"));
		    	student.setBirthdate(rs.getString("BirthDate"));
		    	student.setGender(rs.getString("Gender"));
		    	student.setGrade(rs.getString("Grade"));
		    	student.setCustomerStudentId(rs.getString("Ext_Pin1"));
		    	
		    	// Set Student Accomodation
		    	/*
				studentAccom.setScreen_Magnifier(rs.getString("SCREEN_MAGNIFIER"));
				studentAccom.setScreen_Reader(rs.getString("Screen_Reader"));
				studentAccom.setCalculator(rs.getString("Calculator"));
				studentAccom.setTest_Pause(rs.getString("Test_Pause"));
				studentAccom.setUntimed_Test(rs.getString("Untimed_Test"));
				studentAccom.setQuestion_background_color(rs.getString("Question_Background_Color"));
				studentAccom.setQuestion_font_color(rs.getString("Question_Font_Color"));
				studentAccom.setQuestion_font_size(rs.getString("Question_Font_Size"));
				studentAccom.setAnswer_background_color(rs.getString("Answer_Background_Color"));
				studentAccom.setAnswer_font_color(rs.getString("Answer_Font_Color"));
				studentAccom.setAnswer_font_size(rs.getString("Answer_Font_Size"));
				studentAccom.setHighlighter(rs.getString("Highlighter"));
				studentAccom.setMusic_File_Id(rs.getString("Music_File_Id"));
				studentAccom.setMasking_ruler(rs.getString("Masking_Ruler"));
				studentAccom.setMagnifying_glass(rs.getString("Magnifying_Glass"));
				studentAccom.setExtended_time(rs.getString("Extended_Time"));
				studentAccom.setMasking_tool(rs.getString("Masking_Tool"));
				studentAccom.setMicrophone_headphone(rs.getString("Microphone_Headphone"));
				studentAccom.setExtended_time_factor(rs.getFloat("Extended_Time_Factor"));				
                */
		    	//student.setExtStudentId(extStudentIds);
		    	
		    }
		    
		    student.setHeirarchySet(heirarchyList);
		    //student.setAccomodations(studentAccom);
		    
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			return student;
		}
		catch (Exception e) {
			e.printStackTrace();
			return student;
		}
		finally {
			rs.close();
			cstmt.close();
			if (conn!= null) conn.close();		
		}

		return student;
			
	}
	

	/*
	 * Methiod to update the Student API Status table
	 */
	public boolean updateStudentAPIstatus(Integer pStudentID, String pAppName, String pExportStatus, String pErrorCode, String pErrorMessage) throws Exception {
		
		Connection conn = null;
		CallableStatement cstmt = null;
		//ResultSet rs = null;
		
		try {
			if (pErrorMessage.length() > 200 )
				pErrorMessage = pErrorMessage.substring(1, 200);
			
			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PK_Students.updateStudentAPIStatus(?, ?, ?, ?, ?); END;");
			cstmt.setInt(1, pStudentID);
			cstmt.setString(2, pAppName);
			cstmt.setString(3, pExportStatus);			
			cstmt.setString(4, pErrorCode);
			cstmt.setString(5, pErrorMessage);
			
			cstmt.execute();
		    
			
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			//rs.close();
			cstmt.close();
			if (conn!= null) conn.close();				
		}
		return true;
	}
	
	public List<HierarchyNode> getHierarchy(Integer pStudentID) throws Exception {
		List <HierarchyNode> heirarchyList = new ArrayList<HierarchyNode>();		

		Connection conn = null;
		CallableStatement cstmt = null;
		//PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = dbConnection();
			//cstmt = conn.prepareCall("BEGIN PK_Students.HeirarchyParents(?, ?); END;");
			cstmt = conn.prepareCall("BEGIN PK_Students.Heirarchy(?, ?); END;");
			cstmt.setInt(1, pStudentID);
			cstmt.registerOutParameter(2,OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(2);

			while (rs.next()) {
				HierarchyNode heirarchy = new HierarchyNode();
				
				heirarchy.setHeirarchyCategoryName(rs.getString("CATEGORY_NAME"));				
				//heirarchy.setOasHeirarchyId(rs.getInt("ANCESTOR_ORG_NODE_ID"));
				heirarchy.setOasHeirarchyId(rs.getInt("OAS_Heirarchy_ID"));
				heirarchy.setCode(rs.getString("ORG_NODE_CODE"));
				heirarchy.setName(rs.getString("ORG_NODE_NAME"));
				heirarchyList.add(heirarchy);
				
				System.out.print(rs.getString("CATEGORY_NAME")+", " );
				System.out.print(rs.getString("OAS_Heirarchy_ID")+", " );
				System.out.print(rs.getString("ORG_NODE_CODE")+", " );
				System.out.println(rs.getString("ORG_NODE_NAME"));
			}
			
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			return heirarchyList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return heirarchyList;
		} finally {
			rs.close();
			cstmt.close();
			if (conn!= null) conn.close();				
		}
		return heirarchyList;
	}
	
	

	/*
	 * method to return Student Accomodation
	 * @param Student ID
	 * Returns resultset
	public StudentAccomodationVO getStudentAccomodationdetails(String pStudentID)  throws Exception {
		StudentAccomodationVO studentAccom = new StudentAccomodationVO();
		
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PK_Students.StudentAccomodation(?, ?); END;");
			cstmt.setString(1, pStudentID);
			cstmt.registerOutParameter(2,OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(2);

			//pstmt = conn.prepareStatement(query);
			//pstmt.setString(1, pStudentID);
			//pstmt.setString(2, pNodeCategoryName);
		    
			//rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println("Screen_Reader="+rs.getString("Screen_Reader"));
				
				studentAccom.setScreen_Magnifier(rs.getString("SCREEN_MAGNIFIER"));
				studentAccom.setScreen_Reader(rs.getString("Screen_Reader"));
				studentAccom.setCalculator(rs.getString("Calculator"));
				studentAccom.setTest_Pause(rs.getString("Test_Pause"));
				studentAccom.setUntimed_Test(rs.getString("Untimed_Test"));
				studentAccom.setQuestion_background_color(rs.getString("Question_Background_Color"));
				studentAccom.setQuestion_font_color(rs.getString("Question_Font_Color"));
				studentAccom.setQuestion_font_size(rs.getString("Question_Font_Size"));
				studentAccom.setAnswer_background_color(rs.getString("Answer_Background_Color"));
				studentAccom.setAnswer_font_color(rs.getString("Answer_Font_Color"));
				studentAccom.setAnswer_font_size(rs.getString("Answer_Font_Size"));
				studentAccom.setHighlighter(rs.getString("Highlighter"));
				studentAccom.setMusic_File_Id(rs.getString("Music_File_Id"));
				studentAccom.setMasking_ruler(rs.getString("Masking_Ruler"));
				studentAccom.setMagnifying_glass(rs.getString("Magnifying_Glass"));
				studentAccom.setExtended_time(rs.getString("Extended_Time"));
				studentAccom.setMasking_tool(rs.getString("Masking_Tool"));
				studentAccom.setMicrophone_headphone(rs.getString("Microphone_Headphone"));
				studentAccom.setExtended_time_factor(rs.getFloat("Extended_Time_Factor"));				
			}
			
			
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			return studentAccom;
		}
		catch (Exception e) {
			e.printStackTrace();
			return studentAccom;
		} finally {
			rs.close();
			cstmt.close();
			if (conn!= null) conn.close();				
		}
		return studentAccom;
	}
	 */
	
	
	/*
	public HeirarchyNode getHierarchyDataOld(String pStudentID, String pNodeCategoryName) throws Exception {
		HeirarchyNode heirarchy = new HeirarchyNode();
		
		Connection conn = null;
		CallableStatement cstmt = null;
		//PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PK_Students.HeirarchyParents(?, ?, ?); END;");
			cstmt.setString(1, pStudentID);
			cstmt.setString(2, pNodeCategoryName);
			cstmt.registerOutParameter(3,OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(3);

			//pstmt = conn.prepareStatement(query);
			//pstmt.setString(1, pStudentID);
			//pstmt.setString(2, pNodeCategoryName);
		    
			//rs = pstmt.executeQuery();
			while (rs.next()) {

				heirarchy.setHeirarchyCategoryName(rs.getString("CATEGORY_NAME"));				
				heirarchy.setOasHeirarchyId(rs.getInt("ANCESTOR_ORG_NODE_ID"));
				heirarchy.setCode(rs.getString("ORG_NODE_CODE"));
				heirarchy.setName(rs.getString("ORG_NODE_NAME"));
			}
			
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			return heirarchy;
		}
		catch (Exception e) {
			e.printStackTrace();
			return heirarchy;
		} finally {
			rs.close();
			cstmt.close();
			if (conn!= null) conn.close();				
		}
		return heirarchy;
	}
	*/

	/*
	public HeirarchyNode getHierarchyClass(String pStudentID) throws Exception {
		HeirarchyNode heirarchy = new HeirarchyNode();
		
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = dbConnection();
			cstmt = conn.prepareCall("BEGIN PK_Students.HeirarchyClass(?, ?); END;");
		    cstmt.setString(1, pStudentID);
			cstmt.registerOutParameter(2,OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(2);

			//pstmt = conn.prepareStatement(query);
			//pstmt.setString(1, pStudentID);
			//rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				heirarchy.setHeirarchyCategoryName(rs.getString("CATEGORY_NAME"));
				heirarchy.setOasHeirarchyId(rs.getInt("ORG_NODE_ID"));
				heirarchy.setCode(rs.getString("ORG_NODE_CODE"));
				heirarchy.setName(rs.getString("ORG_NODE_NAME"));
			}
			
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			return heirarchy;
		}
		catch (Exception e) {
			e.printStackTrace();
			return heirarchy;
		} finally {
			rs.close();
			cstmt.close();
			if (conn!= null) conn.close();				
		}
		return heirarchy;
	}
	*/
	
}
