package sessionOperation;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import weblogic.servlet.security.ServletAuthentication;

import com.ctb.bean.testAdmin.User;

/**
 * Servlet implementation class for Servlet: LTIAuthentication
 * 
 */
public class LTIAuthentication extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	private static final String LTI_TRUE = "true";
	private static final String LTI_AUTH = "LTI_Auth";
	private static final String LTI_USER_ID = "LTI_UserID";
	private static final String LTI_USER_NAME = "LTI_UserName";
	private static final String LTI_ROLE_NAME = "LTI_RoleName";
	
	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public LTIAuthentication() {
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String customerID = request.getParameter("oauth_consumer_key");
		if (customerID == null || customerID.isEmpty()) {
			System.out.println("Customer ID cannot blank");
			request.setAttribute("message","Consumer key cannot be blank");
			gotoErrorPage(request,response);
			return;
		}
		String userID = request.getParameter("user_id");
		if (userID == null || userID.isEmpty()) {
			System.out.println("User ID cannot be blank");
			request.setAttribute("message","Consumer key cannot be blank");
			gotoErrorPage(request,response);
			
			return;
		}
		LTIValidation validation = new LTIValidation();
		if (validation.validateRequest(request, "sample")) {
			
			if (validation.validateCustomer(customerID)) {
				
				User user = validation.validateUser(customerID, userID);
				if (user == null) {
					System.out.println("User ID does not exist - user id:" + userID
							+ " customer id:" + customerID);
					request.setAttribute("message","User ID does not exist");
					gotoErrorPage(request,response);
					return;
				}
				else
				{//the user is valid
					System.out.println("LTI Authentication Successful."+user.getUserId()+","+user.getUserName()+
							","+user.getRole().getRoleName());
					HttpSession sess = request.getSession(true);
					sess.setAttribute(LTI_AUTH, LTI_TRUE);
					sess.setAttribute(LTI_USER_ID, user.getUserId());
					sess.setAttribute(LTI_USER_NAME, user.getUserName());
					sess.setAttribute(LTI_ROLE_NAME,user.getRole().getRoleName());
					sess.setAttribute("customerId","");
					sess.setAttribute("userName", user.getUserName());
					try {
						ServletAuthentication.login(new LTICallbackHandler(), request);
					} catch (LoginException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					response.sendRedirect("sessionOperation/begin.do");
					//response.setHeader("Location","SessionOperationController/gotoCurrentUI.do");
					
				}
			}
			else
			{
				System.out.println("Customer ID is invalid");
				//response.getWriter().write("Customer ID cannot blank");
				request.setAttribute("message","Consumer key is not valid");
				gotoErrorPage(request,response);
				return;
			}
		} else {
			System.out.println("Signature not valid");
			request.setAttribute("message","Signature is invalid");
			gotoErrorPage(request,response);
			
		}
	}
	private void gotoErrorPage(HttpServletRequest request, HttpServletResponse response)
	{
		try {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/LTIError.jsp");
			if(rd==null)
			{
				System.out.println("************Page URL is wrong");
			}
			else
			{
				rd.forward(request,response);
			}
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private class LTICallbackHandler implements CallbackHandler
	{

		@Override
		public void handle(Callback[] callbacks) throws IOException,
				UnsupportedCallbackException {
			if(callbacks!=null){
				for(int i=0;i<callbacks.length;i++)
				{
					System.out.println("call back class..."+callbacks[i].getClass().getName());
					if(callbacks[i] instanceof NameCallback)
					{
						NameCallback nc = (NameCallback)callbacks[i];
						nc.setName("tai_tasc");
					}
					else
					if(callbacks[i] instanceof PasswordCallback)
					{
						PasswordCallback pc = (PasswordCallback)callbacks[i];
						pc.setPassword("welcome1".toCharArray());
					}
				}
			}
			
		}
		
	}
}