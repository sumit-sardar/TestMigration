package com.ctb.utils;

import java.util.ArrayList;

public class StudentHeader {
	 private final String firstName = Constants.REQUIREDFIELD_FIRST_NAME;
	    private final String middleName = Constants.MIDDLE_NAME ;
	    private final String lastName = Constants.REQUIREDFIELD_LAST_NAME ;
	    private final String birthDate = Constants.REQUIREDFIELD_DATE_OF_BIRTH ;
	    private final String headerDateOfBirthDate = Constants.REQUIREDFIELD_DATE_OF_BIRTH ;
	    private final String grade = Constants.REQUIREDFIELD_GRADE ;
	    private final String gender = Constants.REQUIREDFIELD_GENDER;
	    private final String studentId = Constants.STUDENT_ID;
	    private final String studentId2 = Constants.STUDENT_ID2;   
	    
	    private final String screenReader = Constants.SCREEN_READER ;
	    private final String calculator = Constants.CALCULATOR ; 
	    private final String testPause = Constants.TEST_PAUSE;
	    private final String untimedTest = Constants.UNTIMED_TEST;
	    private final String highlighter = Constants.HIGHLIGHTER ;
	    
	    private final String questionBackgroundColor = Constants.QUESTION_BACKGROUND_COLOR ;
	    private final String questionFontColor = Constants.QUESTION_FONT_COLOR ;
	    private final String questionFontSize = Constants.QUESTION_FONT_SIZE ;
	    private final String answerBackgroundColor = Constants.ANSWER_BACKGROUND_COLOR ;
	    private final String answerFontColor = Constants.ANSWER_FONT_COLOR ;
	    private final String answereFontSize = Constants.FONT_SIZE;
	    
	    private ArrayList studentHeaderList = new ArrayList ();

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
		public String getHeaderDateOfBirthDate() {
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

		/**
		 * @return the studentHeaderList
		 */
		public ArrayList getStudentHeaderList() {
			return studentHeaderList;
		}
	    
	    
}
