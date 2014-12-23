package com.mhe.ctb.oas.BMTSync.spring.jms;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jms-qa.xml")
public class JMSDebugRunnerTests {

	
	private Logger LOGGER = Logger.getLogger(JMSDebugRunnerTests.class);

	@Test
	public void testOrgNodeDAO() throws Exception
	{
		Thread.sleep(10000);
		LOGGER.info("testOrgNodeDAO");
	}	
}
