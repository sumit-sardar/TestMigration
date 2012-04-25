package softwareOperation;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import util.BroadcastUtils;
import weblogic.logging.NonCatalogLogger;

import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.testSessionInfo.utils.PermissionsUtils;
import com.ctb.util.OASLogger;

@Jpf.Controller()
public class SoftwareOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
	
    @Control()
    private com.ctb.control.db.BroadcastMessageLog message;
    
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
     * @jpf:forward name="success" path="installSoftware.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "installSoftware.jsp")
    })
    protected Forward begin()
    {    	
		getLoggedInUserPrincipal();
		
		getUserDetails();

		setupUserPermission();
    	    	
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
		
        String PC_URI = "'" + getdownloadURI("TDCINSTPC") + "'";
        String MAC_URI = "'" + getdownloadURI("TDCINSTMAC") + "'";
        String LINUX_URI = "'" + getdownloadURI("TDCINSTLIN") + "'";

        String RUN_PC_URI = "'" + getdownloadURI("TDCRUNPC") + "'";
        String RUN_MAC_URI = "'" + getdownloadURI("TDCRUNMAC") + "'";
        String RUN_LINUX_URI = "'" + getdownloadURI("TDCRUNLIN") + "'";
        
        this.getRequest().setAttribute("downloadURI_PC", "location.href=" + PC_URI);
        this.getRequest().setAttribute("downloadURI_MAC", "location.href=" + MAC_URI);
        this.getRequest().setAttribute("downloadURI_LINUX", "location.href=" + LINUX_URI);

        this.getRequest().setAttribute("runURI_PC", "location.href=" + RUN_PC_URI);
        this.getRequest().setAttribute("runURI_MAC", "location.href=" + RUN_MAC_URI);
        this.getRequest().setAttribute("runURI_LINUX", "location.href=" + RUN_LINUX_URI);
        
   		return new Forward("success");
    }
	
    /**
     * getdownloadURI: download installer URL based on user and OS 
    */
    private String getdownloadURI(String resourceTypeCode) 
    {
    	NonCatalogLogger logger =OASLogger.getLogger(this.getClass().getName());
   	 	logger.info("Entering getdownloadURI()");
   	 	String uri = "";
        try {      
            //Changes for OAS – Alternate URL - Part I-TAS
        	uri = this.testSessionStatus.getParentResourceUriForUser(this.userName, resourceTypeCode);
        }    
        catch( CTBBusinessException e ) {
            System.err.print(e.getStackTrace());
        }
        return uri;
    }
	
    
	/////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
    /**
	 * ASSESSMENTS actions
	 */    
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "sessionsLink", path = "assessments_sessionsLink.do"),
			@Jpf.Forward(name = "programStatusLink", path = "assessments_programStatus.do")
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
	protected Forward assessments_programStatus()
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
	        @Jpf.Forward(name = "bulkMoveLink", path = "organizations_manageBulkMove.do"),
	        @Jpf.Forward(name = "OOSLink", path = "organizations_manageOutOfSchool.do")
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
    
    @Jpf.Action()
	protected Forward organizations_manageOutOfSchool()
	{
        try
        {
            String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do";
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
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "begin.do") 
	    }) 
	protected Forward services_installSoftware()
	{
	    return new Forward("success");
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
	
    @Jpf.Action()
	protected Forward services_downloadData()
	{
        try
        {
            String url = "/OrganizationWeb/downloadOperation/services_downloadData.do";
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
	protected Forward broadcastMessage()
	{
	    return null;
	}
	
	
	@Jpf.Action()
	protected Forward myProfile()
	{
	    return null;
	}
	
	/**
     * STUDENT SCORING actions
     */    
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "studentScoringLink", path = "scoring_studentScoring.do")
        }) 
    protected Forward studentScoring()
    {
    	String menuId = (String)this.getRequest().getParameter("menuId");    	
    	String forwardName = (menuId != null) ? menuId : "studentScoringLink";
    	
        return new Forward(forwardName);
    }
    
    @Jpf.Action()
	protected Forward scoring_studentScoring()
	{
        try
        {
            String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
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
    
private void setUpAllUserPermission(CustomerConfiguration [] customerConfigurations) {
    	
    	boolean hasBulkStudentConfigurable = false;
    	boolean hasBulkStudentMoveConfigurable = false;
    	boolean hasOOSConfigurable = false;
    	boolean laslinkCustomer = false;
    	boolean adminUser = isAdminUser();
    	boolean hasUploadDownloadConfig = false;
    	boolean hasProgramStatusConfig = false;
    	boolean hasScoringConfigurable = false;
    	boolean hasLicenseConfiguration= false;
    	boolean TABECustomer = false;
    	String roleName = this.user.getRole().getRoleName();
    	
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				// For Bulk Accommodation
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true;
					continue;
				}
				// For Bulk Student Move
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Bulk_Move_Students") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentMoveConfigurable = true;
					continue;
				}
				// For Out Of School Student
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("OOS_Configurable") && 
						cc.getDefaultValue().equals("T")) {
					hasOOSConfigurable = true;
					continue;
				}
				// For LasLink Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
						&& cc.getDefaultValue().equals("T")) {
	            	laslinkCustomer = true;
	            	continue;
	            }
				// For Upload Download
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Upload_Download")
						&& cc.getDefaultValue().equals("T")) {
					hasUploadDownloadConfig = true;
					continue;
	            }
				// For Program Status
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Program_Status") && 
						cc.getDefaultValue().equals("T")) {
					hasProgramStatusConfig = true;
					continue;
				}
				// For Hand Scoring
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
	            		cc.getDefaultValue().equals("T")	) {
					hasScoringConfigurable = true;
					continue;
	            }
				//For License
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
	            		cc.getDefaultValue().equals("T")	) {
					hasLicenseConfiguration = true;
					continue;
	            }
				// For TABE Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer")) {
	            	TABECustomer = true;
	            	continue;
	            }
			}
			
		}
		this.getSession().setAttribute("showReportTab", new Boolean(userHasReports().booleanValue() || laslinkCustomer));
		this.getSession().setAttribute("isBulkAccommodationConfigured",new Boolean(hasBulkStudentConfigurable));
		this.getSession().setAttribute("isBulkMoveConfigured",new Boolean(hasBulkStudentMoveConfigurable));
		this.getSession().setAttribute("isOOSConfigured",new Boolean(hasOOSConfigurable));
		this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		this.getSession().setAttribute("hasProgramStatusConfigured",new Boolean(hasProgramStatusConfig && adminUser));
		this.getSession().setAttribute("hasScoringConfigured",new Boolean(hasScoringConfigurable));
		this.getSession().setAttribute("hasLicenseConfigured",new Boolean(hasLicenseConfiguration && adminUser));
		this.getSession().setAttribute("adminUser", new Boolean(adminUser));
		boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
		this.getSession().setAttribute("canRegisterStudent", new Boolean(TABECustomer && validUser));
		this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);
    }
    
	private void setupUserPermission()
	{
        CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);
        /*boolean adminUser = isAdminUser();*/
       /* boolean TABECustomer = isTABECustomer(customerConfigs);*/
        /*boolean laslinkCustomer = isLaslinkCustomer(customerConfigs);*/
        
        setUpAllUserPermission(customerConfigs);
        
        /*this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));*/
        
        /*this.getSession().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigs));*/
    	
        /*this.getSession().setAttribute("hasUploadDownloadConfigured", 
        		new Boolean( hasUploadDownloadConfig().booleanValue() && adminUser));*/
        
        /*this.getSession().setAttribute("hasProgramStatusConfigured", 
        		new Boolean( hasProgramStatusConfig(customerConfigs).booleanValue() && adminUser));*/
        
        /*this.getSession().setAttribute("hasScoringConfigured", 
        		new Boolean( customerHasScoring(customerConfigs).booleanValue() && adminUser));*/
        
       /* this.getSession().setAttribute("canRegisterStudent", canRegisterStudent(customerConfigs));*/
        
     	/*this.getSession().setAttribute("hasLicenseConfigured", hasLicenseConfiguration(customerConfigs).booleanValue() && adminUser);*/

		/*this.getSession().setAttribute("isBulkMoveConfigured",customerHasBulkMove(customerConfigs));*/
		
     	/*this.getSession().setAttribute("adminUser", new Boolean(adminUser));*/
     	
     	/*this.getSession().setAttribute("isOOSConfigured",customerHasOOS(customerConfigs));*/	// Changes for Out Of School
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
    
    /*private Boolean canRegisterStudent(CustomerConfiguration [] customerConfigs) 
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
    }*/
    
    /*private Boolean hasLicenseConfiguration(CustomerConfiguration [] customerConfigs)
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
    }*/
    
    /*private Boolean customerHasScoring(CustomerConfiguration [] customerConfigs)
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
    }*/

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
    
    /**
	 * Bulk Accommodation
	 */
	/*private Boolean customerHasBulkAccommodation(CustomerConfiguration[] customerConfigurations) 
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
	}*/
	

	/**
	 * Bulk Move
	 */
	/*private Boolean customerHasBulkMove(CustomerConfiguration[] customerConfigurations) 
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
	}*/
	
	// Changes for Out Of School
	/**
	 * Out Of School
	 */
	/*private Boolean customerHasOOS(CustomerConfiguration[] customerConfigurations) 
	{
		boolean hasOOSConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("OOS_Configurable") && 
						cc.getDefaultValue().equals("T")) {
					hasOOSConfigurable = true; 
					break;
				}
			}
		}
		return new Boolean(hasOOSConfigurable);           
	}*/
	
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
    
    /*private Boolean hasUploadDownloadConfig()
    {
        Boolean hasUploadDownloadConfig = Boolean.FALSE;
        try {   
            hasUploadDownloadConfig = this.testSessionStatus.hasUploadDownloadConfig(this.userName);
        } 
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return hasUploadDownloadConfig;
    }*/

    /*private Boolean hasProgramStatusConfig(CustomerConfiguration[] customerConfigurations)
    {	    	
    	Boolean hasProgramStatusConfig = Boolean.FALSE;
    	if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Program_Status") && 
						cc.getDefaultValue().equals("T")) {
					hasProgramStatusConfig = true; 
					break;
				}
			}
		}
		return new Boolean(hasProgramStatusConfig);   
    }*/
    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    


	
    
}