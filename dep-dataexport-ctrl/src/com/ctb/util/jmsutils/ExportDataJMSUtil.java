package com.ctb.util.jmsutils;

import javax.jms.ObjectMessage;

import com.ctb.control.JmsConnection;
import com.ctb.exception.JmsConnectionException.CustomJMSConnectionException;

public class ExportDataJMSUtil extends JmsConnection {

	private ExportDataVO exportDataVO = new ExportDataVO ();
	
	
	public void initGenerateReportTask ( String users, Integer customerId) 
			throws CustomJMSConnectionException{
		
		exportDataVO.setCustomerId(customerId);
		
		this.SendMessage();
		
	}
	
	/**
	 * 
	 */
	
	public  ObjectMessage generateMessageObject (ObjectMessage objectMessage) 
			throws CustomJMSConnectionException  {
		
		try {
			
			objectMessage.setObject(exportDataVO.getCustomerId());
			
		} catch (Exception e) {
			
			throw new CustomJMSConnectionException ("Object message exception...");
		}
		
		return objectMessage;
	}

}
