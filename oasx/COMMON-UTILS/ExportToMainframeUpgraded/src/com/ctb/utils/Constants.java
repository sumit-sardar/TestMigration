package com.ctb.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Constants {
	
	public static final DateFormat FILE_DATE_OUTPUT_FORMAT = new SimpleDateFormat("MMddyyHHmm");
	public static final String ACCOMMO_LABEL = "Accommodations"; 
	public static final String NAME_SEPARATOR = "_";
	public static final String GROUP = "GOAS";
	public static final String DATAFILE = "DATAFILE";
	public static final String ORDERFILE = "ORDERFILE";
	public static final String DATA_FILE_EXTN = ".dat";
	public static final String ORDER_FILE_EXTN = ".csv";
	
	/**
	 * Order File Header Name
	 */
	public static final String CUSTOMER_ID = "CUST_ID";
	public static final String CUSTOMER_NAME = "CUST_NAME";
	public static final String STATE = "ST";
	public static final String ORG_TESTING_PROGRAM = "ORG_TP";
	public static final String SCORING_ORDER_NUMBER = "SO";
	public static final String TAG_NUMBER = "TAG";
	public static final String TEST_NAME1 = "TEST_NAME";
	public static final String TEST_NAME2 = "TEST2_NAME";
	public static final String TEST_NAME3 = "TEST3_NAME";
	public static final String TEST_DATE = "TESTDATE";
	public static final String CASE_COUNT = "CASES";
	public static final String RE_RUN_FLAG = "RE-RUN";
	public static final String LONGITUDINAL_FLAG = "LONGITUDINAL";
	public static final String RE_ROSTER_FLAG = "RE_ROSTER";
	public static final String DATA_FILE_NAME = "DATA_FILE_NAME";
	public static final String CUSTOMER_CONTACT = "CUSTOMER_CONTACT";
	public static final String CUSTOMER_EMAIL = "CUSTOMER_CONTACT_EMAIL";
	public static final String CUSTOMER_PHONE = "CUSTOMER_CONTACT_PHONE";
	public static final String TB = "TB";
	public static final String HIERARCHY_MODE_LOCATION = "Hierarchy Mode location";
	public static final String SPECIAL_CODE_SELECT = "Special code select";
	public static final String EXPECTED_TITLES = "Expected Titles";
	public static final String HIERARCHY_MODE_LOCATION2 = "Hierarchy Mode location";
	public static final String SPECIAL_CODE_SELECT2 = "Special code select";
	public static final String EXPECTED_TITLES2 = "Expected Titles";
	public static final String SUBMITTER_EMAIL = "SUBMITTER_EMAIL";
	
	/**
	 * Query for LAS Links A / B / ESP A Customer 
	 **/
	
	public static String sql = "SELECT NODE.ORG_NODE_MDR_NUMBER AS MDR, ONC.CATEGORY_NAME AS CATEGORYNAME, ORG_NODE_CODE AS NODECODE, NODE.ORG_NODE_NAME AS NODENAME, ONC.CATEGORY_LEVEL AS CATEGORYLEVEL, ROS.TEST_ROSTER_ID AS ROSTERID FROM ORG_NODE_ANCESTOR ONA, ORG_NODE_CATEGORY ONC, ORG_NODE NODE, ORG_NODE_STUDENT ONS, TEST_ROSTER ROS WHERE ROS.TEST_ROSTER_ID IN (#) AND ONS.ORG_NODE_ID = ROS.ORG_NODE_ID AND ONS.STUDENT_ID = ROS.STUDENT_ID AND ONA.ORG_NODE_ID = ONS.ORG_NODE_ID AND ONC.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID AND NODE.ORG_NODE_ID = ONA.ANCESTOR_ORG_NODE_ID AND UPPER(ONC.CATEGORY_NAME) NOT IN ('CTB', 'ROOT') ORDER BY ROS.TEST_ROSTER_ID, ONC.CATEGORY_LEVEL";
//	public static String sql = "SELECT ORG_NODE_MDR_NUMBER AS MDR, CATEGORY_NAME AS CATEGORYNAME, ORG_NODE_CODE AS NODECODE, NODE.ORG_NODE_NAME AS NODENAME, ONC.CATEGORY_LEVEL AS CATEGORYLEVEL, ONS.STUDENT_ID  AS STUDENTID FROM ORG_NODE_ANCESTOR ONA, ORG_NODE_CATEGORY ONC, ORG_NODE NODE, ORG_NODE_STUDENT ONS WHERE    ONS.ORG_NODE_ID = ONA.ORG_NODE_ID   AND  ONS.STUDENT_ID IN (#) AND ONC.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID   AND NODE.ORG_NODE_ID = ONA.ANCESTOR_ORG_NODE_ID";
	public static String testRosterDetails = "SELECT ROS.TEST_ROSTER_ID AS ROSTER_ID, SISS.VALIDATION_STATUS AS VALIDATIONSTATUS, SISS.ITEM_SET_ORDER AS ITEMSETORDER, SISS.EXEMPTIONS AS TESTEXEMPTIONS, SISS.ABSENT AS ABSENT, DECODE(ISS.ITEM_SET_NAME, 'HABLANDO', 'SPEAKING', 'ESCUCHANDO', 'LISTENING', 'LECTURA', 'READING', 'ESCRITURA', 'WRITING', ISS.ITEM_SET_NAME) AS ITEMSETNAME FROM STUDENT_ITEM_SET_STATUS SISS, TEST_ROSTER ROS, ITEM_SET ISS WHERE ISS.ITEM_SET_ID = SISS.ITEM_SET_ID AND SISS.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID AND (ROS.STUDENT_ID, ROS.TEST_ROSTER_ID) IN (#) ORDER BY ROS.TEST_ROSTER_ID, SISS.ITEM_SET_ORDER";
	public static String scoreSkilAreaSQL = "SELECT COMPFACT.STUDENTID || COMPFACT.SESSIONID AS ID, " +
											"		'Overall' AS NAME," + 
											"       COMPFACT.SCALE_SCORE AS SCALE_SCORE," + 
											"       COMPFACT.PROFICENCY_LEVEL AS PROFICENCY_LEVEL," + 
											"       NULL AS POINTS_OBTAINED," + 
											"       NULL AS PERCENT_OBTAINED" + 
											"  FROM LASLINK_COMPOSITE_FACT COMPFACT" + 
											" WHERE (COMPFACT.STUDENTID,COMPFACT.SESSIONID) IN (#) " + 
											"UNION ALL " + 
											"SELECT CAREAFCT.STUDENTID || CAREAFCT.SESSIONID AS ID, " +
											"CAREADIM.NAME                    AS NAME," + 
											"       CAREAFCT.SCALE_SCORE             AS SCALE_SCORE," + 
											"       CAREAFCT.PROFICENCY_LEVEL        AS PROFICENCY_LEVEL," + 
											"       CAREAFCT.POINTS_OBTAINED         AS POINTS_OBTAINED," + 
											"       CAREAFCT.PERCENT_OBTAINED        AS PERCENT_OBTAINED" + 
											"  FROM LASLINK_CONTENT_AREA_FACT CAREAFCT, CONTENT_AREA_DIM CAREADIM" + 
											" WHERE CAREAFCT.CONTENT_AREAID = CAREADIM.CONTENT_AREAID" + 
											"   AND CONTENT_AREA_TYPE = 'LL CONTENT AREA'" + 
											"   AND (CAREAFCT.STUDENTID,CAREAFCT.SESSIONID) IN (#)";
	public static String customerDemographicsql = "SELECT THIS_.CUSTOMER_DEMOGRAPHIC_ID AS CUSTOMER_DEMOGRAPHIC_ID, THIS_.CUSTOMER_ID AS CUSTOMER_ID, THIS_.LABEL_NAME  AS LABEL_NAME FROM CUSTOMER_DEMOGRAPHIC THIS_ WHERE THIS_.CUSTOMER_ID = ?";
	public static String customerDemographicsqlWithLevel = "SELECT CD.CUSTOMER_DEMOGRAPHIC_ID AS CUSTOMER_DEMOGRAPHIC_ID, CD.CUSTOMER_ID AS CUSTOMER_ID, CD.LABEL_NAME AS LABEL_NAME, CDV.VALUE_NAME, CDV.VALUE_CODE FROM CUSTOMER_DEMOGRAPHIC CD, CUSTOMER_DEMOGRAPHIC_VALUE CDV WHERE CD.CUSTOMER_ID = ? AND CDV.CUSTOMER_DEMOGRAPHIC_ID = CD.CUSTOMER_DEMOGRAPHIC_ID AND CD.LABEL_NAME = ?";
	public static String customersql = "SELECT CUST.STATEPR AS STATE, NVL(ADDR.CITY, ' ') AS CITY, CUST.CONTACT_EMAIL AS EMAIL, CUST.CONTACT_PHONE AS PHONE, CUST.CONTACT_NAME AS CONTACT, (SELECT MAX(CATEGORY_LEVEL) AS MODELLEVEL FROM ORG_NODE_CATEGORY WHERE CUSTOMER_ID = CUST.CUSTOMER_ID AND ACTIVATION_STATUS = 'AC') AS MODEL_LEVEL FROM CUSTOMER CUST, ADDRESS ADDR WHERE CUSTOMER_ID = ? AND CUST.BILLING_ADDRESS_ID = ADDR.ADDRESS_ID(+)";
	public static String studentContactSql = "SELECT STUDENTCON0_.STUDENT_ID  AS STUDENT_ID, STUDENTCON0_.STUDENT_CONTACT_ID AS STUDENT_CONTACT_ID, STUDENTCON0_.CITY  AS CITY,   STUDENTCON0_.STATEPR  AS STATEPR,   STUDENTCON0_.STUDENT_ID  AS STUDENT_ID  FROM STUDENT_CONTACT STUDENTCON0_  WHERE STUDENTCON0_.STUDENT_ID IN (#)";
	public static String studentDemographicSql = "SELECT SDD.STUDENT_DEMOGRAPHIC_DATA_ID , SDD.CUSTOMER_DEMOGRAPHIC_ID , SDD.VALUE_NAME, SDD.VALUE, SDD.STUDENT_ID FROM STUDENT_DEMOGRAPHIC_DATA SDD WHERE SDD.STUDENT_ID IN (#)";
	public static String subSkillItemAreaInformation = "select tad.test_admin_id, tad.product_id || iset.item_set_id subskill_id, iset.item_set_name, iset1.item_set_name itemCategory from test_admin tad, product, item_set_category icat, item_set iset, item_set iset1, item_set_parent isp where tad.test_admin_id in (#) and icat.item_set_category_level = 4 and tad.product_id = product.product_id and product.parent_product_id = icat.framework_product_id and iset.item_set_category_id = icat.item_set_category_id and isp.item_set_id = iset.item_set_id and iset1.item_set_id = isp.parent_item_set_id";
	public static String subSkillIrsInformation = "select sec_objid, studentid||sessionid as id, percent_obtained, points_obtained  from laslink_sec_obj_fact  where (studentid,sessionid) in (#)";

	
	public static String rosterAllItemDetails = "SELECT * "+
												" FROM (SELECT DD.ROSTERID," + 
												"        DD.OASITEMID," + 
												"        DD.ITEMINDEX," + 
												"        DD.ITEMTYPE," + 
												"        DD.ITEMCORRECTRESPONSE," + 
												"        DD.SUBTESTID," + 
												"        DD.SUBTESTNAME," + 
												"        (SELECT DECODE(NVL(SISS.VALIDATION_STATUS, 'IN')," + 
												"                       'IN'," + 
												"                       'NC'," + 
												"                       (DECODE(NVL(SISS.EXEMPTIONS, 'N')," + 
												"                               'Y'," + 
												"                               'NC'," + 
												"                               ((DECODE(NVL(SISS.ABSENT, 'N'), 'Y', 'NC', 'CO'))))))" + 
												"           FROM STUDENT_ITEM_SET_STATUS SISS" + 
												"          WHERE SISS.TEST_ROSTER_ID = DD.ROSTERID" + 
												"            AND SISS.ITEM_SET_ID = DD.SUBTESTID) AS VALID," + 
												"        DECODE(DD.ITEMTYPE," + 
												"               'SR'," + 
												"               (SELECT ITR.RESPONSE" + 
												"                  FROM ITEM_RESPONSE ITR" + 
												"                 WHERE ITR.TEST_ROSTER_ID = DD.ROSTERID" + 
												"                   AND ITR.ITEM_sET_ID = DD.SUBTESTID" + 
												"                   AND ITR.ITEM_ID = DD.OASITEMID" + 
												"                   AND ITR.RESPONSE_SEQ_NUM = DD.MAXSEQNUM)," + 
												"               'CR'," + 
												"               (SELECT IRP.POINTS" + 
												"                  FROM ITEM_RESPONSE_POINTS IRP, ITEM_RESPONSE IRR" + 
												"                 WHERE IRR.TEST_ROSTER_ID = DD.ROSTERID" + 
												"                   AND IRR.ITEM_sET_ID = DD.SUBTESTID" + 
												"                   AND IRR.ITEM_ID = DD.OASITEMID" + 
												"                   AND IRR.RESPONSE_SEQ_NUM = DD.MAXSEQNUM" + 
												"                   AND IRP.ITEM_RESPONSE_ID = IRR.ITEM_RESPONSE_ID)) AS RESPONSE" + 
												"   FROM (SELECT DISTINCT ROS.TEST_ROSTER_ID AS ROSTERID," + 
												"                         ITEM.ITEM_ID AS OASITEMID," + 
												"                         TDISI.ITEM_SORT_ORDER AS ITEMINDEX," + 
												"                         ITEM.ITEM_TYPE AS ITEMTYPE," + 
												"                         ITEM.CORRECT_ANSWER AS ITEMCORRECTRESPONSE," + 
												"                         TD.ITEM_SET_ID AS SUBTESTID," + 
												"                         DECODE(TD.ITEM_SET_NAME," + 
												"                                'HABLANDO'," + 
												"                                'SPEAKING'," + 
												"                                'ESCUCHANDO'," + 
												"                                'LISTENING'," + 
												"                                'LECTURA'," + 
												"                                'READING'," + 
												"                                'ESCRITURA'," + 
												"                                'WRITING'," + 
												"                                TD.ITEM_SET_NAME) AS SUBTESTNAME," + 
												"                         (SELECT MAX(IR.RESPONSE_SEQ_NUM)" + 
												"                            FROM ITEM_RESPONSE IR" + 
												"                           WHERE IR.ITEM_SET_ID = TD.ITEM_SET_ID" + 
												"                             AND IR.ITEM_ID = ITEM.ITEM_ID" + 
												"                             AND IR.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID) AS MAXSEQNUM" + 
												"           FROM ITEM," + 
												"                ITEM_SET SEC," + 
												"                ITEM_SET_CATEGORY SECCAT," + 
												"                ITEM_SET_ANCESTOR SECISA," + 
												"                ITEM_SET_ITEM SECISI," + 
												"                ITEM_SET TD," + 
												"                ITEM_SET_ANCESTOR TDISA," + 
												"                ITEM_SET_ITEM TDISI," + 
												"                DATAPOINT DP," + 
												"                TEST_ROSTER ROS," + 
												"                TEST_ADMIN ADM," + 
												"                TEST_CATALOG TC," + 
												"                PRODUCT PROD " + 
												"          WHERE ROS.TEST_ROSTER_ID IN (#)" + 
												"            AND ADM.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID" + 
												"            AND TC.TEST_CATALOG_ID = ADM.TEST_CATALOG_ID" + 
												"            AND PROD.PRODUCT_ID = TC.PRODUCT_ID" + 
												"            AND ITEM.ACTIVATION_STATUS = 'AC'" + 
												"            AND TC.ACTIVATION_STATUS = 'AC'" + 
												"            AND SEC.ITEM_SET_ID = SECISA.ANCESTOR_ITEM_SET_ID" + 
												"            AND SEC.ITEM_SET_TYPE = 'RE'" + 
												"            AND SECISA.ITEM_SET_ID = SECISI.ITEM_SET_ID" + 
												"            AND ITEM.ITEM_ID = SECISI.ITEM_ID" + 
												"            AND TDISI.ITEM_ID = ITEM.ITEM_ID" + 
												"            AND TD.ITEM_SET_ID = TDISI.ITEM_SET_ID" + 
												"            AND TD.ITEM_SET_TYPE = 'TD'" + 
												"            AND TDISA.ITEM_SET_ID = TD.ITEM_SET_ID" + 
												"            AND ADM.ITEM_SET_ID = TDISA.ANCESTOR_ITEM_SET_ID" + 
												"            AND SECCAT.ITEM_SET_CATEGORY_ID = SEC.ITEM_SET_CATEGORY_ID" + 
												"            AND SECCAT.ITEM_SET_CATEGORY_LEVEL =" + 
												"                PROD.SEC_SCORING_ITEM_SET_LEVEL" + 
												"            AND DP.ITEM_ID = ITEM.ITEM_ID" + 
												"            AND TD.SAMPLE = 'F'" + 
												"            AND (TD.ITEM_SET_LEVEL != 'L' OR PROD.PRODUCT_TYPE = 'TL')" + 
												"            AND SECCAT.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID) DD" + 
												"  ORDER BY DD.ROSTERID, DD.SUBTESTID, DD.ITEMINDEX) "+
												"	WHERE VALID IS NOT NULL";


	public static String subtestIndicator = "SELECT TR.TEST_ROSTER_ID, CONL.SUBTEST_MODEL FROM TEST_ROSTER TR, TEST_ADMIN TA, CUSTOMER_CONFIGURATION CC, CUSTOMER_ORGNODE_LICENSE CONL, PRODUCT PROD WHERE TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID AND TA.CUSTOMER_ID = CC.CUSTOMER_ID AND CC.CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription' AND CC.DEFAULT_VALUE = 'T' AND TA.PRODUCT_ID = PROD.PRODUCT_ID AND CONL.CUSTOMER_ID = CC.CUSTOMER_ID AND CONL.ORG_NODE_ID = TR.ORG_NODE_ID AND CONL.PRODUCT_ID = PROD.PARENT_PRODUCT_ID AND (TR.STUDENT_ID,TR.TEST_ROSTER_ID) IN (#)";
	
	public static String modifiedQueryToFetchRosters = 	" SELECT ROSTER.*" + 
														"  FROM (SELECT ROST.TEST_ROSTER_ID," + 
														"               ROST.ACTIVATION_STATUS," + 
														"               ROST.TEST_COMPLETION_STATUS," + 
														"               ROST.CUSTOMER_ID," + 
														"               ROST.STUDENT_ID," + 
														"               ROST.TEST_ADMIN_ID," + 
														"               ROST.PRODUCT_ID," + 
														"               ROST.COMPLETION_DATE," + 
														"               STUDENT.FIRST_NAME as FIRST_NAME," + 
														"               STUDENT.LAST_NAME as LAST_NAME," + 
														"               STUDENT.MIDDLE_NAME as MIDDLE_NAME," + 
														"               STUDENT.BIRTHDATE as BIRTHDATE," + 
														"               decode(upper(STUDENT.GENDER), 'U', ' ', STUDENT.GENDER) as GENDER," + 
														"               STUDENT.GRADE as GRADE0," + 
														"               STUDENT.TEST_PURPOSE as TEST_PURPOSE," + 
														"               STUDENT.EXT_PIN1 as EXT_PIN1," + 
														"               ROST.TESTDATE," + 
														"               ROST.FORM," + 
														"               ROST.TIMEZONE," + 
														"               CATALOG.TEST_LEVEL AS TESTLEVEL," + 
														"               TO_CHAR(NVL(PROG.PROGRAM_START_DATE, '')," + 
														"                       'MMDDYYYY HH24:MI:SS') AS PROGRAMSTARTDATE," + 
														"               TO_CHAR((ROST.COMPLETION_DATE), 'MMDDYYYY HH24:MI:SS') AS DATETESTINGCOMPLETED," + 
														"               ROST.CATALOG_ID," + 
														"               ROST.PROGRAM_ID" + 
														"          FROM (SELECT 		 ROS.TEST_ROSTER_ID AS TEST_ROSTER_ID," + 
														"                                ROS.ACTIVATION_STATUS AS ACTIVATION_STATUS," + 
														"                                ROS.TEST_COMPLETION_STATUS AS TEST_COMPLETION_STATUS," + 
														"                                ROS.CUSTOMER_ID AS CUSTOMER_ID," + 
														"                                ROS.STUDENT_ID AS STUDENT_ID," + 
														"                                ROS.TEST_ADMIN_ID AS TEST_ADMIN_ID," + 
														"                                ROS.COMPLETION_DATE_TIME AS COMPLETION_DATE," + 
														"                                TO_CHAR((ROS.START_DATE_TIME)," + 
														"                                        'MMDDYY HH24:MI:SS') AS TESTDATE," + 
														"                                PROD.PRODUCT_ID AS PRODUCT_ID," + 
														"                                TA.PREFERRED_FORM AS FORM," + 
														"                                TA.TIME_ZONE AS TIMEZONE," + 
														"                                TA.TEST_CATALOG_ID AS CATALOG_ID," + 
														"                                TA.PROGRAM_ID" + 
														"                  FROM TEST_ROSTER ROS, TEST_ADMIN TA, PRODUCT PROD" + 
														"                 WHERE TA.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID" + 
														"                   AND TA.PRODUCT_ID = PROD.PRODUCT_ID" + 
														"                   AND PROD.PRODUCT_TYPE <> 'ST'" + 
														"                   AND PROD.PARENT_PRODUCT_ID = ?" + 
														"                   AND ROS.CUSTOMER_ID = ?" + 
														"                   AND ROS.ACTIVATION_STATUS = 'AC'" + 
														"                   AND ROS.TEST_COMPLETION_STATUS = 'CO'" + 
														"                UNION" + 
														"                SELECT 		 ROS.TEST_ROSTER_ID AS TEST_ROSTER_ID," + 
														"                                ROS.ACTIVATION_STATUS AS ACTIVATION_STATUS," + 
														"                                ROS.TEST_COMPLETION_STATUS AS TEST_COMPLETION_STATUS," + 
														"                                ROS.CUSTOMER_ID AS CUSTOMER_ID," + 
														"                                ROS.STUDENT_ID AS STUDENT_ID," + 
														"                                ROS.TEST_ADMIN_ID AS TEST_ADMIN_ID," + 
														"                                ROS.COMPLETION_DATE_TIME AS COMPLETION_DATE," + 
														"                                TO_CHAR((ROS.START_DATE_TIME)," + 
														"                                        'MMDDYY HH24:MI:SS') AS TESTDATE," + 
														"                                PROD.PRODUCT_ID AS PRODUCT_ID," + 
														"                                TA.PREFERRED_FORM AS FORM," + 
														"                                TA.TIME_ZONE AS TIMEZONE," + 
														"                                TA.TEST_CATALOG_ID AS CATALOG_ID," + 
														"                                TA.PROGRAM_ID" + 
														"                  FROM TEST_ROSTER             ROS," + 
														"                       TEST_ADMIN              TA," + 
														"                       PRODUCT                 PROD" + 
														"                 WHERE TA.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID" + 
														"                   AND TA.PRODUCT_ID = PROD.PRODUCT_ID" + 
														"                   AND PROD.PRODUCT_TYPE <> 'ST'" + 
														"                   AND PROD.PARENT_PRODUCT_ID = ?" + 
														"                   AND ROS.CUSTOMER_ID = ?" + 
														"                   AND ROS.ACTIVATION_STATUS = 'AC'" + 
														"                   AND ROS.TEST_COMPLETION_STATUS = 'IC'" + 
														"                   AND EXISTS" + 
														"                 (SELECT 1" + 
														"                          FROM STUDENT_ITEM_SET_STATUS SISS" + 
														"                         WHERE SISS.COMPLETION_STATUS IN ('CO', 'IS')" + 
														"                           AND SISS.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID)" + 
														"                   AND NOT EXISTS" + 
														"                 (SELECT 1" + 
														"                          FROM STUDENT_ITEM_SET_STATUS SI" + 
														"                         WHERE SI.COMPLETION_STATUS = 'IN'" + 
														"                           AND SI.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID)" + 
														"                UNION" + 
														"                SELECT 		 ROS.TEST_ROSTER_ID AS TEST_ROSTER_ID," + 
														"                                ROS.ACTIVATION_STATUS AS ACTIVATION_STATUS," + 
														"                                ROS.TEST_COMPLETION_STATUS AS TEST_COMPLETION_STATUS," + 
														"                                ROS.CUSTOMER_ID AS CUSTOMER_ID," + 
														"                                ROS.STUDENT_ID AS STUDENT_ID," + 
														"                                ROS.TEST_ADMIN_ID AS TEST_ADMIN_ID," + 
														"                                ROS.COMPLETION_DATE_TIME AS COMPLETION_DATE," + 
														"                                TO_CHAR((ROS.START_DATE_TIME)," + 
														"                                        'MMDDYY HH24:MI:SS') AS TESTDATE," + 
														"                                PROD.PRODUCT_ID AS PRODUCT_ID," + 
														"                                TA.PREFERRED_FORM AS FORM," + 
														"                                TA.TIME_ZONE AS TIMEZONE," + 
														"                                TA.TEST_CATALOG_ID AS CATALOG_ID," + 
														"                                TA.PROGRAM_ID" + 
														"                  FROM TEST_ROSTER             ROS," + 
														"                       TEST_ADMIN              TA," + 
														"                       PRODUCT                 PROD" + 
														"                 WHERE TA.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID" + 
														"                   AND TA.PRODUCT_ID = PROD.PRODUCT_ID" + 
														"                   AND PROD.PRODUCT_TYPE <> 'ST'" + 
														"                   AND PROD.PARENT_PRODUCT_ID = ?" + 
														"                   AND ROS.CUSTOMER_ID = ?" + 
														"                   AND ROS.ACTIVATION_STATUS = 'AC'" + 
														"                   AND ROS.TEST_COMPLETION_STATUS = 'IS'" + 
														"                   AND EXISTS " + 
														"                   (SELECT 1 " +
														"                           FROM STUDENT_ITEM_SET_STATUS SISS " +
														"                          WHERE SISS.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID " +
														"                            AND SISS.COMPLETION_STATUS = 'CO')) ROST," + 
														"               STUDENT," + 
														"               TEST_CATALOG CATALOG," + 
														"               PROGRAM PROG" + 
														"         WHERE STUDENT.STUDENT_ID = ROST.STUDENT_ID" + 
														"           AND ROST.CATALOG_ID = CATALOG.TEST_CATALOG_ID" + 
														"           AND PROG.PROGRAM_ID = ROST.PROGRAM_ID(+)" + 
														"         ORDER BY ROST.TEST_ROSTER_ID) ROSTER" + 
														" WHERE ROSTER.TEST_ROSTER_ID IS NOT NULL ";
	
	
	/** End 1st Edition **/
	
}
