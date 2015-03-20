package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.mhe.ctb.oas.BMTSync.controller.AssignmentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

/**
 * Polling thread to read from a queue periodically and send its contents to BMT.
 * @author kristy_tracer
 */
public class BMTTestAssignmentBlockingQueueWorker extends Thread {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(BMTTestAssignmentBlockingQueueWorker.class);
	
	/** The queue. */
	private final BMTBlockingQueue<TestAssignmentMessageType> queue;
	
	/** The REST client. */
	private final AssignmentRestClient restClient;
	
	/** The size of the batch to read and send at once. */
	private int batchSize;
	
	/** Whether the thread should be running or not. */
	private boolean shouldRun;

	/**
	 * Constructor.
	 * @param queue
	 * @param restClient
	 * @param batchSize
	 */
	public BMTTestAssignmentBlockingQueueWorker(final BMTBlockingQueue<TestAssignmentMessageType> queue,
			final AssignmentRestClient restClient, final int batchSize) {
		this.queue = queue;
		this.restClient = restClient;
		this.batchSize = batchSize;
		this.shouldRun = true;
	}
	
	/**
	 * Do the thing.
	 */
	public void run() {
		while(shouldRun) {
			synchronized(this) {
				pollForWork();
			}
		}
	}

	/**
	 * Stop doing the thing.
	 */
	public void shouldStop() {
		shouldRun = false;
	}
	
	/**
	 * The thing to be done: try to dequeue messages, and if that succeeds, send those messages to BMT.
	 */
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
