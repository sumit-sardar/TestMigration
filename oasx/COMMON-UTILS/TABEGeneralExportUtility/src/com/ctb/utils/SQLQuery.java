package com.ctb.utils;

public class SQLQuery {

	public static String PRODUCT_TYPE_SQL = "SELECT product_type FROM product WHERE product_id = ?";
	
	public static String sql = "select org_node_mdr_number as mdr, category_name as categoryName,"
			+ " org_node_code as nodeCode, node.org_node_name as nodeName, onc.category_level as categoryLevel"
			+ " from org_node_ancestor ona, org_node_category onc, org_node node, org_node_student ons "
			+ " where    ons.org_node_id = ona.org_node_id   and  ons.student_id =  ? "
			+ " and onc.org_node_category_id = node.org_node_category_id   and node.org_node_id = ona.ancestor_org_node_id"
			+ " order by onc.category_level desc";

	public static String testSessionSQl = "select tad.preferred_form as form, tc.test_level as testLevel,tad.time_zone as timezone,"
			+ " to_Char((roster.start_date_time),'MMDDYY HH24:MI:SS') as testDate,to_Char((roster.completion_date_time),'MMDDYY HH24:MI:SS')  as dateTestingCompleted,  to_Char(nvl(prg.program_start_date, ''),'MMDDYYYY HH24:MI:SS')  as programStartDate"
			+ " from test_admin tad, test_roster roster,test_catalog tc, program prg "
			+ "	where prg.program_id = tad.program_id(+)"
			+ " and  tad.test_admin_id = roster.test_admin_id and roster.test_completion_status in ('CO','IS','IC')"
			+ " and tc.test_catalog_id = tad.test_catalog_id"
			+ " and roster.test_roster_id = ? ";

	public static String customerModelLevel = " select max(category_level) as modelLevel"
			+ " from org_node_category where customer_id = :customerId";

	public static String testRosterDetails = " select distinct siss.VALIDATION_STATUS as validationStatus,"
			+ " siss.ITEM_SET_ORDER as itemSetOrder,siss.EXEMPTIONS as testExemptions,"
			+ " siss.ABSENT as absent,decode (iss.item_set_name,'HABLANDO','Speaking','ESCUCHANDO', 'Listening','LECTURA','Reading','ESCRITURA','Writing',iss.item_set_name) as itemSetName from student_item_set_status siss,"
			+ " test_roster ros, item_set iss where iss.item_set_id = siss.item_set_id and "
			+ " siss.test_roster_id = ros.test_roster_id and ros.student_id = ? "
			+ " and ros.test_roster_id = ? order by siss.item_set_order";

	public static String scoreSkilAreaSQL = "select careadim.name as name," 
			+ "careafct.scale_score as scaleScore," 
			+ "careafct.grade_equivalent as gradeEquivalent," 
			+ "careafct.nrs_levelid as nrsLevel," 
			+ "careafct.percentage_mastery as percentageMastery"
			+ " from tabe_content_area_fact careafct, content_area_dim careadim "
			+ " where careafct.content_areaid = careadim.content_areaid"
			+ " and content_area_type = ?"
			+ " and careafct.studentid = :studentId "
			+ " and careafct.sessionid = :sessionId ";
	
	public static String getSemScores = "select ist.item_set_name, siss.sem_score," 
			+ " to_char((maxdate.mdate),'MMDDYY HH24:MI:SS') as maxDate, siss.ability_score from" 
			+ " student_item_set_status siss, item_set ist, " 
			+ " (select max(completion_date_time) mdate from student_item_set_status" 
			+ " where test_roster_id = ?) maxdate where ist.sample = 'F' and"
			+ " ist.item_set_level <> 'L' and"
			+ " siss.item_set_id = ist.item_set_id and siss.test_roster_id = ?";

	public static String scoreSkilAreaOverAllSQL = "select compdim.name as name, compfact.scale_score as scaleScore, "
			+ "compfact.grade_equivalent as gradeEquivalent, compfact.nrs_levelid as levelId"
			+ " from tabe_composite_fact compfact, composite_dim compdim"
			+ " where compfact.compositeid = compdim.compositeid"
			+ " and compfact.studentid = :studentId "
			+ " and compfact.sessionid = :sessionId ";
	
	public static String getPredictedScores = "select preddim.name as name, predfact.predicted_ged as predictedGed "
			+ " from tabe_pred_subject_fact predfact, pred_subject_dim preddim"
			+ " where predfact.pred_subjectid = preddim.pred_subjectid"
			+ " and predfact.studentid = :studentId "
			+ " and predfact.sessionid = :sessionId ";
	
	public static String OBJECTIVE_MASTERY_SQL = "select primdim.name as objectiveName,"
            + " leveldim.name as masterylevel,"
            + " primfact.mastery_levelid as mastery "
            + " from tabe_prim_obj_fact primfact, prim_obj_dim primdim, level_dim leveldim"
            + " where primfact.levelid = leveldim.levelid"
            + " and primfact.prim_objid = primdim.prim_objid"
            + " and primfact.studentid = :studentId"
            + " and primfact.sessionid = :sessionId";
	
	public static String customerDemographicsql = "select this_.customer_demographic_id as customer_demographic_id, "
			+ " this_.customer_id as customer_id, this_.label_name  as label_name  "
			+ "from customer_demographic this_ where this_.customer_id = ?";

	public static String customerDemographicsqlWithLevel = "select this_.customer_demographic_id as customer_demographic_id, "
			+ " this_.customer_id as customer_id, this_.label_name  as label_name  from customer_demographic this_ "
			+ "where this_.customer_id = ? and label_name = 'Accommodations' ";

	public static String customersql = "select cust.STATEPR as state, nvl(addr.CITY,' ') as CITY,"
			+" cust.CONTACT_EMAIL as email, cust.CONTACT_PHONE as phone,cust.CONTACT_NAME as contact from Customer cust,"
			+" Address addr where CUSTOMER_ID = ? and cust.billing_address_id = addr.address_id(+) ";

	public static String testRosterSqlOld = " select this_.TEST_ROSTER_ID    as TEST_ROSTER_ID, this_.ACTIVATION_STATUS  as ACTIVATION_STATUS,   this_.TEST_COMPLETION_STATUS as TEST_COMPLETION_STATUS,"
			+ "  this_.CUSTOMER_ID            as CUSTOMER_ID, this_.STUDENT_ID  as STUDENT_ID, this_.TEST_ADMIN_ID   as TEST_ADMIN_ID  "
			+ " from TEST_ROSTER this_  "
			+ "where this_.CUSTOMER_ID = ?  and this_.ACTIVATION_STATUS = 'AC' "
			+ "and this_.TEST_COMPLETION_STATUS in ('CO', 'IS', 'IC')";
	
	public static String testRosterSql = "select tr.test_roster_id as test_roster_id,"
								        + " tr.activation_status as activation_status,"
								        + " tr.test_completion_status as test_completion_status,"
								        + " tr.customer_id as customer_id,"
								        + " tr.student_id as student_id,"
								        + " tr.test_admin_id   as test_admin_id,"
								        + " to_char((tr.completion_date_time),'MMDDYYYY HH24:MI:SS')  as dateTestingCompleted,"
								        + " ta.time_zone as timeZone,"
								        + " tr.restart_number as restartNumber,"
								        + " tr.last_mseq as lastMSEQ,"
								        + " to_char((tr.start_date_time),'MMDDYYYY HH24:MI:SS') as startDate,"
								        + " ta.location as test_location,"
								        + " p.product_name"
								        + " from test_roster tr, test_admin ta, product p"
								        + " where tr.customer_id = ?"
								        + " and tr.activation_status = 'AC'"
								        + " and tr.TEST_COMPLETION_STATUS in ('CO', 'IS', 'IC')"
								        + " and tr.VALIDATION_STATUS = 'VA'"
								        + " and tr.test_admin_id = ta.test_admin_id"
								        + " and ta.product_id = p.product_id "
								        + " and ta.product_id = ?"
								        + " and tr.last_used_sds like ('https://cat.ctb.com%')"
								        + " order by tr.test_roster_id";
	
	public static String testRosterByIDSql = " select this_.TEST_ROSTER_ID as TEST_ROSTER_ID, this_.ACTIVATION_STATUS as ACTIVATION_STATUS,"
			+ " this_.TEST_COMPLETION_STATUS as TEST_COMPLETION_STATUS, this_.CUSTOMER_ID as CUSTOMER_ID,  this_.STUDENT_ID   as STUDENT_ID,"
			+ " this_.TEST_ADMIN_ID  as TEST_ADMIN_ID  from TEST_ROSTER this_   where this_.TEST_ROSTER_ID IN( <#ROSTER_ID_LIST#> )";

	public static String testRosterWithStudentByRosterIDSql = " select this_.TEST_ROSTER_ID   as TEST_ROSTER_ID,  this_.ACTIVATION_STATUS  as ACTIVATION_STATUS,  this_.TEST_COMPLETION_STATUS as TEST_COMPLETION_STATUS, this_.STUDENT_ID   as STUDENT_ID,  this_.TEST_ADMIN_ID  as TEST_ADMIN_ID "
			+ " , student0_.FIRST_NAME   as FIRST_NAME,  student0_.LAST_NAME    as LAST_NAME,  student0_.MIDDLE_NAME  as MIDDLE_NAME,  student0_.BIRTHDATE    as BIRTHDATE,  decode(upper(student0_.GENDER), 'U', ' ', student0_.GENDER) as GENDER,  student0_.GRADE  as GRADE0,  student0_.TEST_PURPOSE as TEST_PURPOSE,   student0_.EXT_PIN1  as EXT_PIN1"
			+ "  from TEST_ROSTER this_ , student student0_  "
			+ " where this_.STUDENT_ID = student0_.STUDENT_ID  and this_.TEST_ROSTER_ID IN( <#ROSTER_ID_LIST#> )";

	public static String studentSql = "  select student0_.STUDENT_ID as STUDENT_ID, student0_.FIRST_NAME as FIRST_NAME,"
			+ " student0_.LAST_NAME as LAST_NAME,student0_.MIDDLE_NAME  as MIDDLE_NAME, to_char(student0_.BIRTHDATE, 'MMDDYY') as BIRTHDATE," 
			+ " student0_.GENDER as GENDER, student0_.GRADE  as GRADE0,student0_.CUSTOMER_ID as CUSTOMER_ID," 
			+ " student0_.TEST_PURPOSE as TEST_PURPOSE,   student0_.EXT_PIN1  as EXT_PIN1, student0_.EXT_PIN2  as EXT_PIN2  from student student0_ "
			+ " where student0_.STUDENT_ID = ? ";
	
	public static String studentAccommodationsSql = "select sa.SCREEN_READER AS SCREEN_READER, sa.CALCULATOR AS CALCULATOR,"
			+ " sa.TEST_PAUSE AS TEST_PAUSE, sa.UNTIMED_TEST UNTIMED_TEST, sa.QUESTION_BACKGROUND_COLOR AS QUESTION_BACKGROUND_COLOR,"
			+ " sa.QUESTION_FONT_COLOR AS QUESTION_FONT_COLOR, sa.QUESTION_FONT_SIZE AS QUESTION_FONT_SIZE,"
			+ " sa.ANSWER_BACKGROUND_COLOR AS ANSWER_BACKGROUND_COLOR, sa.ANSWER_FONT_COLOR AS ANSWER_FONT_COLOR,"
			+ " sa.ANSWER_FONT_SIZE AS ANSWER_FONT_SIZE, sa.HIGHLIGHTER AS HIGHLIGHTER,"
			+ " DECODE((SELECT AUDIO_FILE_NAME FROM MUSIC_FILE_LIST MFL WHERE MFL.FILE_ID=SA.MUSIC_FILE_ID),NULL,'',"
			+ " (SELECT AUDIO_FILE_NAME FROM MUSIC_FILE_LIST MFL WHERE MFL.FILE_ID = SA.MUSIC_FILE_ID)) AS MUSIC_FILE_NAME,"
			+ " sa.MASKING_RULER AS MASKING_RULER, sa.MAGNIFYING_GLASS AS MAGNIFYING_GLASS, sa.EXTENDED_TIME AS EXTENDED_TIME,"
			+ " sa.MASKING_TOOL AS MASKING_TOOL"
			+ " from student_accommodation sa where sa.STUDENT_ID = ?";

	public static String studentContactSql = " select studentcon0_.STUDENT_ID  as STUDENT_ID, studentcon0_.STUDENT_CONTACT_ID as STUDENT_CONTACT_ID, studentcon0_.CITY  as CITY,   studentcon0_.STATEPR  as STATEPR,   studentcon0_.STUDENT_ID  as STUDENT_ID  from STUDENT_CONTACT studentcon0_  where studentcon0_.STUDENT_ID = ?";

	public static String studentDemographicSql = " select STUDENT_DEMOGRAPHIC_DATA_ID , CUSTOMER_DEMOGRAPHIC_ID , VALUE_NAME, VALUE from student_demographic_data sdd where sdd.student_id = ? ";

	public static String customerDemographiValuecsql = "select value_name,value_code,customer_demographic_id  from customer_demographic_value  where customer_demographic_id =?";

	@SuppressWarnings("unused")
	private String subSkillItemAreaInformation1 = "select tad.product_id || iset.item_set_id subskill_id,"
			+ " iset.item_set_name from test_admin tad,product,item_set_category icat,item_set iset"
			+ " where tad.test_admin_id = ? and icat.item_set_category_level = 4 and tad.product_id = product.product_id"
			+ " and product.parent_product_id = icat.framework_product_id and iset.item_set_category_id = icat.item_set_category_id";

	public static String subSkillItemAreaInformation = "select tad.product_id || iset.item_set_id subskill_id,iset.item_set_name, "
			+ " iset1.item_set_name itemCategory from test_admin tad, product, item_set_category icat, item_set iset,item_set iset1,"
			+ " item_set_parent isp where tad.test_admin_id = ? and icat.item_set_category_level = 4 and"
			+ " tad.product_id = product.product_id and product.parent_product_id = icat.framework_product_id"
			+ " and iset.item_set_category_id = icat.item_set_category_id and isp.item_set_id = iset.item_set_id"
			+ " and iset1.item_set_id = isp.parent_item_set_id";

	public static String subSkillIrsInformation = "select sec_objid, sessionid, percent_obtained, points_obtained  from laslink_sec_obj_fact "
			+ " where studentid = ? and sessionid = ?";

	public static String ALL_ITEMS_DETAILS_SQL_TB = " SELECT irp.item_id, irp.response as original_response, " +
														" decode(irp.response, item.correct_answer, '1', '0') response, " +
														" derived.maxseqnum, irp.response_elapsed_time, irp.item_response_id, " +
														" item.ADS_ITEM_ID AS OAS_ITEM_ID" +
														" FROM item_response irp, item," +
														" (select item_response.item_id item_id, item_set_id, max(response_seq_num) maxseqnum  " +
														" from item_response , item  where test_roster_id = ?  " +
														" and item_response.item_id = item.item_id " +
														" and item.item_type = 'SR' " +
														" and item.ACTIVATION_STATUS = 'AC' " +
														" and item_response.item_set_id = ? " +
														" group by item_response.item_id, item_set_id ) derived " +
														" where irp.item_id = derived.item_id " +
														" and irp.item_set_id = derived.item_set_id  " +
														" and irp.response_seq_num = derived.maxseqnum  " +
														" and item.item_id = derived.item_id " +
														" and irp.item_id = item.item_id " +
														" and irp.test_roster_id = ? " +
														" and irp.item_set_id = ? " +
														" ORDER BY derived.maxseqnum";
	
/*	public static String ALL_ITEMS_DETAILS_SQL_TA = " SELECT IRP.ITEM_ID,"
													+ " ITEM.ADS_ITEM_ID AS OAS_ITEM_ID,"
													+ " IRP.RESPONSE AS ORIGINAL_RESPONSE,"
													+ " DECODE(IRP.RESPONSE, ITEM.CORRECT_ANSWER, '1', '0') RESPONSE,"
													+ " DERIVED.MAXSEQNUM,"
													+ " IRP.RESPONSE_ELAPSED_TIME,"
													+ " IRP.ITEM_RESPONSE_ID"
													+ " FROM ITEM_RESPONSE IRP,"
													+ " ITEM,"
													+ " (SELECT ITEM_RESPONSE.ITEM_ID ITEM_ID,"
													+ " ITEM_RESPONSE.ITEM_SET_ID ITEM_SET_ID,"
													+ " MAX(RESPONSE_SEQ_NUM) MAXSEQNUM"
													+ " FROM ITEM_RESPONSE, ITEM, TEST_ROSTER TR"
													+ " WHERE ITEM_RESPONSE.TEST_ROSTER_ID = ?"
													+ " AND ITEM_RESPONSE.ITEM_ID = ITEM.ITEM_ID"
													+ " AND ITEM.ITEM_TYPE = 'SR'"
													+ " AND ITEM.ACTIVATION_STATUS = 'AC'"
													+ " AND ITEM_RESPONSE.ITEM_SET_ID = ?"
													+ " AND TR.TEST_ROSTER_ID = ITEM_RESPONSE.TEST_ROSTER_ID"
													+ " AND ITEM_RESPONSE.RESPONSE_SEQ_NUM >= ((TR.RESTART_NUMBER - 1) * 1000000)"
													+ " GROUP BY ITEM_RESPONSE.ITEM_ID, ITEM_SET_ID) DERIVED"
													+ " WHERE IRP.ITEM_ID = DERIVED.ITEM_ID"
													+ " AND IRP.ITEM_SET_ID = DERIVED.ITEM_SET_ID"
													+ " AND IRP.RESPONSE_SEQ_NUM = DERIVED.MAXSEQNUM"
													+ " AND ITEM.ITEM_ID = DERIVED.ITEM_ID"
													+ " AND IRP.ITEM_ID = ITEM.ITEM_ID"
													+ " AND IRP.TEST_ROSTER_ID = ?"
													+ " AND IRP.ITEM_SET_ID = ?"
													+ " ORDER BY DERIVED.MAXSEQNUM";*/
	
	public static String ALL_ITEMS_DETAILS_SQL_TA = " SELECT IRP.ITEM_ID,"
													+ " ITEM.ADS_ITEM_ID AS OAS_ITEM_ID,"
													+ " IRP.RESPONSE AS ORIGINAL_RESPONSE,"
													+ " DECODE(IRP.RESPONSE, ITEM.CORRECT_ANSWER, '1', '0') RESPONSE,"
													+ " DERIVED.MAXSEQNUM,"
													+ " IRP.RESPONSE_ELAPSED_TIME,"
													+ " IRP.ITEM_RESPONSE_ID"
													+ " FROM ITEM_RESPONSE IRP,"
													+ " ITEM,"
													+ " (SELECT ITEM_RESPONSE.ITEM_ID ITEM_ID,"
													+ " ITEM_RESPONSE.ITEM_SET_ID ITEM_SET_ID,"
													+ " TR.RESTART_NUMBER,"
													+ " MAX(RESPONSE_SEQ_NUM) MAXSEQNUM"
													+ " FROM ITEM_RESPONSE, ITEM, TEST_ROSTER TR"
													+ " WHERE ITEM_RESPONSE.TEST_ROSTER_ID = ?"
													+ " AND ITEM_RESPONSE.ITEM_ID = ITEM.ITEM_ID"
													+ " AND ITEM.ITEM_TYPE = 'SR'"
													+ " AND ITEM.ACTIVATION_STATUS = 'AC'"
													+ " AND ITEM_RESPONSE.ITEM_SET_ID = ?"
													+ " AND TR.TEST_ROSTER_ID = ITEM_RESPONSE.TEST_ROSTER_ID"
													+ " AND ITEM_RESPONSE.RESPONSE_SEQ_NUM >="
													+ " (TRUNC((SELECT MAX(IR.RESPONSE_SEQ_NUM)"
													+ " FROM ITEM_RESPONSE IR"
													+ " WHERE IR.TEST_ROSTER_ID = ?"
													+ " AND IR.ITEM_SET_ID = ?) / 1000000 ) * 1000000)"
													+ " GROUP BY ITEM_RESPONSE.ITEM_ID, ITEM_SET_ID, TR.RESTART_NUMBER ) DERIVED"
													+ " WHERE IRP.ITEM_ID = DERIVED.ITEM_ID"
													+ " AND IRP.ITEM_SET_ID = DERIVED.ITEM_SET_ID"
													+ " AND IRP.RESPONSE_SEQ_NUM = DERIVED.MAXSEQNUM"
													+ " AND ITEM.ITEM_ID = DERIVED.ITEM_ID"
													+ " AND IRP.ITEM_ID = ITEM.ITEM_ID"
													+ " AND IRP.TEST_ROSTER_ID = ?"
													+ " AND IRP.ITEM_SET_ID = ?"
													+ " ORDER BY DERIVED.MAXSEQNUM";
	
	public static String GET_ITEM_RESPONSE_FOR_ITEM = "select item_id " 
												    + "from item_response "
												    + "where test_roster_id = ? "
												    + "and item_set_id = ? "
												    + "and response_seq_num < ? "
												    + "order by response_seq_num desc";
	
	public static String OBJECTIVE_SCORE_SQL = "SELECT siss.objective_score " +
											     "FROM student_item_set_status siss, " +
											     "item_set ist " +
											    "WHERE ist.SAMPLE = 'F' " +
											      "AND siss.item_set_id = ist.item_set_id " +
											      "AND siss.test_roster_id = ? ";
	
	public static String OBJECTIVE_SCORE_TB = "SELECT SUBSTR(TPOF.PRIM_OBJID, 5, LENGTH(TPOF.PRIM_OBJID)) AS objectiveId," +
											" TPOF.POINTS_OBTAINED AS rawScore," +
											" TPOF.POINTS_POSSIBLE AS totalRawScore," +
											" TPOF.MASTERY_LEVELID AS mastery" +
											" FROM TABE_PRIM_OBJ_FACT TPOF, PRIM_OBJ_DIM PD" +
											" WHERE TPOF.PRIM_OBJID = PD.PRIM_OBJID" +
											" AND TPOF.SESSIONID = ?" +
											" AND TPOF.STUDENTID = ?";
	
	/*public static String ALL_OBJECTIVE_SQL_TA = "SELECT objective_id, objective_name " +
			                                   "FROM tabe_cat_objective";*/
	
	public static String ALL_OBJECTIVE_SQL_TA = "SELECT TCO.OBJECTIVE_ID,"+
												" DECODE(CA.ITEM_SET_NAME,'Applied Mathematics','AM'," +
												" DECODE(CA.ITEM_SET_NAME,'Language','LN'," +
												" DECODE(CA.ITEM_SET_NAME,'Mathematics Computation','MC'," +
												" DECODE(CA.ITEM_SET_NAME, 'Reading', 'RD', '')))) || '-' ||" +
												" TCO.OBJECTIVE_NAME AS OBJECTIVE_NAME" +
												" FROM TABE_CAT_OBJECTIVE TCO, ITEM_SET CA" +
												" WHERE CA.ITEM_SET_ID = TCO.CONTENT_AREA_ID";
	
	/*public static String ALL_OBJECTIVE_SQL_TB = "SELECT DISTINCT ISET.ITEM_SET_ID AS objective_id,"
											 +" ISET.ITEM_SET_NAME as objective_name"
											 +" FROM PRODUCT PROD, ITEM_SET_CATEGORY ISC, ITEM_SET ISET"
											 +" WHERE PROD.SCORING_ITEM_SET_LEVEL = ISC.ITEM_SET_CATEGORY_LEVEL"
											 +" AND PROD.PARENT_PRODUCT_ID = ISC.FRAMEWORK_PRODUCT_ID"
											 +" AND ISET.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID"
											 +" AND ISET.ITEM_SET_TYPE = 'RE'"
											 +" AND PRODUCT_TYPE = 'TB'"
											 +" GROUP BY ISET.ITEM_SET_ID, ISET.ITEM_SET_NAME";*/
	
	public static String ALL_OBJECTIVE_SQL_TB = "SELECT ISET.ITEM_SET_ID AS OBJECTIVE_ID,"  +
												" DECODE(CA.ITEM_SET_NAME,'Applied Mathematics','AM',"  +
												" DECODE(CA.ITEM_SET_NAME,'Language','LN',"  +
												" DECODE(CA.ITEM_SET_NAME,'Language Mechanics','LM',"  +
												" DECODE(CA.ITEM_SET_NAME,'Math Computation','MC',"  +
												" DECODE(CA.ITEM_SET_NAME,'Reading','RD',"  +
												" DECODE(CA.ITEM_SET_NAME,'Spelling','SP',"  +
												" DECODE(CA.ITEM_SET_NAME,'Vocabulary','VO',''))))))) || '-' ||ISET.ITEM_SET_NAME AS OBJECTIVE_NAME"  +
												" FROM PRODUCT           PROD,"  +
												" ITEM_SET_CATEGORY ISC,"  +
												" ITEM_SET          ISET,"  +
												" ITEM_SET          CA,"  +
												" ITEM_SET_CATEGORY CAC,"  +
												" ITEM_SET_ANCESTOR ISA"  +
												" WHERE PROD.SCORING_ITEM_SET_LEVEL = ISC.ITEM_SET_CATEGORY_LEVEL"  +
												" AND PROD.PARENT_PRODUCT_ID = ISC.FRAMEWORK_PRODUCT_ID"  +
												" AND ISET.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID"  +
												" AND ISET.ITEM_SET_TYPE = 'RE'"  +
												" AND PRODUCT_TYPE = ?"  +
												" AND ISA.ITEM_SET_ID = ISET.ITEM_SET_ID"  +
												" AND ISA.ANCESTOR_ITEM_SET_ID = CA.ITEM_SET_ID"  +
												" AND CA.ITEM_SET_CATEGORY_ID = CAC.ITEM_SET_CATEGORY_ID"  +
												" AND CAC.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID"  +
												" AND CAC.ITEM_SET_CATEGORY_LEVEL = PROD.CONTENT_AREA_LEVEL"  +
												" GROUP BY CA.ITEM_SET_NAME, ISET.ITEM_SET_ID, ISET.ITEM_SET_NAME"  +
												" ORDER BY ISET.ITEM_SET_ID, OBJECTIVE_NAME";
	
	public static String CONTENT_DOMAIN_FOR_ROSTER_SQL_TABE = 
															 "SELECT ISET.ITEM_SET_ID," +
															 " ISET.ITEM_SET_NAME" +
															 " FROM STUDENT_ITEM_SET_STATUS SISS," +
															 " ITEM_SET ISET" +
															 " WHERE SISS.TEST_ROSTER_ID= ?" +
															 " AND SISS.ITEM_SET_ID =  ISET.ITEM_SET_ID" +
															 " AND ISET.SAMPLE = 'F'" +
														     " AND ISET.ITEM_SET_LEVEL <> 'L'" +
															 " ORDER BY ISET.ITEM_SET_ID" ;			
	public static String CONTENT_DOMAIN_FOR_ROSTER_SQL_TABE_LOCATOR = 
															 "SELECT ISET.ITEM_SET_ID," +
															 " SUBSTR(ISET.ITEM_SET_NAME,14) AS ITEM_SET_NAME" +
															 " FROM STUDENT_ITEM_SET_STATUS SISS," +
															 " ITEM_SET ISET" +
															 " WHERE SISS.TEST_ROSTER_ID= ?" +
															 " AND SISS.ITEM_SET_ID =  ISET.ITEM_SET_ID" +
															 " AND ISET.SAMPLE = 'F'" +
															 " ORDER BY ISET.ITEM_SET_ID" ;
	
	public static final String GET_ITEMS_FOR_ITEM_SET_SQL = "SELECT isi.item_id, item.correct_answer " +
	  														  "FROM item_set_item isi, item " +
	  														 "WHERE isi.item_id = item.item_id " +
	  														   "AND item.item_type = 'SR' " +
	  														   "AND item.activation_status = 'AC' " +
	  														   "AND isi.item_set_id = ? " +
	  														 "ORDER BY isi.item_sort_order";
	
	public static final String ITEM_TOTAL_TIME_VISIT_SQL = "SELECT irp.item_id item_id, irp.item_set_id," +
  															  "SUM(irp.response_elapsed_time) total_time " +
  															 "FROM item_response irp, item " +
  															"WHERE test_roster_id = ? " +
  															  "AND irp.item_id = item.item_id " +
  															  "AND irp.item_set_id = ? " +
  															  "AND item.item_type = 'SR' " +
  															  "AND item.activation_status = 'AC' " +
  															"GROUP BY irp.item_id, irp.item_set_id";
	
	public static final String RESTART_ITEM_SQL = "SELECT irp.item_id " +
													"FROM item_response irp, item " +
  												   "WHERE test_roster_id = ? " +
  												     "AND irp.item_id = item.item_id " +
  													 "AND irp.item_set_id = ? " +
  													 "AND item.item_type = 'SR' " +
  													 "AND item.activation_status = 'AC' " +
  													 "AND irp.response_seq_num > 100000 " +
  												   "ORDER BY irp.response_seq_num";
	
	public static final String GET_ALL_CONTENT_DOMAIN_SQL = "SELECT DISTINCT items.item_set_name, items.item_set_id " +
															    "FROM test_catalog tc, item_set_ancestor isa, item_set items " +
															   "WHERE tc.item_set_id = isa.ancestor_item_set_id " +
															     "AND isa.item_set_id = items.item_set_id " +
															     "AND items.item_set_type = 'TD' " +
															     "AND items.activation_status = 'AC' " +
															     "AND items.sample = 'F' " +
															     "AND items.item_set_level <> 'L' " +
															     "AND tc.product_id = ? " +
															   "ORDER BY items.item_set_id";
	
	public static final String GET_ALL_CONTENT_DOMAIN_SQL_FOR_LOCATOR = "SELECT DISTINCT substr(items.item_set_name,14) AS ITEM_SET_NAME, items.item_set_id " +
															    "FROM test_catalog tc, item_set_ancestor isa, item_set items " +
															   "WHERE tc.item_set_id = isa.ancestor_item_set_id " +
															     "AND isa.item_set_id = items.item_set_id " +
															     "AND items.item_set_type = 'TD' " +
															     "AND items.activation_status = 'AC' " +
															     "AND items.sample = 'F' " +
															     "AND tc.product_id = ? " +
															   "ORDER BY items.item_set_id";

	public static final String CATEGORY_LEVEL_FOR_CUSTOMER = "select category_level from org_node_category where customer_id = ?";
	
	public static final String TOTAL_TEST_TIME_SQL_PER_CONTENT_AREA = "SELECT SUM(itms.time_limit) total_time " +
	   													"FROM student_item_set_status siss, item_set itms " +
	   													"WHERE itms.item_set_id = siss.item_set_id " +
	   													"AND itms.SAMPLE = 'F' " +
	   													"AND itms.item_set_level <> 'L' " +
	   													"AND siss.item_set_id = ?" +
	   													"AND siss.test_roster_id = ?";

	public static final String TOTAL_TIME_TAKEN_SQL_PER_CONTENT_AREA = "SELECT SUM(ir.response_elapsed_time) total_time " +
														"FROM item_response ir " +
														"WHERE ir.item_set_id = ?" +
														"AND ir.test_roster_id = ?";
	
	public static final String TOTAL_TEST_TIME_SQL = "SELECT SUM(itms.time_limit) total_time " +
		  "FROM student_item_set_status siss, item_set itms " +
		  "WHERE itms.item_set_id = siss.item_set_id " +
	      "AND itms.SAMPLE = 'F' " +
	      "AND itms.item_set_level <> 'L' " +
	      "AND siss.test_roster_id = ?";
	
	public static final String TOTAL_TIME_TAKEN_SQL = "SELECT SUM(ir.response_elapsed_time) total_time " +
		   "FROM item_response ir " +
		   "WHERE ir.test_roster_id = ?";

	public static final String getAbilityScores = "SELECT cadim.name, tcaf.scale_score " +
													"FROM tabe_content_area_fact tcaf, content_area_dim cadim " +
													"WHERE tcaf.studentid = ? " +
													"AND tcaf.sessionid = ?" +
													"AND tcaf.content_areaid = cadim.content_areaid";
	
	public static final String CONTENT_DOMAIN_SQL_TABE_ADAPTIVE = "SELECT siss.item_set_id, " +
													"items.item_set_name, " +
													"items.item_set_level, " +
													"siss.raw_score, " +
													"siss.objective_score " +
													"FROM student_item_set_status siss, item_set items " +
													"WHERE items.item_set_id = siss.item_set_id " +
													"AND items.sample = 'F' " +
													"AND items.item_set_level <> 'L' " +
													"AND siss.test_roster_id = ? " +
													"ORDER BY items.item_set_name ";
	
	public static String CONTENT_DOMAIN_SQL_TABE_ONLINE = "SELECT DISTINCT ISET.ITEM_SET_ID, substr(ISETTD.ITEM_SET_NAME,6) as ITEM_SET_NAME,"+
													" ISETTD.ITEM_SET_LEVEL, SISS.RAW_SCORE, SISS.OBJECTIVE_SCORE"+
													" FROM STUDENT_ITEM_SET_STATUS SISS,"+
													" ITEM_SET ISET,"+
													" TEST_ROSTER TR,"+
													" TEST_ADMIN TA,"+
													" ITEM_SET_ITEM ISI1,"+
													" ITEM_SET_ITEM ISI2,"+
													" ITEM,"+
													" PRODUCT PROD,"+
													" ITEM_SET_CATEGORY ISC,"+
													" ITEM_SET_ANCESTOR ISA,"+
													" ITEM_SET ISETTD"+
													" WHERE SISS.TEST_ROSTER_ID = ?"+
													" AND SISS.ITEM_SET_ID = ISETTD.ITEM_SET_ID"+
													" AND ISETTD.SAMPLE = 'F'"+
													" AND ISETTD.ITEM_SET_LEVEL <> 'L'"+
													" AND TR.TEST_ROSTER_ID = SISS.TEST_ROSTER_ID"+
													" AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID"+
													" AND TA.PRODUCT_ID = PROD.PRODUCT_ID"+
													" AND PROD.PARENT_PRODUCT_ID = ISC.FRAMEWORK_PRODUCT_ID"+
													" AND PROD.CONTENT_AREA_LEVEL = ISC.ITEM_SET_CATEGORY_LEVEL"+
													" AND ISET.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID"+
													" AND ISETTD.ITEM_SET_ID = ISI1.ITEM_SET_ID"+
													" AND ISI1.ITEM_ID = ITEM.ITEM_ID"+
													" AND ISI2.ITEM_ID = ITEM.ITEM_ID"+
													" AND ISI2.ITEM_SET_ID = ISA.ITEM_SET_ID"+
													" AND ISA.ANCESTOR_ITEM_SET_ID = ISET.ITEM_SET_ID"+
													" AND ISET.ITEM_SET_TYPE = 'RE'"+
													" ORDER BY ISET.ITEM_SET_ID";
	
	public static String CONTENT_DOMAIN_SQL_TABE_LOCATOR = "SELECT DISTINCT ISET.ITEM_SET_ID, substr(ISETTD.ITEM_SET_NAME,14) as ITEM_SET_NAME,"+
													" ISETTD.ITEM_SET_LEVEL, SISS.RAW_SCORE, SISS.OBJECTIVE_SCORE"+
													" FROM STUDENT_ITEM_SET_STATUS SISS,"+
													" ITEM_SET ISET,"+
													" TEST_ROSTER TR,"+
													" TEST_ADMIN TA,"+
													" ITEM_SET_ITEM ISI1,"+
													" ITEM_SET_ITEM ISI2,"+
													" ITEM,"+
													" PRODUCT PROD,"+
													" ITEM_SET_CATEGORY ISC,"+
													" ITEM_SET_ANCESTOR ISA,"+
													" ITEM_SET ISETTD"+
													" WHERE SISS.TEST_ROSTER_ID = ?"+
													" AND SISS.ITEM_SET_ID = ISETTD.ITEM_SET_ID"+
													" AND ISETTD.SAMPLE = 'F'"+
													" AND TR.TEST_ROSTER_ID = SISS.TEST_ROSTER_ID"+
													" AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID"+
													" AND TA.PRODUCT_ID = PROD.PRODUCT_ID"+
													" AND PROD.PARENT_PRODUCT_ID = ISC.FRAMEWORK_PRODUCT_ID"+
													" AND PROD.CONTENT_AREA_LEVEL = ISC.ITEM_SET_CATEGORY_LEVEL"+
													" AND ISET.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID"+
													" AND ISETTD.ITEM_SET_ID = ISI1.ITEM_SET_ID"+
													" AND ISI1.ITEM_ID = ITEM.ITEM_ID"+
													" AND ISI2.ITEM_ID = ITEM.ITEM_ID"+
													" AND ISI2.ITEM_SET_ID = ISA.ITEM_SET_ID"+
													" AND ISA.ANCESTOR_ITEM_SET_ID = ISET.ITEM_SET_ID"+
													" AND ISET.ITEM_SET_TYPE = 'RE'"+
													" ORDER BY ISET.ITEM_SET_ID";
	
	public static final String SCALE_SCORE_SQL = "SELECT tcaf.content_areaid, " +
													"tcaf.scale_score " +
													"FROM tabe_content_area_fact tcaf " +
													"WHERE tcaf.studentid = ? " +
													"AND tcaf.sessionid = ? ";
	
	public static final String SCALE_SCORE_SQL_TA = "SELECT TA.PRODUCT_ID || SISS.ITEM_SET_ID AS CONTENT_AREAID," +
												" SISS.ABILITY_SCORE AS SCALE_SCORE" +
												" FROM STUDENT_ITEM_SET_STATUS SISS," +
												" TEST_ROSTER TR," +
												" TEST_ADMIN TA," +
												" ITEM_SET ISET" +
												" WHERE TR.STUDENT_ID = ?" +
												" AND TR.TEST_ADMIN_ID = ?" +
												" AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID" +
												" AND ISET.ITEM_SET_ID = SISS.ITEM_SET_ID" +
												" AND ISET.SAMPLE = 'F'" +
												" AND TR.TEST_ROSTER_ID = SISS.TEST_ROSTER_ID";
	
	public static final String GET_LAST_ITEM_SQL = "SELECT item_id AS lastItemId "
													+ "FROM ITEM_RESPONSE "
													+ "WHERE RESPONSE_SEQ_NUM = "
													+ "(SELECT MAX(IR.RESPONSE_SEQ_NUM) "
													+ "FROM ITEM_RESPONSE IR "
													+ "WHERE IR.TEST_ROSTER_ID =?) "
													+ "AND TEST_ROSTER_ID = ?";
}