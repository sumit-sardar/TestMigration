package com.ctb.lexington.exception;


public class EventChannelException extends RuntimeException {

    /**
     * Constructor for EventChannelException.
     * @param message
     */
    public EventChannelException(String message) {
        super(message);
    }

    /**
     * Constructor for EventChannelException.
     * @param message
     * @param cause
     */
    public EventChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for EventChannelException.
     * @param cause
     */
    public EventChannelException(Throwable cause) {
        super(cause);
    }
}
