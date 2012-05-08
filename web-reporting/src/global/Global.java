package global;

import org.apache.beehive.netui.pageflow.*;
import java.io.IOException;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * The Global page flow is used to define actions which can be invoked by any other
 * page flow in a webapp. The "jpf:catch" annotation provides a global way to catch
 * unhandled exceptions by forwarding to an error page.
 *
 * removed@jpf:catch type="PageFlowException" method="handlePageFlowException"
 *
 * @jpf:catch type="Exception" method="handleException"
 */
@SuppressWarnings("deprecation")
@Jpf.Controller(catches = { 
    @Jpf.Catch(type = Exception.class,
               method = "handleException")
})
public class Global extends GlobalApp
{ 
    static final long serialVersionUID = 1L;    

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
     * Global action to peform the logout activity.  Includes session invalidation
     * and redirection to the logout conversation/command.
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
                                       String message, Object form )
    {
        String userName = (String)getSession().getAttribute("userName");
        if (userName == null) {
            return new Forward( "sessionTimeout" );
        }
        
        System.err.print( "[" + getRequest().getContextPath() + "] " );
        System.err.println( "Unhandled exception caught in Immediate Reporting Global.app:" );
        ex.printStackTrace();
        return new Forward( "errorPage" );
    }

    /*
     * Handler for native page flow exceptions (e.g., ActionNotFoundException,
     * which is thrown when an unknown page flow action is requested). This handler
     * allows PageFlowExceptions to write informative error pages to the response.
     * To use the standard exception-handler for these exceptions, simply remove
     * this method and the "jpf:catch" annotation for PageFlowException.
     *
     * @jpf:exception-handler
     * 
     *
    
    public Forward handlePageFlowException( PageFlowException ex, String actionName,
                                            String message, FormData form ) 
        throws java.io.IOException
    { 
        System.err.println("TestAdministration Name="+actionName+" message="+message);
        ex.printStackTrace();
        ex.sendError( getRequest(), getResponse() ); 
        return null; 
    } 
 */ 
}
