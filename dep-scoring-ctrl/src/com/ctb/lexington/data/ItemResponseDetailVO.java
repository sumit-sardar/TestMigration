package com.ctb.lexington.data;

/**
 * ItemResponseDetailVO.java Copyright CTB/McGraw-Hill, 2002 CONFIDENTIAL
 */
import java.io.Serializable;
import java.util.List;

/**
 * @author  Tai Truong
 * @version $Id$
 */
public class ItemResponseDetailVO extends ItemResponseVO implements Serializable
{
    /** This beans's static label to be used for identification. */
    public static final String VO_LABEL =
        "com.ctb.lexington.data.ItemResponseDetailVO";
    public static final String VO_ARRAY_LABEL =
        "com.ctb.lexington.data.ItemResponseDetailVO.array";

    private List itemResponsePoints = null;
    private Integer itemSetPosition;
    /** The list of possible answer choices (AnswerChoiceVO) for this item */
    private List answerChoices;
    private String itemType;
    private Boolean isOnlineCr = null;

    /**
     * Constructor.
     */
    public ItemResponseDetailVO() {}

    /**
     * Sets the is online cr.
     *
     * @param isOnlineCr_ Boolean true if item is an online cr.
     */
    public void setIsOnlineCr(Boolean isOnlineCr_)
    {
        this.isOnlineCr = isOnlineCr_;
    }

    /**
     * Gets the Boolean - true if item is an online cr.
     *
     * @return  Boolean.
     */
    public Boolean getIsOnlineCr()
    {
        return this.isOnlineCr;
    }

    /**
     * Sets the list of possible answer choices.
     *
     * @param answerChoices_ the list of possible answer choices.
     */
    public void setAnswerChoices(List answerChoices_)
    {
        this.answerChoices = answerChoices_;
    }

    /**
     * Gets the list of possible answer choices.
     *
     * @return the list.
     */
    public List getAnswerChoices()
    {
        return this.answerChoices;
    }

    /**
     * Setter for property itemResponsePoints.
     *
     * @param points_ The <code>List</code> of {@link
     *        com.ctb.lexington.data.ItemResponsePointsDetailVO}s for this response.
     */
    public void setItemResponsePointsVO(List points_)
    {
        this.itemResponsePoints = points_;
    }

    /**
     * Getter for property itemResponsePoints.
     *
     * @return The <code>List</code> of {@link
     *         com.ctb.lexington.data.ItemResponsePointsDetailVO}s for this
     *         response.
     */
    public List getItemResponsePointsVO()
    {
        return this.itemResponsePoints;
    }

    /**
     * Setter for property itemSetPosition.
     *
     * @param itemSetPosition New value of property itemSetPosition.
     */
    public void setItemSetPosition(Integer itemSetPosition)
    {
        this.itemSetPosition = itemSetPosition;
    }

    /**
     * Getter for property itemSetPosition.
     *
     * @return Value of property itemSetPosition.
     */
    public Integer getItemSetPosition()
    {
        return itemSetPosition;
    }

    /**
     * Setter for property itemType.
     *
     * @param itemType New value of property itemType.
     */
    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }

    /**
     * Getter for property itemType.
     *
     * @return Value of property itemType.
     */
    public String getItemType()
    {
        return itemType;
    }

    public Object clone() {
        return super.clone();
    }
}
