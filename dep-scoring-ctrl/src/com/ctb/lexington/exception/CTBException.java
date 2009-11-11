package com.ctb.lexington.exception;

// GRNDS imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This exception is the base CTB exception.
 * Standard Java and J2EE exceptions should be used when applicable.
 *
 * @author <a href="mailto:john.m.shields@accenture.com">John M. Shields</a>
 * @version
 * $Id$
 * $Id$
 */
public class CTBException extends Exception
{
    // -- member vars --
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Holds the error code strings. */
    protected List errorCodes = new ArrayList(1);
    /** Holds the error messages. */
    protected HashMap messages = new HashMap(1);
    /** Holds the class names. */
    protected HashMap classpaths = new HashMap(1);
    /** The string used to separate multiple display messages. */
    private String separator = "<br>" + '\n';
    
    // -- static vars --
    
    private static final String TRACE_TAG = "CTBException";
    protected static final String PROP_PREFIX = "errorcode.";
    /** Holds the configuration domain for this class. */
    protected static String domain = "errorcode";
    /** Holds the configuration sub-domains. */
    protected static String[] subDomains = null;
    
    //-- Constructors --//
    
    /**
     * Creates new <code>CTBException</code> without detail message.
     */
    public CTBException()
    {
        this( null, null, null );
    }
    
    /**
     * Creates the exception using the supplied error code.
     */
    public CTBException( String errorCode_ )
    {
        this( errorCode_, null, null );
    }
    
    /**
     * Creates an exception for the supplied error code and uses the supplied
     * message as the default message.
     *
     * @param errorCode_ The error code for this exception.
     * @param msg_ The default message for this exception.
     */
    public CTBException(String errorCode_, String msg_)
    {
        this( errorCode_, msg_, null );
    }
    
    
    
    public CTBException(String errorCode_, String msg_, Throwable exception_)
    {
        super( msg_, exception_ );
        addErrorCode( errorCode_, msg_ );
    }
    
    public CTBException(String[] codes_)
    {
        super();
        final String SUB_TRACE = TRACE_TAG + ".CTBException(String[])";
        
        for( int i=0; i>codes_.length; i++ )
        {
            addErrorCode( codes_[i] );
        }
        
    }
    
    // -- public methods --
    
    /**
     * hasErrors() returns true if there are entries in the ErrorCode List
     *
     * @return boolean
     */
    public boolean hasErrors()
    {
        return ! this.errorCodes.isEmpty();
    }
    
    /**
     * Gets the first error code in the list of error codes for this exception.
     *
     * @return String  The first error code in the list of error codes.
     */
    public String getErrorCode()
    {
      String value = null;

      if( !this.errorCodes.isEmpty() )
        value = (String) this.errorCodes.get(0);

      return value;
    }
    
    /**
     * Gets the error code list for this exception.
     *
     * @return List The error code List.
     */
    public List getErrorCodes()
    {
        return this.errorCodes;
    }
    
    /**
     * Gets the error message for the supplied error code.
     */
    public String getDisplayMessage( String errorCode_ )
    {
        final String SUB_TRACE = TRACE_TAG + ".getDisplayMessage(String)";

        if( this.hasErrors() )
        {
            return (String)this.messages.get( errorCode_ );
        }
        else
        {
            return "";
        }
    }
    
    /**
     * Convenience method for getting the error message for this exception
     * without knowing the error code. This method returns all of the display
     * messages for all contained error codes.
     */
    public String getDisplayMessage()
    {
        final String SUB_TRACE = TRACE_TAG + ".getDisplayMessage()";
        
        // create string buffer
        StringBuffer buf = new StringBuffer();
        
        // only populate if has errors
        if( this.hasErrors() )
        {
            
            // Get first message
            String fm = (String)this.messages.get( this.errorCodes.get( 0 ) );
            
            // append first message
            buf.append( fm );
            
            // Append each error code's display message
            for( int i=1; i < this.errorCodes.size(); i++ )
            {
                buf.append( getSeparator() );
                String msg = (String)this.messages.get(
                                            this.errorCodes.get( i ) );
                buf.append( msg );
            }
        }
        
        return buf.toString();
    }
    
    /**
     * Gets the class path for the supplied error code.
     */
    public String getClassPath( String errorCode_ )
    {
        final String SUB_TRACE = TRACE_TAG + ".getClassPath(String)";

        if( this.hasErrors() )
        {
            return (String)this.classpaths.get( errorCode_ );
        }
        else
        {
            return "";
        }
    }
    
    /**
     * Convenience method for getting the error message for this exception
     * without knowing the error code. This method returns the classes list
     * for all enclosed error codes.
     */
    public String getClassPath()
    {
        final String SUB_TRACE = TRACE_TAG + ".getClassPath()";
        
        // create string buffer
        StringBuffer buf = new StringBuffer();
        
        // only populate if has errors
        if( this.hasErrors() )
        {
            
            // Get first message
            String fm = (String)this.classpaths.get( this.errorCodes.get( 0 ) );
            
            // append first message
            buf.append( fm );
            
            // Append each error code's display message
            for( int i=1; i < this.errorCodes.size(); i++ )
            {
                buf.append( getSeparator() );
                String msg = (String)this.classpaths.get(
                this.errorCodes.get( i ) );
                buf.append( msg );
            }
        }
        
        return buf.toString();
    }
    
    /**
     * Sets the GRNDS configuration domain and sub-domain for this class.
     */
    public void setConfigurationDomain( String domain_, String[] subDomains_ )
    {
        domain = domain_;
        subDomains = subDomains_;
    }
    
    /**
     * Adds an String errorCode to the errorCode List.
     *
     * @param String errorCode
     */
    public final void addErrorCode( String errorCode_ )
    {
        addErrorCode( errorCode_, null );
    }
    
    /**
     * Adds an String errorCode to the errorCode List.
     *
     * @param String errorCode
     */
    public final void addErrorCode( String errorCode_, String msg_ )
    {
        final String SUB_TRACE = TRACE_TAG + ".addErrorCode()";
            
        if( errorCode_ != null && ! errorCode_.equals( "" ) )
        {
            final String configPrefix = PROP_PREFIX + errorCode_;
            
            this.errorCodes.add( errorCode_ );
        }
        else
        {
            putDisplayMessage( null, msg_ );
            this.errorCodes.add( null );
        }
        
    }
    
    // -- private methods --
    
    /**
     * Puts the supplied error message in the hash using the supplied error
     * code as the key.
     */
    private void putDisplayMessage( String errorCode_, String displayMessage_ )
    {
        final String SUB_TRACE = TRACE_TAG + ".putDisplayMessage()";
        
        // error message can be null - use null string though
        if( displayMessage_ == null )
        {
            // set to null string
            displayMessage_ = "";
        }
        
        // put in map
        this.messages.put( errorCode_, displayMessage_ );
        
    }
    
    /**
     * Puts the supplied class path in the hash using the supplied error
     * code as the key.
     */
    private void putClassPath( String errorCode_, String classPath_ )
    {
        final String SUB_TRACE = TRACE_TAG + ".putClassPath()";
        
        // can be null - use null string though
        if( classPath_ == null )
        {
            // set to null string
            classPath_ = "";
        }
        
        // put in map
        this.classpaths.put( errorCode_, classPath_ );
        
    }
    
    /**
     * Sets the string used to separate the display messages from multiple
     * error codes.
     */
    public void setSeparator( String s_ )
    {
        this.separator = s_;
    }
    
    /**
     * Returns the message separator for this exception.
     */
    private String getSeparator()
    {
        return this.separator;
    }
    
    /**
     * Sets the configuration domain.
     */
    public void setConfigDomain( String s_ )
    {
        domain = s_;
    }
    
    /**
     * Sets the configuration domain.
     */
    public void setConfigSubdomains( String[] s_ )
    {
        subDomains = s_;
    }
}