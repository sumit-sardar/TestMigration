package com.ctb.lexington.exception;

/*
 * AssembleNotSupportedException.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */


// GRNDS imports

/**
 * This exception is used to indicate that assemble method is not supported.
 *
 * @author Adrienne Dimayuga
 * @version $Id$
 */
public class AssembleNotSupportedException extends CTBException
{
    //-- Constructors --//
    
    public AssembleNotSupportedException()
    {
        super();
    }
    
    public AssembleNotSupportedException(String errorCode_)
    {
        super( errorCode_ );
    }
    
    public AssembleNotSupportedException(String errorCode_, String msg_)
    {
        super( errorCode_, msg_ );
    }

    public AssembleNotSupportedException(String errorCode_, String msg_, Throwable e_)
    {
        super( errorCode_, msg_, e_ );
    }
        
    public AssembleNotSupportedException(String[] codes_)
    {
        super( codes_ );
    }
    
}

