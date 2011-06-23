package com.ctb.lexington.db.data;
import java.util.Map;
public class StudentDemographicData{

	private String customer;
	private String gender;
	private String ethnicity;
	private String ell;
	private String frLunch;
	private String iep;
	private String lfStatus;
	private String lep;
	private String migrant;
	private String section504;
	private Map researchData;
	private String screenMagnifier;
	private String screenReader;
	private String calculator;
	private String testPause;
	private String untimedTest;
	private String questionBGColor;
	private String questionFontColor;
	private String questionFontSize;
	private String musicFileId; // Added for Laslink
	private String maskingRuler; // Added for Laslink
	private String magnifyingGlass; // Added for Laslink

	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getEthnicity() {
		return ethnicity;
	}
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Map getResearchData() {
		return researchData;
	}
	public void setResearchData(Map researchData) {
		this.researchData = researchData;
	}
	public String getCalculator() {
		return calculator;
	}
	public void setCalculator(String calculator) {
		this.calculator = calculator;
	}
	public String getQuestionBGColor() {
		return questionBGColor;
	}
	public void setQuestionBGColor(String questionBGColor) {
		this.questionBGColor = questionBGColor;
	}
	public String getQuestionFontColor() {
		return questionFontColor;
	}
	public void setQuestionFontColor(String questionFontColor) {
		this.questionFontColor = questionFontColor;
	}
	public String getQuestionFontSize() {
		return questionFontSize;
	}
	public void setQuestionFontSize(String questionFontSize) {
		this.questionFontSize = questionFontSize;
	}
	public String getScreenMagnifier() {
		return screenMagnifier;
	}
	public void setScreenMagnifier(String screenMagnifier) {
		this.screenMagnifier = screenMagnifier;
	}
	public String getScreenReader() {
		return screenReader;
	}
	public void setScreenReader(String screenReader) {
		this.screenReader = screenReader;
	}
	public String getTestPause() {
		return testPause;
	}
	public void setTestPause(String testPause) {
		this.testPause = testPause;
	}
	public String getUntimedTest() {
		return untimedTest;
	}
	public void setUntimedTest(String untimedTest) {
		this.untimedTest = untimedTest;
	}
	public String getEll() {
		return ell;
	}
	public void setEll(String ell) {
		this.ell = ell;
	}
	public String getFrLunch() {
		return frLunch;
	}
	public void setFrLunch(String frLunch) {
		this.frLunch = frLunch;
	}
	public String getIep() {
		return iep;
	}
	public void setIep(String iep) {
		this.iep = iep;
	}
	public String getLep() {
		return lep;
	}
	public void setLep(String lep) {
		this.lep = lep;
	}
	public String getLfStatus() {
		return lfStatus;
	}
	public void setLfStatus(String lfStatus) {
		this.lfStatus = lfStatus;
	}
	public String getMigrant() {
		return migrant;
	}
	public void setMigrant(String migrant) {
		this.migrant = migrant;
	}
	public String getSection504() {
		return section504;
	}
	public void setSection504(String section504) {
		this.section504 = section504;
	}
	public String getMusicFileId() {
		return musicFileId;
	}
	public void setMusicFileId(String musicFileId) {
		this.musicFileId = musicFileId;
	}
	public String getMaskingRuler() {
		return maskingRuler;
	}
	public void setMaskingRuler(String maskingRuler) {
		this.maskingRuler = maskingRuler;
	}
	public String getMagnifyingGlass() {
		return magnifyingGlass;
	}
	public void setMagnifyingGlass(String magnifyingGlass) {
		this.magnifyingGlass = magnifyingGlass;
	}
}