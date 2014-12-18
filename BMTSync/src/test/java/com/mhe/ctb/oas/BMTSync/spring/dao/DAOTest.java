package com.mhe.ctb.oas.BMTSync.spring.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.exception.UnknownStudentException;
import com.mhe.ctb.oas.BMTSync.model.Student;
import com.mhe.ctb.oas.BMTSync.model.Student.Accomodations;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class DAOTest {

	@Autowired
	private SpringStudentDAO _studentDao;
	
	private Logger logger = Logger.getLogger(DAOTest.class);
	
	private static final int STUDENT_ID = 413595;

	@Test
	public void testStudentDAO_getStudent_success() throws SQLException {
		Student student = null;
		try {
			student = _studentDao.getStudent(STUDENT_ID);
			assertEquals("Student ID should match", Integer.valueOf(STUDENT_ID), student.getOasStudentId());
		} catch (UnknownStudentException use) {
			fail();
		}
		
		assertNotNull(student);
		Accomodations accommodations = student.getAccomodations();
		assertNotNull(accommodations);
	}
	
	@Test(expected = UnknownStudentException.class)
	public void testStudentDAO_getStudent_failUnknownStudentException() throws SQLException, UnknownStudentException {
		_studentDao.getStudent(-1);
	}
	
	
	@Test
	public void testStudentDAO_updateStudentAPIStatus() throws SQLException {
		_studentDao.updateStudentAPIStatus(STUDENT_ID, true, "Testing query.");
	}
}
