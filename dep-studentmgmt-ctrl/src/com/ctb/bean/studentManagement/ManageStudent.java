package com.ctb.bean.studentManagement; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.studentManagement.Address;

import java.util.Date;

/**
 * Data bean representing the partial contents of the OAS.STUDENT table 
 * and the student's assigned org nodes.
 * 
 * @author John_Wang
 */
public class ManageStudent extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer id;
    private String loginId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String studentName;
    private String studentIdNumber;
    private String studentIdNumber2;
    private String grade;
    private String gender;
    private Date birthDate;
    private Integer createdBy;
    private OrganizationNode [] organizationNodes;
     //Changes for CA-ABE
    //START
    private Address address;
    private Integer addressId;
    private String instructorFirstName;
    private String instructorLastName;
    private String visibleAcrossOrganization;
    private String isSSN;
    private String isPBAFormSigned;
    //END
    
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}
	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the organizationNodes
	 */
	public OrganizationNode[] getOrganizationNodes() {
		return organizationNodes;
	}
	/**
	 * @param organizationNodes the organizationNodes to set
	 */
	public void setOrganizationNodes(OrganizationNode[] organizationNodes) {
		this.organizationNodes = organizationNodes;
	}
	/**
	 * @return the studentName
	 */
	public String getStudentName() {
		return studentName;
	}
	/**
	 * @param studentName the studentName to set
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * @return the studentIdNumber
	 */
	public String getStudentIdNumber() {
		return studentIdNumber;
	}
	/**
	 * @param studentIdNumber the studentIdNumber to set
	 */
	public void setStudentIdNumber(String studentIdNumber) {
		this.studentIdNumber = studentIdNumber;
	}
	/**
	 * @return the studentIdNumber2
	 */
	public String getStudentIdNumber2() {
		return studentIdNumber2;
	}
	/**
	 * @param studentIdNumber2 the studentIdNumber2 to set
	 */
	public void setStudentIdNumber2(String studentIdNumber2) {
		this.studentIdNumber2 = studentIdNumber2;
	}
	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}    
	/**
	 * @return Returns the createdBy.
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
	public Integer getAddressId() {
		return addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	/**
	 * @return the instructorFirstName
	 */
	public String getInstructorFirstName() {
		return instructorFirstName;
	}
	/**
	 * @param instructorFirstName the instructorFirstName to set
	 */
	public void setInstructorFirstName(String instructorFirstName) {
		this.instructorFirstName = instructorFirstName;
	}
	/**
	 * @return the instructorLastName
	 */
	public String getInstructorLastName() {
		return instructorLastName;
	}
	/**
	 * @param instructorLastName the instructorLastName to set
	 */
	public void setInstructorLastName(String instructorLastName) {
		this.instructorLastName = instructorLastName;
	}
	/**
	 * @return the visibleAcrossOrganization
	 */
	public String getVisibleAcrossOrganization() {
		return visibleAcrossOrganization;
	}
	/**
	 * @param visibleAcrossOrganization the visibleAcrossOrganization to set
	 */
	public void setVisibleAcrossOrganization(String visibleAcrossOrganization) {
		this.visibleAcrossOrganization = visibleAcrossOrganization;
	}
	/**
	 * @return the isSSN
	 */
	public String getIsSSN() {
		return isSSN;
	}
	/**
	 * @param isSSN the isSSN to set
	 */
	public void setIsSSN(String isSSN) {
		this.isSSN = isSSN;
	}
	/**
	 * @return the isPBAFormSigned
	 */
	public String getIsPBAFormSigned() {
		return isPBAFormSigned;
	}
	/**
	 * @param isPBAFormSigned the isPBAFormSigned to set
	 */
	public void setIsPBAFormSigned(String isPBAFormSigned) {
		this.isPBAFormSigned = isPBAFormSigned;
	}
    
} 

