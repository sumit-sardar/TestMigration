package taglib;

import com.ctb.bean.testAdmin.CustomerReport;
import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;


/**
 * @author Tai Truong
 * 
 */ 
public class ReporNavigationTag extends CTBTag 
{
    private List reportList = null;    
    private String selectedReport = null;
    private String testAdminId = null;
    
    public void setReportList(List reportList) {
        this.reportList = reportList;
    }
    
    public void setSelectedReport(String selectedReport) {
        this.selectedReport = selectedReport;
    }
    
    public void setTestAdminId(String testAdminId) {
        this.testAdminId = testAdminId;
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
        displayTableStart("reportNavLinks floatLeft");		
			displayRowStart(null);                            

            displayCellStart(null, "37");
                writeToPage("&nbsp;");
            displayCellEnd();

            for (int i=0 ; this.reportList != null && i < this.reportList.size() ; i++) {
                CustomerReport cr = (CustomerReport)this.reportList.get(i);
                String reportName = cr.getReportName();
                String displayName = cr.getDisplayName();
                String verticalBar = (i == 0) ? "" : "|&nbsp;&nbsp;";

                if (this.selectedReport.equals(reportName)) {
                    displayCellStart("currentNav");
                        writeToPage("<a href=\"/TestSessionInfoWeb/homepage/viewReports.do?report=" + reportName + "\">" + displayName + "</a>");
                    displayCellEnd();
                }
                else {
                    displayCellStart(null);
                        String content = verticalBar;
                        content += "<a href=\"/TestSessionInfoWeb/homepage/viewReports.do?report=" + reportName + "\">" + displayName + "</a>";
                        writeToPage(content);
                        
                        if (i < this.reportList.size()-1) {
                            cr = (CustomerReport)this.reportList.get(i+1);
                            reportName = cr.getReportName();
                            if (this.selectedReport.equals(reportName)) {
                                writeToPage("&nbsp;&nbsp;|");
                            }
                        }
                        
                    displayCellEnd();
                }
    
            }
            if(this.reportList == null) {
                String verticalBar = "";
                displayCellStart(null);
                String content = verticalBar;
                content += "<a href=\"/TestSessionInfoWeb/viewmonitorstatus/begin.do?testAdminId=" + testAdminId + "\">Back</a>";
                writeToPage(content);
                displayCellEnd();
            }
        
			displayRowEnd();             
		displayTableEnd();
    }

     
}

