package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;


public class BMTStudentBlockingQueuePoller {

	public final ScheduledThreadPoolExecutor executor;
	private static final Logger logger = Logger.getLogger(BMTStudentBlockingQueuePoller.class);

	public BMTStudentBlockingQueuePoller(final BMTBlockingQueue<StudentMessageType> queue, final StudentRestClient restClient) {
		logger.info("Creating poller to watch queue for messages to post to BMT.");
		executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(new BMTStudentBlockingQueueWorker(queue, restClient), 1, 5, TimeUnit.SECONDS);
	}
}
