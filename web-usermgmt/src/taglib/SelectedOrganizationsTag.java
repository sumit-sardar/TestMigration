package taglib;

import dto.PathNode;
import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;


/**
 * @author Tai Truong
 *  
 */  
public class SelectedOrganizationsTag extends CTBTag 
{
	private List selectedOrgNodes;
    private List orgNodesForSelector;
    private String userAgent;
	
    public void setSelectedOrgNodes(List selectedOrgNodes) {
        this.selectedOrgNodes = selectedOrgNodes;
    }
    
    public void setOrgNodesForSelector(List orgNodesForSelector) {
        this.orgNodesForSelector = orgNodesForSelector;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent.toLowerCase();
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
        displayOrgTableStart();
        
            String displayType = getDisplayType();      
            if (this.selectedOrgNodes.size() > 0) {
                displayType = "display: none";     
            }        
              
            displayOrgRowStart("message", displayType);   
                displayCellStart("transparent-small");
                    writeToPage(getMessageText());
                displayCellEnd();
            displayRowEnd();  

            
            for (int i=0 ; i<orgNodesForSelector.size() ; i++) {
                PathNode node = (PathNode)orgNodesForSelector.get(i);
                String orgId = node.getId().toString();
                String orgName = node.getName();                
                String fullpath = node.getFullPathName();
                String selectable = node.getSelectable();  
                
                displayType = getDisplayType();      
                if (selectable.equalsIgnoreCase("true")) {
                    displayType = "display: none";
                }
                
                displayOrgRowStart(orgId, displayType);   
                    displayCellStart("transparent-small");
                        writeToPage(getLinkText(fullpath, orgId, orgName));
                    displayCellEnd();
                displayRowEnd();  
            }
                        
        displayTableEnd();
        
        
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

    private String getMessageText()
    {
        return "<font color=\"gray\">None selected. Use the control on the right to select.</font>";
    }
    	
    private String getLinkText(String fullpath, String orgId, String orgName)
    {
        String linkText = "<a ";
        linkText += "href=\"#\" style=\"text-decoration: none\" ";
        linkText += "title=\" " + fullpath + "\" "; 
        linkText += "onclick=\"return setupOrgNodePath('" + orgId + "'); \" >";
        linkText += orgName;
        linkText += "</a>";        
        return linkText;
    }
}

