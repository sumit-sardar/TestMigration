package com.ctb.lexington.util;

import java.util.Comparator;

import com.ctb.lexington.data.ItemSetVO;
import com.ctb.lexington.valueobject.FilterVO;


/**
 * ItemSetComparator Copyright CTB/McGraw-Hill, 2002 CONFIDENTIAL
 *
 * @author Adrienne J. Dimayuga
 * @version $Id$
 */
public class ItemSetComparator implements Comparator
{
    /**
     * DOCUMENT ME!
     *
     * @param o1 DOCUMENT ME!
     * @param o2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean compareObjCode = false;

    public static String toComparableString( String src )
    {
    	String result = "";
    	if(src != null){
	        StringBuffer dest = new StringBuffer();
	        int i, number = -1;
	        int size = src.length();
	        char ch, chNumber;
	        for ( i = 0; i < size; i++ )
	        {
	            ch = src.charAt( i );
	            if ( Character.isDigit( ch ) == true )
	            {
	                if ( number == -1 )
	                {
	                    number = ch - '0';
	                }
	                else
	                {
	                    number = number * 10 + ( ch - '0' );
	                }
	            }
	            else
	            {
	                if ( number != -1 )
	                {
	                    chNumber = ( char )( 'A' + number );
	                    dest.append( chNumber );
	                    number = -1;
	                }
	                dest.append( ch );
	            }
	        }
	        if ( number != -1 )
	        {
	            chNumber = ( char )( 'A' + number );
	            dest.append( chNumber );
	        }
	        result = dest.toString();
    	}
    	return result;
    }

    public int compare(Object o1, Object o2)
    {
        if ((o1 instanceof ItemSetVO) && (o2 instanceof ItemSetVO))
        {
            ItemSetVO s1 = (ItemSetVO)o1;
            ItemSetVO s2 = (ItemSetVO)o2;
            if ( compareObjCode )
            {
                String string1 = s1.getExtCmsItemSetId();
                String string2 = s2.getExtCmsItemSetId();
                if ( string1 != null && string2 != null )
                {
                    string1 = toComparableString( string1 );
                    string2 = toComparableString( string2 );
                    if ( !string1.equals( string2 ))
                    {
                        return string1.compareTo( string2 );
                    }
                    else
                        return s1.getItemSetDisplayName().compareTo(s2.getItemSetDisplayName());
                }
                else
                {
                    string1 = s1.getItemSetDisplayName();
                    string2 = s2.getItemSetDisplayName();
                    string1 = toComparableString( string1 );
                    string2 = toComparableString( string2 );
                    return string1.compareTo( string2 );
                }
            }
            else
            {
                String string1 = s1.getItemSetDisplayName();
                String string2 = s2.getItemSetDisplayName();
                string1 = toComparableString( string1 );
                string2 = toComparableString( string2 );
                if ( !string1.equals( string2 ))
                {
                    return string1.compareTo( string2 );
                }
/*                if (!s1.getItemSetName().equals(s2.getItemSetName()))
                {
                    return s1.getItemSetName().compareTo(s2.getItemSetName());
                } */
                else
                {
                    return s1.getItemSetId().compareTo(s2.getItemSetId());
                }
            }
        }

        else if ((o1 instanceof FilterVO) && (o2 instanceof FilterVO))
        {
            FilterVO s1 = (FilterVO)o1;
            FilterVO s2 = (FilterVO)o2;

            if (!s1.getFilterName().equals(s2.getFilterName()))
            {
                return s1.getFilterName().compareTo(s2.getFilterName());
            }
            else
            {
                return 0;
            }
        }

        else
        {
            return 0;
        }
    }
}


/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:45  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:48:28  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.7  2005/08/02 22:08:59  ncohen
 * replace trunk with iknow-whalphin
 *
 * Revision 1.6.4.1  2005/06/28 17:51:39  jbecker
 * refactored, new tests
 *
 * Revision 1.6  2005/05/03 21:25:22  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.5.12.1  2004/08/17 22:02:09  binkley
 * Globally organize imports (removed 1,000+ warnings).
 *
 * Revision 1.5  2003/08/25 22:54:27  ggennaro
 * Merging code from iknowR3v2 to the trunk.
 *
 * Revision 1.4  2003/07/15 17:06:03  wchang
 * Handle sorting of number in string
 *
 * Revision 1.3  2003/07/14 23:30:35  wchang
 * Add comparison for extCMSItemSetId
 *
 * Revision 1.2.10.1  2003/08/13 23:44:17  wchang
 * Sorting objective
 *
 * Revision 1.2  2003/01/31 04:04:39  oasuser
 * merged from OASR3tmp to trunk.  Code not compiling, so java will be re-merged at a later date.
 *
 * Revision 1.1.10.2  2003/01/29 01:45:49  wchang
 * no message
 *
 * Revision 1.1.10.1  2003/01/23 19:52:36  jshields
 * TestAdministration refactoring.
 * JMS 2003-01-23 11:51
 *
 * Revision 1.1  2002/10/07 01:36:40  adimayug
 * creation
 *
 *
 */
