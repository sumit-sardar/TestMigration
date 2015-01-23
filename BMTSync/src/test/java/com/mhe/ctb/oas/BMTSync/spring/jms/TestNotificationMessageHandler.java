package com.mhe.ctb.oas.BMTSync.spring.jms;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Calendar;

import javax.jms.JMSException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestNotificationMessageHandler {

	private NotificationMessageHandler<StudentMessageType> handler;
	
	private BMTBlockingQueue<StudentMessageType> queue;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		queue = mock(BMTBlockingQueue.class);
		handler = new NotificationMessageHandler<StudentMessageType>(queue);
	}
	
	@Test
	public void testNotificationMessageHandler_success() throws Exception {
		StudentMessageType message = new StudentMessageType();
		message.setCustomerId(1);
		message.setStudentId(1001);
		message.setUpdatedDateTime(Calendar.getInstance());

		handler.handleMessage(message);
		verify(queue, times(1)).enqueueWithTimeout(message);
	}
	
	@Test(expected = JMSException.class)
	public void testTestAdminMessageType_failNullMessage() throws Exception {
		handler.handleMessage(null);
	}
}
