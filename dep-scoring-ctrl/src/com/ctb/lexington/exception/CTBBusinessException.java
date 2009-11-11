package com.ctb.lexington.exception;



/**
 * This exception is used to indicated that a business-level error has occured.
 * Standard Java and J2EE exceptions should be used when applicable.
 *
 * @author <a href="mailto:john.m.shields@accenture.com">John M. Shields</a>
 * @version $Id$
 */
public class CTBBusinessException extends CTBException
{
    //-- Constructors --//
    public CTBBusinessException()
    {
        super();
    }

    /**
     * Creates a new CTBBusinessException object.
     *
     * @param errorCode_ DOCUMENT ME!
     */
    public CTBBusinessException(String errorCode_)
    {
        super(errorCode_);
    }

    /**
     * Creates a new CTBBusinessException object.
     *
     * @param errorCode_ DOCUMENT ME!
     * @param msg_ DOCUMENT ME!
     */
    public CTBBusinessException(String errorCode_, String msg_)
    {
        super(errorCode_, msg_);
    }

    /**
     * Creates a new CTBBusinessException object.
     *
     * @param errorCode_ DOCUMENT ME!
     * @param msg_ DOCUMENT ME!
     * @param exception_ DOCUMENT ME!
     */
    public CTBBusinessException(String errorCode_, String msg_,
                                Throwable exception_)
    {
        super(errorCode_, msg_, exception_);
    }

    /**
     * Creates a new CTBBusinessException object.
     *
     * @param codes_ DOCUMENT ME!
     */
    public CTBBusinessException(String[] codes_)
    {
        super(codes_);
    }
}
