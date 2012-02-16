package com.ctb.dto;

import org.ffpojo.metadata.positional.annotation.PositionalField;

public class StudentContact {

	private Integer studentContactId;
	private String state;
	private String city;
	private Student student;
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(state);
		sb.append(city);
		return sb.toString();
		
	}
	
	/**
	 * @return the student
	 */
	public Student getStudent() {
		return student;
	}
	/**
	 * @param student the student to set
	 */
	public void setStudent(Student student) {
		this.student = student;
	}
	
	
	
	/**
	 * @return the studentContactId
	 */
	public Integer getStudentContactId() {
		return studentContactId;
	}
	/**
	 * @param studentContactId the studentContactId to set
	 */
	public void setStudentContactId(Integer studentContactId) {
		this.studentContactId = studentContactId;
	}
	/**
	 * @return the state
	 */
	@PositionalField(initialPosition = 109, finalPosition =110, trimOnRead= true)
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the city
	 */
	@PositionalField(initialPosition = 79, finalPosition =108, trimOnRead= true)
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
}
