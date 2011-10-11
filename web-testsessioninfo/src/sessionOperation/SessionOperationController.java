package sessionOperation;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import com.ctb.testSessionInfo.dto.FileInfo;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;

@Jpf.Controller()
public class SessionOperationController extends PageFlowController {
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
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "sessionsLink", path = "assessments_sessions.do"),
            @Jpf.Forward(name = "studentScoringLink", path = "assessments_studentScoring.do"),
            @Jpf.Forward(name = "programStatusLink", path = "assessments_programStatus.do")
        }) 
    protected Forward assessments()
    {
    	String menuId = (String)this.getRequest().getParameter("menuId");    	
    	String forwardName = (menuId != null) ? menuId : "sessionsLink";
    	
        return new Forward(forwardName);
    }

    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "assessments_sessions.jsp") 
        }) 
    protected Forward assessments_sessions()
    {
        return new Forward("success");
    }

    @Jpf.Action()
    protected Forward assessments_studentScoring()
    {
        try
        {
            String url = "/HandScoringWeb/scoringOperation/assessments.do";
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
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "reports.jsp"), 
            @Jpf.Forward(name = "viewReports", path = "viewReports.do") 
            
        }) 
    protected Forward reports()
    {
        return new Forward("success");
        //return new Forward("viewReports");
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
    ///////////////////////////// END OF NEW NAVIGATION ACTIONS ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
	
	
}