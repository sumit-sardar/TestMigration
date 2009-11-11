/**
 * 
 */
package com.ctb.lexington.db.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.ctb.lexington.data.ScanStudentVO;
import com.ctb.lexington.data.ScanUploadVO;
import com.ctb.lexington.data.StudentVO;
import com.ctb.lexington.db.data.StudentData;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.Stringx;


/**
 * StudentMatchHelper
 * 
 * @author nate_cohen, Jingping Gu(automatch)
 *
 * Provides methods to apply the TestMate holistic name-matching
 * algorithm to two students or a student and a group.
 */
public class StudentMatchHelper {
	private static final String TRACE_TAG  = "StudentMatchHelper";
	
	public static final int DEFAULT_MATCH_THRESHOLD = 18;
	public static final int DEFAULT_STARTING_SCORE = 15;
	public static final int MATCH_LASTNAME_LENGTH   = 11;
	public static final int MATCH_FIRSTNAME_LENGTH  = 8;
	public static final int MATCH_MIDDLENAME_LENGTH = 1;
	public static final int MATCH_LAST_LENGTH = MATCH_LASTNAME_LENGTH;
	public static final int MATCH_FIRST_LENGTH = MATCH_LAST_LENGTH + MATCH_FIRSTNAME_LENGTH;
	public static final int MATCH_MIDDLE_LENGTH = MATCH_FIRST_LENGTH + MATCH_MIDDLENAME_LENGTH;
	
	public static final String RESPONSE_MULTIPLE_MARK = "*";
	public static final String RESPONSE_UNBUBBLED = "-";
	
	public static boolean automatch(ScanStudentVO scanStudentVO, StudentVO studentVO, Integer automatchCriteriaId) {
		boolean match = false;
		
		switch (automatchCriteriaId.intValue()) {
        	case ScanUploadVO.MATCH_STUDENTID_LASTNAME:
        		if (StudentMatchHelper.studentIdAndLastNameMatch(scanStudentVO, studentVO)) {
        			match = true;
        		}
        		break;
        	case ScanUploadVO.MATCH_DEMO:
        		if (StudentMatchHelper.exactMatchScanStudents(scanStudentVO, studentVO)) {
        			match = true;
        		}
        		break;
        	default:
        		break;
		}
		return match;
	}
	
	public static boolean studentIdMatch(ScanStudentVO scanStudent_, StudentVO student_) {
		boolean result = false;
		String scanStudentId = standardize(scanStudent_.getStudentNumber(), false, true);
		String studentId = standardize(student_.getExtPin1(), false, true);
		
		if (!Stringx.isEmpty(scanStudentId) && !Stringx.isEmpty(studentId) && scanStudentId.equals(studentId)) {
			result = true;
		}
		return result;
	}
	
	public static boolean studentIdMatch(ScanStudentVO scanStudent1, ScanStudentVO scanStudent2) {
		boolean result = false;
		String scanStudentId = standardize(scanStudent1.getStudentNumber(), false, true);
		String studentId = standardize(scanStudent2.getStudentNumber(), false, true);
		
		if (!Stringx.isEmpty(scanStudentId) && !Stringx.isEmpty(studentId) && scanStudentId.equals(studentId)) {
			result = true;
		}
		return result;
	}
	
	public static boolean studentIdAndLastNameMatch(ScanStudentVO scanStudent_, StudentVO student_) {
		boolean result = false;
		String scanStudentLastName = standardize(scanStudent_.getLastName(), false, true);
		String studentLastName = standardize(student_.getLastName(), false, true);
		
		if (studentIdMatch(scanStudent_, student_) && scanStudentLastName.equals(studentLastName)) {
			result = true;
		}
		
		
		return result;
	}
	
	public static boolean studentIdAndLastNameMatch(ScanStudentVO scanStudent_, ScanStudentVO student_) {
		boolean result = false;
		String scanStudentLastName = standardize(scanStudent_.getLastName(), false, true);
		String studentLastName = standardize(student_.getLastName(), false, true);
		
		if (studentIdMatch(scanStudent_, student_) && scanStudentLastName.equals(studentLastName)) {
			result = true;
		}
		return result;
	}
	
	/**
	 * Matches students by demographics info 
	 * @param scanStudent_ a stduent in the scan file
	 * @param student_ a student in the system
	 * @param exactMatch exact match if true, otherwise there is no require on middle name match
	 * @return true if students match, false else
	 */
	public static boolean exactMatchScanStudents(ScanStudentVO scanStudent_, StudentVO student_) {
		boolean result = false;
		String scanStudentFirstName = standardize(scanStudent_.getFirstName(), false, true);
		String scanStudentLastName = standardize(scanStudent_.getLastName(), false, true);
		String scanStudentMiddleName = standardize(scanStudent_.getMiddleName(), false, true);
		String scanStudentBirthDay = standardize(scanStudent_.getBirthDay(), true, false);
		String scanStudentBirthMonth = standardize(scanStudent_.getBirthMonth(), true, false);
		String scanStudentBirthYear = standardize(scanStudent_.getBirthYear(), true, false);
		String scanStudentGender = standardize(translateGender(scanStudent_.getGender()), false, true);

		String studentFirstName = standardize(student_.getFirstName(), false, true);
		String studentLastName = standardize(student_.getLastName(), false, true);
		String studentMiddleName = standardize(student_.getMiddleName(), false, true);
		String studentBirthDay = standardize(student_.getBirthDateDay(), true, false);
		String studentBirthMonth = standardize(student_.getBirthDateMonth(), true, false);
		String studentBirthYear = standardize(student_.getBirthDateYear(), true, false);
		String studentGender = standardize(student_.getGender(), false, true);
		
		if(scanStudentFirstName.equals(studentFirstName) &&
		   scanStudentLastName.equals(studentLastName) &&
		   middleNamesMatch(scanStudentMiddleName, studentMiddleName) &&
		   scanStudentBirthDay.equals(studentBirthDay) &&
		   scanStudentBirthMonth.equals(studentBirthMonth) &&
		   scanStudentBirthYear.equals(studentBirthYear) &&
		   gendersMatch(scanStudentGender, studentGender))
			result = true;
		
		return result;
	}

	public static boolean exactMatchScanStudents(ScanStudentVO scanStudent_, ScanStudentVO student_) {
		boolean result = false;
		String scanStudentFirstName = standardize(scanStudent_.getFirstName(), false, true);
		String scanStudentLastName = standardize(scanStudent_.getLastName(), false, true);
		String scanStudentMiddleName = standardize(scanStudent_.getMiddleName(), false, true);
		String scanStudentBirthDay = standardize(scanStudent_.getBirthDay(), true, false);
		String scanStudentBirthMonth = standardize(scanStudent_.getBirthMonth(), true, false);
		String scanStudentBirthYear = standardize(scanStudent_.getBirthYear(), true, false);
		String scanStudentGender = standardize(translateGender(scanStudent_.getGender()), false, true);
		

		String studentFirstName = standardize(student_.getFirstName(), false, true);
		String studentLastName = standardize(student_.getLastName(), false, true);
		String studentMiddleName = standardize(student_.getMiddleName(), false, true);
		String studentBirthDay = standardize(student_.getBirthDay(), true, false);
		String studentBirthMonth = standardize(student_.getBirthMonth(), true, false);
		String studentBirthYear = standardize(student_.getBirthYear(), true, false);
		String studentGender = standardize(student_.getGender(), false, true);
		
		if(scanStudentFirstName.equals(studentFirstName) &&
		   scanStudentLastName.equals(studentLastName) &&
		   middleNamesMatch(scanStudentMiddleName, studentMiddleName) &&
		   scanStudentBirthDay.equals(studentBirthDay) &&
		   scanStudentBirthMonth.equals(studentBirthMonth) &&
		   scanStudentBirthYear.equals(studentBirthYear) &&
		   gendersMatch(scanStudentGender, studentGender))
			result = true;
		
		return result;
	}

	private static boolean middleNamesMatch(String scanStudentMiddleName_, String studentMiddleName_){
		boolean result = false;

		if (Stringx.isEmpty(scanStudentMiddleName_) || 
			Stringx.isEmpty(studentMiddleName_) ||
			scanStudentMiddleName_ == null ||
			studentMiddleName_ == null ||
			scanStudentMiddleName_.substring(0, 1).equals(studentMiddleName_.substring(0,1)))
			result = true;
		
		return result;
	}
	
	private static boolean gendersMatch(String scanStudentGender_, String studentGender_){
		return (scanStudentGender_.equals(studentGender_) ||
				studentGender_.equalsIgnoreCase("U") ||
				studentGender_.equals(""));
	}
	
	private static String translateGender(String gender_){
		String result = gender_;
		if(gender_ == null || gender_.equals(""))
			result = "U";
		return result;
	}
	
	public static void standardizeForScanUpload(StudentVO student_){
		student_.setBirthDateDay(standardize(student_.getBirthDateDay(), true, false));
		student_.setBirthDateMonth(standardize(student_.getBirthDateMonth(), true, false));
		student_.setBirthDateYear(standardize(student_.getBirthDateYear(), true, false));
		student_.setFirstName(standardize(student_.getFirstName(), false, true));
		student_.setLastName(standardize(student_.getLastName(), false, true));
		student_.setMiddleName(standardize(student_.getMiddleName(), false, true));
		student_.setGender(standardize(translateGender(student_.getGender()), false, true));
		
	}
	
	public static String standardize(String input_, boolean isNumber_, boolean trim_){
		String result = input_;
		if(input_ == null)
			result = "";
		else{
			if(trim_)
				result = result.trim();
			result = result.toUpperCase();
		}
		if(isNumber_){
			try{
				Integer number = new Integer(result);
				result = number.toString();
			}
			catch(NumberFormatException e){				
			}
		}
		return result;
	}
	
	public static boolean matchScanStudents(ScanStudentVO scanStudent_, StudentVO student_) {
		return matchScanStudents(scanStudent_, student_, DEFAULT_MATCH_THRESHOLD, DEFAULT_STARTING_SCORE);
	}
	
	public static boolean matchScanStudents(ScanStudentVO scanStudent_, StudentVO student_, int threshold, int start) {
		StudentData studentData1 = new StudentData();
		StudentData studentData2 = new StudentData();
		
		studentData1.setFirstName(scanStudent_.getFirstName());
		studentData1.setLastName(scanStudent_.getLastName());
		studentData1.setMiddleInitial(scanStudent_.getMiddleName());
		studentData1.setBirthDate(getDate(scanStudent_.getBirthMonth(), scanStudent_.getBirthDay(), scanStudent_.getBirthYear()));
		studentData1.setGender(scanStudent_.getGender());
		
		studentData2.setFirstName(student_.getFirstName());
		studentData2.setLastName(student_.getLastName());
		studentData2.setMiddleInitial(student_.getMiddleName());
		studentData2.setBirthDate(getDate(student_.getBirthDateMonth(), student_.getBirthDateDay(), student_.getBirthDateYear()));
		studentData2.setGender(student_.getGender());
		
		return matchStudents(studentData1, studentData2, new Boolean(true), threshold, start);
	}
	
	private static Date getDate(String month, String day, String year) {
        final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            return format.parse(month + "/" + day + "/" + year);
        } catch (ParseException exc) {
            return null;
        }
    }

    public static boolean matchStudents(StudentData student1_, StudentData student2_, Boolean enhanced) {
		return matchStudents(student1_, student2_, enhanced, DEFAULT_MATCH_THRESHOLD, DEFAULT_STARTING_SCORE);
	}
	
	/**
	 * Compares two students to determine if they match
	 * Uses both the standard and enhanced name match algorithms.
	 * (copied from ScanUploadManagerEJB)
	 * @param Student1_ student to be matched
	 * @param Student2_ student to be matched
	 * @return boolean true if records match else false
	 */
	public static boolean matchStudents(StudentData student1_, StudentData student2_, Boolean enhanced, int threshold, int score) {
		final String SUB_TRACE = TRACE_TAG + ".matchScanStudentInstance()";
		int index;
		int matches;
		int position;
		boolean found;
		boolean[] used = new boolean[MATCH_MIDDLE_LENGTH]; // used to track which pairs are used
		StringBuffer matchPair = new StringBuffer();
		
		StringBuffer student1 = standardizeName(student1_.getLastName(),
				student1_.getFirstName(),
				student1_.getMiddleInitial());
		StringBuffer student2 = standardizeName(student2_.getLastName(),
				student2_.getFirstName(),
				student2_.getMiddleInitial());
		
		

		for (index = 0; index < MATCH_MIDDLE_LENGTH; index++)
			used[index] = false;

		// Standard match algorithm
		for (index = 0; index < MATCH_MIDDLE_LENGTH - 1; index++) {
			matchPair.replace(0, 2, student1.substring(index, index+2));
			position = student2.toString().indexOf(matchPair.toString());
			if (position < 0)
				score--;
			else
				if (!used[position])
					used[position] = true;
				else {
					found = false;
					do {
						position = student2.toString().indexOf(matchPair.toString(), position+1);
						if (position < 0)
							break;
						if (!used[position]) {
							used[position] = true;
							found = true;
						}
					} while (position > -1 && !found);
					if (!found)
						score--;
				}
		}

		// get data from VOs
		String scanStudentBirthDay       = "";
		String scanStudentBirthMonth     = "";
		String scanStudentBirthYear      = "";
		if(student1_.getBirthDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(student1_.getBirthDate());
			scanStudentBirthDay       = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			scanStudentBirthMonth     = String.valueOf(cal.get(Calendar.MONTH));
			scanStudentBirthYear      = String.valueOf(cal.get(Calendar.YEAR));
		}
		String scanStudentGender     = student1_.getGender();

		String studentBirthDay       = "";
		String studentBirthMonth     = "";
		String studentBirthYear      = "";
		if(student2_.getBirthDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(student2_.getBirthDate());
			studentBirthDay       = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			studentBirthMonth     = String.valueOf(cal.get(Calendar.MONTH));
			studentBirthYear      = String.valueOf(cal.get(Calendar.YEAR));
		}
		String studentGender         = student2_.getGender();

		if (scanStudentBirthMonth != null && studentBirthMonth != null && scanStudentBirthMonth.equals(studentBirthMonth)){
			score = score + 2;
		} else if (scanStudentBirthMonth != null && (scanStudentBirthMonth.equals(RESPONSE_UNBUBBLED + RESPONSE_UNBUBBLED) ||
				scanStudentBirthMonth.equals(RESPONSE_MULTIPLE_MARK + RESPONSE_MULTIPLE_MARK) ||
				scanStudentBirthMonth.trim().equals(""))){
			score++;
		} else if (studentBirthMonth != null && (studentBirthMonth.equals(RESPONSE_UNBUBBLED + RESPONSE_UNBUBBLED) ||
				studentBirthMonth.equals(RESPONSE_MULTIPLE_MARK + RESPONSE_MULTIPLE_MARK) ||
				studentBirthMonth.trim().equals(""))){
			score++;
		}
		
		if (scanStudentBirthDay != null && studentBirthDay != null && scanStudentBirthDay.equals(studentBirthDay)){
			score = score + 3;
		} else if (scanStudentBirthDay != null &&
				(scanStudentBirthDay.indexOf(RESPONSE_UNBUBBLED, -1) > -1 ||
						scanStudentBirthDay.indexOf(RESPONSE_MULTIPLE_MARK, -1) > -1 ||
						scanStudentBirthDay.trim().equals(""))){
			score++;
		} else if (studentBirthDay != null &&
				(studentBirthDay.indexOf(RESPONSE_UNBUBBLED, -1) > -1 ||
						studentBirthDay.indexOf(RESPONSE_MULTIPLE_MARK, -1) > -1 ||
						studentBirthDay.trim().equals(""))){
			score++;
		}

		if (scanStudentBirthYear != null && studentBirthYear != null && scanStudentBirthYear.equals(studentBirthYear)){
			score++;
		}

		if (scanStudentGender != null && studentGender != null && scanStudentGender.equals(studentGender)){
			score++;
		}

		if (score < threshold){
			return false;
		}
		
		if(enhanced.booleanValue()) {
			// Enhanced name match algorithm
			student1.replace(0, student1.length(), student1_.getFirstName().toUpperCase());
			student2.replace(0, student2.length(), student2_.getFirstName().toUpperCase());
			position = student1.length() - student2.length();
			if (position < 0){
				while (student1.length() < student2.length()){
					student1.append(" ");
				}
			}
			else if (position > 0){
				while (student2.length() < student1.length()){
					student2.append(" ");
				}
			}
	
			// test first names
			matches = 0;
			for (index = 0; index < student1.length(); index++){
				if (student1.charAt(index) == student2.charAt(index)){
					matches++;
				}
			}
			if (matches < (student1.length() + 1) / 2){
				
				return false;
			}
	
			student1.replace(0, student1.length(), student1_.getLastName().toUpperCase());
			student2.replace(0, student2.length(), student2_.getLastName().toUpperCase());
			position = student1.length() - student2.length();
			if (position < 0)
				while (student1.length() < student2.length())
					student1.append(" ");
			else if (position > 0)
				while (student2.length() < student1.length())
					student2.append(" ");
	
			// test last names
			matches = 0;
			for (index = 0; index < student1.length(); index++){
				if (student1.charAt(index) == student2.charAt(index)){
					matches++;
				}
			}
	
			if (matches < (student1.length() + 1) / 2){
				
				return false;
			}
			//GrndsTrace.exitScope();
		}
		
		return true;
	}
	
	public static boolean isDuplicateRecord(ScanStudentVO scanStudent1, ArrayList scanStudentList, Integer automatchCriteria) 
	throws CTBSystemException {
		boolean duplicated = false;
	  
		ScanStudentVO scanStudent2 = null;
		int count = 0;
		for (int i=0; i<scanStudentList.size(); i++) {
			scanStudent2 = (ScanStudentVO)scanStudentList.get(i);
			switch (automatchCriteria.intValue()) {
				case ScanUploadVO.MATCH_STUDENTID_LASTNAME:
					if (StudentMatchHelper.studentIdAndLastNameMatch(scanStudent1, scanStudent2)) {
						count++;
					}
						
					break;
				case ScanUploadVO.MATCH_DEMO:
					if (StudentMatchHelper.exactMatchScanStudents(scanStudent1, scanStudent2))
						count++;
					break;
				default:
					break;
			}
			if ( count>1 ) {
				duplicated = true;
				break;
			}
		}
		return duplicated;
	}
	
	public static boolean automatch(Collection students, ScanStudentVO scanStudentVO, Integer automatchCriteriaId) {
		boolean match = false;
		Iterator studentsIterator = students.iterator();
        StudentVO studentVO = null;
        int matchCount = 0;
        while (studentsIterator.hasNext()) {
            studentVO = (StudentVO) studentsIterator.next();

            if(StudentMatchHelper.automatch(scanStudentVO, studentVO, automatchCriteriaId)) {
            	match = true;
            	matchCount++;
            }
            if (matchCount>1) {
            	match = false;
            	break;
            }
        }
		return match;
	}
	
	private static StringBuffer standardizeName(String last, String first, String middle) {
		final String SUB_TRACE = TRACE_TAG + ".standardizeName()";

		// make sure we have String objects
		if(last == null){
			last = new String();
		}
		if(first == null){
			first = new String();
		}
		if(middle == null){
			middle = new String();
		}
		
		StringBuffer work = new StringBuffer();
		
		work.append(last.toUpperCase());
		if (work.length() > MATCH_LAST_LENGTH)
			work.delete(MATCH_LAST_LENGTH, work.length());
		else
			while (work.length() < MATCH_LAST_LENGTH)
				work.append(" ");

		work.append(first.toUpperCase());
		if (work.length() > MATCH_FIRST_LENGTH)
			work.delete(MATCH_FIRST_LENGTH, work.length());
		else
			while (work.length() < MATCH_FIRST_LENGTH)
				work.append(" ");

		work.append(middle.toUpperCase());
		if (work.length() > MATCH_MIDDLE_LENGTH)
			work.delete(MATCH_MIDDLE_LENGTH, work.length());
		else
			while (work.length() < MATCH_MIDDLE_LENGTH)
				work.append(" ");

		return work;
	}	

}
