package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.mhe.ctb.oas.BMTSync.controller.AssignmentRestClient;
import com.mhe.ctb.oas.BMTSync.controller.TestAdminRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAdminMessageType;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

public class BMTTestAdminBlockingQueuePoller implements DisposableBean, InitializingBean {
	
	private static final Logger logger = Logger.getLogger(BMTTestAdminBlockingQueuePoller.class);

	private BMTTestAdminBlockingQueueWorker worker;
	private BMTBlockingQueue<TestAdminMessageType> queue;
	private TestAdminRestClient restClient;
	
	@Autowired
	public BMTTestAdminBlockingQueuePoller(final BMTBlockingQueue<TestAdminMessageType> queue, final TestAdminRestClient restClient) {
		this.queue = queue;
		this.restClient = restClient;
	}

	@Override
	public void destroy() throws Exception {
		worker.shouldStop();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Creating poller to watch queue for TestAdmin messages to post to BMT.");
		worker = new BMTTestAdminBlockingQueueWorker(queue, restClient);
		worker.start();
	}

}
