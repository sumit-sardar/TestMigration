package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.mhe.ctb.oas.BMTSync.controller.TestAdminRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAdminMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

/**
 * A worker to pull objects off of a queue periodically and send messages to BMT.
 * @author kristy_tracer
 *
 */
public class BMTTestAdminBlockingQueueWorker extends Thread {

	/** Logger. */
	private static final Logger logger = Logger.getLogger(BMTTestAdminBlockingQueueWorker.class);
	
	/** The queue. */
	private final BMTBlockingQueue<TestAdminMessageType> queue;
	
	/** The REST client. */
	private final TestAdminRestClient restClient;
	
	/** Whether the thread should run. */
	private boolean shouldRun;

	/**
	 * Constructor.
	 * @param queue
	 * @param restClient
	 */
	public BMTTestAdminBlockingQueueWorker(final BMTBlockingQueue<TestAdminMessageType> queue, final TestAdminRestClient restClient) {
		this.queue = queue;
		this.restClient = restClient;
		this.shouldRun = true;
	}
	
	/** Do the thing. */
	public void run() {
		while(shouldRun) {
			synchronized(this) {
				pollForWork();
			}
		}
	}

	/** Don't do the thing. */
	public void shouldStop() {
		shouldRun = false;
	}
	
	/** The thing: poll the queue for work, and if it finds something, send it to BMT. */
	/** package-private */ void pollForWork() { // Set for testing.
		try {
			logger.debug("Starting polling for test admin messages to post to BMT....");
			final List<TestAdminMessageType> messages = queue.dequeue();
		
			if (CollectionUtils.isEmpty(messages)) {
				logger.debug("No test admin messages to post to BMT.");
			} else {
				logger.debug("Posting " + Integer.valueOf(messages.size()).toString() + " test admin messages to BMT.");
				for (final TestAdminMessageType message : messages) {
					restClient.postTestAdmin(message.getTestAdminId());
				}
			}
		} catch (Exception e) {
			logger.fatal("Error-absorbing catcher: " + e.getMessage(), e);
		}

	}
}
