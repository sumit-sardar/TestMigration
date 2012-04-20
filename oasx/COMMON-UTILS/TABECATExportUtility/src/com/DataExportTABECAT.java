package com;

import java.io.File;
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

import org.ffpojo.exception.FFPojoException;
import org.ffpojo.file.writer.FileSystemFlatFileWriter;
import org.ffpojo.file.writer.FlatFileWriter;

import com.ctb.dto.AbilityScore;
import com.ctb.dto.CustomerDemographic;
import com.ctb.dto.CustomerDemographicValue;
import com.ctb.dto.GradeEquivalent;
import com.ctb.dto.ItemResponses;
import com.ctb.dto.NRSLevels;
import com.ctb.dto.ObjectiveLevel;
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
	private static String userDir = System.getProperty("user.dir").toLowerCase();
	private static final String FILE_TYPE = ExtractUtil.getDetail("oas.exportdata.fileType");
	private static final String FILE_NAME = ExtractUtil.getDetail("oas.exportdata.fileName");
	private static final String LOCAL_FILE_PATH = ExtractUtil.getDetail("oas.exportdata.filepath");
	private static final Map<String, String> OBJECTIVE_MAP = new LinkedHashMap<String, String>();
	private static final Map<String, Integer> CONTENT_DOMAINS = new LinkedHashMap<String, Integer>();
	private static final Map<Integer, Map<String, ItemResponses>> ITEM_SET_ITEM = new HashMap<Integer, Map<String, ItemResponses>>(50);
	
	static {
		CONTENT_DOMAINS.put("Reading", 0);
		CONTENT_DOMAINS.put("Mathematics Computation", 0);
		CONTENT_DOMAINS.put("Applied Mathematics", 0);
		CONTENT_DOMAINS.put("Language", 0);
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
		FlatFileWriter ffWriter = null;
		try{
			ffWriter = new FileSystemFlatFileWriter(file, true);
			ffWriter.writeRecordList(myList);
			ffWriter.close();
		} finally {
			if(ffWriter!=null){
				ffWriter.close();
			}
		}
		System.out.println("Export file successfully generated:["+fileName+"]");
		System.out.println("Completed Writing");
	}
	
	private List<TABEFile> createList() throws Exception{
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
				createOrganization(oascon, catData, roster.getStudentId(),
						stateMap,districtMap, schoolMap, classMap);
				fillAccomodations(studentInfo.getStudentDemographic(), customerDemographic, catData);
				createAbilityScoreInformation(irscon,catData,roster);
				getSemScores(oascon, catData, roster,catData.getAbilityScores());
				fillObjective(oascon, irscon,catData,roster);
				prepareItemResponses(oascon, catData, roster);
				tabeFileList.add(catData);								
			}
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
			
			//System.out.println("populateCustomer");
		}finally {
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
				std.setCustomerId(Integer.valueOf(CUSTOMER_ID));
				std.setTestPurpose(rs.getString(9));
				std.setExtStudentId(rs.getString(10));
				std
				.setStudentDemographic(getStudentDemographic(con,
						studentId));

			}

			// System.out.println("getStudent");
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
						 && new Integer(organizationMapSize - 3).toString() != null
						&& rs.getString(5)
						.equalsIgnoreCase(
								new Integer(organizationMapSize - 3)
								.toString())) {
					tfil.setOrgLevel1Name(rs.getString(4));
					tfil.setOrgLevel1Code(rs.getString(3));

				} else if (rs.getString(5) != null
						 && new Integer(organizationMapSize - 2).toString() != null
							&& rs.getString(5)
							.equalsIgnoreCase(
									new Integer(organizationMapSize - 2)
									.toString())) {
					tfil.setOrgLevel2Name(rs.getString(4));
					tfil.setOrgLevel2Code(rs.getString(3));
				}
				else if (rs.getString(5) != null
						 && new Integer(organizationMapSize - 1).toString() != null
							&& rs.getString(5)
							.equalsIgnoreCase(
									new Integer(organizationMapSize - 1)
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
			
				}else{
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
		} finally {
			SqlUtil.close(ps, rs);
		}
		tfil.setDateTestingCompleted(Utility.getTimeZone(testCompleted, roster.getTimeZone(), true));
		tfil.setAbilityScores(abilityScore);	
	}
	
	private void fillObjective(Connection oascon, Connection irscon, TABEFile tfil, TestRoster roster) 
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
			ps = irscon.prepareStatement(SQLQuery.OBJECTIVE_MASTERY_SQL);
			ps.setInt(1, roster.getStudentId());
			ps.setInt(2, roster.getTestAdminId());
			rs = ps.executeQuery();
			while(rs.next()) {
				if (rs.getString("objectivename") != null){
					objMasteryLevel.getObjectiveMap().put(rs.getString("objectivename"), rs.getString("masterylevel"));
					objMastery.getObjectiveMap().put(rs.getString("objectivename"), rs.getString("mastery"));
				}				
			}
			SqlUtil.close(ps, rs);
			
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

	private static void prepareItemResponses(Connection con, TABEFile tfil,
	   TestRoster roster) throws IOException, Exception {
	   PreparedStatement ps = null ;
	   ResultSet rs = null;
	   Map<String, ItemResponses> itemMap = new HashMap<String, ItemResponses>();
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
				   response.append("RD");
				   itemCount = 29;
			   } else if("Mathematics Computation".equals(itemSetName)) {
				   response.append("MC");
				   itemCount = 24;
			   } else if("Applied Mathematics".equals(itemSetName)) {
				   response.append("AM");
				   itemCount = 29;
			   } else if("Language".equals(itemSetName)) {
				   response.append("LN");
				   itemCount = 29;
			   }
			    
			   ps = con.prepareStatement(SQLQuery.ALL_ITEMS_DETAILS_SQL);
			   ps.setInt(1, roster.getTestRosterId());
			   ps.setInt(2, itemSetId);
			   ps.setInt(3, roster.getTestRosterId());
			   ps.setInt(4, itemSetId);
			   rs = ps.executeQuery();
			   int index = 0;
			   while (rs.next()) {
				  ItemResponses ir = new ItemResponses();
				  ir.setItemId(rs.getString("item_id"));
				  ir.setResponseValue(rs.getString("response"));
				  ir.setOriginalResponse(rs.getString("original_response"));
				  ir.setIndex(index++);
				  itemMap.put(rs.getString("item_id"), ir);
			   }
			   SqlUtil.close(ps, rs);
			   
			   ps = con.prepareStatement(SQLQuery.ITEM_TOTAL_TIME_VISIT_SQL);
			   ps.setInt(1, roster.getTestRosterId());
			   ps.setInt(2, itemSetId);
			   rs = ps.executeQuery();
			   while (rs.next()){
				  ItemResponses ir = itemMap.get(rs.getString("item_id"));
				  ir.setResponseTime(rs.getString("total_time"));
			   }
			   SqlUtil.close(ps, rs);
			   
			   Set<String> itemSet = itemMap.keySet();
			   List<String> itemList = new ArrayList<String>(itemSet);
			   for(int i=0; i<itemCount; i++) {
				   if(i < itemList.size()) {
					   ItemResponses ir = itemMap.get(itemList.get(i));
					   itemIds.append(ir.getItemId() + ",");
					   itemResponse.append(ir.getResponseValue());
					   itemOrgResponse.append(ir.getOriginalResponse());
					   resposneTime.append(ir.getResponseTime() + ",");
				   } else {
					   itemIds.append(",");
					   itemResponse.append(" ");
					   itemOrgResponse.append(" ");
					   resposneTime.append(",");
				   }
			   }
			   response.append(itemIds.substring(0, itemIds.length() - 1)
					   		 + itemResponse + itemOrgResponse
					   	     + resposneTime.substring(0, resposneTime.length() - 1));
			   
			   ps = con.prepareStatement(SQLQuery.RESTART_ITEM_SQL);
			   ps.setInt(1, roster.getTestRosterId());
			   ps.setInt(2, itemSetId);
			   rs = ps.executeQuery();
			   if(rs.next()) {
			      ItemResponses ir = itemMap.get(rs.getString("item_id"));
				   if(ir.getIndex() < 10) {
					   response.append("1" + ir.getIndex() + " ");
				   } else {
					   response.append("1" + ir.getIndex());
				   }
			   } else {
				   response.append("00");
			   }
			   SqlUtil.close(ps, rs);
		   }
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
}