package downloadOperation;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.PermissionsUtils;

import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;

@Jpf.Controller()
public class DownloadOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
	

	private String userName = null;
	private Integer customerId = null;
    private User user = null;

	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

    /**
     * @jpf:action
     * @jpf:forward name="success" path="uploadData.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "downloadData.jsp")
    })
    protected Forward begin()
    {    	
		getLoggedInUserPrincipal();
		
		getUserDetails();

		setupUserPermission();

   		return new Forward("success");
    }
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
    /**
     * ASSESSMENTS actions
     */    
    @Jpf.Action(forwards = { 
    		@Jpf.Forward(name = "sessionsLink", path = "assessments_sessionsLink.do"),
    		@Jpf.Forward(name = "studentScoringLink", path = "assessments_studentScoringLink.do"),
    		@Jpf.Forward(name = "programStatusLink", path = "assessments_programStatusLink.do")
    })   
    protected Forward assessments()
    {

    	String menuId = (String)this.getRequest().getParameter("menuId");    	
    	String forwardName = (menuId != null) ? menuId : "sessionsLink";

    	return new Forward(forwardName);	    
    }


    @Jpf.Action()
    protected Forward assessments_sessionsLink()
    {
    	try
    	{
    		String url = "/SessionWeb/sessionOperation/assessments_sessions.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }

    @Jpf.Action()
    protected Forward assessments_studentScoringLink()
    {
    	try
    	{
    		String url = "/SessionWeb/sessionOperation/assessments_studentScoring.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }

    @Jpf.Action()
    protected Forward assessments_programStatusLink()
    {
    	try
    	{
    		String url = "/SessionWeb/programOperation/assessments_programStatus.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }


    /**
     * ORGANIZATIONS actions
     */    
    @Jpf.Action(forwards = { 
    		@Jpf.Forward(name = "studentsLink", path = "organizations_manageStudents.do"),
    		@Jpf.Forward(name = "usersLink", path = "organizations_manageUsers.do"),
    		@Jpf.Forward(name = "organizationsLink", path = "organizations_manageOrganizations.do"),
    		@Jpf.Forward(name = "bulkAccomLink", path = "organizations_manageBulkAccommodation.do"),
	        @Jpf.Forward(name = "bulkMoveLink", path = "organizations_manageBulkMove.do")
    }) 
    protected Forward organizations()
    {
    	String menuId = (String)this.getRequest().getParameter("menuId");    	
    	String forwardName = (menuId != null) ? menuId : "studentsLink";

    	return new Forward(forwardName);
    }


    @Jpf.Action()
    protected Forward organizations_manageOrganizations()
    {
    	try
    	{
    		String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }

    @Jpf.Action()
    protected Forward organizations_manageStudents()
    {
    	try
    	{
    		String url = "/StudentWeb/studentOperation/organizations_manageStudents.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }

    @Jpf.Action()
    protected Forward organizations_manageBulkAccommodation()
    {
    	try
    	{
    		String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }

    @Jpf.Action() 
    protected Forward organizations_manageUsers()
    {
    	try
    	{
    		String url = "/UserWeb/userOperation/organizations_manageUsers.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;

    }    

    @Jpf.Action()
	protected Forward organizations_manageBulkMove()
	{
        try
        {
            String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
    
    /**
     * REPORTS actions
     */    
    @Jpf.Action()
    protected Forward reports()
    {
    	try
    	{
    		String url = "/SessionWeb/sessionOperation/reports.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }


    /**
     * SERVICES actions
     */    
    @Jpf.Action(forwards = { 
    		@Jpf.Forward(name = "manageLicensesLink", path = "services_manageLicenses.do"),
    		@Jpf.Forward(name = "installSoftwareLink", path = "services_installSoftware.do"),
    		@Jpf.Forward(name = "downloadTestLink", path = "services_downloadTest.do"),
    		@Jpf.Forward(name = "uploadDataLink", path = "services_uploadData.do"),
    		@Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do")
    }) 
    protected Forward services()
    {
    	String menuId = (String)this.getRequest().getParameter("menuId");    	
    	String forwardName = (menuId != null) ? menuId : "installSoftwareLink";

    	return new Forward(forwardName);
    }

    @Jpf.Action()
    protected Forward services_manageLicenses()
    {
    	try
    	{
    		String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }



    @Jpf.Action()
    protected Forward services_installSoftware()
    {
    	try
    	{
    		String url = "/SessionWeb/softwareOperation/services_installSoftware.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }

    @Jpf.Action()
    protected Forward services_downloadTest()
    {
    	try
    	{
    		String url = "/SessionWeb/testContentOperation/services_downloadTest.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }

    @Jpf.Action()
    protected Forward services_uploadData()
    {
    	try
    	{
    		String url = "/OrganizationWeb/uploadOperation/services_uploadData.do";
    		getResponse().sendRedirect(url);
    	} 
    	catch (IOException ioe)
    	{
    		System.err.print(ioe.getStackTrace());
    	}
    	return null;
    }

    @Jpf.Action(forwards = { 
    		@Jpf.Forward(name = "success", path = "begin.do") 
    }) 
    protected Forward services_downloadData()
    {
    	return new Forward("success");
    }

	/**
	 * @jpf:action
	 */
	@Jpf.Action()
	protected Forward broadcastMessage()
	{
	    return null;
	}
	
	
	@Jpf.Action()
	protected Forward myProfile()
	{
	    return null;
	}


    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
    private void getLoggedInUserPrincipal()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) {
            this.userName = principal.toString();
        }        
        getSession().setAttribute("userName", this.userName);
    }
    
    private void getUserDetails()
    {
        Boolean supportAccommodations = Boolean.TRUE;
        String userTimeZone = "GMT";
             	
        try
        {
            this.user = this.testSessionStatus.getUserDetails(this.userName, this.userName);
            Customer customer = this.user.getCustomer();
            this.customerId = customer.getCustomerId();
            getSession().setAttribute("customerId", customerId); 
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
    }
    
	private void setupUserPermission()
	{
        CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);
        boolean adminUser = isAdminUser();
        boolean TABECustomer = isTABECustomer(customerConfigs);
        boolean laslinkCustomer = isLaslinkCustomer(customerConfigs);
        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));

        this.getSession().setAttribute("hasUploadDownloadConfigured", 
        		new Boolean( hasUploadDownloadConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasProgramStatusConfigured", 
        		new Boolean( hasProgramStatusConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasScoringConfigured", 
        		new Boolean( customerHasScoring(customerConfigs).booleanValue() && adminUser));
        
        this.getSession().setAttribute("canRegisterStudent", canRegisterStudent(customerConfigs));
        
     	this.getSession().setAttribute("hasLicenseConfigured", hasLicenseConfiguration(customerConfigs).booleanValue() && adminUser);
     	
     	this.getSession().setAttribute("isBulkMoveConfigured",customerHasBulkMove(customerConfigs));
    	
     	this.getSession().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigs));    	
    	
        this.getSession().setAttribute("adminUser", new Boolean(adminUser));     	
	}

    private Boolean userHasReports() 
    {
        boolean hasReports = false;
        try
        {      
            Customer customer = this.user.getCustomer();
            Integer customerId = customer.getCustomerId();   
            hasReports = this.testSessionStatus.userHasReports(this.userName, customerId);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return new Boolean(hasReports);           
    }

    private boolean isAdminUser() 
    {               
        String roleName = this.user.getRole().getRoleName();        
        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR); 
    }
    
    private Boolean canRegisterStudent(CustomerConfiguration [] customerConfigs) 
    {               
        String roleName = this.user.getRole().getRoleName();        
        boolean validCustomer = false; 

        for (int i=0; i < customerConfigs.length; i++)
        {
            CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer"))
            {
                validCustomer = true; 
            }               
        }
        
        boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
        
        return new Boolean(validCustomer && validUser);
    }
    
    private Boolean hasLicenseConfiguration(CustomerConfiguration [] customerConfigs)
    {               
    	 boolean hasLicenseConfiguration = false;

        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasLicenseConfiguration = true;
                break;
            } 
        }
       
        return new Boolean(hasLicenseConfiguration);
    }
    
    private Boolean customerHasScoring(CustomerConfiguration [] customerConfigs)
    {               
        Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasScoringConfigurable = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasScoringConfigurable = true;
            } 
        }
        return new Boolean(hasScoringConfigurable);
    }

    private boolean isLaslinkCustomer(CustomerConfiguration [] customerConfigs)
    {               
        boolean laslinkCustomer = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
					&& cc.getDefaultValue().equals("T")) {
            	laslinkCustomer = true;
            }
        }
        return laslinkCustomer;
    }

    private boolean isTABECustomer(CustomerConfiguration [] customerConfigs)
    {               
        boolean TABECustomer = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer")) {
            	TABECustomer = true;
            }
        }
        return TABECustomer;
    }
    
    private CustomerConfiguration [] getCustomerConfigurations(Integer customerId)
    {               
        CustomerConfiguration [] ccArray = null;
        try
        {      
            ccArray = this.testSessionStatus.getCustomerConfigurations(this.userName, customerId);       
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return ccArray;
    }
    
    private Boolean hasUploadDownloadConfig()
    {
        Boolean hasUploadDownloadConfig = Boolean.FALSE;
        try {   
            hasUploadDownloadConfig = this.testSessionStatus.hasUploadDownloadConfig(this.userName);
        } 
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return hasUploadDownloadConfig;
    }

    private Boolean hasProgramStatusConfig()
    {	
        Boolean hasProgramStatusConfig = Boolean.FALSE;
        try {   
            hasProgramStatusConfig = this.testSessionStatus.hasProgramStatusConfig(this.userName);
        } 
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return hasProgramStatusConfig;
    }
    
    /**
	 * Bulk Accommodation
	 */
	private Boolean customerHasBulkAccommodation(CustomerConfiguration[] customerConfigurations) 
	{
		boolean hasBulkStudentConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true; 
					break;
				}
			}
		}
		return new Boolean(hasBulkStudentConfigurable);           
	}
	
	/**
	 * Bulk Move
	 */
	private Boolean customerHasBulkMove(CustomerConfiguration[] customerConfigurations) 
	{
		boolean hasBulkStudentConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Bulk_Move_Students") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true; 
					break;
				}
			}
		}
		return new Boolean(hasBulkStudentConfigurable);           
	}

    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
	
}