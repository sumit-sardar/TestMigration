package com.mhe.ctb.oas.BMTSync.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.model.Student;

/**
 * Request to BMT to synch students.
 * @author oas
 */
public class CreateStudentsRequest {
	// This isn't a property so there's no getter or setter.
	private static final Logger logger = Logger.getLogger(Student.class);
	
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
	
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("Failure to serialize Student object!");
			return null;
		}
	}
}
