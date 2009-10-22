package messaging;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class MessagingController extends PageFlowController
{
    static final long serialVersionUID = 1L;


    // Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="viewPageMessaging.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "viewPageMessaging.do")
    })
    protected Forward begin()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="pageMessaging.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "pageMessaging.jsp")
    })
    protected Forward viewPageMessaging()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="tableMessaging.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "tableMessaging.jsp")
		}
	)
    protected Forward viewTableMessaging()
    {
        return new Forward("success");
    }
}
