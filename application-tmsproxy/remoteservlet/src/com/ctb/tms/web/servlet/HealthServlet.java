package com.ctb.tms.web.servlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ctb.tdc.web.utils.ServletUtils;
import com.ctb.tms.web.listener.AppStatusContextListener;
import com.tangosol.net.CacheFactory;

public class HealthServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(HealthServlet.class);
	
	public HealthServlet() {
		super();
	}

    public void init() throws ServletException {
		// do nothing
    }

	public void destroy() {
		super.destroy(); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Server status check . . .");
		response.setContentType("text/plain;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		String checkStatus = AppStatusContextListener.checkStatus;
		String fileStatus = AppStatusContextListener.fileStatus;
		if(!"Pass".equals(checkStatus) || !"Pass".equals(fileStatus)) {
			out.write("appCode=Fail");
		} else {
			out.write("appCode=Pass");
		}
		out.flush();
		out.close();
		response.flushBuffer();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {    
        doGet(request, response);            
    }
	
}
