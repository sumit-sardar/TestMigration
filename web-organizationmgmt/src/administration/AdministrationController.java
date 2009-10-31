package administration;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import java.io.IOException;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class AdministrationController extends PageFlowController
{


    static final long serialVersionUID = 1L;
    
    private String userName = null;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.ProgramStatus programStatus;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.uploadDownloadManagement.UploadDownloadManagement uploadDownloadManagement;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.licensing.Licensing licensing;
    
    
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward manageProgram()
    {
        try
        {
            String url = "/TestSessionInfoWeb/manageProgram/begin.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="administration.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "administration.jsp")
    })
    protected Forward begin()
    {
        getUserDetails();
        this.getSession().setAttribute("hasUploadDownloadConfig", hasUploadDownloadConfig());
        this.getSession().setAttribute("hasProgramStatusConfig", hasProgramStatusConfig());
        this.getSession().setAttribute("hasLicenseConfig", hasLicenseConfig());
        this.getSession().setAttribute("userHasReports", userHasReports());
        return new Forward("success");
    }
    
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward manageOrganization()
    {
        try
        {
            String url = "/OrganizationManagementWeb/manageOrganization/begin.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
    
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward manageUpload()
    {
        try
        {
            String url = "/OrganizationManagementWeb/manageUpload/begin.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }

    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward manageDownload()
    {
        try
        {
            String url = "/OrganizationManagementWeb/manageDownload/begin.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }

    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward manageLicense()
    {
        try {
            String url = "/OrganizationManagementWeb/manageLicense/begin.do";
            getResponse().sendRedirect(url);
        } 
        catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }

    /**
     * getUserDetails
     */
    private void getUserDetails()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) 
            this.userName = principal.toString();
        else            
            this.userName = (String)getSession().getAttribute("userName");
        
        getSession().setAttribute("userName", this.userName);
    }
    
    /**
     * userHasReports
     */
    private Boolean userHasReports() 
    {
        Boolean hasReports = Boolean.FALSE;
        try {   
            hasReports = this.organizationManagement.userHasReports(this.userName);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return hasReports;
    }
    
    /**
     * userHasReports
     */
    private Boolean hasUploadDownloadConfig()
    {
        Boolean hasUploadDownloadConfig = Boolean.FALSE;
        try {   
           
            hasUploadDownloadConfig = this.uploadDownloadManagement.hasUploadDownloadConfig(this.userName);
        
        } catch (CTBBusinessException be) {
            
            be.printStackTrace();
        
        }
        return hasUploadDownloadConfig;
    }

    /**
     * hasProgramStatusConfig
     */
    private Boolean hasProgramStatusConfig()
    {
        Boolean hasProgramStatusConfig = Boolean.FALSE;
        try {   
            hasProgramStatusConfig = this.programStatus.hasProgramStatusConfig(this.userName);
        
        } catch (CTBBusinessException be) {
            
            be.printStackTrace();
        
        }
        return hasProgramStatusConfig;
    }

    /**
     * hasLicenseConfig
     */
    private Boolean hasLicenseConfig()
    {
        Boolean hasLicenseConfig = Boolean.FALSE;        
        try {
            CustomerLicense[] cls = this.licensing.getCustomerLicenseData(this.userName, null);            
            hasLicenseConfig = new Boolean(cls.length > 0);
        }    
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return hasLicenseConfig;
    }



}