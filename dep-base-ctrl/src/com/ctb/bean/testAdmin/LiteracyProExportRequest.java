package com.ctb.bean.testAdmin;

import java.sql.Blob;

import com.ctb.bean.CTBBean;

public class LiteracyProExportRequest extends CTBBean {

    static final long serialVersionUID = 1L;
    private Long exportRequestId;
    private Integer userID;
    private Integer customerId;
    private String exportDate;
    private String fileName;
    Integer orgNodeName;
    private String status;
    private Blob fileContent;
    private String message;

    public LiteracyProExportRequest() {
	super();
    }

    public LiteracyProExportRequest(Integer userID, Integer customerId,
	    String exportDate, Integer orgNodeName, String status,
	    String message) {
	super();
	this.userID = userID;
	this.customerId = customerId;
	this.exportDate = exportDate;
	this.orgNodeName = orgNodeName;
	this.status = status;
	this.message = message;
    }

    /**
     * @return the exportRequestId
     */
    public Long getExportRequestId() {
	return exportRequestId;
    }

    /**
     * @param exportRequestId
     *                the exportRequestId to set
     */
    public void setExportRequestId(Long exportRequestId) {
	this.exportRequestId = exportRequestId;
    }

    /**
     * @return the userID
     */
    public Integer getUserID() {
	return userID;
    }

    /**
     * @param userID
     *                the userID to set
     */
    public void setUserID(Integer userID) {
	this.userID = userID;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
	return customerId;
    }

    /**
     * @param customerId
     *                the customerId to set
     */
    public void setCustomerId(Integer customerId) {
	this.customerId = customerId;
    }

    /**
     * @return the exportDate
     */
    public String getExportDate() {
	return exportDate;
    }

    /**
     * @param exportDate
     *                the exportDate to set
     */
    public void setExportDate(String exportDate) {
	this.exportDate = exportDate;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
	return fileName;
    }

    /**
     * @param fileName
     *                the fileName to set
     */
    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public Integer getOrgNodeName() {
	return orgNodeName;
    }

    public void setOrgNodeName(Integer orgNodeName) {
	this.orgNodeName = orgNodeName;
    }

    /**
     * @return the status
     */
    public String getStatus() {
	return status;
    }

    /**
     * @param status
     *                the status to set
     */
    public void setStatus(String status) {
	this.status = status;
    }

    /**
     * @return the fileContent
     */
    public Blob getFileContent() {
	return fileContent;
    }

    /**
     * @param fileContent
     *                the fileContent to set
     */
    public void setFileContent(Blob fileContent) {
	this.fileContent = fileContent;
    }

    /**
     * @return the message
     */
    public String getMessage() {
	return message;
    }

    /**
     * @param message
     *                the message to set
     */
    public void setMessage(String message) {
	this.message = message;
    }

    public String toString() {
        return exportDate + " - " + fileName + " - " + status;
    }
}
