/*
 * ArgumentParserException.java
 *
 * Created on August 26, 2002, 9:27 AM
 */
package com.ctb.lexington.exception;

/**
 * @author knevarez
 * @version
 */
public class ArgumentParserException extends java.lang.Exception
{
    /**
     * Creates new <code>ArgumentParserException</code> without detail message.
     */
    public ArgumentParserException() {}

    /**
     * Constructs an <code>ArgumentParserException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ArgumentParserException(String msg)
    {
        super(msg);
    }
}
