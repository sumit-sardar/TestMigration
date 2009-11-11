/*
 * Created on Jul 22, 2004 Channel.java
 */
package com.ctb.lexington.domain.score.event.common;

/**
 * @author nate_cohen
 */
public interface Channel {

    public void send(Event event);

    /***********************************************************************************************
     * @param recipient
     * @param event An arbitrary instance of the specific event type you want to subscribe to on
     *            this channel
     */
    public void subscribe(EventRecipient recipient, Class eventClass);
}