package global;

import org.apache.beehive.netui.pageflow.*;
import com.ctb.bean.content.DeliverableUnitBean;
import com.ctb.bean.content.SchedulableUnitBean;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * The Global page flow is used to define actions which can be invoked by any other
 * page flow in a webapp. The "jpf:catch" annotation provides a global way to catch
 * unhandled exceptions by forwarding to an error page.
 *
 * Giuseppe Gennaro
 * Removed jpf:catch type="PageFlowException" method="handlePageFlowException" due
 * to cross-site scripting exploit.
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
    public String userLogined = "false";
    public String studentFirstName = "John";
    public String studentLastName = "Doe";
    public String calculator = "0";
  //  public String magnifier = "0";
    public String screenReader = "0";
	public String speedAdjustment = "0";
    public String rest_break = "0";
    public String untimed = "0";
    public String highlighter = "true";
    public String answerFgColor ;
    public String answerBgColor ;
    public String answerFontSize;
    public String questionFgColor;
    public String questionBgColor;
    public String questionFontSize;
    
    //Changes for new accommodations added
    public String maskingRuler  = "0";
    public String magnifyingGlass  = "0";
    public String auditoryCalming  = "0";
    public String extendedTime  = "0";
    
    public String userFolder;
    
    public String eliminatorResource = "/ContentReviewWeb/resources/eliminator.swf";
        
    public DeliverableUnitBean currentDeliverableUnitBean;
    public SchedulableUnitBean[] SchedulableUnits;
     
    /**
    * Sample global action that will return to the default Controller.jpf in
    * the webapp root.
    * @jpf:action
    * @jpf:forward name="home" path="/ContentReviewPageFlow/ContentReviewPageFlowController.jpf"
    */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "home",
                     path = "/ContentReviewPageFlow/ContentReviewPageFlowController.jpf")
    })
    public Forward home()
    {
        return new Forward("home");
    }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="errorPage" path="/error.jsp"
     */
    @Jpf.ExceptionHandler(forwards = { 
        @Jpf.Forward(name = "errorPage", path = "/error.jsp")
		}
	)
    protected Forward handleException( Exception ex, String actionName,
                                       String message, FormData form )
    {
        System.err.print( "[" + getRequest().getContextPath() + "] " );
        System.err.println( "Unhandled exception caught in Global.app:" );
        ex.printStackTrace();
        return new Forward( "errorPage" );
    }

    /** 
     * Handler for native page flow exceptions (e.g., ActionNotFoundException,
     * which is thrown when an unknown page flow action is requested). This handler
     * allows PageFlowExceptions to write informative error pages to the response.
     * To use the standard exception-handler for these exceptions, simply remove
     * this method and the "jpf:catch" annotation for PageFlowException.
     *
     * jpf:exception-handler
     * 
     * Giuseppe Gennaro: 
     * Removed due to cross-site scripting exploit with generic page.  
     * 
    public Forward handlePageFlowException( PageFlowException ex, String actionName,
                                            String message, FormData form ) 
        throws java.io.IOException
    { 
        ex.sendError( getRequest(), getResponse() ); 
        return null; 
    } 
    */
}
