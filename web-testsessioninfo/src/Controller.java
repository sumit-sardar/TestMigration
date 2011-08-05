import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.netui.pageflow.annotations.Jpf;

import com.ctb.bean.testAdmin.User;
import com.ctb.testSessionInfo.utils.JsonUtils;

/**
 * This is the default controller for a blank web application.
 *
 * @jpf:controller
 *  */
@Jpf.Controller()
public class Controller extends PageFlowController
{
    static final long serialVersionUID = 1L;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp")
		}
	)
    protected Forward begin()
    {
        return new Forward("index");
    }

    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp")
		}
	)
    protected Forward authUser()
    {
    	HttpServletResponse resp = this.getResponse();
    	HttpServletRequest req = this.getRequest();
    	
    	resp.setContentType("application/x-javascript");  
    	//resp.setContentType("text/x-json");  
    	
        String callback = req.getParameter("callback");
		String username = req.getParameter("username");
	    String password = req.getParameter("password");

        PrintWriter out = null;
		try {
			out = resp.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		try {

			User user = this.testSessionStatus.getUserDetails(username, username);
			
			if (user == null) {
		        out.println("{\"Error\":\"Invalid Username\"}");
		        out.flush();			
		        return null;
			}
			
			String userPassword = user.getPassword();
			System.out.println(userPassword);
			String encodePassword = JsonUtils.encodePassword(password);
			System.out.println(encodePassword);

			if (! userPassword.equals(encodePassword)) {
		        out.println("{\"Error\":\"Invalid Password\"}");
		        out.flush();			
		        return null;
			}
			
			String reportParam = this.testSessionStatus.getReportParams(username);
			StringTokenizer st = new StringTokenizer(reportParam, "|"); 
			String sys = st.nextToken(); 
			String parms = st.nextToken(); 

	        if (callback != null) {
	            out.println(callback + "(");
	        }
			
	        out.println("{" +
	                "\"sys\": \"" + sys + "\", " +
	                "\"parms\": \"" + parms + "\"}");

	        if (callback != null) {
	          out.println(")");
	        }
	        out.flush();
	        
	        
		} catch (Exception e) {
			e.printStackTrace();
	        out.println("{ERROR}");
	        out.flush();			
		}
		
		return null;
    }


}
