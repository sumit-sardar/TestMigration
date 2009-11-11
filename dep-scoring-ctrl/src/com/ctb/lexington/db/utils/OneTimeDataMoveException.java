package com.ctb.lexington.db.utils;


public class OneTimeDataMoveException extends Exception {

    public OneTimeDataMoveException(String string) {
        super(string);
    }

    public OneTimeDataMoveException(String msg, Exception e){
    	super(msg, e);
    }
}
