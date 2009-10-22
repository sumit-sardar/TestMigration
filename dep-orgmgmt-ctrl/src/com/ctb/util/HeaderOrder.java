package com.ctb.util; 
/*
 * returns the order of the header for both users and students
 * @author  Tata Consultancy Services
*/
import java.util.ArrayList;

public class HeaderOrder 
{ 
    public static ArrayList getUserHeaderOrderList() {
        
        ArrayList headerArray = new ArrayList();
        
        headerArray.add(CTBConstants.REQUIREDFIELD_FIRST_NAME);
        headerArray.add(CTBConstants.MIDDLE_NAME);
        headerArray.add(CTBConstants.REQUIREDFIELD_LAST_NAME);
        headerArray.add(CTBConstants.EMAIL);
        headerArray.add(CTBConstants.REQUIREDFIELD_TIME_ZONE);
        headerArray.add(CTBConstants.REQUIREDFIELD_ROLE);
        headerArray.add(CTBConstants.EXT_PIN1);
        headerArray.add(CTBConstants.ADDRESS_LINE_1);
        headerArray.add(CTBConstants.ADDRESS_LINE_2);
        headerArray.add(CTBConstants.CITY);
        headerArray.add(CTBConstants.STATE_NAME);
        headerArray.add(CTBConstants.ZIP);
        headerArray.add(CTBConstants.PRIMARY_PHONE);
        headerArray.add(CTBConstants.SECONDARY_PHONE);
        headerArray.add(CTBConstants.FAX);
        
        return headerArray;
    }
    
    public static ArrayList getStudentHeaderOrderList() {
        
        ArrayList headerArray = new ArrayList();
        
        headerArray.add(CTBConstants.REQUIREDFIELD_FIRST_NAME);
        headerArray.add(CTBConstants.MIDDLE_NAME);
        headerArray.add(CTBConstants.REQUIREDFIELD_LAST_NAME);
        headerArray.add(CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH);
        headerArray.add(CTBConstants.REQUIREDFIELD_GRADE);
        headerArray.add(CTBConstants.REQUIREDFIELD_GENDER);
        headerArray.add(CTBConstants.STUDENT_ID);
        headerArray.add(CTBConstants.STUDENT_ID2);
        headerArray.add(CTBConstants.SCREEN_READER);
        headerArray.add(CTBConstants.CALCULATOR);
        headerArray.add(CTBConstants.TEST_PAUSE);
        headerArray.add(CTBConstants.UNTIMED_TEST);
        headerArray.add(CTBConstants.HIGHLIGHTER);
        headerArray.add(CTBConstants.QUESTION_BACKGROUND_COLOR);
        headerArray.add(CTBConstants.QUESTION_FONT_COLOR);
        headerArray.add(CTBConstants.ANSWER_BACKGROUND_COLOR);
        headerArray.add(CTBConstants.ANSWER_FONT_COLOR);
        headerArray.add(CTBConstants.FONT_SIZE);
       
        return headerArray;
    }
} 
