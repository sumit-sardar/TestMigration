package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * BroadcastMessage.java
 * @author Nate_Cohen
 *
 * Data bean representing the contents of the OAS.BROADCAST_MESSAGE_LOG table
 */
public class BroadcastMessage extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Date displayDateTime;
    private Date createdDateTime;
    private String message;
    private Integer priorityValue;
    private Integer createdBy;
    private Date startDate;
    private Date endDate;
    private String activationStatus;
    private Integer broadcastMessageLogId;
    private String application;
    private String messageType;
    
    /**
	 * @return Returns the activationStatus.
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus The activationStatus to set.
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	/**
	 * @return Returns the application.
	 */
	public String getApplication() {
		return application;
	}
	/**
	 * @param application The application to set.
	 */
	public void setApplication(String application) {
		this.application = application;
	}
	/**
	 * @return Returns the broadcastMessageLogId.
	 */
	public Integer getBroadcastMessageLogId() {
		return broadcastMessageLogId;
	}
	/**
	 * @param broadcastMessageLogId The broadcastMessageLogId to set.
	 */
	public void setBroadcastMessageLogId(Integer broadcastMessageLogId) {
		this.broadcastMessageLogId = broadcastMessageLogId;
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
	 * @return Returns the displayDateTime.
	 */
	public Date getDisplayDateTime() {
		return displayDateTime;
	}
	/**
	 * @param displayDateTime The displayDateTime to set.
	 */
	public void setDisplayDateTime(Date displayDateTime) {
		this.displayDateTime = displayDateTime;
	}
	/**
	 * @return Returns the endDate.
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return Returns the messageType.
	 */
	public String getMessageType() {
		return messageType;
	}
	/**
	 * @param messageType The messageType to set.
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	/**
	 * @return Returns the priorityValue.
	 */
	public Integer getPriorityValue() {
		return priorityValue;
	}
	/**
	 * @param priorityValue The priorityValue to set.
	 */
	public void setPriorityValue(Integer priorityValue) {
		this.priorityValue = priorityValue;
	}
	/**
	 * @return Returns the startDate.
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
} 
