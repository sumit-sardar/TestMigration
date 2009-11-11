package com.ctb.lexington.domain.score.event.common;

import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

import com.ctb.lexington.util.AutoHashMap;
import com.ctb.lexington.util.Timer;

public class EventChannel implements Channel {
    // a map of lists of subscribers keyed by event class
    private final Map subscribers = new AutoHashMap(Class.class, ArrayList.class, ArrayList.class);

    public void subscribe(EventRecipient eventRecipient, Class eventClass) {
        // check that eventClass is a subclass of Event
        if (!Event.class.isAssignableFrom(eventClass))
            throw new IllegalArgumentException("Events have to inherit from Event class: " + eventClass);

        final ArrayList registeredSubscribers = getRegisteredSubscribers(eventClass);
        if (!registeredSubscribers.contains(eventRecipient)) {
            registeredSubscribers.add(eventRecipient);
        }
    }

    /**
     * Sends the given <var>event </var> to all <code>EventRecipient</code> s subscribed for the
     * class of <var>event </var> and any superclasses of <var>event </var>. If there are no
     * recipients for <var>event </var>, <code>send</code> does nothing.
     * Delivers events to its subscribers in order of subscription. Ordering between super/sub
     * event classes is NOT guaranteed.
     *
     * @param event the event
     */
    public void send(Event event) {
        Timer totalTime = Timer.startTimer();
        // the order of the subcriber list is important!!
        ArrayList subscribers = getSubscribersForEventTypeAndItsSuperTypes(event.getClass());
        for (int i = 0; i < subscribers.size(); i++) {
            final EventRecipient recipient = (EventRecipient) subscribers.get(i);
            if (!EventProcessorHelper.invokeEventHandler(event, recipient))
                throw new IllegalStateException("No onEvent(" + event.getClass() + ") in " + recipient.getClass());
        }
        Timer.logElapsed(SEND_MSG_1 + event.getClass().getName() + SEND_MSG_2 + subscribers.size() + SEND_MSG_3, totalTime);
    }

    private ArrayList getRegisteredSubscribers(Class eventClass) {
        return (ArrayList) subscribers.get(eventClass);
    }

    private ArrayList getSubscribersForEventTypeAndItsSuperTypes(Class eventClass) {
        final ArrayList allSubscribers = new ArrayList();

        // the subscriber map is keyed by event class, and it is possible to a
        // subscribe to a class hierarchy of events.  In the case where there are multiple
        // events that are assignable from eventClass the order of delivery is NOT
        // ordered.  If you need to specify order, the easiest thing to do is to
        // not have inheritance in your events.
        for (Iterator iter = subscribers.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry e = (Map.Entry) iter.next();
            final Class subscriberEventClass = (Class) e.getKey();
            if (subscriberEventClass.isAssignableFrom(eventClass)) {
                allSubscribers.addAll((ArrayList) e.getValue());
            }
        }
        return allSubscribers;
    }

    private static final String SEND_MSG_1 = "total time spent sending (";
    private static final String SEND_MSG_2 = ") to ";
    private static final String SEND_MSG_3 = " subscribers: ";
}