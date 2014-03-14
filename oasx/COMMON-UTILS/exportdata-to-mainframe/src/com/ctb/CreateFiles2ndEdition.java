package com.ctb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.ffpojo.exception.FFPojoException;
import org.ffpojo.file.writer.FileSystemFlatFileWriter;
import org.ffpojo.file.writer.FlatFileWriter;

import com.ctb.dto.CustomerDemographic;
import com.ctb.dto.CustomerDemographicValue;
import com.ctb.dto.ItemResponsesGRTLL2ND;
import com.ctb.dto.OrderFile;
import com.ctb.dto.ProficiencyLevelsLL2ND;
import com.ctb.dto.ReferenceNormalCurveEquivalentsLL2ND;
import com.ctb.dto.ReferencePercentileRanksLL2ND;
import com.ctb.dto.RostersItem;
import com.ctb.dto.ScaleScoresLL2ND;
import com.ctb.dto.SkillAreaNumberCorrectLL2ND;
import com.ctb.dto.SkillAreaPercentCorrectLL2ND;
import com.ctb.dto.SpecialCodes;
import com.ctb.dto.Student;
import com.ctb.dto.StudentContact;
import com.ctb.dto.StudentDemographic;
import com.ctb.dto.SubSkillNumberCorrectLL2ND;
import com.ctb.dto.SubSkillPercentCorrectLL2ND;
import com.ctb.dto.TestRoster;
import com.ctb.dto.TfilLL2ND;
import com.ctb.utils.Configuration;
import com.ctb.utils.EmetricUtil;
import com.ctb.utils.ExtractUtil;
import com.ctb.utils.SftpUtil;
import com.ctb.utils.SqlUtil;
import com.jcraft.jsch.Session;
public class CreateFiles2ndEdition {
	
		
	private static String CUSTOMER_MODEL_LEVEL = " select max(category_level) as modelLevel from org_node_category where customer_id = :customerId and activation_status = 'AC'";
	private static String CUSTOMER_DEMOGRAPHIC_SQL = "select this_.customer_demographic_id as customer_demographic_id, this_.customer_id as customer_id, this_.label_name  as label_name from customer_demographic this_ where this_.customer_id = ?";
	private static String CUSTOMER_DEMOGRAPHIC_VALUE_SQL = "select value_name,value_code,customer_demographic_id  from customer_demographic_value  where customer_demographic_id =?";
	private static String CUSTOMER_DEMOGRAPHIC_WITH_LEVEL_SQL = "select this_.customer_demographic_id as customer_demographic_id, this_.customer_id as customer_id, this_.label_name  as label_name  from customer_demographic this_ where this_.customer_id = ? and label_name = 'Accommodations' ";
	private static String CUSTOMER_SQL = "select cust.STATEPR as state, nvl(addr.CITY,' ') as CITY, cust.CONTACT_EMAIL as email, cust.CONTACT_PHONE as phone,cust.CONTACT_NAME as contact from Customer cust, Address addr where CUSTOMER_ID = ? and cust.billing_address_id = addr.address_id(+) ";
	private static String STUDENT_CONTACT_SQL = " select studentcon0_.STUDENT_ID  as STUDENT_ID, studentcon0_.STUDENT_CONTACT_ID as STUDENT_CONTACT_ID, studentcon0_.CITY  as CITY,   studentcon0_.STATEPR  as STATEPR,   studentcon0_.STUDENT_ID  as STUDENT_ID  from STUDENT_CONTACT studentcon0_  where studentcon0_.STUDENT_ID = ?";
	private static String STUDENT_DEMOGRAPHIC_SQL = " select STUDENT_DEMOGRAPHIC_DATA_ID , CUSTOMER_DEMOGRAPHIC_ID , VALUE_NAME, VALUE from student_demographic_data sdd where sdd.student_id = ? ";
	private static String ORGANIZATION_SQL = "select org_node_mdr_number as mdr, category_name as categoryName, org_node_code as nodeCode, node.org_node_name as nodeName, onc.category_level as categoryLevel from org_node_ancestor ona, org_node_category onc, org_node node, org_node_student ons where    ons.org_node_id = ona.org_node_id   and  ons.student_id =  ? and onc.org_node_category_id = node.org_node_category_id   and node.org_node_id = ona.ancestor_org_node_id";
	private static String TEST_SESSION_SQL = "SELECT TAD.PREFERRED_FORM AS FORM, TC.TEST_LEVEL AS TESTLEVEL, TAD.TIME_ZONE AS TIMEZONE, TO_CHAR((ROSTER.START_DATE_TIME), 'MMDDYY HH24:MI:SS') AS TESTDATE, TO_CHAR(NVL(PRG.PROGRAM_START_DATE, ''), 'MMDDYYYY HH24:MI:SS') AS PROGRAMSTARTDATE FROM TEST_ADMIN TAD, TEST_ROSTER ROSTER, TEST_CATALOG TC, PROGRAM PRG WHERE PRG.PROGRAM_ID = TAD.PROGRAM_ID(+) AND TAD.TEST_ADMIN_ID = ROSTER.TEST_ADMIN_ID AND ROSTER.TEST_COMPLETION_STATUS IN ('CO', 'IS', 'IC') AND TC.TEST_CATALOG_ID = TAD.TEST_CATALOG_ID AND ROSTER.TEST_ROSTER_ID = ?"; 
	private static String TEST_ROSTER_DETAILS_SQL = "SELECT DISTINCT SISS.VALIDATION_STATUS AS VALIDATIONSTATUS, SISS.ITEM_SET_ORDER AS ITEMSETORDER, SISS.EXEMPTIONS AS TESTEXEMPTIONS, SISS.ABSENT AS ABSENT, DECODE(ISS.ITEM_SET_NAME, 'HABLANDO', 'Speaking', 'ESCUCHANDO', 'Listening', 'LECTURA', 'Reading', 'ESCRITURA', 'Writing', ISS.ITEM_SET_NAME) AS ITEMSETNAME FROM STUDENT_ITEM_SET_STATUS SISS, TEST_ROSTER ROS, ITEM_SET ISS WHERE ISS.ITEM_SET_ID = SISS.ITEM_SET_ID AND SISS.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID AND ROS.STUDENT_ID = ? AND ROS.TEST_ROSTER_ID = ? ORDER BY SISS.ITEM_SET_ORDER";
	private static String SCORE_SKILL_AREA_OVERALL_SQL = "SELECT COMPFACT.SCALE_SCORE, COMPFACT.PROFICENCY_LEVEL, COMPFACT.NORMAL_CURVE_EQUIVALENT, COMPFACT.NATIONAL_PERCENTILE FROM LASLINK_COMPOSITE_FACT COMPFACT WHERE COMPFACT.STUDENTID = :STUDENTID AND COMPFACT.SESSIONID = :SESSIONID";
	private static String SCORE_SKILL_AREA_SQL = "SELECT CAREADIM.NAME  AS NAME, CAREAFCT.SCALE_SCORE  AS SCALE_SCORE, CAREAFCT.PROFICENCY_LEVEL  AS PROFICENCY_LEVEL, CAREAFCT.POINTS_OBTAINED  AS POINTS_OBTAINED, CAREAFCT.PERCENT_OBTAINED  AS PERCENT_OBTAINED, CAREAFCT.NORMAL_CURVE_EQUIVALENT AS NORMAL_CURE_EUIVALENT, CAREAFCT.NATIONAL_PERCENTILE  AS NATIONAL_PERCENTILE, CAREAFCT.LEXILE AS LEXILE FROM LASLINK_CONTENT_AREA_FACT CAREAFCT, CONTENT_AREA_DIM CAREADIM WHERE CAREAFCT.CONTENT_AREAID = CAREADIM.CONTENT_AREAID AND CONTENT_AREA_TYPE = 'LL CONTENT AREA' AND CAREAFCT.STUDENTID = :STUDENTID AND CAREAFCT.SESSIONID = :SESSIONID";
	private static String SUBSKILL_ITEM_AREA_INFOMATION = "SELECT TAD.PRODUCT_ID || ISET.ITEM_SET_ID SUBSKILL_ID, ISET.ITEM_SET_NAME, ISET1.ITEM_SET_NAME ITEMCATEGORY, TAD.PRODUCT_ID AS PRODUCTID FROM TEST_ADMIN TAD, PRODUCT, ITEM_SET_CATEGORY ICAT, ITEM_SET ISET, ITEM_SET ISET1, ITEM_SET_PARENT ISP WHERE TAD.TEST_ADMIN_ID = ? AND ICAT.ITEM_SET_CATEGORY_LEVEL = 4 AND TAD.PRODUCT_ID = PRODUCT.PRODUCT_ID AND PRODUCT.PARENT_PRODUCT_ID = ICAT.FRAMEWORK_PRODUCT_ID AND ISET.ITEM_SET_CATEGORY_ID = ICAT.ITEM_SET_CATEGORY_ID AND ISP.ITEM_SET_ID = ISET.ITEM_SET_ID AND ISET1.ITEM_SET_ID = ISP.PARENT_ITEM_SET_ID";
	private static String ACADEMIC_SKILL_AREA_INFORMATION = "SELECT OBJECTIVE_ID, OBJECTIVE_NAME, SUBJECT FROM LASLINK_CD_OBJECTIVE";
	private static String SUBSKILL_IRS_INFORMATION = "SELECT SEC_OBJID, SESSIONID, PERCENT_OBTAINED, POINTS_OBTAINED FROM LASLINK_SEC_OBJ_FACT WHERE STUDENTID = ? AND SESSIONID = ?";
	private static String SUBTEST_INDICATOR = "SELECT CONL.SUBTEST_MODEL  FROM TEST_ROSTER              TR, TEST_ADMIN               TA, CUSTOMER_CONFIGURATION   CC, CUSTOMER_ORGNODE_LICENSE CONL, PRODUCT                  PROD WHERE TR.TEST_ROSTER_ID = ? AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID AND  TA.CUSTOMER_ID = CC.CUSTOMER_ID AND CC.CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription' AND CC.DEFAULT_VALUE = 'T' AND TA.PRODUCT_ID = PROD.PRODUCT_ID AND CONL.CUSTOMER_ID = CC.CUSTOMER_ID AND CONL.ORG_NODE_ID = TR.ORG_NODE_ID"; // AND CONL.PRODUCT_ID = PROD.PARENT_PRODUCT_ID//Fix for defect#75718::Commenting out this condition as parent-product-id is different for Las A/B and Las C/EspB..Date:10Oct,2013
	private static String ALL_ITEM_DETAILS_FOR_ROSTER = "SELECT DD.*, VALID,  STATUS FROM  ( SELECT DISTINCT ITEM.ITEM_ID AS OASITEMID,   TDISI.ITEM_SORT_ORDER AS ITEMINDEX,   ITEM.ITEM_TYPE AS ITEMTYPE,   ITEM.CORRECT_ANSWER AS ITEMCORRECTRESPONSE,   TD.ITEM_SET_ID AS SUBTESTID,    DECODE (TD.ITEM_SET_NAME,'HABLANDO','SPEAKING','ESCUCHANDO', 'LISTENING','LECTURA','READING','ESCRITURA','WRITING', TD.ITEM_SET_NAME) AS SUBTESTNAME " +
	" FROM   ITEM,   ITEM_SET SEC,   ITEM_SET_CATEGORY SECCAT,   ITEM_SET_ANCESTOR SECISA,   ITEM_SET_ITEM SECISI,   ITEM_SET TD,   ITEM_SET_ANCESTOR TDISA,   ITEM_SET_ITEM TDISI,   DATAPOINT DP,   TEST_ROSTER ROS,   TEST_ADMIN ADM,   TEST_CATALOG TC,   PRODUCT PROD,  STUDENT_ITEM_SET_STATUS SIS  WHERE   ROS.TEST_ROSTER_ID = ?   AND ADM.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID   AND TC.TEST_CATALOG_ID = ADM.TEST_CATALOG_ID   AND PROD.PRODUCT_ID = TC.PRODUCT_ID   AND ITEM.ACTIVATION_STATUS = 'AC'   AND TC.ACTIVATION_STATUS = 'AC'  " +
	" AND SEC.ITEM_SET_ID = SECISA.ANCESTOR_ITEM_SET_ID   AND SEC.ITEM_SET_TYPE = 'RE'   AND SECISA.ITEM_SET_ID = SECISI.ITEM_SET_ID   AND ITEM.ITEM_ID = SECISI.ITEM_ID   AND TDISI.ITEM_ID = ITEM.ITEM_ID   AND TD.ITEM_SET_ID = TDISI.ITEM_SET_ID   AND TD.ITEM_SET_TYPE = 'TD'  AND TDISA.ITEM_SET_ID = TD.ITEM_SET_ID   AND ADM.ITEM_SET_ID = TDISA.ANCESTOR_ITEM_SET_ID   AND SECCAT.ITEM_SET_CATEGORY_ID = SEC.ITEM_SET_CATEGORY_ID   AND SECCAT.ITEM_SET_CATEGORY_LEVEL = PROD.SEC_SCORING_ITEM_SET_LEVEL   AND DP.ITEM_ID = ITEM.ITEM_ID   AND TD.SAMPLE = 'F' " +
	" AND (TD.ITEM_SET_LEVEL != 'L' OR PROD.PRODUCT_TYPE = 'TL')   AND SECCAT.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID  AND SIS.TEST_ROSTER_ID  = ROS.TEST_ROSTER_ID    ) DD , ( SELECT SIS.ITEM_SET_ID , DECODE(NVL(SIS.VALIDATION_STATUS, 'IN'), 'IN', 'NC', (DECODE(NVL(SIS.EXEMPTIONS, 'N'), 'Y',  'NC', ((DECODE(NVL(SIS.ABSENT, 'N'), 'Y', 'NC', 'CO')))))) VALID FROM STUDENT_ITEM_SET_STATUS SIS WHERE TEST_ROSTER_ID = ?	 )DD1,  (SELECT SISS.ITEM_SET_ID,    DECODE( SISS.COMPLETION_STATUS, 'CO', 'CO', (DECODE(SISS.COMPLETION_STATUS, 'IS', 'CO', ((DECODE(SISS.COMPLETION_STATUS, 'IC', 'CO', 'NC')))))) STATUS  FROM STUDENT_ITEM_SET_STATUS SISS" +
	" WHERE SISS.TEST_ROSTER_ID = ? ) DD2 WHERE DD.SUBTESTID = DD1.ITEM_SET_ID AND dd.subtestid   = dd2.item_set_id ORDER BY SUBTESTID, ITEMINDEX";
	private static String ITEM_RESPONSE_SR_FOR_ROSTER = "SELECT IRP.ITEM_ID, IRP.RESPONSE FROM ITEM_RESPONSE IRP, (SELECT ITEM_RESPONSE.ITEM_ID ITEM_ID, ITEM_SET_ID, MAX(RESPONSE_SEQ_NUM) MAXSEQNUM FROM ITEM_RESPONSE, ITEM WHERE TEST_ROSTER_ID = ? AND ITEM_RESPONSE.ITEM_ID = ITEM.ITEM_ID AND ITEM.ITEM_TYPE = 'SR' AND ITEM.ACTIVATION_STATUS = 'AC' GROUP BY ITEM_RESPONSE.ITEM_ID, ITEM_SET_ID) DERIVED WHERE IRP.ITEM_ID = DERIVED.ITEM_ID AND IRP.ITEM_SET_ID = DERIVED.ITEM_SET_ID AND IRP.RESPONSE_SEQ_NUM = DERIVED.MAXSEQNUM AND IRP.TEST_ROSTER_ID = ?";
	private static String ITEM_RESPONSE_CR_FOR_ROSTER = "SELECT DISTINCT RESPONSE.ITEM_ID, POINTS FROM ITEM_RESPONSE_POINTS IRPS, (SELECT IRP.ITEM_ID, IRP.ITEM_RESPONSE_ID FROM ITEM_RESPONSE IRP, (SELECT ITEM_RESPONSE.ITEM_ID, ITEM_SET_ID, TEST_ROSTER_ID, MAX(RESPONSE_SEQ_NUM) MAXSEQNUM FROM ITEM_RESPONSE, ITEM WHERE TEST_ROSTER_ID = ? AND ITEM_RESPONSE.ITEM_ID = ITEM.ITEM_ID AND ITEM.ITEM_TYPE = 'CR' AND ITEM.ACTIVATION_STATUS = 'AC' GROUP BY ITEM_RESPONSE.ITEM_ID, ITEM_SET_ID, TEST_ROSTER_ID) DERIVED WHERE IRP.ITEM_ID = DERIVED.ITEM_ID AND IRP.ITEM_SET_ID = DERIVED.ITEM_SET_ID AND IRP.RESPONSE_SEQ_NUM = DERIVED.MAXSEQNUM AND IRP.TEST_ROSTER_ID = DERIVED.TEST_ROSTER_ID AND IRP.TEST_ROSTER_ID = ?) RESPONSE WHERE RESPONSE.ITEM_RESPONSE_ID = IRPS.ITEM_RESPONSE_ID";
	private static String STUDENT_SQL = "  select student0_.STUDENT_ID   as STUDENT_ID, student0_.FIRST_NAME   as FIRST_NAME,  student0_.LAST_NAME    as LAST_NAME,  student0_.MIDDLE_NAME  as MIDDLE_NAME,  student0_.BIRTHDATE    as BIRTHDATE,    decode(upper(student0_.GENDER), 'U', ' ', student0_.GENDER)   as GENDER,  student0_.GRADE  as GRADE0,   student0_.CUSTOMER_ID  as CUSTOMER_ID,   student0_.TEST_PURPOSE as TEST_PURPOSE,   student0_.EXT_PIN1  as EXT_PIN1  from student student0_   where student0_.STUDENT_ID = ? ";
	private static String MODIFIED_QUERY_TO_FETCH_ROSTER = " SELECT TEST_ROSTER_ID, " +
														" 		 ACTIVATION_STATUS, " +
														" 	  	 TEST_COMPLETION_STATUS, " +
														" 		 CUSTOMER_ID, " +
														" 		 STUDENT_ID, " +
														" 		 TEST_ADMIN_ID, " +
														" 		 PRODUCT_ID, " +
														" 		 COMPLETION_DATE" +
														"   FROM (SELECT DISTINCT ROS.TEST_ROSTER_ID         AS TEST_ROSTER_ID,"+
														"                 ROS.ACTIVATION_STATUS      AS ACTIVATION_STATUS,"+
														"                 ROS.TEST_COMPLETION_STATUS AS TEST_COMPLETION_STATUS,"+
														"                 ROS.CUSTOMER_ID            AS CUSTOMER_ID,"+
														"                 ROS.STUDENT_ID             AS STUDENT_ID,"+
														"                 ROS.TEST_ADMIN_ID          AS TEST_ADMIN_ID,"+
														"                 TA.PRODUCT_ID              AS PRODUCT_ID, " +
														"				  ROS.COMPLETION_DATE_TIME   AS COMPLETION_DATE "+
														"   FROM TEST_ROSTER ROS, TEST_ADMIN TA, PRODUCT PROD "+
														"  WHERE TA.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID "+
														"    AND TA.PRODUCT_ID = PROD.PRODUCT_ID "+
														"    AND PROD.PARENT_PRODUCT_ID = ? "+
														"    AND ROS.CUSTOMER_ID = ? "+
														"    AND ROS.ACTIVATION_STATUS = 'AC'"+
														"    AND ROS.TEST_COMPLETION_STATUS = 'CO'"+
													
														" UNION"+
													
														" SELECT DISTINCT ROS.TEST_ROSTER_ID         AS TEST_ROSTER_ID,"+
														"                 ROS.ACTIVATION_STATUS      AS ACTIVATION_STATUS,"+
														"                 ROS.TEST_COMPLETION_STATUS AS TEST_COMPLETION_STATUS,"+
														"                 ROS.CUSTOMER_ID            AS CUSTOMER_ID,"+
														"                 ROS.STUDENT_ID             AS STUDENT_ID,"+
														"                 ROS.TEST_ADMIN_ID          AS TEST_ADMIN_ID,"+
														"                 TA.PRODUCT_ID              AS PRODUCT_ID, " +
														"				  ROS.COMPLETION_DATE_TIME   AS COMPLETION_DATE "+
														"   FROM TEST_ROSTER             ROS,"+
														"        STUDENT_ITEM_SET_STATUS SIS,"+
														"        TEST_ADMIN              TA,"+
														"        PRODUCT                 PROD "+
														"  WHERE TA.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID "+
														"    AND TA.PRODUCT_ID = PROD.PRODUCT_ID "+
														"    AND PROD.PARENT_PRODUCT_ID = ? "+
														"    AND ROS.CUSTOMER_ID = ? "+
														"    AND ROS.ACTIVATION_STATUS = 'AC'"+
														"    AND ROS.TEST_COMPLETION_STATUS = 'IC'"+
														"    AND SIS.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID "+
														"    AND EXISTS (SELECT 1 "+
														"           FROM STUDENT_ITEM_SET_STATUS SISS "+
														"          WHERE SISS.COMPLETION_STATUS IN ('CO', 'IS')"+
														"            AND SISS.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID)"+
														"    AND NOT EXISTS"+
														"  (SELECT 1 "+
														"           FROM STUDENT_ITEM_SET_STATUS SI"+
														"          WHERE SI.COMPLETION_STATUS = 'IN'"+
														"            AND SI.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID)"+
													
														" UNION"+
													
														" SELECT DISTINCT ROS.TEST_ROSTER_ID         AS TEST_ROSTER_ID,"+
														"                 ROS.ACTIVATION_STATUS      AS ACTIVATION_STATUS,"+
														"                 ROS.TEST_COMPLETION_STATUS AS TEST_COMPLETION_STATUS,"+
														"                 ROS.CUSTOMER_ID            AS CUSTOMER_ID,"+
														"                 ROS.STUDENT_ID             AS STUDENT_ID,"+
														"                 ROS.TEST_ADMIN_ID          AS TEST_ADMIN_ID,"+
														"                 TA.PRODUCT_ID              AS PRODUCT_ID, " +
														" 				  ROS.COMPLETION_DATE_TIME   AS COMPLETION_DATE "+
														"   FROM TEST_ROSTER             ROS,"+
														"        STUDENT_ITEM_SET_STATUS SISS,"+
														"        TEST_ADMIN              TA,"+
														"        PRODUCT                 PROD "+
														"  WHERE TA.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID "+
														"    AND TA.PRODUCT_ID = PROD.PRODUCT_ID "+
														"    AND PROD.PARENT_PRODUCT_ID = ? "+
														"    AND ROS.CUSTOMER_ID = ? "+
														"    AND ROS.ACTIVATION_STATUS = 'AC'"+
														"    AND ROS.TEST_COMPLETION_STATUS = 'IS'"+
														"    AND SISS.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID "+
														"    AND SISS.COMPLETION_STATUS = 'CO') ROSTER " +
														"  WHERE ROSTER.TEST_ROSTER_ID IS NOT NULL";
	
	private static final DateFormat fileDateOutputFormat = new SimpleDateFormat("MMddyyHHmm");
	
	private static final String group = "GOAS";
	private static final String DATAFILE = "DATAFILE";
	private String customerModelLevelValue = null;
	private String customerState = null;
	private String customerCity = null;
	private String testDate = null;
	private String programDate = null;
	private int districtElementNumber = 0;
	private int schoolElementNumber = 0;
	private int classElementNumber = 0;
	private int sectionElementNumber = 0;
	private int groupElementNumber = 0;
	private int divisionElementNumber = 0;
	private int levelElementNumber = 0;
	private int studentElementNumber = 0;
	private boolean isInvalidSpeaking = false;
	private boolean isInvalidListeing = false;
	private boolean isInvalidReading = false;
	private boolean isInvalidWriting = false;
	public boolean isValidStartDate = false;
	public boolean isValidEndDate = false;
	private HashMap<String, String> subSkillAreaScoreInfo = new HashMap<String, String>();
	private HashMap<String, String> subSkillAreaItemCategory = new HashMap<String, String>();
	static TreeMap<String, String> wrongMap = new TreeMap<String, String>();
	private Integer customerId = new Integer(ExtractUtil.getDetail("oas.customerId"));
	private Integer frameProductId = new Integer(ExtractUtil.getDetail("oas.frameworkProductId"));
	private String extractSpanStartDate = new String(ExtractUtil.getDetail("oas.extractSpanStartDate"));
	private String extractSpanEndDate = new String(ExtractUtil.getDetail("oas.extractSpanEndDate"));;
	private String MFid = new String(ExtractUtil.getDetail("oas.mainFrameId"));
	static {
		wrongMap.put("A", "1");
		wrongMap.put("B", "2");
		wrongMap.put("C", "3");
		wrongMap.put("D", "4");
	}
	
	public void writeToText() throws IOException, FFPojoException, SQLException,Exception {

		OrderFile orderFile = new OrderFile();
		System.out.println("Collecting data for report....");
		List<TfilLL2ND> myList = createList(orderFile);
		if(null == myList){
			System.err.println("No roster found to extract.\nExecution forcefully stoped.");
			System.exit(1);
		}
		System.out.println("Data collecting process complete.");
		String localFilePath = ExtractUtil.getDetail("oas.exportdata.filepath");
		System.out.println("LocalFilePath :: "+localFilePath);
		String fileName = customerState + "_" + testDate.substring(0,6) + "_" 
		+ orderFile.getCustomerId() + "_" 
		//+ orderFile.getOrgTestingProgram() + "_"
		+ MFid.trim() + "_"
		+ orderFile.getCustomerName().trim() + "_"
		+ group + "_"
		+ DATAFILE + "_" + fileDateOutputFormat.format(new Date())
		+ ".dat";
		if(!(new File(localFilePath)).exists()){
			File f = new File(localFilePath);
			f.mkdirs();
		}
		File file = new File(localFilePath, fileName);
		FlatFileWriter ffWriter = null;
		try{
			ffWriter = new FileSystemFlatFileWriter(file, true);
			ffWriter.writeRecordList(myList);
			ffWriter.close();
			System.out.println("Export file successfully generated:["+fileName+"]");
			orderFile.setDataFileName(EmetricUtil.truncate(fileName,
					new Integer(100)).substring(0, fileName.length()));
			System.out.println("Completed Writing");
			System.out.println("Preparing Order File");
			prepareOrderFile(orderFile, localFilePath, fileName);
		} finally {
			if(ffWriter!=null){
				ffWriter.close();
			}
		}
		/* SFTP the generated data file: Start */
		Configuration config = new Configuration();
		String destinationDir = config.getFtpFilepath();
		Session session = null;
		try{
			session = SftpUtil.getSFtpSession(config);
			SftpUtil.doSftp(session, destinationDir, localFilePath, fileName);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SftpUtil.closeSFtpClient(session);
		}
		/* SFTP the generated data file: End */		

	}
	
	public List<TfilLL2ND> createList(OrderFile orderFile) throws Exception {
		
		List<TestRoster> myrosterList = new ArrayList<TestRoster>();
		List<CustomerDemographic> customerAccomList = new ArrayList<CustomerDemographic>();
		List<CustomerDemographic> customerDemoList = new ArrayList<CustomerDemographic>();
		List<TfilLL2ND> tfilList = new ArrayList<TfilLL2ND>();
		HashMap<String, Integer> districtMap = new HashMap<String, Integer>();
		HashMap<String, Integer> schoolMap = new HashMap<String, Integer>();
		HashMap<String, Integer> classMap = new HashMap<String, Integer>();
		HashMap<String, Integer> sectionMap = new HashMap<String, Integer>();
		HashMap<String, Integer> groupMap = new HashMap<String, Integer>();
		HashMap<String, Integer> divisionMap = new HashMap<String, Integer>();
		HashMap<String, Integer> levelMap = new HashMap<String, Integer>();
		HashMap<Integer, String> customerDemographic = new HashMap<Integer, String>();
		Integer studentCount = 0;
		Connection oascon = null;
		Connection irscon = null;
		
		try{
			System.out.println("Processing started For Laslink Second Edition...");
			oascon = SqlUtil.openOASDBconnectionForResearch();
			irscon = SqlUtil.openIRSDBconnectionForResearch();
			myrosterList = getTestRoster(oascon);
			if(myrosterList.size() == 0){
				return null;
			}
			generateModelLevel(oascon);
			customerDemoList = getCustomerDemographic(oascon);
			Set<CustomerDemographic> set = new HashSet<CustomerDemographic>(customerDemoList);
			customerAccomList = getCustomerLeveledDemographicValue(oascon);
			Set<CustomerDemographic> setAccomodation = new HashSet<CustomerDemographic>(customerAccomList);

			for (CustomerDemographic c : set) {
				customerDemographic.put(c.getCustomerDemographicId(), c
						.getLabelName());
			}
			populateCustomer(oascon, orderFile);
			for (TestRoster roster : myrosterList) {
				System.out.println("Processing started for roster:"+roster.getTestRosterId());
				TreeMap<String, Object[]> invalidSubtestMap = new TreeMap<String, Object[]>();
				TfilLL2ND tfil = new TfilLL2ND();
				String isClassNodeRequiredDummyName = "";
				isClassNodeRequiredDummyName = Configuration.getIsClassNodeRequiredDummyName();
				if(null != isClassNodeRequiredDummyName && !"".equalsIgnoreCase(isClassNodeRequiredDummyName)
						&& Integer.valueOf(this.customerModelLevelValue).intValue() < 7){
					int value = Integer.valueOf(this.customerModelLevelValue).intValue();
					value++;
					tfil.setModelLevel(new Integer(value).toString());
				}
				else{
					tfil.setModelLevel(this.customerModelLevelValue);
				}
				tfil.setState(this.customerState);
				tfil.setCity(this.customerCity);
				
				//tfil.setTestRosterId(String.valueOf(roster.getTestRosterId())); //Needed this field for unit testing
				
				//Student Information
				Student st = roster.getStudent();
				setStudentList(tfil, st);
				
				// Organization
				createOrganization(oascon, tfil, roster.getStudentId(),
						districtMap, schoolMap, classMap, sectionMap, groupMap, divisionMap, levelMap, orderFile);
				
				// Test Session Details
				createTestSessionDetails(oascon, tfil, roster.getTestRosterId(), orderFile);
				
				//Accommodation
				createAccomodations(st.getStudentDemographic(), setAccomodation, customerDemographic, tfil , roster);
				
				//Invalidation Status
				createStudentItemStatusDetails(oascon, tfil, roster.getTestRosterId(), roster.getStudentId(), invalidSubtestMap);
				
				//Skill Area Information : Scale Score, Proficiency Level, NCE, PR, Point Obtained, Percent Obtained, Lexile
				createSkillAreaScoreInformation(irscon, tfil, roster, invalidSubtestMap);
				
				//Sub Skill Area Information 
				createSubSkillAreaScoreInformation(oascon, irscon, tfil, roster);

				//Item response for a roster
				createItemResponseInformation(oascon, roster, tfil);
				
				//Sub test Indicator (Commented as Not Applicable for mainframe)
				//createSubtestIndicatorFlag(oascon, roster.getTestRosterId(), tfil);

				tfilList.add(tfil);
				studentCount++;
			}
			orderFile.setCaseCount(studentCount.toString());
			
		} finally {
			SqlUtil.close(oascon);
			SqlUtil.close(irscon);
		}
		
		return tfilList;
	}
	
	private void prepareOrderFile(OrderFile orderFile, String filedir,
			String fileName) throws IOException {

		String orderFileName = fileName.substring(0, fileName.length() - 23);

		orderFileName = orderFileName + "ORDERFILE_"
		+ fileDateOutputFormat.format(new Date()) + ".csv";
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(filedir, orderFileName));

			writer.append("CUST_ID");
			writer.append(',');
			writer.append("CUST_NAME");
			writer.append(',');
			writer.append("ST");
			writer.append(',');
			writer.append("ORG_TP");
			writer.append(',');
			writer.append("SO");
			writer.append(',');
			writer.append("TAG");
			writer.append(',');
			writer.append("TEST_NAME");
			writer.append(',');
			writer.append("TEST2_NAME");
			writer.append(',');
			writer.append("TEST3_NAME");
			writer.append(',');
			writer.append("TESTDATE");
			writer.append(',');
			writer.append("CASES");
			writer.append(',');
			writer.append("RE-RUN");
			writer.append(',');
			writer.append("LONGITUDINAL");
			writer.append(',');
			writer.append("RE_ROSTER");
			writer.append(',');
			writer.append("DATA_FILE_NAME");
			writer.append(',');
			writer.append("CUSTOMER_CONTACT");
			writer.append(',');
			writer.append("CUSTOMER_CONTACT_EMAIL");
			writer.append(',');
			writer.append("CUSTOMER_CONTACT_PHONE");
			writer.append(',');
			writer.append("TB");
			writer.append(',');
			writer.append("Hierarchy Mode location");
			writer.append(',');
			writer.append("Special code select");
			writer.append(',');
			writer.append("Expected Titles");
			writer.append(',');
			writer.append("Hierarchy Mode location");
			writer.append(',');
			writer.append("Special code select");
			writer.append(',');
			writer.append("Expected Titles");
			writer.append(',');
			writer.append("SUBMITTER_EMAIL");
			writer.append('\n');

			writer.append(orderFile.getCustomerId().toString());
			writer.append(',');
			writer.append(orderFile.getCustomerName());
			writer.append(',');
			writer.append(orderFile.getCustomerStateAbbrevation());
			writer.append(',');
			writer.append(orderFile.getOrgTestingProgram());
			writer.append(',');
			writer.append(orderFile.getScoringOrderNumber());
			writer.append(',');
			writer.append(orderFile.getTagNumber());
			writer.append(',');
			writer.append(orderFile.getTestName1());
			writer.append(',');
			writer.append(orderFile.getTestName2());
			writer.append(',');
			writer.append(orderFile.getTestName3());
			writer.append(',');
			writer.append(orderFile.getTestDate());
			writer.append(',');
			writer.append(orderFile.getCaseCount());
			writer.append(',');
			writer.append(orderFile.getReRunFlag());
			writer.append(',');
			writer.append(orderFile.getLongitudinalFlag());
			writer.append(',');
			writer.append(orderFile.getReRosterFlag());
			writer.append(',');
			writer.append(orderFile.getDataFileName());
			writer.append(',');
			writer.append(orderFile.getCustomerContact());
			writer.append(',');
			writer.append(orderFile.getCustomerEmail());
			writer.append(',');
			writer.append(orderFile.getCustomerPhone());
			
			writer.append(',');
			writer.append(orderFile.getTB());
			writer.append(',');
			writer.append(orderFile.getHierarchyModeLocation());
			writer.append(',');
			writer.append(orderFile.getSpecialCodeSelect());
			writer.append(',');
			writer.append(orderFile.getExpectedTitles());
			writer.append(',');
			writer.append(orderFile.getHierarchyModeLocation2());
			writer.append(',');
			writer.append(orderFile.getSpecialCodeSelect2());
			writer.append(',');
			writer.append(orderFile.getExpectedTitles2());
			writer.append(',');
			writer.append(orderFile.getSubmittersEmail());
			writer.append('\n');

			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err.println("Error in Preparing OrderFile");
			e.printStackTrace();
			throw e;

		} finally {
			if(writer!=null){
				writer.close();
			}
		}
		System.out.println("Order file successfully generated:["+orderFileName+"]");
		/* SFTP the generated order file: Start */
		Configuration config = new Configuration();
		String destinationDir = config.getFtpFilepath();
		Session session = null;
		try{
			session = SftpUtil.getSFtpSession(config);
			SftpUtil.doSftp(session, destinationDir, filedir, orderFileName);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SftpUtil.closeSFtpClient(session);
		}
		/* SFTP the generated order file: End */
	}

	
	private void createStudentItemStatusDetails(Connection oascon,
			TfilLL2ND tfil, Integer testRosterId, Integer studentId, TreeMap<String, Object[]> invalidationMap) throws Exception{

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = oascon.prepareStatement(TEST_ROSTER_DETAILS_SQL);
			ps.setInt(1, studentId);
			ps.setInt(2, testRosterId);
			rs = ps.executeQuery();
			while (rs.next()) {
				Object[] val = new Object[3];
				if (rs.getString(5).equalsIgnoreCase("Speaking")) {
					tfil.setTestInvalidationSpeaking(rs.getString(1)
							.equalsIgnoreCase("IN") ? "1" : " ");
					val[0] = (rs.getString(1).equalsIgnoreCase("IN")) ? "1" : " ";
					val[1] = (rs.getString(3).equalsIgnoreCase("Y")) ? "1" : " ";
					val[2] = (rs.getString(4).equalsIgnoreCase("Y")) ? "1" : " ";
				} else if (rs.getString(5).toString().equalsIgnoreCase("Listening")) {
					tfil.setTestInvalidationListening(rs.getString(1)
							.toString().equalsIgnoreCase("IN") ? "1" : " ");
					val[0] = (rs.getString(1).equalsIgnoreCase("IN")) ? "1" : " ";
					val[1] = (rs.getString(3).equalsIgnoreCase("Y")) ? "1" : " ";
					val[2] = (rs.getString(4).equalsIgnoreCase("Y")) ? "1" : " ";
				} else if (rs.getString(5).toString().equalsIgnoreCase("Reading")) {
					tfil.setTestInvalidationReading(rs.getString(1).toString()
							.equalsIgnoreCase("IN") ? "1" : " ");
					val[0] = (rs.getString(1).equalsIgnoreCase("IN")) ? "1" : " ";
					val[1] = (rs.getString(3).equalsIgnoreCase("Y")) ? "1" : " ";
					val[2] = (rs.getString(4).equalsIgnoreCase("Y")) ? "1" : " ";
				} else if (rs.getString(5).toString().equalsIgnoreCase("Writing")) {
					tfil.setTestInvalidationWriting(rs.getString(1).toString()
							.equalsIgnoreCase("IN") ? "1" : " ");
					val[0] = (rs.getString(1).equalsIgnoreCase("IN")) ? "1" : " ";
					val[1] = (rs.getString(3).equalsIgnoreCase("Y")) ? "1" : " ";
					val[2] = (rs.getString(4).equalsIgnoreCase("Y")) ? "1" : " ";
				}
				invalidationMap.put(rs.getString(5).toString().toLowerCase(), val);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at createStudentItemStatusDetails:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

	}

	private void createTestSessionDetails(Connection oascon, TfilLL2ND tfil,
			Integer testRosterId, OrderFile orderFile) throws Exception {

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = oascon.prepareStatement(TEST_SESSION_SQL);
			ps.setInt(1, testRosterId);
			rs = ps.executeQuery();
			while (rs.next()) {

				if ("C".equalsIgnoreCase(rs.getString(1))) {
					tfil.setTestName("LAS Links 2nd Edition");
					tfil.setTestForm("C");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								"LASLinks2", new Integer(11))
								.toUpperCase());
				} else if ("D".equalsIgnoreCase(rs.getString(1))) {
					tfil.setTestName("LAS Links 2nd Edition");
					tfil.setTestForm("D");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								"LASLinks2", new Integer(11))
								.toUpperCase());
				} else if ("ESP B".equalsIgnoreCase(rs.getString(1)) || 
						"Español B".equalsIgnoreCase(rs.getString(1))) {
					tfil.setTestName("LAS Links 2nd Edition Español");
					tfil.setTestForm("T");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								"LASLinks2", new Integer(11))
								.toUpperCase());
				}

				if (rs.getString(2) != null
						&& rs.getString(2).toString().equals("K")) {
					tfil.setTestLevel("1");
				} else if (rs.getString(2) != null
						&& rs.getString(2).toString().equals("1")) {
					tfil.setTestLevel("1");
				} else if (rs.getString(2) != null
						&& rs.getString(2).toString().equals("2-3")) {
					tfil.setTestLevel("2");
				} else if (rs.getString(2) != null
						&& rs.getString(2).toString().equals("4-5")) {
					tfil.setTestLevel("3");
				} else if (rs.getString(2) != null
						&& rs.getString(2).toString().equals("6-8")) {
					tfil.setTestLevel("4");
				} else if (rs.getString(2) != null
						&& rs.getString(2).toString().equals("9-12")) {
					tfil.setTestLevel("5");
				}
				if(rs.getString(4) != null){
					tfil.setTestDate(EmetricUtil.getTimeZone(rs.getString(4).toString(),rs.getString(3).toString(),true));
				}
					this.testDate = tfil.getTestDate();
					
				if(rs.getString(5) != null && this.programDate == null){
					this.programDate = EmetricUtil.getTimeZone(rs.getString(5).toString(),rs.getString(3).toString(),true);
				}
			}

			if (orderFile.getTestDate() == null && this.programDate != null){
				orderFile.setTestDate(this.programDate.substring(0, 6));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at createTestSessionDetails:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

	}

	private void setStudentList(TfilLL2ND tfil, Student st) {
		
		HashMap<Integer, Integer> studentMap = new HashMap<Integer, Integer>();
		Integer integer = studentMap.get(st.getStudentId());
		if (integer == null) {
			integer = ++studentElementNumber;
			studentMap.put(st.getStudentId(), integer);
		}
		tfil.setStudentElementNumber(studentMap.get(st.getStudentId()).toString());
		tfil.setStudentLastName(st.getLastName());
		tfil.setStudentFirstName(st.getFirstName());
		tfil.setStudentGender(st.getGender());
		tfil.setStudentMiddleName(st.getMiddleName());
		tfil.setExtStudentId(st.getExtStudentId());
		tfil.setStudentGradeFromAnswerSheets(EmetricUtil.formatGrade(st.getGrade()));
		tfil.setGrade(EmetricUtil.formatGrade(st.getGrade()));
		tfil.setStudentBirthDate(st.getBirthDate());
		tfil.setStudentChronologicalAge(EmetricUtil.calculateAge(st.getBirthDate()));
	}

	private void generateModelLevel(Connection conn) throws Exception  {

		String modelLevel = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(CUSTOMER_MODEL_LEVEL);
			ps.setInt(1, customerId);
			rs = ps.executeQuery();
			if (rs.next()) {
				modelLevel = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at generateModelLevel:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		this.customerModelLevelValue = modelLevel;
	}
	
	private List<CustomerDemographic> getCustomerDemographic(Connection con) throws Exception {
		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(CUSTOMER_DEMOGRAPHIC_SQL);
			ps.setInt(1, this.customerId);
			rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDemographic cd = new CustomerDemographic();
				cd.setCustomerDemographicId(rs.getInt(1));
				cd.setCustomerId(rs.getInt(2));
				cd.setLabelName(rs.getString(3));
				cd.setCustomerDemographicValue(getCustomerDemographicValue(con,
						rs.getInt(1)));
				myList.add(cd);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getCustomerDemographic:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return myList;
	}

	private Set<CustomerDemographicValue> getCustomerDemographicValue(
			Connection con, int customerDemographicId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Set<CustomerDemographicValue> customerDemographicValue = new HashSet<CustomerDemographicValue>();
		try {
			ps = con.prepareStatement(CUSTOMER_DEMOGRAPHIC_VALUE_SQL);
			ps.setInt(1, customerDemographicId);
			rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDemographicValue cdv = new CustomerDemographicValue();
				cdv.setValueName(rs.getString(1));
				cdv.setValueCode(rs.getString(2));
				cdv.setCustomerDemographicId(rs.getInt(3));
				customerDemographicValue.add(cdv);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getCustomerDemographicValue:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return customerDemographicValue;
	}
	
	private List<CustomerDemographic> getCustomerLeveledDemographicValue(
			Connection con) throws Exception {

		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(CUSTOMER_DEMOGRAPHIC_WITH_LEVEL_SQL);
			ps.setInt(1, customerId);
			rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDemographic cd = new CustomerDemographic();

				cd.setCustomerDemographicId(rs.getInt(1));
				cd.setCustomerId(rs.getInt(2));
				cd.setLabelName(rs.getString(3));
				cd.setCustomerDemographicValue(getCustomerDemographicValue(con,
						rs.getInt(1)));
				myList.add(cd);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getCustomerLeveledDemographicValue:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);

		}

		return myList;

	}
	
	private void populateCustomer(Connection con, OrderFile orderFile) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(CUSTOMER_SQL);
			ps.setInt(1, customerId);
			rs = ps.executeQuery();
			while (rs.next()) {
				this.customerState = rs.getString(1);
				this.customerCity = rs.getString(2);
				orderFile.setCustomerStateAbbrevation(rs.getString(1));
				orderFile.setCustomerEmail(EmetricUtil.truncate(
						rs.getString(3), new Integer(64)));
				orderFile.setCustomerPhone(EmetricUtil.truncate(EmetricUtil
						.convertPhoneNumber(rs.getString(4)), new Integer(21)));
				orderFile.setCustomerContact(EmetricUtil.truncate(rs
						.getString(5), new Integer(64)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at populateCustomer:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
	}
	
	private List<TestRoster> getTestRoster(Connection con) throws Exception {
		PreparedStatement ps = null ;
		ResultSet rs = null;
		 List<TestRoster> rosterList = new ArrayList<TestRoster>();
		try{
			if(this.isValidStartDate) {
				String START_DATE = this.extractSpanStartDate.replaceAll("/", "").toString().trim();
				MODIFIED_QUERY_TO_FETCH_ROSTER += " AND TRUNC(ROSTER.COMPLETION_DATE) >= TO_DATE('" + START_DATE + "', 'MMDDYYYY')";
			}
			if(this.isValidEndDate) {
				String END_DATE = this.extractSpanEndDate.replaceAll("/", "").toString().trim();
				MODIFIED_QUERY_TO_FETCH_ROSTER += " AND TRUNC(ROSTER.COMPLETION_DATE) <= TO_DATE('" + END_DATE + "', 'MMDDYYYY')";
			}
			ps = con.prepareStatement(MODIFIED_QUERY_TO_FETCH_ROSTER);
			ps.setInt(1, frameProductId);
			ps.setInt(2, customerId);
			ps.setInt(3, frameProductId);
			ps.setInt(4, customerId);
			ps.setInt(5, frameProductId);
			ps.setInt(6, customerId);
			rs = ps.executeQuery(); 
			while (rs.next()){
				TestRoster ros = new TestRoster();
				ros.setTestRosterId(rs.getInt(1));
				ros.setActivationStatus(rs.getString(2));
				ros.setTestCompletionStatus(rs.getString(3));
				ros.setCustomerId(customerId);
				ros.setStudentId(rs.getInt(5));
				ros.setTestAdminId(rs.getInt(6));
				ros.setProductId(rs.getInt(7));
				ros.setStudent(getStudent(con,rs.getInt(5))); 
				rosterList.add(ros);
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getTestRoster:"+e.getMessage());
			
		}finally {
			SqlUtil.close(ps, rs);
		}

		return rosterList;
	}
	
	private Set<StudentContact> getStudentContact(Connection con, int studentId) throws Exception {
		Set<StudentContact> studentContact = new HashSet<StudentContact>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(STUDENT_CONTACT_SQL);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			while (rs.next()) {
				StudentContact sdentCon = new StudentContact();
				sdentCon.setStudentContactId(rs.getInt(2));
				sdentCon.setCity(rs.getString(3));
				sdentCon.setState(rs.getString(4));
				studentContact.add(sdentCon);

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getStudentContact:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return studentContact;
	}
	
	private Set<StudentDemographic> getStudentDemographic(Connection con,
			int studentId) throws Exception {
		Set<StudentDemographic> studentDemographicSet = new HashSet<StudentDemographic>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(STUDENT_DEMOGRAPHIC_SQL);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			while (rs.next()) {
				StudentDemographic studentDemographic = new StudentDemographic();
				studentDemographic.setStudentDemographicId(rs.getInt(1));
				studentDemographic.setStudentId(studentId);
				studentDemographic.setCustomerDemographicId(rs.getInt(2));
				studentDemographic.setValueName(rs.getString(3));
				studentDemographic.setValue(rs.getString(4));
				studentDemographicSet.add(studentDemographic);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getStudentDemographic:"+e.getMessage());
			
		} finally {
			SqlUtil.close(ps, rs);
		}

		return studentDemographicSet;
	}
	
	private void createOrganization(Connection con, TfilLL2ND tfil,
			Integer studentId, HashMap<String, Integer> districtMap,
			HashMap<String, Integer> schoolMap,
			HashMap<String, Integer> classMap,
			HashMap<String, Integer> sectionMap,
			HashMap<String, Integer> groupMap,
			HashMap<String, Integer> divisionMap,
			HashMap<String, Integer> levelMap, OrderFile orderFile) throws Exception {

		TreeMap<Integer, String> organizationMap = new TreeMap<Integer, String>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(ORGANIZATION_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString(4).equalsIgnoreCase("root")
						|| rs.getString(4).equalsIgnoreCase("CTB")) {
					// do nothing
				} else {
					organizationMap.put(new Integer(rs.getString(5)), rs
							.getString(4));
				}

			}
			Integer organizationMapSize = organizationMap.size();
			rs.beforeFirst();

			while (rs.next()) {
				if (rs.getString(4).equalsIgnoreCase("root")
						|| rs.getString(4).equalsIgnoreCase("CTB")) {
					// do nothing
				} else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 6).toString() != null
						&& rs.getString(5)
						.equalsIgnoreCase(
								new Integer(organizationMapSize - 6)
								.toString())) {
					tfil.setElementNameA(rs.getString(4).toString());
					tfil.setElementLabelA(rs.getString(2));
					Integer integer = districtMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++districtElementNumber;
						districtMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberA(String.valueOf(districtMap.get(rs
							.getString(4))));
					tfil.setElementSpecialCodesA(rs.getString(3));
					tfil.setMdrNumA(rs.getString(1));
					//tfil.setOrganizationId("XX" + rs.getString(1));
					tfil.setElementIdA(rs.getString(1));
					tfil.setElementStructureLevelA("01");
					if (orderFile.getCustomerId() == null)
						orderFile.setCustomerId(rs.getString(1));
	
				} else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 5).toString() != null
						&& rs.getString(5).toString()
						.equalsIgnoreCase(
								Integer.valueOf(organizationMapSize - 5)
								.toString())) {
					tfil.setElementNameB(rs.getString(4));
					tfil.setElementLabelB(rs.getString(2));
					Integer integer = schoolMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++schoolElementNumber;
						schoolMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberB(String.valueOf(schoolMap.get(rs
							.getString(4))));
					tfil.setElementSpecialCodesB(rs.getString(3));
					tfil.setMdrNumB(rs.getString(1));
					tfil.setElementStructureLevelB("02");
					tfil.setElementIdB(rs.getString(1));
				}
	
				else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 4).toString() != null
						&& rs.getString(5)
						.equalsIgnoreCase(
								new Integer(organizationMapSize - 4)
								.toString())) {
					tfil.setElementNameC(rs.getString(4));
					tfil.setElementLabelC(rs.getString(2));
					Integer integer = classMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++classElementNumber;
						classMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberC(String.valueOf(classMap.get(rs
							.getString(4))));
					tfil.setElementSpecialCodesC(rs.getString(3));
					tfil.setMdrNumC(rs.getString(1));
					tfil.setElementStructureLevelC("03");
					tfil.setElementIdC(rs.getString(1));
	
				}
				else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 3).toString() != null
						&& rs.getString(5)
						.equalsIgnoreCase(
								new Integer(organizationMapSize - 3)
								.toString())) {
					tfil.setElementNameD(rs.getString(4));
					tfil.setElementLabelD(rs.getString(2));
					Integer integer = sectionMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++sectionElementNumber;
						sectionMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberD(String.valueOf(sectionMap.get(rs
							.getString(4))));
					tfil.setElementSpecialCodesD(rs.getString(3));
					tfil.setMdrNumD(rs.getString(1));
					tfil.setElementStructureLevelD("04");
					tfil.setElementIdD(rs.getString(1));
	
				}
				else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 2).toString() != null
						&& rs.getString(5)
						.equalsIgnoreCase(
								new Integer(organizationMapSize - 2)
								.toString())) {
					tfil.setElementNameE(rs.getString(4));
					tfil.setElementLabelE(rs.getString(2));
					Integer integer = groupMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++groupElementNumber;
						groupMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberE(String.valueOf(groupMap.get(rs
							.getString(4))));
					tfil.setElementSpecialCodesE(rs.getString(3));
					tfil.setMdrNumE(rs.getString(1));
					tfil.setElementStructureLevelE("05");
					tfil.setElementIdE(rs.getString(1));
	
				}
				else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 1).toString() != null
						&& rs.getString(5).equalsIgnoreCase(
								new Integer(organizationMapSize - 1)
								.toString())) {
					tfil.setElementNameF(rs.getString(4));
					tfil.setElementLabelF(rs.getString(2));
					Integer integer = divisionMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++divisionElementNumber;
						divisionMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberF(String.valueOf(divisionMap.get(rs
							.getString(4))));
					tfil.setElementSpecialCodesF(rs.getString(3));
					tfil.setMdrNumF(rs.getString(1));
					tfil.setElementStructureLevelF("06");
					tfil.setElementIdF(rs.getString(1));
	
				}
				else if (rs.getString(5) != null
						&& rs.getString(5).equalsIgnoreCase(
								organizationMapSize.toString())) {
					tfil.setElementNameG(rs.getString(4));
					tfil.setElementLabelG(rs.getString(2));
					Integer integer = levelMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++levelElementNumber;
						levelMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberG(String.valueOf(levelMap.get(rs
							.getString(4))));
					tfil.setElementSpecialCodesG(rs.getString(3));
					tfil.setMdrNumG(rs.getString(1));
					tfil.setElementStructureLevelG("07");
					tfil.setElementIdG(rs.getString(1));
				}
		}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at createOrganization:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		if (tfil.getElementNameF() == null) {
			//tfil.setOrganizationId("XX" + tfil.getElementIdG());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getElementIdG());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameG(), 30));
			
		}else if (tfil.getElementNameE() == null) {
			//tfil.setOrganizationId("XX" + tfil.getElementIdF());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getElementIdF());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameF(), 30));
			
		}else if (tfil.getElementNameD() == null) {
			//tfil.setOrganizationId("XX" + tfil.getElementIdE());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getElementIdE());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameE(), 30));
			
		}else if (tfil.getElementNameC() == null) {
			//tfil.setOrganizationId("XX" + tfil.getElementIdD());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getElementIdD());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameD(), 30));
			
		}else if (tfil.getElementNameB() == null) {
			//tfil.setOrganizationId("XX" + tfil.getElementIdC());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getElementIdC());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameC(), 30));
			
		}else if (tfil.getElementNameA() == null) {
			//tfil.setOrganizationId("XX" + tfil.getElementIdB());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getElementIdB());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameB(), 30));
			
		}
		
		if (null != MFid)
			tfil.setOrganizationId(MFid.trim()); //set mainFrameId as OrganizationId
		if (orderFile.getOrgTestingProgram() == null)
			orderFile.setOrgTestingProgram(tfil.getOrganizationId());
		if (orderFile.getCustomerName() == null)
			orderFile.setCustomerName(EmetricUtil.truncate(tfil
					.getElementNameA(), 30));

		
		/* Added for Mainframe-OAS Blended Reporting : Add class level node based on flag isClassNodeRequired */
		//start ::
		String isClassNodeRequiredDummyName = "";
		isClassNodeRequiredDummyName = Configuration.getIsClassNodeRequiredDummyName();
		//System.out.println("isClassNodeRequired >> "+isClassNodeRequired);
		if(null != isClassNodeRequiredDummyName && !"".equalsIgnoreCase(isClassNodeRequiredDummyName) && organizationMap.size() < 7){
			/* shift each element to its immediate upper level... */
			if (tfil.getElementNameB() != null){
				tfil.setElementNameA(tfil.getElementNameB());
				tfil.setElementLabelA(tfil.getElementLabelB());
				tfil.setElementNumberA(tfil.getElementNumberB());
				tfil.setElementSpecialCodesA(tfil.getElementSpecialCodesB());
				tfil.setMdrNumA(tfil.getMdrNumB());
				tfil.setElementIdA(tfil.getElementIdB());
				tfil.setElementStructureLevelA("01");
				//orderFile.setCustomerId(tfil.getSchoolId());
			}
			if (tfil.getElementNameC() != null){
				tfil.setElementNameB(tfil.getElementNameC());
				tfil.setElementLabelB(tfil.getElementLabelC());
				tfil.setElementNumberB(tfil.getElementNumberC());
				tfil.setElementSpecialCodesB(tfil.getElementSpecialCodesC());
				tfil.setMdrNumB(tfil.getMdrNumC());
				tfil.setElementIdB(tfil.getElementIdC());
				//tfil.setCustomerId(tfil.getClassId());
				tfil.setElementStructureLevelB("02");
				//orderFile.setCustomerId(tfil.getClassId());
			}
			if (tfil.getElementNameD() != null){
				tfil.setElementNameC(tfil.getElementNameD());
				tfil.setElementLabelC(tfil.getElementLabelD());
				tfil.setElementNumberC(tfil.getElementNumberD());
				tfil.setElementSpecialCodesC(tfil.getElementSpecialCodesD());
				tfil.setMdrNumC(tfil.getMdrNumD());
				tfil.setElementIdC(tfil.getElementIdD());
				//tfil.setCustomerId(tfil.getSectionId());
				tfil.setElementStructureLevelC("03");
				//orderFile.setCustomerId(tfil.getSectionId());
			}
			if (tfil.getElementNameE() != null){
				tfil.setElementNameD(tfil.getElementNameE());
				tfil.setElementLabelD(tfil.getElementLabelE());
				tfil.setElementNumberD(tfil.getElementNumberE());
				tfil.setElementSpecialCodesD(tfil.getElementSpecialCodesE());
				tfil.setMdrNumD(tfil.getMdrNumE());
				tfil.setElementIdD(tfil.getElementIdE());
				//tfil.setCustomerId(tfil.getGroupId());
				tfil.setElementStructureLevelD("04");
				//orderFile.setCustomerId(tfil.getGroupId());
			}
			if (tfil.getElementNameF() != null){
				tfil.setElementNameE(tfil.getElementNameF());
				tfil.setElementLabelE(tfil.getElementLabelF());
				tfil.setElementNumberE(tfil.getElementNumberF());
				tfil.setElementSpecialCodesE(tfil.getElementSpecialCodesF());
				tfil.setMdrNumE(tfil.getMdrNumF());
				tfil.setElementIdE(tfil.getElementIdF());
				//tfil.setCustomerId(tfil.getDivisionId());
				tfil.setElementStructureLevelE("05");
				//orderFile.setCustomerId(tfil.getDivisionId());
			}
			if (tfil.getElementNameG() != null){
				tfil.setElementNameF(tfil.getElementNameG());
				tfil.setElementLabelF(tfil.getElementLabelG());
				tfil.setElementNumberF(tfil.getElementNumberG());
				tfil.setElementSpecialCodesF(tfil.getElementSpecialCodesG());
				tfil.setMdrNumF(tfil.getMdrNumG());
				tfil.setElementIdF(tfil.getElementIdG());
				//tfil.setCustomerId(tfil.getLeafLevelId());
				tfil.setElementStructureLevelF("06");
				//orderFile.setCustomerId(tfil.getLeafLevelId());
				
				/* push the dummy class node at leaf level... */
				tfil.setElementNameG(isClassNodeRequiredDummyName);
				tfil.setElementLabelG("Class");
				//tfil.setElementNumberG(tfil.getElementNumberG());
				tfil.setElementNumberG("");// blank for now
				tfil.setElementSpecialCodesG("");
				tfil.setMdrNumG("");
				tfil.setElementIdG("");//blank for now
				//tfil.setCustomerId(tfil.getLeafLevelId());
				tfil.setElementStructureLevelG("07");
				//orderFile.setCustomerId(tfil.getLeafLevelId());
			}
		}
		//end ::
	}

	public void createAccomodations(Set<StudentDemographic> sd,
			Set<CustomerDemographic> cd,
			HashMap<Integer, String> customerDemographic, TfilLL2ND tfil, TestRoster roster) {

		TreeMap<String, StudentDemographic> set1 = new TreeMap<String, StudentDemographic>();
		TreeMap<String, CustomerDemographic> set2 = new TreeMap<String, CustomerDemographic>();
		HashMap<Integer, String> studentDemographic = new HashMap<Integer, String>();
		SpecialCodes specialCodes = new SpecialCodes();

		for (StudentDemographic studentDem : sd) {
			if (studentDem.getValue() != null){
				set1.put(studentDem.getValue(), studentDem);
			}
			studentDemographic.put(studentDem.getCustomerDemographicId(), studentDem.getValueName());
			if (customerDemographic.containsKey(studentDem.getCustomerDemographicId())) {
				
				String customerDemoName = customerDemographic.get(studentDem.getCustomerDemographicId());

				if (customerDemoName.equalsIgnoreCase("Ethnicity")) {
					setEthnicity(sd, tfil);
				} else if (customerDemoName.startsWith("Home")) {
					tfil.setHomeLanguage("English".equals(studentDem.getValueName()) ? "20" : studentDem.getValue());
				} else if (customerDemoName.startsWith("Program")) {
					setProgramParticipation(sd, tfil, roster.getProductId(),studentDem.getCustomerDemographicId());
				} else if (customerDemoName
						.equalsIgnoreCase("Special Education")) {
					setSpecialEducation(sd, tfil);
				} else if (customerDemoName.endsWith("S-K")) {
					specialCodes.setSpecialCodeK(studentDem.getValueName());
				} else if (customerDemoName.endsWith("S-L")) {
					specialCodes.setSpecialCodeL(studentDem.getValueName());
				} else if (customerDemoName.endsWith("S-M")) {
					specialCodes.setSpecialCodeM(studentDem.getValueName());
				} else if (customerDemoName.endsWith("S-N")) {
					specialCodes.setSpecialCodeN(studentDem.getValueName());
				} else if (customerDemoName.endsWith("S-O")) {
					specialCodes.setSpecialCodeO(studentDem.getValueName());
				} else if (customerDemoName.endsWith("S-P")) {
					specialCodes.setSpecialCodeP(studentDem.getValueName());
				} else if (customerDemoName.endsWith("S-Q")) {
					specialCodes.setSpecialCodeQ(studentDem.getValueName());
				} else if (customerDemoName.endsWith("S-R")) {
					specialCodes.setSpecialCodeR(studentDem.getValueName());
				} else if (customerDemoName.endsWith("S-S")) {
					specialCodes.setSpecialCodeS(studentDem.getValueName());
				} else if (customerDemoName.endsWith("S-T")) {
					specialCodes.setSpecialCodeT(studentDem.getValueName());
				} else if (customerDemoName.startsWith("Race")) {
					setRacefield(sd, tfil,studentDem.getCustomerDemographicId());
				} else if (customerDemoName.startsWith("C/D/EspB") && customerDemoName.endsWith("Speaking")) {
					tfil.setAccommodationSpeaking(studentDem.getValue());
				} else if (customerDemoName.startsWith("C/D/EspB") && customerDemoName.endsWith("Listening")) {
					tfil.setAccommodationListening(studentDem.getValue());
				} else if (customerDemoName.startsWith("C/D/EspB") && customerDemoName.endsWith("Reading")) {
					tfil.setAccommodationReading(studentDem.getValue());
				} else if (customerDemoName.startsWith("C/D/EspB") && customerDemoName.endsWith("Writing")) {
					tfil.setAccommodationWriting(studentDem.getValue());
				}
			}
			tfil.setSpecialCodes(specialCodes);

		}
	}
	
	
	private void setRacefield(Set<StudentDemographic> sd, TfilLL2ND tfil, Integer customerDemographicId) {
		
		for (StudentDemographic studentDemo : sd) {
			if(studentDemo.getCustomerDemographicId()==customerDemographicId) {
				if(studentDemo.getValueName().startsWith("American")) {
					tfil.setRace1("1");
				} else if(studentDemo.getValueName().startsWith("Asian")) {
					tfil.setRace2("1");
				} else if(studentDemo.getValueName().startsWith("Black")) {
					tfil.setRace3("1");
				} else if(studentDemo.getValueName().startsWith("Native")) {
					tfil.setRace4("1");
				} else if(studentDemo.getValueName().startsWith("White")) {
					tfil.setRace5("1");
				}
			}
		}
	}

	private void setProgramParticipation(Set<StudentDemographic> sd,
			TfilLL2ND tfil, Integer productId, Integer customerDemographicId) {

		for (StudentDemographic studentDemo : sd) {
			if(studentDemo.getCustomerDemographicId()==customerDemographicId) {
				if (studentDemo.getValueName().startsWith("AEL") 
						&& (productId == 7501)) {
					tfil.setAcademicEngLearner("1");
				} else if (studentDemo.getValueName().startsWith("ESEA")) {
					tfil.setEseaTitle1("1");
				} else if (studentDemo.getValueName().startsWith("English Language")) {
					tfil.setEllEseaTitle("1");
				} else if (studentDemo.getValueName().startsWith("Gifted")) {
					tfil.setGiftedAndTalented("1");
				} else if (studentDemo.getValueName().startsWith("Indian Edu")) {
					tfil.setIndianEducation("1");
				} else if (studentDemo.getValueName().startsWith("Migrant")) {
					tfil.setMigrantEducation("1");
				} else if (studentDemo.getValueName().startsWith("Other")
						&& (productId == 7501)) {
					tfil.setOtherDemograph("1");
				}
			}
		}
	}

	private void setSpecialEducation(Set<StudentDemographic> sd, TfilLL2ND tfil) {

		for (StudentDemographic studentDemo : sd) {
			if (studentDemo.getValueName().equalsIgnoreCase("IEP")) {
				tfil.setIep("1");
			} else if (studentDemo.getValueName().equalsIgnoreCase("504")) {
				tfil.setSbi504("1");
			}
		}
	}
	
	private void setEthnicity(Set<StudentDemographic> sd, TfilLL2ND tfil) {

		String valueName = null;
		String value = null;
		for (StudentDemographic studentDemo : sd) {
			if (studentDemo.getValueName().startsWith("Hispanic") 
					|| studentDemo.getValueName().startsWith("mexicano")
					|| studentDemo.getValueName().startsWith("cubano")
					|| studentDemo.getValueName().startsWith("puertorrique")
					|| studentDemo.getValueName().startsWith("puertorriqueño")
					|| studentDemo.getValueName().startsWith("dominicano")
					|| studentDemo.getValueName().startsWith("centroamericano")
					|| studentDemo.getValueName().startsWith("sudamericano")
					|| studentDemo.getValueName().startsWith("otro")) {
				tfil.setEthinicity1("0");
				valueName = studentDemo.getValueName();
				value = studentDemo.getValue();
				break;
			}else {
				tfil.setEthinicity1("1");
			}
		}
			
		if("0".equalsIgnoreCase(tfil.getEthinicity1())){
			if(valueName.startsWith("mexicano")){
				tfil.setEthinicity2("1");
			}else if (valueName.startsWith("cubano")){
				tfil.setEthinicity2("2");
			}else if (valueName.startsWith("puertorrique") 
					|| valueName.startsWith("puertorriqueño")){
				tfil.setEthinicity2("3");
			}else if (valueName.startsWith("dominicano")){
				tfil.setEthinicity2("4");
			}else if (valueName.startsWith("centroamericano")){
				tfil.setEthinicity2("5");
			}else if (valueName.startsWith("sudamericano")){
				tfil.setEthinicity2("6");
			}else if (valueName.startsWith("otro")){
				tfil.setEthinicity2("7");
			}else{
				tfil.setEthinicity2(" ");
			}
		}else {
			tfil.setEthinicity2(" ");
		}
	}
	
	
	private void createSkillAreaScoreInformation(Connection irscon,
			TfilLL2ND tfil, TestRoster roster, TreeMap<String, Object[]> invalidSubtestMap) throws Exception{
		
		TreeMap<String, Object[]> treeMap = new TreeMap<String, Object[]>();
		TreeMap<String, Object[]> overallMap = new TreeMap<String, Object[]>();
		HashMap<String,String> contentAreaFact = new HashMap<String, String>();
		boolean isComprehensionPopulated = true;
		boolean isOralPopulated = true;
		boolean isProductivePopulated = true;
		boolean isLiteracyPopulated = true;
		boolean isScaleOverall = true;
		boolean isProficiencyLevelOverall = true;
		Integer speaking = 0;
		Integer listening = 0;
		Integer reading = 0;
		Integer writing = 0;
		isInvalidSpeaking = false;
		isInvalidListeing = false;
		isInvalidReading = false;
		isInvalidWriting = false;
		ProficiencyLevelsLL2ND pl = new ProficiencyLevelsLL2ND();
		ScaleScoresLL2ND ss = new ScaleScoresLL2ND();
		ReferenceNormalCurveEquivalentsLL2ND  nce = new ReferenceNormalCurveEquivalentsLL2ND();
		ReferencePercentileRanksLL2ND pr = new ReferencePercentileRanksLL2ND();
		SkillAreaNumberCorrectLL2ND po = new SkillAreaNumberCorrectLL2ND();
		SkillAreaPercentCorrectLL2ND pc = new SkillAreaPercentCorrectLL2ND();
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		String lexileScore = null;
		
		try {
			ps2 = irscon.prepareStatement(SCORE_SKILL_AREA_OVERALL_SQL);
			ps2.setInt(1, roster.getStudentId());
			ps2.setInt(2, roster.getTestAdminId());
			rs2 = ps2.executeQuery();
			if (rs2.next()) {
				Object[] val = new Object[4];
				val[0] = rs2.getString(1);
				val[1] = rs2.getString(2);
				val[2] = rs2.getString(3);
				val[3] = rs2.getString(4);
				overallMap.put("overall", val);
			} else {
				Object[] val = null;
				overallMap.put("overall", val);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at createSkillAreaScoreInformation:"+e.getMessage());
		} finally {
			SqlUtil.close(ps2, rs2);
		}

		try {
			ps = irscon.prepareStatement(SCORE_SKILL_AREA_SQL);
			ps.setInt(1, roster.getStudentId());
			ps.setInt(2, roster.getTestAdminId());
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					Object[] val = new Object[7];
					val[1] = rs.getString(2);
					val[2] = rs.getString(3);
					val[3] = rs.getString(4);
					val[4] = rs.getString(5);
					val[5] = rs.getString(6);
					val[6] = rs.getString(7);

					if(rs.getString(8) != null){
						lexileScore = rs.getString(8);
					}
					if (rs.getString(1).toString().trim().equalsIgnoreCase("speaking")) {
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(), val);
						contentAreaFact.put("speaking",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim().equalsIgnoreCase("listening")) {
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(), val);
						contentAreaFact.put("listening",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim().equalsIgnoreCase("reading")) {
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(), val);
						contentAreaFact.put("reading",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim().equalsIgnoreCase("writing")) {
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(), val);
						contentAreaFact.put("writing",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim().equalsIgnoreCase("comprehension")) {
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(), val);
						contentAreaFact.put("comprehension",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim().equalsIgnoreCase("oral")) {
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(), val);
						contentAreaFact.put("oral",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim().equalsIgnoreCase("productive")) {
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(), val);
						contentAreaFact.put("productive",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim().equalsIgnoreCase("literacy")) {
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(), val);
						contentAreaFact.put("literacy",rs.getString(1).toString().trim().toLowerCase());
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at createSkillAreaScoreInformation1:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		if (invalidSubtestMap.containsKey("speaking") && invalidSubtestMap.get("speaking")[1].toString().equalsIgnoreCase("1")) {
			ss.setSpeaking("INV");
			speaking++;
		} else if (invalidSubtestMap.containsKey("speaking") && invalidSubtestMap.get("speaking")[2].toString().equalsIgnoreCase("1")) {
			ss.setSpeaking("INV");
			speaking++;
		} else if (tfil.getTestInvalidationSpeaking().equalsIgnoreCase("1")) {
			ss.setSpeaking("INV");
			speaking++;
		} else {
			Object[] val = treeMap.get("speaking");
			if (val != null) {
				ss.setSpeaking(val[1].toString());
				pl.setSpeaking(val[2].toString());
				po.setSpeaking(val[3].toString());
				pc.setSpeaking(val[4].toString());
				nce.setSpeaking(val[5].toString());
				pr.setSpeaking(val[6].toString());
			}
		}

		if (invalidSubtestMap.containsKey("listening") && invalidSubtestMap.get("listening")[1].toString().equalsIgnoreCase("1")) {
			ss.setListening("INV");
			listening++;
		} else if (invalidSubtestMap.containsKey("listening") && invalidSubtestMap.get("listening")[2].toString().equalsIgnoreCase("1")) {
			ss.setListening("INV");
			listening++;
		} else if (tfil.getTestInvalidationListening().equalsIgnoreCase("1")) {
			ss.setListening("INV");
			listening++;
		} else {
			Object[] val = treeMap.get("listening");
			if (val != null) {
				ss.setListening(val[1].toString());
				pl.setListening(val[2].toString());
				po.setListening(val[3].toString());
				pc.setListening(val[4].toString());
				nce.setListening(val[5].toString());
				pr.setListening(val[6].toString());
			}
		}

		if (invalidSubtestMap.containsKey("reading") && invalidSubtestMap.get("reading")[1].toString().equalsIgnoreCase("1")) {
			ss.setReading("INV");
			reading++;
		} else if (invalidSubtestMap.containsKey("reading") && invalidSubtestMap.get("reading")[2].toString().equalsIgnoreCase("1")) {
			ss.setReading("INV");
			reading++;
		} else if (tfil.getTestInvalidationReading().equalsIgnoreCase("1")) {
			ss.setReading("INV");
			reading++;
		} else {
			Object[] val = treeMap.get("reading");
			if (val != null) {
				ss.setReading(val[1].toString());
				pl.setReading(val[2].toString());
				po.setReading(val[3].toString());
				pc.setReading(val[4].toString());
				nce.setReading(val[5].toString());
				pr.setReading(val[6].toString());
				tfil.setLexileScore((lexileScore == null)?"":lexileScore);
			}
		}

		if (invalidSubtestMap.containsKey("writing") && invalidSubtestMap.get("writing")[1].toString().equalsIgnoreCase("1")) {
			ss.setWriting("INV");
			writing++;
		} else if (invalidSubtestMap.containsKey("writing") && invalidSubtestMap.get("writing")[2].toString().equalsIgnoreCase("1")) {
			ss.setWriting("INV");
			writing++;
		} else if (tfil.getTestInvalidationWriting().equalsIgnoreCase("1")) {
			ss.setWriting("INV");
			writing++;
		} else {
			Object[] val = treeMap.get("writing");
			if (val != null) {
				ss.setWriting(val[1].toString());
				pl.setWriting(val[2].toString());
				po.setWriting(val[3].toString());
				pc.setWriting(val[4].toString());
				nce.setWriting(val[5].toString());
				pr.setWriting(val[6].toString());
			}
		}
		if (speaking > 0) {
			po.setSpeaking("");
			pl.setSpeaking("");
			pc.setSpeaking("");
			isOralPopulated = false;
			isProductivePopulated = false;
			isInvalidSpeaking = true;
			isProficiencyLevelOverall = false;
			isScaleOverall = false;
		}
		if (listening > 0) {
			po.setListening("");
			pl.setListening("");
			pc.setListening("");
			isComprehensionPopulated = false;
			isOralPopulated = false;
			isInvalidListeing = true;
			isProficiencyLevelOverall = false;
			isScaleOverall = false;
		}
		if (reading > 0) {
			po.setReading("");
			pl.setReading("");
			pc.setReading("");
			isComprehensionPopulated = false;
			isLiteracyPopulated = false;
			isInvalidReading = true;
			isProficiencyLevelOverall = false;
			isScaleOverall = false;
		}
		if (writing > 0) {
			po.setWriting("");
			pl.setWriting("");
			pc.setWriting("");
			isProductivePopulated = false;
			isLiteracyPopulated = false;
			isInvalidWriting = true;
			isProficiencyLevelOverall = false;
			isScaleOverall = false;
		}
		
		if(contentAreaFact.containsKey("listening") && contentAreaFact.containsKey("reading")){
			if (isComprehensionPopulated) {
				Object[] val = treeMap.get("comprehension");
				if (val != null) {
					ss.setComprehension(val[1].toString());
					pl.setComprehension(val[2].toString());
					nce.setComprehension(val[5].toString());
					pr.setComprehension(val[6].toString());
				}
				else {
					ss.setComprehension(" ");
				}
			} else {
				ss.setComprehension("N/A");
			}
		}
		else{
			ss.setComprehension(" ");
			pl.setComprehension(" ");
			nce.setComprehension(" ");
			pr.setComprehension(" ");
		}

		if(contentAreaFact.containsKey("speaking") && contentAreaFact.containsKey("listening")){
			if (isOralPopulated) {
				Object[] val = treeMap.get("oral");
				if (val != null) {
					ss.setOral(val[1].toString());
					pl.setOral(val[2].toString());
					nce.setOral(val[5].toString());
					pr.setOral(val[6].toString());
				} else {
					ss.setOral(" ");
				}
			} else {
				ss.setOral("N/A");
			}
		}
		else{
			ss.setOral(" ");
			pl.setOral(" ");
			nce.setOral(" ");
			pr.setOral(" ");
		}
		
		if(contentAreaFact.containsKey("speaking") && contentAreaFact.containsKey("writing")){
			if (isProductivePopulated) {
				Object[] val = treeMap.get("productive");
				if (val != null) {
					ss.setProductive(val[1].toString());
					pl.setProductive(val[2].toString());
					nce.setProductive(val[5].toString());
					pr.setProductive(val[6].toString());
				} else {
					ss.setProductive(" ");
				}
			} else {
				ss.setProductive("N/A");
			}
		}
		else{
			ss.setProductive(" ");
			pl.setProductive(" ");
			nce.setProductive(" ");
			pr.setProductive(" ");
		}
		
		if(contentAreaFact.containsKey("reading") && contentAreaFact.containsKey("writing")){
			if (isLiteracyPopulated) {
				Object[] val = treeMap.get("literacy");
				if (val != null) {
					ss.setLiteracy(val[1].toString());
					pl.setLiteracy(val[2].toString());
					nce.setLiteracy(val[5].toString());
					pr.setLiteracy(val[6].toString());
				} else {
					ss.setLiteracy(" ");
				}
			} else {
				ss.setLiteracy("N/A");
			}
		}
		else{
			ss.setLiteracy(" ");
			pl.setLiteracy(" ");
			nce.setLiteracy(" ");
			pr.setLiteracy(" ");
		}
		
		if(contentAreaFact.containsKey("listening") && contentAreaFact.containsKey("reading") && contentAreaFact.containsKey("speaking") && contentAreaFact.containsKey("writing") ){
			if (isScaleOverall) {
				if(overallMap.get("overall") != null){
					ss.setOverall(overallMap.get("overall")[0].toString());
				}else{
					ss.setOverall("");
				}
			} else {
				ss.setOverall("N/A");
			}
		}
		else{
			ss.setOverall(" ");
		}
		if(contentAreaFact.containsKey("listening") && contentAreaFact.containsKey("reading") && contentAreaFact.containsKey("speaking") && contentAreaFact.containsKey("writing") ){
			if (isProficiencyLevelOverall) {
				if(overallMap.get("overall") != null){
					pl.setOverall(overallMap.get("overall")[1].toString());
					nce.setOverall(overallMap.get("overall")[2].toString());
					pr.setOverall(overallMap.get("overall")[3].toString());
				}else{
					pl.setOverall("");
					nce.setOverall("");
					pr.setOverall("");
				}
			} else {
				pl.setOverall("");
				nce.setOverall("");
				pr.setOverall("");
			}	
		}
		else{
			pl.setOverall(" ");
			nce.setOverall(" ");
			pr.setOverall(" ");
		}


		tfil.setScaleScores(ss);
		tfil.setProficiencyLevels(pl);
		tfil.setReferenceNormalCurveEquivalent(nce);
		tfil.setReferencePercentileRanks(pr);
		tfil.setSkillAreaNumberCorrect(po);
		tfil.setSkillAreaPercentCorrect(pc);
	}
	
	
	private void createSubSkillAreaScoreInformation(Connection oascon,
			Connection irscon, TfilLL2ND tfil, TestRoster roster) throws Exception{
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		SubSkillNumberCorrectLL2ND subNumCorrect = new SubSkillNumberCorrectLL2ND();
		SubSkillPercentCorrectLL2ND subPercCorrect = new SubSkillPercentCorrectLL2ND();
		String subSkillName;
		@SuppressWarnings("unused")
		String subSkillCategoryName;
		HashMap<String, String> pointsObtained = new HashMap<String, String>();
		HashMap<String, String> percentObtained = new HashMap<String, String>();

		try {
			ps2 = oascon.prepareStatement(SUBSKILL_ITEM_AREA_INFOMATION);
			ps2.setInt(1, roster.getTestAdminId());
			rs2 = ps2.executeQuery();
			while (rs2.next()) {
				subSkillAreaScoreInfo.put(rs2.getString(1), rs2.getString(2));
				subSkillAreaItemCategory.put(rs2.getString(2), rs2.getString(3));
				createAcademicScore(oascon,subSkillAreaScoreInfo,subSkillAreaItemCategory, rs2.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at createSubSkillAreaScoreInformation:"+e.getMessage());
		} finally {
			SqlUtil.close(ps2, rs2);
		}
		
		try {
			ps = irscon.prepareStatement(SUBSKILL_IRS_INFORMATION);
			ps.setInt(1, roster.getStudentId());
			ps.setInt(2, roster.getTestAdminId());
			rs = ps.executeQuery();
			while (rs.next()) {
				percentObtained.put(rs.getString(1), rs.getString(3));
				pointsObtained.put(rs.getString(1), rs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at createSubSkillAreaScoreInformation1:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		for (String x : percentObtained.keySet()) {
			subSkillName = subSkillAreaScoreInfo.get(x);
			if(subSkillName != null){
				if (subSkillName.contains("Social, Intercultural, and Instructional Communication") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subPercCorrect.setSpeakingSocialInstructionalCommunication(percentObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subPercCorrect.setListeningSocialInstructionalCommunication(percentObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						subPercCorrect.setReadingSocialInstructionalCommunication(percentObtained.get(x).toString());
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						subPercCorrect.setWritingSocialInstructionalCommunication(percentObtained.get(x).toString());
					}
				} else if (subSkillName.contains("Mathematics / Science / Technical Subjects") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subPercCorrect.setSpeakingMathematicsScienceTechnical(percentObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subPercCorrect.setListeningMathematicsScienceTechnical(percentObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						if(Integer.parseInt(pointsObtained.get(x).toString()) < 3 && "K".equalsIgnoreCase(roster.getStudent().getGrade().toString())){
							subPercCorrect.setReadingMathematicsScienceTechnical("XXXXX");
						}else{
							subPercCorrect.setReadingMathematicsScienceTechnical(percentObtained.get(x).toString());
						}
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						if(Integer.parseInt(pointsObtained.get(x).toString()) < 3 && "K".equalsIgnoreCase(roster.getStudent().getGrade().toString())){
							subPercCorrect.setWritingMathematicsScienceTechnical("XXXXX");
						}else{
							subPercCorrect.setWritingMathematicsScienceTechnical(percentObtained.get(x).toString());
						}
					}
				} else if (subSkillName.contains("Language Arts / Social Studies / History") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subPercCorrect.setSpeakingLanguageArtsScocialHistory(percentObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subPercCorrect.setListeningLanguageArtsScocialHistory(percentObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						if(Integer.parseInt(pointsObtained.get(x).toString()) < 3 && "K".equalsIgnoreCase(roster.getStudent().getGrade().toString())){
							subPercCorrect.setReadingLanguageArtsScocialHistory("XXXXX");
						}else{
							subPercCorrect.setReadingLanguageArtsScocialHistory(percentObtained.get(x).toString());
						}
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						if(Integer.parseInt(pointsObtained.get(x).toString()) < 3 && "K".equalsIgnoreCase(roster.getStudent().getGrade().toString())){
							subPercCorrect.setWritingLanguageArtsScocialHistory("XXXXX");
						}else{
							subPercCorrect.setWritingLanguageArtsScocialHistory(percentObtained.get(x).toString());
						}
					}
				} else if (subSkillName.contains("Foundational Skills") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
					if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						subPercCorrect.setReadingFoundationalSkills(percentObtained.get(x).toString());
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						subPercCorrect.setWritingFoundationalSkills(percentObtained.get(x).toString());
					}
				} else if (subSkillName.contains("Academic") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subPercCorrect.setSpeakingAcademic(percentObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subPercCorrect.setListeningAcademic(percentObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						subPercCorrect.setReadingAcademic(percentObtained.get(x).toString());
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						subPercCorrect.setWritingAcademic(percentObtained.get(x).toString());
					}
				}
			}
		}
		if("3".equals(tfil.getTestLevel()) ||	"4".equals(tfil.getTestLevel()) ||
					"5".equals(tfil.getTestLevel())){
			subPercCorrect.setReadingFoundationalSkills("N/A");
		}
		if("2".equals(tfil.getTestLevel()) ||	"3".equals(tfil.getTestLevel()) ||
					"4".equals(tfil.getTestLevel()) || "5".equals(tfil.getTestLevel())){
			subPercCorrect.setWritingFoundationalSkills("N/A");
		}
		
		tfil.setSubSkillPercentCorrect(subPercCorrect);

		for (String x : pointsObtained.keySet()) {
			subSkillName = subSkillAreaScoreInfo.get(x);
			if(subSkillName != null){
				if (subSkillName.contains("Social, Intercultural, and Instructional Communication") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subNumCorrect.setSpeakingSocialInstructionalCommunication(pointsObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subNumCorrect.setListeningSocialInstructionalCommunication(pointsObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						subNumCorrect.setReadingSocialInstructionalCommunication(pointsObtained.get(x).toString());
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						subNumCorrect.setWritingSocialInstructionalCommunication(pointsObtained.get(x).toString());
					}
				} else if (subSkillName.contains("Mathematics / Science / Technical Subjects") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subNumCorrect.setSpeakingMathematicsScienceTechnical(pointsObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subNumCorrect.setListeningMathematicsScienceTechnical(pointsObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						if(Integer.parseInt(pointsObtained.get(x).toString()) < 3 && "K".equalsIgnoreCase(roster.getStudent().getGrade().toString())){
							subNumCorrect.setReadingMathematicsScienceTechnical("XXX");
						}else{
							subNumCorrect.setReadingMathematicsScienceTechnical(pointsObtained.get(x).toString());
						}
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						if(Integer.parseInt(pointsObtained.get(x).toString()) < 3 && "K".equalsIgnoreCase(roster.getStudent().getGrade().toString())){
							subNumCorrect.setWritingMathematicsScienceTechnical("XXX");
						}else{
							subNumCorrect.setWritingMathematicsScienceTechnical(pointsObtained.get(x).toString());
						}
					}
				} else if (subSkillName.contains("Language Arts / Social Studies / History") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subNumCorrect.setSpeakingLanguageArtsScocialHistory(pointsObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subNumCorrect.setListeningLanguageArtsScocialHistory(pointsObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						if(Integer.parseInt(pointsObtained.get(x).toString()) < 3 && "K".equalsIgnoreCase(roster.getStudent().getGrade().toString())){
							subNumCorrect.setReadingLanguageArtsScocialHistory("XXX");
						}else{
							subNumCorrect.setReadingLanguageArtsScocialHistory(pointsObtained.get(x).toString());
						}
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						if(Integer.parseInt(pointsObtained.get(x).toString()) < 3 && "K".equalsIgnoreCase(roster.getStudent().getGrade().toString())){
							subNumCorrect.setWritingLanguageArtsScocialHistory("XXX");
						}else{
							subNumCorrect.setWritingLanguageArtsScocialHistory(pointsObtained.get(x).toString());
						}
					}
				} else if (subSkillName.contains("Foundational Skills") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
					if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						subNumCorrect.setReadingFoundationalSkills(pointsObtained.get(x).toString());
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						subNumCorrect.setWritingFoundationalSkills(pointsObtained.get(x).toString());
					}
				} else if (subSkillName.contains("Academic") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subNumCorrect.setSpeakingAcademic(pointsObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subNumCorrect.setListeningAcademic(pointsObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						subNumCorrect.setReadingAcademic(pointsObtained.get(x).toString());
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						subNumCorrect.setWritingAcademic(pointsObtained.get(x).toString());
					}
				}
			}
		}
		if("3".equals(tfil.getTestLevel()) ||	"4".equals(tfil.getTestLevel()) ||
				"5".equals(tfil.getTestLevel())){
			subNumCorrect.setReadingFoundationalSkills("N/A");
		}
		if("2".equals(tfil.getTestLevel()) ||	"3".equals(tfil.getTestLevel()) ||
					"4".equals(tfil.getTestLevel()) || "5".equals(tfil.getTestLevel())){
			subNumCorrect.setWritingFoundationalSkills("N/A");
		}
		tfil.setSubSkillNumberCorrect(subNumCorrect);
	}
	
	
	private void createAcademicScore(Connection oascon,
			HashMap<String, String> subSkillAreaScoreInfo2,
			HashMap<String, String> subSkillAreaItemCategory2, String productId) throws Exception {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = oascon.prepareStatement(ACADEMIC_SKILL_AREA_INFORMATION);
			rs = ps.executeQuery();
			while (rs.next()) {
				subSkillAreaScoreInfo.put((productId+rs.getString(1)), rs.getString(2));
				subSkillAreaItemCategory.put(rs.getString(2), rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at createAcademicScore:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
		
	}

	private boolean fillValidSubSkillScore(String skillName, HashMap<String, String> subSkillCategory){
		String skill;
		skill = subSkillCategory.get(skillName);
		
		if(skill.equalsIgnoreCase("Speaking") && isInvalidSpeaking){
			return false;
		}else if(skill.equalsIgnoreCase("Listening") && isInvalidListeing){
			return false;
		}else if(skill.equalsIgnoreCase("Reading") && isInvalidReading){
			return false;
		}else if(skill.equalsIgnoreCase("Writing") && isInvalidWriting){
			return false;
		}else{
			return true;
		}
	}
	
	private boolean isValidGrade(TfilLL2ND tfil, String subtest){
		
		//String grade = roster.getStudent().getGrade().toString();
		if("Reading".equalsIgnoreCase(subtest)){
			if("3".equals(tfil.getTestLevel()) ||	"4".equals(tfil.getTestLevel()) ||
					"5".equals(tfil.getTestLevel())){
				return false;
			}else{
				return true;
			}
		}else if ("Writing".equalsIgnoreCase(subtest)){
			if("2".equals(tfil.getTestLevel()) ||	"3".equals(tfil.getTestLevel()) ||
					"4".equals(tfil.getTestLevel()) || "5".equals(tfil.getTestLevel())){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	private void createSubtestIndicatorFlag (Connection con,
			Integer rosterId, TfilLL2ND tfil) throws Exception {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SUBTEST_INDICATOR);
			ps.setInt(1, rosterId);
			rs = ps.executeQuery();
			if (rs.next()) {
				if(rs.getString(1) != null && rs.getString(1).equals("T")) {
					tfil.setSubtestIndicatorFlag(rs.getString(1));
				}else {
					tfil.setSubtestIndicatorFlag("");
				}
			} else {
				tfil.setSubtestIndicatorFlag("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at createSubtestIndicatorFlag:"+e.getMessage());
		}finally {
			SqlUtil.close(ps, rs);
		}
	}
	
	private void createItemResponseInformation(Connection oascon,
			TestRoster roster, TfilLL2ND tfil) throws Exception{
		

		Map<String, TreeMap<String, LinkedList<RostersItem>>> allItems = getItemResponseGrt(oascon, roster);
		ItemResponsesGRTLL2ND responsesGRT = new ItemResponsesGRTLL2ND();
		String speakingMCItems = "";
		String speakingCRItems = "";
		
		String listeningMCItems = "";
		
		String readingMCItems = "";
		String readingCRItems = "";
		
		String writingMCItems = "";
		String writingCRItems = "";
		

		/*******************************************************
		 * speaking
		 *******************************************************/
		if (allItems.get("SPEAKING") != null) {
			TreeMap<String, LinkedList<RostersItem>> speakingitem = allItems.get("SPEAKING");
			
			/// for sr items
			/*if (speakingitem.get("SR") != null) {
				speakingMCItems = constractSRItemResponseString(speakingMCItems, speakingitem, 20); 
			}*/

			//responsesGRT.setSpeakingMCItems(speakingMCItems);

			if (speakingitem.get("CR") != null) {
				speakingCRItems = constractCRItemResponseString(speakingCRItems, speakingitem, 20); 
    		}

			responsesGRT.setSpeakingMCItems(speakingCRItems);
			
			responsesGRT.setSpeakingCRItems("");

		} else {
			responsesGRT.setSpeakingMCItems("");
			responsesGRT.setSpeakingCRItems("");
		}
		

		/*******************************************************
		 * listening
		 *******************************************************/
		if (allItems.get("LISTENING") != null) {
			TreeMap<String, LinkedList<RostersItem>> listeningitem = allItems
					.get("LISTENING");
			// / for sr items
			if (listeningitem.get("SR") != null) {
				listeningMCItems = constractSRItemResponseString(listeningMCItems, listeningitem, 25); 
			}

			responsesGRT.setListeningMCItems(listeningMCItems);

		} else {
			responsesGRT.setListeningMCItems("");
		}
		
	
		/*******************************************************
		 * reading
		 *******************************************************/
		if (allItems.get("READING") != null) {
			TreeMap<String, LinkedList<RostersItem>> readingitem = allItems
					.get("READING");
			// / for sr items
			if (readingitem.get("SR") != null) {
				readingMCItems = constractSRItemResponseString(readingMCItems, readingitem, 35); 
			}
			
			responsesGRT.setReadingMCItems(readingMCItems);
			
			if (readingitem.get("CR") != null) {
				readingCRItems = constractCRItemResponseString(readingCRItems, readingitem, 10);
			}
			
			responsesGRT.setReadingCRItems(readingCRItems);

		} else {
			responsesGRT.setReadingMCItems("");
			responsesGRT.setReadingCRItems("");
		}
		
		/*******************************************************
		 * writing
		 *******************************************************/
		if (allItems.get("WRITING") != null) {
			TreeMap<String, LinkedList<RostersItem>> writingitem = allItems
					.get("WRITING");
			
			/// for sr items
			if (writingitem.get("SR") != null) {
				writingMCItems = constractSRItemResponseString(writingMCItems, writingitem, 20); 
			}

			responsesGRT.setWritingMCItems(writingMCItems);

			if (writingitem.get("CR") != null) {
				writingCRItems = constractCRItemResponseString(writingCRItems, writingitem, 20); 
    		}

			responsesGRT.setWritingCRItems(writingCRItems);

		} else {
			responsesGRT.setWritingMCItems("");
			responsesGRT.setWritingCRItems("");
		}

		
		tfil.setItemResponseGRT(responsesGRT);
	
	}


	private Map<String,TreeMap<String,  LinkedList<RostersItem>>> getItemResponseGrt(Connection oascon, TestRoster roster) throws Exception {
		Map<String,TreeMap<String,  LinkedList<RostersItem>>> allitem =  new TreeMap<String,TreeMap<String,  LinkedList<RostersItem>>> ();
		PreparedStatement ps = null ;
		ResultSet rs = null;
		 Map<String,RostersItem> srValueMap = new TreeMap<String,RostersItem>();
		 Map<String,RostersItem> crValueMap = new TreeMap<String,RostersItem>();
		try{
			ps = oascon.prepareStatement(ALL_ITEM_DETAILS_FOR_ROSTER);
			ps.setInt(1, roster.getTestRosterId());
			ps.setInt(2, roster.getTestRosterId());
			ps.setInt(3, roster.getTestRosterId());
			rs = ps.executeQuery(); 
			//rs.setFetchSize(500);
			while (rs.next()){
				
				RostersItem item = new RostersItem();
				item.setItemId(rs.getString(1));
				item.setItemIndx(rs.getString(2));
				item.setItemType(rs.getString(3));
				item.setItemCorrectResponse(rs.getString(4));
				item.setItemSetIdTD(rs.getString(5));				
				item.setItemDescriptio(rs.getString(6));
				item.setItemValidationStatusForScoring(rs.getString(7));
				item.setIsSubtestCompleted(rs.getString(8));
				populateMap(allitem, item, srValueMap,crValueMap );
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getItemResponseGrt:"+e.getMessage());
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		
		try{
			ps = oascon.prepareStatement(ITEM_RESPONSE_SR_FOR_ROSTER);
			ps.setInt(1, roster.getTestRosterId());
			ps.setInt(2, roster.getTestRosterId());
			rs = ps.executeQuery(); rs.setFetchSize(500);
			while (rs.next()){
				RostersItem  item= srValueMap.get(rs.getString(1));
				item.setStudentResponse(rs.getString(2));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getItemResponseGrt1:"+e.getMessage());
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		
		try{
			ps = oascon.prepareStatement(ITEM_RESPONSE_CR_FOR_ROSTER);
			ps.setInt(1, roster.getTestRosterId());
			ps.setInt(2, roster.getTestRosterId());
			rs = ps.executeQuery(); rs.setFetchSize(500);
			while (rs.next()){
				RostersItem  item= crValueMap.get(rs.getString(1));
				item.setStudentResponse(rs.getString(2));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getItemResponseGrt2:"+e.getMessage());
			
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		return allitem;
	}
	
	private void populateMap(
			Map<String, TreeMap<String, LinkedList<RostersItem>>> allitem,
			RostersItem item, Map<String, RostersItem> srValueMap,
			Map<String, RostersItem> crValueMap) {

		String itemDesc = item.getItemDescriptio().toUpperCase();
		String itemtype = item.getItemType().toUpperCase();
		if (item.getItemType().trim().equalsIgnoreCase("CR")) {
			crValueMap.put(item.getItemId(), item);
		} else {
			srValueMap.put(item.getItemId(), item);
		}
		if (allitem.get(itemDesc) != null) {
			TreeMap<String, LinkedList<RostersItem>> descMap = allitem
					.get(itemDesc);
			if (descMap.get(itemtype) != null) {
				LinkedList<RostersItem> rostersItemList = descMap.get(itemtype);
				rostersItemList.add(item);
			} else {
				LinkedList<RostersItem> rostersItemList = new LinkedList<RostersItem>();
				descMap.put(itemtype, rostersItemList);
				rostersItemList.add(item);
			}

		} else {
			TreeMap<String, LinkedList<RostersItem>> typeMap = new TreeMap<String, LinkedList<RostersItem>>();
			allitem.put(itemDesc, typeMap);
			LinkedList<RostersItem> rostersItemList = new LinkedList<RostersItem>();
			typeMap.put(itemtype, rostersItemList);
			rostersItemList.add(item);
		}
	}
	
	private String constractSRItemResponseString(String MCItems, TreeMap<String, LinkedList<RostersItem>> itemS, int positionLength ) {
		LinkedList<RostersItem> itemlist = itemS.get("SR");
		int itemCount = 0;
		for (RostersItem item : itemlist) {
			if (item.isItemValidateForScoring()
					&& (item.getStudentResponse() != null && item
							.getStudentResponse().trim().length() > 0)) {
				if (!item.getItemCorrectResponse().equalsIgnoreCase(
						item.getStudentResponse())) {
					MCItems += wrongMap.get(item
							.getStudentResponse().trim());
				} else {
					MCItems += item.getStudentResponse().trim();
				}

			} else {
				MCItems += " ";
			}
			itemCount++;
			if(itemCount == positionLength){
				break;
			}
		}
		
		return MCItems;

	}
	
	private String constractCRItemResponseString(String CRItems,
			TreeMap<String, LinkedList<RostersItem>> itemMap, int positionLength) {
		LinkedList<RostersItem> itemlist = itemMap.get("CR");
		int itemCount = 0; 
		for (RostersItem item : itemlist) {
			if (item.isItemValidateForScoring() && item.isSubtestCompleted()){
				if((item.getStudentResponse() != null && item
							.getStudentResponse().trim().length() > 0)) {
					CRItems+=item.getStudentResponse();
				}else {
					CRItems += "A";
				}
			} else {
				CRItems += " ";
			}
			itemCount++;
			if(itemCount == positionLength){
				break;
			}
		}
		return CRItems;
	}
	
	private Student getStudent(Connection con, int studentId)throws Exception {
		Student std = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(STUDENT_SQL);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			if (rs.next()) {
				std = new Student();
				std.setStudentId(studentId);
				std.setFirstName(rs.getString(2));
				std.setLastName(rs.getString(3));
				std.setMiddleName(rs.getString(4));
				std.setBirthDate(rs.getDate(5));
				std.setGender(rs.getString(6));
				std.setGrade(rs.getString(7));
				std.setCustomerId(customerId);
				std.setTestPurpose(rs.getString(9));
				std.setExtStudentId(rs.getString(10));
				std.setStudentContact(getStudentContact(con, studentId));
				std.setStudentDemographic(getStudentDemographic(con,studentId));

			}

		} finally {
			SqlUtil.close(ps, rs);
		}

		return std;
	}
}
