package com.ctb.util.jmsutils;

import java.util.ArrayList;

import javax.jms.ObjectMessage;

import com.ctb.control.JmsConnection;
import com.ctb.exception.JmsConnectionException.CustomJMSConnectionException;

public class ExportDataJMSUtil extends JmsConnection {

	private ExportDataVO exportDataVO = new ExportDataVO ();
	
	
	public void initGenerateReportTask ( String users, Integer customerId, Integer userId, int jobId, ArrayList<Integer>  testroster) 
			throws CustomJMSConnectionException{
		
		exportDataVO.setCustomerId(customerId);
		exportDataVO.setUserId(userId);
		exportDataVO.setUserName(users);
		exportDataVO.setJobId(jobId);
		exportDataVO.setTestroster(testroster);
		
		this.SendMessage();
		
	}
	
	/**
	 * 
	 */
	
	public  ObjectMessage generateMessageObject (ObjectMessage objectMessage) 
			throws CustomJMSConnectionException  {
		
		try {
			
			objectMessage.setObject(exportDataVO);
			
		} catch (Exception e) {
			
			throw new CustomJMSConnectionException ("Object message exception...");
		}
		
		return objectMessage;
	}

}
