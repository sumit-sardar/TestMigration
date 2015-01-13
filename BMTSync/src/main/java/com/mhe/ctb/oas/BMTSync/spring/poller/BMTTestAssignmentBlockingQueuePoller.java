package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;

import com.mhe.ctb.oas.BMTSync.controller.AssignmentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;


public class BMTTestAssignmentBlockingQueuePoller implements DisposableBean {

	public final ScheduledThreadPoolExecutor executor;
	private static final Logger logger = Logger.getLogger(BMTTestAssignmentBlockingQueuePoller.class);

	public BMTTestAssignmentBlockingQueuePoller(final BMTBlockingQueue<TestAssignmentMessageType> queue,
			final AssignmentRestClient restClient) {
		logger.info("Creating poller to watch queue for TestAssignment messages to post to BMT.");
		executor = new ScheduledThreadPoolExecutor(1);
		executor.setKeepAliveTime(1, TimeUnit.MINUTES);
		executor.scheduleAtFixedRate(new BMTTestAssignmentBlockingQueueWorker(queue, restClient), 1, 5, TimeUnit.SECONDS);
	}

	@Override
	public void destroy() throws Exception {
        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS))
        {
            logger.warn("Unable to shut down within 10 seconds.  Forcing Termination.");
            executor.shutdownNow();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        }
	}
}
