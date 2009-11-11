package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: ScanFileVO</p>
 * <p>Description: holds file-level metadata within an upload</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Nate Cohen
 * @version 1.0
 */

public class ScanFileVO implements Serializable{

    public  static final String VO_LABEL       = "com.ctb.lexington.data.ScanFileVO";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    public static final String FILE_STATUS_NOT_PROCESSED = "NP";
    public static final String FILE_STATUS_PENDING_VALIDATION = "PV";
    public static final String FILE_STATUS_PENDING_MATCHING = "PM";
    public static final String FILE_STATUS_ERROR_PARSING = "EP";
    public static final String FILE_STATUS_ERROR_HEADER = "EH";
    public static final String FILE_STATUS_HIDDEN = "HN";
    public static final String FILE_STATUS_SUCCESS = "SS";

    public ScanFileVO() {
    }
    private Integer scanFileId = null;
    private Integer scanUploadId = null;
    private String uploadFileName = null;
    private java.util.Date uploadDate = null;
    private Integer userId = null;
    private Integer recordCount = null;
    private Integer overwrittenTestAdminId = null;
    private String scanFileStatus = null;
    private String fileContent = null;
    private String detailedStatus = null;
    private String uploadBy = null;
    private String fileSize = null;
    private String orgNodeName = null;
    private String automatchCriteria = null;
    private boolean automatchOverwrite;
    private boolean deletable = true;

    public boolean getDeletable(){
    	return this.deletable;
    }
    public void setDeletable(boolean deletable_){
    	this.deletable = deletable_;
    }
    public boolean getAutomatchOverwrite(){
    	return this.automatchOverwrite;
    }
    public void setAutomatchOverwrite(boolean automatchOverwrite_){
    	this.automatchOverwrite = automatchOverwrite_;
    }
    public String getAutomatchCriteria(){
    	return this.automatchCriteria;
    }
    public void setAutomatchCriteria(String automatchCriteria_){
    	this.automatchCriteria = automatchCriteria_;
    }
    public String getOrgNodeName() {
        return orgNodeName;
    }
    public void setOrgNodeName(String orgNodeName_) {
        this.orgNodeName = orgNodeName_;
    }
    public String getFileSize() {
        return fileSize;
    }
    public void setFileSize(String fileSize_) {
        this.fileSize = fileSize_;
    }
    public Integer getScanFileId() {
        return scanFileId;
    }
    public void setScanFileId(Integer scanFileId) {
        this.scanFileId = scanFileId;
    }
    public void setScanUploadId(Integer scanUploadId) {
        this.scanUploadId = scanUploadId;
    }
    public Integer getScanUploadId() {
        return scanUploadId;
    }
    public void setDetailedStatus(String detailedStatus_) {
        this.detailedStatus = detailedStatus_;
    }
    public String getDetailedStatus() {
        return detailedStatus;
    }
    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }
    public String getUploadFileName() {
        return uploadFileName;
    }
    public void setUploadDate(java.util.Date uploadDate) {
        this.uploadDate = uploadDate;
    }
    public java.util.Date getUploadDate() {
        return uploadDate;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }
    public Integer getRecordCount() {
        return recordCount;
    }
    public void setOverwrittenTestAdminId(Integer overwrittenTestAdminId) {
        this.overwrittenTestAdminId = overwrittenTestAdminId;
    }
    public Integer getOverwrittenTestAdminId() {
        return overwrittenTestAdminId;
    }
    public void setScanFileStatus(String scanFileStatus) {
        this.scanFileStatus = scanFileStatus;
    }
    public String getScanFileStatus() {
        return scanFileStatus;
    }
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public String getFileContent() {
		return fileContent;
	}
	public String getUploadBy() {
		return uploadBy;
	}
	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}
	
	public boolean equals(Object object){
		boolean isEqual = false;
		try{
			ScanFileVO sf = (ScanFileVO) object;
			if(((sf.overwrittenTestAdminId == null && this.overwrittenTestAdminId == null) ||
				(sf.overwrittenTestAdminId.equals(this.overwrittenTestAdminId))) &&
			   ((sf.recordCount == null && this.recordCount == null) ||
				(sf.recordCount.equals(this.recordCount))) &&
			   ((sf.scanFileId == null && this.scanFileId == null) ||
				(sf.scanFileId.equals(this.scanFileId))) &&
			   ((sf.scanFileStatus == null && this.scanFileStatus == null) ||
				(sf.scanFileStatus.equals(this.scanFileStatus))) &&
			   ((sf.scanUploadId == null && this.scanUploadId == null) ||
				(sf.scanUploadId.equals(this.scanUploadId))) &&
			   ((sf.uploadDate == null && this.uploadDate == null) ||
				(sf.uploadDate.equals(this.uploadDate))) &&
			   ((sf.uploadFileName == null && this.uploadFileName == null) ||
				(sf.uploadFileName.equals(this.uploadFileName))) &&
			   ((sf.userId == null && this.userId == null) ||
				(sf.userId.equals(this.userId))) &&
			   ((sf.fileContent == null && this.fileContent == null) ||
			    (sf.fileContent.equals(this.fileContent)))) {
				isEqual = true;
			 }
		}
		catch(Exception e){
		}
		return isEqual;
	}
}