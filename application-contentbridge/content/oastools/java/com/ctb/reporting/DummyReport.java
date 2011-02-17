package com.ctb.reporting;

public class DummyReport implements Report {

    private final String message;
    
    public DummyReport(String message) {
    	this.message = message;
    }
    
    public boolean isSuccess() {
        return true;
    }

    public String toString(boolean isSubReport) {
        return message;
    }
    
    public String getMessage() {
    	return message;
    }
}
