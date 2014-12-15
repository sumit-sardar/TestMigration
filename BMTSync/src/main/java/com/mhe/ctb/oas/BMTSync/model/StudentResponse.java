package com.mhe.ctb.oas.BMTSync.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mhe.ctb.oas.BMTSync.model.Student.Accomodations;

/**
 * A Student represented in JSON
 * 
 * @author cparis
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"oasStudentId", "oasCustomerId", "studentusername", 
	"firstName","middleName", "lastName","birthdate","gender", "grade",
    "heirarchySet", "accomodations","customerStudentId", 
    "errorCode","errorMessage"  } )

public class StudentResponse {
	private Integer _oasStudentId;
	private Integer _oasCustomerId;
	private String _studentusername;
	private String _firstName;
	private String _middleName;
	private String _lastName;
	private String _birthDate;
	private String _gender;
	private String _grade;
	//private String[] _extStudentId;
	private List<HeirarchyNode> _heirarchySet;
	//private StudentAccomodation _accomodations;
	private Accomodations accomodations;
	private String _customerStudentId;
	private Integer _errorCode;
	private String _errorMessage; 
	


	
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

	public String getStudentusername() {
		return _studentusername;
	}

	@JsonProperty("studentusername")
	public void setStudentusername(String studentUsername) {
		_studentusername = studentUsername;
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

	public String getBirthDate() {
		return _birthDate;
	}

	@JsonProperty("birthDate")
	public void setBirthDate(String birthDate) {
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


	public List<HeirarchyNode> getHeirarchySet() {
		return _heirarchySet;
	}
	
	@JsonProperty("heirarchySet")
	public void setHeirarchySet(List<HeirarchyNode> heirarchySet) {
		_heirarchySet = heirarchySet;
	}

	public Accomodations getAccomodations() {
		return accomodations;
	}
	
	@JsonProperty("accomodations")
	public void setAccomodations(Accomodations accomodations) {
		this.accomodations = accomodations;
	}
	

	public String getCustomerStudentId() {
		return _customerStudentId;
	}

	@JsonProperty("customerStudentId")
	public void setCustomerStudentId(String oasCustomerStudentId) {
		_customerStudentId = oasCustomerStudentId;
	}
	

	public Integer getErrorCode() {
		return _errorCode;
	}

	@JsonProperty(value="errorCode", required=true)
	public void setErrorCode(Integer errorCode) {
		_errorCode = errorCode;
	}
	
	public String getErrorMessage() {
		return _errorMessage;
	}

	
	@JsonProperty("errorMessage")
	public void setErrorMessage(String errorMessage) {
		_errorMessage = errorMessage;
	}	
	

	/*
	public String[] getExtStudentId() {
		return _extStudentId;
	}

	@JsonProperty("studentIds")
	public void setExtStudentId(String[] extStudentId) {
		_extStudentId = extStudentId;
	}
    */	
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
