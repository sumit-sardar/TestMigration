package com.ctb.util;

import java.io.Serializable;


public class OperationStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean isSuccess = false;
	private boolean IsSystemError = false;
	private String successMessage ;
	private ValidationFailedInfo validationFailedInfo;
	private SuccessInfo successInfo;
	private boolean isLicenseError = false;
	
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

	/**
	 * @return the isLicenseError
	 */
	public boolean isLicenseError() {
		return isLicenseError;
	}

	/**
	 * @param isLicenseError the isLicenseError to set
	 */
	public void setLicenseError(boolean isLicenseError) {
		this.isLicenseError = isLicenseError;
	}
	

}
