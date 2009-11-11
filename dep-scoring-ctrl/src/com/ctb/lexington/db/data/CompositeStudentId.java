package com.ctb.lexington.db.data;


public class CompositeStudentId {
	private Long id;
	private Long versionedId;
	
	public CompositeStudentId(Long id, Long versionedId) {
		this.id = id;
		this.versionedId = versionedId;
	}

	public Long getStudentId() {
		return id;
	}

	public Long getStudentVersionId() {
		return versionedId;
	}


}
