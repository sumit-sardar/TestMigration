package com.ctb.util;



public class OperationStatus {
	private boolean isSuccess = false;
	private boolean IsSystemError = false;
	private String successMessage ;
	private ValidationFailedInfo validationFailedInfo;
	private SuccessInfo successInfo;
	
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

	
	/**
	 * @return the isSystemError
	 */
	public boolean isSystemError() {
		return IsSystemError;
	}

	
	/**
	 * @param isSystemError the isSystemError to set
	 */
	public void setSystemError(boolean isSystemError) {
		IsSystemError = isSystemError;
	}

	
	/**
	 * @return the successInfo
	 */
	public SuccessInfo getSuccessInfo() {
		return successInfo;
	}

	
	/**
	 * @param successInfo the successInfo to set
	 */
	public void setSuccessInfo(SuccessInfo successInfo) {
		this.successInfo = successInfo;
	}
	

}
