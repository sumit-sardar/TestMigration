package com.ctb.lexington.util;

import java.util.Comparator;

/**
 * ItemSetComparator Copyright CTB/McGraw-Hill, 2005 CONFIDENTIAL
 *
 * @author Jon Becker
 */
public class GradeNameComparator implements Comparator
{
    public int compare(Object o1, Object o2)
    {
        if ((o1 instanceof String) && (o2 instanceof String)){
        	String s1 = (String)o1;
        	String s2 = (String)o2;
        	s1 = s1.toLowerCase();
        	s2 = s2.toLowerCase();
        	return ItemSetComparator.toComparableString(s1).compareTo(ItemSetComparator.toComparableString(s2));
        }
        else
        	return 0;
    }
}