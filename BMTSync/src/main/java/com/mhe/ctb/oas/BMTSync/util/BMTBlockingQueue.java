package com.mhe.ctb.oas.BMTSync.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.jms.support.converter.MessageConversionException;

import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;

public class BMTBlockingQueue extends LinkedBlockingQueue<StudentMessageType> {	

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
	public void enqueueWithTimeout(final StudentMessageType message) throws MessageConversionException {

		try {
			if (super.offer(message, timeout, TimeUnit.MILLISECONDS)) {
				logger.info("Message added to queue. [studentId=" + message.getStudentId().toString() + "]");
				return;
			}
		} catch (InterruptedException ie) {
			logger.error("Interrupted while waiting to add message to queue.", ie);
			throw new MessageConversionException("Interrupted when waiting to add message to queue.", ie);
		}
		logger.error("Failed to add message to queue; queue likely full. [studentId="
				+ message.getStudentId().toString() + "]");
		throw new MessageConversionException("Failed to add message to queue; queue likely full. [studentId="
				+ message.getStudentId().toString() + "]");
	}
	
	public List<StudentMessageType> dequeue() {
		final List<StudentMessageType> messageList = new ArrayList<StudentMessageType>();
		long countdown = timeout;
		while (countdown > 0) {
			Calendar start = Calendar.getInstance();
			try {
				final StudentMessageType message = super.poll(countdown,  TimeUnit.MILLISECONDS);
				messageList.add(message);
			} catch (InterruptedException ie) {
				logger.error("Interrupted while waiting to dequeue additional messages. Returning current list.");
				countdown = 0;
			}
			Calendar end = Calendar.getInstance();
			long diff = end.getTimeInMillis() - start.getTimeInMillis();
			countdown -= diff;
		}
		return messageList;
	}
}
