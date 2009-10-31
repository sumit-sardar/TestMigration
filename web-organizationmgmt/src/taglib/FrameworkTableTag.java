package taglib;

import dto.Level;
import dto.PathNode;
import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;


/**
 * @author Tai Truong
 *  
 */  
public class FrameworkTableTag extends CTBTag 
{
	private List levelList;
    private String userAgent;
    private String width;
	
    public void setLevelList(List levelList) {
    	
        this.levelList = levelList;       
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent.toLowerCase();
    }

    public void setWidth(String width) {
        this.width = width;
    }
    
	public int doStartTag() throws JspException 
    {
        if (this.width == null)
            this.width = "510";
            
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
        String displayType = getDisplayType();      

        displayFrameworkTableStart();
        
            displayRowStart("sortable");
                displayCellStartColspanHeight("sortableControls", "2", "30");
                    displayButton("renameId", "Rename", "renameRow();");
                    displayButton("deleteId", "Delete", "deleteRow();");
                    displayButton("insertBeforeId", "Insert Before", "addRow('before');");
                    displayButton("insertAfterId", "Insert After", "addRow('after');");
                displayCellEnd();
            displayRowEnd();  

            displayRowStart("dynamicHeader");
                displayCellHeaderStart("sortable alignCenter", "2%", "30");
                    writeToPage("&nbsp;Layer&nbsp;");
                displayCellHeaderEnd();
                displayCellHeaderStart("sortable alignLeft", "*", "30");
                    writeToPage("&nbsp;Name&nbsp;");
                displayCellHeaderEnd();
            displayRowEnd();  
            
            for (int i=0 ; i<levelList.size() ; i++) {
                Level level = (Level)levelList.get(i);
                String levelName = level.getName();                
                int index = i + 1;
                String permtext = buildPermissionText(level);
                
                displayFrameworkRowStart(index, permtext);
                    displayCellStart("dynamic", null, "center");
                        writeToPage(String.valueOf(index));
                    displayCellEnd();
                    displayCellStart("dynamic", null, "left");
                        displayStaticDiv(index, getDisplayType(), levelName);
                        displayInputDiv(index, getHiddenType(), levelName, level.getId().toString());
                    displayCellEnd();
                displayRowEnd();  
            }
                        
        displayTableEnd();
        
        
	}

    private String buildPermissionText(Level level)
    {
        String text = "";
        if (level.getDeletable().booleanValue())
            text += "deletable_";
        else
            text += "notDeletable_";
        if (level.getBeforeInsertable().booleanValue())
            text += "beforeInsertable_";
        else
            text += "notBeforeInsertable_";
        if (level.getAfterInsertable().booleanValue())
            text += "afterInsertable";
        else
            text += "notAfterInsertable";
            
        return text;
    }
    
	private void displayFrameworkTableStart() throws IOException 
    {
		StringBuffer buff = new StringBuffer();
		buff.append("<table id=\"frameworkTable\" class=\"dynamic\" width=\"" + this.width + "\" >");
		writeToPage(buff.toString());	
	}

	protected void displayFrameworkRowStart(int index, String permtext) throws IOException 
    {        
        String rowId = "src_row_" + String.valueOf(index);      
          
		StringBuffer buff = new StringBuffer();
		buff.append("<tr ");
        buff.append(" id=\"" + rowId + "\"");	
        buff.append(" permtext=\"" + permtext + "\"");	
        buff.append(" class=\"dynamic\" ");
        buff.append(" onmouseover=\"return hilightRow(this);\" ");
        buff.append(" onmouseout=\"return unhilightRow(this);\" ");
        buff.append(" onmousedown=\"return selectRow(this);\" ");
        buff.append(" ondblclick=\"return enableEditText(this);\" ");
		buff.append(">");
		writeToPage(buff.toString());	
	}

	private void displayStaticDiv(int index, String style, String text) throws IOException {
        String divId = "static_text_" + String.valueOf(index);  
              
		StringBuffer buff = new StringBuffer();
		buff.append("<div ");
		buff.append("id=\"" + divId + "\" ");
		buff.append("style=\"" + style + "\" >");
		buff.append(text);
		buff.append("</div>");
		writeToPage(buff.toString());	
	}

	private void displayInputDiv(int index, String style, String text, String alt) throws IOException {
        String divId = "input_text_" + String.valueOf(index);        
        String inputId = "txt_index_" + String.valueOf(index);        
        
		StringBuffer buff = new StringBuffer();
		buff.append("<div ");
		buff.append("id=\"" + divId + "\" ");
		buff.append("style=\"" + style + "\" >");

		buff.append("<input type=\"text\" ");
		buff.append("id=\"" + inputId + "\" ");
		buff.append("name=\"" + inputId + "\" ");
		buff.append("alt=\"" + alt + "\" ");
		buff.append("style=\"width: 450px\" ");
		buff.append("value=\"" + text + "\" ");
        buff.append("maxlength=\"32\" ");
       	buff.append("onkeypress=\"return handleEnter(event);\" ");
		buff.append("onblur=\"return resetInternalStates();\" ");
		buff.append(">");
		buff.append("</div>");
		writeToPage(buff.toString());	
	}

	private void displayButton(String id, String value, String onClick) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("&nbsp;&nbsp;<input type=\"button\" ");
		buff.append("id=\"" + id + "\" ");
		buff.append("value=\"" + value + "\" ");
		buff.append("onClick=\"" + onClick + "\" ");
		buff.append(">");
		writeToPage(buff.toString());	
	}

    private String getDisplayType()
    {
        String displayType = "display: block";
        
        if (userAgent.indexOf("firefox") != -1) {
            displayType = "display: table-row";
        }        
        if (userAgent.indexOf("mac") != -1) {
            displayType = "display: table-row";
        }        
        return displayType;                   
    }

    private String getHiddenType()
    {
        return "display: none";
    }
    
}

