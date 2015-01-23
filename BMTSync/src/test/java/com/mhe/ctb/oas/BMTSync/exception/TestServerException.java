package com.mhe.ctb.oas.BMTSync.exception;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestServerException {

	@Test
	public void testServerException_empty() throws Exception {
		try {
			throw new ServerException();
		} catch (ServerException se) {
			if (se.getMessage() != null) {
				fail();
			}
		}
	}

	@Test
	public void testServerException_withMessage() throws Exception {
		try {
			throw new ServerException("Boom!");
		} catch (ServerException se) {
			if (se.getMessage() != "Boom!") {
				fail();
			}
		}
	}
	
	@Test
	public void testServerException_withThrowable() throws Exception {
		try {
			throw new ServerException(new Exception("Get burnt toast funky, rodents!"));
		} catch (ServerException se) {
			if (!se.getMessage().equals("java.lang.Exception: Get burnt toast funky, rodents!")) {
				fail();
			}
		}
	}

	@Test
	public void testServerException_withMessageAndThrowable() throws Exception {
		try {
			throw new ServerException("Boom!", new Exception());
		} catch (ServerException se) {
			if (se.getMessage() != "Boom!") {
				fail();
			}
		}
	}
}
