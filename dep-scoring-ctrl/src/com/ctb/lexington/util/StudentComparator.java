package com.ctb.lexington.util;

/*
 * StudentComparator.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.util.Comparator;

import com.ctb.lexington.data.StudentVO;

/**
 * StudentComparator
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 * @author <a href="mailto:dfuller@ctb.com">Dave Fuller</a>
 * @version
 * $Id$
 */
public class StudentComparator implements Comparator
{
    public int compare( Object o1, Object o2 )
    {
        if ( (o1 instanceof StudentVO)
               && (o2 instanceof StudentVO) )
        {
            StudentVO s1 = (StudentVO)o1;
            StudentVO s2 = (StudentVO)o2;

            if ( (s1.getLastName() == null) || (s2.getLastName() == null)  ||
                 (s1.getFirstName()== null) || (s2.getFirstName()== null) )
            {    //when there is not whole bean populated
                return s1.getStudentId().compareTo( s2.getStudentId() );
            }
            else if ( !s1.getLastName().equals( s2.getLastName() ) )
            {
                return s1.getLastName().compareTo( s2.getLastName() );
            }
            else if ( !s1.getFirstName().equals( s2.getFirstName() ) )
            {
                return s1.getFirstName().compareTo( s2.getFirstName() );
            }
            else
            {
                return s1.getUserName().compareTo( s2.getUserName() );
            }
        }

        else if ( (o1 instanceof StudentVO)
               && (o1 instanceof StudentVO) )
        {
            StudentVO s1 = (StudentVO)o1;
            StudentVO s2 = (StudentVO)o2;

            if ( !s1.getLastName().equals( s2.getLastName() ) )
            {
                return s1.getLastName().compareTo( s2.getLastName() );
            }
            else if ( !s1.getFirstName().equals( s2.getFirstName() ) )
            {
                return s1.getFirstName().compareTo( s2.getFirstName() );
            }
            else
            {
                return s1.getUserName().compareTo( s2.getUserName() );
            }
        }

        else if ( (o1 instanceof StudentVO)
               && (o1 instanceof StudentVO) )
        {
            StudentVO s1 = (StudentVO)o1;
            StudentVO s2 = (StudentVO)o2;

            if ( (s1.getLastName() == null) || (s2.getLastName() == null)  ||
                 (s1.getFirstName()== null) || (s2.getFirstName()== null) )
            {    //when there is not whole bean populated
                return s1.getStudentId().compareTo( s2.getStudentId() );
            }
            else if ( !s1.getLastName().equals( s2.getLastName() ) )
            {
                return s1.getLastName().compareTo( s2.getLastName() );
            }
            else if ( !s1.getFirstName().equals( s2.getFirstName() ) )
            {
                return s1.getFirstName().compareTo( s2.getFirstName() );
            }
            else
            {
                return s1.getStudentId().compareTo( s2.getStudentId() );
            }
        }

        else if ( (o1 instanceof String)
               && (o2 instanceof String) )
        {
            String studName1 = (String)o1;
            String studName2 = (String)o2;

            return studName1.compareTo( studName2 );
        }
        else
            return 0;

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
 * Revision 1.11  2005/05/03 21:25:22  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.9.18.1  2004/03/23 23:02:08  rmbhat
 * imports corrected. there are more on the way.
 *
 * Revision 1.9  2003/01/31 04:04:42  oasuser
 * merged from OASR3tmp to trunk.  Code not compiling, so java will be re-merged at a later date.
 *
 * Revision 1.8.10.3  2003/01/28 23:15:03  sprakash
 * StudentBean --> StudentVO updates
 *
 * Revision 1.8.10.2  2003/01/27 21:01:47  sprakash
 * Removed StudentSearchResultBean
 *
 * Revision 1.8.10.1  2003/01/22 01:47:57  sprakash
 * StudentBean to StudentVO updates and assert keyword fix
 *
 * Revision 1.8  2002/10/25 04:32:00  kgawetsk
 * Added case for checking Id, if the names are not populated.
 *
 * Revision 1.7  2002/10/10 23:50:25  kgawetsk
 * Added StudentVO.
 *
 * Revision 1.6  2002/09/25 02:25:59  kgawetsk
 * Handle String object for generic Name sort.
 *
 * Revision 1.5  2002/09/05 20:27:21  oasuser
 * Clobbered trunk with code from ATSR1 branch.
 *
 * Revision 1.3.4.1  2002/08/22 18:32:02  jshields
 * Fixed control-M problems.
 *
 * Revision 1.3  2002/08/20 22:51:09  oasuser
 * Merged build-R1-20020813-161424
 *
 * Revision 1.2  2002/05/21 18:38:57  oasuser
 * Committing to the trunk
 *
 * Revision 1.1.2.4  2002/05/09 03:28:21  adimayug
 * sorting for StudentVOs
 *
 * Revision 1.1.2.3  2002/05/05 23:29:14  adimayug
 * updated if instace is a StudentLocal to lastly check the studentId
 *
 * Revision 1.1.2.2  2002/05/05 21:52:03  adimayug
 * Added to check instance of object.  Added elseif statement to handle if Student/User Local Interface
 *
 * Revision 1.1.2.1  2002/04/17 21:19:31  dfuller
 * initial check-in
 *
 */
