package com.ctb.testSessionInfo.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class StudentDeleteValidationVO implements Serializable {
	private boolean isDeletionValid = false;
	private String validationMsg;
	private String undeletedStdUsrName="";
	private List<String> undeletedStdIds=new ArrayList<String>();
	
	public String getUndeletedStdUsrName() {
		return undeletedStdUsrName;
	}

	public void setUndeletedStdUsrName(String undeletedStdUsrName) {
		this.undeletedStdUsrName = undeletedStdUsrName;
	}

	public List<String> getUndeletedStdIds() {
		return undeletedStdIds;
	}

	public void setUndeletedStdIds(List<String> undeletedStdIds) {
		this.undeletedStdIds = undeletedStdIds;
	}

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
