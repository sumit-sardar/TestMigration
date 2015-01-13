package com.mhe.ctb.oas.BMTSync.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestBMTBlockingQueue {
	
	private BMTBlockingQueue<StudentMessageType> queue;
	
	private static final int QUEUE_SIZE = 10;
	private static final long TIMEOUT = 5;
	
	@Before
	public void setUp() {
		queue = new BMTBlockingQueue<StudentMessageType>(QUEUE_SIZE, TIMEOUT);
	}
	
	@Test
	public void testBMTBlockingQueue_success() throws InterruptedException {
		int first = 1;
		int second = 2;
		queue.enqueueWithTimeout(createMessage(first));
		queue.enqueueWithTimeout(createMessage(second));
		List<StudentMessageType> messageList = queue.dequeue();
		assertEquals("Should be two messages in the queue.", Integer.valueOf(2), Integer.valueOf(messageList.size()));
		assertMessageMatches(messageList.get(0), first);
		assertMessageMatches(messageList.get(1), second);
		
		messageList = queue.dequeue();
		assertEquals("Queue should be empty", Integer.valueOf(0), Integer.valueOf(messageList.size()));
	}

	@Test(expected = MessageConversionException.class)
	public void testBMTBlockingQueue_failFullQueue() throws InterruptedException {
		for (int index = 0; index <= QUEUE_SIZE; index++) {
			queue.enqueueWithTimeout(createMessage(index));
		}
	}
	
	private StudentMessageType createMessage(final int index) {
		final StudentMessageType message = new StudentMessageType();
		message.setCustomerId(index);
		message.setStudentId(100000+index);
		message.setUpdatedDateTime(Calendar.getInstance());
		return message;
	}
	
	private void assertMessageMatches(final StudentMessageType message, int index) {
		assertEquals(message.getCustomerId(), Integer.valueOf(index));
		assertEquals(message.getStudentId(), Integer.valueOf(100000+index));
		assertNotNull(message.getUpdatedDateTime());
	}
	
}
