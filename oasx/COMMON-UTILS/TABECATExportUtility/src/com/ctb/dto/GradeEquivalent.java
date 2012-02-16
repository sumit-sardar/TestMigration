package com.ctb.dto;

import com.ctb.utils.Utility;


public class GradeEquivalent 
{
	
	private String reading ="";
	private String mathComp="";
	private String appliedMath="";
	private String language="";
	private String totalMath="";
	private String totalBattery="";


	
	public String toString(){
		
		String val="";
		val += Utility.getNumberFormatedString(reading)
		+Utility.getNumberFormatedString(mathComp) 
		+Utility.getNumberFormatedString(appliedMath) 
		+Utility.getNumberFormatedString(language) 
		+Utility.getNumberFormatedString(totalMath)
		+Utility.getNumberFormatedString(totalBattery); 		

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
	 * @return the totalMath
	 */
	public String getTotalMath() {
		return totalMath;
	}



	/**
	 * @param totalMath the totalMath to set
	 */
	public void setTotalMath(String totalMath) {
		this.totalMath = totalMath;
	}



	/**
	 * @return the totalBattery
	 */
	public String getTotalBattery() {
		return totalBattery;
	}



	/**
	 * @param totalBattery the totalBattery to set
	 */
	public void setTotalBattery(String totalBattery) {
		this.totalBattery = totalBattery;
	}
	
}
