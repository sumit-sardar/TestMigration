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
import javax.servlet.http.Cookie;
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
	private static final String ERROR_USER_DISABLED = "user_disabled";
	// is
	// invalid
	private static final String ERROR_LTI_ERROR = "lti_error";// signature is

	private static final String USER_VALID_STATUS = "AC";

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

			if (errorURL == null || errorURL.isEmpty()) {
				errorURL = "/LTIError.jsp";
			}
			gotoErrorPage(request, response, errorURL, ERROR_UNKNOWN_CUSTOMER,
					"Consumer key cannot be blank");
			return;
		}
		// retrieve the URL from database if not sent as a parameter
		if (errorURL == null || errorURL.isEmpty()) {
			errorURL = validation.getErrorURL(customerID);
			if (errorURL == null || errorURL.isEmpty()) {
				errorURL = "/LTIError.jsp";// error page link was not provided
				// by the provider as well as in the
				// database.
			}
		}
		String userID = request.getParameter("user_id");
		if (userID == null || userID.isEmpty()) {
			System.out.println("User ID cannot be blank");
			gotoErrorPage(request, response, errorURL, ERROR_UNKNOWN_USER,
					"User ID cannot be blank");

			return;
		}

		String secretKey = validation.getSecretKey(customerID);
		if (secretKey == null || secretKey.isEmpty()) {
			System.out.println("Secret key not defined for customer "
					+ customerID);
			gotoErrorPage(request, response, errorURL, ERROR_LTI_ERROR,
					"Secret key not defined for " + customerID);

			return;
		}

		if (validation.validateRequest(request, secretKey)) {

			if (validation.validateCustomer(customerID)) {

				User user = validation.validateUser(customerID, userID);
				if (user == null) {
					System.out.println("User ID does not exist - user id:"
							+ userID + " customer id:" + customerID);

					gotoErrorPage(request, response, errorURL,
							ERROR_UNKNOWN_USER, "User ID does not exist");
					return;
				} else {// the user is valid
					if (user.getActivationStatus() != null
							&& user.getActivationStatus().equalsIgnoreCase(
									USER_VALID_STATUS)) {
						validation.updateUser(user.getUserId());
						System.out.println("LTI Authentication Successful."
								+ user.getUserId() + "," + user.getUserName()
								+ "," + user.getRole().getRoleName());
						HttpSession sess = request.getSession(true);
						
						try {
							ServletAuthentication.login(new LTICallbackHandler(
									user.getUserName(), user.getPassword()),
									request);
						} catch (LoginException e) {
							
							e.printStackTrace();
						}
						// **[IAA]: OAS-457 LAS 2014 - LAUSD - SSO - Hide LogOut
						// menu
						Cookie cookie = new Cookie("isSSO_LTIUser", "true");
						// cookie.setMaxAge(24*60*60);//24hrs
						cookie.setPath("/");
						// cookie.setSecure(true);
						response.addCookie(cookie);
						sess.setAttribute("isSSO_LTIUser", new Boolean(
								Boolean.TRUE));
						Cookie cookieErrorURL = new Cookie("LTI_ErrorURL",getErrorURL(errorURL));
						cookie.setPath("/");
						response.addCookie(cookieErrorURL);
						
						response.sendRedirect("sessionOperation/begin.do");
					} else {
						System.out.println("User is disabled");

						gotoErrorPage(request, response, errorURL,
								ERROR_USER_DISABLED, "User is disabled");
					}
					// response.setHeader("Location","SessionOperationController/gotoCurrentUI.do");

				}
			} else {
				System.out.println("Customer ID is invalid");

				gotoErrorPage(request, response, errorURL,
						ERROR_UNKNOWN_CUSTOMER, "Customer ID is invalid");
				return;
			}
		} else {
			System.out.println("Signature not valid");
			request.setAttribute("message", "Signature is invalid");
			gotoErrorPage(request, response, errorURL, ERROR_LTI_ERROR,
					"Signature is invalid");

		}
	}

	private void gotoErrorPage(HttpServletRequest request,
			HttpServletResponse response, String errorURL, String errorCode,
			String errorMsg) {
		try {
						
			errorURL = getErrorURL(errorURL);
			errorURL += "ERROR_CODE=" + errorCode;
			/*String redirectURL = errorURL;
			if ((errorURL == null) || errorURL.isEmpty()) {
				errorURL = "/LTIError.jsp?ERROR_CODE=" + errorCode;
			} else {
				if (errorURL.indexOf('?') >= 0) {
					errorURL = errorURL + "&ERROR_CODE=" + errorCode;
				} else {
					errorURL = errorURL + "?ERROR_CODE=" + errorCode;
				}
			}*/
			response.sendRedirect(errorURL);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	private String getErrorURL(String errorURL)
	{
		String adjustedErrorURL = errorURL;
		
		if(errorURL==null || errorURL.isEmpty())
		{
			errorURL = "/LTIError.jsp?";
		}
		else
		if(errorURL.indexOf('?')>=0)
		{
			errorURL +='&';
		}
		else
		{
			errorURL +='?';
		}
		
		return adjustedErrorURL;
	}
	private class LTICallbackHandler implements CallbackHandler {

		private String userName;
		private String password;

		public LTICallbackHandler(String username, String password) {
			this.userName = username;
			this.password = password;
		}

		@Override
		public void handle(Callback[] callbacks) throws IOException,
				UnsupportedCallbackException {
			if (callbacks != null) {
				for (int i = 0; i < callbacks.length; i++) {
					// System.out.println("call back
					// class..."+callbacks[i].getClass().getName());
					if (callbacks[i] instanceof NameCallback) {
						NameCallback nc = (NameCallback) callbacks[i];
						nc.setName(this.userName);
					} else if (callbacks[i] instanceof PasswordCallback) {
						PasswordCallback pc = (PasswordCallback) callbacks[i];
						pc.setPassword(this.password.toCharArray());
					}
				}
			}

		}

	}
}