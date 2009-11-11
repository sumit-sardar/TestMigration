package com.ctb.lexington.db.data;


public class UserData {

	private String firstName;
	private String lastName;

	public String getTeacherName() {
		return firstName +" " +lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
