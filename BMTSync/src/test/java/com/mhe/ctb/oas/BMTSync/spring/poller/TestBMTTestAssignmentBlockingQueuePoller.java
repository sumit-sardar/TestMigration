package com.mhe.ctb.oas.BMTSync.spring.poller;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.controller.AssignmentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestBMTTestAssignmentBlockingQueuePoller {
	
	private BMTTestAssignmentBlockingQueuePoller poller;
	@SuppressWarnings("unchecked")
	private final BMTBlockingQueue<TestAssignmentMessageType> queue = mock(BMTBlockingQueue.class);
	private final AssignmentRestClient restClient = mock(AssignmentRestClient.class);
	
	@Test
	public void testBMTTestAssignmentBlockingQueuePoller_success() throws Exception {
		poller = new BMTTestAssignmentBlockingQueuePoller(queue, restClient);
		poller.afterPropertiesSet();
		poller.destroy();
	}
}
