package com.mhe.ctb.oas.BMTSync.spring.jms;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestTestAssignmentMessageType {

	private TestAssignmentMessageType message;
	
	@Test
	public void testTestAssignmentMessageType_success() {
		message = new TestAssignmentMessageType();
		message.setCustomerId(1);
		message.setTestAdminId(101);
		message.setTestRosterId(10001);
		message.setStudentId(1000001);
		message.setUpdatedDateTime(Calendar.getInstance());
		assertEquals(Integer.valueOf(1), Integer.valueOf(message.getCustomerId()));
		assertEquals("testAdminId", message.getPrimaryKeyName());
		assertEquals("101", message.getPrimaryKeyValue());
		assertEquals("studentId", message.getSecondaryKeyName());
		assertEquals("1000001", message.getSecondaryKeyValue());
		assertEquals("TestAssignment", message.getMessageType());
		assertEquals(" [testAdminId=101,studentId=1000001]", message.getLogDetails());
	}
	
	@Test
	public void testTestAssignmentMessageType_nulls() {
		message = new TestAssignmentMessageType();
		assertEquals("testAdminId", message.getPrimaryKeyName());
		assertEquals(null, message.getPrimaryKeyValue());
		assertEquals("studentId", message.getSecondaryKeyName());
		assertEquals(null, message.getSecondaryKeyValue());
		assertEquals("TestAssignment", message.getMessageType());
		assertEquals(" [testAdminId=null,studentId=null]", message.getLogDetails());
	}}
