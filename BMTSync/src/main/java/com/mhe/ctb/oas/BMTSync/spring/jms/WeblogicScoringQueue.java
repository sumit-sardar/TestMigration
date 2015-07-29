package com.mhe.ctb.oas.BMTSync.spring.jms;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;

import org.springframework.beans.factory.DisposableBean;

public class WeblogicScoringQueue implements ScoringQueue, DisposableBean {
	private QueueConnectionFactory qconFactory;
	private QueueConnection qcon;
	private QueueSession qsession;
	private QueueSender qsender;
	private Queue queue;

	/**
	 * Creates all the necessary objects for sending
	 * messages to a JMS queue.
	 *
	 * @param ctx JNDI initial context
	 * @param queueName name of queue
	 * @exception NamingException if operation cannot be performed
	 * @exception JMSException if JMS fails to initialize due to internal error
	 */
	@Override
	public void init(Context ctx, String jmsFactory, String queueName) throws NamingException, JMSException {
		qconFactory = (QueueConnectionFactory) ctx.lookup(jmsFactory);
	    qcon = qconFactory.createQueueConnection();
	    qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	    queue = (Queue) ctx.lookup(queueName);
	    qsender = qsession.createSender(queue);
	    qcon.start();
	}

	/**
	 * Sends a message to a JMS queue.
	 *
	 * @param message  message to be sent
	 * @exception JMSException if JMS fails to send message due to internal error
	 */
	@Override
	public void send(Integer testRosterId) throws JMSException {
		final ObjectMessage msg = qsession.createObjectMessage();
		msg.setObject(new Integer(testRosterId));  
	    qsender.send(msg);
	}

	/**
	 * Closes JMS objects.
	 * @exception JMSException if JMS fails to close objects due to internal error
	 */
	@Override
	public void destroy() throws Exception {
		qsender.close();
		qsession.close();
		qcon.close();
	}
}
