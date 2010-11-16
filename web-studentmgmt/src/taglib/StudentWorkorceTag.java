package taglib;

import com.ctb.bean.studentManagement.StudentOtherDetail;
import com.ctb.bean.studentManagement.StudentOtherDetailValue;
import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;


/**
 * @author TCS
 *  
 */  
public class StudentWorkorceTag extends CTBTag 
{
	private List studentWorkForceDetails;
	private Boolean viewOnly = Boolean.FALSE;
	//CA-ABE student intake
	private Boolean mandatoryField = Boolean.FALSE;
	private Boolean studentImported = Boolean.FALSE;
	private int tabIndex = 1;
	private String section = "Workforce";



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
			if ((this.studentWorkForceDetails != null) && (this.studentWorkForceDetails.size() > 0)) {
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
		int totalCount = this.studentWorkForceDetails.size();
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
			StudentOtherDetail sdd = (StudentOtherDetail)this.studentWorkForceDetails.get(i);
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
			StudentOtherDetail sdd = (StudentOtherDetail)this.studentWorkForceDetails.get(i);
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

	private void displayStudentProgramGoals(StudentOtherDetail sdd) throws IOException 
	{
		String displayName = sdd.getLabelName().trim();
		boolean multipleAllowed = sdd.getValueCardinality().equals("dropdown");
		boolean editable = true;

		System.out.println("1");
		displayRowStart();
		displayCellStart("transparent");
		//Changes for CA-ABE student intake
		StudentOtherDetailValue[] values = sdd.getStudentOtherDetailValues();
		if (this.mandatoryField && values.length > 1) {

			writeToPage("<span>*&nbsp;</span><b>" + displayName + "</b>");
		} else {
			writeToPage("<b>" + displayName + "</b>");
		}

		displayCellEnd();
		displayRowEnd();  

		//StudentOtherDetailValue[] values = sdd.getStudentOtherDetailValues();

		if( multipleAllowed ) {
			displayValues_Dropdown(displayName, values, editable);
		} else { 
			
			displayValues_TextBox(displayName, values[0] ,editable);

		}
	}

		private void displayValues_TextBox(String displayName,StudentOtherDetailValue value,
				boolean enable) throws IOException {
			System.out.println("text box");
			// TODO Auto-generated method stub
			displayRowStart();
			displayCellStart("transparent-small");
			String optionValue = value.getValueName().trim();
			displayTableStart();
			displayRowStart();
			displayCellStart("transparent-small");
			writeToPage(textBox(section+ "_" + displayName ,optionValue,enable));
			displayCellEnd();
			displayRowEnd();
			displayTableEnd();
			displayCellEnd();
			displayRowEnd();


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
			" tabindex=\"" + (this.tabIndex++) + "\" " +
			(isChecked?"checked=\"true\" ":" ") + disabled + "/>";
		}

		private String textBox(String name,String value,   boolean editable) 
		{
			String disabled = (this.viewOnly.booleanValue() || (! editable)) ? " disabled " : "";
			String nameId = name  ;
			System.out.println("nameId.." + nameId);
			
			String constrainNumericChar = (nameId.equals("Workforce_Scheduled Work Hours Per Week") || nameId.equals("Workforce_Hourly Wage")) ? " return constrainNumericChar(event);": "";
			//String focusNextControl = (nameId.equals("Scheduled Work Hours Per Week") || nameId.equals("Hourly Wage")) ? " focusNextControl(this);": "";
			
			return "<input type=\"text\" name=\"" + nameId + "\" id=\"" + nameId + "\"" +  "maxlength=" + "64" + 
			" style="+ " margin-left:"+"25px;"+	" value=\""+ value + "\" " +  
			" tabindex=\"" + (this.tabIndex++) + "\" " +
			  disabled +" onkeypress=\""+ constrainNumericChar + "\"/>";
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
		public List getStudentWorkForceDetails() {
			return studentWorkForceDetails;
		}

		/**
		 * @param programAndGoals the programAndGoals to set
		 */
		public void setStudentWorkForceDetails(List studentWorkForceDetails) {
			this.studentWorkForceDetails = studentWorkForceDetails;
		}

	}

