/**
 * 
 */
package com.ctb.prism.web.dbutility;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import oracle.sql.CLOB;
import weblogic.utils.StringUtils;

import com.ctb.exception.CTBBusinessException;
import com.ctb.prism.web.constant.PrismWebServiceConstant;
import com.ctb.prism.web.controller.ContentDetailsTO;
import com.ctb.prism.web.controller.ContentScoreDetailsTO;
import com.ctb.prism.web.controller.ContentScoreTO;
import com.ctb.prism.web.controller.CustHierarchyDetailsTO;
import com.ctb.prism.web.controller.DemoTO;
import com.ctb.prism.web.controller.ItemResponseTO;
import com.ctb.prism.web.controller.ItemResponsesDetailsTO;
import com.ctb.prism.web.controller.ObjectiveScoreDetailsTO;
import com.ctb.prism.web.controller.ObjectiveScoreTO;
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
	private static final String GET_ITEM_RESP_SR = "SELECT itm.item_type           AS itemtype,       res.response            AS response,       itm.item_id             AS itemid,       set_itm.item_sort_order AS itemorder,       itm.correct_answer      AS correctans  FROM item_response res, item_set_item set_itm, item itm WHERE itm.item_id = set_itm.item_id   AND set_itm.item_set_id = res.item_set_id   AND set_itm.item_id = res.item_id   AND res.test_roster_id = ?   AND res.item_set_id IN (SELECT DISTINCT t.item_set_id                             FROM item_set_parent t, item_response r                            WHERE t.parent_item_set_id = ?                              AND r.item_set_id = t.item_set_id                              AND r.test_roster_id = ?                            GROUP BY t.item_set_id)   AND res.item_response_id =       (SELECT MAX(r.item_response_id)          FROM item_response r         WHERE r.test_roster_id = ?           AND r.item_set_id IN (SELECT DISTINCT t.item_set_id                                   FROM item_set_parent t, item_response r                                  WHERE t.parent_item_set_id = ?                                    AND r.item_set_id = t.item_set_id                                    AND r.test_roster_id = ?                                  GROUP BY t.item_set_id)           AND r.item_id = res.item_id)   AND itm.item_type = ? ORDER BY set_itm.item_sort_order";
	private static final String GET_ITEM_RESP_GR_CR = "SELECT critm.item_type            AS itemtype,       crres.constructed_response AS response,       critm.item_id              AS itemid,       cr_set_itm.item_sort_order AS itemorder  FROM item_response_cr crres, item_set_item cr_set_itm, item critm WHERE critm.item_id = cr_set_itm.item_id   AND cr_set_itm.item_set_id = crres.item_set_id   AND cr_set_itm.item_id = crres.item_id   AND crres.test_roster_id = ?   AND crres.item_set_id IN (SELECT DISTINCT t.item_set_id                               FROM item_set_parent t, item_response r                              WHERE t.parent_item_set_id = ?                                AND r.item_set_id = t.item_set_id                                AND r.test_roster_id = ?                             GROUP BY t.item_set_id)   AND crres.test_roster_id =       (SELECT MAX(r.test_roster_id)          FROM item_response_cr r         WHERE r.test_roster_id = ?           AND r.item_set_id IN (SELECT DISTINCT t.item_set_id                                   FROM item_set_parent t, item_response r                                  WHERE t.parent_item_set_id = ?                                    AND r.item_set_id = t.item_set_id                                    AND r.test_roster_id = ?                                  GROUP BY t.item_set_id)           AND r.item_id = crres.item_id)   AND critm.item_type = ? ORDER BY cr_set_itm.item_sort_order";
	private static final String GET_CONTENT_SCORE_DETAILS = "SELECT t.points_obtained         AS number_correct,       t.points_possible         AS number_possible,       t.scale_score             AS scale_score,       ''                        AS high_school_equiv,       t.national_percentile     AS percentile_rank,       t.normal_curve_equivalent AS normal_curve_equivalent,       ''                        AS hse_scale_score_range  FROM tabe_content_area_fact t WHERE t.studentid = ?   AND t.sessionid = ?   AND SUBSTR(t.content_areaid, 5) = ?";
	private static final String GET_CONTENT_DETAILS = "SELECT DISTINCT ipp.item_set_name AS \"content_code_name\",                ipp.item_set_id AS \"item_set_id\"  FROM student_item_set_status t,       item_set                s,       item_set_parent         ip,       item_set                ipp WHERE t.test_roster_id = ?   AND s.item_set_id = t.item_set_id   AND ip.item_set_id = s.item_set_id   AND ipp.item_set_id = ip.parent_item_set_id   AND s.SAMPLE = 'F'";
	private static final String GET_TEST_TAKEN_DATE = "SELECT MAX(r.created_date_time) AS \"test_taken_dt\"  FROM item_response r WHERE r.item_set_id IN (SELECT DISTINCT t.item_set_id                           FROM item_set_parent t, item_response r                          WHERE t.parent_item_set_id = ?                            AND r.item_set_id = t.item_set_id                            AND r.test_roster_id = ?                          GROUP BY t.item_set_id)   AND r.test_roster_id = ?";
	private static final String GET_OBJECTIVE_LIST = "SELECT 'P' AS lvl, prim.item_set_id AS itemsetid, prim.item_set_name AS objname  FROM item_set prim,       item_set_item pisi,       item_set_ancestor pisip,       item_set_category pisc,       item_set sub,       item_set_item isi,       product prod,       item WHERE sub.item_set_id IN (SELECT DISTINCT t.item_set_id                             FROM item_set_parent t, item_response r                            WHERE t.parent_item_set_id = ?                              AND r.item_set_id = t.item_set_id                              AND r.test_roster_id = ?                            GROUP BY t.item_set_id)   AND isi.item_set_id = sub.item_set_id   AND isi.item_id = pisi.item_id   AND pisip.item_set_id = pisi.item_set_id   AND pisip.ancestor_item_set_id = prim.item_set_id   AND prim.item_set_category_id = pisc.item_set_category_id   AND pisc.item_set_category_level = prod.scoring_item_set_level   AND prod.product_id IN       (SELECT adm.product_id AS prodid          FROM test_admin adm         WHERE adm.test_admin_id = ?)   AND prod.parent_product_id = pisc.framework_product_id   AND isi.item_id = item.item_id   AND isi.suppressed = 'F'   AND item.item_type != 'RQ'   AND (sub.item_set_level != 'L' OR prod.product_type = 'TL') UNION SELECT 'S' AS lvl , sec.item_set_id AS itemsetid, sec.item_set_name AS objname  FROM item_set sec,       item_set_item sisi,       item_set_ancestor sisip,       item_set_category sisc,       item_set sub,       item_set_item isi,       product prod,       item WHERE sub.item_set_id IN (SELECT DISTINCT t.item_set_id                             FROM item_set_parent t, item_response r                            WHERE t.parent_item_set_id = ?                              AND r.item_set_id = t.item_set_id                              AND r.test_roster_id = ?                            GROUP BY t.item_set_id)   AND isi.item_set_id = sub.item_set_id   AND isi.item_id = sisi.item_id   AND sisip.item_set_id = sisi.item_set_id   AND sisip.ancestor_item_set_id = sec.item_set_id   AND sec.item_set_category_id = sisc.item_set_category_id   AND sisc.item_set_category_level = prod.sec_scoring_item_set_level   AND prod.product_id IN       (SELECT adm.product_id AS prodid          FROM test_admin adm         WHERE adm.test_admin_id = ?)   AND prod.parent_product_id = sisc.framework_product_id   AND isi.item_id = item.item_id   AND isi.suppressed = 'F'   AND item.item_type != 'RQ'   AND(sub.item_set_level != 'L' OR prod.product_type = 'TL')";
	private static final String GET_PRIM_OBJ_SCORE = "SELECT t.percent_obtained AS numcorrect,       t.points_possible AS numpossible,       '' AS scalescore,       t.mastery_levelid AS mastery,       '' AS objmasscalescorerng,       '' AS itmattempflag  FROM tabe_prim_obj_fact t WHERE substr(t.prim_objid, 5)  = ? AND t.studentid = ? AND t.sessionid = ?";
	private static final String GET_SEC_OBJ_SCORE = "SELECT t.percent_obtained AS numcorrect,       t.points_possible AS numpossible,       '' AS scalescore,       t.mastery_levelid AS mastery,       '' AS objmasscalescorerng,       '' AS itmattempflag  FROM tabe_sec_obj_fact t WHERE substr(t.sec_objid, 5) = ?   AND t.studentid = ?   AND t.sessionid = ?";
	
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
			pstSR.setLong(4, rosterID);
			pstSR.setLong(5, itemSetId);
			pstSR.setLong(6, rosterID);
			pstSR.setString(7, PrismWebServiceConstant.SRItemResponseSetType);
			rsSR = pstSR.executeQuery();
			ItemResponseTO srItemResponseTO = new ItemResponseTO();
			srItemResponseTO.setItemSetType(PrismWebServiceConstant.SRItemResponseSetType);
			srItemResponseTO.setItemCode(PrismWebServiceConstant.itemResponseItemCodeMap.get("SR"));
			
			StringBuffer stScoreVal = new StringBuffer(); 
			boolean srItmPresent = false;
			while(rsSR.next()){
				srItmPresent = true;
				String stdRes = rsSR.getString("RESPONSE");
				String correctAns = rsSR.getString("CORRECTANS");
				if(stdRes == null || "".equals(stdRes)){
					stScoreVal.append("-");
				}else if(correctAns == null || "".equals(correctAns)){
					stScoreVal.append("-");
				}else if(stdRes.equals(correctAns)){
					stScoreVal.append(stdRes);
				}else{
					stScoreVal.append(PrismWebServiceConstant.itemResponseSRScoreValMap.get(stdRes.toUpperCase()));
				}
			}
			srItemResponseTO.setScoreValue(stScoreVal.toString());
			if(srItmPresent){
				itemResponseTOLst.add(srItemResponseTO);
			}
			
			con.prepareStatement(GET_ITEM_RESP_GR_CR);
			pstCR.setLong(1, rosterID);
			pstCR.setLong(2, itemSetId);
			pstCR.setLong(3, rosterID);
			pstCR.setLong(4, rosterID);
			pstCR.setLong(5, itemSetId);
			pstCR.setLong(6, rosterID);
			pstCR.setString(7, PrismWebServiceConstant.CRItemResponseSetType);
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
			pstCR.setLong(4, rosterID);
			pstCR.setLong(5, itemSetId);
			pstCR.setLong(6, rosterID);
			pstCR.setString(7, PrismWebServiceConstant.GRItemResponseSetType);
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
	 * Get the Content Score Details
	 * @param studentId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static ContentScoreDetailsTO getContentScoreDetails(Integer studentId, long sessionId, long itemSetId) throws CTBBusinessException{
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		ContentScoreDetailsTO contentScoreDetailsTO = new ContentScoreDetailsTO();
		List<ContentScoreTO> contentScoreTOLst = contentScoreDetailsTO.getCollContentScoreTO(); 
		
		try {
			con = openIRSDBcon(false);
			pst = con.prepareStatement(GET_CONTENT_SCORE_DETAILS);
			pst.setLong(1, studentId);
			pst.setLong(2, sessionId);
			pst.setLong(3, itemSetId);
			rs = pst.executeQuery();
			while(rs.next()){
				ContentScoreTO NCcontentScoreTO = new ContentScoreTO();
				NCcontentScoreTO.setScoreType(PrismWebServiceConstant.NCContentScoreDetails);
				NCcontentScoreTO.setScoreValue(rs.getString("number_correct"));
				contentScoreTOLst.add(NCcontentScoreTO);
				
				ContentScoreTO NPcontentScoreTO = new ContentScoreTO();
				NPcontentScoreTO.setScoreType(PrismWebServiceConstant.NPContentScoreDetails);
				NPcontentScoreTO.setScoreValue(rs.getString("number_possible"));
				contentScoreTOLst.add(NPcontentScoreTO);
				
				ContentScoreTO SScontentScoreTO = new ContentScoreTO();
				SScontentScoreTO.setScoreType(PrismWebServiceConstant.SSContentScoreDetails);
				SScontentScoreTO.setScoreValue(rs.getString("scale_score"));
				contentScoreTOLst.add(SScontentScoreTO);
				
				ContentScoreTO HSEcontentScoreTO = new ContentScoreTO();
				HSEcontentScoreTO.setScoreType(PrismWebServiceConstant.HSEContentScoreDetails);
				HSEcontentScoreTO.setScoreValue(rs.getString("high_school_equiv"));
				contentScoreTOLst.add(HSEcontentScoreTO);
				
				ContentScoreTO PRcontentScoreTO = new ContentScoreTO();
				PRcontentScoreTO.setScoreType(PrismWebServiceConstant.PRContentScoreDetails);
				PRcontentScoreTO.setScoreValue(rs.getString("percentile_rank"));
				contentScoreTOLst.add(PRcontentScoreTO);
				
				ContentScoreTO NCEcontentScoreTO = new ContentScoreTO();
				NCEcontentScoreTO.setScoreType(PrismWebServiceConstant.NCEContentScoreDetails);
				NCEcontentScoreTO.setScoreValue(rs.getString("normal_curve_equivalent"));
				contentScoreTOLst.add(NCEcontentScoreTO);
				
				ContentScoreTO SSRcontentScoreTO = new ContentScoreTO();
				SSRcontentScoreTO.setScoreType(PrismWebServiceConstant.SSRContentScoreDetails);
				SSRcontentScoreTO.setScoreValue(rs.getString("hse_scale_score_range"));
				contentScoreTOLst.add(SSRcontentScoreTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return contentScoreDetailsTO;
	}
	
	
	/**
	 * Get the Sub Test Accommodation
	 * @param rosterId
	 * @param subtestAccommodationsTO
	 * @return
	 * @throws CTBBusinessException
	 */
	public static List<ContentDetailsTO> getContentDetailsTO(long rosterId, SubtestAccommodationsTO subtestAccommodationsTO, Integer studentId, long sessionId) throws CTBBusinessException{
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		PreparedStatement testTakenPst = null;
		ResultSet testTakenRS = null;
		List<ContentDetailsTO> contentDetailsTOList = new ArrayList<ContentDetailsTO>(); 
		
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_CONTENT_DETAILS);
			pst.setLong(1, rosterId);
			rs = pst.executeQuery();
			while(rs.next()){
				ContentDetailsTO contentDetailsTO = new ContentDetailsTO();
				contentDetailsTO.setDataChanged(true);
				String contentCodeName = rs.getString("content_code_name");
				Integer contentCode = PrismWebServiceConstant.contentDetailsContentCodeMap.get(contentCodeName);
				if(contentCode != null){
					contentDetailsTO.setContentCode(String.valueOf(contentCode));
					
					testTakenPst = con.prepareStatement(GET_TEST_TAKEN_DATE);
					testTakenPst.setLong(1, rs.getLong("item_set_id"));
					testTakenPst.setLong(2, rosterId);
					testTakenPst.setLong(3, rosterId);
					testTakenRS = testTakenPst.executeQuery();
					
					while(testTakenRS.next()){
						contentDetailsTO.setDateTestTaken(testTakenRS.getString("test_taken_dt"));
					}
					
					contentDetailsTO.setSubtestAccommodationsTO(subtestAccommodationsTO);
					
					ItemResponsesDetailsTO itemResponsesDetailsTO = getItemResponsesDetail(rosterId, rs.getLong("item_set_id"));
					contentDetailsTO.setItemResponsesDetailsTO(itemResponsesDetailsTO);
					
					ContentScoreDetailsTO contentScoreDetailsTO = getContentScoreDetails(studentId, sessionId, rs.getLong("item_set_id"));
					contentDetailsTO.setContentScoreDetailsTO(contentScoreDetailsTO);
					
					List<ObjectiveScoreDetailsTO> objectiveScoreDetailsList = getObjectiveScoreDetails(rs.getLong("item_set_id"), rosterId, sessionId, studentId);
					contentDetailsTO.getCollObjectiveScoreDetailsTO().addAll(objectiveScoreDetailsList);
					
					//TODO - Set the value
					contentDetailsTOList.add(contentDetailsTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(testTakenPst);
			close(testTakenRS);
			close(con, pst, rs);
		}
		return contentDetailsTOList;
	}
	
	/**
	 * Get the Objective Score Details
	 * @param itemSetId
	 * @param rosterId
	 * @param sessionId
	 * @param studentId 
	 * @return
	 */
	private static List<ObjectiveScoreDetailsTO> getObjectiveScoreDetails(
			long itemSetId, long rosterId, long sessionId, Integer studentId ) {
		// TODO Auto-generated method stub
		List<ObjectiveScoreDetailsTO> objectiveScoreDetailsLst = new ArrayList<ObjectiveScoreDetailsTO>();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection irsCon = null;
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_OBJECTIVE_LIST);
			pst.setLong(1, itemSetId);
			pst.setLong(2, rosterId);
			pst.setLong(3, sessionId);
			pst.setLong(4, itemSetId);
			pst.setLong(5, rosterId);
			pst.setLong(6, sessionId);
			rs = pst.executeQuery();
			irsCon = openIRSDBcon(false);
			while(rs.next()){
				PreparedStatement irsPst = null;
				ResultSet irsRs = null;
				ObjectiveScoreDetailsTO objectiveScoreDetails = new ObjectiveScoreDetailsTO();
				String highLvl = rs.getString("lvl");
				if("P".equals(highLvl)){
					try{
						irsPst = irsCon.prepareStatement(GET_PRIM_OBJ_SCORE);
						irsPst.setLong(1, rs.getLong("itemsetid"));
						irsPst.setLong(2, studentId);
						irsPst.setLong(3, sessionId);
						irsRs = irsPst.executeQuery();
						
						while(irsRs.next()){
							ObjectiveScoreTO NCobjectiveScoreTO = new ObjectiveScoreTO();
							NCobjectiveScoreTO.setScoreType(PrismWebServiceConstant.NCObjectiveScoreDetails);
							NCobjectiveScoreTO.setValue(irsRs.getString("numcorrect"));
							objectiveScoreDetails.getCollObjectiveScoreTO().add(NCobjectiveScoreTO);
							
							ObjectiveScoreTO NPobjectiveScoreTO = new ObjectiveScoreTO();
							NPobjectiveScoreTO.setScoreType(PrismWebServiceConstant.NPObjectiveScoreDetails);
							NPobjectiveScoreTO.setValue(irsRs.getString("numpossible"));
							objectiveScoreDetails.getCollObjectiveScoreTO().add(NPobjectiveScoreTO);
							
							ObjectiveScoreTO SSobjectiveScoreTO = new ObjectiveScoreTO();
							SSobjectiveScoreTO.setScoreType(PrismWebServiceConstant.SSObjectiveScoreDetails);
							SSobjectiveScoreTO.setValue(irsRs.getString("scalescore"));
							objectiveScoreDetails.getCollObjectiveScoreTO().add(SSobjectiveScoreTO);
							
							ObjectiveScoreTO MAobjectiveScoreTO = new ObjectiveScoreTO();
							MAobjectiveScoreTO.setScoreType(PrismWebServiceConstant.MAObjectiveScoreDetails);
							MAobjectiveScoreTO.setValue(irsRs.getString("mastery") != null ? irsRs.getString("mastery") : "-");
							objectiveScoreDetails.getCollObjectiveScoreTO().add(MAobjectiveScoreTO);
							
							ObjectiveScoreTO MRobjectiveScoreTO = new ObjectiveScoreTO();
							MRobjectiveScoreTO.setScoreType(PrismWebServiceConstant.MRObjectiveScoreDetails);
							MRobjectiveScoreTO.setValue(irsRs.getString("objmasscalescorerng"));
							objectiveScoreDetails.getCollObjectiveScoreTO().add(MRobjectiveScoreTO);
							
							objectiveScoreDetails.setObjectiveName(rs.getString("objname"));
							
						}
						
						objectiveScoreDetailsLst.add(objectiveScoreDetails);
						
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						close(irsPst);
						close(irsRs);
					}
					
				}else{
					try{
						irsPst = irsCon.prepareStatement(GET_SEC_OBJ_SCORE);
						irsPst.setLong(1, rs.getLong("itemsetid"));
						irsPst.setLong(2, studentId);
						irsPst.setLong(3, sessionId);
						irsRs = irsPst.executeQuery();
						
						while(irsRs.next()){
							ObjectiveScoreTO NCobjectiveScoreTO = new ObjectiveScoreTO();
							NCobjectiveScoreTO.setScoreType(PrismWebServiceConstant.NCObjectiveScoreDetails);
							NCobjectiveScoreTO.setValue(irsRs.getString("numcorrect"));
							objectiveScoreDetails.getCollObjectiveScoreTO().add(NCobjectiveScoreTO);
							
							ObjectiveScoreTO NPobjectiveScoreTO = new ObjectiveScoreTO();
							NPobjectiveScoreTO.setScoreType(PrismWebServiceConstant.NPObjectiveScoreDetails);
							NPobjectiveScoreTO.setValue(irsRs.getString("numpossible"));
							objectiveScoreDetails.getCollObjectiveScoreTO().add(NPobjectiveScoreTO);
							
							ObjectiveScoreTO SSobjectiveScoreTO = new ObjectiveScoreTO();
							SSobjectiveScoreTO.setScoreType(PrismWebServiceConstant.SSObjectiveScoreDetails);
							SSobjectiveScoreTO.setValue(irsRs.getString("scalescore"));
							objectiveScoreDetails.getCollObjectiveScoreTO().add(SSobjectiveScoreTO);
							
							ObjectiveScoreTO MAobjectiveScoreTO = new ObjectiveScoreTO();
							MAobjectiveScoreTO.setScoreType(PrismWebServiceConstant.MAObjectiveScoreDetails);
							MAobjectiveScoreTO.setValue(irsRs.getString("mastery") != null ? irsRs.getString("mastery") : "-");
							objectiveScoreDetails.getCollObjectiveScoreTO().add(MAobjectiveScoreTO);
							
							ObjectiveScoreTO MRobjectiveScoreTO = new ObjectiveScoreTO();
							MRobjectiveScoreTO.setScoreType(PrismWebServiceConstant.MRObjectiveScoreDetails);
							MRobjectiveScoreTO.setValue(irsRs.getString("objmasscalescorerng"));
							objectiveScoreDetails.getCollObjectiveScoreTO().add(MRobjectiveScoreTO);
							
							objectiveScoreDetails.setObjectiveName(rs.getString("objname"));
							
						}
						
						objectiveScoreDetailsLst.add(objectiveScoreDetails);
						
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						close(irsPst);
						close(irsRs);
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			close(irsCon);
			close(con, pst, rs);
		}
		return objectiveScoreDetailsLst;
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
			Date stdDOBDt = PrismWebServiceConstant.dateFormat.parse(studentDOB);
			long ageInMillis = new Date().getTime() - stdDOBDt.getTime();
			Date defaultStartDate = PrismWebServiceConstant.dateFormat.parse(PrismWebServiceConstant.defaultStartDateStr);
			long addedAgeWithDefaultStartDt = defaultStartDate.getTime() + ageInMillis;
			Date ageWithDefaultStartDate = new Date(addedAgeWithDefaultStartDt);
			return String.valueOf(ageWithDefaultStartDate.getYear() - defaultStartDate.getYear());
		}else{
			return "";
		}
	}
	
	/**
	 * Get OAS DB connection
	 * @param isCommitable
	 * @return
	 * @throws CTBBusinessException
	 */
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

	/**
	 * Get IRS DB connection
	 * @param isCommitable
	 * @return
	 * @throws CTBBusinessException
	 */
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
	
	/**
	 * 
	 * @param jndiName
	 * @return
	 * @throws NamingException
	 */
	private static DataSource locateDataSource(String jndiName ) throws NamingException{
		Context ctx = new InitialContext();
		DataSource ds =  (DataSource) ctx.lookup(jndiName);
		return ds;
	}
	
	/**
	 * Close connection
	 * @param con
	 */
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

	/**
	 * Close statement
	 * @param st
	 */
	private static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

	/**
	 * Close result set
	 * @param rs
	 */
	private static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}
	
	/**
	 * Close statement and result set
	 * @param st
	 * @param rs
	 */
	private static void close(Statement st, ResultSet rs) {
		close(rs);
		close(st);

	}
	
	/**
	 * Close connection, statement and result set 
	 * @param con
	 * @param st
	 * @param rs
	 */
	private static void close(Connection con, Statement st, ResultSet rs) {
		close(rs);
		close(st);
		close(con);

	}

	/**
	 * Close connection and statement
	 * @param con
	 * @param st
	 */
	private static void close(Connection con, Statement st) {
		close(st);
		close(con);
		
	}
	
	/**
	 * Read the Clob data
	 * @param clob
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private static String readOracleClob(CLOB clob) throws SQLException, IOException {
    	if(clob==null){
    		return "";
    	}
    	// get character stream to retrieve clob data
        Reader instream = clob.getCharacterStream();
        StringBuffer sb = new StringBuffer();
        // create temporary buffer for read
        char[] buffer = new char[1024];

        // length of characters read
        int length = 0;

        // fetch data
        while ((length = instream.read(buffer)) != -1)
        {
            sb.append(buffer, 0, length);
        }

        // Close input stream
        instream.close();

        return sb.toString();
    }
	
}
