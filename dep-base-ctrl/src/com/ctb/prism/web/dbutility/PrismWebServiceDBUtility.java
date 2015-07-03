/**
 * 
 */
package com.ctb.prism.web.dbutility;

import java.io.IOException;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import weblogic.utils.StringUtils;

import com.ctb.exception.CTBBusinessException;
import com.ctb.prism.web.bean.PrismAuditLog;
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
import com.ctb.prism.web.handler.PrismWebServiceHandler;



/**
 * @author TCS
 *
 */
public class PrismWebServiceDBUtility {
	private static final String oasDtataSourceJndiName = "oasDataSource";
	private static final String irsDtataSourceJndiName = "irsDataSource";
	
	private static final String GET_STUDENT_BIO = "select stu.student_id as id,       stu.user_name as loginId,       stu.first_name as firstName,       substr(stu.middle_name,1,1) as middleName,       stu.last_name as lastName,       concat(concat(stu.last_name, ', '), concat(stu.first_name, concat(' ', stu.MIDDLE_NAME))) as studentName,       stu.gender as gender,       to_char(stu.birthdate , 'MM/DD/YYYY') as birthDate,    to_char(stu.birthdate , 'MMDDYY') as birthDateMMDDYY,      stu.grade as grade,       stu.ext_pin1 as studentIdNumber,       stu.ext_pin2 as studentIdNumber2,       stu.test_purpose as testPurpose,       stu.created_by as createdBy,       NVL(stu.out_of_school, 'No') as outOfSchool  from student stu where stu.student_id = ?";
	private static final String GET_STUDENT_BIO_SEC_EDITION = "SELECT STU.STUDENT_ID AS ID, STU.USER_NAME AS LOGINID, STU.FIRST_NAME AS FIRSTNAME, SUBSTR(STU.MIDDLE_NAME, 1, 1) AS MIDDLENAME, STU.LAST_NAME AS LASTNAME, CONCAT(CONCAT(STU.LAST_NAME, ', '), CONCAT(STU.FIRST_NAME, CONCAT(' ', STU.MIDDLE_NAME))) AS STUDENTNAME, STU.GRADE AS GRADE, STU.EXT_PIN1 AS STUDENTIDNUMBER, STU.EXT_PIN2 AS STUDENTIDNUMBER2, STU.TEST_PURPOSE AS TESTPURPOSE, STU.CREATED_BY AS CREATEDBY, NVL(STU.OUT_OF_SCHOOL, 'No') AS OUTOFSCHOOL FROM STUDENT STU WHERE STU.STUDENT_ID = ?";
	private static final String GET_CUST_ORG_HIGR = "SELECT DISTINCT node.org_node_id            AS orgnodeid,                node.customer_id            AS customerid,                node.org_node_category_id   AS orgnodecategoryid,                node.org_node_name          AS orgnodename,                node.ext_qed_pin            AS extqedpin,                node.ext_elm_id             AS extelmid,                node.ext_org_node_type      AS extorgnodetype,                node.org_node_description   AS orgnodedescription,                node.created_by             AS createdby,                node.created_date_time      AS createddatetime,                node.updated_by             AS updatedby,                node.updated_date_time      AS updateddatetime,                node.activation_status      AS activationstatus,                node.data_import_history_id AS dataimporthistoryid,                node.parent_state           AS parentstate,                node.parent_region          AS parentregion,                node.parent_county          AS parentcounty,                node.parent_district        AS parentdistrict,                node.org_node_code          AS orgnodecode,                ona.number_of_levels        AS numberoflevels,                cat.category_name           AS orgtype  FROM org_node          node,       org_node_category cat,       org_node_ancestor ona,       org_node_student  ons,       test_roster    tr WHERE ona.ancestor_org_node_id = node.org_node_id   AND ona.org_node_id = ons.org_node_id   AND ons.org_node_id = tr.Org_Node_Id   AND tr.student_id  = ?   AND tr.test_roster_id = ?   AND node.org_node_id NOT IN (1, 2)   AND cat.org_node_category_id = node.org_node_category_id /*AND ons.activation_status = 'AC'*/ AND ons.student_id = tr.student_id ORDER BY ona.number_of_levels DESC";
	private static final String GET_ROSTER_LIST_FOR_STUDENT = "select t.test_roster_id as rosterId    from test_roster t   where t.student_id = ?";
	private static final String GET_STUDENT_DEMO = "SELECT  s.item_set_form AS fldtestform, t.form_assignment AS testform     , ta.product_id AS prodid              FROM student_item_set_status st,                        item_set                s,                        item_set_parent         ip,                        item_set                ipp,test_roster t,    test_admin              ta              WHERE st.test_roster_id =  t.test_roster_id                    AND s.item_set_id = st.item_set_id                    AND ip.item_set_id = s.item_set_id                    AND ipp.item_set_id = ip.parent_item_set_id                    AND s.SAMPLE = 'F'                    AND t.test_roster_id = ?        AND ta.test_admin_id = t.test_admin_id            AND ROWNUM = 1 ";
	private static final String GET_SUBTEST_ACCOM = "SELECT custdemo.label_code AS \"subTestAccom\",       decode((SELECT COUNT(1)                FROM student_demographic_data stddemo               WHERE stddemo.student_id = ?                 AND stddemo.customer_demographic_id =                     custdemo.customer_demographic_id),              0,              'N',              'Y') AS \"subTestAccomVal\"  FROM customer_demographic custdemo WHERE custdemo.customer_id =       (SELECT s.customer_id FROM student s WHERE s.student_id = ?)       AND /*(*/upper(custdemo.demo_category) = ?  /*OR upper(custdemo.demo_category) = ? )*/ ";
	private static final String GET_ITEM_RESP_SR = "SELECT tab.itemtype AS itemtype, tab.response AS response, tab.itemid AS itemid, tab.itemorder AS itemorder, tab.correctans AS correctans, tab.fieldtest AS fieldtest, tab.suppressed AS suppressed  FROM (SELECT itm.item_type  AS itemtype, res.response AS response,  itm.item_id AS itemid, set_itm.item_sort_order AS itemorder, itm.correct_answer AS correctans, set_itm.field_test AS fieldtest, set_itm.suppressed AS suppressed FROM item_response res, item_set_item set_itm, item itm WHERE itm.item_id = set_itm.item_id AND set_itm.item_set_id = res.item_set_id AND set_itm.item_id = res.item_id AND res.test_roster_id = ? AND itm.item_type = ? AND res.item_set_id IN (SELECT DISTINCT t.item_set_id FROM item_set_parent t, item_response r, item_set itst, student_item_set_status s WHERE t.parent_item_set_id = ? AND t.item_set_id = s.item_set_id AND s.test_roster_id = ? AND r.item_set_id = t.item_set_id AND r.test_roster_id = ? AND itst.item_set_id = t.item_set_id AND itst.SAMPLE <> 'T' GROUP BY t.item_set_id) AND res.RESPONSE_SEQ_NUM =  (SELECT MAX(r.RESPONSE_SEQ_NUM) FROM item_response r WHERE r.test_roster_id = ?  AND r.item_set_id IN  (SELECT DISTINCT t.item_set_id  FROM item_set_parent t, item_response r, item_set itst, student_item_set_status s WHERE t.parent_item_set_id = ?  AND t.item_set_id = s.item_set_id AND s.test_roster_id = ? AND r.item_set_id = t.item_set_id AND r.test_roster_id = ?  AND itst.item_set_id = t.item_set_id AND itst.SAMPLE <> 'T' GROUP BY t.item_set_id) AND r.item_id = res.item_id) UNION SELECT itm.item_type AS itemtype, '' ASresponse, itm.item_id AS itemid, t.item_sort_order AS itemorder, itm.correct_answer AS correctans, t.field_test AS fieldtest, t.suppressed AS suppressed  FROM item_set_item t, item itm WHERE t.item_set_id IN (SELECT DISTINCT t.item_set_id FROM item_set_parent t, item_set itst, student_item_set_status s WHERE t.parent_item_set_id = ? AND t.item_set_id = s.item_set_id AND s.test_roster_id = ? AND itst.item_set_id = t.item_set_id AND itst.SAMPLE <> 'T' GROUP BY t.item_set_id) AND t.item_id = itm.item_id AND itm.item_type = ? AND NOT EXISTS (SELECT 1 FROM item_response res WHERE res.test_roster_id = ? AND res.item_set_id = t.item_set_id AND res.item_id = t.item_id AND rownum = 1)) tab ORDER BY itemorder";
	private static final String GET_ITEM_RESP_CR = "SELECT p.final_score AS crScore, p.item_id AS itemId, setitm.item_sort_order AS iremOrder FROM item_datapoint_score p, item_set_item setitm WHERE p.item_reader_id = (SELECT MAX(item_reader_id) FROM item_datapoint_score c WHERE c.test_roster_id = ? AND c.item_id = p.item_id  AND c.student_id = p.student_id) AND p.test_roster_id = ?   AND p.item_id = setitm.item_id   AND setitm.item_set_id IN ((SELECT DISTINCT t.item_set_id   FROM item_set_parent t, student_item_set_status s   WHERE t.item_set_id = s.item_set_id AND s.test_roster_id = ? AND t.parent_item_set_id = ? GROUP BY t.item_set_id))   AND p.student_id = ? AND setitm.field_test <> 'T' ORDER BY setitm.item_sort_order";
	private static final String GET_ITEM_RESP_GR = "SELECT tab.itemtype AS itemtype, tab.response AS response, tab.itemid AS itemid, tab.itemorder AS itemorder, DECODE(nvl(LENGTH(TRIM(TAB.RESPONSE)), 0), 0, 'O', TAB.RESPSTATUS) AS respstatus FROM   (SELECT 'GR' AS itemtype, utl_url.unescape(dbms_lob.substr(crres.constructed_response, length(crres.constructed_response), 1)) AS response, critm.item_id AS itemid,cr_set_itm.item_sort_order AS itemorder, '' AS respstatus    FROM item_response_cr crres,   item_set_item cr_set_itm,  item critm   WHERE critm.item_id = cr_set_itm.item_id  AND cr_set_itm.item_set_id = crres.item_set_id   AND cr_set_itm.item_id = crres.item_id    AND crres.test_roster_id = ?    AND crres.item_set_id IN   (SELECT DISTINCT t.item_set_id  FROM item_set_parent t, item_response_cr r, student_item_set_status s, item_set itst  WHERE t.item_set_id = s.item_set_id  AND t.parent_item_set_id = ?       AND r.item_set_id = t.item_set_id   AND r.test_roster_id = ?    AND s.test_roster_id = ?  AND itst.item_set_id = t.item_set_id  AND itst.SAMPLE <> 'T'   GROUP BY t.item_set_id)    AND crres.test_roster_id = (SELECT MAX(r.test_roster_id)  FROM item_response_cr r   WHERE r.test_roster_id = ?  AND r.item_set_id IN  (SELECT DISTINCT t.item_set_id  FROM item_set_parent t,  item_response_cr r, student_item_set_status s,item_set itst WHERE t.item_set_id = s.item_set_id AND t.parent_item_set_id = ?  AND r.item_set_id = t.item_set_id  AND r.test_roster_id = ?  AND s.test_roster_id = ? AND itst.item_set_id = t.item_set_id AND itst.SAMPLE <> 'T' GROUP BY t.item_set_id)  AND r.item_id = crres.item_id)  AND upper(critm.answer_area) = ?   AND cr_set_itm.field_test <> 'T'  UNION SELECT 'GR' AS itemtype,'                ' AS response,itm.item_id AS itemid, itset.item_sort_order AS itemorder, 'O' AS respstatus FROM item_set_item itset, item itm WHERE itset.item_set_id IN (SELECT DISTINCT t.item_set_id    FROM item_set_parent t ,  student_item_set_status s, item_set itst    WHERE t.item_set_id = s.item_set_id     AND t.parent_item_set_id = ?     AND itst.item_set_id = t.item_set_id     AND s.test_roster_id = ?  AND itst.SAMPLE <> 'T'    GROUP BY t.item_set_id)  AND itset.item_id = itm.item_id  AND upper(itm.answer_area) =?   AND itset.field_test <> 'T'  AND NOT EXISTS  (SELECT 1 FROM item_response_cr res  WHERE res.test_roster_id = ? AND res.item_set_id=itset.item_set_id  AND res.item_id = itset.item_id  AND rownum = 1)) tab ORDER BY itemorder";
	private static final String GET_CONTENT_SCORE_DETAILS = "SELECT t.points_obtained         AS number_correct,       t.points_possible         AS number_possible,       t.scale_score             AS scale_score,       t.proficiency_level                      AS high_school_equiv,       t.national_percentile     AS percentile_rank,       t.normal_curve_equivalent AS normal_curve_equivalent,       t.scale_score_range        AS hse_scale_score_range, t.scoring_status AS scoring_status  FROM tasc_content_area_fact t WHERE t.studentid = ?   AND t.sessionid = ?   AND t.content_areaid = ?";
	private static final String GET_CONTENT_DETAILS = "SELECT DISTINCT ipp.item_set_name AS \"content_code_name\",   ipp.item_set_id AS \"item_set_id\"  , t.completion_status AS \"completion_status\" , t.validation_status AS \"validation_status\" FROM student_item_set_status t,       item_set                s,       item_set_parent         ip,       item_set                ipp WHERE t.test_roster_id = ?   AND s.item_set_id = t.item_set_id   AND ip.item_set_id = s.item_set_id   AND ipp.item_set_id = ip.parent_item_set_id   AND s.SAMPLE = 'F'";
	private static final String GET_COMPOSITE_CONTENT_DETAILS = "SELECT t.points_obtained AS \"ncScoreVal\",       t.points_possible AS \"npScoreVal\",       t.scale_score AS \"ssScoreVal\",       0 AS \"hseScoreVal\",       t.national_percentile AS \"prScoreVal\",       t.normal_curve_equivalent AS \"nceScoreVal\",       '' AS \"ssrScoreVal\",       com.name AS \"compName\" ,     to_char(t.test_completion_timestamp, 'MMDDYY') AS \"dtTstTaken\"  FROM tasc_composite_fact t, composite_dim com WHERE t.studentid = ?   AND t.sessionid = ?   AND com.compositeid = t.compositeid";
	
	/** 
	    GET_TEST_TAKEN_DATE query changed for story "OAS-598 TASC Operational - PRISM export - Change Date Field", 
	 	fetches SISS.start_date_time in place of SISS.completion_date_time  
	*/													
	private static final String GET_TEST_TAKEN_DATE = "SELECT MIN(siss.start_date_time) AS test_taken_dt FROM student_item_set_status siss WHERE siss.test_roster_id = ? AND siss.item_set_id IN (SELECT DISTINCT t.item_set_id FROM item_set_parent t, item_set iset WHERE t.parent_item_set_id = ? and iset.item_set_id = t.item_set_id and iset.SAMPLE = 'F' GROUP BY t.item_set_id)";
	private static final String GET_SUBTEST_STATUS = "SELECT t.validation_status AS status  FROM student_item_set_status t,item_set its WHERE t.item_set_id = its.item_set_id and its.sample='F' and t.test_roster_id = ?   AND t.item_set_id IN (SELECT DISTINCT it.item_set_id   FROM item_set_parent it, student_item_set_status s WHERE it.parent_item_set_id = ? AND it.item_set_id = s.item_set_id AND s.test_roster_id = ?  GROUP BY it.item_set_id)";
	private static final String GET_OBJECTIVE_LIST = "SELECT 'S' AS lvl, prim.item_set_id AS itemsetid, prim.item_set_name AS objname, objdet.content_code || objdet.objective_code AS objcode  FROM item_set prim, item_set_item pisi, item_set_ancestor pisip, item_set_category pisc, item_set sub, item_set_item isi, product prod, item, objective_details objdet WHERE sub.item_set_id IN (SELECT DISTINCT t.item_set_id  FROM item_set_parent t, student_item_set_status s WHERE t.parent_item_set_id = ? AND t.item_set_id = s.item_set_id AND s.test_roster_id = ? GROUP BY t.item_set_id) AND isi.item_set_id = sub.item_set_id   AND isi.item_id = pisi.item_id   AND pisip.item_set_id = pisi.item_set_id   AND pisip.ancestor_item_set_id = prim.item_set_id   AND prim.item_set_category_id = pisc.item_set_category_id   AND pisc.item_set_category_level = prod.scoring_item_set_level AND prod.product_id IN (SELECT adm.product_id AS prodid FROM test_admin adm WHERE adm.test_admin_id = ?) AND prod.parent_product_id = pisc.framework_product_id AND isi.item_id = item.item_id AND isi.suppressed = 'F'   AND item.item_type != 'RQ'   AND (sub.item_set_level != 'L' OR prod.product_type = 'TL') AND objdet.content_code = ?   AND upper(objdet.objective_title) = upper(prim.item_set_name) UNION SELECT 'S' AS lvl, sec.item_set_id AS itemsetid, sec.item_set_name AS objname, objdet.content_code || objdet.objective_code AS objcode  FROM item_set sec, item_set_item sisi, item_set_ancestor sisip, item_set_category sisc, item_set sub, item_set_item isi, product prod, item, objective_details objdet WHERE sub.item_set_id IN (SELECT DISTINCT t.item_set_id FROM item_set_parent t, student_item_set_status s WHERE t.parent_item_set_id = ? AND t.item_set_id = s.item_set_id AND s.test_roster_id = ? GROUP BY t.item_set_id)   AND isi.item_set_id = sub.item_set_id   AND isi.item_id = sisi.item_id   AND sisip.item_set_id = sisi.item_set_id   AND sisip.ancestor_item_set_id = sec.item_set_id   AND sec.item_set_category_id = sisc.item_set_category_id   AND sisc.item_set_category_level = prod.sec_scoring_item_set_level   AND prod.product_id IN (SELECT adm.product_id AS prodid                             FROM test_admin adm                           WHERE adm.test_admin_id = ?)   AND prod.parent_product_id = sisc.framework_product_id   AND isi.item_id = item.item_id   AND isi.suppressed = 'F'   AND item.item_type != 'RQ'   AND (sub.item_set_level != 'L' OR prod.product_type = 'TL') AND objdet.content_code = ?   AND upper(objdet.objective_title) = upper(sec.item_set_name) ";
	private static final String GET_PRIM_OBJ_SCORE = "SELECT t.points_obtained AS numcorrect,       t.points_possible AS numpossible,       t.SCALE_SCORE AS scalescore,       t.mastery_levelid AS mastery,       '' AS objmasscalescorerng,       t.scoring_status AS itmattempflag  FROM tasc_prim_obj_fact t WHERE substr(t.prim_objid, 5)  = ? AND t.studentid = ? AND t.sessionid = ?";
	private static final String GET_SEC_OBJ_SCORE = "SELECT t.points_obtained AS numcorrect,       t.points_possible AS numpossible,       t.SCALE_SCORE AS scalescore,       t.mastery_levelid AS mastery,       t.scale_score_range AS objmasscalescorerng,       t.scoring_status AS itmattempflag ,       t.condition_code    AS conditioncode  FROM tasc_sec_obj_fact t WHERE substr(t.sec_objid, 5) = ?   AND t.studentid = ?   AND t.sessionid = ?";
	private static final String GET_SURVEY_BIO_RES = "SELECT tab.\"response\" AS \"response\",       tab.\"quesId\" AS \"quesId\",       tab.\"quesOrder\" AS \"quesOrder\",       tab.\"quesCode\" AS \"quesCode\",       tab.\"quesType\" AS \"quesType\"  FROM (SELECT to_clob(serop.question_option_prism) AS \"response\",               serq.question_id AS \"quesId\",               serq.question_order AS \"quesOrder\",               serq.question_code AS \"quesCode\",               'SR' AS \"quesType\"          FROM item_response res, student_survey_question serq, item itm, STUDENT_SURVEY_QUESTION_OPTION serop         WHERE serq.product_id =               (SELECT tstad.product_id                  FROM test_roster ros, test_admin tstad                 WHERE ros.test_roster_id = ?                   AND tstad.test_admin_id = ros.test_admin_id                   AND rownum = 1)           AND res.item_id = serq.question_id           AND res.test_roster_id = ?           AND res.RESPONSE_SEQ_NUM =               (SELECT MAX(t.RESPONSE_SEQ_NUM)                  FROM item_response t                 WHERE t.test_roster_id = ?                   AND t.item_id = res.item_id)           AND itm.item_id = res.item_id           AND serop.question_option_oas = res.response           AND serop.question_id = res.item_id           AND serop.question_order = serq.question_order           AND itm.item_type = 'SR'         UNION ALL         SELECT  to_clob(utl_url.unescape(dbms_lob.substr(crres.constructed_response,                                                length(crres.constructed_response),                                                1))) AS \"response\",               serq.question_id AS \"quesId\",               serq.question_order AS \"quesOrder\",               serq.question_code AS \"quesCode\",               'GR' AS \"quesType\"          FROM item_response_cr        crres,               student_survey_question serq,               item                    itm         WHERE serq.product_id =               (SELECT tstad.product_id                  FROM test_roster ros, test_admin tstad                 WHERE ros.test_roster_id = ?                   AND tstad.test_admin_id = ros.test_admin_id                   AND rownum = 1)           AND crres.item_id = serq.question_id           AND crres.test_roster_id = ?           AND itm.item_id = crres.item_id          AND   itm.answer_area = 'GRID'    UNION ALL         SELECT  to_clob(utl_url.unescape(dbms_lob.substr(crres.constructed_response,length(crres.constructed_response),1))) AS \"response\",               serq.question_id AS \"quesId\",              serq.question_order AS \"quesOrder\",               serq.question_code AS \"quesCode\",               'CR' AS \"quesType\"          FROM item_response_cr       crres,               student_survey_question serq,               item                    itm         WHERE serq.product_id =               (SELECT tstad.product_id                  FROM test_roster ros, test_admin tstad                 WHERE ros.test_roster_id = ?                   AND tstad.test_admin_id = ros.test_admin_id                   AND rownum = 1)           AND crres.item_id = serq.question_id           AND crres.test_roster_id = ?           AND itm.item_id = crres.item_id           AND itm.item_type = 'CR' AND (itm.answer_area is null  OR upper(itm.answer_area) = 'AUDIOITEM')) tab ORDER BY tab.\"quesOrder\"";
	//Changes for story : OAS-2544 // private static final String CHECK_CR_SCORE_PRESENT = "SELECT count(1) AS count_row FROM item_datapoint_score t WHERE t.test_roster_id = ? AND ROWNUM = 1";
	private static final String CHECK_CR_SCORE_PRESENT = "SELECT T.ITEM_ID AS CRITEMID, T.FINAL_SCORE AS RAWSCORE FROM ITEM_DATAPOINT_SCORE T, ITEM_SET_ITEM ISI, ITEM_SET_PARENT ISP, ITEM_SET ISET, ITEM  I WHERE T.TEST_ROSTER_ID = ? AND ISP.PARENT_ITEM_SET_ID = ? AND ISET.ITEM_SET_ID = ISP.ITEM_SET_ID AND ISET.SAMPLE = 'F' AND ISI.ITEM_SET_ID = ISET.ITEM_SET_ID AND ISET.ACTIVATION_STATUS = 'AC' AND ISI.SUPPRESSED = 'F' AND ISI.FIELD_TEST = 'F' AND I.ITEM_ID = ISI.ITEM_ID AND I.ITEM_TYPE = 'CR' AND I.ACTIVATION_STATUS = 'AC' AND I.ANSWER_AREA IS NULL AND T.ITEM_ID = I.ITEM_ID AND T.ITEM_READER_ID = (SELECT MAX(ITEM_READER_ID) FROM ITEM_DATAPOINT_SCORE IDS WHERE IDS.TEST_ROSTER_ID = T.TEST_ROSTER_ID AND IDS.ITEM_ID = T.ITEM_ID)";
	private static final String CHECK_CR_SCORE_IN_IRS = "SELECT IDIM.OAS_ITEMID, IFACT.POINTS_OBTAINED, SECFACT.CONDITION_CODE FROM TASC_ITEM_FACT IFACT, TASC_SEC_OBJ_FACT SECFACT, ITEM_DIM IDIM WHERE SECFACT.SESSIONID = IFACT.SESSIONID AND SECFACT.STUDENTID = IFACT.STUDENTID AND SECFACT.SEC_OBJID = IDIM.SEC_OBJID AND IDIM.ITEMID = IFACT.ITEMID AND IFACT.SESSIONID = ? AND IFACT.STUDENTID = ? AND IDIM.OAS_ITEMID IN (?)";
	
	private static final String GET_GR_RESP_STATUS = "SELECT decode(t.points_obtained, 1, 'R', 0, 'W', 'O') AS respstatus,       t.points_obtained AS origresp,       dim.oas_itemid AS itmid  FROM tasc_item_fact t, item_dim dim WHERE t.studentid = ?   AND t.sessionid = ?  AND t.itemid = dim.itemid  AND dim.oas_itemid IN ";
	private static final String GET_CONTENT_AREA_ID = "SELECT DISTINCT productid,       contentareaid,       contentareaname  FROM (SELECT DISTINCT dp.item_id,                        dp.max_points,                        productid,                        contentareaid,                        contentareaname,                        contentareatype,                        subject,                        contentareanumitems,                        subtestform,                        subtestlevel,                        subtestid          FROM (SELECT DISTINCT prod.product_id AS productid,                                prod.product_id || ca.item_set_id AS contentareaid,                                ca.item_set_name AS contentareaname,                                prod.product_type || ' CONTENT AREA' AS contentareatype,                                prod.product_type || ' ' || ca.item_set_name AS subject,                                COUNT(DISTINCT item.item_id) AS contentareanumitems,                                td.item_set_form AS subtestform,                                td.item_set_level AS subtestlevel,                                td.item_set_id AS subtestid                  FROM item,                       item_set ca,                       item_set_category cacat,                       item_set_ancestor caisa,                       item_set_item caisi,                       item_set_ancestor tcisa,                       item_set_item tcisi,                       test_roster ros,                       test_admin adm,                       test_catalog tc,                       product prod,                       item_set td                 WHERE ros.test_roster_id = ?                   AND adm.test_admin_id = ros.test_admin_id                   AND tc.test_catalog_id = adm.test_catalog_id                   AND prod.product_id = tc.product_id                   AND item.activation_status = 'AC'                   AND tc.activation_status = 'AC'                   AND ca.item_set_id = caisa.ancestor_item_set_id                   AND ca.item_set_type = 'RE'                   AND caisa.item_set_id = caisi.item_set_id                  AND item.item_id = caisi.item_id                   AND tcisi.suppressed = 'F'                   AND tcisi.item_id = item.item_id                  AND tcisa.item_set_id = tcisi.item_set_id                   AND adm.item_set_id = tcisa.ancestor_item_set_id                   AND cacat.item_set_category_id = ca.item_set_category_id                   AND cacat.item_set_category_level =                      prod.content_area_level                   AND td.item_set_id = tcisi.item_set_id                   AND td.SAMPLE = 'F'                  AND (td.item_set_level != 'L' OR prod.product_type = 'TL')                   AND cacat.framework_product_id = prod.parent_product_id                GROUP BY prod.product_id,                          prod.product_id || ca.item_set_id,                          ca.item_set_name,                         prod.product_type || ' CONTENT AREA',                          prod.product_type || ' ' || ca.item_set_name,                         td.item_set_form,                          td.item_set_level,                          td.item_set_id) derived,               datapoint dp,              item_set_item isi         WHERE isi.item_set_id = derived.subtestid           AND isi.suppressed = 'F'           AND dp.item_id =isi.item_id) derived1 GROUP BY productid,          contentareaid,          contentareaname";
	private static final String GET_CUST_CONF_ACCOMMODATION = "SELECT * FROM student_accommodation t WHERE t.student_id = ? ";
	private static final String GET_CUSTOMER_KEY = "select distinct  bridge.SYSTEM_KEY as systemKey,  bridge.CUSTOMER_KEY as customerKey from  customer_report_bridge bridge where  bridge.customer_id = ? and bridge.product_id = 4500 and bridge.report_name='Prism' ";
	private static final String GET_PRISMWS_URL = " SELECT DISTINCT resource_URI as resourceURL  from customer_resource Where customer_id = ? and Resource_type_code = 'PRISMWSURL' and ROWNUM = 1 ";
	
	private static final String INSERT_WS_ERROR_LOG = "{CALL INSERT INTO ws_error_log  (ws_error_log_key,   student_id,   roster_id,   session_id,   status,   invoke_count,   ws_type,   message, ADDITIONAL_INFO) VALUES  (SEQ_WS_ERROR_LOG_KEY.NEXTVAL,   ?,   ?,   ?,   'Progress',   0,   ?,   ?,  ?) RETURNING ws_error_log_key INTO ?}";
	private static final String DELETE_WS_ERROR_LOG = "DELETE WS_ERROR_LOG WHERE WS_ERROR_LOG_KEY = ?";
	private static final String UPDATE_WS_ERROR_LOG = "UPDATE ws_error_log   SET invoke_count = ?, message = ?, updated_date = SYSDATE, status = ?, ADDITIONAL_INFO = ? WHERE ws_error_log_key = ?";
	private static final String SELECT_WS_ERROR_LOG = "select a.UPDATED_DATE UPDATEDATE,a.WS_ERROR_LOG_KEY LOGKEY,a.INVOKE_COUNT INVKCOUNT,a.STUDENT_ID STDID,a.ROSTER_ID RSTRID,a.SESSION_ID SESSIONID,a.WS_TYPE WSTYP from WS_ERROR_LOG a, TMP_WS_ERROR_LOG_ROWNUM t where a.rowid = t.row_id order by a.UPDATED_DATE";
	private static final String SP_FETCH_WS_ERRORS = "{call sp_fetch_ws_errors(?)}";
	private static final String INSERT_WS_ERROR_LOG_ERERROR ="{CALL INSERT INTO ws_error_log (ws_error_log_key,student_id, roster_id, session_id, status, invoke_count, ws_type, message, ADDITIONAL_INFO) VALUES (SEQ_WS_ERROR_LOG_KEY.NEXTVAL, ?, ?, ?, 'Failed', 0, 'Scoring', 'eResource error', '')  RETURNING ws_error_log_key INTO ?}";

	private static final String CHECK_ROSTER_STATUS = "SELECT 1  FROM TEST_ROSTER T WHERE T.TEST_ROSTER_ID = ?   AND (T.TEST_COMPLETION_STATUS = 'SC' OR T.TEST_COMPLETION_STATUS = 'NT')";
	private static final String GET_SESSION_TIMEZONE = "SELECT TIME_ZONE as session_time_zone FROM test_admin WHERE TEST_ADMIN_ID = ?";
	private static final String GET_ROSTER_FORM = "SELECT FORM_ASSIGNMENT FROM TEST_ROSTER WHERE TEST_ROSTER_ID = ?";
	
	/**
	 * Prism Audit purpose DB Calls.
	 */
	private static final String INSERT_WS_AUDIT_LOG = " { CALL INSERT INTO PRISM_WS_AUDIT_LOG (INVOKE_ID, ROSTER_ID, STUDENT_ID, SESSION_ID, WS_INVOKE_TIMESTAMP, RETRY_PROCESS, RETRY_ERROR_LOG_ID , WS_TYPE, OAS_XML_SENT) VALUES ( SEQ_PRISM_AUDIT_LOG_ID.NEXTVAL , ? , ? , ? , ? , ? , ? , ? , ? ) RETURNING INVOKE_ID INTO ? } ";
	private static final String UPDATE_WS_AUDIT_LOG = " UPDATE PRISM_WS_AUDIT_LOG  SET WS_RESPONSE_TIMESTAMP = ? ,  PRISM_PROCESS_ID = ? , PRISM_PARTITION_ID = ? , STATUS_CODE = ? , MESSAGE = ? , WS_ERROR_LOG_KEY = ? , PRISM_RESPONSE = ? , UPDATED_DATE_TIME = ?  WHERE INVOKE_ID = ?  ";
	
	
	/**
	 * Get Prism Web Service URL
	 * @param customerId
	 * @return
	 */
	public static String getPrismWSURL(Integer customerId){
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		String customerURL = "";
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_PRISMWS_URL);
			pst.setInt(1, customerId);
			rs = pst.executeQuery();
			while(rs.next()){
				customerURL = rs.getString("resourceURL");
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getPrismWSURL() method to execute query : \n " +  GET_PRISMWS_URL);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return customerURL;
	}
	
	/**
	 * Get Student Bio Information
	 * @param studentId
	 * @return
	 * @throws CTBBusinessException
	 */
	public static StudentBioTO getStudentBio(java.lang.Integer studentId){
		
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
			System.err.println("Error in the PrismWebServiceDBUtility.getStudentBio() method to execute query : \n " +  GET_STUDENT_BIO);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return std;
	}
	
	/**
	 * Get Student Bio Information for Second Edition
	 * @param studentId
	 * @return std (StudentBioTO)
	 */
	public static StudentBioTO getStudentBioSecondEdition(java.lang.Integer studentId){
		
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		StudentBioTO std = new StudentBioTO();
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_STUDENT_BIO_SEC_EDITION);
			pst.setLong(1, studentId);
			rs = pst.executeQuery();
			populateStudentBioTOSecondEdition(rs, std);
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getStudentBioSecondEdition() method to execute query : \n " +  GET_STUDENT_BIO_SEC_EDITION);
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
			populateCustHierarchyDetailsTO(rs, custHierarchyDetailsTO);
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getCustomerHigherarchy() method to execute query : \n " +  GET_CUST_ORG_HIGR);
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
			while(rs.next()){
				rosterIds.add(rs.getLong("rosterId"));
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getRosterListForStudent() method to execute query : \n " +  GET_ROSTER_LIST_FOR_STUDENT);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return rosterIds;
	}
	
	/**
	 * This method inserts audit data in PRISM_WS_AUDIT_LOG table.
	 * @param auditLog
	 * @return
	 */
	public static Long insertWSAuditTableDate(PrismAuditLog auditLog) {
		CallableStatement cst = null;
		Connection con = null;
		ResultSet rs = null;
		long invokeId = 0;
		try {
			con = openOASDBcon(false);
			cst = con.prepareCall(INSERT_WS_AUDIT_LOG);
			cst.setLong(1, auditLog.getRosterId());
			cst.setLong(2, auditLog.getStudentId());
			cst.setLong(3, auditLog.getSessionId());
			cst.setTimestamp(4, new Timestamp(auditLog.getWsInvokeTimestamp().longValue()));
			cst.setString(5, auditLog.getRetryProcess());
			if (auditLog.getRetryErrorLogId() == null) {
				cst.setObject(6 , null );
			}else {
				cst.setLong(6 , auditLog.getRetryErrorLogId());
			}
			cst.setString(7, auditLog.getWsType());
			cst.setString(8, auditLog.getOasXmlSent());
			cst.registerOutParameter(9, Types.NUMERIC);
			int count = cst.executeUpdate();
			if(count > 0){
				invokeId = cst.getLong(9);
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.insertWSAuditTableDate() method to execute query : \n " +  INSERT_WS_AUDIT_LOG);
			e.printStackTrace();
		} finally {
			close(con, cst, rs);
		}
		return invokeId;
	}
	
	
	/**
	 * This method inserts audit data in PRISM_WS_AUDIT_LOG table.
	 * @param auditLog
	 * @return
	 */
	public static void updateWSAuditTableDate(PrismAuditLog auditLog) {
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(UPDATE_WS_AUDIT_LOG);
			if(auditLog.getWsResponseTimestamp() == null){
				pst.setObject(1, null);
			}else{
				pst.setTimestamp(1, new Timestamp(auditLog.getWsResponseTimestamp().longValue()));
			}
			pst.setString(2, auditLog.getPrismProcessId());
			pst.setString(3, auditLog.getPrismPartitionId());
			pst.setString(4, auditLog.getStatusCode());
			pst.setString(5, auditLog.getMessage());
			if(auditLog.getErrorLogKey() == null){
				pst.setObject(6, null);
			}else{
				pst.setLong(6, auditLog.getErrorLogKey());
			}
			pst.setString(7, auditLog.getPrismResponse());
			pst.setTimestamp(8, new Timestamp(auditLog.getUpdatedDateTime().longValue()));
			pst.setLong(9, auditLog.getInvokeId());
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error in the PrismWebServiceDBUtility.updateWSAuditTableDate() method to execute query : \n " +  UPDATE_WS_AUDIT_LOG);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
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
			while(rs.next()){
				DemoTO demoTOTstFrm = new DemoTO();
				demoTOTstFrm.setDemoName("Test_Form");
				String testForm = rs.getString("testForm");
				if(testForm != null && testForm.startsWith("A")){
					demoTOTstFrm.setDemovalue(PrismWebServiceConstant.ATestFormStr);
				}else if(testForm != null && testForm.startsWith("B")){
					demoTOTstFrm.setDemovalue(PrismWebServiceConstant.BTestFormStr);
				}else if(testForm != null && testForm.startsWith("C")){
					demoTOTstFrm.setDemovalue(PrismWebServiceConstant.CTestFormStr);
				}else if(testForm != null && testForm.startsWith("D")){
					demoTOTstFrm.setDemovalue(PrismWebServiceConstant.DTestFormStr);
				}else if(testForm != null && testForm.startsWith("E")){
					demoTOTstFrm.setDemovalue(PrismWebServiceConstant.ETestFormStr);
				}else if(testForm != null && testForm.startsWith("F")){
					demoTOTstFrm.setDemovalue(PrismWebServiceConstant.FTestFormStr);
				}
				demoList.add(demoTOTstFrm);
				
				DemoTO demoTOFldTstFrm = new DemoTO();
				demoTOFldTstFrm.setDemoName("Fld_Tst_Form");
				String demoValue=rs.getString("fldtestform");
				if(demoValue.contains("-")){
					demoTOFldTstFrm.setDemovalue(demoValue.substring(0,demoValue.indexOf("-")));
				}else{
					demoTOFldTstFrm.setDemovalue(demoValue);
				}
				demoList.add(demoTOFldTstFrm);
				
				DemoTO demoTOTstPltFrm = new DemoTO();
				demoTOTstPltFrm.setDemoName("Tst_Platform");
				demoTOTstPltFrm.setDemovalue("0");
				demoList.add(demoTOTstPltFrm);
				try{
					String testLang = PrismWebServiceConstant.resourceBundler.getString(rs.getString("prodid"));
					if(testLang != null && !"".equals(testLang)){
						DemoTO demoTOTstLang = new DemoTO();
						demoTOTstLang.setDemoName("Test_Lan");
						demoTOTstLang.setDemovalue(testLang);
						demoList.add(demoTOTstLang);
					}
				}catch(Exception e){
					System.err.println("Error to find the test language in property file for product id : " + rs.getString("prodid") + " for the roster : " + rosterId);
				}
			}
			studentDemoTO.setDataChanged(true);
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getStudentDemo() method to execute query : \n " +  GET_STUDENT_DEMO);
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
			//pst.setString(4, PrismWebServiceConstant.AddStdInfoSubTestAcc);
			rs = pst.executeQuery();
			while(rs.next()){
				SubtestAccommodationTO subtestAccommodationTO = new SubtestAccommodationTO();
				subtestAccommodationTO.setName(rs.getString("subTestAccom"));
				if("Y".equals(rs.getString("subTestAccomVal"))){
					subtestAccommodationTO.setValue("Y");
				}
				subtestAccommodationLst.add(subtestAccommodationTO);
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getSubTestAccommodation() method to execute query : \n " +  GET_SUBTEST_ACCOM);
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
	 * @param sessionId 
	 * @return
	 * @throws CTBBusinessException
	 */
	public static ItemResponsesDetailsTO getItemResponsesDetail(long rosterID, long itemSetId, Integer studentId, long sessionId, boolean invStatus) {
		Connection con = null;
		PreparedStatement pstSR = null;
		ResultSet rsSR = null;
		PreparedStatement pstCR = null;
		ResultSet rsCR = null;
		PreparedStatement pstGR = null;
		ResultSet rsGR = null;
		ItemResponsesDetailsTO itemResponsesDetailsTO = new ItemResponsesDetailsTO();
		List<ItemResponseTO> itemResponseTOLst =  itemResponsesDetailsTO.getItemResponseTO();
		try {
			con = openOASDBcon(false);
			
			//SR item response is stored in java object
			pstSR = con.prepareStatement(GET_ITEM_RESP_SR);
			pstSR.setLong(1, rosterID);
			pstSR.setString(2, PrismWebServiceConstant.SRItemResponseSetType);
			pstSR.setLong(3, itemSetId);
			pstSR.setLong(4, rosterID);
			pstSR.setLong(5, rosterID);
			pstSR.setLong(6, rosterID);
			pstSR.setLong(7, itemSetId);
			pstSR.setLong(8, rosterID);
			pstSR.setLong(9, rosterID);
			pstSR.setLong(10, itemSetId);
			pstSR.setLong(11, rosterID);
			pstSR.setString(12, PrismWebServiceConstant.SRItemResponseSetType);
			pstSR.setLong(13, rosterID);
			rsSR = pstSR.executeQuery();
			ItemResponseTO srItemResponseTO = new ItemResponseTO();
			srItemResponseTO.setItemSetType(PrismWebServiceConstant.SRItemResponseSetType);
			srItemResponseTO.setItemCode(PrismWebServiceConstant.itemResponseItemCodeMap.get(PrismWebServiceConstant.SRItemResponseSetType));
			
			StringBuffer stScoreVal = new StringBuffer(); 
			boolean srItmPresent = false;
			while(rsSR.next()){
				srItmPresent = true;
				String stdRes = rsSR.getString("RESPONSE");
				String correctAns = rsSR.getString("CORRECTANS");
				String fieldTest = rsSR.getString("fieldtest");
				//String suppressed = rsSR.getString("suppressed");
				if(!("F".equalsIgnoreCase(fieldTest) /*&& "F".equalsIgnoreCase(suppressed)*/)){//suppressed is commented for defect 76233
					stScoreVal.append(" ");//Set the blank value Field Test SR item
				}else if(stdRes == null || "".equals(stdRes)){
					stScoreVal.append(" ");
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
			pstCR.setLong(3, rosterID);
			pstCR.setLong(4, itemSetId);
			pstCR.setLong(5, studentId);
			rsCR = pstCR.executeQuery();

			ItemResponseTO crItemResponseTO = new ItemResponseTO();
			crItemResponseTO.setItemSetType(PrismWebServiceConstant.CRItemResponseSetType);
			crItemResponseTO.setItemCode(PrismWebServiceConstant.itemResponseItemCodeMap.get(PrismWebServiceConstant.CRItemResponseSetType));
			StringBuffer crScoreVal =  new StringBuffer();
			boolean crItmPresent = false;
			while(rsCR.next()){
				crItmPresent = true;
				//crScoreVal.append(formatResponse(rsCR.getString("crScore"), 2));
				crScoreVal.append(rsCR.getString("crScore"));
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
			pstGR.setLong(5, rosterID);
			pstGR.setLong(6, itemSetId);
			pstGR.setLong(7, rosterID);
			pstGR.setLong(8, rosterID);
			pstGR.setString(9, PrismWebServiceConstant.GRIDItemResponseSetType);
			pstGR.setLong(10, itemSetId);
			pstGR.setLong(11, rosterID);
			pstGR.setString(12, PrismWebServiceConstant.GRIDItemResponseSetType);
			pstGR.setLong(13, rosterID);
			rsGR = pstGR.executeQuery();
			ItemResponseTO grItemResponseTO = new ItemResponseTO();
			grItemResponseTO.setItemSetType(PrismWebServiceConstant.GRItemResponseSetType);
			grItemResponseTO.setItemCode(PrismWebServiceConstant.itemResponseItemCodeMap.get(PrismWebServiceConstant.GREditedResponseTxt));
			StringBuffer grScoreVal =  new StringBuffer();
			boolean grItmPresent = false;
			Map<String, String> respStatusMap = new LinkedHashMap<String, String>();
			StringBuffer wrRespStatusItmId = new StringBuffer();
			while(rsGR.next()){
				grItmPresent = true;
				grScoreVal.append(formatResponse(rsGR.getString("response"), 16));
				String respStatus =  rsGR.getString("respstatus");
				if(invStatus){
					respStatusMap.put(rsGR.getString("itemid"), "I");
				}else{
					if(respStatus != null && !"".equals(respStatus) && "O".equals(respStatus)){
						respStatusMap.put(rsGR.getString("itemid"), "O");
					}else{
						respStatusMap.put(rsGR.getString("itemid"), " ");
						if(wrRespStatusItmId != null && wrRespStatusItmId.length() != 0){
							wrRespStatusItmId.append(" , ");
						}
						wrRespStatusItmId.append("'" + rsGR.getString("itemid") + "'");
					}
				}
			}
			
			grItemResponseTO.setScoreValue(grScoreVal.toString());
			if(grItmPresent){
				if(!invStatus && wrRespStatusItmId != null && wrRespStatusItmId.length() > 0){
					respStatusMap = getGRResponseStatus(wrRespStatusItmId, studentId, sessionId, respStatusMap);
				}
				ItemResponseTO grResponseStatusTO = new ItemResponseTO();
				grResponseStatusTO.setItemSetType(PrismWebServiceConstant.GRItemResponseSetType);
				grResponseStatusTO.setItemCode(PrismWebServiceConstant.itemResponseItemCodeMap.get(PrismWebServiceConstant.GRStatusTxt));
				grResponseStatusTO.setScoreValue(StringUtils.join(respStatusMap.values().toArray(new String[0]), ""));
				itemResponseTOLst.add(grResponseStatusTO);
				
				itemResponseTOLst.add(grItemResponseTO);
			}
			
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getItemResponsesDetail() method");
			e.printStackTrace();
		} finally {
			close(pstCR, rsCR);
			close(pstGR, rsGR);
			close(con, pstSR, rsSR);
		}
		return itemResponsesDetailsTO;
	}
	
	/**
	 * Get the GR Response Status
	 * @param wrRespStatusItmId
	 * @param studentId
	 * @param sessionId
	 * @param respStatusMap 
	 * @return
	 */
	private static Map<String, String> getGRResponseStatus(StringBuffer wrRespStatusItmId,
			Integer studentId, long sessionId, Map<String, String> respStatusMap) {
		
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = openIRSDBcon(false);
			pst = con.prepareStatement(GET_GR_RESP_STATUS + " ( " + wrRespStatusItmId.toString() + " ) ");
			pst.setLong(1, studentId);
			pst.setLong(2, sessionId);
			rs = pst.executeQuery();
			while(rs.next()){
				respStatusMap.put(rs.getString("itmid"), rs.getString("respstatus"));
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getGRResponseStatus() method to execute query : \n " +  GET_GR_RESP_STATUS);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return respStatusMap;
	
	}

	/**
	 * Format the response
	 * @param res
	 * @param count 
	 * @return
	 */
	private static String formatResponse(String res, int count) {
		StringBuffer sb = new StringBuffer();
		String resTemp =  res != null ? res : "";
		sb.append(resTemp);
		for(int i = 0 ; i < count - resTemp.length() ; i++){
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * Get the Content Score Details
	 * @param studentId
	 * @param reItemSetId 
	 * @return
	 * @throws CTBBusinessException
	 */
	public static Object[] getContentScoreDetails(Integer studentId, long sessionId, long itemSetId, long reItemSetId) {
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		ContentScoreDetailsTO contentScoreDetailsTO = new ContentScoreDetailsTO();
		List<ContentScoreTO> contentScoreTOLst = contentScoreDetailsTO.getCollContentScoreTO(); 
		
		String scoringStatus = PrismWebServiceConstant.OmittedContentStatusCode;
		
		Object[] retObj = new Object[2];
		
		try {
			con = openIRSDBcon(false);
			pst = con.prepareStatement(GET_CONTENT_SCORE_DETAILS);
			pst.setLong(1, studentId);
			pst.setLong(2, sessionId);
			pst.setLong(3, reItemSetId);
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
				
				scoringStatus = rs.getString("scoring_status");
			}
			
			retObj[0] = contentScoreDetailsTO;
			retObj[1] = scoringStatus;
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getContentScoreDetails() method to execute query : \n " +  GET_CONTENT_SCORE_DETAILS);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return retObj;
	}
	
	
	/**
	 * Get content details
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
		
		Map<Integer,List<SubtestAccommodationTO>> custConfAccommodationsMap = null;
		
		try {
			Map<String, Long> contentAreaID = getContentAreaIDMap(rosterId);
			
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_CONTENT_DETAILS);
			pst.setLong(1, rosterId);
			rs = pst.executeQuery();
			
			boolean sendELA = true;
			boolean sendOverAll = true;			
			
			Map<Integer, ContentDetailsTO> contentDetailsTOMap = getContentDetailsTOMap();
			String sessionTimeZone = getSessionTimeZone(sessionId);		
			while(rs.next()){
				String contentCodeName = rs.getString("content_code_name");
				long itemSetId = rs.getLong("item_set_id");
				String testStatus = rs.getString("completion_status");
				String valStatus = rs.getString("validation_status");
				Integer contentCode = PrismWebServiceConstant.contentDetailsContentCodeMap.get(contentCodeName);
				ContentDetailsTO contentDetailsTO = contentDetailsTOMap.get(contentCode);
				List<ObjectiveScoreDetailsTO> objectiveScoreDetailsList = new ArrayList<ObjectiveScoreDetailsTO>();
				boolean isCRScorePresent = false;
				if(contentCode != null && contentDetailsTO != null){
					contentDetailsTO.setDataChanged(true);
					contentDetailsTO.setContentCode(String.valueOf(contentCode));

					if(custConfAccommodationsMap == null){
						custConfAccommodationsMap = getCustConfAccommodations(studentId);
					}
					
					if(PrismWebServiceConstant.subTestAccomCatNameMap.get(contentCodeName) != null){
						SubtestAccommodationsTO subtestAccommodationsTO =  getSubTestAccommodation(studentId, contentCodeName);
						contentDetailsTO.setSubtestAccommodationsTO(subtestAccommodationsTO);
						if(custConfAccommodationsMap != null){
							subtestAccommodationsTO.getCollSubtestAccommodationTO().addAll(custConfAccommodationsMap.get(contentCode));
						}
					}
					
					
					contentDetailsTO.setDateTestTaken(getContentTestTakenDt(rosterId, itemSetId, sessionTimeZone));
					
					String statusCode = getContentStatusCode(rosterId, itemSetId);
					if(statusCode != null && !"".equals(statusCode)){
						contentDetailsTO.setStatusCode(PrismWebServiceConstant.contentDetailsStausCodeMap.get(statusCode) != null ? PrismWebServiceConstant.contentDetailsStausCodeMap.get(statusCode) : "");
					}
					
					Object[] contentScoreDetailsObjs = getContentScoreDetails(studentId, sessionId, itemSetId, contentAreaID.get(contentCodeName));
					
					String scoringStatus = (String) contentScoreDetailsObjs[1];
					
					// Changes for story : OAS-2544 // Checking the availability of EHS Score at OAS and match with the IRS 
					if (contentCode == PrismWebServiceConstant.wrContentCode
							&& !StringUtils.isEmptyString(testStatus)
							&& !StringUtils.isEmptyString(valStatus)) {
						isCRScorePresent = checkCRScoreAvailablility(rosterId, itemSetId, studentId, sessionId, scoringStatus);
					}
					
					if(PrismWebServiceConstant.InvalidContentStatusCode.equalsIgnoreCase(statusCode)){//For the invalid test special handling
						//If invaldiated when CR scoring is in progress don't sent item responses
						if(contentCode == PrismWebServiceConstant.wrContentCode){
							if(!StringUtils.isEmptyString(testStatus) && testStatus.equalsIgnoreCase("CO") && isCRScorePresent){
								ItemResponsesDetailsTO itemResponsesDetailsTO = getItemResponsesDetail(rosterId, itemSetId,studentId, sessionId, true);
								contentDetailsTO.setItemResponsesDetailsTO(itemResponsesDetailsTO);
							}
						}else{
							ItemResponsesDetailsTO itemResponsesDetailsTO = getItemResponsesDetail(rosterId, itemSetId,studentId, sessionId, true);
							contentDetailsTO.setItemResponsesDetailsTO(itemResponsesDetailsTO);
						}
						
						if(contentCode == PrismWebServiceConstant.readingContentCode || contentCode == PrismWebServiceConstant.wrContentCode){
							sendELA = false;
						}
						sendOverAll = false;
						objectiveScoreDetailsList = getObjectivesForOmSupInvStatus(rosterId, itemSetId, sessionId, statusCode, contentCode, studentId);
						contentDetailsTO.getCollObjectiveScoreDetailsTO().addAll(objectiveScoreDetailsList);
						continue;
					}
					
					//Check the CR scoring availability for Writing Sub test and send SIP flag
					if(contentCode == PrismWebServiceConstant.wrContentCode && !StringUtils.isEmptyString(testStatus) && !StringUtils.isEmptyString(valStatus)
							&& valStatus.equalsIgnoreCase("VA") && testStatus.equalsIgnoreCase("CO")){
						if(!isCRScorePresent){
							contentDetailsTO.setStatusCode(PrismWebServiceConstant.contentDetailsStausCodeMap.get(PrismWebServiceConstant.ScoringInProgressCode));
							sendELA = false;
							sendOverAll = false;
							objectiveScoreDetailsList = getObjectivesForOmSupInvStatus(rosterId, itemSetId, sessionId, PrismWebServiceConstant.ScoringInProgressCode, contentCode, studentId);
							contentDetailsTO.getCollObjectiveScoreDetailsTO().addAll(objectiveScoreDetailsList);
							continue;
						}
					}
					
					if(scoringStatus != null && !"".equals(scoringStatus) && !PrismWebServiceConstant.VAScoringStatus.equalsIgnoreCase(scoringStatus)){
						contentDetailsTO.setStatusCode(PrismWebServiceConstant.contentDetailsStausCodeMap.get(scoringStatus) != null ? PrismWebServiceConstant.contentDetailsStausCodeMap.get(scoringStatus) : "");
						if(PrismWebServiceConstant.OmittedContentStatusCode.equalsIgnoreCase(scoringStatus)){//Special Handling for Omitted Content 
							if(contentCode == PrismWebServiceConstant.readingContentCode || contentCode == PrismWebServiceConstant.wrContentCode){
								sendELA = false;
							}
							sendOverAll = false;
							contentDetailsTO.setDateTestTaken(null);
							objectiveScoreDetailsList = getObjectivesForOmSupInvStatus(rosterId, itemSetId, sessionId, scoringStatus, contentCode, studentId);
							contentDetailsTO.getCollObjectiveScoreDetailsTO().addAll(objectiveScoreDetailsList);
							continue;
						}else if(PrismWebServiceConstant.SuppressedContentStatusCode.equalsIgnoreCase(scoringStatus)){//Special Handling for Suppressed Content
							ItemResponsesDetailsTO itemResponsesDetailsTO = getItemResponsesDetail(rosterId, itemSetId,studentId, sessionId, false);
							contentDetailsTO.setItemResponsesDetailsTO(itemResponsesDetailsTO);
							if(contentCode == PrismWebServiceConstant.readingContentCode || contentCode == PrismWebServiceConstant.wrContentCode){
								sendELA = false;
							}
							sendOverAll = false;
							objectiveScoreDetailsList = getObjectivesForOmSupInvStatus(rosterId, itemSetId, sessionId, scoringStatus, contentCode, studentId);
							contentDetailsTO.getCollObjectiveScoreDetailsTO().addAll(objectiveScoreDetailsList);
							continue;
						}
					}else{
						contentDetailsTO.setStatusCode("");
					}
					
					ContentScoreDetailsTO contentScoreDetailsTO = (ContentScoreDetailsTO) contentScoreDetailsObjs[0];
					contentDetailsTO.setContentScoreDetailsTO(contentScoreDetailsTO);
					
					ItemResponsesDetailsTO itemResponsesDetailsTO = getItemResponsesDetail(rosterId, itemSetId,studentId, sessionId, false);
					contentDetailsTO.setItemResponsesDetailsTO(itemResponsesDetailsTO);
					
					objectiveScoreDetailsList = getObjectiveScoreDetails(itemSetId, rosterId, sessionId, studentId, contentCode);
					contentDetailsTO.getCollObjectiveScoreDetailsTO().addAll(objectiveScoreDetailsList);
				}
			}
			
			//Set the composite content score details
			setCompositeContentScoreDetails(studentId, sessionId, contentDetailsTOMap, sendELA, sendOverAll);
			
			contentDetailsTOList.addAll(contentDetailsTOMap.values());
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getContentDetailsTO() method to execute query : \n " +  GET_CONTENT_DETAILS);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return contentDetailsTOList;
	}
	
	/**
	 * Return the time zone of a session
	 * @param sessionId
	 * @return
	 */
    public static String getSessionTimeZone(long sessionId){
	   PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		String sessionTimeZone = "GMT";//default value
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_SESSION_TIMEZONE);
			pst.setLong(1, sessionId);
			rs = pst.executeQuery();
			while(rs.next()){
				sessionTimeZone = rs.getString("session_time_zone");
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getSessionTimeZone() method to execute query : \n " +  GET_SESSION_TIMEZONE);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return sessionTimeZone;
    }
	
	/**
	 * Get the customer config accommodations
	 * @param studentId
	 * @return
	 */
	private static Map<Integer,List<SubtestAccommodationTO>> getCustConfAccommodations(
			Integer studentId) {
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		Map<Integer,List<SubtestAccommodationTO>> custConfAccommodationsMap = new HashMap<Integer, List<SubtestAccommodationTO>>();
		List<SubtestAccommodationTO> custConfAccommodationsMath = new ArrayList<SubtestAccommodationTO>();
		List<SubtestAccommodationTO> custConfAccommodationsRead = new ArrayList<SubtestAccommodationTO>();
		List<SubtestAccommodationTO> custConfAccommodationsSci = new ArrayList<SubtestAccommodationTO>();
		List<SubtestAccommodationTO> custConfAccommodationsSoc = new ArrayList<SubtestAccommodationTO>();
		List<SubtestAccommodationTO> custConfAccommodationsWrit = new ArrayList<SubtestAccommodationTO>();
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_CUST_CONF_ACCOMMODATION);
			pst.setLong(1, studentId);
			rs = pst.executeQuery();
			if(rs.next()){
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				for (int i = 1; i < columnCount + 1; i++ ) {
				  String name = rsmd.getColumnName(i);
				  try{
					  String oasAttrName = PrismWebServiceConstant.resourceBundler.getString(name.toUpperCase());
					  if(oasAttrName != null && !"".equals(oasAttrName)){
						  if(rs.getString(name) != null && ("T".equalsIgnoreCase(rs.getString(name)) || "1.5".equalsIgnoreCase(rs.getString(name)))){
							  custConfAccommodationsMath.add(buildSTAccommodation(oasAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Mathematics"), "Y"));
							  custConfAccommodationsRead.add(buildSTAccommodation(oasAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Reading"), "Y"));
							  custConfAccommodationsSci.add(buildSTAccommodation(oasAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Science"), "Y"));
							  custConfAccommodationsSoc.add(buildSTAccommodation(oasAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Social Studies"), "Y"));
							  custConfAccommodationsWrit.add(buildSTAccommodation(oasAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Writing"), "Y"));
						  }else{
							  custConfAccommodationsMath.add(buildSTAccommodation(oasAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Mathematics"), null));
							  custConfAccommodationsRead.add(buildSTAccommodation(oasAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Reading"), null));
							  custConfAccommodationsSci.add(buildSTAccommodation(oasAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Science"), null));
							  custConfAccommodationsSoc.add(buildSTAccommodation(oasAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Social Studies"), null));
							  custConfAccommodationsWrit.add(buildSTAccommodation(oasAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Writing"), null));
						  }
					  }
				  }catch(Exception e){
					  //Do nothing,just to avoid exception as some of the columns are not used and hence they are not defined in property file 
				  }
				}
				
				if(rs.getString("question_background_color") != null && !"".equals(rs.getString("question_background_color"))){
					 custConfAccommodationsMath.add(buildSTAccommodation(PrismWebServiceConstant.fontBackGrClrAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Mathematics"),"Y"));
					 custConfAccommodationsRead.add(buildSTAccommodation(PrismWebServiceConstant.fontBackGrClrAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Reading"), "Y"));
					 custConfAccommodationsSci.add(buildSTAccommodation(PrismWebServiceConstant.fontBackGrClrAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Science"), "Y"));
					 custConfAccommodationsSoc.add(buildSTAccommodation(PrismWebServiceConstant.fontBackGrClrAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Social Studies"), "Y"));
					 custConfAccommodationsWrit.add(buildSTAccommodation(PrismWebServiceConstant.fontBackGrClrAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Writing"), "Y"));
				}else{
					 custConfAccommodationsMath.add(buildSTAccommodation(PrismWebServiceConstant.fontBackGrClrAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Mathematics"), null));
					 custConfAccommodationsRead.add(buildSTAccommodation(PrismWebServiceConstant.fontBackGrClrAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Reading"), null));
					 custConfAccommodationsSci.add(buildSTAccommodation(PrismWebServiceConstant.fontBackGrClrAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Science"), null));
					 custConfAccommodationsSoc.add(buildSTAccommodation(PrismWebServiceConstant.fontBackGrClrAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Social Studies"), null));
					 custConfAccommodationsWrit.add(buildSTAccommodation(PrismWebServiceConstant.fontBackGrClrAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Writing"), null));
				}
				
				if(rs.getString("music_file_id") != null && !"".equals(rs.getString("music_file_id"))){
					 custConfAccommodationsMath.add(buildSTAccommodation(PrismWebServiceConstant.musicPlayerAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Mathematics"),"Y"));
					 custConfAccommodationsRead.add(buildSTAccommodation(PrismWebServiceConstant.musicPlayerAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Reading"), "Y"));
					 custConfAccommodationsSci.add(buildSTAccommodation(PrismWebServiceConstant.musicPlayerAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Science"), "Y"));
					 custConfAccommodationsSoc.add(buildSTAccommodation(PrismWebServiceConstant.musicPlayerAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Social Studies"), "Y"));
					 custConfAccommodationsWrit.add(buildSTAccommodation(PrismWebServiceConstant.musicPlayerAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Writing"), "Y"));
				}else{
					 custConfAccommodationsMath.add(buildSTAccommodation(PrismWebServiceConstant.musicPlayerAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Mathematics"), null));
					 custConfAccommodationsRead.add(buildSTAccommodation(PrismWebServiceConstant.musicPlayerAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Reading"), null));
					 custConfAccommodationsSci.add(buildSTAccommodation(PrismWebServiceConstant.musicPlayerAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Science"), null));
					 custConfAccommodationsSoc.add(buildSTAccommodation(PrismWebServiceConstant.musicPlayerAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Social Studies"), null));
					 custConfAccommodationsWrit.add(buildSTAccommodation(PrismWebServiceConstant.musicPlayerAttrName, PrismWebServiceConstant.contentDetailsContentCodeMap.get("Writing"), null));
				}
				
			}
			
			custConfAccommodationsMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Mathematics"), custConfAccommodationsMath);
			custConfAccommodationsMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Reading"), custConfAccommodationsRead);
			custConfAccommodationsMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Science"), custConfAccommodationsSci);
			custConfAccommodationsMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Social Studies"), custConfAccommodationsSoc);
			custConfAccommodationsMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Writing"), custConfAccommodationsWrit);
			
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getCustConfAccommodations() method to execute query : \n " +  GET_CUST_CONF_ACCOMMODATION);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return custConfAccommodationsMap;
	}
	
	/**
	 * builds SubtestAccommodationTO
	 * @param oasAttrName
	 * @param contentCode
	 * @param value
	 * @return
	 */
	private static SubtestAccommodationTO buildSTAccommodation(String oasAttrName, Integer contentCode, String value){
		SubtestAccommodationTO accommodationTO = new SubtestAccommodationTO();
		accommodationTO.setName(oasAttrName+"_"+String.valueOf(contentCode.intValue()));
		accommodationTO.setValue(value);
		return accommodationTO;
	}

	/**
	 * Get the Content Details TO map
	 * @return
	 */
	private static Map<Integer, ContentDetailsTO> getContentDetailsTOMap() {
		Map<Integer, ContentDetailsTO> contentDetailsTOMap =  new HashMap<Integer, ContentDetailsTO>();
		
		String omitedStatusCode = PrismWebServiceConstant.contentDetailsStausCodeMap.get(PrismWebServiceConstant.OmittedContentStatusCode);
		
		ContentDetailsTO readingContentDetailsTO = new ContentDetailsTO();
		readingContentDetailsTO.setStatusCode(omitedStatusCode);
		readingContentDetailsTO.setContentCode(String.valueOf(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Reading")));
		readingContentDetailsTO.setDataChanged(true);
		contentDetailsTOMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Reading") , readingContentDetailsTO);
		
		ContentDetailsTO writingContentDetailsTO = new ContentDetailsTO();
		writingContentDetailsTO.setStatusCode(omitedStatusCode);
		writingContentDetailsTO.setContentCode(String.valueOf(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Writing")));
		writingContentDetailsTO.setDataChanged(true);
		contentDetailsTOMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Writing") , writingContentDetailsTO);
		
		ContentDetailsTO elaContentDetailsTO = new ContentDetailsTO();
		elaContentDetailsTO.setStatusCode(omitedStatusCode);
		elaContentDetailsTO.setContentCode(String.valueOf(PrismWebServiceConstant.contentDetailsContentCodeMap.get("ELA")));
		elaContentDetailsTO.setDataChanged(true);
		contentDetailsTOMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("ELA") ,elaContentDetailsTO);
		
		ContentDetailsTO mathContentDetailsTO = new ContentDetailsTO();
		mathContentDetailsTO.setStatusCode(omitedStatusCode);
		mathContentDetailsTO.setContentCode(String.valueOf(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Mathematics")));
		mathContentDetailsTO.setDataChanged(true);
		contentDetailsTOMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Mathematics") , mathContentDetailsTO);
		
		ContentDetailsTO scienceContentDetailsTO = new ContentDetailsTO();
		scienceContentDetailsTO.setStatusCode(omitedStatusCode);
		scienceContentDetailsTO.setContentCode(String.valueOf(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Science")));
		scienceContentDetailsTO.setDataChanged(true);
		contentDetailsTOMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Science") , scienceContentDetailsTO);
		
		ContentDetailsTO soclStudContentDetailsTO = new ContentDetailsTO();
		soclStudContentDetailsTO.setStatusCode(omitedStatusCode);
		soclStudContentDetailsTO.setContentCode(String.valueOf(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Social Studies")));
		soclStudContentDetailsTO.setDataChanged(true);
		contentDetailsTOMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Social Studies") , soclStudContentDetailsTO);
		
		ContentDetailsTO overallContentDetailsTO = new ContentDetailsTO();
		overallContentDetailsTO.setStatusCode(omitedStatusCode);
		overallContentDetailsTO.setContentCode(String.valueOf(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Overall")));
		overallContentDetailsTO.setDataChanged(true);
		contentDetailsTOMap.put(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Overall") , overallContentDetailsTO);
		
		return contentDetailsTOMap;
	}

	/**
	 * Get the Content Area ID Map
	 * @param rosterId
	 * @return
	 */
	private static Map<String, Long> getContentAreaIDMap(long rosterId) {
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		Map<String, Long> contentAreaID = new HashMap<String, Long>();
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_CONTENT_AREA_ID);
			pst.setLong(1, rosterId);
			rs = pst.executeQuery();
			while(rs.next()){
				contentAreaID.put(rs.getString("contentareaname"), rs.getLong("contentareaid"));
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getContentAreaIDMap() method to execute query : \n " +  GET_CONTENT_AREA_ID);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return contentAreaID;
	}

	/**
	 * Check The CR scoring availability
	 * @param rosterId
	 * @return
	 */
	private static boolean checkCRScoreAvailablility(long rosterId, long itemSetId, Integer studentId, long sessionId, String scoringStatus) {
		
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		boolean checkCRScorePresent = false;
		boolean isFirstValue = true;
		Map<String,String> itemScoreMap = new HashMap<String, String>();
		StringBuilder inClause = new StringBuilder();
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(CHECK_CR_SCORE_PRESENT);
			pst.setLong(1, rosterId);
			pst.setLong(2, itemSetId);
			rs = pst.executeQuery();
			//-- Changes for story : OAS-2544 --//
			while (rs.next()) {
				checkCRScorePresent = true;

				// Store CR item and OAS end score map
				itemScoreMap.put(rs.getString(1), rs.getString(2));

				// Making in clause value for IRS
				if (isFirstValue) {
					isFirstValue = false;
				} else {
					inClause.append(",");
				}
				inClause.append(rs.getString(1));
			}
			
			/**
			 * Checking IRS score for each Item, if score is present in OAS
			 * and score status of content area not in OM and SUP. 
			 */
			if (checkCRScorePresent) {
				if (PrismWebServiceConstant.OmittedContentStatusCode.equalsIgnoreCase(scoringStatus)
						|| PrismWebServiceConstant.SuppressedContentStatusCode.equalsIgnoreCase(scoringStatus)) {
					return checkCRScorePresent;
				} else {
					close(con, pst, rs);
					return validateScoreWithIrs(sessionId, studentId, itemScoreMap, inClause);
				}
			}
			//-- Changes for story : OAS-2544 --//
			
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.checkCRScoreAvailablility() method to execute query : \n " +  CHECK_CR_SCORE_PRESENT);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return false;
	}

	/**
	 * Validate the OAS score is present in IRS or not
	 * @param sessionId
	 * @param studentId
	 * @param itemScoreMap
	 * @param inClause
	 * @return boolean
	 */
	private static boolean validateScoreWithIrs(long sessionId, Integer studentId,
			Map<String, String> itemScoreMap,
			StringBuilder inClause) {

		PreparedStatement pst = null;
		Connection irscon = null;
		ResultSet rs = null;
		try {
			irscon = openIRSDBcon(false);
			pst = irscon.prepareStatement(CHECK_CR_SCORE_IN_IRS);
			pst.setLong(1, sessionId);
			pst.setLong(2, studentId);
			pst.setString(3, inClause.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				String scoreValue = itemScoreMap.get(rs.getString(1));
				String regex = "[0-9]+";
				if (scoreValue.matches(regex)) { // Checking the numeric value
					return (Integer.valueOf(scoreValue).intValue() == Integer
							.valueOf(rs.getString(2)).intValue()) ? true
							: false;
				} else {
					return (scoreValue.equals(rs.getString(3))) ? true : false;
				}
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.validateScoreWithIrs() method to execute query : \n " + CHECK_CR_SCORE_IN_IRS);
			e.printStackTrace();
		} finally {
			close(irscon, pst, rs);
		}
		return false;
	}

	/**
	 * Get Content Test Taken Date
	 * @param rosterId
	 * @param itemSetId
	 * @return
	 */
	public static String getContentTestTakenDt(long rosterId, long itemSetId, String sessionTimeZone){
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		String testTakenDt = "";
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_TEST_TAKEN_DATE);
			pst.setLong(1, rosterId);
			pst.setLong(2, itemSetId);
			rs = pst.executeQuery();
			while(rs.next()){
				if(rs.getDate("test_taken_dt")!=null){
					java.util.Date testTakenDate = rs.getTimestamp("test_taken_dt");
					if(sessionTimeZone != null && !"".equals(sessionTimeZone)){
						testTakenDt = formatDateToDateString(com.ctb.util.DateUtils.getAdjustedDate(testTakenDate,"GMT",sessionTimeZone,testTakenDate));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getContentTestTakenDt() method to execute query : \n " +  GET_TEST_TAKEN_DATE);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return testTakenDt;
	}
	
	public static String formatDateToDateString(Date date){
		String DATE_FORMAT = "MMddyy";
        String result = null;
        if (date == null)
            return result;

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(DATE_FORMAT);
        try{
            result = sdf.format(date);
        }
        catch (Exception e){
        	System.err.println("Error in parsing the test taken date");
            e.printStackTrace();
        }
        return result;
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
			subTestStatusPst.setLong(3, rosterId);
			subTestStatusRS = subTestStatusPst.executeQuery();

			while (subTestStatusRS.next()) {
				if (subTestStatusRS.getString("status") != null && PrismWebServiceConstant.InvalidContentStatus.equalsIgnoreCase(subTestStatusRS.getString("status"))) {
					return PrismWebServiceConstant.InvalidContentStatusCode;
				}
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getContentStatusCode() method to execute query : \n " +  GET_SUBTEST_STATUS);
			e.printStackTrace();
		}finally{
			close(con, subTestStatusPst,  subTestStatusRS);
		}
		return "";
	}
	
	

	/**
	 * Set the composite content score details
	 * @param studentId
	 * @param sessionId
	 * @param contentDetailsTOMap 
	 * @param sendELA 
	 * @param sendOverAll 
	 * @return
	 * @throws CTBBusinessException
	 * @throws SQLException
	 */
	private static List<ContentDetailsTO> setCompositeContentScoreDetails(
			Integer studentId, long sessionId, Map<Integer, ContentDetailsTO> contentDetailsTOMap,boolean sendELA, boolean sendOverAll) {
		PreparedStatement compTestPst = null;
		ResultSet compTestRS = null;
		Connection irsCon = null;
		List<ContentDetailsTO> contentDetailsTOList = new ArrayList<ContentDetailsTO>();
		
		ContentDetailsTO elaContentDetailsTO =  contentDetailsTOMap.get(PrismWebServiceConstant.contentDetailsContentCodeMap.get("ELA"));
		elaContentDetailsTO.setStatusCode(PrismWebServiceConstant.contentDetailsStausCodeMap.get(PrismWebServiceConstant.NACompositeStatusCode));
		elaContentDetailsTO.setContentCode(String.valueOf(PrismWebServiceConstant.contentDetailsContentCodeMap.get("ELA")));
		contentDetailsTOList.add(elaContentDetailsTO);
		
		ContentDetailsTO overAllContentDetailsTO =  contentDetailsTOMap.get(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Overall"));
		overAllContentDetailsTO.setStatusCode(PrismWebServiceConstant.contentDetailsStausCodeMap.get(PrismWebServiceConstant.NACompositeStatusCode));
		overAllContentDetailsTO.setContentCode(String.valueOf(PrismWebServiceConstant.contentDetailsContentCodeMap.get("Overall")));
		contentDetailsTOList.add(overAllContentDetailsTO);
		
		try {
			
			if(sendELA || sendOverAll){				
			irsCon = openIRSDBcon(false);
			compTestPst = irsCon
					.prepareStatement(GET_COMPOSITE_CONTENT_DETAILS);
			compTestPst.setLong(1, studentId);
			compTestPst.setLong(2, sessionId);
			compTestRS = compTestPst.executeQuery();
			while (compTestRS.next()) {
					ContentDetailsTO contentDetailsTO = null;
					String contentCodeName = compTestRS.getString("compName");
					Integer contentCode = 0;
					if(contentCodeName != null && contentCodeName.indexOf("ELA") >= 0){
						contentDetailsTO = elaContentDetailsTO;
						if(!sendELA){
							contentDetailsTO.setContentScoreDetailsTO(buildEmptyContentScoreDetailsTO());
							continue;						
						}
						contentCode = PrismWebServiceConstant.contentDetailsContentCodeMap.get("ELA");
					}else if(contentCodeName != null && contentCodeName.indexOf("Overall") >= 0){
						contentDetailsTO = overAllContentDetailsTO;
						if(!sendOverAll){
							contentDetailsTO.setContentScoreDetailsTO(buildEmptyContentScoreDetailsTO());
							continue;						
						}
						contentCode = PrismWebServiceConstant.contentDetailsContentCodeMap.get("Overall");
					}
					if(contentDetailsTO != null){
							ContentScoreDetailsTO contentScoreDetailsTO = new ContentScoreDetailsTO();
							contentDetailsTO.setContentCode(String.valueOf(contentCode));
							contentDetailsTO.setStatusCode("");
							contentDetailsTO.setDataChanged(true);
			
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
					}
				}	
			}else{
				ContentDetailsTO contentDetailsTO = null;
				contentDetailsTO = elaContentDetailsTO;
				contentDetailsTO.setContentScoreDetailsTO(buildEmptyContentScoreDetailsTO());
				
				contentDetailsTO = null;
				contentDetailsTO = overAllContentDetailsTO;
				contentDetailsTO.setContentScoreDetailsTO(buildEmptyContentScoreDetailsTO());
				
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getCompositeContentScoreDetails() method to execute query : \n " +  GET_COMPOSITE_CONTENT_DETAILS);
			e.printStackTrace();
		} finally {
			close(irsCon, compTestPst, compTestRS);
		}
		return contentDetailsTOList;
	}
	
	
	/**
	 * Create the empty Content Score details Object
	 * @return
	 */
	private static ContentScoreDetailsTO buildEmptyContentScoreDetailsTO(){
		
		ContentScoreDetailsTO contentScoreDetailsTO = new ContentScoreDetailsTO();
		
		ContentScoreTO ncContentScoreTO = new ContentScoreTO();
		ncContentScoreTO.setScoreType(PrismWebServiceConstant.NCContentScoreDetails);
		contentScoreDetailsTO.getCollContentScoreTO().add(ncContentScoreTO);

		ContentScoreTO npContentScoreTO = new ContentScoreTO();
		npContentScoreTO.setScoreType(PrismWebServiceConstant.NPContentScoreDetails);
		contentScoreDetailsTO.getCollContentScoreTO().add(npContentScoreTO);

		ContentScoreTO ssContentScoreTO = new ContentScoreTO();
		ssContentScoreTO.setScoreType(PrismWebServiceConstant.SSContentScoreDetails);
		contentScoreDetailsTO.getCollContentScoreTO().add(ssContentScoreTO);

		ContentScoreTO hseContentScoreTO = new ContentScoreTO();
		hseContentScoreTO.setScoreType(PrismWebServiceConstant.HSEContentScoreDetails);
		contentScoreDetailsTO.getCollContentScoreTO().add(hseContentScoreTO);

		ContentScoreTO prContentScoreTO = new ContentScoreTO();
		prContentScoreTO.setScoreType(PrismWebServiceConstant.PRContentScoreDetails);
		contentScoreDetailsTO.getCollContentScoreTO().add(prContentScoreTO);

		ContentScoreTO nceContentScoreTO = new ContentScoreTO();
		nceContentScoreTO.setScoreType(PrismWebServiceConstant.NCEContentScoreDetails);
		contentScoreDetailsTO.getCollContentScoreTO().add(nceContentScoreTO);

		ContentScoreTO ssrContentScoreTO = new ContentScoreTO();
		ssrContentScoreTO.setScoreType(PrismWebServiceConstant.SSRContentScoreDetails);
		contentScoreDetailsTO.getCollContentScoreTO().add(ssrContentScoreTO);
		
		return contentScoreDetailsTO;
	}
	
	
	/**
	 * Get the Objective Score Details
	 * @param itemSetId
	 * @param rosterId
	 * @param sessionId
	 * @param studentId 
	 * @param contentCode 
	 * @return
	 */
	private static List<ObjectiveScoreDetailsTO> getObjectiveScoreDetails(
			long itemSetId, long rosterId, long sessionId, Integer studentId, Integer contentCode ) {
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
			pst.setLong(3, rosterId);
			pst.setString(4, String.valueOf(contentCode));
			pst.setLong(5, itemSetId);
			pst.setLong(6, rosterId);
			pst.setLong(7, sessionId);
			pst.setString(8, String.valueOf(contentCode));
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
							
							ObjectiveScoreTO INRCobjectiveScoreTO = new ObjectiveScoreTO();
							INRCobjectiveScoreTO.setScoreType(PrismWebServiceConstant.OSCObjectiveScoreDetails);
							INRCobjectiveScoreTO.setValue(irsRs.getString("itmattempflag"));
							objectiveScoreDetails.getCollObjectiveScoreTO().add(INRCobjectiveScoreTO);
							
							objectiveScoreDetails.setObjectiveName(rs.getString("objname"));
							objectiveScoreDetails.setObjectiveCode(rs.getString("objcode"));
							
						}
						
						objectiveScoreDetailsLst.add(objectiveScoreDetails);
						
					}catch(Exception e){
						System.err.println("Error in the PrismWebServiceDBUtility.getObjectiveScoreDetails() method to execute query : \n " +  GET_PRIM_OBJ_SCORE);
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
							String objName = rs.getString("objname");
							//Special Handling for Writing 2nd Objective
							if(PrismWebServiceConstant.wrContentCode == contentCode && objName.toLowerCase().contains(PrismWebServiceConstant.wr2ndObjName.toLowerCase())){
								specialHandingForWR2ndObj(objectiveScoreDetails, irsRs);
							}else{
								String itmAttempFlag = irsRs.getString("itmattempflag");
								boolean noneItmAttmtd = false;
								ObjectiveScoreTO INRCobjectiveScoreTO = new ObjectiveScoreTO();
								INRCobjectiveScoreTO.setScoreType(PrismWebServiceConstant.OSCObjectiveScoreDetails);
								if(PrismWebServiceConstant.NoneItmAtmtdVal.equalsIgnoreCase(itmAttempFlag)){
									INRCobjectiveScoreTO.setValue(PrismWebServiceConstant.NoneItmAttmtdScoreVal);
									noneItmAttmtd = true;
								}else if(PrismWebServiceConstant.SomeItmAtmtdVal.equalsIgnoreCase(itmAttempFlag)){
									INRCobjectiveScoreTO.setValue(PrismWebServiceConstant.SomeItmAttmtdScoreVal);
								}else{
									INRCobjectiveScoreTO.setValue(PrismWebServiceConstant.AllItmAttmtdScoreVal);
								}
								objectiveScoreDetails.getCollObjectiveScoreTO().add(INRCobjectiveScoreTO);
								
								ObjectiveScoreTO NCobjectiveScoreTO = new ObjectiveScoreTO();
								NCobjectiveScoreTO.setScoreType(PrismWebServiceConstant.NCObjectiveScoreDetails);
								NCobjectiveScoreTO.setValue(!noneItmAttmtd ? irsRs.getString("numcorrect") : "");
								objectiveScoreDetails.getCollObjectiveScoreTO().add(NCobjectiveScoreTO);
								
								ObjectiveScoreTO NPobjectiveScoreTO = new ObjectiveScoreTO();
								NPobjectiveScoreTO.setScoreType(PrismWebServiceConstant.NPObjectiveScoreDetails);
								NPobjectiveScoreTO.setValue(!noneItmAttmtd ? irsRs.getString("numpossible") : "");
								objectiveScoreDetails.getCollObjectiveScoreTO().add(NPobjectiveScoreTO);
								
								ObjectiveScoreTO SSobjectiveScoreTO = new ObjectiveScoreTO();
								SSobjectiveScoreTO.setScoreType(PrismWebServiceConstant.SSObjectiveScoreDetails);
								SSobjectiveScoreTO.setValue(!noneItmAttmtd ? irsRs.getString("scalescore") : "");
								objectiveScoreDetails.getCollObjectiveScoreTO().add(SSobjectiveScoreTO);
								
								ObjectiveScoreTO MAobjectiveScoreTO = new ObjectiveScoreTO();
								MAobjectiveScoreTO.setScoreType(PrismWebServiceConstant.MAObjectiveScoreDetails);
								MAobjectiveScoreTO.setValue(!noneItmAttmtd ? (irsRs.getString("mastery") != null ? irsRs.getString("mastery") : "") : "");
								objectiveScoreDetails.getCollObjectiveScoreTO().add(MAobjectiveScoreTO);
								
								ObjectiveScoreTO MRobjectiveScoreTO = new ObjectiveScoreTO();
								MRobjectiveScoreTO.setScoreType(PrismWebServiceConstant.MRObjectiveScoreDetails);
								MRobjectiveScoreTO.setValue(!noneItmAttmtd ? irsRs.getString("objmasscalescorerng") : "");
								objectiveScoreDetails.getCollObjectiveScoreTO().add(MRobjectiveScoreTO);
								
							}
							
							objectiveScoreDetails.setObjectiveName(rs.getString("objname"));
							objectiveScoreDetails.setObjectiveCode(rs.getString("objcode"));
							
						}
						
						objectiveScoreDetailsLst.add(objectiveScoreDetails);
						
					}catch(Exception e){
						System.err.println("Error in the PrismWebServiceDBUtility.getObjectiveScoreDetails() method to execute query : \n " +  GET_SEC_OBJ_SCORE);
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

	private static List<ObjectiveScoreDetailsTO> getObjectivesForOmSupInvStatus(long rosterId, long itemSetId, long sessionId, String statusCode, Integer contentCode, Integer studentId) {
		List<ObjectiveScoreDetailsTO> objectiveScoreDetailsLst = new ArrayList<ObjectiveScoreDetailsTO>();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_OBJECTIVE_LIST);
			pst.setLong(1, itemSetId);
			pst.setLong(2, sessionId);
			pst.setLong(3, rosterId);
			pst.setString(4, String.valueOf(contentCode));
			pst.setLong(5, itemSetId);
			pst.setLong(6, rosterId);
			pst.setLong(7, sessionId);
			pst.setString(8, String.valueOf(contentCode));
			rs = pst.executeQuery();
			while(rs.next()){
				String objName = rs.getString("objname");
				ObjectiveScoreDetailsTO objectiveScoreDetails = new ObjectiveScoreDetailsTO();
				
				ObjectiveScoreTO NCobjectiveScoreTO = new ObjectiveScoreTO();
				NCobjectiveScoreTO.setScoreType(PrismWebServiceConstant.NCObjectiveScoreDetails);
							
				ObjectiveScoreTO NPobjectiveScoreTO = new ObjectiveScoreTO();
				NPobjectiveScoreTO.setScoreType(PrismWebServiceConstant.NPObjectiveScoreDetails);
							
				ObjectiveScoreTO SSobjectiveScoreTO = new ObjectiveScoreTO();
				SSobjectiveScoreTO.setScoreType(PrismWebServiceConstant.SSObjectiveScoreDetails);
							
				ObjectiveScoreTO MAobjectiveScoreTO = new ObjectiveScoreTO();
				MAobjectiveScoreTO.setScoreType(PrismWebServiceConstant.MAObjectiveScoreDetails);
							
				ObjectiveScoreTO MRobjectiveScoreTO = new ObjectiveScoreTO();
				MRobjectiveScoreTO.setScoreType(PrismWebServiceConstant.MRObjectiveScoreDetails);
				
				if(PrismWebServiceConstant.wrContentCode == contentCode 
						&& objName.toLowerCase().contains(PrismWebServiceConstant.wr2ndObjName.toLowerCase())){
					ObjectiveScoreTO CCobjectiveScoreTO = new ObjectiveScoreTO();
					CCobjectiveScoreTO.setScoreType(PrismWebServiceConstant.CCObjectiveScoreDetails);
					objectiveScoreDetails.getCollObjectiveScoreTO().add(CCobjectiveScoreTO);
					
					//if WR Obj2 scores available for SUP,INV status then NP and MR will be reported to PRISM
					if(statusCode.equalsIgnoreCase(PrismWebServiceConstant.SuppressedContentStatusCode)){
						Map<String,String> scoreMap = getSecScoreForWRObj2(rs.getLong("itemsetid"),sessionId,studentId);
						if(scoreMap != null && scoreMap.size() > 0){
							NPobjectiveScoreTO.setValue(scoreMap.get(PrismWebServiceConstant.NPObjectiveScoreDetails));
							MRobjectiveScoreTO.setValue(scoreMap.get(PrismWebServiceConstant.MRObjectiveScoreDetails));
						}
					}
				}
				
				ObjectiveScoreTO OSCobjectiveScoreTO = new ObjectiveScoreTO();
				OSCobjectiveScoreTO.setScoreType(PrismWebServiceConstant.OSCObjectiveScoreDetails);
				if(!(PrismWebServiceConstant.wrContentCode == contentCode 
						&& objName.toLowerCase().contains(PrismWebServiceConstant.wr2ndObjName.toLowerCase()))){
					if(statusCode.equalsIgnoreCase(PrismWebServiceConstant.OmittedContentStatusCode)){
						OSCobjectiveScoreTO.setValue("-");
					}
				}
				
				objectiveScoreDetails.getCollObjectiveScoreTO().add(NCobjectiveScoreTO);
				objectiveScoreDetails.getCollObjectiveScoreTO().add(NPobjectiveScoreTO);
				objectiveScoreDetails.getCollObjectiveScoreTO().add(SSobjectiveScoreTO);
				objectiveScoreDetails.getCollObjectiveScoreTO().add(MAobjectiveScoreTO);
				objectiveScoreDetails.getCollObjectiveScoreTO().add(MRobjectiveScoreTO);
				objectiveScoreDetails.getCollObjectiveScoreTO().add(OSCobjectiveScoreTO);
							
				objectiveScoreDetails.setObjectiveName(rs.getString("objname"));
				objectiveScoreDetails.setObjectiveCode(rs.getString("objcode"));
							
				objectiveScoreDetailsLst.add(objectiveScoreDetails);
			}
		}catch(Exception ex){
			System.err.println("Error in the PrismWebServiceDBUtility.getObjectivesForOmSupInvStatus() method to execute query : \n " +  GET_OBJECTIVE_LIST);
			ex.printStackTrace();
		}finally{
			close(con, pst, rs);
		}
		return objectiveScoreDetailsLst;
	}
	
	/**
	 * Get the NP, MR score for WR 2nd Objective
	 * @param itemSetId
	 * @param sessionId
	 * @param studentId
	 * @return Map<String, String>
	 */
	private static Map<String, String> getSecScoreForWRObj2(long itemSetId, long sessionId,Integer studentId){
		PreparedStatement irsPst = null;
		ResultSet irsRs = null;
		Connection irsCon = null;		
		Map<String, String> scoreMap = new HashMap<String, String>();
		try{
		irsCon = openIRSDBcon(false);
		irsPst = irsCon.prepareStatement(GET_SEC_OBJ_SCORE);
		irsPst.setLong(1, itemSetId);
		irsPst.setLong(2, studentId);
		irsPst.setLong(3, sessionId);
		irsRs = irsPst.executeQuery();
		while(irsRs.next()){
			scoreMap.put(PrismWebServiceConstant.NPObjectiveScoreDetails, irsRs.getString("numpossible"));
			scoreMap.put(PrismWebServiceConstant.MRObjectiveScoreDetails, irsRs.getString("objmasscalescorerng"));
		}
		}catch(Exception e){
			System.err.println("Error in the PrismWebServiceDBUtility.getSecScoreForWRObj2() method to execute query : \n " +  GET_SEC_OBJ_SCORE);
			e.printStackTrace();
		}finally{
			close(irsCon, irsPst, irsRs);
		}
		
		return scoreMap;
	}
	
	/**
	 * Special Handling for Writing 2nd Objective
	 * 
	 * @param objectiveScoreDetails
	 * @param irsRs
	 * @throws SQLException
	 */
	private static void specialHandingForWR2ndObj(
			ObjectiveScoreDetailsTO objectiveScoreDetails, ResultSet irsRs) throws SQLException {

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
		SSobjectiveScoreTO.setValue("");
		objectiveScoreDetails.getCollObjectiveScoreTO().add(SSobjectiveScoreTO);
		
		ObjectiveScoreTO MAobjectiveScoreTO = new ObjectiveScoreTO();
		MAobjectiveScoreTO.setScoreType(PrismWebServiceConstant.MAObjectiveScoreDetails);
		MAobjectiveScoreTO.setValue(irsRs.getString("mastery") != null ? irsRs.getString("mastery") : "");
		objectiveScoreDetails.getCollObjectiveScoreTO().add(MAobjectiveScoreTO);
		
		ObjectiveScoreTO MRobjectiveScoreTO = new ObjectiveScoreTO();
		MRobjectiveScoreTO.setScoreType(PrismWebServiceConstant.MRObjectiveScoreDetails);
		MRobjectiveScoreTO.setValue(irsRs.getString("objmasscalescorerng"));
		objectiveScoreDetails.getCollObjectiveScoreTO().add(MRobjectiveScoreTO);
		
		ObjectiveScoreTO CCobjectiveScoreTO = new ObjectiveScoreTO();
		CCobjectiveScoreTO.setScoreType(PrismWebServiceConstant.CCObjectiveScoreDetails);
		String conditionCode = irsRs.getString("conditioncode");
		if(conditionCode != null && !"".equals(conditionCode)){
			CCobjectiveScoreTO.setValue(conditionCode);
		}
		objectiveScoreDetails.getCollObjectiveScoreTO().add(CCobjectiveScoreTO);
		
		ObjectiveScoreTO INRCobjectiveScoreTO = new ObjectiveScoreTO();
		INRCobjectiveScoreTO.setScoreType(PrismWebServiceConstant.OSCObjectiveScoreDetails);		
		objectiveScoreDetails.getCollObjectiveScoreTO().add(INRCobjectiveScoreTO);
		
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
					switch(rs.getInt("quesOrder"))
					{
					// 1. What is your ethnicity? Hispanic/Latino//Not Hispanic/Latino
					case 1:
						Ethnicity = readOracleClob(rs.getClob("response"));
						break;
					// 2. Is your race American Indian or Alaska Native? Yes/No
					case 2:
						RaceAmericanIndianAlaskaNative = readOracleClob(rs.getClob("response")).compareToIgnoreCase("Y") == 0 ? true : false;
						if (RaceAmericanIndianAlaskaNative)RaceAnswerCount++;
						break;
					// 3. Is your race Asian? Yes/No
					case 3:
						RaceAsian = readOracleClob(rs.getClob("response")).compareToIgnoreCase("Y") == 0 ? true : false;
						if (RaceAsian)RaceAnswerCount++;
						break;
					// 4. Is your race Black or African American? Yes/No
					case 4:
						RaceBlack = readOracleClob(rs.getClob("response")).compareToIgnoreCase("Y") == 0 ? true : false;
						if (RaceBlack)RaceAnswerCount++;
						break;
					// 5. Is your race Native Hawaiian or other Pacific Islander? Yes/No
					case 5:
						RaceNativeHawaiian = readOracleClob(rs.getClob("response")).compareToIgnoreCase("Y") == 0 ? true : false;
						if (RaceNativeHawaiian)RaceAnswerCount++;
						break;
					// 6. Is your race White? Yes/No
					case 6:
						RaceWhite = readOracleClob(rs.getClob("response")).compareToIgnoreCase("Y") == 0 ? true : false;
						if (RaceWhite)RaceAnswerCount++;
						break;
					}
				} else {
					SurveyBioTO surveyBioTO = new SurveyBioTO();
					surveyBioTO.setSurveyName(rs.getString("quesCode"));
					String surveyValue = "";
					
					if(rs.getString("quesType").equalsIgnoreCase("CR")){
						surveyValue= removeXMLTags(readOracleClob(rs.getClob("response")));
					}else{
						surveyValue = readOracleClob(rs.getClob("response"));
					}
					
					surveyBioTO.setSurveyValue(formatSurveyValue(rs.getString("quesOrder"),surveyValue));
					surveyBioLst.add(surveyBioTO);
				}
			}
			//Adding Rslvd_Ethnic
			// Are marked 'Hispanic' and any single Race, any combination of 2 or more Races, or No Race
			if (Ethnicity != null && Ethnicity != "" && Ethnicity.equalsIgnoreCase("Hispanic")) {
				ResolvedEthnicityRace = "Hispanic";
			} else if (RaceAnswerCount >= 2) {
				ResolvedEthnicityRace = "Two or More Races";
			} else if (RaceAmericanIndianAlaskaNative) {
				ResolvedEthnicityRace = "American Indian";
			} else if (RaceBlack) {
				ResolvedEthnicityRace = "Black";
			} else if (RaceAsian) {
				ResolvedEthnicityRace = "Asian";
			} else if (RaceNativeHawaiian) {
				ResolvedEthnicityRace = "Pacific Islander";
			} else if (RaceWhite) {
				ResolvedEthnicityRace = "White";
			} else {
				ResolvedEthnicityRace = "";// blank or unknown
			}

			SurveyBioTO surveyBioTO = new SurveyBioTO();
			surveyBioTO.setSurveyName("Rslvd_Ethnic");
			if (ResolvedEthnicityRace != null && PrismWebServiceConstant.rslvdEthnicityMap.get(ResolvedEthnicityRace) != null) {
				surveyBioTO.setSurveyValue(PrismWebServiceConstant.rslvdEthnicityMap.get(ResolvedEthnicityRace));
			}
			surveyBioLst.add(0, surveyBioTO);
			
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getStudentSurveyBio() method to execute query : \n " +  GET_SURVEY_BIO_RES);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		
		studentSurveyBio.setDataChanged(true);
		
		return studentSurveyBio;
	}
	
	/**
	 * Remove XML tags in Constructed Responses
	 * @param sVal
	 * @return
	 */
	public static String removeXMLTags(String sVal) {
		String CDATA = "<![CDATA[";
		String END_CDATA = "]]>";
		String surveyValue = "";
		if (trim(sVal) != "" && sVal.indexOf(CDATA) > 0
				&& sVal.lastIndexOf(END_CDATA) > 0) {
			surveyValue = sVal.substring(sVal.indexOf(CDATA) + 9, sVal
					.lastIndexOf(END_CDATA));
		}
		return surveyValue;
	}
	
	/**
	 * Format the surveyValue based on the question order
	 * @param quesOrder
	 * @param returnStr
	 * @return 
	 */
	public static String formatSurveyValue(String quesOrder, String sVal) {
		String HYPEN = "-";
		String surveryValue = "";
		StringBuilder sb = new StringBuilder();
		if (quesOrder.equals("7")) {// Phone no formatting
			sb.append(sVal.substring(0, 3));
			sb.append(HYPEN);
			sb.append(sVal.substring(3, 6));
			sb.append(HYPEN);
			sb.append(sVal.substring(6, sVal.length()));
			surveryValue = sb.toString();
		} else if (quesOrder.equals("12")) {// Zip code formatting
			sb.append(sVal.substring(0, 5));
			sb.append(HYPEN);
			sb.append(sVal.substring(5, sVal.length()));
			surveryValue = sb.toString();
		} else if (quesOrder.equals("24")) {
			surveryValue = trim(sVal);
		} else if (quesOrder.equals("55")) {// Highest level of education
			Integer i = stringToInteger(trim(sVal));
			if (i != null) {
				if (i.intValue() == 0) {
					surveryValue = "KG";
				} else if (i.intValue() > 0 && i.intValue() < 13) {
					surveryValue = String.format("%02d", i);
				}
			}
		} else if (quesOrder.equals("56")) {// Home Language
			Integer i = stringToInteger(trim(sVal));
			if (i != null && i.intValue() > 0 && i.intValue() < 18) {
				surveryValue = String.format("%02d", i);
			}
		} else if (new Integer(quesOrder) > 60 && new Integer(quesOrder) < 81) {
			// Local Use Fields (state questions)
			surveryValue = trim(sVal);
			if (surveryValue.length() > 1) {
				surveryValue = HYPEN;
			}
		}else{
			surveryValue = sVal;
		}
		return surveryValue;
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
			parentOrgNodeCode = rs.getString("orgNodeCode");
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
	 * Populate the Student TO
	 * @param rs
	 * @param std
	 * @throws SQLException 
	 */
	private static void populateStudentBioTOSecondEdition(ResultSet rs, StudentBioTO std) throws SQLException, ParseException {
		while(rs.next()){
			std.setLastName(rs.getString("lastName"));
			std.setFirstName(rs.getString("firstName"));
			std.setMiddleInit((rs.getString("middleName") != null && !"".equals(rs.getString("middleName"))) ? rs.getString("middleName") : "" );
			std.setGender("");
			std.setGrade(rs.getString("grade"));
			std.setChrnlgclAge("");
			std.setBirthDate("");
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
	 * Get Customer Key
	 * @param rosterId
	 * @param itemSetId
	 * @return
	 */
	public static String getCustomerKey(Integer customerId){
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		String customerKey = "";
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_CUSTOMER_KEY);
			pst.setInt(1, customerId);
			rs = pst.executeQuery();
			while(rs.next()){
				customerKey = rs.getString("customerKey");
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getCustomerKey() method to execute query : \n " +  GET_CUSTOMER_KEY);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return customerKey;
	}
	
	/**
	 * Insert the WS Error Log table
	 * @param studentId
	 * @param rosterId
	 * @param sessionId
	 * @param wsType
	 * @param additionalInfo 
	 * @return
	 */
	public static long insertWSErrorLog(Integer studentId, long rosterId, long sessionId, String wsType, String errorMsg, String additionalInfo){
		CallableStatement cst  = null;
		Connection con = null;
		long wsErrorLogKey = 0;
		try {
			con = openOASDBcon(false);
			cst  = con.prepareCall(INSERT_WS_ERROR_LOG);
			cst.setLong(1, studentId);
			cst.setLong(2, rosterId);
			cst.setLong(3, sessionId);
			cst.setString(4, wsType);
			cst.setString(5, errorMsg);
			cst.setString(6, additionalInfo);
			cst.registerOutParameter(7, Types.NUMERIC);
			int count = cst.executeUpdate();
			if(count > 0){
				wsErrorLogKey = cst.getLong(7);
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.insertWSErrorLog() method to execute query : \n " +  INSERT_WS_ERROR_LOG);
			e.printStackTrace();
		} finally {
			close(con, cst);
		}
		return wsErrorLogKey;
	}
	
	/**
	 * Insert into WS Error Log table for eResource error
	 * @param studentId
	 * @param rosterId
	 * @param sessionId
	 * @return
	 */
	public static long insertWSErrorLogForEReesourceError(Integer studentId, long rosterId, long sessionId){
		CallableStatement cst  = null;
		Connection con = null;
		long wsErrorLogKey = 0;
		try {
			con = openOASDBcon(false);
			cst  = con.prepareCall(INSERT_WS_ERROR_LOG_ERERROR);
			cst.setLong(1, studentId);
			cst.setLong(2, rosterId);
			cst.setLong(3, sessionId);
			cst.registerOutParameter(4, Types.NUMERIC);
			int count = cst.executeUpdate();
			if(count > 0){
				wsErrorLogKey = cst.getLong(4);
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.insertWSErrorLogForEReesourceError() method to execute query : \n " +  INSERT_WS_ERROR_LOG_ERERROR);
			e.printStackTrace();
		} finally {
			close(con, cst);
		}
		return wsErrorLogKey;
	}
	
	/**
	 * Delete WS Error Log table
	 * @param wsErrorLogKey
	 */
	public static void delWSErrorLog(long wsErrorLogKey, Connection lockcon){
		PreparedStatement pst  = null;
		Connection con = null;
		try {
			if(lockcon != null){
				con = lockcon;
			}else{
				con = openOASDBcon(false);
			}
			pst  = con.prepareCall(DELETE_WS_ERROR_LOG);
			pst.setLong(1, wsErrorLogKey);
			pst.executeUpdate();
			System.out.println("PrismWebServiceDBUtility.delWSErrorLog : Successfully deleted WS Error Log Key : " + wsErrorLogKey);
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.delWSErrorLog() method to execute query : \n " +  DELETE_WS_ERROR_LOG);
			e.printStackTrace();
		} finally {
			if(lockcon != null){
				close(pst);
			}else{
				close(con, pst);
			}
		}
	}
	
	
	/**
	 * Update WS Error Log table
	 * @param wsErrorLogKey
	 * @param lockcon 
	 */
	public static void updateWSErrorLog(long wsErrorLogKey, int invokeCount, String message, String status, String additionalInfo, Connection lockcon){
		PreparedStatement pst  = null;
		Connection con = null;
		try {
			if(lockcon != null){
				con = lockcon;
			}else{
				con = openOASDBcon(false);
			}
			pst  = con.prepareCall(UPDATE_WS_ERROR_LOG);
			pst.setLong(1, invokeCount);
			pst.setString(2, subString(message, 3500));
			pst.setString(3, status);
			pst.setString(4, additionalInfo);
			pst.setLong(5, wsErrorLogKey);
			pst.executeUpdate();
			System.out.println("PrismWebServiceDBUtility.updateWSErrorLog : Update invoked for the error log key : " + wsErrorLogKey + " with Status : " + status);
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.updateWSErrorLog() method to execute query : \n " +  UPDATE_WS_ERROR_LOG);
			e.printStackTrace();
		} finally {
			if(lockcon != null){
				close(pst);
			}else{
				close(con, pst);
			}
		}
	}
	


	/**
	 * Update WS Error Log table
	 * @param wsErrorLogKey
	 * @throws SQLException 
	 */
	public static void retryWSProgress() throws SQLException{
		PreparedStatement pst  = null;
		CallableStatement cst  = null;
		Connection con = null;
		ResultSet rs = null;
		long logkey = 0L;
		int invkcount = 0;
		Integer stdid = 0; 
		long rstrid = 0L;
		long sessionid = 0L;
		String wstyp = "";
		try {
			con = openOASDBcon(true);
			con.setAutoCommit(false);
			cst  = con.prepareCall(SP_FETCH_WS_ERRORS);
			cst.setInt(1, PrismWebServiceConstant.retryReqRowCount);
			cst.execute();
			pst  = con.prepareCall(SELECT_WS_ERROR_LOG);
			rs = pst.executeQuery();
			while(rs.next()){
				logkey = rs.getLong("logkey");
				invkcount = rs.getInt("invkcount");
				stdid = rs.getInt("stdid");
				rstrid = rs.getLong("rstrid");
				sessionid = rs.getLong("sessionid");
				wstyp = rs.getString("wstyp");
				invkcount++;
				System.out.println("Prism Web Service retry for WS_ERROR_LOG key : " + logkey + " WS Type :"+ wstyp + " retry count : " + invkcount);
				if(wstyp != null && !"".equals(wstyp) && "Scoring".equalsIgnoreCase(wstyp)){
					PrismWebServiceHandler.scoring(rstrid,  stdid, sessionid, invkcount, logkey, con);
				}else{
					PrismWebServiceHandler.editStudent(stdid, invkcount, logkey, con);
				}
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getWSErrorLogProgress() method to execute query : \n " +  SELECT_WS_ERROR_LOG);
			e.printStackTrace();
		} finally {
			con.commit();
			con.setAutoCommit(true);
			close(con, cst, rs);
			close(con, pst, rs);
		}
	}
	
	
	/**
	 * Check Roster status. If Roster is in SC and NT status then returning false else true
	 * 
	 * @param rosterID
	 * @return
	 */
	public static boolean checkValidRosterStatus(long rosterID){
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		boolean validRoster = true;
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(CHECK_ROSTER_STATUS);
			pst.setLong(1, rosterID);
			rs = pst.executeQuery();
			while(rs.next()){
				validRoster = false;
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.checkRosterStatus() method to execute query : \n " +  CHECK_ROSTER_STATUS);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return validRoster;
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
			}else{
				conn.setAutoCommit(true);
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
	

	/**
	 * Sub String
	 * @param message
	 * @param maxLength
	 * @return
	 */
	private static String subString(String message, int maxLength) {
		String returnMsg = "";
		if(message != null && !"".equals(message)){
			if(message.length() > maxLength){
				returnMsg = message.substring(0, maxLength);
			}else{
				returnMsg = message;
			}
		}
		return returnMsg;
	}
	
	/**
	 * Trims String
	 * @param message
	 * @return
	 */
	private static String trim(String message){
		String returnMsg = "";
		if(message != null && !"".equals(message)){
			returnMsg = message.trim();
		}
		return returnMsg;
	}
	
	/**
	 * String to Integer
	 * @param value
	 * @return
	 */
	public static Integer stringToInteger(String value){
		Integer i = null;
		try {
			i = Integer.parseInt(value);
		} catch (Exception e) {
			return i;
		}
		return i;
	}
	
	/**
	 * Get Roster Form Assignment. If Second Edition return true if First Edition return false
	 * 
	 * @param rosterId
	 * @return boolean
	 */
	public static boolean getRosterFormEdition(long rosterId){
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		String form = null;
		boolean isTASCSecondEdition = false;
		try {
			con = openOASDBcon(false);
			pst = con.prepareStatement(GET_ROSTER_FORM);
			pst.setLong(1, rosterId);
			rs = pst.executeQuery();
			
			if(rs.next()){
				form = rs.getString("FORM_ASSIGNMENT");
				if ( null != form && (form.startsWith("D") || form.startsWith("E") || form.startsWith("F"))) { // Second Edition Forms
					isTASCSecondEdition = true;
	             }
	             else if ( null != form && ( form.startsWith("A") || form.startsWith("B") || form.startsWith("C"))) { // First Edition Forms
	            	 isTASCSecondEdition = false;
	             }
			}
		} catch (Exception e) {
			System.err.println("Error in the PrismWebServiceDBUtility.getRosterFormEdition() method to execute query : \n " +  GET_ROSTER_FORM);
			e.printStackTrace();
		} finally {
			close(con, pst, rs);
		}
		return isTASCSecondEdition;
	}
	
	
	/**
	 * Get the current timestamp in GMT time-zone  in long format
	 * @return java.sql.Date
	 */
	public static Long getCurrentGMTDateTime() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		Calendar cal=Calendar.getInstance(TimeZone.getDefault());
		Date dateGMT=cal.getTime();
		return new Long(dateGMT.getTime());
	}
	
}
