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

	// error codes
	private static final String ERROR_UNKNOWN_CUSTOMER = "unknown_customer";
	private static final String ERROR_UNKNOWN_USER = "unknown_user";// user id
																	// is
																	// invalid
	private static final String ERROR_LTI_ERROR = "lti_error";// signature is
																// invalid
	private static final String ERROR_USER_DISABLED = "";

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

		LTIValidation validation = new LTIValidation();
		String errorURL = request.getParameter("lti_return_url");
		
		String customerID = request.getParameter("oauth_consumer_key");
		if (customerID == null || customerID.isEmpty()) {
			System.out.println("Customer ID cannot blank");
			
			if(errorURL == null || errorURL.isEmpty())
			{
					errorURL = "/LTIError.jsp";	
			}
			gotoErrorPage(request,response,errorURL,ERROR_UNKNOWN_CUSTOMER,"Consumer key cannot be blank");
			return;
		}
		//retrieve the URL from database if not sent as a parameter
		if(errorURL == null || errorURL.isEmpty())
		{
			errorURL = validation.getErrorURL(customerID);
			if(errorURL==null || errorURL.isEmpty()){
				errorURL = "/LTIError.jsp";// error page link was not provided
											// by the provider as well as in the
											// database.
			}
		}
		String userID = request.getParameter("user_id");
		if (userID == null || userID.isEmpty()) {
			System.out.println("User ID cannot be blank");
			gotoErrorPage(request,response,errorURL,ERROR_UNKNOWN_USER,"User ID cannot be blank");
			
			return;
		}
		
		String secretKey = validation.getSecretKey(customerID);
		if(secretKey == null || secretKey.isEmpty())
		{
			System.out.println("Secret key not defined for customer "+customerID);
			gotoErrorPage(request,response,errorURL,ERROR_LTI_ERROR,"Secret key not defined for "+customerID);
			
			return;
		}
		
		if (validation.validateRequest(request,secretKey )) {
			
			if (validation.validateCustomer(customerID)) {
				
				User user = validation.validateUser(customerID, userID);
				if (user == null) {
					System.out.println("User ID does not exist - user id:" + userID
							+ " customer id:" + customerID);
					request.setAttribute("message","User ID does not exist");
					
					gotoErrorPage(request,response,errorURL,ERROR_UNKNOWN_USER,"User ID does not exist");
					return;
				}
				else
				{// the user is valid
					validation.updateLogInTime(user.getUserId());
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
					//**[IAA]: OAS-457 LAS 2014 - LAUSD - SSO - Hide LogOut menu
					sess.setAttribute("isSSO_LTIUser",new Boolean(Boolean.TRUE));
					response.sendRedirect("sessionOperation/begin.do");
					// response.setHeader("Location","SessionOperationController/gotoCurrentUI.do");
					
				}
			}
			else
			{
				System.out.println("Customer ID is invalid");
				
				gotoErrorPage(request,response,errorURL,ERROR_UNKNOWN_CUSTOMER,"Customer ID is invalid");
				return;
			}
		} else {
			System.out.println("Signature not valid");
			request.setAttribute("message","Signature is invalid");
			gotoErrorPage(request,response,errorURL,ERROR_LTI_ERROR,"Signature is invalid");
			
		}
	}

	private void gotoErrorPage(HttpServletRequest request,
			HttpServletResponse response, String errorURL, String errorCode,
			String errorMsg) {
		try {
			request.setAttribute("message",errorCode+":"+ errorMsg);
			RequestDispatcher rd = getServletContext().getRequestDispatcher(errorURL);
			if (rd == null) {
				System.out.println("************Page URL is wrong");
			} else {
				rd.forward(request, response);
			}
		} catch (ServletException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private class LTICallbackHandler implements CallbackHandler {

		@Override
		public void handle(Callback[] callbacks) throws IOException,
				UnsupportedCallbackException {
			if (callbacks != null) {
				for (int i = 0; i < callbacks.length; i++) {
					// System.out.println("call back
					// class..."+callbacks[i].getClass().getName());
					if (callbacks[i] instanceof NameCallback) {
						NameCallback nc = (NameCallback) callbacks[i];
						nc.setName("tai_tasc");
					} else if (callbacks[i] instanceof PasswordCallback) {
						PasswordCallback pc = (PasswordCallback) callbacks[i];
						pc.setPassword("welcome1".toCharArray());
					}
				}
			}

		}

	}
}