package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

public class BMTStudentBlockingQueueWorker extends Thread {

	private static final Logger logger = Logger.getLogger(BMTStudentBlockingQueueWorker.class);

	private final BMTBlockingQueue<StudentMessageType> queue;
	private final StudentRestClient restClient;
	private boolean shouldRun;

	public BMTStudentBlockingQueueWorker(final BMTBlockingQueue<StudentMessageType> queue, final StudentRestClient restClient) {
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
	
	/* package-private */ void pollForWork() { // Set so it can be tested.
		try {
			logger.debug("Starting polling for student messages to post to BMT....");
			final List<StudentMessageType> messages = queue.dequeue();
		
			if (CollectionUtils.isEmpty(messages)) {
				logger.debug("No student messages to post to BMT.");
			} else {
				logger.debug("Posting " + Integer.valueOf(messages.size()).toString() + " student messages to BMT.");
				restClient.postStudentList(messages);
			}
		} catch (Exception e) {
			logger.fatal("Error-absorbing catcher: " + e.getMessage(), e);
		}
	}
}
