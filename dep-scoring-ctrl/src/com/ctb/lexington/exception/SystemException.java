package com.ctb.lexington.exception;

public class SystemException extends RuntimeException {
    public SystemException() {
        super();
    }

    public SystemException(String msg) {
        super(msg);
    }

    public SystemException(String msg, Throwable cause) {
        super(msg, cause);
    }
}