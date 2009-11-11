/*
 * TabeTestVOComparator.java
 *
 * Created on November 15, 2002, 4:18 PM
 */

package com.ctb.lexington.util;

/**
 *
 * @author  WChang
 * @version 
 */

import java.util.Comparator;

import com.ctb.lexington.data.TestVO;

public class TabeTestVOComparator implements Comparator
{

    public int compare( Object o1, Object o2 )
    {
        TestVO tab1 = (TestVO)o1;
        TestVO tab2 = (TestVO)o2;
        double form1Double = Double.parseDouble( tab1.getForm() );
        double form2Double = Double.parseDouble( tab2.getForm() );
        if ( form1Double > form2Double)
            return ProductDetailVOComparator.bigger;
        else if ( form1Double < form2Double )
            return ProductDetailVOComparator.smaller;
        else
        {
            String level1 = tab1.getLevel();
            String level2 = tab2.getLevel();
            return level1.compareTo( level2 );           
        }       
    }
}

