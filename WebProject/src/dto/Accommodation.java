package dto;

/**
 * This object contains information for student accommodations
 * None of these values required from ACUITY through input 
 * or populated by OAS in return.  
 * 
 * @author Tai_Truong
 */
public class Accommodation implements java.io.Serializable {
    static final long serialVersionUID = 1L;

    private Boolean screenMagnifier = null;
    private Boolean screenReader = null;
    private Boolean calculator = null; 
    private Boolean testPause = null;
    private Boolean untimedTest = null;
    private String questionBackgroundColor = null;	// 32 chars
    private String questionFontColor = null;		// 32 chars
    private String questionFontSize = null;			// 32 chars
    private String answerBackgroundColor = null;	// 32 chars
    private String answerFontColor = null;			// 32 chars
    private String answerFontSize = null;			// 32 chars
    private Boolean highlighter = null;
    
	public Accommodation() {
	}

	public Boolean getScreenMagnifier() {
		return screenMagnifier;
	}

	public void setScreenMagnifier(Boolean screenMagnifier) {
		this.screenMagnifier = screenMagnifier;
	}

	public Boolean getScreenReader() {
		return screenReader;
	}

	public void setScreenReader(Boolean screenReader) {
		this.screenReader = screenReader;
	}

	public Boolean getCalculator() {
		return calculator;
	}

	public void setCalculator(Boolean calculator) {
		this.calculator = calculator;
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

	public String getQuestionBackgroundColor() {
		return questionBackgroundColor;
	}

	public void setQuestionBackgroundColor(String questionBackgroundColor) {
		this.questionBackgroundColor = questionBackgroundColor;
	}

	public String getQuestionFontColor() {
		return questionFontColor;
	}

	public void setQuestionFontColor(String questionFontColor) {
		this.questionFontColor = questionFontColor;
	}

	public String getQuestionFontSize() {
		return questionFontSize;
	}

	public void setQuestionFontSize(String questionFontSize) {
		this.questionFontSize = questionFontSize;
	}

	public String getAnswerBackgroundColor() {
		return answerBackgroundColor;
	}

	public void setAnswerBackgroundColor(String answerBackgroundColor) {
		this.answerBackgroundColor = answerBackgroundColor;
	}

	public String getAnswerFontColor() {
		return answerFontColor;
	}

	public void setAnswerFontColor(String answerFontColor) {
		this.answerFontColor = answerFontColor;
	}

	public String getAnswerFontSize() {
		return answerFontSize;
	}

	public void setAnswerFontSize(String answerFontSize) {
		this.answerFontSize = answerFontSize;
	}

	public Boolean getHighlighter() {
		return highlighter;
	}

	public void setHighlighter(Boolean highlighter) {
		this.highlighter = highlighter;
	}
    
	
}
