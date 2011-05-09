package administration;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;

import java.io.IOException;
import java.sql.SQLException;

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
    private User user = null;
    
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
     * @common:control
     */    
    @Control()
    private com.ctb.control.db.Users users;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
    
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
        //for Bulk Accommodation
        customerHasBulkAccommodation();
        customerHasScoring(); // For hand scoring changes
        this.getSession().setAttribute("hasUploadDownloadConfig", hasUploadDownloadConfig());
        this.getSession().setAttribute("hasProgramStatusConfig", hasProgramStatusConfig());
        
        this.getSession().setAttribute("hasLicenseConfig", hasLicenseConfig());
        this.getSession().setAttribute("userHasReports", userHasReports());
        this.getSession().setAttribute("isAdminUserAtLeafNode", isAdminUserAtLeafNode());
        
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
    protected Forward viewLicense()
    {
        try {
            String url = "/OrganizationManagementWeb/manageLicense/beginViewLicense.do";
            getResponse().sendRedirect(url);
        } 
        catch( IOException ioe ) {
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
            String url = "/OrganizationManagementWeb/manageLicense/beginManageLicense.do";
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
        try {
            this.user = this.userManagement.getUser(this.userName, 
                                                this.userName);
       //     this.displayNewMessage = user.getDisplayNewMessage();                                                
        }
        catch (Exception e) {
            e.printStackTrace();
        } 
        
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
     * isAdminUserAtLeafNode
     */
    private Boolean isAdminUserAtLeafNode()
    {
        Boolean isAdminUser = new Boolean(this.user.getRole().getRoleName().equals("Administrator"));
        Boolean leafNode = Boolean.TRUE;
    	
    	try {
			UserNodeData userTopNodes = this.userManagement.getTopUserNodesForUser(this.userName, null, null, null);		
	        UserNode[] userNodes = userTopNodes.getUserNodes();   
	        
	        for (int i=0 ; i<userNodes.length ; i++) {
	            UserNode userNode = userNodes[i];
	            if (userNode.getChildNodeCount().intValue() > 0) {
	            	leafNode = Boolean.FALSE;
	            }
	        }			
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
        return (isAdminUser && leafNode);
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

	//Bulk Accommodation
	private Boolean customerHasBulkAccommodation()
    {               
        Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasBulkStudentConfigurable = false;

        try
        {      
			CustomerConfiguration [] customerConfigurations = users.getCustomerConfigurations(customerId.intValue());
			if (customerConfigurations == null || customerConfigurations.length == 0) {
				customerConfigurations = users.getCustomerConfigurations(2);
			}
            
            for (int i=0; i < customerConfigurations.length; i++)
            {
            	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
                //Bulk Accommodation
                if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && cc.getDefaultValue().equals("T")	)
                {
                    this.getSession().setAttribute("isBulkAccommodationConfigured", true);
                    break;
                } 
            }
        }
        catch (SQLException se) {
        	se.printStackTrace();
		}
        
       
        return new Boolean(hasBulkStudentConfigurable);
    }
	
	//changes for scoring
	
	/**
	 * This method checks whether customer is configured to access the scoring feature or not.
	 * @return Return Boolean 
	 */
	private Boolean customerHasScoring()
    {               
		Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasScoringConfigurable = false;
        
        try
        {      
			CustomerConfiguration [] customerConfigurations = users.getCustomerConfigurations(customerId.intValue());
			if (customerConfigurations == null || customerConfigurations.length == 0) {
				customerConfigurations = users.getCustomerConfigurations(2);
			}
        

        for (int i=0; i < customerConfigurations.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasScoringConfigurable = true;
                  break;
            } 
        }
       }
        
        catch (SQLException se) {
        	se.printStackTrace();
		}
        getSession().setAttribute("isScoringConfigured", hasScoringConfigurable);
        return new Boolean(hasScoringConfigurable);
    }
    
	
       


}