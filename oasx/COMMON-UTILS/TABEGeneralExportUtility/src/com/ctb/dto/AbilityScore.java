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
	private String languageMechAbilityScore = "";
	private String languageMechSEMScore= "";
	private String spellingAbilityScore = "";
	private String spellingSEMScore= "";
	private String vocabularyAbilityScore = "";
	private String vocabularySEMScore= "";
	
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
		if(readingSEMScore != null && readingSEMScore.length() > 0) {
			return String.valueOf(Math.round(Double.valueOf(readingSEMScore)));
		}
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
		if(mathCompSEMScore != null && mathCompSEMScore.length() > 0) {
			return String.valueOf(Math.round(Double.valueOf(mathCompSEMScore)));
		}
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
		if(appliedMathSEMScore != null && appliedMathSEMScore.length() > 0) {
			return String.valueOf(Math.round(Double.valueOf(appliedMathSEMScore)));
		}
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
		if(languageSEMScore != null && languageSEMScore.length() > 0) {
			return String.valueOf(Math.round(Double.valueOf(languageSEMScore)));
		}
		return languageSEMScore;
	}



	/**
	 * @param languageSEMScore the languageSEMScore to set
	 */
	public void setLanguageSEMScore(String languageSEMScore) {
		this.languageSEMScore = languageSEMScore;
	}



	/**
	 * @return the languageMechAbilityScore
	 */
	public String getLanguageMechAbilityScore() {
		return languageMechAbilityScore;
	}



	/**
	 * @param languageMechAbilityScore the languageMechAbilityScore to set
	 */
	public void setLanguageMechAbilityScore(String languageMechAbilityScore) {
		this.languageMechAbilityScore = languageMechAbilityScore;
	}



	/**
	 * @return the languageMechSEMScore
	 */
	public String getLanguageMechSEMScore() {
		return languageMechSEMScore;
	}



	/**
	 * @param languageMechSEMScore the languageMechSEMScore to set
	 */
	public void setLanguageMechSEMScore(String languageMechSEMScore) {
		this.languageMechSEMScore = languageMechSEMScore;
	}



	/**
	 * @return the spellingAbilityScore
	 */
	public String getSpellingAbilityScore() {
		return spellingAbilityScore;
	}



	/**
	 * @param spellingAbilityScore the spellingAbilityScore to set
	 */
	public void setSpellingAbilityScore(String spellingAbilityScore) {
		this.spellingAbilityScore = spellingAbilityScore;
	}



	/**
	 * @return the spellingSEMScore
	 */
	public String getSpellingSEMScore() {
		return spellingSEMScore;
	}



	/**
	 * @param spellingSEMScore the spellingSEMScore to set
	 */
	public void setSpellingSEMScore(String spellingSEMScore) {
		this.spellingSEMScore = spellingSEMScore;
	}



	/**
	 * @return the vocabularyAbilityScore
	 */
	public String getVocabularyAbilityScore() {
		return vocabularyAbilityScore;
	}



	/**
	 * @param vocabularyAbilityScore the vocabularyAbilityScore to set
	 */
	public void setVocabularyAbilityScore(String vocabularyAbilityScore) {
		this.vocabularyAbilityScore = vocabularyAbilityScore;
	}



	/**
	 * @return the vocabularySEMScore
	 */
	public String getVocabularySEMScore() {
		return vocabularySEMScore;
	}



	/**
	 * @param vocabularySEMScore the vocabularySEMScore to set
	 */
	public void setVocabularySEMScore(String vocabularySEMScore) {
		this.vocabularySEMScore = vocabularySEMScore;
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
		+Utility.getFormatedString(getReadingSEMScore(), 3) 
		+Utility.getFormatedString(mathCompAbilityScore, 3) 
		+Utility.getFormatedString(getMathCompSEMScore(), 3) 
		+Utility.getFormatedString(appliedMathAbilityScore, 3) 
		+Utility.getFormatedString(getAppliedMathSEMScore(), 3) 
		+Utility.getFormatedString(languageAbilityScore, 3)
		+Utility.getFormatedString(getLanguageSEMScore(), 3)
		+Utility.getFormatedString(totalMathAbilityScore, 3)
		+Utility.getFormatedString(totalBatteryAbilityScore, 3);

		return val;
		
	}
}
