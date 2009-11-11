package com.ctb.lexington.util;

/**
 * A very lightweight timing utility that is togglable by setting a system property.
 *
 * @author mnkamiya
 * @version $Id$
 */
public class Timer {

    public static final String TIMER_TOGGLE_PROPERTY = "com.ctb.lexington.util.Timer";

    private static final String TRUE = "true";
    private static final String LOG_TAG = "Timing: ";

    private long startTime;

    private Timer() {
        startTime = System.currentTimeMillis();
    }

    private long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public static Timer startTimer() {
        if (isEnabled())
            return new Timer();
        else
            return null;
    }

    public static void logElapsed(String message, Timer timer) {
        if (null!=timer) {
            StringBuffer buff = new StringBuffer(LOG_TAG);
            buff.append(message);
            buff.append(timer.elapsedTime());
            System.out.println(buff.toString());
        }
    }

    public static boolean isEnabled() {
        return TRUE.equals(System.getProperty(TIMER_TOGGLE_PROPERTY));
    }
}
