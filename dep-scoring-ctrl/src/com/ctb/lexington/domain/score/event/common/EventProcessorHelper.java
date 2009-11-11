package com.ctb.lexington.domain.score.event.common;

import java.lang.reflect.InvocationTargetException;

import com.ctb.lexington.domain.score.scorer.calculator.Calculator;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.EventChannelException;
import com.ctb.lexington.exception.EventChannelIllegalStateException;

public class EventProcessorHelper {
    /**
     * Constructor for EventProcessorHelper.
     */
    private EventProcessorHelper() {
        super();
    }

    /**
     * Finds the matching <code>onEvent</code> handler for the given <var>event</var>
     * within the <var>handlerClass</var>.  This is a public method so it is visible
     * from the test package.
     *
     * @param event the event
     *
     * @return the method
     */
    public static boolean invokeEventHandler(Event event, EventRecipient recipient) {
        for (Class eventClass = event.getClass(); eventClass != null; eventClass = eventClass.getSuperclass()) {
            try {
                //Method eventHandler = recipient.getClass().getMethod("onEvent", new Class[]{eventClass});
                try {
                    if (recipient instanceof Calculator) {
                        // once we have a reference to the method which handles this event,
                        // call the validateState method for that class
                        //Method validateStateHandler = recipient.getClass().getMethod("validateState", new Class[] {Event.class});
                        //validateStateHandler.invoke(recipient, new Object[] {event});
                        ((Calculator) recipient).validateState(event);
                    } 
                    //eventHandler.invoke(recipient, new Object[] {event});
                    recipient.onEvent(event);
                    return true;
                /*} catch (IllegalAccessException e) {
                    rethrowAsEventChannelException(e);
                } catch (InvocationTargetException e) {
                    rethrowAsEventChannelException(e);
                }*/
	            } catch (EventChannelIllegalStateException e) {
	                rethrowAsEventChannelException(e);
	            } catch (IllegalArgumentException e) {
	                rethrowAsEventChannelException(e);
	            }
            //} catch (NoSuchMethodException e) {
            } catch (CTBSystemException e) {
            	e.printStackTrace();
                // ignore
            }
            // event is always a subclass of type Event. Once we are that high in the inheritance tree
            // we stop
            if (Event.class.equals(eventClass))
                return false;
        }
        return false;
    }

    private static void rethrowAsEventChannelException(Throwable t) {
        Throwable throwable = t.getCause();

        if (throwable != null) {
            if (throwable instanceof RuntimeException) {
                rethrowAsEventChannelException(t.getCause());
            } else if (throwable instanceof InvocationTargetException) {
                rethrowAsEventChannelException(t.getCause());
            }
        }

        throw new EventChannelException(t);
    }
}