package com.ctb.tms.web.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noNamespace.AdssvcRequestDocument;
import noNamespace.AdssvcRequestDocument.AdssvcRequest;
import noNamespace.TmssvcRequestDocument;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.LoginRequest;

import com.bea.xml.XmlException;
import com.ctb.tdc.web.utils.ServletUtils;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.ADSHectorSink;
import com.ctb.tms.nosql.ADSHectorSource;
import com.ctb.tms.nosql.OASHectorSource;
import com.ctb.tms.rdb.ADSDBSource;

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
			//System.out.println(xml);
			
			if (method != null && method.equals(ServletUtils.LOGIN_METHOD))
	            result = login(xml);
			else if (method != null && method.equals(ServletUtils.GET_SUBTEST_METHOD))
	            result = getSubtest(xml); 
			else if (method != null && method.equals(ServletUtils.DOWNLOAD_ITEM_METHOD))
	            result = downloadItem(xml); 
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

	private String save(HttpServletResponse response, String xml) throws XmlException {
		return null;
	}

	private String login(String xml) throws XmlException, IOException, ClassNotFoundException {
		TmssvcRequestDocument document = TmssvcRequestDocument.Factory.parse(xml);
		LoginRequest lr = document.getTmssvcRequest().getLoginRequest();
		StudentCredentials creds = new StudentCredentials();
		if(lr.getUserName() == null || lr.getUserName().trim().length() < 1) {
			creds.setUsername("jared-john-1122");//lr.getUserName());
			creds.setPassword("atone8");//lr.getPassword());
			creds.setAccesscode("overhang84");//lr.getAccessCode());
		} else {
			creds.setUsername(lr.getUserName());
			creds.setPassword(lr.getPassword());
			creds.setAccesscode(lr.getAccessCode());
		}
		RosterData rd = OASHectorSource.getRosterData(creds);
		return rd.getLoginDocument().xmlText();
	}
	
	public String getSubtest(String xml) throws XmlException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
	{
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
		AdssvcRequest request = document.getAdssvcRequest();
		
		int subtestId = (new Integer(request.getGetSubtest().getSubtestid())).intValue();
		String hash = request.getGetSubtest().getHash();
		String subtest = ADSHectorSource.getSubtest(subtestId, hash);
		if(subtest == null) {
			Connection conn = null;
			try {
				conn = ADSDBSource.getADSConnection();
				subtest = ADSDBSource.getSubtest(conn, subtestId, hash);
			} finally {
				if(conn != null) {
					conn.close();
				}
			}
			ADSHectorSink.putSubtest(subtestId, hash, subtest);
		}
		return subtest;
	}
	
	public String downloadItem(String xml) throws XmlException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
	{
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
		AdssvcRequest request = document.getAdssvcRequest();
		
		int itemId = (new Integer(request.getDownloadItem().getItemid())).intValue();
		String hash = request.getDownloadItem().getHash();
		String subtest = ADSHectorSource.getItem(itemId, hash);
		if(subtest == null) {
			Connection conn = null;
			try {
				conn = ADSDBSource.getADSConnection();
				subtest = ADSDBSource.getItem(conn, itemId, hash);
			} finally {
				if(conn != null) {
					conn.close();
				}
			}
			ADSHectorSink.putItem(itemId, hash, subtest);
		}
		return subtest;
	}

	private String getMethod(HttpServletRequest request) {
    	String URI = request.getRequestURI();
    	String result = URI.substring(URI.lastIndexOf("/") + 1);
		if(result.equals(ServletUtils.SAVE_METHOD)) {
			String requestXML = request.getParameter("requestXML");
			if(requestXML.indexOf("adssvc_request") >= 0) {
				if (requestXML.indexOf("get_subtest") >= 0) 
					result = ServletUtils.GET_SUBTEST_METHOD;
				else if (requestXML.indexOf("download_item") >= 0) 
                	result = ServletUtils.DOWNLOAD_ITEM_METHOD;
			}
		}       	
        //System.out.println(result);
    	return result;
	}

}
