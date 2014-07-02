package com.ctb.util.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LTIAuthFilter implements Filter {

	private static final String LTI_TRUE = "true";
	private static final String LTI_AUTH = "LTI_Auth";
	private static final String LTI_USER_ID = "LTI_UserID";
	private static final String LTI_USER_NAME = "LTI_UserName";
	private static final String LTI_ROLE_NAME = "LTI_RoleName";
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		boolean ltiUser = false;
		HttpServletRequest httpReq = (HttpServletRequest)request;
		HttpSession sess = httpReq.getSession();
		if(sess!=null)
		{
			String ltiAuth = (String)sess.getAttribute(LTI_AUTH);
			if(ltiAuth!=null && ltiAuth.equals(LTI_TRUE))
			{
				//This user has been authenticated by LTI
				ltiUser = true;
			}
		}
		
		
		if(ltiUser)
		{
			LTIRequestWrapper ltiWrapper = new LTIRequestWrapper(httpReq);
			chain.doFilter(ltiWrapper, response);
			
			if(ltiWrapper.getUserPrincipal()!=null)
				System.out.println("Inside LTI filter ..."+ltiWrapper.getUserPrincipal().toString());
				else
					System.out.println("LTI Filter NOT LOGGED IN");
		}
		else
		{
			System.out.println("Not an LTI User");
			chain.doFilter(request, response);
		}
		
		
		System.out.println("after do filter");

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
