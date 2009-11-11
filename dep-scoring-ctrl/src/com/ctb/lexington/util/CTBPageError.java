package com.ctb.lexington.util;


/*
 * CTBPageError.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */


//import java classes
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <code>CTBPageError</code> provides parameters and methods for setting form error messages to
 * be displayed to the user.
 * <p>
 * <p>
 * @version:    1.00
 *
 * @author <a href="mailto:mary.g.phelps@accenture.com">Mary G. Phelps</a>, Feb 04, 2002
 *
 * <PRE>
 * <U>Updated by:</U>
 * <U>Description:</U>
 * </PRE>
 */
public class CTBPageError {

    /** 
     * The errorMessages List will contain all errors associated the form.
     */
    private List errorMessage = new ArrayList(1);

    /** Constructor */
    public CTBPageError() {
    }
    
    /**
     * The getErrorMessage method returns a String containing the concatenation
     * of all page errors. 
     *
     * @return String 
     */
    public String getErrorMessage()
    {
       String errors = null;
       Iterator it = errorMessage.iterator();
       while (it.hasNext())
       {
           if (errors==null)
               errors=(String)it.next();
           else 
               errors=errors + "<BR>" + (String)it.next();
       }
       return errors;
    }
    
    /**
     * The getErrorMessages method returns a List of error messages for the Page.
     *
     * @return List
     */
    public List getErrorMessages()
    {
        return this.errorMessage;
    }
    
    
    /**
     * The setErrorMessage method sets an error message within the errorMessage
     * List.
     *
     * @param String msg_
     */
    
    public void setErrorMessage(String msg_)
    {
       this.errorMessage.add(msg_);
    } 
    
    /*
     * The hasErrors method will return true if there are errors in the page
     * and false if there are no errors.
     *
     *@return boolean 
     */
    public boolean hasErrors()
    {
        return ! this.errorMessage.isEmpty();
    }
        
    /**
     * The resetErrorMessage method empties the error list.
     */
    public void resetErrorMessage()
    {
        this.errorMessage.clear();
    }
        
}
