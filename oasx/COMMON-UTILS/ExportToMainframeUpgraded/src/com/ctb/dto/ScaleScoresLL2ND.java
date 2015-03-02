package com.ctb.dto;

import com.ctb.utils.EmetricUtil;




public class ScaleScoresLL2ND
{

	private String speaking = "";
	private String listening= "";
	private String reading= "";
	private String writing= "";
	private String overall= "";
	private String comprehension= "";
	private String oral= "";
	private String productive= "";
	private String literacy= "";

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

	public String getOverall() {
		return overall;
	}

	public void setOverall(String overall) {
		this.overall = overall;
	}

	public String getComprehension() {
		return comprehension;
	}

	public void setComprehension(String comprehension) {
		this.comprehension = comprehension;
	}

	public String getOral() {
		return oral;
	}

	public void setOral(String oral) {
		this.oral = oral;
	}
	
	public String getProductive() {
		return productive;
	}

	public void setProductive(String productive) {
		this.productive = productive;
	}

	public String getLiteracy() {
		return literacy;
	}

	public void setLiteracy(String literacy) {
		this.literacy = literacy;
	}
	
	public String toString(){
		String val="";
		val += EmetricUtil.getFormatedStringScaleScore(speaking, 3)
		+EmetricUtil.getFormatedStringScaleScore(listening, 3) 
		+EmetricUtil.getFormatedStringScaleScore(reading, 3) 
		+EmetricUtil.getFormatedStringScaleScore(writing, 3) 
		+EmetricUtil.getFormatedStringScaleScore(overall, 3) 
		+EmetricUtil.getFormatedStringScaleScore(comprehension, 3) 
		+EmetricUtil.getFormatedStringScaleScore(oral, 3)
		+EmetricUtil.getFormatedStringScaleScore(literacy, 3)
		+EmetricUtil.getFormatedStringScaleScore(productive, 3);

		return val;
		
	}
}
