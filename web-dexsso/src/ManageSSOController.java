import java.net.URLDecoder;

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
    	System.out.println("ManageSSO 32");
    	Forward result = globalApp.logout();
    	try {
    		System.out.println("ManageSSO 35");
    		Cookie cookie = new Cookie( "TAS_SESSIONID", "00000000000000000000" );
        	cookie.setMaxAge( -42000 );
        	this.getResponse().addCookie(cookie);
        	String tokenValue = this.getRequest().getParameter("oamSessionID");
        	System.out.println("Token - " + tokenValue);
        	tokenValue = URLDecoder.decode(tokenValue,"UTF-8");
        	System.out.println("Token after URL Decode - " + tokenValue);
            //Base 64
        	BASE64Encoder base64 =  new BASE64Encoder();
            tokenValue = base64.encode(tokenValue.getBytes());
            System.out.println("Token after Base 64 encode - " + tokenValue);
            tokenValue=tokenValue.replaceAll("\\r\\n", "");
            System.out.println("Token after replacing CRLF - " + tokenValue);
            Cookie encodeCookie = new Cookie( "DExPerimeterAtnToken", tokenValue);
        	encodeCookie.setMaxAge( -42000 );
        	this.getResponse().addCookie(encodeCookie);
            this.getResponse().setHeader("Set-Cookie", "TAS_SESSIONID=00000000000000000000;path=" + this.getRequest().getContextPath() + "lm_defect" );
            
    	
    	} catch (Exception e) {
    		
    		e.printStackTrace();
    		System.out.println("ManageSSO 53");
    	}
        
         
        

    	System.out.println("ManageSSO 59");
    	return new Forward("success");
    }
    
}
