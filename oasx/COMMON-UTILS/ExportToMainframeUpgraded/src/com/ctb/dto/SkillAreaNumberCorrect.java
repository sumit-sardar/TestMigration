package com.ctb.dto;

import com.ctb.utils.EmetricUtil;




public class SkillAreaNumberCorrect 
{

	private String speaking= "";
	private String listening= "";
	private String reading= "";
	private String writing= "";
	private String unused= "";
		

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
		val += EmetricUtil.getFormatedNumberCorrectString(speaking, 3)
		+EmetricUtil.getFormatedNumberCorrectString(listening, 3) 
		+EmetricUtil.getFormatedNumberCorrectString(reading, 3) 
		+EmetricUtil.getFormatedNumberCorrectString(writing, 3) 
		+EmetricUtil.getFormatedNumberCorrectString(unused, 9);
	

		return val;
		
	}
}


