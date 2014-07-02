package com.ctb.util.web.filter;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class LTIRequestWrapper extends HttpServletRequestWrapper {

	private static final String LTI_TRUE = "true";
	private static final String LTI_AUTH = "LTI_Auth";
	private static final String LTI_USER_ID = "LTI_UserID";
	private static final String LTI_USER_NAME = "LTI_UserName";
	private static final String LTI_ROLE_NAME = "LTI_RoleName";

	String userName;
	Integer userId;
	String roleName;
	HttpServletRequest actualRequest;

	public LTIRequestWrapper(HttpServletRequest request) {
		super(request);

		actualRequest = request;
		HttpSession sess = actualRequest.getSession(false);

		if (sess != null) {
			userId = (Integer) sess.getAttribute(LTI_USER_ID);
			userName = (String) sess.getAttribute(LTI_USER_NAME);
			roleName = (String) sess.getAttribute(LTI_ROLE_NAME);

		}

	}

	@Override
	public boolean isUserInRole(String role) {
		System.out.println("get user role...."+roleName);
		if (roleName == null) {
			return this.actualRequest.isUserInRole(role);
		}
		return roleName.equals(role);
	}

	@Override
	public Principal getUserPrincipal() {
		
		
		if (this.userName == null) {
			return actualRequest.getUserPrincipal();
		}

		// make an anonymous implementation to just return our user
		return new Principal() {
			@Override
			public String getName() {
				System.out.println("get user principal...."+userName);
				return userName;
			}
			@Override
			public String toString()
			{
				return userName;
			}
		};
	}

}
