package com.ctb.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.ffpojo.metadata.positional.annotation.PositionalField;
import org.ffpojo.metadata.positional.annotation.PositionalRecord;

@PositionalRecord
public class Student {

	private Integer studentId;
	private String lastName;
	private String firstName;
	private String middleName;
	private Date birthDate;
	private String ethnicity;
	private String gender;
	private String grade;
	private Integer customerId;
	private String testPurpose;
	private String extStudentId;

	private Set<StudentContact> studentContact = new HashSet<StudentContact>();
	private Set<StudentDemographic> studentDemographic = new HashSet<StudentDemographic>();
	private Set<TestRoster> studentRoster = new HashSet<TestRoster>();
	private Accomodations accomodations;

	/**
	 * @return the studentroster
	 */
	public Set<TestRoster> getStudentRoster() {
		return studentRoster;
	}

	/**
	 * @param studentroster
	 *            the studentroster to set
	 */
	public void setStudentRoster(Set<TestRoster> studentRoster) {
		this.studentRoster = studentRoster;
	}

	/**
	 * @return the studentId
	 */

	public Integer getStudentId() {
		return studentId;
	}

	/**
	 * @param studentId
	 *            the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	/**
	 * @return the firstName
	 */
	@PositionalField(initialPosition = 325, finalPosition = 339)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	@PositionalField(initialPosition = 340, finalPosition = 340)
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	@PositionalField(initialPosition = 305, finalPosition = 324)
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the birthDate
	 */
	@PositionalField(initialPosition = 341, finalPosition = 352)
	public Date getBirthDate() {

		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		/*
		 * if(birthDate!= null){ System.out.println("BirthDate" + birthDate);
		 * String DATE_FORMAT = "MMDDYY"; SimpleDateFormat sdf = new
		 * SimpleDateFormat(DATE_FORMAT); sdf.format(birthDate);
		 * System.out.println("BirthDate" + birthDate); }
		 */
		this.birthDate = birthDate;

	}

	/**
	 * @return the ethnicity
	 */
	public String getEthnicity() {
		return ethnicity;
	}

	/**
	 * @param ethnicity
	 *            the ethnicity to set
	 */
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	/**
	 * @return the gender
	 */
	@PositionalField(initialPosition = 353, finalPosition = 353)
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the grade
	 */
	@PositionalField(initialPosition = 77, finalPosition = 78)
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the testPurpose
	 */
	public String getTestPurpose() {
		return testPurpose;
	}

	/**
	 * @param testPurpose
	 *            the testPurpose to set
	 */
	public void setTestPurpose(String testPurpose) {
		this.testPurpose = testPurpose;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the studentContact
	 */
	@PositionalField(initialPosition = 79, finalPosition = 110)
	public Set<StudentContact> getStudentContact() {
		return studentContact;
	}

	/**
	 * @param studentContact
	 *            the studentContact to set
	 */
	public void setStudentContact(Set<StudentContact> studentContact) {
		this.studentContact = studentContact;
	}

	/**
	 * @return the studentDemographic
	 */

	public Set<StudentDemographic> getStudentDemographic() {
		return studentDemographic;
	}

	/**
	 * @param studentDemographic
	 *            the studentDemographic to set
	 */
	public void setStudentDemographic(Set<StudentDemographic> studentDemographic) {
		this.studentDemographic = studentDemographic;
	}

	/**
	 * @return the accomodations
	 */
	@PositionalField(initialPosition = 423, finalPosition = 506)
	public Accomodations getAccomodations() {
		return accomodations;
	}

	/**
	 * @param accomodations
	 *            the accomodations to set
	 */
	public void setAccomodations(Accomodations accomodations) {
		this.accomodations = accomodations;
	}

	/**
	 * @return the extStudentId
	 */
	public String getExtStudentId() {
		return extStudentId;
	}

	/**
	 * @param extStudentId
	 *            the extStudentId to set
	 */
	public void setExtStudentId(String extStudentId) {
		this.extStudentId = extStudentId;
	}

}
