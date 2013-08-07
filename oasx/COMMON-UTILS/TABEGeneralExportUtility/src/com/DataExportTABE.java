package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.ffpojo.exception.FFPojoException;

import au.com.bytecode.opencsv.CSVWriter;

import com.ctb.dto.AbilityScore;
import com.ctb.dto.CustomerDemographic;
import com.ctb.dto.CustomerDemographicValue;
import com.ctb.dto.GradeEquivalent;
import com.ctb.dto.ItemResponses;
import com.ctb.dto.NRSLevels;
import com.ctb.dto.ObjectiveLevel;
import com.ctb.dto.Organization;
import com.ctb.dto.PercentageMastery;
import com.ctb.dto.PredictedGED;
import com.ctb.dto.Scores;
import com.ctb.dto.Student;
import com.ctb.dto.StudentAccommodations;
import com.ctb.dto.StudentDemographic;
import com.ctb.dto.TABEFile;
import com.ctb.dto.TestRoster;
import com.ctb.utils.ExtractUtil;
import com.ctb.utils.SQLQuery;
import com.ctb.utils.SqlUtil;
import com.ctb.utils.Utility;

public class DataExportTABE {
	
	private static String CUSTOMER_ID ;
	private static String PRODUCT_ID ;
	private static final String FILE_TYPE = ExtractUtil.getDetail("oas.exportdata.fileType");
	private static String FILE_NAME = ExtractUtil.getDetail("oas.exportdata.genfileName");
	private static final String LOCAL_FILE_PATH = ExtractUtil.getDetail("oas.exportdata.filepath");
	private static final Map<String, String> OBJECTIVE_MAP = new LinkedHashMap<String, String>();
	private static final Map<String, Integer> CONTENT_DOMAINS = new LinkedHashMap<String, Integer>();
	private static final Map<String, Integer> CONTENT_DOMAINS_TB = new LinkedHashMap<String, Integer>();
	private static final Set<Integer> CUST_CATEGORIES = new TreeSet<Integer>();
	private static final String SEPARATOR = "#";
	private static final String SPACE = " ";
	private static final String PIPE = "|";
	private static final Map<String, Integer> ITEM_COUNT_MAP = new LinkedHashMap<String, Integer>();
	private static final Map<String, Integer> ITEM_COUNT_MAP_TA = new LinkedHashMap<String, Integer>();
	private static final Map<String, Integer> ITEM_COUNT_MAP_TB = new LinkedHashMap<String, Integer>();
	private static final Map<String, Integer> ITEM_COUNT_MAP_TS = new LinkedHashMap<String, Integer>();
	private static final Map<String, Integer> ITEM_COUNT_MAP_TL = new LinkedHashMap<String, Integer>();
	private static final Map<String, Integer> SCORE_TYPES = new LinkedHashMap<String, Integer>();
	private static String PRODUCT_TYPE = "";
	private static String [] productIds = null;
	static String env = "";
	static Properties properties = null;
	
	static {
		CONTENT_DOMAINS_TB.put("Reading", 0);
		CONTENT_DOMAINS_TB.put("Mathematics Computation", 0);
		CONTENT_DOMAINS_TB.put("Applied Mathematics", 0);
		CONTENT_DOMAINS_TB.put("Language", 0);
		CONTENT_DOMAINS_TB.put("Vocabulary", 0);
		CONTENT_DOMAINS_TB.put("Language Mechanics", 0);
		CONTENT_DOMAINS_TB.put("Spelling", 0);
		
		SCORE_TYPES.put("Objective Raw Score", 0);
		SCORE_TYPES.put("Objective Total Raw Score", 0);
		SCORE_TYPES.put("Objective Scale Score", 0);
		SCORE_TYPES.put("Objective Scale Score SEM", 0);
		SCORE_TYPES.put("Objective Mastery Level", 0);
		SCORE_TYPES.put("Objective Mastery", 0);
		
		ITEM_COUNT_MAP_TA.put("Reading", 29);
		ITEM_COUNT_MAP_TA.put("Mathematics Computation", 24);
		ITEM_COUNT_MAP_TA.put("Applied Mathematics", 29);
		ITEM_COUNT_MAP_TA.put("Language", 29);
		
		ITEM_COUNT_MAP_TB.put("Reading", 50);
		ITEM_COUNT_MAP_TB.put("Mathematics Computation", 40);
		ITEM_COUNT_MAP_TB.put("Applied Mathematics", 55);
		ITEM_COUNT_MAP_TB.put("Language", 55);
		ITEM_COUNT_MAP_TB.put("Vocabulary", 20);
		ITEM_COUNT_MAP_TB.put("Language Mechanics", 20);
		ITEM_COUNT_MAP_TB.put("Spelling", 20);
		
		ITEM_COUNT_MAP_TS.put("Reading", 25);
		ITEM_COUNT_MAP_TS.put("Mathematics Computation", 25);
		ITEM_COUNT_MAP_TS.put("Applied Mathematics", 25);
		ITEM_COUNT_MAP_TS.put("Language", 25);
		ITEM_COUNT_MAP_TS.put("Vocabulary", 20);
		ITEM_COUNT_MAP_TS.put("Language Mechanics", 20);
		ITEM_COUNT_MAP_TS.put("Spelling", 20);
		
		ITEM_COUNT_MAP_TL.put("Reading", 12);
		ITEM_COUNT_MAP_TL.put("Mathematics Computation", 8);
		ITEM_COUNT_MAP_TL.put("Applied Mathematics", 8);
		ITEM_COUNT_MAP_TL.put("Language", 12);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataExportTABE dataExport = new DataExportTABE();
		try {
			boolean customerIdProvided = false;
			boolean productIdProvided = false;
			dataExport.getCommandLine(args);
			if(!"".equals(env)) {
				properties = dataExport.loadProperties(env);
				if(dataExport.getPropertyValue("oas.customerId") != null
						&& !"".equals(dataExport.getPropertyValue("oas.customerId")) 
						&& dataExport.getPropertyValue("oas.customerId").length() > 0) {
					CUSTOMER_ID = dataExport.getPropertyValue("oas.customerId");
					customerIdProvided = true;
				}
				else {
					System.out.println("Customer ID is not specified in Resource Bundle named config.properties");
				}
				if(dataExport.getPropertyValue("oas.productId") != null 
						&& !"".equals(dataExport.getPropertyValue("oas.productId")) 
						&& dataExport.getPropertyValue("oas.productId").length() > 0) {
					PRODUCT_ID = dataExport.getPropertyValue("oas.productId");
					productIdProvided = true;
					productIds = PRODUCT_ID.split(",");
					for(int i=0 ; i<productIds.length ; i++) {
						if(productIds[i].equals("4013")) {
							System.out.println("Survey Product Id can not be Input to the program, So stopping the execution");
							productIdProvided = false;
						}
						/*else if(productIds[i].equals("4008")) {
							System.out.println("Locator Product Id can not be Input to the program, So stopping the execution");
							productIdProvided = false;
						}*/
					}
				}
				else {
					System.out.println("Product ID is not specified in Resource Bundle named config.properties");
				}
				if(customerIdProvided && productIdProvided) {
					dataExport.writeToText();
					//System.out.println("Customer ID is : " + CUSTOMER_ID );
					//System.out.println("Product ID is : " + PRODUCT_ID );
				}
				else {
					System.out.println("As mandatory inputs Customer_ID/Product_ID are missing" +
							" or wrong type of ProductIDs are given in Resource Bundle so program is exiting..");
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeToText() throws IOException, FFPojoException, SQLException,Exception {

		List<TABEFile> myList = createList();
		String fileName = FILE_NAME + "_" + System.currentTimeMillis() + FILE_TYPE;
		if(!(new File(LOCAL_FILE_PATH)).exists()){
			File f = new File(LOCAL_FILE_PATH);
			f.mkdirs();
		}
		File file = new File(LOCAL_FILE_PATH, fileName);
		CSVWriter writer = new CSVWriter(new FileWriter(file));
		StringBuilder headerRow = new StringBuilder();		
		int i=0;
		StringBuilder contentDomains = new StringBuilder();
		for (String scoreType: SCORE_TYPES.keySet()) {
				contentDomains.append(scoreType).append("(Recall Information").append(SEPARATOR)
				.append("Construct Meaning in Context").append(SEPARATOR).append("Interpret, Evaluate, and Extend Meaning").append(SEPARATOR)
				.append("Whole Numbers").append(SEPARATOR).append("Decimals, Fractions, Percents").append(SEPARATOR)				
				.append("Integers").append(SEPARATOR).append("Algebraic Operations").append(SEPARATOR)
				.append("Number and Number Operations").append(SEPARATOR).append("Computation and Estimation").append(SEPARATOR)
				.append("Measurement").append(SEPARATOR).append("Geometry and Spatial Sense").append(SEPARATOR)
				.append("Statistics and Probability").append(SEPARATOR).append("Patterns, Functions, Algebra").append(SEPARATOR)
				.append("Problem Solving and Reasoning").append(SEPARATOR).append("Usage").append(SEPARATOR)
				.append("Sentence and Paragraph Development").append(SEPARATOR).append("Writing Mechanics and Conventions").append(SEPARATOR)
				.append("Add Whole Numbers").append(SEPARATOR).append("Capitalization").append(SEPARATOR)
				.append("Computation in Context").append(SEPARATOR).append("Consonant").append(SEPARATOR)
				.append("Construct Meaning").append(SEPARATOR).append("Data Analysis").append(SEPARATOR)
				.append("Decimals").append(SEPARATOR).append("Divide Whole Numbers").append(SEPARATOR)
				.append("Estimation").append(SEPARATOR).append("Evaluate/Extend Meaning").append(SEPARATOR)
				.append("Fractions").append(SEPARATOR).append("Interpret Graphic Information").append(SEPARATOR)
				.append("Multimeaning Words").append(SEPARATOR).append("Multiply Whole Numbers").append(SEPARATOR)
				.append("Order of Operations").append(SEPARATOR).append("Paragraph Development").append(SEPARATOR)
				.append("Percents").append(SEPARATOR).append("Pre-Reading Skills").append(SEPARATOR)
				.append("Punctuation").append(SEPARATOR).append("Sentence Formation").append(SEPARATOR)
				.append("Sentences, Phrases, Clauses").append(SEPARATOR).append("Structural Unit").append(SEPARATOR)
				.append("Subtract Whole Numbers").append(SEPARATOR).append("Vowel").append(SEPARATOR)
				.append("Word Meaning").append(SEPARATOR).append("Words in Context").append(SEPARATOR)
				.append("Writing Conventions)").append(SEPARATOR);
		}
		int j=0;
		for(TABEFile tabe: myList) {
			if("TB".equals(tabe.getProductType())) {
				if(CONTENT_DOMAINS.size() != CONTENT_DOMAINS_TB.size()) {
					CONTENT_DOMAINS.put("Vocabulary", 0);
					CONTENT_DOMAINS.put("Language Mechanics", 0);
					CONTENT_DOMAINS.put("Spelling", 0);
				}
				break;
			}
		}
		for(String contentDomainName : CONTENT_DOMAINS.keySet()) {
			//contentDomains.append(contentDomainName).append(SEPARATOR);
			contentDomains.append("SubTest"+(++j)).append(SEPARATOR);
			contentDomains.append("Level, Raw Score, Scale Score").append(SEPARATOR);
			contentDomains.append("Item Response Data").append(SEPARATOR);
			//Integer itemCount = ITEM_COUNT_MAP.get(contentDomainName);
			Integer itemCount = ITEM_COUNT_MAP_TB.get(contentDomainName);
			if(itemCount != null) {
				i=0;
				for(i=0 ; i < itemCount-1 ; i++) {
					contentDomains.append(SPACE).append(SEPARATOR);
				}
				contentDomains.append("Restart Flag").append(SEPARATOR).append("Restart Item Number").append(SEPARATOR)
				.append("Timed Out").append(SEPARATOR).append("Stopped Item Number").append(SEPARATOR);			
			}
		}			
				
		headerRow.append("Product Type").append(SEPARATOR).append("Customer Id").append(SEPARATOR)
		.append("State Name").append(SEPARATOR).append("State Code").append(SEPARATOR)
		.append("District Name").append(SEPARATOR).append("District Code").append(SEPARATOR)
		.append("School Name").append(SEPARATOR).append("School Code").append(SEPARATOR)
		.append("Class Name").append(SEPARATOR).append("Class Code").append(SEPARATOR)
		.append("OAS Roster Id").append(SEPARATOR).append("OAS Student Id").append(SEPARATOR)
		.append("Student Id").append(SEPARATOR).append("Student Id 2").append(SEPARATOR)
		.append("First Name").append(SEPARATOR).append("Middle Name").append(SEPARATOR)
		.append("Last Name").append(SEPARATOR).append("Grade").append(SEPARATOR)
		.append("Birth Date").append(SEPARATOR).append("Gender").append(SEPARATOR)
		.append("Ethnicity").append(SEPARATOR).append("ELL").append(SEPARATOR)
		.append("Free Lunch").append(SEPARATOR).append("IEP").append(SEPARATOR)
		.append("LEP").append(SEPARATOR).append("Labor Force").append(SEPARATOR)
		.append("Migrant").append(SEPARATOR).append("Section 504").append(SEPARATOR)
		.append("Screen Magnifier").append(SEPARATOR).append("Screen Reader").append(SEPARATOR)
		.append("Calculator").append(SEPARATOR).append("Test Pause").append(SEPARATOR)
		.append("Untimed Test").append(SEPARATOR).append("Question Background Color").append(SEPARATOR)
		.append("Question Font Color").append(SEPARATOR).append("Question Font Size").append(SEPARATOR)
		.append("Answer Background Color").append(SEPARATOR).append("Answer Font Color").append(SEPARATOR)
		.append("Answer Font Size").append(SEPARATOR).append("Highlighter").append(SEPARATOR)
		.append("Music File Name").append(SEPARATOR).append("Masking Ruler").append(SEPARATOR)
		.append("Magnifying Glass").append(SEPARATOR).append("Extended Time").append(SEPARATOR)
		.append("Masking Tool").append(SEPARATOR).append("Microphone Headphone").append(SEPARATOR)
		.append("Testing Site").append(SEPARATOR).append("Date Testing Complete").append(SEPARATOR)
		.append("Interrupted").append(SEPARATOR).append("Test Form Id").append(SEPARATOR)
		.append("Last Item").append(SEPARATOR).append("Timed Out").append(SEPARATOR)
		.append("Reading - Ability Score").append(SEPARATOR)
		.append("Reading - SEM Score").append(SEPARATOR).append("Math Comp - Ability Score").append(SEPARATOR)
		.append("Math Comp - SEM Score").append(SEPARATOR).append("Applied Math - Ability Score").append(SEPARATOR)
		.append("Applied Math - SEM Score").append(SEPARATOR).append("Language - Ability Score").append(SEPARATOR).append("Language - SEM Score").append(SEPARATOR)
		
		.append("Language Mechanics - Ability Score").append(SEPARATOR)//.append("Language Mechanics - SEM Score").append(SEPARATOR)
		.append("Vocabulary - Ability Score").append(SEPARATOR)//.append("Vocabulary - SEM Score").append(SEPARATOR)
		.append("Spelling - Ability Score").append(SEPARATOR)//.append("Spelling - SEM Score").append(SEPARATOR)
		.append("Total Math - Ability Score").append(SEPARATOR).append("Total Battery - Ability Score").append(SEPARATOR)
		
		.append("Grade Equivalent - Reading").append(SEPARATOR).append("Grade Equivalent - Math Comp").append(SEPARATOR)
		.append("Grade Equivalent - Applied Math").append(SEPARATOR).append("Grade Equivalent - Language").append(SEPARATOR)
		.append("Grade Equivalent - Language Mechanics").append(SEPARATOR).append("Grade Equivalent - Vocabulary").append(SEPARATOR)
		.append("Grade Equivalent - Spelling").append(SEPARATOR).append("Grade Equivalent - Total Math").append(SEPARATOR)
		.append("Grade Equivalent - Total Battery").append(SEPARATOR)
		
		.append("NRS Level - Reading").append(SEPARATOR).append("NRS Level - Math Comp").append(SEPARATOR)
		.append("NRS Level - Applied Math").append(SEPARATOR).append("NRS Level - Language").append(SEPARATOR)
		.append("NRS Level - Language Mechanics").append(SEPARATOR).append("NRS Level - Vocabulary").append(SEPARATOR)
		.append("NRS Level - Spelling").append(SEPARATOR).append("NRS Level - Total Math").append(SEPARATOR)
		.append("NRS Level - Total Battery").append(SEPARATOR)
		
		.append("Percentage Mastery - Reading").append(SEPARATOR).append("Percentage Mastery - Math Comp").append(SEPARATOR)
		.append("Percentage Mastery - Applied Math").append(SEPARATOR).append("Percentage Mastery - Language").append(SEPARATOR)
		.append("Percentage Mastery - Language Mechanics").append(SEPARATOR).append("Percentage Mastery - Vocabulary").append(SEPARATOR)
		.append("Percentage Mastery - Spelling").append(SEPARATOR)
		
		.append("Predicted GED - Average").append(SEPARATOR)
		.append("Predicted GED - Math").append(SEPARATOR).append("Predicted GED - Reading").append(SEPARATOR)
		.append("Predicted GED - Science").append(SEPARATOR).append("Predicted GED - Social Studies").append(SEPARATOR)
		.append("Predicted GED - Writing").append(SEPARATOR).append(contentDomains.substring(0, contentDomains.length() - 1));		
				
		writer.writeNext(headerRow.toString().split(SEPARATOR));
		try {
			StringBuilder row = new StringBuilder();
			for(TABEFile tabe: myList) {
				
				row = new StringBuilder();
				row.append(tabe.getProductType()).append(SEPARATOR).append(tabe.getCustomerID()).append(SEPARATOR)
				.append(tabe.getOrgLevel1Name()).append(SEPARATOR).append(tabe.getOrgLevel1Code()).append(SEPARATOR)
				.append(tabe.getOrgLevel2Name()).append(SEPARATOR).append(tabe.getOrgLevel2Code()).append(SEPARATOR)
				.append(tabe.getOrgLevel3Name()).append(SEPARATOR).append(tabe.getOrgLevel3Code()).append(SEPARATOR)
				.append(tabe.getOrgLevel4Name()).append(SEPARATOR).append(tabe.getOrgLevel4Code()).append(SEPARATOR)
				.append(tabe.getRosterId()).append(SEPARATOR).append(tabe.getStudentID()).append(SEPARATOR)
				.append(tabe.getExtStudentId()).append(SEPARATOR).append(tabe.getExtStudentId2()).append(SEPARATOR)
				.append(tabe.getStudentFirstName()).append(SEPARATOR).append(tabe.getStudentMiddleName()).append(SEPARATOR)
				.append(tabe.getStudentLastName()).append(SEPARATOR).append(tabe.getGrade()).append(SEPARATOR)
				.append(tabe.getStudentBirthDate()).append(SEPARATOR).append(tabe.getStudentGender()).append(SEPARATOR)
				.append(tabe.getEthnicity()).append(SEPARATOR).append(tabe.getEll()).append(SEPARATOR)
				.append(tabe.getFreeLunch()).append(SEPARATOR).append(tabe.getIep()).append(SEPARATOR)
				.append(tabe.getLep()).append(SEPARATOR).append(tabe.getLaborForceStatus()).append(SEPARATOR)
				.append(tabe.getMigrant()).append(SEPARATOR).append(tabe.getSection504()).append(SEPARATOR)
				.append(tabe.getScreenMagnifier()).append(SEPARATOR).append(tabe.getScreenReader()).append(SEPARATOR)
				.append(tabe.getCalculator()).append(SEPARATOR).append(tabe.getTestPause()).append(SEPARATOR)
				.append(tabe.getUntimedTest()).append(SEPARATOR).append(tabe.getQuestionBackgroundColor()).append(SEPARATOR)
				.append(tabe.getQuestionFontColor()).append(SEPARATOR).append(tabe.getQuestionFontSize()).append(SEPARATOR)
				.append(tabe.getAnswerBackgroundColor()).append(SEPARATOR).append(tabe.getAnswerFontColor()).append(SEPARATOR)
				.append(tabe.getAnswerFontSize()).append(SEPARATOR).append(tabe.getHighlighter()).append(SEPARATOR)
				.append(tabe.getMusicFileName()).append(SEPARATOR).append(tabe.getMaskingRuler()).append(SEPARATOR)
				.append(tabe.getMagnifyingGlass()).append(SEPARATOR).append(tabe.getExtendedTime()).append(SEPARATOR)
				.append(tabe.getMaskingTool()).append(SEPARATOR).append(tabe.getMicrophoneHeadphone()).append(SEPARATOR)
				.append(tabe.getTestingSite()).append(SEPARATOR).append(tabe.getDateTestingCompleted()).append(SEPARATOR)
				.append(tabe.getInterrupted()).append(SEPARATOR).append(tabe.getTestFormId()).append(SEPARATOR)
				.append(tabe.getLastItem()).append(SEPARATOR).append(tabe.getTimedOut()).append(SEPARATOR)
					.append(tabe.getAbilityScores().getReadingAbilityScore()).append(SEPARATOR).append(tabe.getAbilityScores().getReadingSEMScore()).append(SEPARATOR)
					.append(tabe.getAbilityScores().getMathCompAbilityScore()).append(SEPARATOR).append(tabe.getAbilityScores().getMathCompSEMScore()).append(SEPARATOR)
					.append(tabe.getAbilityScores().getAppliedMathAbilityScore()).append(SEPARATOR).append(tabe.getAbilityScores().getAppliedMathSEMScore()).append(SEPARATOR)
					.append(tabe.getAbilityScores().getLanguageAbilityScore()).append(SEPARATOR).append(tabe.getAbilityScores().getLanguageSEMScore()).append(SEPARATOR)
					.append(tabe.getAbilityScores().getLanguageMechAbilityScore()).append(SEPARATOR)//.append(tabe.getAbilityScores().getLanguageMechSEMScore()).append(SEPARATOR)
					.append(tabe.getAbilityScores().getVocabularyAbilityScore()).append(SEPARATOR)//.append(tabe.getAbilityScores().getVocabularySEMScore()).append(SEPARATOR)
					.append(tabe.getAbilityScores().getSpellingAbilityScore()).append(SEPARATOR)//.append(tabe.getAbilityScores().getSpellingSEMScore()).append(SEPARATOR)
					.append(tabe.getAbilityScores().getTotalMathAbilityScore()).append(SEPARATOR).append(tabe.getAbilityScores().getTotalBatteryAbilityScore()).append(SEPARATOR)
					
					.append(tabe.getGradeEquivalent().getReading()).append(SEPARATOR)
					.append(tabe.getGradeEquivalent().getMathComp()).append(SEPARATOR).append(tabe.getGradeEquivalent().getAppliedMath()).append(SEPARATOR)
					.append(tabe.getGradeEquivalent().getLanguage()).append(SEPARATOR).append(tabe.getGradeEquivalent().getLanguageMech()).append(SEPARATOR)
					.append(tabe.getGradeEquivalent().getVocabulary()).append(SEPARATOR).append(tabe.getGradeEquivalent().getSpelling()).append(SEPARATOR)
					.append(tabe.getGradeEquivalent().getTotalMath()).append(SEPARATOR).append(tabe.getGradeEquivalent().getTotalBattery()).append(SEPARATOR)
					
					.append(tabe.getNrsLevels().getReading()).append(SEPARATOR)
					.append(tabe.getNrsLevels().getMathComp()).append(SEPARATOR).append(tabe.getNrsLevels().getAppliedMath()).append(SEPARATOR)
					.append(tabe.getNrsLevels().getLanguage()).append(SEPARATOR).append(tabe.getNrsLevels().getLanguageMech()).append(SEPARATOR)
					.append(tabe.getNrsLevels().getVocabulary()).append(SEPARATOR).append(tabe.getNrsLevels().getSpelling()).append(SEPARATOR)
					.append(tabe.getNrsLevels().getTotalMath()).append(SEPARATOR).append(tabe.getNrsLevels().getTotalBattery()).append(SEPARATOR)
					
					.append(tabe.getPercentageMastery().getReading()).append(SEPARATOR)
					.append(tabe.getPercentageMastery().getMathComp()).append(SEPARATOR).append(tabe.getPercentageMastery().getAppliedMath()).append(SEPARATOR)
					.append(tabe.getPercentageMastery().getLanguage()).append(SEPARATOR).append(tabe.getPercentageMastery().getLanguageMech()).append(SEPARATOR)
					.append(tabe.getPercentageMastery().getVocabulary()).append(SEPARATOR).append(tabe.getPercentageMastery().getSpelling()).append(SEPARATOR)
					
					.append(tabe.getPredictedGED().getAverage()).append(SEPARATOR)
					.append(tabe.getPredictedGED().getMath()).append(SEPARATOR).append(tabe.getPredictedGED().getReading()).append(SEPARATOR)
					.append(tabe.getPredictedGED().getScience()).append(SEPARATOR).append(tabe.getPredictedGED().getSocialStudies()).append(SEPARATOR)
					.append(tabe.getPredictedGED().getWriting()).append(SEPARATOR).append(tabe.getObjectiveRawScore())
					
					.append(tabe.getObjectiveTotalRawScore()).append(tabe.getObjectiveScaleScore())
					.append(tabe.getObjectiveScaleScoreSEM()).append(tabe.getObjectiveMasteryLevel())
					.append(tabe.getObjectiveMastery())
				
				.append(tabe.getItemResponse());
				
				writer.writeNext(row.toString().split(SEPARATOR));
			}
			System.out.println("Export file successfully generated:["+fileName+"]");
			System.out.println("Completed Writing");
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}		
		
	private List<TABEFile> createList() throws Exception{
		List<TABEFile> tabeFileList = new ArrayList<TABEFile>();
		List<TestRoster> myrosterList = new ArrayList<TestRoster>();
		List<CustomerDemographic> customerDemoList = new ArrayList<CustomerDemographic>();
		HashMap<Integer, String> customerDemographic = new HashMap<Integer, String>();
		Connection oascon = null;
		Connection irscon = null;
		
		try {
			oascon = SqlUtil.openOASDBconnectionForResearch();
			irscon = SqlUtil.openIRSDBconnectionForResearch();
			int totalCount = 0;
			for (int i=0 ; i<productIds.length ; i++) {
				clearAllMaps();
				getProductType(oascon, Integer.parseInt(productIds[i]));
				//System.out.println("Excution for " + productIds[i] + " Product Id and Product Type " + PRODUCT_TYPE);
				getAllContentDomain(oascon, Integer.parseInt(productIds[i]));
				if(!"TL".equals(PRODUCT_TYPE)) {
					getAllObjectives(oascon);
				}
				customerDemoList = getCustomerDemographic(oascon);
				Set<CustomerDemographic> set = new HashSet<CustomerDemographic>(
						customerDemoList);
				for (CustomerDemographic c : set) {
					customerDemographic.put(c.getCustomerDemographicId(), c
							.getLabelName());
				}
				myrosterList = getTestRoster(oascon, Integer.parseInt(productIds[i]));
				int count = 0;
				for (TestRoster roster : myrosterList) {
					TABEFile catData = new TABEFile();
					Student studentInfo = roster.getStudent();
					fillStudent(catData, studentInfo);
					catData.setCustomerID(CUSTOMER_ID.toString());
					catData.setProductType(PRODUCT_TYPE.toString());
					catData.setRosterId(roster.getTestRosterId().toString());
					catData.setDateTestingCompleted(roster.getDateTestingCompleted());
					catData.setTestingSite(roster.getTestingSite());
					catData.setTestFormId(roster.getTestFormId());
					if (roster.getLastMseq() > 1000000 || roster.getRestartNumber() > 1){
						catData.setInterrupted("1");
					}else{
						catData.setInterrupted("0");
					}
					getLastItem(oascon, catData, roster);
					getTimedOut(oascon, catData, roster);
					createOrganization(oascon, catData, roster.getStudentId());
					fillEthnicity(studentInfo.getStudentDemographic(), customerDemographic, catData);
					fillAccommodations(studentInfo.getStudentAccommodations(), catData);
					createAbilityScoreInformation(irscon,catData,roster);
					//Assuming SEM Scores available only for CAT and Ability score in OAS db for CAT is final
					//getSemScores(oascon, catData, roster,catData.getAbilityScores()); 
					if("TA".equals(PRODUCT_TYPE)) {
						getSemScores(oascon, catData, roster,catData.getAbilityScores());
						fillObjective(oascon, catData, roster);
					}else if("TB".equals(PRODUCT_TYPE)){
						fillObjectiveForOnline(oascon, irscon, catData, roster);
					}
					prepareItemResponses(oascon, irscon, catData, roster, Integer.parseInt(productIds[i]));				
					tabeFileList.add(catData);	
					System.out.println("Record Processed: " + ++count);
				}
				System.out.println("Total records for Product Id " + productIds[i] + " is " + count);
				totalCount += count;
			}
			System.out.println("Total records: " + totalCount);
		} finally {
			SqlUtil.close(oascon);
			SqlUtil.close(irscon);
		}
		return tabeFileList;
	}
	
	private void clearAllMaps()
	{
		try{
			OBJECTIVE_MAP.clear();
			CONTENT_DOMAINS.clear();
			CUST_CATEGORIES.clear();
			ITEM_COUNT_MAP.clear();
		}catch(Exception e){
			
		}
	}
	
	private List<CustomerDemographic> getCustomerDemographic(Connection con)
	throws SQLException {
		//System.out.println("getCustomerDemographic start");
		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		/*
		 * Criteria crit = session.createCriteria(CustomerDemographic.class);
		 * crit.add(Expression.eq("customerId", customerId)); myList =
		 * crit.list();
		 */
	 
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SQLQuery.customerDemographicsql);
			ps.setString(1, CUSTOMER_ID);
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
            //System.out.println("getCustomerDemographic End");
		} finally {
			SqlUtil.close(ps, rs);
		}

		return myList;
	}
	
	private Set<CustomerDemographicValue> getCustomerDemographicValue(
			Connection con, int customerDemographicId) throws SQLException {
		//System.out.println("getCustomerDemographicValue start");
		PreparedStatement ps = null;
		ResultSet rs = null;
		Set<CustomerDemographicValue> customerDemographicValue = new HashSet<CustomerDemographicValue>();
		try {
			ps = con.prepareStatement(SQLQuery.customerDemographiValuecsql);
			ps.setInt(1, customerDemographicId);
			rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDemographicValue cdv = new CustomerDemographicValue();
				cdv.setValueName(rs.getString(1));
				cdv.setCustomerDemographicId(rs.getInt(3));
				customerDemographicValue.add(cdv);
			}

			//System.out.println("getCustomerDemographicValue End");
		} finally {
			SqlUtil.close(ps, rs);
		}

		return customerDemographicValue;

	}
	
	private List<TestRoster> getTestRoster(Connection con, Integer productId) throws SQLException {
		PreparedStatement ps = null ;
		ResultSet rs = null;
		 List<TestRoster> rosterList = new ArrayList<TestRoster>();
		try{
			ps = con.prepareStatement(SQLQuery.testRosterSql);
			ps.setString(1, CUSTOMER_ID);
			ps.setInt(2, productId);
			rs = ps.executeQuery(); 
			rs.setFetchSize(500);
			while (rs.next()){
				TestRoster ros = new TestRoster();
				ros.setTestRosterId(rs.getInt(1));
				ros.setActivationStatus(rs.getString(2));
				ros.setTestCompletionStatus(rs.getString(3));
				ros.setCustomerId(Integer.valueOf(CUSTOMER_ID));
				ros.setStudentId(rs.getInt(5));
				ros.setTestAdminId(rs.getInt(6));
				if(rs.getString(7) != null && rs.getString(8) != null)
				ros.setDateTestingCompleted(Utility.getTimeZone(rs.getString(7).toString(),rs.getString(8).toString(),true));
				ros.setRestartNumber(rs.getInt(9));
				ros.setLastMseq(rs.getInt(10));
				ros.setStartDate(rs.getString(11));
				ros.setStudent(getStudent(con,rs.getInt(5))); 
				ros.setTimeZone(rs.getString(8));
				ros.setTestingSite(rs.getString(12));
				String productName = rs.getString("product_name");
				if(PRODUCT_TYPE.equals("TB")) {
					if(productName.indexOf("9") > 0) {
						ros.setTestFormId("9");
					} else if(productName.indexOf("10") > 0) {
						ros.setTestFormId("10");
					}
				}
				else if(PRODUCT_TYPE.equals("TA")) {
					ros.setTestFormId("CAT");
				}
				rosterList.add(ros);
			}
		} finally {
			SqlUtil.close(ps, rs);
		}
		return rosterList;
	}
	
	private Student getStudent(Connection con, int studentId)
	throws SQLException {
		Student std = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SQLQuery.studentSql);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			if (rs.next()) {
				std = new Student();
				std.setStudentId(studentId);
				std.setFirstName(rs.getString(2) != null ? rs.getString(2) : "");
				std.setLastName(rs.getString(3) != null ? rs.getString(3) : "");
				std.setMiddleName(rs.getString(4) != null ? rs.getString(4) : "");
				std.setBirthDate(rs.getString(5));
				std.setGender(rs.getString(6) != null ? rs.getString(6) : "");
				std.setGrade(rs.getString(7) != null ? rs.getString(7) : "");
				std.setCustomerId(Integer.valueOf(CUSTOMER_ID));
				std.setTestPurpose(rs.getString(9) != null ? rs.getString(9) : "");
				std.setExtStudentId(rs.getString(10) != null ? rs.getString(10) : "");
				std.setExtStudentId2(rs.getString(11)!= null ? rs.getString(11) : "");
				std.setStudentDemographic(getStudentDemographic(con, studentId));
				std.setStudentAccommodations(getStudentAccommodations(con, studentId));
			}
		} finally {
			SqlUtil.close(ps, rs);
		}
		return std;
	}

	private Set<StudentDemographic> getStudentDemographic(Connection con,
			int studentId) throws SQLException {
		Set<StudentDemographic> studentDemographicSet = new HashSet<StudentDemographic>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SQLQuery.studentDemographicSql);
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

		} finally {
			SqlUtil.close(ps, rs);
		}

		return studentDemographicSet;
	}
	
	private Set<StudentAccommodations> getStudentAccommodations(Connection con,
			int studentId) throws SQLException {
		Set<StudentAccommodations> studentAccommodationsSet = new HashSet<StudentAccommodations>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SQLQuery.studentAccommodationsSql);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			while (rs.next()) {
				StudentAccommodations studentAccommodations = new StudentAccommodations();
				studentAccommodations.setStudentId(studentId);
				if(rs.getString(1) != null)
					studentAccommodations.setScreenMagnifier(setStudentAccommodationValue(rs.getString(1)));
				if(rs.getString(2) != null)
					studentAccommodations.setScreenReader(setStudentAccommodationValue(rs.getString(2)));
				if(rs.getString(3) != null)
					studentAccommodations.setCalculator(setStudentAccommodationValue(rs.getString(3)));
				if(rs.getString(4) != null)
					studentAccommodations.setTestPause(setStudentAccommodationValue(rs.getString(4)));
				if(rs.getString(5) != null)
					studentAccommodations.setUntimedTest(setStudentAccommodationValue(rs.getString(5)));
				if(rs.getString(6) != null)
					studentAccommodations.setQuestionBackgroundColor(setColor(rs.getString(6)));
				if(rs.getString(7) != null)
					studentAccommodations.setQuestionFontColor(setColor(rs.getString(7)));
				if(rs.getString(8) != null)
					studentAccommodations.setQuestionFontSize(setFontSize(rs.getString(8)));
				if(rs.getString(9) != null)
					studentAccommodations.setAnswerBackgroundColor(setColor(rs.getString(9)));
				if(rs.getString(10) != null)
					studentAccommodations.setAnswerFontColor(setColor(rs.getString(10)));
				if(rs.getString(11) != null)
					studentAccommodations.setAnswerFontSize(setFontSize(rs.getString(11)));
				if(rs.getString(12) != null)
					studentAccommodations.setHighlighter(setStudentAccommodationValue(rs.getString(12)));
				if(rs.getString(13) != null)
					studentAccommodations.setMusicFileName(rs.getString(13));
				if(rs.getString(14) != null)
					studentAccommodations.setMaskingRuler(setStudentAccommodationValue(rs.getString(14)));
				if(rs.getString(15) != null)
					studentAccommodations.setMagnifyingGlass(setStudentAccommodationValue(rs.getString(15)));
				if(rs.getString(16) != null)
					studentAccommodations.setExtendedTime(setStudentAccommodationValue(rs.getString(16)));
				if(rs.getString(17) != null)
					studentAccommodations.setMaskingTool(setStudentAccommodationValue(rs.getString(17)));
				if(rs.getString(18) != null)
					studentAccommodations.setMicrophoneHeadphone(setStudentAccommodationValue(rs.getString(18)));
				studentAccommodationsSet.add(studentAccommodations);
			}

		} finally {
			SqlUtil.close(ps, rs);
		}

		return studentAccommodationsSet;
	}

	private void createOrganization(Connection con, TABEFile tfil,
			Integer studentId)
	throws SQLException {

		//System.out.println("Create Organization Start");
		List<Organization> studentCatList = new ArrayList<Organization>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if(CUST_CATEGORIES.isEmpty()) {
				ps = con.prepareStatement(SQLQuery.CATEGORY_LEVEL_FOR_CUSTOMER);
				ps.setString(1, tfil.getCustomerID());
				rs = ps.executeQuery();
				while(rs.next()) {
					CUST_CATEGORIES.add(rs.getInt("category_level"));
				}
				SqlUtil.close(ps, rs);
			}
			
			ps = con.prepareStatement(SQLQuery.sql);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				if (rs.getString(4).equalsIgnoreCase("root")
						|| rs.getString(4).equalsIgnoreCase("CTB")) {
					// do nothing
				} else {
					Organization org = new Organization();
					org.setNodeCode(rs.getString("nodeCode") != null ? rs.getString("nodeCode") : "");
					org.setNodeName(rs.getString("nodeName") != null ? rs.getString("nodeName") : "");
					org.setCategoryLevel(rs.getInt("categoryLevel"));
					studentCatList.add(org);
				}
			}
			
			if(CUST_CATEGORIES.size() >= 4) {
				for (Organization org : studentCatList) {
					if(org.getCategoryLevel() == 1) {
						tfil.setOrgLevel1Code(org.getNodeCode());
						tfil.setOrgLevel1Name(org.getNodeName());
					} else if(org.getCategoryLevel() == 2) {
						tfil.setOrgLevel2Code(org.getNodeCode());
						tfil.setOrgLevel2Name(org.getNodeName());
					} else if(org.getCategoryLevel() == 3) {
						tfil.setOrgLevel3Code(org.getNodeCode());
						tfil.setOrgLevel3Name(org.getNodeName());
					} else if(org.getCategoryLevel() == 4) {
						tfil.setOrgLevel4Code(org.getNodeCode());
						tfil.setOrgLevel4Name(org.getNodeName());
					}
				}
			} else {
				int i=1;
				for (Organization org : studentCatList) {
					if(i == 1){
						tfil.setOrgLevel4Code(org.getNodeCode());
						tfil.setOrgLevel4Name(org.getNodeName());
					} else if(i == 2) {
						tfil.setOrgLevel3Code(org.getNodeCode());
						tfil.setOrgLevel3Name(org.getNodeName());
					} else if(i == 3) {
						tfil.setOrgLevel2Code(org.getNodeCode());
						tfil.setOrgLevel2Name(org.getNodeName());
					}
					i++;	
				}
				if(tfil.getOrgLevel2Name() == null) {
					tfil.setOrgLevel2Code(tfil.getOrgLevel3Code());
					tfil.setOrgLevel2Name(tfil.getOrgLevel3Name());
				}
				if(tfil.getOrgLevel1Name() == null) {
					tfil.setOrgLevel1Code(tfil.getOrgLevel2Code());
					tfil.setOrgLevel1Name(tfil.getOrgLevel2Name());
				}
			}
			//System.out.println("Create Organization End");
		} finally {
			SqlUtil.close(ps, rs);
		}
	}
	
	private void fillStudent(TABEFile tfil, Student st){
		tfil.setStudentID(st.getStudentId().toString());
		tfil.setStudentLastName(st.getLastName());
		tfil.setStudentFirstName(st.getFirstName());
		tfil.setStudentGender(st.getGender());
		tfil.setStudentMiddleName(st.getMiddleName());
		tfil.setExtStudentId(st.getExtStudentId().toString());
		tfil.setExtStudentId2(st.getExtStudentId2().toString());
		tfil.setGrade(Utility.formatGrade(st.getGrade()));
		tfil.setStudentBirthDate(st.getBirthDate());
	}
	
	private void fillEthnicity(Set<StudentDemographic> sd,
			HashMap<Integer, String> customerDemographic, TABEFile tfil) {

		TreeMap<String, StudentDemographic> set1 = new TreeMap<String, StudentDemographic>();
		HashMap<Integer, String> studentDemographic = new HashMap<Integer, String>();

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
				} else if (customerDemoName.equalsIgnoreCase("ELL")) {
					tfil.setEll(studentDem.getValueName() != null ? "1" : "0");
				} else if (customerDemoName.startsWith("Free")) {
					tfil.setFreeLunch(studentDem.getValueName() != null ? "1" : "0");
				} else if (customerDemoName.startsWith("IEP")) {
					tfil.setIep(studentDem.getValueName() != null ? "1" : "0");
				} else if (customerDemoName.startsWith("LEP")) {
					tfil.setLep(studentDem.getValueName() != null ? "1" : "0");
				} else if (customerDemoName.startsWith("Labor")) {
					setLaborForce(sd,tfil);
				}else if (customerDemoName.startsWith("Migrant")) {
					tfil.setMigrant(studentDem.getValueName() != null ? "1" : "0");
				}else if (customerDemoName.startsWith("Section")) {
					tfil.setSection504(studentDem.getValueName() != null ? "1" : "0");
				}
			}
		}
	}
	
	//Fix for Defect 68453
	private void setEthnicity(Set<StudentDemographic> sd, TABEFile tfil) {		
		for (StudentDemographic studentDemo : sd) {
			if(studentDemo.getValueName() == null){
				tfil.setEthnicity("0");
			}else if (studentDemo.getValueName().startsWith("African")) {
				tfil.setEthnicity("1");
			} else if (studentDemo.getValueName().startsWith("American")) {
				tfil.setEthnicity("2");
			} else if (studentDemo.getValueName().startsWith("Asian")) {
				tfil.setEthnicity("3");
			} else if (studentDemo.getValueName().startsWith("Caucasian")) {
				tfil.setEthnicity("4");
			} else if (studentDemo.getValueName().startsWith("Hispanic")) {
				tfil.setEthnicity("5");
			} else if (studentDemo.getValueName().startsWith("Multi-")
					|| studentDemo.getValueName().startsWith("Multiethnic")
					|| studentDemo.getValueName().startsWith("Multi et")) {
				tfil.setEthnicity("6");
			} 
		}
	}
	
	private void setLaborForce(Set<StudentDemographic> sd, TABEFile tfil) {
		for (StudentDemographic studentDemo : sd) {
			if(studentDemo.getValueName() == null){
				tfil.setLaborForceStatus("0");
			}else if (studentDemo.getValueName().startsWith("Employed")) {
				tfil.setLaborForceStatus("1");
			} else if (studentDemo.getValueName().startsWith("Not")) {
				tfil.setLaborForceStatus("2");
			} else if (studentDemo.getValueName().startsWith("Unemployed")) {
				tfil.setLaborForceStatus("3");
			}
		}	
	}
	
	private void fillAccommodations(Set<StudentAccommodations> sa, TABEFile tfill) {
		for(StudentAccommodations studentAccom : sa) {
			tfill.setScreenMagnifier(studentAccom.getScreenMagnifier());
			tfill.setScreenReader(studentAccom.getScreenReader());
			tfill.setCalculator(studentAccom.getCalculator());
			tfill.setTestPause(studentAccom.getTestPause());
			tfill.setUntimedTest(studentAccom.getUntimedTest());
			tfill.setQuestionBackgroundColor(studentAccom.getQuestionBackgroundColor());
			tfill.setQuestionFontColor(studentAccom.getQuestionFontColor());
			tfill.setQuestionFontSize(studentAccom.getQuestionFontSize());
			tfill.setAnswerBackgroundColor(studentAccom.getAnswerBackgroundColor());
			tfill.setAnswerFontColor(studentAccom.getAnswerFontColor());
			tfill.setAnswerFontSize(studentAccom.getAnswerFontSize());
			tfill.setHighlighter(studentAccom.getHighlighter());
			tfill.setMusicFileName(studentAccom.getMusicFileName());
			tfill.setMaskingRuler(studentAccom.getMaskingRuler());
			tfill.setMagnifyingGlass(studentAccom.getMagnifyingGlass());
			tfill.setExtendedTime(studentAccom.getExtendedTime());
			tfill.setMaskingTool(studentAccom.getMaskingTool());
			tfill.setMicrophoneHeadphone(studentAccom.getMicrophoneHeadphone());
		}
	}

	private void createAbilityScoreInformation(Connection con, TABEFile tfil, TestRoster roster) throws SQLException{
		AbilityScore abilityScore = new AbilityScore();
		GradeEquivalent gradeEquivalent = new GradeEquivalent();
		NRSLevels nrsLevel = new NRSLevels();
		PercentageMastery percentMast = new PercentageMastery();
		PredictedGED predictedGed =  new PredictedGED();
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs3 = null;
			
		try {
			ps2 = con.prepareStatement(SQLQuery.scoreSkilAreaOverAllSQL);
			ps2.setInt(1, roster.getStudentId());
			ps2.setInt(2, roster.getTestAdminId());
			rs2 = ps2.executeQuery();
			while(rs2.next()) {
				if (rs2.getString(1).toString().equalsIgnoreCase("Total Mathematics")){
					abilityScore.setTotalMathAbilityScore(rs2.getString(2) != null ? rs2.getString(2) : "" );
					gradeEquivalent.setTotalMath(rs2.getString(3) != null ? rs2.getString(3) : "" );
					nrsLevel.setTotalMath(rs2.getString(4) != null ? rs2.getString(4) : "" );
			
				} else {
					abilityScore.setTotalBatteryAbilityScore(rs2.getString(2) != null ? rs2.getString(2) : "" );
					gradeEquivalent.setTotalBattery(rs2.getString(3) != null ? rs2.getString(3) : "");
					nrsLevel.setTotalBattery(rs2.getString(4) != null ? rs2.getString(4) : "");					
				}
			}
		} finally {
			SqlUtil.close(ps2, rs2);
		}
		try {
			ps = con.prepareStatement(SQLQuery.scoreSkilAreaSQL);
			ps.setString(1, PRODUCT_TYPE+" CONTENT AREA");
			ps.setInt(2, roster.getStudentId());
			ps.setInt(3, roster.getTestAdminId());
			rs = ps.executeQuery();
			while(rs.next()) {
				if (rs.getString(1).toString().equalsIgnoreCase("Mathematics Computation") ||
						rs.getString(1).toString().equalsIgnoreCase("Math Computation")){
					abilityScore.setMathCompAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setMathComp(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setMathComp(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setMathComp(rs.getString(5) != null ? rs.getString(5) : "");
			
				} else if(rs.getString(1).toString().equalsIgnoreCase("Reading")){
					abilityScore.setReadingAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setReading(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setReading(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setReading(rs.getString(5) != null ? rs.getString(5) : "");	
					
				} else if(rs.getString(1).toString().equalsIgnoreCase("Applied Mathematics")){
					abilityScore.setAppliedMathAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setAppliedMath(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setAppliedMath(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setAppliedMath(rs.getString(5) != null ? rs.getString(5) : "");				
				
				} 
				else if(rs.getString(1).toString().equalsIgnoreCase("Language")){
					abilityScore.setLanguageAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setLanguage(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setLanguage(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setLanguage(rs.getString(5) != null ? rs.getString(5) : "");				
				} 
				else if(rs.getString(1).toString().equalsIgnoreCase("Language Mechanics")){
					abilityScore.setLanguageMechAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setLanguageMech(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setLanguageMech(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setLanguageMech(rs.getString(5) != null ? rs.getString(5) : "");				
				} 
				else if(rs.getString(1).toString().equalsIgnoreCase("Spelling")){
					abilityScore.setSpellingAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setSpelling(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setSpelling(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setSpelling(rs.getString(5) != null ? rs.getString(5) : "");				
				} 
				else if(rs.getString(1).toString().equalsIgnoreCase("Vocabulary")){
					abilityScore.setVocabularyAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setVocabulary(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setVocabulary(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setVocabulary(rs.getString(5) != null ? rs.getString(5) : "");				
				}
				
			}
		} finally {
			SqlUtil.close(ps, rs);
		}
		try {
			ps3 = con.prepareStatement(SQLQuery.getPredictedScores);
			ps3.setInt(1, roster.getStudentId());
			ps3.setInt(2, roster.getTestAdminId());
			rs3 = ps3.executeQuery();
			while(rs3.next()) {
				if (rs3.getString(1).toString().equalsIgnoreCase("Average")){	
					predictedGed.setAverage(rs3.getString(2) != null ? rs3.getString(2) : "");
					
				}else if(rs3.getString(1).toString().equalsIgnoreCase("Math")){
					predictedGed.setMath(rs3.getString(2) != null ? rs3.getString(2) : "");
					
				}else if(rs3.getString(1).toString().equalsIgnoreCase("Reading")){
					predictedGed.setReading(rs3.getString(2) != null ? rs3.getString(2) : "");			
				
				}else if(rs3.getString(1).toString().equalsIgnoreCase("Science")){
					predictedGed.setScience(rs3.getString(2) != null ? rs3.getString(2) : "");
			
				}else if(rs3.getString(1).toString().startsWith("Social")){
					predictedGed.setSocialStudies(rs3.getString(2) != null ? rs3.getString(2) : "");
			
				}else if(rs3.getString(1).toString().equalsIgnoreCase("Writing")){
					predictedGed.setWriting(rs3.getString(2) != null ? rs3.getString(2) : "");
			
				}
			}
		} finally {
			SqlUtil.close(ps3, rs3);
		}
		tfil.setAbilityScores(abilityScore);
		tfil.setGradeEquivalent(gradeEquivalent);
		tfil.setNrsLevels(nrsLevel);
		tfil.setPercentageMastery(percentMast);	
		tfil.setPredictedGED(predictedGed);
	}
	
	private void getSemScores(Connection con, TABEFile tfil, TestRoster roster, AbilityScore abilityScore)
	throws SQLException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		//String testCompleted = "";
		try {
			ps = con.prepareStatement(SQLQuery.getSemScores);
			ps.setInt(1, roster.getTestRosterId());
			ps.setInt(2, roster.getTestRosterId());
			rs = ps.executeQuery();
			while(rs.next()) {
				if (rs.getString(1).toString().equalsIgnoreCase("Mathematics Computation")){
					abilityScore.setMathCompAbilityScore(rs.getString(4) != null ? rs.getString(4) : "" );
					abilityScore.setMathCompSEMScore(rs.getString(2) != null ? rs.getString(2) : "" );			
				}else if(rs.getString(1).toString().equalsIgnoreCase("Reading")) {
					abilityScore.setReadingAbilityScore(rs.getString(4) != null ? rs.getString(4) : "" );
					abilityScore.setReadingSEMScore(rs.getString(2) != null ? rs.getString(2) : "" );				
				}else if(rs.getString(1).toString().equalsIgnoreCase("Applied Mathematics")) {
					abilityScore.setAppliedMathAbilityScore(rs.getString(4) != null ? rs.getString(4) : "" );
					abilityScore.setAppliedMathSEMScore(rs.getString(2) != null ? rs.getString(2) : "" );				
				}else if(rs.getString(1).toString().equalsIgnoreCase("Language")) {
					abilityScore.setLanguageAbilityScore(rs.getString(4) != null ? rs.getString(4) : "" );
					abilityScore.setLanguageSEMScore(rs.getString(2) != null ? rs.getString(2) : "" );				
				}
				//testCompleted = rs.getString(3) != null ? rs.getString(3) : roster.getStartDate();
				//if(tfil.getDateTestingCompleted() == null || tfil.getDateTestingCompleted().length() == 0){
				//testCompleted = rs.getString(3) != null ? rs.getString(3) : roster.getStartDate();
				//if(!"".equals(testCompleted))
				//tfil.setDateTestingCompleted(Utility.getTimeZone(testCompleted, roster.getTimeZone(), true));
				//}
			} 
		} finally {
			SqlUtil.close(ps, rs);
		}
		//tfil.setDateTestingCompleted(Utility.getTimeZone(testCompleted, roster.getTimeZone(), true));
		tfil.setAbilityScores(abilityScore);	
	}
	
	private void fillObjective(Connection oascon, TABEFile tfil, TestRoster roster) 
	throws SQLException, Exception {

		PreparedStatement ps = null;
		ResultSet rs = null;
		ObjectiveLevel objMasteryLevel = new ObjectiveLevel();
		ObjectiveLevel objMastery = new ObjectiveLevel();
		ObjectiveLevel objRawScore = new ObjectiveLevel();
		ObjectiveLevel objTotalRawScore = new ObjectiveLevel();
		ObjectiveLevel objScaleScore = new ObjectiveLevel();
		ObjectiveLevel objScaleScoreSEM = new ObjectiveLevel();
		
		try{
			ps = oascon.prepareStatement(SQLQuery.OBJECTIVE_SCORE_SQL);
			ps.setInt(1, roster.getTestRosterId());
			rs = ps.executeQuery();
			while(rs.next()) {
				String objectiveScore = rs.getString("objective_score");
				if(objectiveScore != null) {
					String [] objectives = objectiveScore.split("\\|");
					for(String objective : objectives) {
						String [] scores = objective.split(",");
						String objectiveName = OBJECTIVE_MAP.get(scores[0]);						
						if(objectiveName != null) {
							objRawScore.getObjectiveMap().put(objectiveName, scores[1]);
							objTotalRawScore.getObjectiveMap().put(objectiveName, scores[2]);
							objScaleScore.getObjectiveMap().put(objectiveName, scores[3]);
							objScaleScoreSEM.getObjectiveMap().put(objectiveName, scores[4]);
							if(scores.length > 6) {
								objMasteryLevel.getObjectiveMap().put(objectiveName, scores[5]);
								if(scores[6] != null) {
									objMastery.getObjectiveMap().put(objectiveName, String.valueOf(Integer.parseInt(scores[6]) + 1));
								}
							} else {
								objMasteryLevel.getObjectiveMap().put(objectiveName, "N/A");
								objMastery.getObjectiveMap().put(objectiveName, String.valueOf(Integer.parseInt(scores[5]) + 1));
							}
						}
					}
				}
			}
			tfil.setObjectiveMasteryLevel(objMasteryLevel);
			tfil.setObjectiveMastery(objMastery);
			tfil.setObjectiveRawScore(objRawScore);
			tfil.setObjectiveTotalRawScore(objTotalRawScore);
			tfil.setObjectiveScaleScore(objScaleScore);
			tfil.setObjectiveScaleScoreSEM(objScaleScoreSEM);
		} finally{
			SqlUtil.close(ps, rs);
		}
	}	
	
	private void fillObjectiveForOnline(Connection oascon, Connection irscon, TABEFile tfil, TestRoster roster) 
	throws SQLException, Exception {

		PreparedStatement ps = null;
		ResultSet rs = null;
		ObjectiveLevel objMasteryLevel = new ObjectiveLevel();
		ObjectiveLevel objMastery = new ObjectiveLevel();
		ObjectiveLevel objRawScore = new ObjectiveLevel();
		ObjectiveLevel objTotalRawScore = new ObjectiveLevel();
		ObjectiveLevel objScaleScore = new ObjectiveLevel();
		ObjectiveLevel objScaleScoreSEM = new ObjectiveLevel();
		
		try{
			ps = irscon.prepareStatement(SQLQuery.OBJECTIVE_SCORE_TB);
			ps.setInt(1, roster.getTestAdminId());
			ps.setInt(2, roster.getStudentId());
			rs = ps.executeQuery();
			//System.out.println(rs.getFetchSize());
			//System.out.println(rs.getRow());
			while(rs.next()) {
				String objectiveName = OBJECTIVE_MAP.get(rs.getString("objectiveId"));
				//System.out.println("Objective Id : " + rs.getInt("objectiveId") + " Objective Name : " + objectiveName);
				if(objectiveName != null) {
					objRawScore.getObjectiveMap().put(objectiveName, String.valueOf(rs.getInt("rawScore")));
					objTotalRawScore.getObjectiveMap().put(objectiveName, String.valueOf(rs.getInt("totalRawScore")));
					//objScaleScore.getObjectiveMap().put();
					//objScaleScoreSEM.getObjectiveMap().put();
					objMasteryLevel.getObjectiveMap().put(objectiveName, String.valueOf(rs.getInt("mastery")));
					//objMastery.getObjectiveMap().put();
				}
				tfil.setObjectiveMasteryLevel(objMasteryLevel);
				tfil.setObjectiveMastery(objMastery);
				tfil.setObjectiveRawScore(objRawScore);
				tfil.setObjectiveTotalRawScore(objTotalRawScore);
				tfil.setObjectiveScaleScore(objScaleScore);
				tfil.setObjectiveScaleScoreSEM(objScaleScoreSEM);
			} 
		} finally{
			SqlUtil.close(ps, rs);
		}
	}	
	private void prepareItemResponses(Connection con, Connection irscon, TABEFile tfil,
	   TestRoster roster, Integer productId) throws IOException, Exception {
		   PreparedStatement ps = null ;
		   ResultSet rs = null;
		   PreparedStatement ps1 = null ;
		   ResultSet rs1 = null;
		   int timedOut = 0;
		   Map<String, ItemResponses> itemMap = new LinkedHashMap<String, ItemResponses>();
		   Map<String, Scores> scoreMap = new LinkedHashMap<String, Scores>();
		   Map<String, Scores> scaleScoreMap = new LinkedHashMap<String, Scores>();
		   StringBuilder response = new StringBuilder();
		   try {
			   getContentDomain(con, roster.getTestRosterId());
			   Set<String> keySet = CONTENT_DOMAINS.keySet();
			   int itemCount = 0;
			   for(String itemSetName: keySet) {
				   StringBuilder itemResposneData = new StringBuilder();
				   Integer itemSetId = CONTENT_DOMAINS.get(itemSetName);
				   System.out.println("Item Set Name : " + itemSetName + " : Item Set ID : " + itemSetId);
				   if("Reading".equals(itemSetName)) {
					   response.append("RD" + SEPARATOR);
					   //itemCount = 29;
				   } else if("Mathematics Computation".equals(itemSetName)) {
					   response.append("MC" + SEPARATOR);
					   //itemCount = 24;
				   } else if("Applied Mathematics".equals(itemSetName)) {
					   response.append("AM" + SEPARATOR);
					   //itemCount = 29;
				   } else if("Language".equals(itemSetName)) {
					   response.append("LN" + SEPARATOR);
					   //itemCount = 29;
				   } else if("Language Mechanics".equals(itemSetName)) {
					   response.append("LM" + SEPARATOR);
					   //itemCount = 29;
				   } else if("Vocabulary".equals(itemSetName)) {
					   response.append("VO" + SEPARATOR);
					   //itemCount = 29;
				   } else if("Spelling".equals(itemSetName)) {
					   response.append("SP" + SEPARATOR);
					   //itemCount = 29;
				   }
				   //ITEM_COUNT_MAP.put(itemSetName, itemCount);
				   //Getting content domain
					if("TB".equals(PRODUCT_TYPE)) {
						//System.out.println("This is Online product.");
						ps = con.prepareStatement(SQLQuery.CONTENT_DOMAIN_SQL_TABE_ONLINE);
					}
					else if("TA".equals(PRODUCT_TYPE)) {
						ps = con.prepareStatement(SQLQuery.CONTENT_DOMAIN_SQL_TABE_ADAPTIVE);
					}
					else if("TL".equals(PRODUCT_TYPE)) {
						ps = con.prepareStatement(SQLQuery.CONTENT_DOMAIN_SQL_TABE_LOCATOR);
					}
					ps.setInt(1, roster.getTestRosterId());
					rs = ps.executeQuery();
					while(rs.next()) {
						Scores score = new Scores();
						score.setItemSetId(rs.getString("item_set_id"));
						score.setContentAreaId(productId + rs.getString("item_set_id"));
						score.setItemSetName(rs.getString("item_set_name"));
						score.setLevel(rs.getString("item_set_level"));
						score.setRawScore(rs.getString("raw_score"));
						/*System.out.println("Item set Id : " + rs.getString("item_set_id") + " : Content Area Id : "
								+ score.getContentAreaId() + " : Item Set Name : " + rs.getString("item_set_name") + 
								" : Item Set Level : " + rs.getString("item_set_level") + " : Raw Score : " + rs.getString("raw_score"));*/
						scoreMap.put(score.getItemSetName(), score);
						scaleScoreMap.put(score.getContentAreaId(), score);
					}
					SqlUtil.close(ps, rs);
					//Getting scale score
					ps = irscon.prepareStatement(SQLQuery.SCALE_SCORE_SQL);
					ps.setInt(1, roster.getStudentId());
					ps.setInt(2, roster.getTestAdminId());
					rs = ps.executeQuery();
					while(rs.next()) {
						Scores score = scaleScoreMap.get(rs.getString("content_areaid"));
						//System.out.println("Scale score for Product Id : " + rs.getString("content_areaid") + " is " + rs.getString("scale_score"));
						if(score != null) {
							score.setScaleScore(rs.getString("scale_score"));
						}
					}
				   SqlUtil.close(ps, rs);
					Scores score = scoreMap.get(itemSetName);
					if(score != null) {
						Scores scaleScore = scaleScoreMap.get(score.getContentAreaId());
						if(scaleScore != null) {
							score.setScaleScore(scaleScore.getScaleScore());
						}
					}
					if(score != null) {
						response.append(Utility.formatData(score.getLevel()) + "," + Utility.formatData(score.getRawScore())
							 	  + "," + Utility.formatData(score.getScaleScore()) + SEPARATOR);
					} else {
						response.append(",," + SEPARATOR ); 
					}
				   if("TB".equals(PRODUCT_TYPE) || "TL".equals(PRODUCT_TYPE)) {
					   ps = con.prepareStatement(SQLQuery.ALL_ITEMS_DETAILS_SQL_TB);
					   ps.setInt(1, roster.getTestRosterId());
					   ps.setInt(2, itemSetId);
					   ps.setInt(3, roster.getTestRosterId());
					   ps.setInt(4, itemSetId);
				   }
				   else if("TA".equals(PRODUCT_TYPE)) {
					   ps = con.prepareStatement(SQLQuery.ALL_ITEMS_DETAILS_SQL_TA);
					   ps.setInt(1, roster.getTestRosterId());
					   ps.setInt(2, itemSetId);
					   ps.setInt(3, roster.getTestRosterId());
					   ps.setInt(4, itemSetId);
					   ps.setInt(5, roster.getTestRosterId());
					   ps.setInt(6, itemSetId);
				   }
				   rs = ps.executeQuery();
				   int index = 1;
				   int currentSequenceNo = 0;
				   int lastSequenceNo = 0;
				   int restartItemNumber = 0;
				   String lastItemId = "";
				   //System.out.println("Item Set Id : " + itemSetId);
				   while (rs.next()) {
					  ItemResponses ir = new ItemResponses();
					  currentSequenceNo = rs.getInt("maxseqnum");
					  ir.setItemId(rs.getString("item_id"));
					  ir.setResponseValue(rs.getString("response"));
					  ir.setOriginalResponse(rs.getString("original_response"));
					  ir.setResponseTime(rs.getString("response_elapsed_time"));
					  ir.setOasItemId(rs.getString("oas_item_id"));
					  ir.setSequenceNo(currentSequenceNo);
					  ir.setIndex(index++);	
					  //stoppedItem = rs.getString("item_id");
					  if(currentSequenceNo > lastSequenceNo + 100000) {
						  ps1 = con.prepareStatement(SQLQuery.GET_ITEM_RESPONSE_FOR_ITEM);
						  ps1.setInt(1, roster.getTestRosterId());
						  ps1.setInt(2, itemSetId);
						  ps1.setLong(3, currentSequenceNo - 100000);
						  rs1 = ps1.executeQuery();
						  //System.out.println("Max seq num : " + rs.getInt("maxseqnum"));
						  //System.out.println("Last seq num : " + lastSequenceNo);
						  //System.out.println("Current seq num : " + currentSequenceNo);
						  if(rs1.next()) {
							  lastItemId = rs1.getString("item_id");							  
							  if(!lastItemId.equals(ir.getItemId())) {
								  itemMap.remove(lastItemId);
								  if(restartItemNumber == 0) {
									  restartItemNumber = ir.getIndex() - 1;
								  }
							  } else {
								  if(restartItemNumber == 0) {
									  restartItemNumber = ir.getIndex();
								  } 
							  }
						  }
						  SqlUtil.close(ps1, rs1);
					  }
					  lastSequenceNo = currentSequenceNo;
					  itemMap.put(ir.getItemId(), ir);
					  //tfil.setLastItem(rs.getString(1));   This is according to TABE online logic, We will set this parameter later.
				   }
				   //System.out.println("Item count " + index); 
				   SqlUtil.close(ps, rs);
				    
				   Set<String> itemSet = itemMap.keySet();
				   List<String> itemList = new ArrayList<String>(itemSet);
				   itemCount = itemList.size();
				   ITEM_COUNT_MAP.put(itemSetName, itemCount);
				   //System.out.println("value of item count " + itemCount);
				   for(int i=0; i<itemCount; i++) {
					   ItemResponses ir = itemMap.get(itemList.get(i));
					   String itemId = ir.getItemId().trim() + PIPE;
					   String oasItemId = ir.getOasItemId().trim() + PIPE;
					   String rawScore = ir.getResponseValue().trim() + PIPE;
					   String originalResponse = ir.getOriginalResponse().trim() + PIPE;
					   String timeOnItem = ir.getResponseTime().trim();
					   itemResposneData.append(itemId + oasItemId + 
							   rawScore + originalResponse + timeOnItem + SEPARATOR);
				   }
				   int itemMaxCountForSubtest = ITEM_COUNT_MAP_TB.get(itemSetName);
				   for(int i=0; i<itemMaxCountForSubtest-itemCount ;i++) {
					   itemResposneData.append(SPACE + SEPARATOR);
				   }
				   
				   String stoppedItem = String.valueOf(itemList.size());
				   //System.out.println("Item response size : " + itemList.size());
				   //System.out.println("Item Count : " + itemCount);
				   timedOut = getTimedOutForEachContentArea(con, tfil, roster, itemSetId);
				   //System.out.println("Timed Out : " + timedOut);
				   //System.out.println("Product Id : " + productId);
				   int itemCountForSubtest = 0;
				   if (productId == 4008) {
					   itemCountForSubtest = ITEM_COUNT_MAP_TL.get(itemSetName); 
				   }
				   else if (productId == 4009 || productId == 4011) {
					   itemCountForSubtest = ITEM_COUNT_MAP_TS.get(itemSetName); 
				   }
				   else if (productId == 4010 || productId == 4012) {
					   itemCountForSubtest = ITEM_COUNT_MAP_TB.get(itemSetName); 
				   }
				   else if (productId == 8001) {
					   itemCountForSubtest = ITEM_COUNT_MAP_TA.get(itemSetName); 
				   }
				   //System.out.println("Item count for subtest : " + itemCountForSubtest);
				   if(timedOut == 0) {  // timedOut = 0 means not IN
					   if(itemList.size() < itemCountForSubtest) {  // Test is stopped due to some other reason than IN may be IS
						   timedOut = 2;
					   } else {   // If the test is not stopped then stoppedItem is set to blank
						   stoppedItem = "";
					   }
				   }
				   if(itemResposneData.length() > 0) {
					   response.append(itemResposneData);
				   } else {
					   response.append(SPACE + SEPARATOR);
				   }
				   if(itemMap.size() > 0) {
					   if(restartItemNumber == 0) {
						   response.append("0" + SEPARATOR + "0" + SEPARATOR);
					   } else {
						   response.append("1" + SEPARATOR + restartItemNumber + SEPARATOR);
					   }
					   response.append(timedOut + SEPARATOR);
					   response.append(stoppedItem + SEPARATOR);
				   } else {
					   response.append(SPACE + SEPARATOR + SPACE + SEPARATOR + SPACE + SEPARATOR + SPACE + SEPARATOR);
				   }
				   itemMap.clear();
			   }
			   
			   if(response.length() > 1)
				  tfil.setItemResponse(response.substring(0, response.length() - 1));
			   else 
				  tfil.setItemResponse(response.toString());
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				SqlUtil.close(ps, rs);
			}
		}
	
	private static void getAllObjectives(Connection con) {
	    PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if("TA".equals(PRODUCT_TYPE)){
				ps = con.prepareStatement(SQLQuery.ALL_OBJECTIVE_SQL_TA);
			}else if("TB".equals(PRODUCT_TYPE)){
				ps = con.prepareStatement(SQLQuery.ALL_OBJECTIVE_SQL_TB);
			}
			rs = ps.executeQuery();
			while(rs.next()) {
				OBJECTIVE_MAP.put(rs.getString("objective_id"), rs.getString("objective_name"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
    }
	
	private static void getContentDomain(Connection con, Integer testRosterId) 
    throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(("TL".equals(PRODUCT_TYPE))? SQLQuery.CONTENT_DOMAIN_FOR_ROSTER_SQL_TABE_LOCATOR : SQLQuery.CONTENT_DOMAIN_FOR_ROSTER_SQL_TABE);
			ps.setInt(1, testRosterId);
			rs = ps.executeQuery();
			//System.out.println("Product Type is : " + PRODUCT_TYPE);
			while(rs.next()) {
				if("TB".equals(PRODUCT_TYPE)) {
					//System.out.println("Item Set Name [TABE] : " + rs.getString("item_set_name") + " : Item set Name : " + rs.getString("item_set_name") + " : Item Set ID : " + rs.getInt("item_set_id"));
					CONTENT_DOMAINS.put(rs.getString("item_set_name").substring(5), rs.getInt("item_set_id"));
				}
				else if("TA".equals(PRODUCT_TYPE) || "TL".equals(PRODUCT_TYPE)) {
					//System.out.println("Item Set Name : " + rs.getString("item_set_name") + " : Item set Name : " + rs.getString("item_set_name") + " : Item Set ID : " + rs.getInt("item_set_id"));
					CONTENT_DOMAINS.put(rs.getString("item_set_name"), rs.getInt("item_set_id"));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
	}
	
	private void getAllContentDomain(Connection con, Integer productId) 
    throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(("TL".equals(PRODUCT_TYPE))?SQLQuery.GET_ALL_CONTENT_DOMAIN_SQL_FOR_LOCATOR:SQLQuery.GET_ALL_CONTENT_DOMAIN_SQL);
			ps.setInt(1, productId);
			rs = ps.executeQuery();
			while(rs.next()) {
				if("TB".equals(PRODUCT_TYPE)) {
					CONTENT_DOMAINS.put(rs.getString("item_set_name").substring(5), rs.getInt("item_set_id"));
				}
				else if("TA".equals(PRODUCT_TYPE) || "TL".equals(PRODUCT_TYPE)) {
					CONTENT_DOMAINS.put(rs.getString("item_set_name"), rs.getInt("item_set_id"));
				}
				
			}
			//System.out.println("Content Domain Map Size : " + CONTENT_DOMAINS.size() + " for Product Type : " + PRODUCT_TYPE);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
	}
	
	private int getTimedOutForEachContentArea(Connection con, TABEFile tfil, TestRoster roster, Integer itemSetId) 
	   throws SQLException {
			PreparedStatement ps = null;
			ResultSet rs = null;
			long totalTestTime = 0L;
			long totalTimeTaken = 0L;
			int timedOut = 0;
			try {
				ps = con.prepareStatement(SQLQuery.TOTAL_TEST_TIME_SQL_PER_CONTENT_AREA);
				ps.setInt(1, itemSetId);
				ps.setInt(2, roster.getTestRosterId());
				rs = ps.executeQuery();
				if(rs.next()) {
					totalTestTime = rs.getLong("total_time");
				}
				SqlUtil.close(ps, rs);
				ps = con.prepareStatement(SQLQuery.TOTAL_TIME_TAKEN_SQL_PER_CONTENT_AREA);
				ps.setInt(1, itemSetId);
				ps.setInt(2, roster.getTestRosterId());
				rs = ps.executeQuery();
				if(rs.next()) {
					totalTimeTaken = rs.getLong("total_time");
				}
				if(totalTimeTaken > totalTestTime) {
					timedOut = 1;
				} else {
					timedOut = 0;
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				SqlUtil.close(ps, rs);
			}
			return timedOut;
		}
	
		private String setStudentAccommodationValue(String studentAccommodationValue) {
			String studentAccomValue = "";
			if(studentAccommodationValue.equalsIgnoreCase("T")) {
				studentAccomValue = "TRUE";
			}
			else if(studentAccommodationValue.equalsIgnoreCase("F")) {
				studentAccomValue = "FALSE";
			}
			return studentAccomValue;
		}
		
		private String setColor(String studentAccommodationValue) {
			
			String color = "";
			final String[] colorTexts = {"Light blue", "Light pink", "Light yellow", 
                "White",      "Black",      "Dark blue", 
                "Dark brown", "Yellow",     "Green"
               };
			final String[] colorHexs = { "#CCECFF",    "#FFCCCC",    "#FFFFB0",
                "#FFFFFF",    "#000000",    "#000080",
                "#663300",    "#FFFF99",    "#00CC00"
              };
			
			for (int i =0 ; i < colorHexs.length ; i++) {
				if(studentAccommodationValue.equalsIgnoreCase(colorHexs[i])) {
					color = colorTexts[i];
				}
			}
			return color;
		}
		
		private String setFontSize(String studentAccommodationValue) {
			
			String fontSize = "";
			final String[] fontSizes = { "1", "1.5" };
		    final String[] fontSizeLabels = { "Standard", "Large" };
			
			for (int i =0 ; i < fontSizeLabels.length ; i++) {
				if(studentAccommodationValue.equalsIgnoreCase(fontSizes[i])) {
					fontSize = fontSizeLabels[i];
				}
			}
			return fontSize;
		}
		
		private void getProductType(Connection oascon, Integer productId) 
			throws SQLException  {
			
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = oascon.prepareStatement(SQLQuery.PRODUCT_TYPE_SQL);
				ps.setInt(1, productId);
				rs = ps.executeQuery();
				if(rs.next()) {
					PRODUCT_TYPE = rs.getString("PRODUCT_TYPE");
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				SqlUtil.close(ps, rs);
			}
		}
		
		private void getTimedOut(Connection con, TABEFile tfil, TestRoster roster) 
		   throws SQLException {
				PreparedStatement ps = null;
				ResultSet rs = null;
				long totalTestTime = 0L;
				long totalTimeTaken = 0L;
				try {
					ps = con.prepareStatement(SQLQuery.TOTAL_TEST_TIME_SQL);
					ps.setInt(1, roster.getTestRosterId());
					rs = ps.executeQuery();
					if(rs.next()) {
						totalTestTime = rs.getLong("total_time");
					}
					SqlUtil.close(ps, rs);
					ps = con.prepareStatement(SQLQuery.TOTAL_TIME_TAKEN_SQL);
					ps.setInt(1, roster.getTestRosterId());
					rs = ps.executeQuery();
					if(rs.next()) {
						totalTimeTaken = rs.getLong("total_time");
					}
					if(totalTimeTaken > totalTestTime) {
						tfil.setTimedOut("T");
					} else {
						tfil.setTimedOut("F");
					}
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					SqlUtil.close(ps, rs);
				}
			}
		
		private void getLastItem(Connection con, TABEFile tfil, TestRoster roster) 
		   throws SQLException {
				PreparedStatement ps = null;
				ResultSet rs = null;
				String lastItem = "";
				try {
					ps = con.prepareStatement(SQLQuery.GET_LAST_ITEM_SQL);
					ps.setInt(1, roster.getTestRosterId());
					ps.setInt(2, roster.getTestRosterId());
					rs = ps.executeQuery();
					if(rs.next()) {
						lastItem = rs.getString("lastItemId");
					}
					tfil.setLastItem(lastItem);
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					SqlUtil.close(ps, rs);
				}
			}
		
		private static Properties loadProperties(String env)
		{
			InputStream in = null;
			Properties prop = new Properties();
			try {
				in = new FileInputStream(env);
				prop.load(in);
				return prop;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		private String getPropertyValue(String name)
		{
			String value = this.properties.getProperty(name);
			if ((value == null) || (value.length() == 0)) {
				return "";
			}
			return value;
		}
		
		private void getCommandLine(String[] args)
		{
			if ((args.length < 1) || (args[0].indexOf('=') >= 0)) {
				System.out.println("Cannot parse command line. No command specified. " +
									"Kindly provide properties file as argument.");
			}
			else
				this.env = args[0].toLowerCase();
		}
}