package com.ctb.lexington.db.data;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ctb.lexington.db.record.Persistent;

public class StudentTestHistoryData implements Persistent {

    private Long studentTestHistoryId;
    private Long testRosterId;
    private Long customerId;
    private Long testAdminId;
    private String testCompletionStatus;
    private Long studentId;
    private Timestamp startDateTime;
    private Timestamp completionDateTime;
    private String testName;
    private String testAdminName;
    private Long testCatalogId;
    private Long testItemSetId;
    private Long productId;
    private String productName;
    private String productType;
    private String testValidationStatus;
    private Long testValidationUpdatedBy;
    private Timestamp testValidationUpdatedDate;
    private String testValidationUpdatedNote;
    private String lexingtonVersion;
    private Long tutorialId;
    private String userName;
    private String lastName;
    private String firstName;
    private String middleName;
    private String prefix;
    private String suffix;
    private Timestamp birthdate;
    private String gender;
    private String ethnicity;
    private String email;
    private String grade;
    private Long age;
    private String extPin1;
    private String extPin2;
    private String extPin3;
    private String extSchoolId;
    private String extElmId;
    private String customerName;
    private Timestamp createdDateTime;
    private String scoringStatus;
    private Long creatorOrgNodeId;
    private Long parentOrgNodeId;
    private Long grandparentOrgNodeId;
    private String creatorOrgNodeName;
    private String parentOrgNodeName;
    private String grandparentOrgNodeName;
    private String contactName;
    private String contactType;
    private String contactEmail;
    private String streetLine1;
    private String streetLine2;
    private String streetLine3;
    private String city;
    private String statepr;
    private String country;
    private String zipcode;
    private String primaryPhone;
    private String secondaryPhone;
    private String fax;
    private Long schedulerUserId;
    private String schedulerUserName;
    private String atsArchive;
    private Long studentOrgNodeId;
    private String captureMethod;
    private Long atsStudentOrgNodeId;

    /**
     * @return Returns the age.
     */
    public Long getAge() {
        return age;
    }

    /**
     * @param age The age to set.
     */
    public void setAge(Long age) {
        this.age = age;
    }

    /**
     * @return Returns the atsArchive.
     */
    public String getAtsArchive() {
        return atsArchive;
    }

    /**
     * @param atsArchive The atsArchive to set.
     */
    public void setAtsArchive(String atsArchive) {
        this.atsArchive = atsArchive;
    }

    /**
     * @return Returns the atsStudentOrgNodeId.
     */
    public Long getAtsStudentOrgNodeId() {
        return atsStudentOrgNodeId;
    }

    /**
     * @param atsStudentOrgNodeId The atsStudentOrgNodeId to set.
     */
    public void setAtsStudentOrgNodeId(Long atsStudentOrgNodeId) {
        this.atsStudentOrgNodeId = atsStudentOrgNodeId;
    }

    /**
     * @return Returns the birthdate.
     */
    public Timestamp getBirthdate() {
        return birthdate;
    }

    /**
     * @param birthdate The birthdate to set.
     */
    public void setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * @return Returns the captureMethod.
     */
    public String getCaptureMethod() {
        return captureMethod;
    }

    /**
     * @param captureMethod The captureMethod to set.
     */
    public void setCaptureMethod(String captureMethod) {
        this.captureMethod = captureMethod;
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
     * @return Returns the completionDateTime.
     */
    public Timestamp getCompletionDateTime() {
        return completionDateTime;
    }

    /**
     * @param completionDateTime The completionDateTime to set.
     */
    public void setCompletionDateTime(Timestamp completionDateTime) {
        this.completionDateTime = completionDateTime;
    }

    /**
     * @return Returns the contactEmail.
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @param contactEmail The contactEmail to set.
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * @return Returns the contactName.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName The contactName to set.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return Returns the contactType.
     */
    public String getContactType() {
        return contactType;
    }

    /**
     * @param contactType The contactType to set.
     */
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    /**
     * @return Returns the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country The country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return Returns the createdDateTime.
     */
    public Timestamp getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * @param createdDateTime The createdDateTime to set.
     */
    public void setCreatedDateTime(Timestamp createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /**
     * @return Returns the creatorOrgNodeId.
     */
    public Long getCreatorOrgNodeId() {
        return creatorOrgNodeId;
    }

    /**
     * @param creatorOrgNodeId The creatorOrgNodeId to set.
     */
    public void setCreatorOrgNodeId(Long creatorOrgNodeId) {
        this.creatorOrgNodeId = creatorOrgNodeId;
    }

    /**
     * @return Returns the creatorOrgNodeName.
     */
    public String getCreatorOrgNodeName() {
        return creatorOrgNodeName;
    }

    /**
     * @param creatorOrgNodeName The creatorOrgNodeName to set.
     */
    public void setCreatorOrgNodeName(String creatorOrgNodeName) {
        this.creatorOrgNodeName = creatorOrgNodeName;
    }

    /**
     * @return Returns the customerId.
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId The customerId to set.
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return Returns the customerName.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName The customerName to set.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
     * @return Returns the fax.
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax The fax to set.
     */
    public void setFax(String fax) {
        this.fax = fax;
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
     * @return Returns the grandparentOrgNodeId.
     */
    public Long getGrandparentOrgNodeId() {
        return grandparentOrgNodeId;
    }

    /**
     * @param grandparentOrgNodeId The grandparentOrgNodeId to set.
     */
    public void setGrandparentOrgNodeId(Long grandparentOrgNodeId) {
        this.grandparentOrgNodeId = grandparentOrgNodeId;
    }

    /**
     * @return Returns the grandparentOrgNodeName.
     */
    public String getGrandparentOrgNodeName() {
        return grandparentOrgNodeName;
    }

    /**
     * @param grandparentOrgNodeName The grandparentOrgNodeName to set.
     */
    public void setGrandparentOrgNodeName(String grandparentOrgNodeName) {
        this.grandparentOrgNodeName = grandparentOrgNodeName;
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
     * @return Returns the lexingtonVersion.
     */
    public String getLexingtonVersion() {
        return lexingtonVersion;
    }

    /**
     * @param lexingtonVersion The lexingtonVersion to set.
     */
    public void setLexingtonVersion(String lexingtonVersion) {
        this.lexingtonVersion = lexingtonVersion;
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
     * @return Returns the parentOrgNodeId.
     */
    public Long getParentOrgNodeId() {
        return parentOrgNodeId;
    }

    /**
     * @param parentOrgNodeId The parentOrgNodeId to set.
     */
    public void setParentOrgNodeId(Long parentOrgNodeId) {
        this.parentOrgNodeId = parentOrgNodeId;
    }

    /**
     * @return Returns the parentOrgNodeName.
     */
    public String getParentOrgNodeName() {
        return parentOrgNodeName;
    }

    /**
     * @param parentOrgNodeName The parentOrgNodeName to set.
     */
    public void setParentOrgNodeName(String parentOrgNodeName) {
        this.parentOrgNodeName = parentOrgNodeName;
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
     * @return Returns the primaryPhone.
     */
    public String getPrimaryPhone() {
        return primaryPhone;
    }

    /**
     * @param primaryPhone The primaryPhone to set.
     */
    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    /**
     * @return Returns the productId.
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId The productId to set.
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return Returns the productName.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName The productName to set.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * @return Returns the schedulerUserId.
     */
    public Long getSchedulerUserId() {
        return schedulerUserId;
    }

    /**
     * @param schedulerUserId The schedulerUserId to set.
     */
    public void setSchedulerUserId(Long schedulerUserId) {
        this.schedulerUserId = schedulerUserId;
    }

    /**
     * @return Returns the schedulerUserName.
     */
    public String getSchedulerUserName() {
        return schedulerUserName;
    }

    /**
     * @param schedulerUserName The schedulerUserName to set.
     */
    public void setSchedulerUserName(String schedulerUserName) {
        this.schedulerUserName = schedulerUserName;
    }

    /**
     * @return Returns the scoringStatus.
     */
    public String getScoringStatus() {
        return scoringStatus;
    }

    /**
     * @param scoringStatus The scoringStatus to set.
     */
    public void setScoringStatus(String scoringStatus) {
        this.scoringStatus = scoringStatus;
    }

    /**
     * @return Returns the secondaryPhone.
     */
    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    /**
     * @param secondaryPhone The secondaryPhone to set.
     */
    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

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

    /**
     * @return Returns the statepr.
     */
    public String getStatepr() {
        return statepr;
    }

    /**
     * @param statepr The statepr to set.
     */
    public void setStatepr(String statepr) {
        this.statepr = statepr;
    }

    /**
     * @return Returns the streetLine1.
     */
    public String getStreetLine1() {
        return streetLine1;
    }

    /**
     * @param streetLine1 The streetLine1 to set.
     */
    public void setStreetLine1(String streetLine1) {
        this.streetLine1 = streetLine1;
    }

    /**
     * @return Returns the streetLine2.
     */
    public String getStreetLine2() {
        return streetLine2;
    }

    /**
     * @param streetLine2 The streetLine2 to set.
     */
    public void setStreetLine2(String streetLine2) {
        this.streetLine2 = streetLine2;
    }

    /**
     * @return Returns the streetLine3.
     */
    public String getStreetLine3() {
        return streetLine3;
    }

    /**
     * @param streetLine3 The streetLine3 to set.
     */
    public void setStreetLine3(String streetLine3) {
        this.streetLine3 = streetLine3;
    }

    /**
     * @return Returns the studentId.
     */
    public Long getStudentId() {
        return studentId;
    }

    /**
     * @param studentId The studentId to set.
     */
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    /**
     * @return Returns the studentOrgNodeId.
     */
    public Long getStudentOrgNodeId() {
        return studentOrgNodeId;
    }

    /**
     * @param studentOrgNodeId The studentOrgNodeId to set.
     */
    public void setStudentOrgNodeId(Long studentOrgNodeId) {
        this.studentOrgNodeId = studentOrgNodeId;
    }

    /**
     * @return Returns the studentTestHistoryId.
     */
    public Long getStudentTestHistoryId() {
        return studentTestHistoryId;
    }

    /**
     * @param studentTestHistoryId The studentTestHistoryId to set.
     */
    public void setStudentTestHistoryId(Long studentTestHistoryId) {
        this.studentTestHistoryId = studentTestHistoryId;
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
     * @return Returns the testAdminId.
     */
    public Long getTestAdminId() {
        return testAdminId;
    }

    /**
     * @param testAdminId The testAdminId to set.
     */
    public void setTestAdminId(Long testAdminId) {
        this.testAdminId = testAdminId;
    }

    /**
     * @return Returns the testAdminName.
     */
    public String getTestAdminName() {
        return testAdminName;
    }

    /**
     * @param testAdminName The testAdminName to set.
     */
    public void setTestAdminName(String testAdminName) {
        this.testAdminName = testAdminName;
    }

    /**
     * @return Returns the testCatalogId.
     */
    public Long getTestCatalogId() {
        return testCatalogId;
    }

    /**
     * @param testCatalogId The testCatalogId to set.
     */
    public void setTestCatalogId(Long testCatalogId) {
        this.testCatalogId = testCatalogId;
    }

    /**
     * @return Returns the testCompletionStatus.
     */
    public String getTestCompletionStatus() {
        return testCompletionStatus;
    }

    /**
     * @param testCompletionStatus The testCompletionStatus to set.
     */
    public void setTestCompletionStatus(String testCompletionStatus) {
        this.testCompletionStatus = testCompletionStatus;
    }

    /**
     * @return Returns the testItemSetId.
     */
    public Long getTestItemSetId() {
        return testItemSetId;
    }

    /**
     * @param testItemSetId The testItemSetId to set.
     */
    public void setTestItemSetId(Long testItemSetId) {
        this.testItemSetId = testItemSetId;
    }

    /**
     * @return Returns the testName.
     */
    public String getTestName() {
        return testName;
    }

    /**
     * @param testName The testName to set.
     */
    public void setTestName(String testName) {
        this.testName = testName;
    }

    /**
     * @return Returns the testRosterId.
     */
    public Long getTestRosterId() {
        return testRosterId;
    }

    /**
     * @param testRosterId The testRosterId to set.
     */
    public void setTestRosterId(Long testRosterId) {
        this.testRosterId = testRosterId;
    }

    /**
     * @return Returns the testValidationStatus.
     */
    public String getTestValidationStatus() {
        return testValidationStatus;
    }

    /**
     * @param testValidationStatus The testValidationStatus to set.
     */
    public void setTestValidationStatus(String testValidationStatus) {
        this.testValidationStatus = testValidationStatus;
    }

    /**
     * @return Returns the testValidationUpdatedBy.
     */
    public Long getTestValidationUpdatedBy() {
        return testValidationUpdatedBy;
    }

    /**
     * @param testValidationUpdatedBy The testValidationUpdatedBy to set.
     */
    public void setTestValidationUpdatedBy(Long testValidationUpdatedBy) {
        this.testValidationUpdatedBy = testValidationUpdatedBy;
    }

    /**
     * @return Returns the testValidationUpdatedDate.
     */
    public Timestamp getTestValidationUpdatedDate() {
        return testValidationUpdatedDate;
    }

    /**
     * @param testValidationUpdatedDate The testValidationUpdatedDate to set.
     */
    public void setTestValidationUpdatedDate(Timestamp testValidationUpdatedDate) {
        this.testValidationUpdatedDate = testValidationUpdatedDate;
    }

    /**
     * @return Returns the testValidationUpdatedNote.
     */
    public String getTestValidationUpdatedNote() {
        return testValidationUpdatedNote;
    }

    /**
     * @param testValidationUpdatedNote The testValidationUpdatedNote to set.
     */
    public void setTestValidationUpdatedNote(String testValidationUpdatedNote) {
        this.testValidationUpdatedNote = testValidationUpdatedNote;
    }

    /**
     * @return Returns the tutorialId.
     */
    public Long getTutorialId() {
        return tutorialId;
    }

    /**
     * @param tutorialId The tutorialId to set.
     */
    public void setTutorialId(Long tutorialId) {
        this.tutorialId = tutorialId;
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
     * @return Returns the zipcode.
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * @param zipcode The zipcode to set.
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}