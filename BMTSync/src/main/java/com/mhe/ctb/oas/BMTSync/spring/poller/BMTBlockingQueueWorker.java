package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

public class BMTBlockingQueueWorker implements Runnable {

	private final BMTBlockingQueue queue;
	private final StudentRestClient restClient;
	private static final Logger logger = Logger.getLogger(BMTBlockingQueueWorker.class);

	public BMTBlockingQueueWorker(final BMTBlockingQueue queue) {
		this.queue = queue;
		restClient = new StudentRestClient();
	}
	
	public void run() {
		logger.info("Starting polling for student messages to post to BMT....");
		final List<StudentMessageType> messages = queue.dequeue();
		
		if (CollectionUtils.isEmpty(messages)) {
			logger.info("No messages to post to BMT.");
			// Nothing to do here. Go away.
			return;
		}
		
		logger.info("Posting " + Integer.valueOf(messages.size()).toString() + " messages to BMT.");
		restClient.postStudentList(messages);
	}

}
