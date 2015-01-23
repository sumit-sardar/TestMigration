package com.mhe.ctb.oas.BMTSync.exception;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestUnknownTestStatusException {

	@Test
	public void testUnknownTestStatusException_withMessage() throws Exception {
		try {
			throw new UnknownTestStatusException(1);
		} catch (UnknownTestStatusException se) {
			if (!se.getMessage().equals("Unknown Test Status Exception. [Roster Id=1]")) {
				fail();
			}
		}
	}
}
