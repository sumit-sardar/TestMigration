package com.mhe.ctb.oas.BMTSync.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentRoster {

	private String oasRosterId;
	private String oasStudentId; 
	private String studentpassword;
	private List<TestDelivery>  testDeliveryList;

	
	public String getOasRosterId() {
		return oasRosterId;
	}
	@JsonProperty(value="oasRoasterId", required=true)
	public void setOasRosterId(String oasRosterId) {
		this.oasRosterId = oasRosterId;
	}
	
	public String getOasStudentid() {
		return oasStudentId;
	}
	@JsonProperty(value="oasStudentId", required=true)
	public void setOasStudentid(String oasStudentId) {
		this.oasStudentId = oasStudentId;
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
