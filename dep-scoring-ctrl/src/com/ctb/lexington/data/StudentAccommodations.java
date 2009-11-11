package com.ctb.lexington.data;

import java.io.Serializable;

import com.ctb.lexington.util.Stringx;

public class StudentAccommodations implements Serializable {

    private static final String[] colorTexts = {"Light blue", "Light pink", "Light yellow", 
    	                                        "White",      "Black",      "Dark blue", 
    	                                        "Dark brown", "Yellow",     "Green"
    	                                       };
    private static final String[] colorHexs = { "#CCECFF",    "#FFCCCC",    "#FFFFB0",
    	                                        "#FFFFFF",    "#000000",    "#000080",
    	                                        "#663300",    "#FFFF99",    "#00CC00"
    	                                      };
    private static final String[] fontSizes = { "1", "1.5", "2" };
    private static final String[] fontSizeLabels = { "Standard", "Larger", "Largest" };
    
    public static final String DEFAULT_QUESTION_BGRDCOLOR = colorHexs[3];
    public static final String DEFAULT_QUESTION_FONTSIZE = fontSizes[0];
    public static final String DEFAULT_QUESTION_FONTCOLOR = colorHexs[4];
    public static final String DEFAULT_ANSWER_BGRDCOLOR = colorHexs[2];
    public static final String DEFAULT_ANSWER_FONTSIZE = fontSizes[0];
    public static final String DEFAULT_ANSWER_FONTCOLOR = colorHexs[4];
    public static final String DEFAULT_FONT_SIZE = fontSizes[0];

    
    private Boolean requiredAccommodations = null;    
    private Boolean colorFont = null;    

    private String screenMagnifier = null;    
    private String screenReader = null;    
    private String calculator = null;    
    private String testPause = null;    
    private String untimedTest = null;    
    
    private String question_bgrdColor = DEFAULT_QUESTION_BGRDCOLOR;
    private String question_fontSize = DEFAULT_QUESTION_FONTSIZE;
    private String question_fontColor = DEFAULT_QUESTION_FONTCOLOR;
    private String answer_bgrdColor = DEFAULT_ANSWER_BGRDCOLOR;
    private String answer_fontSize = DEFAULT_ANSWER_FONTSIZE;
    private String answer_fontColor = DEFAULT_ANSWER_FONTCOLOR;
 

    
    
    public StudentAccommodations() {
	    this.requiredAccommodations = Boolean.FALSE;    
        this.colorFont = Boolean.FALSE;
    }


    public String getAnswer_bgrdColor() {
        return answer_bgrdColor;
    }
    public void setAnswer_bgrdColor(String answer_bgrdColor) {
        this.answer_bgrdColor = answer_bgrdColor;
    }
    public String getAnswer_fontColor() {
        return answer_fontColor;
    }
    public void setAnswer_fontColor(String answer_fontColor) {
        this.answer_fontColor = answer_fontColor;
    }
    public String getAnswer_fontSize() {
        return answer_fontSize;
    }
    public void setAnswer_fontSize(String answer_fontSize) {
        this.answer_fontSize = answer_fontSize;
    }
    public String getCalculator() {
        return calculator;
    }
    public void setCalculator(String calculator) {
        this.calculator = calculator;
    }
    public String getQuestion_bgrdColor() {
        return question_bgrdColor;
    }
    public void setQuestion_bgrdColor(String question_bgrdColor) {
        this.question_bgrdColor = question_bgrdColor;
    }
    public String getQuestion_fontColor() {
        return question_fontColor;
    }
    public void setQuestion_fontColor(String question_fontColor) {
        this.question_fontColor = question_fontColor;
    }
    public String getQuestion_fontSize() {
        return question_fontSize;
    }
    public void setQuestion_fontSize(String question_fontSize) {
        this.question_fontSize = question_fontSize;
    }
    public String getScreenMagnifier() {
        return screenMagnifier;
    }
    public void setScreenMagnifier(String screenMagnifier) {
        this.screenMagnifier = screenMagnifier;
    }
    public String getScreenReader() {
        return screenReader;
    }
    public void setScreenReader(String screenReader) {
        this.screenReader = screenReader;
    }
    public String getTestPause() {
        return testPause;
    }
    public void setTestPause(String testPause) {
        this.testPause = testPause;
    }
    public String getUntimedTest() {
        return untimedTest;
    }
    public void setUntimedTest(String untimedTest) {
        this.untimedTest = untimedTest;
    }
    public Boolean getRequiredAccommodations() {
        return requiredAccommodations;
    }
    public void setRequiredAccommodations(Boolean requiredAccommodations) {
        this.requiredAccommodations = requiredAccommodations;
    }
    public Boolean getColorFont() {
        return colorFont;
    }
    public void setColorFont(Boolean colorFont) {
        this.colorFont = colorFont;
    }
    public String getColorTextMapping(String hexValue) {
        for (int i=0 ; i<colorHexs.length ; i++) {
            if (hexValue.equals(colorHexs[i]))
                return colorTexts[i];
        }
        return colorTexts[0];
    }
    public String getHexValueMapping(String color) {
        for (int i=0 ; i<colorTexts.length ; i++) {
            if (color.equals(colorTexts[i]))
                return colorHexs[i];
        }
        return colorHexs[0];
    }
    public String getColorText(int index) {
        return colorTexts[index];    
    }
    public String getHexValue(int index) {
        return colorHexs[index];    
    }
    public String getFontSize(int index) {
        return fontSizes[index];    
    }
    public String getFontSizeLabel(int index) {
        return fontSizeLabels[index];    
    }
    public String getFontSizeLabelMapping(String fontSize) {
        for( int i=0 ; i < fontSizeLabels.length; i++ ) {
            if( fontSize.equals(fontSizes[i]) )
                return fontSizeLabels[i];
        }
        return fontSizeLabels[0];
    }
    public boolean validateInput() {
	    
        if( this.requiredAccommodations.booleanValue() == false )
            return true;
        
        if( Stringx.isEmpty(this.screenMagnifier) && 
        	Stringx.isEmpty(this.screenReader) &&   
			Stringx.isEmpty(this.calculator) && 
			Stringx.isEmpty(this.testPause) && 
			Stringx.isEmpty(this.untimedTest)&& 
        	this.colorFont.booleanValue() == false ) {
            return false;
        }
        
        return true;
    }
    
}

