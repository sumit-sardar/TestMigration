package com.mhe.ctb.oas.BMTSync.rest;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mhe.ctb.oas.BMTSync.model.Student;

public class CreateStudentsRequest {
	private final List<Student> _students;

	public List<Student> getStudents() {
		return _students;
	}
	
	public CreateStudentsRequest() {
		_students = new ArrayList<Student>();
	}
	
	@JsonProperty(value="students", required=true)
	public void addStudents(final List<Student> students) {
		_students.addAll(students);
	}

	@JsonProperty(value="students", required=true)
	public void addStudent(final Student student) {
		_students.add(student);
	}
	
}
