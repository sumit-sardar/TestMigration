package com.ctb.util;

import com.ctb.bean.testAdmin.StudentAccommodations;

import dto.Accommodation;


public class AccommodationUtil {

    private static final String[] colorTexts = {"Light blue", "Light pink", "Light yellow", 
        "White",      "Black",      "Dark blue", 
        "Dark brown", "Yellow",     "Green"
       };
    private static final String[] colorHexs = { "#CCECFF",    "#FFCCCC",    "#FFFFB0",
        "#FFFFFF",    "#000000",    "#000080",
        "#663300",    "#FFFF99",    "#00CC00"
      };
	
    public static StudentAccommodations makeCopy(Integer studentId, Accommodation accom)
    {
        StudentAccommodations copied = new StudentAccommodations();    
        boolean hasData = false;
        
        copied.setStudentId(studentId);
        
        if ((accom.getScreenReader() != null) && accom.getScreenReader().booleanValue()) {
            copied.setScreenReader("T");
            hasData = true;
        }
        else {
            copied.setScreenReader("F");
        }
        
        if ((accom.getCalculator() != null) && accom.getCalculator().booleanValue()) {
            copied.setCalculator("T");
            hasData = true;
        }
        else {
            copied.setCalculator("F");
        }
        
        if ((accom.getTestPause() != null) && accom.getTestPause().booleanValue()) {
            copied.setTestPause("T");
            hasData = true;
        }
        else {
            copied.setTestPause("F");
        }
        
        if ((accom.getUntimedTest() != null) && accom.getUntimedTest().booleanValue()) {
            copied.setUntimedTest("T");
            hasData = true;
        }
        else {
            copied.setUntimedTest("F");
        }
        
        if ((accom.getHighlighter() != null) && accom.getHighlighter().booleanValue()) {
            copied.setHighlighter("T");
            hasData = true;
        }
        else {
            copied.setHighlighter("F");
        }

        copied.setScreenMagnifier("");  // not used
        copied.setStudentGrade("");     // not used
        
        if (accom.getFontSize() != null) {
            copied.setColorFontAccommodation("T");

            String value = getColorHexMapping(accom.getQuestionBackgroundColor());
            copied.setQuestionBackgroundColor(value);
            
            value = getColorHexMapping(accom.getQuestionFontColor());
            copied.setQuestionFontColor(value);
            
            copied.setQuestionFontSize(accom.getFontSize());
            
            value = getColorHexMapping(accom.getAnswerBackgroundColor());
            copied.setAnswerBackgroundColor(value);
            
            value = getColorHexMapping(accom.getAnswerFontColor());
            copied.setAnswerFontColor(value);
            
            copied.setAnswerFontSize(accom.getFontSize());

            hasData = true;
        }
        else {
            copied.setColorFontAccommodation("F");

            copied.setQuestionBackgroundColor(null);
            copied.setQuestionFontColor(null);
            copied.setQuestionFontSize(null);
            
            copied.setAnswerBackgroundColor(null);
            copied.setAnswerFontColor(null);
            copied.setAnswerFontSize(null);
        }
        
        return copied;    	
    }
    
    private static String getColorHexMapping(String color) {
    	if (color != null) {
	        for (int i=0 ; i<colorTexts.length ; i++) {
	            if (color.equals(colorTexts[i]))
	                return colorHexs[i];
	        }
    	}
        return colorHexs[0];
    }
    
}
