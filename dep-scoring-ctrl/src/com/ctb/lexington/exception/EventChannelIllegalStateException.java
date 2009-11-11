package com.ctb.lexington.exception;

public class EventChannelIllegalStateException extends IllegalStateException {
    private final Class eventClass;
    private final Class reqdPredecessorClass;

    /**
     * Constructor for EventChannelIllegalStateException.
     */
    public EventChannelIllegalStateException(Class eventClass, Class reqdPredecessorClass) {
        // HACKTAG: Building the message up front is hacky - we should be overriding getMessage, etc
        // but for right now, this works
        super("Event " + eventClass.getName() + " received before event "
                + reqdPredecessorClass.getName());
        this.eventClass = eventClass;
        this.reqdPredecessorClass = reqdPredecessorClass;
    }

    public final Class getEvent() {
        return eventClass;
    }

    public final Class getReqdPredecessor() {
        return reqdPredecessorClass;
    }
}