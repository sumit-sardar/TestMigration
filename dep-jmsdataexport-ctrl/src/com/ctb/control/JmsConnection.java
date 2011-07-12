package com.ctb.control;


import java.util.Hashtable;
import java.util.ResourceBundle;

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

/**
 * This class will be taking responsibility to generate JMS connection related objects
 * @author TCS
 *
 */

@SuppressWarnings("unchecked")
public abstract class JmsConnection {

	private static Context jndiContext = null;




	/**
	 * The instance InitialContext will be created  after the JmsConnection will be loaded 
	 */

	static {
		ResourceBundle rb = null;
		String providerurl = null;
		String principal = null;
		String credentials = null;
		String jndifactory = null;
		try {
			//jndiContext = new InitialContext ();
			rb = ResourceBundle.getBundle("security");
			providerurl = rb.getString("irsExportdataJmsProviderurl");
			principal = rb.getString("jmsPrincipal");
			credentials = rb.getString("jmsCredentials");
			jndifactory = rb.getString("jndiFactory");

			Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY,jndifactory);
            env.put(Context.PROVIDER_URL,providerurl);
            env.put("java.naming.security.principal", principal);
    	    env.put("java.naming.security.credentials", credentials);
    	    System.out.println("env"+env);
    	    //System.out.println("providerurl["+providerurl+"] principal["+principal+"]credentials"+credentials+"]jndifactory["+jndifactory+"]");
            jndiContext = new InitialContext(env);
            
		}  catch (NamingException e) {
			System.out.println("Could not create JNDI API context: " + e.toString());
			System.out.println("providerurl["+providerurl+"] principal["+principal+"]credentials"+credentials+"]jndifactory["+jndifactory+"]");
		} catch (Exception e) {
			System.out.println("JNDI API lookup failed: " + e.toString());
			System.out.println("providerurl["+providerurl+"] principal["+principal+"]credentials"+credentials+"]jndifactory["+jndifactory+"]");
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
		ResourceBundle rb = ResourceBundle.getBundle("security");
		String irsConnectionFactory = rb.getString("irsExportdataJmsConnectionfactory");
		try {

			queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup(irsConnectionFactory);

		} catch (NamingException e) {
			e.printStackTrace();
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
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
		ResourceBundle rb = ResourceBundle.getBundle("security");
		String irsqueue = rb.getString("irsExportdataJmsQueue");
		try {

			jmsQueue = (Queue) jndiContext.lookup(irsqueue);


		} catch (NamingException e) {
			e.printStackTrace();
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
			throw new CustomJMSConnectionException ("Could not create JNDI API context: " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
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
