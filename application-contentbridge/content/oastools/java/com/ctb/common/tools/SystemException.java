package com.ctb.common.tools;


import java.io.*;


public class SystemException extends RuntimeException {

    private Throwable _cause;
    private String _stackTrace;

    public SystemException() {
        super();
    }

    public SystemException(String msg) {
        super(msg);
    }

    public SystemException(String msg, Throwable cause) {
        super(msg);
        _cause = cause;
    }
    
/*    public StackTraceElement[] getStackTrace()
    {
        return super.getStackTrace();
    }
*/
    private void getStackTraceString() {
        if (_stackTrace != null) {
            return;
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printer = new PrintWriter(stringWriter);

        super.printStackTrace(printer);
        if (_cause != null) {
            printer.println("Cause: ");
            _cause.printStackTrace(printer);
        }
        printer.flush();
        printer.close();
        _stackTrace = stringWriter.getBuffer().toString();
    }

    public Throwable getCause() {
        return _cause;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     *	Prints the stack trace to the specified output stream, as well as the stack trace of
     *	any nested exceptions.
     */
    public void printStackTrace(PrintStream out) {
        getStackTraceString();
        try {
            out.write(_stackTrace.getBytes());
        } catch (IOException e) {
            throw new SystemException("Failed to write stack trace to stream", e);
        }
    }

    /**
     *	Prints the stack trace to the specified output stream, as well as the stack trace of
     *	any nested exceptions.
     */
    public void printStackTrace(PrintWriter out) {
        getStackTraceString();
        out.write(_stackTrace);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        getStackTraceString();
        s.defaultWriteObject();
    }
}
