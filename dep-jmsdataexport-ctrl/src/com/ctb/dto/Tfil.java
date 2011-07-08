package com.ctb.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ffpojo.metadata.positional.annotation.PositionalField;
import org.ffpojo.metadata.positional.annotation.PositionalRecord;

@PositionalRecord
public class Tfil {

	private String modelLevel;
	private String organizationId;
	private String elementNameA;
	private String elementStructureLevelA;
	private String elementNumberA;
	private String elementSpecialCodesA;
	private String customerId;
	private String remainderOfElementSpecialCodesA;
	private String grade;
	private String city;
	private String state;
	private String elementNameB;
	private String elementStructureLevelB;
	private String elementNumberB;
	private String elementSpecialCodesB;
	private String schoolId;
	private String remainderOfElementSpecialCodesB;
	private String elementNameC;
	private String elementStructureLevelC;
	private String elementNumberC;
	private String elementSpecialCodesC;
	private String testName;
	private String testForm;
	private String testLevel;
	private String testDate;
	private String scoringType = new String("N");
	private String testHierarchyUnused;
	private String classId;

	private String studentElementNumber;
	private String studentLastName;
	private String studentFirstName;
	private String studentMiddleName;
	private String studentBirthDate;
	private String studentChronologicalAge;
	private String studentGender;
	private String extStudentId;
	private String ethinicity;
	private String homeLanguage;
	private String purposeOfTestp;
	private String eseaTitle1;
	private String ellEseaTitle;
	private String giftedAndTalented;
	private String indianEducation;
	private String migrantEducation;
	private String iep;
	private String sbi504;
	private String disability;
	private String ctbUseColumns;
	private String sbiUnused;
	private String studentGradeFromAnswerSheets;
	private String dateTestingCompleted;
	private String testInvalidationSpeaking= new String(" ");
	private String testInvalidationListening= new String(" ");
	private String testInvalidationReading= new String(" ");
	private String testInvalidationWriting= new String(" ");
	private String testingExemptionsSpeaking= new String(" ");
	private String testingExemptionsListening= new String(" ");
	private String testingExemptionsReading= new String(" ");
	private String testingExemptionsWriting= new String(" ");
	private String absentSpeaking= new String(" ");
	private String absentListening= new String(" ");
	private String absentReading= new String(" ");
	private String absentWriting= new String(" ");
	private String usaSchoolEnrollment;
	private String mobilityGrade;
	private Accomodations accomodations;
	private String specialCodeK = new String(" ");
	private String specialCodeL = new String(" ");
	private String specialCodeM = new String(" ");
	private String specialCodeN = new String(" ");
	private String specialCodeO = new String(" ");
	private String specialCodeP = new String(" ");
	private String specialCodeQ = new String(" ");
	private String specialCodeR = new String(" ");
	private String specialCodeS = new String(" ");
	private String specialCodeT = new String(" ");
	private SpecialCodes userDefinedBioPage;
	private String sbiCtbUseColumns;
	private ScaleScores scaleScores;
	private ProficiencyLevels proficiencyLevels;
	private ReferenceNormalCurveEquivalents referenceNormalCurveEquivalent;
	private ReferencePercentileRanks referencePercentileRanks;
	private SkillAreaNumberCorrect skillAreaNumberCorrect;
	private SkillAreaPercentCorrect skillAreaPercentCorrect;
	private SubSkillNumberCorrect subSkillNumberCorrect;
	private SubSkillPercentCorrect subSkillPercentCorrect;
	private ItemResponsesGRT itemResponseGRT;

	/**
	 * @return the modelLevel
	 */
	@PositionalField(initialPosition = 1, finalPosition = 1)
	public String getModelLevel() {
		return modelLevel;
	}

	/**
	 * @param modelLevel
	 *            the modelLevel to set
	 */
	public void setModelLevel(String modelLevel) {
		this.modelLevel = modelLevel;
	}

	/**
	 * @return the organizationId
	 */
	@PositionalField(initialPosition = 2, finalPosition = 11)
	public String getOrganizationId() {

		return organizationId;
	}

	/**
	 * @param organizationId
	 *            the organizationId to set
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the elementNameA
	 */
	@PositionalField(initialPosition = 12, finalPosition = 41)
	public String getElementNameA() {
		return elementNameA;
	}

	/**
	 * @param elementNameA
	 *            the elementNameA to set
	 */
	public void setElementNameA(String elementNameA) {
		this.elementNameA = elementNameA;
	}

	/**
	 * @return the elementStructureLevelA
	 */
	@PositionalField(initialPosition = 42, finalPosition = 43)
	public String getElementStructureLevelA() {
		return elementStructureLevelA;
	}

	/**
	 * @param elementStructureLevelA
	 *            the elementStructureLevelA to set
	 */
	public void setElementStructureLevelA(String elementStructureLevelA) {
		this.elementStructureLevelA = elementStructureLevelA;
	}

	/**
	 * @return the elementNumberA
	 */
	@PositionalField(initialPosition = 44, finalPosition = 50)
	public String getElementNumberA() {

		return elementNumberA;
	}

	/**
	 * @param elementNumberA
	 *            the elementNumberA to set
	 */
	public void setElementNumberA(String elementNumberA) {
		elementNumberA = String.format("%7s", elementNumberA).replace(' ', '0');
		this.elementNumberA = elementNumberA;
	}

	/**
	 * @return the elementSpecialCodesA
	 */
	@PositionalField(initialPosition = 51, finalPosition = 60)
	public String getElementSpecialCodesA() {
		return elementSpecialCodesA;
	}

	/**
	 * @param elementSpecialCodesA
	 *            the elementSpecialCodesA to set
	 */
	public void setElementSpecialCodesA(String elementSpecialCodesA) {
		this.elementSpecialCodesA = elementSpecialCodesA;
	}

	/**
	 * @return the customerId
	 */
	@PositionalField(initialPosition = 61, finalPosition = 68)
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the remainderOfElementSpecialCodesA
	 */
	@PositionalField(initialPosition = 69, finalPosition = 76)
	public String getRemainderOfElementSpecialCodesA() {
		return remainderOfElementSpecialCodesA;
	}

	/**
	 * @param remainderOfElementSpecialCodesA
	 *            the remainderOfElementSpecialCodesA to set
	 */
	public void setRemainderOfElementSpecialCodesA(
			String remainderOfElementSpecialCodesA) {
		this.remainderOfElementSpecialCodesA = remainderOfElementSpecialCodesA;
	}

	/**
	 * @return the grade
	 */
	@PositionalField(initialPosition = 77, finalPosition = 78)
	public String getGrade() {

		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		grade = String.format("%2s", grade).replace(' ', '0');
		this.grade = grade;
	}

	/**
	 * @return the city
	 */
	@PositionalField(initialPosition = 79, finalPosition = 108)
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	@PositionalField(initialPosition = 109, finalPosition = 110)
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the elementNameB
	 */
	@PositionalField(initialPosition = 111, finalPosition = 140)
	public String getElementNameB() {
		return elementNameB;
	}

	/**
	 * @param elementNameB
	 *            the elementNameB to set
	 */
	public void setElementNameB(String elementNameB) {
		this.elementNameB = elementNameB;
	}

	/**
	 * @return the elementStructureLevelB
	 */
	@PositionalField(initialPosition = 141, finalPosition = 142)
	public String getElementStructureLevelB() {
		return elementStructureLevelB;
	}

	/**
	 * @param elementStructureLevelB
	 *            the elementStructureLevelB to set
	 */
	public void setElementStructureLevelB(String elementStructureLevelB) {
		this.elementStructureLevelB = elementStructureLevelB;
	}

	/**
	 * @return the elementNumberB
	 */
	@PositionalField(initialPosition = 143, finalPosition = 149)
	public String getElementNumberB() {

		return elementNumberB;
	}

	/**
	 * @param elementNumberB
	 *            the elementNumberB to set
	 */
	public void setElementNumberB(String elementNumberB) {

		elementNumberB = String.format("%7s", elementNumberB).replace(' ', '0');
		this.elementNumberB = elementNumberB;
	}

	/**
	 * @return the elementSpecialCodesB
	 */
	@PositionalField(initialPosition = 150, finalPosition = 159)
	public String getElementSpecialCodesB() {
		return elementSpecialCodesB;
	}

	/**
	 * @param elementSpecialCodesB
	 *            the elementSpecialCodesB to set
	 */
	public void setElementSpecialCodesB(String elementSpecialCodesB) {
		this.elementSpecialCodesB = elementSpecialCodesB;
	}

	/**
	 * @return the schoolId
	 */
	@PositionalField(initialPosition = 160, finalPosition = 167)
	public String getSchoolId() {
		return schoolId;
	}

	/**
	 * @param schoolId
	 *            the schoolId to set
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	/**
	 * @return the remainderOfElementSpecialCodesB
	 */
	@PositionalField(initialPosition = 168, finalPosition = 175)
	public String getRemainderOfElementSpecialCodesB() {
		return remainderOfElementSpecialCodesB;
	}

	/**
	 * @param remainderOfElementSpecialCodesB
	 *            the remainderOfElementSpecialCodesB to set
	 */
	public void setRemainderOfElementSpecialCodesB(
			String remainderOfElementSpecialCodesB) {
		this.remainderOfElementSpecialCodesB = remainderOfElementSpecialCodesB;
	}

	/**
	 * @return the elementNameC
	 */
	@PositionalField(initialPosition = 176, finalPosition = 205)
	public String getElementNameC() {
		return elementNameC;
	}

	/**
	 * @param elementNameC
	 *            the elementNameC to set
	 */
	public void setElementNameC(String elementNameC) {
		this.elementNameC = elementNameC;
	}

	/**
	 * @return the elementStructureLevelC
	 */
	@PositionalField(initialPosition = 206, finalPosition = 207)
	public String getElementStructureLevelC() {
		return elementStructureLevelC;
	}

	/**
	 * @param elementStructureLevelC
	 *            the elementStructureLevelC to set
	 */
	public void setElementStructureLevelC(String elementStructureLevelC) {
		this.elementStructureLevelC = elementStructureLevelC;
	}

	/**
	 * @return the elementNumberC
	 */
	@PositionalField(initialPosition = 208, finalPosition = 214)
	public String getElementNumberC() {

		return elementNumberC;
	}

	/**
	 * @param elementNumberC
	 *            the elementNumberC to set
	 */
	public void setElementNumberC(String elementNumberC) {
		elementNumberC = String.format("%7s", elementNumberC).replace(' ', '0');
		this.elementNumberC = elementNumberC;
	}

	/**
	 * @return the elementSpecialCodesC
	 */
	@PositionalField(initialPosition = 215, finalPosition = 240)
	public String getElementSpecialCodesC() {
		return elementSpecialCodesC;
	}

	/**
	 * @param elementSpecialCodesC
	 *            the elementSpecialCodesC to set
	 */
	public void setElementSpecialCodesC(String elementSpecialCodesC) {
		this.elementSpecialCodesC = elementSpecialCodesC;
	}

	/**
	 * @return the testName
	 */
	@PositionalField(initialPosition = 241, finalPosition = 270)
	public String getTestName() {
		return testName;
	}

	/**
	 * @param testName
	 *            the testName to set
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}

	/**
	 * @return the testForm
	 */
	@PositionalField(initialPosition = 271, finalPosition = 272)
	public String getTestForm() {
		return testForm;
	}

	/**
	 * @param testForm
	 *            the testForm to set
	 */
	public void setTestForm(String testForm) {
		this.testForm = testForm;
	}

	/**
	 * @return the testLevel
	 */
	@PositionalField(initialPosition = 273, finalPosition = 274)
	public String getTestLevel() {
		return testLevel;
	}

	/**
	 * @param testLevel
	 *            the testLevel to set
	 */
	public void setTestLevel(String testLevel) {
		this.testLevel = testLevel;
	}

	/**
	 * @return the testDate
	 */
	@PositionalField(initialPosition = 275, finalPosition = 280)
	public String getTestDate() {
		return testDate;
	}

	/**
	 * @param testDate
	 *            the testDate to set
	 */
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	/**
	 * @return the scoringType
	 */
	@PositionalField(initialPosition = 281, finalPosition = 281)
	public String getScoringType() {
		return scoringType;
	}

	/**
	 * @param scoringType
	 *            the scoringType to set
	 */
	public void setScoringType(String scoringType) {
		this.scoringType = scoringType;
	}

	/**
	 * @return the testHierarchyUnused
	 */
	@PositionalField(initialPosition = 282, finalPosition = 297)
	public String getTestHierarchyUnused() {
		return testHierarchyUnused;
	}

	/**
	 * @param testHierarchyUnused
	 *            the testHierarchyUnused to set
	 */
	public void setTestHierarchyUnused(String testHierarchyUnused) {
		this.testHierarchyUnused = testHierarchyUnused;
	}

	/**
	 * @return the studentElementNumber
	 */
	@PositionalField(initialPosition = 298, finalPosition = 304)
	public String getStudentElementNumber() {
		return studentElementNumber;
	}

	/**
	 * @param studentElementNumber
	 *            the studentElementNumber to set
	 */
	public void setStudentElementNumber(String studentElementNumber) {
		studentElementNumber = String.format("%7s", studentElementNumber)
				.replace(' ', '0');
		;
		this.studentElementNumber = studentElementNumber;
	}

	/**
	 * @return the studentLastName
	 */
	@PositionalField(initialPosition = 305, finalPosition = 324)
	public String getStudentLastName() {
		return studentLastName;
	}

	/**
	 * @param studentLastName
	 *            the studentLastName to set
	 */
	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	/**
	 * @return the studentFirstName
	 */
	@PositionalField(initialPosition = 325, finalPosition = 339)
	public String getStudentFirstName() {
		return studentFirstName;
	}

	/**
	 * @param studentFirstName
	 *            the studentFirstName to set
	 */
	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	/**
	 * @return the studentMiddleName
	 */
	@PositionalField(initialPosition = 340, finalPosition = 340)
	public String getStudentMiddleName() {
		return studentMiddleName;
	}

	/**
	 * @param studentMiddleName
	 *            the studentMiddleName to set
	 */
	public void setStudentMiddleName(String studentMiddleName) {
		this.studentMiddleName = studentMiddleName;
	}

	/**
	 * @return the studentBirthDate
	 */
	@PositionalField(initialPosition = 341, finalPosition = 346)
	public String getStudentBirthDate() {

		return studentBirthDate;
	}

	/**
	 * @param studentBirthDate
	 *            the studentBirthDate to set
	 */
	public void setStudentBirthDate(Date studentBirthDate) {
		// String dateString = new String(studentBirthDate.toString());
		String dtDate;
		SimpleDateFormat sdfop = new SimpleDateFormat("MMddyy");
		// SimpleDateFormat sdfip = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dtDate = sdfop.format(studentBirthDate);
		} catch (Exception e) {
			dtDate = studentBirthDate.toString();
		}
		this.studentBirthDate = dtDate;
	}

	/**
	 * @return the studentChronologicalAge
	 */
	@PositionalField(initialPosition = 347, finalPosition = 349)
	public String getStudentChronologicalAge() {
		return studentChronologicalAge;
	}

	/**
	 * @param studentChronologicalAge
	 *            the studentChronologicalAge to set
	 */
	public void setStudentChronologicalAge(String studentChronologicalAge) {
		this.studentChronologicalAge = studentChronologicalAge;
	}

	/**
	 * @return the studentGender
	 */
	@PositionalField(initialPosition = 350, finalPosition = 350)
	public String getStudentGender() {
		return studentGender;
	}

	/**
	 * @param studentGender
	 *            the studentGender to set
	 */
	public void setStudentGender(String studentGender) {
		this.studentGender = studentGender;
	}

	/**
	 * @return the extStudentId
	 */
	@PositionalField(initialPosition = 351, finalPosition = 360)
	public String getExtStudentId() {
		return extStudentId;
	}

	/**
	 * @param extStudentId
	 *            the extStudentId to set
	 */
	public void setExtStudentId(String extStudentId) {
		this.extStudentId = extStudentId;
	}

	/**
	 * @return the ethinicity
	 */
	@PositionalField(initialPosition = 361, finalPosition = 361)
	public String getEthinicity() {
		return ethinicity;
	}

	/**
	 * @param ethinicity
	 *            the ethinicity to set
	 */
	public void setEthinicity(String ethinicity) {
		this.ethinicity = ethinicity;
	}

	/**
	 * @return the homeLanguage
	 */
	@PositionalField(initialPosition = 362, finalPosition = 363)
	public String getHomeLanguage() {
		return homeLanguage;
	}

	/**
	 * @param homeLanguage
	 *            the homeLanguage to set
	 */
	public void setHomeLanguage(String homeLanguage) {
		this.homeLanguage = homeLanguage;
	}

	/**
	 * @return the purposeOfTestp
	 */
	@PositionalField(initialPosition = 364, finalPosition = 364)
	public String getPurposeOfTestp() {
		return purposeOfTestp;
	}

	/**
	 * @param purposeOfTestp
	 *            the purposeOfTestp to set
	 */
	public void setPurposeOfTestp(String purposeOfTestp) {
		this.purposeOfTestp = purposeOfTestp;
	}

	/**
	 * @return the eseaTitle1
	 */
	@PositionalField(initialPosition = 365, finalPosition = 365)
	public String getEseaTitle1() {
		return eseaTitle1;
	}

	/**
	 * @param eseaTitle1
	 *            the eseaTitle1 to set
	 */
	public void setEseaTitle1(String eseaTitle1) {
		this.eseaTitle1 = eseaTitle1;
	}

	/**
	 * @return the ellEseaTitle
	 */
	@PositionalField(initialPosition = 366, finalPosition = 366)
	public String getEllEseaTitle() {
		return ellEseaTitle;
	}

	/**
	 * @param ellEseaTitle
	 *            the ellEseaTitle to set
	 */
	public void setEllEseaTitle(String ellEseaTitle) {
		this.ellEseaTitle = ellEseaTitle;
	}

	/**
	 * @return the giftedAndTalented
	 */
	@PositionalField(initialPosition = 367, finalPosition = 367)
	public String getGiftedAndTalented() {
		return giftedAndTalented;
	}

	/**
	 * @param giftedAndTalented
	 *            the giftedAndTalented to set
	 */
	public void setGiftedAndTalented(String giftedAndTalented) {
		this.giftedAndTalented = giftedAndTalented;
	}

	/**
	 * @return the indianEducation
	 */
	@PositionalField(initialPosition = 368, finalPosition = 368)
	public String getIndianEducation() {
		return indianEducation;
	}

	/**
	 * @param indianEducation
	 *            the indianEducation to set
	 */
	public void setIndianEducation(String indianEducation) {
		this.indianEducation = indianEducation;
	}

	/**
	 * @return the migrantEducation
	 */
	@PositionalField(initialPosition = 369, finalPosition = 369)
	public String getMigrantEducation() {
		return migrantEducation;
	}

	/**
	 * @param migrantEducation
	 *            the migrantEducation to set
	 */
	public void setMigrantEducation(String migrantEducation) {
		this.migrantEducation = migrantEducation;
	}

	/**
	 * @return the iep
	 */
	@PositionalField(initialPosition = 370, finalPosition = 370)
	public String getIep() {
		return iep;
	}

	/**
	 * @param iep
	 *            the iep to set
	 */
	public void setIep(String iep) {
		this.iep = iep;
	}

	/**
	 * @return the sbi504
	 */
	@PositionalField(initialPosition = 371, finalPosition = 371)
	public String getSbi504() {
		return sbi504;
	}

	/**
	 * @param sbi504
	 *            the sbi504 to set
	 */
	public void setSbi504(String sbi504) {
		this.sbi504 = sbi504;
	}

	/**
	 * @return the disability
	 */
	@PositionalField(initialPosition = 372, finalPosition = 372)
	public String getDisability() {
		return disability;
	}

	/**
	 * @param disability
	 *            the disability to set
	 */
	public void setDisability(String disability) {
		this.disability = disability;
	}

	/**
	 * @return the ctbUseColumns
	 */
	@PositionalField(initialPosition = 373, finalPosition = 376)
	public String getCtbUseColumns() {
		return ctbUseColumns;
	}

	/**
	 * @param ctbUseColumns
	 *            the ctbUseColumns to set
	 */
	public void setCtbUseColumns(String ctbUseColumns) {
		this.ctbUseColumns = ctbUseColumns;
	}

	/**
	 * @return the sbiUnused
	 */
	@PositionalField(initialPosition = 377, finalPosition = 386)
	public String getSbiUnused() {
		return sbiUnused;
	}

	/**
	 * @param sbiUnused
	 *            the sbiUnused to set
	 */
	public void setSbiUnused(String sbiUnused) {
		this.sbiUnused = sbiUnused;
	}

	/**
	 * @return the studentGradeFromAnswerSheets
	 */
	@PositionalField(initialPosition = 387, finalPosition = 388)
	public String getStudentGradeFromAnswerSheets() {
		return studentGradeFromAnswerSheets;
	}

	/**
	 * @param studentGradeFromAnswerSheets
	 *            the studentGradeFromAnswerSheets to set
	 */
	public void setStudentGradeFromAnswerSheets(
			String studentGradeFromAnswerSheets) {
		studentGradeFromAnswerSheets = String.format("%2s",
				studentGradeFromAnswerSheets).replace(' ', '0');
		this.studentGradeFromAnswerSheets = studentGradeFromAnswerSheets;
	}

	/**
	 * @return the dateTestingCompleted
	 */
	@PositionalField(initialPosition = 389, finalPosition = 396)
	public String getDateTestingCompleted() {
		return dateTestingCompleted;
	}

	/**
	 * @param dateTestingCompleted
	 *            the dateTestingCompleted to set
	 */
	public void setDateTestingCompleted(String dateTestingCompleted) {
		this.dateTestingCompleted = dateTestingCompleted;
	}

	/**
	 * @return the testInvalidationSpeaking
	 */
	@PositionalField(initialPosition = 397, finalPosition = 397)
	public String getTestInvalidationSpeaking() {
		return testInvalidationSpeaking;
	}

	/**
	 * @param testInvalidationSpeaking
	 *            the testInvalidationSpeaking to set
	 */
	public void setTestInvalidationSpeaking(String testInvalidationSpeaking) {

		this.testInvalidationSpeaking = testInvalidationSpeaking;
	}

	/**
	 * @return the testInvalidationListening
	 */
	@PositionalField(initialPosition = 398, finalPosition = 398)
	public String getTestInvalidationListening() {
		return testInvalidationListening;
	}

	/**
	 * @param testInvalidationListening
	 *            the testInvalidationListening to set
	 */
	public void setTestInvalidationListening(String testInvalidationListening) {
		this.testInvalidationListening = testInvalidationListening;
	}

	/**
	 * @return the testInvalidationReading
	 */
	@PositionalField(initialPosition = 399, finalPosition = 399)
	public String getTestInvalidationReading() {
		return testInvalidationReading;
	}

	/**
	 * @param testInvalidationReading
	 *            the testInvalidationReading to set
	 */
	public void setTestInvalidationReading(String testInvalidationReading) {
		this.testInvalidationReading = testInvalidationReading;
	}

	/**
	 * @return the testInvalidationWriting
	 */
	@PositionalField(initialPosition = 400, finalPosition = 400)
	public String getTestInvalidationWriting() {
		return testInvalidationWriting;
	}

	/**
	 * @param testInvalidationWriting
	 *            the testInvalidationWriting to set
	 */
	public void setTestInvalidationWriting(String testInvalidationWriting) {
		this.testInvalidationWriting = testInvalidationWriting;
	}

	/**
	 * @return the testingExemptionsSpeaking
	 */
	@PositionalField(initialPosition = 401, finalPosition = 401)
	public String getTestingExemptionsSpeaking() {
		return testingExemptionsSpeaking;
	}

	/**
	 * @param testingExemptionsSpeaking
	 *            the testingExemptionsSpeaking to set
	 */
	public void setTestingExemptionsSpeaking(String testingExemptionsSpeaking) {
		this.testingExemptionsSpeaking = testingExemptionsSpeaking;
	}

	/**
	 * @return the testingExemptionsListening
	 */
	@PositionalField(initialPosition = 402, finalPosition = 402)
	public String getTestingExemptionsListening() {
		return testingExemptionsListening;
	}

	/**
	 * @param testingExemptionsListening
	 *            the testingExemptionsListening to set
	 */
	public void setTestingExemptionsListening(String testingExemptionsListening) {
		this.testingExemptionsListening = testingExemptionsListening;
	}

	/**
	 * @return the testingExemptionsReading
	 */
	@PositionalField(initialPosition = 403, finalPosition = 403)
	public String getTestingExemptionsReading() {
		return testingExemptionsReading;
	}

	/**
	 * @param testingExemptionsReading
	 *            the testingExemptionsReading to set
	 */
	public void setTestingExemptionsReading(String testingExemptionsReading) {
		this.testingExemptionsReading = testingExemptionsReading;
	}

	/**
	 * @return the testingExemptionsWriting
	 */
	@PositionalField(initialPosition = 404, finalPosition = 404)
	public String getTestingExemptionsWriting() {
		return testingExemptionsWriting;
	}

	/**
	 * @param testingExemptionsWriting
	 *            the testingExemptionsWriting to set
	 */
	public void setTestingExemptionsWriting(String testingExemptionsWriting) {
		this.testingExemptionsWriting = testingExemptionsWriting;
	}

	/**
	 * @return the absentSpeaking
	 */
	@PositionalField(initialPosition = 405, finalPosition = 405)
	public String getAbsentSpeaking() {
		return absentSpeaking;
	}

	/**
	 * @param absentSpeaking
	 *            the absentSpeaking to set
	 */
	public void setAbsentSpeaking(String absentSpeaking) {
		this.absentSpeaking = absentSpeaking;
	}

	/**
	 * @return the absentListening
	 */
	@PositionalField(initialPosition = 406, finalPosition = 406)
	public String getAbsentListening() {
		return absentListening;
	}

	/**
	 * @param absentListening
	 *            the absentListening to set
	 */
	public void setAbsentListening(String absentListening) {
		this.absentListening = absentListening;
	}

	/**
	 * @return the absentReading
	 */
	@PositionalField(initialPosition = 407, finalPosition = 407)
	public String getAbsentReading() {
		return absentReading;
	}

	/**
	 * @param absentReading
	 *            the absentReading to set
	 */
	public void setAbsentReading(String absentReading) {
		this.absentReading = absentReading;
	}

	/**
	 * @return the absentWriting
	 */
	@PositionalField(initialPosition = 408, finalPosition = 408)
	public String getAbsentWriting() {
		return absentWriting;
	}

	/**
	 * @param absentWriting
	 *            the absentWriting to set
	 */
	public void setAbsentWriting(String absentWriting) {
		this.absentWriting = absentWriting;
	}

	/**
	 * @return the usaSchoolEnrollment
	 */
	@PositionalField(initialPosition = 409, finalPosition = 412)
	public String getUsaSchoolEnrollment() {
		return usaSchoolEnrollment;
	}

	/**
	 * @param usaSchoolEnrollment
	 *            the usaSchoolEnrollment to set
	 */
	public void setUsaSchoolEnrollment(String usaSchoolEnrollment) {
		this.usaSchoolEnrollment = usaSchoolEnrollment;
	}

	/**
	 * @return the mobilityGrade
	 */
	@PositionalField(initialPosition = 413, finalPosition = 414)
	public String getMobilityGrade() {
		return mobilityGrade;
	}

	/**
	 * @param mobilityGrade
	 *            the mobilityGrade to set
	 */
	public void setMobilityGrade(String mobilityGrade) {
		this.mobilityGrade = mobilityGrade;
	}

	/**
	 * @return the accomodations
	 */
	@PositionalField(initialPosition = 415, finalPosition = 442)
	public Accomodations getAccomodations() {
		return accomodations;
	}

	/**
	 * @param accomodations
	 *            the accomodations to set
	 */
	public void setAccomodations(Accomodations accomodations) {
		this.accomodations = accomodations;
	}

	/**
	 * @return the userDefinedBioPage
	 */
	@PositionalField(initialPosition = 443, finalPosition = 452)
	public SpecialCodes getUserDefinedBioPage() {
		return userDefinedBioPage;
	}

	/**
	 * @param userDefinedBioPage
	 *            the userDefinedBioPage to set
	 */
	public void setUserDefinedBioPage(SpecialCodes userDefinedBioPage) {
		this.userDefinedBioPage = userDefinedBioPage;
	}

	/**
	 * @return the sbiCtbUseColumns
	 */
	@PositionalField(initialPosition = 453, finalPosition = 506)
	public String getSbiCtbUseColumns() {
		return sbiCtbUseColumns;
	}

	/**
	 * @param sbiCtbUseColumns
	 *            the sbiCtbUseColumns to set
	 */
	public void setSbiCtbUseColumns(String sbiCtbUseColumns) {
		this.sbiCtbUseColumns = sbiCtbUseColumns;
	}

	/**
	 * @return the scaleScores
	 */
	@PositionalField(initialPosition = 507, finalPosition = 527)
	public ScaleScores getScaleScores() {
		return scaleScores;
	}

	/**
	 * @param scaleScores
	 *            the scaleScores to set
	 */
	public void setScaleScores(ScaleScores scaleScores) {
		this.scaleScores = scaleScores;
	}

	/**
	 * @return the proficiencyLevels
	 */
	@PositionalField(initialPosition = 528, finalPosition = 534)
	public ProficiencyLevels getProficiencyLevels() {
		return proficiencyLevels;
	}

	/**
	 * @param proficiencyLevels
	 *            the proficiencyLevels to set
	 */
	public void setProficiencyLevels(ProficiencyLevels proficiencyLevels) {
		this.proficiencyLevels = proficiencyLevels;
	}

	/**
	 * @return the referenceNormalCurveEquivalent
	 */
	@PositionalField(initialPosition = 535, finalPosition = 548)
	public ReferenceNormalCurveEquivalents getReferenceNormalCurveEquivalent() {
		return referenceNormalCurveEquivalent;
	}

	/**
	 * @param referenceNormalCurveEquivalent
	 *            the referenceNormalCurveEquivalent to set
	 */
	public void setReferenceNormalCurveEquivalent(
			ReferenceNormalCurveEquivalents referenceNormalCurveEquivalent) {
		this.referenceNormalCurveEquivalent = referenceNormalCurveEquivalent;
	}

	/**
	 * @return the referencePercentileRanks
	 */
	@PositionalField(initialPosition = 549, finalPosition = 562)
	public ReferencePercentileRanks getReferencePercentileRanks() {
		return referencePercentileRanks;
	}

	/**
	 * @param referencePercentileRanks
	 *            the referencePercentileRanks to set
	 */
	public void setReferencePercentileRanks(
			ReferencePercentileRanks referencePercentileRanks) {
		this.referencePercentileRanks = referencePercentileRanks;
	}

	/**
	 * @return the skillAreaNumberCorrect
	 */
	@PositionalField(initialPosition = 563, finalPosition = 583)
	public SkillAreaNumberCorrect getSkillAreaNumberCorrect() {
		return skillAreaNumberCorrect;
	}

	/**
	 * @param skillAreaNumberCorrect
	 *            the skillAreaNumberCorrect to set
	 */
	public void setSkillAreaNumberCorrect(
			SkillAreaNumberCorrect skillAreaNumberCorrect) {
		this.skillAreaNumberCorrect = skillAreaNumberCorrect;
	}

	/**
	 * @return the skillAreaPercentCorrect
	 */
	@PositionalField(initialPosition = 584, finalPosition = 618)
	public SkillAreaPercentCorrect getSkillAreaPercentCorrect() {
		return skillAreaPercentCorrect;
	}

	/**
	 * @param skillAreaPercentCorrect
	 *            the skillAreaPercentCorrect to set
	 */
	public void setSkillAreaPercentCorrect(
			SkillAreaPercentCorrect skillAreaPercentCorrect) {
		this.skillAreaPercentCorrect = skillAreaPercentCorrect;
	}

	/**
	 * @return the subSkillNumberCorrect
	 */
	@PositionalField(initialPosition = 619, finalPosition = 663)
	public SubSkillNumberCorrect getSubSkillNumberCorrect() {
		return subSkillNumberCorrect;
	}

	/**
	 * @param subSkillNumberCorrect
	 *            the subSkillNumberCorrect to set
	 */
	public void setSubSkillNumberCorrect(
			SubSkillNumberCorrect subSkillNumberCorrect) {
		this.subSkillNumberCorrect = subSkillNumberCorrect;
	}

	/**
	 * @return the subSkillPercentCorrect
	 */
	@PositionalField(initialPosition = 664, finalPosition = 798)
	public SubSkillPercentCorrect getSubSkillPercentCorrect() {
		return subSkillPercentCorrect;
	}

	/**
	 * @param subSkillPercentCorrect
	 *            the subSkillPercentCorrect to set
	 */
	public void setSubSkillPercentCorrect(
			SubSkillPercentCorrect subSkillPercentCorrect) {
		this.subSkillPercentCorrect = subSkillPercentCorrect;
	}

	/**
	 * @return the itemResponseGRT
	 */
	@PositionalField(initialPosition = 799, finalPosition = 898)
	public ItemResponsesGRT getItemResponseGRT() {
		return itemResponseGRT;
	}

	/**
	 * @param itemResponseGRT
	 *            the itemResponseGRT to set
	 */
	public void setItemResponseGRT(ItemResponsesGRT itemResponseGRT) {
		this.itemResponseGRT = itemResponseGRT;
	}

	/**
	 * @param studentBirthDate
	 *            the studentBirthDate to set
	 */
	public void setStudentBirthDate(String studentBirthDate) {
		this.studentBirthDate = studentBirthDate;
	}

	/**
	 * @return the specialCodeK
	 */
	public String getSpecialCodeK() {
		return specialCodeK;
	}

	/**
	 * @param specialCodeK
	 *            the specialCodeK to set
	 */
	public void setSpecialCodeK(String specialCodeK) {
		this.specialCodeK = specialCodeK;
	}

	/**
	 * @return the specialCodeL
	 */
	public String getSpecialCodeL() {
		return specialCodeL;
	}

	/**
	 * @param specialCodeL
	 *            the specialCodeL to set
	 */
	public void setSpecialCodeL(String specialCodeL) {
		this.specialCodeL = specialCodeL;
	}

	/**
	 * @return the specialCodeM
	 */
	public String getSpecialCodeM() {
		return specialCodeM;
	}

	/**
	 * @param specialCodeM
	 *            the specialCodeM to set
	 */
	public void setSpecialCodeM(String specialCodeM) {
		this.specialCodeM = specialCodeM;
	}

	/**
	 * @return the specialCodeN
	 */
	public String getSpecialCodeN() {
		return specialCodeN;
	}

	/**
	 * @param specialCodeN
	 *            the specialCodeN to set
	 */
	public void setSpecialCodeN(String specialCodeN) {
		this.specialCodeN = specialCodeN;
	}

	/**
	 * @return the specialCodeO
	 */
	public String getSpecialCodeO() {
		return specialCodeO;
	}

	/**
	 * @param specialCodeO
	 *            the specialCodeO to set
	 */
	public void setSpecialCodeO(String specialCodeO) {
		this.specialCodeO = specialCodeO;
	}

	/**
	 * @return the specialCodeP
	 */
	public String getSpecialCodeP() {
		return specialCodeP;
	}

	/**
	 * @param specialCodeP
	 *            the specialCodeP to set
	 */
	public void setSpecialCodeP(String specialCodeP) {
		this.specialCodeP = specialCodeP;
	}

	/**
	 * @return the specialCodeQ
	 */
	public String getSpecialCodeQ() {
		return specialCodeQ;
	}

	/**
	 * @param specialCodeQ
	 *            the specialCodeQ to set
	 */
	public void setSpecialCodeQ(String specialCodeQ) {
		this.specialCodeQ = specialCodeQ;
	}

	/**
	 * @return the specialCodeR
	 */
	public String getSpecialCodeR() {
		return specialCodeR;
	}

	/**
	 * @param specialCodeR
	 *            the specialCodeR to set
	 */
	public void setSpecialCodeR(String specialCodeR) {
		this.specialCodeR = specialCodeR;
	}

	/**
	 * @return the specialCodeS
	 */
	public String getSpecialCodeS() {
		return specialCodeS;
	}

	/**
	 * @param specialCodeS
	 *            the specialCodeS to set
	 */
	public void setSpecialCodeS(String specialCodeS) {
		this.specialCodeS = specialCodeS;
	}

	/**
	 * @return the specialCodeT
	 */
	public String getSpecialCodeT() {
		return specialCodeT;
	}

	/**
	 * @param specialCodeT the specialCodeT to set
	 */
	public void setSpecialCodeT(String specialCodeT) {
		this.specialCodeT = specialCodeT;
	}

	/**
	 * @return the classId
	 */
	public String getClassId() {
		return classId;
	}

	/**
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}

}
