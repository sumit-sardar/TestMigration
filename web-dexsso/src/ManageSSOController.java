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
    		Cookie cookie = new Cookie( "TAS_SESSIONID", "00000000000000000000" );
        	cookie.setMaxAge( -42000 );
        	this.getResponse().addCookie(cookie);
        	String tokenValue = this.getRequest().getParameter("oamSessionID");
        	
        	tokenValue = URLDecoder.decode(tokenValue,"UTF-8");
        	
        	tokenValue=tokenValue.replaceAll("\\r\\n", "");
            
            tokenValue=URLEncoder.encode(tokenValue,"UTF-8");
            
            //Base 64
        	BASE64Encoder base64 =  new BASE64Encoder();
            tokenValue = base64.encode(tokenValue.getBytes());
           
            tokenValue=tokenValue.replaceAll("\\r\\n", "");
          
            Cookie encodeCookie = new Cookie( "DExPerimeterAtnToken", tokenValue);
        	encodeCookie.setMaxAge( -42000 );
        	this.getResponse().addCookie(encodeCookie);
            this.getResponse().setHeader("Set-Cookie", "TAS_SESSIONID=00000000000000000000;path=" + this.getRequest().getContextPath() + "lm_defect" );
            
    	
    	} catch (Exception e) {
    		
    		e.printStackTrace();
    		
    	}
        
         
        

    	
    	return new Forward("success");
    }
    
}
