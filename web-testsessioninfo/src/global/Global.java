package global;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.PrivilegedExceptionAction;
import java.util.ResourceBundle;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import weblogic.jndi.Environment;
import weblogic.management.commo.Config;
import weblogic.management.commo.CommoMBeanServer;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.security.authentication.UserLockoutManagerMBean;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.service.UserLockoutManager;


/**
 * The Global page flow is used to define actions which can be invoked by any other
 * page flow in a webapp. The "jpf:catch" annotation provides a global way to catch
 * unhandled exceptions by forwarding to an error page.
 *
 * Giuseppe Gennaro
 * removed@jpf:catch type="PageFlowException" method="handlePageFlowException" 
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
    * @jpf:action
     * @jpf:forward name="success" path="/login.jsp" redirect="true"
    */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", 
                     path = "/login.jsp", 
                     redirect = true)
    })
    public Forward login()
    {
        return new Forward("success");
    }
     
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
     * @jpf:action 
     * @jpf:forward name="success" path="/logout.jsp" redirect="true"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", 
                     path = "/logout.jsp", 
                     redirect = true)
    })
    public Forward logout()
    {
        try
        {
            super.logout(false);
            this.getSession().invalidate();
        } 
        catch (Exception e)
        {
            System.err.print(e.getStackTrace());
        }        
        return new Forward("success");
    }

    /**
     * @jpf:action 
     * @jpf:forward name="success" path="logout.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "logout.do")
    })
    public Forward sessionTimeout()
    {
        ResourceBundle rb = ResourceBundle.getBundle("errorMessages");
        String message = rb.getString("sessionTimeout");
        this.getRequest().setAttribute("message", message);
        return new Forward("success");
    }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="errorPage" path="/error.jsp"
     * @jpf:forward name="timeout" path="sessionTimeout.do"
     */
    @Jpf.ExceptionHandler(forwards = { 
        @Jpf.Forward(name = "errorPage",
                     path = "/error.jsp"), 
        @Jpf.Forward(name = "timeout",
                     path = "sessionTimeout.do")
    })
    protected Forward handleException(Exception ex, String actionName, String message, FormData form)
    {
        String userName = (String)getSession().getAttribute("userName");
        if (userName == null)
        {
            return new Forward("timeout");
        }
        
        System.err.print("[" +
                         getRequest().getContextPath() +
                         "] ");
        System.err.println("Unhandled exception caught in TestSessionInfo Global.app:");
        ex.printStackTrace();
        return new Forward("errorPage");
    }
    
    private static ResourceBundle security = null;
    
    /**
     * @jpf:action
     * @jpf:forward name="error" path="/login_error.jsp"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "error", path = "/login_error.jsp")
		}
	)
    public Forward loginError()
    {
        try {
            String username = this.getRequest().getParameter("j_username");
            boolean lockedOut = false;
            if(!"".equals(username.trim())) {            
            	lockedOut = isLockedOut(username);
            }
        
            if(lockedOut) {
                ResourceBundle rb = ResourceBundle.getBundle("errorMessages");
                String message = rb.getString("userLockout");
                this.getRequest().setAttribute("errorMsg", message);
            }
        } catch (UndeclaredThrowableException e) {
            e.getUndeclaredThrowable().printStackTrace();
        }  catch (Exception e) {
            e.printStackTrace();
        }          
        return new Forward( "error" );
    }   
        
    /**
     * Retrieve a reference to a <code>ServerSecurityRuntimeMBean</code> instance.
     * @post return value is non-null.
     * @return a <code>ServerSecurityRuntimeMBean</code> instance.
     * @exception ServletException if the lookup of the bean instance fails.
     */
    private boolean isLockedOut(final String username)
        throws ServletException {

        boolean lockout = false;
        try {
            if(security == null) security = ResourceBundle.getBundle("security");
            String adminUrl = security.getString("adminURL");
            String adminUser = security.getString("adminUser");
            String adminPass = security.getString("adminPass");
             
            // retrieve a reference to MBeanHome
            Environment env = new Environment();
            env.setSecurityPrincipal(adminUser);
            env.setSecurityCredentials(adminPass);
            env.setProviderUrl(adminUrl);
            Context jndiContext = env.getInitialContext();

            // retrieve user lockout manager
            CommoMBeanServer svr = Config.getMBeanServer();
            System.out.println("server name: " + svr.getServerName());
            UserLockoutManagerMBean mgr = (UserLockoutManagerMBean)svr.getMBean(new ObjectName("Security:Name=myrealmUserLockoutManager"));
            System.out.println("mgr: " + mgr);
            lockout = mgr.isLockedOut(username);

            /*
            System.out.println("LoginAttemptsWhileLockedTotalCount: " + mgr.getLoginAttemptsWhileLockedTotalCount());
            System.out.println("    InvalidLoginAttemptsTotalCount: " + mgr.getInvalidLoginAttemptsTotalCount());
            System.out.println("        InvalidLoginUsersHighCount: " + mgr.getInvalidLoginUsersHighCount());
            System.out.println("           LockedUsersCurrentCount: " + mgr.getLockedUsersCurrentCount());
            System.out.println("           UnlockedUsersTotalCount: " + mgr.getUnlockedUsersTotalCount());
            System.out.println("           UserLockedoutTotalCount: " + mgr.getUserLockoutTotalCount());
            System.out.println("                 LoginFailureCount: " + mgr.getLoginFailureCount(username));
            System.out.println("                  LastLoginFailure: " + mgr.getLastLoginFailure(username));
            System.out.println("                       isLockedOut: " + mgr.isLockedOut(username));
            */
            
        } catch(Exception e) {
            e.printStackTrace();
            throw (ServletException)
                new ServletException().initCause(e);
        }

        return lockout;
    }
    
    
}
