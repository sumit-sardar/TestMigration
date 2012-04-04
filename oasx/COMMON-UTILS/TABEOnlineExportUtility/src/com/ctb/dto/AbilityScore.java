package com.ctb.dto;

import com.ctb.utils.Utility;




public class AbilityScore 
{

	private String readingAbilityScore = "";
	private String readingSEMScore= "";
	private String mathCompAbilityScore = "";
	private String mathCompSEMScore= "";
	private String appliedMathAbilityScore = "";
	private String appliedMathSEMScore= "";
	private String languageAbilityScore = "";
	private String languageSEMScore= "";
	
	private String totalMathAbilityScore = "";
	private String totalBatteryAbilityScore= "";
	




	/**
	 * @return the readingAbilityScore
	 */
	public String getReadingAbilityScore() {
		return readingAbilityScore;
	}



	/**
	 * @param readingAbilityScore the readingAbilityScore to set
	 */
	public void setReadingAbilityScore(String readingAbilityScore) {
		this.readingAbilityScore = readingAbilityScore;
	}



	/**
	 * @return the readingSEMScore
	 */
	public String getReadingSEMScore() {
		return readingSEMScore;
	}



	/**
	 * @param readingSEMScore the readingSEMScore to set
	 */
	public void setReadingSEMScore(String readingSEMScore) {
		this.readingSEMScore = readingSEMScore;
	}



	/**
	 * @return the mathCompAbilityScore
	 */
	public String getMathCompAbilityScore() {
		return mathCompAbilityScore;
	}



	/**
	 * @param mathCompAbilityScore the mathCompAbilityScore to set
	 */
	public void setMathCompAbilityScore(String mathCompAbilityScore) {
		this.mathCompAbilityScore = mathCompAbilityScore;
	}



	/**
	 * @return the mathCompSEMScore
	 */
	public String getMathCompSEMScore() {
		return mathCompSEMScore;
	}



	/**
	 * @param mathCompSEMScore the mathCompSEMScore to set
	 */
	public void setMathCompSEMScore(String mathCompSEMScore) {
		this.mathCompSEMScore = mathCompSEMScore;
	}



	/**
	 * @return the appliedMathAbilityScore
	 */
	public String getAppliedMathAbilityScore() {
		return appliedMathAbilityScore;
	}



	/**
	 * @param appliedMathAbilityScore the appliedMathAbilityScore to set
	 */
	public void setAppliedMathAbilityScore(String appliedMathAbilityScore) {
		this.appliedMathAbilityScore = appliedMathAbilityScore;
	}



	/**
	 * @return the appliedMathSEMScore
	 */
	public String getAppliedMathSEMScore() {
		return appliedMathSEMScore;
	}



	/**
	 * @param appliedMathSEMScore the appliedMathSEMScore to set
	 */
	public void setAppliedMathSEMScore(String appliedMathSEMScore) {
		this.appliedMathSEMScore = appliedMathSEMScore;
	}



	/**
	 * @return the languageAbilityScore
	 */
	public String getLanguageAbilityScore() {
		return languageAbilityScore;
	}



	/**
	 * @param languageAbilityScore the languageAbilityScore to set
	 */
	public void setLanguageAbilityScore(String languageAbilityScore) {
		this.languageAbilityScore = languageAbilityScore;
	}



	/**
	 * @return the languageSEMScore
	 */
	public String getLanguageSEMScore() {
		return languageSEMScore;
	}



	/**
	 * @param languageSEMScore the languageSEMScore to set
	 */
	public void setLanguageSEMScore(String languageSEMScore) {
		this.languageSEMScore = languageSEMScore;
	}



	/**
	 * @return the totalMathAbilityScore
	 */
	public String getTotalMathAbilityScore() {
		return totalMathAbilityScore;
	}



	/**
	 * @param totalMathAbilityScore the totalMathAbilityScore to set
	 */
	public void setTotalMathAbilityScore(String totalMathAbilityScore) {
		this.totalMathAbilityScore = totalMathAbilityScore;
	}



	/**
	 * @return the totalBatteryAbilityScore
	 */
	public String getTotalBatteryAbilityScore() {
		return totalBatteryAbilityScore;
	}



	/**
	 * @param totalBatteryAbilityScore the totalBatteryAbilityScore to set
	 */
	public void setTotalBatteryAbilityScore(String totalBatteryAbilityScore) {
		this.totalBatteryAbilityScore = totalBatteryAbilityScore;
	}
	
	
	public String toString(){
		String val="";
		val += Utility.getFormatedString(readingAbilityScore, 3)
		+Utility.getFormatedString(readingSEMScore, 3) 
		+Utility.getFormatedString(mathCompAbilityScore, 3) 
		+Utility.getFormatedString(mathCompSEMScore, 3) 
		+Utility.getFormatedString(appliedMathAbilityScore, 3) 
		+Utility.getFormatedString(appliedMathSEMScore, 3) 
		+Utility.getFormatedString(languageAbilityScore, 3)
		+Utility.getFormatedString(languageSEMScore, 3)
		+Utility.getFormatedString(totalMathAbilityScore, 3)
		+Utility.getFormatedString(totalBatteryAbilityScore, 3);

		return val;
		
	}

}
