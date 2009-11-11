package com.ctb.lexington.util;

/*
 * ScanStudentComparator.java
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 */

import java.util.Comparator;

import com.ctb.lexington.data.ScanStudentVO;

/**
 * ScanStudentGradeDescendingComparator
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 * @author <a href="mailto:jonathan_becker@ctb.com">Jon Becker</a>
 */
public abstract class ScanStudentBaseComparator implements Comparator
{
    public int compare( Object o1, Object o2 )
    {
    	int result = 0;
        if ( (o1 instanceof ScanStudentVO)
          && (o2 instanceof ScanStudentVO) )
        {
        	result = compareScanStudents((ScanStudentVO) o1, (ScanStudentVO) o2);
        }
        return result;

    }
    
    protected abstract int compareScanStudents(ScanStudentVO s1_, ScanStudentVO s2_);
    
    protected int compareGrades(ScanStudentVO s1_, ScanStudentVO s2_){
    	int result = 0;
    	String s1Grade = s1_.getGrade() == null ? "" : s1_.getGrade();
    	String s2Grade = s2_.getGrade() == null ? "" : s2_.getGrade();
    	boolean s1IsK = s1Grade.equalsIgnoreCase("K");
    	boolean s2IsK = s2Grade.equalsIgnoreCase("K");
    	boolean s1IsMultipleMark = multipleMark(s1Grade);
    	boolean s2IsMultipleMark = multipleMark(s2Grade);
    	Integer s1Number = getNumber(s1Grade);
    	Integer s2Number = getNumber(s2Grade);
    	boolean s1IsNumber = (s1Number != null);
    	boolean s2IsNumber = (s2Number != null);
    	
       	if(s1Grade.equals(s2Grade))
       		result = 0;
       	else if(s1IsMultipleMark)
       		result = -1;
       	else if(s2IsMultipleMark)
       		result = 1;
       	else if(s1IsK)
       		result = -1;
       	else if(s2IsK)
       		result = 1;
       	else if(s1IsNumber){
       		if(s2IsNumber)
       			result = s1Number.compareTo(s2Number);
       		else
       			result = -1;
       	}
       	else if(s2IsNumber)
       		result = 1;
       	//else both blank result = 0
    	return result;
    }
    
    private Integer getNumber(String value_){
    	Integer result = null;
    	try{
    		result = new Integer(value_);
    	}
    	catch (Exception e){}
    	return result;
    }
    protected int compareGenders(ScanStudentVO s1_, ScanStudentVO s2_){
    	int result = 0;
    	String s1Gender = unknownGender(s1_.getGender()) ? "U" : s1_.getGender();
    	String s2Gender = unknownGender(s2_.getGender()) ? "U" : s2_.getGender();
    	result = s1Gender.compareTo(s2Gender);
    	return result;
    }
    
    private boolean unknownGender(String gender_){
    	boolean result = false;
    	if(gender_ == null ||
    	   gender_.equalsIgnoreCase("U") ||
		   gender_.equals("*"))
    		result = true;
    	return result;
    }
    
    protected int compareNames(ScanStudentVO s1_, ScanStudentVO s2_){
    	int result = 0;
    	
    	String s1LastName = s1_.getLastName() == null ? "" : s1_.getLastName().toLowerCase();
    	String s2LastName = s2_.getLastName() == null ? "" : s2_.getLastName().toLowerCase();
    	String s1FirstName = s1_.getFirstName() == null ? "" : s1_.getFirstName().toLowerCase();
    	String s2FirstName = s2_.getFirstName() == null ? "" : s2_.getFirstName().toLowerCase();
    	String s1MiddleName = s1_.getMiddleName() == null ? "" : s1_.getMiddleName().toLowerCase();
    	String s2MiddleName = s2_.getMiddleName() == null ? "" : s2_.getMiddleName().toLowerCase();
    	
    	result = s1LastName.compareTo( s2LastName );
        if ( result == 0 )
            result =s1FirstName.compareTo( s2FirstName );
        if( result == 0 )
        	result = s1MiddleName.compareTo(s2MiddleName);
        
        return result;
    }

    protected int compareBirthdates(ScanStudentVO s1_, ScanStudentVO s2_){
    	int result = 0;
    	String s1Month = s1_.getBirthMonth() == null ? "" : s1_.getBirthMonth();
    	String s1Day = s1_.getBirthDay() == null ? "": s1_.getBirthDay();
    	String s1Year = getFourDigitYear(s1_.getBirthYear());
    	String s2Month = s2_.getBirthMonth() == null ? "" : s2_.getBirthMonth();
    	String s2Day = s2_.getBirthDay()== null ? "" : s2_.getBirthDay();
    	String s2Year = getFourDigitYear(s2_.getBirthYear());
    	boolean s1IsMultipleMarks = (multipleMark(s1Month) || multipleMark(s1Day) || multipleMark(s1Year));
    	boolean s2IsMultipleMarks = (multipleMark(s2Month) || multipleMark(s2Day) || multipleMark(s2Year));
    	boolean s1IsBlank = (s1Month.equals("") && s1Day.equals("") && s1Year.equals(""));
    	boolean s2IsBlank = (s2Month.equals("") && s2Day.equals("") && s2Year.equals(""));
    	if(s1IsMultipleMarks){
    		if(s2IsMultipleMarks)
    			result = 0;
    		else
    			result = -1;
    	}
    	else if (s2IsMultipleMarks)
    		result = 1;
    	else if(s1IsBlank){
    		if(s2IsBlank)
    			result = 0;
    		else
    			result = 1;
    	}
    	else if(s2IsBlank)
    		result = -1;
    	else{
	    	result = s1Year.compareTo(s2Year);
	    	if(result == 0)
	    		result = s1Month.compareTo(s2Month);
	    	if(result == 0){
	    		result = s1Day.compareTo(s2Day);
	    	}
    	}
        return result;
    }

    private boolean multipleMark(String value_){
    	return (value_.indexOf("*") != -1);
    }
    
    private String getFourDigitYear(String year_){
    	String nonNullYear = year_ == null ? "z" : year_;
    	String twoDigitYear = nonNullYear.length() == 2 ? nonNullYear : "0" + nonNullYear;
    	String result = "zz" + twoDigitYear;
    	try{
    		int currentFourDigitYear = new Integer(DateUtils.getCurrentYear("GMT")).intValue();
    		int year = new Integer(twoDigitYear).intValue();
    		int currentTwoDigitYear = currentFourDigitYear % 100;
    		int currentDecade = (currentFourDigitYear / 100) * 100;
    		if(year <= currentTwoDigitYear)
    			result = String.valueOf(year + currentDecade);
    		else
    			result = String.valueOf(year + currentDecade - 100);
    	}
    	catch(Exception e){}
    	return result;
    }
}