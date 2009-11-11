package com.ctb.lexington.util;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import com.ctb.lexington.data.TestAdminVO;
//import com.ctb.lexington.ejb.entity.StudentLocal;

/**
 * TestAdminComparator
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 * @author <a href="mailto:dfuller@ctb.com">Dave Fuller</a>
 * @version
 * $Id$
 */
public class TestAdminComparator implements Comparator
{

    public int compare( Object o1, Object o2 )
    {
        TestAdminVO tab1 = (TestAdminVO)o1;
        TestAdminVO tab2 = (TestAdminVO)o2;
        Calendar c1 = tab1.getLoginStartDate();
        Date d1 = c1.getTime();
        Calendar c2 = tab2.getLoginStartDate();
        Date d2 = c2.getTime();
        return d1.compareTo( d2 );
    }

}

/* $Log$
/* Revision 1.1  2007/01/30 01:31:46  ncohen
/* port scoring to 4.x platform
/*
/* Revision 1.1  2006/02/23 20:48:28  ncohen
/* create new module for legacy OAS app
/*
/* Revision 1.7  2005/05/03 21:25:18  ncohen
/* replace HEAD with iknow-millbarge
/*
/* Revision 1.6.22.1  2004/08/17 22:02:09  binkley
/* Globally organize imports (removed 1,000+ warnings).
/*
/* Revision 1.6  2003/01/31 04:04:44  oasuser
/* merged from OASR3tmp to trunk.  Code not compiling, so java will be re-merged at a later date.
/*
/* Revision 1.5.14.1  2003/01/23 19:52:39  jshields
/* TestAdministration refactoring.
/* JMS 2003-01-23 11:51
/*
/* Revision 1.5  2002/09/05 20:27:21  oasuser
/* Clobbered trunk with code from ATSR1 branch.
/*
/* Revision 1.3.4.1  2002/08/22 18:32:02  jshields
/* Fixed control-M problems.
/*
/* Revision 1.3  2002/08/20 22:51:09  oasuser
/* Merged build-R1-20020813-161424
/*
/* Revision 1.2  2002/05/21 18:38:57  oasuser
/* Committing to the trunk
/*
/* Revision 1.1.2.3  2002/05/10 19:26:24  dfuller
/* fixed a problem where wrong field was being used for comparison
/*
/* Revision 1.1.2.2  2002/05/09 18:26:18  dfuller
/* added some handling for null values
/*
/* Revision 1.1.2.1  2002/05/08 23:27:23  dfuller
/* initial check-in
/*
 */
