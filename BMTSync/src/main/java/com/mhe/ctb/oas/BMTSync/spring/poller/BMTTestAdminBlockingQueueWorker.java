package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.mhe.ctb.oas.BMTSync.controller.TestAdminRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAdminMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

public class BMTTestAdminBlockingQueueWorker extends Thread {

	private static final Logger logger = Logger.getLogger(BMTTestAdminBlockingQueueWorker.class);
	
	private final BMTBlockingQueue<TestAdminMessageType> queue;
	private final TestAdminRestClient restClient;
	private boolean shouldRun;

	public BMTTestAdminBlockingQueueWorker(final BMTBlockingQueue<TestAdminMessageType> queue, final TestAdminRestClient restClient) {
		this.queue = queue;
		this.restClient = restClient;
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
		} catch (InterruptedException ie) {
			logger.warn("Intentionally interrupted. Shutting down thread. ", ie);
			shouldRun = false;
			return;
		} catch (Exception e) {
			logger.fatal("Error-absorbing catcher: " + e.getMessage(), e);
		}

	}
}
