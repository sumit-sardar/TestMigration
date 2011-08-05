package downloadclient;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.testAdmin.CustomerResourceData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.OASLogger;

import java.sql.SQLException;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import weblogic.logging.NonCatalogLogger;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class DownloadClientController extends PageFlowController
{

    static final long serialVersionUID = 1L;
    private String userName = null;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
    

    // Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="download_client.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "download_client.jsp")
    })
    protected Forward begin()
    {
    	
    	java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) 
            this.userName = principal.toString();
        getSession().setAttribute("userName", this.userName);
    	
        String PC_URI = "'" + getdownloadURI("TDCINSTPC") + "'";
        String MAC_URI = "'" + getdownloadURI("TDCINSTMAC") + "'";
        String LINUX_URI = "'" + getdownloadURI("TDCINSTLIN") + "'";
        
        this.getRequest().setAttribute("downloadURI_PC", "location.href=" + PC_URI);
        this.getRequest().setAttribute("downloadURI_MAC", "location.href=" + MAC_URI);
        this.getRequest().setAttribute("downloadURI_LINUX", "location.href=" + LINUX_URI);

        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/homepage/HomePageController.jpf"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "/homepage/HomePageController.jpf")
		}
	)
    protected Forward goto_homepage()
    {
        return new Forward("success");
    }

    
   /**
     * getdownloadURI: download installer URL based on user and OS 
    */
    private String getdownloadURI(String resourceTypeCode) 
    {
    	NonCatalogLogger logger =OASLogger.getLogger(this.getClass().getName());
   	 	logger.info("Entering getdownloadURI()");
        try {      
            //Changes for OAS – Alternate URL - Part I-TAS
            String uri = this.testSessionStatus.getParentResourceUriForUser(this.userName, resourceTypeCode);
            if(uri != null && !uri.equals(""))
            	return uri;

        }    
        catch( CTBBusinessException e ) {
            System.err.print(e.getStackTrace());
        }
        
        // should not get here anyway
        if (resourceTypeCode.equals("TDCINSTPC")) 
            return "http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.exe";
        else            
        if (resourceTypeCode.equals("TDCINSTMAC")) 
            return "http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.zip";        
        else            
        if (resourceTypeCode.equals("TDCINSTLIN")) 
            return "http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.bin";        
        else            
        return "http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.exe";
    }
    
}
