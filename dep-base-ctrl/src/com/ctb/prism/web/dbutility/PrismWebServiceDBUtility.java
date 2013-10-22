/**
 * 
 */
package com.ctb.prism.web.dbutility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ctb.exception.CTBBusinessException;
import com.ctb.prism.web.controller.CustHierarchyDetailsTO;
import com.ctb.prism.web.controller.DemoTO;
import com.ctb.prism.web.controller.ItemResponseTO;
import com.ctb.prism.web.controller.ItemResponsesDetailsTO;
import com.ctb.prism.web.controller.OrgDetailsTO;
import com.ctb.prism.web.controller.StudentBioTO;
import com.ctb.prism.web.controller.StudentDemoTO;
import com.ctb.prism.web.controller.SubtestAccommodationTO;
import com.ctb.prism.web.controller.SubtestAccommodationsTO;



/**
 * @author TCS
 *
 */
public class PrismWebServiceDBUtility {
	private static final String oasDtataSourceJndiName = "oasDataSource";
	private static final String irsDtataSourceJndiName = "irsDataSource";
	
	private static final String GET_STUDENT_BIO = "select stu.student_id as id,       stu.user_name as loginId,       stu.first_name as firstName,       substr(stu.middle_name,1,1) as middleName,       stu.last_name as lastName,       concat(concat(stu.last_name, ', '), concat(stu.first_name, concat(' ', stu.MIDDLE_NAME))) as studentName,       stu.gender as gender,       to_char(stu.birthdate , 'MM/DD/YYYY') as birthDate,    to_char(stu.birthdate , 'MMDDYY') as birthDateMMDDYY,      stu.grade as grade,       stu.ext_pin1 as studentIdNumber,       stu.ext_pin2 as studentIdNumber2,       stu.test_purpose as testPurpose,       stu.created_by as createdBy,       NVL(stu.out_of_school, 'No') as outOfSchool  from student stu where stu.student_id = ?";
	private static final String GET_CUST_ORG_HIGR = "select distinct node.org_node_id            as orgNodeId,                node.customer_id            as customerId,                node.org_node_category_id   as orgNodeCategoryId,                node.org_node_name          as orgNodeName,                node.ext_qed_pin            as extQedPin,                node.ext_elm_id             as extElmId,                node.ext_org_node_type      as extOrgNodeType,                node.org_node_description   as orgNodeDescription,                node.created_by             as createdBy,                node.created_date_time      as createdDateTime,                node.updated_by             as updatedBy,                node.updated_date_time      as updatedDateTime,                node.activation_status      as activationStatus,                node.data_import_history_id as dataImportHistoryId,                node.parent_state           as parentState,                node.parent_region          as parentRegion,                node.parent_county          as parentCounty,                node.parent_district        as parentDistrict,                node.org_node_code          as orgNodeCode,                ona.number_of_levels        as numberOfLevels , cat.category_name           as orgType from org_node          node,       org_node_category cat,       org_node_ancestor ona,       org_node_student  ons where ona.ancestor_org_node_id = node.org_node_id   and ona.org_node_id = ONS.ORG_NODE_ID   and ons.student_id = ?   and node.org_node_id not in (1,2) and cat.org_node_category_id = node.org_node_category_id order by ona.number_of_levels desc";
	private static final String GET_ROSTER_LIST_FOR_STUDENT = "select t.test_roster_id as rosterId    from test_roster t   where t.student_id = ?";
	private static final String GET_STUDENT_DEMO = "select t.form_assignment as testForm    from test_roster t   where t.test_roster_id = ? ";
	private static final String GET_SUBTEST_ACCOM = "SELECT CUSTDEMO.LABEL_CODE AS \"subTestAccom\"  FROM STUDENT_DEMOGRAPHIC_DATA STDDEMO, CUSTOMER_DEMOGRAPHIC CUSTDEMO WHERE STDDEMO.STUDENT_ID = ?   AND STDDEMO.CUSTOMER_DEMOGRAPHIC_ID = CUSTDEMO.CUSTOMER_DEMOGRAPHIC_ID";
	private static final String GET_ITEM_RESP_SR = "SELECT ITM.ITEM_TYPE           AS ITEMTYPE,       RES.RESPONSE            AS RESPONSE,       ITM.ITEM_ID             AS ITEMID,       SET_ITM.ITEM_SORT_ORDER AS ITEMORDER  FROM ITEM_RESPONSE RES, ITEM_SET_ITEM SET_ITM, ITEM ITM WHERE ITM.ITEM_ID = SET_ITM.ITEM_ID   AND SET_ITM.ITEM_SET_ID = RES.ITEM_SET_ID   AND SET_ITM.ITEM_ID = RES.ITEM_ID   AND RES.TEST_ROSTER_ID = ?   AND RES.ITEM_SET_ID = ?   AND RES.ITEM_RESPONSE_ID =       (SELECT MAX(R.ITEM_RESPONSE_ID)          FROM ITEM_RESPONSE R         WHERE R.TEST_ROSTER_ID = ?           AND R.ITEM_SET_ID = ?           AND R.ITEM_ID = RES.ITEM_ID)   AND ITM.ITEM_TYPE = ? ORDER BY SET_ITM.ITEM_SORT_ORDER";
	private static final String GET_ITEM_RESP_GR_CR = "SELECT CRITM.ITEM_TYPE            AS ITEMTYPE,       CRRES.CONSTRUCTED_RESPONSE AS RESPONSE,       CRITM.ITEM_ID              AS ITEMID,       CR_SET_ITM.ITEM_SORT_ORDER AS ITEMORDER  FROM ITEM_RESPONSE_CR CRRES, ITEM_SET_ITEM CR_SET_ITM, ITEM CRITM WHERE CRITM.ITEM_ID = CR_SET_ITM.ITEM_ID   AND CR_SET_ITM.ITEM_SET_ID = CRRES.ITEM_SET_ID   AND CR_SET_ITM.ITEM_ID = CRRES.ITEM_ID   AND CRRES.TEST_ROSTER_ID = ?   AND CRRES.ITEM_SET_ID = ?   AND CRRES.TEST_ROSTER_ID =       (SELECT MAX(R.TEST_ROSTER_ID)          FROM ITEM_RESPONSE_CR R         WHERE R.TEST_ROSTER_ID = ?           AND R.ITEM_SET_ID = ?           AND R.ITEM_ID = CRRES.ITEM_ID)   AND CRITM.ITEM_TYPE = ?   ORDER BY CR_SET_ITM.ITEM_SORT_ORDER";
	

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static final String defaultStartDateStr = "01/01/1900";

	/**
	 * Get Student Bio Information
	 * @param studentId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static StudentBioTO getStudentBio(java.lang.Integer studentId) throws CTBBusinessException{
		//StudentManagement studentManagement = new StudentManagementImpl();
		//return studentManagement.getManageStudent(userName, studentId);
		
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		StudentBioTO std = new StudentBioTO();
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_STUDENT_BIO);
			pst.setLong(1, studentId);
			rs = pst.executeQuery();
			populateStudentBioTO(rs, std);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return std;
	}

	/**
	 * Get Student Organization Information
	 * @param orgNodeId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static CustHierarchyDetailsTO  getCustomerHigherarchy(Integer studentId) throws CTBBusinessException{
		//StudentManagement studentManagement = new StudentManagementImpl();
		//return studentManagement.getAncestorOrganizationNodesForOrgNode(orgNodeId);
		CustHierarchyDetailsTO custHierarchyDetailsTO = new CustHierarchyDetailsTO();
		
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_CUST_ORG_HIGR);
			pst.setLong(1, studentId);
			rs = pst.executeQuery();
			populateCustHierarchyDetailsTO(rs, custHierarchyDetailsTO);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		
		return custHierarchyDetailsTO;
	}
	
	/**
	 * Get the Roster lists for a student
	 * @param studentId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static List<Long> getRosterListForStudent(Integer studentId) throws CTBBusinessException{
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		List<Long> rosterIds = new ArrayList<Long>(); 
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_ROSTER_LIST_FOR_STUDENT);
			pst.setLong(1, studentId);
			rs = pst.executeQuery();
			while(rs.next()){
				rosterIds.add(rs.getLong("rosterId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return rosterIds;
	}
	
	
	/**
	 * Get the student demo data
	 * @param rosterId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static StudentDemoTO getStudentDemo(long rosterId) throws CTBBusinessException{
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		StudentDemoTO studentDemoTO = new StudentDemoTO();
		List<DemoTO> demoList = studentDemoTO.getCollDemoTO();
		
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_STUDENT_DEMO);
			pst.setLong(1, rosterId);
			rs = pst.executeQuery();
			while(rs.next()){
				DemoTO demoTO = new DemoTO();
				demoTO.setDemoName("Test_Form");
				demoTO.setDemovalue(rs.getString("testForm"));
				demoTO.setDemoName("Fld_Tst_Form");
				demoTO.setDemovalue(rs.getString("testForm"));
				demoTO.setDemoName("Tst_Platform");
				demoTO.setDemovalue("0");
				demoList.add(demoTO);
			}
			studentDemoTO.setDataChanged(true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return studentDemoTO;
	}
	
	/**
	 * Get the Sub Test Accommodation
	 * @param studentId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static SubtestAccommodationsTO getSubTestAccommodation(Integer studentId) throws CTBBusinessException{
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		SubtestAccommodationsTO subtestAccommodationsTO = new SubtestAccommodationsTO();
		List<SubtestAccommodationTO> subtestAccommodationLst = subtestAccommodationsTO.getCollSubtestAccommodationTO(); 
		
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_SUBTEST_ACCOM);
			pst.setLong(1, studentId);
			rs = pst.executeQuery();
			while(rs.next()){
				SubtestAccommodationTO subtestAccommodationTO = new SubtestAccommodationTO();
				subtestAccommodationTO.setName(rs.getString("subTestAccom"));
				subtestAccommodationTO.setValue("Y");
				subtestAccommodationLst.add(subtestAccommodationTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return subtestAccommodationsTO;
	}
	
	/**
	 * Get the Item Response Details
	 * @param studentId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static ItemResponsesDetailsTO getItemResponsesDetail(long rosterID, long itemSetId) throws CTBBusinessException{
		Connection con = null;
		PreparedStatement pstSR = null;
		ResultSet rsSR = null;
		PreparedStatement pstCR = null;
		ResultSet rsCR = null;
		PreparedStatement pstGR = null;
		ResultSet rsGR = null;
		ItemResponsesDetailsTO itemResponsesDetailsTO = new ItemResponsesDetailsTO();
		List<ItemResponseTO> itemResponseTOLst =  itemResponsesDetailsTO.getItemResponseTO();
		//TODO - Not yet completed.  
		try {
			con = openOASDBcon(false);
			pstSR = con.prepareStatement(GET_ITEM_RESP_SR);
			pstSR.setLong(1, rosterID);
			pstSR.setLong(2, itemSetId);
			pstSR.setLong(3, rosterID);
			pstSR.setLong(4, itemSetId);
			pstSR.setString(5, "SR");
			rsSR = pstSR.executeQuery();
			while(rsSR.next()){
				ItemResponseTO itemResponseTO = new ItemResponseTO();
				//TODO - populate the data in itemResponseTO
				
				itemResponseTOLst.add(itemResponseTO);
			}
			
			con.prepareStatement(GET_ITEM_RESP_GR_CR);
			pstCR.setLong(1, rosterID);
			pstCR.setLong(2, itemSetId);
			pstCR.setLong(3, rosterID);
			pstCR.setLong(4, itemSetId);
			pstCR.setString(5, "CR");
			rsCR = pstCR.executeQuery();
			while(rsSR.next()){
				ItemResponseTO itemResponseTO = new ItemResponseTO();
				//TODO - populate the data in itemResponseTO
				
				itemResponseTOLst.add(itemResponseTO);
			}
			
			con.prepareStatement(GET_ITEM_RESP_GR_CR);
			pstCR.setLong(1, rosterID);
			pstCR.setLong(2, itemSetId);
			pstCR.setLong(3, rosterID);
			pstCR.setLong(4, itemSetId);
			pstCR.setString(5, "GR");
			rsCR = pstCR.executeQuery();
			while(rsSR.next()){
				ItemResponseTO itemResponseTO = new ItemResponseTO();
				//TODO - populate the data in itemResponseTO
				
				itemResponseTOLst.add(itemResponseTO);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(pstCR, rsCR);
			close(pstGR, rsGR);
			close(con, pstSR, rsSR);
		}
		return itemResponsesDetailsTO;
	}
	
	/**
	 * Populate the Customer Hierarchy Details TO
	 * @param rs
	 * @param custHierarchyDetailsTO
	 * @throws SQLException 
	 */
	private static void populateCustHierarchyDetailsTO(ResultSet rs,
			CustHierarchyDetailsTO custHierarchyDetailsTO) throws SQLException {
		
		List<OrgDetailsTO> orgDetailList = custHierarchyDetailsTO.getCollOrgDetailsTO();
		int orgLevel = 0;
		String parentOrgNodeCode = "0";
		int maxHighrCount = 0;
		long customerID = 0L;
		
		while(rs.next()){
			OrgDetailsTO orgDetailsTO = new OrgDetailsTO();
			orgDetailsTO.setOrgName(rs.getString("orgNodeName"));
			orgDetailsTO.setOrgLabel(rs.getString("orgType"));
			orgDetailsTO.setOrgLevel(String.valueOf(++orgLevel));
			orgDetailsTO.setOrgNodeId(rs.getString("orgNodeId"));
			orgDetailsTO.setOrgCode(rs.getString("orgNodeCode"));
			orgDetailsTO.setParentOrgCode(parentOrgNodeCode);
			orgDetailList.add(orgDetailsTO);	
			maxHighrCount++;
			customerID = rs.getLong("customerId");
			parentOrgNodeCode = rs.getString("orgNodeId");
		}
		custHierarchyDetailsTO.setDataChanged(true);		
		custHierarchyDetailsTO.setMaxHierarchy(String.valueOf(maxHighrCount));
		custHierarchyDetailsTO.setCustomerId(String.valueOf(customerID));
		
	}

	/**
	 * Populate the Student TO
	 * @param rs
	 * @param std
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	private static void populateStudentBioTO(ResultSet rs, StudentBioTO std) throws SQLException, ParseException {
		while(rs.next()){
			std.setLastName(rs.getString("lastName"));
			std.setFirstName(rs.getString("firstName"));
			std.setMiddleInit((rs.getString("middleName") != null && !"".equals(rs.getString("middleName"))) ? rs.getString("middleName") : "" );
			String stdGender = rs.getString("gender");
			std.setGender(("M".equalsIgnoreCase(stdGender) || "F".equalsIgnoreCase(stdGender)) ? stdGender : "");
			std.setGrade(rs.getString("grade"));
			std.setChrnlgclAge(getChronologicalAge(rs.getString("birthDate")));
			std.setBirthDate(rs.getString("birthDateMMDDYY"));
			std.setExamineeId(rs.getString("studentIdNumber"));
			std.setOasStudentId(rs.getString("id"));
			std.setDataChanged(true);
		}
		
	}
	

	/**
	 * Get the Chronological  Age
	 * @param studentDOB
	 * @return
	 * @throws ParseException 
	 */
	private static String getChronologicalAge(String studentDOB) throws ParseException {
		if(studentDOB != null && !"".equals(studentDOB)){
			Date stdDOBDt = dateFormat.parse(studentDOB);
			long ageInMillis = new Date().getTime() - stdDOBDt.getTime();
			Date defaultStartDate = dateFormat.parse(defaultStartDateStr);
			long addedAgeWithDefaultStartDt = defaultStartDate.getTime() + ageInMillis;
			Date ageWithDefaultStartDate = new Date(addedAgeWithDefaultStartDt);
			return String.valueOf(ageWithDefaultStartDate.getYear() - defaultStartDate.getYear());
		}else{
			return "";
		}
	}
	
	private static Connection openOASDBcon(boolean isCommitable)
			throws CTBBusinessException {
		Connection conn = null;
		try {
			DataSource ds = locateDataSource(oasDtataSourceJndiName);
			conn = ds.getConnection();
			if (isCommitable) {
				conn.setAutoCommit(false);
			}
		} catch (NamingException e) {
			System.err.println("NamingException:"
					+ "JNDI name for oas datasource does not exists.");
			throw new CTBBusinessException("NamingException:"
					+ "JNDI name for oas datasource does not exists.");
		} catch (SQLException e) {
			System.err.println("SQLException:"
					+ "while getting oas database connection.");
			throw new CTBBusinessException("SQLException:"
					+ "while getting oas database connection.");
		} catch (Exception e) {
			System.err.println("Exception:"
					+ "while getting oas database connection.");
			throw new CTBBusinessException("Exception:"
					+ "while getting oas database connection.");
		}

		return conn;

	}

	private static Connection openIRSDBcon(boolean isCommitable)
			throws CTBBusinessException {

		Connection conn = null;
		try {
			DataSource ds = locateDataSource(irsDtataSourceJndiName);
			conn = ds.getConnection();
			if (isCommitable) {
				conn.setAutoCommit(false);
			}
		} catch (NamingException e) {
			System.err.println("NamingException:"
					+ "JNDI name for irs datasource does not found.");
			throw new CTBBusinessException("NamingException:"
					+ "JNDI name for irs datasource does not found.");
		} catch (SQLException e) {
			System.err.println("SQLException:"
					+ "while getting irs database connection.");
			throw new CTBBusinessException("NamingException:"
					+ "while getting irs database connection.");
		} catch (Exception e) {
			System.err.println("Exception:"
					+ "while getting irs database connection.");
			throw new CTBBusinessException("Exception:"
					+ "while getting  irs database connection.");
		}

		return conn;
	}
	
	private static DataSource locateDataSource(String jndiName ) throws NamingException{
		Context ctx = new InitialContext();
		DataSource ds =  (DataSource) ctx.lookup(jndiName);
		return ds;
	}
	
	private static void close(Connection con) {
		if (con != null) {
			try {
				if(!con.getAutoCommit())
					con.rollback();
				con.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

	private static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

	private static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}
	
	private static void close(Statement st, ResultSet rs) {
		close(rs);
		close(st);

	}
	private static void close(Connection con, Statement st, ResultSet rs) {
		close(rs);
		close(st);
		close(con);

	}

	private static void close(Connection con, Statement st) {
		close(st);
		close(con);
		
	}
	
}
