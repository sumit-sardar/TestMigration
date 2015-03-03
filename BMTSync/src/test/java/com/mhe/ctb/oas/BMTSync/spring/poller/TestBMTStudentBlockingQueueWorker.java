package com.mhe.ctb.oas.BMTSync.spring.poller;

import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsResponse;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestBMTStudentBlockingQueueWorker {
	
	private BMTStudentBlockingQueueWorker worker;
	@SuppressWarnings("unchecked")
	private final BMTBlockingQueue<StudentMessageType> queue = mock(BMTBlockingQueue.class);
	private final StudentRestClient restClient = mock(StudentRestClient.class);
	
	@Before
	public void setUp() {
		worker = new BMTStudentBlockingQueueWorker(queue, restClient);
	}
	
	@Test
	public void testBMTStudentBlockingQueueWorker_successWithMessage() throws Exception {
		final StudentMessageType message = new StudentMessageType();
		message.setCustomerId(1);
		message.setStudentId(1001);
		message.setUpdatedDateTime(Calendar.getInstance());
		final List<StudentMessageType> messageList = new ArrayList<StudentMessageType>();
		messageList.add(message);
		final CreateStudentsResponse response = new CreateStudentsResponse();
		response.setSuccessCount(1);
		response.setFailureCount(0);
		response.setFailures(null);
		when(queue.dequeue()).thenReturn(messageList);
		when(restClient.postStudentList(messageList)).thenReturn(response);
		worker.pollForWork();
	}
	
	@Test
	public void testBMTStudentBlockingQueueWorker_successWithoutMessage() throws Exception {
		final List<StudentMessageType> messageList = new ArrayList<StudentMessageType>();
		final CreateStudentsResponse response = new CreateStudentsResponse();
		response.setSuccessCount(0);
		response.setFailureCount(0);
		response.setFailures(null);
		when(queue.dequeue()).thenReturn(messageList);
		when(restClient.postStudentList(messageList)).thenReturn(response);
		worker.pollForWork();
	}
	
	@Test
	public void testBMTStudentBlockingQueueWorker_successShouldStop() throws Exception {
		worker.shouldStop();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testBMTStudentBlockingQueueWorker_failOtherException() throws Exception {
		when(queue.dequeue()).thenThrow(new RuntimeException("Boo!"));
		worker.pollForWork();
		verify(restClient, times(0)).postStudentList(anyList());
	}
}
