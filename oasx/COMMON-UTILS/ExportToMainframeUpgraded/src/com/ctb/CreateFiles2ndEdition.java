package com.ctb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.ffpojo.exception.FFPojoException;
import org.ffpojo.file.writer.FileSystemFlatFileWriter;
import org.ffpojo.file.writer.FlatFileWriter;

import com.ctb.dto.CustomerDemographic;
import com.ctb.dto.ItemResponsesGRTLL2ND;
import com.ctb.dto.OrderFile;
import com.ctb.dto.Organization;
import com.ctb.dto.ProficiencyLevelsLL2ND;
import com.ctb.dto.ReferenceNormalCurveEquivalentsLL2ND;
import com.ctb.dto.ReferencePercentileRanksLL2ND;
import com.ctb.dto.RostersItem;
import com.ctb.dto.ScaleScoresLL2ND;
import com.ctb.dto.SkillArea;
import com.ctb.dto.SkillAreaNumberCorrectLL2ND;
import com.ctb.dto.SkillAreaPercentCorrectLL2ND;
import com.ctb.dto.SpecialCodes;
import com.ctb.dto.Student;
import com.ctb.dto.StudentContact;
import com.ctb.dto.StudentDemographic;
import com.ctb.dto.StudentItemSetStatus;
import com.ctb.dto.SubSkillArea;
import com.ctb.dto.SubSkillNumberCorrectLL2ND;
import com.ctb.dto.SubSkillPercentCorrectLL2ND;
import com.ctb.dto.TestAdmin;
import com.ctb.dto.TestRoster;
import com.ctb.dto.TfilLL2ND;
import com.ctb.utils.Configuration;
import com.ctb.utils.Constants;
import com.ctb.utils.EmetricUtil;
import com.ctb.utils.ExtractUtil;
import com.ctb.utils.ProcessRostersLL2ND;
import com.ctb.utils.SftpUtil;
import com.ctb.utils.SimpleCache;
import com.ctb.utils.SqlUtil;
import com.jcraft.jsch.Session;

/**
 * 
 * @author TCS
 *
 */
public class CreateFiles2ndEdition {
	static Logger logger = Logger.getLogger(CreateFiles2ndEdition.class.getName());
			
	private static final int BATCH_SIZE = 998;
	private String customerModelLevelValue = null;
	private String customerState = null;
	private String customerCity = null;
	private static String testDate = null;
	private static String programDate = null;
	private int districtElementNumber = 0;
	private int schoolElementNumber = 0;
	private int classElementNumber = 0;
	private int sectionElementNumber = 0;
	private int groupElementNumber = 0;
	private int divisionElementNumber = 0;
	private int levelElementNumber = 0;
	private int studentElementNumber = 0;
	private boolean isValidStartDate = false;
	private boolean isValidEndDate = false;
	static Map<String, String> wrongMap = new TreeMap<String, String>();
	private Integer customerId;
	private Integer frameProductId;
	private String extractSpanStartDate;
	private String extractSpanEndDate;
	private String MFid;
	private Map<String, Integer> districtMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> schoolMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> classMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> sectionMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> groupMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> divisionMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> levelMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<Integer, Integer> studentMap = Collections.synchronizedMap(new HashMap<Integer, Integer>());
	private static Integer ROSTER_COUNTER = Integer.valueOf(0);
//	private Progress progress = new Progress(true);
	static {
		wrongMap.put("A", "1");
		wrongMap.put("B", "2");
		wrongMap.put("C", "3");
		wrongMap.put("D", "4");
	}
	
	public CreateFiles2ndEdition(boolean isValidStartDate, boolean isValidEndDate, String extractSpanStartDate,
			String extractSpanEndDate, Integer customerId, Integer frameworkProductId, String mFid, int classLevelElementNumber) {
		this.isValidStartDate = isValidStartDate;
		this.isValidEndDate = isValidEndDate;
		this.extractSpanStartDate = extractSpanStartDate;
		this.extractSpanEndDate = extractSpanEndDate;
		this.customerId = customerId;
		this.frameProductId = frameworkProductId;
		this.MFid = mFid;
		this.levelElementNumber = classLevelElementNumber;
	}

	public CreateFiles2ndEdition() {
		super();
	}

	/**
	 * Write Export File and Order File and transfer file to specific location for LAS Links Form C/D/Espanol B
	 * @throws IOException
	 * @throws FFPojoException
	 * @throws SQLException
	 * @throws Exception
	 */
	public void writeToText() throws IOException, FFPojoException,SQLException, Exception {

		OrderFile orderFile = new OrderFile();
		/**
		 * Generate collection of export data object
		 */
		List<TfilLL2ND> myList = createList(orderFile);
		if (null == myList) {
			logger.error("No roster found to extract.\nExecution forcefully stopped.");
			System.exit(1);
		}
		logger.info("Data populattion process completed.");
		
		/**
		 * Generate data file name
		 */
		StringBuilder fileName = new StringBuilder();
		fileName.append(customerState).append(Constants.NAME_SEPARATOR).append(
				testDate.substring(0, 6)).append(Constants.NAME_SEPARATOR)
				.append(orderFile.getCustomerId()).append(
						Constants.NAME_SEPARATOR).append(MFid.trim()).append(
						Constants.NAME_SEPARATOR).append(
						orderFile.getCustomerName()).append(
						Constants.NAME_SEPARATOR).append(Constants.GROUP);
						
		String orderFileName = fileName.toString();
		
		fileName.append(Constants.NAME_SEPARATOR).append(Constants.DATAFILE)
				.append(Constants.NAME_SEPARATOR).append(
						Constants.FILE_DATE_OUTPUT_FORMAT.format(new Date()))
				.append(Constants.DATA_FILE_EXTN);

		String localFilePath = ExtractUtil.getDetail("oas.exportdata.filepath");
		logger.info("Creating file at Local File Path :: " + localFilePath);
		if (!(new File(localFilePath)).exists()) {
			File f = new File(localFilePath);
			f.mkdirs();
		}
		File file = new File(localFilePath, fileName.toString());
		FlatFileWriter ffWriter = null;
		try {
			ffWriter = new FileSystemFlatFileWriter(file, true);
			ffWriter.writeRecordList(myList);
			ffWriter.close();
			logger.info("Export file successfully generated :: [" + fileName.toString() + "]");
			orderFile.setDataFileName(EmetricUtil.truncate(fileName.toString(),
					new Integer(100)).substring(0, fileName.length()));
			logger.info("Preparing Order File...");
			prepareOrderFile(orderFile, localFilePath, orderFileName);
		}finally {
			if (ffWriter != null) {
				ffWriter.close();
			}
		}
		logger.info("Data file transfer process started...");
		/* SFTP the generated data file: Start */
		String destinationDir = Configuration.getFtpFilepath();
		Session session = null;
		try {
			session = SftpUtil.getSFtpSession();
			SftpUtil.doSftp(session, destinationDir, localFilePath, fileName.toString());
			logger.info("Data file transfer process completed.");
		} catch (Exception e) {
			logger.error("Data file transfer process failed.");
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			SftpUtil.closeSFtpClient(session);
		}
		/* SFTP the generated data file: End */
	}
	
	/**
	 * Collect all students data and create a collection. 
	 * @param orderFile
	 * @return Collection of flat file object.
	 * @throws Exception
	 */
	public List<TfilLL2ND> createList(OrderFile orderFile) throws Exception {
		
		List<TfilLL2ND> tfilList = Collections.synchronizedList(new ArrayList<TfilLL2ND>());
		Map<Integer, String> customerDemographic = Collections.synchronizedMap(new HashMap<Integer, String>());
		SimpleCache cache = new SimpleCache();
		Integer studentCount = 0;
		Connection oascon = null;
		Connection irscon = null;
		Thread thread = null;
		
		try{
			logger.info("Processing started For Laslink Second Edition...");
			oascon = SqlUtil.openOASDBconnectionForResearch();
			irscon = SqlUtil.openIRSDBconnectionForResearch();
			
			/**
			 * Collecting all student data and populate the cache
			 */
			logger.info("Chache In Memory Size :: "+cache.getCacheInMemorySize());
			getTestRoster(oascon, irscon, cache);
			studentCount = cache.size();
			if(studentCount == 0){
				return null;
			}
			logger.info("Collecting all customer infomation...");
			populateCustomer(oascon, orderFile);
			customerDemographic = getCustomerDemographicLebelMap(oascon);
			logger.info("Customer information collection is completed");
			
			
			Integer threadCount = Integer.valueOf(Configuration.getThreadCount());
			ExecutorService executor = Executors.newFixedThreadPool(threadCount.intValue());
			logger.info("Roster Processing started with thread count :: " + threadCount);
			
			for (Integer rosterId : cache.getKeys()) {
				//studentCount = getStudentCounter();
				TestRoster roster = cache.getRoster(rosterId);
				if(roster != null){
					ProcessRostersLL2ND process = new ProcessRostersLL2ND(roster,
							customerModelLevelValue, customerState, customerCity,
							customerDemographic, orderFile, this);
					thread = new Thread(process);
					executor.execute(thread);
				}
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
				// Break after all the task is completed.
			}
			
			orderFile.setCaseCount(studentCount.toString());
			tfilList = ProcessRostersLL2ND.getFinalList();
			
		} finally {
			SqlUtil.close(oascon);
			SqlUtil.close(irscon);
			cache.closeCache();
		}
		/**
		 * Sort the collection with compare to student element number in ASC order
		 */
		Collections.sort(tfilList, new Comparator<TfilLL2ND>() {
	        @Override
	        public int compare(TfilLL2ND  tfil, TfilLL2ND  tfil2)
	        {
	            return  tfil.getStudentElementNumber().compareTo(tfil2.getStudentElementNumber());
	        }
	    });
		
		return tfilList;
	}
	
	public static synchronized int getStudentCounter()
	  {
	    return (CreateFiles2ndEdition.ROSTER_COUNTER = Integer.valueOf(ROSTER_COUNTER.intValue() + 1)).intValue();
	  }
	
	/**
	 * Prepare order file in temporary local directory and transfer file in a specified location
	 * @param orderFile
	 * @param filedir
	 * @param fileName
	 * @throws Exception
	 */
	private void prepareOrderFile(OrderFile orderFile, String filedir,
			String fileName) throws Exception {

		StringBuilder orderFileName = new StringBuilder();
		orderFileName.append(fileName).append(Constants.NAME_SEPARATOR).append(
				Constants.ORDERFILE).append(Constants.NAME_SEPARATOR).append(
				Constants.FILE_DATE_OUTPUT_FORMAT.format(new Date())).append(
				Constants.ORDER_FILE_EXTN);
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(filedir, orderFileName.toString()));

			writer.append(Constants.CUSTOMER_ID).append(',')
			.append(Constants.CUSTOMER_NAME).append(',')
			.append(Constants.STATE).append(',')
			.append(Constants.ORG_TESTING_PROGRAM).append(',')
			.append(Constants.SCORING_ORDER_NUMBER).append(',')
			.append(Constants.TAG_NUMBER).append(',')
			.append(Constants.TEST_NAME1).append(',')
			.append(Constants.TEST_NAME2).append(',')
			.append(Constants.TEST_NAME3).append(',')
			.append(Constants.TEST_DATE).append(',')
			.append(Constants.CASE_COUNT).append(',')
			.append(Constants.RE_RUN_FLAG).append(',')
			.append(Constants.LONGITUDINAL_FLAG).append(',')
			.append(Constants.RE_ROSTER_FLAG).append(',')
			.append(Constants.DATA_FILE_NAME).append(',')
			.append(Constants.CUSTOMER_CONTACT).append(',')
			.append(Constants.CUSTOMER_EMAIL).append(',')
			.append(Constants.CUSTOMER_PHONE).append(',')
			.append(Constants.TB).append(',')
			.append(Constants.HIERARCHY_MODE_LOCATION).append(',')
			.append(Constants.SPECIAL_CODE_SELECT).append(',')
			.append(Constants.EXPECTED_TITLES).append(',')
			.append(Constants.HIERARCHY_MODE_LOCATION2).append(',')
			.append(Constants.SPECIAL_CODE_SELECT2).append(',')
			.append(Constants.EXPECTED_TITLES2).append(',')
			.append(Constants.SUBMITTER_EMAIL)
			.append('\n');

			writer.append(orderFile.getCustomerId().toString()).append(',')
			.append(orderFile.getCustomerName()).append(',')
			.append(orderFile.getCustomerStateAbbrevation()).append(',')
			.append(orderFile.getOrgTestingProgram()).append(',')
			.append(orderFile.getScoringOrderNumber()).append(',')
			.append(orderFile.getTagNumber()).append(',')
			.append(orderFile.getTestName1()).append(',')
			.append(orderFile.getTestName2()).append(',')
			.append(orderFile.getTestName3()).append(',')
			.append(orderFile.getTestDate()).append(',')
			.append(orderFile.getCaseCount()).append(',')
			.append(orderFile.getReRunFlag()).append(',')
			.append(orderFile.getLongitudinalFlag()).append(',')
			.append(orderFile.getReRosterFlag()).append(',')
			.append(orderFile.getDataFileName()).append(',')
			.append(orderFile.getCustomerContact()).append(',')
			.append(orderFile.getCustomerEmail()).append(',')
			.append(orderFile.getCustomerPhone()).append(',')
			.append(orderFile.getTB()).append(',')
			.append(orderFile.getHierarchyModeLocation()).append(',')
			.append(orderFile.getSpecialCodeSelect()).append(',')
			.append(orderFile.getExpectedTitles()).append(',')
			.append(orderFile.getHierarchyModeLocation2()).append(',')
			.append(orderFile.getSpecialCodeSelect2()).append(',')
			.append(orderFile.getExpectedTitles2()).append(',')
			.append(orderFile.getSubmittersEmail())
			.append('\n');

			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error("Error in Preparing OrderFile");
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;

		} finally {
			if(writer!=null){
				writer.close();
			}
		}
		logger.info("Order file successfully generated :: [" + orderFileName.toString() + "]");
		
		logger.info("Order file transfer process started...");
		/* SFTP the generated order file: Start */
		String destinationDir = Configuration.getFtpFilepath();
		Session session = null;
		try{
			session = SftpUtil.getSFtpSession();
			SftpUtil.doSftp(session, destinationDir, filedir, orderFileName.toString());
			logger.info("Order file transfer process completed.");
		}catch(Exception e){
			logger.error("Order file transfer process failed.");
			logger.error(e.getMessage());
			e.printStackTrace();
		}finally{
			SftpUtil.closeSFtpClient(session);
		}
		/* SFTP the generated order file: End */
	}


	/**
	 * Set sub test status details for student
	 * @param tfil
	 * @param roster
	 * @param invalidationMap
	 * @throws Exception
	 */
	public void createStudentItemStatusDetails(
			TfilLL2ND tfil, TestRoster roster, Map<String, Object[]> invalidationMap) throws Exception{

		Set<StudentItemSetStatus> sissSet = roster.getStudentItemSetStatus();
		Iterator<StudentItemSetStatus> it = sissSet.iterator();
		while(it.hasNext()){
			StudentItemSetStatus siss = it.next();
			Object[] val = new Object[3];
			if (siss.getItemSetName().equalsIgnoreCase("Speaking")) {
				tfil.setTestInvalidationSpeaking(siss.getValidationStatus().equalsIgnoreCase("IN") ? "1" : " ");
				val[0] = (siss.getValidationStatus().equalsIgnoreCase("IN")) ? "1" : " ";
				val[1] = (siss.getExemptions().equalsIgnoreCase("Y")) ? "1" : " ";
				val[2] = (siss.getAbsent().equalsIgnoreCase("Y")) ? "1" : " ";
			} else if (siss.getItemSetName().equalsIgnoreCase("Listening")) {
				tfil.setTestInvalidationListening(siss.getValidationStatus().equalsIgnoreCase("IN") ? "1" : " ");
				val[0] = (siss.getValidationStatus().equalsIgnoreCase("IN")) ? "1" : " ";
				val[1] = (siss.getExemptions().equalsIgnoreCase("Y")) ? "1" : " ";
				val[2] = (siss.getAbsent().equalsIgnoreCase("Y")) ? "1" : " ";
			} else if (siss.getItemSetName().equalsIgnoreCase("Reading")) {
				tfil.setTestInvalidationReading(siss.getValidationStatus().equalsIgnoreCase("IN") ? "1" : " ");
				val[0] = (siss.getValidationStatus().equalsIgnoreCase("IN")) ? "1" : " ";
				val[1] = (siss.getExemptions().equalsIgnoreCase("Y")) ? "1" : " ";
				val[2] = (siss.getAbsent().equalsIgnoreCase("Y")) ? "1" : " ";
			} else if (siss.getItemSetName().equalsIgnoreCase("Writing")) {
				tfil.setTestInvalidationWriting(siss.getValidationStatus().equalsIgnoreCase("IN") ? "1" : " ");
				val[0] = (siss.getValidationStatus().equalsIgnoreCase("IN")) ? "1" : " ";
				val[1] = (siss.getExemptions().equalsIgnoreCase("Y")) ? "1" : " ";
				val[2] = (siss.getAbsent().equalsIgnoreCase("Y")) ? "1" : " ";
			}
			invalidationMap.put(siss.getItemSetName().toLowerCase(), val);
		}
	}

	/**
	 * Set session details of student
	 * @param tfil
	 * @param roster
	 * @param orderFile
	 * @throws Exception
	 */
	public void createTestSessionDetails(TfilLL2ND tfil, TestRoster roster, OrderFile orderFile) throws Exception {

		TestAdmin test = roster.getTest_admin();
		if (Constants.FORMC.equalsIgnoreCase(test.getPreferedForm())) {
			tfil.setTestName("LAS Links 2nd Edition");
			tfil.setTestForm("C");
			if (orderFile.getTestName1() == null)
				orderFile.setTestName1(EmetricUtil.truncate(
						"LAS Links2", new Integer(11))
						.toUpperCase());
		} else if (Constants.FORMD.equalsIgnoreCase(test.getPreferedForm())) {
			tfil.setTestName("LAS Links 2nd Edition");
			tfil.setTestForm("D");
			if (orderFile.getTestName1() == null)
				orderFile.setTestName1(EmetricUtil.truncate(
						"LAS Links2", new Integer(11))
						.toUpperCase());
		} else if (Constants.FORM_T.equalsIgnoreCase(test.getPreferedForm())) {
			tfil.setTestName("LAS Links 2nd Edition Español");
			tfil.setTestForm("T");
			if (orderFile.getTestName1() == null)
				orderFile.setTestName1(EmetricUtil.truncate(
						"ESPANOL2", new Integer(11))
						.toUpperCase());
		}

		if (test.getTestLevel() != null
				&& test.getTestLevel().equals("K")) {
			tfil.setTestLevel("1");
		} else if (test.getTestLevel() != null
				&& test.getTestLevel().equals("1")) {
			tfil.setTestLevel("1");
		} else if (test.getTestLevel() != null
				&& test.getTestLevel().equals("2-3")) {
			tfil.setTestLevel("2");
		} else if (test.getTestLevel() != null
				&& test.getTestLevel().equals("4-5")) {
			tfil.setTestLevel("3");
		} else if (test.getTestLevel() != null
				&&test.getTestLevel().equals("6-8")) {
			tfil.setTestLevel("4");
		} else if (test.getTestLevel() != null
				&& test.getTestLevel().equals("9-12")) {
			tfil.setTestLevel("5");
		}
		if(test.getTestDate() != null){
			tfil.setTestDate(EmetricUtil.getTimeZone(test.getTestDate(),test.getTimezone(),true));
		}
			CreateFiles2ndEdition.testDate = tfil.getTestDate();
			
		if(test.getProgramStartDate() != null && CreateFiles2ndEdition.programDate == null){
			CreateFiles2ndEdition.programDate = EmetricUtil.getTimeZone(test.getProgramStartDate(),test.getTimezone(),true);
		}

		if (orderFile.getTestDate() == null && CreateFiles2ndEdition.programDate != null){
			orderFile.setTestDate(CreateFiles2ndEdition.programDate.substring(0, 6));
		}
	}

	/**
	 * Set student details
	 * @param tfil
	 * @param st
	 * @param rosterId
	 */
	public void setStudentList(TfilLL2ND tfil, Student st, Integer rosterId) {
		
		tfil.setStudentElementNumber(studentMap.get(rosterId).toString());
		tfil.setStudentLastName(st.getLastName());
		tfil.setStudentFirstName(st.getFirstName());
		tfil.setStudentGender(st.getGender());
		tfil.setStudentMiddleName(st.getMiddleName());
		tfil.setExtStudentId(st.getExtStudentId());
		tfil.setStudentGradeFromAnswerSheets(EmetricUtil.formatGrade(st.getGrade()));
		tfil.setGrade(EmetricUtil.formatGrade(st.getGrade()));
		tfil.setStudentBirthDate(st.getBirthDate());
		tfil.setStudentChronologicalAge(EmetricUtil.calculateChronologicalAge(st.getBirthDate()));
	}

	/**
	 * Get all demographic information for customer
	 * @param con
	 * @return
	 * @throws Exception
	 */
	private Map<Integer, String> getCustomerDemographicLebelMap(Connection con) throws Exception {
		Map<Integer, String> demoMap = Collections.synchronizedMap(new HashMap<Integer, String>());
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(Constants.CUSTOMER_DEMOGRAPHIC_SQL);
			ps.setInt(1, this.customerId);
			rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDemographic cd = new CustomerDemographic();
				cd.setCustomerDemographicId(rs.getInt(1));
				cd.setCustomerId(rs.getInt(2));
				cd.setLabelName(rs.getString(3));
				if(!demoMap.containsKey(cd.getCustomerDemographicId())){
					demoMap.put(cd.getCustomerDemographicId(), cd.getLabelName());
				}
			}

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new Exception("SQLException at getCustomerDemographic:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
		return demoMap;
	}

	
	/**
	 * Populate customer detail information
	 * @param con
	 * @param orderFile
	 * @throws Exception
	 */
	private void populateCustomer(Connection con, OrderFile orderFile) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(Constants.CUSTOMER_SQL);
			ps.setInt(1, customerId);
			rs = ps.executeQuery();
			while (rs.next()) {
				this.customerState = rs.getString(1);
				this.customerCity = rs.getString(2);
				this.customerModelLevelValue = rs.getString(6);
				orderFile.setCustomerStateAbbrevation(rs.getString(1));
				orderFile.setCustomerEmail(EmetricUtil.truncate(
						rs.getString(3), new Integer(64)));
				orderFile.setCustomerPhone(EmetricUtil.truncate(EmetricUtil
						.convertPhoneNumber(rs.getString(4)), new Integer(21)));
				orderFile.setCustomerContact(EmetricUtil.truncate(rs
						.getString(5), new Integer(64)));
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new Exception("SQLException at populateCustomer:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
	}
	
	/**
	 * Fetch all required rosters information for the a customer
	 * @param oascon
	 * @param irscon
	 * @param cache
	 * @throws Exception
	 */
	private void getTestRoster(Connection oascon, Connection irscon, SimpleCache cache) throws Exception {
		PreparedStatement ps = null ;
		ResultSet rs = null;
		StringBuilder studentIds = new StringBuilder();
		StringBuilder rosterStud = new StringBuilder();
		StringBuilder rosterIds = new StringBuilder();
		StringBuilder sessionStuIds = new StringBuilder();
		StringBuilder adminIds = new StringBuilder();
		int counter = 0;
		boolean firstValue = true;
		try{
			if(this.isValidStartDate) {
				String START_DATE = this.extractSpanStartDate.replaceAll("/", "").toString().trim();
				Constants.MODIFIED_QUERY_TO_FETCH_ROSTER += " AND TRUNC(ROSTER.COMPLETION_DATE) >= TO_DATE('" + START_DATE + "', 'MMDDYYYY')";
			}
			if(this.isValidEndDate) {
				String END_DATE = this.extractSpanEndDate.replaceAll("/", "").toString().trim();
				Constants.MODIFIED_QUERY_TO_FETCH_ROSTER += " AND TRUNC(ROSTER.COMPLETION_DATE) <= TO_DATE('" + END_DATE + "', 'MMDDYYYY')";
			}
			ps = oascon.prepareStatement(Constants.MODIFIED_QUERY_TO_FETCH_ROSTER);
			ps.setInt(1, frameProductId);
			ps.setInt(2, customerId);
			ps.setInt(3, frameProductId);
			ps.setInt(4, customerId);
			ps.setInt(5, frameProductId);
			ps.setInt(6, customerId);
			ps.setFetchSize(100);
			rs = ps.executeQuery(); 
			int cot = 0;
			TestRoster ros  = null;
			while (rs.next()){
				ros = new TestRoster();
				Student std = new Student();
				TestAdmin adm = new TestAdmin();
				
				ros.setTestRosterId(rs.getInt(1));
				ros.setActivationStatus(rs.getString(2));
				ros.setTestCompletionStatus(rs.getString(3));
				ros.setCustomerId(customerId);
				ros.setStudentId(rs.getInt(5));
				ros.setTestAdminId(rs.getInt(6));
				ros.setProductId(rs.getInt(7));
				ros.setStudent(std);
				ros.setTest_admin(adm);
				
				std.setStudentId(rs.getInt(5));
				std.setFirstName(rs.getString(9));
				std.setLastName(rs.getString(10));
				std.setMiddleName(rs.getString(11));
				std.setBirthDate(rs.getDate(12));
				std.setGender(rs.getString(13));
				std.setGrade(rs.getString(14));
				std.setCustomerId(customerId);
				std.setTestPurpose(rs.getString(15));
				std.setExtStudentId(rs.getString(16));
				
				adm.setTestDate(rs.getString(17));
				adm.setPreferedForm(rs.getString(18));
				adm.setTimezone(rs.getString(19));
				adm.setTestLevel(rs.getString(20));
				adm.setProgramStartDate(rs.getString(21));
				
				if(adm.getTestDate()== null || adm.getProgramStartDate() == null || adm.getPreferedForm() == null){
					throw new Exception(
							"Any of these field, Rosters START_DATE_TIME or Test PREFERED_FORM or TIME_ZONE or PROGRAM may have null value for roster "
									+ ros.getTestRosterId());
				}
				
				//generate student sequence 
				Integer integer = studentMap.get(ros.getTestRosterId());
				if (integer == null) {
					integer = ++studentElementNumber;
					studentMap.put(ros.getTestRosterId(), integer);
				}
				
				/**
				 * Populate required combined string for further query
				 */	
				String sisId = "("+ros.getStudentId()+","+ros.getTestRosterId()+")";
				String irsId = "("+ros.getStudentId()+","+ros.getTestAdminId()+")";
				if(firstValue){
					firstValue = false;
				}else{
					studentIds.append(',');
					rosterStud.append(',');
					rosterIds.append(',');
					sessionStuIds.append(',');
					adminIds.append(',');
				}
				studentIds.append(std.getStudentId());
				rosterStud.append(sisId);
				rosterIds.append(ros.getTestRosterId());
				sessionStuIds.append(irsId);
				adminIds.append(ros.getTestAdminId());
				counter++;
				if(counter % BATCH_SIZE == 0){
					studentIds.append('#');
					rosterStud.append('#');
					rosterIds.append('#');
					sessionStuIds.append('#');
					adminIds.append('#');
					firstValue = true;
					counter = 0;
				}
				cache.addRoster(ros.getTestRosterId(),ros);
				cot++;
			}
			logger.info("Found rosters... #Total Roster Size :: "+cot);
		}catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getTestRoster:"+e.getMessage());
		}finally {
			SqlUtil.close(ps, rs);
		}
		if(cache.size() != 0){
			logger.info("Roster process will be started after 8 level of caching...");
			generateStudentDetails(oascon, irscon, cache, studentIds.toString(),
					rosterStud.toString(), rosterIds.toString(), sessionStuIds
							.toString(), adminIds.toString());
			logger.info("8 level of Caching process completed... #Total Cache Size :: " +cache.size());
		}
	}
	
	/**
	 * Populate contact, hierarchy, test status, response and result details for all students
	 * @param oascon
	 * @param irscon
	 * @param cache
	 * @param studentIds
	 * @param rosterStudIds
	 * @param rosterIds
	 * @param sessionStudents
	 * @param adminIds
	 * @throws Exception
	 */
	private void generateStudentDetails(Connection oascon, Connection irscon,
			SimpleCache cache, String studentIds, String rosterStudIds,
			String rosterIds, String sessionStudents, String adminIds) throws Exception {
		
		getStudentItemSetStatus(oascon, cache, rosterStudIds);
		getItemResponseGrt(oascon, cache, rosterIds);
		getStudentOrganization(oascon, cache, rosterIds);
		Map<Integer, Set<StudentContact>> studConMap = getStudentContact(oascon, studentIds);
		Map<Integer, Set<StudentDemographic>> studDemoMap = getStudentDemographic(oascon, studentIds);
		Map<String, Set<SkillArea>> rosterSkillAreaMap = getScoreSkillArea(irscon, sessionStudents);
		Map<Integer, Set<SubSkillArea>> subSkillMap = getSubSkillInformation(oascon, adminIds);
		Map<String, Set<SubSkillArea>> pointsMap = getIrsSubSkillScore(irscon,sessionStudents);
		if ((studConMap != null && studConMap.size() > 0)
				|| (studDemoMap != null && studDemoMap.size() > 0)
				|| rosterSkillAreaMap.size() > 0){
			List<Integer> rosters = cache.getKeys();
			Iterator<Integer> it = rosters.iterator();
			while(it.hasNext()){
				Integer rosterId = it.next(); 
				TestRoster roster = cache.getRoster(rosterId);
				if(roster != null) {
					Student student = roster.getStudent();
					String key = roster.getStudentId().toString()+ roster.getTestAdminId().toString();
					if(studConMap.containsKey(student.getStudentId()))
						student.setStudentContact(studConMap.get(student.getStudentId()));
					if(studDemoMap.containsKey(student.getStudentId()))
						student.setStudentDemographic(studDemoMap.get(student.getStudentId()));
					if(rosterSkillAreaMap.containsKey(key))
						roster.setSkillAreas(rosterSkillAreaMap.get(key));
					if(subSkillMap.containsKey(roster.getTestAdminId()))
						roster.setSubSkillAreas(subSkillMap.get(roster.getTestAdminId()));
					if(pointsMap.containsKey(key))
						roster.setPointsScores(pointsMap.get(key));
					cache.addRoster(rosterId, roster);
				}
			}
		}
		studConMap = null;
		studDemoMap = null;
		rosterSkillAreaMap = null;
		subSkillMap = null;
		pointsMap = null;
	}

	/**
	 * Get students objective level score information
	 * @param irscon
	 * @param sessionStudents
	 * @return
	 * @throws Exception
	 */
	private Map<String, Set<SubSkillArea>> getIrsSubSkillScore(
			Connection irscon, String sessionStudents) throws Exception{
		Map<String, Set<SubSkillArea>> rosterSkillAreaMap = Collections.synchronizedMap(new HashMap<String, Set<SubSkillArea>>());
		Set<SubSkillArea> skillAreaSet = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = sessionStudents.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.SUBSKILL_IRS_INFORMATION.replaceAll("#", str[i]);
				ps = irscon.prepareStatement(query);
				ps.setFetchSize(100);
				rs = ps.executeQuery();
				while (rs.next()) {
					String key = new String(rs.getString(2));
					SubSkillArea skillArea = new SubSkillArea();
					skillArea.setSubSkillId(rs.getString(1));
					skillArea.setPercentObtained(rs.getString(3));
					skillArea.setPointsObtained(rs.getString(4));
					if(!rosterSkillAreaMap.containsKey(key)){
						skillAreaSet = Collections.synchronizedSet(new HashSet<SubSkillArea>());
						skillAreaSet.add(skillArea);
						rosterSkillAreaMap.put(key, skillAreaSet);
					}else{
						rosterSkillAreaMap.get(key).add(skillArea);
					}
				}
			}
			System.out.print("\b");
			System.out.println("8 ... Done");
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new Exception("SQLException at getIrsSubSkillScore:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
		
		return rosterSkillAreaMap;
	}

	/**
	 * Get students objective level reporting informations
	 * @param con
	 * @param adminIds
	 * @return Map<Integer, Set<SubSkillArea>> subSkillMap
	 * @throws Exception
	 */
	private Map<Integer, Set<SubSkillArea>> getSubSkillInformation(Connection con, String adminIds) throws Exception{
		Map<Integer, Set<SubSkillArea>> subSkillMap = Collections.synchronizedMap(new HashMap<Integer, Set<SubSkillArea>>());
		Set<SubSkillArea> academicSet = getAcademicSubtest(con); // get Academic report information of student
		Set<SubSkillArea> subSkillSet = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = adminIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.SUBSKILL_ITEM_AREA_INFOMATION.replaceAll("#", str[i]);
				ps = con.prepareStatement(query);
				ps.setFetchSize(100);
				rs = ps.executeQuery();
				while (rs.next()) {
					Integer key = rs.getInt(1);
					SubSkillArea sa = new SubSkillArea();
					sa.setSubSkillId(rs.getString(2));
					sa.setSubSkillName(rs.getString(3));
					sa.setSubSkillCategory(rs.getString(4));
					sa.setProuductId(rs.getInt(5));
					if(!subSkillMap.containsKey(key)){
						subSkillSet = Collections.synchronizedSet(new HashSet<SubSkillArea>());
						subSkillSet.addAll(academicSet);
						subSkillSet.add(sa);
						subSkillMap.put(key, subSkillSet);
					}else{
						subSkillMap.get(key).add(sa);
					}
				}
			}
			System.out.print("\b");
			System.out.print("7");
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new Exception("SQLException at getSubSkillInformation:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
		
		return subSkillMap;
	}

	/**
	 * Get students academic reports information details
	 * @param con
	 * @return Set<SubSkillArea> subAreaSet
	 * @throws Exception
	 */
	private Set<SubSkillArea> getAcademicSubtest(Connection con) throws Exception{
		Set<SubSkillArea> subAreaSet = new HashSet<SubSkillArea>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
				String query = Constants.ACADEMIC_SKILL_AREA_INFORMATION;
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				while (rs.next()) {
					SubSkillArea sa = new SubSkillArea();
					sa.setSubSkillId(rs.getString(1));
					sa.setSubSkillName(rs.getString(2));
					sa.setSubSkillCategory(rs.getString(3));
					subAreaSet.add(sa);
				}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getAcademicSubtest:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
				
		return subAreaSet;
	}

	/**
	 * Get students content area level score details
	 * @param irscon
	 * @param sessionStudents
	 * @return Map<String, Set<SkillArea>> rosterSkillAreaMap
	 * @throws Exception
	 */
	private Map<String, Set<SkillArea>> getScoreSkillArea(Connection irscon, String sessionStudents) throws Exception{
		Map<String, Set<SkillArea>> rosterSkillAreaMap = Collections.synchronizedMap(new HashMap<String, Set<SkillArea>>());
		Set<SkillArea> skillAreaSet = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = sessionStudents.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.SCORE_SKILL_AREA_SQL.replaceAll("#", str[i]);
				ps = irscon.prepareStatement(query);
				ps.setFetchSize(100);
				rs = ps.executeQuery();
				while (rs.next()) {
					String key = rs.getString(1);
					SkillArea skillArea = new SkillArea();
					skillArea.setName(rs.getString(2));
					skillArea.setScaleScore(rs.getString(3));
					skillArea.setProficiencyLevel(rs.getString(4));
					skillArea.setPointsObtained(rs.getString(5));
					skillArea.setPercentObtained(rs.getString(6));
					skillArea.setNormCurveEqui(rs.getString(7));
					skillArea.setNationalPercentile(rs.getString(8));
					skillArea.setLexile(rs.getString(9));
					if(!rosterSkillAreaMap.containsKey(key)){
						skillAreaSet = Collections.synchronizedSet(new HashSet<SkillArea>());
						skillAreaSet.add(skillArea);
						rosterSkillAreaMap.put(key, skillAreaSet);
					}else{
						rosterSkillAreaMap.get(key).add(skillArea);
					}
				}
			}
			System.out.print("\b");
			System.out.print("6");
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new Exception("SQLException at getScoreSkillArea:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
		
		return rosterSkillAreaMap;
	}
	
	
	/**
	 * Get organization hierarchy details for all students
	 * @param con
	 * @param cache
	 * @param rosterIds
	 * @throws Exception
	 */
	private void getStudentOrganization(Connection con, SimpleCache cache, String rosterIds) throws Exception {
		
		Set<Integer>rosterIdSet = new HashSet<Integer>();
		Set<Organization> orgSet = Collections.synchronizedSet(new HashSet<Organization>());
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = rosterIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.ORGANIZATION_SQL.replace("#", str[i]);
				ps = con.prepareStatement(query);
				ps.setFetchSize(50);
				rs = ps.executeQuery();
				while (rs.next()) {
					Integer testRosterId = rs.getInt(6);
					TestRoster roster = cache.getRoster(testRosterId);
					if(roster != null){
						Organization org = new Organization();
						org.setOrgNodeMdrNumber(rs.getString(1));
						org.setOrgCategoryName(rs.getString(2));
						org.setOrgNodeCode(rs.getString(3));
						org.setOrgNodeName(rs.getString(4));
						org.setCategoryLevel(rs.getString(5));

						if(!rosterIdSet.contains(testRosterId)){
							orgSet = Collections.synchronizedSet(new HashSet<Organization>());
							
							rosterIdSet.add(testRosterId);
						}
						
						orgSet.add(org);
//						synchronized(roster){
							roster.setOrgnaizationSet(orgSet);
							cache.addRoster(testRosterId, roster);
//						}
					}
					
				}
				SqlUtil.close(ps, rs);
			}
			System.out.print("\b");
			System.out.print("3");
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new Exception("SQLException at getStudentOrganization:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
		
	}

	/**
	 * Get all Students contact details
	 * @param con
	 * @param studentIds
	 * @return Map<Integer, Set<StudentContact>> studentContactMap
	 * @throws Exception
	 */
	private Map<Integer, Set<StudentContact>> getStudentContact(Connection con, String studentIds) throws Exception {
		Map<Integer, Set<StudentContact>> studentContactMap = Collections.synchronizedMap(new HashMap<Integer, Set<StudentContact>>());
		Set<StudentContact> studentContact =  null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = studentIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.STUDENT_CONTACT_SQL.replace("#", str[i]);
				ps = con.prepareStatement(query);
				ps.setFetchSize(100);
				rs = ps.executeQuery();
				while (rs.next()) {
					StudentContact sdentCon = new StudentContact();
					Integer studentId = rs.getInt(1);
					sdentCon.setStudentContactId(rs.getInt(2));
					sdentCon.setCity(rs.getString(3));
					sdentCon.setState(rs.getString(4));
					if(!studentContactMap.containsKey(studentId)){
						studentContact = Collections.synchronizedSet(new HashSet<StudentContact>());
						studentContact.add(sdentCon);
						studentContactMap.put(studentId, studentContact);
					}else{
						studentContactMap.get(studentId).add(sdentCon);
					}
					
				}
				SqlUtil.close(ps, rs);
			}
			System.out.print("\b");
			System.out.print("4");
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new Exception("SQLException at getStudentContact:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
		return studentContactMap;
	}
	
	/**
	 * Get Students demographics details
	 * @param con
	 * @param studentIds
	 * @return Map<Integer, Set<StudentDemographic>> studentDemoMap
	 * @throws Exception
	 */
	private Map<Integer, Set<StudentDemographic>> getStudentDemographic(Connection con, String studentIds) throws Exception {
		Map<Integer, Set<StudentDemographic>> studentDemoMap = Collections.synchronizedMap(new HashMap<Integer, Set<StudentDemographic>>());
		Set<StudentDemographic> studentDemographicSet = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = studentIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.STUDENT_DEMOGRAPHIC_SQL.replace("#", str[i]);
				ps = con.prepareStatement(query);
				ps.setFetchSize(100);
				rs = ps.executeQuery();
				while (rs.next()) {
					Integer studentId = rs.getInt(5);
					StudentDemographic studentDemographic = new StudentDemographic();
					studentDemographic.setStudentDemographicId(rs.getInt(1));
					studentDemographic.setCustomerDemographicId(rs.getInt(2));
					studentDemographic.setValueName(rs.getString(3));
					studentDemographic.setValue(rs.getString(4));
					
					if(!studentDemoMap.containsKey(studentId)){
						studentDemographicSet = Collections.synchronizedSet(new HashSet<StudentDemographic>());
						studentDemographicSet.add(studentDemographic);
						studentDemoMap.put(studentId, studentDemographicSet);
					}else{
						studentDemoMap.get(studentId).add(studentDemographic);
					}
					
				}
				SqlUtil.close(ps, rs);
			}
			System.out.print("\b");
			System.out.print("5");
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new Exception("SQLException at getStudentDemographic:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return studentDemoMap;
	}
	
	/**
	 * Get students test status details
	 * @param con
	 * @param cache
	 * @param rosterStudIds
	 * @throws Exception
	 */
	private void getStudentItemSetStatus(Connection con, SimpleCache cache, String rosterStudIds) throws Exception {
		
		Set<Integer>rosterIds = new HashSet<Integer>();
		Set<StudentItemSetStatus> sissSet = Collections.synchronizedSet(new HashSet<StudentItemSetStatus>());
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = rosterStudIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.TEST_ROSTER_DETAILS_SQL.replace("#", str[i]);
				ps = con.prepareStatement(query);
				ps.setFetchSize(100);
				rs = ps.executeQuery();
				while (rs.next()) {
					Integer testRosterId = rs.getInt(1);
					TestRoster roster = cache.getRoster(testRosterId);
					if(roster != null){
						StudentItemSetStatus siss = new StudentItemSetStatus();
						siss.setValidationStatus(rs.getString(2));
						siss.setExemptions(rs.getString(4));
						siss.setAbsent(rs.getString(5));
						siss.setItemSetName(rs.getString(6));
						
						if(!rosterIds.contains(testRosterId)){
							sissSet = Collections.synchronizedSet(new HashSet<StudentItemSetStatus>());
							rosterIds.add(testRosterId);
						}
						
						sissSet.add(siss);						
//						synchronized(roster){
							roster.setStudentItemSetStatus(sissSet);
							cache.addRoster(testRosterId, roster);
//						}
					}
					
				}
				SqlUtil.close(ps, rs);
			}
			System.out.print("Completed Level ... 1");
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new Exception("SQLException at getStudentItemSetStatus:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
	}
	
	/**
	 * Set organization hierarchy details
	 * @param tfil
	 * @param roster
	 * @param orderFile
	 * @throws Exception
	 */
	public synchronized void createOrganization(TfilLL2ND tfil,
			TestRoster roster, OrderFile orderFile) throws Exception {

		Set<Organization> organizationSet = roster.getOrgnaizationSet();
		Integer organizationMapSize = organizationSet.size();
		Iterator<Organization> it = organizationSet.iterator();
		while (it.hasNext()) {
			Organization org = it.next();
			if (org.getOrgNodeName().equalsIgnoreCase("root")
					|| org.getOrgNodeName().equalsIgnoreCase("CTB")) {
				// do nothing
			} else if (org.getCategoryLevel() != null
					&& new Integer(organizationMapSize - 6).toString() != null
					&& org.getCategoryLevel()
					.equalsIgnoreCase(
							new Integer(organizationMapSize - 6)
							.toString())) {
				tfil.setElementNameA(org.getOrgNodeName().toString());
				tfil.setElementLabelA(org.getOrgCategoryName());
				Integer integer = districtMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++districtElementNumber;
					districtMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberA(String.valueOf(districtMap.get(org.getOrgNodeMdrNumber())));
				tfil.setElementSpecialCodesA(org.getOrgNodeCode());
				tfil.setMdrNumA(org.getOrgNodeMdrNumber());
				//tfil.setOrganizationId("XX" + rs.getString(1));
				tfil.setElementIdA(org.getOrgNodeMdrNumber());
				tfil.setElementStructureLevelA("01");
				if (orderFile.getCustomerId() == null)
					orderFile.setCustomerId(org.getOrgNodeMdrNumber());

			} else if (org.getCategoryLevel() != null
					&& new Integer(organizationMapSize - 5).toString() != null
					&& org.getCategoryLevel().toString()
					.equalsIgnoreCase(
							Integer.valueOf(organizationMapSize - 5)
							.toString())) {
				tfil.setElementNameB(org.getOrgNodeName());
				tfil.setElementLabelB(org.getOrgCategoryName());
				Integer integer = schoolMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++schoolElementNumber;
					schoolMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberB(String.valueOf(schoolMap.get(org.getOrgNodeMdrNumber())));
				tfil.setElementSpecialCodesB(org.getOrgNodeCode());
				tfil.setMdrNumB(org.getOrgNodeMdrNumber());
				tfil.setElementStructureLevelB("02");
				tfil.setElementIdB(org.getOrgNodeMdrNumber());
			}

			else if (org.getCategoryLevel() != null
					&& new Integer(organizationMapSize - 4).toString() != null
					&& org.getCategoryLevel()
					.equalsIgnoreCase(
							new Integer(organizationMapSize - 4)
							.toString())) {
				tfil.setElementNameC(org.getOrgNodeName());
				tfil.setElementLabelC(org.getOrgCategoryName());
				Integer integer = classMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++classElementNumber;
					classMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberC(String.valueOf(classMap.get(org.getOrgNodeMdrNumber())));
				tfil.setElementSpecialCodesC(org.getOrgNodeCode());
				tfil.setMdrNumC(org.getOrgNodeMdrNumber());
				tfil.setElementStructureLevelC("03");
				tfil.setElementIdC(org.getOrgNodeMdrNumber());

			}
			else if (org.getCategoryLevel() != null
					&& new Integer(organizationMapSize - 3).toString() != null
					&& org.getCategoryLevel()
					.equalsIgnoreCase(
							new Integer(organizationMapSize - 3)
							.toString())) {
				tfil.setElementNameD(org.getOrgNodeName());
				tfil.setElementLabelD(org.getOrgCategoryName());
				Integer integer = sectionMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++sectionElementNumber;
					sectionMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberD(String.valueOf(sectionMap.get(org.getOrgNodeMdrNumber())));
				tfil.setElementSpecialCodesD(org.getOrgNodeCode());
				tfil.setMdrNumD(org.getOrgNodeMdrNumber());
				tfil.setElementStructureLevelD("04");
				tfil.setElementIdD(org.getOrgNodeMdrNumber());

			}
			else if (org.getCategoryLevel() != null
					&& new Integer(organizationMapSize - 2).toString() != null
					&& org.getCategoryLevel()
					.equalsIgnoreCase(
							new Integer(organizationMapSize - 2)
							.toString())) {
				tfil.setElementNameE(org.getOrgNodeName());
				tfil.setElementLabelE(org.getOrgCategoryName());
				Integer integer = groupMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++groupElementNumber;
					groupMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberE(String.valueOf(groupMap.get(org.getOrgNodeMdrNumber())));
				tfil.setElementSpecialCodesE(org.getOrgNodeCode());
				tfil.setMdrNumE(org.getOrgNodeMdrNumber());
				tfil.setElementStructureLevelE("05");
				tfil.setElementIdE(org.getOrgNodeMdrNumber());

			}
			else if (org.getCategoryLevel() != null
					&& new Integer(organizationMapSize - 1).toString() != null
					&& org.getCategoryLevel().equalsIgnoreCase(
							new Integer(organizationMapSize - 1)
							.toString())) {
				tfil.setElementNameF(org.getOrgNodeName());
				tfil.setElementLabelF(org.getOrgCategoryName());
				Integer integer = divisionMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++divisionElementNumber;
					divisionMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberF(String.valueOf(divisionMap.get(org.getOrgNodeMdrNumber())));
				tfil.setElementSpecialCodesF(org.getOrgNodeCode());
				tfil.setMdrNumF(org.getOrgNodeMdrNumber());
				tfil.setElementStructureLevelF("06");
				tfil.setElementIdF(org.getOrgNodeMdrNumber());

			}
			else if (org.getCategoryLevel() != null
					&& org.getCategoryLevel().equalsIgnoreCase(
							organizationMapSize.toString())) {
				tfil.setElementNameG(org.getOrgNodeName());
				tfil.setElementLabelG(org.getOrgCategoryName());
				Integer integer = levelMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++levelElementNumber;
					levelMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberG(String.valueOf(levelMap.get(org.getOrgNodeMdrNumber())));
				tfil.setElementSpecialCodesG(org.getOrgNodeCode());
				tfil.setMdrNumG(org.getOrgNodeMdrNumber());
				tfil.setElementStructureLevelG("07");
				tfil.setElementIdG(org.getOrgNodeMdrNumber());
			}
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
		if(null != isClassNodeRequiredDummyName && !"".equalsIgnoreCase(isClassNodeRequiredDummyName) && organizationMapSize < 7){
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

	/**
	 * Set accommodation details for student
	 * @param sd
	 * @param customerDemographic
	 * @param tfil
	 * @param roster
	 */
	public void createAccomodations(Set<StudentDemographic> sd,
			Map<Integer, String> customerDemographic, TfilLL2ND tfil, TestRoster roster) {

		Map<String, StudentDemographic> set1 = new TreeMap<String, StudentDemographic>();
//		Map<String, CustomerDemographic> set2 = new TreeMap<String, CustomerDemographic>();
		Map<Integer, String> studentDemographic = Collections.synchronizedMap(new HashMap<Integer, String>());
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
	
	/**
	 * Set different Race field value for student
	 * @param sd
	 * @param tfil
	 * @param customerDemographicId
	 */
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

	/**
	 * Set Program participation value for student
	 * @param sd
	 * @param tfil
	 * @param productId
	 * @param customerDemographicId
	 */
	private void setProgramParticipation(Set<StudentDemographic> sd,
			TfilLL2ND tfil, Integer productId, Integer customerDemographicId) {

		for (StudentDemographic studentDemo : sd) {
			if(studentDemo.getCustomerDemographicId()==customerDemographicId) {
				if (studentDemo.getValueName().startsWith("AEL") 
						&& ((productId == 7501) || (productId == 7505))) {
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
						&& ((productId == 7501) || (productId == 7505))) {
					tfil.setOtherDemograph("1");
				}
			}
		}
	}

	/**
	 * Set Special education value for a student
	 * @param sd
	 * @param tfil
	 */
	private void setSpecialEducation(Set<StudentDemographic> sd, TfilLL2ND tfil) {

		for (StudentDemographic studentDemo : sd) {
			if (studentDemo.getValueName().equalsIgnoreCase("IEP")) {
				tfil.setIep("1");
			} else if (studentDemo.getValueName().equalsIgnoreCase("504")) {
				tfil.setSbi504("1");
			}
		}
	}
	
	/**
	 * Set Ethnicity value for a student
	 * @param sd
	 * @param tfil
	 */
	private void setEthnicity(Set<StudentDemographic> sd, TfilLL2ND tfil) {

		String valueName = null;
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
	
	/**
	 * Set Composite or Content Area details for student
	 * @param tfil
	 * @param roster
	 * @param invalidSubtestMap
	 * @param invalidSubtestList
	 * @throws Exception
	 */
	public void createSkillAreaScoreInformation(TfilLL2ND tfil, TestRoster roster, Map<String, Object[]> invalidSubtestMap, List<String> invalidSubtestList) throws Exception{
		
		Map<String, Object[]> treeMap = new TreeMap<String, Object[]>();
		Map<String, Object[]> overallMap = new TreeMap<String, Object[]>();
		Map<String,String> contentAreaFact = Collections.synchronizedMap(new HashMap<String, String>());
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
		ProficiencyLevelsLL2ND pl = new ProficiencyLevelsLL2ND();
		ScaleScoresLL2ND ss = new ScaleScoresLL2ND();
		ReferenceNormalCurveEquivalentsLL2ND  nce = new ReferenceNormalCurveEquivalentsLL2ND();
		ReferencePercentileRanksLL2ND pr = new ReferencePercentileRanksLL2ND();
		SkillAreaNumberCorrectLL2ND po = new SkillAreaNumberCorrectLL2ND();
		SkillAreaPercentCorrectLL2ND pc = new SkillAreaPercentCorrectLL2ND();
		String lexileScore = null;
		Set<SkillArea> skillAreaSet = roster.getSkillAreas();
		Iterator<SkillArea> it = skillAreaSet.iterator();
		while(it.hasNext()){
			SkillArea sArea = it.next();
			if (sArea.getName() != null){
				if(sArea.getName().equalsIgnoreCase("overall")){
					Object[] val = new Object[4];
					val[0] = sArea.getScaleScore();
					val[1] = sArea.getProficiencyLevel();
					val[2] = sArea.getNormCurveEqui();
					val[3] = sArea.getNationalPercentile();
					overallMap.put("overall", val);
				}else{
					Object[] val = new Object[7];
					val[1] = sArea.getScaleScore();
					val[2] = sArea.getProficiencyLevel();
					val[3] = sArea.getPointsObtained();
					val[4] = sArea.getPercentObtained();
					val[5] = sArea.getNormCurveEqui();
					val[6] = sArea.getNationalPercentile();

					if(sArea.getLexile() != null){
						lexileScore = sArea.getLexile();
					}
					String name = sArea.getName().toLowerCase();
					if (name.equalsIgnoreCase("speaking")) {
						treeMap.put(name.toLowerCase(), val);
						contentAreaFact.put("speaking",name.toLowerCase());
					} else if (name.equalsIgnoreCase("listening")) {
						treeMap.put(name.toLowerCase(), val);
						contentAreaFact.put("listening",name.toLowerCase());
					} else if (name.equalsIgnoreCase("reading")) {
						treeMap.put(name.toLowerCase(), val);
						contentAreaFact.put("reading",name.toLowerCase());
					} else if (name.equalsIgnoreCase("writing")) {
						treeMap.put(name.toLowerCase(), val);
						contentAreaFact.put("writing",name.toLowerCase());
					} else if (name.equalsIgnoreCase("comprehension")) {
						treeMap.put(name.toLowerCase(), val);
						contentAreaFact.put("comprehension",name.toLowerCase());
					} else if (name.equalsIgnoreCase("oral")) {
						treeMap.put(name.toLowerCase(), val);
						contentAreaFact.put("oral",name.toLowerCase());
					} else if (name.equalsIgnoreCase("productive")) {
						treeMap.put(name.toLowerCase(), val);
						contentAreaFact.put("productive",name.toLowerCase());
					} else if (name.equalsIgnoreCase("literacy")) {
						treeMap.put(name.toLowerCase(), val);
						contentAreaFact.put("literacy",name.toLowerCase());
					}
				}
			}
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
			invalidSubtestList.add("SPEAKING");
			isProficiencyLevelOverall = false;
			isScaleOverall = false;
		}
		if (listening > 0) {
			po.setListening("");
			pl.setListening("");
			pc.setListening("");
			isComprehensionPopulated = false;
			isOralPopulated = false;
			invalidSubtestList.add("LISTENING");
			isProficiencyLevelOverall = false;
			isScaleOverall = false;
		}
		if (reading > 0) {
			po.setReading("");
			pl.setReading("");
			pc.setReading("");
			isComprehensionPopulated = false;
			isLiteracyPopulated = false;
			invalidSubtestList.add("READING");
			isProficiencyLevelOverall = false;
			isScaleOverall = false;
		}
		if (writing > 0) {
			po.setWriting("");
			pl.setWriting("");
			pc.setWriting("");
			isProductivePopulated = false;
			isLiteracyPopulated = false;
			invalidSubtestList.add("WRITING");
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
	
	/**
	 * Set Objective level score details of student
	 * @param tfil
	 * @param roster
	 * @param invalidSubtestList
	 * @throws Exception
	 */
	public void createSubSkillAreaScoreInformation(TfilLL2ND tfil, TestRoster roster, List<String> invalidSubtestList) throws Exception{
		
		SubSkillNumberCorrectLL2ND subNumCorrect = new SubSkillNumberCorrectLL2ND();
		SubSkillPercentCorrectLL2ND subPercCorrect = new SubSkillPercentCorrectLL2ND();
		String subSkillName;
		Map<String, String> pointsObtained = Collections.synchronizedMap(new HashMap<String, String>());
		Map<String, String> percentObtained = Collections.synchronizedMap(new HashMap<String, String>());
		Map<String, String> subSkillAreaScoreInfo = Collections.synchronizedMap(new HashMap<String, String>());
		Map<String, String> subSkillAreaItemCategory = Collections.synchronizedMap(new HashMap<String, String>());
		boolean isInvalidSpeaking = invalidSubtestList.contains("SPEAKING");
		boolean isInvalidListeing = invalidSubtestList.contains("LISTENING");
		boolean isInvalidReading = invalidSubtestList.contains("READING");
		boolean isInvalidWriting = invalidSubtestList.contains("WRITING");
		Integer productId = null;
		String preferredForm = roster.getTest_admin().getPreferedForm();
		Set<SubSkillArea> subSkillAreas = roster.getSubSkillAreas();
		Iterator<SubSkillArea> it = subSkillAreas.iterator();
		while(it.hasNext()){
			SubSkillArea sa = it.next();
			if(productId == null){
				productId = roster.getProductId();
			}
			subSkillAreaScoreInfo.put((productId+sa.getSubSkillId()), sa.getSubSkillName());
			subSkillAreaItemCategory.put(sa.getSubSkillName(), sa.getSubSkillCategory());
		}
		subSkillAreas =null;
		subSkillAreas = roster.getPointsScores();
		Iterator<SubSkillArea> it1 = subSkillAreas.iterator();
		while(it1.hasNext()){
			SubSkillArea sa = it1.next();
			percentObtained.put(sa.getSubSkillId(), sa.getPercentObtained());
			pointsObtained.put(sa.getSubSkillId(), sa.getPointsObtained());
		}
		
		for (String x : percentObtained.keySet()) {
			subSkillName = subSkillAreaScoreInfo.get(x);
			if(subSkillName != null){
				if (subSkillName.contains("Social, Intercultural, and Instructional Communication") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subPercCorrect.setSpeakingSocialInstructionalCommunication(percentObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subPercCorrect.setListeningSocialInstructionalCommunication(percentObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						subPercCorrect.setReadingSocialInstructionalCommunication(percentObtained.get(x).toString());
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						subPercCorrect.setWritingSocialInstructionalCommunication(percentObtained.get(x).toString());
					}
				} else if (subSkillName.contains("Mathematics / Science / Technical Subjects") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						
						/**
						 * Below condition : Value X would be passed for Grade K (for Form T only)
						 * where score point is less than 3
						 */
						if (Integer.parseInt(pointsObtained.get(x).toString()) < 3
								&& "K".equalsIgnoreCase(roster.getStudent()
										.getGrade().toString())
								&& Constants.FORM_T.equals(preferredForm)){
							subPercCorrect.setSpeakingMathematicsScienceTechnical("XXXXX");
						}else{
							subPercCorrect.setSpeakingMathematicsScienceTechnical(percentObtained.get(x).toString());
						}
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subPercCorrect.setListeningMathematicsScienceTechnical(percentObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						
						/**
						 * Below condition : Value X would be passed for Grade K (For all Forms)
						 * where score point is less than 3
						 */
						if (Integer.parseInt(pointsObtained.get(x).toString()) < 3
								&& "K".equalsIgnoreCase(roster.getStudent()
										.getGrade().toString())){
							subPercCorrect.setReadingMathematicsScienceTechnical("XXXXX");
						}else{
							subPercCorrect.setReadingMathematicsScienceTechnical(percentObtained.get(x).toString());
						}
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						
						/**
						 * Below condition : Value X would be passed for Grade K (for all forms)
						 * where score point is less than 3
						 */
						if (Integer.parseInt(pointsObtained.get(x).toString()) < 3
								&& "K".equalsIgnoreCase(roster.getStudent()
										.getGrade().toString())){
							subPercCorrect.setWritingMathematicsScienceTechnical("XXXXX");
						}else{
							subPercCorrect.setWritingMathematicsScienceTechnical(percentObtained.get(x).toString());
						}
					}
				} else if (subSkillName.contains("Language Arts / Social Studies / History") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subPercCorrect.setSpeakingLanguageArtsScocialHistory(percentObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subPercCorrect.setListeningLanguageArtsScocialHistory(percentObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						/**
						 * Below condition : Value X would be passed for Grade K (For all Forms) 
						 * where score point is less than 3
						 */
						if (Integer.parseInt(pointsObtained.get(x).toString()) < 3
								&& "K".equalsIgnoreCase(roster.getStudent()
										.getGrade().toString())){
							subPercCorrect.setReadingLanguageArtsScocialHistory("XXXXX");
						}else{
							subPercCorrect.setReadingLanguageArtsScocialHistory(percentObtained.get(x).toString());
						}
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						/**
						 * Below condition : X would be passed for Grade K (for all forms)
						 * where score point is less than 3
						 */
						if (Integer.parseInt(pointsObtained.get(x).toString()) < 3
								&& "K".equalsIgnoreCase(roster.getStudent()
										.getGrade().toString())){
							subPercCorrect.setWritingLanguageArtsScocialHistory("XXXXX");
						}else{
							subPercCorrect.setWritingLanguageArtsScocialHistory(percentObtained.get(x).toString());
						}
					}
				} else if (subSkillName.contains("Foundational Skills") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
					if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						subPercCorrect.setReadingFoundationalSkills(percentObtained.get(x).toString());
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						subPercCorrect.setWritingFoundationalSkills(percentObtained.get(x).toString());
					}
				} else if (subSkillName.contains("Academic") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
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
				if (subSkillName.contains("Social, Intercultural, and Instructional Communication") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subNumCorrect.setSpeakingSocialInstructionalCommunication(pointsObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subNumCorrect.setListeningSocialInstructionalCommunication(pointsObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						subNumCorrect.setReadingSocialInstructionalCommunication(pointsObtained.get(x).toString());
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						subNumCorrect.setWritingSocialInstructionalCommunication(pointsObtained.get(x).toString());
					}
				} else if (subSkillName.contains("Mathematics / Science / Technical Subjects") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						
						/**
						 * Below condition : Value X would be passed for Grade K (for Form T only) 
						 * where score point is less than 3, Blank
						 */
						if (Integer.parseInt(pointsObtained.get(x).toString()) < 3
								&& "K".equalsIgnoreCase(roster.getStudent()
										.getGrade().toString())
								&& Constants.FORM_T.equals(preferredForm)){
							subNumCorrect.setSpeakingMathematicsScienceTechnical("XXX");
						}else{
							subNumCorrect.setSpeakingMathematicsScienceTechnical(pointsObtained.get(x).toString());
						}
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subNumCorrect.setListeningMathematicsScienceTechnical(pointsObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						
						/**
						 * Below condition : Value X would be passed for Grade K  (For all Forms) 
						 * where score point is less than 3, Blank
						 */
						if (Integer.parseInt(pointsObtained.get(x).toString()) < 3
								&& "K".equalsIgnoreCase(roster.getStudent()
										.getGrade().toString())){
							subNumCorrect.setReadingMathematicsScienceTechnical("XXX");
						}else{
							subNumCorrect.setReadingMathematicsScienceTechnical(pointsObtained.get(x).toString());
						}
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						
						/**
						 * Below condition : Value  X would be passed for Grade K (for all forms)
						 * where score point is less than 3
						 */
						if (Integer.parseInt(pointsObtained.get(x).toString()) < 3
								&& "K".equalsIgnoreCase(roster.getStudent()
										.getGrade().toString())){
							subNumCorrect.setWritingMathematicsScienceTechnical("XXX");
						}else{
							subNumCorrect.setWritingMathematicsScienceTechnical(pointsObtained.get(x).toString());
						}
					}
				} else if (subSkillName.contains("Language Arts / Social Studies / History") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
					if("Speaking".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidSpeaking){
						subNumCorrect.setSpeakingLanguageArtsScocialHistory(pointsObtained.get(x).toString());
					}else if("Listening".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidListeing){
						subNumCorrect.setListeningLanguageArtsScocialHistory(pointsObtained.get(x).toString());
					}else if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						
						/**
						 * Below condition : X would be passed for Grade K  (For all Forms)
						 * where score point is less than 3, Blank
						 */
						if (Integer.parseInt(pointsObtained.get(x).toString()) < 3
								&& "K".equalsIgnoreCase(roster.getStudent()
										.getGrade().toString())){
							subNumCorrect.setReadingLanguageArtsScocialHistory("XXX");
						}else{
							subNumCorrect.setReadingLanguageArtsScocialHistory(pointsObtained.get(x).toString());
						}
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						
						/**
						 * Below condition : Value  X would be passed for Grade K (for all forms)
						 * where score point is less than 3
						 */
						if (Integer.parseInt(pointsObtained.get(x).toString()) < 3
								&& "K".equalsIgnoreCase(roster.getStudent()
										.getGrade().toString())){
							subNumCorrect.setWritingLanguageArtsScocialHistory("XXX");
						}else{
							subNumCorrect.setWritingLanguageArtsScocialHistory(pointsObtained.get(x).toString());
						}
					}
				} else if (subSkillName.contains("Foundational Skills") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
					if("Reading".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidReading){
						subNumCorrect.setReadingFoundationalSkills(pointsObtained.get(x).toString());
					}else if("Writing".equalsIgnoreCase(subSkillAreaItemCategory.get(subSkillName)) && !isInvalidWriting){
						subNumCorrect.setWritingFoundationalSkills(pointsObtained.get(x).toString());
					}
				} else if (subSkillName.contains("Academic") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
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
	
	
	private boolean fillValidSubSkillScore(String skillName, Map<String, String> subSkillCategory, List<String> invalidSubtestList){
		String skill;
		skill = subSkillCategory.get(skillName);
		
		if(skill.equalsIgnoreCase("Speaking") && invalidSubtestList.contains(skill.toUpperCase())){
			return false;
		}else if(skill.equalsIgnoreCase("Listening") && invalidSubtestList.contains(skill.toUpperCase())){
			return false;
		}else if(skill.equalsIgnoreCase("Reading") && invalidSubtestList.contains(skill.toUpperCase())){
			return false;
		}else if(skill.equalsIgnoreCase("Writing") && invalidSubtestList.contains(skill.toUpperCase())){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Set response details of student
	 * 
	 */
	public void createItemResponseInformation(TestRoster roster, TfilLL2ND tfil) throws Exception{
		

		Map<String, Map<String, List<RostersItem>>> allItems = roster.getAllitemDetails();
		ItemResponsesGRTLL2ND responsesGRT = new ItemResponsesGRTLL2ND();
		//String speakingMCItems = "";
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
			Map<String, List<RostersItem>> speakingitem = allItems.get("SPEAKING");
			
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
			Map<String, List<RostersItem>> listeningitem = allItems
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
			Map<String, List<RostersItem>> readingitem = allItems
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
			Map<String, List<RostersItem>> writingitem = allItems
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


	/**
	 * Get students response details
	 * @param oascon
	 * @param cache
	 * @param rosterIds
	 * @throws Exception
	 */
	private void getItemResponseGrt(Connection oascon, SimpleCache cache, String rosterIds) throws Exception {
		Set<Integer> rosterSet = new HashSet<Integer>();
		Map<String,Map<String,  List<RostersItem>>> allitem = Collections.synchronizedMap(new HashMap<String,Map<String,  List<RostersItem>>> ());
		PreparedStatement ps = null ;
		ResultSet rs = null;
		try{
			String [] str = rosterIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.ALL_ITEM_DETAILS_FOR_ROSTER.replace("#", str[i]);
				ps = oascon.prepareStatement(query);
				ps.setFetchSize(100);
				rs = ps.executeQuery();
				while (rs.next()) {					
					RostersItem item = new RostersItem();
					Integer rosterId = rs.getInt(1);
					TestRoster rosters = cache.getRoster(rosterId);
					if(rosters != null){
						item.setItemId(rs.getString(2));
						item.setItemIndx(rs.getString(3));
						item.setItemType(rs.getString(4));
						item.setItemCorrectResponse((null == rs.getString(5))? "":rs.getString(5));
						item.setItemSetIdTD(rs.getString(6));				
						item.setItemDescriptio(rs.getString(7));
						item.setItemValidationStatusForScoring(rs.getString(8));
						item.setIsSubtestCompleted(rs.getString(9));
						item.setStudentResponse((null == rs.getString(10))? "":rs.getString(10));
						if(!rosterSet.contains(rosterId)){
							allitem = Collections.synchronizedMap(new HashMap<String,Map<String,  List<RostersItem>>> ());
							rosterSet.add(rosterId);
						}
//						synchronized(rosters){
							populateMap(allitem, item);
							rosters.setAllitemDetails(allitem);
							cache.addRoster(rosterId, rosters);
//						}
					}
				}
				SqlUtil.close(ps, rs);
			}
			System.out.print("\b");
			System.out.print("2");
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new Exception("SQLException at getItemResponseGrt:"+e.getMessage());
		}finally {
			SqlUtil.close(ps, rs);
		}
	}
	
	private void populateMap(Map<String, Map<String, List<RostersItem>>> allitem,RostersItem item) {

		String itemDesc = item.getItemDescriptio().toUpperCase();
		String itemtype = item.getItemType().toUpperCase();
		if (allitem.get(itemDesc) != null) {
			Map<String, List<RostersItem>> descMap = allitem.get(itemDesc);
			if (descMap.get(itemtype) != null) {
				List<RostersItem> rostersItemList = descMap.get(itemtype);
				rostersItemList.add(item);
			} else {
				List<RostersItem> rostersItemList = Collections.synchronizedList(new ArrayList<RostersItem>());
				descMap.put(itemtype, rostersItemList);
				rostersItemList.add(item);
			}

		} else {
			Map<String, List<RostersItem>> typeMap =  Collections.synchronizedMap(new HashMap<String, List<RostersItem>>());
			allitem.put(itemDesc, typeMap);
			List<RostersItem> rostersItemList = Collections.synchronizedList( new LinkedList<RostersItem>());
			typeMap.put(itemtype, rostersItemList);
			rostersItemList.add(item);
		}
	}
	
	private String constractSRItemResponseString(String MCItems, Map<String, List<RostersItem>> itemS, int positionLength ) {
		List<RostersItem> itemlist = itemS.get("SR");
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
			Map<String, List<RostersItem>> itemMap, int positionLength) {
		List<RostersItem> itemlist = itemMap.get("CR");
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
}
