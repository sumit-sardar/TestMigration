package com.ctb.dto;

import com.ctb.utils.Utility;




public class PercentageMastery 
{
	private String reading ="";
	private String mathComp="";
	private String appliedMath="";
	private String language="";
	
	
	
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


	
}


