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

import com.mhe.ctb.oas.BMTSync.controller.AssignmentRestClient;
import com.mhe.ctb.oas.BMTSync.rest.CreateAssignmentResponse;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestBMTTestAssignmentBlockingQueueWorker {
	
	private BMTTestAssignmentBlockingQueueWorker worker;
	@SuppressWarnings("unchecked")
	private final BMTBlockingQueue<TestAssignmentMessageType> queue = mock(BMTBlockingQueue.class);
	private final AssignmentRestClient restClient = mock(AssignmentRestClient.class);
	
	@Before
	public void setUp() {
		worker = new BMTTestAssignmentBlockingQueueWorker(queue, restClient);
	}
	
	@Test
	public void testBMTTestAssignmentBlockingQueueWorker_successWithMessage() throws Exception {
		final TestAssignmentMessageType message = new TestAssignmentMessageType();
		message.setCustomerId(1);
		message.setTestAdminId(1001);
		message.setStudentId(10001);
		final List<TestAssignmentMessageType> messageList = new ArrayList<TestAssignmentMessageType>();
		messageList.add(message);
		final CreateAssignmentResponse response = new CreateAssignmentResponse();
		response.setSuccessCount(1);
		response.setFailureCount(0);
		when(queue.dequeue()).thenReturn(messageList);
		when(restClient.postStudentAssignment(message.getTestAdminId(), message.getStudentId())).thenReturn(response);
		worker.pollForWork();
	}
	
	@Test
	public void testBMTTestAssignmentBlockingQueueWorker_successWithoutMessage() throws Exception {
		final List<TestAssignmentMessageType> messageList = new ArrayList<TestAssignmentMessageType>();
		when(queue.dequeue()).thenReturn(messageList);
		worker.pollForWork();
		verify(restClient, times(0)).postStudentAssignment(anyInt(), anyInt());
	}
	
	@Test
	public void testBMTTestAssignmentBlockingQueueWorker_successShouldStop() throws Exception {
		worker.shouldStop();
	}
	
	@Test
	public void testBMTTestAssignmentBlockingQueueWorker_failInterrupted() throws Exception {
		when(queue.dequeue()).thenThrow(new InterruptedException("Interruption!"));
		worker.pollForWork();
		verify(restClient, times(0)).postStudentAssignment(anyInt(), anyInt());
	}
	
	@Test
	public void testBMTTestAssignmentBlockingQueueWorker_failOtherException() throws Exception {
		when(queue.dequeue()).thenThrow(new RuntimeException("Boo!"));
		worker.pollForWork();
		verify(restClient, times(0)).postStudentAssignment(anyInt(), anyInt());
	}
}
