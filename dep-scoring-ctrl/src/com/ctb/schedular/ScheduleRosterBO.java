package com.ctb.schedular;

import java.util.List;
import java.util.ResourceBundle;

import javax.naming.InitialContext;

import com.ctb.control.jms.QueueSend;

/**
 * @author TCS Kolkata Offshore 
 * @version 05/09/2012
 */
public class ScheduleRosterBO {
	  private String jndiFactory = "";
	  private String jmsFactory = "";
	  private String jmsURL = "";
	  private String jmsQueue = "";
	  private String jmsPrincipal = "";
	  private String jmsCredentials = "";
	  
	 

	public void scheduleRoster(Configuration conf){
		List<Integer> rosterList = null;
		InitialContext ic = null;
		QueueSend qs = null;
		ScheduleRosterDao dao = new ScheduleRosterDao();
		
		try{
			getResourceValue();
			ic = QueueSend.getInitialContext(jndiFactory, jmsURL, jmsPrincipal, jmsCredentials);
			qs = new QueueSend();
			qs.init(ic, jmsFactory, jmsQueue);
			
			rosterList = dao.getSchedulableRosterList(conf);
			System.out.println("Schedular will invoke scoring for Roster "+rosterList+".");
			for (Integer integer : rosterList) {
				invokeScoring(qs, integer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			ClosableHelper.close(qs);
			ClosableHelper.close(ic);
		}
		
	}
	
	
	
	 private void getResourceValue() throws Exception {
		ResourceBundle rb = ResourceBundle.getBundle("security");
		jndiFactory = rb.getString("jndiFactory");
		jmsFactory = rb.getString("jmsFactory");
		jmsURL = rb.getString("jmsURL");
		jmsQueue = rb.getString("jmsQueue");
		jmsPrincipal = rb.getString("jmsPrincipal");
		jmsCredentials = rb.getString("jmsCredentials");
	}
	 
	 @SuppressWarnings("static-access")
	private void invokeScoring(QueueSend qs, Integer testRosterId) throws Exception {
		qs.readAndSend(qs, testRosterId);
		
	}
	
	 
}
