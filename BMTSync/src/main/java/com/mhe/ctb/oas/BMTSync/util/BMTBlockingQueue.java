package com.mhe.ctb.oas.BMTSync.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.jms.support.converter.MessageConversionException;

import com.mhe.ctb.oas.BMTSync.spring.jms.EnqueueableMessage;

public class BMTBlockingQueue<T extends EnqueueableMessage> extends LinkedBlockingQueue<T> {	

	private static final long serialVersionUID = 4458659170047975987L;
	
	private static final Logger logger = Logger.getLogger(BMTBlockingQueue.class);
	private final long timeout;
	private final int queueSize;
	
	public BMTBlockingQueue(final int queueSize, final long timeout) {
		super(queueSize);
		this.queueSize = queueSize;
		this.timeout = timeout;
	}
	
	/**
	 * Attempts to add a message to a queue with a rudimentary exception handling system.
	 */
	public void enqueueWithTimeout(final T message) throws MessageConversionException {

		try {
			if (super.offer(message, timeout, TimeUnit.MILLISECONDS)) {
				logger.info("Message added to queue." + message.getLogDetails());
				return;
			}
		} catch (InterruptedException ie) {
			logger.error("Interrupted while waiting to add message to queue. " + message.getLogDetails(), ie);
			throw new MessageConversionException("Interrupted while waiting to add message to queue."
					+ message.getLogDetails(), ie);
		}
		logger.error("Failed to add message to queue; queue likely full." + message.getLogDetails());
		throw new MessageConversionException("Failed to add message to queue; queue likely full." + message.getLogDetails());
	}
	
	public List<T> dequeue() {
		final List<T> messageList = new ArrayList<T>();
		logger.info("Attempting to dequeue message from the queue....");
		T message = null;
		try {
			message = super.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException ie) {
			logger.error("Interrupted while waiting to dequeue additional messages.");
		}
		if (message == null) {
			logger.info("No messages in queue. Returning empty.");
			return Collections.emptyList();
		}
		logger.debug("One message dequeued from the queue; draining the queue up to queue size.");
		messageList.add(message);
		// We got one; pull whatever's in the queue up to the limit of the return size.
		super.drainTo(messageList, queueSize - 1);
		logger.info("Returning " + Integer.valueOf(messageList.size()).toString() + " messages from queue.");
		return messageList;
	}
}
