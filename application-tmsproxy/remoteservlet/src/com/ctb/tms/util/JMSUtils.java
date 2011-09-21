package com.ctb.tms.util;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class JMSUtils {
	
	static Logger logger = Logger.getLogger(JMSUtils.class);
	
    private static String jndiFactory = "";
    private static String jmsFactory = "";
    private static String jmsURL = "";
    private static String jmsQueue = "";
    private static String jmsPrincipal = "";
    private static String jmsCredentials = "";
    
    static {
    	ResourceBundle rb = ResourceBundle.getBundle("security");
	    jndiFactory = rb.getString("jndiFactory");
	    jmsFactory = rb.getString("jmsFactory");
	    jmsURL = rb.getString("jmsURL");
	    jmsQueue = rb.getString("jmsQueue");
	    jmsPrincipal = rb.getString("jmsPrincipal");
	    jmsCredentials = rb.getString("jmsCredentials");
	}
       
    public static void sendMessage(Serializable messageValue) {
    	try {
			InitialContext ctx = null;
			QueueConnectionFactory qcf = null;
			QueueConnection qc = null;
			QueueSession qsess = null;
			Queue q = null;
			QueueSender qsndr = null;
		   
		   Hashtable properties = new Hashtable();
		   properties.put(Context.INITIAL_CONTEXT_FACTORY, jndiFactory);
		   properties.put(Context.PROVIDER_URL, jmsURL);
		   properties.put(Context.SECURITY_PRINCIPAL, jmsPrincipal);
		   properties.put(Context.SECURITY_CREDENTIALS, jmsCredentials);
		
		   ctx = new InitialContext(properties);
		   qcf = (QueueConnectionFactory)ctx.lookup(jmsFactory);
		   qc = qcf.createQueueConnection();
		   qsess = qc.createQueueSession(false, 0);
		   q = (Queue) ctx.lookup(jmsQueue);
		   qsndr = qsess.createSender(q);
		   ObjectMessage message = qsess.createObjectMessage();
		   message.setObject(messageValue);
		   qsndr.send(message);
		
		   // clean up
		   message = null;
		   qsndr.close();
		   qsndr = null;
		   q = null;
		   qsess.close();
		   qsess = null;
		   qc.close();
		   qc = null;
		   qcf = null;
		   ctx = null;
		   
		   logger.debug("Sent scoring message for roster: " + messageValue);
	   }
	   catch (Exception e) {
	       logger.error(e.getMessage(), e);
	   }
   }
    
}

