package taglib;

import com.ctb.bean.testAdmin.CustomerReport;
import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;


/**
 * @author Tai Truong
 * 
 */ 
public class ReportNameListTag extends CTBTag 
{
    private List reportList = null;    
    private String displayStyle = " style=\"display: block\"";            
    
    public void setReportList(List reportList) {
        this.reportList = reportList;
    }
    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }

	            
	public int doStartTag() throws JspException {
		try { 
            if ((this.reportList != null) && (this.reportList.size() > 0))            
                displayContent();
            else
                displayMessage();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new JspException(e.toString());
		}

		return SKIP_BODY;
	}

    private void displayContent() throws IOException 
    {
        displayTableStart("transparent");		

        for (int i=0 ; i<this.reportList.size() ; i++) {
            CustomerReport cr = (CustomerReport)this.reportList.get(i);
            String content = null;
            String reportName = cr.getReportName();
            String displayName = cr.getDisplayName();
            String description = cr.getDescription();
            String url = cr.getReportUrl();
            
			displayRowStart("transparent");                            
				displayCellStart("transparent", "32");
                    writeToPage("&nbsp;");
				displayCellEnd();
				displayCellStart("transparent", "650");
                    content = "<li style=\"list-style-type: square;\" >";
                    content += "<a href=\"/SessionWeb/sessionOperation/TABEReport.do?report=" + reportName + "\"" + this.displayStyle + " >" + displayName + "</a>";
                    content += "</li>";                    
                    content += description;                    
                    writeToPage(content);
				displayCellEnd();
			displayRowEnd();     
            
        }
        
		displayTableEnd();
    }


    private void displayMessage() throws IOException 
    {
        writeToPage("<div class=\"informationMessage\">");                        
        writeToPage("<table>");
        writeToPage("<tr>");
            writeToPage("<th rowspan=\"2\"><img src=\"/SessionWeb/resources/images/messaging/icon_info.gif\" border=\"0\" width=\"23\" height=\"23\"></th>"); 
            writeToPage("<th>There are no reports associated with the selected program.</th>");
        writeToPage("</tr>");
        writeToPage("<tr>");
            writeToPage("<td>");
                writeToPage("Select a different program to view reports.");
            writeToPage("</td>");
        writeToPage("</tr>");
        writeToPage("</table>");                        
        writeToPage("</div>");                
    }
    
}

