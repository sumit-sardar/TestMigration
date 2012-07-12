package com.ctb.utils;

public class SQLQuery {

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
			+ " and content_area_type = 'TA CONTENT AREA'"
			+ " and careafct.studentid = :studentId "
			+ " and careafct.sessionid = :sessionId ";
	
	public static String getSemScores = "select ist.item_set_name, siss.sem_score," 
			+ " to_char((maxdate.mdate),'MMDDYY HH24:MI:SS') as maxDate, siss.ability_score from" 
			+ " student_item_set_status siss, item_set ist, " 
			+ " (select max(completion_date_time) mdate from student_item_set_status" 
			+ " where test_roster_id = ?) maxdate where ist.sample = 'F' and"
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
								        + "tr.activation_status as activation_status,"
								        + "tr.test_completion_status as test_completion_status,"
								        + " tr.customer_id as customer_id,"
								        + " tr.student_id as student_id,"
								        + " tr.test_admin_id   as test_admin_id,"
								        + " to_char((tr.completion_date_time),'MMDDYYYY HH24:MI:SS')  as dateTestingCompleted,"
								        + " ta.time_zone as timeZone,"
								        + " tr.restart_number as restartNumber,"
								        + " tr.last_mseq as lastMSEQ,"
								        + " to_char((tr.start_date_time),'MMDDYYYY HH24:MI:SS') as startDate" 
								        + " from test_roster tr, test_admin ta"
								        + " where tr.customer_id = ?"
								        + " and tr.activation_status = 'AC'"
								        + " and tr.TEST_COMPLETION_STATUS in ('CO', 'IS', 'IC')"
								        + " and tr.test_admin_id = ta.test_admin_id"
								        + " and ta.product_id = ?"
								        + " order by tr.test_roster_id";
	
	public static String testRosterByIDSql = " select this_.TEST_ROSTER_ID as TEST_ROSTER_ID, this_.ACTIVATION_STATUS as ACTIVATION_STATUS,"
			+ " this_.TEST_COMPLETION_STATUS as TEST_COMPLETION_STATUS, this_.CUSTOMER_ID as CUSTOMER_ID,  this_.STUDENT_ID   as STUDENT_ID,"
			+ " this_.TEST_ADMIN_ID  as TEST_ADMIN_ID  from TEST_ROSTER this_   where this_.TEST_ROSTER_ID IN( <#ROSTER_ID_LIST#> )";

	public static String testRosterWithStudentByRosterIDSql = " select this_.TEST_ROSTER_ID   as TEST_ROSTER_ID,  this_.ACTIVATION_STATUS  as ACTIVATION_STATUS,  this_.TEST_COMPLETION_STATUS as TEST_COMPLETION_STATUS, this_.STUDENT_ID   as STUDENT_ID,  this_.TEST_ADMIN_ID  as TEST_ADMIN_ID "
			+ " , student0_.FIRST_NAME   as FIRST_NAME,  student0_.LAST_NAME    as LAST_NAME,  student0_.MIDDLE_NAME  as MIDDLE_NAME,  student0_.BIRTHDATE    as BIRTHDATE,  decode(upper(student0_.GENDER), 'U', ' ', student0_.GENDER) as GENDER,  student0_.GRADE  as GRADE0,  student0_.TEST_PURPOSE as TEST_PURPOSE,   student0_.EXT_PIN1  as EXT_PIN1"
			+ "  from TEST_ROSTER this_ , student student0_  "
			+ " where this_.STUDENT_ID = student0_.STUDENT_ID  and this_.TEST_ROSTER_ID IN( <#ROSTER_ID_LIST#> )";

	public static String studentSql = "  select student0_.STUDENT_ID as STUDENT_ID, student0_.FIRST_NAME as FIRST_NAME,"
			+ " student0_.LAST_NAME as LAST_NAME,student0_.MIDDLE_NAME  as MIDDLE_NAME,student0_.BIRTHDATE as BIRTHDATE," 
			+ " student0_.GENDER as GENDER, student0_.GRADE  as GRADE0,student0_.CUSTOMER_ID as CUSTOMER_ID," 
			+ " student0_.TEST_PURPOSE as TEST_PURPOSE,   student0_.EXT_PIN1  as EXT_PIN1  from student student0_ "
			+ " where student0_.STUDENT_ID = ? ";

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

	public static String ALL_ITEMS_DETAILS_SQL = " SELECT irp.item_id, irp.response as original_response, " +
												" decode(irp.response, item.correct_answer, '1', '0') response, " +
												" derived.maxseqnum, irp.response_elapsed_time, irp.item_response_id " +
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
	
	public static String ALL_OBJECTIVE_SQL = "SELECT objective_id, objective_name " +
			                                   "FROM tabe_cat_objective";
	
	public static String CONTENT_DOMAIN_FOR_ROSTER_SQL = "SELECT DISTINCT its.item_set_id, its.item_set_name " +
      										               "FROM item_response irp, item_set its " +
      										              "WHERE its.item_set_id = irp.item_set_id " +
      										                "AND its.sample = 'F' " +
      										                "AND irp.test_roster_id = ?";
	
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
															   "ORDER BY items.item_set_name";
	
	public static final String CATEGORY_LEVEL_FOR_CUSTOMER = "select category_level from org_node_category where customer_id = ?";
	
	public static final String TOTAL_TEST_TIME_SQL = "SELECT SUM(itms.time_limit) total_time " +
	   													"FROM student_item_set_status siss, item_set itms " +
	   													"WHERE itms.item_set_id = siss.item_set_id " +
	   													"AND itms.SAMPLE = 'F' " +
	   													"AND itms.item_set_level <> 'L' " +
	   													"AND siss.item_set_id = ?" +
	   													"AND siss.test_roster_id = ?";

	public static final String TOTAL_TIME_TAKEN_SQL = "SELECT SUM(ir.response_elapsed_time) total_time " +
														"FROM item_response ir " +
														"WHERE ir.item_set_id = ?" +
														"AND ir.test_roster_id = ?";

	public static final String getAbilityScores = "SELECT cadim.name, tcaf.scale_score " +
													"FROM tabe_content_area_fact tcaf, content_area_dim cadim " +
													"WHERE tcaf.studentid = ? " +
													"AND tcaf.sessionid = ?" +
													"AND tcaf.content_areaid = cadim.content_areaid";
}