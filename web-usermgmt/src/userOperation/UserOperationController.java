package userOperation;


import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import com.ctb.util.web.sanitizer.SanitizedFormData;


@Jpf.Controller()
public class UserOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;

	private String userName = null;
	private Integer customerId = null;


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
	 * getUserDetails
	 */
	private void getUserDetails()
	{
		java.security.Principal principal = getRequest().getUserPrincipal();
		if (principal != null) 
			this.userName = principal.toString();
		else            
			this.userName = (String)getSession().getAttribute("userName");
	}
	
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="organizations.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "organizations.do")
	})
	protected Forward begin()
	{
		getUserDetails();
		
		return new Forward("success");
	}
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
	/**
	 * ASSESSMENTS actions
	 */    
    @Jpf.Action()
	protected Forward assessments()
	{
        try
        {
            String url = "/TestSessionInfoWeb/homepage/assessments.do";
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
	        @Jpf.Forward(name = "organizationsLink", path = "organizations_manageOrganizations.do"),
	        @Jpf.Forward(name = "studentsLink", path = "organizations_manageStudents.do"),
	        @Jpf.Forward(name = "usersLink", path = "organizations_manageUsers.do")
	    }) 
	protected Forward organizations()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "usersLink";
		System.out.println(forwardName);
		
	    return new Forward(forwardName);
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward organizations_manageOrganizations()
	{
		return new Forward("success");
	}
	
    @Jpf.Action()
	protected Forward organizations_manageStudents()
	{
        try
        {
            String url = "/StudentManagementWeb/studentOperation/organizations.do?menuId=studentsLink";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "beginFindUser.do") 
	    }) 
	protected Forward organizations_manageUsers()
	{
		getUserDetails();		

		return new Forward("success");
	}

    /**
     * REPORTS actions
     */    
    @Jpf.Action()
    protected Forward reports()
    {
        try
        {
            String url = "/TestSessionInfoWeb/homepage/reports.do";
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
		String forwardName = (menuId != null) ? menuId : "manageLicensesLink";
		System.out.println(forwardName);
		
	    return new Forward(forwardName);
	}
	
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "blankPage.jsp") 
        }) 
    protected Forward services_manageLicenses()
    {
        return new Forward("success");
    }
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_installSoftware()
	{
	    return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_downloadTest()
	{
	    return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_uploadData()
	{
	    return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_downloadData()
	{
	    return new Forward("success");
	}

	/**
	 * BROADCAST MESSAGE actions
	 */    
	/**
	 * @jpf:action
	 */
	@Jpf.Action()
	protected Forward broadcastMessage()
	{
	    return null;
	}
	
	
	/**
	 * MYPROFILE actions
	 */    
	@Jpf.Action()
	protected Forward myProfile()
	{
	    return null;
	}


    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF NEW NAVIGATION ACTIONS ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="findUser.jsp"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "findUser.jsp")
	})
	protected Forward beginFindUser()
	{
		return new Forward("success");
	}

	
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** MANAGEUSERFORM ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
	/**
	 * FormData get and set methods may be overwritten by the Form Bean editor.
	 */
	public static class UserOperationForm extends SanitizedFormData
	{

	}	
	
}