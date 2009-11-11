package com.ctb.lexington.util;

import com.ctb.lexington.data.AnswerChoiceDetailVO;

/*
 * AnswerChoiceComparator.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */
import java.util.*;


/**
 * AnswerChoiceComparator Copyright CTB/McGraw-Hill, 2002 CONFIDENTIAL
 *
 * @author <a href="mailto:Krys_Gawetski@ctb.com">Krys Gawetski</a>
 * @version $Id$
 */
public class AnswerChoiceComparator implements Comparator
{
    /**
     * DOCUMENT ME!
     *
     * @param o1 DOCUMENT ME!
     * @param o2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Object o1, Object o2)
    {
        if ((o1 instanceof AnswerChoiceDetailVO) &&
                     (o1 instanceof AnswerChoiceDetailVO))
        {
            AnswerChoiceDetailVO a1 = (AnswerChoiceDetailVO)o1;
            AnswerChoiceDetailVO a2 = (AnswerChoiceDetailVO)o2;

            return (a1.getAnswerChoiceDescription().compareTo(a2.getAnswerChoiceDescription()));
        }

        else
        {
            return 0;
        }
    }
}


/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:46  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:48:29  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.4  2003/02/08 01:44:50  oasuser
 * merged OASR3tmp to trunk.
 *
 * Revision 1.3  2003/01/31 04:04:36  oasuser
 * merged from OASR3tmp to trunk.  Code not compiling, so java will be re-merged at a later date.
 *
 * Revision 1.2.6.2  2003/02/06 01:16:14  jshields
 * Moved classes to data and bean packages.
 * Removed deprecated bean classes.
 * Minor updates to SpecialCodeManager.
 * Other minor fixes.
 *
 * Revision 1.2.6.1  2003/01/23 19:52:34  jshields
 * TestAdministration refactoring.
 * JMS 2003-01-23 11:51
 *
 * Revision 1.2  2003/01/08 02:09:27  oasuser
 * merged from iknowR2-fixes branch as of 12-20 to trunk
 *
 * Revision 1.1.2.1  2003/01/03 01:03:28  kgawetsk
 * Creation.
 *
 */
