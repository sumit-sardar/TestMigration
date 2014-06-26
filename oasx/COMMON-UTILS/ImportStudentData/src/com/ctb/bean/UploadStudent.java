package com.ctb.bean;

public class UploadStudent {

	private ManageStudent manageStudent;
	private StudentAccommodations studentAccommodations;
	private StudentDemoGraphics[] studentDemographic;
	private Student student;
//	private boolean isNewStudent = false;
	
	
	public UploadStudent(ManageStudent manageStudent,
			StudentAccommodations studentAccommodations,
			StudentDemoGraphics[] studentDemographic, Student student) {
		super();
		this.manageStudent = manageStudent;
		this.studentAccommodations = studentAccommodations;
		this.studentDemographic = studentDemographic;
		this.student = student;
		
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


	public Student getStudent() {
		return student;
	}


//	public boolean isNewStudent() {
//		return isNewStudent;
//	}
//	
	
}
