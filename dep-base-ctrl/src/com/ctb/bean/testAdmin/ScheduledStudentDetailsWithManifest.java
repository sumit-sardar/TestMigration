package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

public class ScheduledStudentDetailsWithManifest extends CTBBean {
	static final long serialVersionUID = 1L;

	private TestSession testSession;
	private StudentManifest[] studentManifests;
	private TestElement[] allSchedulableUnit ;

	public TestSession getTestSession() {
		return testSession;
	}

	public void setTestSession(TestSession testSession) {
		this.testSession = testSession;
	}

	public StudentManifest[] getStudentManifests() {
		return studentManifests;
	}

	public void setStudentManifests(StudentManifest[] studentManifests) {
		this.studentManifests = studentManifests;
	}

	public TestElement[] getAllSchedulableUnit() {
		return allSchedulableUnit;
	}

	public void setAllSchedulableUnit(TestElement[] allSchedulableUnit) {
		this.allSchedulableUnit = allSchedulableUnit;
	}

	

}
