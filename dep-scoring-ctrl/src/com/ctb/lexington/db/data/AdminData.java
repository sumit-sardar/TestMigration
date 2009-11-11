package com.ctb.lexington.db.data;

import java.sql.Date;
import java.sql.Timestamp;

public class AdminData{
	/*IrsCustomerDim
	  * */
	 private Long customerId;
	 private String customerName;
     private String customerKey;
	 
	 /*IrsAssessmentDim
	  * */
	 private String assessmentName;
	 private Long productId;
	 private Long assessmentId;
	 private String assessmentType;
	    
	 /*IrsProgramDim
	  * */
	 private Long programId;
	 //private Long customerId;
	 private String programName;
	 private Timestamp progStartDate;
	 private Timestamp progEndDate; 
     private String normsGroup;
     private String normsYear;
	 
	 /*IrsScehdulerDim
	  * */
	 private Long schedulerId;
	 private String schedulerName;
	 
	 /*IrsSessionDim
	  * */
	 private Long sessionId;
	 private Long numberOfStudents;
	 //private Long programId;
	 private Timestamp windowStartDate;
	 private Timestamp windowEndDate;
	 //private Long schedulerId;
     
     private String timeZone;
	 
	 /*IrsProductDim
	  * */
	// private Long productid;
	 private String productName;
	/**
	 * @return the assessmentId
	 */
	public Long getAssessmentId() {
		return assessmentId;
	}
	/**
	 * @param assessmentId the assessmentId to set
	 */
	public void setAssessmentId(Long assessmentId) {
		this.assessmentId = assessmentId;
	}
	/**
	 * @return the assessmentName
	 */
	public String getAssessmentName() {
		return assessmentName;
	}
	/**
	 * @param assessmentName the assessmentName to set
	 */
	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}
	/**
	 * @return the assessmentType
	 */
	public String getAssessmentType() {
		return assessmentType;
	}
	/**
	 * @param assessmentType the assessmentType to set
	 */
	public void setAssessmentType(String assessmentType) {
		this.assessmentType = assessmentType;
	}
	/**
	 * @return the customerId
	 */
	public Long getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
    /**
	 * @return the customerKey
	 */
	public String getCustomerKey() {
		return customerKey;
	}
	/**
	 * @param customerKey the customerKey to set
	 */
	public void setCustomerKey(String customerKey) {
		this.customerKey = customerKey;
	}
	/**
	 * @return the name
	 */
	public String getproductName() {
		return productName;
	}
	/**
	 * @param name the name to set
	 */
	public void setproductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the numberOfStudents
	 */
	public Long getNumberOfStudents() {
		return numberOfStudents;
	}
	/**
	 * @param numberOfStudents the numberOfStudents to set
	 */
	public void setNumberOfStudents(Long numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}
	
	/**
	 * @return the productId
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * @return the progEndDate
	 */
	public Timestamp getProgEndDate() {
		return progEndDate;
	}
	/**
	 * @param progEndDate the progEndDate to set
	 */
	public void setProgEndDate(Timestamp progEndDate) {
		this.progEndDate = progEndDate;
	}
	/**
	 * @return the programId
	 */
	public Long getProgramId() {
		return programId;
	}
	/**
	 * @param programId the programId to set
	 */
	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	/**
	 * @return the programName
	 */
	public String getProgramName() {
		return programName;
	}
	/**
	 * @param programName the programName to set
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}
    /**
	 * @return the normsGroup
	 */
	public String getNormsGroup() {
		return normsGroup;
	}
	/**
	 * @param normsGroup the normsGroup to set
	 */
	public void setNormsGroup(String normsGroup) {
		this.normsGroup = normsGroup;
	}
    /**
	 * @return the normsYear
	 */
	public String getNormsYear() {
		return normsYear;
	}
	/**
	 * @param normsYear the normsYear to set
	 */
	public void setNormsYear(String normsYear) {
		this.normsYear = normsYear;
	}
	/**
	 * @return the progStartDate
	 */
	public Timestamp getProgStartDate() {
		return progStartDate;
	}
	/**
	 * @param progStartDate the progStartDate to set
	 */
	public void setProgStartDate(Timestamp progStartDate) {
		this.progStartDate = progStartDate;
	}
	/**
	 * @return the schedulerId
	 */
	public Long getSchedulerId() {
		return schedulerId;
	}
	/**
	 * @param schedulerId the schedulerId to set
	 */
	public void setSchedulerId(Long schedulerId) {
		this.schedulerId = schedulerId;
	}
	/**
	 * @return the schedulerName
	 */
	public String getSchedulerName() {
		return schedulerName;
	}
	/**
	 * @param schedulerName the schedulerName to set
	 */
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
	/**
	 * @return the sessionId
	 */
	public Long getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * @return the windowEndDate
	 */
	public Timestamp getWindowEndDate() {
		return windowEndDate;
	}
	/**
	 * @param windowEndDate the windowEndDate to set
	 */
	public void setWindowEndDate(Timestamp windowEndDate) {
		this.windowEndDate = windowEndDate;
	}
	/**
	 * @return the windowStartDate
	 */
	public Timestamp getWindowStartDate() {
		return windowStartDate;
	}
	/**
	 * @param windowStartDate the windowStartDate to set
	 */
	public void setWindowStartDate(Timestamp windowStartDate) {
		this.windowStartDate = windowStartDate;
	}
    
    /**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
}

	
