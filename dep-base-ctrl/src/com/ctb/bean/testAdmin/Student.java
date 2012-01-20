package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;
import java.util.HashMap;

/**
 * Data bean representing the contents of the OAS.STUDENT table
 * 
 * @author Nate_Cohen, John_Wang
 */
public class Student extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer studentId;
    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredName;
    private String prefix;
    private String suffix;
    private Date birthdate;
    private String gender;
    private String ethnicity;
    private String email;
    private String grade;
    private String extElmId;
    private String extPin1;
    private String extPin2;
    private String extPin3;
    private String extSchoolId;
    private String activeSession;
    private String potentialDuplicatedStudent;
    private Integer createdBy;
    private Date createdDateTime;
    private Integer updatedBy;
    private Date updatedDateTime;
    private String activationStatus;
    private Integer dataImportHistoryId;
    private Integer customerId;
    private Integer precodeId;
    private String barcode;
    private String udf;
    private String udf1;
    private String udf2;
    private String testPurpose;
    private String extendedTimeAccom; // Added for Student PAcing
    private String outOfSchool; //Added for out of school changes
    
    
    // Start changes for Student Pacing
    /**
	 * @return the extendedTimeAccom
	 */
	public String getExtendedTimeAccom() {
		return extendedTimeAccom;
	}

	/**
	 * @param extendedTimeAccom the extendedTimeAccom to set
	 */
	public void setExtendedTimeAccom(String extendedTimeAccom) {
		this.extendedTimeAccom = extendedTimeAccom;
	}
	
	// End changes for Student Pacing

	public boolean equals(Object other) {
        if(this.extElmId != null) {
            return this.extElmId.equals(((Student)other).extElmId);
        } else if(this.studentId != null) {
            return this.studentId.equals(((Student)other).studentId);
        } else return false;
    }
    
    public int hashCode() {
        if(this.extElmId != null) {
            return (int) Long.parseLong(this.extElmId.replaceAll("-", ""));
        } else if(this.studentId != null) {
            return this.studentId.intValue();
        } else return -1;
    }
    
	/**
	 * @return Returns the activationStatus.
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus The activationStatus to set.
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	/**
	 * @return Returns the activeSession.
	 */
	public String getActiveSession() {
		return activeSession;
	}
	/**
	 * @param activeSession The activeSession to set.
	 */
	public void setActiveSession(String activeSession) {
		this.activeSession = activeSession;
	}
	/**
	 * @return Returns the birthdate.
	 */
	public Date getBirthdate() {
		return birthdate;
	}
	/**
	 * @param birthdate The birthdate to set.
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
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
	/**
	 * @return Returns the createdDateTime.
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime The createdDateTime to set.
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	/**
	 * @return Returns the dataImportHistoryId.
	 */
	public Integer getDataImportHistoryId() {
		return dataImportHistoryId;
	}
	/**
	 * @param dataImportHistoryId The dataImportHistoryId to set.
	 */
	public void setDataImportHistoryId(Integer dataImportHistoryId) {
		this.dataImportHistoryId = dataImportHistoryId;
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
	 * @return Returns the ethnicity.
	 */
	public String getEthnicity() {
		return ethnicity;
	}
	/**
	 * @param ethnicity The ethnicity to set.
	 */
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
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
	 * @return Returns the extPin1.
	 */
	public String getExtPin1() {
		return extPin1;
	}
	/**
	 * @param extPin1 The extPin1 to set.
	 */
	public void setExtPin1(String extPin1) {
		this.extPin1 = extPin1;
	}
	/**
	 * @return Returns the extPin2.
	 */
	public String getExtPin2() {
		return extPin2;
	}
	/**
	 * @param extPin2 The extPin2 to set.
	 */
	public void setExtPin2(String extPin2) {
		this.extPin2 = extPin2;
	}
	/**
	 * @return Returns the extPin3.
	 */
	public String getExtPin3() {
		return extPin3;
	}
	/**
	 * @param extPin3 The extPin3 to set.
	 */
	public void setExtPin3(String extPin3) {
		this.extPin3 = extPin3;
	}
	/**
	 * @return Returns the extSchoolId.
	 */
	public String getExtSchoolId() {
		return extSchoolId;
	}
	/**
	 * @param extSchoolId The extSchoolId to set.
	 */
	public void setExtSchoolId(String extSchoolId) {
		this.extSchoolId = extSchoolId;
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
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName The middleName to set.
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the potentialDuplicatedStudent.
	 */
	public String getPotentialDuplicatedStudent() {
		return potentialDuplicatedStudent;
	}
	/**
	 * @param potentialDuplicatedStudent The potentialDuplicatedStudent to set.
	 */
	public void setPotentialDuplicatedStudent(String potentialDuplicatedStudent) {
		this.potentialDuplicatedStudent = potentialDuplicatedStudent;
	}
	/**
	 * @return Returns the preferredName.
	 */
	public String getPreferredName() {
		return preferredName;
	}
	/**
	 * @param preferredName The preferredName to set.
	 */
	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}
	/**
	 * @return Returns the prefix.
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix The prefix to set.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * @return Returns the studentId.
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId The studentId to set.
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return Returns the suffix.
	 */
	public String getSuffix() {
		return suffix;
	}
	/**
	 * @param suffix The suffix to set.
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	/**
	 * @return Returns the updatedBy.
	 */
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy The updatedBy to set.
	 */
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return Returns the updatedDateTime.
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime The updatedDateTime to set.
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the barcode
	 */
	public String getBarcode() {
		return barcode;
	}
	/**
	 * @param barcode the barcode to set
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the precodeId
	 */
	public Integer getPrecodeId() {
		return precodeId;
	}
	/**
	 * @param precodeId the precodeId to set
	 */
	public void setPrecodeId(Integer precodeId) {
		this.precodeId = precodeId;
	}
	/**
	 * @return the udf
	 */
	public String getUdf() {
		return udf;
	}
	/**
	 * @param udf the udf to set
	 */
	public void setUdf(String udf) {
		this.udf = udf;
	}
	/**
	 * @return the udf1
	 */
	public String getUdf1() {
		return udf1;
	}
	/**
	 * @param udf1 the udf1 to set
	 */
	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}
	/**
	 * @return the udf2
	 */
	public String getUdf2() {
		return udf2;
	}
	/**
	 * @param udf2 the udf2 to set
	 */
	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}

	/**
	 * @return the testPurpose
	 */
	public String getTestPurpose() {
		return testPurpose;
	}

	/**
	 * @param testPurpose the testPurpose to set
	 */
	public void setTestPurpose(String testPurpose) {
		this.testPurpose = testPurpose;
	}

	/**
	 * @return the outOfSchool
	 */
	public String getOutOfSchool() {
		return outOfSchool;
	}

	/**
	 * @param outOfSchool the outOfSchool to set
	 */
	public void setOutOfSchool(String outOfSchool) {
		this.outOfSchool = outOfSchool;
	}
} 
