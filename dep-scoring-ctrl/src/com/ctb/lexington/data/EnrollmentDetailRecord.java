package com.ctb.lexington.data;

/**
 * <p>
 * Title: EnrollmentDetailRecord.java
 * </p>
 *
 * <p>
 * Description: This class represents the data retrieved from the database for the OE
 *              Enrollment Detail Report.  It includes functionality to compare records and
 *              to represent records as a String.  Input Strings are converted to a blank
 *              string if null.
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
 * EnrollmentDetailRecord
 *
 * @author $Earl Waller$
 */
public class EnrollmentDetailRecord implements Serializable, Comparable
{
	String district;
	String districtCode;
	String school;
	String schoolCode;
	String gradeName;
	String seed;
	String enrollment;

    /**
     * Creates a new EnrollmentDetailRecord object.
     */
    public EnrollmentDetailRecord()
    {
    	this.district = "";
		this.districtCode = "";
		this.school = "";
		this.schoolCode = "";
		this.gradeName = "";
		this.seed = "";
		this.enrollment = "";
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
	 * @param schoolCode_ String value of school code
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
	 * Gets the schoolCode
	 *
	 * @return String The school code
	 */
	public String getSchoolCode()
	{
		return(this.schoolCode);
	}

	/**
	 * Sets the grade name
	 *
	 * @param gradeName String value of grade name
	 */
	public void setGradeName(String gradeName)
	{
		if(gradeName == null){
			this.gradeName = "";
		}
		else{
			this.gradeName = gradeName;
		}
	}

	/**
	 * Gets the gradeName
	 *
	 * @return String The grade name
	 */
	public String getGradeName()
	{
		return(this.gradeName);
	}

	/**
	 * Sets the seed
	 *
	 * @param seed String value of seed enrollment
	 */
	public void setSeed(String seed)
	{
		if(seed == null){
			this.seed = "";
		}
		else{
			this.seed = seed;
		}
	}

	/**
	 * Gets the seed
	 *
	 * @return String The seed enrollment
	 */
	public String getSeed()
	{
		return(this.seed);
	}

	/**
	 * Sets the enrollment
	 *
	 * @param enrollment String value of enrollment
	 */
	public void setEnrollment(String enrollment)
	{
		if(enrollment == null){
			this.enrollment = "";
		}
		else{
			this.enrollment = enrollment;
		}
	}

	/**
	 * Gets the enrollment
	 *
	 * @return String The enrollment
	 */
	public String getEnrollment()
	{
		return(this.enrollment);
	}

	/**
	 * Compares this object to another object
	 * Sort order is by district, districtCode, school, schoolCode, gradeName
	 *
	 * @param obj_  The EnrollmentDetailRecord to compare to
	 * @return int negative number, 0, or positive number if parameter is less than, equal to, or greater than
	 */
	public int compareTo(Object obj_)
	{
		int sortOrder = 0;
		EnrollmentDetailRecord otherObj = (EnrollmentDetailRecord) obj_;
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
				int schoolCompare = this.school.compareTo(otherObj.getSchool());
				if(schoolCompare!=0){
					sortOrder = schoolCompare;
				}
				else{
					int schoolCodeCompare = this.schoolCode.compareTo(otherObj.getSchoolCode());
					if(schoolCodeCompare!=0){
						sortOrder = schoolCodeCompare;
					}
					else{
						sortOrder = this.gradeName.compareTo(otherObj.getGradeName());
					}
				}
			}
		}
		return(sortOrder);
	}

	/**
	 *  Tests to see if this object is equals to another object excluding enrollment
	 *
	 * @return boolean
	 */
	public boolean equals(Object obj_)
	{
		try{
			EnrollmentDetailRecord otherObj = (EnrollmentDetailRecord) obj_;
			return(this.district.equals(otherObj.getDistrict()) &&
			       this.districtCode.equals(otherObj.getDistrictCode()) &&
			       this.school.equals(otherObj.getSchool()) &&
			       this.schoolCode.equals(otherObj.getSchoolCode()) &&
			       this.gradeName.equals(otherObj.getGradeName()));
		}
		catch (Exception e){
			return false;
		}
	}

	/**
	 *  Tests to see if this object is equals to another object
	 *
	 * @return boolean
	 */
	public boolean sameSchool(EnrollmentDetailRecord obj_)
	{
		try{
			return(this.district.equals(obj_.getDistrict()) &&
			       this.districtCode.equals(obj_.getDistrictCode()) &&
			       this.school.equals(obj_.getSchool()) &&
			       this.schoolCode.equals(obj_.getSchoolCode()));
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
		       this.gradeName  + "\t" +
		       this.seed + "\t" +
		       this.enrollment);
	}
}
