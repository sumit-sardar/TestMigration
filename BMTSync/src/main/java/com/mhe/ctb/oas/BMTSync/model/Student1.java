package com.mhe.ctb.oas.BMTSync.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * A Student represented in JSON
 * 
 * @author cparis
 */
public class Student1 {
	private Integer _oasStudentId;
	private Integer _oasCustomerId;
	private String _loginName;
	private String _firstName;
	private String _middleName;
	private String _lastName;
	private Date _birthDate;
	private String _gender;
	private String _grade;
	private String[] _extStudentId;
	private List<HeirarchyNode1> _heirarchy;

	public Integer getOasStudentId() {
		return _oasStudentId;
	}

	@JsonProperty(value="oasStudentId", required=true)
	public void setOasStudentId(Integer oasStudentId) {
		_oasStudentId = oasStudentId;
	}

	public Integer getOasCustomerId() {
		return _oasCustomerId;
	}

	@JsonProperty(value="oasCustomerId", required=true)
	public void setOasCustomerId(Integer oasCustomerId) {
		_oasCustomerId = oasCustomerId;
	}

	public String getLoginName() {
		return _loginName;
	}

	@JsonProperty("loginName")
	public void setLoginName(String loginName) {
		_loginName = loginName;
	}

	public String getFirstName() {
		return _firstName;
	}

	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		_firstName = firstName;
	}

	public String getMiddleName() {
		return _middleName;
	}

	@JsonProperty("middleName")
	public void setMiddleName(String middleName) {
		_middleName = middleName;
	}

	public String getLastName() {
		return _lastName;
	}

	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		_lastName = lastName;
	}

	public Date getBirthDate() {
		return _birthDate;
	}

	@JsonProperty("birthDate")
	public void setBirthDate(Date birthDate) {
		_birthDate = birthDate;
	}

	public String getGender() {
		return _gender;
	}

	@JsonProperty("gender")
	public void setGender(String gender) {
		_gender = gender;
	}

	public String getGrade() {
		return _grade;
	}

	@JsonProperty("grade")
	public void setGrade(String grade) {
		_grade = grade;
	}

	public String[] getExtStudentId() {
		return _extStudentId;
	}

	@JsonProperty("studentIds")
	public void setExtStudentId(String[] extStudentId) {
		_extStudentId = extStudentId;
	}
	
	public List<HeirarchyNode1> getHeirarchy() {
		return _heirarchy;
	}
	
	@JsonProperty("heirarchySet")
	public void setHeirarchy(List<HeirarchyNode1> heirarchy) {
		_heirarchy = heirarchy;
	}

	/*
	 * 
	 * "oasStudentId":1234, "oasCustomerId":1234,
	 * "studentusername":"austin_coffey_1106", "firstName":"John",
	 * "middlename":"A.", "lastName": "Doe", "birthdate":"11/6/1996",
	 * "gender":"M", "grade":"3",
	 * 
	 * "customerStudentId":"788973", "heirarchySet": { "State" :
	 * {OasHeirarchyId":"0", "Code":"0", "Name":"California"}, "District"
	 * :{OasHeirarchyId":"32101", "Code":"98574", "Name":"Monterey"}, "School" :
	 * {OasHeirarchyId":"43185", "Code":"132574", "Name":"Monterey Elementary"},
	 * "Class": [ {"OasHierarchyId":"89089", "Name":"Mrs Jones"},
	 * {"OasHierarchyId":"89065", "Name":"Mrs Smith"} ] }
	 */
}
