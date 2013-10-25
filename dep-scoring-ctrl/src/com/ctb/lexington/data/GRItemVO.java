package com.ctb.lexington.data;

import java.sql.Clob;

import com.ctb.lexington.db.record.Persistent;

public class GRItemVO  extends Object implements Persistent, java.io.Serializable, java.lang.Cloneable {
	
	private String itemId;
	private Long itemSetId;
	private Long testRosterId;
	private Clob grResponse = null;
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the grResponse
	 */
	public Clob getGrResponse() {
		return grResponse;
	}
	/**
	 * @param grResponse the grResponse to set
	 */
	public void setGrResponse(Clob grResponse) {
		this.grResponse = grResponse;
	}
	/**
	 * @return the itemSetId
	 */
	public Long getItemSetId() {
		return itemSetId;
	}
	/**
	 * @param itemSetId the itemSetId to set
	 */
	public void setItemSetId(Long itemSetId) {
		this.itemSetId = itemSetId;
	}
	/**
	 * @return the testRosterId
	 */
	public Long getTestRosterId() {
		return testRosterId;
	}
	/**
	 * @param testRosterId the testRosterId to set
	 */
	public void setTestRosterId(Long testRosterId) {
		this.testRosterId = testRosterId;
	}
}
