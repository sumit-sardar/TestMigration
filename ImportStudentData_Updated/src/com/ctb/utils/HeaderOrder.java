package com.ctb.utils;

import java.util.ArrayList;

public class HeaderOrder {
public static ArrayList<String> getStudentHeaderOrderList() {        
        ArrayList<String> headerArray = new ArrayList<String>();        
        headerArray.add(Constants.REQUIREDFIELD_FIRST_NAME);
        headerArray.add(Constants.MIDDLE_NAME);
        headerArray.add(Constants.REQUIREDFIELD_LAST_NAME);
        headerArray.add(Constants.REQUIREDFIELD_DATE_OF_BIRTH);
        headerArray.add(Constants.REQUIREDFIELD_GRADE);
        headerArray.add(Constants.REQUIREDFIELD_GENDER);
        headerArray.add(Constants.STUDENT_ID);
        headerArray.add(Constants.STUDENT_ID2);
        headerArray.add(Constants.SCREEN_READER);
        headerArray.add(Constants.CALCULATOR);
        headerArray.add(Constants.TEST_PAUSE);
        headerArray.add(Constants.UNTIMED_TEST);
        headerArray.add(Constants.HIGHLIGHTER);
        headerArray.add(Constants.QUESTION_BACKGROUND_COLOR);
        headerArray.add(Constants.QUESTION_FONT_COLOR);
        headerArray.add(Constants.ANSWER_BACKGROUND_COLOR);
        headerArray.add(Constants.ANSWER_FONT_COLOR);
        headerArray.add(Constants.FONT_SIZE);       
        return headerArray;
    }
}
