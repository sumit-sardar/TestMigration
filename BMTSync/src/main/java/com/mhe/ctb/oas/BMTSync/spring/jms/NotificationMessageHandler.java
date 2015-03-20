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
	
	/** Constructor. */
	public NotificationMessageHandler(final BMTBlockingQueue<T> queue) {
		this.queue = queue;
	}
	
	/**
	 * Handle a message sent to us from the database.
	 * @param message The message sent by the database.
	 * @throws MessageConversionException If there's a conversion error
	 * @throws InterruptedException If the thread is interrupted.
	 * @throws JMSException If something else goes wrong with JMS.
	 */
    public void handleMessage(final T message) 
            throws MessageConversionException, InterruptedException, JMSException {
    	if (message == null) {
    		throw new JMSException("Message should not be null!");
    	}

    	logger.debug("Handling " + message.getMessageType() + " message.");
    	
    	// Only accept messages which have content.
    	if (message.getPrimaryKeyValue() == null || message.getSecondaryKeyValue() == null) {
    		throw new MessageConversionException("Content of message cannot be null. " + message.getLogDetails());
    	}
    	
    	logger.info("Received message: " + message.getLogDetails());

    	// Message isn't null, so add it to the queue to post.
    	try {    		
    		queue.enqueueWithTimeout(message);
    	} catch (InterruptedException ie) {
    		throw ie;
    	} catch (MessageConversionException mce) {
    		throw mce;
    	}
    }
}
