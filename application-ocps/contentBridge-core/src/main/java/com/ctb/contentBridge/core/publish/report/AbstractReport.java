package com.ctb.contentBridge.core.publish.report;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;

public abstract class AbstractReport implements Report {
    private boolean success;
    private Exception exception;
    private Date startTime;
    private String threadId;

    public AbstractReport() {
        this.threadId = Thread.currentThread().getName();
        this.startTime = new Date();
    }

    public Exception getException() {
        return exception;
    }

    public Date getStartTime() {
        return startTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setException(Exception exception) {
    	setSuccess(false);
        this.exception = exception;
    }

    public void setSuccess(boolean b) {
        success = b;
    }

    public String toString(boolean isSubReport) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        FormatterFactory.create(this).print(
            new PrintWriter(new PrintStream(o), true),
            isSubReport);
        return o.toString();
    }

    public String toString() {
        return this.toString(false);
    }

}
