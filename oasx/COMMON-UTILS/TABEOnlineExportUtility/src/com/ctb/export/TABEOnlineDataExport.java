package com.ctb.export;

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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import au.com.bytecode.opencsv.CSVWriter;

import com.ctb.dto.CustomerDemographic;
import com.ctb.dto.CustomerDemographicValue;
import com.ctb.dto.ItemResponses;
import com.ctb.dto.Scores;
import com.ctb.dto.Student;
import com.ctb.dto.StudentDemographic;
import com.ctb.dto.TABEFile;
import com.ctb.dto.TestRoster;
import com.ctb.utils.ExtractUtil;
import com.ctb.utils.SQLQuery;
import com.ctb.utils.SqlUtil;
import com.ctb.utils.Utility;

public class TABEOnlineDataExport {
	
	private static final Integer CUSTOMER_ID = Integer.valueOf(ExtractUtil.getDetail("oas.customerId"));
	private static final Integer PRODUCT_ID = Integer.valueOf(ExtractUtil.getDetail("oas.productId"));
	//private static final String USER_DIR = System.getProperty("user.dir").toLowerCase();
	private static final String LOCAL_FILE_PATH = ExtractUtil.getDetail("oas.exportdata.filepath");
	private static final String FILE_NAME = ExtractUtil.getDetail("oas.exportdata.fileName");
	private static final String SEPARATOR = "#";
	private static final List<String> CONTENT_DOMAIN_LIST = new ArrayList<String>();
	private static final Map<String, List<ItemResponses>> ITEM_SET_MAP = new HashMap<String, List<ItemResponses>>();
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TABEOnlineDataExport dataExport = new TABEOnlineDataExport();
		try {
			dataExport.writeToText();
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeToText() throws Exception {

		List<TABEFile> myList = createList();
		String modFileName = FILE_NAME + "_" + System.currentTimeMillis() + ExtractUtil.getDetail("oas.exportdata.fileType");
		if(!(new File(LOCAL_FILE_PATH)).exists()){
			File f = new File(LOCAL_FILE_PATH);
			f.mkdirs();
		}
		File file = new File(LOCAL_FILE_PATH, modFileName);
		CSVWriter writer = new CSVWriter(new FileWriter(file));
		StringBuilder headerRow = new StringBuilder();
		StringBuilder contentDomains = new StringBuilder();
		//StringBuilder items = new StringBuilder();
		for (String contentDomain: CONTENT_DOMAIN_LIST) {
			contentDomains.append(contentDomain).append("(Level, Raw Score, Scale Score)").append(SEPARATOR);
		}
		/*for (ItemResponses item: ITEM_LIST) {
			items.append(item.getItemId()).append(SEPARATOR);
		}*/
		headerRow.append("Customer Id").append(SEPARATOR).append("State Name").append(SEPARATOR)
		.append("State Code").append(SEPARATOR).append("District Name").append(SEPARATOR)
		.append("District Code").append(SEPARATOR).append("School Name").append(SEPARATOR)
		.append("School Code").append(SEPARATOR).append("Class Name").append(SEPARATOR)
		.append("Class Code").append(SEPARATOR).append("Student Id").append(SEPARATOR)
		.append("Student Id 2").append(SEPARATOR).append("First Name").append(SEPARATOR)
		.append("Middle Name").append(SEPARATOR).append("Last Name").append(SEPARATOR)
		.append("Gender").append(SEPARATOR).append("Ethenicity").append(SEPARATOR)
		.append("ELL").append(SEPARATOR).append("Free Lunch").append(SEPARATOR)
		.append("IEP").append(SEPARATOR).append("LEP").append(SEPARATOR)
		.append("Labor Force").append(SEPARATOR).append("Migrant").append(SEPARATOR)
		.append("Section 504").append(SEPARATOR)
		.append("Interrupted").append(SEPARATOR).append("Test Form Id").append(SEPARATOR)
		.append("Last Item").append(SEPARATOR).append("Timed Out").append(SEPARATOR)
		.append("Scale Vector Response").append(SEPARATOR)
		.append(contentDomains.substring(0, contentDomains.length() - 1));
		
		writer.writeNext(headerRow.toString().split(SEPARATOR));
		try {
			StringBuilder row = new StringBuilder();
			List<String[]> rows = new ArrayList<String[]>();
			for(TABEFile tabe: myList) {
				
				row.append(tabe.getCustomerID()).append(SEPARATOR).append(tabe.getOrgLevel1Name()).append(SEPARATOR)
				.append(tabe.getOrgLevel1Code()).append(SEPARATOR).append(tabe.getOrgLevel2Name()).append(SEPARATOR)
				.append(tabe.getOrgLevel2Code()).append(SEPARATOR).append(tabe.getOrgLevel3Name()).append(SEPARATOR)
				.append(tabe.getOrgLevel3Code()).append(SEPARATOR).append(tabe.getOrgLevel4Name()).append(SEPARATOR)
				.append(tabe.getOrgLevel4Code()).append(SEPARATOR).append(tabe.getStudentID()).append(SEPARATOR)
				.append(tabe.getStudentId2()).append(SEPARATOR).append(tabe.getStudentFirstName()).append(SEPARATOR)
				.append(tabe.getStudentMiddleName()).append(SEPARATOR).append(tabe.getStudentLastName()).append(SEPARATOR)
				.append(tabe.getStudentGender()).append(SEPARATOR).append(tabe.getEthnicity()).append(SEPARATOR)
				.append(tabe.getEll()).append(SEPARATOR).append(tabe.getFreeLunch()).append(SEPARATOR)
				.append(tabe.getIep()).append(SEPARATOR).append(tabe.getLep()).append(SEPARATOR)
				.append(tabe.getLaborForceStatus()).append(SEPARATOR).append(tabe.getMigrant()).append(SEPARATOR)
				.append(tabe.getSection504()).append(SEPARATOR)
				.append(tabe.getInterrupted()).append(SEPARATOR).append(tabe.getTestFormId()).append(SEPARATOR)
				.append(tabe.getLastItem()).append(SEPARATOR).append(tabe.getTimedOut()).append(SEPARATOR)
				.append(tabe.getScaleVectorResponse()).append(SEPARATOR).append(tabe.getScores());
				
				rows.add(row.toString().split(SEPARATOR));
				row = new StringBuilder();
			}
			writer.writeAll(rows);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}
	private List<TABEFile> createList() throws Exception {
		List<TABEFile> tabeFileList = new ArrayList<TABEFile>();
		List<TestRoster> myrosterList = new ArrayList<TestRoster>();
		List<CustomerDemographic> customerDemoList = new ArrayList<CustomerDemographic>();
		HashMap<String, Integer> stateMap = new HashMap<String, Integer>();
		HashMap<String, Integer> districtMap = new HashMap<String, Integer>();
		HashMap<String, Integer> schoolMap = new HashMap<String, Integer>();
		HashMap<String, Integer> classMap = new HashMap<String, Integer>();
		HashMap<Integer, String> customerDemographic = new HashMap<Integer, String>();
		Connection oascon = null;
		Connection irscon = null;
		
		try {
			oascon = SqlUtil.openOASDBconnectionForResearch();
			irscon = SqlUtil.openIRSDBconnectionForResearch();
			
			//getAllItems();
			getAllContentDomain(oascon);
			
			customerDemoList = getCustomerDemographic(oascon);
			Set<CustomerDemographic> set = new HashSet<CustomerDemographic>(customerDemoList);
			for (CustomerDemographic c : set) {
				customerDemographic.put(c.getCustomerDemographicId(), c.getLabelName());
			}
			myrosterList = getTestRoster(oascon);
			for (TestRoster roster : myrosterList) {
				TABEFile catData = new TABEFile();
				
				catData.setCustomerID(CUSTOMER_ID.toString());				
				//	catData.setDateTestingCompleted(roster.getDateTestingCompleted());
				if (roster.getLastMseq() > 1000000 || roster.getRestartNumber() > 1){
					catData.setInterrupted("0");
				}else{
					catData.setInterrupted("1");
				}
				catData.setTestFormId(roster.getTestFormId());
				catData.setTestLevel(roster.getTestLevel());
				
				Student studentInfo = roster.getStudent();
				fillStudent(catData, studentInfo);
				createOrganization(oascon, catData, roster.getStudentId(),
						stateMap,districtMap, schoolMap, classMap);
				fillAccomodations(studentInfo.getStudentDemographic(), customerDemographic, catData);
				getScores(oascon, irscon, catData, roster);
				getScaleVectorResponse(oascon, catData, roster);
				getTimedOut(oascon, catData, roster);
				//createAbilityScoreInformation(irscon,catData,roster);
				//getSemScores(oascon, catData, roster,catData.getAbilityScores());
				//fillObjective(irscon,catData,roster);
				tabeFileList.add(catData);								
			}
		} catch(Exception e) {
			e.printStackTrace();
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
			ps.setInt(1, CUSTOMER_ID);
			rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDemographic cd = new CustomerDemographic();
				cd.setCustomerDemographicId(rs.getInt(1));
				cd.setCustomerId(rs.getInt(2));
				cd.setLabelName(rs.getString(3));
				cd.setCustomerDemographicValue(getCustomerDemographicValue(con, rs.getInt(1)));
				myList.add(cd);
			}
            System.out.println("getCustomerDemographic End");
		} catch(Exception e) {
			e.printStackTrace();
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
		} catch(Exception e) {
			e.printStackTrace();
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
			ps.setInt(1, CUSTOMER_ID);
			ps.setInt(2, PRODUCT_ID);
			rs = ps.executeQuery(); 
			rs.setFetchSize(500);
			while (rs.next()){
				TestRoster ros = new TestRoster();
				ros.setTestRosterId(rs.getInt(1));
				ros.setActivationStatus(rs.getString(2));
				ros.setTestCompletionStatus(rs.getString(3));
				ros.setCustomerId(CUSTOMER_ID);
				ros.setStudentId(rs.getInt(5));
				ros.setTestAdminId(rs.getInt(6));
				//ros.setDateTestingCompleted(Utility.getTimeZone(rs.getString(7).toString(),rs.getString(8).toString(),true));
				ros.setRestartNumber(rs.getInt(9));
				ros.setLastMseq(rs.getInt(10));
				ros.setStartDate(rs.getString(11));
				ros.setStudent(getStudent(con,rs.getInt(5))); 
				ros.setTimeZone(rs.getString(8));
				ros.setTestFormId(rs.getString("testForm"));
				ros.setTestLevel(rs.getString("testLevel"));
				rosterList.add(ros);
			}
		} catch(Exception e) {
			e.printStackTrace();
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
				std.setFirstName(rs.getString(2));
				std.setLastName(rs.getString(3));
				std.setMiddleName(rs.getString(4));
				std.setBirthDate(rs.getDate(5));
				std.setGender(rs.getString(6));
				std.setGrade(rs.getString(7));
				std.setCustomerId(CUSTOMER_ID);
				std.setTestPurpose(rs.getString(9));
				std.setExtStudentId(rs.getString(10));
				std.setStudentDemographic(getStudentDemographic(con, studentId));
			}
		} catch(Exception e) {
			e.printStackTrace();
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

		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
		return studentDemographicSet;
	}

	private void createOrganization(Connection con, TABEFile tfil,
			Integer studentId,HashMap<String, Integer> stateMap,
			HashMap<String, Integer> districtMap,
			HashMap<String, Integer> schoolMap,
			HashMap<String, Integer> classMap)
	throws SQLException {

		System.out.println("Create Organization Start");
		TreeMap<Integer, String> organizationMap = new TreeMap<Integer, String>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SQLQuery.sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			
			/*if we take the category level there are chances that the customer will have only three 
			levels instead of the usual four so
			trying to create a map instead that will give the exact
			number of organizations from which we can detect the levels*/
			while (rs.next()) {
				if (rs.getString(4).equalsIgnoreCase("root")
						|| rs.getString(4).equalsIgnoreCase("CTB")) {
				} else {
					organizationMap.put(new Integer(rs.getString(5)), rs.getString(4));
				}
			}
			Integer organizationMapSize = organizationMap.size();
			rs.beforeFirst();
			while (rs.next()) {
				if (rs.getString(4).equalsIgnoreCase("root")
						|| rs.getString(4).equalsIgnoreCase("CTB")) {
				} else if (rs.getString(5) != null
						 && Integer.valueOf(organizationMapSize - 3).toString() != null
						&& rs.getString(5)
						.equalsIgnoreCase(
								Integer.valueOf(organizationMapSize - 3)
								.toString())) {
					tfil.setOrgLevel1Name(rs.getString(4));
					tfil.setOrgLevel1Code(rs.getString(3));

				} else if (rs.getString(5) != null
						 && Integer.valueOf(organizationMapSize - 2).toString() != null
							&& rs.getString(5)
							.equalsIgnoreCase(
									Integer.valueOf(organizationMapSize - 2)
									.toString())) {
					tfil.setOrgLevel2Name(rs.getString(4));
					tfil.setOrgLevel2Code(rs.getString(3));
				}
				else if (rs.getString(5) != null
						 && Integer.valueOf(organizationMapSize - 1).toString() != null
							&& rs.getString(5)
							.equalsIgnoreCase(
									Integer.valueOf(organizationMapSize - 1)
									.toString())) {
					tfil.setOrgLevel3Name(rs.getString(4));
					tfil.setOrgLevel3Code(rs.getString(3));
				}
				else if (rs.getString(5) != null
						&& rs.getString(5).equalsIgnoreCase(organizationMapSize.toString())) {
					tfil.setOrgLevel4Name(rs.getString(4));
					tfil.setOrgLevel4Code(rs.getString(3));
				}
			}
			if(tfil.getOrgLevel3Name() == null){
				tfil.setOrgLevel3Name(tfil.getOrgLevel4Name());
				tfil.setOrgLevel3Code(tfil.getOrgLevel4Code());
				
			}else if(tfil.getOrgLevel2Name() == null){
				tfil.setOrgLevel2Name(tfil.getOrgLevel3Name());
				tfil.setOrgLevel2Code(tfil.getOrgLevel3Code());
				
			}else if(tfil.getOrgLevel1Name() == null){
				tfil.setOrgLevel1Name(tfil.getOrgLevel2Name());
				tfil.setOrgLevel1Code(tfil.getOrgLevel2Code());				
			}
			System.out.println("Create Organization End");
		} catch(Exception e) {
			e.printStackTrace();
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
		tfil.setTestPurpose(st.getTestPurpose());
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
			if (customerDemographic.containsKey(studentDem.getCustomerDemographicId())) {
				String customerDemoName = customerDemographic.get(studentDem.getCustomerDemographicId());

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
	
	

	/*private void createAbilityScoreInformation(Connection con, TABEFile tfil, TestRoster roster) throws SQLException{
		TreeMap<String, Object[]> treeMap = new TreeMap<String, Object[]>();
		HashMap<String,String> contentAreaFact = new HashMap<String, String>();
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
			
				}else{
					abilityScore.setTotalBatteryAbilityScore(rs2.getString(2) != null ? rs2.getString(2) : "" );
					gradeEquivalent.setTotalBattery(rs2.getString(3) != null ? rs2.getString(3) : "");
					nrsLevel.setTotalBattery(rs2.getString(4) != null ? rs2.getString(4) : "");					
				}

			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps2, rs2);
		}
		
		try{
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
			
				}else if(rs.getString(1).toString().equalsIgnoreCase("Reading")){
					abilityScore.setReadingAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setReading(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setReading(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setReading(rs.getString(5) != null ? rs.getString(5) : "");	
					
				}else if(rs.getString(1).toString().equalsIgnoreCase("Applied Mathematics")){
					abilityScore.setAppliedMathAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setAppliedMath(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setAppliedMath(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setAppliedMath(rs.getString(5) != null ? rs.getString(5) : "");				
				
				}else if(rs.getString(1).toString().equalsIgnoreCase("Language")){
					abilityScore.setLanguageAbilityScore(rs.getString(2) != null ? rs.getString(2) : "" );
					gradeEquivalent.setLanguage(rs.getString(3) != null ? rs.getString(3) : "" );
					nrsLevel.setLanguage(rs.getString(4) != null ? rs.getString(4) : "" );
					percentMast.setLanguage(rs.getString(5) != null ? rs.getString(5) : "");				
				}

			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally{
			SqlUtil.close(ps, rs);
		}
		try{
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
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally{
			SqlUtil.close(ps3, rs3);
		}
		
		tfil.setAbilityScores(abilityScore);
		tfil.setGradeEquivalent(gradeEquivalent);
		tfil.setNrsLevels(nrsLevel);
		tfil.setPercentageMastery(percentMast);	
		tfil.setPredictedGED(predictedGed);
	}
	
	private void getSemScores(Connection con, TABEFile tfil, TestRoster roster, AbilityScore abilityScore) throws SQLException{

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
					abilityScore.setMathCompSEMScore(rs.getString(2) != null ? rs.getString(2) : "" );			
				}else if(rs.getString(1).toString().equalsIgnoreCase("Reading")) {
					abilityScore.setReadingSEMScore(rs.getString(2) != null ? rs.getString(2) : "" );				
				}else if(rs.getString(1).toString().equalsIgnoreCase("Applied Mathematics")) {
					abilityScore.setAppliedMathSEMScore(rs.getString(2) != null ? rs.getString(2) : "" );				
				}else if(rs.getString(1).toString().equalsIgnoreCase("Language")) {
					abilityScore.setLanguageSEMScore(rs.getString(2) != null ? rs.getString(2) : "" );				
				}
				testCompleted = rs.getString(3) != null ? rs.getString(3) : roster.getStartDate();
				
			} 

		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
		
		tfil.setDateTestingCompleted(Utility.getTimeZone(testCompleted, roster.getTimeZone(), true));
		tfil.setAbilityScores(abilityScore);	
	}
	
	private void fillObjective(Connection con, TABEFile tfil, TestRoster roster) throws SQLException, Exception{

		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<String, ObjectiveObj> objectiveMap =  new HashMap<String, ObjectiveObj>();
		ObjectiveLevel objLevel =  new ObjectiveLevel();
		ObjectiveMastery objMastery =  new ObjectiveMastery();
		Method[] objectiveLevelMethods = ObjectiveLevel.class.getMethods();
		Method[] objectiveMasteryMethods = ObjectiveMastery.class.getMethods();
		ObjectiveObj objectiveObject = new ObjectiveObj();
		
		try{
			ps = con.prepareStatement(SQLQuery.getObjectiveScores);
			ps.setInt(1, roster.getStudentId());
			ps.setInt(2, roster.getTestAdminId());
			rs = ps.executeQuery();

			while(rs.next()) {
				if (rs.getString(1) != null){
					ObjectiveObj objective =  new ObjectiveObj();
					objective.setObjectiveLevel(rs.getString(2) != null ? rs.getString(2) : "");
					objective.setObjectiveMastery(rs.getString(3) != null ? rs.getString(3) : "");
					String tempStr = rs.getString(1);
					String[] tempStrArray = tempStr.split(" ");
					if (tempStrArray.length > 0){
						tempStrArray = tempStrArray[0].split("/");
					}
					if (tempStrArray.length > 0){
						tempStrArray = tempStrArray[0].split(",");
					}
											
					objectiveMap.put(tempStrArray[0].toLowerCase(),objective);
				}				
			} 
			
			for (Method method: objectiveLevelMethods){
				if (isSetter(method)){
					String key = method.getName().substring(3).toLowerCase();
					if(objectiveMap.containsKey(key)){
						objectiveObject = objectiveMap.get(key);
						method.invoke(objLevel, objectiveObject.getObjectiveLevel());
					}
				}
			}
			for (Method method: objectiveMasteryMethods){
				if (isSetter(method)){
					String key = method.getName().substring(3).toLowerCase();
					if(objectiveMap.containsKey(key)){
						objectiveObject = objectiveMap.get(key);
						method.invoke(objMastery, objectiveObject.getObjectiveMastery());
					}
				}
			}
			tfil.setObjectiveLevel(objLevel);
			tfil.setObjectiveMastery(objMastery);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally{
			SqlUtil.close(ps, rs);
		}
	}	

	private static boolean isSetter(Method method){
	  if(!method.getName().startsWith("set")) return false;
	  if(method.getParameterTypes().length != 1) return false;
	  return true;
	}*/
	
	private static void prepareItemResponses(Connection con, String itemSetId,
			TABEFile tfil, TestRoster roster) throws IOException, Exception {
		
		   PreparedStatement ps = null ;
		   ResultSet rs = null;
		   HashMap<String, ItemResponses> itemMap = new HashMap<String, ItemResponses>();
		   StringBuilder response = new StringBuilder();
		   try {
				ps = con.prepareStatement(SQLQuery.ALL_ITEMS_DETAILS_SQL);
				ps.setInt(1, roster.getTestRosterId());
				ps.setString(2, itemSetId);
				ps.setInt(3, roster.getTestRosterId());
				rs = ps.executeQuery();
				while (rs.next()){
					ItemResponses ir = new ItemResponses();
					ir.setItemId(rs.getString(1));
					ir.setResponseValue(rs.getString(2));
					itemMap.put(ir.getItemId(), ir);
					tfil.setLastItem(rs.getString(1));
				}
				//Getting first time visit data
				ps = con.prepareStatement(SQLQuery.ITEM_FIRST_TIME_VISIT_SQL);
				ps.setInt(1, roster.getTestRosterId());
				ps.setString(2, itemSetId);
				ps.setInt(3, roster.getTestRosterId());
				rs = ps.executeQuery();
				while (rs.next()){
					ItemResponses ir = itemMap.get(rs.getString(1));
					ir.setFirstVisitTime(rs.getString(2));
				}
				//Getting total time visit data
				ps = con.prepareStatement(SQLQuery.ITEM_TOTAL_TIME_VISIT_SQL);
				ps.setInt(1, roster.getTestRosterId());
				ps.setString(2, itemSetId);
				rs = ps.executeQuery();
				while (rs.next()){
					ItemResponses ir = itemMap.get(rs.getString(1));
					ir.setTotalTime(rs.getString(3));
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				SqlUtil.close(ps, rs);
			}
			List<ItemResponses> items = ITEM_SET_MAP.get(itemSetId);
			for(ItemResponses item: items) {
				ItemResponses ir = itemMap.get(item.getItemId());
				if(ir != null) {
					response.append(item.getItemOrder() + "," + Utility.formatData(ir.getFirstVisitTime()) + 
									"," + Utility.formatData(ir.getTotalTime()) + SEPARATOR);	
				} else {
					response.append(item.getItemOrder() + ",," + SEPARATOR);
				}
			}
			if(response.length() > 1)
				tfil.setItemResponse(response.substring(0, response.length() - 1));
			else 
				tfil.setItemResponse(response.toString());
	}
	
	private void getScores(Connection oascon, Connection irscon, 
						   TABEFile tfil, TestRoster roster) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, Scores> scoreMap = new LinkedHashMap<String, Scores>();
		StringBuilder scores = new StringBuilder();
		try {
			//Getting content domain
			ps = oascon.prepareStatement(SQLQuery.CONTENT_DOMAIN_SQL);
			ps.setInt(1, roster.getTestRosterId());
			rs = ps.executeQuery();
			while(rs.next()) {
				Scores score = new Scores();
				score.setItemSetId(rs.getString("item_set_id"));
				score.setContentAreaId(PRODUCT_ID + rs.getString("item_set_id"));
				score.setItemSetName(rs.getString("item_set_name"));
				score.setLevel(rs.getString("item_set_level"));
				score.setRawScore(rs.getString("raw_score"));
				scoreMap.put(score.getContentAreaId(), score);
			}
			//Getting scale score
			ps = irscon.prepareStatement(SQLQuery.SCALE_SCORE_SQL);
			ps.setInt(1, roster.getStudentId());
			ps.setInt(2, roster.getTestAdminId());
			rs = ps.executeQuery();
			while(rs.next()) {
				Scores score = scoreMap.get(rs.getString("content_areaid"));
				if(score != null) {
					score.setScaleScore(rs.getString("scale_score"));
				}
			}
			Set<String> keySet = scoreMap.keySet();
			for(Iterator<String> itr = keySet.iterator(); itr.hasNext();) {
				Scores score = scoreMap.get(itr.next());
				if(ITEM_SET_MAP.get(score.getItemSetId()) == null) {
					getAllItemsForItemSet(oascon, score.getItemSetId());
				}
				prepareItemResponses(oascon, score.getItemSetId(), tfil, roster);
				scores.append(Utility.formatData(score.getLevel()) + "," + Utility.formatData(score.getRawScore())
					 	  + "," + Utility.formatData(score.getScaleScore()) + SEPARATOR 
					 	  + tfil.getItemResponse() + SEPARATOR);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
		if(scores.length() > 1)
			tfil.setScores(scores.substring(0, scores.length() - 1));
		else 
			tfil.setScores(scores.toString());
	}
	
	private void getScaleVectorResponse(Connection oascon, 
			   TABEFile tfil, TestRoster roster) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder response = new StringBuilder();
		try {
			//Getting content domain
			ps = oascon.prepareStatement(SQLQuery.SCORED_RESPONSE_VECTOR_SQL);
			ps.setInt(1, roster.getTestRosterId());
			ps.setInt(2, roster.getTestRosterId());
			rs = ps.executeQuery();
			while(rs.next()) {
				response.append(rs.getString("response") + ",");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
		if(response.length() > 0)
			tfil.setScaleVectorResponse(response.substring(0, response.length() - 1));
		else
			tfil.setScaleVectorResponse(response.toString());
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
   
   private void getAllContentDomain(Connection con) 
   throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SQLQuery.GET_ALL_CONTENT_DOMAIN_SQL);
			ps.setInt(1, PRODUCT_ID);
			rs = ps.executeQuery();
			while(rs.next()) {
				CONTENT_DOMAIN_LIST.add(rs.getString("item_set_name"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
   }
   
   private void getAllItemsForItemSet(Connection con, String itemSetId) {
	    PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			List<ItemResponses> items = new ArrayList<ItemResponses>();
			ps = con.prepareStatement(SQLQuery.GET_ITEMS_FOR_ITEM_SET_SQL);
			ps.setString(1, itemSetId);
			rs = ps.executeQuery();
			while(rs.next()) {
				ItemResponses ir = new ItemResponses();
				ir.setItemId(rs.getString("item_id"));
				ir.setItemOrder(rs.getString("item_sort_order"));
				items.add(ir);
			}
			ITEM_SET_MAP.put(itemSetId, items);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
   }
}