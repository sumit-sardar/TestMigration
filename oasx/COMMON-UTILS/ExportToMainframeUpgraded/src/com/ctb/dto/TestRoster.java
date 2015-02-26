package com.ctb.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ffpojo.metadata.positional.annotation.PositionalRecord;

@PositionalRecord
public class TestRoster implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer testRosterId ;
	private String activationStatus ;
	private String testCompletionStatus;
	private Student student;
	private Integer customerId;
	private Integer studentId;
	private Integer  testAdminId;
	private Integer productId; // Added for 2nd Edition
	private TestAdmin test_admin;
	private String subtestModel;
	private Set<StudentItemSetStatus> studentItemSetStatus = new HashSet<StudentItemSetStatus> ();
	private Set<Organization> orgnaizationSet = Collections.synchronizedSet( new HashSet<Organization>());
	private Map<String,Map<String,  List<RostersItem>>> allitemDetails = Collections.synchronizedMap(new HashMap<String, Map<String,List<RostersItem>>>());
	private Set<SkillArea> skillAreas = Collections.synchronizedSet(new HashSet<SkillArea>());
	private Set<SubSkillArea> subSkillAreas= Collections.synchronizedSet(new HashSet<SubSkillArea>());
	private  Set<SubSkillArea> pointsScores = Collections.synchronizedSet(new HashSet<SubSkillArea>());
	
	/**
	 * @return the studentItemSetStatus
	 */
	public Set<StudentItemSetStatus> getStudentItemSetStatus() {
		return studentItemSetStatus;
	}
	/**
	 * @param studentItemSetStatus the studentItemSetStatus to set
	 */
	public void setStudentItemSetStatus(
			Set<StudentItemSetStatus> studentItemSetStatus) {
		this.studentItemSetStatus = studentItemSetStatus;
	}
	/**
	 * @return the student
	 */
	public Student getStudent() {
		return student;
	}
	/**
	 * @param student the student to set
	 */
	public void setStudent(Student student) {
		this.student = student;
	}
	/**
	 * @return the testRosterId
	 */
	public Integer getTestRosterId() {
		return testRosterId;
	}
	/**
	 * @param testRosterId the testRosterId to set
	 */
	public void setTestRosterId(Integer testRosterId) {
		this.testRosterId = testRosterId;
	}
	
	/**
	 * @return the activationStatus
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus the activationStatus to set
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	/**
	 * @return the testCompletionStatus
	 */
	public String getTestCompletionStatus() {
		return testCompletionStatus;
	}
	/**
	 * @param testCompletionStatus the testCompletionStatus to set
	 */
	public void setTestCompletionStatus(String testCompletionStatus) {
		this.testCompletionStatus = testCompletionStatus;
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
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the testAdminId
	 */
	public Integer getTestAdminId() {
		return testAdminId;
	}
	/**
	 * @param testAdminId the testAdminId to set
	 */
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}
	/**
	 * @return the productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * @return the test_admin
	 */
	public TestAdmin getTest_admin() {
		return test_admin;
	}
	/**
	 * @param testAdmin the test_admin to set
	 */
	public void setTest_admin(TestAdmin testAdmin) {
		test_admin = testAdmin;
	}
	/**
	 * @return the orgnaizationSet
	 */
	public Set<Organization> getOrgnaizationSet() {
		return orgnaizationSet;
	}
	/**
	 * @param orgnaizationSet the orgnaizationSet to set
	 */
	public void setOrgnaizationSet(Set<Organization> orgnaizationSet) {
		this.orgnaizationSet = orgnaizationSet;
	}
	/**
	 * @return the allitemDetails
	 */
	public Map<String, Map<String, List<RostersItem>>> getAllitemDetails() {
		return allitemDetails;
	}
	/**
	 * @param allitemDetails the allitemDetails to set
	 */
	public void setAllitemDetails(
			Map<String,Map<String, List<RostersItem>>> allitemDetails) {
		this.allitemDetails = allitemDetails;
	}
	/**
	 * @return the skillAreas
	 */
	public Set<SkillArea> getSkillAreas() {
		return skillAreas;
	}
	/**
	 * @param skillAreas the skillAreas to set
	 */
	public void setSkillAreas(Set<SkillArea> skillAreas) {
		this.skillAreas = skillAreas;
	}
	/**
	 * @return the subSkillAreas
	 */
	public Set<SubSkillArea> getSubSkillAreas() {
		return subSkillAreas;
	}
	/**
	 * @param subSkillAreas the subSkillAreas to set
	 */
	public void setSubSkillAreas(Set<SubSkillArea> subSkillAreas) {
		this.subSkillAreas = subSkillAreas;
	}
	/**
	 * @return the pointsScores
	 */
	public Set<SubSkillArea> getPointsScores() {
		return pointsScores;
	}
	/**
	 * @param pointsScores the pointsScores to set
	 */
	public void setPointsScores(Set<SubSkillArea> pointsScores) {
		this.pointsScores = pointsScores;
	}
	/**
	 * @return the subtestModel
	 */
	public String getSubtestModel() {
		return subtestModel;
	}
	/**
	 * @param subtestModel the subtestModel to set
	 */
	public void setSubtestModel(String subtestModel) {
		this.subtestModel = subtestModel;
	}
	
}
