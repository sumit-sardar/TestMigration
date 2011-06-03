package com.ctb.tms.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noNamespace.TmssvcRequestDocument;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.LoginRequest;

import com.bea.xml.XmlException;
import com.ctb.tdc.web.utils.ServletUtils;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.OASHectorSource;

public class TMSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public TMSServlet() {
		super();
	}

    public void init() throws ServletException {
        // do nothing
    }

	public void destroy() {
		super.destroy(); 
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {    
        doGet(request, response);            
    }

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = getMethod(request);
		String result = ServletUtils.OK;
		try {
			String xml = request.getParameter("requestXML");
			System.out.println(xml);
			
			if (method != null && method.equals(ServletUtils.LOGIN_METHOD))
	            result = login(xml);
	        else if (method != null && method.equals(ServletUtils.SAVE_METHOD))
	            result = save(response, xml);        
	        else if (method != null && method.equals(ServletUtils.FEEDBACK_METHOD))
	            result = feedback(xml);        
	        else if (method != null && method.equals(ServletUtils.UPLOAD_AUDIT_FILE_METHOD))
	            result = uploadAuditFile(xml);
	        else if (method != null && method.equals(ServletUtils.WRITE_TO_AUDIT_FILE_METHOD))
	            result = writeToAuditFile(xml);
	        else
	            result = ServletUtils.ERROR;   
			
	        // return response to client
	        if (result != null) {
	        	ServletUtils.writeResponse(response, result);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private String writeToAuditFile(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	private String uploadAuditFile(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	private String feedback(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	private String save(HttpServletResponse response, String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	private String login(String xml) throws XmlException, IOException, ClassNotFoundException {
		TmssvcRequestDocument document = TmssvcRequestDocument.Factory.parse(xml);
		LoginRequest lr = document.getTmssvcRequest().getLoginRequest();
		StudentCredentials creds = new StudentCredentials();
		creds.setUsername(lr.getUserName());
		creds.setPassword(lr.getPassword());
		creds.setAccesscode(lr.getAccessCode());
		RosterData rd = OASHectorSource.getRosterData(creds);
		return rd.getLoginDocument().xmlText();
	}

	private String getMethod(HttpServletRequest request) {
    	String URI = request.getRequestURI();
    	String result = URI.substring(URI.lastIndexOf("/") + 1);
		//System.out.println(result);
    	return result;
	}

}
