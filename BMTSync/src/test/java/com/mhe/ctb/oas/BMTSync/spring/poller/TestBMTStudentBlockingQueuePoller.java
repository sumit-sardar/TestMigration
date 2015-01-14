package com.mhe.ctb.oas.BMTSync.spring.poller;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestBMTStudentBlockingQueuePoller {
	
	private BMTStudentBlockingQueuePoller poller;
	@SuppressWarnings("unchecked")
	private final BMTBlockingQueue<StudentMessageType> queue = mock(BMTBlockingQueue.class);
	private final StudentRestClient restClient = mock(StudentRestClient.class);
	
	@Test
	public void testBMTStudentBlockingQueuePoller_success() throws Exception {
		poller = new BMTStudentBlockingQueuePoller(queue, restClient);
		poller.afterPropertiesSet();
		poller.destroy();
	}
}
