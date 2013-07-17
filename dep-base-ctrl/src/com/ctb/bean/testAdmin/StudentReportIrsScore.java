package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the values required to display 
 * immediate scores from irs database.
 * 
 * @author TCS
 */

public class StudentReportIrsScore extends CTBBean{
	
	static final long serialVersionUID = 1L;
	
	private String contentAreaName;
	private String rawScore = "N/A";
	private String scaleScore = "N/A";
	private String proficiencyLevel = "N/A";
	
	/*Added for Academic Report*/
	private String PtsPossible = "N/A";
	private String PtsObtained = "N/A";
	private String PerCorrect = "N/A";
	
	private String spPtsPossible = "N/A";
	private String spPtsObtained = "N/A";
	private String spPerCorrect = "N/A";
	private String spTotalScore = "N/A";
	
	private String lnPtsPossible = "N/A";
	private String lnPtsObtained = "N/A";
	private String lnPerCorrect = "N/A";
	private String lnTotalScore = "N/A";
	
	private String rdPtsPossible = "N/A";
	private String rdPtsObtained = "N/A";
	private String rdPerCorrect = "N/A";
	private String rdTotalScore = "N/A";
	
	private String wrPtsPossible = "N/A";
	private String wrPtsObtained = "N/A";
	private String wrPerCorrect = "N/A";
	private String wrTotalScore = "N/A";
	/*End*/
	private String testRosterId;
	/**
	 * @return the contentAreaName
	 */
	public String getContentAreaName() {
		return contentAreaName;
	}
	/**
	 * @param contentAreaName the contentAreaName to set
	 */
	public void setContentAreaName(String contentAreaName) {
		this.contentAreaName = contentAreaName;
	}
	/**
	 * @return the rawScore
	 */
	public String getRawScore() {
		return rawScore;
	}
	/**
	 * @param rawScore the rawScore to set
	 */
	public void setRawScore(String rawScore) {
		this.rawScore = rawScore;
	}
	/**
	 * @return the scaleScore
	 */
	public String getScaleScore() {
		return scaleScore;
	}
	/**
	 * @param scaleScore the scaleScore to set
	 */
	public void setScaleScore(String scaleScore) {
		this.scaleScore = scaleScore;
	}
	/**
	 * @return the proficiencyLevel
	 */
	public String getProficiencyLevel() {
		return proficiencyLevel;
	}
	/**
	 * @param proficiencyLevel the proficiencyLevel to set
	 */
	public void setProficiencyLevel(String proficiencyLevel) {
		this.proficiencyLevel = proficiencyLevel;
	}
	
	public String getTestRosterId() {
		return testRosterId;
	}
	public void setTestRosterId(String testRosterId) {
		this.testRosterId = testRosterId;
	}
	/**
	 * @return the ptsPossible
	 */
	public String getPtsPossible() {
		return PtsPossible;
	}
	/**
	 * @param ptsPossible the ptsPossible to set
	 */
	public void setPtsPossible(String ptsPossible) {
		PtsPossible = ptsPossible;
	}
	/**
	 * @return the ptsObtained
	 */
	public String getPtsObtained() {
		return PtsObtained;
	}
	/**
	 * @param ptsObtained the ptsObtained to set
	 */
	public void setPtsObtained(String ptsObtained) {
		PtsObtained = ptsObtained;
	}
	/**
	 * @return the perCorrect
	 */
	public String getPerCorrect() {
		return PerCorrect;
	}
	/**
	 * @param perCorrect the perCorrect to set
	 */
	public void setPerCorrect(String perCorrect) {
		PerCorrect = perCorrect;
	}
	/**
	 * @return the spPtsPossible
	 */
	public String getSpPtsPossible() {
		return spPtsPossible;
	}
	/**
	 * @param spPtsPossible the spPtsPossible to set
	 */
	public void setSpPtsPossible(String spPtsPossible) {
		this.spPtsPossible = spPtsPossible;
	}
	/**
	 * @return the spPtsObtained
	 */
	public String getSpPtsObtained() {
		return spPtsObtained;
	}
	/**
	 * @param spPtsObtained the spPtsObtained to set
	 */
	public void setSpPtsObtained(String spPtsObtained) {
		this.spPtsObtained = spPtsObtained;
	}
	/**
	 * @return the spPerCorrect
	 */
	public String getSpPerCorrect() {
		return spPerCorrect;
	}
	/**
	 * @param spPerCorrect the spPerCorrect to set
	 */
	public void setSpPerCorrect(String spPerCorrect) {
		this.spPerCorrect = spPerCorrect;
	}
	/**
	 * @return the spTotalScore
	 */
	public String getSpTotalScore() {
		return spTotalScore;
	}
	/**
	 * @param spTotalScore the spTotalScore to set
	 */
	public void setSpTotalScore(String spTotalScore) {
		this.spTotalScore = spTotalScore;
	}
	/**
	 * @return the lnPtsPossible
	 */
	public String getLnPtsPossible() {
		return lnPtsPossible;
	}
	/**
	 * @param lnPtsPossible the lnPtsPossible to set
	 */
	public void setLnPtsPossible(String lnPtsPossible) {
		this.lnPtsPossible = lnPtsPossible;
	}
	/**
	 * @return the lnPtsObtained
	 */
	public String getLnPtsObtained() {
		return lnPtsObtained;
	}
	/**
	 * @param lnPtsObtained the lnPtsObtained to set
	 */
	public void setLnPtsObtained(String lnPtsObtained) {
		this.lnPtsObtained = lnPtsObtained;
	}
	/**
	 * @return the lnPerCorrect
	 */
	public String getLnPerCorrect() {
		return lnPerCorrect;
	}
	/**
	 * @param lnPerCorrect the lnPerCorrect to set
	 */
	public void setLnPerCorrect(String lnPerCorrect) {
		this.lnPerCorrect = lnPerCorrect;
	}
	/**
	 * @return the lnTotalScore
	 */
	public String getLnTotalScore() {
		return lnTotalScore;
	}
	/**
	 * @param lnTotalScore the lnTotalScore to set
	 */
	public void setLnTotalScore(String lnTotalScore) {
		this.lnTotalScore = lnTotalScore;
	}
	/**
	 * @return the rdPtsPossible
	 */
	public String getRdPtsPossible() {
		return rdPtsPossible;
	}
	/**
	 * @param rdPtsPossible the rdPtsPossible to set
	 */
	public void setRdPtsPossible(String rdPtsPossible) {
		this.rdPtsPossible = rdPtsPossible;
	}
	/**
	 * @return the rdPtsObtained
	 */
	public String getRdPtsObtained() {
		return rdPtsObtained;
	}
	/**
	 * @param rdPtsObtained the rdPtsObtained to set
	 */
	public void setRdPtsObtained(String rdPtsObtained) {
		this.rdPtsObtained = rdPtsObtained;
	}
	/**
	 * @return the rdPerCorrect
	 */
	public String getRdPerCorrect() {
		return rdPerCorrect;
	}
	/**
	 * @param rdPerCorrect the rdPerCorrect to set
	 */
	public void setRdPerCorrect(String rdPerCorrect) {
		this.rdPerCorrect = rdPerCorrect;
	}
	/**
	 * @return the rdTotalScore
	 */
	public String getRdTotalScore() {
		return rdTotalScore;
	}
	/**
	 * @param rdTotalScore the rdTotalScore to set
	 */
	public void setRdTotalScore(String rdTotalScore) {
		this.rdTotalScore = rdTotalScore;
	}
	/**
	 * @return the wrPtsPossible
	 */
	public String getWrPtsPossible() {
		return wrPtsPossible;
	}
	/**
	 * @param wrPtsPossible the wrPtsPossible to set
	 */
	public void setWrPtsPossible(String wrPtsPossible) {
		this.wrPtsPossible = wrPtsPossible;
	}
	/**
	 * @return the wrPtsObtained
	 */
	public String getWrPtsObtained() {
		return wrPtsObtained;
	}
	/**
	 * @param wrPtsObtained the wrPtsObtained to set
	 */
	public void setWrPtsObtained(String wrPtsObtained) {
		this.wrPtsObtained = wrPtsObtained;
	}
	/**
	 * @return the wrPerCorrect
	 */
	public String getWrPerCorrect() {
		return wrPerCorrect;
	}
	/**
	 * @param wrPerCorrect the wrPerCorrect to set
	 */
	public void setWrPerCorrect(String wrPerCorrect) {
		this.wrPerCorrect = wrPerCorrect;
	}
	/**
	 * @return the wrTotalScore
	 */
	public String getWrTotalScore() {
		return wrTotalScore;
	}
	/**
	 * @param wrTotalScore the wrTotalScore to set
	 */
	public void setWrTotalScore(String wrTotalScore) {
		this.wrTotalScore = wrTotalScore;
	}
	
	
}
