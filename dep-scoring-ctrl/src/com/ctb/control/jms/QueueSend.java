package com.ctb.control.jms; 

import java.io.IOException;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

 /**
 * @author TCS Kolkata Offshore 
 * @version 05/09/2012
 */
public class QueueSend {

	  private QueueConnectionFactory qconFactory;
	  private QueueConnection qcon;
	  private QueueSession qsession;
	  private QueueSender qsender;
	  private Queue queue;
	  private ObjectMessage msg;

	  /**
	   * Creates all the necessary objects for sending
	   * messages to a JMS queue.
	   *
	   * @param ctx JNDI initial context
	   * @param queueName name of queue
	   * @exception NamingException if operation cannot be performed
	   * @exception JMSException if JMS fails to initialize due to internal error
	   */
	  public void init(Context ctx, String jmsFactory, String queueName)
	    throws NamingException, JMSException
	  {
	    qconFactory = (QueueConnectionFactory) ctx.lookup(jmsFactory);
	    qcon = qconFactory.createQueueConnection();
	    qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	    queue = (Queue) ctx.lookup(queueName);
	    qsender = qsession.createSender(queue);
	    msg = qsession.createObjectMessage();
	    qcon.start();
	  }

	  /**
	   * Sends a message to a JMS queue.
	   *
	   * @param message  message to be sent
	   * @exception JMSException if JMS fails to send message due to internal error
	   */
	  public void send(Integer testRosterId) throws JMSException {
		msg.setObject(new Integer(testRosterId));  
	    qsender.send(msg);
	  }

	  /**
	   * Closes JMS objects.
	   * @exception JMSException if JMS fails to close objects due to internal error
	   */
	  public void close() throws JMSException {
	    qsender.close();
	    qsession.close();
	    qcon.close();
	  }
	 

	  public static void readAndSend(QueueSend qs,Integer testRosterId)
	    throws IOException, JMSException
	  {
	    if (testRosterId != null) {
	        qs.send(testRosterId);
	        System.out.println("JMS Message Sent");
	     } 
	  }

	  public static InitialContext getInitialContext(String contextFactory,String url,
			  String principal,String credentials)
	    throws NamingException
	  {
	    Hashtable<String,String> env = new Hashtable<String,String>();
	    env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
	    env.put(Context.PROVIDER_URL, url);
	    env.put("java.naming.security.principal", principal);
	    env.put("java.naming.security.credentials", credentials);
	    return new InitialContext(env);
	  }
	
}
