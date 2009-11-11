/*
 * ProductDetailVOComparator.java
 *
 * Created on November 15, 2002, 10:54 PM
 */
package com.ctb.lexington.util;

import java.util.Comparator;

import com.ctb.lexington.data.ProductVO;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ProductDetailVOComparator implements Comparator
{
    /** DOCUMENT ME! */
    public static final int bigger =
        Integer.toString(2).compareTo(Integer.toString(1));

    /** DOCUMENT ME! */
    public static final int smaller =
        Integer.toString(1).compareTo(Integer.toString(2));

    /** DOCUMENT ME! */
    public static final int equal =
        Integer.toString(3).compareTo(Integer.toString(3));

    /** DOCUMENT ME! */
    public static final int sortByProductId = 1;

    /** DOCUMENT ME! */
    public static final int sortByProductName = 2;

    /** DOCUMENT ME! */
    public int sortField;

    /**
     * Creates a new ProductDetailVOComparator object.
     *
     * @param sortby DOCUMENT ME!
     */
    public ProductDetailVOComparator(int sortby)
    {
        sortField = sortby;
    }

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
        if (sortField == sortByProductId)
        {
            return compareByProductId(o1, o2);
        }
        else
        {
            return compareByProductName(o1, o2);
        }
    }

    private int compareByProductId(Object o1, Object o2)
    {
        ProductVO tab1 = (ProductVO)o1;
        ProductVO tab2 = (ProductVO)o2;

        return tab1.getProductId().compareTo(tab2.getProductId());
    }

    private int compareByProductName(Object o1, Object o2)
    {
        ProductVO tab1 = (ProductVO)o1;
        ProductVO tab2 = (ProductVO)o2;

        return tab1.getProductName().compareTo(tab2.getProductName());
    }
}
