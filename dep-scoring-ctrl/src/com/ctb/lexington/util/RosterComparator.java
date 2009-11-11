package com.ctb.lexington.util;

/*
 * RosterComparator.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.util.Comparator;

import com.ctb.lexington.data.StudentVO;
import com.ctb.lexington.data.RosterVO;

/**
 * @author <a href="mailto:Krys_Gawetski@ctb.com">Krys Gawetski</a>
 * @version
 * $Id$
 */
public class RosterComparator implements Comparator
{
    public int compare( Object o1, Object o2 )
    {
        if ( (o1 instanceof RosterVO) && (o2 instanceof RosterVO) ){
                RosterVO s  = (RosterVO)o1;
                RosterVO s2 = (RosterVO)o2;
                if( s.getStudentId().equals( s2.getStudentId()) ){
                    return s.getOrgNodeId().compareTo( s2.getOrgNodeId() );
                }else{
                    return s.getStudentId().compareTo( s2.getStudentId());
                }
            }
            else 
                if ( (o1 instanceof StudentVO) && (o2 instanceof StudentVO) ){
                    StudentVO s  = (StudentVO)o1;
                    StudentVO s2 = (StudentVO)o2;
                    if( s.getStudentId().equals( s2.getStudentId()) ){
                        return s.getOrgNodeId().compareTo( s2.getOrgNodeId() );
                    }else{
                        return s.getStudentId().compareTo( s2.getStudentId());
                    }
                }        
        return 1; //Not Equal
    }
}
/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:44  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:48:28  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.2  2005/05/03 21:25:22  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.1.2.5  2005/03/04 22:00:15  gawetski
 * Cosmetics.
 *
 * Revision 1.1.2.4  2005/02/15 16:52:44  gawetski
 * fix to equal
 *
 * Revision 1.1.2.3  2005/02/14 18:01:00  gawetski
 * Corrections
 *
 * Revision 1.1.2.2  2005/02/11 21:44:24  gawetski
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2005/02/11 19:20:03  gawetski
 * Student MultiNode Assignments defect fix.
 *
 */