/*
 * Created on Mar 09, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.ctb.lexington.util;
 
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import com.ctb.lexington.data.StudentVO;

/**
 * @author Tai Truong
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class StudentInfoComparators {

	public StudentInfoComparators() {	}
	
	public Comparator getStudentVOComparator(String columnIndex) {
		if (columnIndex == "1") return new NameComparator();
		if (columnIndex == "2") return new GradeComparator();
		if (columnIndex == "3") return new GenderComparator();
		if (columnIndex == "4") return new StudentIdComparator();
		if (columnIndex == "5") return new HasResponsesAscendingComparator();
		if (columnIndex == "6") return new HasNoResponsesDescendingComparator();
		if (columnIndex == "7") return new GradeAscendingComparator();
		if (columnIndex == "8") return new GradeDescendingComparator();
		if (columnIndex == "9") return new NameAscendingComparator();
		if (columnIndex == "10") return new NameDescendingComparator();
		if (columnIndex == "11") return new GenderAscendingComparator();
		if (columnIndex == "12") return new GenderDescendingComparator();
		if (columnIndex == "13") return new StudentIdAscendingComparator();
		if (columnIndex == "14") return new StudentIdDescendingComparator();
		if (columnIndex == "15") return new BirthDateAscendingComparator();
		if (columnIndex == "16") return new BirthDateDescendingComparator();
		if (columnIndex == "17") return new LastNameAscendingComparator();
		if (columnIndex == "18") return new LastNameDescendingComparator();
		if (columnIndex == "19") return new FirstNameAscendingComparator();
		if (columnIndex == "20") return new FirstNameDescendingComparator();
		if (columnIndex == "21") return new LoginNameAscendingComparator();
		if (columnIndex == "22") return new LoginNameDescendingComparator();
		if (columnIndex == "23") return new PasswordAscendingComparator();
		if (columnIndex == "24") return new PasswordDescendingComparator();
		if (columnIndex == "25") return new ValidationStatusAscendingComparator();
		if (columnIndex == "26") return new ValidationStatusDescendingComparator();
		if (columnIndex == "27") return new OnlineTestStatusAscendingComparator();
		if (columnIndex == "28") return new OnlineTestStatusDescendingComparator();
		return new BirthdateComparator();
	}
	
	private class BirthdateComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			Calendar leftDate = left.getBirthdate();
			Calendar rightDate = right.getBirthdate();
			if (leftDate.after(rightDate)) return -1;
			if (leftDate.before(rightDate)) return +1;
			else return 0;
		}
	}
	
	private class NameComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftLastName = left.getLastName() != null ? left.getLastName() : ""; 
			String rightLastName = right.getLastName() != null ? right.getLastName() : "";
			int result = leftLastName.compareTo(rightLastName);
			if (result == 0 ) 
				result = left.getFirstName().compareTo(right.getFirstName());
			return result;
		}
	}

	private class LastNameAscendingComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftLastName = left.getLastName() != null ? left.getLastName() : ""; 
			String rightLastName = right.getLastName() != null ? right.getLastName() : "";
			int result = leftLastName.compareTo(rightLastName);
			if (result == 0 ) 
				result = left.getFirstName().compareTo(right.getFirstName());
			return result;
		}
	}

	private class LastNameDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			return new LastNameAscendingComparator().compare(o2, o1);
		}
	}
	
	private class FirstNameAscendingComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftFirstName = left.getFirstName() != null ? left.getFirstName() : ""; 
			String rightFirstName = right.getFirstName() != null ? right.getFirstName() : "";
			int result = leftFirstName.compareTo(rightFirstName);
			if (result == 0 ) 
				result = left.getLastName().compareTo(right.getLastName());
			return result;
		}
	}

	private class FirstNameDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			return new FirstNameAscendingComparator().compare(o2, o1);
		}
	}

	private class LoginNameAscendingComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftLoginName = left.getUserName() != null ? left.getUserName() : ""; 
			String rightLoginName = right.getUserName() != null ? right.getUserName() : "";
			int result = leftLoginName.compareTo(rightLoginName);
			if (result == 0 ) 
				result = left.getLastName().compareTo(right.getLastName());
			return result;
		}
	}

	private class LoginNameDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			return new LoginNameAscendingComparator().compare(o2, o1);
		}
	}

	private class PasswordAscendingComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftPassword = left.getPassword() != null ? left.getPassword() : ""; 
			String rightPassword = right.getPassword() != null ? right.getPassword() : "";
			int result = leftPassword.compareTo(rightPassword);
			if (result == 0 ) 
				result = left.getLastName().compareTo(right.getLastName());
			return result;
		}
	}

	private class PasswordDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			return new PasswordAscendingComparator().compare(o2, o1);
		}
	}

	private class ValidationStatusAscendingComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftStatus = left.getValidationStatus() != null ? left.getValidationStatus() : ""; 
			String rightStatus = right.getValidationStatus() != null ? right.getValidationStatus() : "";
			int result = leftStatus.compareTo(rightStatus);
			if (result == 0 ) 
				result = left.getLastName().compareTo(right.getLastName());
			return result;
		}
	}

	private class ValidationStatusDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			return new ValidationStatusAscendingComparator().compare(o2, o1);
		}
	}

	private class OnlineTestStatusAscendingComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftStatus = left.getTestCompletionStatus() != null ? left.getTestCompletionStatus() : ""; 
			String rightStatus = right.getTestCompletionStatus() != null ? right.getTestCompletionStatus() : "";
			int result = leftStatus.compareTo(rightStatus);
			if (result == 0 ) 
				result = left.getLastName().compareTo(right.getLastName());
			return result;
		}
	}

	private class OnlineTestStatusDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			return new OnlineTestStatusAscendingComparator().compare(o2, o1);
		}
	}
	
	private class GradeComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftGrade = left.getGrade() != null ? left.getGrade() : "0";
			String rightGrade = right.getGrade() != null ? right.getGrade() : "0";
			return leftGrade.compareTo(rightGrade);
		}
	}
	
	private class StudentIdComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			Integer leftStudentID = left.getStudentId() != null ? left.getStudentId() : new Integer(0);
			Integer rightStudentID = right.getStudentId() != null ? right.getStudentId() : new Integer(0);
			return leftStudentID.compareTo(rightStudentID);
		}
	}
	
	private class GenderComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftGender = left.getGender() != null ? left.getGender() : "";
			String rightGender = right.getGender() != null ? right.getGender() : "";
			return leftGender.compareTo(rightGender);
		}
	}
	
	private class HasResponsesAscendingComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftHasResponses = left.haveSomeResponses() ? "Yes" : "No";
			String rightHasResponses = right.haveSomeResponses() ? "Yes" : "No";
			result = leftHasResponses.compareTo(rightHasResponses);
			if(result == 0)
				result = new NameAscendingComparator().compare(o1, o2);
			return result;
		}
	}
	
	private class HasNoResponsesDescendingComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftHasResponses = left.haveSomeResponses() ? "Yes" : "No";
			String rightHasResponses = right.haveSomeResponses() ? "Yes" : "No";
			result = rightHasResponses.compareTo(leftHasResponses);
			if(result == 0)
				result = new NameAscendingComparator().compare(o1, o2);
			return result;
		}
	}
	
	private class GradeAscendingComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftGrade = left.getGrade() != null ? left.getGrade() : "0";		
			String rightGrade = right.getGrade() != null ? right.getGrade() : "0";
			
			if(leftGrade.equalsIgnoreCase("K")){
				if(rightGrade.equalsIgnoreCase("K"))
					result = new NameAscendingComparator().compare(o1, o2);
				else
					result = -1;
			}
			else if(rightGrade.equalsIgnoreCase("K"))
				result = 1;
			else if (leftGrade.equals(rightGrade))
				result = new NameAscendingComparator().compare(o1, o2);
			else {
            	try{
	            	Integer rightGradeInt = new Integer(rightGrade);
	            	Integer leftGradeInt = new Integer(leftGrade);
	            	result = leftGradeInt.compareTo(rightGradeInt);
            	}
            	catch (Exception e){
            		result = leftGrade.compareTo(rightGrade);
            	}
			}
			return result;
		}
	}
	
	private class GradeDescendingComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftGrade = left.getGrade() != null ? left.getGrade() : "99";		
			String rightGrade = right.getGrade() != null ? right.getGrade() : "99";
			if(rightGrade.equalsIgnoreCase("K")){
				if(leftGrade.equalsIgnoreCase("K"))
					result = new NameAscendingComparator().compare(o1, o2);
				else
					result = -1;
			}
			else if(leftGrade.equalsIgnoreCase("K"))
				result = 1;
			else if (rightGrade.equals(leftGrade))
				result = new NameAscendingComparator().compare(o1, o2);
			else {
            	try{
	            	Integer rightGradeInt = new Integer(rightGrade);
	            	Integer leftGradeInt = new Integer(leftGrade);
	            	result = rightGradeInt.compareTo(leftGradeInt);
            	}
            	catch (Exception e){
            		result = rightGrade.compareTo(leftGrade);
            	}
			}
			return result;
		}
	}
	
	private class NameAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftFirstName = left.getFirstName() != null ? left.getFirstName() : "";		
			String rightFirstName = right.getFirstName() != null ? right.getFirstName() : "";
			String leftLastName = left.getLastName() != null ? left.getLastName() : "";		
			String rightLastName = right.getLastName() != null ? right.getLastName() : "";
			String leftMiddleName = left.getMiddleName() != null ? left.getMiddleName() : "";		
			String rightMiddleName = right.getMiddleName() != null ? right.getMiddleName() : "";
			result = leftLastName.toLowerCase().compareTo(rightLastName.toLowerCase());
			if(result == 0)
				result = leftFirstName.toLowerCase().compareTo(rightFirstName.toLowerCase());
			if(result == 0)
				result = leftMiddleName.toLowerCase().compareTo(rightMiddleName.toLowerCase());
			return result;
		}
	}
	
	private class NameDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			return new NameAscendingComparator().compare(o2, o1);
		}
	}

	private class GenderAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftGender = left.getGender() != null ? left.getGender() : "";		
			String rightGender = right.getGender() != null ? right.getGender() : "";	
			result = leftGender.toLowerCase().compareTo(rightGender.toLowerCase());
			if(result == 0)
				result = new NameAscendingComparator().compare(o1, o2);
			return result;
		}
	}
	
	private class GenderDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftGender = left.getGender() != null ? left.getGender() : "";		
			String rightGender = right.getGender() != null ? right.getGender() : "";	
			result = rightGender.toLowerCase().compareTo(leftGender.toLowerCase());
			if(result == 0)
				result = new NameAscendingComparator().compare(o1, o2);
			return result;
		}
	}

	private class StudentIdAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftStudentId = left.getExtPin1() != null ? left.getExtPin1() : "";		
			String rightStudentId = right.getExtPin1() != null ? right.getExtPin1() : "";	
			result = leftStudentId.toLowerCase().compareTo(rightStudentId.toLowerCase());
			if(result == 0)
				result = new NameAscendingComparator().compare(o1, o2);
			return result;
		}
	}
	
	private class StudentIdDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			String leftStudentId = left.getExtPin1() != null ? left.getExtPin1() : "";		
			String rightStudentId = right.getExtPin1() != null ? right.getExtPin1() : "";	
			result = rightStudentId.toLowerCase().compareTo(leftStudentId.toLowerCase());
			if(result == 0)
				result = new NameAscendingComparator().compare(o1, o2);
			return result;
		}
	}

	private class BirthDateAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			Date leftBirthdate = left.getBirthdate().getTime();		
			Date rightBirthdate = right.getBirthdate().getTime();	
			result = leftBirthdate.compareTo(rightBirthdate);
			if(result == 0)
				result = new NameAscendingComparator().compare(o1, o2);
			return result;
		}
	}
	
	private class BirthDateDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			StudentVO left = (StudentVO) o1;
			StudentVO right = (StudentVO) o2;
			Date leftBirthdate = left.getBirthdate().getTime();		
			Date rightBirthdate = right.getBirthdate().getTime();	
			result = rightBirthdate.compareTo(leftBirthdate);
			if(result == 0)
				result = new NameAscendingComparator().compare(o1, o2);
			return result;
		}
	}
}
