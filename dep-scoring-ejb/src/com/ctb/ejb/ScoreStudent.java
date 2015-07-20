package com.ctb.ejb;

import com.ctb.lexington.domain.score.scorer.ScorerFactory;
import javax.ejb.*;
import javax.jms.*;
import weblogic.ejb.*;
import weblogic.ejbgen.JarSettings;

/**
 * @ejbgen:message-driven
 *   ejb-name = ScoreStudent
 *   destination-jndi-name = ScoreStudentJMSQueue
 *   destination-type = javax.jms.Queue
 *   default-transaction="Required"
 *   max-beans-in-free-pool = 10
 *
 */
@JarSettings(createTables = JarSettings.CreateTables.CREATE_ONLY)
@weblogic.ejbgen.MessageDriven(ejbName = "ScoreStudent", 
                               destinationJndiName = "ScoreStudentJMSQueue", 
                               destinationType = "javax.jms.Queue", 
                               defaultTransaction = weblogic.ejbgen.MessageDriven.DefaultTransaction.NOT_SUPPORTED, 
	maxBeansInFreePool = "10"
)
public class ScoreStudent
  extends GenericMessageDrivenBean
  implements MessageDrivenBean, MessageListener
{
  public void onMessage(Message msg) {
    if (msg instanceof ObjectMessage) {
        try {
        	ObjectMessage objMsg = (ObjectMessage) msg;
            if((objMsg.getObject() instanceof String) && ((String)objMsg.getObject()).contains("#")){
            	String msgStr = (String)objMsg.getObject();
            	Integer rosterId = Integer.valueOf(msgStr.substring(0, msgStr.indexOf("#")));
            	Long invokeKey = Long.valueOf(msgStr.substring(msgStr.indexOf("#")+1));
	            System.out.println("***** SCORING: ScoreStudentMessageEJB: onMessage: got a roster: " + rosterId);
	            ScorerFactory.invokeScoring(rosterId, invokeKey, false, true, false, true);
            }else {
	        	Integer rosterId = (Integer) objMsg.getObject();
	            System.out.println("***** SCORING: ScoreStudentMessageEJB: onMessage: got a roster: " + rosterId);
	            ScorerFactory.invokeScoring(rosterId, null, false, true, false, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("***** SCORING: ScoreStudentMessageEJB: onMessage: An error has occured: " + e.getMessage());
        }
    }
  }
}


