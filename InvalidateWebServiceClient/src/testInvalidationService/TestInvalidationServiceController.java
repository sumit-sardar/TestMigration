package testInvalidationService;

import java.util.ArrayList;
import java.util.List;

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
		secureUser.setUserName("tai_ws");
		secureUser.setPassword("12345");
		Long student_id = Long.parseLong(getRequest().getParameter("studentId"));
		Long session_id = Long.parseLong(getRequest().getParameter("sessionId"));
		StudentValidationDetails detail = new StudentValidationDetails();
		detail.setStudentId(student_id);
		detail.setSessionId(session_id);
		List<String> subtestList = new ArrayList<String>();
		String subtest1 = getRequest().getParameter("subtestName1");
		if(subtest1 != null && !"".equals(subtest1) && subtest1.length() > 0) {
			subtestList.add(subtest1);
		}
		String subtest2 = getRequest().getParameter("subtestName2");
		if(subtest2 != null && !"".equals(subtest2) && subtest2.length() > 0) {
			subtestList.add(subtest2);
		}
		String subtest3 = getRequest().getParameter("subtestName3");
		if(subtest3 != null && !"".equals(subtest3) && subtest3.length() > 0) {
			subtestList.add(subtest3);
		}
		String subtest4 = getRequest().getParameter("subtestName4");
		if(subtest4 != null && !"".equals(subtest4) && subtest4.length() > 0) {
			subtestList.add(subtest4);
		}
		String subtest5 = getRequest().getParameter("subtestName5");
		if(subtest5 != null && !"".equals(subtest5) && subtest5.length() > 0) {
			subtestList.add(subtest5);
		}
		String subtest6 = getRequest().getParameter("subtestName6");
		if(subtest6 != null && !"".equals(subtest6) && subtest6.length() > 0) {
			subtestList.add(subtest6);
		}
		String subtest7 = getRequest().getParameter("subtestName7");
		if(subtest7 != null && !"".equals(subtest7) && subtest7.length() > 0) {
			subtestList.add(subtest7);
		}
		String[] array = (String []) subtestList.toArray(new String[0]);
		detail.setSubtest(array);
		String status = invalidateWSServiceControl.validateClass(detail, secureUser);
		System.out.println("client - > " + status);
        return new Forward("success");
    }
    
}