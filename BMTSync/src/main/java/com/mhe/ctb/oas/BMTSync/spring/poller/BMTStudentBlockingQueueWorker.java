package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;



public class BMTStudentBlockingQueueWorker implements Runnable {

	private final BMTBlockingQueue<StudentMessageType> queue;
	private final StudentRestClient restClient;
	private static final Logger logger = Logger.getLogger(BMTStudentBlockingQueueWorker.class);

	public BMTStudentBlockingQueueWorker(final BMTBlockingQueue<StudentMessageType> queue, final StudentRestClient restClient) {
		this.queue = queue;
		this.restClient = restClient;
	}
	
	public void run() {
		logger.debug("Starting polling for student messages to post to BMT....");
		final List<StudentMessageType> messages = queue.dequeue();
		
		if (CollectionUtils.isEmpty(messages)) {
			logger.debug("No student messages to post to BMT.");
			// Nothing to do here. Go away.
			return;
		}
		
		logger.debug("Posting " + Integer.valueOf(messages.size()).toString() + " student messages to BMT.");
		restClient.postStudentList(messages);
	}

}
