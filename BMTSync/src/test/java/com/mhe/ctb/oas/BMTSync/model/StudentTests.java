package com.mhe.ctb.oas.BMTSync.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.TestUtils;

public class StudentTests {
	private static final long EPOCH = 1411516800000L;
	private static final Date TEST_DATE = new Date(EPOCH);
	
	private Student1 generateStudent()
	{
		String[] studentIds = {"ID1", "ID2"};
		
		Student1 student = new Student1();
		student.setBirthDate(TEST_DATE);
		student.setExtStudentId(studentIds);
		student.setFirstName("first name");
		student.setGender("male");
		student.setGrade("3");
		student.setLastName("last name");
		student.setLoginName("login");
		student.setMiddleName("middle name");
		student.setOasCustomerId(1234);
		student.setOasStudentId(456);

		return student;
	}
	
	private void validateStudent(Student1 student) throws Exception
	{
		assertNotNull(student.getBirthDate());
		assertEquals(TEST_DATE.getTime(), student.getBirthDate().getTime());

		String[] studentIds = student.getExtStudentId();
		assertNotNull(studentIds);
		assertEquals(2, studentIds.length);
		assertEquals(studentIds[0], "ID1");
		assertEquals(studentIds[1], "ID2");

		assertEquals("first name", student.getFirstName());
		assertEquals("male", student.getGender());
		assertEquals("3", student.getGrade());
		assertEquals("last name", student.getLastName());
		assertEquals("login", student.getLoginName());
		assertEquals("middle name", student.getMiddleName());
		assertEquals(new Integer(1234), student.getOasCustomerId());
		assertEquals(new Integer(456), student.getOasStudentId());
	}
	
	
	
	/**
	 * Tests the creation and validation logic of this test harness
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStudentPojo() throws Exception
	{
		Student1 student = generateStudent();
		validateStudent(student);
	}
	
	
	/**
	 * Tests if we can take a Student object and encode it back as JSON
	 * @throws Exception
	 */
	@Test
	public void testJSONEncoding() throws Exception
	{
		Student1 student = generateStudent();
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(student));
		
	}

	/**
	 * Tests if we can read a JSON string from a file and load it into a student object, then validates the object.
	 * @throws Exception
	 */
	@Test
	public void testJSONReader() throws Exception
	{
		String json = TestUtils.readFileFromClasspath(this.getClass(), "student.json");
		ObjectMapper mapper = new ObjectMapper();
		
		Student1 student = mapper.readValue(json, Student1.class);
		validateStudent(student);
		
		
	}
	
	/*
	// Useful method to help generate the student request JSON blob for testing
	@Test
	public void testCreateStudentRequest() throws Exception
	{
		List<Student> students= new LinkedList<Student>();
		students.add(generateStudent());
		students.add(generateStudent());
		
		CreateStudentsRequest request = new CreateStudentsRequest();
		request.setStudents(students);

		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(request));
	}
	*/
}
