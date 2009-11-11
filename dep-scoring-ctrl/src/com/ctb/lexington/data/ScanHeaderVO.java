package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: ScanHeaderVO</p>
 * <p>Description: holds metadata for a header group
 * (test session access code) within an upload</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Nate Cohen
 * @version 1.0
 */

public class ScanHeaderVO implements Serializable{

    public  static final String VO_LABEL       = "com.ctb.lexington.data.ScanHeaderVO";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    // statuses
    public static final String HEADER_STATUS_PENDING_VALIDATION = "PV";
    public static final String HEADER_STATUS_PENDING_MATCHING = "PM";
    public static final String HEADER_STATUS_ERROR_STUDENT = "ES";
    public static final String HEADER_STATUS_ERROR_ACCESS_CODE = "EA";
    public static final String HEADER_STATUS_HIDDEN = "HN";
    public static final String HEADER_STATUS_SUCCESS = "SS";

    public ScanHeaderVO() {
    }
    private Integer scanHeaderId;
    private Integer scanFileId;
    private Integer scanHeaderIndex;
    private String accessCode;
    private Integer testAdminId;
    private java.util.Date testAdminIdModDate;
    private Integer testAdminIdModUserId;
    private String scanHeaderStatus;
    private Integer studentCount;
    private String sessionName;
    private String test;
    private Date startDate;
    private Date endDate;
    private String scheduledBy;
    private String matchedAccessCode;
    
    public void setMatchedAccessCode(String matchedAccessCode_) {
        this.matchedAccessCode = matchedAccessCode_;
    }
    public String getMatchedAccessCode() {
        return matchedAccessCode;
    }
    public String getSessionName(){
    	return this.sessionName;
    }
    public String getTest(){
    	return this.test;
    }
    public Date getStartDate(){
    	return this.startDate;
    }
    public Date getEndDate(){
    	return this.endDate;
    }
    public String getScheduledBy(){
    	return this.scheduledBy;
    }
    public void setSessionName(String sessionName_){
    	this.sessionName = sessionName_;
    }
    public void setTest(String test_){
    	this.test = test_;
    }
    public void setStartDate(Date startDate_){
    	this.startDate = startDate_;
    }
    public void setEndDate(Date endDate_){
    	this.endDate = endDate_;
    }
    public void setScheduledBy(String scheduledBy_){
    	this.scheduledBy = scheduledBy_;
    }

    public void setStudentCount(int count) {
        this.studentCount = new Integer(count);
    }
    public Integer getStudentCount() {
        return this.studentCount;
    }
    public Integer getScanHeaderId() {
        return scanHeaderId;
    }
    public void setScanHeaderId(Integer scanHeaderId) {
        this.scanHeaderId = scanHeaderId;
    }
    public void setScanFileId(Integer scanFileId) {
        this.scanFileId = scanFileId;
    }
    public Integer getScanFileId() {
        return scanFileId;
    }
    public void setScanHeaderIndex(Integer scanHeaderIndex) {
        this.scanHeaderIndex = scanHeaderIndex;
    }
    public Integer getScanHeaderIndex() {
        return scanHeaderIndex;
    }
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    public String getAccessCode() {
        return accessCode;
    }
    public void setTestAdminId(Integer testAdminId) {
        this.testAdminId = testAdminId;
    }
    public Integer getTestAdminId() {
        return testAdminId;
    }
    public void setTestAdminIdModDate(java.util.Date testAdminIdModDate) {
        this.testAdminIdModDate = testAdminIdModDate;
    }
    public java.util.Date getTestAdminIdModDate() {
        return testAdminIdModDate;
    }
    public void setTestAdminIdModUserId(Integer testAdminIdModUserId) {
        this.testAdminIdModUserId = testAdminIdModUserId;
    }
    public Integer getTestAdminIdModUserId() {
        return testAdminIdModUserId;
    }
    public void setScanHeaderStatus(String scanHeaderStatus) {
        this.scanHeaderStatus = scanHeaderStatus;
    }
    public String getScanHeaderStatus() {
        return scanHeaderStatus;
    }
	public boolean equals(Object object){
		boolean isEqual = false;
		try{
			ScanHeaderVO sh = (ScanHeaderVO) object;
			if(((sh.accessCode == null && this.accessCode == null) ||
				 (sh.accessCode.equals(this.accessCode))) &&
				((sh.scanFileId == null && this.scanFileId == null) ||
				 (sh.scanFileId.equals(this.scanFileId))) &&
				((sh.scanHeaderId == null && this.scanHeaderId == null) ||
				 (sh.scanHeaderId.equals(this.scanHeaderId))) &&
				((sh.scanHeaderIndex == null && this.scanHeaderIndex == null) ||
				 (sh.scanHeaderIndex.equals(this.scanHeaderIndex))) &&
				((sh.scanHeaderStatus == null && this.scanHeaderStatus == null) ||
				 (sh.scanHeaderStatus.equals(this.scanHeaderStatus))) &&
				((sh.testAdminId == null && this.testAdminId == null) ||
				 (sh.testAdminId.equals(this.testAdminId))) &&
				((sh.testAdminIdModDate == null && this.testAdminIdModDate == null) ||
				 (sh.testAdminIdModDate.equals(this.testAdminIdModDate))) &&
				((sh.testAdminIdModUserId == null && this.testAdminIdModUserId == null) |
				 (sh.testAdminIdModUserId.equals(this.testAdminIdModUserId)))) {
				isEqual = true;
			}
		}
		catch (Exception e){
		}
		return isEqual;
	}
	/**
	 * 
	 */
	public void populateExtraHeaderInfo() {
	}
}