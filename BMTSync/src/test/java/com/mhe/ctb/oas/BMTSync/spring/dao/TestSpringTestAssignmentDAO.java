package com.mhe.ctb.oas.BMTSync.spring.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.exception.UnknownTestAssignmentException;
import com.mhe.ctb.oas.BMTSync.model.StudentRoster;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestSpringTestAssignmentDAO {

	@Autowired
	private TestAssignmentDAO _assignmentDAO;
	
	//These tests assume the existence of a database and are rightfully integration tests.
	
	private static final int TEST_ADMIN_ID = 209881;
	private static final int STUDENT_ID = 15920241;

	@Test
	public void testAssignmentDAO_Integ_getTestAssignment_success() throws SQLException {
		TestAssignment assignment = null;
		try {
			assignment = _assignmentDAO.getTestAssignment(TEST_ADMIN_ID, STUDENT_ID);
			assertEquals("Test Admin ID should match", Integer.valueOf(TEST_ADMIN_ID), assignment.getOasTestAdministrationID());
		} catch (UnknownTestAssignmentException use) {
			fail();
		}
		assertNotNull(assignment);
		assertNotNull(assignment.getDeliveryWindow());
		assertNotNull(assignment.getParameters());
		assertNotNull(assignment.getRoster());
		List<StudentRoster> roster = assignment.getRoster();
		String studentId = roster.get(0).getOasStudentid();
		assertNotNull(studentId);
		assertEquals("Student ID should match", String.format("%d", STUDENT_ID), studentId);
	}
	
	@Test(expected = UnknownTestAssignmentException.class)
	public void testAssignmentDAO_Integ_getTestAssignment_failUnknownTestAssignmentException()
			throws SQLException, UnknownTestAssignmentException {
		_assignmentDAO.getTestAssignment(-1, -1);
	}
	
	
	@Test
	public void testAssignmentDAO_Integ_updateAssignmentAPIStatus_success() throws SQLException, UnknownTestAssignmentException {
		_assignmentDAO.updateAssignmentAPIStatus(TEST_ADMIN_ID, STUDENT_ID, true, "", "Testing query.");
	}
}
