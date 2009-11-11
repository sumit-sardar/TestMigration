package com.ctb.lexington.exception;


/*
 * CTBSystemException.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

/**
 * This exception is used to indicated that a system-level error has occured.
 * Standard Java and J2EE exceptions should be used when applicable.
 *
 * @author <a href="mailto:john.m.shields@accenture.com">John M. Shields</a>
 * @version $Id$
 */
public class CTBSystemException extends CTBException
{
    //-- Constructors --//
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CTBSystemException()
    {
        super();
    }
    
    public CTBSystemException( String errorCode_ )
    {
        super( errorCode_ );
    }
    
    public CTBSystemException( String errorCode_, String msg_ )
    {
        super( errorCode_, msg_ );
    }

    public CTBSystemException( String errorCode_, String msg_, Throwable e_ )
    {
        super( errorCode_, msg_, e_ );
    }
        
    public CTBSystemException( String [] codes_ )
    {
        super( codes_ );
    }
    
}


