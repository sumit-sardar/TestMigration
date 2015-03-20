package com.mhe.ctb.oas.BMTSync.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.jms.support.converter.MessageConversionException;

import com.mhe.ctb.oas.BMTSync.spring.jms.EnqueueableMessage;

/**
 * Class to implement a blocking queue of EnqueueableMessage objects.
 * @author kristy_tracer
 *
 * @param <T> A class implementing EnqueueableMessage
 */
public class BMTBlockingQueue<T extends EnqueueableMessage> extends LinkedBlockingQueue<T> {	

	private static final long serialVersionUID = 4458659170047975987L;
	
	/** The logger. */
	private static final Logger logger = Logger.getLogger(BMTBlockingQueue.class);
	
	/** The timeout for waiting for new messages. */
	private final long timeout;
	
	/** The size of the queue. */
	private final int queueSize;
	
	/**
	 * Constructor.
	 * @param queueSize
	 * @param timeout
	 */
	public BMTBlockingQueue(final int queueSize, final long timeout) {
		super(queueSize);
		this.queueSize = queueSize;
		this.timeout = timeout;
	}
	
	/**
	 * Attempts to add a message to a queue with a rudimentary exception handling system.
	 * @param message Message to be enqueued.
	 * @throws InterruptedException If something interrupts the thread.
	 * @throws MessageConversionException If there's a JMS conversion exception.
	 */
	public void enqueueWithTimeout(final T message) throws InterruptedException, MessageConversionException {

		try {
			if (super.offer(message, timeout, TimeUnit.MILLISECONDS)) {
				logger.info(message.getMessageType() + " message added to queue." + message.getLogDetails());
				return;
			}
		} catch (InterruptedException ie) {
			logger.error("Interrupted while waiting to add message to queue. " + message.getLogDetails(), ie);
			throw ie;
		}
		logger.error("Failed to add message to queue; queue likely full." + message.getLogDetails());
		throw new MessageConversionException("Failed to add message to queue; queue likely full." + message.getLogDetails());
	}
	
	/**
	 * Generic dequeue to return the entire block of enqueued messages.
	 * @return All the enqueued messages.
	 */
	public List<T> dequeue() {
		return dequeue(queueSize);
	}
	
	/**
	 * A dequeue to return only a subset of the messages in the blocking queue.
	 * @param batchSize The maximum number of messages to return.
	 * @return The dequeued messages.
	 */
	public List<T> dequeue(final int batchSize) {
		final List<T> messageList = new ArrayList<T>();
		logger.debug("Attempting to dequeue message from the queue....");
		T message = null;
		try {
			message = super.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException ie) {
			logger.error("Interrupted while waiting to dequeue message from queue.", ie);
		}
		if (message == null) {
			logger.debug("No messages in queue. Returning empty.");
			return Collections.emptyList();
		}
		logger.debug("One message dequeued from the queue; draining the queue up to queue size.");
		messageList.add(message);
		// We got one; pull whatever's in the queue up to the limit of the return size.
		super.drainTo(messageList, batchSize - 1);
		logger.info("Returning " + Integer.valueOf(messageList.size()).toString() + " messages from queue.");
		return messageList;
	}
}
