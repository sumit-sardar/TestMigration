package com.ctb.dto;

import java.io.Serializable;

public class StudentItemSetStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String validationStatus;
	private String exemptions;
	private String absent;
	private TestRoster testRoster;
	
	private Integer itemSetId;
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(validationStatus);
		sb.append(exemptions);
		sb.append(absent);
		return sb.toString();
		
	}
	
	
	/**
	 * @return the itemSetId
	 */
	public Integer getItemSetId() {
		return itemSetId;
	}

	/**
	 * @param itemSetId the itemSetId to set
	 */
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}

	

	/**
	 * @return the testRoster
	 */
	public TestRoster getTestRoster() {
		return testRoster;
	}

	/**
	 * @param testRoster the testRoster to set
	 */
	public void setTestRoster(TestRoster testRoster) {
		this.testRoster = testRoster;
	}

	

	/**
	 * @return the validationStatus
	 */
	public String getValidationStatus() {
		return validationStatus;
	}

	/**
	 * @param validationStatus
	 *            the validationStatus to set
	 */
	public void setValidationStatus(String validationStatus) {
		this.validationStatus = validationStatus;
	}

	/**
	 * @return the exemptions
	 */
	public String getExemptions() {
		return exemptions;
	}

	/**
	 * @param exemptions
	 *            the exemptions to set
	 */
	public void setExemptions(String exemptions) {
		this.exemptions = exemptions;
	}

	/**
	 * @return the absent
	 */
	public String getAbsent() {
		return absent;
	}

	/**
	 * @param absent
	 *            the absent to set
	 */
	public void setAbsent(String absent) {
		this.absent = absent;
	}

	
}




 
