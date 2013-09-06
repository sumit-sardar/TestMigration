package com.ctb.processor;

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

import com.ctb.bean.testAdmin.User;
import com.ctb.db.EmailProcessorDao;
import com.ctb.dto.Accomodations;
import com.ctb.dto.CustomerDemographic;
import com.ctb.dto.CustomerDemographicValue;
import com.ctb.dto.ItemResponsesGRT;
import com.ctb.dto.OrderFile;
import com.ctb.dto.ProficiencyLevels;
import com.ctb.dto.RostersItem;
import com.ctb.dto.ScaleScores;
import com.ctb.dto.SkillAreaNumberCorrect;
import com.ctb.dto.SkillAreaPercentCorrect;
import com.ctb.dto.SpecialCodes;
import com.ctb.dto.Student;
import com.ctb.dto.StudentContact;
import com.ctb.dto.StudentDemographic;
import com.ctb.dto.SubSkillNumberCorrect;
import com.ctb.dto.SubSkillPercentCorrect;
import com.ctb.dto.TestRoster;
import com.ctb.dto.Tfil;
import com.ctb.exception.CTBBusinessException;
import com.ctb.utils.Configuration;
import com.ctb.utils.EmetricUtil;
import com.ctb.utils.SqlUtil;

public class FileGenerator {

	private static final DateFormat fileDateOutputFormat = new SimpleDateFormat(
	"MMddyyHHmm");

	private static String sql = "select org_node_mdr_number as mdr, category_name as categoryName,"
		+ " org_node_code as nodeCode, node.org_node_name as nodeName, onc.category_level as categoryLevel"
		+ " from org_node_ancestor ona, org_node_category onc, org_node node, org_node_student ons "
		+ " where    ons.org_node_id = ona.org_node_id   and  ons.student_id =  ? "
		+ " and onc.org_node_category_id = node.org_node_category_id   and node.org_node_id = ona.ancestor_org_node_id";

	// Defect Fix for 66423 and timeZobe defect
	private static String testSessionSQl = "select tad.preferred_form as form, tc.test_level as testLevel,tad.time_zone as timezone,"
		+ " to_Char((roster.start_date_time),'MMDDYY HH24:MI:SS') as testDate,to_Char((roster.completion_date_time),'MMDDYYYY HH24:MI:SS')  as dateTestingCompleted,  to_Char(nvl(prg.program_start_date, ''),'MMDDYYYY HH24:MI:SS')  as programStartDate"
		+ " from test_admin tad, test_roster roster,test_catalog tc, program prg "
		+ "	where prg.program_id = tad.program_id(+)"
	    + " and  tad.test_admin_id = roster.test_admin_id and roster.test_completion_status in ('CO','IS','IC')"
		+ " and tc.test_catalog_id = tad.test_catalog_id"
		+ " and roster.test_roster_id = ? ";

	private static String customerModelLevel = " select max(category_level) as modelLevel"
		+ " from org_node_category where customer_id = :customerId and activation_status = 'AC'";

	private static String testRosterDetails = " select distinct siss.VALIDATION_STATUS as validationStatus,"
		+ " siss.ITEM_SET_ORDER as itemSetOrder,siss.EXEMPTIONS as testExemptions,"
		+ " siss.ABSENT as absent,decode (iss.item_set_name,'HABLANDO','Speaking','ESCUCHANDO', 'Listening','LECTURA','Reading','ESCRITURA','Writing',iss.item_set_name) as itemSetName from student_item_set_status siss,"
		+ " test_roster ros, item_set iss where iss.item_set_id = siss.item_set_id and "
		+ " siss.test_roster_id = ros.test_roster_id and ros.student_id = ? "
		+ " and ros.test_roster_id = ? order by siss.item_set_order";

	private static String scoreSkilAreaSQL = "select careadim.name as name,    careafct.scale_score as scale_score,     careafct.proficency_level as proficency_level, "
		+ " careafct.points_obtained as points_obtained,     careafct.percent_obtained as percent_obtained"
		+ " from laslink_content_area_fact careafct, content_area_dim careadim "
		+ " where careafct.content_areaid = careadim.content_areaid"
		+ " and content_area_type = 'LL CONTENT AREA'"
		+ " and careafct.studentid = :studentId "
		+ " and careafct.sessionid = :sessionId";

	private static String scoreSkilAreaOverAllSQL = " Select compfact.scale_score, compfact.proficency_level "
		+ "from laslink_composite_fact compfact  "
		+ "where compfact.studentid = :studentId "
		+ "and compfact.sessionid = :sessionId";
	private String customerDemographicsql = "select this_.customer_demographic_id as customer_demographic_id, "
		+ " this_.customer_id as customer_id, this_.label_name  as label_name  "
		+ "from customer_demographic this_ where this_.customer_id = ?";
	private String customerDemographicsqlWithLevel = "select this_.customer_demographic_id as customer_demographic_id, "
		+ " this_.customer_id as customer_id, this_.label_name  as label_name  from customer_demographic this_ "
		+ "where this_.customer_id = ? and label_name = 'Accommodations' ";
	private String customersql = "select cust.STATEPR as state, nvl(addr.CITY,' ') as CITY, cust.CONTACT_EMAIL as email, cust.CONTACT_PHONE as phone,cust.CONTACT_NAME as contact from Customer cust, Address addr where CUSTOMER_ID = ? and cust.billing_address_id = addr.address_id(+) ";
	private String testRosterSql = " select this_.TEST_ROSTER_ID    as TEST_ROSTER_ID, this_.ACTIVATION_STATUS  as ACTIVATION_STATUS,   this_.TEST_COMPLETION_STATUS as TEST_COMPLETION_STATUS,"
		+ "  this_.CUSTOMER_ID            as CUSTOMER_ID, this_.STUDENT_ID  as STUDENT_ID, this_.TEST_ADMIN_ID   as TEST_ADMIN_ID  "
		+ " from TEST_ROSTER this_  "
		+ "where this_.CUSTOMER_ID = ?  and this_.ACTIVATION_STATUS = 'AC' "
		+ "and this_.TEST_COMPLETION_STATUS in ('CO', 'IS', 'IC')";
	
	@SuppressWarnings("unused")
	private String  testRosterByIDSql = " select this_.TEST_ROSTER_ID   as TEST_ROSTER_ID,  this_.ACTIVATION_STATUS  as ACTIVATION_STATUS,  this_.TEST_COMPLETION_STATUS as TEST_COMPLETION_STATUS, this_.CUSTOMER_ID as CUSTOMER_ID,  this_.STUDENT_ID   as STUDENT_ID,  this_.TEST_ADMIN_ID  as TEST_ADMIN_ID  from TEST_ROSTER this_   where this_.TEST_ROSTER_ID IN( <#ROSTER_ID_LIST#> )";
	private String  testRosterWithStudentByRosterIDSql = " select this_.TEST_ROSTER_ID   as TEST_ROSTER_ID,  this_.ACTIVATION_STATUS  as ACTIVATION_STATUS,  this_.TEST_COMPLETION_STATUS as TEST_COMPLETION_STATUS, this_.STUDENT_ID   as STUDENT_ID,  this_.TEST_ADMIN_ID  as TEST_ADMIN_ID " +
			" , student0_.FIRST_NAME   as FIRST_NAME,  student0_.LAST_NAME    as LAST_NAME,  student0_.MIDDLE_NAME  as MIDDLE_NAME,  student0_.BIRTHDATE    as BIRTHDATE,  decode(upper(student0_.GENDER), 'U', ' ', student0_.GENDER) as GENDER,  student0_.GRADE  as GRADE0,  student0_.TEST_PURPOSE as TEST_PURPOSE,   student0_.EXT_PIN1  as EXT_PIN1" +
			"  from TEST_ROSTER this_ , student student0_  " +
			" where this_.STUDENT_ID = student0_.STUDENT_ID  and this_.TEST_ROSTER_ID IN( <#ROSTER_ID_LIST#> )";

	private String studentSql = "  select student0_.STUDENT_ID   as STUDENT_ID, student0_.FIRST_NAME   as FIRST_NAME,  student0_.LAST_NAME    as LAST_NAME,  student0_.MIDDLE_NAME  as MIDDLE_NAME,  student0_.BIRTHDATE    as BIRTHDATE,  student0_.GENDER       as GENDER,  student0_.GRADE  as GRADE0,   student0_.CUSTOMER_ID  as CUSTOMER_ID,   student0_.TEST_PURPOSE as TEST_PURPOSE,   student0_.EXT_PIN1  as EXT_PIN1  from student student0_   where student0_.STUDENT_ID = ? ";

	private String studentContactSql = " select studentcon0_.STUDENT_ID  as STUDENT_ID, studentcon0_.STUDENT_CONTACT_ID as STUDENT_CONTACT_ID, studentcon0_.CITY  as CITY,   studentcon0_.STATEPR  as STATEPR,   studentcon0_.STUDENT_ID  as STUDENT_ID  from STUDENT_CONTACT studentcon0_  where studentcon0_.STUDENT_ID = ?";

	private String studentDemographicSql = " select STUDENT_DEMOGRAPHIC_DATA_ID , CUSTOMER_DEMOGRAPHIC_ID , VALUE_NAME, VALUE from student_demographic_data sdd where sdd.student_id = ? ";

	private String customerDemographiValuecsql = "select value_name,value_code,customer_demographic_id  from customer_demographic_value  where customer_demographic_id =?";
	@SuppressWarnings("unused")
	private String subSkillItemAreaInformation1 = "select tad.product_id || iset.item_set_id subskill_id,"
		+ " iset.item_set_name from test_admin tad,product,item_set_category icat,item_set iset"
		+ " where tad.test_admin_id = ? and icat.item_set_category_level = 4 and tad.product_id = product.product_id"
		+ " and product.parent_product_id = icat.framework_product_id and iset.item_set_category_id = icat.item_set_category_id";

	private String subSkillItemAreaInformation = "select tad.product_id || iset.item_set_id subskill_id,iset.item_set_name, "
		+ " iset1.item_set_name itemCategory from test_admin tad, product, item_set_category icat, item_set iset,item_set iset1,"
		+ " item_set_parent isp where tad.test_admin_id = ? and icat.item_set_category_level = 4 and"
		+ " tad.product_id = product.product_id and product.parent_product_id = icat.framework_product_id"
		+ " and iset.item_set_category_id = icat.item_set_category_id and isp.item_set_id = iset.item_set_id"
		+ " and iset1.item_set_id = isp.parent_item_set_id";

	private String subSkillIrsInformation = "select sec_objid, sessionid, percent_obtained, points_obtained  from laslink_sec_obj_fact "
		+ " where studentid = ? and sessionid = ?";

	
	private String rosterAllItemDetails = " select dd.*, valid from  ( select distinct item.item_id as oasItemId,   tdisi.item_sort_order as itemIndex,   item.item_type as itemType,   item.correct_answer as itemCorrectResponse,   td.item_set_id as subtestId,  " +
	"  decode (td.item_set_name,'HABLANDO','Speaking','ESCUCHANDO', 'Listening','LECTURA','Reading','ESCRITURA','Writing', td.item_set_name) as subtestName " +
	" from   item,   item_set sec,   item_Set_category seccat,   item_set_ancestor secisa,   item_Set_item secisi,   item_set td,   item_set_ancestor tdisa,   item_set_item tdisi,   datapoint dp,   test_roster ros,   test_Admin adm,   test_catalog tc,   product prod,  student_item_set_status sis " +
	" where   ros.test_roster_id = ?   and adm.test_admin_id = ros.test_admin_id   and tc.test_catalog_id = adm.test_catalog_id   and prod.product_id = tc.product_id   and item.ACTIVATION_STATUS = 'AC'   and tc.ACTIVATION_STATUS = 'AC'  " +
	" and sec.item_Set_id = secisa.ancestor_item_Set_id   and sec.item_set_type = 'RE'   and secisa.item_set_id = secisi.item_Set_id   and item.item_id = secisi.item_id   and tdisi.item_id = item.item_id   and td.item_set_id = tdisi.item_set_id   and td.item_set_type = 'TD' " +
	" and tdisa.item_set_id = td.item_set_id   and adm.item_set_id = tdisa.ancestor_item_set_id   and seccat.item_Set_category_id = sec.item_set_category_id   and seccat.item_set_category_level = prod.sec_scoring_item_Set_level   and dp.item_id = item.item_id   and td.sample = 'F' " +
	" AND (td.item_set_level != 'L' OR PROD.PRODUCT_TYPE = 'TL')   and seccat.framework_product_id = prod.PARENT_PRODUCT_ID  and sis.test_roster_id  = ros.test_roster_id   " +
	" ) dd , ( select sis.item_set_id , decode(nvl(sis.validation_status, 'IN'), 'IN', 'NC', (decode(nvl(sis.exemptions, 'N'), 'Y',  'NC', ((decode(nvl(sis.absent, 'N'), 'Y', 'NC', 'CO')))))) valid from student_item_set_status sis where test_roster_id = ?" +
	" )dd1 where dd.subtestId = dd1.item_set_id order by subtestId, itemIndex";

	private String rosterAllSRItemsResponseDetails = " select irp.item_id, irp.response from item_response irp,  (select item_response.item_id item_id, item_set_id, max(response_seq_num) maxseqnum   from item_response , item  where test_roster_id = ?  and item_response.item_id =item.item_id and item.item_type = 'SR'and item.ACTIVATION_STATUS = 'AC' group by item_response.item_id, item_set_id ) derived  " +
	" where irp.item_id = derived.item_id    and irp.item_set_id = derived.item_set_id    and irp.response_seq_num = derived.maxseqnum    and irp.test_roster_id = ? ";

	private String rosterAllCRItemsResponseDetails = " select distinct response.item_id,points    from item_response_points irps,  (select irp.item_id, irp.item_response_id     from item_response irp,  " +
	" (select item_response.item_id,    item_set_id,     test_roster_id,    max(response_seq_num) maxseqnum  " +
	"  from item_response , item where test_roster_id = ?   and item_response.item_id =item.item_id and item.item_type = 'CR' and item.ACTIVATION_STATUS = 'AC' " +
	"  group by item_response.item_id, item_set_id, test_roster_id) derived   where irp.item_id = derived.item_id     and irp.item_set_id = derived.item_set_id   and irp.response_seq_num = derived.maxseqnum    and irp.test_roster_id = derived.test_roster_id    and irp.test_roster_id = ? ) response   where response.item_response_id = irps.item_response_id ";

	private String subtestIndicator = "SELECT CONL.SUBTEST_MODEL  FROM TEST_ROSTER              TR, TEST_ADMIN               TA, CUSTOMER_CONFIGURATION   CC, CUSTOMER_ORGNODE_LICENSE CONL, PRODUCT                  PROD WHERE TR.TEST_ROSTER_ID = ? AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID AND " +
	"TA.CUSTOMER_ID = CC.CUSTOMER_ID AND CC.CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription' AND CC.DEFAULT_VALUE = 'T' AND TA.PRODUCT_ID = PROD.PRODUCT_ID AND CONL.CUSTOMER_ID = CC.CUSTOMER_ID AND CONL.ORG_NODE_ID = TR.ORG_NODE_ID AND CONL.PRODUCT_ID = PROD.PARENT_PRODUCT_ID ";
	
	private String getFrameworkProductId = "SELECT PROD.PARENT_PRODUCT_ID FROM PRODUCT PROD, TEST_ADMIN TADMIN, TEST_ROSTER TR WHERE TR.TEST_ROSTER_ID = ? AND TR.TEST_ADMIN_ID = TADMIN.TEST_ADMIN_ID AND TADMIN.PRODUCT_ID = PROD.PRODUCT_ID";
		
		
	private int districtElementNumber = 0;
	private int schoolElementNumber = 0;
	private int classElementNumber = 0;
	private int sectionElementNumber = 0;
	private int groupElementNumber = 0;
	private int divisionElementNumber = 0;
	private int levelElementNumber = 0;
	private int studentElementNumber = 0;
	private String customerModelLevelValue = null;
	private String customerState = null;
	private String customerCity = null;
	private String testDate = null;
	private String programDate = null;
	
	@SuppressWarnings("unused")
	private static final String blank = "";
	private HashMap<String, String> subSkillAreaScoreInfo = new HashMap<String, String>();
	private HashMap<String, String> subSkillAreaItemCategory = new HashMap<String, String>();

	private static final String group = "GOAS";
	private static final String DATAFILE = "DATAFILE";
	private boolean isInvalidSpeaking = false;
	private boolean isInvalidListeing = false;
	private boolean isInvalidReading = false;
	private boolean isInvalidWriting = false;

	private Integer customerId = 0;
	static TreeMap<String, String> wrongMap = new TreeMap<String, String>();
	private List<String> fileNameList;
	private Integer userId;	
	
	
	static {
		
		wrongMap.put("A", "1");
		wrongMap.put("B", "2");
		wrongMap.put("C", "3");
		wrongMap.put("D", "4");
	}
	
	
	/*public static void main(String[] args) throws CTBBusinessException {
		FileGenerator example = new FileGenerator();

			System.out.println("Making TXT from POJO...");
			example.fileNameList = new ArrayList<String>();
			example.writeToText();

			System.out.println("END !");
		
	}*/
	
	public void execute (int customerId, Integer userId, List<String> fileNameList,  List<String> formettedTestRoster) throws CTBBusinessException{

			FileGeneratorForLL2ND fileGen2nd = new FileGeneratorForLL2ND();
			System.out.println("File generation started.");
			if(isLL2ND(formettedTestRoster)){
				fileGen2nd.writeToText(formettedTestRoster, new Integer(customerId),  userId, fileNameList);
			}else{
				this.customerId = customerId;
				this.fileNameList = fileNameList;
				this.userId = userId;
				writeToText( formettedTestRoster);
			}
			System.out.println("File generation completed.");

	}
	
	private void writeToText( List<String> formettedTestRoster) throws CTBBusinessException  {
		
		FlatFileWriter ffWriter = null;
		OrderFile orderFile = new OrderFile();
		System.out.println("Collecting data for report....");
		List<Tfil> myList = createList(orderFile,  formettedTestRoster);
		System.out.println("Data collected .");
		String localFilePath = Configuration.getLocalFilePath();
		String  formatedDate = fileDateOutputFormat.format(new Date()) ;
		
		String dataFileName = customerState + "_" + testDate.substring(0,6) + "_"
		+ customerId + "_" + orderFile.getOrgTestingProgram() + "_"
		+ orderFile.getCustomerName().trim() + "_" + group + "_"
		+ DATAFILE + "_" + formatedDate	+ ".dat";
		
		String orderFileName = dataFileName.substring(0, dataFileName.length() - 23);
		orderFileName = orderFileName + "ORDERFILE_"+ formatedDate + ".csv";
		
		
		
		if(!(new File(localFilePath)).exists()){
			File f = new File(localFilePath);
			f.mkdirs();
		}
		fileNameList.add(new File(localFilePath, dataFileName).getAbsolutePath());
		fileNameList.add(new File(localFilePath, orderFileName).getAbsolutePath());
		
		
		File file = new File(localFilePath, dataFileName);
		
		try{
			System.out.println("Preparing Data File.");
			ffWriter = new FileSystemFlatFileWriter(file, true);
			ffWriter.writeRecordList(myList);
			ffWriter.close();
			//System.out.println("Export file successfully generated:["+dataFileName+"]");
			EmailProcessorDao emailProcessorDao = new EmailProcessorDao();
			User user = emailProcessorDao.getUserDetails(userId);
			if( user !=null ) {
				orderFile.setSubmittersEmail(user.getEmail());
			}
			orderFile.setDataFileName(EmetricUtil.truncate(dataFileName,
					100).substring(0, dataFileName.length()));
			System.out.println("Data File ["+dataFileName+"] created.");
			
			System.out.println("Preparing Order File.");
			prepareOrderFile(orderFile, localFilePath, orderFileName);
			System.out.println("Order File ["+orderFileName+"] created.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CTBBusinessException("IOException while writing into data file:"+e.getMessage());
		} catch (FFPojoException e) {
			e.printStackTrace();
			throw new CTBBusinessException("IOException while writing into data file:"+e.getMessage());
		} finally {
			if(ffWriter!=null){
				try {
					ffWriter.close();
				} catch (IOException e) {
				}
			}
		}
		

	}

	/*
	 * private static List<TestAndHierarchy> createList() { List<TestAndHierarchy>
	 * myList = new ArrayList<TestAndHierarchy>(); DbQuery dbquery; try {
	 * dbquery = new DbQuery(); myList = dbquery.getDummyData(); } catch
	 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * return myList; }
	 */

	private boolean isLL2ND(List<String> formettedTestRoster)throws CTBBusinessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer testRosterId = null;
		Integer productId = null;
		try{
			ps =  SqlUtil.openOASDBcon().prepareStatement(getFrameworkProductId);
			testRosterId = Integer.parseInt((formettedTestRoster.toArray(new String[formettedTestRoster.size()]))[0]);
			ps.setInt(1, testRosterId.intValue());
			rs = ps.executeQuery(); 
			rs.next();
			productId = rs.getInt(1);
			if(productId != null && productId == 7500)
				return true;
			else
				return false;
		}catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at isLL2ND :"+e.getMessage());
		}finally {
			SqlUtil.close(ps, rs);
		}
	}

	private List<Tfil> createList(OrderFile orderFile,  List<String> formettedTestRoster) throws CTBBusinessException {
		List<TestRoster> myrosterList = new ArrayList<TestRoster>();
		List<CustomerDemographic> customerAccomList = new ArrayList<CustomerDemographic>();
		List<CustomerDemographic> customerDemoList = new ArrayList<CustomerDemographic>();
		List<Tfil> tfilList = new ArrayList<Tfil>();
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

		try {
			oascon = SqlUtil.openOASDBcon();
			irscon = SqlUtil.openIRSDBcon();
			generateModelLevel(oascon);
			customerDemoList = getCustomerDemographic(oascon);
			Set<CustomerDemographic> set = new HashSet<CustomerDemographic>(
					customerDemoList);
			customerAccomList = getCustomerLeveledDemographicValue(oascon);
			Set<CustomerDemographic> setAccomodation = new HashSet<CustomerDemographic>(
					customerAccomList);

			for (CustomerDemographic c : set) {

				customerDemographic.put(c.getCustomerDemographicId(), c
						.getLabelName());
			}
			populateCustomer(oascon, orderFile);
			//myrosterList = getTestRoster(oascon);
			for (String rosterIdIn : formettedTestRoster) {
				//System.out.println("111:"+rosterIdIn);
				myrosterList = getTestRosterFromID(oascon, rosterIdIn);
				for (TestRoster roster : myrosterList) {
					
					System.out.println("Processing started for roster:"+roster);
					Tfil tfil = new Tfil();

					Student st = roster.getStudent();

					setStudentList(tfil, st);

					// Accomodations
					Accomodations accomodations = createAccomodations(st
							.getStudentDemographic(), setAccomodation,
							customerDemographic, tfil);
					tfil.setAccomodations(accomodations);

					tfil.setModelLevel(customerModelLevelValue);
					tfil.setState(this.customerState);
					tfil.setCity(this.customerCity);
					// System.out.println("roster id "+ roster.getTestRosterId()
					// +" : : "+ roster.getTestAdminId());
					// org node
					createOrganization(oascon, tfil, roster.getStudentId(),
							districtMap, schoolMap, classMap, sectionMap, groupMap, divisionMap, levelMap, orderFile);
					// create test Session
					createTestSessionDetails(oascon, tfil, roster
							.getTestRosterId(), orderFile);
					// create studentItemSetstatus
					createStudentItemStatusDetails(oascon, tfil, roster
							.getTestRosterId(), roster.getStudentId());
					System.out.println("Processing score for roster:"+roster);
					// added for Skill Area Score
					createSkillAreaScoreInformation(irscon, tfil, roster);

					createSubSkillAreaScoreInformation(oascon, irscon, tfil,
							roster);
					// createItemResponseInformation(oascon, roster,tfil); //
					// for emetric research analysis
					createSubtestIndicatorFlag(oascon, roster.getTestRosterId(), tfil);
					tfilList.add(tfil);
					studentCount++;
					System.out.println("Processing completed for roster:"+roster);
				}
			}
			
			
			
			/** ***************** */
			orderFile.setCaseCount(studentCount.toString());

		} finally {
			SqlUtil.close(oascon);
			SqlUtil.close(irscon);

		}
		// throw again the first exception
		return tfilList;
	}

	@SuppressWarnings("unused")
	private void createItemResponseInformation(Connection oascon,
			TestRoster roster, Tfil tfil) throws CTBBusinessException  {
		Map<String, TreeMap<String, LinkedList<RostersItem>>> allItems = getItemResponseGrt(
				oascon, roster);
		//System.out.println("roster"+roster.getTestRosterId());
		ItemResponsesGRT responsesGRT = new ItemResponsesGRT();
		String speakingMCItems = "";
		String speakingCRItems = "";
		
		String listeningMCItems = "";
		String readingMCItems = "";
		
		String writingMCItems = "";
		String writingCRItems = "";
		

		/*******************************************************
		 * speaking
		 *******************************************************/
		if (allItems.get("SPEAKING") != null) {
			TreeMap<String, LinkedList<RostersItem>> speakingitem = allItems
					.get("SPEAKING");
			
			/// for sr items
			if (speakingitem.get("SR") != null) {
				speakingMCItems = constractSRItemResponseString(speakingMCItems, speakingitem); 
			}

			responsesGRT.setSpeakingMCItems(speakingMCItems);

			if (speakingitem.get("CR") != null) {
				speakingCRItems = constractCRItemResponseString(speakingCRItems, speakingitem); 
    		}

			responsesGRT.setSpeakingCRItems(speakingCRItems);

		} else {
			responsesGRT.setSpeakingMCItems("");
			responsesGRT.setSpeakingMCItems("");
		}
		

		/*******************************************************
		 * listening
		 *******************************************************/
		if (allItems.get("LISTENING") != null) {
			TreeMap<String, LinkedList<RostersItem>> listeningitem = allItems
					.get("LISTENING");
			// / for sr items
			if (listeningitem.get("SR") != null) {
				listeningMCItems = constractSRItemResponseString(listeningMCItems, listeningitem); 
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
				readingMCItems = constractSRItemResponseString(readingMCItems, readingitem); 
			}

			responsesGRT.setReadingMCItems(readingMCItems);

		} else {
			responsesGRT.setReadingMCItems("");
		}
		
		/*******************************************************
		 * speaking
		 *******************************************************/
		if (allItems.get("WRITING") != null) {
			TreeMap<String, LinkedList<RostersItem>> writingitem = allItems
					.get("WRITING");
			
			/// for sr items
			if (writingitem.get("SR") != null) {
				writingMCItems = constractSRItemResponseString(writingMCItems, writingitem); 
			}

			responsesGRT.setWritingMCItems(writingMCItems);

			if (writingitem.get("CR") != null) {
				writingCRItems = constractCRItemResponseString(writingCRItems, writingitem); 
    		}

			responsesGRT.setWritingCRItems(writingCRItems);

		} else {
			responsesGRT.setWritingMCItems("");
			responsesGRT.setWritingCRItems("");
		}

		
		tfil.setItemResponseGRT(responsesGRT);
	}
	

	private String constractCRItemResponseString(String speakingCRItems,
			TreeMap<String, LinkedList<RostersItem>> speakingitem) {
		LinkedList<RostersItem> itemlist = speakingitem.get("CR");
		for (RostersItem item : itemlist) {
			//System.out.println(item);
			if (item.isItemValidateForScoring()
					&& (item.getStudentResponse() != null && item
							.getStudentResponse().trim().length() > 0)) {
				speakingCRItems+=item.getStudentResponse();	
			} else {
				speakingCRItems += " ";
			}
		}
		return speakingCRItems;
	}
	
	

	private String constractSRItemResponseString(String speakingMCItems, TreeMap<String, LinkedList<RostersItem>> speakingitem ) {
		LinkedList<RostersItem> itemlist = speakingitem.get("SR");
		for (RostersItem item : itemlist) {
			//System.out.println(item);
			if (item.isItemValidateForScoring()
					&& (item.getStudentResponse() != null && item
							.getStudentResponse().trim().length() > 0)) {
				if (!item.getItemCorrectResponse().equalsIgnoreCase(
						item.getStudentResponse())) {
					speakingMCItems += wrongMap.get(item
							.getStudentResponse().trim());
				} else {
					speakingMCItems += item.getStudentResponse().trim();
				}

			} else {
				speakingMCItems += " ";
			}
		}
		
		return speakingMCItems;

	}
	
	
	

	private Map<String,TreeMap<String,  LinkedList<RostersItem>>> getItemResponseGrt(Connection oascon, TestRoster roster) throws CTBBusinessException {
		Map<String,TreeMap<String,  LinkedList<RostersItem>>> allitem =  new TreeMap<String,TreeMap<String,  LinkedList<RostersItem>>> ();
		PreparedStatement ps = null ;
		ResultSet rs = null;
		 Map<String,RostersItem> srValueMap = new TreeMap<String,RostersItem>();
		 Map<String,RostersItem> crValueMap = new TreeMap<String,RostersItem>();
		try{
			ps = oascon.prepareStatement(rosterAllItemDetails);
			ps.setInt(1, roster.getTestRosterId());
			ps.setInt(2, roster.getTestRosterId());
			rs = ps.executeQuery(); 
			rs.setFetchSize(500);
			while (rs.next()){
				
				RostersItem item = new RostersItem();
				item.setItemId(rs.getString(1));
				item.setItemIndx(rs.getString(2));
				item.setItemType(rs.getString(3));
				item.setItemCorrectResponse(rs.getString(4));
				item.setItemSetIdTD(rs.getString(5));				
				item.setItemDescriptio(rs.getString(6));
				item.setItemValidationStatusForScoring(rs.getString(7));
				populateMap(allitem, item, srValueMap,crValueMap );
			}
			
			//System.out.println("populateCustomer");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getItemResponseGrt:"+e.getMessage());
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		
		try{
			ps = oascon.prepareStatement(rosterAllSRItemsResponseDetails);
			ps.setInt(1, roster.getTestRosterId());
			ps.setInt(2, roster.getTestRosterId());
			rs = ps.executeQuery(); rs.setFetchSize(500);
			while (rs.next()){
				RostersItem  item= srValueMap.get(rs.getString(1));
				item.setStudentResponse(rs.getString(2));
			}
			
			//System.out.println("populateCustomer");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getItemResponseGrt1:"+e.getMessage());
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		
		try{
			ps = oascon.prepareStatement(rosterAllCRItemsResponseDetails);
			ps.setInt(1, roster.getTestRosterId());
			ps.setInt(2, roster.getTestRosterId());
			rs = ps.executeQuery(); rs.setFetchSize(500);
			while (rs.next()){
				RostersItem  item= crValueMap.get(rs.getString(1));
				item.setStudentResponse(rs.getString(2));
			}
			
			//System.out.println("populateCustomer");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getItemResponseGrt2:"+e.getMessage());
			
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


	@SuppressWarnings("unused")
	private List<TestRoster> getTestRoster(Connection con) throws CTBBusinessException {
		PreparedStatement ps = null ;
		ResultSet rs = null;
		 List<TestRoster> rosterList = new ArrayList<TestRoster>();
		try{
			ps = con.prepareStatement(testRosterSql);
			ps.setInt(1, customerId);
			rs = ps.executeQuery(); 
			rs.setFetchSize(500);
			while (rs.next()){
				TestRoster ros = new TestRoster();
				ros.setTestRosterId(rs.getInt(1));
				ros.setActivationStatus(rs.getString(2));
				ros.setTestCompletionStatus(rs.getString(3));
				ros.setCustomerId(customerId);
				ros.setStudentId(rs.getInt(5));
				ros.setTestAdminId(rs.getInt(6));
				ros.setStudent(getStudent(con,rs.getInt(5))); 
				rosterList.add(ros);
			
			}
			
			//System.out.println("populateCustomer");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getTestRoster:"+e.getMessage());
		}finally {
			SqlUtil.close(ps, rs);
		}
		return rosterList;
	}
	
	
	private List<TestRoster> getTestRosterFromID(Connection con,
			String rosterIdIn) throws CTBBusinessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TestRoster> rosterList = new ArrayList<TestRoster>();

		String testRosterByIDSqlUpdated = testRosterWithStudentByRosterIDSql.replace(
				"<#ROSTER_ID_LIST#>", rosterIdIn);
		//System.out.println(testRosterByIDSqlUpdated);

		try {
			ps = con.prepareStatement(testRosterByIDSqlUpdated);
			rs = ps.executeQuery();
			rs.setFetchSize(500);
			while (rs.next()) {
				TestRoster ros = new TestRoster();
				Student std = new Student();
				ros.setTestRosterId(rs.getInt(1));
				ros.setActivationStatus(rs.getString(2));
				ros.setTestCompletionStatus(rs.getString(3));
				ros.setCustomerId(customerId);
				ros.setStudentId(rs.getInt(4));
				std.setStudentId(rs.getInt(4));
				ros.setTestAdminId(rs.getInt(5));
				ros.setStudent(std);
				
				
				
				std.setFirstName(rs.getString(6));
				std.setLastName(rs.getString(7));
				std.setMiddleName(rs.getString(8));
				std.setBirthDate(rs.getDate(9));
				std.setGender(rs.getString(10));
				std.setGrade(rs.getString(11));
				std.setCustomerId(customerId);
				std.setTestPurpose(rs.getString(12));
				std.setExtStudentId(rs.getString(13));
				
				std.setStudentContact(getStudentContact(con, std.getStudentId()));
				std
				.setStudentDemographic(getStudentDemographic(con,std.getStudentId()));
				
				
				rosterList.add(ros);
			}

			// System.out.println("populateCustomer");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getTestRoster:"
					+ e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return rosterList;
	}

	private Student getStudent(Connection con, int studentId)
	throws  CTBBusinessException {
		Student std = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(studentSql);
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
				std
				.setStudentDemographic(getStudentDemographic(con,
						studentId));

			}

			// System.out.println("getStudent");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getStudent:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return std;
	}

	private Set<StudentDemographic> getStudentDemographic(Connection con,
			int studentId) throws CTBBusinessException {
		Set<StudentDemographic> studentDemographicSet = new HashSet<StudentDemographic>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(studentDemographicSql);
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

			// System.out.println("getStudentDemographic:"+studentId+"::"+studentDemographicSet);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getStudentDemographic:"+e.getMessage());
			
		} finally {
			SqlUtil.close(ps, rs);
		}

		return studentDemographicSet;
	}

	private Set<StudentContact> getStudentContact(Connection con, int studentId) throws CTBBusinessException {
		Set<StudentContact> studentContact = new HashSet<StudentContact>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(studentContactSql);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			while (rs.next()) {
				StudentContact sdentCon = new StudentContact();
				sdentCon.setStudentContactId(rs.getInt(2));
				sdentCon.setCity(rs.getString(3));
				sdentCon.setState(rs.getString(4));
				studentContact.add(sdentCon);

			}

			// System.out.println("getstudentContact");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getStudentContact:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return studentContact;
	}

	private void populateCustomer(Connection con, OrderFile orderFile) throws CTBBusinessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(customersql);
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

			// System.out.println("populateCustomer");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at populateCustomer:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
	}

	private List<CustomerDemographic> getCustomerDemographic(Connection con) throws CTBBusinessException {
		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		/*
		 * Criteria crit = session.createCriteria(CustomerDemographic.class);
		 * crit.add(Expression.eq("customerId", customerId)); myList =
		 * crit.list();
		 */
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(customerDemographicsql);
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

			// System.out.println("getCustomerDemographic:"+myList);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getCustomerDemographic:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return myList;
	}

	private Set<CustomerDemographicValue> getCustomerDemographicValue(
			Connection con, int customerDemographicId) throws CTBBusinessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Set<CustomerDemographicValue> customerDemographicValue = new HashSet<CustomerDemographicValue>();
		try {
			ps = con.prepareStatement(customerDemographiValuecsql);
			ps.setInt(1, customerDemographicId);
			rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDemographicValue cdv = new CustomerDemographicValue();
				cdv.setValueName(rs.getString(1));
				cdv.setValueCode(rs.getString(2));
				cdv.setCustomerDemographicId(rs.getInt(3));
				customerDemographicValue.add(cdv);
			}

			// System.out.println("customerDemographicValue");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getCustomerDemographicValue:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return customerDemographicValue;

	}

	private List<CustomerDemographic> getCustomerLeveledDemographicValue(
			Connection con) throws CTBBusinessException {

		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(customerDemographicsqlWithLevel);
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

			// System.out.println("myList");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at getCustomerLeveledDemographicValue:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);

		}

		return myList;

	}

	private Accomodations createAccomodations(Set<StudentDemographic> sd,
			Set<CustomerDemographic> cd,
			HashMap<Integer, String> customerDemographic, Tfil tfil) {

		TreeMap<String, StudentDemographic> set1 = new TreeMap<String, StudentDemographic>();
		TreeMap<String, CustomerDemographic> set2 = new TreeMap<String, CustomerDemographic>();
		HashMap<Integer, String> studentDemographic = new HashMap<Integer, String>();
		Accomodations accomodations = new Accomodations();
		SpecialCodes specialCodes = new SpecialCodes();
		// For Defect Fix 66411
		for (StudentDemographic studentDem : sd) {
			if (studentDem.getValue() != null){
				set1.put(studentDem.getValue(), studentDem);
			}
			studentDemographic.put(studentDem.getCustomerDemographicId(),
					studentDem.getValueName());
			if (customerDemographic.containsKey(studentDem
					.getCustomerDemographicId())) {
				String customerDemoName = customerDemographic.get(studentDem
						.getCustomerDemographicId());

				if (customerDemoName.equalsIgnoreCase("Ethnicity")) {
					setEthnicity(sd, tfil);
				} else if (customerDemoName.equalsIgnoreCase("Disability")) {
					setDisabilityCode(sd, tfil);
				} else if (customerDemoName.startsWith("Home")) {
					tfil.setHomeLanguage(studentDem.getValue());
				} else if (customerDemoName.startsWith("USA")) {
					tfil.setUsaSchoolEnrollment(studentDem.getValueName());
				} else if (customerDemoName.startsWith("Program")) {
					setProgramParticipation(sd, tfil);
				} else if (customerDemoName
						.equalsIgnoreCase("Special Education")) {
					setSpecialEducation(sd, tfil);
				} else if (customerDemoName.equalsIgnoreCase("Mobility")) {
					tfil.setMobilityGrade(EmetricUtil.formatGrade(studentDem
							.getValueName()));
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
				}

			}
			tfil.setUserDefinedBioPage(specialCodes);

		}
		for (CustomerDemographic customerDem : cd) {
			set2.put(customerDem.getLabelName(), customerDem);
			// System.out.println("customerDem.getLabelName:: " +
			// customerDem.getLabelName());
		}

		for (Map.Entry<String, StudentDemographic> entry : set1.entrySet()) {
			for (Map.Entry<String, CustomerDemographic> entry1 : set2
					.entrySet()) {
				Set<CustomerDemographicValue> set = entry1.getValue()
				.getCustomerDemographicValue();
				for (CustomerDemographicValue value : set) {
					if (value.getValueCode().trim().equalsIgnoreCase(
							entry.getKey().trim())) {
						String string = value.getValueCode().replace('-', '_');

						try {
							accomodations.getClass().getMethod("set" + string,
									String.class).invoke(accomodations,
									"1");
							break;
						} catch (Exception e) {
							e.printStackTrace();

						}
					}
				}

			}

		}

		return accomodations;

	}

	private void setStudentList(Tfil tfil, Student st) {
		HashMap<Integer, Integer> studentMap = new HashMap<Integer, Integer>();

		Integer integer = studentMap.get(st.getStudentId());
		if (integer == null) {
			integer = ++studentElementNumber;
			studentMap.put(st.getStudentId(), integer);

		}
		tfil.setStudentElementNumber(studentMap.get(st.getStudentId())
				.toString());
		tfil.setStudentLastName(st.getLastName());
		tfil.setStudentFirstName(st.getFirstName());
		tfil.setStudentGender(st.getGender());
		tfil.setStudentMiddleName(st.getMiddleName());

		tfil.setExtStudentId(st.getExtStudentId());
		tfil.setStudentGradeFromAnswerSheets(EmetricUtil.formatGrade(st
				.getGrade()));
		tfil.setGrade(EmetricUtil.formatGrade(st.getGrade()));
		tfil.setStudentBirthDate(st.getBirthDate());

		tfil.setStudentChronologicalAge(EmetricUtil.calculateAge(st
				.getBirthDate()));

		tfil.setPurposeOfTestp(st.getTestPurpose());

	}

	private void setEthnicity(Set<StudentDemographic> sd, Tfil tfil) {

		for (StudentDemographic studentDemo : sd) {
			if (studentDemo.getValueName().startsWith("American")) {
				tfil.setEthinicity("1");
			} else if (studentDemo.getValueName().startsWith("African")) {
				tfil.setEthinicity("2");
			} else if (studentDemo.getValueName().startsWith("Asian")) {
				tfil.setEthinicity("3");
			} else if (studentDemo.getValueName().startsWith("Pacific")) {
				tfil.setEthinicity("4");
			} else if (studentDemo.getValueName().startsWith("Hispanic")) {
				tfil.setEthinicity("5");
			} else if (studentDemo.getValueName().startsWith("White")) {
				tfil.setEthinicity("6");
			} else if (studentDemo.getValueName().startsWith("Multiethnic")) {
				tfil.setEthinicity("7");
			} else if (studentDemo.getValueName().startsWith("Other")) {
				tfil.setEthinicity("8");
			} else if (studentDemo.getValueName().startsWith("mexicano")) {
				tfil.setEthinicity("1");
			} else if (studentDemo.getValueName().startsWith(
			"mexicano-americano")) {
				tfil.setEthinicity("2");
			} else if (studentDemo.getValueName().startsWith("cubano")) {
				tfil.setEthinicity("3");
			} else if (studentDemo.getValueName()
					.startsWith("cubano-americano")) {
				tfil.setEthinicity("4");
			} else if (studentDemo.getValueName().startsWith("puertorrique")) {
				tfil.setEthinicity("5");
			} else if (studentDemo.getValueName().startsWith("dominicano")) {
				tfil.setEthinicity("6");
			} else if (studentDemo.getValueName().startsWith("centroamericano")) {
				tfil.setEthinicity("7");
			} else if (studentDemo.getValueName().startsWith("sudamericano")) {
				tfil.setEthinicity("8");
			} else if (studentDemo.getValueName().startsWith("otro")) {
				tfil.setEthinicity("9");
			}
		}
	}

	private void setDisabilityCode(Set<StudentDemographic> sd, Tfil tfil) {

		for (StudentDemographic studentDemo : sd) {
			if (studentDemo.getValueName().equalsIgnoreCase("Autism")) {
				tfil.setDisability("1");
			} else if (studentDemo.getValueName().startsWith("Deaf")) {
				tfil.setDisability("2");
			} else if (studentDemo.getValueName().equalsIgnoreCase("Hearing Impairment")) {
				tfil.setDisability("3");
			} else if (studentDemo.getValueName().equalsIgnoreCase("Multiple Disabilities")) {
				tfil.setDisability("4");
			} else if (studentDemo.getValueName().equalsIgnoreCase("Orthopedic Impairment")) {
				tfil.setDisability("5");
			} else if (studentDemo.getValueName().equalsIgnoreCase("Other Health Impairments")) {
				tfil.setDisability("6");
			} else if (studentDemo.getValueName().equalsIgnoreCase("Serious Emotional Disturbance")) {
				tfil.setDisability("7");
			} else if (studentDemo.getValueName().equalsIgnoreCase("Learning Disability")) {
				tfil.setDisability("8");
			} else if (studentDemo.getValueName().equalsIgnoreCase("Speech or Language Impairment")) {
				tfil.setDisability("9");
			} else if (studentDemo.getValueName().equalsIgnoreCase("Traumatic Brain Injury")) {
				tfil.setDisability("A");
			} else if (studentDemo.getValueName().equalsIgnoreCase("Visual Impairment")) {
				tfil.setDisability("B");
			} else if (studentDemo.getValueName().equalsIgnoreCase("Mental Retardation")) {
				tfil.setDisability("C");
			}
		}
	}

	private void setProgramParticipation(Set<StudentDemographic> sd, Tfil tfil) {

		for (StudentDemographic studentDemo : sd) {
			if (studentDemo.getValueName().startsWith("ESEA")) {
				tfil.setEseaTitle1("1");
			} else if (studentDemo.getValueName()
					.startsWith("English Language")) {
				tfil.setEllEseaTitle("1");
			} else if (studentDemo.getValueName().startsWith("Gifted")) {
				tfil.setGiftedAndTalented("1");
			} else if (studentDemo.getValueName().startsWith("Indian Edu")) {
				tfil.setIndianEducation("1");
			} else if (studentDemo.getValueName().startsWith("Migrant")) {
				tfil.setMigrantEducation("1");
			}
		}
	}

	private void setSpecialEducation(Set<StudentDemographic> sd, Tfil tfil) {

		for (StudentDemographic studentDemo : sd) {
			if (studentDemo.getValueName().equalsIgnoreCase("IEP")) {
				tfil.setIep("1");
			} else if (studentDemo.getValueName().equalsIgnoreCase("504")) {
				tfil.setSbi504("1");
			}
		}
	}

	private void generateModelLevel(Connection conn) throws CTBBusinessException  {
		/*
		 * Query query1 = session.createSQLQuery(customerModelLevel).addScalar(
		 * "modelLevel", Hibernate.STRING).setInteger("customerId", customerId);
		 * 
		 * this.customerModelLevelValue = query1.uniqueResult().toString();
		 */
		String modelLevel = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(customerModelLevel);
			ps.setInt(1, customerId);
			rs = ps.executeQuery();
			if (rs.next()) {
				modelLevel = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at generateModelLevel:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		this.customerModelLevelValue = modelLevel;
	}

	private void createOrganization(Connection con, Tfil tfil,
			Integer studentId, HashMap<String, Integer> districtMap,
			HashMap<String, Integer> schoolMap,
			HashMap<String, Integer> classMap,
			HashMap<String, Integer> sectionMap,
			HashMap<String, Integer> groupMap,
			HashMap<String, Integer> divisionMap,
			HashMap<String, Integer> levelMap, OrderFile orderFile) throws CTBBusinessException {

		TreeMap<Integer, String> organizationMap = new TreeMap<Integer, String>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		//Integer count= null;
		try {
			ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
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

			// Entry<Integer, String> lastEntry = organizationMap.lastEntry();
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
					tfil.setElementALabel(rs.getString(2));
					Integer integer = districtMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++districtElementNumber;
						districtMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberA(String.valueOf(districtMap.get(rs
							.getString(4))));
					if (rs.getString(3) != null)
						tfil.setElementSpecialCodesA(rs.getString(3));
					tfil.setOrganizationId("XX" + rs.getString(1));
					tfil.setCustomerId(rs.getString(1));
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
					tfil.setElementBLabel(rs.getString(2));
					Integer integer = schoolMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++schoolElementNumber;
						schoolMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberB(String.valueOf(schoolMap.get(rs
							.getString(4))));
					if (rs.getString(3) != null)
						tfil.setElementSpecialCodesB(rs.getString(3));
					tfil.setElementStructureLevelB("02");
					tfil.setSchoolId(rs.getString(1));
				}
	
				else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 4).toString() != null
						&& rs.getString(5)
						.equalsIgnoreCase(
								new Integer(organizationMapSize - 4)
								.toString())) {
					tfil.setElementNameC(rs.getString(4));
					tfil.setElementCLabel(rs.getString(2));
					Integer integer = classMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++classElementNumber;
						classMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberC(String.valueOf(classMap.get(rs
							.getString(4))));
					if (rs.getString(3) != null)
						tfil.setElementSpecialCodesC(rs.getString(3));
					tfil.setElementStructureLevelC("03");
					tfil.setClassId(rs.getString(1));
	
				}
				else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 3).toString() != null
						&& rs.getString(5)
						.equalsIgnoreCase(
								new Integer(organizationMapSize - 3)
								.toString())) {
					tfil.setElementNameD(rs.getString(4));
					tfil.setElementDLabel(rs.getString(2));
					Integer integer = sectionMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++sectionElementNumber;
						sectionMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberD(String.valueOf(sectionMap.get(rs
							.getString(4))));
					if (rs.getString(3) != null)
						tfil.setElementSpecialCodesD(rs.getString(3));
					tfil.setElementStructureLevelD("04");
					tfil.setSectionId(rs.getString(1));
	
				}
				else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 2).toString() != null
						&& rs.getString(5)
						.equalsIgnoreCase(
								new Integer(organizationMapSize - 2)
								.toString())) {
					tfil.setElementNameE(rs.getString(4));
					tfil.setElementELabel(rs.getString(2));
					Integer integer = groupMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++groupElementNumber;
						groupMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberE(String.valueOf(groupMap.get(rs
							.getString(4))));
					if (rs.getString(3) != null)
						tfil.setElementSpecialCodesE(rs.getString(3));
					tfil.setElementStructureLevelE("05");
					tfil.setGroupId(rs.getString(1));
	
				}
				else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 1).toString() != null
						&& rs.getString(5).equalsIgnoreCase(
								new Integer(organizationMapSize - 1)
								.toString())) {
					tfil.setElementNameF(rs.getString(4));
					tfil.setElementFLabel(rs.getString(2));
					Integer integer = divisionMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++divisionElementNumber;
						divisionMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberF(String.valueOf(divisionMap.get(rs
							.getString(4))));
					if (rs.getString(3) != null)
						tfil.setElementSpecialCodesF(rs.getString(3));
					tfil.setElementStructureLevelF("06");
					tfil.setDivisionId(rs.getString(1));
	
				}
				else if (rs.getString(5) != null
						&& rs.getString(5).equalsIgnoreCase(
								organizationMapSize.toString())) {
					tfil.setElementNameG(rs.getString(4));
					tfil.setElementGLabel(rs.getString(2));
					Integer integer = levelMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++levelElementNumber;
						levelMap.put(rs.getString(4), integer);
	
					}
					tfil.setElementNumberG(String.valueOf(levelMap.get(rs
							.getString(4))));
					if (rs.getString(3) != null)
						tfil.setElementSpecialCodesG(rs.getString(3));
					tfil.setElementStructureLevelG("07");
					tfil.setLeafLevelId(rs.getString(1));
				}
		}

			// System.out.println("createOrganization");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at createOrganization:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		// For defect Fix 66410
		/*if (tfil.getElementNameB() == null) {
			tfil.setElementNameB(tfil.getElementNameC());
			tfil.setElementNumberB(tfil.getElementNumberC());
			tfil.setElementSpecialCodesB(tfil.getElementSpecialCodesC());
			tfil.setElementStructureLevelB("02");
			tfil.setSchoolId(tfil.getClassId());

			tfil.setElementNameA(tfil.getElementNameC());
			tfil.setElementNumberA(tfil.getElementNumberC());
			tfil.setElementSpecialCodesA(tfil.getElementSpecialCodesC());
			tfil.setOrganizationId("XX" + tfil.getClassId());
			tfil.setCustomerId(tfil.getClassId());
			tfil.setElementStructureLevelA("01");
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getClassId());

		} else if (tfil.getElementNameA() == null) {
			tfil.setElementNameA(tfil.getElementNameB());
			tfil.setElementNumberA(tfil.getElementNumberB());
			tfil.setElementSpecialCodesA(tfil.getElementSpecialCodesB());
			tfil.setOrganizationId("XX" + tfil.getSchoolId());
			tfil.setCustomerId(tfil.getSchoolId());
			tfil.setElementStructureLevelA("01");
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getSchoolId());
		}*/
		//Added for expand to 7 level.
		if (tfil.getElementNameF() == null) {
			tfil.setOrganizationId("XX" + tfil.getLeafLevelId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getLeafLevelId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameG(), 30));
			
		}else if (tfil.getElementNameE() == null) {
			tfil.setOrganizationId("XX" + tfil.getDivisionId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getDivisionId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameF(), 30));
			
		}else if (tfil.getElementNameD() == null) {
			tfil.setOrganizationId("XX" + tfil.getGroupId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getGroupId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameE(), 30));
			
		}else if (tfil.getElementNameC() == null) {
			tfil.setOrganizationId("XX" + tfil.getSectionId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getSectionId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameD(), 30));
			
		}else if (tfil.getElementNameB() == null) {
			tfil.setOrganizationId("XX" + tfil.getClassId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getClassId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameC(), 30));
			
		}else if (tfil.getElementNameA() == null) {
			tfil.setOrganizationId("XX" + tfil.getSchoolId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getSchoolId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameB(), 30));
			
		}		

		if (orderFile.getOrgTestingProgram() == null)
			orderFile.setOrgTestingProgram(tfil.getOrganizationId());
		if (orderFile.getCustomerName() == null)
			orderFile.setCustomerName(EmetricUtil.truncate(tfil
					.getElementNameA(), 30));

	}

	private void createTestSessionDetails(Connection con, Tfil tfil,
			Integer rosterId, OrderFile orderFile) throws CTBBusinessException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(testSessionSQl);
			ps.setInt(1, rosterId);
			rs = ps.executeQuery();
			while (rs.next()) {

				if (rs.getString(1).equalsIgnoreCase("A")) {
					tfil.setTestName("LAS LINKS OPERATIONAL");
					tfil.setTestForm("A");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								"LAS Links", new Integer(10))
								.toUpperCase());
				} else if (rs.getString(1).equalsIgnoreCase("B")) {
					tfil.setTestName("LAS LINKS OPERATIONAL");
					tfil.setTestForm("B");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								"LAS Links", new Integer(10))
								.toUpperCase());
				} else if (rs.getString(1).equalsIgnoreCase("Espanol")) {
					tfil.setTestName("LAS LINKS OPERATIONAL");
					tfil.setTestForm("S");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								"ESPANOL", new Integer(10))
								.toUpperCase());
				} else if (rs.getString(1).startsWith("Esp")) {
					tfil.setTestName("LAS LINKS OPERATIONAL");
					tfil.setTestForm("S");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								"ESPANOL", new Integer(10))
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
					
				if(rs.getString(5) != null){
				tfil.setDateTestingCompleted(EmetricUtil.getTimeZone(rs.getString(5).toString(),rs.getString(3).toString(),false));
				}
				
				if(rs.getString(6) != null && this.programDate == null){
					this.programDate = EmetricUtil.getTimeZone(rs.getString(6).toString(),rs.getString(3).toString(),true);
				}


			}

			if (orderFile.getTestDate() == null && this.programDate != null){
				orderFile.setTestDate(this.programDate.substring(0, 6));
			}


			// System.out.println("createTestSessionDetails");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at createTestSessionDetails:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

	}

	private void createStudentItemStatusDetails(Connection con, Tfil tfil,
			Integer rosterId, Integer studentId) throws CTBBusinessException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(testRosterDetails);
			ps.setInt(1, studentId);
			ps.setInt(2, rosterId);
			rs = ps.executeQuery();
			while (rs.next()) {

				if (rs.getString(5).equalsIgnoreCase("Speaking")) {
					tfil.setTestInvalidationSpeaking(rs.getString(1)
							.equalsIgnoreCase("IN") ? "1" : " ");
					tfil.setTestingExemptionsSpeaking(rs.getString(3)
							.equalsIgnoreCase("Y") ? "1" : " ");
					tfil.setAbsentSpeaking(rs.getString(4)
							.equalsIgnoreCase("Y") ? "1" : " ");
				} else if (rs.getString(5).toString().equalsIgnoreCase(
				"Listening")) {
					tfil.setTestInvalidationListening(rs.getString(1)
							.toString().equalsIgnoreCase("IN") ? "1" : " ");
					tfil.setTestingExemptionsListening(rs.getString(3)
							.toString().equalsIgnoreCase("Y") ? "1" : " ");
					tfil.setAbsentListening(rs.getString(4).toString()
							.equalsIgnoreCase("Y") ? "1" : " ");
				} else if (rs.getString(5).toString().equalsIgnoreCase(
				"Reading")) {
					tfil.setTestInvalidationReading(rs.getString(1).toString()
							.equalsIgnoreCase("IN") ? "1" : " ");
					tfil.setTestingExemptionsReading(rs.getString(3).toString()
							.equalsIgnoreCase("Y") ? "1" : " ");
					tfil.setAbsentReading(rs.getString(4).toString()
							.equalsIgnoreCase("Y") ? "1" : " ");
				} else if (rs.getString(5).toString().equalsIgnoreCase(
				"Writing")) {
					tfil.setTestInvalidationWriting(rs.getString(1).toString()
							.equalsIgnoreCase("IN") ? "1" : " ");
					tfil.setTestingExemptionsWriting(rs.getString(3).toString()
							.equalsIgnoreCase("Y") ? "1" : " ");
					tfil.setAbsentWriting(rs.getString(4).toString()
							.equalsIgnoreCase("Y") ? "1" : " ");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at createStudentItemStatusDetails:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

	}

	private void createSkillAreaScoreInformation(Connection con, Tfil tfil,
			TestRoster roster) throws CTBBusinessException {
		TreeMap<String, Object[]> treeMap = new TreeMap<String, Object[]>();
		HashMap<String,String> contentAreaFact = new HashMap<String, String>();
		boolean isComprehensionPopulated = true;
		boolean isOralPopulated = true;
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
		ProficiencyLevels pl = new ProficiencyLevels();
		ScaleScores ss = new ScaleScores();
		SkillAreaNumberCorrect po = new SkillAreaNumberCorrect();
		SkillAreaPercentCorrect pc = new SkillAreaPercentCorrect();
		String scaleScoreOverall;
		String profLevelScoreOverall;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		/* String[] skillAreaScoreInfoOverAll = new String[2]; */
		try {
			ps2 = con.prepareStatement(scoreSkilAreaOverAllSQL);
			ps2.setInt(1, roster.getStudentId());
			ps2.setInt(2, roster.getTestAdminId());
			rs2 = ps2.executeQuery();
			if (rs2.next()) {
				scaleScoreOverall = rs2.getString(1);
				profLevelScoreOverall = rs2.getString(2);

			} else {
				scaleScoreOverall = "";
				profLevelScoreOverall ="";
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at createSkillAreaScoreInformation:"+e.getMessage());
		} finally {
			SqlUtil.close(ps2, rs2);
		}

		try {
			ps = con.prepareStatement(scoreSkilAreaSQL);
			ps.setInt(1, roster.getStudentId());
			ps.setInt(2, roster.getTestAdminId());
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					Object[] val = new Object[5];
					val[1] = rs.getString(2);
					val[2] = rs.getString(3);
					val[3] = rs.getString(4);
					val[4] = rs.getString(5);

					if (rs.getString(1).toString().trim().equalsIgnoreCase(
					"speaking")) {
						treeMap.put(rs.getString(1).toString().trim()
								.toLowerCase(), val);
						contentAreaFact.put("speaking",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim()
							.equalsIgnoreCase("listening")) {
						treeMap.put(rs.getString(1).toString().trim()
								.toLowerCase(), val);
						contentAreaFact.put("listening",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim()
							.equalsIgnoreCase("reading")) {
						treeMap.put(rs.getString(1).toString().trim()
								.toLowerCase(), val);
						contentAreaFact.put("reading",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim()
							.equalsIgnoreCase("writing")) {
						treeMap.put(rs.getString(1).toString().trim()
								.toLowerCase(), val);
						contentAreaFact.put("writing",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim()
							.equalsIgnoreCase("comprehension")) {
						treeMap.put(rs.getString(1).toString().trim()
								.toLowerCase(), val);
						contentAreaFact.put("comprehension",rs.getString(1).toString().trim().toLowerCase());
					} else if (rs.getString(1).toString().trim()
							.equalsIgnoreCase("oral")) {
						treeMap.put(rs.getString(1).toString().trim()
								.toLowerCase(), val);
						contentAreaFact.put("oral",rs.getString(1).toString().trim().toLowerCase());
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at createSkillAreaScoreInformation1:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		if (tfil.getTestingExemptionsSpeaking().equalsIgnoreCase("1")) {
			ss.setSpeaking("EXM");
			speaking++;
		} else if (tfil.getAbsentSpeaking().equalsIgnoreCase("1")) {
			ss.setSpeaking("ABS");
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
			}
		}

		if (tfil.getTestingExemptionsListening().equalsIgnoreCase("1")) {
			ss.setListening("EXM");
			listening++;
		} else if (tfil.getAbsentListening().equalsIgnoreCase("1")) {
			ss.setListening("ABS");
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
			}
		}

		if (tfil.getTestingExemptionsReading().equalsIgnoreCase("1")) {
			ss.setReading("EXM");
			reading++;
		} else if (tfil.getAbsentReading().equalsIgnoreCase("1")) {
			ss.setReading("ABS");
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
			}
		}

		if (tfil.getTestingExemptionsWriting().equalsIgnoreCase("1")) {
			ss.setWriting("EXM");
			writing++;
		} else if (tfil.getAbsentWriting().equalsIgnoreCase("1")) {
			ss.setWriting("ABS");
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
			}
		}

		if (speaking > 0) {
			po.setSpeaking("");
			pl.setSpeaking("");
			pc.setSpeaking("");
			isOralPopulated = false;
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
			isInvalidReading = true;
			isProficiencyLevelOverall = false;
			isScaleOverall = false;
		}
		if (writing > 0) {
			po.setWriting("");
			pl.setWriting("");
			pc.setWriting("");
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
		}

		if(contentAreaFact.containsKey("speaking") && contentAreaFact.containsKey("listening")){
			if (isOralPopulated) {
				Object[] val = treeMap.get("oral");
				if (val != null) {
					ss.setOral(val[1].toString());
					pl.setOral(val[2].toString());
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
		}
		
		if(contentAreaFact.containsKey("listening") && contentAreaFact.containsKey("reading") && contentAreaFact.containsKey("speaking") && contentAreaFact.containsKey("writing") ){
			if (isScaleOverall ) {

				if(scaleScoreOverall == null){
					scaleScoreOverall= "";
				}
				ss.setOverall(scaleScoreOverall);

			} else {
				ss.setOverall("N/A");
			}

		}
		else{
			ss.setOverall(" ");
		}
		if(contentAreaFact.containsKey("listening") && contentAreaFact.containsKey("reading") && contentAreaFact.containsKey("speaking") && contentAreaFact.containsKey("writing") ){
			if (isProficiencyLevelOverall ) {

				if(profLevelScoreOverall == null){
					profLevelScoreOverall= "";
				}
				pl.setOverall(profLevelScoreOverall);


			} else {
				pl.setOverall("");
			}	
		}
		else{
			ss.setOverall(" ");
		}


		tfil.setScaleScores(ss);
		tfil.setProficiencyLevels(pl);
		tfil.setSkillAreaNumberCorrect(po);
		tfil.setSkillAreaPercentCorrect(pc);
	}

	private void createSubSkillAreaScoreInformation(Connection oasCon,
			Connection irsCon, Tfil tfil, TestRoster roster) throws CTBBusinessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		SubSkillNumberCorrect subNumCorrect = new SubSkillNumberCorrect();
		SubSkillPercentCorrect subPercCorrect = new SubSkillPercentCorrect();
		String subSkillName;
		@SuppressWarnings("unused")
		String subSkillCategoryName;
		HashMap<String, String> pointsObtained = new HashMap<String, String>();
		HashMap<String, String> percentObtained = new HashMap<String, String>();

		try {
			ps2 = oasCon.prepareStatement(subSkillItemAreaInformation);
			ps2.setInt(1, roster.getTestAdminId());
			rs2 = ps2.executeQuery();
			while (rs2.next()) {

				subSkillAreaScoreInfo.put(rs2.getString(1), rs2.getString(2));
				subSkillAreaItemCategory.put(rs2.getString(2), rs2.getString(3));

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at createSubSkillAreaScoreInformation:"+e.getMessage());
		} finally {
			SqlUtil.close(ps2, rs2);
		}
		
		
		try {
			ps = irsCon.prepareStatement(subSkillIrsInformation);
			ps.setInt(1, roster.getStudentId());
			ps.setInt(2, roster.getTestAdminId());
			rs = ps.executeQuery();
			while (rs.next()) {
				percentObtained.put(rs.getString(1), rs.getString(3));
				pointsObtained.put(rs.getString(1), rs.getString(4));

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("SQLException at createSubSkillAreaScoreInformation1:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		for (String x : percentObtained.keySet()) {
			subSkillName = subSkillAreaScoreInfo.get(x);
			
			
			if (subSkillName.equalsIgnoreCase("Speak in Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setSpeakInWords(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Speak in Sentences")  && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setSpeakSentences(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Make Conversation") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setMakeConversations(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Tell a Story") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setTellAStory(percentObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Listen for Information")&& fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
				subPercCorrect.setListenForInformation(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Listen in the Classroom") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setListenInTheClassroom(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Listen and Comprehend") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setListenAndComprehend(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Analyze Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setAnalyzeWords(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Read Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setReadWords(percentObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Read for Understanding")&& fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory)) {
				subPercCorrect.setReadForUnderStanding(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Use Conventions") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setUseConventions(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Write About") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setWriteAbout(percentObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Write Why") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setWriteWhy(percentObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Write in Detail") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subPercCorrect.setWriteInDetail(percentObtained.get(x)
						.toString());
			}
		}

		tfil.setSubSkillPercentCorrect(subPercCorrect);

		for (String x : pointsObtained.keySet()) {
			subSkillName = subSkillAreaScoreInfo.get(x);

			if (subSkillName.equalsIgnoreCase("Speak in Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setSpeakInWords(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Speak in Sentences") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setSpeakSentences(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Make Conversation") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setMakeConversations(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Tell a Story") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setTellAStory(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Listen for Information") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setListenForInformation(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Listen in the Classroom") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setListenInTheClassroom(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Listen and Comprehend") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setListenAndComprehend(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Analyze Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setAnalyzeWords(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Read Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setReadWords(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Read for Understanding") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setReadForUnderStanding(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Use Conventions") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setUseConventions(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Write About") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setWriteAbout(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Write Why") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect.setWriteWhy(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Write in Detail") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory) ) {
				subNumCorrect
				.setWriteInDetail(pointsObtained.get(x).toString());
			}
		}
		tfil.setSubSkillNumberCorrect(subNumCorrect);
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

	private void prepareOrderFile(OrderFile orderFile, String filedir,
			String orderFileName) throws IOException {

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

	}
	

	private void createSubtestIndicatorFlag (Connection con,
			Integer rosterId, Tfil tfil) throws CTBBusinessException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(subtestIndicator);
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
			throw new CTBBusinessException("SQLException at createSubtestIndicatorFlag:"+e.getMessage());
		}finally {
			SqlUtil.close(ps, rs);
		}
	}

}
