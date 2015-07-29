package com.mhe.ctb.oas.BMTSync.spring.jms;

import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NamingException;

public interface ScoringQueue {
	void init(Context ctx, String jmsFactory, String queueName) throws NamingException, JMSException;
	
	void send(Integer testRosterId) throws JMSException;
}
