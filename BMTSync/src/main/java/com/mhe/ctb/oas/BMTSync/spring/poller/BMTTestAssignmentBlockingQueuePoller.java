package com.mhe.ctb.oas.BMTSync.spring.poller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.mhe.ctb.oas.BMTSync.controller.AssignmentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

public class BMTTestAssignmentBlockingQueuePoller implements DisposableBean, InitializingBean {

	private static final Logger logger = Logger.getLogger(BMTTestAssignmentBlockingQueuePoller.class);
	
	private BMTTestAssignmentBlockingQueueWorker worker;
	private BMTBlockingQueue<TestAssignmentMessageType> queue;
	private AssignmentRestClient restClient;
	
	@Autowired
	public BMTTestAssignmentBlockingQueuePoller(final BMTBlockingQueue<TestAssignmentMessageType> queue, final AssignmentRestClient restClient) {
		this.queue = queue;
		this.restClient = restClient;
	}

	@Override
	public void destroy() throws Exception {
		worker.shouldStop();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Creating poller to watch queue for Student messages to post to BMT.");
		worker = new BMTTestAssignmentBlockingQueueWorker(queue, restClient);
		worker.start();
	}
}
