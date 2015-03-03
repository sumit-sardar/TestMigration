package com.mhe.ctb.oas.BMTSync.spring.poller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.controller.TestAdminRestClient;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsResponse;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestAdminResponse;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAdminMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestBMTTestAdminBlockingQueueWorker {
	
	private BMTTestAdminBlockingQueueWorker worker;
	@SuppressWarnings("unchecked")
	private final BMTBlockingQueue<TestAdminMessageType> queue = mock(BMTBlockingQueue.class);
	private final TestAdminRestClient restClient = mock(TestAdminRestClient.class);
	
	@Before
	public void setUp() {
		worker = new BMTTestAdminBlockingQueueWorker(queue, restClient);
	}
	
	@Test
	public void testBMTTestAdminBlockingQueueWorker_successWithMessage() throws Exception {
		final TestAdminMessageType message = new TestAdminMessageType();
		message.setCustomerId(1);
		message.setTestAdminId(1001);
		final List<TestAdminMessageType> messageList = new ArrayList<TestAdminMessageType>();
		messageList.add(message);
		final CreateTestAdminResponse response = new CreateTestAdminResponse();
		response.setSuccessCount(1);
		response.setFailureCount(0);
		when(queue.dequeue()).thenReturn(messageList);
		when(restClient.postTestAdmin(message.getTestAdminId())).thenReturn(response);
		worker.pollForWork();
	}
	
	@Test
	public void testBMTTestAdminBlockingQueueWorker_successWithoutMessage() throws Exception {
		final List<TestAdminMessageType> messageList = new ArrayList<TestAdminMessageType>();
		final CreateStudentsResponse response = new CreateStudentsResponse();
		response.setSuccessCount(0);
		response.setFailureCount(0);
		response.setFailures(null);
		when(queue.dequeue()).thenReturn(messageList);
		worker.pollForWork();
		verify(restClient, times(0)).postTestAdmin(anyInt());
	}
	
	@Test
	public void testBMTTestAdminBlockingQueueWorker_successShouldStop() throws Exception {
		worker.shouldStop();
	}

	@Test
	public void testBMTTestAdminBlockingQueueWorker_failOtherException() throws Exception {
		when(queue.dequeue()).thenThrow(new RuntimeException("Boo!"));
		worker.pollForWork();
		verify(restClient, times(0)).postTestAdmin(anyInt());
	}
}
