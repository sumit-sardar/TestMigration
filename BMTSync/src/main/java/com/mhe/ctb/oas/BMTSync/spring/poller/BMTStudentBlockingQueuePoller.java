package com.mhe.ctb.oas.BMTSync.spring.poller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

/**
 * Poller to start a worker thread and monitor its performance.
 * @author kristy_tracer
 *
 */
public class BMTStudentBlockingQueuePoller implements DisposableBean, InitializingBean {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(BMTStudentBlockingQueuePoller.class);
	
	/** The worker. */
	private BMTStudentBlockingQueueWorker worker;
	
	/** The queue. */
	private BMTBlockingQueue<StudentMessageType> queue;
	
	/** The REST client. */
	private StudentRestClient restClient;

	/**
	 * The constructor.
	 * @param queue
	 * @param restClient
	 */
	@Autowired
	public BMTStudentBlockingQueuePoller(final BMTBlockingQueue<StudentMessageType> queue, final StudentRestClient restClient) {
		this.queue = queue;
		this.restClient = restClient;
	}

	/** Stop the worker if the application is shut down. */
	@Override
	public void destroy() throws Exception {
		worker.shouldStop();
	}

	/** Start the worker once the bean is created. */
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Creating poller to watch queue for Student messages to post to BMT.");
		worker = new BMTStudentBlockingQueueWorker(queue, restClient);
		worker.start();
	}
}
