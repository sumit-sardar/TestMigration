package com.ctb.dto;

import com.ctb.utils.EmetricUtil;




public class SkillAreaPercentCorrect
{
	
	private String speaking="";
	private String listening="";
	private String reading="";
	private String writing="";
	private String unused="";
		

	public String getSpeaking() {
		return speaking;
	}

	public void setSpeaking(String speaking) {
		this.speaking = speaking;
	}

	public String getListening() {
		return listening;
	}

	public void setListening(String listening) {
		this.listening = listening;
	}

	public String getReading() {
		return reading;
	}

	public void setReading(String reading) {
		this.reading = reading;
	}

	public String getWriting() {
		return writing;
	}

	public void setWriting(String writing) {
		this.writing = writing;
	}

	public String getUnused() {
		return unused;
	}

	public void setUnused(String unused) {
		this.unused = unused;
	}
	public String toString(){
		String val="";
		val += EmetricUtil.getNumberFormatedString(speaking)
		+EmetricUtil.getNumberFormatedString(listening) 
		+EmetricUtil.getNumberFormatedString(reading) 
		+EmetricUtil.getNumberFormatedString(writing) 
		+EmetricUtil.getFormatedString(unused, 15);
		

		return val;
		
	}
}
