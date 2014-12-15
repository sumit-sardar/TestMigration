package com.mhe.ctb.oas.BMTSync.spring.jms;


import javax.jms.JMSException;

import org.apache.jasper.compiler.JspUtil.ValidAttribute;
import org.apache.log4j.Logger;
import org.springframework.jms.support.converter.MessageConversionException;

import com.mhe.ctb.oas.BMTSync.controller.StudentRestClient;

/**
 * Handles a message from the Student Update Queue
 * 
 * @author cparis
 *
 */
public class NotificationMessageHandler {
	private static Logger LOGGER = Logger.getLogger(NotificationMessageHandler.class);
	private final StudentRestClient restClient;
	
	public NotificationMessageHandler() {
		restClient = new StudentRestClient();
	}
	
    public void handleMessage(final StudentMessageType message) 
            throws MessageConversionException, JMSException {
    	final StringBuilder msgBldr = new StringBuilder();
    	boolean invalidMessage = false;
    	if (message == null) {
    		throw new JMSException("Message should not be null!");
    	}
    	
    	if (message.getCustomerId() == null) {
    		invalidMessage = true;
    		msgBldr.append("[customerId=null]");
    	}
    	
    	if (message.getStudentId() == null) {
    		invalidMessage = true;
    		msgBldr.append("[studentId=null]");
    	}
    	/*	
    	if (message.getUpdatedDateTime() == null) {
    		invalidMessage = true;
    		msgBldr.append("[updatedDateTime=null]");
    	}
    	*/
    	if (invalidMessage) {
    		throw new MessageConversionException("Content of message cannot be null. " + msgBldr.toString());
    	}
    	
    	LOGGER.debug(String.format("Received Update: customerId(%s) studentId(%s) updateDateTime(%s)",
    			message.getCustomerId(), message.getStudentId(), message.getUpdatedDateTime())
    	);
    	restClient.postStudentList(message.getCustomerId(), message.getStudentId(), message.getUpdatedDateTime());
    }


	
}
