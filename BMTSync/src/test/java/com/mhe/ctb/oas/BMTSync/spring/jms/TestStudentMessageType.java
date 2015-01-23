package com.mhe.ctb.oas.BMTSync.spring.jms;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestStudentMessageType {

	private StudentMessageType message;
	
	@Test
	public void testStudentMessageType_success() {
		message = new StudentMessageType();
		message.setCustomerId(1);
		message.setStudentId(1001);
		message.setUpdatedDateTime(Calendar.getInstance());
		assertEquals(Integer.valueOf(1), Integer.valueOf(message.getCustomerId()));
		assertEquals("studentId", message.getPrimaryKeyName());
		assertEquals("1001", message.getPrimaryKeyValue());
		assertEquals("customerId", message.getSecondaryKeyName());
		assertEquals("1", message.getSecondaryKeyValue());
		assertEquals("Student", message.getMessageType());
		assertEquals(" [studentId=1001,customerId=1]", message.getLogDetails());
	}
	
	@Test
	public void testStudentMessageType_nulls() {
		message = new StudentMessageType();
		assertEquals("studentId", message.getPrimaryKeyName());
		assertEquals(null, message.getPrimaryKeyValue());
		assertEquals("customerId", message.getSecondaryKeyName());
		assertEquals(null, message.getSecondaryKeyValue());
		assertEquals("Student", message.getMessageType());
		assertEquals(" [studentId=null,customerId=null]", message.getLogDetails());
	}}
