package taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;

import utils.DateUtils;

import com.ctb.bean.studentManagement.StudentOtherDetail;
import com.ctb.bean.studentManagement.StudentOtherDetailValue;


/**
 * @author Tai Truong
 *  
 */  
public class StudentEduAndInstrTag extends CTBTag 
{
	private List educationAndInstruction;
	private Boolean viewOnly = Boolean.FALSE;
	//CA-ABE student intake
	private Boolean mandatoryField = Boolean.FALSE;
	private Boolean studentImported = Boolean.FALSE;
	private int tabIndex = 1;
	
  
    public void setViewOnly(Boolean viewOnly) {
        this.viewOnly = viewOnly;
    }

    public void setStudentImported(Boolean studentImported) {
        this.studentImported = studentImported;
    }
    
    public void setTabIndex(int tabIndex) {
    	this.tabIndex = tabIndex;
    }
    
    
	public int doStartTag() throws JspException 
    {
		try {
		    if ((this.educationAndInstruction != null) && (this.educationAndInstruction.size() > 0)) {
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
		int totalCount = this.educationAndInstruction.size();
		int secondCount = totalCount / 2;
		int firstCount = totalCount - secondCount;
		String field, description;

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
			StudentOtherDetail sdd = (StudentOtherDetail)this.educationAndInstruction.get(i);
			displayStudentEduAndInstr(sdd);
			displayRowStart();
			displayCellStart("transparent-small");
			writeToPage("&nbsp;");
			displayCellEnd();
			displayRowEnd();  
		}
		
		 field = "Date of Entry into this class";
         description = "<b>Date of Entry into this class</b>:";
         for(int i=0; i<totalCount; i++){
        	 StudentOtherDetail sdd = (StudentOtherDetail)this.educationAndInstruction.get(i);
        	 if(sdd.getValueCardinality().equals("Date")){ 
        		 StudentOtherDetailValue[] values = sdd.getStudentOtherDetailValues();
        		 displayControlRow(field, description, true, values); 
        		 break;
             }

         }
                                
         
		displayTableEnd();

		displayCellEnd();


		// second column
		displayCellStart("CollapsibleTextNoBorder", "50%");

		displayTableStart();
		for (int i=firstCount ; i<totalCount ; i++) {
			StudentOtherDetail sdd = (StudentOtherDetail)this.educationAndInstruction.get(i);
			displayStudentEduAndInstr(sdd);
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

	private void displayStudentEduAndInstr(StudentOtherDetail sdd) throws IOException 
	{
		String displayName = sdd.getLabelName().trim();
		boolean multipleAllowed = sdd.getMultipleAllowedFlag().equals("true");
		boolean editable = true;
		//check for CA-ABE followup to make Labor Force Status Non Editable
		if (this.mandatoryField) {

			if( sdd.getVisible().equals("NON EDITABLE")){
				editable = false;
			}

		}
		
		StudentOtherDetailValue[] values = sdd.getStudentOtherDetailValues();
		displayRowStart();
		displayCellStart("transparent");
		//Changes for CA-ABE student intake
		if(!sdd.getValueCardinality().equals("Date")){
			if (this.mandatoryField) {
	
				writeToPage("<span>*&nbsp;</span><b>" + displayName + "</b>");
			} else {
				writeToPage("<b>" + displayName + "</b>");
			}
		}
		displayCellEnd();
		displayRowEnd();  

		
		if( multipleAllowed ) {
			displayValues_CheckBoxes(displayName, values, editable);
		} 
		else if(!sdd.getValueCardinality().equals("Date")){ 
			if ( values.length <= 1 ) {  
				//displayValues_CheckBoxes(displayName, values, editable);
				displayValues_TextBox(displayName, values[0].getValueName() ,editable);

			}
			else if ( sdd.getValueCardinality().equals("multipleDropdown")) {  
				displayValues_MultiDropdown(displayName, values, editable);
			} 
			else if ( values.length < 5 ) {  
				displayValues_RadioButtons(displayName, values, editable);
			}
			else { 
				displayValues_Dropdown(displayName, values, editable);
			}

		}
	}

	private void displayValues_TextBox(String displayName,String value,
			boolean enable) throws IOException {
		System.out.println("text box");
		// TODO Auto-generated method stub
		displayRowStart();
		displayCellStart("transparent-small");
		writeToPage(textBox(displayName,value,enable));
		displayCellEnd();
		displayRowEnd();


	}


	private void displayValues_CheckBoxes(String name, StudentOtherDetailValue[] values, boolean editable) throws IOException 
	{
		for (int i=0 ; i<values.length ; i++) {
			StudentOtherDetailValue sdv = (StudentOtherDetailValue)values[i];
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

	private void displayValues_RadioButtons(String name, StudentOtherDetailValue[] values, boolean editable) throws IOException 
	{
		int i;
		String value = null;
		boolean selected = false;	
		boolean hasSelected = false;	

		for (i=0 ; i<values.length ; i++) {
			StudentOtherDetailValue sdv = (StudentOtherDetailValue)values[i];
			value = sdv.getValueName().trim();
			selected = sdv.getSelectedFlag().equals("true");		
			String prin = sdv.getValueName();     //added for CA-ABE
			/*if (selected)
				hasSelected = true;*/
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

	}

	private void displayValues_Dropdown(String name, StudentOtherDetailValue[] values, boolean editable) throws IOException 
	{
		int i;
		String value = null;
		boolean selected = false;	
		String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";

		displayRowStart();
		displayCellStart("transparent-small");
		writeToPage(getSpaces(8));
		writeToPage("<select name=\"" + name + "\" style=width:280px " + disabled + " tabindex=\"" + (this.tabIndex++) + "\" " + " >");
		writeToPage(option("Please Select", true));
		for (i=0 ; i<values.length ; i++) {
			StudentOtherDetailValue sdv = (StudentOtherDetailValue)values[i];
			value = sdv.getValueName().trim();
			selected = sdv.getSelectedFlag().equals("true");
			writeToPage(option(value, selected));
		}
		writeToPage("</select>");
		displayCellEnd();
		displayRowEnd();  
	}
	
	private void displayValues_DateDropdown(String name, String[] values, boolean editable, StudentOtherDetailValue sodValue ) throws IOException 
	{
		int i;
		String value = null;
		boolean selected = false;	
		String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";
		writeToPage(getSpaces(8));
		writeToPage("<select name=\"" + name + "\" style=width:76px " + disabled + " tabindex=\"" + (this.tabIndex++) + "\" " + " >");
		for (i=0 ; i<values.length ; i++) {
			
			if(sodValue.getValueCode()!= null && sodValue.getValueCode()!= "" && sodValue.getValueCode().equals(values[i])) {
					selected = true	;
			}
			else
				selected = false;
			
			writeToPage(option(values[i], selected));
		}
		writeToPage("</select>");
		
	}

	private void displayValues_MultiDropdown(String name, StudentOtherDetailValue[] values, boolean editable) throws IOException 
	{
		int i;
		String value = null;
		boolean selected = false;	
		String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";

		displayRowStart();
		displayCellStart("transparent-small");
		displayTableStart();

		for (i=0 ; i<values.length ; i++) {
			String[] paramsTyes = values[i].getValueName().split(",");

			displayRowStart();
				displayCellStart("transparent");
					writeToPage(getSpaces(6));
					writeToPage("<b>" + paramsTyes[0] + "</b>");
				displayCellEnd();
				displayCellStart("transparent-small");
					writeToPage(getSpaces(8));
					writeToPage("<select name=\"" + paramsTyes[0] + "\" style=width:190px " + disabled + " tabindex=\"" + (this.tabIndex++) + "\" " + " >");
					writeToPage(option("Please Select", true));

					for(int j=1; j < paramsTyes.length; j++){
					StudentOtherDetailValue sdv = (StudentOtherDetailValue)values[i];
					value = paramsTyes[j].trim();
					if(sdv.getValueCode()!= null && sdv.getValueCode().equals(value))
						selected = true;
					else 
						selected=false;
				
					writeToPage(option(value, selected));
					}
					writeToPage("</select>");
				displayCellEnd();
			displayRowEnd();

		}	
		displayTableEnd();
		displayCellEnd();
		displayRowEnd();


	}
	private void displayControlRow(String field, String description, 
            boolean editable, StudentOtherDetailValue[] values) throws IOException 
   {
       displayRowStart();
       		displayCellStart("transparent");
       			writeToPage("<span>*&nbsp;</span><b>" + description + "</b>");
			displayCellEnd();
		displayRowEnd();  
       
       displayRowStart();
       	   displayCellStart("transparent-small");
               displayValues_DateDropdown("Month", DateUtils.getMonthOptions(),editable, values[0]);
               displayValues_DateDropdown("Day", DateUtils.getDayOptions(),editable, values[1]);
               displayValues_DateDropdown("Year", DateUtils.getYearOptions(),editable, values[2]);
          displayCellEnd();
       displayRowEnd();
}	


	private String option(String value, boolean isChecked) 
	{
		String selected = isChecked ? "selected" : "";
		return "<option " + selected + " >" + value + "</option>";
	}

	private String textBox(String name,String value,   boolean editable) 
	{
		String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";
		String nameId = name  ;
		System.out.println("nameId.." + nameId);
		return "<input type=\"text\" name=\"" + nameId + "\" id=\"" + nameId + "\"" +  "maxlength=" + "4" + 
		" style="+ " margin-left:"+"25px;"+	" value=\""+ value + "\" " +  
		" tabindex=\"" + (this.tabIndex++) + "\" " +
		disabled + " onkeypress=\""+ "return constrainNumericChar(event)" + "\"/>";
	}


	private String checkBox(String name, String value, boolean isChecked, boolean editable) 
	{
		String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";
		String nameId = name + "_" + value;
		return "<input type=\"checkbox\" name=\"" + nameId + "\" id=\"" + nameId + "\"" +
		" value=\""+ value + "\" " + 
		" tabindex=\"" + (this.tabIndex++) + "\" " +
		(isChecked?"checked=\"true\" ":" ") + disabled + "/>";
	}

	private String radioButton(String name, String value, boolean isChecked, boolean editable) 
	{
		String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";
		return "<input type=\"radio\" name=\"" + name + "\" id=\"" + name + "\"" +
		" value=\"" + value + "\" " + 
		" tabindex=\"" + (this.tabIndex++) + "\" " +
		(isChecked?"checked=\"true\" ":" ") + disabled + "/>";			//added for CA-ABE
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
	 * @return the educationAndInstruction
	 */
	public List getEducationAndInstruction() {
		return educationAndInstruction;
	}

	/**
	 * @param educationAndInstruction the educationAndInstruction to set
	 */
	public void setEducationAndInstruction(List educationAndInstruction) {
		this.educationAndInstruction = educationAndInstruction;
	}

	/**
	 * @return the viewOnly
	 */
	public Boolean getViewOnly() {
		return viewOnly;
	}

	/**
	 * @return the mandatoryField
	 */
	public Boolean getMandatoryField() {
		return mandatoryField;
	}

	/**
	 * @return the studentImported
	 */
	public Boolean getStudentImported() {
		return studentImported;
	}

	/**
	 * @return the tabIndex
	 */
	public int getTabIndex() {
		return tabIndex;
	}

}

