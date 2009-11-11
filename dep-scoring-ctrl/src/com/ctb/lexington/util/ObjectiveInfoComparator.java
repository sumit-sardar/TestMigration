package com.ctb.lexington.util;

import java.util.Comparator;

import com.ctb.lexington.data.ObjectiveInfo;


public class ObjectiveInfoComparator implements Comparator
{
    public int compare(Object o1, Object o2)
    {
    	if ((o1 instanceof ObjectiveInfo) && (o2 instanceof ObjectiveInfo))
        {
            ObjectiveInfo s1 = (ObjectiveInfo)o1;
            ObjectiveInfo s2 = (ObjectiveInfo)o2;
            return ItemSetComparator.toComparableString(s1.getName()).compareTo(ItemSetComparator.toComparableString(s2.getName()));
        }
        else
        {
            return 0;
        }
    }
}
