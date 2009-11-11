package com.ctb.lexington.data;

/**
 * <p>
 * Title: EnrollmentReportRecord.java
 * </p>
 *
 * <p>
 * Description: This class represents the data retrieved from the database for the OE Enrollment
 *              Status Report.  It includes functionality to compare records and to represent
 *              records as a String.  Input Strings are converted to a blank string if null.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 *
 * <p>
 * Company: CTB McGraw Hill
 * </p>
 */
import java.io.Serializable;

/**
 * EnrollmentReportRecord
 *
 * @author $Jon Becker$
 */
public class EnrollmentReportRecord implements Serializable, Comparable
{
	String district;
	String districtCode;
	String school;
	String schoolCode;
	String siteType;
	String phoneNumber;
	String testCoordinator;
	String address;
	String city;
	String status;

    /**
     * Creates a new EnrollmentReportRecord object.
     */
    public EnrollmentReportRecord()
    {
    	district = "";
		districtCode = "";
		school = "";
		schoolCode = "";
		siteType = "";
		phoneNumber = "";
		testCoordinator = "";
		address = "";
		city = "";
		status = "";
    }

	/**
	 * Sets the district
	 *
	 * @param district String value of district
	 */
    public void setDistrict(String district_)
    {
        if(district_ == null){
        	this.district = "";
        }
        else{
        	this.district = district_;
	    }
    }

	/**
	 * Gets the district
	 *
	 * @return String The district
	 */
	public String getDistrict()
	{
		return(this.district);
	}

	/**
	 * Sets the district code
	 *
	 * @param district String value of district code
	 */
	public void setDistrictCode(String districtCode_)
	{
		if(districtCode_ == null){
			this.districtCode = "";
		}
		else{
			this.districtCode = districtCode_;
		}
	}

	/**
	 * Gets the district code
	 *
	 * @return String The district code
	 */
	public String getDistrictCode()
	{
		return(this.districtCode);
	}

	/**
	 * Sets the school
	 *
	 * @param school String value of school
	 */
	public void setSchool(String school_)
	{
		if(school_ == null){
			this.school = "";
		}
		else{
			this.school = school_;
		}
	}

	/**
	 * Gets the school
	 *
	 * @return String The school
	 */
	public String getSchool()
	{
		return(this.school);
	}

	/**
	 * Sets the school code
	 *
	 * @param school String value of school code
	 */
	public void setSchoolCode(String schoolCode_)
	{
		if(schoolCode_ == null){
			this.schoolCode = "";
		}
		else{
			this.schoolCode = schoolCode_;
		}
	}

	/**
	 * Gets the school code
	 *
	 * @return String The school code
	 */
	public String getSchoolCode()
	{
		return(this.schoolCode);
	}

	/**
	 * Sets the site type
	 *
	 * @param school String value of site type
	 */
	public void setSiteType(String siteType_)
	{
		if(siteType_ == null){
			this.siteType = "";
		}
		else{
			this.siteType = siteType_;
		}
	}

	/**
	 * Gets the site type
	 *
	 * @return String The site type
	 */
	public String getSiteType()
	{
		return(this.siteType);
	}

	/**
	 * Sets the phone number
	 *
	 * @param phoneNumber_ String value of phone number
	 */
	public void setPhoneNumber(String phoneNumber_)
	{
		if(phoneNumber_ == null){
			this.phoneNumber = "";
		}
		else{
			this.phoneNumber = phoneNumber_;
		}
	}

	/**
	 * Gets the phoneNumber
	 *
	 * @return String The phone number
	 */
	public String getPhoneNumber()
	{
		return(this.phoneNumber);
	}

	/**
	 * Sets the test coordinator
	 *
	 * @param testCoordinator_ String value of test coordinator
	 */
	public void setTestCoordinator(String testCoordinator_)
	{
		if(testCoordinator_ == null){
			this.testCoordinator = "";
		}
		else{
			this.testCoordinator = testCoordinator_;
		}
	}

	/**
	 * Gets the test coordinator
	 *
	 * @return String The test coordinator
	 */
	public String getTestCoordinator()
	{
		return(this.testCoordinator);
	}

	/**
	 * Sets the address
	 *
	 * @param address_ String value of address
	 */
	public void setAddress(String address_)
	{
		if(address_ == null){
			this.address = "";
		}
		else{
			this.address = address_;
		}
	}

	/**
	 * Gets the address
	 *
	 * @return String The address
	 */
	public String getAddress()
	{
		return(this.address);
	}

	/**
	 * Sets the city
	 *
	 * @param city_ String value of city
	 */
	public void setCity(String city_)
	{
		if(city_ == null){
			this.city = "";
		}
		else{
			this.city = city_;
		}
	}

	/**
	 * Gets the city
	 *
	 * @return String The city number
	 */
	public String getCity()
	{
		return(this.city);
	}

	/**
	 * Sets the status
	 *
	 * @param status_ String value of status
	 */
	public void setStatus(String status_)
	{
		if(status_ == null){
			this.status = "";
		}
		else{
			this.status = status_;
		}
	}

	/**
	 * Gets the status
	 *
	 * @return String The status
	 */
	public String getStatus()
	{
		return(this.status);
	}

	/**
	 * Compares this object to another object
	 * Sort order is by district,
	 *
	 * @param obj_  The EnrollmentReportRecord to compare to
	 * @return int negative number, 0, or positive number if parameter is less than, equal to, or greater than
	 */
	public int compareTo(Object obj_)
	{
		int sortOrder = 0;
		EnrollmentReportRecord otherObj = (EnrollmentReportRecord) obj_;
		int districtCompare = this.district.compareTo(otherObj.getDistrict());
		if(districtCompare!=0){
			sortOrder = districtCompare;
		}
		else{
			int districtCodeCompare = this.districtCode.compareTo(otherObj.getDistrictCode());
			if(districtCodeCompare!=0){
				sortOrder = districtCodeCompare;
			}
			else{
				int siteTypeCompare = this.siteType.compareTo(otherObj.getSiteType());
				if(siteTypeCompare!=0){
					sortOrder = siteTypeCompare;
				}
				else{
					int schoolCompare = this.school.compareTo(otherObj.getSchool());
					if(schoolCompare!=0){
						sortOrder = schoolCompare;
					}
					else{
						int schoolCodeCompare = this.schoolCode.compareTo(otherObj.getSchoolCode());
						if(schoolCodeCompare!=0){
							sortOrder = schoolCodeCompare;
						}
						else{ // Confirmed Enrollments, Logged In, No Login Attempt
							sortOrder = this.status.compareTo(otherObj.getStatus());
						}
					}
				}
			}
		}
		return(sortOrder);
	}

	/**
	 *  Tests to see if this object is equals to another object excluding status
	 *
	 * @return String String representation of the object
	 */
	public boolean equals(Object obj_)
	{
		try{
			EnrollmentReportRecord otherObj = (EnrollmentReportRecord) obj_;
			return(this.district.equals(otherObj.getDistrict()) &&
			       this.districtCode.equals(otherObj.getDistrictCode()) &&
			       this.school.equals(otherObj.getSchool()) &&
			       this.schoolCode.equals(otherObj.getSchoolCode()) &&
			       this.siteType.equals(otherObj.getSiteType()) &&
			       this.phoneNumber.equals(otherObj.getPhoneNumber()) &&
			       this.testCoordinator.equals(otherObj.getTestCoordinator()) &&
			       this.address.equals(otherObj.getAddress()) &&
			       this.city.equals(otherObj.getCity()));
		}
		catch (Exception e){
			return false;
		}
	}


	/**
	 * Returns the object as a tab delimited string
	 *
	 * @return String String representation of the object
	 */
	public String toString()
	{
		return(this.district + "\t" +
		       this.districtCode + "\t" +
		       this.school + "\t" +
		       this.schoolCode  + "\t" +
		       this.siteType  + "\t" +
		       this.phoneNumber + "\t" +
		       this.testCoordinator  + "\t" +
		       this.address  + "\t" +
		       this.city + "\t" +
		       this.status);
	}
}
