package com.mhe.ctb.oas.BMTSync.spring.poller;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.controller.TestAdminRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAdminMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestBMTTestAdminBlockingQueuePoller {
	
	private BMTTestAdminBlockingQueuePoller poller;
	@SuppressWarnings("unchecked")
	private final BMTBlockingQueue<TestAdminMessageType> queue = mock(BMTBlockingQueue.class);
	private final TestAdminRestClient restClient = mock(TestAdminRestClient.class);
	
	@Test
	public void testBMTTestAssignmentBlockingQueuePoller_success() throws Exception {
		poller = new BMTTestAdminBlockingQueuePoller(queue, restClient);
		poller.afterPropertiesSet();
		poller.destroy();
	}
}
