package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.mhe.ctb.oas.BMTSync.controller.AssignmentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

public class BMTTestAssignmentBlockingQueueWorker implements Runnable {

	private final BMTBlockingQueue<TestAssignmentMessageType> queue;
	private final AssignmentRestClient restClient;
	private static final Logger logger = Logger.getLogger(BMTTestAssignmentBlockingQueueWorker.class);

	public BMTTestAssignmentBlockingQueueWorker(final BMTBlockingQueue<TestAssignmentMessageType> queue,
			final AssignmentRestClient restClient) {
		this.queue = queue;
		this.restClient = restClient;
	}
	
	public void run() {
		logger.info("Starting polling for test assignment messages to post to BMT....");
		final List<TestAssignmentMessageType> messages = queue.dequeue();
		
		if (CollectionUtils.isEmpty(messages)) {
			logger.info("No test assignment messages to post to BMT.");
			// Nothing to do here. Go away.
			return;
		}
		
		logger.info("Posting " + Integer.valueOf(messages.size()).toString() + " test assignment messages to BMT.");
		for (final TestAssignmentMessageType message : messages) {
			restClient.postStudentAssignment(message.getTestAdminId(), message.getStudentId());
		}
	}

}
