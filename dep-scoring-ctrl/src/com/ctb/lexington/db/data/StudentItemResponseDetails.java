/*
 * Created on Oct 1, 2004
 *
 * StudentItemResponseData.java
 */
package com.ctb.lexington.db.data;

import java.sql.Clob;

import com.ctb.lexington.db.record.Persistent;

/**
 * @author nate_cohen
 *
 * StudentItemResponseData
 */
public class StudentItemResponseDetails implements Persistent {
	private Long studentTestHistoryId;
	private Integer itemSetId;
	private String itemId;
	private Clob constructedResponse;
	/**
	 * @return Returns the constructedResponse.
	 */
	public Clob getConstructedResponse() {
		return constructedResponse;
	}
	/**
	 * @param constructedResponse The constructedResponse to set.
	 */
	public void setConstructedResponse(Clob constructedResponse) {
		this.constructedResponse = constructedResponse;
	}
	/**
	 * @return Returns the itemId.
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return Returns the itemSetId.
	 */
	public Integer getItemSetId() {
		return itemSetId;
	}
	/**
	 * @param itemSetId The itemSetId to set.
	 */
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}
	/**
	 * @return Returns the studentTestHistoryId.
	 */
	public Long getStudentTestHistoryId() {
		return studentTestHistoryId;
	}
	/**
	 * @param studentTestHistoryId The studentTestHistoryId to set.
	 */
	public void setStudentTestHistoryId(Long studentTestHistoryId) {
		this.studentTestHistoryId = studentTestHistoryId;
	}
}
