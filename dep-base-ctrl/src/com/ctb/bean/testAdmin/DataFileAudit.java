package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;
import java.sql.Blob;

public class DataFileAudit extends CTBBean
{ 
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
    
    public  Integer getUploadCount() {
			return uploadCount;
    }

    public  void setUploadCount(Integer uploadCount) {
        this.uploadCount = uploadCount;
    }
    
    public byte[] getFaildRec() {
		return faildRec;
	}

	public void setFaildRec(byte[] faildRec) {
		this.faildRec = faildRec;
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
	 * @return Returns the customerId.
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId The customerId to set.
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return Returns the dataFileAuditId.
	 */
	public Integer getDataFileAuditId() {
		return dataFileAuditId;
	}
	/**
	 * @param dataFileAuditId The dataFileAuditId to set.
	 */
	public void setDataFileAuditId(Integer dataFileAuditId) {
		this.dataFileAuditId = dataFileAuditId;
	}
	/**
	 * @return Returns the failedRecord.
	 */
	public Blob getFailedRecord() {
		return failedRecord;
	}
	/**
	 * @param failedRecord The failedRecord to set.
	 */
	public void setFailedRecord(Blob failedRecord) {
		this.failedRecord = failedRecord;
	}
	/**
	 * @return Returns the failedRecordCount.
	 */
	public Integer getFailedRecordCount() {
		return failedRecordCount;
	}
	/**
	 * @param failedRecordCount The failedRecordCount to set.
	 */
	public void setFailedRecordCount(Integer failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Returns the uploadFileName.
	 */
	public String getUploadFileName() {
		return uploadFileName;
	}
	/**
	 * @param uploadFileName The uploadFileName to set.
	 */
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	/**
	 * @return Returns the uploadFileRecordCount.
	 */
	public Integer getUploadFileRecordCount() {
		return uploadFileRecordCount;
	}
	/**
	 * @param uploadFileRecordCount The uploadFileRecordCount to set.
	 */
	public void setUploadFileRecordCount(Integer uploadFileRecordCount) {
		this.uploadFileRecordCount = uploadFileRecordCount;
	}
	/**
	 * @return Returns the userId.
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
    /**
	 * @return Returns the editable.
	 */
	public String getEditable() {
		return editable;
	}
	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(String editable) {
		this.editable = editable;
	}
} 
