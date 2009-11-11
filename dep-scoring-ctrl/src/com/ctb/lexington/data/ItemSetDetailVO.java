package com.ctb.lexington.data;

/*
 * ItemSetVO.java
 *
 * Created on September 27, 2002, 2:23 PM
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 */

import com.ctb.lexington.data.ItemSetVO;

/**
 * Detailed value object for the ItemSetVO used to contain additional
 * information and helper methods associated with item sets.
 *
 * @author <a href="mailto:giuseppe_gennaro@ctb.com">Giuseppe Gennaro</a>
 * @version $Id$
 */

public class ItemSetDetailVO extends ItemSetVO {
    //............................................................Member variables
    public static final String VO_LABEL = "com.ctb.lexington.data.ItemSetDetailVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    /**
     * The maximum value found for the item responses for this item set.
     */
    private Integer maxItemResponseSeqNum;

    private ItemSetSectionVO[] sections;

    //................................................................Constructors

    /**
     * Constructor.
     */
    public ItemSetDetailVO() {
    }

    //.............................................................Get/set methods

    /**
     * Returns the value for responseSeqNumSeed member variable.
     *
     * @return An Integer containing the numeric value to be used as the seed
     *         for any responses for this item set.
     */
    public Integer getMaxItemResponseSeqNum() {
        return this.maxItemResponseSeqNum;
    }

    /**
     * Sets the value for responseSeqNumSeed member variable. If null is provided,
     * it will default to Integer(0).
     */
    public void setMaxItemResponseSeqNum(Integer maxItemResponseSeqNum) {
        if (maxItemResponseSeqNum == null)
            this.maxItemResponseSeqNum = new Integer(0);
        else
            this.maxItemResponseSeqNum = maxItemResponseSeqNum;
    }

    public ItemSetSectionVO[] getSections() {
        return this.sections;
    }

    public void setSections(ItemSetSectionVO[] sections) {
        this.sections = sections;
    }

    public boolean hasSections() {
        return (this.sections!=null && this.sections.length > 0);
    }
    //..............................................................Helper methods
}