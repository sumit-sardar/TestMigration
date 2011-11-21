package com.ctb.tms.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ctb.tdc.web.utils.ServletUtils;
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
		boolean active = CacheFactory.getCache("OASResponseCache").isActive() &&
						 CacheFactory.getCache("OASManifestCache").isActive() &&
						 CacheFactory.getCache("OASRosterCache").isActive() &&
						 CacheFactory.getCache("ADSItemCache").isActive() &&
						 CacheFactory.getCache("ADSItemSetCache").isActive();
		if(active) {
			out.println("appCode=Pass");
			logger.info(" . . . passed.");
		} else {
			out.println("appCode=Fail");
			logger.info(" . . . failed.");
		}
		out.flush();
		out.close();
		response.flushBuffer();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {    
        doGet(request, response);            
    }
	
}
