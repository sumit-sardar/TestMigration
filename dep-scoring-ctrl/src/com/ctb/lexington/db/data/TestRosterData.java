/*
 * Created on Aug 15, 2004
 *
 * TestRosterData.java
 */
package com.ctb.lexington.db.data;

/**
 * @author Nate_Cohen
 *
 * TestRosterData
 */
public class TestRosterData {
	
	private Long testRosterId;
	private String rosterTestCompletionStatus;
	private String rosterScoringStatus;

	/**
	 * @return Returns the rosterScoringStatus.
	 */
	public String getRosterScoringStatus() {
		return rosterScoringStatus;
	}
	/**
	 * @param rosterScoringStatus The rosterScoringStatus to set.
	 */
	public void setRosterScoringStatus(String rosterScoringStatus) {
		this.rosterScoringStatus = rosterScoringStatus;
	}
	/**
	 * @return Returns the rosterTestCompletionStatus.
	 */
	public String getRosterTestCompletionStatus() {
		return rosterTestCompletionStatus;
	}
	/**
	 * @param rosterTestCompletionStatus The rosterTestCompletionStatus to set.
	 */
	public void setRosterTestCompletionStatus(String rosterTestCompletionStatus) {
		this.rosterTestCompletionStatus = rosterTestCompletionStatus;
	}
	/**
	 * @return Returns the testRosterId.
	 */
	public Long getTestRosterId() {
		return testRosterId;
	}
	/**
	 * @param testRosterId The testRosterId to set.
	 */
	public void setTestRosterId(Long testRosterId) {
		this.testRosterId = testRosterId;
	}
}
