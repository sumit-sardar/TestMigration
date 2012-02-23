package com.ctb.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ffpojo.metadata.positional.annotation.PositionalField;
import org.ffpojo.metadata.positional.annotation.PositionalRecord;

@PositionalRecord
public class TABEFile {

	private String customerID;
	private String orgLevel1Name;
	private String orgLevel1Code;
	private String orgLevel2Name;
	private String orgLevel2Code;
	private String orgLevel3Name;
	private String orgLevel3Code;
	private String orgLevel4Name;
	private String orgLevel4Code;
	private String grade;
	
	private String studentID;
	private String studentLastName;
	private String studentFirstName;
	private String studentMiddleName;
	private String studentBirthDate;
	private String studentGender;
	private String studentId2;
	private String ell = new String("0");
	private String ethnicity = new String("0");
	private String freeLunch = new String("0");
	private String iep = new String("0");
	private String lep = new String("0");
	private String laborForceStatus = new String("0");
	private String migrant = new String("0");
	private String section504 = new String("0");
	private String dateTestingCompleted;
	private String interrupted = new String("0");

	private AbilityScore abilityScores;
	private GradeEquivalent gradeEquivalent;
	private NRSLevels nrsLevels;
	private PercentageMastery percentageMastery;
	private PredictedGED predictedGED;
	private ObjectiveLevel objectiveLevel;
	private ObjectiveMastery objectiveMastery;
	private String itemResponse;

	/**
	 * @return the customerID
	 */
	@PositionalField(initialPosition = 1, finalPosition = 6)
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * @return the orgLevel1Name
	 */
	@PositionalField(initialPosition = 7, finalPosition = 38)
	public String getOrgLevel1Name() {
		return orgLevel1Name;
	}

	/**
	 * @param orgLevel1Name the orgLevel1Name to set
	 */
	public void setOrgLevel1Name(String orgLevel1Name) {
		this.orgLevel1Name = orgLevel1Name;
	}

	/**
	 * @return the orgLevel1Code
	 */
	@PositionalField(initialPosition = 39, finalPosition = 48)
	public String getOrgLevel1Code() {
		return orgLevel1Code;
	}

	/**
	 * @param orgLevel1Code the orgLevel1Code to set
	 */
	public void setOrgLevel1Code(String orgLevel1Code) {
		this.orgLevel1Code = orgLevel1Code;
	}

	/**
	 * @return the orgLevel2Name
	 */
	@PositionalField(initialPosition = 49, finalPosition = 80)
	public String getOrgLevel2Name() {
		return orgLevel2Name;
	}

	/**
	 * @param orgLevel2Name the orgLevel2Name to set
	 */
	public void setOrgLevel2Name(String orgLevel2Name) {
		this.orgLevel2Name = orgLevel2Name;
	}

	/**
	 * @return the orgLevel2Code
	 */
	@PositionalField(initialPosition = 81, finalPosition = 90)
	public String getOrgLevel2Code() {
		return orgLevel2Code;
	}

	/**
	 * @param orgLevel2Code the orgLevel2Code to set
	 */
	public void setOrgLevel2Code(String orgLevel2Code) {
		this.orgLevel2Code = orgLevel2Code;
	}

	/**
	 * @return the orgLevel3Name
	 */
	@PositionalField(initialPosition = 91, finalPosition = 122)
	public String getOrgLevel3Name() {
		return orgLevel3Name;
	}

	/**
	 * @param orgLevel3Name the orgLevel3Name to set
	 */
	public void setOrgLevel3Name(String orgLevel3Name) {
		this.orgLevel3Name = orgLevel3Name;
	}

	/**
	 * @return the orgLevel3Code
	 */
	@PositionalField(initialPosition = 123, finalPosition = 132)
	public String getOrgLevel3Code() {
		return orgLevel3Code;
	}

	/**
	 * @param orgLevel3Code the orgLevel3Code to set
	 */
	public void setOrgLevel3Code(String orgLevel3Code) {
		this.orgLevel3Code = orgLevel3Code;
	}

	/**
	 * @return the orgLevel4Name
	 */
	@PositionalField(initialPosition = 133, finalPosition = 164)
	public String getOrgLevel4Name() {
		return orgLevel4Name;
	}

	/**
	 * @param orgLevel4Name the orgLevel4Name to set
	 */
	public void setOrgLevel4Name(String orgLevel4Name) {
		this.orgLevel4Name = orgLevel4Name;
	}

	/**
	 * @return the orgLevel4Code
	 */
	@PositionalField(initialPosition = 165, finalPosition = 174)
	public String getOrgLevel4Code() {
		return orgLevel4Code;
	}

	/**
	 * @param orgLevel4Code the orgLevel4Code to set
	 */
	public void setOrgLevel4Code(String orgLevel4Code) {
		this.orgLevel4Code = orgLevel4Code;
	}

	/**
	 * @return the grade
	 */
	@PositionalField(initialPosition = 175, finalPosition = 176)
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the studentID
	 */
	@PositionalField(initialPosition = 177, finalPosition = 186)
	public String getStudentID() {
		return studentID;
	}

	/**
	 * @param studentID the studentID to set
	 */
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * @return the studentLastName
	 */
	@PositionalField(initialPosition = 187, finalPosition = 218)
	public String getStudentLastName() {
		return studentLastName;
	}

	/**
	 * @param studentLastName the studentLastName to set
	 */
	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	/**
	 * @return the studentFirstName
	 */
	@PositionalField(initialPosition = 219, finalPosition = 250)
	public String getStudentFirstName() {
		return studentFirstName;
	}

	/**
	 * @param studentFirstName the studentFirstName to set
	 */
	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	/**
	 * @return the studentMiddleName
	 */
	@PositionalField(initialPosition = 251, finalPosition = 251)
	public String getStudentMiddleName() {
		return studentMiddleName;
	}

	/**
	 * @param studentMiddleName the studentMiddleName to set
	 */
	public void setStudentMiddleName(String studentMiddleName) {
		this.studentMiddleName = studentMiddleName;
	}

	/**
	 * @return the studentBirthDate
	 */
	@PositionalField(initialPosition = 252, finalPosition = 257)
	public String getStudentBirthDate() {
		return studentBirthDate;
	}

	/**
	 * @param studentBirthDate the studentBirthDate to set
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
	 * @return the studentGender
	 */
	@PositionalField(initialPosition = 258, finalPosition = 258)
	public String getStudentGender() {
		return studentGender;
	}

	/**
	 * @param studentGender the studentGender to set
	 */
	public void setStudentGender(String studentGender) {
		this.studentGender = studentGender;
	}

	/**
	 * @return the studentId2
	 */
	@PositionalField(initialPosition = 259, finalPosition = 268)
	public String getStudentId2() {
		return studentId2;
	}

	/**
	 * @param studentId2 the studentId2 to set
	 */
	public void setStudentId2(String studentId2) {
		this.studentId2 = studentId2;
	}

	/**
	 * @return the ell
	 */
	@PositionalField(initialPosition = 269, finalPosition = 269)
	public String getEll() {
		return ell;
	}

	/**
	 * @param ell the ell to set
	 */
	public void setEll(String ell) {
		this.ell = ell;
	}

	/**
	 * @return the ethnicity
	 */
	@PositionalField(initialPosition = 270, finalPosition = 270)
	public String getEthnicity() {
		return ethnicity;
	}

	/**
	 * @param ethnicity the ethnicity to set
	 */
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	/**
	 * @return the freeLunch
	 */
	@PositionalField(initialPosition = 271, finalPosition = 271)
	public String getFreeLunch() {
		return freeLunch;
	}

	/**
	 * @param freeLunch the freeLunch to set
	 */
	public void setFreeLunch(String freeLunch) {
		this.freeLunch = freeLunch;
	}

	/**
	 * @return the iep
	 */
	@PositionalField(initialPosition = 272, finalPosition = 272)
	public String getIep() {
		return iep;
	}

	/**
	 * @param iep the iep to set
	 */
	public void setIep(String iep) {
		this.iep = iep;
	}

	/**
	 * @return the lep
	 */
	@PositionalField(initialPosition = 273, finalPosition = 273)
	public String getLep() {
		return lep;
	}

	/**
	 * @param lep the lep to set
	 */
	public void setLep(String lep) {
		this.lep = lep;
	}

	/**
	 * @return the laborForceStatus
	 */
	@PositionalField(initialPosition = 274, finalPosition = 274)
	public String getLaborForceStatus() {
		return laborForceStatus;
	}

	/**
	 * @param laborForceStatus the laborForceStatus to set
	 */
	public void setLaborForceStatus(String laborForceStatus) {
		this.laborForceStatus = laborForceStatus;
	}

	/**
	 * @return the migrant
	 */
	@PositionalField(initialPosition = 275, finalPosition = 275)
	public String getMigrant() {
		return migrant;
	}

	/**
	 * @param migrant the migrant to set
	 */
	public void setMigrant(String migrant) {
		this.migrant = migrant;
	}

	/**
	 * @return the section504
	 */
	@PositionalField(initialPosition = 276, finalPosition = 276)
	public String getSection504() {
		return section504;
	}

	/**
	 * @param section504 the section504 to set
	 */
	public void setSection504(String section504) {
		this.section504 = section504;
	}

	/**
	 * @return the dateTestingCompleted
	 */
	@PositionalField(initialPosition = 277, finalPosition = 282)
	public String getDateTestingCompleted() {
		return dateTestingCompleted;
	}

	/**
	 * @param dateTestingCompleted the dateTestingCompleted to set
	 */
	public void setDateTestingCompleted(String dateTestingCompleted) {
		this.dateTestingCompleted = dateTestingCompleted;
	}

	/**
	 * @return the interrupted
	 */
	@PositionalField(initialPosition = 283, finalPosition = 283)
	public String getInterrupted() {
		return interrupted;
	}

	/**
	 * @param interrupted the interrupted to set
	 */
	public void setInterrupted(String interrupted) {
		this.interrupted = interrupted;
	}

	/**
	 * @return the abilityScores
	 */
	@PositionalField(initialPosition = 284, finalPosition = 313)
	public AbilityScore getAbilityScores() {
		return abilityScores;
	}

	/**
	 * @param abilityScores the abilityScores to set
	 */
	public void setAbilityScores(AbilityScore abilityScores) {
		this.abilityScores = abilityScores;
	}

	/**
	 * @return the gradeEquivalent
	 */
	@PositionalField(initialPosition = 314, finalPosition = 343)
	public GradeEquivalent getGradeEquivalent() {
		return gradeEquivalent;
	}

	/**
	 * @param gradeEquivalent the gradeEquivalent to set
	 */
	public void setGradeEquivalent(GradeEquivalent gradeEquivalent) {
		this.gradeEquivalent = gradeEquivalent;
	}

	/**
	 * @return the nrsLevels
	 */
	@PositionalField(initialPosition = 344, finalPosition = 349)
	public NRSLevels getNrsLevels() {
		return nrsLevels;
	}

	/**
	 * @param nrsLevels the nrsLevels to set
	 */
	public void setNrsLevels(NRSLevels nrsLevels) {
		this.nrsLevels = nrsLevels;
	}

	/**
	 * @return the percentageMastery
	 */
	@PositionalField(initialPosition = 350, finalPosition = 361)
	public PercentageMastery getPercentageMastery() {
		return percentageMastery;
	}

	/**
	 * @param percentageMastery the percentageMastery to set
	 */
	public void setPercentageMastery(PercentageMastery percentageMastery) {
		this.percentageMastery = percentageMastery;
	}

	/**
	 * @return the predictedGED
	 */
	@PositionalField(initialPosition = 362, finalPosition = 379)
	public PredictedGED getPredictedGED() {
		return predictedGED;
	}

	/**
	 * @param predictedGED the predictedGED to set
	 */
	public void setPredictedGED(PredictedGED predictedGED) {
		this.predictedGED = predictedGED;
	}

	/**
	 * @return the objectiveLevel
	 */
	@PositionalField(initialPosition = 380, finalPosition = 396)
	public ObjectiveLevel getObjectiveLevel() {
		return objectiveLevel;
	}

	/**
	 * @param objectiveLevel the objectiveLevel to set
	 */
	public void setObjectiveLevel(ObjectiveLevel objectiveLevel) {
		this.objectiveLevel = objectiveLevel;
	}

	/**
	 * @return the objectiveMastery
	 */
	@PositionalField(initialPosition = 397, finalPosition = 413)
	public ObjectiveMastery getObjectiveMastery() {
		return objectiveMastery;
	}

	/**
	 * @param objectiveMastery the objectiveMastery to set
	 */
	public void setObjectiveMastery(ObjectiveMastery objectiveMastery) {
		this.objectiveMastery = objectiveMastery;
	}

	/**
	 * @return the itemResponse
	 */
	@PositionalField(initialPosition = 414, finalPosition = 2006)
	public String getItemResponse() {
		return itemResponse;
	}

	/**
	 * @param itemResponse the itemResponse to set
	 */
	public void setItemResponse(String itemResponse) {
		this.itemResponse = itemResponse;
	}

}