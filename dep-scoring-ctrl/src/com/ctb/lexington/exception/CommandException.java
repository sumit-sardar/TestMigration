package com.ctb.lexington.exception;


/*
 * CommandException.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */


// GRNDS imports

/**
 * This exception is used to indicated that a system-level error has occured.
 * Standard Java and J2EE exceptions should be used when applicable.
 *
 * @author John M. Shields
 * @version $Id$
 */
public class CommandException extends CTBException
{
    //-- Constructors --//
    
    public CommandException()
    {
        super();
    }
    
    public CommandException(String errorCode_)
    {
        super( errorCode_ );
    }
    
    public CommandException(String errorCode_, String msg_)
    {
        super( errorCode_, msg_ );
    }

    public CommandException(String errorCode_, String msg_, Throwable e_)
    {
        super( errorCode_, msg_, e_ );
    }
        
    public CommandException(String[] codes_)
    {
        super( codes_ );
    }
    
}


