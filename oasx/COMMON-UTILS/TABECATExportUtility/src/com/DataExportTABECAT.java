package com;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import com.ctb.dto.Student;
import com.ctb.dto.StudentDemographic;
import com.ctb.dto.TABEFile;
import com.ctb.dto.TestRoster;
import com.ctb.utils.ExtractUtil;
import com.ctb.utils.SQLQuery;
import com.ctb.utils.SqlUtil;
import com.ctb.utils.Utility;

public class DataExportTABECAT {
	
	private static String CUSTOMER_ID = ExtractUtil.getDetail("oas.customerId");
	private static Integer PRODUCT_ID = new Integer(ExtractUtil.getDetail("oas.productId"));
	private static final String FILE_TYPE = ExtractUtil.getDetail("oas.exportdata.fileType");
	private static final String FILE_NAME = ExtractUtil.getDetail("oas.exportdata.fileName");
	private static final String LOCAL_FILE_PATH = ExtractUtil.getDetail("oas.exportdata.filepath");
	private static final Map<String, String> OBJECTIVE_MAP = new LinkedHashMap<String, String>();
	private static final Map<String, Integer> CONTENT_DOMAINS = new LinkedHashMap<String, Integer>();
	private static final Set<Integer> CUST_CATEGORIES = new TreeSet<Integer>();
	private static final String SEPARATOR = "#";
	private static final String SPACE = " ";
	private static final Map<String, Integer> ITEM_COUNT_MAP = new LinkedHashMap<String, Integer>();
	private static final Map<String, Integer> SCORE_TYPES = new LinkedHashMap<String, Integer>();
	
	static {
		CONTENT_DOMAINS.put("Reading", 0);
		CONTENT_DOMAINS.put("Mathematics Computation", 0);
		CONTENT_DOMAINS.put("Applied Mathematics", 0);
		CONTENT_DOMAINS.put("Language", 0);
		
		SCORE_TYPES.put("Objective Raw Score", 0);
		SCORE_TYPES.put("Objective Total Raw Score", 0);
		SCORE_TYPES.put("Objective Scale Score", 0);
		SCORE_TYPES.put("Objective Scale Score SEM", 0);
		SCORE_TYPES.put("Objective Mastery Level", 0);
		SCORE_TYPES.put("Objective Mastery", 0);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataExportTABECAT dataExport = new DataExportTABECAT();
		try {
			if(args != null && args.length > 0) {
				if(args[0] != null) {
					CUSTOMER_ID = args[0];
				}
				if(args[1] != null) {
					PRODUCT_ID = Integer.valueOf(args[1]);
				}
			}
			dataExport.writeToText();
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
		String itemDetails[] = {"ItemID","ItemResponse","ItemOrgResponse","ItemRespTime"};
		int count=0;
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
				.append("Sentence and Paragraph Development").append(SEPARATOR).append("Writing Mechanics and Conventions)").append(SEPARATOR);
		}
		
		for(String contentDomainName: CONTENT_DOMAINS.keySet()) {
			count=0;
			contentDomains.append(contentDomainName).append(SEPARATOR);
			Integer itemCount = ITEM_COUNT_MAP.get(contentDomainName);
			if(itemCount != null) {
				i=0;
				while(i < itemCount) {
					contentDomains.append(itemDetails[count]+(i+1)).append(SEPARATOR);
					i++;
					if(i==(itemCount)) {
						if(count==(itemDetails.length)-1){
							break;
						}
						i=0;
						count++;
						continue;
					}	
				}
				contentDomains.append("Restart Flag").append(SEPARATOR).append("Restart Item Number").append(SEPARATOR)
				.append("Timed Out").append(SEPARATOR).append("Stopped Item Number").append(SEPARATOR);			
			}
		}			
				
		headerRow.append("Customer Id").append(SEPARATOR).append("State Name").append(SEPARATOR)
		.append("State Code").append(SEPARATOR).append("District Name").append(SEPARATOR)
		.append("District Code").append(SEPARATOR).append("School Name").append(SEPARATOR)
		.append("School Code").append(SEPARATOR).append("Class Name").append(SEPARATOR)
		.append("Class Code").append(SEPARATOR).append("Grade").append(SEPARATOR)
		.append("Student Id").append(SEPARATOR).append("First Name").append(SEPARATOR)
		.append("Last Name").append(SEPARATOR).append("Middle Initial").append(SEPARATOR)
		.append("Birth Date").append(SEPARATOR).append("Gender").append(SEPARATOR)
		.append("Student Id 2").append(SEPARATOR).append("ELL").append(SEPARATOR)		
		.append("Ethnicity").append(SEPARATOR).append("Free Lunch").append(SEPARATOR)
		.append("IEP").append(SEPARATOR).append("LEP").append(SEPARATOR)
		.append("Labor Force").append(SEPARATOR).append("Migrant").append(SEPARATOR)
		.append("Section 504").append(SEPARATOR).append("Date Testing Complete").append(SEPARATOR)
		.append("Interrupted").append(SEPARATOR).append("Reading - Ability Score").append(SEPARATOR)
		.append("Reading - SEM Score").append(SEPARATOR).append("Math Comp - Ability Score").append(SEPARATOR)
		.append("Math Comp - SEM Score").append(SEPARATOR).append("Applied Math - Ability Score").append(SEPARATOR)
		.append("Applied Math - SEM Score").append(SEPARATOR).append("Language - Ability Score").append(SEPARATOR)
		.append("Language - SEM Score").append(SEPARATOR).append("Total Math - Ability Score").append(SEPARATOR)
		.append("Total Battery - Ability Score").append(SEPARATOR).append("Grade Equivalent - Reading").append(SEPARATOR)
		.append("Grade Equivalent - Math Comp").append(SEPARATOR).append("Grade Equivalent - Applied Math").append(SEPARATOR)
		.append("Grade Equivalent - Language").append(SEPARATOR).append("Grade Equivalent - Total Math").append(SEPARATOR)
		.append("Grade Equivalent - Total Battery").append(SEPARATOR).append("NRS Level - Reading").append(SEPARATOR)
		.append("NRS Level - Math Comp").append(SEPARATOR).append("NRS Level - Applied Math").append(SEPARATOR)
		.append("NRS Level - Language").append(SEPARATOR).append("NRS Level - Total Math").append(SEPARATOR)
		.append("NRS Level - Total Battery").append(SEPARATOR).append("Percentage Mastery - Reading").append(SEPARATOR)
		.append("Percentage Mastery - Math Comp").append(SEPARATOR).append("Percentage Mastery - Applied Math").append(SEPARATOR)
		.append("Percentage Mastery - Language").append(SEPARATOR).append("Predicted GED - Average").append(SEPARATOR)
		.append("Predicted GED - Math").append(SEPARATOR).append("Predicted GED - Reading").append(SEPARATOR)
		.append("Predicted GED - Science").append(SEPARATOR).append("Predicted GED - Social Studies").append(SEPARATOR)
		.append("Predicted GED - Writing").append(SEPARATOR).append(contentDomains.substring(0, contentDomains.length() - 1));		
				
		writer.writeNext(headerRow.toString().split(SEPARATOR));
		try {
			StringBuilder row = new StringBuilder();
			for(TABEFile tabe: myList) {
				
				row = new StringBuilder();
				row.append(tabe.getCustomerID()).append(SEPARATOR).append(tabe.getOrgLevel1Name()).append(SEPARATOR)
				.append(tabe.getOrgLevel1Code()).append(SEPARATOR).append(tabe.getOrgLevel2Name()).append(SEPARATOR)
				.append(tabe.getOrgLevel2Code()).append(SEPARATOR).append(tabe.getOrgLevel3Name()).append(SEPARATOR)
				.append(tabe.getOrgLevel3Code()).append(SEPARATOR).append(tabe.getOrgLevel4Name()).append(SEPARATOR)
				.append(tabe.getOrgLevel4Code()).append(SEPARATOR).append(tabe.getGrade()).append(SEPARATOR)
				.append(tabe.getStudentID()).append(SEPARATOR).append(tabe.getStudentFirstName()).append(SEPARATOR)
				.append(tabe.getStudentLastName()).append(SEPARATOR).append(tabe.getStudentMiddleName()).append(SEPARATOR)
				.append(tabe.getStudentBirthDate()).append(SEPARATOR).append(tabe.getStudentGender()).append(SEPARATOR)
				.append(tabe.getStudentId2()).append(SEPARATOR).append(tabe.getEll()).append(SEPARATOR)
				.append(tabe.getEthnicity()).append(SEPARATOR).append(tabe.getFreeLunch()).append(SEPARATOR)
				.append(tabe.getIep()).append(SEPARATOR).append(tabe.getLep()).append(SEPARATOR)
				.append(tabe.getLaborForceStatus()).append(SEPARATOR).append(tabe.getMigrant()).append(SEPARATOR)
				.append(tabe.getSection504()).append(SEPARATOR).append(tabe.getDateTestingCompleted()).append(SEPARATOR)
				.append(tabe.getInterrupted()).append(SEPARATOR).append(tabe.getAbilityScores().getReadingAbilityScore()).append(SEPARATOR)
				.append(tabe.getAbilityScores().getReadingSEMScore()).append(SEPARATOR).append(tabe.getAbilityScores().getMathCompAbilityScore()).append(SEPARATOR)
				.append(tabe.getAbilityScores().getMathCompSEMScore()).append(SEPARATOR).append(tabe.getAbilityScores().getAppliedMathAbilityScore()).append(SEPARATOR)
				.append(tabe.getAbilityScores().getAppliedMathSEMScore()).append(SEPARATOR).append(tabe.getAbilityScores().getLanguageAbilityScore()).append(SEPARATOR)
				.append(tabe.getAbilityScores().getLanguageSEMScore()).append(SEPARATOR).append(tabe.getAbilityScores().getTotalMathAbilityScore()).append(SEPARATOR)
				.append(tabe.getAbilityScores().getTotalBatteryAbilityScore()).append(SEPARATOR).append(tabe.getGradeEquivalent().getReading()).append(SEPARATOR)
				.append(tabe.getGradeEquivalent().getMathComp()).append(SEPARATOR).append(tabe.getGradeEquivalent().getAppliedMath()).append(SEPARATOR)
				.append(tabe.getGradeEquivalent().getLanguage()).append(SEPARATOR).append(tabe.getGradeEquivalent().getTotalMath()).append(SEPARATOR)
				.append(tabe.getGradeEquivalent().getTotalBattery()).append(SEPARATOR).append(tabe.getNrsLevels().getReading()).append(SEPARATOR)
				.append(tabe.getNrsLevels().getMathComp()).append(SEPARATOR).append(tabe.getNrsLevels().getAppliedMath()).append(SEPARATOR)
				.append(tabe.getNrsLevels().getLanguage()).append(SEPARATOR).append(tabe.getNrsLevels().getTotalMath()).append(SEPARATOR)
				.append(tabe.getNrsLevels().getTotalBattery()).append(SEPARATOR).append(tabe.getPercentageMastery().getReading()).append(SEPARATOR)
				.append(tabe.getPercentageMastery().getMathComp()).append(SEPARATOR).append(tabe.getPercentageMastery().getAppliedMath()).append(SEPARATOR)
				.append(tabe.getPercentageMastery().getLanguage()).append(SEPARATOR).append(tabe.getPredictedGED().getAverage()).append(SEPARATOR)
				.append(tabe.getPredictedGED().getMath()).append(SEPARATOR).append(tabe.getPredictedGED().getReading()).append(SEPARATOR)
				.append(tabe.getPredictedGED().getScience()).append(SEPARATOR).append(tabe.getPredictedGED().getSocialStudies()).append(SEPARATOR)
				.append(tabe.getPredictedGED().getWriting()).append(SEPARATOR).append(tabe.getObjectiveRawScore())
				.append(tabe.getObjectiveTotalRawScore()).append(tabe.getObjectiveScaleScore())
				.append(tabe.getObjectiveScaleScoreSEM()).append(tabe.getObjectiveMasteryLevel())
				.append(tabe.getObjectiveMastery()).append(tabe.getItemResponse());
				
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
			getAllContentDomain(oascon);
			getAllObjectives(oascon);
			customerDemoList = getCustomerDemographic(oascon);
			Set<CustomerDemographic> set = new HashSet<CustomerDemographic>(
					customerDemoList);

			for (CustomerDemographic c : set) {

				customerDemographic.put(c.getCustomerDemographicId(), c
						.getLabelName());
			}
			myrosterList = getTestRoster(oascon);
			int count = 0;
			for (TestRoster roster : myrosterList) {
				TABEFile catData = new TABEFile();
				
				Student studentInfo = roster.getStudent();
				fillStudent(catData, studentInfo);
				catData.setCustomerID(CUSTOMER_ID.toString());				
				//	catData.setDateTestingCompleted(roster.getDateTestingCompleted());
				if (roster.getLastMseq() > 1000000 || roster.getRestartNumber() > 1){
					catData.setInterrupted("1");
				}else{
					catData.setInterrupted("0");
				}
				createOrganization(oascon, catData, roster.getStudentId());
				fillAccomodations(studentInfo.getStudentDemographic(), customerDemographic, catData);
				createAbilityScoreInformation(irscon,catData,roster);
				getSemScores(oascon, catData, roster,catData.getAbilityScores());
				fillObjective(oascon, catData, roster);
				prepareItemResponses(oascon, catData, roster);				
				tabeFileList.add(catData);	
				System.out.println("Record Processed: " + ++count);
			}
			System.out.println("Total records: " + count);
		} finally {
			SqlUtil.close(oascon);
			SqlUtil.close(irscon);
		}
		return tabeFileList;
	}
	
	
	private List<CustomerDemographic> getCustomerDemographic(Connection con)
	throws SQLException {
		System.out.println("getCustomerDemographic start");
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
            System.out.println("getCustomerDemographic End");
		} finally {
			SqlUtil.close(ps, rs);
		}

		return myList;
	}
	
	private Set<CustomerDemographicValue> getCustomerDemographicValue(
			Connection con, int customerDemographicId) throws SQLException {
		System.out.println("getCustomerDemographicValue start");
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

			System.out.println("getCustomerDemographicValue End");
		} finally {
			SqlUtil.close(ps, rs);
		}

		return customerDemographicValue;

	}
	
	private List<TestRoster> getTestRoster(Connection con) throws SQLException {
		PreparedStatement ps = null ;
		ResultSet rs = null;
		 List<TestRoster> rosterList = new ArrayList<TestRoster>();
		try{
			ps = con.prepareStatement(SQLQuery.testRosterSql);
			ps.setString(1, CUSTOMER_ID);
			ps.setInt(2, PRODUCT_ID);
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
				//ros.setDateTestingCompleted(Utility.getTimeZone(rs.getString(7).toString(),rs.getString(8).toString(),true));
				ros.setRestartNumber(rs.getInt(9));
				ros.setLastMseq(rs.getInt(10));
				ros.setStartDate(rs.getString(11));
				ros.setStudent(getStudent(con,rs.getInt(5))); 
				ros.setTimeZone(rs.getString(8));
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
				std.setBirthDate(rs.getDate(5));
				std.setGender(rs.getString(6) != null ? rs.getString(6) : "");
				std.setGrade(rs.getString(7) != null ? rs.getString(7) : "");
				std.setCustomerId(Integer.valueOf(CUSTOMER_ID));
				std.setTestPurpose(rs.getString(9) != null ? rs.getString(9) : "");
				std.setExtStudentId(rs.getString(10) != null ? rs.getString(10) : "");
				std.setStudentDemographic(getStudentDemographic(con,
						studentId));
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

	private void createOrganization(Connection con, TABEFile tfil,
			Integer studentId)
	throws SQLException {

		System.out.println("Create Organization Start");
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
			System.out.println("Create Organization End");
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
		tfil.setStudentId2(st.getExtStudentId());
		tfil.setGrade(Utility.formatGrade(st.getGrade()));
		tfil.setStudentBirthDate(st.getBirthDate());

	}
	
	private void fillAccomodations(Set<StudentDemographic> sd,
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
			ps.setInt(1, roster.getStudentId());
			ps.setInt(2, roster.getTestAdminId());
			rs = ps.executeQuery();
			while(rs.next()) {
				if (rs.getString(1).toString().equalsIgnoreCase("Mathematics Computation")){
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
				
				} else if(rs.getString(1).toString().equalsIgnoreCase("Language")){
					abilityScore.setLanguageAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setLanguage(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setLanguage(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setLanguage(rs.getString(5) != null ? rs.getString(5) : "");				
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
		String testCompleted = "";
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
				testCompleted = rs.getString(3) != null ? rs.getString(3) : roster.getStartDate();
			} 
		} finally {
			SqlUtil.close(ps, rs);
		}
		tfil.setDateTestingCompleted(Utility.getTimeZone(testCompleted, roster.getTimeZone(), true));
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
					for(String objective: objectives) {
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

	private void prepareItemResponses(Connection con, TABEFile tfil,
	   TestRoster roster) throws IOException, Exception {
		   PreparedStatement ps = null ;
		   ResultSet rs = null;
		   PreparedStatement ps1 = null ;
		   ResultSet rs1 = null;
		   int timedOut = 0;
		   Map<String, ItemResponses> itemMap = new LinkedHashMap<String, ItemResponses>();
		   StringBuilder response = new StringBuilder();
		   try {
			   getContentDomain(con, roster.getTestRosterId());
			   Set<String> keySet = CONTENT_DOMAINS.keySet();
			   int itemCount = 0;
			   for(String itemSetName: keySet) {
				   StringBuilder itemIds = new StringBuilder();
				   StringBuilder itemResponse = new StringBuilder();
				   StringBuilder itemOrgResponse = new StringBuilder();
				   StringBuilder resposneTime = new StringBuilder();
				   Integer itemSetId = CONTENT_DOMAINS.get(itemSetName);
				   if("Reading".equals(itemSetName)) {
					   response.append("RD" + SEPARATOR);
					   itemCount = 29;
				   } else if("Mathematics Computation".equals(itemSetName)) {
					   response.append("MC" + SEPARATOR);
					   itemCount = 24;
				   } else if("Applied Mathematics".equals(itemSetName)) {
					   response.append("AM" + SEPARATOR);
					   itemCount = 29;
				   } else if("Language".equals(itemSetName)) {
					   response.append("LN" + SEPARATOR);
					   itemCount = 29;
				   }
				   ITEM_COUNT_MAP.put(itemSetName, itemCount);
				   ps = con.prepareStatement(SQLQuery.ALL_ITEMS_DETAILS_SQL);
				   ps.setInt(1, roster.getTestRosterId());
				   ps.setInt(2, itemSetId);
				   ps.setInt(3, roster.getTestRosterId());
				   ps.setInt(4, itemSetId);
				   rs = ps.executeQuery();
				   int index = 1;
				   int currentSequenceNo = 0;
				   int lastSequenceNo = 0;
				   int restartItemNumber = 0;
				   String lastItemId = "";
				   while (rs.next()) {
					  ItemResponses ir = new ItemResponses();
					  currentSequenceNo = rs.getInt("maxseqnum");
					  ir.setItemId(rs.getString("item_id"));
					  ir.setResponseValue(rs.getString("response"));
					  ir.setOriginalResponse(rs.getString("original_response"));
					  ir.setResponseTime(rs.getString("response_elapsed_time"));
					  ir.setSequenceNo(currentSequenceNo);
					  ir.setIndex(index++);	
					 // stoppedItem = rs.getString("item_id");
					  if(currentSequenceNo > lastSequenceNo + 100000) {
						  ps1 = con.prepareStatement(SQLQuery.GET_ITEM_RESPONSE_FOR_ITEM);
						  ps1.setInt(1, roster.getTestRosterId());
						  ps1.setInt(2, itemSetId);
						  ps1.setLong(3, currentSequenceNo - 100000);
						  rs1 = ps1.executeQuery();
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
				   }
				   SqlUtil.close(ps, rs);
				    
				   Set<String> itemSet = itemMap.keySet();
				   List<String> itemList = new ArrayList<String>(itemSet);
				   for(int i=0; i<itemCount; i++) {
					   if(i < itemList.size()) {
						   ItemResponses ir = itemMap.get(itemList.get(i));
						   itemIds.append(ir.getItemId().trim() + SEPARATOR);
						   itemResponse.append(ir.getResponseValue().trim() + SEPARATOR);
						   itemOrgResponse.append(ir.getOriginalResponse().trim() + SEPARATOR);
						   resposneTime.append(ir.getResponseTime().trim() + SEPARATOR);
					   } else {
						   itemIds.append(SPACE + SEPARATOR);
						   itemResponse.append(SPACE + SEPARATOR);
						   itemOrgResponse.append(SPACE + SEPARATOR);
						   resposneTime.append(SPACE + SEPARATOR);
					   }
				   }
				   
				   String stoppedItem = String.valueOf(itemList.size());
				   timedOut = getTimedOut(con, tfil, roster, itemSetId);
				   if(timedOut == 0) {
					   if(itemList.size() < itemCount) {
						   timedOut = 2;
					   } else {
						   stoppedItem = "";
					   }
				   }
				   response.append(itemIds.substring(0, itemIds.length() - 1)+ SEPARATOR
						   		 + itemResponse + itemOrgResponse 
						   		 + resposneTime.substring(0, resposneTime.length() - 1) + SEPARATOR);
				   
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
			ps = con.prepareStatement(SQLQuery.ALL_OBJECTIVE_SQL);
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
			ps = con.prepareStatement(SQLQuery.CONTENT_DOMAIN_FOR_ROSTER_SQL);
			ps.setInt(1, testRosterId);
			rs = ps.executeQuery();
			while(rs.next()) {
				CONTENT_DOMAINS.put(rs.getString("item_set_name"), rs.getInt("item_set_id"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
	}
	
	private void getAllContentDomain(Connection con) 
    throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SQLQuery.GET_ALL_CONTENT_DOMAIN_SQL);
			ps.setInt(1, PRODUCT_ID);
			rs = ps.executeQuery();
			while(rs.next()) {
				CONTENT_DOMAINS.put(rs.getString("item_set_name"), rs.getInt("item_set_id"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
	}
	
	private int getTimedOut(Connection con, TABEFile tfil, TestRoster roster, Integer itemSetId) 
	   throws SQLException {
			PreparedStatement ps = null;
			ResultSet rs = null;
			long totalTestTime = 0L;
			long totalTimeTaken = 0L;
			int timedOut = 0;
			try {
				ps = con.prepareStatement(SQLQuery.TOTAL_TEST_TIME_SQL);
				ps.setInt(1, itemSetId);
				ps.setInt(2, roster.getTestRosterId());
				rs = ps.executeQuery();
				if(rs.next()) {
					totalTestTime = rs.getLong("total_time");
				}
				SqlUtil.close(ps, rs);
				ps = con.prepareStatement(SQLQuery.TOTAL_TIME_TAKEN_SQL);
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
}