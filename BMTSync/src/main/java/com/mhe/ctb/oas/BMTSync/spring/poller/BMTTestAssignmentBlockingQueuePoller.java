package com.mhe.ctb.oas.BMTSync.spring.poller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.mhe.ctb.oas.BMTSync.controller.AssignmentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

/**
 * A poller to start threads and monitor their	 progress.
 * @author kristy_tracer
 */
public class BMTTestAssignmentBlockingQueuePoller implements DisposableBean, InitializingBean {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(BMTTestAssignmentBlockingQueuePoller.class);
	
	/** A list of workers */
	private List<BMTTestAssignmentBlockingQueueWorker> workers;
	
	/** The queue for the workers. */
	private BMTBlockingQueue<TestAssignmentMessageType> queue;
	
	/** The rest client for the workers. */
	private AssignmentRestClient restClient;
	
	/** The number of workers to start. */
	private int workerCount;
	
	/** The size of the batch to send to BMT at once. */
	private int batchSize;
	
	/**
	 * Constructor.
	 * @param queue
	 * @param restClient
	 * @param workerCount
	 * @param batchSize
	 */
	@Autowired
	public BMTTestAssignmentBlockingQueuePoller(final BMTBlockingQueue<TestAssignmentMessageType> queue,
			final AssignmentRestClient restClient, final int workerCount, final int batchSize) {
		this.queue = queue;
		this.restClient = restClient;
		this.workerCount = workerCount;
		this.batchSize = batchSize;
	}

	/** Bean class to shut down all the worker threads when the app stops. */
	@Override
	public void destroy() throws Exception {
		for (final BMTTestAssignmentBlockingQueueWorker worker : workers) {
			worker.shouldStop();
		}
	}

	/** Start workers after the bean is constructed. */
	@Override
	public void afterPropertiesSet() throws Exception {
		workers = new ArrayList<BMTTestAssignmentBlockingQueueWorker>();
		for (int index = 0; index < workerCount; index++) {
			logger.info("Creating poller " + Integer.valueOf(index).toString() + " to watch queue for Assignment messages to post to BMT.");
			final BMTTestAssignmentBlockingQueueWorker worker = new BMTTestAssignmentBlockingQueueWorker(queue, restClient, batchSize);
			worker.start();
			workers.add(worker);
		}
	}
}
