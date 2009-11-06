package manageSSO;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class InternalSSOController extends PageFlowController
{

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/redir.jsp"
    */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/redir.jsp")
		}
	)
    protected Forward begin()
    {
        return new Forward("success");
    }
    
}
