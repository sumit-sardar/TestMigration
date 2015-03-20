package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

/**
 * A worker to pull messages from a queue and send them to BMT.
 * @author kristy_tracer
 *
 */
public class BMTStudentBlockingQueueWorker extends Thread {

	/** Logger. */
	private static final Logger logger = Logger.getLogger(BMTStudentBlockingQueueWorker.class);

	/** Queue. */
	private final BMTBlockingQueue<StudentMessageType> queue;
	
	/** BMT REST client. */
	private final StudentRestClient restClient;
	
	/** Whether the thread should run. */
	private boolean shouldRun;

	/**
	 * Constructor.
	 * @param queue
	 * @param restClient
	 */
	public BMTStudentBlockingQueueWorker(final BMTBlockingQueue<StudentMessageType> queue, final StudentRestClient restClient) {
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
	
	/** The thing: poll the queue for work, and if it finds some, send it to BMT. */
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
