package com.ctb.lexington.exception;

/**
 * This exception is used to indicated that an authentication attempt failed.
 * Standard Java and J2EE exceptions should be used when applicable.
 *
 * @author <a href="mailto:john.m.shields@accenture.com">John M. Shields</a>
 * @version $Id$
 */
public class CTBAuthenticationException extends CTBException
{
    //-- Constructors --//

    /**
     * Creates new <code>CTBArchitectureException</code> without detail
     * message.
     */
    public CTBAuthenticationException()
    {
        super();
    }

    /**
     * Creates a new CTBAuthenticationException object.
     *
     * @param errorCode_ DOCUMENT ME!
     */
    public CTBAuthenticationException(String errorCode_)
    {
        super(errorCode_);
    }

    /**
     * Creates a new CTBAuthenticationException object.
     *
     * @param errorCode_ DOCUMENT ME!
     * @param msg_ DOCUMENT ME!
     */
    public CTBAuthenticationException(String errorCode_, String msg_)
    {
        super(errorCode_, msg_);
    }

    /**
     * Creates a new CTBAuthenticationException object.
     *
     * @param errorCode_ DOCUMENT ME!
     * @param msg_ DOCUMENT ME!
     * @param exception_ DOCUMENT ME!
     */
    public CTBAuthenticationException(String errorCode_, String msg_,
                                      Throwable exception_)
    {
        super(errorCode_, msg_, exception_);
    }

    /**
     * Creates a new CTBAuthenticationException object.
     *
     * @param codes_ DOCUMENT ME!
     */
    public CTBAuthenticationException(String[] codes_)
    {
        super(codes_);
    }
}
