package com.ctb.lexington.util;

import java.util.Comparator;
import java.io.Serializable;

import com.ctb.lexington.data.TestAdminVO;

/**
 * TestAccessCodeComparator
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 * @author tai truong
 */

public class TestAccessCodeComparator implements Comparator,Serializable
{

    public int compare( Object o1, Object o2 )
    {
        TestAdminVO tab1 = (TestAdminVO)o1;
        TestAdminVO tab2 = (TestAdminVO)o2;
        String str1 = tab1.getAccessCode();
        String str2 = tab2.getAccessCode();
        return str1.compareTo( str2 );
    }

}
