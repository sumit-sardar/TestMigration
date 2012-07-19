package com.ctb.lexington.db.irsdata.irstvdata;

import java.math.BigDecimal;

public class WsTVPrimObjFactData {
	
    private Long primObjid;
    private String primObjName;
    private Long studentScore;
    private String masteryLevel;
    private BigDecimal nationalAverage;
    
    
	public Long getPrimObjid() {
		return primObjid;
	}
	public void setPrimObjid(Long primObjid) {
		this.primObjid = primObjid;
	}
	public String getPrimObjName() {
		return primObjName;
	}
	public void setPrimObjName(String primObjName) {
		this.primObjName = primObjName;
	}
	public String getMasteryLevel() {
		return masteryLevel;
	}
	public void setMasteryLevel(String masteryLevel) {
		this.masteryLevel = masteryLevel;
	}
	public BigDecimal getNationalAverage() {
		return nationalAverage;
	}
	public void setNationalAverage(BigDecimal nationalAverage) {
		this.nationalAverage = nationalAverage;
	}
	public Long getStudentScore() {
		return studentScore;
	}
	public void setStudentScore(Long studentScore) {
		this.studentScore = studentScore;
	}

}
