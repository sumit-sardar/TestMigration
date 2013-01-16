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
	private String testRosterId = "";
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
	
	//Addition for eMetrict 7 level extend  
	private String testType;
	private String elementALabel;
	private String elementBLabel;
	private String elementCLabel;
	private String remainderOfElementSpecialCodeC;
	private String elementNameD;
	private String elementDLabel;
	private String elementStructureLevelD;
	private String elementNumberD;
	private String elementSpecialCodesD;
	private String sectionId;
	private String remainderOfElementSpecialCodesD;
	private String elementNameE;
	private String elementELabel;
	private String elementStructureLevelE;
	private String elementNumberE;
	private String elementSpecialCodesE;
	private String groupId;
	private String remainderOfElementSpecialCodesE;
	private String elementNameF;
	private String elementFLabel;
	private String elementStructureLevelF;
	private String elementNumberF;
	private String elementSpecialCodesF;
	private String divisionId;
	private String remainderOfElementSpecialCodesF;
	private String elementNameG;
	private String elementGLabel;
	private String elementStructureLevelG;
	private String elementNumberG;
	private String elementSpecialCodesG;
	private String leafLevelId;
	private String remainderOfElementSpecialCodesG;
	private String subtestIndicatorFlag;
	
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
	 * @return the testType
	 */
	//@PositionalField(initialPosition = 9, finalPosition = 11)
	public String getTestType() {
		return testType;
	}

	/**
	 * @param testType the testType to set
	 */
	public void setTestType(String testType) {
		this.testType = testType;
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
	
	@PositionalField(initialPosition = 42, finalPosition = 56)
	/**
	 * @return the elementALabel
	 */
	public String getElementALabel() {
		return elementALabel;
	}

	/**
	 * @param elementALabel the elementALabel to set
	 */
	public void setElementALabel(String elementALabel) {
		this.elementALabel = elementALabel;
	}
	/**
	 * @return the elementStructureLevelA
	 */
	@PositionalField(initialPosition = 57, finalPosition = 58)
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
	@PositionalField(initialPosition = 59, finalPosition = 65)
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
	@PositionalField(initialPosition = 66, finalPosition = 75)
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
	@PositionalField(initialPosition = 76, finalPosition = 83)
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@PositionalField(initialPosition = 84, finalPosition = 91)
	public String getRemainderOfElementSpecialCodesA() {
		return remainderOfElementSpecialCodesA;
	}

	public void setRemainderOfElementSpecialCodesA(
			String remainderOfElementSpecialCodesA) {
		this.remainderOfElementSpecialCodesA = remainderOfElementSpecialCodesA;
	}

	/**
	 * @return the grade
	 */
	@PositionalField(initialPosition = 92, finalPosition = 93)
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
	@PositionalField(initialPosition = 94, finalPosition = 123)
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
	@PositionalField(initialPosition = 124, finalPosition = 125)
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
	@PositionalField(initialPosition = 126, finalPosition = 155)
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
	 * @return the elementBLabel
	 */
	@PositionalField(initialPosition = 156, finalPosition = 170)
	public String getElementBLabel() {
		return elementBLabel;
	}

	/**
	 * @param elementBLabel the elementBLabel to set
	 */
	public void setElementBLabel(String elementBLabel) {
		this.elementBLabel = elementBLabel;
	}

	/**
	 * @return the elementStructureLevelB
	 */
	@PositionalField(initialPosition = 171, finalPosition = 172)
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
	@PositionalField(initialPosition = 173, finalPosition = 179)
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
	@PositionalField(initialPosition = 180, finalPosition = 189)
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
	@PositionalField(initialPosition = 190, finalPosition = 197)
	public String getSchoolId() {
		return schoolId;
	}

	/**
	 * @param schoolId the schoolId to set
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	@PositionalField(initialPosition = 198, finalPosition = 205)
	public String getRemainderOfElementSpecialCodesB() {
		return remainderOfElementSpecialCodesB;
	}

	public void setRemainderOfElementSpecialCodesB(
			String remainderOfElementSpecialCodesB) {
		this.remainderOfElementSpecialCodesB = remainderOfElementSpecialCodesB;
	}

	/**
	 * @return the elementNameC
	 */
	@PositionalField(initialPosition = 206, finalPosition = 235)
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
	 * @return the elementCLabel
	 */
	@PositionalField(initialPosition = 236, finalPosition = 250)
	public String getElementCLabel() {
		return elementCLabel;
	}

	/**
	 * @param elementCLabel the elementCLabel to set
	 */
	public void setElementCLabel(String elementCLabel) {
		this.elementCLabel = elementCLabel;
	}

	/**
	 * @return the elementStructureLevelC
	 */
	@PositionalField(initialPosition = 251, finalPosition = 252)
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
	@PositionalField(initialPosition = 253, finalPosition = 259)
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
	@PositionalField(initialPosition = 260, finalPosition = 269)
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
	 * @return the classId
	 */
	@PositionalField(initialPosition = 270, finalPosition = 277)
	public String getClassId() {
		return classId;
	}

	/**
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	@PositionalField(initialPosition = 278, finalPosition = 285)
	public String getRemainderOfElementSpecialCodeC() {
		return remainderOfElementSpecialCodeC;
	}

	public void setRemainderOfElementSpecialCodeC(
			String remainderOfElementSpecialCodeC) {
		this.remainderOfElementSpecialCodeC = remainderOfElementSpecialCodeC;
	}

	@PositionalField(initialPosition = 286, finalPosition = 315)
	public String getElementNameD() {
		return elementNameD;
	}

	public void setElementNameD(String elementNameD) {
		this.elementNameD = elementNameD;
	}
		
	/**
	 * @return the elementDLabel
	 */
	@PositionalField(initialPosition = 316, finalPosition = 330)
	public String getElementDLabel() {
		return elementDLabel;
	}

	/**
	 * @param elementDLabel the elementDLabel to set
	 */
	public void setElementDLabel(String elementDLabel) {
		this.elementDLabel = elementDLabel;
	}

	@PositionalField(initialPosition = 331, finalPosition = 332)
	public String getElementStructureLevelD() {
		return elementStructureLevelD;
	}

	public void setElementStructureLevelD(String elementStructureLevelD) {
		this.elementStructureLevelD = elementStructureLevelD;
	}

	@PositionalField(initialPosition = 333, finalPosition = 339)
	public String getElementNumberD() {
		return elementNumberD;
	}

	public void setElementNumberD(String elementNumberD) {
		elementNumberD = String.format("%7s", elementNumberD).replace(' ', '0');
		this.elementNumberD = elementNumberD;
	}

	@PositionalField(initialPosition = 340, finalPosition = 349)
	public String getElementSpecialCodesD() {
		return elementSpecialCodesD;
	}

	public void setElementSpecialCodesD(String elementSpecialCodesD) {
		this.elementSpecialCodesD = elementSpecialCodesD;
	}

	/**
	 * @return the sectionId
	 */
	@PositionalField(initialPosition = 350, finalPosition = 357)
	public String getSectionId() {
		return sectionId;
	}

	/**
	 * @param sectionId the sectionId to set
	 */
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	
	@PositionalField(initialPosition = 358, finalPosition = 365)
	public String getRemainderOfElementSpecialCodesD() {
		return remainderOfElementSpecialCodesD;
	}

	public void setRemainderOfElementSpecialCodesD(
			String remainderOfElementSpecialCodesD) {
		this.remainderOfElementSpecialCodesD = remainderOfElementSpecialCodesD;
	}

	@PositionalField(initialPosition = 366, finalPosition = 395)
	public String getElementNameE() {
		return elementNameE;
	}

	public void setElementNameE(String elementNameE) {
		this.elementNameE = elementNameE;
	}
	
	/**
	 * @return the elementELabel
	 */
	@PositionalField(initialPosition = 396, finalPosition = 410)
	public String getElementELabel() {
		return elementELabel;
	}

	/**
	 * @param elementELabel the elementELabel to set
	 */
	public void setElementELabel(String elementELabel) {
		this.elementELabel = elementELabel;
	}

	@PositionalField(initialPosition = 411, finalPosition = 412)
	public String getElementStructureLevelE() {
		return elementStructureLevelE;
	}

	public void setElementStructureLevelE(String elementStructureLevelE) {
		this.elementStructureLevelE = elementStructureLevelE;
	}

	@PositionalField(initialPosition = 413, finalPosition = 419)
	public String getElementNumberE() {
		return elementNumberE;
	}

	public void setElementNumberE(String elementNumberE) {
		elementNumberE = String.format("%7s", elementNumberE).replace(' ', '0');
		this.elementNumberE = elementNumberE;
	}

	@PositionalField(initialPosition = 420, finalPosition = 429)
	public String getElementSpecialCodesE() {
		return elementSpecialCodesE;
	}

	public void setElementSpecialCodesE(String elementSpecialCodesE) {
		this.elementSpecialCodesE = elementSpecialCodesE;
	}
	
	/**
	 * @return the groupId
	 */
	@PositionalField(initialPosition = 430, finalPosition = 437)
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@PositionalField(initialPosition = 438, finalPosition = 445)
	public String getRemainderOfElementSpecialCodesE() {
		return remainderOfElementSpecialCodesE;
	}

	public void setRemainderOfElementSpecialCodesE(
			String remainderOfElementSpecialCodesE) {
		this.remainderOfElementSpecialCodesE = remainderOfElementSpecialCodesE;
	}

	@PositionalField(initialPosition = 446, finalPosition = 475)
	public String getElementNameF() {
		return elementNameF;
	}

	public void setElementNameF(String elementNameF) {
		this.elementNameF = elementNameF;
	}
	
	/**
	 * @return the elementFLabel
	 */
	@PositionalField(initialPosition = 476, finalPosition = 490)
	public String getElementFLabel() {
		return elementFLabel;
	}

	/**
	 * @param elementFLabel the elementFLabel to set
	 */
	public void setElementFLabel(String elementFLabel) {
		this.elementFLabel = elementFLabel;
	}

	@PositionalField(initialPosition = 491, finalPosition = 492)
	public String getElementStructureLevelF() {
		return elementStructureLevelF;
	}

	public void setElementStructureLevelF(String elementStructureLevelF) {
		this.elementStructureLevelF = elementStructureLevelF;
	}

	@PositionalField(initialPosition = 493, finalPosition = 499)
	public String getElementNumberF() {
		return elementNumberF;
	}

	public void setElementNumberF(String elementNumberF) {
		elementNumberF = String.format("%7s", elementNumberF).replace(' ', '0');
		this.elementNumberF = elementNumberF;
	}

	@PositionalField(initialPosition =500 , finalPosition = 509)
	public String getElementSpecialCodesF() {
		return elementSpecialCodesF;
	}

	public void setElementSpecialCodesF(String elementSpecialCodesF) {
		this.elementSpecialCodesF = elementSpecialCodesF;
	}
	
	/**
	 * @return the divisionId
	 */
	@PositionalField(initialPosition =510 , finalPosition = 517)
	public String getDivisionId() {
		return divisionId;
	}

	/**
	 * @param divisionId the divisionId to set
	 */
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	@PositionalField(initialPosition =518 , finalPosition = 525)
	public String getRemainderOfElementSpecialCodesF() {
		return remainderOfElementSpecialCodesF;
	}

	public void setRemainderOfElementSpecialCodesF(
			String remainderOfElementSpecialCodesF) {
		this.remainderOfElementSpecialCodesF = remainderOfElementSpecialCodesF;
	}

	/**
	 * @return the elementNameG
	 */
	@PositionalField(initialPosition =526 , finalPosition = 555)
	public String getElementNameG() {
		return elementNameG;
	}

	/**
	 * @param elementNameG the elementNameG to set
	 */
	public void setElementNameG(String elementNameG) {
		this.elementNameG = elementNameG;
	}
	
	/**
	 * @return the elementGLabel
	 */
	@PositionalField(initialPosition =556 , finalPosition = 570)
	public String getElementGLabel() {
		return elementGLabel;
	}

	/**
	 * @param elementGLabel the elementGLabel to set
	 */
	public void setElementGLabel(String elementGLabel) {
		this.elementGLabel = elementGLabel;
	}
	
	/**
	 * @return the elementStructureLevelG
	 */
	@PositionalField(initialPosition =571 , finalPosition = 572)
	public String getElementStructureLevelG() {
		return elementStructureLevelG;
	}

	/**
	 * @param elementStructureLevelG the elementStructureLevelG to set
	 */
	public void setElementStructureLevelG(String elementStructureLevelG) {
		this.elementStructureLevelG = elementStructureLevelG;
	}
	
	/**
	 * @return the elementNumberG
	 */
	@PositionalField(initialPosition =573 , finalPosition = 579)
	public String getElementNumberG() {
		return elementNumberG;
	}

	/**
	 * @param elementNumberG the elementNumberG to set
	 */
	public void setElementNumberG(String elementNumberG) {
		elementNumberG = String.format("%7s", elementNumberG).replace(' ', '0');
		this.elementNumberG = elementNumberG;
	}

	/**
	 * @return the elementSpecialCodesG
	 */
	@PositionalField(initialPosition =580 , finalPosition = 589)
	public String getElementSpecialCodesG() {
		return elementSpecialCodesG;
	}

	/**
	 * @param elementSpecialCodesG the elementSpecialCodesG to set
	 */
	public void setElementSpecialCodesG(String elementSpecialCodesG) {
		this.elementSpecialCodesG = elementSpecialCodesG;
	}

	/**
	 * @return the leafLevelId
	 */
	@PositionalField(initialPosition =590 , finalPosition = 597)
	public String getLeafLevelId() {
		return leafLevelId;
	}

	/**
	 * @param leafLevelId the leafLevelId to set
	 */
	public void setLeafLevelId(String leafLevelId) {
		this.leafLevelId = leafLevelId;
	}
	
	@PositionalField(initialPosition =598 , finalPosition = 605)
	public String getRemainderOfElementSpecialCodesG() {
		return remainderOfElementSpecialCodesG;
	}

	public void setRemainderOfElementSpecialCodesG(
			String remainderOfElementSpecialCodesG) {
		this.remainderOfElementSpecialCodesG = remainderOfElementSpecialCodesG;
	}

	/**
	 * @return the testName
	 */
	@PositionalField(initialPosition = 606, finalPosition = 635)
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
	@PositionalField(initialPosition = 636, finalPosition = 637)
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
	@PositionalField(initialPosition = 638, finalPosition = 639)
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
	@PositionalField(initialPosition = 640, finalPosition = 645)
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
	@PositionalField(initialPosition = 646, finalPosition = 646)
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
	@PositionalField(initialPosition = 647, finalPosition = 662)
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
	@PositionalField(initialPosition = 663, finalPosition = 669)
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
	@PositionalField(initialPosition = 670, finalPosition = 689)
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
	@PositionalField(initialPosition = 690, finalPosition = 704)
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
	@PositionalField(initialPosition = 705, finalPosition = 705)
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
	@PositionalField(initialPosition = 706, finalPosition = 711)
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
	@PositionalField(initialPosition = 712, finalPosition = 714)
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
	@PositionalField(initialPosition = 715, finalPosition = 715)
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
	@PositionalField(initialPosition = 716, finalPosition = 725)
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
	@PositionalField(initialPosition = 726, finalPosition = 726)
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
	@PositionalField(initialPosition = 727, finalPosition = 728)
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
	@PositionalField(initialPosition = 729, finalPosition = 729)
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
	@PositionalField(initialPosition = 730, finalPosition = 730)
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
	@PositionalField(initialPosition = 731, finalPosition = 731)
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
	@PositionalField(initialPosition = 732, finalPosition = 732)
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
	@PositionalField(initialPosition = 733, finalPosition = 733)
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
	@PositionalField(initialPosition = 734, finalPosition = 734)
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
	@PositionalField(initialPosition = 735, finalPosition = 735)
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
	@PositionalField(initialPosition = 736, finalPosition = 736)
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
	@PositionalField(initialPosition = 737, finalPosition = 737)
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
	@PositionalField(initialPosition = 738, finalPosition = 741)
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
	@PositionalField(initialPosition = 742, finalPosition = 751)
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
	@PositionalField(initialPosition = 752, finalPosition = 753)
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
	@PositionalField(initialPosition = 754, finalPosition = 761)
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
	@PositionalField(initialPosition = 762, finalPosition = 762)
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
	@PositionalField(initialPosition = 763, finalPosition = 763)
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
	@PositionalField(initialPosition = 764, finalPosition = 764)
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
	@PositionalField(initialPosition = 765, finalPosition = 765)
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
	@PositionalField(initialPosition = 766, finalPosition = 766)
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
	@PositionalField(initialPosition = 767, finalPosition = 767)
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
	@PositionalField(initialPosition = 768, finalPosition = 768)
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
	@PositionalField(initialPosition = 769, finalPosition = 769)
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
	@PositionalField(initialPosition = 770, finalPosition = 770)
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
	@PositionalField(initialPosition = 771, finalPosition = 771)
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
	@PositionalField(initialPosition = 772, finalPosition = 772)
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
	@PositionalField(initialPosition = 773, finalPosition = 773)
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
	@PositionalField(initialPosition = 774, finalPosition = 777)
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
	@PositionalField(initialPosition = 778, finalPosition = 779)
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
	@PositionalField(initialPosition = 780, finalPosition = 807)
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
	@PositionalField(initialPosition = 808, finalPosition = 817)
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
	@PositionalField(initialPosition = 818, finalPosition = 871)
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
	@PositionalField(initialPosition = 872, finalPosition = 892)
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
	@PositionalField(initialPosition = 893, finalPosition = 899)
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
	@PositionalField(initialPosition = 900, finalPosition = 913)
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
	@PositionalField(initialPosition = 914, finalPosition = 927)
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
	@PositionalField(initialPosition = 928, finalPosition = 948)
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
	@PositionalField(initialPosition = 949, finalPosition = 983)
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
	@PositionalField(initialPosition = 984, finalPosition = 1028)
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
	@PositionalField(initialPosition = 1029, finalPosition = 1163)
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
	@PositionalField(initialPosition = 1164, finalPosition = 1288)
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
	 * @return the subtestIndicatorFlag
	 */
	@PositionalField(initialPosition = 1289, finalPosition = 1289)
	public String getSubtestIndicatorFlag() {
		return subtestIndicatorFlag;
	}

	/**
	 * @param subtestIndicatorFlag the subtestIndicatorFlag to set
	 */
	public void setSubtestIndicatorFlag(String subtestIndicatorFlag) {
		this.subtestIndicatorFlag = subtestIndicatorFlag;
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

	
	
}