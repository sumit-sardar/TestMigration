package com.ctb.bean;

/**
 * Data bean representing the contents of the StudentDemographicValue table and
 * student selection on demographic values
 * 
 * @author TCS
 */

public class StudentDemographicValue extends CustomerDemographicValue {
	static final long serialVersionUID = 1L;
	private String selectedFlag;

	public StudentDemographicValue() {
		super();
	}

	/**
	 * @return the selectedFlag
	 */
	public String getSelectedFlag() {
		return selectedFlag;
	}

	/**
	 * @param selectedFlag
	 *            the selectedFlag to set
	 */
	public void setSelectedFlag(String selectedFlag) {
		this.selectedFlag = selectedFlag;
	}

}
