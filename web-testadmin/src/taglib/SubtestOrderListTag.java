package taglib;

import data.SubtestVO;
import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;


/**
 * @author Tai Truong
 * 
 */ 
public class SubtestOrderListTag extends CTBTag 
{
	private List allSubtests = null;
	private List availableSubtests = null;
	private List selectedSubtests = null;
	private List levelOptions = null;
    private Boolean changeLevel = Boolean.TRUE;
    private Boolean showLevel = Boolean.TRUE;    
	private String broswerType = "msie";    
    private String numberOfRows = "0";
    
    
    public void setAllSubtests(List allSubtests) {
        this.allSubtests = allSubtests;
    }

    public void setAvailableSubtests(List availableSubtests) {
        this.availableSubtests = availableSubtests;
    }

    public void setSelectedSubtests(List selectedSubtests) {
        this.selectedSubtests = selectedSubtests;
    }
    
    public void setLevelOptions(List levelOptions) {
        this.levelOptions = levelOptions;
    }

    public void setChangeLevel(Boolean changeLevel) {
        this.changeLevel = changeLevel;
    }

    public void setShowLevel(Boolean showLevel) {
        this.showLevel = showLevel;
    }

    public void setBroswerType(String broswerType) {
        this.broswerType = broswerType;
    }

	            
	public int doStartTag() throws JspException {
		try { 
            int count = this.allSubtests.size();
            this.numberOfRows = String.valueOf(count);
            
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
        displayTableStart("layout");		
			displayRowStart("layout");
            
                // source table
				displayCellStart("layout", "48%");
                    displaySourceTable();
				displayCellEnd();

                // button table
				displayCellStart("layout", "4%");
                    displayButtonTable();
				displayCellEnd();

                // right table
				displayCellStart("layout", "48%");
                    displayDestinationTable();
				displayCellEnd();

			displayRowEnd();
                    

            // move up / down buttons
			displayRowStart("layoutButtom");
				displayCellStart("layoutButtom", null, null, null, "3");
                    displayTableStart("transparent", null, "right");		
                        displayRowStart("transparent");
                            displayCellStart("transparent");
                                writeToPage("<input type=\"button\" value=\"Move Up\" name=\"moveUp\" id=\"moveUp\" disabled onclick=\"return moveRow( chosenRow, -1 );\" style=\"font-family: Arial, Verdana, Sans Serif; font-size: 11px;\"/>&nbsp;");
                                writeToPage("<input type=\"button\" value=\"Move Down\" name=\"moveDown\" id=\"moveDown\" disabled onclick=\"return moveRow( chosenRow, 1 );\" style=\"font-family: Arial, Verdana, Sans Serif; font-size: 11px;\"/>&nbsp;");                                                  
                            displayCellEnd();
                        displayRowEnd();
                    displayTableEnd();                    
				displayCellEnd();                
			displayRowEnd();
            
		displayTableEnd();
    }

    private void displaySourceTable() throws IOException 
    {
        displayTableStart("columnLayout");		
			displayRowStart("columnLayout");
				displayCellStart("columnLayout");
                
                    // available subtest title 
                    displayTableStart("dynamic");		
                        displayRowStart("dynamicHeader");
                            displayHeaderStart("dynamicHeader");
                                writeToPage("<input type=\"hidden\" id=\"numberOfRows\" name=\"numberOfRows\" value=\"" + this.numberOfRows + "\">");
                                writeToPage("Available Subtests");
                            displayHeaderEnd();
                        displayRowEnd();
                    displayTableEnd();
                    
                    
                    // list of rows
                    displaySourceTableStart();
                    
                    for (int i=0 ; i<this.allSubtests.size() ; i++) {
                        SubtestVO subtest = (SubtestVO)this.allSubtests.get(i);
                        int index = i + 1;
                        String displayFlag = getDisplayHide(); 
                        if (containItem(this.availableSubtests, subtest)) {
                            displayFlag = getDisplayShow(); 
                        }                        
                        displaySourceRowStart(index, displayFlag);                            
                            displaySourceCell(subtest.getSubtestName());
                        displayRowEnd();                                    
                    }
                    
                    displayTableEnd();        
                    
				displayCellEnd();
			displayRowEnd();
		displayTableEnd();
    }

    private void displayDestinationTable() throws IOException 
    {
        displayTableStart("columnLayout");		
			displayRowStart("columnLayout");
				displayCellStart("columnLayout");

                    // selected subtest title
                    displayTableStart("dynamic");		
                        displayRowStart("dynamicHeader");
                            displayHeaderStart("dynamicHeader");
                                writeToPage("Selected Subtests");
                            displayHeaderEnd();
                        if (this.showLevel.booleanValue()) {
                            displayHeaderStart("dynamicHeader", "40");
                                writeToPage("Level");
                            displayHeaderEnd();
                        }
                        displayRowEnd();
                    displayTableEnd();
                      
                    // list of rows
                    displayDestinationTableStart();
                    
                    for (int i=0 ; i<this.allSubtests.size() ; i++) {
                        SubtestVO subtest = (SubtestVO)this.allSubtests.get(i);
                        int index = i + 1;
                        String displayFlag = getDisplayHide(); 
                        String level = subtest.getLevel();
                        if (containItem(this.selectedSubtests, subtest)) {
                            displayFlag = getDisplayShow(); 
                            level = getItemLevel(this.selectedSubtests, subtest);
                        }                                                
                        displayDestinationRowStart(index, displayFlag);  
                            String value = String.valueOf(index);
                            
                            if (displayFlag.equals(getDisplayHide()))
                                value = "0";        
                                              
                            displayDestinationNameCell(subtest.getSubtestName(), index, value);
                            if (this.showLevel.booleanValue()) {
                                if (this.changeLevel.booleanValue())
                                    displayDestinationLevelDropdown(index, level);
                                else
                                    displayDestinationLevelStatic(index, level);
                            }
                        displayRowEnd();                                    
                    }
                    
                    displayTableEnd();        


				displayCellEnd();
			displayRowEnd();
		displayTableEnd();
    }

    private void displayButtonTable() throws IOException 
    {
        displayTableStart("columnButtonLayout");		
			displayRowStart("columnButtonLayout");
				displayCellStart("columnButtonLayout");
                
                    String disabled = null;
                    
                    // navigation buttons
                    displayTableStart("transparent");		
                        displayRowStart();
                            displayCellStart();
                                disabled = "disabled";
                                displayButton("addRow", ">", disabled, "addSelectedRow(chosenRow);");                
                            displayCellEnd();
                        displayRowEnd();            
                        displayRowStart();
                            displayCellStart();
                                disabled = "disabled";
                                if (this.availableSubtests.size() > 0)
                                    disabled = "";                    
                                displayButton("addAllRows", ">>", disabled, "addAllSelectedRows(chosenRow);");                
                            displayCellEnd();
                        displayRowEnd();            
                        displayRowStart();
                            displayCellStart();
                                writeToPage("&nbsp;");
                            displayCellEnd();
                        displayRowEnd();
                        displayRowStart();
                            displayCellStart();
                                disabled = "disabled";
                                displayButton("removeRow", "<", disabled, "removeSelectedRow(chosenRow);");                
                            displayCellEnd();
                        displayRowEnd();            
                        displayRowStart();
                            displayCellStart();
                                disabled = "disabled";
                                if (this.selectedSubtests.size() > 0)
                                    disabled = "";                    
                                displayButton("removeAllRows", "<<", disabled, "removeAllSelectedRows(chosenRow);");                
                            displayCellEnd();
                        displayRowEnd();            
                    displayTableEnd();
            
				displayCellEnd();
			displayRowEnd();            
		displayTableEnd();
    }

	private void displaySourceTableStart() throws IOException {
		StringBuffer buff = new StringBuffer();
        buff.append("<table class=\"dynamic\" onselectstart=\"return false;\" >");
		writeToPage(buff.toString());
	}

	private void displaySourceRowStart(int index, String displayFlag) throws IOException {
        String id = "src_row_" + String.valueOf(index);
        String display = "display: " + displayFlag;
		StringBuffer buff = new StringBuffer();
        buff.append("<tr id=\"" + id + "\" class=\"dynamic\" style=\"" + display + "\" onmouseover=\"return hilightRow(this);\" onmouseout=\"return unhilightRow(this);\"  onmousedown=\"return selectRow(this);\">");    
		writeToPage(buff.toString());
    }

	private void displaySourceCell(String name) throws IOException 
    {
		StringBuffer buff = new StringBuffer();
        buff.append("<td class=\"dynamic\">" + name + "</td>");
		writeToPage(buff.toString());        
    }
        

	private void displayDestinationTableStart() throws IOException {
		StringBuffer buff = new StringBuffer();
        buff.append("<table class=\"dynamic\" onselectstart=\"return false;\" >");
		writeToPage(buff.toString());
	}

	private void displayDestinationRowStart(int index, String displayFlag) throws IOException {
        String id = "des_row_" + String.valueOf(index);
        String display = "display: " + displayFlag;
		StringBuffer buff = new StringBuffer();
        buff.append("<tr id=\"" + id + "\" class=\"dynamic\" style=\"" + display + "\" onmouseover=\"return hilightRow(this);\" onmouseout=\"return unhilightRow(this);\"  onmousedown=\"return selectRow(this);\">");    
		writeToPage(buff.toString());
    }

	private void displayDestinationNameCell(String name, int index, String value) throws IOException 
    {
        String strIndex = "index_" + String.valueOf(index);
		StringBuffer buff = new StringBuffer();
        buff.append("<td class=\"dynamic\">");
        buff.append("<input type=\"hidden\" value=\"" + value + "\" name=\"" + strIndex + "\" id=\"" + strIndex + "\" />");
        buff.append(name);
        buff.append("</td>");
		writeToPage(buff.toString());        
    }

    private void displayDestinationLevelDropdown(int index, String level) throws IOException 
    {
        String value = String.valueOf(index);
        String strLevel = "level_" + value;
		StringBuffer buff = new StringBuffer();
        buff.append("<td class=\"dynamic\" width=\"40\" align=\"center\" >");
        buff.append("<select name=\"" + strLevel + "\" id=\"" + strLevel + "\" size=\"1\" style=\"font-family: Arial, Verdana, Sans Serif; font-size: 11px;\">");
        for (int i=0 ; i<this.levelOptions.size() ; i++) {
            String option = (String)this.levelOptions.get(i);         
            if (option.equalsIgnoreCase(level))           
                buff.append("<option value=\"" + option + "\" selected>" + option + "</option>");
            else
                buff.append("<option value=\"" + option + "\">" + option + "</option>");
        }
        buff.append("</select>");
        buff.append("</td>");
		writeToPage(buff.toString());        
    }

    private void displayDestinationLevelStatic(int index, String level) throws IOException 
    {
        String value = String.valueOf(index);
        String strLevel = "level_" + value;
		StringBuffer buff = new StringBuffer();
        buff.append("<td class=\"dynamic\" width=\"40\" align=\"center\" >");
        buff.append("<input type=\"hidden\" name=\"" + strLevel + "\" id=\"" + strLevel + "\" value=\"" + level + "\" >");
        buff.append(level);
        buff.append("</td>");
		writeToPage(buff.toString());        
    }

    private void displayButton(String id, String value, String disabled, String func) throws IOException 
    {        
		StringBuffer buff = new StringBuffer();
        buff.append("<input type=\"button\" value=\"" + value + "\" name=\"" + id + "\" id=\"" + id + "\" ");
        buff.append(disabled + " onclick=\"return " + func + "\" style=\"width:30px\" />");          
		writeToPage(buff.toString());        
    }
    
    private String getDisplayShow()
    {
        if (this.broswerType.equalsIgnoreCase("msie"))
            return "block";
        else
            return "table_row";
    }

    private String getDisplayHide()
    {
        return "none";
    }


    private boolean containItem(List srcList, SubtestVO item)
    {
        boolean found = false;
        for (int i=0 ; i<srcList.size() ; i++) {        
            SubtestVO subtest = (SubtestVO)srcList.get(i);    
            if (subtest.getId().intValue() == item.getId().intValue()) {
                found = true;
            }
        }
        return found;
    }

    private String getItemLevel(List srcList, SubtestVO item)
    {
        String level = "";
        for (int i=0 ; i<srcList.size() ; i++) {        
            SubtestVO subtest = (SubtestVO)srcList.get(i);    
            if (subtest.getId().intValue() == item.getId().intValue()) {
                level = subtest.getLevel();
            }
        }
        return level;
    }
    
}

