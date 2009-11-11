package com.ctb.lexington.data;

/**
 * ItemResponsePointsDetailVO.java Copyright CTB/McGraw-Hill, 2002 CONFIDENTIAL
 */
import java.io.Serializable;
import java.util.List;


/**
 *
 *
 * @author  Tai Truong
 * @version $Id$
 * @author Tai Truong
 * @version $Id$
 */
public class ItemResponsePointsDetailVO extends ItemResponsePointsVO
    implements Serializable
{
    /** This beans's static label to be used for identification. */
    public static final String VO_LABEL =
        "com.ctb.lexington.data.ItemResponsePointsDetailVO";
    public static final String VO_ARRAY_LABEL =
        "com.ctb.lexington.data.ItemResponsePointsDetailVO.array";
    private Integer maxPoints = null;
    private Integer minPoints = null;

    /** The list of possible condition codes (ConditionCodeVO) for this item */
    private List conditionCodes;
    /**
     * Constructor.
     */
    public ItemResponsePointsDetailVO() {}

    /**
     * Sets the list of condition codes for this datapoint.
     *
     * @param answerChoices_ the list of condition codes.
     */
    public void setConditionCodes(List conditionCodes_)
    {
        this.conditionCodes = conditionCodes_;
    }

    /**
     * Gets the list of condition codes for this datapoint.
     *
     * @return the list.
     */
    public List getConditionCodes()
    {
        return this.conditionCodes;
    }

    /**
     * Sets the maximum points allowed for this datapoint.
     *
     * @param maxPoints_ the maximum points.
     */
    public void setMaxPoints(Integer maxPoints_)
    {
        this.maxPoints = maxPoints_;
    }

    /**
     * Gets the maximum points allowed for this datapoint.
     *
     * @return the max points.
     */
    public Integer getMaxPoints()
    {
        return this.maxPoints;
    }

    /**
     * Sets the minimum points allowed for this datapoint.
     *
     * @param minPoints_ the minimum points.
     */
    public void setMinPoints(Integer minPoints_)
    {
        this.minPoints = minPoints_;
    }

    /**
     * Gets the minimum points allowed for this datapoint.
     *
     * @return the min points.
     */
    public Integer getMinPoints()
    {
        return this.minPoints;
    }
}
