package taglib;

import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import dto.StudentAccommodationsDetail;
import java.io.IOException;

import javax.servlet.jsp.JspException;


/**
 * @author Tai Truong
 * 
 */ 
public class StudentAccommodationsTag extends CTBTag 
{
	private StudentAccommodationsDetail accommodations = null;
    private CustomerConfiguration[] customerConfigurations = null;
    
	private Boolean viewOnly = Boolean.FALSE;
	
    public void setAccommodations(StudentAccommodationsDetail accommodations) {
        this.accommodations = accommodations;
    }
    public void setCustomerConfigurations(CustomerConfiguration[] customerConfigurations) {
        this.customerConfigurations = customerConfigurations;
    }
    public void setViewOnly(Boolean viewOnly) {
        this.viewOnly = viewOnly;
    }
	      
	public int doStartTag() throws JspException 
    {        
		try {
		    displayContent();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new JspException(e.toString());
		}

		return SKIP_BODY;
	}

    private void displayContent() throws IOException 
    {
        displayTableStart("simple");
		
            displayEmptyRow("2");
        
			displayRowStart("transparent");
				displayCellStartColspan("transparent", null, "3");
					writeToPage("Select the accommodation(s) the student requires while testing. These settings can be adjusted for each test.");
				displayCellEnd();
			displayRowEnd();

			displayRowStart("transparent");
            
                // column 1
				displayCellStart("transparent-top", "45%");
                    displayColumnOne();
				displayCellEnd();

                // separator column
				displayCellStart("transparent-top", "5%");
                    writeToPage("&nbsp;");
				displayCellEnd();

                // column 2
				displayCellStart("transparent-top", "45%");
                    displayColumnTwo();
				displayCellEnd();

		displayTableEnd();
	}

    private void displayColumnOne() throws IOException 
    {
        boolean checked, disabled;
        String field, description;
        
        displayTableStart("transparent");

            displayEmptyRow("2");
                
            // screenReader
            field = "screen_reader";
            description = "<b>Allow Screen Reader</b>:";
            checked = this.accommodations.getScreenReader().booleanValue();
            disabled = isDisabled(field);
            displayControlRow(field, description, checked, disabled, null);                        
            displayTextRow("Program reads certain text blocks and/or image labels aloud.", "20");
            
            
            displayEmptyRow("2");
            displayEmptyRow("2");


            // calculator
            field = "calculator";
            description = "<b>Online calculator</b>:";
            checked = this.accommodations.getCalculator().booleanValue();
            disabled = isDisabled(field);
            displayControlRow(field, description, checked, disabled, null);                        
            displayTextRow("A calculator is provided for all test questions allowing a calculator as an accommodation.", "20");


            displayEmptyRow("2");
            displayEmptyRow("2");


            // testPause
            field = "test_pause";
            description = "<b>Test Pause</b>:";
            checked = this.accommodations.getTestPause().booleanValue();
            disabled = isDisabled(field);
            displayControlRow(field, description, checked, disabled, null);                        
            displayTextRow("Allow student to pause test for rest breaks.", "20");


            displayEmptyRow("2");
            displayEmptyRow("2");


            // untimedTest
            field = "untimed_test";
            description = "<b>Untimed Test</b>:";
            checked = this.accommodations.getUntimedTest().booleanValue();
            disabled = isDisabled(field);
            displayControlRow(field, description, checked, disabled, null);                        
            displayTextRow("Eliminate time limits.", "20");

            displayEmptyRow("2");
            displayEmptyRow("2");
            
            
            // highlighter
            field = "highlighter";
            description = "<b>Highlighter</b>:";
            checked = this.accommodations.getHighlighter().booleanValue();
            disabled = isDisabled(field);
            displayControlRow(field, description, checked, disabled, null);                        
            displayTextRow("Allow student to highlight test.", "20");

        
            displayEmptyRow("2");
                        
        displayTableEnd();
    }
    
    private void displayColumnTwo() throws IOException 
    {
        boolean checked, disabled;
        String field, description, selection, bgrdColor;
        String questionColorStyle = this.accommodations.getQuestionColorStyle();
        String answerColorStyle = this.accommodations.getAnswerColorStyle();
        String fontSize = this.accommodations.getFontSize();
        
        displayTableStart("transparent");
        
            displayEmptyRow("2");
                    
            // colorFont
            field = "colorFont";
            description = "<b>Color and Font</b>:";
            checked = this.accommodations.getColorFont().booleanValue();
            disabled = isDisabled(field);
            displayControlRow(field, description, checked, disabled, "toogleColorSettingsLink('colorFont');");                        
            displayTextRow("Change screen colors and/or font size.", "20");
                                
		displayTableEnd();
        
        
        displayColorTableStart("310");

            displayEmptyRow("3");
        
            // Question settings
            displayRowStart("transparent");
                displayEmptyColumn("15");
                displayCellStartColspan("transparent-small", "*", "2");                
                    writeToPage("&nbsp;<b>Question settings</b>");
                displayCellEnd();
            displayRowEnd();        

            // Question A
            displayRowStart("transparent");
                displayEmptyColumn("15");
                displayCellStart("transparent", "50", "right");                
                    displayTableStartWithStyle("transparent", "width: 50px; height: 50px;");
                        displayRowStart("transparent");
                            displayCellStartWithStyle("questionBox", "transparent-top", questionColorStyle);
                                writeToPage("A");
                            displayCellEnd();
                        displayRowEnd();        
                    displayTableEnd();
                displayCellEnd();
                displayCellStart("transparent");                
                    displayTableStart("transparent");
                        displayRowStart("transparent");
                            displayCellStart("transparent", "30%");
                                writeToPage("&nbsp;Background:&nbsp;");
                            displayCellEnd();
                            displayCellStart("transparent", "70%");
                                field = "question_bgrdColor";
                                disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
                                selection = this.accommodations.getQuestion_bgrdColor();
                                displayBackgroundColorOptions(field, disabled, selection, "setQuestionColorOptions();");
                            displayCellEnd();                            
                        displayRowEnd();        
                        displayRowStart("transparent");
                            displayCellStart("transparent", "30%");
                                writeToPage("&nbsp;Font Color:&nbsp;");
                            displayCellEnd();
                            displayCellStart("transparent", "70%");
                                field = "question_fontColor";
                                disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
                                selection = this.accommodations.getQuestion_fontColor();
                                bgrdColor = this.accommodations.getQuestion_bgrdColor();
                                displayForegroundColorOptions(field, disabled, bgrdColor, selection, "setQuestionColorBox();");
                            displayCellEnd();                            
                        displayRowEnd();        
                    displayTableEnd();
                displayCellEnd();
            displayRowEnd();        
            
            
            displayEmptyRow("3");

        
            // Answer settings
            displayRowStart("transparent");
                displayEmptyColumn("15");
                displayCellStartColspan("transparent-small", "*", "2");                
                    writeToPage("&nbsp;<b>Answer settings</b>");
                displayCellEnd();
            displayRowEnd();        

            // Answer A
            displayRowStart("transparent");
                displayEmptyColumn("15");
                displayCellStart("transparent", "50", "right");                
                    displayTableStartWithStyle("transparent", "width: 50px; height: 50px;");
                        displayRowStart("transparent");
                            displayCellStartWithStyle("answerBox", "transparent-top", answerColorStyle);
                                writeToPage("A");
                            displayCellEnd();
                        displayRowEnd();        
                    displayTableEnd();
                displayCellEnd();
                displayCellStart("transparent");                
                    displayTableStart("transparent");
                        displayRowStart("transparent");
                            displayCellStart("transparent", "30%");
                                writeToPage("&nbsp;Background:&nbsp;");
                            displayCellEnd();
                            displayCellStart("transparent", "70%");
                                field = "answer_bgrdColor";
                                disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
                                selection = this.accommodations.getAnswer_bgrdColor();
                                displayBackgroundColorOptions(field, disabled, selection, "setAnswerColorOptions();");
                            displayCellEnd();                            
                        displayRowEnd();        
                        displayRowStart("transparent");
                            displayCellStart("transparent", "30%");
                                writeToPage("&nbsp;Font Color:&nbsp;");
                            displayCellEnd();
                            displayCellStart("transparent", "70%");
                                field = "answer_fontColor";
                                disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
                                selection = this.accommodations.getAnswer_fontColor();
                                bgrdColor = this.accommodations.getAnswer_bgrdColor();
                                displayForegroundColorOptions(field, disabled, bgrdColor, selection, "setAnswerColorBox();");
                            displayCellEnd();                            
                        displayRowEnd();        
                    displayTableEnd();
                displayCellEnd();
            displayRowEnd();        
            
		displayTableEnd();
        
        
        // Font size
        displayColorTableStart("300");
            displayRowStart("transparent");
                displayCellStart("transparent");

                    displayColorTableStart("200");
                        
                        displayRowStart("transparent");
                    
                            displayEmptyColumn("15");
                            displayTextColumn("Standard Font Size:", "110");
                            displayCellStart("transparent-small");
                                field = "standartFont";
                                disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
                                checked = fontSize.equals("1") ? true : false;
                                writeToPage(buildRadio(field, "1", checked, disabled, "setFontSize('12px');"));    
                            displayCellEnd();
                        displayRowEnd();        
            
                        displayRowStart("transparent");
                    
                            displayEmptyColumn("15");
                            displayTextColumn("Large Font Size:", "110");
                            displayCellStart("transparent-small");
                                field = "largeFont";
                                disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
                                checked = fontSize.equals("1.5") ? true : false;
                                writeToPage(buildRadio(field, "1.5", checked, disabled, "setFontSize('18px');"));    
                            displayCellEnd();
                        displayRowEnd();        
                                            
                    displayTableEnd();
        
                displayCellEnd();
            displayRowEnd();        
        displayTableEnd();
        
	// Added for Auditory Calming
		displayColorTableStart("300");
			displayRowStart("transparent");
				displayCellStart("transparent");
					displayColorTableStart("160");
						displayRowStart("transparent");
							displayEmptyColumn("15");
							displayTextColumn("Music Player:", "110");
							displayCellStart("transparent-small");
								field = "Auditory_Calming";
								disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
								checked = false;
								if(!isDisabled(field))
									writeToPage(buildRadioButton(field, "Auditory_Calming", checked, disabled, "enableAudioFiles()"));
								else
									writeToPage(buildRadioButton("disabled", "disabled", checked, disabled, null));
							displayCellEnd();
						displayRowEnd();        
					displayTableEnd();
				displayCellEnd();
				displayCellStart("transparent","","left");
					field = "music_files";
					disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
					selection = null;
					displayAudioFiles(field, disabled, selection, null);    
				displayCellEnd();
			displayRowEnd();        
		displayTableEnd();
        
        
	// Added for Masking Ruler
		displayColorTableStart("300");
			displayRowStart("transparent");
				displayCellStart("transparent");
					displayColorTableStart("160");
						displayRowStart("transparent");
							displayEmptyColumn("15");
							displayTextColumn("Masking Ruler:", "110");
							displayCellStart("transparent-small");
								field = "Masking_Ruler";
								disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
								checked = false;
								if(!isDisabled(field))
									writeToPage(buildRadioButton(field, "Masking_Ruler", checked, disabled, null));
								else
									writeToPage(buildRadioButton("disabled", "disabled", checked, disabled, null));
							    
							displayCellEnd();
						displayRowEnd();        
					displayTableEnd();
				displayCellEnd();
				displayCellStart("transparent");
					disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
					writeToPage(buildPreviewButton("previewColor", "Preview", disabled));    
				displayCellEnd();
			displayRowEnd();        
		displayTableEnd();
        
    }
    
    private void displayEmptyRow(String colspan) throws IOException 
    {
        displayRowStart("transparent");
            displayCellStartColspan("transparent-small", null, colspan);
                writeToPage("&nbsp;");
            displayCellEnd();
        displayRowEnd();        
    }

    private void displayEmptyColumn(String width) throws IOException 
    {
        displayCellStart("transparent-small", width);
            writeToPage("&nbsp;");
        displayCellEnd();
    }

    private void displayTextColumn(String content, String width) throws IOException 
    {
        displayCellStart("transparent-small", width);
            writeToPage(content);
        displayCellEnd();
    }

    private void displayTextRow(String content, String width) throws IOException 
    {
        displayRowStart("transparent");
            displayCellStart("transparent-small", width);
                writeToPage("&nbsp;");
            displayCellEnd();
            displayCellStart("transparent-small");
                writeToPage(content);
            displayCellEnd();
        displayRowEnd();        
    }

    private void displayControlRow(String field, String description, 
            boolean checked, boolean disabled, String onClick) throws IOException 
    {
        displayRowStart("transparent");
            displayCellStart("transparent-small", "20");
                writeToPage(buildCheckbox(field, checked, disabled, onClick));
            displayCellEnd();
            displayCellStart("transparent-small");
                writeToPage(description);
            displayCellEnd();
        displayRowEnd();
    }
    
    private void displayBackgroundColorOptions(String id, boolean disabled, String selection, String onChange) throws IOException 
    {
        String style = "width: 120px";
        String disableStr = disabled ? "disabled" : "";
        
        writeToPage("<select id=\"" + id + "\" name=\"" + id + "\" style=\"" + style + "\" onchange=\"" + onChange + "\" " + disableStr + " >");
            writeToPage(buildOption(this.accommodations.getColorText(4), selection));       // Black
            writeToPage(buildOption(this.accommodations.getColorText(5), selection));       // Dark blue
            writeToPage(buildOption(this.accommodations.getColorText(0), selection));       // Light blue
            writeToPage(buildOption(this.accommodations.getColorText(1), selection));       // Light pink
            writeToPage(buildOption(this.accommodations.getColorText(2), selection));       // Light yellow
            writeToPage(buildOption(this.accommodations.getColorText(3), selection));       // White
        writeToPage("</select>");
    }

    private void displayForegroundColorOptions(String id, boolean disabled, String bgrdColor, String selection, String onChange) throws IOException 
    {
        String style = "width: 120px";
        String disableStr = disabled ? "disabled" : "";
        
        writeToPage("<select id=\"" + id + "\" name=\"" + id + "\" style=\"" + style + "\" onchange=\"" + onChange + "\" " + disableStr + " >");
            if (bgrdColor.equals("Dark blue")) {
                writeToPage(buildOption(this.accommodations.getColorText(3), selection));   // White
            }
            else
            if (bgrdColor.equals("Black")) {
                writeToPage(buildOption(this.accommodations.getColorText(8), selection));   // Green
                writeToPage(buildOption(this.accommodations.getColorText(3), selection));   // White
                writeToPage(buildOption(this.accommodations.getColorText(7), selection));   // Yellow
            }
            else {
                writeToPage(buildOption(this.accommodations.getColorText(4), selection));   // Black
                writeToPage(buildOption(this.accommodations.getColorText(5), selection));   // Dark blue
                writeToPage(buildOption(this.accommodations.getColorText(6), selection));   // Dark brown
            }
        writeToPage("</select>");
    }
    
 // Added for auditory calming
    
    private void displayAudioFiles(String id, boolean disabled, String selection, String onChange) throws IOException 
    {
        String style = "width: 120px";
        String disableStr = disabled ? "disabled" : "";
        if(selection==null || selection.equals(""))
        	selection="Waves.mp3";
        writeToPage("<select id=\"" + id + "\" name=\"" + id + "\" style=\"" + style + "\" onchange=\"" + onChange + "\" " + disableStr + " >");
            writeToPage(buildOption("Waves.mp3", selection));
            writeToPage(buildOption("RunningWater.mp3", selection));
            writeToPage(buildOption("GentleBlowingWind.mp3", selection));

        writeToPage("</select>");
    }

	private String buildCheckbox(String id, boolean checked, boolean disabled, String onClick) 
    {
	    StringBuffer buf = new StringBuffer();
        buf.append("<input type=\"checkbox\" id=\"" + id + "\" name=\"" + id + "\" ");
        if (checked)
            buf.append(" checked ");        
        if (disabled)
            buf.append(" disabled ");                
        if (onClick != null)
            buf.append(" onClick=\"" + onClick + "\" ");                
        buf.append(" />");
	    return buf.toString();
	}

	private String buildRadio(String id, String value, boolean checked, boolean disabled, String onClick) 
    {
	    StringBuffer buf = new StringBuffer();
        buf.append("<input type=\"radio\" id=\"" + id + "\" name=\"fontSize\" value=\"" + value + "\" onClick=\"" + onClick + "\" ");
        if (checked)
            buf.append(" checked ");        
        if (disabled)
            buf.append(" disabled ");                
        buf.append(" />");
	    return buf.toString();
	}

	private String buildPreviewButton(String id, String value, boolean disabled) 
    {
	    StringBuffer buf = new StringBuffer();
        buf.append("<input type=\"button\" id=\"" + id + "\" name=\"" + id + "\" value=\"" + value + "\"");
        buf.append("onClick=\"openColorPreviewWindow(); return true;\"");        
        if (disabled)
            buf.append(" disabled ");                        
        buf.append(" />");
	    return buf.toString();
	}
        
	private String buildOption(String value, String sel) {
	    StringBuffer buf = new StringBuffer(); 
        buf.append("<option value=\"" + value + "\"");
        if (value.equalsIgnoreCase(sel))
            buf.append(" selected ");        
        buf.append(" >");        
        buf.append(value);        
        buf.append("</option>");
	    return buf.toString();
	}
	
	//Added for Masking and Auditory Calming
	
	private String buildRadioButton(String id, String value, boolean checked, boolean disabled, String onClick) 
    {
	    StringBuffer buf = new StringBuffer();
        buf.append("<input type=\"radio\" id=\"" + id + "\" name=\""+id+"\"value=\"" + value + "\" onClick=\"" + onClick + "\" ");
        if (checked)
            buf.append(" checked ");        
        if (disabled)
            buf.append(" disabled ");                
        buf.append(" />");
	    return buf.toString();
	}

    private boolean isDisabled(String field) 
    {
        if (this.viewOnly.booleanValue())
            return true;
            
        boolean disabled = false;
        int counter = 0; // Added for calming and masking. Specially, if configuration is not present with the customer at all.
        
        for (int i=0 ; i<this.customerConfigurations.length ; i++) {
            CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
            String ccName = cc.getCustomerConfigurationName();
            String editable = cc.getEditable();
            
            if (ccName.equalsIgnoreCase(field)) {
            	counter++;
                if ((editable != null) && editable.equalsIgnoreCase("F"))
                    disabled = true;
            }
        }        
        if(counter == 0 && (field.equals("Masking_Ruler") || field.equals("Auditory_Calming")))
        	disabled=true;
        
        return disabled;
    }
}

