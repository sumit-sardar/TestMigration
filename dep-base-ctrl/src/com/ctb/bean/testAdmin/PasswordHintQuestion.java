package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;


/**
 * Data bean representing the partial contents of the OAS.statepr_code table 
 *  
 * @author Tata Consultency Services
 */

public class PasswordHintQuestion extends CTBBean { 
    private String passwordHintQuestionId;
    private String passwordHintQuestion;
    private Long createdBy;
	private Date createdDateTime;
	private Long updatedBy;
	private Date updatedDateTime;
	private String activationStatus;
    
    /**
	* @return Returns the statePr.
	*/
    public String getPasswordHintQuestionId() {
		return this.passwordHintQuestionId;
	}
    /**
	 * @param statePr The statePr to set.
	 */
	public void setPasswordHintQuestionId(String passwordHintQuestionId) {
		this.passwordHintQuestionId = passwordHintQuestionId;
	}
    /**
	* @return Returns the statePrDesc.
	*/
    public String getPasswordHintQuestion() {
		return this.passwordHintQuestion;
	}
    /**
	 * @param statePrDesc The statePrDesc to set.
	 */
	public void setPasswordHintQuestion(String passwordHintQuestion) {
		this.passwordHintQuestion = passwordHintQuestion;
	}
    
    /**
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
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
	 * @return the updatedBy
	 */
	public Long getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return the updatedDateTime
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime the updatedDateTime to set
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
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
} 
