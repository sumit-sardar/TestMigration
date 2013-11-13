/**
 * 
 */
package com.ctb.prism.web.dbutility;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
import com.ctb.prism.web.controller.StudentSurveyBioTO;
import com.ctb.prism.web.controller.SubtestAccommodationTO;
import com.ctb.prism.web.controller.SubtestAccommodationsTO;
import com.ctb.prism.web.controller.SurveyBioTO;



/**
 * @author TCS
 *
 */
public class PrismWebServiceDBUtility {
	private static final String oasDtataSourceJndiName = "oasDataSource";
	private static final String irsDtataSourceJndiName = "irsDataSource";
	
	private static final String GET_STUDENT_BIO = "select stu.student_id as id,       stu.user_name as loginId,       stu.first_name as firstName,       substr(stu.middle_name,1,1) as middleName,       stu.last_name as lastName,       concat(concat(stu.last_name, ', '), concat(stu.first_name, concat(' ', stu.MIDDLE_NAME))) as studentName,       stu.gender as gender,       to_char(stu.birthdate , 'MM/DD/YYYY') as birthDate,    to_char(stu.birthdate , 'MMDDYY') as birthDateMMDDYY,      stu.grade as grade,       stu.ext_pin1 as studentIdNumber,       stu.ext_pin2 as studentIdNumber2,       stu.test_purpose as testPurpose,       stu.created_by as createdBy,       NVL(stu.out_of_school, 'No') as outOfSchool  from student stu where stu.student_id = ?";
	private static final String GET_CUST_ORG_HIGR = "SELECT DISTINCT node.org_node_id            AS orgnodeid,                node.customer_id            AS customerid,                node.org_node_category_id   AS orgnodecategoryid,                node.org_node_name          AS orgnodename,                node.ext_qed_pin            AS extqedpin,                node.ext_elm_id             AS extelmid,                node.ext_org_node_type      AS extorgnodetype,                node.org_node_description   AS orgnodedescription,                node.created_by             AS createdby,                node.created_date_time      AS createddatetime,                node.updated_by             AS updatedby,                node.updated_date_time      AS updateddatetime,                node.activation_status      AS activationstatus,                node.data_import_history_id AS dataimporthistoryid,                node.parent_state           AS parentstate,                node.parent_region          AS parentregion,                node.parent_county          AS parentcounty,                node.parent_district        AS parentdistrict,                node.org_node_code          AS orgnodecode,                ona.number_of_levels        AS numberoflevels,                cat.category_name           AS orgtype  FROM org_node          node,       org_node_category cat,       org_node_ancestor ona,       org_node_student  ons,       test_roster    tr WHERE ona.ancestor_org_node_id = node.org_node_id   AND ona.org_node_id = ons.org_node_id   AND ons.org_node_id = tr.Org_Node_Id   AND tr.student_id  = ?   AND tr.test_roster_id = ?   AND node.org_node_id NOT IN (1, 2)   AND cat.org_node_category_id = node.org_node_category_id AND ons.activation_status = 'AC' ORDER BY ona.number_of_levels DESC";
	private static final String GET_ROSTER_LIST_FOR_STUDENT = "select t.test_roster_id as rosterId    from test_roster t   where t.student_id = ?";
	private static final String GET_STUDENT_DEMO = "select t.form_assignment as testForm    from test_roster t   where t.test_roster_id = ? ";
	private static final String GET_SUBTEST_ACCOM = "SELECT custdemo.label_code AS \"subTestAccom\",       decode((SELECT COUNT(1)                FROM student_demographic_data stddemo               WHERE stddemo.student_id = ?                 AND stddemo.customer_demographic_id =                     custdemo.customer_demographic_id),              0,              'N',              'Y') AS \"subTestAccomVal\"  FROM customer_demographic custdemo WHERE custdemo.customer_id =       (SELECT s.customer_id FROM student s WHERE s.student_id = ?)       AND (upper(custdemo.demo_category) = ?  OR custdemo.demo_category IS NULL) ";
	private static final String GET_ITEM_RESP_SR = "SELECT tab.itemtype   AS itemtype,       tab.response   AS response,       tab.itemid     AS itemid,       tab.itemorder  AS itemorder,       tab.correctans AS correctans  FROM (SELECT itm.item_type           AS itemtype,               res.response            AS response,               itm.item_id             AS itemid,               set_itm.item_sort_order AS itemorder,               itm.correct_answer      AS correctans          FROM item_response res, item_set_item set_itm, item itm         WHERE itm.item_id = set_itm.item_id           AND set_itm.item_set_id = res.item_set_id           AND set_itm.item_id = res.item_id           AND res.test_roster_id = ?           AND itm.item_type = ?           AND res.item_set_id IN               (SELECT DISTINCT t.item_set_id                  FROM item_set_parent t, item_response r                 WHERE t.parent_item_set_id = ?                   AND r.item_set_id = t.item_set_id                   AND r.test_roster_id = ?                 GROUP BY t.item_set_id)           AND res.item_response_id =               (SELECT MAX(r.item_response_id)                  FROM item_response r                 WHERE r.test_roster_id = ?                   AND r.item_set_id IN                       (SELECT DISTINCT t.item_set_id                          FROM item_set_parent t, item_response r                         WHERE t.parent_item_set_id = ?                           AND r.item_set_id = t.item_set_id                           AND r.test_roster_id = ?                         GROUP BY t.item_set_id)                   AND r.item_id = res.item_id)                UNION        SELECT itm.item_type AS itemtype,               '' AS response,               itm.item_id AS itemid,               t.item_sort_order AS itemorder,               itm.correct_answer AS correctans          FROM item_set_item t, item itm         WHERE t.item_set_id IN (SELECT DISTINCT t.item_set_id                                   FROM item_set_parent t, item_response r                                  WHERE t.parent_item_set_id = ?                                    AND r.item_set_id = t.item_set_id                                   AND r.test_roster_id = ?                                  GROUP BY t.item_set_id)           AND t.item_id = itm.item_id           AND itm.item_type = ?          AND NOT EXISTS (SELECT 1                  FROM item_response res                 WHERE res.test_roster_id = ?                   AND res.item_set_id =t.item_set_id                   AND res.item_id = t.item_id                   AND rownum = 1)) tab ORDER BY itemorder";
	private static final String GET_ITEM_RESP_CR = "SELECT /*to_char(SUM(decode(length(TRIM(translate(p.final_score,                                                ' +-.0123456789',                                                ' '))),                          NULL,                          p.final_score,                          0)),               '09')*/ p.final_score AS \"crScore\",       p.item_id AS \"itemId\",       setitm.item_sort_order AS \"iremOrder\"  FROM item_datapoint_score p, item_set_item setitm WHERE p.item_reader_id = (SELECT MAX(item_reader_id)                             FROM item_datapoint_score c                            WHERE c.test_roster_id = ?                              AND c.item_id = p.item_id   AND c.student_id = p.student_id)   AND p.test_roster_id = ?   AND p.item_id = setitm.item_id   AND setitm.item_set_id IN       ((SELECT DISTINCT t.item_set_id           FROM item_set_parent t, item_response_cr  r          WHERE t.parent_item_set_id = ?            AND r.item_set_id = t.item_set_id            AND r.test_roster_id = ?          GROUP BY t.item_set_id))   AND p.student_id = ? /*GROUP BY p.item_id, setitm.item_sort_order*/ ORDER BY setitm.item_sort_order";
	private static final String GET_ITEM_RESP_GR = "SELECT tab.itemtype  AS itemtype,       tab.response  AS response,       tab.itemid    AS itemid,       tab.itemorder AS itemorder  FROM (                SELECT 'GR' AS itemtype,                utl_url.unescape(dbms_lob.substr(crres.constructed_response,                                                 length(crres.constructed_response),                                                 1)) AS response,                critm.item_id AS itemid,                cr_set_itm.item_sort_order AS itemorder          FROM item_response_cr crres, item_set_item cr_set_itm, item critm         WHERE critm.item_id = cr_set_itm.item_id           AND cr_set_itm.item_set_id = crres.item_set_id           AND cr_set_itm.item_id = crres.item_id           AND crres.test_roster_id = ?           AND crres.item_set_id IN               (SELECT DISTINCT t.item_set_id                  FROM item_set_parent t, item_response_cr r                 WHERE t.parent_item_set_id = ?                   AND r.item_set_id = t.item_set_id                   AND r.test_roster_id = ?                 GROUP BY t.item_set_id)           AND crres.test_roster_id =               (SELECT MAX(r.test_roster_id)                  FROM item_response_cr r                 WHERE r.test_roster_id = ?                   AND r.item_set_id IN                       (SELECT DISTINCT t.item_set_id                          FROM item_set_parent t, item_response_cr r                         WHERE t.parent_item_set_id = ?                           AND r.item_set_id = t.item_set_id                           AND r.test_roster_id = ?                         GROUP BY t.item_set_id)                   AND r.item_id = crres.item_id)           AND critm.answer_area = ?        UNION        SELECT 'GR' AS itemtype,               '                ' AS response,               itm.item_id AS itemid,               t.item_sort_order AS itemorder          FROM item_set_item t, item itm         WHERE t.item_set_id IN (SELECT DISTINCT t.item_set_id                                   FROM item_set_parent t, item_response_cr r                                 WHERE t.parent_item_set_id = ?                                    AND r.item_set_id = t.item_set_id                                    AND r.test_roster_id = ?                                  GROUP BY t.item_set_id)           AND t.item_id = itm.item_id           AND itm.answer_area =?          AND NOT EXISTS (SELECT 1                  FROM item_response_cr res                 WHERE res.test_roster_id = ?                   AND res.item_set_id=t.item_set_id                   AND res.item_id = t.item_id                   AND rownum = 1)) tab ORDER BY itemorder";
	private static final String GET_CONTENT_SCORE_DETAILS = "SELECT t.points_obtained         AS number_correct,       t.points_possible         AS number_possible,       t.scale_score             AS scale_score,       t.proficiency_level                      AS high_school_equiv,       t.national_percentile     AS percentile_rank,       t.normal_curve_equivalent AS normal_curve_equivalent,       ''                        AS hse_scale_score_range, t.scoring_status AS scoring_status  FROM tasc_content_area_fact t WHERE t.studentid = ?   AND t.sessionid = ?   AND SUBSTR(t.content_areaid, 5) = ?";
	private static final String GET_CONTENT_DETAILS = "SELECT DISTINCT ipp.item_set_name AS \"content_code_name\",                ipp.item_set_id AS \"item_set_id\"  FROM student_item_set_status t,       item_set                s,       item_set_parent         ip,       item_set                ipp WHERE t.test_roster_id = ?   AND s.item_set_id = t.item_set_id   AND ip.item_set_id = s.item_set_id   AND ipp.item_set_id = ip.parent_item_set_id   AND s.SAMPLE = 'F'";
	private static final String GET_COMPOSITE_CONTENT_DETAILS = "SELECT t.points_obtained AS \"ncScoreVal\",       t.points_possible AS \"npScoreVal\",       t.scale_score AS \"ssScoreVal\",       0 AS \"hseScoreVal\",       t.national_percentile AS \"prScoreVal\",       t.normal_curve_equivalent AS \"nceScoreVal\",       '' AS \"ssrScoreVal\",       com.name AS \"compName\" ,     t.test_completion_timestamp AS \"dtTstTaken\"  FROM tasc_composite_fact t, composite_dim com WHERE t.studentid = ?   AND t.sessionid = ?   AND com.compositeid = t.compositeid";
	private static final String GET_TEST_TAKEN_DATE = "SELECT to_char(MAX(r.created_date_time),'MMDDYY') AS \"test_taken_dt\"  FROM item_response r WHERE r.item_set_id IN (SELECT DISTINCT t.item_set_id                           FROM item_set_parent t, item_response r                          WHERE t.parent_item_set_id = ?                            AND r.item_set_id = t.item_set_id                            AND r.test_roster_id = ?                          GROUP BY t.item_set_id)   AND r.test_roster_id = ?";
	private static final String GET_SUBTEST_STATUS = "SELECT t.validation_status AS status  FROM student_item_set_status t WHERE t.test_roster_id = ?   AND t.item_set_id IN (SELECT DISTINCT it.item_set_id                           FROM item_set_parent it                          WHERE it.parent_item_set_id = ?                          GROUP BY it.item_set_id)";
	private static final String GET_OBJECTIVE_LIST = "SELECT 'P' AS lvl,       prim.item_set_id AS itemsetid,       prim.item_set_name AS objname  FROM item_set prim,       item_set_item pisi,       item_set_ancestor pisip,       item_set_category pisc,       item_set sub,       item_set_item isi,       product prod,       item WHERE sub.item_set_id IN (SELECT DISTINCT t.item_set_id                             FROM item_set_parent t                            WHERE t.parent_item_set_id = ?                            GROUP BY t.item_set_id)   AND isi.item_set_id = sub.item_set_id   AND isi.item_id = pisi.item_id   AND pisip.item_set_id = pisi.item_set_id   AND pisip.ancestor_item_set_id = prim.item_set_id   AND prim.item_set_category_id = pisc.item_set_category_id   AND pisc.item_set_category_level = prod.scoring_item_set_level   AND prod.product_id IN (SELECT adm.product_id AS prodid                             FROM test_admin adm                            WHERE adm.test_admin_id = ?)   AND prod.parent_product_id = pisc.framework_product_id   AND isi.item_id = item.item_id   AND isi.suppressed = 'F'   AND item.item_type != 'RQ'   AND (sub.item_set_level != 'L' OR prod.product_type = 'TL') UNION SELECT 'S' AS lvl,       sec.item_set_id AS itemsetid,       sec.item_set_name AS objname  FROM item_set sec,       item_set_item sisi,       item_set_ancestor sisip,       item_set_category sisc,       item_set sub,       item_set_item isi,       product prod,       item WHERE sub.item_set_id IN (SELECT DISTINCT t.item_set_id                             FROM item_set_parent t                            WHERE t.parent_item_set_id = ?                            GROUP BY t.item_set_id)   AND isi.item_set_id = sub.item_set_id   AND isi.item_id = sisi.item_id   AND sisip.item_set_id = sisi.item_set_id   AND sisip.ancestor_item_set_id = sec.item_set_id   AND sec.item_set_category_id = sisc.item_set_category_id   AND sisc.item_set_category_level = prod.sec_scoring_item_set_level   AND prod.product_id IN (SELECT adm.product_id AS prodid                             FROM test_admin adm                           WHERE adm.test_admin_id = ?)   AND prod.parent_product_id = sisc.framework_product_id   AND isi.item_id = item.item_id   AND isi.suppressed = 'F'   AND item.item_type != 'RQ'   AND (sub.item_set_level != 'L' OR prod.product_type = 'TL')";
	private static final String GET_PRIM_OBJ_SCORE = "SELECT t.percent_obtained AS numcorrect,       t.points_possible AS numpossible,       '' AS scalescore,       t.mastery_levelid AS mastery,       '' AS objmasscalescorerng,       '' AS itmattempflag  FROM tasc_prim_obj_fact t WHERE substr(t.prim_objid, 5)  = ? AND t.studentid = ? AND t.sessionid = ?";
	private static final String GET_SEC_OBJ_SCORE = "SELECT t.percent_obtained AS numcorrect,       t.points_possible AS numpossible,       '' AS scalescore,       t.mastery_levelid AS mastery,       '' AS objmasscalescorerng,       '' AS itmattempflag  FROM tasc_sec_obj_fact t WHERE substr(t.sec_objid, 5) = ?   AND t.studentid = ?   AND t.sessionid = ?";
	private static final String GET_SURVEY_BIO_RES = "SELECT tab.\"response\" AS \"response\",       tab.\"quesId\" AS \"quesId\",       tab.\"quesOrder\" AS \"quesOrder\",       tab.\"quesCode\" AS \"quesCode\",       tab.\"quesType\" AS \"quesType\"  FROM (SELECT to_clob(serop.question_option_prism) AS \"response\",               serq.question_id AS \"quesId\",               serq.question_order AS \"quesOrder\",               serq.question_code AS \"quesCode\",               'SR' AS \"quesType\"          FROM item_response res, student_survey_question serq, item itm, STUDENT_SURVEY_QUESTION_OPTION serop         WHERE serq.product_id =               (SELECT tstad.product_id                  FROM test_roster ros, test_admin tstad                 WHERE ros.test_roster_id = ?                   AND tstad.test_admin_id = ros.test_admin_id                   AND rownum = 1)           AND res.item_id = serq.question_id           AND res.test_roster_id = ?           AND res.item_response_id =               (SELECT MAX(t.item_response_id)                  FROM item_response t                 WHERE t.test_roster_id = ?                   AND t.item_id = res.item_id)           AND itm.item_id = res.item_id           AND serop.question_option_oas = res.response           AND serop.question_id = res.item_id           AND serop.question_order = serq.question_order           AND itm.item_type = 'SR'         UNION ALL         SELECT crres.constructed_response AS \"response\",               serq.question_id AS \"quesId\",               serq.question_order AS \"quesOrder\",               serq.question_code AS \"quesCode\",               'GR' AS \"quesType\"          FROM item_response_cr        crres,               student_survey_question serq,               item                    itm         WHERE serq.product_id =               (SELECT tstad.product_id                  FROM test_roster ros, test_admin tstad                 WHERE ros.test_roster_id = ?                   AND tstad.test_admin_id = ros.test_admin_id                   AND rownum = 1)           AND crres.item_id = serq.question_id           AND crres.test_roster_id = ?           AND itm.item_id = crres.item_id          AND itm.item_type = 'GR'         UNION ALL         SELECT crres.constructed_response AS \"response\",               serq.question_id AS \"quesId\",              serq.question_order AS \"quesOrder\",               serq.question_code AS \"quesCode\",               'CR' AS \"quesType\"          FROM item_response_cr       crres,               student_survey_question serq,               item                    itm         WHERE serq.product_id =               (SELECT tstad.product_id                  FROM test_roster ros, test_admin tstad                 WHERE ros.test_roster_id = ?                   AND tstad.test_admin_id = ros.test_admin_id                   AND rownum = 1)           AND crres.item_id = serq.question_id           AND crres.test_roster_id = ?           AND itm.item_id = crres.item_id           AND itm.item_type = 'CR') tab ORDER BY tab.\"quesOrder\"";
	private static final String CHECK_CR_SCORE_PRESENT = "SELECT count(1) AS count_row FROM item_datapoint_score t WHERE t.test_roster_id = ? AND ROWNUM = 1";
	
	/**
	 * Get Student Bio Information
	 * @param studentId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static StudentBioTO getStudentBio(java.lang.Integer studentId){
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
			System.out.println("PrismWebServiceDBUtility.getStudentBio : Query for getStudentBio : " + GET_STUDENT_BIO);
			populateStudentBioTO(rs, std);
			//TODO - set student id(OAS_Stnt_ID attribute name) in std(Field was not available)
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return std;
	}

	/**
	 * Get Student Organization Information
	 * @param rosterID 
	 * @param orgNodeId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static CustHierarchyDetailsTO  getCustomerHigherarchy(Integer studentId, long rosterID){
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
			pst.setLong(2, rosterID);
			rs = pst.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getCustomerHigherarchy : Query for getCustomerHigherarchy : " + GET_CUST_ORG_HIGR);
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
	public static List<Long> getRosterListForStudent(Integer studentId) {
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		List<Long> rosterIds = new ArrayList<Long>(); 
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_ROSTER_LIST_FOR_STUDENT);
			pst.setLong(1, studentId);
			rs = pst.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getRosterListForStudent : Query for getRosterListForStudent : " + GET_ROSTER_LIST_FOR_STUDENT);
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
	public static StudentDemoTO getStudentDemo(long rosterId) {
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
			System.out.println("PrismWebServiceDBUtility.getStudentDemo : Query for getStudentDemo : " + GET_STUDENT_DEMO);
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
	public static SubtestAccommodationsTO getSubTestAccommodation(Integer studentId, String contentCodeName) {
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		SubtestAccommodationsTO subtestAccommodationsTO = new SubtestAccommodationsTO();
		List<SubtestAccommodationTO> subtestAccommodationLst = subtestAccommodationsTO.getCollSubtestAccommodationTO(); 
		
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_SUBTEST_ACCOM);
			pst.setLong(1, studentId);
			pst.setLong(2, studentId);
			pst.setString(3, PrismWebServiceConstant.subTestAccomCatNameMap.get(contentCodeName));
			rs = pst.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getSubTestAccommodation : Query for getSubTestAccommodation : " + GET_SUBTEST_ACCOM);
			while(rs.next()){
				SubtestAccommodationTO subtestAccommodationTO = new SubtestAccommodationTO();
				subtestAccommodationTO.setName(rs.getString("subTestAccom"));
				if("Y".equals(rs.getString("subTestAccomVal"))){
					subtestAccommodationTO.setValue("Y");
				}
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
	 * @param studentId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static ItemResponsesDetailsTO getItemResponsesDetail(long rosterID, long itemSetId, Integer studentId) {
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
			
			//SR item response is stored in java object
			pstSR = con.prepareStatement(GET_ITEM_RESP_SR);
			pstSR.setLong(1, rosterID);
			pstSR.setString(2, PrismWebServiceConstant.SRItemResponseSetType);
			pstSR.setLong(3, itemSetId);
			pstSR.setLong(4, rosterID);
			pstSR.setLong(5, rosterID);
			pstSR.setLong(6, itemSetId);
			pstSR.setLong(7, rosterID);
			pstSR.setLong(8, itemSetId);
			pstSR.setLong(9, rosterID);
			pstSR.setString(10, PrismWebServiceConstant.SRItemResponseSetType);
			pstSR.setLong(11, rosterID);
			rsSR = pstSR.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getItemResponsesDetail : Query for getItemResponsesDetail SR : " + GET_ITEM_RESP_SR);
			ItemResponseTO srItemResponseTO = new ItemResponseTO();
			srItemResponseTO.setItemSetType(PrismWebServiceConstant.SRItemResponseSetType);
			srItemResponseTO.setItemCode(PrismWebServiceConstant.itemResponseItemCodeMap.get(PrismWebServiceConstant.SRItemResponseSetType));
			
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
			
			//CR item response is stored in java object
			pstCR = con.prepareStatement(GET_ITEM_RESP_CR);
			pstCR.setLong(1, rosterID);
			pstCR.setLong(2, rosterID);
			pstCR.setLong(3, itemSetId);
			pstCR.setLong(4, rosterID);
			pstCR.setLong(5, studentId);
			rsCR = pstCR.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getItemResponsesDetail : Query for getItemResponsesDetail CR : " + GET_ITEM_RESP_CR);
			
			ItemResponseTO crItemResponseTO = new ItemResponseTO();
			crItemResponseTO.setItemSetType(PrismWebServiceConstant.CRItemResponseSetType);
			crItemResponseTO.setItemCode(PrismWebServiceConstant.itemResponseItemCodeMap.get(PrismWebServiceConstant.CRItemResponseSetType));
			StringBuffer crScoreVal =  new StringBuffer();
			boolean crItmPresent = false;
			while(rsCR.next()){
				crItmPresent = true;
				crScoreVal.append(formatResponse(rsCR.getString("crScore"), 2));
			}
			crItemResponseTO.setScoreValue(crScoreVal.toString());
			if(crItmPresent){
				itemResponseTOLst.add(crItemResponseTO);
			}
			
			//GR item response is stored in java object
			pstGR = con.prepareStatement(GET_ITEM_RESP_GR);
			pstGR.setLong(1, rosterID);
			pstGR.setLong(2, itemSetId);
			pstGR.setLong(3, rosterID);
			pstGR.setLong(4, rosterID);
			pstGR.setLong(5, itemSetId);
			pstGR.setLong(6, rosterID);
			pstGR.setString(7, PrismWebServiceConstant.GRIDItemResponseSetType);
			pstGR.setLong(8, itemSetId);
			pstGR.setLong(9, rosterID);
			pstGR.setString(10, PrismWebServiceConstant.GRIDItemResponseSetType);
			pstGR.setLong(11, rosterID);
			rsGR = pstGR.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getItemResponsesDetail : Query for getItemResponsesDetail GR : " + GET_ITEM_RESP_GR);
			ItemResponseTO grItemResponseTO = new ItemResponseTO();
			grItemResponseTO.setItemSetType(PrismWebServiceConstant.GRItemResponseSetType);
			grItemResponseTO.setItemCode(PrismWebServiceConstant.itemResponseItemCodeMap.get(PrismWebServiceConstant.GREditedResponseTxt));
			StringBuffer grScoreVal =  new StringBuffer();
			boolean grItmPresent = false;
			while(rsGR.next()){
				grItmPresent = true;
				grScoreVal.append(rsGR.getString("response"));
			}
			grItemResponseTO.setEditedResponse(formatResponse(grScoreVal.toString(), 16));
			if(grItmPresent){
				itemResponseTOLst.add(grItemResponseTO);
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
	 * Format the response
	 * @param res
	 * @param count 
	 * @return
	 */
	private static String formatResponse(String res, int count) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < count - res.length() ; i++){
			sb.append(" ");
		}
		sb.append(res);
		return sb.toString();
	}

	/**
	 * Get the Content Score Details
	 * @param studentId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static Object[] getContentScoreDetails(Integer studentId, long sessionId, long itemSetId) {
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		ContentScoreDetailsTO contentScoreDetailsTO = new ContentScoreDetailsTO();
		List<ContentScoreTO> contentScoreTOLst = contentScoreDetailsTO.getCollContentScoreTO(); 
		
		String scoringStatus = "";
		
		Object[] retObj = new Object[2];
		
		try {
			con = openIRSDBcon(false);
			pst = con.prepareStatement(GET_CONTENT_SCORE_DETAILS);
			pst.setLong(1, studentId);
			pst.setLong(2, sessionId);
			pst.setLong(3, itemSetId);
			rs = pst.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getContentScoreDetails : Query for getContentScoreDetails : " + GET_CONTENT_SCORE_DETAILS);
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
				
				//TODO - data not yet populated. Need the column name in query
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
				
				//TODO - data not yet populated. Need the column name in query
				ContentScoreTO SSRcontentScoreTO = new ContentScoreTO();
				SSRcontentScoreTO.setScoreType(PrismWebServiceConstant.SSRContentScoreDetails);
				SSRcontentScoreTO.setScoreValue(rs.getString("hse_scale_score_range"));
				contentScoreTOLst.add(SSRcontentScoreTO);
				
				scoringStatus = rs.getString("scoring_status");
			}
			
			retObj[0] = contentScoreDetailsTO;
			retObj[1] = scoringStatus;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return retObj;
	}
	
	
	/**
	 * Get the Sub Test Accommodation
	 * @param rosterId
	 * @param subtestAccommodationsTO
	 * @return
	 * @throws CTBBusinessException
	 */
	public static List<ContentDetailsTO> getContentDetailsTO(long rosterId,  Integer studentId, long sessionId) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		List<ContentDetailsTO> contentDetailsTOList = new ArrayList<ContentDetailsTO>(); 
		
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_CONTENT_DETAILS);
			pst.setLong(1, rosterId);
			rs = pst.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getContentDetailsTO : Query for getContentDetailsTO : " + GET_CONTENT_DETAILS);
			while(rs.next()){
				ContentDetailsTO contentDetailsTO = new ContentDetailsTO();
				contentDetailsTO.setDataChanged(true);
				String contentCodeName = rs.getString("content_code_name");
				Integer contentCode = PrismWebServiceConstant.contentDetailsContentCodeMap.get(contentCodeName);
				if(contentCode != null){
					contentDetailsTO.setContentCode(String.valueOf(contentCode));
					
					contentDetailsTO.setDataChanged(true);
					
					//Check the CR scoring availability for Writing Sub test, depending on that hold the send scoring for SR item
					if(contentCode == 2){
						boolean isCRScorePresent = checkCRScoreAvailablility(rosterId);
						if(!isCRScorePresent){
							contentDetailsTO.setStatusCode(PrismWebServiceConstant.OmitedContentStatusCode);
							contentDetailsTOList.add(contentDetailsTO);
							continue;
						}
					}
					
					contentDetailsTO.setDateTestTaken(getContentTestTakenDt(rosterId, rs.getLong("item_set_id")));
					
					contentDetailsTO.setStatusCode(getContentStatusCode(rosterId, rs.getLong("item_set_id")));
					
					if(PrismWebServiceConstant.subTestAccomCatNameMap.get(contentCodeName) != null){
						SubtestAccommodationsTO subtestAccommodationsTO =  PrismWebServiceDBUtility.getSubTestAccommodation(studentId, contentCodeName);
						contentDetailsTO.setSubtestAccommodationsTO(subtestAccommodationsTO);
					}
					Object[] contentScoreDetailsObjs = getContentScoreDetails(studentId, sessionId, rs.getLong("item_set_id"));
					
					String scoringStatus = (String) contentScoreDetailsObjs[1];
					
					if(scoringStatus != null && !"".equals(scoringStatus) && !PrismWebServiceConstant.VAScoringStatus.equalsIgnoreCase(scoringStatus)){
						contentDetailsTO.setStatusCode(scoringStatus);
						if(PrismWebServiceConstant.OmitedContentStatusCode.equalsIgnoreCase(scoringStatus)){
							contentDetailsTOList.add(contentDetailsTO);
							continue;
						}
					}
					
					ContentScoreDetailsTO contentScoreDetailsTO = (ContentScoreDetailsTO) contentScoreDetailsObjs[0];
					contentDetailsTO.setContentScoreDetailsTO(contentScoreDetailsTO);
					
					ItemResponsesDetailsTO itemResponsesDetailsTO = getItemResponsesDetail(rosterId, rs.getLong("item_set_id"),studentId);
					contentDetailsTO.setItemResponsesDetailsTO(itemResponsesDetailsTO);
					
					List<ObjectiveScoreDetailsTO> objectiveScoreDetailsList = getObjectiveScoreDetails(rs.getLong("item_set_id"), rosterId, sessionId, studentId);
					contentDetailsTO.getCollObjectiveScoreDetailsTO().addAll(objectiveScoreDetailsList);
					
					contentDetailsTOList.add(contentDetailsTO);
				}
			}
			
			//Get the composite content score details
			List<ContentDetailsTO> compContentDetails = getCompositeContentScoreDetails(studentId, sessionId);
			contentDetailsTOList.addAll(compContentDetails);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return contentDetailsTOList;
	}
	
	/**
	 * Check The CR scoring availability
	 * @param rosterId
	 * @return
	 */
	private static boolean checkCRScoreAvailablility(long rosterId) {
		
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		boolean checkCRScorePresent = false;
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(CHECK_CR_SCORE_PRESENT);
			pst.setLong(1, rosterId);
			rs = pst.executeQuery();
			System.out.println("PrismWebServiceDBUtility.checkCRScoreAvailablility : Query for checkCRScoreAvailablility : " + CHECK_CR_SCORE_PRESENT);
			if(rs.next() && rs.getInt("count_row") >= 1){
				checkCRScorePresent	= true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return checkCRScorePresent;
	}

	/**
	 * Get Content Test Taken Date
	 * @param rosterId
	 * @param itemSetId
	 * @return
	 */
	public static String getContentTestTakenDt(long rosterId, long itemSetId){
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		String testTakenDt = "";
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_TEST_TAKEN_DATE);
			pst.setLong(1, itemSetId);
			pst.setLong(2, rosterId);
			pst.setLong(3, rosterId);
			rs = pst.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getContentTestTakenDt : Query for getContentTestTakenDt : " + GET_TEST_TAKEN_DATE);
			while(rs.next()){
				testTakenDt = rs.getString("test_taken_dt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return testTakenDt;
	}
	
	/**
	 * Get the content status code
	 * @param rosterId
	 * @param itemSetId
	 * @return
	 */
	private static String getContentStatusCode(long rosterId, long itemSetId) {
		PreparedStatement subTestStatusPst = null;
		ResultSet subTestStatusRS = null;
		Connection con = null;
		try {
			con = openOASDBcon(false);
			subTestStatusPst = con.prepareStatement(GET_SUBTEST_STATUS);
			subTestStatusPst.setLong(1, rosterId);
			subTestStatusPst.setLong(2, itemSetId);
			System.out.println("PrismWebServiceDBUtility.getContentStatusCode : Query for getContentStatusCode : " + GET_SUBTEST_STATUS);
			subTestStatusRS = subTestStatusPst.executeQuery();

			while (subTestStatusRS.next()) {
				if (subTestStatusRS.getString("status") != null && PrismWebServiceConstant.InvalidContentStatus.equalsIgnoreCase(subTestStatusRS.getString("status"))) {
					return PrismWebServiceConstant.InvalidContentStatusCode;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close(con, subTestStatusPst,  subTestStatusRS);
		}
		return "";
	}
	
	

	/**
	 * Get the composite content score details
	 * @param studentId
	 * @param sessionId
	 * @return
	 * @throws CTBBusinessException
	 * @throws SQLException
	 */
	private static List<ContentDetailsTO> getCompositeContentScoreDetails(
			Integer studentId, long sessionId) {
		PreparedStatement compTestPst = null;
		ResultSet compTestRS = null;
		Connection irsCon = null;
		List<ContentDetailsTO> contentDetailsTOList = new ArrayList<ContentDetailsTO>();
		
		ContentDetailsTO elaContentDetailsTO =  new ContentDetailsTO();
		elaContentDetailsTO.setStatusCode(PrismWebServiceConstant.NACompositeStatusCode);
		contentDetailsTOList.add(elaContentDetailsTO);
		
		ContentDetailsTO overAllContentDetailsTO =  new ContentDetailsTO();
		overAllContentDetailsTO.setStatusCode(PrismWebServiceConstant.NACompositeStatusCode);
		
		contentDetailsTOList.add(overAllContentDetailsTO);
		
		try {
			irsCon = openIRSDBcon(false);
			compTestPst = irsCon
					.prepareStatement(GET_COMPOSITE_CONTENT_DETAILS);
			compTestPst.setLong(1, studentId);
			compTestPst.setLong(2, sessionId);
			compTestRS = compTestPst.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getCompositeContentScoreDetails : Query for getCompositeContentScoreDetails : " + GET_COMPOSITE_CONTENT_DETAILS);
			ContentScoreDetailsTO contentScoreDetailsTO = new ContentScoreDetailsTO();
			while (compTestRS.next()) {
				ContentDetailsTO contentDetailsTO = null;
				String contentCodeName = compTestRS.getString("compName");
				Integer contentCode = PrismWebServiceConstant.contentDetailsContentCodeMap.get(contentCodeName);
				if (contentCode != null) {
					if("ELA".equals(contentCodeName)){
						contentDetailsTO = elaContentDetailsTO;
					}else{
						contentDetailsTO = overAllContentDetailsTO;
					}
					contentDetailsTO.setContentCode(String.valueOf(contentCode));
					contentDetailsTO.setStatusCode("");
				}
				contentDetailsTO.setDataChanged(true);
				contentDetailsTO.setDateTestTaken(compTestRS.getString("dtTstTaken"));

				ContentScoreTO ncContentScoreTO = new ContentScoreTO();
				ncContentScoreTO.setScoreType(PrismWebServiceConstant.NCContentScoreDetails);
				ncContentScoreTO.setScoreValue(compTestRS.getString("ncScoreVal"));
				contentScoreDetailsTO.getCollContentScoreTO().add(ncContentScoreTO);

				ContentScoreTO npContentScoreTO = new ContentScoreTO();
				npContentScoreTO.setScoreType(PrismWebServiceConstant.NPContentScoreDetails);
				npContentScoreTO.setScoreValue(compTestRS.getString("npScoreVal"));
				contentScoreDetailsTO.getCollContentScoreTO().add(npContentScoreTO);

				ContentScoreTO ssContentScoreTO = new ContentScoreTO();
				ssContentScoreTO.setScoreType(PrismWebServiceConstant.SSContentScoreDetails);
				ssContentScoreTO.setScoreValue(compTestRS.getString("ssScoreVal"));
				contentScoreDetailsTO.getCollContentScoreTO().add(ssContentScoreTO);

				ContentScoreTO hseContentScoreTO = new ContentScoreTO();
				hseContentScoreTO.setScoreType(PrismWebServiceConstant.HSEContentScoreDetails);
				hseContentScoreTO.setScoreValue(compTestRS.getString("hseScoreVal"));
				contentScoreDetailsTO.getCollContentScoreTO().add(hseContentScoreTO);

				ContentScoreTO prContentScoreTO = new ContentScoreTO();
				prContentScoreTO.setScoreType(PrismWebServiceConstant.PRContentScoreDetails);
				prContentScoreTO.setScoreValue(compTestRS.getString("prScoreVal"));
				contentScoreDetailsTO.getCollContentScoreTO().add(prContentScoreTO);

				ContentScoreTO nceContentScoreTO = new ContentScoreTO();
				nceContentScoreTO.setScoreType(PrismWebServiceConstant.NCEContentScoreDetails);
				nceContentScoreTO.setScoreValue(compTestRS.getString("nceScoreVal"));
				contentScoreDetailsTO.getCollContentScoreTO().add(nceContentScoreTO);

				ContentScoreTO ssrContentScoreTO = new ContentScoreTO();
				ssrContentScoreTO.setScoreType(PrismWebServiceConstant.SSRContentScoreDetails);
				ssrContentScoreTO.setScoreValue(compTestRS.getString("ssrScoreVal"));
				contentScoreDetailsTO.getCollContentScoreTO().add(ssrContentScoreTO);

				contentDetailsTO.setContentScoreDetailsTO(contentScoreDetailsTO);
				// TODO - Set the value status code to contentDetailsTO
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(irsCon, compTestPst, compTestRS);
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
			pst.setLong(2, sessionId);
			pst.setLong(3, itemSetId);
			pst.setLong(4, sessionId);
			rs = pst.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getObjectiveScoreDetails : Query for getObjectiveScoreDetails : " + GET_OBJECTIVE_LIST);
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
						System.out.println("PrismWebServiceDBUtility.getObjectiveScoreDetails : Query for getObjectiveScoreDetails : " + GET_PRIM_OBJ_SCORE);
						
						while(irsRs.next()){
							//TODO - SSObjectiveScoreDetails, MRObjectiveScoreDetails is pending
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
						System.out.println("PrismWebServiceDBUtility.getObjectiveScoreDetails : Query for getObjectiveScoreDetails : " + GET_SEC_OBJ_SCORE);
						
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
	 * Get Student Survey Bio Response
	 * @param rosterId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static StudentSurveyBioTO getStudentSurveyBio(long rosterId) throws CTBBusinessException{
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		StudentSurveyBioTO studentSurveyBio = new StudentSurveyBioTO();
		List<SurveyBioTO>  surveyBioLst = studentSurveyBio.getCollSurveyBioTO();
		
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_SURVEY_BIO_RES);
			pst.setLong(1, rosterId);
			pst.setLong(2, rosterId);
			pst.setLong(3, rosterId);
			pst.setLong(4, rosterId);
			pst.setLong(5, rosterId);
			pst.setLong(6, rosterId);
			pst.setLong(7, rosterId);
			rs = pst.executeQuery();
			System.out.println("PrismWebServiceDBUtility.getStudentSurveyBio : Query for getStudentSurveyBio : " + GET_SURVEY_BIO_RES);
			String Ethnicity = "";
			Boolean RaceAmericanIndianAlaskaNative = false;
			Boolean RaceAsian = false;
			Boolean RaceBlack = false;
			Boolean RaceNativeHawaiian = false;
			Boolean RaceWhite = false;
			int RaceAnswerCount = 0;
			String ResolvedEthnicityRace = null;
			while(rs.next()){
				
				//** Resolve Ethnicity/Race Values
				if (rs.getString("quesCode").compareToIgnoreCase("Rslvd_Ethnic") == 0)
				{
					switch(Integer.getInteger(rs.getString("quesOrder")))
					{
					//1.	What is your ethnicity? Hispanic/Latino//Not Hispanic/Latino
					case 1:
						Ethnicity = readOracleClob(rs.getClob("response"));
						break;
					//2.	Is your race American Indian or Alaska Native? Yes/No	
					case 2:
						RaceAmericanIndianAlaskaNative = readOracleClob(rs.getClob("response")).compareToIgnoreCase("Y")==0?true:false;
						if (readOracleClob(rs.getClob("response")).length()!=0) RaceAnswerCount++;
						break;
					//3.	Is your race Asian? Yes/No
					case 3:
						RaceAsian = readOracleClob(rs.getClob("response")).compareToIgnoreCase("Y")==0?true:false;
						if (readOracleClob(rs.getClob("response")).length()!=0) RaceAnswerCount++;
						break;	
					//4.	Is your race Black or African American? Yes/No	
					case 4:
						RaceBlack = readOracleClob(rs.getClob("response")).compareToIgnoreCase("Y")==0?true:false;
						if (readOracleClob(rs.getClob("response")).length()!=0) RaceAnswerCount++;
						break;	
					//5.	Is your race Native Hawaiian or other Pacific Islander? Yes/No
					case 5:
						RaceNativeHawaiian = readOracleClob(rs.getClob("response")).compareToIgnoreCase("Y")==0?true:false;
						if (readOracleClob(rs.getClob("response")).length()!=0) RaceAnswerCount++;
						break;	
					//6.	Is your race White? Yes/No
					case 6:
						RaceWhite = readOracleClob(rs.getClob("response")).compareToIgnoreCase("Y")==0?true:false;
						if (readOracleClob(rs.getClob("response")).length()!=0) RaceAnswerCount++;
						break;							
					}
				}
				else
				{
					if (ResolvedEthnicityRace == null)
					{
						//Are marked 'Not Hispanic' and 'American Indian or Alaska Native' OR Did not mark Hispanic yes or no (left it blank) and are marked ‘American Indian or Alaska Native’
						if ((Ethnicity.compareToIgnoreCase("Not Hispanic")==0 || Ethnicity.compareToIgnoreCase("")==0) && RaceAmericanIndianAlaskaNative && RaceAnswerCount==1)
						{
							ResolvedEthnicityRace = "American Indian";
						}
						//Are marked 'Not Hispanic' and 'Black or African American' OR Did not mark Hispanic yes or no (left it blank) and are marked ‘Black or African American’
						else if ((Ethnicity.compareToIgnoreCase("Not Hispanic")==0 || Ethnicity.compareToIgnoreCase("")==0) && RaceBlack && RaceAnswerCount==1)
						{
							ResolvedEthnicityRace = "Black";
						}
						//*Are marked 'Not Hispanic' and 'Asian' OR 1) Did not mark Hispanic yes or no (left it blank) and are marked ‘Asian’
						else if ((Ethnicity.compareToIgnoreCase("Not Hispanic")==0 || Ethnicity.compareToIgnoreCase("")==0) && RaceAsian && RaceAnswerCount==1)
						{
							ResolvedEthnicityRace = "Asian";
						}	
						//Are marked 'Hispanic' and any single Race, any combination of 2 or more Races, or No Race 
						else if (Ethnicity.compareToIgnoreCase("Hispanic")==0 && RaceAnswerCount>=0)
						{
							ResolvedEthnicityRace = "Hispanic";
						}	
						//Are marked 'Not Hispanic' and 'White' OR Did not mark Hispanic yes or no (left it blank) and are marked ‘White’
						else if ((Ethnicity.compareToIgnoreCase("Not Hispanic")==0 || Ethnicity.compareToIgnoreCase("")==0) && RaceWhite && RaceAnswerCount==1)
						{
							ResolvedEthnicityRace = "White";
						}	
						//Are marked 'Not Hispanic' and 'Native Hawaiian/Other Pacific Islander' OR Did not mark Hispanic yes or no (left it blank) and are marked 'Native Hawaiian/Other Pacific Islander'
						else if ((Ethnicity.compareToIgnoreCase("Not Hispanic")==0 || Ethnicity.compareToIgnoreCase("")==0) && RaceNativeHawaiian && RaceAnswerCount==1)
						{
							ResolvedEthnicityRace = "Pacific Islander";
						}	
						//Are marked 'Not Hispanic' and any 2 or more Races OR Did not mark Hispanic (yes or no) and marked more than one race
						else if ((Ethnicity.compareToIgnoreCase("Not Hispanic")==0 || Ethnicity.compareToIgnoreCase("")==0) && RaceAnswerCount>=2)
						{
							ResolvedEthnicityRace = "Two or More Races";
						}		
						//If ‘Not Hispanic’ is marked and no Race is marked Or Did not mark either ‘Hispanic’ or ‘Not Hispanic’ and no Race is marked. Or If BOTH ‘Hispanic’ and ‘Not Hispanic’ are marked, regardless of what is indicated for Race
						else if ((Ethnicity.compareToIgnoreCase("Not Hispanic")==0 && RaceAnswerCount==0) || (Ethnicity.compareToIgnoreCase("Hispanic")!=0 && Ethnicity.compareToIgnoreCase("Not Hispanic")!=0 && RaceAnswerCount==0) || (Ethnicity.compareToIgnoreCase("Hispanic")==0 && Ethnicity.compareToIgnoreCase("Not Hispanic")==0 ))
						{
							ResolvedEthnicityRace = "";//blank or unknown
						}
						SurveyBioTO surveyBioTO = new SurveyBioTO();
						surveyBioTO.setSurveyName("Rslvd_Ethnic");
						surveyBioTO.setSurveyValue(ResolvedEthnicityRace);
						surveyBioLst.add(surveyBioTO);
					}
					SurveyBioTO surveyBioTO = new SurveyBioTO();
					surveyBioTO.setSurveyName(rs.getString("quesCode"));
					surveyBioTO.setSurveyValue(readOracleClob(rs.getClob("response")));
					surveyBioLst.add(surveyBioTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		
		studentSurveyBio.setDataChanged(true);
		
		return studentSurveyBio;
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
		custHierarchyDetailsTO.setTestName(PrismWebServiceConstant.CustHierarchyDetailsTestName);
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
	private static String getChronologicalAge_(String studentDOB) throws ParseException {
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
	 * Get the Chronological  Age
	 * @param studentDOB
	 * @return
	 * @throws ParseException 
	 */
	private static String getChronologicalAge(String studentDOB) throws ParseException {
		if(studentDOB != null && !"".equals(studentDOB)){
			Calendar dob = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
			dob.setTime(PrismWebServiceConstant.dateFormat.parse(studentDOB));
			int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH))
			{
				age--;
			}
			else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH))
			{
				age--;
			}
			return String.valueOf(age);
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
	private static String readOracleClob(Clob clob) throws SQLException, IOException {
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
