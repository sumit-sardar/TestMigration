package com.mhe.ctb.oas.BMTSync.spring.jms;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestTestAdminMessageType {

	private TestAdminMessageType message;
	
	@Test
	public void testTestAdminMessageType_success() {
		message = new TestAdminMessageType();
		message.setCustomerId(1);
		message.setTestAdminId(1001);
		assertEquals(Integer.valueOf(1), Integer.valueOf(message.getCustomerId()));
		assertEquals("testAdminId", message.getPrimaryKeyName());
		assertEquals("1001", message.getPrimaryKeyValue());
		assertEquals("customerId", message.getSecondaryKeyName());
		assertEquals("1", message.getSecondaryKeyValue());
		assertEquals("TestAdmin", message.getMessageType());
		assertEquals(" [testAdminId=1001,customerId=1]", message.getLogDetails());
	}
	
	@Test
	public void testTestAdminMessageType_nulls() {
		message = new TestAdminMessageType();
		assertEquals("testAdminId", message.getPrimaryKeyName());
		assertEquals(null, message.getPrimaryKeyValue());
		assertEquals("customerId", message.getSecondaryKeyName());
		assertEquals(null, message.getSecondaryKeyValue());
		assertEquals("TestAdmin", message.getMessageType());
		assertEquals(" [testAdminId=null,customerId=null]", message.getLogDetails());
	}}
