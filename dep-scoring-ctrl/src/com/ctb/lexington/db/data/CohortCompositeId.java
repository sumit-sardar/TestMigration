package com.ctb.lexington.db.data;


public class CohortCompositeId {

	private Long cohortId;
	private Long cohortGroupId;

	public CohortCompositeId(Long cohortId, Long cohortGroupId) {
		this.cohortId=cohortId;
		this.cohortGroupId = cohortGroupId;
	}

	public Long getCohortGroupId() {
		return cohortGroupId;
	}

	public Long getCohortId() {
		return cohortId;
	}

}
