package com.ctb.bean.dataExportManagement;

import com.ctb.bean.CTBBean;
import java.util.Date;
import java.util.List;

public class ManageJob extends CTBBean{
	
	static final long serialVersionUID = 1L;
    private Integer jobId;
    private Integer studentCount;
    private String createdDateTime;
    private String jobStatus;
    private String createdBy;
    private String lastUpdateTime;
    private String lastUpdateStatus;
    private String message;
	
	/**
	 * @return the jobId
	 */
	public Integer getJobId() {
		return jobId;
	}
	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	/**
	 * @return the studentCount
	 */
	public Integer getStudentCount() {
		return studentCount;
	}
	/**
	 * @param studentCount the studentCount to set
	 */
	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}
	/**
	 * @return the createdDateTime
	 */
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	
	/**
	 * @return the jobStatus
	 */
	public String getJobStatus() {
		return jobStatus;
	}
	/**
	 * @param jobStatus the jobStatus to set
	 */
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the lastUpdateTime
	 */
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	/**
	 * @param lastUpdateTime the lastUpdateTime to set
	 */
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	/**
	 * @return the lastUpdateStatus
	 */
	public String getLastUpdateStatus() {
		return lastUpdateStatus;
	}
	/**
	 * @param lastUpdateStatus the lastUpdateStatus to set
	 */
	public void setLastUpdateStatus(String lastUpdateStatus) {
		this.lastUpdateStatus = lastUpdateStatus;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}


}
