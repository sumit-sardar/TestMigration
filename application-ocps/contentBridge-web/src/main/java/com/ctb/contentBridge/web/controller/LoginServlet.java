package com.ctb.contentBridge.web.controller;

/**
 * @author 543559
 *
 */

import java.io.File;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ctb.contentBridge.core.audit.dao.UserDAO;
import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.UserBean;
import com.ctb.contentBridge.core.upload.service.ContentCreatorService;



/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {


/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public void doGet(HttpServletRequest request, HttpServletResponse response) 
			           throws ServletException, java.io.IOException {

try
{	    

     UserBean user = new UserBean();
     user.setUserName(request.getParameter("un"));
     user.setPassword(request.getParameter("pw"));
     
     System.out.println("************** "+request.getParameter("environment"));
     
     String classpath = System.getProperty("java.class.path");
     System.out.println(classpath);
     
     String msEnv = request.getParameter("environment");
     
     
     
     user = UserDAO.login(user);
          	   		  
     if (user.isValid() && user.getPassword() != null)
     {
	        
          HttpSession session = request.getSession(true);	    
          session.setAttribute("currentSessionUser",user);
          //response.sendRedirect("userLogged.jsp");
          
          RequestDispatcher rd= getServletContext().getRequestDispatcher("/view/userLogged.jsp");
          rd.forward(request, response);   
          
         /* Configuration configuration=  new Configuration();
     	  configuration.load(new File("C:\\OCPS\\mappingdata\\"+msEnv+".properties"));
          ContentCreatorService mvContentCreatorService = new ContentCreatorService();
          mvContentCreatorService.processCatalog("13396", configuration);*/
          
          
     }
	        
     else 
          response.sendRedirect("view/invalidLogin.jsp"); //error page 
} 

catch (Throwable theException) 	    
{
     System.out.println(theException);
}
}
/*
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletResponse hsr = (HttpServletResponse) res;
    hsr.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    hsr.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    hsr.setDateHeader("Expires", 0); // Proxies.
    chain.doFilter(req, res);
}*/	
}