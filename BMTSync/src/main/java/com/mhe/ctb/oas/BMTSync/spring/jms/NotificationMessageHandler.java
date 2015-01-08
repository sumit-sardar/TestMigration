package com.mhe.ctb.oas.BMTSync.spring.jms;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.jms.support.converter.MessageConversionException;

import com.mhe.ctb.oas.BMTSync.util.BMTBlockingQueue;

/**
 * Handles a message from the Student Update Queue
 * 
 * @author cparis
 *
 */
public class NotificationMessageHandler<T extends EnqueueableMessage> {
	private static Logger logger = Logger.getLogger(NotificationMessageHandler.class);
	private final BMTBlockingQueue<T> queue;
	
	public NotificationMessageHandler(final BMTBlockingQueue<T> queue) {
		this.queue = queue;
	}
	
    public void handleMessage(final T message) 
            throws MessageConversionException, JMSException {
    	if (message == null) {
    		throw new JMSException("Message should not be null!");
    	}
    	
    	if (message.getPrimaryKeyValue() == null || message.getSecondaryKeyValue() == null) {
    		throw new MessageConversionException("Content of message cannot be null. " + message.getLogDetails());
    	}
    	
    	logger.info("Received message: " + message.getLogDetails());

    	// Message isn't null, so add it to the queue to post.
    	try {    		
    		queue.enqueueWithTimeout(message);
    	} catch (MessageConversionException mce) {
    		throw mce;
    	}
    }
}
