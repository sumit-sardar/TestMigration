package com.ctb.util.web.listener; 

import java.lang.reflect.Field;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * HTTP Session Attribute Listener to assist as a watch dog on developers
 * putting non-serializable attributes on the session.
 */
public class WatchdogSessionAttributeListener implements HttpSessionAttributeListener
{ 
    
    
    public void attributeAdded(HttpSessionBindingEvent event)
    {
        checkSerializable(event, "attributeAdded");
    }
    

    public void attributeRemoved(HttpSessionBindingEvent event)
    {
        checkSerializable(event, "attributeRemoved");
    }

    public void attributeReplaced(HttpSessionBindingEvent event)
    {
        checkSerializable(event, "attributeReplaced");
    }
    
    
    private void checkSerializable(HttpSessionBindingEvent event, String eventType) {
        System.err.println( "WatchdogSessionAttributeListener." + eventType );
        System.err.println( " sessionID:         " + event.getSession().getId() );
        System.err.println( " name of attribute: " + event.getName() );
        System.err.println( " value of object:   " + event.getValue().getClass().toString() );

        try {
            Field serialVersionUID = event.getValue().getClass().getDeclaredField("serialVersionUID");
            System.err.println( " serialVersionUID:  " + serialVersionUID.toString() );
        } catch( NoSuchFieldException nsfe ) {
            System.err.println( " serialVersionUID:  not defined" );
        }

        try {                
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject( event.getValue());
            out.close();
            System.err.println( " - serialized succeeded" );
        } catch( NotSerializableException nse ) {
            System.err.println( " - serialized failed! NotSerializableException caught!" );
        } catch( IOException ioe ) {
            System.err.println( " - serialization test failed!" );
            ioe.printStackTrace(System.err);
        }
    }
    
} 
