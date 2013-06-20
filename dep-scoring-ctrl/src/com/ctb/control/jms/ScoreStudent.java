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
                        sendJndiName = "irsDataSource", 
                        jndiConnectionFactory = "irsDataSource"
)
public interface ScoreStudent extends JMSControl 
{
    /**
     * this method will send a javax.jms.ObjectMessage to send-jndi-name
     */
    public void sendObjectMessage(Object payload);
    static final long serialVersionUID = 1L;
}
