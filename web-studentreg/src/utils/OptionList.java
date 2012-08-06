package utils; 

import java.util.ArrayList;
import java.util.List;

public class OptionList {
	public String[] gradeOptions = null;
	public String[] genderOptions = null;
	public String[] monthOptions = null;
	public String[] dayOptions = null;
	public String[] yearOptions = null;
	public String[] testPurposeOptions = null;
	
	private boolean profileEditable = true;
	/**
	 * @return the profileEditable
	 */
	public boolean isProfileEditable() {
		return profileEditable;
	}
	/**
	 * @param profileEditable the profileEditable to set
	 */
	public void setProfileEditable(boolean profileEditable) {
		this.profileEditable = profileEditable;
	}
	/**
	 * @return the monthOptions
	 */
	public String[] getMonthOptions() {
		return monthOptions;
	}
	/**
	 * @param monthOptions the monthOptions to set
	 */
	public void setMonthOptions(String[] monthOptions) {
		this.monthOptions = monthOptions;
	}
	/**
	 * @return the dayOptions
	 */
	public String[] getDayOptions() {
		return dayOptions;
	}
	/**
	 * @param dayOptions the dayOptions to set
	 */
	public void setDayOptions(String[] dayOptions) {
		this.dayOptions = dayOptions;
	}
	/**
	 * @return the yearOptions
	 */
	public String[] getYearOptions() {
		return yearOptions;
	}
	/**
	 * @param yearOptions the yearOptions to set
	 */
	public void setYearOptions(String[] yearOptions) {
		this.yearOptions = yearOptions;
	}
	/**
	 * @return the gradeOptions
	 */
	public String[] getGradeOptions() {
		return gradeOptions;
	}
	/**
	 * @param gradeOptions the gradeOptions to set
	 */
	public void setGradeOptions(String[] gradeOptions) {
		this.gradeOptions = gradeOptions;
	}
	/**
	 * @return the genderOptions
	 */
	public String[] getGenderOptions() {
		return genderOptions;
	}
	/**
	 * @param genderOptions the genderOptions to set
	 */
	public void setGenderOptions(String[] genderOptions) {
		this.genderOptions = genderOptions;
	}
	/**
	 * @return the testPurposeOptions
	 */
	public String[] getTestPurposeOptions() {
		return testPurposeOptions;
	}
	/**
	 * @param testPurposeOptions the testPurposeOptions to set
	 */
	public void setTestPurposeOptions(String[] testPurposeOptions) {
		this.testPurposeOptions = testPurposeOptions;
	}
	
	
	

}
