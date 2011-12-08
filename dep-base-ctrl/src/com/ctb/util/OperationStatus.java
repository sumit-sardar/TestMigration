package com.ctb.util;



public class OperationStatus {
	private boolean isSuccess ;
	private String successMessage ;
	private ValidationFailedInfo validationFailedInfo;
	
	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return isSuccess;
	}
	
	/**
	 * @param isSuccess the isSuccess to set
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	/**
	 * @return the successMessage
	 */
	public String getSuccessMessage() {
		return successMessage;
	}
	
	/**
	 * @param successMessage the successMessage to set
	 */
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	
	/**
	 * @return the validation
	 */
	public ValidationFailedInfo getValidationFailedInfo() {
		return validationFailedInfo;
	}
	
	/**
	 * @param validation the validation to set
	 */
	public void setValidationFailedInfo(ValidationFailedInfo validation) {
		this.validationFailedInfo = validation;
	}
	

}
