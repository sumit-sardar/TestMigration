package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.mhe.ctb.oas.BMTSync.controller.AssignmentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

public class BMTTestAssignmentBlockingQueueWorker extends Thread {

	private static final Logger logger = Logger.getLogger(BMTTestAssignmentBlockingQueueWorker.class);
	
	private final BMTBlockingQueue<TestAssignmentMessageType> queue;
	private final AssignmentRestClient restClient;
	private int batchSize;
	private boolean shouldRun;

	public BMTTestAssignmentBlockingQueueWorker(final BMTBlockingQueue<TestAssignmentMessageType> queue,
			final AssignmentRestClient restClient, final int batchSize) {
		this.queue = queue;
		this.restClient = restClient;
		this.batchSize = batchSize;
		this.shouldRun = true;
	}
	
	public void run() {
		while(shouldRun) {
			synchronized(this) {
				pollForWork();
			}
		}
	}

	public void shouldStop() {
		shouldRun = false;
	}
	
	/** package-private */ void pollForWork() { // set up for testing.
		try {
			logger.debug("Starting polling for test assignment messages to post to BMT....");
			final List<TestAssignmentMessageType> messages = queue.dequeue(batchSize);
		
			if (CollectionUtils.isEmpty(messages)) {
				logger.debug("No test assignment messages to post to BMT.");
			} else {
				logger.debug("Posting " + Integer.valueOf(messages.size()).toString() + " test assignment messages to BMT.");
				restClient.postStudentAssignment(messages);
			}
		} catch (Exception e) {
			logger.fatal("Error-absorbing catcher: " + e.getMessage(), e);
		}
	}
}
