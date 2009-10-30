package taglib;

import data.SubtestVO;
import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;


/**
 * @author Tai Truong
 * 
 */ 
public class AutoLocatorTag extends CTBTag 
{
    private String subtestName = "";    
    private String levelName = "";    
    private Boolean showLevel = Boolean.TRUE;    
    private Boolean checked = Boolean.TRUE;    
    private Boolean checkboxDisabled = Boolean.FALSE;    

    public void setSubtestName(String subtestName) {
        this.subtestName = subtestName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }    

    public void setShowLevel(Boolean showLevel) {
        this.showLevel = showLevel;
    }
    
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public void setCheckboxDisabled(Boolean checkboxDisabled) {
        this.checkboxDisabled = checkboxDisabled;
    }
	            
	public int doStartTag() throws JspException {
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
        displayTableStart("sortable");		

			displayRowStart("sortable");            
                // headers
                displayHeaderStart("sortable alignCenter", "60");
                    writeToPage("&nbsp;Select");
                displayHeaderEnd();
                displayHeaderStart("sortable alignLeft", "*");
                    writeToPage("&nbsp;&nbsp;Subtest Name");
                displayHeaderEnd();
            if (this.showLevel.booleanValue()) {                
                displayHeaderStart("sortable alignCenter", "50");
                    writeToPage("&nbsp;Level");
                displayHeaderEnd();
            }
			displayRowEnd();            

			displayRowStart("sortable");                            
				displayCellStart("sortable alignCenter", "60");
                    String onClick = "setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'autoLocator', 'autoLocator');";                
                    writeToPage(checkBoxForm("{actionForm.autoLocator}", "autoLocator", 
                                             this.checked.booleanValue(), this.checkboxDisabled.booleanValue(), onClick));                    
				displayCellEnd();
				displayCellStart("sortable alignLeft", "*");
                    writeToPage(this.subtestName);
				displayCellEnd();
            if (this.showLevel.booleanValue()) {                
				displayCellStart("sortable alignCenter", "50");
                    writeToPage(this.levelName);
				displayCellEnd();
            }
			displayRowEnd();     
                   
		displayTableEnd();
    }

    
}

