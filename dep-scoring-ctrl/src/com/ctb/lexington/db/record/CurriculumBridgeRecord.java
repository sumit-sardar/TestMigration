package com.ctb.lexington.db.record;

/*
 * Generated class: do NOT format or modify in any way!
 */
public class CurriculumBridgeRecord implements Persistent {
    public static final String TABLE_NAME = "STS_CURRICULUM_BRIDGE";

    public static final String CHILD_CURR_DIM_ID = "CHILD_CURR_DIM_ID";
    public static final String PARENT_CURR_DIM_ID = "PARENT_CURR_DIM_ID";
    public static final String NUMBER_OF_LEVELS = "NUMBER_OF_LEVELS";
    public static final String BOTTOM_MOST_FLAG = "BOTTOM_MOST_FLAG";
    public static final String TOP_MOST_FLAG = "TOP_MOST_FLAG";

    private Long childCurrDimId;
    private Long parentCurrDimId;
    private Long numberOfLevels;
    private String bottomMostFlag;
    private String topMostFlag;

    public Long getChildCurrDimId() {
        return childCurrDimId;
    }

    public void setChildCurrDimId(Long childCurrDimId) {
        this.childCurrDimId = childCurrDimId;
    }

    public Long getParentCurrDimId() {
        return parentCurrDimId;
    }

    public void setParentCurrDimId(Long parentCurrDimId) {
        this.parentCurrDimId = parentCurrDimId;
    }

    public Long getNumberOfLevels() {
        return numberOfLevels;
    }

    public void setNumberOfLevels(Long numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
    }

    public String getBottomMostFlag() {
        return bottomMostFlag;
    }

    public void setBottomMostFlag(String bottomMostFlag) {
        this.bottomMostFlag = bottomMostFlag;
    }

    public String getTopMostFlag() {
        return topMostFlag;
    }

    public void setTopMostFlag(String topMostFlag) {
        this.topMostFlag = topMostFlag;
    }
}