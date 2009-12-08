package com.ctb.control.jms; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueSend {

	// Defines the JNDI context factory.
	  public final static String JNDI_FACTORY="weblogic.jndi.WLInitialContextFactory";

	  // Defines the JMS context factory.
	  public final static String JMS_FACTORY="irsConnectionFactory";
	 
	  // Defines the JMS context factory.
	  public final static String JMS_URL="t3://dagobah.mhe.mhc:22411";

	  // Defines the queue.
	  public final static String QUEUE="ScoreStudentJMSQueue";

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
	  public void init(Context ctx, String queueName)
	    throws NamingException, JMSException
	  {
	    qconFactory = (QueueConnectionFactory) ctx.lookup(JMS_FACTORY);
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
	  public void send() throws JMSException {
	    msg.setObject(new Integer("1626113"));
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
	 /** main() method.
	  *
	  * @param args WebLogic Server URL
	  * @exception Exception if operation fails
	  */
	  public static void invoke(Integer testRosterId) throws Exception {
	    if (JMS_URL == null || "".equals(JMS_URL)) {
	      System.out.println("Usage: java examples.jms.queue.QueueSend WebLogicURL");
	      return;
	    }
	    InitialContext ic = getInitialContext(JMS_URL);
	    QueueSend qs = new QueueSend();
	    qs.init(ic, QUEUE);
	    readAndSend(qs);
	    qs.close();
	    
	    System.out.println("+++ ++Build3");
	    
	 /*   Hashtable<String,String> env = new Hashtable<String,String>();
	    env.put("java.naming.security.principal", "tai_dev");
	    env.put("java.naming.security.credentials", "tai009");
	   */ ic.close();
	    
	  }
	  
	  /** main() method.
	  *
	  * @param args WebLogic Server URL
	  * @exception Exception if operation fails
	  */
	  public static void main(String[] args) throws Exception {
		  //QueueSend qs1 = new QueueSend();
		  //QueueSend.invoke();
		  
	  }

	  private static void readAndSend(QueueSend qs)
	    throws IOException, JMSException
	  {
	    //BufferedReader msgStream = new BufferedReader(new InputStreamReader(System.in));
	    String line=null;
	    boolean quitNow = false;
	    do {
	      line = "Hi This is Queue";
	      if (line != null && line.trim().length() != 0) {
	        qs.send();
	        System.out.println("JMS Message Sent: "+line+"\n");
	        quitNow = true;
	      }
	    } while (! quitNow);

	  }

	  private static InitialContext getInitialContext(String url)
	    throws NamingException
	  {
	    Hashtable<String,String> env = new Hashtable<String,String>();
	    env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
	    env.put(Context.PROVIDER_URL, url);
	    env.put("java.naming.security.principal", "system");
	    env.put("java.naming.security.credentials", "ditp2luh");
	    return new InitialContext(env);
	  }
	
}
