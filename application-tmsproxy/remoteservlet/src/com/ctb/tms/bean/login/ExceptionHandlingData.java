/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctb.tms.bean.login;

import com.ctb.tms.nosql.coherence.ExceptionStore;
import com.ctb.tms.nosql.coherence.ExceptionStoreClient;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class is used as the data class for the exception cache. It contains the
 * exception, the data causing the exception, the number of tries this data can
 * be sent and the lastRetry it was sent. It also contains the cache name used
 * to resend the data to it.
 *
 * @author McGraw Hill
 * @since 2.0.3
 * @see ExceptionStore
 * @see ExceptionStoreClient
 */
public class ExceptionHandlingData implements Comparable {

    /**
     * Exception that cause this to happen.
     */
    public Exception ex;
    /**
     * Application data value that caused the exception.
     */
    public Object value;
    /**
     * Key of the application data.
     */
    public Object key;
    /**
     * Number of retries left on this object. Defaults to 3.
     */
    public int retryCount = 3;
    /**
     * Name of the originating cache.
     */
    public String cachename;
    /**
     * time stamp in millisecond of when this was last tried. This is used to
     * respect the inter-request delay.
     */
    public long lastRetry = 0L;

    /**
     * Default constructor. Uses the system property tms.exceptioncache.retries
     * with a default of 3.
     */
    public ExceptionHandlingData() {
        String tries = System.getProperty("tms.exceptioncache.retries", "3");
        Integer i = new Integer(tries);
        this.retryCount = i;
    }

    @Override
    /**
     * Used for the time ordering of the entries, while doing the resubmitting
     *
     * @see ExceptionStore.storeall
     */
    public int compareTo(Object o) {
        ExceptionHandlingData other = (ExceptionHandlingData) (o);
        if (this.lastRetry == other.lastRetry) {
            return 0;
        }
        if (this.lastRetry > other.lastRetry) {
            return 1;
        }
        return -1;

    }

    @Override
    /**
     * This method is used for tracing in the log file. This can be changed, but
     * mostly, it's the value.toString() of the application data that must be
     * specific to what is required.
     */
    public String toString() {
        StringWriter errors = null;
        if (ex != null) {
            errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        }

        String ret = "<data>\n";
        if (key != null) {
            ret += "<Key>" + this.key.toString() + "</Key>\n";
        }
        if (value != null) {
            ret += "<Value>" + this.value.toString() + "</Value>\n";
        }
        if (ex != null) {
            ret += "<Exception>\n";
            ret += "<msg>" + this.ex.getLocalizedMessage() + "</msg>\n";
            ret += "<stack>" + errors.toString() + "<stack>\n";
            ret += "</Exception>";
        }
        ret += "</data>\n";

        return ret;
    }

    public void decRetries() {
        this.retryCount--;
        //System.out.println("Decrementing the retry count for "+key+" to "+this.retryCount);
    }
}
