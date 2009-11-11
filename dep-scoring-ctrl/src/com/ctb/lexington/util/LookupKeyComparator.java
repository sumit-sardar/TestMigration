package com.ctb.lexington.util;

import java.util.Comparator;

import com.ctb.lexington.data.LookupVO;

/**
 * LookupKeyComparator
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 * @author <a href="mailto:dfuller@ctb.com">Dave Fuller</a>
 * @version
 * $Id$
 */
public class LookupKeyComparator implements Comparator
{

    public int compare( Object o1, Object o2 )
    {
        LookupVO b1 = (LookupVO)o1;
        LookupVO b2 = (LookupVO)o2;

        return b1.getCode().compareTo( b2.getCode() );
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
 * Revision 1.7  2005/05/03 21:25:19  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.6.22.1  2004/08/17 22:02:09  binkley
 * Globally organize imports (removed 1,000+ warnings).
 *
 * Revision 1.6  2003/02/08 01:44:53  oasuser
 * merged OASR3tmp to trunk.
 *
 * Revision 1.5.14.1  2003/02/06 01:16:15  jshields
 * Moved classes to data and bean packages.
 * Removed deprecated bean classes.
 * Minor updates to SpecialCodeManager.
 * Other minor fixes.
 *
 * Revision 1.5  2002/09/05 20:27:20  oasuser
 * Clobbered trunk with code from ATSR1 branch.
 *
 * Revision 1.3.4.1  2002/08/22 18:32:00  jshields
 * Fixed control-M problems.
 *
 * Revision 1.3  2002/08/20 22:51:08  oasuser
 * Merged build-R1-20020813-161424
 *
 * Revision 1.2  2002/05/21 18:38:53  oasuser
 * Committing to the trunk
 *
 * Revision 1.1.2.1  2002/04/25 06:16:58  dfuller
 * initial check-in
 *
 * Revision 1.1.2.1  2002/04/17 21:19:31  dfuller
 * initial check-in
 *
 */
