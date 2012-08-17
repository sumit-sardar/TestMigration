package findPassword;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.testSessionInfo.utils.WebUtils;
import com.ctb.util.userManagement.CTBConstants;
import com.ctb.util.userManagement.FormatUtils;
import java.io.IOException;
import javax.servlet.http.Cookie;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class FindPasswordController extends PageFlowController
{
    static final long serialVersionUID = 1L;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
    
    private String username = null;
    private User user = null;
    private String hintAnswer = null;
    private String email = null;


    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHintAnswer() {
		return hintAnswer;
	}

	public void setHintAnswer(String hintAnswer) {
		this.hintAnswer = hintAnswer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
     * @jpf:action
     * @jpf:forward name="success" path="findPassword.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "findPassword.do")
    })
    protected Forward begin()
    {
        try
        {
            String url = "https://172.16.80.132/bredexsoap/services/ScoringService?wsdl";
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
     * @jpf:forward name="success" path="find_password.jsp"
     * @jpf:forward name="error" path="/login_error.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "find_password.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "/login_error.jsp")
    })
    protected Forward findPassword()
    {
        java.util.ResourceBundle rb = java.util.PropertyResourceBundle.getBundle("errorMessages");
        String errorMsg = null;
        
        if (! getUserDetails())
        {
            errorMsg = rb.getString("findPassword.invalidUsername");            
            this.getRequest().setAttribute("errorMsg", errorMsg);
            this.getRequest().setAttribute("username", this.username);
            return new Forward("error");
        }
        
        if ((this.user.getEmail() == null) || (this.user.getEmail().length() == 0))
        {
            errorMsg = rb.getString("findPassword.noEmail");            
            this.getRequest().setAttribute("errorMsg", errorMsg);
            this.getRequest().setAttribute("username", this.username);
            return new Forward("error");            
        }

        if ((this.user.getPasswordHintAnswer() == null) || (this.user.getPasswordHintAnswer().length() == 0))
        {
            errorMsg = rb.getString("findPassword.notLoginBefore");            
            this.getRequest().setAttribute("errorMsg", errorMsg);
            this.getRequest().setAttribute("username", this.username);
            return new Forward("error");            
        }

        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/logout2.jsp"
     * @jpf:forward name="error" path="find_password.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/logout2.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "find_password.jsp")
    })
    protected Forward getPassword()
    {
        if (! validateUserInput(this.hintAnswer, this.email))
        {
            return new Forward("error");
        }
        
        if (! resetPassword())
        {
            return new Forward("error");
        }

        java.util.ResourceBundle rb = java.util.PropertyResourceBundle.getBundle("errorMessages");
        String errorMsg = rb.getString("findPassword.emailSent");            
        this.getRequest().setAttribute("message", errorMsg);
        
        return new Forward("success");
    }

    /**
    * @jpf:action
     * @jpf:forward name="success" path="/login.jsp"
    */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "/login.jsp")
		}
	)
    public Forward backToLogin()
    {
        Cookie[] cookies = (Cookie[])this.getRequest().getCookies();
        if (cookies != null && cookies.length > 0 ) {    
            for (int i=0 ; i<cookies.length ; i++) {
                Cookie c = cookies[i];
                if (c != null) {
                    if (c.getName().equals("TAS_SESSIONID") || c.getName().equals("_wl_authcookie_")) {                                    
                        Cookie r = new Cookie(c.getName(), null);
                        r.setMaxAge(0);
                        r.setPath("/");
                        this.getResponse().addCookie(r);
                        cookies[i] = null;
                    }
                }
            }
        }        
        
        this.getRequest().setAttribute("username", this.username);
        return new Forward("success");
    }
    
    /* 
     * getUserDetails
    */
    private boolean getUserDetails()
    {
        boolean validUser = false;        
        this.username = this.getRequest().getParameter("username");
        
        if ((this.username != null) && (this.username.length() > 0)) {
            try {
                this.user = this.testSessionStatus.getUserDetails(this.username, this.username);
                if (this.user != null) 
                    validUser = true;
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }        
        return validUser;
    }

    /* 
     * validateUserInput
    */
    private boolean validateUserInput(String hintAnswer, String email)
    {
        java.util.ResourceBundle rb = java.util.PropertyResourceBundle.getBundle("errorMessages");
        String errorMsg = null;            
        
        if ((hintAnswer == null) || (! this.user.getPasswordHintAnswer().equals(hintAnswer))) {
            errorMsg = rb.getString("findPassword.invalidHintAnswer");            
            this.getRequest().setAttribute("errorMsg", errorMsg);
            return false;
        }

        if ((email == null) || (email.length() == 0) || (! WebUtils.validEmail(email))) {
            errorMsg = rb.getString("findPassword.invalidEmail");            
            this.getRequest().setAttribute("errorMsg", errorMsg);
            return false;
        }  
        
        String userEmail = this.user.getEmail();
        if ((userEmail == null) || (userEmail.length() == 0) || (! userEmail.equals(email))) {
            errorMsg = rb.getString("findPassword.emailNotMatch");            
            this.getRequest().setAttribute("errorMsg", errorMsg);
            return false;
        }
          
        return true;
    }

    /* 
     * resetPassword
    */
    private boolean resetPassword()
    {
        boolean ret = true;
        try {            
            this.userManagement.resetPassword(this.username, this.user);
        } 
        catch (Exception e) {
            e.printStackTrace();
            ret = false;            
        }
        return ret;
    }
            
}
