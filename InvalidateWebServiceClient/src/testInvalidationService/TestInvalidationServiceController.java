package testInvalidationService;

import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import com.ctb.dto.SecureUser;
import com.ctb.dto.StudentValidationDetails;

@Jpf.Controller()
public class TestInvalidationServiceController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	
	@Control()
	InvalidateWSServiceControl invalidateWSServiceControl;

	/**
	 * Callback that is invoked when this controller instance is created.
	 */
	@Override
	protected void onCreate() {
	}

	/**
	 * Callback that is invoked when this controller instance is destroyed.
	 */
	@Override
	protected void onDestroy(HttpSession session) {
	}
	
	@Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "callInvalidation.jsp") 
        }) 
    protected Forward begin()
    {
        return new Forward("success");
    }
	
	@Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "callInvalidation.jsp") 
        }) 
    protected Forward callInvalidate()
    {
		SecureUser secureUser = new SecureUser();
		secureUser.setUserName("sarmistha_abe");
		secureUser.setPassword("12345");
		StudentValidationDetails detail = new StudentValidationDetails();
		detail.setStudentId(1644566);
		detail.setSessionId(234672);
		String[] array = {"Mathematics Computation Sample Question", "Reading Sample Questions"};
		detail.setSubtest(array);
		String status = invalidateWSServiceControl.validateClass(detail, secureUser);
		System.out.println("client - > " + status);
        return new Forward("success");
    }
    
}