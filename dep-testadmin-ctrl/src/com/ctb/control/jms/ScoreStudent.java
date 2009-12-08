package com.ctb.control.jms; 

import com.bea.control.*;
import java.io.Serializable;
import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jms.JMSControl;

/** 
 *   @jc:jms send-type="queue" send-jndi-name="ScoreStudentJMSQueue" 
 *            connection-factory-jndi-name="irsConnectionFactory" 
 */
@ControlExtension()
@JMSControl.Destination(sendType = JMSControl.DestinationType.Queue, 
                        sendJndiName = "ScoreStudentJMSQueue", 
                        jndiContextFactory = "weblogic.jndi.WLInitialContextFactory",
                        jndiConnectionFactory = "irsConnectionFactory",
                        jndiProviderURL = "t3://dagobah.mhe.mhc:22411",
                        jndiUsername ="system",
                        jndiPassword = "ditp2luh"

)
public interface ScoreStudent extends JMSControl 
{
    /**
     * this method will send a javax.jms.ObjectMessage to send-jndi-name
     */
    public void sendObjectMessage(Object payload);
    static final long serialVersionUID = 1L;
}
