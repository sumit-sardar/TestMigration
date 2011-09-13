package com.ctb.tms.util;

import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class JMSUtils {
	
	static Logger logger = Logger.getLogger(JMSUtils.class);
	
    private static String jndiFactory = "";
    private static String jmsFactory = "";
    private static String jmsURL = "";
    private static String jmsQueue = "";
    private static String jmsPrincipal = "";
    private static String jmsCredentials = "";
    
    private static InitialContext ctx = null;
    private static QueueConnectionFactory qcf = null;
    private static QueueConnection qc = null;
    private static QueueSession qsess = null;
    private static Queue q = null;
    private static QueueSender qsndr = null;
    private static TextMessage message = null;
    
    static {
    	ResourceBundle rb = ResourceBundle.getBundle("security");
	    jndiFactory = rb.getString("jndiFactory");
	    jmsFactory = rb.getString("jmsFactory");
	    jmsURL = rb.getString("jmsURL");
	    jmsQueue = rb.getString("jmsQueue");
	    jmsPrincipal = rb.getString("jmsPrincipal");
	    jmsCredentials = rb.getString("jmsCredentials");
	}
       
   public static void sendMessage(String messageText) {
       // create InitialContext
       Hashtable properties = new Hashtable();
       properties.put(Context.INITIAL_CONTEXT_FACTORY, jndiFactory);
       // NOTE: The port number of the server is provided in the next line,
       //       followed by the userid and password on the next two lines.
       properties.put(Context.PROVIDER_URL, jmsURL);
       properties.put(Context.SECURITY_PRINCIPAL, jmsPrincipal);
       properties.put(Context.SECURITY_CREDENTIALS, jmsCredentials);
       try {
           ctx = new InitialContext(properties);
       } catch (NamingException ne) {
           ne.printStackTrace(System.err);
           System.exit(0);
       }
       System.out.println("Got InitialContext " + ctx.toString());
       // create QueueConnectionFactory
       try {
           qcf = (QueueConnectionFactory)ctx.lookup(jmsFactory);
       }
       catch (NamingException ne) {
           ne.printStackTrace(System.err);
           System.exit(0);
       }
       System.out.println("Got QueueConnectionFactory " + qcf.toString());
       // create QueueConnection
       try {
           qc = qcf.createQueueConnection();
       }
       catch (JMSException jmse) {
           jmse.printStackTrace(System.err);
           System.exit(0);
       }
       System.out.println("Got QueueConnection " + qc.toString());
       // create QueueSession
       try {
           qsess = qc.createQueueSession(false, 0);
       }
       catch (JMSException jmse) {
           jmse.printStackTrace(System.err);
           System.exit(0);
       }
       System.out.println("Got QueueSession " + qsess.toString());
       // lookup Queue
       try {
           q = (Queue) ctx.lookup(jmsQueue);
       }
       catch (NamingException ne) {
           ne.printStackTrace(System.err);
           System.exit(0);
       }
       System.out.println("Got Queue " + q.toString());
       // create QueueSender
       try {
           qsndr = qsess.createSender(q);
       }
       catch (JMSException jmse) {
           jmse.printStackTrace(System.err);
           System.exit(0);
       }
       System.out.println("Got QueueSender " + qsndr.toString());
       // create TextMessage
       try {
           message = qsess.createTextMessage();
       }
       catch (JMSException jmse) {
           jmse.printStackTrace(System.err);
           System.exit(0);
       }
       System.out.println("Got TextMessage " + message.toString());
       // set message text in TextMessage
       try {
           message.setText(messageText);
       }
       catch (JMSException jmse) {
           jmse.printStackTrace(System.err);
           System.exit(0);
       }
       System.out.println("Set text in TextMessage " + message.toString());
       // send message
       try {
           qsndr.send(message);
       }
       catch (JMSException jmse) {
           jmse.printStackTrace(System.err);
           System.exit(0);
       }
       System.out.println("Sent message ");
       // clean up
       try {
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
       }
       catch (JMSException jmse) {
           jmse.printStackTrace(System.err);
       }
       logger.debug("Cleaned up and done.");
   }
    
}

