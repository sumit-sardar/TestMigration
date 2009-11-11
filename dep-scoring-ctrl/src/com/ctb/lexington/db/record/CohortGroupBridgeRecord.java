package com.ctb.lexington.db.record;

/*
 * Generated class: do NOT format or modify in any way!
 */
public class CohortGroupBridgeRecord implements Persistent {
    public static final String TABLE_NAME = "STS_COHORT_GROUP_BRIDGE";

    public static final String COHORT_GROUP_ID = "COHORT_GROUP_ID";
    public static final String COHORT_DIM_ID = "COHORT_DIM_ID";

    private Long cohortGroupId;
    private Long cohortDimId;

    public Long getCohortGroupId() {
        return cohortGroupId;
    }

    public void setCohortGroupId(Long cohortGroupId) {
        this.cohortGroupId = cohortGroupId;
    }

    public Long getCohortDimId() {
        return cohortDimId;
    }

    public void setCohortDimId(Long cohortDimId) {
        this.cohortDimId = cohortDimId;
    }
}