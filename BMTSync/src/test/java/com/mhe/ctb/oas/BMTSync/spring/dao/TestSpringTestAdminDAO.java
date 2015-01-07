package com.mhe.ctb.oas.BMTSync.spring.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.exception.UnknownTestAdminException;
import com.mhe.ctb.oas.BMTSync.model.TestAdmin;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestSpringTestAdminDAO {

	@Autowired
	private TestAdminDAO _adminDAO;
	
	//These tests assume the existence of a database and are rightfully integration tests.
	
	private static final int TEST_ADMIN_ID = 209881;

	@Test
	public void testAdminDAO_Integ_getTestAdmin_success() throws SQLException {
		TestAdmin admin = null;
		try {
			admin = _adminDAO.getTestAdmin(TEST_ADMIN_ID);
			assertEquals("Test Admin ID should match", Integer.valueOf(TEST_ADMIN_ID), admin.getOasTestAdministrationID());
		} catch (UnknownTestAdminException use) {
			fail();
		}
		assertNotNull(admin);
		assertNotNull(admin.getDeliveryWindow());
	}
	
	@Test(expected = UnknownTestAdminException.class)
	public void testAdminDAO_Integ_getTestAdmin_failUnknownTestAdminException()
			throws SQLException, UnknownTestAdminException {
		_adminDAO.getTestAdmin(-1);
	}
	
	
	@Test
	public void testAdminDAO_Integ_updateTestAdminStatus_success() throws SQLException, UnknownTestAdminException {
		_adminDAO.updateTestAdminStatus(TEST_ADMIN_ID, true, "", "Testing query.");
	}
}
