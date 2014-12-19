package com.mhe.ctb.oas.BMTSync.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentRoster {

	private Integer oasRosterId;
	private Integer oasStudentid; 
	private String studentpassword;
	private List<TestDelivery>  testDeliveryList;

	
	public Integer getOasRosterId() {
		return oasRosterId;
	}
	@JsonProperty(value="oasRosterId", required=true)
	public void setOasRosterId(Integer oasRosterId) {
		this.oasRosterId = oasRosterId;
	}
	
	public Integer getOasStudentid() {
		return oasStudentid;
	}
	@JsonProperty(value="oasStudentid", required=true)
	public void setOasStudentid(Integer oasStudentid) {
		this.oasStudentid = oasStudentid;
	}
	
	
	public String getStudentpassword() {
		return studentpassword;
	}
	@JsonProperty(value="studentpassword", required=true)
	public void setStudentpassword(String studentpassword) {
		this.studentpassword = studentpassword;
	}
	
	
	public List<TestDelivery> getTestDelivery() {
		return testDeliveryList;
	}
	@JsonProperty(value="parts", required=true)
	public void setTestDelivery(List<TestDelivery> testDeliveryList) {
		this.testDeliveryList = testDeliveryList;
	}
	

	
}
