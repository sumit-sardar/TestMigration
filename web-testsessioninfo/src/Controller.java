import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import java.io.IOException;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

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



}
