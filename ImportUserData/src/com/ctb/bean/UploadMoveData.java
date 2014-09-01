package com.ctb.bean;

/**
 * Data bean representing the customer data needed between different methods
 * during Upload Process
 * 
 * @author TCS
 * 
 */
public class UploadMoveData {

	Integer uploadDataFileId = new Integer(0);
	int noOfUserColumn;
	OrgNodeCategory[] orgNodeCategory;
	UserFileRow[] userFileRowHeader;

	public OrgNodeCategory[] getOrgNodeCategory() {
		return orgNodeCategory;
	}

	public void setOrgNodeCategory(OrgNodeCategory[] orgNodeCategory) {
		this.orgNodeCategory = orgNodeCategory;
	}
	

	public UserFileRow[] getUserFileRowHeader() {
		return userFileRowHeader;
	}

	public void setUserFileRowHeader(UserFileRow[] userFileRowHeader) {
		this.userFileRowHeader = userFileRowHeader;
	}

	public Integer getUploadDataFileId() {
		return uploadDataFileId;
	}

	public void setUploadDataFileId(Integer uploadDataFileId) {
		this.uploadDataFileId = uploadDataFileId;
	}

	public int getNoOfUserColumn() {
		return noOfUserColumn;
	}

	public void setNoOfUserColumn(int noOfUserColumn) {
		this.noOfUserColumn = noOfUserColumn;
	}

}
