package com.ctb.lexington.db.data;

import java.util.Date;

import com.ctb.lexington.db.record.Persistent;
import java.sql.Timestamp;

public class StudentData implements Persistent{
	private String firstName;
	private String lastName;
	private String customer;
	private String middleInitial;
	private Date birthDate;
	private String email;
	private String gender;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zipCode;
	private String studentSpecialCode;
	private String studentIdentifier1;
	private String studentIdentifier2;
	private String studentIdentifier3;
	//private String grade;
	private Long ageInYears;
	private Long ageInMonths;
	private String orgNode;

	private Long oasStudentId;
	private String extElmId;
	private String grade;
	private Long demographicId;
    
    private Long studentDimId;
    private Long studentDimVersionId;
    
    private String active;
    
    private String barcode;
    
    private boolean performMatching = true;
    
    private Timestamp startDateTime;
    
    /**
	 * @return Returns the startDateTime.
	 */
	public Timestamp getStartDateTime() {
		return startDateTime;
	}

	/**
	 * @param startDateTime The startDateTime to set.
	 */
	public void setStartDateTime(Timestamp startDateTime) {
		this.startDateTime = startDateTime;
	}

	public boolean performMatching() {
    	return this.performMatching;
    }
	
    public void setPerformMatching(boolean performMatching) {
    	this.performMatching = performMatching;
    }

	/**
	 * @return Returns the addressLine1.
	 */
	public String getAddressLine1() {
		return addressLine1;
	}
	/**
	 * @param addressLine1 The addressLine1 to set.
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	/**
	 * @return Returns the addressLine2.
	 */
	public String getAddressLine2() {
		return addressLine2;
	}
	/**
	 * @param addressLine2 The addressLine2 to set.
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	/**
	 * @return Returns the ageInMonths.
	 */
	public Long getAgeInMonths() {
		return ageInMonths;
	}
	/**
	 * @param ageInMonths The ageInMonths to set.
	 */
	public void setAgeInMonths(Long ageInMonths) {
		this.ageInMonths = ageInMonths;
	}
	/**
	 * @return Returns the ageInYears.
	 */
	public Long getAgeInYears() {
		return ageInYears;
	}
	/**
	 * @param ageInYears The ageInYears to set.
	 */
	public void setAgeInYears(Long ageInYears) {
		this.ageInYears = ageInYears;
	}
	/**
	 * @return Returns the birthDate.
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * @param birthDate The birthDate to set.
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city The city to set.
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return Returns the compositeStudentId.
	 */
	public CompositeStudentId getCompositeStudentId() {
		return new CompositeStudentId(studentDimId, studentDimVersionId);
	}
	/**
	 * @param compositeStudentId The compositeStudentId to set.
	 */
	public void setCompositeStudentId(CompositeStudentId compositeStudentId) {
        studentDimId = compositeStudentId.getStudentId();
        studentDimVersionId = compositeStudentId.getStudentVersionId();
	}
	/**
	 * @return Returns the customer.
	 */
	public String getCustomer() {
		return customer;
	}
	/**
	 * @param customer The customer to set.
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	/**
	 * @return Returns the demographicId.
	 */
	public Long getDemographicId() {
		return demographicId;
	}
	/**
	 * @param demographicId The demographicId to set.
	 */
	public void setDemographicId(Long demographicId) {
		this.demographicId = demographicId;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the extElmId.
	 */
	public String getExtElmId() {
		return extElmId;
	}
	/**
	 * @param extElmId The extElmId to set.
	 */
	public void setExtElmId(String extElmId) {
		this.extElmId = extElmId;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return Returns the gender.
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender The gender to set.
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return Returns the grade.
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * @param grade The grade to set.
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return Returns the middleInitial.
	 */
	public String getMiddleInitial() {
		return middleInitial;
	}
	/**
	 * @param middleInitial The middleInitial to set.
	 */
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	/**
	 * @return Returns the oasStudentId.
	 */
	public Long getOasStudentId() {
		return oasStudentId;
	}
	/**
	 * @param oasStudentId The oasStudentId to set.
	 */
	public void setOasStudentId(Long oasStudentId) {
		this.oasStudentId = oasStudentId;
	}
	/**
	 * @return Returns the orgNode.
	 */
	public String getOrgNode() {
		return orgNode;
	}
	/**
	 * @param orgNode The orgNode to set.
	 */
	public void setOrgNode(String orgNode) {
		this.orgNode = orgNode;
	}
	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return Returns the studentIdentifier1.
	 */
	public String getStudentIdentifier1() {
		return studentIdentifier1;
	}
	/**
	 * @param studentIdentifier1 The studentIdentifier1 to set.
	 */
	public void setStudentIdentifier1(String studentIdentifier1) {
		this.studentIdentifier1 = studentIdentifier1;
	}
	/**
	 * @return Returns the studentIdentifier2.
	 */
	public String getStudentIdentifier2() {
		return studentIdentifier2;
	}
	/**
	 * @param studentIdentifier2 The studentIdentifier2 to set.
	 */
	public void setStudentIdentifier2(String studentIdentifier2) {
		this.studentIdentifier2 = studentIdentifier2;
	}
	/**
	 * @return Returns the studentIdentifier3.
	 */
	public String getStudentIdentifier3() {
		return studentIdentifier3;
	}
	/**
	 * @param studentIdentifier3 The studentIdentifier3 to set.
	 */
	public void setStudentIdentifier3(String studentIdentifier3) {
		this.studentIdentifier3 = studentIdentifier3;
	}
	/**
	 * @return Returns the studentSpecialCode.
	 */
	public String getStudentSpecialCode() {
		return studentSpecialCode;
	}
	/**
	 * @param studentSpecialCode The studentSpecialCode to set.
	 */
	public void setStudentSpecialCode(String studentSpecialCode) {
		this.studentSpecialCode = studentSpecialCode;
	}
	/**
	 * @return Returns the zipCode.
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode The zipCode to set.
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
    
    
    public Long getStudentDimId() {
        return studentDimId;
    }
    public void setStudentDimId(Long studentDimId) {
        this.studentDimId = studentDimId;
    }
    public Long getStudentDimVersionId() {
        return studentDimVersionId;
    }
    public void setStudentDimVersionId(Long studentDimVersionId) {
        this.studentDimVersionId = studentDimVersionId;
    }
    
    
    public String getActive() {
        return active;
    }
    public void setActive(String active) {
        this.active = active;
    }
    
    public String toString() {
        StringBuffer result = new StringBuffer("\n-----");
        result.append(getClass().getName());
        result.append("-----");
        
        result.append("\nfirstName = ");
        result.append(firstName);
        result.append("\nlastName = ");
        result.append(lastName);
        result.append("\ncustomer = ");
        result.append(customer);
        result.append("\nmiddleInitial = ");
        result.append(middleInitial);
        result.append("\nbirthDate = ");
        result.append(birthDate);
        result.append("\nemail = ");
        result.append(email);
        result.append("\ngender = ");
        result.append(gender);
        result.append("\naddressLine1 = ");
        result.append(addressLine1);
        result.append("\naddressLine2 = ");
        result.append(addressLine2);
        result.append("\ncity = ");
        result.append(city);
        result.append("\nstate = ");
        result.append(state);
        result.append("\nzipCode = ");
        result.append(zipCode);
        result.append("\nstudentSpecialCode = ");
        result.append(studentSpecialCode);
        result.append("\nstudentIdentifier1 = ");
        result.append(studentIdentifier1);
        result.append("\nstudentIdentifier2 = ");
        result.append(studentIdentifier2);
        result.append("\nstudentIdentifier3 = ");
        result.append(studentIdentifier3);
        result.append("\nageInYears = ");
        result.append(ageInYears);
        result.append("\nageInMonths = ");
        result.append(ageInMonths);
        result.append("\norgNode = ");
        result.append(orgNode);
        result.append("\noasStudentId = ");
        result.append(oasStudentId);
        result.append("\nextElmId = ");
        result.append(extElmId);
        result.append("\ngrade = ");
        result.append(grade);
        result.append("\ndemographicId = ");
        result.append(demographicId);
        result.append("\nstudentDimId = ");
        result.append(studentDimId);
        result.append("\nstudentDimVersionId = ");
        result.append(studentDimVersionId);
        result.append("\nactive = ");
        result.append(active);
        
        result.append("\n-----END-----");
        return result.toString();
    }

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
