package scoringOperation;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import com.ctb.util.web.sanitizer.SanitizedFormData;

@Jpf.Controller()
public class ScoringOperationController extends PageFlowController {
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
        getSession().setAttribute("userName", this.userName);
	}
	
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="organizations.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "assessments.do")
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
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "sessionsLink", path = "assessments_sessions.do"),
            @Jpf.Forward(name = "studentScoringLink", path = "assessments_studentScoring.do"),
            @Jpf.Forward(name = "programStatusLink", path = "assessments_programStatus.do")
        }) 
    protected Forward assessments()
    {
    	String menuId = (String)this.getRequest().getParameter("menuId");    	
    	String forwardName = (menuId != null) ? menuId : "studentScoringLink";
    	
        return new Forward(forwardName);
    }

    @Jpf.Action()
    protected Forward assessments_sessions()
    {
        try
        {
            String url = "/TestSessionInfoWeb/sessionOperation/assessments.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }

    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "studentScoring.jsp") 
        }) 
    protected Forward assessments_studentScoring()
    {
        return new Forward("success");
    }

    @Jpf.Action()
    protected Forward assessments_programStatus()
    {
        try
        {
            String url = "/TestSessionInfoWeb/programOperation/assessments.do";
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
    @Jpf.Action()
	protected Forward organizations()
	{
        try
        {
            String url = "/OrganizationManagementWeb/orgOperation/organizations.do";
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
            String url = "/TestSessionInfoWeb/sessionOperation/reports.do";
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
	@Jpf.Action()
    protected Forward services()
    {
        try
        {
            String url = "/OrganizationManagementWeb/orgOperation/services.do";
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
	
	
	/**
	 * @jpf:action
	 */    
	@Jpf.Action()
	protected Forward myProfile()
	{
	    return null;
	}


    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF NEW NAVIGATION ACTIONS ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
}