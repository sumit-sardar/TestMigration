package dto; 

import java.util.Date;
import utils.PermissionsUtils;

public class AuditFileHistory implements java.io.Serializable {
	
	
	static final long serialVersionUID = 1L;
	
	//private  Integer uploadFileRecordCount = new Integer(0);
    //private  Integer failedRecordCount = new Integer(0);
    private  String uploadFileRecordCount;
    private  String failedRecordCount;
    private  Integer userId;
    private  Integer dataFileAuditId;
    private  String uploadFileName;
    private  String status;
    private  String createdDateTime;
    private  Integer customerId;
    private  String actionPermission = PermissionsUtils.REFRESH_PERMISSION_TOKEN;
	/**
	 * @return Returns the createdDateTime.
	 */
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime The createdDateTime to set.
	 */
	public void setCreatedDateTime(String createdDateTime) {
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
	 * @return Returns the failedRecordCount.
	 */
	/*public Integer getFailedRecordCount() {
		return this.failedRecordCount != null ? this.failedRecordCount : new Integer(0);
	}*/
	public String getFailedRecordCount() {
		return this.failedRecordCount != null ? this.failedRecordCount : "0";
	}
	/**
	 * @param failedRecordCount The failedRecordCount to set.
	 */
/*	public void setFailedRecordCount(Integer failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}*/
	public void setFailedRecordCount(String failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		
		if ( this.status.equals("IN") ) {
			
			return "In Progress";
			
		} else if ( this.status.equals("FL") ) {
			
			return "Error";
			
		} else {
			
			return "Success";
		}
	}
	public String getStatusCode() {
		return this.status;
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
/*	public Integer getUploadFileRecordCount() {
		return this.uploadFileRecordCount != null 
                ? this.uploadFileRecordCount : new Integer(0);
	}*/
	public String getUploadFileRecordCount() {
		return this.uploadFileRecordCount != null
                ? this.uploadFileRecordCount : "0"; 
	}
	/**
	 * @param uploadFileRecordCount The uploadFileRecordCount to set.
	 */
	/*public void setUploadFileRecordCount(Integer uploadFileRecordCount) {
		this.uploadFileRecordCount = uploadFileRecordCount;
	}*/
	public void setUploadFileRecordCount(String uploadFileRecordCount) {
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
	 * @return Returns the actionPermission.
	 */
	public String getActionPermission() {
		return this.actionPermission != null ? 
            this.actionPermission : PermissionsUtils.REFRESH_PERMISSION_TOKEN;
	}
	/**
	 * @param actionPermission 
	 */
	public void setActionPermission(String actionPermission) {
		this.actionPermission = actionPermission;
	}
    
    
    
}
