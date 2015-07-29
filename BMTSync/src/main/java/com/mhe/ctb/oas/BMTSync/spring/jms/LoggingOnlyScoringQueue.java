package com.mhe.ctb.oas.BMTSync.spring.jms;

import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class LoggingOnlyScoringQueue implements ScoringQueue {

	public static final Logger logger = Logger.getLogger(LoggingOnlyScoringQueue.class);
	
	@Override
	public void init(Context ctx, String jmsFactory, String queueName)
			throws NamingException, JMSException {
		logger.info("[LoggingOnlyScoringQueue] Called init() with the following parameters: [ctx=" + ctx.toString() + ",jmsFactory="
				+ jmsFactory + ",queueName=" + queueName + "]");
	}

	@Override
	public void send(Integer testRosterId) throws JMSException {
		logger.info("[LoggingOnlyScoringQueue] Called send() with the following parameters: [testRosterId=" + testRosterId + "]");
	}

}
