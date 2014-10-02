package sessionOperation;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: LTICookieTest
 * 
 * Test whether the cookies set  by LTIAuthentication is set in the browser
 * Some browser settings like block third party cookies, blocks the LTI cookies
 *
 */
 public class LTICookieTest extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   private static final String ERROR_URL_PARAMNAME = "errorURL";
   private static final String ERROR_CODE = "cookie_disabled";
   private static final String ERROR_URL_PARAM = "lti_errormsg";//Added on 10/01/2014 for OAS-821
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public LTICookieTest() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean success = false;
		
		Cookie[] allCookies = request.getCookies();
		if(allCookies!=null)
		{
			for(Cookie c:allCookies)
			{
				//verify LTI cookie
				if(c!=null && c.getName()!=null && c.getName().equals("isSSO_LTIUser"))
				{
					//the browser has accepted LTI cookie
					success = true;
					break;
				}
			}
		}
		if(success)
		{
			response.sendRedirect("sessionOperation/begin.do");
		}
		else
		{
			String errorURL = request.getParameter(ERROR_URL_PARAMNAME);
			if(errorURL == null || errorURL.isEmpty())
			{
				errorURL = "/LTIError.jsp?ERROR_CODE="+ERROR_CODE;
			}
			else
			{
				//errorURL +="ERROR_CODE="+ERROR_CODE;
				errorURL += ERROR_URL_PARAM + "=" + ERROR_CODE; //Modified on 10/01/2014 for OAS-821
			}
			response.sendRedirect(errorURL);
		}
		
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}   	  	    
}