package com.ctb.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ffpojo.metadata.positional.annotation.PositionalField;
import org.ffpojo.metadata.positional.annotation.PositionalRecord;

@PositionalRecord
public class TfilLL2ND {
	private String modelLevel;
	private String organizationId;
	
	private String elementNameA;
	private String elementLabelA;  
	private String elementStructureLevelA;
	private String elementNumberA;
	private String elementSpecialCodesA;
	private String mdrNumA;
	private String elementSpecialCodesRemainderA;
	private String elementIdA;
	
	private String grade;
	private String city;
	private String state;
	
	private String elementNameB;
	private String elementLabelB;
	private String elementStructureLevelB;
	private String elementNumberB;
	private String elementSpecialCodesB;
	private String mdrNumB;
	private String elementSpecialCodesRemainderB;
	private String elementIdB;
	
	private String elementNameC;
	private String elementLabelC;
	private String elementStructureLevelC;
	private String elementNumberC;
	private String elementSpecialCodesC;
	private String mdrNumC;
	private String elementSpecialCodesRemainderC;
	private String elementIdC;
	
	private String elementNameD;
	private String elementLabelD;
	private String elementStructureLevelD;
	private String elementNumberD;
	private String elementSpecialCodesD;
	private String mdrNumD;
	private String elementSpecialCodesRemainderD;
	private String elementIdD;
	
	private String elementNameE;
	private String elementLabelE;
	private String elementStructureLevelE;
	private String elementNumberE;
	private String elementSpecialCodesE;
	private String mdrNumE;
	private String elementSpecialCodesRemainderE;
	private String elementIdE;
	
	private String elementNameF;
	private String elementLabelF;
	private String elementStructureLevelF;
	private String elementNumberF;
	private String elementSpecialCodesF;
	private String mdrNumF;
	private String elementSpecialCodesRemainderF;
	private String elementIdF;
	
	private String elementNameG;
	private String elementLabelG;
	private String elementStructureLevelG;
	private String elementNumberG;
	private String elementSpecialCodesG;
	private String mdrNumG;
	private String elementSpecialCodesRemainderG;
	private String elementIdG;
	
	private String testName;
	private String testForm;
	private String testLevel;
	private String testDate;
	private String scoringType = "N";
	private String testHierarchyUnused;
	
	private String studentElementNumber;
	private String studentLastName;
	private String studentFirstName;
	private String studentMiddleName;
	private String studentBirthDate;
	private String studentChronologicalAge;
	private String studentGender;
	private String extStudentId;
	
	private String ethinicity1;
	private String ethinicity2;
	
	private String race1;
	private String race2;
	private String race3;
	private String race4;
	private String race5;
	
	private String homeLanguage;
	private String academicEngLearner;
	private String eseaTitle1;
	private String ellEseaTitle;
	private String giftedAndTalented;
	private String indianEducation;
	private String migrantEducation;
	private String otherDemograph;
	private String iep;
	private String sbi504;
	private String sbiUnused;
	private String studentGradeFromAnswerSheets;
	
	private String docUnused;
	private String testInvalidationSpeaking= " ";
	private String testInvalidationListening= " ";
	private String testInvalidationReading= " ";
	private String testInvalidationWriting= " ";
	private String invalidationUnused;
	
	private String accommodationSpeaking=" ";
	private String accommodationListening=" ";
	private String accommodationReading=" ";
	private String accommodationWriting=" ";
	private String accomodationUnused;
	private SpecialCodes specialCodes;
	private String sbiCtbUseColumns;
	
	private ScaleScoresLL2ND scaleScores;
	private ProficiencyLevelsLL2ND proficiencyLevels;
	private ReferenceNormalCurveEquivalentsLL2ND referenceNormalCurveEquivalent;
	private ReferencePercentileRanksLL2ND referencePercentileRanks;
	private SkillAreaNumberCorrectLL2ND skillAreaNumberCorrect;
	private SkillAreaPercentCorrectLL2ND skillAreaPercentCorrect;
	private SubSkillNumberCorrectLL2ND subSkillNumberCorrect;
	private SubSkillPercentCorrectLL2ND subSkillPercentCorrect;
	private ItemResponsesGRTLL2ND itemResponseGRT;
	
	private String lexileScore;
	private String subtestIndicatorFlag;
	
	
	
	
	
	/**
	 * @return the modelLevel
	 */
	@PositionalField(initialPosition = 1, finalPosition = 1)
	public String getModelLevel() {
		return modelLevel;
	}
	/**
	 * @param modelLevel the modelLevel to set
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
	 * @param organizationId the organizationId to set
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
	 * @param elementNameA the elementNameA to set
	 */
	public void setElementNameA(String elementNameA) {
		this.elementNameA = elementNameA;
	}
	/**
	 * @return the elementLabelA
	 */
	@PositionalField(initialPosition = 42, finalPosition = 56)
	public String getElementLabelA() {
		return elementLabelA;
	}
	/**
	 * @param elementLabelA the elementLabelA to set
	 */
	public void setElementLabelA(String elementLabelA) {
		this.elementLabelA = elementLabelA;
	}
	/**
	 * @return the elementStructureLevelA
	 */
	@PositionalField(initialPosition = 57, finalPosition = 58)
	public String getElementStructureLevelA() {
		return elementStructureLevelA;
	}
	/**
	 * @param elementStructureLevelA the elementStructureLevelA to set
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
	 * @param elementNumberA the elementNumberA to set
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
	 * @param elementSpecialCodesA the elementSpecialCodesA to set
	 */
	public void setElementSpecialCodesA(String elementSpecialCodesA) {
		this.elementSpecialCodesA = elementSpecialCodesA;
	}
	/**
	 * @return the mdrNumA
	 */	
	@PositionalField(initialPosition = 76, finalPosition = 83)
	public String getMdrNumA() {
		return mdrNumA;
	}
	/**
	 * @param mdrNumA the mdrNumA to set
	 */
	public void setMdrNumA(String mdrNumA) {
		this.mdrNumA = mdrNumA;
	}
	/**
	 * @return the elementSpecialCodesRemainderA
	 */
	@PositionalField(initialPosition = 84, finalPosition = 91)
	public String getElementSpecialCodesRemainderA() {
		return elementSpecialCodesRemainderA;
	}
	/**
	 * @param elementSpecialCodesRemainderA the elementSpecialCodesRemainderA to set
	 */
	public void setElementSpecialCodesRemainderA(
			String elementSpecialCodesRemainderA) {
		this.elementSpecialCodesRemainderA = elementSpecialCodesRemainderA;
	}
	/**
	 * @return the grade
	 */
	@PositionalField(initialPosition = 92, finalPosition = 93)
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
	 * @return the city
	 */
	@PositionalField(initialPosition = 94, finalPosition = 123)
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
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
	 * @param state the state to set
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
	 * @param elementNameB the elementNameB to set
	 */
	public void setElementNameB(String elementNameB) {
		this.elementNameB = elementNameB;
	}
	/**
	 * @return the elementLabelB
	 */
	@PositionalField(initialPosition = 156, finalPosition = 170)
	public String getElementLabelB() {
		return elementLabelB;
	}
	/**
	 * @param elementLabelB the elementLabelB to set
	 */
	public void setElementLabelB(String elementLabelB) {
		this.elementLabelB = elementLabelB;
	}
	/**
	 * @return the elementStructureLevelB
	 */
	@PositionalField(initialPosition = 171, finalPosition = 172)
	public String getElementStructureLevelB() {
		return elementStructureLevelB;
	}
	/**
	 * @param elementStructureLevelB the elementStructureLevelB to set
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
	 * @param elementNumberB the elementNumberB to set
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
	 * @param elementSpecialCodesB the elementSpecialCodesB to set
	 */
	public void setElementSpecialCodesB(String elementSpecialCodesB) {
		this.elementSpecialCodesB = elementSpecialCodesB;
	}
	/**
	 * @return the mdrNumB
	 */
	@PositionalField(initialPosition = 190, finalPosition = 197)
	public String getMdrNumB() {
		return mdrNumB;
	}
	/**
	 * @param mdrNumB the mdrNumB to set
	 */
	public void setMdrNumB(String mdrNumB) {
		this.mdrNumB = mdrNumB;
	}
	/**
	 * @return the elementSpecialCodesRemainderB
	 */
	@PositionalField(initialPosition = 198, finalPosition = 205)
	public String getElementSpecialCodesRemainderB() {
		return elementSpecialCodesRemainderB;
	}
	/**
	 * @param elementSpecialCodesRemainderB the elementSpecialCodesRemainderB to set
	 */
	public void setElementSpecialCodesRemainderB(
			String elementSpecialCodesRemainderB) {
		this.elementSpecialCodesRemainderB = elementSpecialCodesRemainderB;
	}
	/**
	 * @return the elementNameC
	 */
	@PositionalField(initialPosition = 206, finalPosition = 235)
	public String getElementNameC() {
		return elementNameC;
	}
	/**
	 * @param elementNameC the elementNameC to set
	 */
	public void setElementNameC(String elementNameC) {
		this.elementNameC = elementNameC;
	}
	/**
	 * @return the elementLabelC
	 */
	@PositionalField(initialPosition = 236, finalPosition = 250)
	public String getElementLabelC() {
		return elementLabelC;
	}
	/**
	 * @param elementLabelC the elementLabelC to set
	 */
	public void setElementLabelC(String elementLabelC) {
		this.elementLabelC = elementLabelC;
	}
	/**
	 * @return the elementStructureLevelC
	 */
	@PositionalField(initialPosition = 251, finalPosition = 252)
	public String getElementStructureLevelC() {
		return elementStructureLevelC;
	}
	/**
	 * @param elementStructureLevelC the elementStructureLevelC to set
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
	 * @param elementNumberC the elementNumberC to set
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
	 * @param elementSpecialCodesC the elementSpecialCodesC to set
	 */
	public void setElementSpecialCodesC(String elementSpecialCodesC) {
		this.elementSpecialCodesC = elementSpecialCodesC;
	}
	/**
	 * @return the mdrNumC
	 */
	@PositionalField(initialPosition = 270, finalPosition = 277)
	public String getMdrNumC() {
		return mdrNumC;
	}
	/**
	 * @param mdrNumC the mdrNumC to set
	 */
	public void setMdrNumC(String mdrNumC) {
		this.mdrNumC = mdrNumC;
	}
	/**
	 * @return the elementSpecialCodesRemainderC
	 */
	@PositionalField(initialPosition = 278, finalPosition = 285)
	public String getElementSpecialCodesRemainderC() {
		return elementSpecialCodesRemainderC;
	}
	/**
	 * @param elementSpecialCodesRemainderC the elementSpecialCodesRemainderC to set
	 */
	public void setElementSpecialCodesRemainderC(
			String elementSpecialCodesRemainderC) {
		this.elementSpecialCodesRemainderC = elementSpecialCodesRemainderC;
	}
	/**
	 * @return the elementNameD
	 */
	@PositionalField(initialPosition = 286, finalPosition = 315)
	public String getElementNameD() {
		return elementNameD;
	}
	/**
	 * @param elementNameD the elementNameD to set
	 */
	public void setElementNameD(String elementNameD) {
		this.elementNameD = elementNameD;
	}
	/**
	 * @return the elementLabelD
	 */
	@PositionalField(initialPosition = 316, finalPosition = 330)
	public String getElementLabelD() {
		return elementLabelD;
	}
	/**
	 * @param elementLabelD the elementLabelD to set
	 */
	public void setElementLabelD(String elementLabelD) {
		this.elementLabelD = elementLabelD;
	}
	/**
	 * @return the elementStructureLevelD
	 */
	@PositionalField(initialPosition = 331, finalPosition = 332)
	public String getElementStructureLevelD() {
		return elementStructureLevelD;
	}
	/**
	 * @param elementStructureLevelD the elementStructureLevelD to set
	 */
	public void setElementStructureLevelD(String elementStructureLevelD) {
		this.elementStructureLevelD = elementStructureLevelD;
	}
	/**
	 * @return the elementNumberD
	 */
	@PositionalField(initialPosition = 333, finalPosition = 339)
	public String getElementNumberD() {
		return elementNumberD;
	}
	/**
	 * @param elementNumberD the elementNumberD to set
	 */
	public void setElementNumberD(String elementNumberD) {
		elementNumberD = String.format("%7s", elementNumberD).replace(' ', '0');
		this.elementNumberD = elementNumberD;
	}
	/**
	 * @return the elementSpecialCodesD
	 */
	@PositionalField(initialPosition = 340, finalPosition = 349)
	public String getElementSpecialCodesD() {
		return elementSpecialCodesD;
	}
	/**
	 * @param elementSpecialCodesD the elementSpecialCodesD to set
	 */
	public void setElementSpecialCodesD(String elementSpecialCodesD) {
		this.elementSpecialCodesD = elementSpecialCodesD;
	}
	/**
	 * @return the mdrNumD
	 */
	@PositionalField(initialPosition = 350, finalPosition = 357)
	public String getMdrNumD() {
		return mdrNumD;
	}
	/**
	 * @param mdrNumD the mdrNumD to set
	 */
	public void setMdrNumD(String mdrNumD) {
		this.mdrNumD = mdrNumD;
	}
	/**
	 * @return the elementSpecialCodesRemainderD
	 */
	@PositionalField(initialPosition = 358, finalPosition = 365)
	public String getElementSpecialCodesRemainderD() {
		return elementSpecialCodesRemainderD;
	}
	/**
	 * @param elementSpecialCodesRemainderD the elementSpecialCodesRemainderD to set
	 */
	public void setElementSpecialCodesRemainderD(
			String elementSpecialCodesRemainderD) {
		this.elementSpecialCodesRemainderD = elementSpecialCodesRemainderD;
	}
	/**
	 * @return the elementNameE
	 */
	@PositionalField(initialPosition = 366, finalPosition = 395)
	public String getElementNameE() {
		return elementNameE;
	}
	/**
	 * @param elementNameE the elementNameE to set
	 */
	public void setElementNameE(String elementNameE) {
		this.elementNameE = elementNameE;
	}
	/**
	 * @return the elementLabelE
	 */
	@PositionalField(initialPosition = 396, finalPosition = 410)
	public String getElementLabelE() {
		return elementLabelE;
	}
	/**
	 * @param elementLabelE the elementLabelE to set
	 */
	public void setElementLabelE(String elementLabelE) {
		this.elementLabelE = elementLabelE;
	}
	/**
	 * @return the elementStructureLevelE
	 */
	@PositionalField(initialPosition = 411, finalPosition = 412)
	public String getElementStructureLevelE() {
		return elementStructureLevelE;
	}
	/**
	 * @param elementStructureLevelE the elementStructureLevelE to set
	 */
	public void setElementStructureLevelE(String elementStructureLevelE) {
		this.elementStructureLevelE = elementStructureLevelE;
	}
	/**
	 * @return the elementNumberE
	 */
	@PositionalField(initialPosition = 413, finalPosition = 419)
	public String getElementNumberE() {
		return elementNumberE;
	}
	/**
	 * @param elementNumberE the elementNumberE to set
	 */
	public void setElementNumberE(String elementNumberE) {
		elementNumberE = String.format("%7s", elementNumberE).replace(' ', '0');
		this.elementNumberE = elementNumberE;
	}
	/**
	 * @return the elementSpecialCodesE
	 */
	@PositionalField(initialPosition = 420, finalPosition = 429)
	public String getElementSpecialCodesE() {
		return elementSpecialCodesE;
	}
	/**
	 * @param elementSpecialCodesE the elementSpecialCodesE to set
	 */
	public void setElementSpecialCodesE(String elementSpecialCodesE) {
		this.elementSpecialCodesE = elementSpecialCodesE;
	}
	/**
	 * @return the mdrNumE
	 */
	@PositionalField(initialPosition = 430, finalPosition = 437)
	public String getMdrNumE() {
		return mdrNumE;
	}
	/**
	 * @param mdrNumE the mdrNumE to set
	 */
	public void setMdrNumE(String mdrNumE) {
		this.mdrNumE = mdrNumE;
	}
	/**
	 * @return the elementSpecialCodesRemainderE
	 */
	@PositionalField(initialPosition = 438, finalPosition = 445)
	public String getElementSpecialCodesRemainderE() {
		return elementSpecialCodesRemainderE;
	}
	/**
	 * @param elementSpecialCodesRemainderE the elementSpecialCodesRemainderE to set
	 */
	public void setElementSpecialCodesRemainderE(
			String elementSpecialCodesRemainderE) {
		this.elementSpecialCodesRemainderE = elementSpecialCodesRemainderE;
	}
	/**
	 * @return the elementNameF
	 */
	@PositionalField(initialPosition = 446, finalPosition = 475)
	public String getElementNameF() {
		return elementNameF;
	}
	/**
	 * @param elementNameF the elementNameF to set
	 */
	public void setElementNameF(String elementNameF) {
		this.elementNameF = elementNameF;
	}
	/**
	 * @return the elementLabelF
	 */
	@PositionalField(initialPosition = 476, finalPosition = 490)
	public String getElementLabelF() {
		return elementLabelF;
	}
	/**
	 * @param elementLabelF the elementLabelF to set
	 */
	public void setElementLabelF(String elementLabelF) {
		this.elementLabelF = elementLabelF;
	}
	/**
	 * @return the elementStructureLevelF
	 */
	@PositionalField(initialPosition = 491, finalPosition = 492)
	public String getElementStructureLevelF() {
		return elementStructureLevelF;
	}
	/**
	 * @param elementStructureLevelF the elementStructureLevelF to set
	 */
	public void setElementStructureLevelF(String elementStructureLevelF) {
		this.elementStructureLevelF = elementStructureLevelF;
	}
	/**
	 * @return the elementNumberF
	 */
	@PositionalField(initialPosition = 493, finalPosition = 499)
	public String getElementNumberF() {
		return elementNumberF;
	}
	/**
	 * @param elementNumberF the elementNumberF to set
	 */
	public void setElementNumberF(String elementNumberF) {
		elementNumberF = String.format("%7s", elementNumberF).replace(' ', '0');
		this.elementNumberF = elementNumberF;
	}
	/**
	 * @return the elementSpecialCodesF
	 */
	@PositionalField(initialPosition = 500, finalPosition = 509)
	public String getElementSpecialCodesF() {
		return elementSpecialCodesF;
	}
	/**
	 * @param elementSpecialCodesF the elementSpecialCodesF to set
	 */
	public void setElementSpecialCodesF(String elementSpecialCodesF) {
		this.elementSpecialCodesF = elementSpecialCodesF;
	}
	/**
	 * @return the mdrNumF
	 */
	@PositionalField(initialPosition = 510, finalPosition = 517)
	public String getMdrNumF() {
		return mdrNumF;
	}
	/**
	 * @param mdrNumF the mdrNumF to set
	 */
	public void setMdrNumF(String mdrNumF) {
		this.mdrNumF = mdrNumF;
	}
	/**
	 * @return the elementSpecialCodesRemainderF
	 */
	@PositionalField(initialPosition = 518, finalPosition = 525)
	public String getElementSpecialCodesRemainderF() {
		return elementSpecialCodesRemainderF;
	}
	/**
	 * @param elementSpecialCodesRemainderF the elementSpecialCodesRemainderF to set
	 */
	public void setElementSpecialCodesRemainderF(
			String elementSpecialCodesRemainderF) {
		this.elementSpecialCodesRemainderF = elementSpecialCodesRemainderF;
	}
	/**
	 * @return the elementNameG
	 */
	@PositionalField(initialPosition = 526, finalPosition = 555)
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
	 * @return the elementLabelG
	 */
	@PositionalField(initialPosition = 556, finalPosition = 570)
	public String getElementLabelG() {
		return elementLabelG;
	}
	/**
	 * @param elementLabelG the elementLabelG to set
	 */
	public void setElementLabelG(String elementLabelG) {
		this.elementLabelG = elementLabelG;
	}
	/**
	 * @return the elementStructureLevelG
	 */
	@PositionalField(initialPosition = 571, finalPosition = 572)
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
	@PositionalField(initialPosition = 573, finalPosition = 579)
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
	@PositionalField(initialPosition = 580, finalPosition = 589)
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
	 * @return the elementSpecialCodesG
	 */
	@PositionalField(initialPosition = 590, finalPosition = 597)
	public String getMdrNumG() {
		return mdrNumG;
	}
	/**
	 * @param elementSpecialCodesG the elementSpecialCodesG to set
	 */
	public void setMdrNumG(String mdrNumG) {
		this.mdrNumG = mdrNumG;
	}
	/**
	 * @return the elementSpecialCodesRemainderG
	 */
	@PositionalField(initialPosition = 598, finalPosition = 605)
	public String getElementSpecialCodesRemainderG() {
		return elementSpecialCodesRemainderG;
	}
	/**
	 * @param elementSpecialCodesRemainderG the elementSpecialCodesRemainderG to set
	 */
	public void setElementSpecialCodesRemainderG(
			String elementSpecialCodesRemainderG) {
		this.elementSpecialCodesRemainderG = elementSpecialCodesRemainderG;
	}
	/**
	 * @return the testName
	 */
	@PositionalField(initialPosition = 606, finalPosition = 635)
	public String getTestName() {
		return testName;
	}
	/**
	 * @param testName the testName to set
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
	 * @param testForm the testForm to set
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
	 * @param testLevel the testLevel to set
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
	 * @param testDate the testDate to set
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
	 * @param scoringType the scoringType to set
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
	 * @param testHierarchyUnused the testHierarchyUnused to set
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
	 * @param studentElementNumber the studentElementNumber to set
	 */
	public void setStudentElementNumber(String studentElementNumber) {
		studentElementNumber = String.format("%7s", studentElementNumber).replace(' ', '0');
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
	 * @param studentLastName the studentLastName to set
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
	 * @param studentFirstName the studentFirstName to set
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
	 * @param studentMiddleName the studentMiddleName to set
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
	 * @param studentBirthDate the studentBirthDate to set
	 */
	public void setStudentBirthDate(Date studentBirthDate) {
		String dtDate;
		SimpleDateFormat sdfop = new SimpleDateFormat("MMddyy");
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
	 * @param studentChronologicalAge the studentChronologicalAge to set
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
	 * @param studentGender the studentGender to set
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
	 * @param extStudentId the extStudentId to set
	 */
	public void setExtStudentId(String extStudentId) {
		this.extStudentId = extStudentId;
	}
	/**
	 * @return the ethinicity1
	 */
	@PositionalField(initialPosition = 726, finalPosition = 726)
	public String getEthinicity1() {
		return ethinicity1;
	}
	/**
	 * @param ethinicity1 the ethinicity1 to set
	 */
	public void setEthinicity1(String ethinicity1) {
		this.ethinicity1 = ethinicity1;
	}
	/**
	 * @return the ethinicity2
	 */
	@PositionalField(initialPosition = 727, finalPosition = 727)
	public String getEthinicity2() {
		return ethinicity2;
	}
	/**
	 * @param ethinicity2 the ethinicity2 to set
	 */
	public void setEthinicity2(String ethinicity2) {
		this.ethinicity2 = ethinicity2;
	}
	/**
	 * @return the race1
	 */
	@PositionalField(initialPosition = 728, finalPosition = 728)
	public String getRace1() {
		return race1;
	}
	/**
	 * @param race1 the race1 to set
	 */
	public void setRace1(String race1) {
		this.race1 = race1;
	}
	/**
	 * @return the race2
	 */
	@PositionalField(initialPosition = 729, finalPosition = 729)
	public String getRace2() {
		return race2;
	}
	/**
	 * @param race2 the race2 to set
	 */
	public void setRace2(String race2) {
		this.race2 = race2;
	}
	/**
	 * @return the race3
	 */
	@PositionalField(initialPosition = 730, finalPosition = 730)
	public String getRace3() {
		return race3;
	}
	/**
	 * @param race3 the race3 to set
	 */
	public void setRace3(String race3) {
		this.race3 = race3;
	}
	/**
	 * @return the race4
	 */
	@PositionalField(initialPosition = 731, finalPosition = 731)
	public String getRace4() {
		return race4;
	}
	/**
	 * @param race4 the race4 to set
	 */
	public void setRace4(String race4) {
		this.race4 = race4;
	}
	/**
	 * @return the race5
	 */
	@PositionalField(initialPosition = 732, finalPosition = 732)
	public String getRace5() {
		return race5;
	}
	/**
	 * @param race5 the race5 to set
	 */
	public void setRace5(String race5) {
		this.race5 = race5;
	}
	/**
	 * @return the homeLanguage
	 */
	@PositionalField(initialPosition = 733, finalPosition = 734)
	public String getHomeLanguage() {
		return homeLanguage;
	}
	/**
	 * @param homeLanguage the homeLanguage to set
	 */
	public void setHomeLanguage(String homeLanguage) {
		this.homeLanguage = homeLanguage;
	}
	/**
	 * @return the academicEngLearner
	 */
	@PositionalField(initialPosition = 735, finalPosition = 735)
	public String getAcademicEngLearner() {
		return academicEngLearner;
	}
	/**
	 * @param academicEngLearner the academicEngLearner to set
	 */
	public void setAcademicEngLearner(String academicEngLearner) {
		this.academicEngLearner = academicEngLearner;
	}
	/**
	 * @return the eseaTitle1
	 */
	@PositionalField(initialPosition = 736, finalPosition = 736)
	public String getEseaTitle1() {
		return eseaTitle1;
	}
	/**
	 * @param eseaTitle1 the eseaTitle1 to set
	 */
	public void setEseaTitle1(String eseaTitle1) {
		this.eseaTitle1 = eseaTitle1;
	}
	/**
	 * @return the ellEseaTitle
	 */
	@PositionalField(initialPosition = 737, finalPosition = 737)
	public String getEllEseaTitle() {
		return ellEseaTitle;
	}
	/**
	 * @param ellEseaTitle the ellEseaTitle to set
	 */
	public void setEllEseaTitle(String ellEseaTitle) {
		this.ellEseaTitle = ellEseaTitle;
	}
	/**
	 * @return the giftedAndTalented
	 */
	@PositionalField(initialPosition = 738, finalPosition = 738)
	public String getGiftedAndTalented() {
		return giftedAndTalented;
	}
	/**
	 * @param giftedAndTalented the giftedAndTalented to set
	 */
	public void setGiftedAndTalented(String giftedAndTalented) {
		this.giftedAndTalented = giftedAndTalented;
	}
	/**
	 * @return the indianEducation
	 */
	@PositionalField(initialPosition = 739, finalPosition = 739)
	public String getIndianEducation() {
		return indianEducation;
	}
	/**
	 * @param indianEducation the indianEducation to set
	 */
	public void setIndianEducation(String indianEducation) {
		this.indianEducation = indianEducation;
	}
	/**
	 * @return the migrantEducation
	 */
	@PositionalField(initialPosition = 740, finalPosition = 740)
	public String getMigrantEducation() {
		return migrantEducation;
	}
	/**
	 * @param migrantEducation the migrantEducation to set
	 */
	public void setMigrantEducation(String migrantEducation) {
		this.migrantEducation = migrantEducation;
	}
	/**
	 * @return the otherDemograph
	 */
	@PositionalField(initialPosition = 741, finalPosition = 741)
	public String getOtherDemograph() {
		return otherDemograph;
	}
	/**
	 * @param otherDemograph the otherDemograph to set
	 */
	public void setOtherDemograph(String otherDemograph) {
		this.otherDemograph = otherDemograph;
	}
	/**
	 * @return the iep
	 */
	@PositionalField(initialPosition = 742, finalPosition = 742)
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
	 * @return the sbi504
	 */
	@PositionalField(initialPosition = 743, finalPosition = 743)
	public String getSbi504() {
		return sbi504;
	}
	/**
	 * @param sbi504 the sbi504 to set
	 */
	public void setSbi504(String sbi504) {
		this.sbi504 = sbi504;
	}
	/**
	 * @return the sbiUnused
	 */
	@PositionalField(initialPosition = 744, finalPosition = 751)
	public String getSbiUnused() {
		return sbiUnused;
	}
	/**
	 * @param sbiUnused the sbiUnused to set
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
	 * @param studentGradeFromAnswerSheets the studentGradeFromAnswerSheets to set
	 */
	public void setStudentGradeFromAnswerSheets(String studentGradeFromAnswerSheets) {
		this.studentGradeFromAnswerSheets = studentGradeFromAnswerSheets;
	}
	/**
	 * @return the docUnused
	 */
	@PositionalField(initialPosition = 754, finalPosition = 761)
	public String getDocUnused() {
		return docUnused;
	}
	/**
	 * @param docUnused the docUnused to set
	 */
	public void setDocUnused(String docUnused) {
		this.docUnused = docUnused;
	}
	/**
	 * @return the testInvalidationSpeaking
	 */
	@PositionalField(initialPosition = 762, finalPosition = 762)
	public String getTestInvalidationSpeaking() {
		return testInvalidationSpeaking;
	}
	/**
	 * @param testInvalidationSpeaking the testInvalidationSpeaking to set
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
	 * @param testInvalidationListening the testInvalidationListening to set
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
	 * @param testInvalidationReading the testInvalidationReading to set
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
	 * @param testInvalidationWriting the testInvalidationWriting to set
	 */
	public void setTestInvalidationWriting(String testInvalidationWriting) {
		this.testInvalidationWriting = testInvalidationWriting;
	}
	/**
	 * @return the invalidationUnused
	 */
	@PositionalField(initialPosition = 766, finalPosition = 779)
	public String getInvalidationUnused() {
		return invalidationUnused;
	}
	/**
	 * @param invalidationUnused the invalidationUnused to set
	 */
	public void setInvalidationUnused(String invalidationUnused) {
		this.invalidationUnused = invalidationUnused;
	}
	/**
	 * @return the accommodationSpeaking
	 */
	@PositionalField(initialPosition = 780, finalPosition = 780)
	public String getAccommodationSpeaking() {
		return accommodationSpeaking;
	}
	/**
	 * @param  accommodationSpeaking the accommodationSpeaking to set
	 */
	public void setAccommodationSpeaking(String accommodationSpeaking) {
		this.accommodationSpeaking = accommodationSpeaking;
	}
	/**
	 * @return the accommodationListening
	 */
	@PositionalField(initialPosition = 781, finalPosition = 781)
	public String getAccommodationListening() {
		return accommodationListening;
	}
	/**
	 * @param accommodationListening the accommodationListening to set
	 */
	public void setAccommodationListening(String accommodationListening) {
		this.accommodationListening = accommodationListening;
	}
	/**
	 * @return the accommodationReading
	 */
	@PositionalField(initialPosition = 782, finalPosition = 782)
	public String getAccommodationReading() {
		return accommodationReading;
	}
	/**
	 * @param accommodationReading the accommodationReading to set
	 */
	public void setAccommodationReading(String accommodationReading) {
		this.accommodationReading = accommodationReading;
	}
	/**
	 * @return the accommodationWriting
	 */
	@PositionalField(initialPosition = 783, finalPosition = 783)
	public String getAccommodationWriting() {
		return accommodationWriting;
	}
	/**
	 * @param accommodationWriting the accommodationWriting to set
	 */
	public void setAccommodationWriting(String accommodationWriting) {
		this.accommodationWriting = accommodationWriting;
	}
	/**
	 * @return the accomodationUnused
	 */
	@PositionalField(initialPosition = 784, finalPosition = 807)
	public String getAccomodationUnused() {
		return accomodationUnused;
	}
	/**
	 * @param accomodationUnused the accomodationUnused to set
	 */
	public void setAccomodationUnused(String accomodationUnused) {
		this.accomodationUnused = accomodationUnused;
	}
	/**
	 * @return the specialCodes
	 */
	@PositionalField(initialPosition = 808, finalPosition = 817)
	public SpecialCodes getSpecialCodes() {
		return specialCodes;
	}
	/**
	 * @param specialCodes the specialCodes to set
	 */
	public void setSpecialCodes(SpecialCodes specialCodes) {
		this.specialCodes = specialCodes;
	}
	/**
	 * @return the sbiCtbUseColumns
	 */
	@PositionalField(initialPosition = 818, finalPosition = 871)
	public String getSbiCtbUseColumns() {
		return sbiCtbUseColumns;
	}
	/**
	 * @param sbiCtbUseColumns the sbiCtbUseColumns to set
	 */
	public void setSbiCtbUseColumns(String sbiCtbUseColumns) {
		this.sbiCtbUseColumns = sbiCtbUseColumns;
	}
	/**
	 * @return the scaleScores
	 */
	@PositionalField(initialPosition = 872, finalPosition = 898)
	public ScaleScoresLL2ND getScaleScores() {
		return scaleScores;
	}
	/**
	 * @param scaleScores the scaleScores to set
	 */
	public void setScaleScores(ScaleScoresLL2ND scaleScores) {
		this.scaleScores = scaleScores;
	}
	/**
	 * @return the proficiencyLevels
	 */
	@PositionalField(initialPosition = 899, finalPosition = 907)
	public ProficiencyLevelsLL2ND getProficiencyLevels() {
		return proficiencyLevels;
	}
	/**
	 * @param proficiencyLevels the proficiencyLevels to set
	 */
	public void setProficiencyLevels(ProficiencyLevelsLL2ND proficiencyLevels) {
		this.proficiencyLevels = proficiencyLevels;
	}
	/**
	 * @return the referenceNormalCurveEquivalent
	 */
	@PositionalField(initialPosition = 908, finalPosition = 925)
	public ReferenceNormalCurveEquivalentsLL2ND getReferenceNormalCurveEquivalent() {
		return referenceNormalCurveEquivalent;
	}
	/**
	 * @param referenceNormalCurveEquivalent the referenceNormalCurveEquivalent to set
	 */
	public void setReferenceNormalCurveEquivalent(
			ReferenceNormalCurveEquivalentsLL2ND referenceNormalCurveEquivalent) {
		this.referenceNormalCurveEquivalent = referenceNormalCurveEquivalent;
	}
	/**
	 * @return the referencePercentileRanks
	 */
	@PositionalField(initialPosition = 926, finalPosition = 943)
	public ReferencePercentileRanksLL2ND getReferencePercentileRanks() {
		return referencePercentileRanks;
	}
	/**
	 * @param referencePercentileRanks the referencePercentileRanks to set
	 */
	public void setReferencePercentileRanks(
			ReferencePercentileRanksLL2ND referencePercentileRanks) {
		this.referencePercentileRanks = referencePercentileRanks;
	}
	/**
	 * @return the skillAreaNumberCorrect
	 */
	@PositionalField(initialPosition = 944, finalPosition = 970)
	public SkillAreaNumberCorrectLL2ND getSkillAreaNumberCorrect() {
		return skillAreaNumberCorrect;
	}
	/**
	 * @param skillAreaNumberCorrect the skillAreaNumberCorrect to set
	 */
	public void setSkillAreaNumberCorrect(
			SkillAreaNumberCorrectLL2ND skillAreaNumberCorrect) {
		this.skillAreaNumberCorrect = skillAreaNumberCorrect;
	}
	/**
	 * @return the skillAreaPercentCorrect
	 */
	@PositionalField(initialPosition = 971, finalPosition = 1015)
	public SkillAreaPercentCorrectLL2ND getSkillAreaPercentCorrect() {
		return skillAreaPercentCorrect;
	}
	/**
	 * @param skillAreaPercentCorrect the skillAreaPercentCorrect to set
	 */
	public void setSkillAreaPercentCorrect(
			SkillAreaPercentCorrectLL2ND skillAreaPercentCorrect) {
		this.skillAreaPercentCorrect = skillAreaPercentCorrect;
	}
	/**
	 * @return the subSkillNumberCorrect
	 */
	@PositionalField(initialPosition = 1016, finalPosition = 1081)
	public SubSkillNumberCorrectLL2ND getSubSkillNumberCorrect() {
		return subSkillNumberCorrect;
	}
	/**
	 * @param subSkillNumberCorrect the subSkillNumberCorrect to set
	 */
	public void setSubSkillNumberCorrect(SubSkillNumberCorrectLL2ND subSkillNumberCorrect) {
		this.subSkillNumberCorrect = subSkillNumberCorrect;
	}
	/**
	 * @return the subSkillPercentCorrect
	 */
	@PositionalField(initialPosition = 1082, finalPosition = 1236)
	public SubSkillPercentCorrectLL2ND getSubSkillPercentCorrect() {
		return subSkillPercentCorrect;
	}
	/**
	 * @param subSkillPercentCorrect the subSkillPercentCorrect to set
	 */
	public void setSubSkillPercentCorrect(
			SubSkillPercentCorrectLL2ND subSkillPercentCorrect) {
		this.subSkillPercentCorrect = subSkillPercentCorrect;
	}
	/**
	 * @return the itemResponseGRT
	 */
	@PositionalField(initialPosition = 1237, finalPosition = 1376)
	public ItemResponsesGRTLL2ND getItemResponseGRT() {
		return itemResponseGRT;
	}
	/**
	 * @param itemResponseGRT the itemResponseGRT to set
	 */
	public void setItemResponseGRT(ItemResponsesGRTLL2ND itemResponseGRT) {
		this.itemResponseGRT = itemResponseGRT;
	}
	/**
	 * @return the lexileScore
	 */
	@PositionalField(initialPosition = 1377, finalPosition = 1381)
	public String getLexileScore() {
		return lexileScore;
	}
	/**
	 * @param lexileScore the lexileScore to set
	 */
	public void setLexileScore(String lexileScore) {
		this.lexileScore = lexileScore;
	}
	/**
	 * @return the subtestInvalidationFlag
	 */
	@PositionalField(initialPosition = 1382, finalPosition = 1382)
	public String getSubtestIndicatorFlag() {
		return subtestIndicatorFlag;
	}
	/**
	 * @param subtestInvalidationFlag the subtestInvalidationFlag to set
	 */
	public void setSubtestIndicatorFlag(String subtestIndicatorFlag) {
		this.subtestIndicatorFlag = subtestIndicatorFlag;
	}
	/**
	 * @return the elementIdA
	 */
	public String getElementIdA() {
		return elementIdA;
	}
	/**
	 * @param elementIdA the elementIdA to set
	 */
	public void setElementIdA(String elementIdA) {
		this.elementIdA = elementIdA;
	}
	/**
	 * @return the elementIdB
	 */
	public String getElementIdB() {
		return elementIdB;
	}
	/**
	 * @param elementIdB the elementIdB to set
	 */
	public void setElementIdB(String elementIdB) {
		this.elementIdB = elementIdB;
	}
	/**
	 * @return the elementIdC
	 */
	public String getElementIdC() {
		return elementIdC;
	}
	/**
	 * @param elementIdC the elementIdC to set
	 */
	public void setElementIdC(String elementIdC) {
		this.elementIdC = elementIdC;
	}
	/**
	 * @return the elementIdD
	 */
	public String getElementIdD() {
		return elementIdD;
	}
	/**
	 * @param elementIdD the elementIdD to set
	 */
	public void setElementIdD(String elementIdD) {
		this.elementIdD = elementIdD;
	}
	/**
	 * @return the elementIdE
	 */
	public String getElementIdE() {
		return elementIdE;
	}
	/**
	 * @param elementIdE the elementIdE to set
	 */
	public void setElementIdE(String elementIdE) {
		this.elementIdE = elementIdE;
	}
	/**
	 * @return the elementIdF
	 */
	public String getElementIdF() {
		return elementIdF;
	}
	/**
	 * @param elementIdF the elementIdF to set
	 */
	public void setElementIdF(String elementIdF) {
		this.elementIdF = elementIdF;
	}
	/**
	 * @return the elementIdG
	 */
	public String getElementIdG() {
		return elementIdG;
	}
	/**
	 * @param elementIdG the elementIdG to set
	 */
	public void setElementIdG(String elementIdG) {
		this.elementIdG = elementIdG;
	}
	
}
