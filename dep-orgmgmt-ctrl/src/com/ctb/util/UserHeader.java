package com.ctb.util; 

import java.util.ArrayList;

/*
 * Userd for UserHeader 
 * @author  Tata Consultancy Services
 * 
 */

public class UserHeader {
	
	private final String firstName = "First Name";
	private final String middleName = "Middle Name";
	private final String lastName = "Last Name";
	private final String email = "Email";
	private final String timeZone = "Time Zone";
	private final String role = "Role";
	private final String address1 = "Address Line 1";
	private final String address2 = "Address Line 2";
	private final String city = "City";
	private final String state = "State";
	private final String zip = "Zip";
	private final String primaryPhone = "Primary Phone";
	private final String secondaryPhone = "Secondary Phone";
	private final String faxNumber = "Fax Number";
    //CR
    private final String extPin1 = "External User Id";
    
   private ArrayList userHeaderList = new ArrayList ();
    
    public UserHeader () {
       
        userHeaderList.add("getFirstName");
        userHeaderList.add("getMiddleName");
        userHeaderList.add("getLastName");
        userHeaderList.add("getEmail");
        userHeaderList.add("getTimeZone");
        userHeaderList.add("getRole");
        //CR
        userHeaderList.add("getExtPin1");
        userHeaderList.add("getAddress1");
        userHeaderList.add("getAddress2");
        userHeaderList.add("getCity");
        userHeaderList.add("getState");
        userHeaderList.add("getZip");
        userHeaderList.add("getPrimaryPhone");
        userHeaderList.add("getSecondaryPhone");
        userHeaderList.add("getFaxNumber");
         
    } 
    //CR
    /**
	 * @return Returns the extPin1.
	 */
	public String getExtPin1() {
		return extPin1;
	}
    
    /**
	 * @return Returns the userHeaderList.
	 */
	public ArrayList getUserHeaderList() {
		return userHeaderList;
	}
    
	/**
	 * @return Returns the address1.
	 */
	public String getAddress1() {
		return address1;
	}
	/**
	 * @return Returns the address2.
	 */
	public String getAddress2() {
		return address2;
	}
	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return Returns the faxNumber.
	 */
	public String getFaxNumber() {
		return faxNumber;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @return Returns the primaryPhone.
	 */
	public String getPrimaryPhone() {
		return primaryPhone;
	}
	/**
	 * @return Returns the role.
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @return Returns the secondaryPhone.
	 */
	public String getSecondaryPhone() {
		return secondaryPhone;
	}
	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return state;
	}
	/**
	 * @return Returns the timeZone.
	 */
	public String getTimeZone() {
		return timeZone;
	}
	/**
	 * @return Returns the zip.
	 */
	public String getZip() {
		return zip;
	}
	
	

} 
