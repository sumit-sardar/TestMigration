package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;

import com.mhe.ctb.oas.BMTSync.controller.TestAdminRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAdminMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

public class BMTTestAdminBlockingQueuePoller implements DisposableBean {

	public final ScheduledThreadPoolExecutor executor;
	private static final Logger logger = Logger.getLogger(BMTTestAdminBlockingQueuePoller.class);

	public BMTTestAdminBlockingQueuePoller(final BMTBlockingQueue<TestAdminMessageType> queue,
			final TestAdminRestClient restClient) {
		logger.info("Creating poller to watch queue for TestAdmin messages to post to BMT.");
		executor = new ScheduledThreadPoolExecutor(1);
		executor.setKeepAliveTime(1, TimeUnit.MINUTES);
		executor.scheduleAtFixedRate(new BMTTestAdminBlockingQueueWorker(queue, restClient), 1, 5, TimeUnit.SECONDS);
	}

	@Override
	public void destroy() throws Exception {
		executor.shutdown();
	}
}
