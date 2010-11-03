 //intake and student-record editing UI
package dto;

import java.io.Serializable;
import com.ctb.bean.testAdmin.StudentAccommodations;

public class StudentAccommodationsDetail implements java.io.Serializable {
    static final long serialVersionUID = 1L;

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

    
    private Boolean colorFont = Boolean.FALSE;    
    private Boolean screenReader = Boolean.FALSE;    
    private Boolean calculator = Boolean.FALSE;    
    private Boolean testPause = Boolean.FALSE;    
    private Boolean untimedTest = Boolean.FALSE;
    private Boolean highlighter = Boolean.FALSE;    
    
    private String question_bgrdColor = DEFAULT_QUESTION_BGRDCOLOR;
    private String question_fontColor = DEFAULT_QUESTION_FONTCOLOR;

    private String answer_bgrdColor = DEFAULT_ANSWER_BGRDCOLOR;
    private String answer_fontColor = DEFAULT_ANSWER_FONTCOLOR;
 
    private String fontSize = DEFAULT_ANSWER_FONTSIZE;
    
    public StudentAccommodationsDetail() {
    }

    public StudentAccommodationsDetail(StudentAccommodations sa) 
    {
        if (sa.getCalculator() != null)
            this.calculator = new Boolean(sa.getCalculator().equals("T"));
            
        if (sa.getScreenReader() != null)
            this.screenReader = new Boolean(sa.getScreenReader().equals("T"));
            
        if (sa.getTestPause() != null)
            this.testPause = new Boolean(sa.getTestPause().equals("T"));
            
        if (sa.getUntimedTest() != null)
            this.untimedTest = new Boolean(sa.getUntimedTest().equals("T"));
            
        //depends upon the highlighter value of StudentAccommodations set the highlighter value of StudentAccommodationsDetail.
        if (sa.getHighlighter() != null)
            this.highlighter = new Boolean(sa.getHighlighter().equals("T"));
            
        
        this.colorFont = Boolean.FALSE;
            
        if (sa.getQuestionBackgroundColor() != null) {
            this.question_bgrdColor = sa.getQuestionBackgroundColor();
            this.colorFont = Boolean.TRUE;
        }
        
        if (sa.getQuestionFontColor() != null) {
            this.question_fontColor = sa.getQuestionFontColor();
            this.colorFont = Boolean.TRUE;
        }

        if (sa.getAnswerBackgroundColor() != null) {
            this.answer_bgrdColor = sa.getAnswerBackgroundColor();
            this.colorFont = Boolean.TRUE;
        }

        if (sa.getAnswerFontColor() != null) {
            this.answer_fontColor = sa.getAnswerFontColor();
            this.colorFont = Boolean.TRUE;
        }

        if (sa.getQuestionFontSize() != null) {
            this.fontSize = sa.getQuestionFontSize();
            this.colorFont = Boolean.TRUE;
        }
    }

    public StudentAccommodations makeCopy(Integer studentId) 
    {
        StudentAccommodations copied = new StudentAccommodations();    
        boolean hasData = false;
        
        copied.setStudentId(studentId);
        
        if (this.screenReader.booleanValue()) {
            copied.setScreenReader("T");
            hasData = true;
        }
        else {
            copied.setScreenReader("F");
        }
        
        if (this.calculator.booleanValue()) {
            copied.setCalculator("T");
            hasData = true;
        }
        else {
            copied.setCalculator("F");
        }
        
        if (this.testPause.booleanValue()) {
            copied.setTestPause("T");
            hasData = true;
        }
        else {
            copied.setTestPause("F");
        }
        
        if (this.untimedTest.booleanValue()) {
            copied.setUntimedTest("T");
            hasData = true;
        }
        else {
            copied.setUntimedTest("F");
        }
        //if highlighter set true in StudentAccommodationsDetail then set highlighter value true  in StudentAccommodations.Otherwise set highlighter false.
        if (this.highlighter.booleanValue()) {
            copied.setHighlighter("T");
            hasData = true;
        }
        else {
            copied.setHighlighter("F");
        }
        
        if (this.colorFont.booleanValue()) {
            copied.setColorFontAccommodation("T");

            String value = getColorHexMapping(this.question_bgrdColor);
            copied.setQuestionBackgroundColor(value);
            
            value = getColorHexMapping(this.question_fontColor);
            copied.setQuestionFontColor(value);
            
            copied.setQuestionFontSize(this.fontSize);
            
            value = getColorHexMapping(this.answer_bgrdColor);
            copied.setAnswerBackgroundColor(value);
            
            value = getColorHexMapping(this.answer_fontColor);
            copied.setAnswerFontColor(value);
            
            copied.setAnswerFontSize(this.fontSize);

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
        
        copied.setScreenMagnifier("");  // not used
        copied.setStudentGrade("");     // not used
        
        return copied;
    }

    public StudentAccommodationsDetail createClone() {
        StudentAccommodationsDetail copied = new StudentAccommodationsDetail();
        
        copied.setColorFont(this.colorFont);

        copied.setScreenReader(this.screenReader);
        copied.setCalculator(this.calculator);
        copied.setTestPause(this.testPause);
        copied.setUntimedTest(this.untimedTest);
        copied.setHighlighter(this.highlighter);
        
        copied.setQuestion_bgrdColor(this.question_bgrdColor);
        copied.setQuestion_fontColor(this.question_fontColor);
        
        copied.setAnswer_bgrdColor(this.answer_bgrdColor);
        copied.setAnswer_fontColor(this.answer_fontColor);

        copied.setFontSize(this.fontSize);

        return copied;
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
    public Boolean getCalculator() {
        return calculator;
    }
    public void setCalculator(Boolean calculator) {
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
    public String getFontSize() {
        return this.fontSize;
    }
    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }
    public Boolean getScreenReader() {
        return screenReader;
    }
    public void setScreenReader(Boolean screenReader) {
        this.screenReader = screenReader;
    }
    public Boolean getTestPause() {
        return testPause;
    }
    public void setTestPause(Boolean testPause) {
        this.testPause = testPause;
    }
    public Boolean getUntimedTest() {
        return untimedTest;
    }
    public void setUntimedTest(Boolean untimedTest) {
        this.untimedTest = untimedTest;
    }
    /**
	 * @return Returns the highlighter.
	 */
    public Boolean getHighlighter() {
        return highlighter;
    }
    /**
	 * @param highlighter The highlighter to set.
	 */
    public void setHighlighter(Boolean highlighter) {
        this.highlighter = highlighter;
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
    public String getColorHexMapping(String color) {
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
    public boolean validateInput() 
    {
        if( (this.screenReader.booleanValue() == false) && 
			(this.calculator.booleanValue() == false) && 
			(this.testPause.booleanValue() == false) && 
			(this.untimedTest.booleanValue() == false) && 
            (this.highlighter.booleanValue() == false) && 
        	(this.colorFont.booleanValue() == false)) {
            return false;
        }
        
        return true;
    }
    
    public void convertHexToText() 
    {
        this.question_bgrdColor = getColorTextMapping(this.question_bgrdColor);
        this.question_fontColor = getColorTextMapping(this.question_fontColor);
        this.answer_bgrdColor = getColorTextMapping(this.answer_bgrdColor);
        this.answer_fontColor = getColorTextMapping(this.answer_fontColor);
    }
    
    public void convertTextToHex() 
    {
        this.question_bgrdColor = getColorHexMapping(this.question_bgrdColor);
        this.question_fontColor = getColorHexMapping(this.question_fontColor);
        this.answer_bgrdColor = getColorHexMapping(this.answer_bgrdColor);
        this.answer_fontColor = getColorHexMapping(this.answer_fontColor);
    }
    
    public String getDisplayFontSize()
    {
        return getFontSizeLabelMapping(this.fontSize);
    }
    
    public boolean isValueSelected() 
    {
        if (this.screenReader.equals("true") ||
            this.calculator.equals("true") || this.testPause.equals("true") ||
            this.untimedTest.equals("true") || this.highlighter.equals("true") || 
            this.colorFont.booleanValue() ) {
            
            return true;
        }                
        return false;
    }
    
    public String getQuestionColorStyle() 
    {        
        int baseSize = 14; // Not to be confused with size used within the client, just a visual cue.
        int displayedSize = (int) (baseSize * Float.valueOf(this.fontSize).floatValue());
        
        String bgrdColor = getColorHexMapping(this.question_bgrdColor);
        String fontColor = getColorHexMapping(this.question_fontColor);
                
        String style = "background-color: " + bgrdColor + "; ";
        style += "border-color: #333; ";
        style += "border-style: solid; ";
        style += "border-width: 1px; ";
        style += "color: " + fontColor + "; ";
        style += "text-align: center; ";
        style += "vertical-align: middle; ";
        style += "font-size: " + String.valueOf( displayedSize ) + "px;";
        
        return style;
    }

    public String getAnswerColorStyle() 
    {        
        int baseSize = 14; // Not to be confused with size used within the client, just a visual cue.
        int displayedSize = (int) (baseSize * Float.valueOf(this.fontSize).floatValue());
        
        String bgrdColor = getColorHexMapping(this.answer_bgrdColor);
        String fontColor = getColorHexMapping(this.answer_fontColor);
        
        String style = "background-color: " + bgrdColor + "; ";
        style += "border-color: #333; ";
        style += "border-style: solid; ";
        style += "border-width: 1px; ";
        style += "color: " + fontColor + "; ";
        style += "text-align: center; ";
        style += "vertical-align: middle; ";
        style += "font-size: " + String.valueOf( displayedSize ) + "px;";
        
        return style;
    }
    
}

