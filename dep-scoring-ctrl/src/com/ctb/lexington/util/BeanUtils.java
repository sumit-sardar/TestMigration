package com.ctb.lexington.util;

/*
 * OrganizationManagerEJB.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

// -- java imports --
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 * @author  John M. Shields
 * @version
 * $Id$
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils
{
    // -- static members --
    private static final String TRACE_TAG = "BeanUtils";
    
    /**
     * Determines if any property of a bean set, i.e., not null or an
     * empty string.
     */
    public static boolean areAnyPropertiesSet( Object bean_ )
    throws IllegalAccessException, InvocationTargetException,
           NoSuchMethodException
    {
        final String SUB_TRACE = TRACE_TAG + ".areAnyPropertiesSet()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        // init result
        boolean result = false;
        
        // Get the property map
        Map properties = PropertyUtils.describe( bean_ );
        
        // Remove "class", it's added by describe
        properties.remove( "class" );
        
        // loop checking if any are not empty
        Iterator it = properties.values().iterator();
        while( it.hasNext() )
        {
            Object value = it.next();
            // value is not empty or null
            if( value != null && ! value.toString().equals( "" ) )
            {
                // value is not an empty string or null
                result = true;
                //GrndsTrace.msg( SUB_TRACE, 9, "found non-empty or null: "
                  //              + value );
                break;
            }
        }
        
        //GrndsTrace.exitScope( result );
        return result;
    }
    
    /**
     * Determines if all of the properties of a bean evalutate to an empty
     * <code>String</code>.
     */
    public static boolean areAllPropertiesEmpty( Object bean_ )
    throws IllegalAccessException, InvocationTargetException,
           NoSuchMethodException
    {
        final String SUB_TRACE = TRACE_TAG + ".areAllPropertiesEmpty()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        // init result
        boolean result = true;
        
        // Get the property map
        Map properties = PropertyUtils.describe( bean_ );
        
        // Remove "class", it's added by describe
        properties.remove( "class" );
        
        try
        {
            // loop checking if any are not empty
            Iterator it = properties.values().iterator();
            while( it.hasNext() )
            {
                Object value = it.next();
                // value is not empty
                if( ! value.toString().equals( "" ) )
                {
                    // value is not an empty string
                    result = false;
                    //GrndsTrace.msg( SUB_TRACE, 9, "found non-empty: "+value );
                    break;
                }
            }
        }
        catch( NullPointerException npe )
        {
            // a value is not empty, it's null or properties are null
            // either case they are not ""
            //GrndsTrace.msg( SUB_TRACE, 9, "NullPointerException" );
            result = false;
        }
        
        //GrndsTrace.exitScope( result );
        return result;
        
    }

    /**
     * Determines if any of the properties of a bean are null.
     */
    public static boolean areAnyPropertiesNull( Object bean_ )
    throws IllegalAccessException, InvocationTargetException,
           NoSuchMethodException
    {
        final String SUB_TRACE = TRACE_TAG + ".areAnyPropertiesNull()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        // init result
        boolean result = false;
        
        // Get the property map
        Map properties = PropertyUtils.describe( bean_ );
        
        // Remove "class", it's added by describe
        properties.remove( "class" );
        
        // loop checking if any null
        Iterator it = properties.values().iterator();
        while( it.hasNext() )
        {
            Object value = it.next();
            // value is not empty
            if( value == null )
            {
                // value is null
                result = true;
                //GrndsTrace.msg( SUB_TRACE, 9, "found null value" );
                break;
            }
        }
        
        //GrndsTrace.exitScope( result );
        return result;
        
    }

    /**
     * Returns a <code>Map</code> of properties whose value is null.
     */
    public static Map getNullProperties( Object bean_ )
    throws IllegalAccessException, InvocationTargetException,
           NoSuchMethodException
    {
        final String SUB_TRACE = TRACE_TAG + ".getNullProperties()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        // Get the property map
        Map properties = PropertyUtils.describe( bean_ );
        
        // Remove "class", it's added by describe
        properties.remove( "class" );
        
        // loop checking if any null
        Iterator it = properties.values().iterator();
        while( it.hasNext() )
        {
            Object value = it.next();
            // value is not empty
            if( value != null )
            {
                // remove from map since not null
                //GrndsTrace.msg( SUB_TRACE, 9, "Removing value: " + value );
                it.remove();
            }
        }
        
        //GrndsTrace.exitScope( properties );
        return properties;
        
    }
    
    /**
     * Prints the name/value pairs of the submitted bean in the
     * //GrndsTrace file.
     */
    public static void traceBean( String traceTag_,
                                  int traceLevel_,
                                  Object bean_ )
    {
        if( bean_ != null )
        {
            try
            {
                HashMap desc = (HashMap)BeanUtils.describe( bean_ );
                //GrndsTrace.msg( traceTag_, traceLevel_,
               //                 "Bean Trace: " + desc.toString() );
            }
            catch( Exception e )
            {
                //GrndsTrace.msg( traceTag_, traceLevel_,
                    //            "Error tracing bean: " + e.getMessage() );
            }
        }
        else
        {
            //GrndsTrace.msg(traceTag_, traceLevel_, "Bean Trace: null bean" );
        }
    }
    
}
