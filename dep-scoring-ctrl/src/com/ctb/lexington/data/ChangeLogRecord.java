package com.ctb.lexington.data;

/**
 * <p>
 * Title: ChangeLogRecord.java
 * </p>
 *
 * <p>
 * Description: This class represents the data retrieved from the database for the OE Change
 *              Log Report.  It includes functionality to compare records and to represent
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
 * ChangeLogRecord
 *
 * @author $Earl Waller$
 */
public class ChangeLogRecord implements Serializable, Comparable
{
	String district;
	String districtCode;
	String school;
	String schoolCode;
	String siteType;
	String changeBy;
	String changeDate;
	String changeTime;
	String changeType;
	String changeField;
	String newValue;
	String oldValue;

    /**
     * Creates a new ChangeLogRecord object.
     */
    public ChangeLogRecord()
    {
    	district = "";
		districtCode = "";
		school = "";
		schoolCode = "";
		siteType = "";
		changeBy = "";
		changeDate = "";
		changeTime = "";
		changeType = "";
		changeField = "";
		newValue = "";
		oldValue = "";
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
	 * Sets the changeBy
	 *
	 * @param changeBy String value of changed by
	 */
	public void setChangeBy(String changeBy)
	{
		if(changeBy == null){
			this.changeBy = "";
		}
		else{
			this.changeBy = changeBy;
		}
	}

	/**
	 * Gets the changeBy
	 *
	 * @return String The changeBy
	 */
	public String getChangeBy()
	{
		return(this.changeBy);
	}

	/**
	 * Sets the change date
	 *
	 * @param changeDate String value of change date
	 */
	public void setChangeDate(String changeDate)
	{
		if(changeDate == null){
			this.changeDate = "";
		}
		else{
			this.changeDate = changeDate;
		}
	}

	/**
	 * Gets the changeDate
	 *
	 * @return String The changeDate
	 */
	public String getChangeDate()
	{
		return(this.changeDate);
	}

	/**
	 * Sets the change time
	 *
	 * @param changeTime String value of change time
	 */
	public void setChangeTime(String changeTime)
	{
		if(changeTime == null){
			this.changeTime = "";
		}
		else{
			this.changeTime = changeTime;
		}
	}

	/**
	 * Gets the changeTime
	 *
	 * @return String The changeTime
	 */
	public String getChangeTime()
	{
		return(this.changeTime);
	}

	/**
	 * Sets the change type
	 *
	 * @param changeType String value of change type
	 */
	public void setChangeType(String changeType)
	{
		if(changeType == null){
			this.changeType = "";
		}
		else{
			this.changeType = changeType;
		}
	}

	/**
	 * Gets the changeType
	 *
	 * @return String The change type
	 */
	public String getChangeType()
	{
		return(this.changeType);
	}

	/**
	 * Sets the change field
	 *
	 * @param changeField String value of change field
	 */
	public void setChangeField(String changeField)
	{
		if(changeField == null){
			this.changeField = "";
		}
		else{
			this.changeField = changeField;
		}
	}

	/**
	 * Gets the changeField
	 *
	 * @return String The change field
	 */
	public String getChangeField()
	{
		return(this.changeField);
	}

	/**
	 * Sets the new value
	 *
	 * @param newValue String value of new value
	 */
	public void setNewValue(String newValue)
	{
		if(newValue == null){
			this.newValue = "";
		}
		else{
			this.newValue = newValue;
		}
	}

	/**
	 * Gets the newValue
	 *
	 * @return String The new value
	 */
	public String getNewValue()
	{
		return(this.newValue);
	}

	/**
	 * Sets the old value
	 *
	 * @param oldValue String value of old value
	 */
	public void setOldValue(String oldValue)
	{
		if(oldValue == null){
			this.oldValue = "";
		}
		else{
			this.oldValue = oldValue;
		}
	}

	/**
	 * Gets the oldValue
	 *
	 * @return String The old value
	 */
	public String getOldValue()
	{
		return(this.oldValue);
	}

	/**
	 * Compares this object to another object
	 * Sort order is by district, districtCode, siteType, school, schoolCode, changeDate, changeTime
	 *
	 * @param obj_  The ChangeLogRecord to compare to
	 * @return int negative number, 0, or positive number if parameter is less than, equal to, or greater than
	 */
	public int compareTo(Object obj_)
	{
		int sortOrder = 0;
		ChangeLogRecord otherObj = (ChangeLogRecord) obj_;
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
						else{
							int changeDateCompare = this.changeDate.compareTo(otherObj.getChangeDate());
							if(changeDateCompare!=0){
								sortOrder = changeDateCompare;
							}
							else{
								sortOrder = this.changeTime.compareTo(otherObj.getChangeTime());
							}
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
			ChangeLogRecord otherObj = (ChangeLogRecord) obj_;
			return(this.district.equals(otherObj.getDistrict()) &&
			       this.districtCode.equals(otherObj.getDistrictCode()) &&
			       this.school.equals(otherObj.getSchool()) &&
			       this.schoolCode.equals(otherObj.getSchoolCode()) &&
			       this.changeBy.equals(otherObj.getChangeBy()) &&
			       this.changeDate.equals(otherObj.getChangeDate()) &&
			       this.changeTime.equals(otherObj.getChangeTime()) &&
			       this.changeType.equals(otherObj.getChangeType()) &&
			       this.changeField.equals(otherObj.getChangeField()) &&
			       this.newValue.equals(otherObj.getNewValue()) &&
			       this.oldValue.equals(otherObj.getOldValue()));
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
		       this.changeBy + "\t" +
		       this.changeDate  + "\t" +
		       this.changeTime  + "\t" +
		       this.changeType  + "\t" +
		       this.changeField + "\t" +
		       this.newValue + "\t" +
		       this.oldValue);
	}
}
