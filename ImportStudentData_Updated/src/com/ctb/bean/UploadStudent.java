package com.ctb.bean;

public class UploadStudent extends CTBBean{
	 static final long serialVersionUID = 1L;
	private ManageStudent manageStudent;
	private StudentAccommodations studentAccommodations;
	private StudentDemoGraphics[] studentDemographic;
	
	
	
	public UploadStudent(ManageStudent manageStudent,
			StudentAccommodations studentAccommodations,
			StudentDemoGraphics[] studentDemographic) {
		super();
		this.manageStudent = manageStudent;
		this.studentAccommodations = studentAccommodations;
		this.studentDemographic = studentDemographic;
		
		
	}


	public ManageStudent getManageStudent() {
		return manageStudent;
	}


	public StudentAccommodations getStudentAccommodations() {
		return studentAccommodations;
	}


	public StudentDemoGraphics[] getStudentDemographic() {
		return studentDemographic;
	}


}
