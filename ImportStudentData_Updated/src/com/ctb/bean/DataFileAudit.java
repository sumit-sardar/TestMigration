package com.ctb.bean;

import java.sql.Blob;
import java.util.Date;

public class DataFileAudit extends CTBBean{
	static final long serialVersionUID = 1L;
    private  Integer uploadFileRecordCount = new Integer(0);
    private  Integer failedRecordCount = new Integer(0);
    private  Integer userId;
    private  Integer dataFileAuditId;
    private  String uploadFileName;
    private  String status;
    private  Date createdDateTime;
    private  Blob failedRecord;
    private  Integer customerId;
    private  Integer createdBy;
    private byte[] faildRec;
    private String editable;    
    private  Integer uploadCount;
	/**
	 * @return the uploadFileRecordCount
	 */
	public Integer getUploadFileRecordCount() {
		return uploadFileRecordCount;
	}
	/**
	 * @param uploadFileRecordCount the uploadFileRecordCount to set
	 */
	public void setUploadFileRecordCount(Integer uploadFileRecordCount) {
		this.uploadFileRecordCount = uploadFileRecordCount;
	}
	/**
	 * @return the failedRecordCount
	 */
	public Integer getFailedRecordCount() {
		return failedRecordCount;
	}
	/**
	 * @param failedRecordCount the failedRecordCount to set
	 */
	public void setFailedRecordCount(Integer failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * @return the dataFileAuditId
	 */
	public Integer getDataFileAuditId() {
		return dataFileAuditId;
	}
	/**
	 * @param dataFileAuditId the dataFileAuditId to set
	 */
	public void setDataFileAuditId(Integer dataFileAuditId) {
		this.dataFileAuditId = dataFileAuditId;
	}
	/**
	 * @return the uploadFileName
	 */
	public String getUploadFileName() {
		return uploadFileName;
	}
	/**
	 * @param uploadFileName the uploadFileName to set
	 */
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the createdDateTime
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	/**
	 * @return the failedRecord
	 */
	public Blob getFailedRecord() {
		return failedRecord;
	}
	/**
	 * @param failedRecord the failedRecord to set
	 */
	public void setFailedRecord(Blob failedRecord) {
		this.failedRecord = failedRecord;
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
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the faildRec
	 */
	public byte[] getFaildRec() {
		return faildRec;
	}
	/**
	 * @param faildRec the faildRec to set
	 */
	public void setFaildRec(byte[] faildRec) {
		this.faildRec = faildRec;
	}
	/**
	 * @return the editable
	 */
	public String getEditable() {
		return editable;
	}
	/**
	 * @param editable the editable to set
	 */
	public void setEditable(String editable) {
		this.editable = editable;
	}
	/**
	 * @return the uploadCount
	 */
	public Integer getUploadCount() {
		return uploadCount;
	}
	/**
	 * @param uploadCount the uploadCount to set
	 */
	public void setUploadCount(Integer uploadCount) {
		this.uploadCount = uploadCount;
	}

    
}
