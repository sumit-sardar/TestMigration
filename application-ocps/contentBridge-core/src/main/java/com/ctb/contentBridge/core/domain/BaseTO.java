/**
 * 
 */
package com.ctb.contentBridge.core.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class is the base Transfer Object. Other Transfer Objects should extend
 * this class
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class BaseTO implements Serializable {
	private static final long serialVersionUID = 7543625389410777280L;
	private long createdBy;
	private Timestamp createdAt;
	private long updatedBy;
	private Timestamp updatedAt;

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
}
