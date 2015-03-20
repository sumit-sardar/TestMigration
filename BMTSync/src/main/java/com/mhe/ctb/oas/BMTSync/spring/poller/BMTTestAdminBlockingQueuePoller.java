package com.mhe.ctb.oas.BMTSync.spring.poller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.mhe.ctb.oas.BMTSync.controller.TestAdminRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAdminMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

/**
 * Poller to start a worker thread and monitor is progress.
 * @author kristy_tracer
 *
 */
public class BMTTestAdminBlockingQueuePoller implements DisposableBean, InitializingBean {
	
	/** The logger. */
	private static final Logger logger = Logger.getLogger(BMTTestAdminBlockingQueuePoller.class);

	/** The worker. */
	private BMTTestAdminBlockingQueueWorker worker;
	
	/** The queue for the worker. */
	private BMTBlockingQueue<TestAdminMessageType> queue;
	
	/** The REST client for the worker. */
	private TestAdminRestClient restClient;
	
	/**
	 * Constructor.
	 * @param queue
	 * @param restClient
	 */
	@Autowired
	public BMTTestAdminBlockingQueuePoller(final BMTBlockingQueue<TestAdminMessageType> queue, final TestAdminRestClient restClient) {
		this.queue = queue;
		this.restClient = restClient;
	}

	/** Stop the thread if the application shuts down. */
	@Override
	public void destroy() throws Exception {
		worker.shouldStop();
	}

	/** Start the worker thread after the bean is constructed. */
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Creating poller to watch queue for TestAdmin messages to post to BMT.");
		worker = new BMTTestAdminBlockingQueueWorker(queue, restClient);
		worker.start();
	}

}
