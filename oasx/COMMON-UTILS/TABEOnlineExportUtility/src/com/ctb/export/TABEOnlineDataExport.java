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
	
	private static String CUSTOMER_IDs = ExtractUtil.getDetail("oas.customerIds");
	private static Integer PRODUCT_ID = Integer.valueOf(ExtractUtil.getDetail("oas.productId"));
	private static final String LOCAL_FILE_PATH = ExtractUtil.getDetail("oas.exportdata.filepath");
	private static final String FILE_NAME = ExtractUtil.getDetail("oas.exportdata.fileName");
	private static final String FILE_TYPE = ExtractUtil.getDetail("oas.exportdata.fileType");
	private static final String SEPARATOR = "#";
	private static final Map<String, String> ALL_CONTENT_DOMAIN = new LinkedHashMap<String, String>();
	private static final Map<String, Integer> ITEM_COUNT_MAP = new LinkedHashMap<String, Integer>();
	private static final Map<String, List<ItemResponses>> ITEM_SET_MAP = new HashMap<String, List<ItemResponses>>();
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TABEOnlineDataExport dataExport = new TABEOnlineDataExport();
		try {
			if(args != null && args.length > 0) {
				if(args[0] != null) {
					CUSTOMER_IDs = args[0];
				}
				if(args[1] != null) {
					PRODUCT_ID = Integer.valueOf(args[1]);
				}
			}
			System.out.println("Customer Id: " + CUSTOMER_IDs);
			System.out.println("Product Id: " + PRODUCT_ID);
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
		System.out.println("Start Writing");
		List<TABEFile> myList = createList();
		String modFileName = FILE_NAME + "_" + System.currentTimeMillis() + FILE_TYPE;
		if(!(new File(LOCAL_FILE_PATH)).exists()){
			File f = new File(LOCAL_FILE_PATH);
			f.mkdirs();
		}
		File file = new File(LOCAL_FILE_PATH, modFileName);
		CSVWriter writer = new CSVWriter(new FileWriter(file));
		StringBuilder headerRow = new StringBuilder();
		StringBuilder contentDomains = new StringBuilder();
		for (String contentDomainName: ALL_CONTENT_DOMAIN.keySet()) {
			contentDomains.append(contentDomainName).append("(Level, Raw Score, Scale Score)").append(SEPARATOR)
			.append("Scale Vector Response").append(SEPARATOR);
			Integer itemCount = ITEM_COUNT_MAP.get(contentDomainName);
			if(itemCount != null) {
				for(int i=0; i<itemCount; i++) {
					contentDomains.append("(Item number, first visit, Total time)").append(SEPARATOR);
				}
			}
		}
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
		.append(contentDomains.substring(0, contentDomains.length() - 1));
		
		writer.writeNext(headerRow.toString().split(SEPARATOR));
		try {
			StringBuilder row = new StringBuilder();
			for(TABEFile tabe: myList) {
				
				row = new StringBuilder();
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
				.append(tabe.getScores());
				writer.writeNext(row.toString().split(SEPARATOR));
			}
			System.out.println("Export file successfully generated:["+modFileName+"]");
			System.out.println("Completed Writing");
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
		Map<String, Integer> stateMap = new HashMap<String, Integer>();
		Map<String, Integer> districtMap = new HashMap<String, Integer>();
		Map<String, Integer> schoolMap = new HashMap<String, Integer>();
		Map<String, Integer> classMap = new HashMap<String, Integer>();
		Map<String, Map<Integer, String>> customerDemographic = new HashMap<String, Map<Integer, String>>();
		Map<Integer, String> demoGraphicMap = null;
		Set<CustomerDemographic> demoSet = null;
		Connection oascon = null;
		Connection irscon = null;
		
		try {
			oascon = SqlUtil.openOASDBconnectionForResearch();
			irscon = SqlUtil.openIRSDBconnectionForResearch();
			
			getAllContentDomain(oascon);
			myrosterList = getTestRoster(oascon);
			int count = 0;
			for (TestRoster roster : myrosterList) {
				TABEFile catData = new TABEFile();
				catData.setCustomerID(String.valueOf(roster.getCustomerId()));	
				if(customerDemographic.get(catData.getCustomerID()) == null) {
					customerDemoList = getCustomerDemographic(oascon, catData.getCustomerID());
					demoSet = new HashSet<CustomerDemographic>(customerDemoList);
					demoGraphicMap = new HashMap<Integer, String>();
					for (CustomerDemographic c : demoSet) {
						demoGraphicMap.put(c.getCustomerDemographicId(), c.getLabelName());
					}
					customerDemographic.put(catData.getCustomerID(), demoGraphicMap);
				} else {
					demoGraphicMap = customerDemographic.get(catData.getCustomerID());
				}
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
				fillAccomodations(studentInfo.getStudentDemographic(), demoGraphicMap, catData);
				getScores(oascon, irscon, catData, roster);
				getTimedOut(oascon, catData, roster);
				tabeFileList.add(catData);	
				System.out.println("Processed record count:" + ++count);
			}
			System.out.println("Total number of record exported:" + count);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(oascon);
			SqlUtil.close(irscon);
		}
		return tabeFileList;
	}
	
	
	private List<CustomerDemographic> getCustomerDemographic(Connection con, String customerId)
	throws SQLException {
		System.out.println("getCustomerDemographic start");
		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SQLQuery.customerDemographicsql);
			ps.setString(1, customerId);
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
		String customerCond = " and tr.customer_id in (" + CUSTOMER_IDs + ") order by tr.customer_id ";
		String query = null;
		try{
			if(CUSTOMER_IDs != null) {
				query = SQLQuery.testRosterSql.replace(":customerIds", customerCond);
			} else {
				query = SQLQuery.testRosterSql.replace(":customerIds", "");
			}
			ps = con.prepareStatement(query);
			ps.setInt(1, PRODUCT_ID);
			rs = ps.executeQuery(); 
			rs.setFetchSize(500);
			while (rs.next()){
				TestRoster ros = new TestRoster();
				ros.setTestRosterId(rs.getInt(1));
				ros.setActivationStatus(rs.getString(2));
				ros.setTestCompletionStatus(rs.getString(3));
				ros.setCustomerId(rs.getInt("customer_id"));
				ros.setStudentId(rs.getInt(5));
				ros.setTestAdminId(rs.getInt(6));
				ros.setRestartNumber(rs.getInt(9));
				ros.setLastMseq(rs.getInt(10));
				ros.setStartDate(rs.getString(11));
				ros.setStudent(getStudent(con, rs.getInt("customer_id"), rs.getInt(5))); 
				ros.setTimeZone(rs.getString(8));
				String productName = rs.getString("product_name");
				if(productName.indexOf("9") > 0) {
					ros.setTestFormId("9");
				} else if(productName.indexOf("10") > 0) {
					ros.setTestFormId("10");
				}
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
	
	private Student getStudent(Connection con, Integer customerId, int studentId)
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
				std.setCustomerId(customerId);
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
			Integer studentId, Map<String, Integer> stateMap,
			Map<String, Integer> districtMap,
			Map<String, Integer> schoolMap,
			Map<String, Integer> classMap)
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
			Map<Integer, String> customerDemographic, TABEFile tfil) {

		TreeMap<String, StudentDemographic> set1 = new TreeMap<String, StudentDemographic>();
		Map<Integer, String> studentDemographic = new HashMap<Integer, String>();

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
				} else if (customerDemoName.startsWith("Migrant")) {
					tfil.setMigrant(studentDem.getValueName() != null ? "1" : "0");
				} else if (customerDemoName.startsWith("Section")) {
					tfil.setSection504(studentDem.getValueName() != null ? "1" : "0");
				}
			}
		}
	}
	
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
			SqlUtil.close(ps, rs);
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
			SqlUtil.close(ps, rs);
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
		Map<String, Scores> scaleScoreMap = new LinkedHashMap<String, Scores>();
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
				if(score != null) {
					score.setScaleScore(rs.getString("scale_score"));
				}
			}
			SqlUtil.close(ps, rs);
			Set<String> keySet = ALL_CONTENT_DOMAIN.keySet();
			for(String itemSetName : keySet) {
				String itemSetId = ALL_CONTENT_DOMAIN.get(itemSetName);
				Scores score = scoreMap.get(itemSetName);
				if(score != null) {
					Scores scaleScore = scaleScoreMap.get(score.getContentAreaId());
					if(scaleScore != null) {
						score.setScaleScore(scaleScore.getScaleScore());
					}
				}
				if(score != null) {
					itemSetId = score.getItemSetId();
				}
				if(ITEM_SET_MAP.get(itemSetId) == null) {
					getAllItemsForItemSet(oascon, itemSetId, itemSetName);
				}
				prepareItemResponses(oascon, itemSetId, tfil, roster);
				getScaleVectorResponse(oascon, tfil, roster, itemSetId);
				if(score != null) {
					scores.append(Utility.formatData(score.getLevel()) + "," + Utility.formatData(score.getRawScore())
						 	  + "," + Utility.formatData(score.getScaleScore()) + SEPARATOR 
						 	  + tfil.getScaleVectorResponse() + SEPARATOR
						 	  + tfil.getItemResponse() + SEPARATOR);
				} else {
					scores.append(",," + SEPARATOR 
						 	  + tfil.getScaleVectorResponse() + SEPARATOR
						 	  + tfil.getItemResponse() + SEPARATOR);
				}
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
			   TABEFile tfil, TestRoster roster, String itemSetId) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder response = new StringBuilder();
		Map<String, String> responseMap = new HashMap<String, String>();
		try {
			ps = oascon.prepareStatement(SQLQuery.SCORED_RESPONSE_VECTOR_SQL);
			ps.setInt(1, roster.getTestRosterId());
			ps.setString(2, itemSetId);
			ps.setString(3, itemSetId);
			ps.setInt(4, roster.getTestRosterId());
			rs = ps.executeQuery();
			List<ItemResponses> items = ITEM_SET_MAP.get(itemSetId);
			while(rs.next()) {
				responseMap.put(rs.getString("item_id"), rs.getString("response"));
			}
			for(ItemResponses ir: items) {
				String val = responseMap.get(ir.getItemId());
				if(val != null) {
					response.append(val + ",");
				} else {
					response.append(",");
				}
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
   
   private void getAllContentDomain(Connection con) 
   throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SQLQuery.GET_ALL_CONTENT_DOMAIN_SQL);
			ps.setInt(1, PRODUCT_ID);
			rs = ps.executeQuery();
			while(rs.next()) {
				ALL_CONTENT_DOMAIN.put(rs.getString("item_set_name"), rs.getString("item_set_id"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
   }
   
   private void getAllItemsForItemSet(Connection con, String itemSetId, String itemSetName) {
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
			ITEM_COUNT_MAP.put(itemSetName, items.size());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SqlUtil.close(ps, rs);
		}
   }
}