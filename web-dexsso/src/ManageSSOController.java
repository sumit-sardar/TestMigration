import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import sun.misc.BASE64Encoder;
/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class ManageSSOController extends PageFlowController
{
 
    protected global.Global globalApp;

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/manageSSO/InternalSSOController.jpf" redirect="true"
    */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", 
                     path = "/manageSSO/InternalSSOController.jpf", 
                     redirect = true
			)
		}
	)
    protected Forward begin()
    {
    	Forward result = globalApp.logout();
    	try {
    		 Cookie cookie = new Cookie("TAS_SESSIONID", "00000000000000000000");
             cookie.setMaxAge(-42000);
             getResponse().addCookie(cookie);
             getResponse().setHeader("Set-Cookie", (new StringBuilder()).append("TAS_SESSIONID=00000000000000000000;path=").append(getRequest().getContextPath()).append("lm_defect").toString());

    	} catch (Exception e) {
    		
    		e.printStackTrace();
    		
    	}
        
         
        

    	
    	return new Forward("success");
    }
    
}
