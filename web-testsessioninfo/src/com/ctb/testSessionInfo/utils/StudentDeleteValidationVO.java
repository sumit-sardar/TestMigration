package com.ctb.testSessionInfo.utils;

import java.io.Serializable;

@SuppressWarnings("all")
public class StudentDeleteValidationVO implements Serializable {
	private boolean isDeletionValid = false;
	private String validationMsg;

	public boolean isDeletionValid() {
		return isDeletionValid;
	}

	public void setDeletionValid(boolean isDeletionValid) {
		this.isDeletionValid = isDeletionValid;
	}

	public String getValidationMsg() {
		return validationMsg;
	}

	public void setValidationMsg(String validationMsg) {
		this.validationMsg = validationMsg;
	}
}
