package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

public class BMTBlockingQueuePoller {

	public final ScheduledThreadPoolExecutor executor;
	private static final Logger logger = Logger.getLogger(BMTBlockingQueuePoller.class);

	public BMTBlockingQueuePoller(final BMTBlockingQueue queue, final StudentRestClient restClient) {
		logger.info("Creating poller to watch queue for messages to post to BMT.");
		executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(new BMTBlockingQueueWorker(queue, restClient), 1, 5, TimeUnit.SECONDS);
	}
}