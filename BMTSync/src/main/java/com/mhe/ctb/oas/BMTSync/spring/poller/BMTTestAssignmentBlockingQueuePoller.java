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

public class BMTTestAssignmentBlockingQueuePoller implements DisposableBean, InitializingBean {

	private static final Logger logger = Logger.getLogger(BMTTestAssignmentBlockingQueuePoller.class);
	
	private List<BMTTestAssignmentBlockingQueueWorker> workers;
	private BMTBlockingQueue<TestAssignmentMessageType> queue;
	private AssignmentRestClient restClient;
	private int workerCount;
	private int batchSize;
	
	@Autowired
	public BMTTestAssignmentBlockingQueuePoller(final BMTBlockingQueue<TestAssignmentMessageType> queue,
			final AssignmentRestClient restClient, final int workerCount, final int batchSize) {
		this.queue = queue;
		this.restClient = restClient;
		this.workerCount = workerCount;
		this.batchSize = batchSize;
	}

	@Override
	public void destroy() throws Exception {
		for (final BMTTestAssignmentBlockingQueueWorker worker : workers) {
			worker.shouldStop();
		}
	}

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
