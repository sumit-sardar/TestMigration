package com.ctb.bean;

/**
 * Data bean representing the custome data needed between different methods
 * during Upload Process
 * 
 * @author TCS
 * 
 */
public class UploadMoveData {

	boolean isStudentIdUnique;
	Integer uploadDataFileId = new Integer(0);
	int noOfUserColumn;
	OrgNodeCategory[] orgNodeCategory;
	StudentFileRow[] studentFileRowHeader;
	String[] valueForStudentId;
	String[] valueForStudentId2;

	public OrgNodeCategory[] getOrgNodeCategory() {
		return orgNodeCategory;
	}

	public void setOrgNodeCategory(OrgNodeCategory[] orgNodeCategory) {
		this.orgNodeCategory = orgNodeCategory;
	}

	public StudentFileRow[] getStudentFileRowHeader() {
		return studentFileRowHeader;
	}

	public void setStudentFileRowHeader(StudentFileRow[] studentFileRowHeader) {
		this.studentFileRowHeader = studentFileRowHeader;
	}

	public String[] getValueForStudentId() {
		return valueForStudentId;
	}

	public void setValueForStudentId(String[] valueForStudentId) {
		this.valueForStudentId = valueForStudentId;
	}

	public String[] getValueForStudentId2() {
		return valueForStudentId2;
	}

	public void setValueForStudentId2(String[] valueForStudentId2) {
		this.valueForStudentId2 = valueForStudentId2;
	}

	public boolean isStudentIdUnique() {
		return isStudentIdUnique;
	}

	public void setStudentIdUnique(boolean isStudentIdUnique) {
		this.isStudentIdUnique = isStudentIdUnique;
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
