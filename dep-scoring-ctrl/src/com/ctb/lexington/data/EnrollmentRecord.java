package com.ctb.lexington.data;

/**
 * <p>
 * Title: EnrollmentRecord.java
 * </p>
 *
 * <p>
 * Description: This class represents the enrollment data retrieved from the database for the
 *              OE Enrollment Detail Report.  It includes functionality to compare records and
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
 * EnrollmentRecord
 *
 * @author $Earl Waller$
 * @version $Revision$
 */
public class EnrollmentRecord implements Serializable, Comparable
{
	String gradeName;
	String seed;
	String enrollment;

    /**
     * Creates a new EnrollmentRecord object.
     */
    public EnrollmentRecord()
    {
		this.gradeName = "";
		this.seed = "";
		this.enrollment = "";
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
	 * Sort order is by gradeName
	 *
	 * @param obj_  The EnrollmentRecord to compare to
	 * @return int negative number, 0, or positive number if parameter is less than, equal to, or greater than
	 */
	public int compareTo(Object obj_)
	{
		EnrollmentRecord otherObj = (EnrollmentRecord) obj_;
		return(this.gradeName.compareTo(otherObj.getGradeName()));
	}

	/**
	 *  Tests to see if this object is equals to another object excluding enrollment
	 *
	 * @return String String representation of the object
	 */
	public boolean equals(Object obj_)
	{
		try{
			EnrollmentRecord otherObj = (EnrollmentRecord) obj_;
			return(this.gradeName.equals(otherObj.getGradeName()));
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
		return(this.gradeName  + "\t" +
		       this.seed + "\t" +
		       this.enrollment);
	}
}
