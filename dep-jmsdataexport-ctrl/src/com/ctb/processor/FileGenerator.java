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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.ffpojo.exception.FFPojoException;
import org.ffpojo.file.writer.FileSystemFlatFileWriter;
import org.ffpojo.file.writer.FlatFileWriter;

import com.ctb.dto.Accomodations;
import com.ctb.dto.CustomerDemographic;
import com.ctb.dto.CustomerDemographicValue;
import com.ctb.dto.OrderFile;
import com.ctb.dto.ProficiencyLevels;
import com.ctb.dto.ScaleScores;
import com.ctb.dto.SkillAreaNumberCorrect;
import com.ctb.dto.SkillAreaPercentCorrect;
import com.ctb.dto.SpecialCodes;
import com.ctb.dto.Student;
import com.ctb.dto.StudentContact;
import com.ctb.dto.StudentDemographic;
import com.ctb.dto.TestRoster;
import com.ctb.dto.Tfil;
import com.ctb.utils.EmetricUtil;
import com.ctb.utils.ExtractUtil;
import com.ctb.utils.SqlUtil;

public class FileGenerator {

	private static final DateFormat fileDateOutputFormat = new SimpleDateFormat(
	"MMddyyHHmm");

	private static String sql = "select org_node_mdr_number as mdr, category_name as categoryName,"
		+ " org_node_code as nodeCode, node.org_node_name as nodeName, onc.category_level as categoryLevel"
		+ " from org_node_ancestor ona, org_node_category onc, org_node node, org_node_student ons "
		+ " where    ons.org_node_id = ona.org_node_id   and  ons.student_id =  ? "
		+ " and onc.org_node_category_id = node.org_node_category_id   and node.org_node_id = ona.ancestor_org_node_id";

	// Defect Fix for 66423
	private static String testSessionSQl = "select tad.preferred_form as form, tc.test_level as testLevel,"
		+ " to_Char(roster.start_date_time,'MMDDYY') as testDate,to_Char(roster.completion_date_time,'MMDDYYYY')  as dateTestingCompleted"
		+ " from test_admin tad, test_roster roster,test_catalog tc"
		+ " where tad.test_admin_id = roster.test_admin_id"
		+ " and roster.test_completion_status in ('CO','IS','IC')"
		+ " and tc.test_catalog_id = tad.test_catalog_id"
		+ " and roster.test_roster_id = ? ";

	private static String customerModelLevel = " select max(category_level) as modelLevel"
		+ " from org_node_category where customer_id = :customerId";

	private static String testRosterDetails = " select distinct siss.VALIDATION_STATUS as validationStatus,"
		+ " siss.ITEM_SET_ORDER as itemSetOrder,siss.EXEMPTIONS as testExemptions,"
		+ " siss.ABSENT as absent,iss.item_set_name as itemSetName from student_item_set_status siss,"
		+ " test_roster ros, item_set iss where iss.item_set_id = siss.item_set_id and "
		+ " siss.test_roster_id = ros.test_roster_id and ros.student_id = ? "
		+ " and ros.test_roster_id = ? order by siss.item_set_order";

	private static String scoreSkilAreaSQL = "select careadim.name as name,    careafct.scale_score as scale_score,     careafct.proficency_level as proficency_level, " 
		+" careafct.points_obtained as points_obtained,     careafct.percent_obtained as percent_obtained"
		+" from laslink_content_area_fact careafct, content_area_dim careadim "
		+" where careafct.content_areaid = careadim.content_areaid"
		+" and content_area_type = 'LL CONTENT AREA'"
		+" and careafct.studentid = :studentId "
		+" and careafct.sessionid = :sessionId";

	private static String scoreSkilAreaOverAllSQL = " Select compfact.scale_score, compfact.proficency_level " +
	"from laslink_composite_fact compfact  " +
	"where compfact.studentid = :studentId " +
	"and compfact.sessionid = :sessionId";
	private String customerDemographicsql = "select this_.customer_demographic_id as customer_demographic_id, " +
			" this_.customer_id as customer_id, this_.label_name  as label_name  " +
			"from customer_demographic this_ where this_.customer_id = ?";
	private String customerDemographicsqlWithLevel = "select this_.customer_demographic_id as customer_demographic_id, " +
		" this_.customer_id as customer_id, this_.label_name  as label_name  from customer_demographic this_ " +
		"where this_.customer_id = ? and label_name = 'Accommodations' ";
	private String customersql = "select STATEPR as state,CONTACT_EMAIL as email, CONTACT_PHONE as phone,CONTACT_NAME as contact from Customer where CUSTOMER_ID = ? "; 
	private String testRosterSql =  " select this_.TEST_ROSTER_ID    as TEST_ROSTER_ID, this_.ACTIVATION_STATUS  as ACTIVATION_STATUS,   this_.TEST_COMPLETION_STATUS as TEST_COMPLETION_STATUS," +
			"  this_.CUSTOMER_ID            as CUSTOMER_ID, this_.STUDENT_ID  as STUDENT_ID, this_.TEST_ADMIN_ID   as TEST_ADMIN_ID  " +
			" from TEST_ROSTER this_  " +
			"where this_.CUSTOMER_ID = ?  and this_.ACTIVATION_STATUS = 'AC' " +
			"and this_.TEST_COMPLETION_STATUS in ('CO', 'IS', 'IC')";
	
	private String studentSql = "  select student0_.STUDENT_ID   as STUDENT_ID, student0_.FIRST_NAME   as FIRST_NAME,  student0_.LAST_NAME    as LAST_NAME,  student0_.MIDDLE_NAME  as MIDDLE_NAME,  student0_.BIRTHDATE    as BIRTHDATE,  student0_.GENDER       as GENDER,  student0_.GRADE  as GRADE0,   student0_.CUSTOMER_ID  as CUSTOMER_ID,   student0_.TEST_PURPOSE as TEST_PURPOSE,   student0_.EXT_PIN1  as EXT_PIN1  from student student0_   where student0_.STUDENT_ID = ? ";
		
	private String studentContactSql = " select studentcon0_.STUDENT_ID  as STUDENT_ID, studentcon0_.STUDENT_CONTACT_ID as STUDENT_CONTACT_ID, studentcon0_.CITY  as CITY,   studentcon0_.STATEPR  as STATEPR,   studentcon0_.STUDENT_ID  as STUDENT_ID  from STUDENT_CONTACT studentcon0_  where studentcon0_.STUDENT_ID = ?";
	
	private String studentDemographicSql = " select STUDENT_DEMOGRAPHIC_DATA_ID , CUSTOMER_DEMOGRAPHIC_ID , VALUE_NAME from student_demographic_data sdd where sdd.student_id = ? ";
		
	private String customerDemographiValuecsql="select value_name,customer_demographic_id  from customer_demographic_value  where customer_demographic_id =?";
	private int districtElementNumber = 0;
	private int schoolElementNumber = 0;
	private int classElementNumber = 0;
	private int studentElementNumber = 0;
	private String customerModelLevelValue = null;
	private String customerState = null;
	private String testDate = null;

	

	
	private static final String group = "GOAS";
	private static final String DATAFILE = "DATAFILE";

	private Integer customerId = new Integer(ExtractUtil
			.getDetail("oas.customerId"));
	//private static Integer customerId = null;

	public static void main(String[] args) {
		FileGenerator example = new FileGenerator();
		try {
			System.out.println("Making TXT from POJO...");
			example.writeToText();

			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execute (int customerId){
		try {
			this.customerId = customerId;
			System.out.println("Making TXT from POJO...");
			writeToText();

			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeToText() throws IOException, FFPojoException, SQLException,Exception {

		OrderFile orderFile = new OrderFile();
		List<Tfil> myList = createList(orderFile);
		String localFilePath = ExtractUtil.getDetail("oas.exportdata.filepath");
		String fileName = localFilePath+File.separator + customerState + "_" + testDate + "_"
		+ customerId + "_" + orderFile.getOrgTestingProgram() + "_"
		+ orderFile.getCustomerName().trim() + "_" + group + "_"
		+ DATAFILE + "_" + fileDateOutputFormat.format(new Date())
		+ ".dat";
		File file = new File(fileName);
		FlatFileWriter ffWriter = null;
		try{
			ffWriter = new FileSystemFlatFileWriter(file, true);
			ffWriter.writeRecordList(myList);
			ffWriter.close();
			System.out.println("Export file successfully generated:["+fileName+"]");
			orderFile.setDataFileName(EmetricUtil.truncate(fileName,
					new Integer(100)).substring(1, fileName.length()));
			System.out.println("Completed Writing");
			System.out.println("Preparing Order File");
			prepareOrderFile(orderFile, fileName);
		} finally {
			if(ffWriter!=null){
				ffWriter.close();
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

	private List<Tfil> createList(OrderFile orderFile) throws Exception {
		List<TestRoster> myrosterList = new ArrayList<TestRoster>();
		List<CustomerDemographic> customerAccomList = new ArrayList<CustomerDemographic>();
		List<CustomerDemographic> customerDemoList = new ArrayList<CustomerDemographic>();
		List<Tfil> tfilList = new ArrayList<Tfil>();
		HashMap<String, Integer> districtMap = new HashMap<String, Integer>();
		HashMap<String, Integer> schoolMap = new HashMap<String, Integer>();
		HashMap<String, Integer> classMap = new HashMap<String, Integer>();
		HashMap<Integer, String> customerDemographic = new HashMap<Integer, String>();
		Integer studentCount = 0;

		/*Session oasSession = null;
		Session irsSession = null;*/
		Connection oascon = null;
		Connection irscon = null;
		
		try {
			// This step will read hibernate.cfg.xml
			/*
			 * SessionFactory sessionFactory = new Configuration().
			 * configure(new File("hibernate.cfg.xml")).buildSessionFactory();
			 * session =sessionFactory.openSession();
			 */
			// Create new instance of Contact and set
			/*oasSession = HibernateUtil.getOasSession();
			irsSession = HibernateUtil.getIrsSession();*/
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

				/*
				 * * System.out.println("Customer Demo Value :" +
				 * c.getCustomerDemographicId() + " :: " + c.getLabelName());
				 * 
				 * 
				 * 
				 * for (CustomerDemographicValue cv : c
				 * .getCustomerDemographicValue()) System.out.println("Customer
				 * Demo Value" + cv.getValueName());
				 */
			}

			/*Criteria crit1 = oasSession.createCriteria(TestRoster.class);
			crit1.add(Expression.eq("customerId", customerId));
			crit1.add(Expression.eq("activationStatus", "AC"));
			crit1.add(Restrictions.in("testCompletionStatus", new String[] {
					"CO", "IS", "IC" }));
			myrosterList = crit1.list();
			Query customerQuery = oasSession
			.createSQLQuery(
			"select STATEPR as state,CONTACT_EMAIL as email, CONTACT_PHONE as phone,CONTACT_NAME as contact from Customer where CUSTOMER_ID = :customerId ")
			.addScalar("state", Hibernate.STRING).addScalar("email",
					Hibernate.STRING).addScalar("phone",
							Hibernate.STRING).addScalar("contact",
									Hibernate.STRING).setInteger("customerId",
											customerId);

			List<Object[]> customerObject = customerQuery.list();*/
			
			
			myrosterList= getTestRoster(oascon);
			populateCustomer(oascon, orderFile);

			/*for (Object[] o : customerObject) {
				this.customerState = o[0].toString();
				orderFile.setCustomerStateAbbrevation(o[0].toString());
				orderFile.setCustomerEmail(EmetricUtil.truncate(
						o[1].toString(), new Integer(64)));
				orderFile.setCustomerPhone(EmetricUtil.truncate(EmetricUtil
						.convertPhoneNumber(o[2].toString()), new Integer(21)));
				orderFile.setCustomerContact(EmetricUtil.truncate(o[3]
				                                                    .toString(), new Integer(64)));
			}*/

			for (TestRoster roster : myrosterList) {
				Tfil tfil = new Tfil();
				/*Student st = (Student) oasSession.get(Student.class, roster
						.getStudentId());*/
				Student st = roster.getStudent();
				System.out.println("Student id " + roster.getStudentId());
				//roster.setStudent(st);
				// System.out.println("Date Of birth" + st.getBirthDate());

				setStudentList(tfil, st);

				// Accomodations
				Accomodations accomodations = createAccomodations(st
						.getStudentDemographic(), setAccomodation,
						customerDemographic, tfil);
				tfil.setAccomodations(accomodations);

				tfil.setModelLevel(customerModelLevelValue);
				tfil.setState(this.customerState);

				// org node
				createOrganization(oascon, tfil, roster.getStudentId(),
						districtMap, schoolMap, classMap, orderFile);
				// create test Session
				createTestSessionDetails(oascon, tfil, roster
						.getTestRosterId(), orderFile);
				// create studentItemSetstatus
				createStudentItemStatusDetails(oascon, tfil, roster
						.getTestRosterId(), roster.getStudentId());

				// added for Skill Area Score
				createSkillAreaScoreInformation(irscon,tfil, roster);

				tfilList.add(tfil);
				studentCount++;
			}
			/** ***************** */
			orderFile.setCaseCount(studentCount.toString());
			//oasSession.close();
		} finally {
			SqlUtil.close(oascon);
			SqlUtil.close(irscon);
			
		}
		// throw again the first exception
		return tfilList;
	}

	
	
	private List<TestRoster> getTestRoster(Connection con) throws SQLException {
		PreparedStatement ps = null ;
		ResultSet rs = null;
		 List<TestRoster> rosterList = new ArrayList<TestRoster>();
		try{
			ps = con.prepareStatement(testRosterSql);
			ps.setInt(1, customerId);
			rs = ps.executeQuery(); rs.setFetchSize(500);
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
			
			System.out.println("populateCustomer");
		}finally {
			SqlUtil.close(ps, rs);
		}
		return rosterList;
	}

	private Student getStudent(Connection con, int studentId) throws SQLException {
		Student std = null;
		PreparedStatement ps = null ;
		ResultSet rs = null;		
		try{
			ps = con.prepareStatement(studentSql);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			if (rs.next()){
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
				std.setStudentContact(getStudentContact(con,studentId ));
				std.setStudentDemographic(getStudentDemographic(con,studentId ));
	
			}
			
			System.out.println("getStudent");
		}finally {
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
			ps = con.prepareStatement(studentDemographicSql);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			while (rs.next()) {
				StudentDemographic studentDemographic = new StudentDemographic();
				studentDemographic.setStudentDemographicId(rs.getInt(1));
				studentDemographic.setStudentId(studentId);
				studentDemographic.setCustomerDemographicId(rs.getInt(2));
				studentDemographic.setValueName(rs.getString(3));

				studentDemographicSet.add(studentDemographic);
			}

			System.out.println("getStudentDemographic:"+studentId+"::"+studentDemographicSet);
		} finally {
			SqlUtil.close(ps, rs);
		}

		return studentDemographicSet;
	}

	private Set<StudentContact> getStudentContact(Connection con, int studentId) throws SQLException {
		Set<StudentContact> studentContact = new HashSet<StudentContact>();
		PreparedStatement ps = null ;
		ResultSet rs = null;
		try{
			ps = con.prepareStatement(studentContactSql);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			while (rs.next()){
				StudentContact sdentCon = new StudentContact();
				sdentCon.setStudentContactId(rs.getInt(2));
				sdentCon.setCity(rs.getString(3));
				sdentCon.setState(rs.getString(4));
				studentContact.add(sdentCon);
	
			}
			
			System.out.println("getstudentContact");
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		return studentContact;
	}

	private void populateCustomer(Connection con, OrderFile orderFile) throws SQLException {
		PreparedStatement ps = null ;
		ResultSet rs = null;
		try{
			ps = con.prepareStatement(customersql);
			ps.setInt(1, customerId);
			rs = ps.executeQuery();
			while (rs.next()){
				this.customerState = rs.getString(1);
				orderFile.setCustomerStateAbbrevation(rs.getString(1));
				orderFile.setCustomerEmail(EmetricUtil.truncate(rs.getString(2), new Integer(64)));
				orderFile.setCustomerPhone(EmetricUtil.truncate(EmetricUtil
						.convertPhoneNumber(rs.getString(3)), new Integer(21)));
				orderFile.setCustomerContact(EmetricUtil.truncate(rs.getString(4), new Integer(64)));
			}
			
			System.out.println("populateCustomer");
		}finally {
			SqlUtil.close(ps, rs);
		}
	}

	private List<CustomerDemographic> getCustomerDemographic(Connection con) throws SQLException  {
		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		/*Criteria crit = session.createCriteria(CustomerDemographic.class);
		crit.add(Expression.eq("customerId", customerId));
		myList = crit.list();*/
		PreparedStatement ps = null ;
		ResultSet rs = null;
		try{
			ps = con.prepareStatement(customerDemographicsql);
			ps.setInt(1, customerId);
			rs = ps.executeQuery();
			while (rs.next()){
				CustomerDemographic cd = new CustomerDemographic();
				cd.setCustomerDemographicId(rs.getInt(1));
				cd.setCustomerId(rs.getInt(2));
				cd.setLabelName(rs.getString(3));
				cd.setCustomerDemographicValue(getCustomerDemographicValue(con, rs.getInt(1)));
				myList.add(cd)	;
			}
			
			System.out.println("getCustomerDemographic:"+myList);
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		return myList;
	}
	
	

	private Set<CustomerDemographicValue> getCustomerDemographicValue(Connection con, int customerDemographicId) throws SQLException {
		PreparedStatement ps = null ;
		ResultSet rs = null;
		Set<CustomerDemographicValue> customerDemographicValue = new HashSet<CustomerDemographicValue>();
		try{
			ps = con.prepareStatement(customerDemographiValuecsql);
			ps.setInt(1, customerDemographicId);
			rs = ps.executeQuery();
			while (rs.next()){
				CustomerDemographicValue cdv = new CustomerDemographicValue();
				cdv.setValueName(rs.getString(1));
				cdv.setCustomerDemographicId(rs.getInt(2));
				customerDemographicValue.add(cdv);
			}
			
			
			System.out.println("customerDemographicValue");
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		return customerDemographicValue;
		
	}

	private List<CustomerDemographic> getCustomerLeveledDemographicValue(Connection con) throws SQLException {
/*
		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		Criteria crit = session.createCriteria(CustomerDemographic.class);
		crit.add(Expression.eq("customerId", customerId)).add(
				Expression.eq("labelName", "Accommodations"));
		myList = crit.list();
		return myList;*/
		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		PreparedStatement ps = null ;
		ResultSet rs = null ;
		try{
			ps = con.prepareStatement(customerDemographicsqlWithLevel);
			ps.setInt(1, customerId);
			rs = ps.executeQuery();
			while (rs.next()){
				CustomerDemographic cd = new CustomerDemographic();
				
				cd.setCustomerDemographicId(rs.getInt(1));
				cd.setCustomerId(rs.getInt(2));
				cd.setLabelName(rs.getString(3));
				cd.setCustomerDemographicValue(getCustomerDemographicValue(con, rs.getInt(1)));
				myList.add(cd)	;
			}
			
			System.out.println("myList");
		}finally {
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
			set1.put(studentDem.getValueName(), studentDem);
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
					tfil.setHomeLanguage(studentDem.getValueName());
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
			System.out.println("customerDem.getLabelName:: "
					+ customerDem.getLabelName());
		}

		for (Map.Entry<String, StudentDemographic> entry : set1.entrySet()) {
			for (Map.Entry<String, CustomerDemographic> entry1 : set2
					.entrySet()) {
				Set<CustomerDemographicValue> set = entry1.getValue()
				.getCustomerDemographicValue();
				for (CustomerDemographicValue value : set) {
					if (value.getValueName().trim().equalsIgnoreCase(
							entry.getKey().trim())) {
						String string = value.getValueName().replace('-', '_');

						try {
							accomodations.getClass().getMethod("set" + string,
									String.class).invoke(accomodations,
											new String("1"));
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

		for (StudentContact contact : st.getStudentContact()) {
			tfil.setCity(contact.getCity());
			// tfil.setState(contact.getState());

		}
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
			} else if (studentDemo.getValueName().startsWith("mexicano-americano")) {
				tfil.setEthinicity("2");
			} else if (studentDemo.getValueName().startsWith("cubano")) {
				tfil.setEthinicity("3");
			} else if (studentDemo.getValueName().startsWith("cubano-americano")) {
				tfil.setEthinicity("4");
			} else if (studentDemo.getValueName().startsWith("puertorrique")) {
				tfil.setEthinicity("5");
			} else if (studentDemo.getValueName().startsWith("dominicano")) {
				tfil.setEthinicity("6");
			} else if (studentDemo.getValueName().startsWith("centroamericano")) {
				tfil.setEthinicity("7");
			} else if (studentDemo.getValueName().startsWith("sudamericano")) {
				tfil.setEthinicity("8");
			}else if (studentDemo.getValueName().startsWith("otro")) {
				tfil.setEthinicity("9");
			}
		}
	}

	private void setDisabilityCode(Set<StudentDemographic> sd, Tfil tfil) {

		for (StudentDemographic studentDemo : sd) {
			if (studentDemo.getValueName().equalsIgnoreCase("A")) {
				tfil.setDisability("1");
			} else if (studentDemo.getValueName().equalsIgnoreCase("D")) {
				tfil.setDisability("2");
			} else if (studentDemo.getValueName().equalsIgnoreCase("HI")) {
				tfil.setDisability("3");
			} else if (studentDemo.getValueName().equalsIgnoreCase("MU")) {
				tfil.setDisability("4");
			} else if (studentDemo.getValueName().equalsIgnoreCase("OI")) {
				tfil.setDisability("5");
			} else if (studentDemo.getValueName().equalsIgnoreCase("OHI")) {
				tfil.setDisability("6");
			} else if (studentDemo.getValueName().equalsIgnoreCase("SED")) {
				tfil.setDisability("7");
			} else if (studentDemo.getValueName().equalsIgnoreCase("LN")) {
				tfil.setDisability("8");
			} else if (studentDemo.getValueName().equalsIgnoreCase("SLI")) {
				tfil.setDisability("9");
			} else if (studentDemo.getValueName().equalsIgnoreCase("TBI")) {
				tfil.setDisability("A");
			} else if (studentDemo.getValueName().equalsIgnoreCase("VI")) {
				tfil.setDisability("B");
			} else if (studentDemo.getValueName().equalsIgnoreCase("ME")) {
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

	private void generateModelLevel(Connection conn) throws SQLException {
		/*Query query1 = session.createSQLQuery(customerModelLevel).addScalar(
				"modelLevel", Hibernate.STRING).setInteger("customerId",
						customerId);

		this.customerModelLevelValue = query1.uniqueResult().toString();*/
		String modelLevel = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(customerModelLevel);
			ps.setInt(1, customerId);
			rs = ps.executeQuery();
			if(rs.next()){
				modelLevel = rs.getString(1);
			}
		} finally {
			SqlUtil.close(ps, rs);
		}
		
		this.customerModelLevelValue=  modelLevel;
	}

	private void createOrganization(Connection con, Tfil tfil,
			Integer studentId, HashMap<String, Integer> districtMap,
			HashMap<String, Integer> schoolMap,
			HashMap<String, Integer> classMap, OrderFile orderFile) throws SQLException {

		TreeMap<Integer, String> organizationMap = new TreeMap<Integer, String>();

		/*Query query = session.createSQLQuery(sql).addScalar("mdr",
				Hibernate.STRING).addScalar("categoryName", Hibernate.STRING)
				.addScalar("nodeCode", Hibernate.STRING).addScalar("nodeName",
						Hibernate.STRING).addScalar("categoryLevel",
								Hibernate.STRING).setInteger("studentid", studentId);

		List<Object[]> amount = query.list();
		System.out.println("Object " + amount);

		for (Object[] object : amount) {

			if (object[3].toString().equalsIgnoreCase("root")
					|| object[3].toString().equalsIgnoreCase("CTB")) {
				// do nothing
			} else {
				organizationMap.put(new Integer(object[4].toString()),
						object[3].toString());
			}
		}
		// For defect Fix 66410
		Entry<Integer, String> lastEntry = organizationMap.lastEntry();
		Integer organizationMapSize = organizationMap.size();

		for (Object[] obj : amount) {
			if (obj[3].toString().equalsIgnoreCase("root")
					|| obj[3].toString().equalsIgnoreCase("CTB")) {
				// do nothing
			} else if (obj[4] != null
					&& new Integer(organizationMapSize - 2).toString() != null
					&& obj[4].toString().equalsIgnoreCase(
							new Integer(organizationMapSize - 2).toString())) {
				tfil.setElementNameA(obj[3].toString());

				Integer integer = districtMap.get(obj[3]);
				if (integer == null) {
					integer = ++districtElementNumber;
					districtMap.put(obj[3].toString(), integer);

				}
				tfil.setElementNumberA(String.valueOf(districtMap.get(obj[3]
				                                                          .toString())));
				if (obj[2] != null)
					tfil.setElementSpecialCodesA(obj[2].toString());
				tfil.setOrganizationId("XX" + obj[0].toString());
				tfil.setCustomerId(obj[0].toString());
				tfil.setElementStructureLevelA("01");
				if (orderFile.getCustomerId() == null)
					orderFile.setCustomerId(obj[0].toString());

			} else if (obj[4] != null
					&& new Integer(organizationMapSize - 1).toString() != null
					&& obj[4].toString().equalsIgnoreCase(
							new Integer(organizationMapSize - 1).toString())) {
				tfil.setElementNameB(obj[3].toString());

				Integer integer = schoolMap.get(obj[3]);
				if (integer == null) {
					integer = ++schoolElementNumber;
					schoolMap.put(obj[3].toString(), integer);

				}
				tfil.setElementNumberB(String.valueOf(schoolMap.get(obj[3]
				                                                        .toString())));
				if (obj[2] != null)
					tfil.setElementSpecialCodesB(obj[2].toString());
				tfil.setElementStructureLevelB("02");
				tfil.setSchoolId(obj[0].toString());
			}

			else if (obj[4] != null
					&& obj[4].toString().equalsIgnoreCase(
							organizationMapSize.toString())) {
				tfil.setElementNameC(obj[3].toString());

				Integer integer = classMap.get(obj[3]);
				if (integer == null) {
					integer = ++classElementNumber;
					classMap.put(obj[3].toString(), integer);

				}
				tfil.setElementNumberC(String.valueOf(classMap.get(obj[3].toString())));
				if (obj[2] != null)
					tfil.setElementSpecialCodesC(obj[2].toString());
				tfil.setElementStructureLevelC("03");
				tfil.setClassId(obj[0].toString());

			}
		}*/
		
		
		
		PreparedStatement ps = null;
		ResultSet rs = null;
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
			
			//Entry<Integer, String> lastEntry = organizationMap.lastEntry();
			Integer organizationMapSize = organizationMap.size();
			rs.beforeFirst();
			
			while (rs.next()) {
				if (rs.getString(4).equalsIgnoreCase("root")
						|| rs.getString(4).equalsIgnoreCase("CTB")) {
					// do nothing
				} else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 2).toString() != null
						&& rs.getString(5).equalsIgnoreCase(
								new Integer(organizationMapSize - 2).toString())) {
					tfil.setElementNameA(rs.getString(4).toString());

					Integer integer = districtMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++districtElementNumber;
						districtMap.put(rs.getString(4), integer);

					}
					tfil.setElementNumberA(String.valueOf(districtMap.get(rs.getString(4))));
					if (rs.getString(3) != null)
						tfil.setElementSpecialCodesA(rs.getString(3));
					tfil.setOrganizationId("XX" + rs.getString(1));
					tfil.setCustomerId(rs.getString(1));
					tfil.setElementStructureLevelA("01");
					if (orderFile.getCustomerId() == null)
						orderFile.setCustomerId(rs.getString(1));

				} else if (rs.getString(5) != null
						&& new Integer(organizationMapSize - 1).toString() != null
						&& rs.getString(5).toString().equalsIgnoreCase(
								new Integer(organizationMapSize - 1).toString())) {
					tfil.setElementNameB(rs.getString(4));

					Integer integer = schoolMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++schoolElementNumber;
						schoolMap.put(rs.getString(4), integer);

					}
					tfil.setElementNumberB(String.valueOf(schoolMap.get(rs.getString(4))));
					if (rs.getString(3) != null)
						tfil.setElementSpecialCodesB(rs.getString(3));
					tfil.setElementStructureLevelB("02");
					tfil.setSchoolId(rs.getString(1));
				}

				else if (rs.getString(5) != null
						&& rs.getString(5).equalsIgnoreCase(
								organizationMapSize.toString())) {
					tfil.setElementNameC(rs.getString(4));

					Integer integer = classMap.get(rs.getString(4));
					if (integer == null) {
						integer = ++classElementNumber;
						classMap.put(rs.getString(4), integer);

					}
					tfil.setElementNumberC(String.valueOf(classMap.get(rs.getString(4))));
					if (rs.getString(3) != null)
						tfil.setElementSpecialCodesC(rs.getString(3));
					tfil.setElementStructureLevelC("03");
					tfil.setClassId(rs.getString(1));

				}
			}
			
			
			System.out.println("createOrganization");
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		
		// For defect Fix 66410
		if (tfil.getElementNameB() == null) {
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
		}

		if (orderFile.getOrgTestingProgram() == null)
			orderFile.setOrgTestingProgram(tfil.getOrganizationId());
		if (orderFile.getCustomerName() == null)
			orderFile.setCustomerName(EmetricUtil.truncate(tfil
					.getElementNameA(), new Integer(30)));

	}

	private void createTestSessionDetails(Connection con, Tfil tfil,
			Integer rosterId, OrderFile orderFile) throws SQLException {
		/*Query query = session.createSQLQuery(testSessionSQl).addScalar("form",
				Hibernate.STRING).addScalar("testLevel", Hibernate.STRING)
				.addScalar("testDate", Hibernate.STRING).addScalar(
						"dateTestingCompleted", Hibernate.STRING).setInteger(
								"rosterId", rosterId);

		List<Object[]> testList = query.list();

		for (Object[] obj : testList) {

			if (obj[0].toString().equalsIgnoreCase("A")) {
				tfil.setTestName("LAS Links");
				tfil.setTestForm("A");
				if (orderFile.getTestName1() == null)
					orderFile.setTestName1(EmetricUtil.truncate(
							tfil.getTestName(), new Integer(10)).toUpperCase());
			} else if (obj[0].toString().equalsIgnoreCase("B")) {
				tfil.setTestName("LAS Links");
				tfil.setTestForm("B");
				if (orderFile.getTestName1() == null)
					orderFile.setTestName1(EmetricUtil.truncate(
							tfil.getTestName(), new Integer(10)).toUpperCase());
			} else if (obj[0].toString().equalsIgnoreCase("Espanol")) {
				tfil.setTestName("LAS Links Español");
				tfil.setTestForm("S");
				if (orderFile.getTestName1() == null)
					orderFile.setTestName1(EmetricUtil.truncate(
							tfil.getTestName(), new Integer(10)).toUpperCase());
			} else if (obj[0].toString().startsWith("Esp")) {
				tfil.setTestName("LAS Links Español");
				tfil.setTestForm("S");
				if (orderFile.getTestName1() == null)
					orderFile.setTestName1(EmetricUtil.truncate(
							tfil.getTestName(), new Integer(10)).toUpperCase());
			}

			if (obj[1] != null && obj[1].toString().equals("K")) {
				tfil.setTestLevel("1");
			} else if (obj[1] != null && obj[1].toString().equals("1")) {
				tfil.setTestLevel("1");
			} else if (obj[1] != null && obj[1].toString().equals("2-3")) {
				tfil.setTestLevel("2");
			} else if (obj[1] != null && obj[1].toString().equals("4-5")) {
				tfil.setTestLevel("3");
			} else if (obj[1] != null && obj[1].toString().equals("6-8")) {
				tfil.setTestLevel("4");
			} else if (obj[1] != null && obj[1].toString().equals("9-12")) {
				tfil.setTestLevel("5");
			}
			tfil.setTestDate(obj[2].toString());
			this.testDate = obj[2].toString();
			tfil.setDateTestingCompleted(obj[3].toString());

		}
		if (orderFile.getTestDate() == null)
			orderFile.setTestDate(EmetricUtil.truncate(tfil.getTestDate(),
					new Integer(8)));*/
		
		
		PreparedStatement ps = null ;
		ResultSet rs = null ;
		try{
			ps = con.prepareStatement(testSessionSQl);
			ps.setInt(1, rosterId);
			rs = ps.executeQuery();
			while (rs.next()){

				if (rs.getString(1).equalsIgnoreCase("A")) {
					tfil.setTestName("LAS Links");
					tfil.setTestForm("A");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								tfil.getTestName(), new Integer(10)).toUpperCase());
				} else if (rs.getString(1).equalsIgnoreCase("B")) {
					tfil.setTestName("LAS Links");
					tfil.setTestForm("B");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								tfil.getTestName(), new Integer(10)).toUpperCase());
				} else if (rs.getString(1).equalsIgnoreCase("Espanol")) {
					tfil.setTestName("LAS Links Español");
					tfil.setTestForm("S");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								tfil.getTestName(), new Integer(10)).toUpperCase());
				} else if (rs.getString(1).startsWith("Esp")) {
					tfil.setTestName("LAS Links Español");
					tfil.setTestForm("S");
					if (orderFile.getTestName1() == null)
						orderFile.setTestName1(EmetricUtil.truncate(
								tfil.getTestName(), new Integer(10)).toUpperCase());
				}

				if (rs.getString(2) != null && rs.getString(2).toString().equals("K")) {
					tfil.setTestLevel("1");
				} else if (rs.getString(2) != null && rs.getString(2).toString().equals("1")) {
					tfil.setTestLevel("1");
				} else if (rs.getString(2) != null && rs.getString(2).toString().equals("2-3")) {
					tfil.setTestLevel("2");
				} else if (rs.getString(2) != null && rs.getString(2).toString().equals("4-5")) {
					tfil.setTestLevel("3");
				} else if (rs.getString(2) != null && rs.getString(2).toString().equals("6-8")) {
					tfil.setTestLevel("4");
				} else if (rs.getString(2) != null && rs.getString(2).toString().equals("9-12")) {
					tfil.setTestLevel("5");
				}
				tfil.setTestDate(rs.getString(3).toString());
				this.testDate = rs.getString(3).toString();
				tfil.setDateTestingCompleted(rs.getString(4).toString());
				
				
			}
			
			if (orderFile.getTestDate() == null)
				orderFile.setTestDate(EmetricUtil.truncate(tfil.getTestDate(),
						new Integer(8)));
			
			System.out.println("createTestSessionDetails");
		}finally {
			SqlUtil.close(ps, rs);
		}
		
		
	}

	private void createStudentItemStatusDetails(Connection con, Tfil tfil,
			Integer rosterId, Integer studentId) throws SQLException {
		/*Query query = session.createSQLQuery(testRosterDetails).addScalar(
				"validationstatus", Hibernate.STRING).addScalar("itemSetOrder",
						Hibernate.INTEGER)
						.addScalar("testExemptions", Hibernate.STRING).addScalar(
								"absent", Hibernate.STRING).addScalar("itemSetName",
										Hibernate.STRING).setInteger("rosterId", rosterId)
										.setInteger("studentId", studentId);
		List<Object[]> testStudentItemSetStatusList = query.list();

		for (Object[] obj : testStudentItemSetStatusList) {

			if (obj[4].toString().equalsIgnoreCase("Speaking")) {
				tfil.setTestInvalidationSpeaking(obj[0].toString()
						.equalsIgnoreCase("IN") ? "1" : " ");
				tfil.setTestingExemptionsSpeaking(obj[2].toString()
						.equalsIgnoreCase("Y") ? "1" : " ");
				tfil
				.setAbsentSpeaking(obj[3].toString().equalsIgnoreCase(
				"Y") ? "1" : " ");
			} else if (obj[4].toString().equalsIgnoreCase("Listening")) {
				tfil.setTestInvalidationListening(obj[0].toString()
						.equalsIgnoreCase("IN") ? "1" : " ");
				tfil.setTestingExemptionsListening(obj[2].toString()
						.equalsIgnoreCase("Y") ? "1" : " ");
				tfil
				.setAbsentListening(obj[3].toString().equalsIgnoreCase(
				"Y") ? "1" : " ");
			} else if (obj[4].toString().equalsIgnoreCase("Reading")) {
				tfil.setTestInvalidationReading(obj[0].toString()
						.equalsIgnoreCase("IN") ? "1" : " ");
				tfil.setTestingExemptionsReading(obj[2].toString()
						.equalsIgnoreCase("Y") ? "1" : " ");
				tfil
				.setAbsentReading(obj[3].toString().equalsIgnoreCase(
				"Y") ? "1" : " ");
			} else if (obj[4].toString().equalsIgnoreCase("Writing")) {
				tfil.setTestInvalidationWriting(obj[0].toString()
						.equalsIgnoreCase("IN") ? "1" : " ");
				tfil.setTestingExemptionsWriting(obj[2].toString()
						.equalsIgnoreCase("Y") ? "1" : " ");
				tfil
				.setAbsentWriting(obj[3].toString().equalsIgnoreCase(
				"Y") ? "1" : " ");
			}
		}*/
		
		PreparedStatement ps = null ;
		ResultSet rs = null ;
		try{
			ps = con.prepareStatement(testRosterDetails);
			ps.setInt(1, studentId);
			ps.setInt(2, rosterId);
			rs = ps.executeQuery();
			while (rs.next()){

				if (rs.getString(5).equalsIgnoreCase("Speaking")) {
					tfil.setTestInvalidationSpeaking(rs.getString(1)
							.equalsIgnoreCase("IN") ? "1" : " ");
					tfil.setTestingExemptionsSpeaking(rs.getString(3)
							.equalsIgnoreCase("Y") ? "1" : " ");
					tfil
					.setAbsentSpeaking(rs.getString(4).equalsIgnoreCase(
					"Y") ? "1" : " ");
				} else if (rs.getString(5).toString().equalsIgnoreCase("Listening")) {
					tfil.setTestInvalidationListening(rs.getString(1).toString()
							.equalsIgnoreCase("IN") ? "1" : " ");
					tfil.setTestingExemptionsListening(rs.getString(3).toString()
							.equalsIgnoreCase("Y") ? "1" : " ");
					tfil
					.setAbsentListening(rs.getString(4).toString().equalsIgnoreCase(
					"Y") ? "1" : " ");
				} else if (rs.getString(5).toString().equalsIgnoreCase("Reading")) {
					tfil.setTestInvalidationReading(rs.getString(1).toString()
							.equalsIgnoreCase("IN") ? "1" : " ");
					tfil.setTestingExemptionsReading(rs.getString(3).toString()
							.equalsIgnoreCase("Y") ? "1" : " ");
					tfil
					.setAbsentReading(rs.getString(4).toString().equalsIgnoreCase(
					"Y") ? "1" : " ");
				} else if (rs.getString(5).toString().equalsIgnoreCase("Writing")) {
					tfil.setTestInvalidationWriting(rs.getString(1).toString()
							.equalsIgnoreCase("IN") ? "1" : " ");
					tfil.setTestingExemptionsWriting(rs.getString(3).toString()
							.equalsIgnoreCase("Y") ? "1" : " ");
					tfil
					.setAbsentWriting(rs.getString(4).toString().equalsIgnoreCase(
					"Y") ? "1" : " ");
				}
			}
			
		} finally {
			SqlUtil.close(ps, rs);
		}
		
		
	}



	private void createSkillAreaScoreInformation(Connection con, Tfil tfil,
			TestRoster roster) throws SQLException {
		TreeMap<String, Object[]> treeMap = new TreeMap<String, Object[]>();
		boolean isComprehensionPopulated= true;
		boolean isOralPopulated= true;


		ProficiencyLevels pl = new ProficiencyLevels();
		ScaleScores ss = new ScaleScores();
		SkillAreaNumberCorrect po =  new SkillAreaNumberCorrect();
		SkillAreaPercentCorrect pc = new SkillAreaPercentCorrect();
		
		PreparedStatement ps = null ;
		ResultSet rs = null ;
		PreparedStatement ps2 = null ;
		ResultSet rs2 = null ;
		/*String[] skillAreaScoreInfoOverAll =  new String[2];*/
		try {
			ps2 = con.prepareStatement(scoreSkilAreaOverAllSQL);
			ps2.setInt(1, roster.getStudentId());
			ps2.setInt(2, roster.getTestAdminId());
			rs2 = ps2.executeQuery();
			if (rs2.next()) {
				/*
				 * skillAreaScoreInfoOverAll[0] = rs.getString(1);
				 * skillAreaScoreInfoOverAll[1] = rs.getString(2);
				 */
				ss.setOverall(rs2.getString(1));
				pl.setOverall(rs2.getString(2));

			} else {
				// skillAreaScoreInfoOverAll = null;
				ss.setOverall("N/A");
				pl.setOverall(" ");
			}

		} finally {
			SqlUtil.close(ps2, rs2);
		}

		try {
			ps = con.prepareStatement(scoreSkilAreaSQL);
			ps.setInt(1, roster.getStudentId());
			ps.setInt(2, roster.getTestAdminId());
			rs = ps.executeQuery();
			while (rs.next()) {
				if(rs.getString(1)!=null){
					Object[] val = new Object[5];
					val[1]= rs.getString(2);
					val[2]= rs.getString(3);
					val[3]= rs.getString(4);
					val[4]= rs.getString(5);

					if(rs.getString(1).toString().trim().equalsIgnoreCase("speaking")){
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(),val );
					} else if(rs.getString(1).toString().trim().equalsIgnoreCase("listening")){
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(),val );

					}else if(rs.getString(1).toString().trim().equalsIgnoreCase("reading")){
						treeMap.put(rs.getString(1).toString().trim(),val );

					}else if(rs.getString(1).toString().trim().equalsIgnoreCase("writing")){
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(),val );

					}else if(rs.getString(1).toString().trim().equalsIgnoreCase("comprehension")){
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(),val );

					}else if(rs.getString(1).toString().trim().equalsIgnoreCase("oral")){
						treeMap.put(rs.getString(1).toString().trim().toLowerCase(),val );

					}
				}
			}

		} finally {
			SqlUtil.close(ps, rs);
		}
		
		
		
/*
 * Query query1 = irsSession.createSQLQuery(scoreSkilAreaSQL) .addScalar(
 * "name", Hibernate.STRING) .addScalar("scale_score",Hibernate.STRING)
 * .addScalar("proficency_level", Hibernate.STRING)
 * .addScalar("points_obtained", Hibernate.STRING)
 * .addScalar("percent_obtained", Hibernate.STRING)
 * .setInteger("studentId",roster.getStudentId()) .setInteger("sessionId",
 * roster.getTestAdminId());
 */


	/*	Query query2 = irsSession.createSQLQuery(scoreSkilAreaOverAllSQL)
		.addScalar(	"scale_score", Hibernate.STRING)
		.addScalar("proficency_level",Hibernate.STRING)
		.setInteger("studentId",roster.getStudentId())
		.setInteger("sessionId", roster.getTestAdminId());

		List<Object[]> skillAreaScoreInfoList = query1.list();
		Object[] skillAreaScoreInfoOverAll =  (Object[]) query2.uniqueResult();
		if(skillAreaScoreInfoOverAll!=null){
			ss.setOverall(skillAreaScoreInfoOverAll[0].toString());
			pl.setOverall(skillAreaScoreInfoOverAll[1].toString());
		} else {
			ss.setOverall("N/A");
			pl.setOverall(" ");
		}
		for (Object[] val :  skillAreaScoreInfoList){
			if(val[0]!=null){
				if(val[0].toString().trim().equalsIgnoreCase("speaking")){
					treeMap.put(val[0].toString().trim().toLowerCase(),val );
				} else if(val[0].toString().trim().equalsIgnoreCase("listening")){
					treeMap.put(val[0].toString().trim().toLowerCase(),val );

				}else if(val[0].toString().trim().equalsIgnoreCase("reading")){
					treeMap.put(val[0].toString().trim(),val );

				}else if(val[0].toString().trim().equalsIgnoreCase("writing")){
					treeMap.put(val[0].toString().trim().toLowerCase(),val );

				}else if(val[0].toString().trim().equalsIgnoreCase("comprehension")){
					treeMap.put(val[0].toString().trim().toLowerCase(),val );

				}else if(val[0].toString().trim().equalsIgnoreCase("oral")){
					treeMap.put(val[0].toString().trim().toLowerCase(),val );

				}
			}
		}*/




		if (tfil.getTestInvalidationSpeaking().equalsIgnoreCase("1")) {
			ss.setSpeaking("INV");
			po.setSpeaking("INV");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else if (tfil.getTestingExemptionsSpeaking().equalsIgnoreCase("1")) {
			ss.setSpeaking("EXM");
			po.setSpeaking("EXM");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else if (tfil.getAbsentSpeaking().equalsIgnoreCase("1")) {
			ss.setSpeaking("ABS");
			po.setSpeaking("ABS");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else {
			Object[] val = treeMap.get("speaking");
			if (val != null) {
				ss.setSpeaking(val[1].toString());
				pl.setSpeaking(val[2].toString());
				po.setSpeaking(val[3].toString());
				pc.setSpeaking(val[4].toString());
			}
		}

		if (tfil.getTestInvalidationListening().equalsIgnoreCase("1")) {
			ss.setListening("INV");
			po.setListening("INV");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else if (tfil.getTestingExemptionsListening().equalsIgnoreCase("1")) {
			ss.setListening("EXM");
			po.setListening("EXM");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else if (tfil.getAbsentListening().equalsIgnoreCase("1")) {
			ss.setListening("ABS");
			po.setListening("ABS");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else {
			Object[] val = treeMap.get("listening");
			if (val != null) {
				ss.setListening(val[1].toString());
				pl.setListening(val[2].toString());
				po.setListening(val[3].toString());
				pc.setListening(val[4].toString());
			}
		}

		if (tfil.getTestInvalidationReading().equalsIgnoreCase("1")) {
			ss.setReading("INV");
			po.setReading("INV");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else if (tfil.getTestingExemptionsReading().equalsIgnoreCase("1")) {
			ss.setReading("EXM");
			po.setReading("EXM");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else if (tfil.getAbsentReading().equalsIgnoreCase("1")) {
			ss.setReading("ABS");
			po.setReading("ABS");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else {
			Object[] val = treeMap.get("reading");
			if (val != null) {
				ss.setReading(val[1].toString());
				pl.setReading(val[2].toString());
				po.setReading(val[3].toString());
				pc.setReading(val[4].toString());
			}
		}

		if (tfil.getTestInvalidationWriting().equalsIgnoreCase("1")) {
			ss.setWriting("INV");
			po.setWriting("INV");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else if (tfil.getTestingExemptionsWriting().equalsIgnoreCase("1")) {
			ss.setWriting("EXM");
			po.setWriting("EXM");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else if (tfil.getAbsentWriting().equalsIgnoreCase("1")) {
			ss.setWriting("ABS");
			po.setWriting("ABS");
			isComprehensionPopulated = false;
			isOralPopulated = false;
		} else {
			Object[] val = treeMap.get("writing");
			if (val != null) {
				ss.setWriting(val[1].toString());
				pl.setWriting(val[2].toString());
				po.setWriting(val[3].toString());
				pc.setWriting(val[4].toString());
			}
		}


		if(isComprehensionPopulated){
			Object[] val = treeMap.get("comprehension");
			if(val!=null){
				ss.setComprehension(val[1].toString());
				pl.setComprehension(val[2].toString());
			} else {
				ss.setComprehension("N/A");
			}
			
		} else {
			ss.setComprehension("N/A");
		}

		if(isOralPopulated){
			Object[] val = treeMap.get("oral");
			if(val!=null){
				ss.setOral(val[1].toString());
				pl.setOral(val[2].toString());
			} else {
				ss.setOral("N/A");
			}
			

		} else {
			ss.setOral("N/A");
		}

		tfil.setScaleScores(ss);
		tfil.setProficiencyLevels(pl);
		tfil.setSkillAreaNumberCorrect(po);
		tfil.setSkillAreaPercentCorrect(pc);
	}




	private void prepareOrderFile(OrderFile orderFile, String fileName) throws IOException {

		String orderFileName = fileName.substring(0, fileName.length() - 23);

		orderFileName = orderFileName + "ORDERFILE_"
		+ fileDateOutputFormat.format(new Date()) + ".csv";
		FileWriter writer = null;
		try {
			writer = new FileWriter(orderFileName);

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

}
