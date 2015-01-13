package com.mhe.ctb.oas.BMTSync.spring.poller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;
import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

public class BMTStudentBlockingQueuePoller implements DisposableBean, InitializingBean {

	private static final Logger logger = Logger.getLogger(BMTStudentBlockingQueuePoller.class);
	
	private BMTStudentBlockingQueueWorker worker;
	private BMTBlockingQueue<StudentMessageType> queue;
	private StudentRestClient restClient;

	@Autowired
	public BMTStudentBlockingQueuePoller(final BMTBlockingQueue<StudentMessageType> queue, final StudentRestClient restClient) {
		this.queue = queue;
		this.restClient = restClient;
	}

	@Override
	public void destroy() throws Exception {
		worker.shouldStop();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Creating poller to watch queue for Student messages to post to BMT.");
		worker = new BMTStudentBlockingQueueWorker(queue, restClient);
		worker.start();
	}
}
