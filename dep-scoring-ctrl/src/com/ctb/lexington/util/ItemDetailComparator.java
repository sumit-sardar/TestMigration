package com.ctb.lexington.util;

import java.util.Comparator;

import com.ctb.lexington.data.ItemDetailVO;

/**
 * StudentComparator
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 * @author rmariott
 * @version
 * $Id: ItemDetailComparator.java
 */
public class ItemDetailComparator implements Comparator
{
    private static final String TRACE_TAG = "com.ctb.lexington.util.ItemDetailComparator";

    public ItemDetailComparator( ) {

    }

    public int compare( Object o1, Object o2 )
    {
        // Compare on order, then isDirty, then group order (when applicable),
        // then itemId.

        ItemDetailVO i1 = (ItemDetailVO)o1;
        ItemDetailVO i2 = (ItemDetailVO)o2;

        // Ordering diagnosis message EXAMPLE:
        // "Comparing item 1: 3/(2)/true/null/3C.2.31.2 with item 2: 2/(2)/false/null/5R.23.1.f"
        
        // ORDERBY order number
        if ( i1.getOrderNumber().intValue() != i2.getOrderNumber().intValue() )
        {
            // Order numbers were different, order accordingly.
            ////GrndsTrace.enterScope(TRACE_TAG + "Ordering according to OrderNumber (first).");
            ////GrndsTrace.exitScope( i1.getOrderNumber().compareTo( i2.getOrderNumber() ) );
            return i1.getOrderNumber().compareTo( i2.getOrderNumber() );
        }
        // ORDERBY dirty value
        else if ( !i1.getIsDirty().equals( i2.getIsDirty() ) )
        {
            // Both items have the same orderNumber, but only one of the
            // items was changed (is dirty), order accordingly.  "true" comes
            // after "false", so reverse the order to give "true" precedence.
            //GrndsTrace.enterScope(TRACE_TAG + "Ordering according to isDirtyValue (second).");

            // Determine if the first object (i1) is moving up or down.  Moving "up"
            // means from a higher number to a lower number (from 3 to 1); up on wksht.
            boolean i1MovingUp = true;
            if ( i1.getPreviousOrderNumber().intValue() < i1.getOrderNumber().intValue() )
            {
                // Item is moving down (e.g. from 1 to 3).
                i1MovingUp = false;
            }

            // Determine if the second object (i2) is moving up or down.  Moving "up"
            // means from a higher number to a lower number (from 3 to 1); up on wksht.
            boolean i2MovingUp = true;
            if ( i2.getPreviousOrderNumber().intValue() < i2.getOrderNumber().intValue() )
            {
                // Item is moving down (e.g. from 1 to 3).
                i2MovingUp = false;
            }

            // Check to see if i1 has changed, or...
            if ( i1.getIsDirty().equals("true") )
            {
                if (i1MovingUp)
                {
                    // Moving up (e.g. 3 to 1), order accordingly
                    //GrndsTrace.exitScope( "i1 moving up..." + -(i1.getIsDirty().compareTo( i2.getIsDirty() )) );
                    return -(i1.getIsDirty().compareTo( i2.getIsDirty() ));
                }
                else
                {
                    // Moving down (e.g. 1 to 3), reverse the normal ordering
                    //GrndsTrace.exitScope( "i1 moving down..." + i1.getIsDirty().compareTo( i2.getIsDirty() ) );
                    return i1.getIsDirty().compareTo( i2.getIsDirty() );
                }
            }
            else
            {
                // ...i2 has changed
                if (i2MovingUp)
                {
                    // Moving up (e.g. 3 to 1), order accordingly
                    //GrndsTrace.exitScope( "i2 moving up..." + -(i1.getIsDirty().compareTo( i2.getIsDirty() )) );
                    return -(i1.getIsDirty().compareTo( i2.getIsDirty() ));
                }
                else
                {
                    // Moving down (e.g. 1 to 3), reverse the normal ordering
                    //GrndsTrace.exitScope( "i2 moving down..." + i1.getIsDirty().compareTo( i2.getIsDirty() ) );
                    return i1.getIsDirty().compareTo( i2.getIsDirty() );
                }
            }
        }
        // ORDERBY group order number (but only when both items are in the same group)
        else if ( (i1.getExtStimulusId() != null && i2.getExtStimulusId() != null) &&
                  (i1.getExtStimulusId().equals(i2.getExtStimulusId())) &&
                  (i1.getGroupOrderNumber().intValue() != i2.getGroupOrderNumber().intValue()) )
        {
            // Both items have the same orderNumber and dirty value and are in the same
            // group, check to see if we're ordering a group item that has a
            // groupOrderNumber which needs to be preserved.
            //GrndsTrace.enterScope(TRACE_TAG + "Ordering according to groupOrderNumber (third).");
            //GrndsTrace.exitScope( i1.getGroupOrderNumber().compareTo( i2.getGroupOrderNumber() ) );
            return i1.getGroupOrderNumber().compareTo( i2.getGroupOrderNumber() );
        }
        // ORDERBY group (stimulus) ID
        else if ( (i1.getExtStimulusId() != null && i2.getExtStimulusId() != null) && 
                  (!i1.getExtStimulusId().equals(i2.getExtStimulusId())) )
        {
            // Both items are part of a group, try ordering by group (stimulus) ID before
            // resorting to item ID ordering.
            //GrndsTrace.enterScope(TRACE_TAG + "Ordering according to stimulusID (fourth).");
            //GrndsTrace.exitScope( i1.getExtStimulusId().compareTo( i2.getExtStimulusId() ) );
            return i1.getExtStimulusId().compareTo( i2.getExtStimulusId() );
        }
        // ORDERBY item ID
        else
        {
            // The orderNumber of the two items was the same, and both items
            // had their values changed.  Order by String ItemId.
            //GrndsTrace.enterScope(TRACE_TAG + "Ordering according to itemId (fifth).");
            //GrndsTrace.exitScope( i1.getItemId().compareTo( i2.getItemId() ) );
            return i1.getItemId().compareTo( i2.getItemId() );
        }
    }
}
/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:45  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:48:29  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.14  2005/05/03 21:25:23  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.12.12.1.2.1  2004/10/05 12:59:04  vraravam
 * Removed unnecessary Exception thrown and being handled. Removed unused imports. Fixed deprecation warnings.
 *
 * Revision 1.12.12.1  2004/03/23 23:02:10  rmbhat
 * imports corrected. there are more on the way.
 *
 * Revision 1.12  2003/02/25 03:07:01  oasuser
 * merged from actuate6upgrade to trunk
 *
 * Revision 1.11.2.1  2003/02/25 02:39:58  oasuser
 * Merged from OASR3tmp to actuate6upgrade
 *
 * Revision 1.11  2003/01/31 04:04:39  oasuser
 * merged from OASR3tmp to trunk.  Code not compiling, so java will be re-merged at a later date.
 *
 * Revision 1.10.8.3  2003/02/25 00:38:41  oasuser
 * Merged R0221 to OASR3tmp.
 *
 * Revision 1.10.8.2  2003/01/23 20:15:07  kgawetsk
 * R2.3 - refactoring for ItemDetailVO.
 *
 */
