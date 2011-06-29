package com.ctb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import java.util.Map.Entry;

import org.ffpojo.exception.FFPojoException;
import org.ffpojo.file.writer.FileSystemFlatFileWriter;
import org.ffpojo.file.writer.FlatFileWriter;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

import com.ctb.dto.Accomodations;
import com.ctb.dto.CustomerDemographic;
import com.ctb.dto.CustomerDemographicValue;
import com.ctb.dto.OrderFile;
import com.ctb.dto.SpecialCodes;
import com.ctb.dto.Student;
import com.ctb.dto.StudentContact;
import com.ctb.dto.StudentDemographic;
import com.ctb.dto.TestRoster;
import com.ctb.dto.Tfil;
import com.ctb.utils.EmetricUtil;
import com.ctb.utils.ExtractUtil;
import com.ctb.utils.HibernateUtil;

public class CreateFile {

	private static final DateFormat fileDateOutputFormat = new SimpleDateFormat(
			"MMddyyHHmm");

	private static String sql = "select org_node_mdr_number as mdr, category_name as categoryName,"
			+ " org_node_code as nodeCode, node.org_node_name as nodeName, onc.category_level as categoryLevel"
			+ " from org_node_ancestor ona, org_node_category onc, org_node node, org_node_student ons "
			+ " where    ons.org_node_id = ona.org_node_id   and  ons.student_id =  :studentid"
			+ " and onc.org_node_category_id = node.org_node_category_id   and node.org_node_id = ona.ancestor_org_node_id";

	// Defect Fix for 66423
	private static String testSessionSQl = "select tad.preferred_form as form, tc.test_level as testLevel,"
			+ " to_Char(roster.start_date_time,'MMDDYY') as testDate,to_Char(roster.completion_date_time,'MMDDYYYY')  as dateTestingCompleted"
			+ " from test_admin tad, test_roster roster,test_catalog tc"
			+ " where tad.test_admin_id = roster.test_admin_id"
			+ " and roster.test_completion_status in ('CO','IS','IC')"
			+ " and tc.test_catalog_id = tad.test_catalog_id"
			+ " and roster.test_roster_id = :rosterId";

	private static String customerModelLevel = " select max(category_level) as modelLevel"
			+ " from org_node_category where customer_id = :customerId";

	private static String testRosterDetails = " select distinct siss.VALIDATION_STATUS as validationStatus,"
			+ " siss.ITEM_SET_ORDER as itemSetOrder,siss.EXEMPTIONS as testExemptions,"
			+ " siss.ABSENT as absent,iss.item_set_name as itemSetName from student_item_set_status siss,"
			+ " test_roster ros, item_set iss where iss.item_set_id = siss.item_set_id and "
			+ " siss.test_roster_id = ros.test_roster_id and ros.student_id = :studentId "
			+ " and ros.test_roster_id = :rosterId order by siss.item_set_order";

	private int districtElementNumber = 0;
	private int schoolElementNumber = 0;
	private int classElementNumber = 0;
	private int studentElementNumber = 0;
	private String customerModelLevelValue = null;
	private String customerMdrId = null;
	private String customerName = null;
	private static final String tagNumber = "OAS";
	private String testName = null;
	private String customerState = null;
	private String testDate = null;
	private static final String group = "G+OAS";
	private static final String DATAFILE = "DATAFILE";

	private static Integer customerId = new Integer(ExtractUtil
			.getDetail("oas.customerId"));

	public static void main(String[] args) {
		createFile example = new createFile();
		try {
			System.out.println("Making TXT from POJO...");
			example.writeToText();

			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}

	public void writeToText() throws IOException, FFPojoException {

		OrderFile orderFile = new OrderFile();
		List<Tfil> myList = createList(orderFile);

		String fileName = "/" + customerState + "_" + testDate + "_"
				+ customerId + "_" + orderFile.getOrgTestingProgram() + "_"
				+ orderFile.getCustomerName().trim() + "_" + group + "_"
				+ DATAFILE + "_" + fileDateOutputFormat.format(new Date())
				+ ".dat";
		File file = new File(fileName);
		FlatFileWriter ffWriter = new FileSystemFlatFileWriter(file, true);
		ffWriter.writeRecordList(myList);
		ffWriter.close();
		orderFile.setDataFileName(EmetricUtil.truncate(fileName,
				new Integer(100)).substring(1, fileName.length()));
		System.out.println("Completed Writing");
		System.out.println("Preparing Order File");
		prepareOrderFile(orderFile, fileName);

	}

	/*
	 * private static List<TestAndHierarchy> createList() { List<TestAndHierarchy>
	 * myList = new ArrayList<TestAndHierarchy>(); DbQuery dbquery; try {
	 * dbquery = new DbQuery(); myList = dbquery.getDummyData(); } catch
	 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * return myList; }
	 */

	private List<Tfil> createList(OrderFile orderFile) {
		List<Student> myList = new ArrayList<Student>();
		List<TestRoster> myrosterList = new ArrayList<TestRoster>();
		List<CustomerDemographic> customerAccomList = new ArrayList<CustomerDemographic>();
		List<CustomerDemographic> customerDemoList = new ArrayList<CustomerDemographic>();
		List<Tfil> tfilList = new ArrayList<Tfil>();
		HashMap<String, Integer> districtMap = new HashMap<String, Integer>();
		HashMap<String, Integer> schoolMap = new HashMap<String, Integer>();
		HashMap<String, Integer> classMap = new HashMap<String, Integer>();
		HashMap<Integer, String> customerDemographic = new HashMap<Integer, String>();
		Integer studentCount = 0;

		Session session = null;

		try {
			// This step will read hibernate.cfg.xml
			/*
			 * SessionFactory sessionFactory = new Configuration().
			 * configure(new File("hibernate.cfg.xml")).buildSessionFactory();
			 * session =sessionFactory.openSession();
			 */
			// Create new instance of Contact and set
			session = HibernateUtil.getSession();
			generateModelLevel(session);
			customerDemoList = getCustomerDemographic(session);
			Set<CustomerDemographic> set = new HashSet<CustomerDemographic>(
					customerDemoList);
			customerAccomList = getCustomerDemographicValue(session);
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

			Criteria crit1 = session.createCriteria(TestRoster.class);
			crit1.add(Expression.eq("customerId", customerId));
			crit1.add(Expression.eq("activationStatus", "AC"));
			crit1.add(Restrictions.in("testCompletionStatus", new String[] {
					"CO", "IS", "IC" }));
			myrosterList = crit1.list();
			Query customerQuery = session
					.createSQLQuery(
							"select STATEPR as state,CONTACT_EMAIL as email, CONTACT_PHONE as phone,CONTACT_NAME as contact from Customer where CUSTOMER_ID = :customerId ")
					.addScalar("state", Hibernate.STRING).addScalar("email",
							Hibernate.STRING).addScalar("phone",
							Hibernate.STRING).addScalar("contact",
							Hibernate.STRING).setInteger("customerId",
							customerId);

			List<Object[]> customerObject = customerQuery.list();

			for (Object[] o : customerObject) {
				this.customerState = o[0].toString();
				orderFile.setCustomerStateAbbrevation(o[0].toString());
				orderFile.setCustomerEmail(EmetricUtil.truncate(
						o[1].toString(), new Integer(64)));
				orderFile.setCustomerPhone(EmetricUtil.truncate(EmetricUtil
						.convertPhoneNumber(o[2].toString()), new Integer(21)));
				orderFile.setCustomerContact(EmetricUtil.truncate(o[3]
						.toString(), new Integer(64)));
			}

			for (TestRoster roster : myrosterList) {
				Tfil tfil = new Tfil();
				Student st = (Student) session.get(Student.class, roster
						.getStudentId());
				System.out.println("Student id " + roster.getStudentId());
				roster.setStudent(st);
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
				createOrganization(session, tfil, roster.getStudentId(),
						districtMap, schoolMap, classMap, orderFile);
				// create test Session
				createTestSessionDetails(session, tfil, roster
						.getTestRosterId(), orderFile);
				// create studentItemSetstatus
				createStudentItemStatusDetails(session, tfil, roster
						.getTestRosterId(), roster.getStudentId());

				tfilList.add(tfil);
				studentCount++;
			}
			/** ***************** */
			orderFile.setCaseCount(studentCount.toString());
			session.close();
		} catch (Exception e) {
			System.out.println("error:" + e.getMessage());
			e.printStackTrace();
		}
		// throw again the first exception
		return tfilList;
	}

	public List<CustomerDemographic> getCustomerDemographic(Session session) {
		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		Criteria crit = session.createCriteria(CustomerDemographic.class);
		crit.add(Expression.eq("customerId", customerId));
		myList = crit.list();
		System.out.println("myList");
		return myList;
	}

	public List<CustomerDemographic> getCustomerDemographicValue(Session session) {

		List<CustomerDemographic> myList = new ArrayList<CustomerDemographic>();
		Criteria crit = session.createCriteria(CustomerDemographic.class);
		crit.add(Expression.eq("customerId", customerId)).add(
				Expression.eq("labelName", "Accommodations"));
		myList = crit.list();
		return myList;

	}

	public Accomodations createAccomodations(Set<StudentDemographic> sd,
			Set<CustomerDemographic> cd,
			HashMap<Integer, String> customerDemographic, Tfil tfil) {

		TreeMap<String, StudentDemographic> set1 = new TreeMap<String, StudentDemographic>();
		TreeMap<String, CustomerDemographic> set2 = new TreeMap<String, CustomerDemographic>();
		HashMap<Integer, String> studentDemographic = new HashMap<Integer, String>();
		Accomodations accomodations = new Accomodations();
		SpecialCodes specialCodes = new SpecialCodes();
		List<String> myList = new ArrayList<String>();
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

	public void setStudentList(Tfil tfil, Student st) {
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

	public void setEthnicity(Set<StudentDemographic> sd, Tfil tfil) {

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

	public void setDisabilityCode(Set<StudentDemographic> sd, Tfil tfil) {

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

	public void setProgramParticipation(Set<StudentDemographic> sd, Tfil tfil) {

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

	public void setSpecialEducation(Set<StudentDemographic> sd, Tfil tfil) {

		for (StudentDemographic studentDemo : sd) {
			if (studentDemo.getValueName().equalsIgnoreCase("IEP")) {
				tfil.setIep("1");
			} else if (studentDemo.getValueName().equalsIgnoreCase("504")) {
				tfil.setSbi504("1");
			}
		}
	}

	public void generateModelLevel(Session session) {
		Query query1 = session.createSQLQuery(customerModelLevel).addScalar(
				"modelLevel", Hibernate.STRING).setInteger("customerId",
				customerId);

		this.customerModelLevelValue = query1.uniqueResult().toString();

	}

	public void createOrganization(Session session, Tfil tfil,
			Integer studentId, HashMap<String, Integer> districtMap,
			HashMap<String, Integer> schoolMap,
			HashMap<String, Integer> classMap, OrderFile orderFile) {

		TreeMap<Integer, String> organizationMap = new TreeMap<Integer, String>();

		Query query = session.createSQLQuery(sql).addScalar("mdr",
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
				tfil.setElementNumberC(EmetricUtil.formatSpace(String.valueOf(classMap.get(obj[3]
						.toString())),7));
				if (obj[2] != null)
					tfil.setElementSpecialCodesC(obj[2].toString());
				tfil.setElementStructureLevelC("03");
				tfil.setClassId(obj[0].toString());

			}
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

	public void createTestSessionDetails(Session session, Tfil tfil,
			Integer rosterId, OrderFile orderFile) {
		Query query = session.createSQLQuery(testSessionSQl).addScalar("form",
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
					new Integer(8)));
	}

	public void createStudentItemStatusDetails(Session session, Tfil tfil,
			Integer rosterId, Integer studentId) {
		Query query = session.createSQLQuery(testRosterDetails).addScalar(
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
		}
	}

	public void prepareOrderFile(OrderFile orderFile, String fileName) {

		String orderFileName = fileName.substring(0, fileName.length() - 23);

		orderFileName = orderFileName + "ORDERFILE_"
				+ fileDateOutputFormat.format(new Date()) + ".csv";
		try {
			FileWriter writer = new FileWriter(orderFileName);

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
		} catch (Exception e) {
			System.err.println("Error in Preparing OrderFile");
			e.printStackTrace();

		}

	}

}
