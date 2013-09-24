package taglib;

import com.ctb.bean.studentManagement.StudentDemographic;
import com.ctb.bean.studentManagement.StudentDemographicValue;
import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;


/**
 * @author Tai Truong
 *  
 */  
public class ViewStudentDemographicsTag extends CTBTag 
{
	private List demographics;
	private Boolean viewOnly = Boolean.FALSE;
	private Boolean studentImported = Boolean.FALSE;
	private int tabIndex = 1;
	private boolean isLaslink = false;
	
    public void setDemographics(List demographics) {
        this.demographics= demographics;
    }
    
    public void setViewOnly(Boolean viewOnly) {
        this.viewOnly = viewOnly;
    }

    public void setStudentImported(Boolean studentImported) {
        this.studentImported = studentImported;
    }
    
    public void setTabIndex(int tabIndex) {
    	this.tabIndex = tabIndex;
    }
    
    public void setIsLaslink(boolean isLaslink) {
		this.isLaslink = isLaslink;
	}
    
	public int doStartTag() throws JspException 
    {
		try {
		    if ((this.demographics != null) && (this.demographics.size() > 0)) {
		        displayContent();
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new JspException(e.toString());
		}

		return SKIP_BODY;
	}
 
    private void displayContent() throws IOException 
    {
		int totalCount = this.demographics.size();
        int secondCount = totalCount / 2;
        int firstCount = totalCount - secondCount;
        
        
        displayTableStart("simple");
        
            displayRowStart("transparent");   
                displayCellStartColspan("transparent-small", null, "2");
                    writeToPage("&nbsp;");
                displayCellEnd();
            displayRowEnd();  

            displayRowStart("transparent");   
                     
                // first column
                displayCellStart("CollapsibleTextNoBorder", "50%");
        
                    displayTableStart();
                    for (int i=0 ; i<firstCount ; i++) {
                        StudentDemographic sdd = (StudentDemographic)this.demographics.get(i);
                        displayStudentDemoGraphic(sdd);
                        displayRowStart();
                            displayCellStart("transparent-small");
                                writeToPage("&nbsp;");
                            displayCellEnd();
                        displayRowEnd();  
                    }
                    displayTableEnd();
        
                displayCellEnd();
                
                
                // second column
                displayCellStart("CollapsibleTextNoBorder", "50%");

                    displayTableStart();
                    for (int i=firstCount ; i<totalCount ; i++) {
                        StudentDemographic sdd = (StudentDemographic)this.demographics.get(i);
                        displayStudentDemoGraphic(sdd);
                        displayRowStart();
                            displayCellStart("transparent-small");
                                writeToPage("&nbsp;");
                            displayCellEnd();
                        displayRowEnd();  
                    }
                    displayTableEnd();
        
                displayCellEnd();        
                
            displayRowEnd();  
            
        displayTableEnd();
        
        
	}

    private void displayStudentDemoGraphic(StudentDemographic sdd) throws IOException 
    {
        String displayName = sdd.getLabelName().trim();
        boolean multipleAllowed = sdd.getMultipleAllowedFlag().equals("true");
        boolean editable = true;
        
        if (this.studentImported.booleanValue() && (sdd.getImportEditable() != null)) {
            if (sdd.getImportEditable().equals("F") || sdd.getImportEditable().equals("UNEDITABLE_ON_NULL_RULE")) {
                editable = false;
            }
        }            
        
    	displayRowStart();
            displayCellStart("transparent");
            if(displayName.equals("Accommodations") && isLaslink){
        		writeToPage("<b>" + "Accommodations - A/B Only" + "</b>");
        	}else{
        		writeToPage("<b>" + displayName + "</b>");
        	}
    		displayCellEnd();
    	displayRowEnd();  
    	
	    StudentDemographicValue[] values = sdd.getStudentDemographicValues();
	    if( multipleAllowed ) {
	        displayValues_CheckBoxes(displayName, values, editable);
	    } 
        else { 
		    if ( values.length == 1 ) {  
                displayValues_CheckBoxes(displayName, values, editable);
            }
            else
		    if ( values.length < 5 ) {  
	            displayValues_RadioButtons(displayName, values, editable);
		    } 
            else { 
		        displayValues_Dropdown(displayName, values, editable);
		    }
	    }
	}

	private void displayValues_CheckBoxes(String name, StudentDemographicValue[] values, boolean editable) throws IOException 
    {
	    for (int i=0 ; i<values.length ; i++) {
	        StudentDemographicValue sdv = (StudentDemographicValue)values[i];
		    String value = sdv.getValueName().trim();
		    boolean selected = sdv.getSelectedFlag().equals("true");	
			displayRowStart();
    			displayCellStart("transparent-small");
                
                    displayTableStart();
                    displayRowStart();
                    displayCellStart("transparent-small", "12", null);                    
                        writeToPage(getSpaces(1));	
                    displayCellEnd();
                    displayCellStart("transparent-small", "20", null);                    
                        writeToPage(checkBox(name, value, selected, editable));	
                    displayCellEnd();
                    displayCellStart("transparent-small", "*", null);                    
                        writeToPage(value);
                    displayCellEnd();
                    displayRowEnd();
                    displayTableEnd();
                    
		        displayCellEnd();
			displayRowEnd();  
	    }
	}

	private void displayValues_RadioButtons(String name, StudentDemographicValue[] values, boolean editable) throws IOException 
    {
	    int i;
	    String value = null;
	    boolean selected = false;	
	    boolean hasSelected = false;	
	     
	    for (i=0 ; i<values.length ; i++) {
	        StudentDemographicValue sdv = (StudentDemographicValue)values[i];
		    value = sdv.getValueName().trim();
		    selected = sdv.getSelectedFlag().equals("true");	
		    if (selected)
		        hasSelected = true;
			displayRowStart();
    			displayCellStart("transparent-small");
                
                    displayTableStart();
                    displayRowStart();
                    displayCellStart("transparent-small", "12", null);                    
                        writeToPage(getSpaces(1));  
                    displayCellEnd();                    
                    displayCellStart("transparent-small", "20", null);                    
                        writeToPage(radioButton(name, value, selected, editable));
                    displayCellEnd();
                    displayCellStart("transparent-small", "*", null);                    
                        writeToPage(value);
                    displayCellEnd();
                    displayRowEnd();
                    displayTableEnd();
                    
		        displayCellEnd();
			displayRowEnd();  
	    }
		displayRowStart();
			displayCellStart("transparent-small");
            
                    displayTableStart();
                    displayRowStart();
                    displayCellStart("transparent-small", "12", null);                    
                        writeToPage(getSpaces(1));  
                    displayCellEnd();                    
                    displayCellStart("transparent-small", "20", null);                    
                        writeToPage(radioButton(name, "None", !hasSelected, editable));
                    displayCellEnd();
                    displayCellStart("transparent-small", "*", null);                    
                        writeToPage("None");
                    displayCellEnd();
                    displayRowEnd();
                    displayTableEnd();
                
	        displayCellEnd();
		displayRowEnd();  
	}

	private void displayValues_Dropdown(String name, StudentDemographicValue[] values, boolean editable) throws IOException 
    {
	    int i;
	    String value = null;
	    boolean selected = false;	
	    String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";

	    displayRowStart();
            displayCellStart("transparent-small");
                writeToPage(getSpaces(8));
				writeToPage("<select name=\"" + "view" + name + "\" id=\"" + "view" + name + "\" style=width:280px " + disabled + " tabindex=\"" + (this.tabIndex++) + "\" " + " >");
		        writeToPage(option("Please Select", true));
			    for (i=0 ; i<values.length ; i++) {
			        StudentDemographicValue sdv = (StudentDemographicValue)values[i];
				    value = sdv.getValueName().trim();
				    selected = sdv.getSelectedFlag().equals("true");
   			        writeToPage(option(value, selected));
			    }
		        writeToPage("</select>");
	        displayCellEnd();
		displayRowEnd();  
	}

	private String option(String value, boolean isChecked) 
    {
	    String selected = isChecked ? "selected" : "";
		return "<option " + selected + " >" + value + "</option>";
	}
	
	private String checkBox(String name, String value, boolean isChecked, boolean editable) 
    {
	    String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";
	    String nameId = name + "_" + value;
		return "<input type=\"checkbox\" name=\""  + "view" + nameId + "\" id=\"" + "view"  + nameId + "\"" +
				" value=\""+ value + "\" " + 
				" tabindex=\"" + (this.tabIndex++) + "\" " +
				(isChecked?"checked=\"true\" ":" ") + disabled + "/>";
	}
    
	private String radioButton(String name, String value, boolean isChecked, boolean editable) 
    {
	    String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";
		return "<input type=\"radio\" name=\""  + "view" + name + "\" id=\""  + "view" + name + "\"" +
				" value=\"" + value + "\" " + 
				" tabindex=\"" + (this.tabIndex++) + "\" " +
				(isChecked?"checked=\"true\" ":" ") + disabled + "/>";
	}

    private String getSpaces(int spaces) 
    {
        String str = "";        
        for (int i=0 ; i<spaces ; i++) {
            str += "&nbsp;";
        }
        return str;
    }
    	
}

