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
    private static final String[] fontSizes = { "1", "1.5" };
    private static final String[] fontSizeLabels = { "Standard", "Large" };
    
    public static final String DEFAULT_QUESTION_BGRDCOLOR = colorHexs[3];   // White
    public static final String DEFAULT_QUESTION_FONTSIZE = fontSizes[0];    // Light blue
    public static final String DEFAULT_QUESTION_FONTCOLOR = colorHexs[4];   // Black
    public static final String DEFAULT_ANSWER_BGRDCOLOR = colorHexs[2];     // Light yellow
    public static final String DEFAULT_ANSWER_FONTSIZE = fontSizes[0];      // Light blue
    public static final String DEFAULT_ANSWER_FONTCOLOR = colorHexs[4];     // Black
    public static final String DEFAULT_FONT_SIZE = fontSizes[0];            // 1 == 14px
	
    public static StudentAccommodations makeCopy(Integer studentId, Accommodation accom)
    {
        StudentAccommodations copied = new StudentAccommodations();    
        boolean hasData = false;
        
        copied.setStudentId(studentId);
        
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

        copied.setScreenReader("");		// not used
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

    public static StudentAccommodations createDefault(Integer studentId)
    {
        StudentAccommodations copied = new StudentAccommodations();    
        
        copied.setStudentId(studentId);
        copied.setCalculator("F");
        copied.setTestPause("F");
        copied.setUntimedTest("F");
        copied.setHighlighter("F");
        copied.setScreenReader("F");	
        copied.setScreenMagnifier("");  
        copied.setStudentGrade("");     
        copied.setColorFontAccommodation("F");
        copied.setQuestionBackgroundColor(null);
        copied.setQuestionFontColor(null);
        copied.setQuestionFontSize(null);
        copied.setAnswerBackgroundColor(null);
        copied.setAnswerFontColor(null);
        copied.setAnswerFontSize(null);
        
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
