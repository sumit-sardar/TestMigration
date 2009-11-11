package com.ctb.lexington.util;

import java.util.Comparator;

import com.ctb.lexington.data.BroadcastMessageVO;

/**
 * BroadcastMessageComparator
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 * @author Coiln Ma</a>
 * @version
 * $Id$
 */
public class BroadcastMessageComparator implements Comparator
{

    public int compare( Object o1, Object o2 )
    {       
            BroadcastMessageVO b1 = (BroadcastMessageVO)o1;
            BroadcastMessageVO b2 = (BroadcastMessageVO)o2;
            return b1.getPriorityValue().compareTo( b2.getPriorityValue() );
    }
}