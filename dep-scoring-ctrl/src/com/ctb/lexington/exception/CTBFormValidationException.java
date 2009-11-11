package com.ctb.lexington.exception;


/*
 * CTBFormValidationException.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

// GRNDS imports


/**
 * This exception is used to indicated that a form error has occured. Standard
 * Java and J2EE exceptions should be used when applicable.
 *
 * @author <a href="mailto:john.m.shields@accenture.com">John M. Shields</a>
 * @version $Id$
 */
public class CTBFormValidationException extends CTBException
{
    //-- Constructors --//
    public CTBFormValidationException()
    {
        super();
    }

    /**
     * Creates a new CTBFormValidationException object.
     *
     * @param errorCode_ DOCUMENT ME!
     */
    public CTBFormValidationException(String errorCode_)
    {
        super(errorCode_);
    }

    /**
     * Creates a new CTBFormValidationException object.
     *
     * @param errorCode_ DOCUMENT ME!
     * @param msg_ DOCUMENT ME!
     */
    public CTBFormValidationException(String errorCode_, String msg_)
    {
        super(errorCode_, msg_);
    }

    /**
     * Creates a new CTBFormValidationException object.
     *
     * @param errorCode_ DOCUMENT ME!
     * @param msg_ DOCUMENT ME!
     * @param e_ DOCUMENT ME!
     */
    public CTBFormValidationException(String errorCode_, String msg_,
                                      Throwable e_)
    {
        super(errorCode_, msg_, e_);
    }

    /**
     * Creates a new CTBFormValidationException object.
     *
     * @param codes_ DOCUMENT ME!
     */
    public CTBFormValidationException(String[] codes_)
    {
        super(codes_);
    }
}
