package global;

import org.apache.beehive.netui.pageflow.*;
import java.io.IOException;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * The Global page flow is used to define actions which can be invoked by any other
 * page flow in a webapp. The "jpf:catch" annotation provides a global way to catch
 * unhandled exceptions by forwarding to an error page.
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

    /**
     * Global action to go to the homepage.
    * @jpf:action
    */
    @Jpf.Action()
    public Forward homepage()
    {
        try
        {
            getResponse().sendRedirect("/TestSessionInfoWeb/homepage/HomePageController.jpf");
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
            getResponse().sendRedirect("/TestSessionInfoWeb/logout.do");
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
            getResponse().sendRedirect("/TestSessionInfoWeb/sessionTimeout.do");
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
        System.err.println( "Unhandled exception caught in StudentRegistration Global.app:" );
        ex.printStackTrace();
        return new Forward( "errorPage" );
    }

}
