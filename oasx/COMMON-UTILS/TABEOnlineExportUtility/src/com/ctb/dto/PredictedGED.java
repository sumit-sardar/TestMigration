package com.ctb.dto;

import com.ctb.utils.Utility;

public class PredictedGED {
	
	private String average = "";
	private String math ="";
	private String reading ="";
	private String science="";
	private String socialStudies="";
	private String writing="";
	
	public String toString(){
		String val="";
		val += Utility.getFormatedString(average, 3) 
		+Utility.getFormatedString(math, 3) 
		+Utility.getFormatedString(reading, 3)
		+Utility.getFormatedString(science, 3)
		+Utility.getFormatedString(socialStudies, 3)
		+Utility.getFormatedString(writing, 3);

		return val;
		
	}
	/**
	 * @return the average
	 */
	public String getAverage() {
		return average;
	}
	/**
	 * @param average the average to set
	 */
	public void setAverage(String average) {
		this.average = average;
	}
	/**
	 * @return the math
	 */
	public String getMath() {
		return math;
	}
	/**
	 * @param math the math to set
	 */
	public void setMath(String math) {
		this.math = math;
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
	 * @return the science
	 */
	public String getScience() {
		return science;
	}
	/**
	 * @param science the science to set
	 */
	public void setScience(String science) {
		this.science = science;
	}
	/**
	 * @return the socialStudies
	 */
	public String getSocialStudies() {
		return socialStudies;
	}
	/**
	 * @param socialStudies the socialStudies to set
	 */
	public void setSocialStudies(String socialStudies) {
		this.socialStudies = socialStudies;
	}
	/**
	 * @return the writing
	 */
	public String getWriting() {
		return writing;
	}
	/**
	 * @param writing the writing to set
	 */
	public void setWriting(String writing) {
		this.writing = writing;
	}
	
}
