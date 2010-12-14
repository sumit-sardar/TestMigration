package taglib;

import com.ctb.bean.studentManagement.StudentProgramGoal;
import com.ctb.bean.studentManagement.StudentProgramGoalValue;
import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;


/**
 * @author TCS
 *  
 */  
public class StudentPrgGoalTag extends CTBTag 
{
	private List programAndGoals;
	private Boolean viewOnly = Boolean.FALSE;
	//CA-ABE student intake
	private Boolean mandatoryField = Boolean.FALSE;
	private Boolean studentImported = Boolean.FALSE;
	//private int tabIndex = 1;



	public void setViewOnly(Boolean viewOnly) {
		this.viewOnly = viewOnly;
	}

	public void setStudentImported(Boolean studentImported) {
		this.studentImported = studentImported;
	}

	/*public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}
*/

	public int doStartTag() throws JspException 
	{
		try {
			if ((this.programAndGoals != null) && (this.programAndGoals.size() > 0)) {
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
		int totalCount = this.programAndGoals.size();
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
			StudentProgramGoal sdd = (StudentProgramGoal)this.programAndGoals.get(i);
			displayStudentProgramGoals(sdd);
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
			StudentProgramGoal sdd = (StudentProgramGoal)this.programAndGoals.get(i);
			displayStudentProgramGoals(sdd);
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

	private void displayStudentProgramGoals(StudentProgramGoal sdd) throws IOException 
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
		//Changes for CA-ABE student intake
		
		if (this.mandatoryField && !displayName.equals("Provider Use")) {
			
			writeToPage("<span>*&nbsp;</span><b>" + displayName + "</b>");
		} else {
			writeToPage("<b>" + displayName + "</b>");
		}

		displayCellEnd();
		displayRowEnd();  
		
		StudentProgramGoalValue[] values = sdd.getStudentProgramGoalValues();
		 
		if( multipleAllowed ) {
					displayValues_CheckBoxes(displayName, values, editable);
		} else { 
				if ( values.length <= 1) {
						String paramValue="";
						if (values.length >0) {
							paramValue = values[0].getValueName();
						}
						displayValues_TextBox(displayName, paramValue ,editable);
				} else {
						if ( values.length < 5 && values.length > 1 ) { 
							displayValues_RadioButtons(displayName, values, editable);
						} else if ( values.length > 5 ){ 
							displayValues_Dropdown(displayName, values, editable);
						} 
				}
		}
		
			
	
			
		
		
	}

	private void displayValues_TextBox(String displayName,String value,
			 boolean enable) throws IOException {
		// TODO Auto-generated method stub
		displayRowStart();
		displayCellStart("transparent-small");
		writeToPage(textBox(displayName,value,enable));
		displayCellEnd();
		displayRowEnd();
		

	}

	private void displayValues_CheckBoxes(String name, StudentProgramGoalValue[] values, boolean editable) throws IOException 
	{
		for (int i=0 ; i<values.length ; i++) {
			StudentProgramGoalValue sdv = (StudentProgramGoalValue)values[i];
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

	private void displayValues_RadioButtons(String name, StudentProgramGoalValue[] values, boolean editable) throws IOException 
	{
		int i;
		String value = null;
		boolean selected = false;	
		boolean hasSelected = false;	

		for (i=0 ; i<values.length ; i++) {
			StudentProgramGoalValue sdv = (StudentProgramGoalValue)values[i];
			value = sdv.getValueName().trim();
			selected = sdv.getSelectedFlag().equals("true");		
			String prin = sdv.getValueName();     //added for CA-ABE
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

	private void displayValues_Dropdown(String name, StudentProgramGoalValue[] values, boolean editable) throws IOException 
	{
		int i;
		String value = null;
		boolean selected = false;	
		String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";

		displayRowStart();
		displayCellStart("transparent-small");
		writeToPage(getSpaces(8));
		writeToPage("<select name=\"" + name + "\" style=width:280px " + disabled +  " >");
		writeToPage(option("Please Select", true));
		for (i=0 ; i<values.length ; i++) {
			StudentProgramGoalValue sdv = (StudentProgramGoalValue)values[i];
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
		return "<input type=\"checkbox\" name=\"" + nameId + "\" id=\"" + nameId + "\"" +
		" value=\""+ value + "\" " + 
		(isChecked?"checked=\"true\" ":" ") + disabled + "/>";
	}
	
	private String textBox(String name,String value,   boolean editable) 
	{
		String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";
		String nameId = name  ;
		if(this.viewOnly.booleanValue()) {
			return "<label name=\"" + nameId + "\" id=\"" + nameId + "\"" + 
			" style="+ " margin-left:"+"25px; >"+ value + "</label>";
		}
		else{
			return "<input type=\"text\" name=\"" + nameId + "\" id=\"" + nameId + "\"" +  "maxlength=" + "64" + 
			" style="+ " margin-left:"+"25px;"+	" value=\""+ value + "\" " +  
			disabled + "/>";
		}
	}

	private String radioButton(String name, String value, boolean isChecked, boolean editable) 
	{
		String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";
		return "<input type=\"radio\" name=\"" + name + "\" id=\"" + name + "\"" +
		" value=\"" + value + "\" " + 
		(isChecked?"checked=\"true\" ":" ") + disabled + " onClick=\"displayWorkforceSection(this)\"/>";			//added for CA-ABE
	}

	private String getSpaces(int spaces) 
	{
		String str = "";        
		for (int i=0 ; i<spaces ; i++) {
			str += "&nbsp;";
		}
		return str;
	}

	public void setMandatoryField(Boolean mandatoryField) {
		this.mandatoryField = mandatoryField;
	}

	/**
	 * @return the programAndGoals
	 */
	public List getProgramAndGoals() {
		return programAndGoals;
	}

	/**
	 * @param programAndGoals the programAndGoals to set
	 */
	public void setProgramAndGoals(List programAndGoals) {
		this.programAndGoals = programAndGoals;
	}

}

