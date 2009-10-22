package com.ctb.util; 


import java.util.ArrayList;

/*
 * Userd for StudentHeader 
 * @author  Tata Consultancy Services
 * 
 */

public class StudentHeader 
{
    private final String firstName = CTBConstants.REQUIREDFIELD_FIRST_NAME;
    private final String middleName = CTBConstants.MIDDLE_NAME ;
    private final String lastName = CTBConstants.REQUIREDFIELD_LAST_NAME ;
    private final String birthDate = CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH ;
    private final String headerDateOfBirthDate = CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH ;
    private final String grade = CTBConstants.REQUIREDFIELD_GRADE ;
    private final String gender = CTBConstants.REQUIREDFIELD_GENDER;
    private final String studentId = CTBConstants.STUDENT_ID;
    private final String studentId2 = CTBConstants.STUDENT_ID2;
    
    private final String contactName = CTBConstants.CONTACT_NAME;
	private final String address1 = CTBConstants.ADDRESS_LINE_1;
	private final String address2 = CTBConstants.ADDRESS_LINE_2;
	private final String address3 = CTBConstants.ADDRESS_LINE_3;
	private final String email = CTBConstants.EMAIL;
	private final String timeZone = CTBConstants.REQUIREDFIELD_TIME_ZONE;
	private final String city = CTBConstants.CITY;
	private final String state = CTBConstants.STATE_NAME;
	private final String zip = CTBConstants.ZIP;
	private final String primaryPhone = CTBConstants.PRIMARY_PHONE;
	private final String secondaryPhone = CTBConstants.SECONDARY_PHONE;
	private final String faxNumber = CTBConstants.FAX;

    private final String screenReader = CTBConstants.SCREEN_READER ;
    private final String calculator = CTBConstants.CALCULATOR ; 
    private final String testPause = CTBConstants.TEST_PAUSE;
    private final String untimedTest = CTBConstants.UNTIMED_TEST;
    private final String highlighter = CTBConstants.HIGHLIGHTER ;
    
    private final String questionBackgroundColor = CTBConstants.QUESTION_BACKGROUND_COLOR ;
    private final String questionFontColor = CTBConstants.QUESTION_FONT_COLOR ;
    private final String questionFontSize = CTBConstants.QUESTION_FONT_SIZE ;
    private final String answerBackgroundColor = CTBConstants.ANSWER_BACKGROUND_COLOR ;
    private final String answerFontColor = CTBConstants.ANSWER_FONT_COLOR ;
    private final String answereFontSize = CTBConstants.FONT_SIZE;
    
    private ArrayList studentHeaderList = new ArrayList ();
    


    public ArrayList getStudentHeaderList() {
        return studentHeaderList;
    }    
    
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @return the birthDate
	 */
	public String getBirthDate() {
		return birthDate;
	}
    
    /**
     * @return the headerDateOfBirthDate
     */
    public String getHeaderDateOfBirthDate(){
       return headerDateOfBirthDate;
    }
	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @return the studentId
	 */
	public String getStudentId() {
		return studentId;
	}
	/**
	 * @return the studentId2
	 */
	public String getStudentId2() {
		return studentId2;
	}
	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}
	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}
	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}
	/**
	 * @return the address3
	 */
	public String getAddress3() {
		return address3;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @return the primaryPhone
	 */
	public String getPrimaryPhone() {
		return primaryPhone;
	}
	/**
	 * @return the secondaryPhone
	 */
	public String getSecondaryPhone() {
		return secondaryPhone;
	}
	/**
	 * @return the faxNumber
	 */
	public String getFaxNumber() {
		return faxNumber;
	}
	/**
	 * @return the screenReader
	 */
	public String getScreenReader() {
		return screenReader;
	}
	/**
	 * @return the calculator
	 */
	public String getCalculator() {
		return calculator;
	}
	/**
	 * @return the testPause
	 */
	public String getTestPause() {
		return testPause;
	}
	/**
	 * @return the untimedTest
	 */
	public String getUntimedTest() {
		return untimedTest;
	}
	/**
	 * @return the highlighter
	 */
	public String getHighlighter() {
		return highlighter;
	}
	/**
	 * @return the questionBackgroundColor
	 */
	public String getQuestionBackgroundColor() {
		return questionBackgroundColor;
	}
	/**
	 * @return the questionFontColor
	 */
	public String getQuestionFontColor() {
		return questionFontColor;
	}
	/**
	 * @return the questionFontSize
	 */
	public String getQuestionFontSize() {
		return questionFontSize;
	}
	/**
	 * @return the answerBackgroundColor
	 */
	public String getAnswerBackgroundColor() {
		return answerBackgroundColor;
	}
	/**
	 * @return the answerFontColor
	 */
	public String getAnswerFontColor() {
		return answerFontColor;
	}
	/**
	 * @return the answereFontSize
	 */
	public String getAnswereFontSize() {
		return answereFontSize;
	}

 
    
}
