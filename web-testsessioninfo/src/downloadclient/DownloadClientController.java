package downloadclient;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.testAdmin.CustomerResourceData;
import java.sql.SQLException;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class DownloadClientController extends PageFlowController
{

    static final long serialVersionUID = 1L;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.CustomerResource customerResource;

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
        String PC_URI = "'" +
                        getdownloadURI("TDCINSTPC") +
                        "'";
        String MAC_URI = "'" +
                         getdownloadURI("TDCINSTMAC") +
                         "'";
        
        this.getRequest().setAttribute("downloadURI_PC", "location.href=" +
                                                         PC_URI);
        this.getRequest().setAttribute("downloadURI_MAC", "location.href=" +
                                                          MAC_URI);

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
     * getdownloadURI: download installer URL based on customer and OS 
    */
    private String getdownloadURI(String resourceTypeCode) 
    {
        try {
            CustomerResourceData[] crds = null;
            CustomerResourceData crd = null;
            Integer customerId = (Integer)getSession().getAttribute("customerId");               
            if (customerId == null) 
                customerId = new Integer(2);    // CTB customer
                
            crds = this.customerResource.getCustomerResource(customerId);
            if (crds != null) {
                for (int i=0 ; i<crds.length ; i++) {
                    crd = crds[i];
                    if (crd.getResourceTypeCode().equalsIgnoreCase(resourceTypeCode))
                        return crd.getResourceURI();
                }
            }

            customerId = new Integer(2);    // CTB customer
            crds = this.customerResource.getCustomerResource(customerId);
            if (crds != null) {
                for (int i=0 ; i<crds.length ; i++) {
                    crd = crds[i];
                    if (crd.getResourceTypeCode().equalsIgnoreCase(resourceTypeCode))
                        return crd.getResourceURI();
                }
            }
        }    
        catch( SQLException e ) {
            System.err.print(e.getStackTrace());
        }
        
        // should not get here anyway
        if (resourceTypeCode.equals("TDCINSTPC")) 
            return "/downloadfiles/InstallOnlineAsmt.exe";
        else            
            return "/downloadfiles/InstallOnlineAsmt.zip";        
    }
    
}
