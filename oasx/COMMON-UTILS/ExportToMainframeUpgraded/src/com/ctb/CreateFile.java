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

import com.ctb.dto.Accomodations;
import com.ctb.dto.CustomerDemographic;
import com.ctb.dto.CustomerDemographicValue;
import com.ctb.dto.ItemResponsesGRT;
import com.ctb.dto.OrderFile;
import com.ctb.dto.Organization;
import com.ctb.dto.ProficiencyLevels;
import com.ctb.dto.RostersItem;
import com.ctb.dto.ScaleScores;
import com.ctb.dto.SkillArea;
import com.ctb.dto.SkillAreaNumberCorrect;
import com.ctb.dto.SkillAreaPercentCorrect;
import com.ctb.dto.SpecialCodes;
import com.ctb.dto.Student;
import com.ctb.dto.StudentContact;
import com.ctb.dto.StudentDemographic;
import com.ctb.dto.StudentItemSetStatus;
import com.ctb.dto.SubSkillArea;
import com.ctb.dto.SubSkillNumberCorrect;
import com.ctb.dto.SubSkillPercentCorrect;
import com.ctb.dto.TestAdmin;
import com.ctb.dto.TestRoster;
import com.ctb.dto.Tfil;
import com.ctb.utils.Configuration;
import com.ctb.utils.Constants;
import com.ctb.utils.EmetricUtil;
import com.ctb.utils.ExtractUtil;
import com.ctb.utils.ProcessRosters;
import com.ctb.utils.SftpUtil;
import com.ctb.utils.SimpleCache;
import com.ctb.utils.SqlUtil;
import com.jcraft.jsch.Session;
/**
 * 
 * @author TCS
 *
 */
public class CreateFile{
	static Logger logger = Logger.getLogger(CreateFile.class.getName());
	
	private static final int BATCH_SIZE = 998;
	private int districtElementNumber = 0;
	private int schoolElementNumber = 0;
	private int classElementNumber = 0;
	private int sectionElementNumber = 0;
	private int groupElementNumber = 0;
	private int divisionElementNumber = 0;
	private int levelElementNumber = 0;
	private int studentElementNumber = 0;
	private static String customerModelLevelValue = null;
	private static String customerState = null;
	private static String customerCity = null;
	private static String testDate = null;
	private static String programDate = null;
	private boolean isValidStartDate = false;
	private boolean isValidEndDate = false;
	private String extractSpanStartDate;
	private String extractSpanEndDate;
	private Integer customerId;
	private Integer frameworkProductId;
	private String MFid;
	private Map<String, Integer> districtMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> schoolMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> classMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> sectionMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> groupMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> divisionMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> levelMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<Integer, Integer> studentMap = Collections.synchronizedMap(new HashMap<Integer, Integer>());
	static Map<String, String> wrongMap = new TreeMap<String, String>();
	private static Integer ROSTER_COUNTER = Integer.valueOf(0);
	private static String exportedRosters = null;
	private static String unexportedRosters = null;
	static {
		
		wrongMap.put("A", "1");
		wrongMap.put("B", "2");
		wrongMap.put("C", "3");
		wrongMap.put("D", "4");
	}
	
	
	public CreateFile() {
		super();
	}
		
	public CreateFile(boolean isValidStartDate, boolean isValidEndDate,
			String extractSpanStartDate, String extractSpanEndDate,
			Integer customerId, Integer frameworkProductId, String MFid, int classLevelElementNumber) {
		super();
		this.isValidStartDate = isValidStartDate;
		this.isValidEndDate = isValidEndDate;
		this.extractSpanStartDate = extractSpanStartDate;
		this.extractSpanEndDate = extractSpanEndDate;
		this.customerId = customerId;
		this.frameworkProductId = frameworkProductId;
		this.MFid = MFid;
		this.levelElementNumber = classLevelElementNumber;
	}

	/**
	 * Write Export File and Order File and transfer file to specific location for LAS Links Form A/B/Espanol
	 * @throws IOException
	 * @throws FFPojoException
	 * @throws SQLException
	 * @throws Exception
	 */
	public void writeToText() throws IOException, FFPojoException, SQLException,Exception {

		OrderFile orderFile = new OrderFile();
		/**
		 * Generate collection of export data object
		 */
		List<Tfil> myList = createList(orderFile);
		if(null == myList){
			logger.info("No roster found to extract.\nExecution forcefully stopped.");
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
		
		if(!(new File(localFilePath)).exists()){
			File f = new File(localFilePath);
			f.mkdirs();
		}
		File file = new File(localFilePath, fileName.toString());
		FlatFileWriter ffWriter = null;
		try{
			ffWriter = new FileSystemFlatFileWriter(file, true);
			ffWriter.writeRecordList(myList);
			ffWriter.close();
			logger.info("Export file successfully generated :: [" + fileName.toString() + "]");
			orderFile.setDataFileName(EmetricUtil.truncate(fileName.toString(),
					new Integer(100)).substring(0, fileName.length()));
			logger.info("Preparing Order File...");
			prepareOrderFile(orderFile, localFilePath, orderFileName);
		} finally {
			if(ffWriter!=null){
				ffWriter.close();
			}
		}
		logger.info("Data file transfer process started...");
		/* SFTP the generated data file: Start */
		String destinationDir = Configuration.getFtpFilepath();
		Session session = null;
		try{
			session = SftpUtil.getSFtpSession();
			SftpUtil.doSftp(session, destinationDir, localFilePath, fileName.toString());
			logger.info("Data file transfer process completed.");
		}catch(Exception e){
			logger.error("Data file transfer process failed.");
			logger.error(e.getMessage());
			e.printStackTrace();
		}finally{
			SftpUtil.closeSFtpClient(session);
		}
		/* SFTP the generated data file: End */
		
		if(!"".equals(Configuration.getMarkAsStudnetExported())
				&& "true".equalsIgnoreCase(Configuration.getMarkAsStudnetExported())){
			logger.info("Started marking student exported flag...");
			markExportedFlagAsTrue();
			logger.info("Marking process has been completed...");
		}
	}

	/**
	 * Collect all students data and create a collection.
	 * @param orderFile
	 * @return Collection of flat file object.
	 * @throws Exception
	 */
	private List<Tfil> createList(OrderFile orderFile) throws Exception {
		List<Tfil> tfilList = Collections.synchronizedList(new ArrayList<Tfil>());
		Map<Integer, String> customerDemographic = Collections.synchronizedMap(new HashMap<Integer, String>());
		Set<CustomerDemographic> setAccomodation = Collections.synchronizedSet(new HashSet<CustomerDemographic>());
		SimpleCache cache = new SimpleCache();
		Integer studentCount = 0;
		Connection oascon = null;
		Connection irscon = null;
		Thread thread = null;
		
		try {
			logger.info("Processing started For Laslink First Edition...");
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
			setAccomodation = getCustomerLeveledDemographicValue(oascon);
			logger.info("Customer information collection is completed");
			
			
			Integer threadCount = Integer.valueOf(Configuration.getThreadCount());
			ExecutorService executor = Executors.newFixedThreadPool(threadCount.intValue());
			logger.info("Roster Processing started with thread count :: " + threadCount);
			
			for (Integer rosterId : cache.getKeys()) {
				//studentCount = getStudentCounter();
				TestRoster roster = cache.getRoster(rosterId);
				ProcessRosters process = new ProcessRosters(roster,
						customerModelLevelValue, customerState, customerCity,
						customerDemographic, setAccomodation,
						orderFile, this);
				thread = new Thread(process);
				executor.execute(thread);
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
				// Break after all the task is completed.
			}
			
			orderFile.setCaseCount(studentCount.toString());
			tfilList = ProcessRosters.getFinalList();
			
		} finally {
			SqlUtil.close(oascon);
			SqlUtil.close(irscon);
			cache.closeCache();
		}
		/**
		 * Sort the collection with compare to student element number in ASC order
		 */
		Collections.sort(tfilList, new Comparator<Tfil>() {
	        @Override
	        public int compare(Tfil  tfil, Tfil  tfil2)
	        {
	            return  tfil.getStudentElementNumber().compareTo(tfil2.getStudentElementNumber());
	        }
	    });
		return tfilList;
	}

	public static synchronized int getStudentCounter(){
		return (CreateFile.ROSTER_COUNTER = Integer.valueOf(ROSTER_COUNTER.intValue() + 1)).intValue();
	}
	
	/**
	 * Unused method for LAS Links Form A/B/Espanol
	 * @param oascon
	 * @param roster
	 * @param tfil
	 * @throws SQLException
	 */
	public void createItemResponseInformation(Connection oascon,
			TestRoster roster, Tfil tfil) throws SQLException {
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
	

	/**
	 * Unused method for LAS Links Form A/B/Espanol
	 * @param speakingCRItems
	 * @param speakingitem
	 * @return
	 */
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
	
	
	/**
	 * Unused method for LAS Links Form A/B/Espanol
	 * @param speakingMCItems
	 * @param speakingitem
	 * @return
	 */
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
	
	
	
	/**
	 * Unused method for LAS Links Form A/B/Espanol
	 * @param oascon
	 * @param roster
	 * @return
	 * @throws SQLException
	 */
	private Map<String,TreeMap<String,  LinkedList<RostersItem>>> getItemResponseGrt(Connection oascon, TestRoster roster) throws SQLException {
		Map<String,TreeMap<String,  LinkedList<RostersItem>>> allitem =  new TreeMap<String,TreeMap<String,  LinkedList<RostersItem>>> ();
		PreparedStatement ps = null ;
		ResultSet rs = null;
		 Map<String,RostersItem> srValueMap = new TreeMap<String,RostersItem>();
		 Map<String,RostersItem> crValueMap = new TreeMap<String,RostersItem>();
		try{
			ps = oascon.prepareStatement(Constants.rosterAllItemDetails);
			ps.setInt(1, roster.getTestRosterId());
			ps.setInt(2, roster.getTestRosterId());
			ps.setInt(3, roster.getTestRosterId());
			ps.setInt(4, roster.getTestRosterId());
			rs = ps.executeQuery(); 
			rs.setFetchSize(500);
			while (rs.next()){
				
				RostersItem item = new RostersItem();
				item.setItemId(rs.getString(1));
				item.setItemIndx(rs.getString(2));
				item.setItemType(rs.getString(3));
				item.setItemCorrectResponse((null == rs.getString(4))? "":rs.getString(4));
				item.setItemSetIdTD(rs.getString(5));				
				item.setItemDescriptio(rs.getString(6));
				item.setItemValidationStatusForScoring(rs.getString(8));
				item.setStudentResponse((null == rs.getString(9))? "":rs.getString(9));
				populateMap(allitem, item, srValueMap,crValueMap );
			}
			
		}finally {
			SqlUtil.close(ps, rs);
		}
		return allitem;
	}
	


	/**
	 * Unused method for LAS Links Form A/B/Espanol
	 * @param allitem
	 * @param item
	 * @param srValueMap
	 * @param crValueMap
	 */
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

	/**
	 * Fetch all required rosters information of a customer
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
		StringBuilder sessionStuIds = new StringBuilder();
		StringBuilder adminIds = new StringBuilder();
		StringBuilder rosterIds = new StringBuilder();
		int cot = 0;
		try{
			if (!"".equals(Configuration.getMarkAsStudnetExported())
					&& "true".equalsIgnoreCase(Configuration.getMarkAsStudnetExported())){
				Constants.modifiedQueryToFetchRosters += Constants.STUDENT_EXPORT_CHECK_CONDITION; 
			}
			if(!"".equals(Configuration.getExcludeInvRosters())
					&& "true".equalsIgnoreCase(Configuration.getExcludeInvRosters())){
				Constants.modifiedQueryToFetchRosters += Constants.EXCLUDE_INVALIDATED_ROSTERS_CONDITION;
			}
			if(!"".equals(Configuration.getExcludeInactiveStudent())
					&& "true".equalsIgnoreCase(Configuration.getExcludeInactiveStudent())){
				Constants.modifiedQueryToFetchRosters += Constants.EXCLUDE_INACTIVE_STUDENTS_CONDITION;
			}
			if(this.isValidStartDate) {
				String START_DATE = this.extractSpanStartDate.replaceAll("/", "").toString().trim();
				Constants.modifiedQueryToFetchRosters += " AND TRUNC(ROSTER.COMPLETION_DATE) >= TO_DATE('" + START_DATE + "', 'MMDDYYYY')";
			}
			if(this.isValidEndDate) {
				String END_DATE = this.extractSpanEndDate.replaceAll("/", "").toString().trim();
				Constants.modifiedQueryToFetchRosters += " AND TRUNC(ROSTER.COMPLETION_DATE) <= TO_DATE('" + END_DATE + "', 'MMDDYYYY')";
			}
			ps = oascon.prepareStatement(Constants.modifiedQueryToFetchRosters);
			ps.setInt(1, frameworkProductId);
			ps.setInt(2, customerId);
			ps.setInt(3, frameworkProductId);
			ps.setInt(4, customerId);
			ps.setInt(5, frameworkProductId);
			ps.setInt(6, customerId);
			ps.setFetchSize(100);
			rs = ps.executeQuery();
			int counter = 0;
			while (rs.next()){
				TestRoster ros = new TestRoster();
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
				adm.setDateTestingCompleted(rs.getString(22));
				
				if(adm.getTestDate()== null || adm.getProgramStartDate() == null || adm.getPreferedForm() == null){
					throw new Exception(
							"Any of these field, Roster START_DATE_TIME or Test PREFERED_FORM or TIME_ZONE or PROGRAM may have null value for roster "
									+ ros.getTestRosterId());
				}
				
				//generate student sequence 
				Integer integer = studentMap.get(ros.getTestRosterId());
				if (integer == null) {
					integer = ++studentElementNumber;
					studentMap.put(ros.getTestRosterId(), integer);
				}
				
				rosterIds.append(ros.getTestRosterId());
				rosterIds.append(',');
				counter++;
				if(counter % BATCH_SIZE == 0){
					rosterIds.append('#');
					counter = 0;
				}
				cache.addRoster(ros.getTestRosterId(),ros);
				cot++;
			}
			logger.info("#Total roster found :: "+cot);
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		if(cache.size() != 0){
			//: If  Student exported mark parameter is true then validate the hand scoring status for each roster
			if (!"".equals(Configuration.getMarkAsStudnetExported())
					&& "true".equalsIgnoreCase(Configuration.getMarkAsStudnetExported())){
				logger.info("Validating Hand Scoring Status for each rosters...");
				int inCompletedCount = validateRosterHandScoringStatus(oascon,
						irscon, cache, rosterIds.toString());
				if (inCompletedCount != 0) {
					logger.info("#Hand Scoring status is INCOMPLETE for "
							+ inCompletedCount + " rosters out of " + cot + " ...");
					logger.info("Unscored rosters are :: "+CreateFile.unexportedRosters);
				}
				logger.info("Validation process is completed...");
			}
			if(cache.size() != 0){
				logger.info("#To be exported rosters :: " + cache.size());
				populatedRequiredData(cache, studentIds, rosterStud, sessionStuIds,
						adminIds, (rosterIds = new StringBuilder()));
				logger.info("Roster process will be started after 8 level of caching...");
				generateStudentDetails(oascon, irscon, cache,
						studentIds.toString(), rosterStud.toString(), sessionStuIds
								.toString(), adminIds.toString(), rosterIds
								.toString());
				logger.info("8 level of Caching process completed... #Total Cache Size :: "+ cache.size());
			}
		}
	}

	/**
	 * Populate required combined string for further query
	 * @param cache 
	 * @param studentIds
	 * @param rosterStud
	 * @param sessionStuIds
	 * @param adminIds
	 * @param rosterIds
	 */
	private void populatedRequiredData(SimpleCache cache, StringBuilder studentIds,
			StringBuilder rosterStud, StringBuilder sessionStuIds,
			StringBuilder adminIds, StringBuilder rosterIds) {
		if(cache.size()!= 0){
			int counter = 0;
			boolean firstValue = true;
			for(Integer rosterId: cache.getKeys()){
				TestRoster ros = cache.getRoster(rosterId);
				Student std = ros.getStudent();
				String sisId = "("+ros.getStudentId()+","+ros.getTestRosterId()+")";
				String irsId = "("+ros.getStudentId()+","+ros.getTestAdminId()+")";
				if(firstValue){
					firstValue = false;
				}else{
					studentIds.append(',');
					rosterStud.append(',');
					sessionStuIds.append(',');
					adminIds.append(',');
					rosterIds.append(',');
				}
				studentIds.append(std.getStudentId());
				rosterStud.append(sisId);
				sessionStuIds.append(irsId);
				adminIds.append(ros.getTestAdminId());
				rosterIds.append(ros.getTestRosterId());
				counter++;
				if(counter % BATCH_SIZE == 0){
					studentIds.append('#');
					rosterStud.append('#');
					sessionStuIds.append('#');
					adminIds.append('#');
					rosterIds.append('#');
					firstValue = true;
					counter = 0;
				}
			}
			CreateFile.exportedRosters = rosterIds.toString();
		}
	}

	/**
	 * Populate contact, hierarchy, test status, response and result details for all students
	 * @param oascon
	 * @param irscon
	 * @param cache
	 * @param studentIds
	 * @param rosterStudIds
	 * @param sessionStuIds
	 * @param adminIds
	 * @param rosterIds
	 * @throws Exception
	 */
	private void generateStudentDetails(Connection oascon, Connection irscon, SimpleCache cache, String studentIds, String rosterStudIds,
			String sessionStuIds, String adminIds, String rosterIds) throws Exception {
		getStudentItemSetStatus(oascon, cache, rosterStudIds);
		getSubtestModelFlag(oascon,cache, rosterStudIds);
		getStudentOrganization(oascon, cache, rosterIds);
		Map<Integer, Set<StudentContact>> studConMap = getStudentContact(oascon, studentIds);
		Map<Integer, Set<StudentDemographic>> studDemoMap = getStudentDemographic(oascon, studentIds);
		Map<String, Set<SkillArea>> rosterSkillAreaMap = getScoreSkillArea(irscon, cache, sessionStuIds);
		Map<Integer, Set<SubSkillArea>> subSkillMap = getSubSkillInformation(oascon, adminIds);
		Map<String, Set<SubSkillArea>> pointsMap = getIrsSubSkillScore(irscon,sessionStuIds);
		if ((studConMap != null && studConMap.size() > 0)
				|| (studDemoMap != null && studDemoMap.size() > 0)
				|| rosterSkillAreaMap.size() > 0){
			List<Integer> rosterIdList = cache.getKeys();
			Iterator<Integer> it = rosterIdList.iterator();
			while(it.hasNext()){
				Integer rosterId = it.next(); 
				TestRoster roster = cache.getRoster(rosterId);
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
		System.out.print("\b");
		System.out.println("8 ... Done");
		studConMap = null;
		studDemoMap = null;
		rosterSkillAreaMap = null;
		subSkillMap = null;
		pointsMap = null;
	}
	
	/**
	 * Get sub test model indicator flag for all students
	 * @param oascon
	 * @param cache
	 * @param rosterStudIds
	 * @throws Exception
	 */
	private void getSubtestModelFlag(Connection oascon, SimpleCache cache, String rosterStudIds) throws Exception{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = rosterStudIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.subtestIndicator.replace("#", str[i]);
				ps = oascon.prepareStatement(query);
				rs = ps.executeQuery();
				rs.setFetchSize(50);
				while (rs.next()) {
					Integer testRosterId = rs.getInt(1);
					TestRoster roster = cache.getRoster(testRosterId);
					if(roster != null) {
						synchronized(roster){
							if(rs.getString(2) != null && "T".equals(rs.getString(2))) {
								roster.setSubtestModel("T");
							}else {
								roster.setSubtestModel("");
							}
							cache.addRoster(testRosterId, roster);
						}
					}
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getSubtestModelFlag:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
	}

	/**
	 * Get students objective level score information
	 * @param irscon
	 * @param sessionStudents
	 * @return Map<String, Set<SubSkillArea>> rosterSkillAreaMap
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
				String query = Constants.subSkillIrsInformation.replaceAll("#", str[i]);
				ps = irscon.prepareStatement(query);
				rs = ps.executeQuery();
				rs.setFetchSize(100);
				while (rs.next()) {
					String key = rs.getString(2);
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
			System.out.print("7");
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
		Set<SubSkillArea> subSkillSet = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = adminIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.subSkillItemAreaInformation.replaceAll("#", str[i]);
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				rs.setFetchSize(100);
				while (rs.next()) {
					Integer key = rs.getInt(1);
					SubSkillArea sa = new SubSkillArea();
					sa.setSubSkillId(rs.getString(2));
					sa.setSubSkillName(rs.getString(3));
					sa.setSubSkillCategory(rs.getString(4));
					if(!subSkillMap.containsKey(key)){
						subSkillSet = Collections.synchronizedSet(new HashSet<SubSkillArea>());
						subSkillSet.add(sa);
						subSkillMap.put(key, subSkillSet);
					}else{
						subSkillMap.get(key).add(sa);
					}
				}
			}
			System.out.print("\b");
			System.out.print("6");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getSubSkillInformation:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
		
		return subSkillMap;
	}
	
	/**
	 * Get students content area level score details
	 * @param irscon
	 * @param cache
	 * @param sessionStuIds
	 * @return Map<String, Set<SkillArea>> rosterSkillAreaMap
	 * @throws Exception
	 */
	private Map<String, Set<SkillArea>> getScoreSkillArea(Connection irscon, SimpleCache cache, String sessionStuIds) throws Exception{
		Map<String, Set<SkillArea>> rosterSkillAreaMap = Collections.synchronizedMap(new HashMap<String, Set<SkillArea>>());
		Set<SkillArea> skillAreaSet = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = sessionStuIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.scoreSkilAreaSQL.replaceAll("#", str[i]);
				ps = irscon.prepareStatement(query);
				rs = ps.executeQuery();
				rs.setFetchSize(100);
				while (rs.next()) {
					String key = rs.getString(1);
					SkillArea skillArea = new SkillArea();
					skillArea.setName(rs.getString(2));
					skillArea.setScaleScore(rs.getString(3));
					skillArea.setProficiencyLevel(rs.getString(4));
					skillArea.setPointsObtained(rs.getString(5));
					skillArea.setPercentObtained(rs.getString(6));
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
			System.out.print("5");
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
		Set<Organization> orgSet = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = rosterIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.sql.replace("#", str[i]);
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				rs.setFetchSize(100);
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
						synchronized(roster){
							roster.setOrgnaizationSet(orgSet);
							cache.addRoster(testRosterId, roster);
						}
					}
					
				}
				SqlUtil.close(ps, rs);
			}
			System.out.print("\b");
			System.out.print("2");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getStudentOrganization:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
	}

	/**
	 * Get all students test status details
	 * @param con
	 * @param cache
	 * @param rosterStudIds
	 * @throws SQLException
	 */
	private void getStudentItemSetStatus(
			Connection con, SimpleCache cache, String rosterStudIds) throws SQLException{
		
		Set<Integer>rosterIds = new HashSet<Integer>();
		Set<StudentItemSetStatus> sissSet = Collections.synchronizedSet(new HashSet<StudentItemSetStatus>());
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = rosterStudIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.testRosterDetails.replace("#", str[i]);
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				rs.setFetchSize(50);
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
			throw new SQLException("SQLException at getStudentItemSetStatus:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = studentIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.studentDemographicSql.replace("#", str[i]);
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				rs.setFetchSize(100);
				while (rs.next()) {
					Integer studentId = rs.getInt(5);
					StudentDemographic studentDemographic = new StudentDemographic();
					Set<StudentDemographic> studentDemographicSet = Collections.synchronizedSet(new HashSet<StudentDemographic>());
					studentDemographic.setStudentId(studentId);
					studentDemographic.setStudentDemographicId(rs.getInt(1));
					studentDemographic.setCustomerDemographicId(rs.getInt(2));
					studentDemographic.setValueName(rs.getString(3));
					studentDemographic.setValue(rs.getString(4));
					
					if(!studentDemoMap.containsKey(studentId)){
						studentDemographicSet.add(studentDemographic);
						studentDemoMap.put(studentId, studentDemographicSet);
					}else{
						studentDemoMap.get(studentId).add(studentDemographic);
					}
					
				}
				SqlUtil.close(ps, rs);
			}
			System.out.print("\b");
			System.out.print("4");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("SQLException at getStudentDemographic:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return studentDemoMap;
	}

	/**
	 * Get all students contact details
	 * @param con
	 * @param studentIds
	 * @return Map<Integer, Set<StudentContact>> studentContactMap
	 * @throws Exception
	 */
	private Map<Integer, Set<StudentContact>> getStudentContact(Connection con, String studentIds)
	throws Exception {
		Map<Integer, Set<StudentContact>> studentContactMap = Collections.synchronizedMap(new HashMap<Integer, Set<StudentContact>>());
		Set<StudentContact> studentContact = new HashSet<StudentContact>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String [] str = studentIds.split("#");
			for(int i=0; i<str.length;i++){
				String query = Constants.studentContactSql.replace("#", str[i]);
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				rs.setFetchSize(100);
				while (rs.next()) {
					StudentContact sdentCon = new StudentContact();
					Integer studentId = rs.getInt(1);
					sdentCon.setStudentContactId(rs.getInt(2));
					sdentCon.setCity(rs.getString(3));
					sdentCon.setState(rs.getString(4));
					if(!studentContactMap.containsKey(studentId)){
						studentContact.add(sdentCon);
						studentContactMap.put(studentId, studentContact);
					}else{
						studentContactMap.get(studentId).add(sdentCon);
					}
					
				}
				SqlUtil.close(ps, rs);
			}
			System.out.print("\b");
			System.out.print("3");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("SQLException at getStudentContact:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
		return studentContactMap;
	}

	/**
	 * Populate all required details of customer
	 * @param con
	 * @param orderFile
	 * @throws SQLException
	 */
	private void populateCustomer(Connection con, OrderFile orderFile)
	throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(Constants.customersql);
			ps.setInt(1, this.customerId);
			rs = ps.executeQuery();
			while (rs.next()) {
				CreateFile.customerState = rs.getString(1);
				CreateFile.customerCity = rs.getString(2);
				CreateFile.customerModelLevelValue = rs.getString(6);
				orderFile.setCustomerStateAbbrevation(rs.getString(1));
				orderFile.setCustomerEmail(EmetricUtil.truncate(
						rs.getString(3), new Integer(64)));
				orderFile.setCustomerPhone(EmetricUtil.truncate(EmetricUtil
						.convertPhoneNumber(rs.getString(4)), new Integer(21)));
				orderFile.setCustomerContact(EmetricUtil.truncate(rs
						.getString(5), new Integer(64)));
			}

		} finally {
			SqlUtil.close(ps, rs);
		}
	}

	/**
	 * Get all demographic information for a customer
	 * @param con
	 * @return Map of customer demographic Id and customer demographic label
	 * @throws Exception
	 */
	private Map<Integer, String> getCustomerDemographicLebelMap(Connection con) throws Exception {
		
		Map<Integer, String> demoMap = Collections.synchronizedMap(new HashMap<Integer, String>());
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(Constants.customerDemographicsql);
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
			e.printStackTrace();
			throw new Exception("SQLException at getCustomerDemographic:"+e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}

		return demoMap;
	}


	/**
	 * Get details of ACCOMMODATION label of customer demographic
	 * @param con
	 * @return Set a accommodation demographics details
	 * @throws SQLException
	 */
	private Set<CustomerDemographic> getCustomerLeveledDemographicValue(
			Connection con) throws SQLException {

		Set<CustomerDemographic> mySet = Collections.synchronizedSet(new HashSet<CustomerDemographic>());
		Set<CustomerDemographicValue> customerDemographicValue = Collections.synchronizedSet(new HashSet<CustomerDemographicValue>());
		PreparedStatement ps = null;
		Map<Integer, Set<CustomerDemographicValue>> myMap = Collections.synchronizedMap(new HashMap<Integer, Set<CustomerDemographicValue>>());
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(Constants.customerDemographicsqlWithLevel);
			ps.setInt(1, this.customerId);
			ps.setString(2, Constants.ACCOMMO_LABEL);
			rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDemographicValue cdv = new CustomerDemographicValue();
				
				Integer custDemoId = rs.getInt(1);
				
				cdv.setValueName(rs.getString(4));
				cdv.setValueCode(rs.getString(5));
				
				
				if(!myMap.containsKey(custDemoId)){
					customerDemographicValue.add(cdv);
					myMap.put(custDemoId, customerDemographicValue);
				}else{
					myMap.get(custDemoId).add(cdv);
				}
			}
		} finally {
			SqlUtil.close(ps, rs);

		}
		
		Iterator<Integer> it = myMap.keySet().iterator();
		while(it.hasNext()){
			Integer customerDemoId = it.next();
			CustomerDemographic cd = new CustomerDemographic();
			cd.setCustomerDemographicId(customerDemoId);
			cd.setCustomerId(this.customerId);
			cd.setLabelName(Constants.ACCOMMO_LABEL);
			cd.setCustomerDemographicValue(myMap.get(customerDemoId));
			mySet.add(cd);
		}

		return mySet;

	}

	/**
	 * Set student accommodations 
	 * @param sd
	 * @param cd
	 * @param customerDemographic
	 * @param tfil
	 * @return accommodation object for student
	 */
	public Accomodations createAccomodations(Set<StudentDemographic> sd,
			Set<CustomerDemographic> cd,
			Map<Integer, String> customerDemographic, Tfil tfil) {

		Map<String, StudentDemographic> set1 = Collections.synchronizedMap(new HashMap<String, StudentDemographic>());
		Map<String, CustomerDemographic> set2 = Collections.synchronizedMap(new TreeMap<String, CustomerDemographic>());
		Map<Integer, String> studentDemographic = Collections.synchronizedMap(new HashMap<Integer, String>());
		Accomodations accomodations = new Accomodations();
		SpecialCodes specialCodes = new SpecialCodes();
		for (StudentDemographic studentDem : sd) {
			if (studentDem.getValue() != null){
				set1.put(studentDem.getValue(), studentDem);
			}
			studentDemographic.put(studentDem.getCustomerDemographicId(),
					studentDem.getValueName());
			if (customerDemographic.containsKey(studentDem.getCustomerDemographicId())) {
				String customerDemoName = customerDemographic.get(studentDem
						.getCustomerDemographicId());

				if (customerDemoName.equalsIgnoreCase("Ethnicity")) {
					setEthnicity(sd, tfil, studentDem.getCustomerDemographicId());
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
			//System.out.println("entry.getKey()>>"+entry.getKey().trim());
			for (Map.Entry<String, CustomerDemographic> entry1 : set2
					.entrySet()) {
				Set<CustomerDemographicValue> set = entry1.getValue()
				.getCustomerDemographicValue();
				//System.out.println("entry1.getKey()>>"+entry1.getKey());
				for (CustomerDemographicValue value : set) {
					//System.out.println("value.getValueCode()>>"+value.getValueCode());
					if (value.getValueCode().trim().equalsIgnoreCase(
							entry.getKey().trim())) {
						String string = value.getValueCode().replace('-', '_');
						//System.out.println("match found for :: set"+string);
						try {
							accomodations.getClass().getMethod("set" + string,
									String.class).invoke(accomodations,
									"1");
							break;
						} catch (Exception e) {
							logger.error(e.getMessage());
							e.printStackTrace();
						}
					}
				}

			}

		}

		return accomodations;

	}

	/**
	 * Set student details
	 * @param tfil
	 * @param st
	 * @param rosterId
	 */
	public void setStudentList(Tfil tfil, Student st, Integer rosterId) {
		tfil.setStudentElementNumber(studentMap.get(rosterId).toString());
		tfil.setStudentLastName(st.getLastName());
		tfil.setStudentFirstName(st.getFirstName());
		tfil.setStudentGender(st.getGender());
		tfil.setStudentMiddleName(st.getMiddleName());

		tfil.setExtStudentId(st.getExtStudentId());
		tfil.setStudentGradeFromAnswerSheets(EmetricUtil.formatGrade(st
				.getGrade()));
		tfil.setGrade(EmetricUtil.formatGrade(st.getGrade()));
		tfil.setStudentBirthDate(st.getBirthDate());

		tfil.setStudentChronologicalAge(EmetricUtil.calculateChronologicalAge(st.getBirthDate()));

		tfil.setPurposeOfTestp(st.getTestPurpose());

	}

	private void setEthnicity(Set<StudentDemographic> sd, Tfil tfil, Integer customerDemographicId) {

		for (StudentDemographic studentDemo : sd) {
			if(studentDemo.getCustomerDemographicId()==customerDemographicId) {
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
				} else if (studentDemo.getValueName().startsWith("mexicano-americano")) {
					tfil.setEthinicity("2");
				} else if (studentDemo.getValueName().startsWith("mexicano")) {
					tfil.setEthinicity("1");
				} else if (studentDemo.getValueName().startsWith("cubano-americano")) {
					tfil.setEthinicity("4");
				} else if (studentDemo.getValueName().startsWith("cubano")) {
					tfil.setEthinicity("3");
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

	/**
	 * Set organization hierarchy details
	 * @param tfil
	 * @param roster
	 * @param orderFile
	 */
	public synchronized void createOrganization(Tfil tfil, TestRoster roster, OrderFile orderFile) {

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
				tfil.setElementALabel(org.getOrgCategoryName());
				Integer integer = districtMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++districtElementNumber;
					districtMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberA(String.valueOf(districtMap.get(org.getOrgNodeMdrNumber())));
				if (org.getOrgNodeCode() != null)
					tfil.setElementSpecialCodesA(org.getOrgNodeCode());
				//tfil.setOrganizationId("XX" + rs.getString(1));
				tfil.setCustomerId(org.getOrgNodeMdrNumber());
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
				tfil.setElementBLabel(org.getOrgCategoryName());
				Integer integer = schoolMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++schoolElementNumber;
					schoolMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberB(String.valueOf(schoolMap.get(org.getOrgNodeMdrNumber())));
				if (org.getOrgNodeCode() != null)
					tfil.setElementSpecialCodesB(org.getOrgNodeCode());
				tfil.setElementStructureLevelB("02");
				tfil.setSchoolId(org.getOrgNodeMdrNumber());
			}

			else if (org.getCategoryLevel() != null
					&& new Integer(organizationMapSize - 4).toString() != null
					&& org.getCategoryLevel()
					.equalsIgnoreCase(
							new Integer(organizationMapSize - 4)
							.toString())) {
				tfil.setElementNameC(org.getOrgNodeName());
				tfil.setElementCLabel(org.getOrgCategoryName());
				Integer integer = classMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++classElementNumber;
					classMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberC(String.valueOf(classMap.get(org.getOrgNodeMdrNumber())));
				if (org.getOrgNodeCode() != null)
					tfil.setElementSpecialCodesC(org.getOrgNodeCode());
				tfil.setElementStructureLevelC("03");
				tfil.setClassId(org.getOrgNodeMdrNumber());

			}
			else if (org.getCategoryLevel() != null
					&& new Integer(organizationMapSize - 3).toString() != null
					&& org.getCategoryLevel()
					.equalsIgnoreCase(
							new Integer(organizationMapSize - 3)
							.toString())) {
				tfil.setElementNameD(org.getOrgNodeName());
				tfil.setElementDLabel(org.getOrgCategoryName());
				Integer integer = sectionMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++sectionElementNumber;
					sectionMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberD(String.valueOf(sectionMap.get(org.getOrgNodeMdrNumber())));
				if (org.getOrgNodeCode() != null)
					tfil.setElementSpecialCodesD(org.getOrgNodeCode());
				tfil.setElementStructureLevelD("04");
				tfil.setSectionId(org.getOrgNodeMdrNumber());

			}
			else if (org.getCategoryLevel() != null
					&& new Integer(organizationMapSize - 2).toString() != null
					&& org.getCategoryLevel()
					.equalsIgnoreCase(
							new Integer(organizationMapSize - 2)
							.toString())) {
				tfil.setElementNameE(org.getOrgNodeName());
				tfil.setElementELabel(org.getOrgCategoryName());
				Integer integer = groupMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++groupElementNumber;
					groupMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberE(String.valueOf(groupMap.get(org.getOrgNodeMdrNumber())));
				if (org.getOrgNodeCode() != null)
					tfil.setElementSpecialCodesE(org.getOrgNodeCode());
				tfil.setElementStructureLevelE("05");
				tfil.setGroupId(org.getOrgNodeMdrNumber());

			}
			else if (org.getCategoryLevel() != null
					&& new Integer(organizationMapSize - 1).toString() != null
					&& org.getCategoryLevel().equalsIgnoreCase(
							new Integer(organizationMapSize - 1)
							.toString())) {
				tfil.setElementNameF(org.getOrgNodeName());
				tfil.setElementFLabel(org.getOrgCategoryName());
				Integer integer = divisionMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++divisionElementNumber;
					divisionMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberF(String.valueOf(divisionMap.get(org.getOrgNodeMdrNumber())));
				if (org.getOrgNodeCode() != null)
					tfil.setElementSpecialCodesF(org.getOrgNodeCode());
				tfil.setElementStructureLevelF("06");
				tfil.setDivisionId(org.getOrgNodeMdrNumber());

			}
			else if (org.getCategoryLevel() != null
					&& org.getCategoryLevel().equalsIgnoreCase(
							organizationMapSize.toString())) {
				tfil.setElementNameG(org.getOrgNodeName());
				tfil.setElementGLabel(org.getOrgCategoryName());
				Integer integer = levelMap.get(org.getOrgNodeMdrNumber());
				if (integer == null) {
					integer = ++levelElementNumber;
					levelMap.put(org.getOrgNodeMdrNumber(), integer);

				}
				tfil.setElementNumberG(String.valueOf(levelMap.get(org.getOrgNodeMdrNumber())));
				if (org.getOrgNodeCode() != null)
					tfil.setElementSpecialCodesG(org.getOrgNodeCode());
				tfil.setElementStructureLevelG("07");
				tfil.setLeafLevelId(org.getOrgNodeMdrNumber());
			}
			}

			// System.out.println("createOrganization");
//		} finally {
//			SqlUtil.close(ps, rs);
//		}

		// For defect Fix 66410
		/*if (tfil.getElementNameB() == null) {
			tfil.setElementNameB(tfil.getElementNameC());
			tfil.setElementNumberB(tfil.getElementNumberC());
			tfil.setElementSpecialCodesB(tfil.getElementSpecialCodesC());
			tfil.setElementStructureLevelB("02");
			//tfil.setSchoolId(tfil.getClassId());

			tfil.setElementNameA(tfil.getElementNameC());
			tfil.setElementNumberA(tfil.getElementNumberC());
			tfil.setElementSpecialCodesA(tfil.getElementSpecialCodesC());
			tfil.setOrganizationId("XX" + tfil.getClassId());
			//tfil.setCustomerId(tfil.getClassId());
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
			//tfil.setOrganizationId("XX" + tfil.getLeafLevelId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getLeafLevelId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameG(), 30));
			
		}else if (tfil.getElementNameE() == null) {
			//tfil.setOrganizationId("XX" + tfil.getDivisionId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getDivisionId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameF(), 30));
			
		}else if (tfil.getElementNameD() == null) {
			//tfil.setOrganizationId("XX" + tfil.getGroupId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getGroupId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameE(), 30));
			
		}else if (tfil.getElementNameC() == null) {
			//tfil.setOrganizationId("XX" + tfil.getSectionId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getSectionId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameD(), 30));
			
		}else if (tfil.getElementNameB() == null) {
			//tfil.setOrganizationId("XX" + tfil.getClassId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getClassId());
			if (orderFile.getCustomerName() == null)
				orderFile.setCustomerName(EmetricUtil.truncate(tfil
						.getElementNameC(), 30));
			
		}else if (tfil.getElementNameA() == null) {
			//tfil.setOrganizationId("XX" + tfil.getSchoolId());
			if (orderFile.getCustomerId() == null)
				orderFile.setCustomerId(tfil.getSchoolId());
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
				tfil.setElementALabel(tfil.getElementBLabel());
				tfil.setElementNumberA(tfil.getElementNumberB());
				tfil.setElementSpecialCodesA(tfil.getElementSpecialCodesB());
				//tfil.setOrganizationId("XX" + tfil.getSchoolId());
				tfil.setCustomerId(tfil.getSchoolId());
				tfil.setElementStructureLevelA("01");
				//orderFile.setCustomerId(tfil.getSchoolId());
			}
			if (tfil.getElementNameC() != null){
				tfil.setElementNameB(tfil.getElementNameC());
				tfil.setElementBLabel(tfil.getElementCLabel());
				tfil.setElementNumberB(tfil.getElementNumberC());
				tfil.setElementSpecialCodesB(tfil.getElementSpecialCodesC());
				tfil.setSchoolId(tfil.getClassId());
				//tfil.setCustomerId(tfil.getClassId());
				tfil.setElementStructureLevelB("02");
				//orderFile.setCustomerId(tfil.getClassId());
			}
			if (tfil.getElementNameD() != null){
				tfil.setElementNameC(tfil.getElementNameD());
				tfil.setElementCLabel(tfil.getElementDLabel());
				tfil.setElementNumberC(tfil.getElementNumberD());
				tfil.setElementSpecialCodesC(tfil.getElementSpecialCodesD());
				tfil.setClassId(tfil.getSectionId());
				//tfil.setCustomerId(tfil.getSectionId());
				tfil.setElementStructureLevelC("03");
				//orderFile.setCustomerId(tfil.getSectionId());
			}
			if (tfil.getElementNameE() != null){
				tfil.setElementNameD(tfil.getElementNameE());
				tfil.setElementDLabel(tfil.getElementELabel());
				tfil.setElementNumberD(tfil.getElementNumberE());
				tfil.setElementSpecialCodesD(tfil.getElementSpecialCodesE());
				tfil.setSectionId(tfil.getGroupId());
				//tfil.setCustomerId(tfil.getGroupId());
				tfil.setElementStructureLevelD("04");
				//orderFile.setCustomerId(tfil.getGroupId());
			}
			if (tfil.getElementNameF() != null){
				tfil.setElementNameE(tfil.getElementNameF());
				tfil.setElementELabel(tfil.getElementFLabel());
				tfil.setElementNumberE(tfil.getElementNumberF());
				tfil.setElementSpecialCodesE(tfil.getElementSpecialCodesF());
				tfil.setGroupId(tfil.getDivisionId());
				//tfil.setCustomerId(tfil.getDivisionId());
				tfil.setElementStructureLevelE("05");
				//orderFile.setCustomerId(tfil.getDivisionId());
			}
			if (tfil.getElementNameG() != null){
				tfil.setElementNameF(tfil.getElementNameG());
				tfil.setElementFLabel(tfil.getElementGLabel());
				tfil.setElementNumberF(tfil.getElementNumberG());
				tfil.setElementSpecialCodesF(tfil.getElementSpecialCodesG());
				tfil.setDivisionId(tfil.getLeafLevelId());
				//tfil.setCustomerId(tfil.getLeafLevelId());
				tfil.setElementStructureLevelF("06");
				//orderFile.setCustomerId(tfil.getLeafLevelId());
				
				/* push the dummy class node at leaf level... */
				tfil.setElementNameG(isClassNodeRequiredDummyName);
				tfil.setElementGLabel("Class");
				//tfil.setElementNumberG(tfil.getElementNumberG());
				tfil.setElementNumberG("");// blank for now
				tfil.setElementSpecialCodesG("");
				tfil.setLeafLevelId("");//blank for now
				//tfil.setCustomerId(tfil.getLeafLevelId());
				tfil.setElementStructureLevelG("07");
				//orderFile.setCustomerId(tfil.getLeafLevelId());
			}
		}
		//end ::

	}

	/**
	 * Set sub test status details for student
	 * @param tfil
	 * @param roster
	 * @param orderFile
	 */
	public void createTestSessionDetails(Tfil tfil,
			TestRoster roster, OrderFile orderFile){

		TestAdmin test = roster.getTest_admin();

		if (test.getPreferedForm().equalsIgnoreCase("A")) {
			tfil.setTestName("LAS LINKS OPERATIONAL");
			tfil.setTestForm("A");
			if (orderFile.getTestName1() == null)
				orderFile.setTestName1(EmetricUtil.truncate(
						"LAS Links", new Integer(10))
						.toUpperCase());
		} else if (test.getPreferedForm().equalsIgnoreCase("B")) {
			tfil.setTestName("LAS LINKS OPERATIONAL");
			tfil.setTestForm("B");
			if (orderFile.getTestName1() == null)
				orderFile.setTestName1(EmetricUtil.truncate(
						"LAS Links", new Integer(10))
						.toUpperCase());
		} else if (test.getPreferedForm().equalsIgnoreCase("Espanol")) {
			tfil.setTestName("LAS LINKS OPERATIONAL");
			tfil.setTestForm("S");
			if (orderFile.getTestName1() == null)
				orderFile.setTestName1(EmetricUtil.truncate(
						"ESPANOL", new Integer(10))
						.toUpperCase());
		} else if (test.getPreferedForm().startsWith("Esp")) {
			tfil.setTestName("LAS LINKS OPERATIONAL");
			tfil.setTestForm("S");
			if (orderFile.getTestName1() == null)
				orderFile.setTestName1(EmetricUtil.truncate(
						"ESPANOL", new Integer(10))
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
				&& test.getTestLevel().equals("6-8")) {
			tfil.setTestLevel("4");
		} else if (test.getTestLevel() != null
				&& test.getTestLevel().equals("9-12")) {
			tfil.setTestLevel("5");
		}
		if(test.getTestDate() != null){
			tfil.setTestDate(EmetricUtil.getTimeZone(test.getTestDate(),test.getTimezone(),true));
		}
			CreateFile.testDate = tfil.getTestDate();
			
		if(test.getDateTestingCompleted() != null){
			tfil.setDateTestingCompleted(EmetricUtil.getTimeZone(test.getDateTestingCompleted(),test.getTimezone(),false));
		}
		
		if(test.getProgramStartDate() != null && CreateFile.programDate == null){
			CreateFile.programDate = EmetricUtil.getTimeZone(test.getProgramStartDate(),test.getTimezone(),true);
		}

		if (orderFile.getTestDate() == null && CreateFile.programDate != null){
			orderFile.setTestDate(CreateFile.programDate.substring(0, 6));
		}

	}

	public void createStudentItemStatusDetails(Tfil tfil,	TestRoster roster) {

		Set<StudentItemSetStatus> sissSet = roster.getStudentItemSetStatus();
		Iterator<StudentItemSetStatus> it = sissSet.iterator();
		while(it.hasNext()){
			StudentItemSetStatus siss = it.next();
			if (siss.getItemSetName().equalsIgnoreCase("Speaking")) {
				tfil.setTestInvalidationSpeaking(siss.getValidationStatus()
						.equalsIgnoreCase("IN") ? "1" : " ");
				tfil.setTestingExemptionsSpeaking(siss.getExemptions()
						.equalsIgnoreCase("Y") ? "1" : " ");
				tfil.setAbsentSpeaking(siss.getAbsent()
						.equalsIgnoreCase("Y") ? "1" : " ");
			} else if (siss.getItemSetName().equalsIgnoreCase(
			"Listening")) {
				tfil.setTestInvalidationListening(siss.getValidationStatus()
						.equalsIgnoreCase("IN") ? "1" : " ");
				tfil.setTestingExemptionsListening(siss.getExemptions()
						.equalsIgnoreCase("Y") ? "1" : " ");
				tfil.setAbsentListening(siss.getAbsent()
						.equalsIgnoreCase("Y") ? "1" : " ");
			} else if (siss.getItemSetName().equalsIgnoreCase(
			"Reading")) {
				tfil.setTestInvalidationReading(siss.getValidationStatus()
						.equalsIgnoreCase("IN") ? "1" : " ");
				tfil.setTestingExemptionsReading(siss.getExemptions()
						.equalsIgnoreCase("Y") ? "1" : " ");
				tfil.setAbsentReading(siss.getAbsent()
						.equalsIgnoreCase("Y") ? "1" : " ");
			} else if (siss.getItemSetName().equalsIgnoreCase(
			"Writing")) {
				tfil.setTestInvalidationWriting(siss.getValidationStatus()
						.equalsIgnoreCase("IN") ? "1" : " ");
				tfil.setTestingExemptionsWriting(siss.getExemptions()
						.equalsIgnoreCase("Y") ? "1" : " ");
				tfil.setAbsentWriting(siss.getAbsent()
						.equalsIgnoreCase("Y") ? "1" : " ");
			}
		}
	}

	/**
	 * Set Composite or Content Area details for student
	 * @param tfil
	 * @param roster
	 * @param invalidSubtestList
	 */
	public void createSkillAreaScoreInformation(Tfil tfil,
			TestRoster roster, List<String> invalidSubtestList) {
		Map<String, Object[]> treeMap = Collections.synchronizedMap(new TreeMap<String, Object[]>());
		Map<String,String> contentAreaFact = Collections.synchronizedMap(new HashMap<String, String>());
		boolean isComprehensionPopulated = true;
		boolean isOralPopulated = true;
		boolean isScaleOverall = true;
		boolean isProficiencyLevelOverall = true;
		
		Integer speaking = 0;
		Integer listening = 0;
		Integer reading = 0;
		Integer writing = 0;
		ProficiencyLevels pl = new ProficiencyLevels();
		ScaleScores ss = new ScaleScores();
		SkillAreaNumberCorrect po = new SkillAreaNumberCorrect();
		SkillAreaPercentCorrect pc = new SkillAreaPercentCorrect();
		String scaleScoreOverall = "";
		String profLevelScoreOverall = "";

		Set<SkillArea> skillAreaSet = roster.getSkillAreas();
		Iterator<SkillArea> it = skillAreaSet.iterator();
		while(it.hasNext()){
			SkillArea sArea = it.next();
			if (sArea.getName() != null){
				if("Overall".equalsIgnoreCase(sArea.getName())){
					scaleScoreOverall = sArea.getScaleScore();
					profLevelScoreOverall = sArea.getProficiencyLevel();
				}else{
					Object[] val = new Object[5];
					val[1] = sArea.getScaleScore();
					val[2] = sArea.getProficiencyLevel();
					val[3] = sArea.getPointsObtained();
					val[4] = sArea.getPercentObtained();

					String name = sArea.getName().toLowerCase();
					if (name.equalsIgnoreCase(
					"speaking")) {
						treeMap.put(name, val);
						contentAreaFact.put("speaking",name);
					} else if (name.equalsIgnoreCase("listening")) {
						treeMap.put(name, val);
						contentAreaFact.put("listening",name);
					} else if (name.equalsIgnoreCase("reading")) {
						treeMap.put(name, val);
						contentAreaFact.put("reading",name);
					} else if (name.equalsIgnoreCase("writing")) {
						treeMap.put(name, val);
						contentAreaFact.put("writing",name);
					} else if (name.equalsIgnoreCase("comprehension")) {
						treeMap.put(name, val);
						contentAreaFact.put("comprehension",name);
					} else if (name.equalsIgnoreCase("oral")) {
						treeMap.put(name, val);
						contentAreaFact.put("oral",name);
					}
				}
			}
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
			invalidSubtestList.add("READING");
			isProficiencyLevelOverall = false;
			isScaleOverall = false;
		}
		if (writing > 0) {
			po.setWriting("");
			pl.setWriting("");
			pc.setWriting("");
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

	/**
	 * Set Objective score details of student
	 * @param tfil
	 * @param roster
	 * @param invalidSubtestList
	 * @throws SQLException
	 */
	public void createSubSkillAreaScoreInformation(Tfil tfil, TestRoster roster, List<String> invalidSubtestList)
	throws SQLException {
		SubSkillNumberCorrect subNumCorrect = new SubSkillNumberCorrect();
		SubSkillPercentCorrect subPercCorrect = new SubSkillPercentCorrect();
		String subSkillName;
		Map<String, String> pointsObtained = Collections.synchronizedMap(new HashMap<String, String>());
		Map<String, String> percentObtained = Collections.synchronizedMap(new HashMap<String, String>());
		Map<String, String> subSkillAreaScoreInfo = Collections.synchronizedMap(new HashMap<String, String>());
		Map<String, String> subSkillAreaItemCategory = Collections.synchronizedMap(new HashMap<String, String>());
	
		Set<SubSkillArea> subSkillAreas = roster.getSubSkillAreas();
		Iterator<SubSkillArea> it = subSkillAreas.iterator();
		while(it.hasNext()){
			SubSkillArea sa = it.next();
			subSkillAreaScoreInfo.put(sa.getSubSkillId(), sa.getSubSkillName());
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
			
			
			if (subSkillName.equalsIgnoreCase("Speak in Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setSpeakInWords(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Speak in Sentences")  && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setSpeakSentences(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Make Conversation") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setMakeConversations(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Tell a Story") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setTellAStory(percentObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Listen for Information")&& fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
				subPercCorrect.setListenForInformation(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Listen in the Classroom") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setListenInTheClassroom(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Listen and Comprehend") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setListenAndComprehend(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Analyze Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setAnalyzeWords(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Read Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setReadWords(percentObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Read for Understanding")&& fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList)) {
				subPercCorrect.setReadForUnderStanding(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Use Conventions") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setUseConventions(percentObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Write About") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setWriteAbout(percentObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Write Why") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setWriteWhy(percentObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Write in Detail") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subPercCorrect.setWriteInDetail(percentObtained.get(x)
						.toString());
			}
		}

		tfil.setSubSkillPercentCorrect(subPercCorrect);

		for (String x : pointsObtained.keySet()) {
			subSkillName = subSkillAreaScoreInfo.get(x);

			if (subSkillName.equalsIgnoreCase("Speak in Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setSpeakInWords(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Speak in Sentences") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setSpeakSentences(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Make Conversation") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setMakeConversations(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Tell a Story") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setTellAStory(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Listen for Information") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setListenForInformation(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Listen in the Classroom") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setListenInTheClassroom(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Listen and Comprehend") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setListenAndComprehend(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Analyze Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setAnalyzeWords(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Read Words") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setReadWords(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Read for Understanding") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setReadForUnderStanding(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Use Conventions") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setUseConventions(pointsObtained.get(x)
						.toString());
			} else if (subSkillName.equalsIgnoreCase("Write About") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setWriteAbout(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Write Why") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect.setWriteWhy(pointsObtained.get(x).toString());
			} else if (subSkillName.equalsIgnoreCase("Write in Detail") && fillValidSubSkillScore(subSkillName,subSkillAreaItemCategory, invalidSubtestList) ) {
				subNumCorrect
				.setWriteInDetail(pointsObtained.get(x).toString());
			}
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
	 * Prepare order file in temporary local directory and transfer file in a specified location
	 * @param orderFile
	 * @param filedir
	 * @param fileName
	 * @throws IOException
	 */
	private void prepareOrderFile(OrderFile orderFile, String filedir,
			String fileName) throws IOException {

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
	 * validating Rosters hand scoring status whether hand scoring is completed
	 * or not
	 * 
	 * @param string
	 */
	private int validateRosterHandScoringStatus(Connection oascon,
			Connection irscon, SimpleCache cache, String rosterIds)
			throws Exception {

		StringBuilder rosterBuilder = new StringBuilder();
		Set<Integer> rosterIdSet = new HashSet<Integer>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String[] str = rosterIds.split("#");
			for (int i = 0; i < str.length; i++) {
				String query = Constants.VALIDATE_SCORING_STATUS.replaceAll(
						"#IDS#", str[i].substring(0, str[i].lastIndexOf(",")));
				ps = oascon.prepareStatement(query);
				rs = ps.executeQuery();
				rs.setFetchSize(100);
				while (rs.next()) {
					Integer testRosterId = rs.getInt(1);
					cache.removeRoster(testRosterId);
					rosterIdSet.add(testRosterId);
					rosterBuilder.append(testRosterId).append("|");
				}
				SqlUtil.close(ps, rs);
			}
			CreateFile.unexportedRosters = rosterBuilder.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(
					"SQLException at validateRosterHandScoringStatus:"
							+ e.getMessage());
		} finally {
			SqlUtil.close(ps, rs);
		}
		return rosterIdSet.size();
	}

	/**
	 * Mark the student exported flag as true
	 * 
	 * @throws Exception
	 */
	private void markExportedFlagAsTrue() throws Exception {
		Connection oascon = null;
		PreparedStatement ps = null;
		try {
			oascon = SqlUtil.openOASDBconnectionForResearch();
			String[] str = CreateFile.exportedRosters.split("#");
			for (int i = 0; i < str.length; i++) {
				String query = Constants.MARK_EXPORTED_STATUS.replace("#ROS#",
						str[i]);
				ps = oascon.prepareStatement(query);
				ps.executeUpdate();
				SqlUtil.close(ps);
			}
			oascon.commit();
		} catch (SQLException e) {
			oascon.rollback();
			e.printStackTrace();
			throw new Exception("SQLException at markExportedFlagAsTrue:"
					+ e.getMessage());
		} finally {
			SqlUtil.close(oascon);
		}

	}

}
