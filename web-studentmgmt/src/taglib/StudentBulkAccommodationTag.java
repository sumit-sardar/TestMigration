package taglib;

import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import dto.StudentAccommodationsDetail;
import java.io.IOException;
import com.ctb.bean.testAdmin.StudentAccommodations;

import javax.servlet.jsp.JspException;


/**
 * @author Tai Truong
 * 
 */ 
public class StudentBulkAccommodationTag extends CTBTag 
{
	private StudentAccommodationsDetail accommodations;
    private CustomerConfiguration[] customerConfigurations;
    
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
        String field, description;
        String add="T", delete="F";
        boolean disabled;
        
        displayTableStart("transparent");

            displayEmptyRow("2");
                
            // screenReader
            field = "screen_reader";
            description = "<b>Allow Screen Reader</b>:";
            disabled = isDisabled(field);
            String selectedFlag = this.accommodations.getScreenReaderRBValue();
            displayControlRow(field, description, add,delete, false , disabled, null,selectedFlag);
            
            displayEmptyRow("2");
            displayEmptyRow("2");


            // calculator
            field = "calculator";
            description = "<b>Online calculator</b>:";
            disabled = isDisabled(field);
            String selectedCalculatorFlag = this.accommodations.getCalculatorRBValue();
            displayControlRow(field, description, add, delete, false, disabled, null,selectedCalculatorFlag);                        


            displayEmptyRow("2");
            displayEmptyRow("2");


            // testPause
            field = "test_pause";
            description = "<b>Test Pause</b>:";
            disabled = isDisabled(field);
            String selectedTestPauseFlag = this.accommodations.getTestPauseRBValue();
            displayControlRow(field, description, add, delete, false, disabled, null,selectedTestPauseFlag);                        


            displayEmptyRow("2");
            displayEmptyRow("2");


            // untimedTest
            field = "untimed_test";
            description = "<b>Untimed Test</b>:";
            disabled = isDisabled(field);
            String selectedUntimedTestFlag = this.accommodations.getUntimedTestRBValue();
            displayControlRow(field, description, add, delete, false, disabled, null,selectedUntimedTestFlag);                        

            displayEmptyRow("2");
            displayEmptyRow("2");
            
            
            // highlighter
            field = "highlighter";
            description = "<b>Highlighter</b>:";
            disabled = isDisabled(field);
            String selectedHighlighterFlag = this.accommodations.getHighLighterRBValue();
            displayControlRow(field, description, add, delete, false, disabled, null,selectedHighlighterFlag);                        

        
            displayEmptyRow("2");
                        
        displayTableEnd();
    }
    
    private void displayColumnTwo() throws IOException 
    {
        boolean checked, disabled;
        String add="T", delete="F";
        String field, description, selection, bgrdColor;
        String questionColorStyle = this.accommodations.getQuestionColorStyle();
        String answerColorStyle = this.accommodations.getAnswerColorStyle();
        String fontSize = this.accommodations.getFontSize();
        String selectedColorFont = this.accommodations.getSelectedColorFont();
        displayTableStart("transparent");
        
            displayEmptyRow("2");
                    
            // colorFont
            field = "colorFont";
            description = "<b>Color and Font</b>:";
            disabled = isDisabled(field);
            displayControlRow(field, description, add, delete, false, disabled, "toogleColorSettingsLink('colorFont');",selectedColorFont);                        
                                
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
                                if(selectedColorFont != null && selectedColorFont.equals("T")) {
                                	displayBackgroundColorOptions(field, false, selection, "setQuestionColorOptions();");
                                }else {
                                	displayBackgroundColorOptions(field, disabled, selection, "setQuestionColorOptions();");
                                }
                                
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
                                if(selectedColorFont != null && selectedColorFont.equals("T")) {
                                	displayForegroundColorOptions(field, false, bgrdColor, selection, "setQuestionColorBox();");
                                } else {
                                	displayForegroundColorOptions(field, disabled, bgrdColor, selection, "setQuestionColorBox();");
                                }
                                
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
                                if(selectedColorFont != null && selectedColorFont.equals("T")) {
                                	displayBackgroundColorOptions(field, false, selection, "setAnswerColorOptions();");
                                } else {
                                	displayBackgroundColorOptions(field, disabled, selection, "setAnswerColorOptions();");
                                }
                                
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
                                if(selectedColorFont != null && selectedColorFont.equals("T")) {
                                	displayForegroundColorOptions(field, false, bgrdColor, selection, "setAnswerColorBox();");
                                } else {
                                	displayForegroundColorOptions(field, disabled, bgrdColor, selection, "setAnswerColorBox();");
                                }
                               
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
                                if(selectedColorFont != null && selectedColorFont.equals("T")) {
                                	writeToPage(buildRadioFont(field, "1", checked, false, "setFontSize('12px');")); 
                                } else {
                                	writeToPage(buildRadioFont(field, "1", checked, disabled, "setFontSize('12px');")); 
                                }
                                   
                            displayCellEnd();
                        displayRowEnd();        
            
                        displayRowStart("transparent");
                    
                            displayEmptyColumn("15");
                            displayTextColumn("Large Font Size:", "110");
                            displayCellStart("transparent-small");
                                field = "largeFont";
                                disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
                                checked = fontSize.equals("1.5") ? true : false;
                                if(selectedColorFont != null && selectedColorFont.equals("T")) {
                                	writeToPage(buildRadioFont(field, "1.5", checked, false, "setFontSize('18px');"));
                                } else {
                                	writeToPage(buildRadioFont(field, "1.5", checked, disabled, "setFontSize('18px');"));
                                }
                                    
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
								field = "musicPlayer";
								disabled = /*isDisabled(field) ||*/ (! this.accommodations.getColorFont().booleanValue());                            
								checked = false;//fontSize.equals("1.5") ? true : false;
								writeToPage(buildRadioButton(field, "musicPlayer", checked, disabled, "enableAudioFiles()", "musicPlayer"));   
							displayCellEnd();
						displayRowEnd();        
					displayTableEnd();
				displayCellEnd();
				displayCellStart("transparent","","left");
					field = "audio_files";
					disabled = /*isDisabled(field) || */(! this.accommodations.getColorFont().booleanValue());                            
					selection = null;//this.accommodations.getAnswer_bgrdColor();
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
								field = "maskingRuler";
								disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue());                            
								checked = false;
								if(isDisabled(field))
									writeToPage(buildRadioButton(field, "masking", checked, disabled, null, "maskingRuler"));
								else
									writeToPage(buildRadioButton("invisible", "invisible", checked, disabled, null, "invisible"));
							displayCellEnd();
						displayRowEnd();        
					displayTableEnd();
				displayCellEnd();
				displayCellStart("transparent");
				 disabled = isDisabled(field) || (! this.accommodations.getColorFont().booleanValue()); 
                 if(selectedColorFont != null && selectedColorFont.equals("T")) {
                 	writeToPage(buildPreviewButton("previewColor", "Preview", false));   
                 } else {
                 	writeToPage(buildPreviewButton("previewColor", "Preview", disabled));   
                 }    
				displayCellEnd();
			displayRowEnd();        
		displayTableEnd();
        
    }
    
    private String buildRadioFont(String id, String value, boolean checked, boolean disabled, String onClick) 
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


    private void displayControlRow(String field, String description, String add, String delete,  
            boolean checked, boolean disabled, String onClick,String selectedFlag) throws IOException 
    {
        
    	
       
    	
    	
    	displayRowStart("transparent");
	        displayCellStart("transparent-small", "140");
	        	writeToPage("&nbsp;&nbsp;&nbsp;"+description);

	        displayCellEnd();
	    displayRowEnd();
	    
	    displayRowStart("transparent");
            displayCellStart("transparent-small");
            writeToPage("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            if(selectedFlag != null && selectedFlag.equals("T")){
            	 writeToPage(buildRadio(field, add, true, disabled, onClick));
            } else {
                writeToPage(buildRadio(field, add, false, disabled, onClick));
            }
                writeToPage("Add");
                writeToPage("&nbsp;&nbsp;&nbsp;");
                
            if(selectedFlag != null && selectedFlag.equals("F")){
               	 writeToPage(buildRadio(field, delete, true, disabled, onClick));
            } else {
                   writeToPage(buildRadio(field, delete, false, disabled, onClick));
            }
            	//writeToPage(buildRadio(field, delete, checked, disabled, onClick));
            	writeToPage("Delete");
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
        
        writeToPage("<select id=\"" + id + "\" name=\"" + id + "\" style=\"" + style + "\" onchange=\"" + onChange + "\" " + disableStr + " >");
            writeToPage(buildOption("Waves.mp3", selection));
            writeToPage(buildOption("RunningWater.mp3", selection));
            writeToPage(buildOption("GentleBlowingWind.mp3", selection));

        writeToPage("</select>");
    }
    
    
	private String buildRadio(String id, String value, boolean checked, boolean disabled, String onClick) 
    {
	    StringBuffer buf = new StringBuffer();
        buf.append("<input type=\"radio\" id=\"" + id + "\" name=\"" + id + "\" value=\"" + value + "\" onClick=\"" + onClick + "\" ");
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
	
	private String buildRadioButton(String id, String value, boolean checked, boolean disabled, String onClick, String name) 
    {
	    StringBuffer buf = new StringBuffer();
        buf.append("<input type=\"radio\" id=\"" + id + "\" name=\""+name+"\"value=\"" + value + "\" onClick=\"" + onClick + "\" ");
        if (checked)
            buf.append(" checked ");        
        if (disabled)
            buf.append(" disabled ");                
        buf.append(" />");
	    return buf.toString();
	}


    private boolean isDisabled(String field) 
    {

        boolean disabled = false;
        if(this.customerConfigurations != null) {
        for (int i=0 ; i<this.customerConfigurations.length ; i++) {
            CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
            String ccName = cc.getCustomerConfigurationName();
            String editable = cc.getEditable();
            
            if (ccName.equalsIgnoreCase(field)) {
                if ((editable != null) && editable.equalsIgnoreCase("F"))
                    disabled = true;
            }
        } 
        }
        return disabled;
    }
}
