package com.mhe.ctb.oas.BMTSync.spring.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.exception.UnknownTestStatusException;
import com.mhe.ctb.oas.BMTSync.model.TestStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestSpringTestStatusDAO {
	
	@Autowired
	private TestStatusDAO _testStatusDAO;
	
	private static final int ROSTER_ID = 8662811;
	private static final String OAS_TEST_ID = "ISTEP_MC_G8_F3_SP14_SU1_DU1";
	private static final String DELIVERY_STATUS = "IP";
	private static final String STARTED_DATE = "04-17-2006 4:10:00 PM";
	private static final String COMPLETED_DATE = "04-17-2006 4:46:54 PM";
	
	@Test
	public void testStatusDAO_integ_validateSaveData_success() throws SQLException {
		TestStatus testStatus = null;
		try {
			testStatus = _testStatusDAO.validateSaveData(ROSTER_ID, 
					             OAS_TEST_ID, 
					             DELIVERY_STATUS, 
					             STARTED_DATE, 
					             COMPLETED_DATE);
			
			assertEquals("Test Roster ID should match", ROSTER_ID, testStatus.getOasRosterId());
		} catch (UnknownTestStatusException use) {
			fail();
		}		
	}
	
	

	
}
