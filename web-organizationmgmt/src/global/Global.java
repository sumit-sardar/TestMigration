package global;

import org.apache.beehive.netui.pageflow.*;
import dto.NavigationPath;
import java.io.IOException;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * The Global page flow is used to define actions which can be invoked by any other
 * page flow in a webapp. The "jpf:catch" annotation provides a global way to catch
 * unhandled exceptions by forwarding to an error page.
 *
 * removed @jpf:catch type="PageFlowException" method="handlePageFlowException"
 *
 * @jpf:catch type="Exception" method="handleException"
 */
@Jpf.Controller(catches = { 
    @Jpf.Catch(type = Exception.class,
               method = "handleException")
})
public class Global extends GlobalApp
{ 
    static final long serialVersionUID = 1L;
    
    
    public NavigationPath navPath = null;
    public static final String ACTION_DEFAULT = "defaultAction";
    public static final String ACTION_FIND_ORGANIZATION = "findOrganization";
    public static final String ACTION_ADD_ORGANIZATION = "addOrganization";
    public static final String ACTION_VIEW_ORGANIZATION = "viewOrganization";
    public static final String ACTION_EDIT_ORGANIZATION = "editOrganization";
    public static final String ACTION_DELETE_ORGANIZATION = "deleteOrganization";
    public static final String ACTION_FIND_CUSTOMER      = "findCustomer";
    public static final String ACTION_VIEW_CUSTOMER      = "viewCustomer";
    public static final String ACTION_EDIT_CUSTOMER      = "editCustomer";
    public static final String ACTION_ADD_CUSTOMER       = "addCustomer";
    //added for manage License
    public static final String ACTION_ADD_EDIT_LICENSE       = "addEditLicense";
    public static final String ACTION_ADD_FRAMEWORK        = "addFramework";
    public static final String ACTION_EDIT_FRAMEWORK     = "editFramework";
    public static final String ACTION_VIEW_CUSTOMER_FRAMEWORK     = "viewCustomerFramework";
    public static final String ACTION_APPLY_SEARCH   = "applySearch";
    public static final String ACTION_CLEAR_SEARCH   = "clearSearch";
     
    /**
     * Global action to go to the homepage.
    * @jpf:action
    */
    @Jpf.Action()
    public Forward homepage()
    {
        try
        {
            getResponse().sendRedirect("/SessionWeb/sessionOperation/begin.do");
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
    public Forward logout()
    {
        try
        {
            getResponse().sendRedirect("/SessionWeb/logout.do");
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
    public Forward sessionTimeout()
    {
        try
        {
            getResponse().sendRedirect("/SessionWeb/sessionTimeout.do");
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }        
        return null;
    }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="errorPage" path="/error.jsp"
     * @jpf:forward name="sessionTimeout" path="sessionTimeout.do"
     */
    @Jpf.ExceptionHandler(forwards = { 
        @Jpf.Forward(name = "errorPage",
                     path = "/error.jsp"), 
			@Jpf.Forward(name = "sessionTimeout", path = "sessionTimeout.do")
		}
	)
    protected Forward handleException( Exception ex, String actionName,
                                       String message, FormData form )
    {
        String userName = (String)getSession().getAttribute("userName");
        if (userName == null) {
            return new Forward( "sessionTimeout" );
        }
        
        System.err.print( "[" + getRequest().getContextPath() + "] " );
        System.err.println( "Unhandled exception caught in OrganizationManagement Global.app:" );
        ex.printStackTrace();
        return new Forward( "errorPage" );
    }
    
}
