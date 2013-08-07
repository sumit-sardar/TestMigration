package com.ctb.dto;

import com.ctb.utils.Utility;




public class PercentageMastery 
{
	private String reading ="";
	private String mathComp="";
	private String appliedMath="";
	private String language="";
	private String languageMech ="";
	private String spelling="";
	private String vocabulary="";
	
	public String toString(){
		String val="";
		val += Utility.getFormatedString(reading, 3) 
		+Utility.getFormatedString(mathComp, 3) 
		+Utility.getFormatedString(appliedMath, 3)
		+Utility.getFormatedString(language, 3);

		return val;
	}
	
	/**
	 * @return the reading
	 */
	public String getReading() {
		return reading;
	}
	/**
	 * @param reading the reading to set
	 */
	public void setReading(String reading) {
		this.reading = reading;
	}
	/**
	 * @return the mathComp
	 */
	public String getMathComp() {
		return mathComp;
	}
	/**
	 * @param mathComp the mathComp to set
	 */
	public void setMathComp(String mathComp) {
		this.mathComp = mathComp;
	}
	/**
	 * @return the appliedMath
	 */
	public String getAppliedMath() {
		return appliedMath;
	}
	/**
	 * @param appliedMath the appliedMath to set
	 */
	public void setAppliedMath(String appliedMath) {
		this.appliedMath = appliedMath;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the languageMech
	 */
	public String getLanguageMech() {
		return languageMech;
	}

	/**
	 * @param languageMech the languageMech to set
	 */
	public void setLanguageMech(String languageMech) {
		this.languageMech = languageMech;
	}

	/**
	 * @return the spelling
	 */
	public String getSpelling() {
		return spelling;
	}

	/**
	 * @param spelling the spelling to set
	 */
	public void setSpelling(String spelling) {
		this.spelling = spelling;
	}

	/**
	 * @return the vocabulary
	 */
	public String getVocabulary() {
		return vocabulary;
	}

	/**
	 * @param vocabulary the vocabulary to set
	 */
	public void setVocabulary(String vocabulary) {
		this.vocabulary = vocabulary;
	}


	
}


