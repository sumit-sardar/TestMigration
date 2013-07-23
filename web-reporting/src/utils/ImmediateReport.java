package utils;

public interface ImmediateReport {

	public static final float LINE_WIDTH = 715f;
	public static final float LEFT_X = 32f;
	public static final float TITLE_VALUE_WIDTH = 500f;
	public static final float TITLE_VALUE_X = 250f;
	public static final float TITLE_Y = 580f;
	public static final float FOOTER_WIDTH = 700f;
	public static final float FOOTER_Y = 73f;
	public static final float INFO_LABEL_WIDTH = 100f;
	public static final float INFO_VALUE_WIDTH = 500f;
	public static final float INFO_VALUE_X = 180f;
	
	public static final float TITLE_LINE_SPACING = 5f;
	public static final float LINE_TEST_NAME_SPACING = 10f;
	public static final float LINE_ROSTER_DATA_SPACING = 4f;
	public static final float PAGE_WIDTH = 715f;
	public static final float[] FOUR_COLUMN_TAC_WIDTHS = new float[] {4f, 3f, 3f, 3f};
	public static final float SCORE_TABLE_SPACING = 30f;
	public static final float SCORE_BORDER = 1f;
	
	// Added for Academic Language Report
	public static final float[] THREE_COLUMN_TAC_WIDTHS_FOR_ACADEMIC = new float[] {4f, 3f, 3f, 3f, 3f}; 
	public static final float[] THREE_SUB_COLUMN_TAC_WIDTHS = new float[] {4f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
//	public static final float[] THREE_COLUMN_TAC_WIDTHS_FOR_ACADEMIC_TOTAL = new float[] {4f, 1f, 1f, 1f, 1f}; 
	public static final float SCORE_TABLE_SPACING_FOR_ACADEMIC = 16f;
	public static final float SCORE_TABLE_SPACING_FOR_TOTAL = 173f;
	public static final String ACADEMICTOTALSCORECONDITION = "*Total Score = Social, Intercultural, and Instructional Communication + Academic Score";
	
	public static final String COPYWRITE = "Developed and published by CTB/McGraw-Hill LLC, 20 Ryan Ranch Road, Monterey, California 93940-5703. Copyright � 2013 by CTB/McGraw-Hill LLC. All rights reserved. Only authorized customers may copy, download or print any portion of the document located at www.ctb.com. Any other use or reproduction of this document, in whole or in part, requires written permission of the publisher. \"OAS\" is a trademark of McGraw-Hill Education. All other trademarks and trade names found here are the property of their respective owners and are not associated with the publisher of this OAS.";
	public static final String STUDENT_NAME_LABEL = "Student: ";
	public static final String STUDENT_Id_LABEL = "ID: ";
	public static final String TEST_DATE_LABEL = "Test Date: ";
	public static final String FORM_LABEL = "Form: ";
	public static final String DISTRICT_LABEL = "District: ";
	public static final String SCHOOL_LABEL = "School: ";
	public static final String GRADE_LABEL = "Grade: ";
	public static final String TEST_NAME_LABEL = "Test Name: ";
	public static final String RAW_SCORE = "Raw Score ";
	public static final String SCALE_SCORE = "Scale Score ";
	public static final String PROFICIENCY_LEVEL = "Proficiency Level ";
	
	public static final String STUDENT_NAME_LABEL_CSV = "Student";
	public static final String STUDENT_Id_LABEL_CSV = "ID";
	public static final String TEST_DATE_LABEL_CSV = "Test Date";
	public static final String FORM_LABEL_CSV = "Form";
	public static final String DISTRICT_LABEL_CSV = "District";
	public static final String SCHOOL_LABEL_CSV = "School";
	public static final String GRADE_LABEL_CSV = "Grade";
	public static final String TEST_NAME_LABEL_CSV = "Test Name";
	public static final String RAW_SCORE_CSV= "Raw Score";
	public static final String SCALE_SCORE_CSV = "Scale Score";
	public static final String PROFICIENCY_LEVEL_CSV = "Proficiency Level";
	
	public static final String LISTENING_RAW_SCORE_CSV = "Listening / Raw Score";
	public static final String SPEAKING_RAW_SCORE_CSV = "Speaking / Raw Score";
	public static final String ORAL_RAW_SCORE_CSV = "Oral / Raw Score";
	public static final String READING_RAW_SCORE_CSV = "Reading / Raw Score";
	public static final String WRITING_RAW_SCORE_CSV = "Writing / Raw Score";
	public static final String COMPREHENSION_RAW_SCORE_CSV = "Comprehension / Raw Score";
	public static final String OVERALL_RAW_SCORE_CSV = "Overall / Raw Score";
	public static final String PRODUCTIVE_RAW_SCORE_CSV = "Productive / Raw Score";
	public static final String LITERACY_RAW_SCORE_CSV	= "Literacy /Raw Score";
	
	public static final String LISTENING_SCALE_SCORE_CSV = "Listening / Scale Score";
	public static final String SPEAKING_SCALE_SCORE_CSV = "Speaking / Scale Score";
	public static final String ORAL_SCALE_SCORE_CSV = "Oral / Scale Score";
	public static final String READING_SCALE_SCORE_CSV = "Reading / Scale Score";
	public static final String WRITING_SCALE_SCORE_CSV = "Writing / Scale Score";
	public static final String COMPREHENSION_SCALE_SCORE_CSV = "Comprehension / Scale Score";
	public static final String OVERALL_SCALE_SCORE_CSV = "Overall / Scale Score";
	public static final String PRODUCTIVE_SCALE_SCORE_CSV = "Productive / Scale Score";
	public static final String LITERACY_SCALE_SCORE_CSV ="Literacy / Scale Score";
	
	public static final String LISTENING_PROFICIENCY_LEVEL_CSV = "Listening / PL";
	public static final String SPEAKING_PROFICIENCY_LEVEL_CSV = "Speaking / PL";
	public static final String ORAL_PROFICIENCY_LEVEL_CSV = "Oral / PL";
	public static final String READING_PROFICIENCY_LEVEL_CSV = "Reading / PL";
	public static final String WRITING_PROFICIENCY_LEVEL_CSV = "Writing / PL";
	public static final String COMPREHENSION_PROFICIENCY_LEVEL_CSV = "Comprehension / PL";
	public static final String OVERALL_PROFICIENCY_LEVEL_CSV = "Overall / PL";
	public static final String PRODUCTIVE_PROFICIENCY_LEVEL_CSV = "Productive / PL";
	public static final String LITERACY_PROFICIENCY_LEVEL_CSV ="Literacy / PL";

	public static final String ACADEMIC_LANGUAGE_REPORT = "Academic Language Report";
	
	public static final String PtsPossible = "Pts Possible";
	public static final String PtsObtained = "Pts Obtained";
	public static final String PerCorrect = "% Correct";
	public static final String TotalSpeaking = "Total Speaking";
	public static final String TotalListening = "Total Listening";
	public static final String TotalReading = "Total Reading";
	public static final String TotalWriting = "Total Writing";
	
	public static final String SPEAKING = "Speaking";
	public static final String LISTENING = "Listening";
	public static final String READING = "Reading";
	public static final String WRITING = "Writing";
}
