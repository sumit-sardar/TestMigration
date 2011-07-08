package com.ctb.control;


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

import com.ctb.exception.JmsConnectionException.CustomJMSConnectionException;
import com.ctb.utils.Constants;

/**
 * This class will be taking responsibility to generate JMS connection related objects
 * @author TCS
 *
 */

public abstract class JmsConnection {

	private static Context jndiContext = null;




	/**
	 * The instance InitialContext will be created  after the JmsConnection will be loaded 
	 */

	static {

		try {
			jndiContext = new InitialContext ();
		}  catch (NamingException e) {
			System.out.println("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			System.out.println("JNDI API lookup failed: " + e.toString());
			e.printStackTrace();
		}

	}

	/**
	 * This method has a responsibility to generate <code>QueueConnectionFactory</code> 
	 * by looking up <code>InitialContext</code> with jndiFactory string
	 * @param jndiFact
	 * @return QueueConnectionFactory
	 * @throws CustomJMSConnectionException
	 */

	private  QueueConnectionFactory getConnectionFactory () 
	throws CustomJMSConnectionException{

		QueueConnectionFactory queueConnectionFactory = null;
		try {

			queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup(
					Constants.OASConnFactory);

		} catch (NamingException e) {
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			throw new CustomJMSConnectionException ("JNDI API lookup failed: " + e.toString());
		}

		return queueConnectionFactory;
	}

	/**
	 * This method has a responsibility to generate <code>JMSQueue</code> by looking 
	 * up <code>InitialContext</code> with jndiQueue string
	 * @param jndiQueue
	 * @return QueueConnectionFactory
	 * @throws CustomJMSConnectionException
	 */

	private Queue getQueue () throws CustomJMSConnectionException{

		Queue jmsQueue = null;
		try {

			jmsQueue = (Queue) jndiContext.lookup(Constants.OASQueue);


		} catch (NamingException e) {
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			throw new CustomJMSConnectionException ("JNDI API lookup failed: " + e.toString());
		}

		return jmsQueue;

	}
	
	/**
	 * This method is responsible to generate <code>QueueConnection</code> 
	 * by passing the instance of <code>QueueConnectionFactory</code>
	 * @param queueConnectionFactory
	 * @return QueueConnection
	 * @throws CustomJMSConnectionException
	 */
	
	private QueueConnection getQueueConnection (QueueConnectionFactory queueConnectionFactory) 
		throws CustomJMSConnectionException {

		QueueConnection queueConnection = null;
		try {

			queueConnection = queueConnectionFactory.createQueueConnection();
			
		} catch (JMSException e) {
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			throw new CustomJMSConnectionException ("JNDI API lookup failed: " + e.toString());
		}
		
		return queueConnection;
	}
	
	/**
	 * This method is responsible to generate <code>QueueSession</code> 
	 * by passing the instance of <code>QueueConnection</code>
	 * @param queueConnection
	 * @return QueueSession
	 * @throws CustomJMSConnectionException
	 */
	
	private QueueSession getQueueSession (QueueConnection queueConnection) throws CustomJMSConnectionException {
		
		QueueSession queueSession = null;
		try {

			queueSession =
				queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			
		} catch (JMSException e) {
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			throw new CustomJMSConnectionException ("JNDI API lookup failed: " + e.toString());
		}
		
		return queueSession;
	}
	
	/**
	 * This method is responsible to generate <code>QueueSender</code> 
	 * by passing the instance of <code>QueueConnection</code> and <code>Queue</code>
	 * @param queueSession
	 * @param jmsQueue
	 * @return QueueSender
	 * @throws CustomJMSConnectionException
	 */
	
	private QueueSender getQueueSender (QueueSession queueSession, Queue jmsQueue) 
			throws CustomJMSConnectionException {

		QueueSender queueSender = null;
		try {

			queueSender = queueSession.createSender(jmsQueue); 

		} catch (JMSException e) {
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			throw new CustomJMSConnectionException ("JNDI API lookup failed: " + e.toString());
		}

		return queueSender;
	}
	
	/**
	 * This method is responsible to close the instance of <code>QueueConnection</code> and <code>QueueSession</code>
	 * @param queueConnectionFactory
	 * @param jmsQueue
	 * @throws CustomJMSConnectionException
	 */
	
	private void closeJmsConnection (QueueConnection queueConnection,QueueSession queueSession ) throws CustomJMSConnectionException{

		try {

			if (queueSession != null)                    
				queueSession.close();
			if (queueConnection != null)
				queueConnection.close();
		} catch (JMSException e) {
			throw new CustomJMSConnectionException ("Queue connection exception..."+e.toString());
		}

	}

	/**
	 * This method acts as template and has a responsibility to generate instances of <code>QueueConnectionFactory</code>,<code>Queue</code>,
	 * <code>QueueConnection</code>,<code>QueueSession</code> and <code>QueueSender</code>, by using the instances it sends the <code>MessageObject</code>.
	 * @return void
	 * @throws CustomJMSConnectionException
	 */

	protected void SendMessage () throws CustomJMSConnectionException { 
		try {

			QueueConnectionFactory queueConnectionFactory = this.getConnectionFactory ();
			Queue jmsQueue = this.getQueue ();
			QueueConnection queueConnection = this.getQueueConnection(queueConnectionFactory);
			QueueSession queueSession = this.getQueueSession(queueConnection);
			QueueSender queueSender = this.getQueueSender(queueSession, jmsQueue);
			ObjectMessage objectMessage = generateMessageObject (queueSession.createObjectMessage());
			queueSender.send(objectMessage);
			closeJmsConnection (queueConnection,queueSession );

		} catch (JMSException e) {
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			throw new CustomJMSConnectionException ("JNDI API lookup failed: " + e.toString());
		}
		
	}
	
	/**
	 * 
	 * @return
	 * @throws CustomJMSConnectionException
	 */

	public abstract ObjectMessage generateMessageObject (ObjectMessage objectMessage) throws CustomJMSConnectionException ;
	
	


}
