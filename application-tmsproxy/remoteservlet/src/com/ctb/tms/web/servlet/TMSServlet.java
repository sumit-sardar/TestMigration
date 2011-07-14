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
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;
import noNamespace.AdssvcResponseDocument;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData.Tsd.NextSco;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData.Tsd.Status;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.WriteToAuditFile;
import noNamespace.LmsEventType;
import noNamespace.TmssvcRequestDocument;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.LoginRequest;

import com.bea.xml.XmlException;
import com.ctb.tdc.web.utils.ServletUtils;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestData;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.ADSHectorSink;
import com.ctb.tms.nosql.ADSHectorSource;
import com.ctb.tms.nosql.ADSNoSQLSink;
import com.ctb.tms.nosql.ADSNoSQLSource;
import com.ctb.tms.nosql.OASHectorSink;
import com.ctb.tms.nosql.OASHectorSource;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.ctb.tms.nosql.StorageFactory;
import com.ctb.tms.rdb.ADSDBSource;
import com.ctb.tms.web.listener.TestDeliveryContextListener;

public class TMSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	OASNoSQLSource oasSource = StorageFactory.getOASSource();
	OASNoSQLSink oasSink = StorageFactory.getOASSink();
	ADSNoSQLSource adsSource = StorageFactory.getADSSource();
	ADSNoSQLSink adsSink = StorageFactory.getADSSink();

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
			
			if (method != null && method.startsWith(ServletUtils.LOGIN_METHOD))
	            result = login(xml);
			else if (method != null && method.startsWith(ServletUtils.GET_SUBTEST_METHOD))
	            result = getSubtest(xml); 
			else if (method != null && method.startsWith(ServletUtils.DOWNLOAD_ITEM_METHOD))
	            result = downloadItem(xml); 
	        else if (method != null && method.startsWith(ServletUtils.SAVE_METHOD))
	            result = save(response, xml);        
	        else if (method != null && method.startsWith(ServletUtils.FEEDBACK_METHOD))
	            result = feedback(xml);        
	        else if (method != null && method.startsWith(ServletUtils.UPLOAD_AUDIT_FILE_METHOD))
	            result = uploadAuditFile(xml);
	        else if (method != null && method.startsWith(ServletUtils.WRITE_TO_AUDIT_FILE_METHOD))
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

    private String writeToAuditFile(String xml) throws XmlException {
    	AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
		AdssvcRequest saveRequest = document.getAdssvcRequest();
		AdssvcResponseDocument responseDocument = AdssvcResponseDocument.Factory.newInstance();
        WriteToAuditFile saveResponse = responseDocument.addNewAdssvcResponse().addNewWriteToAuditFile();

        //System.out.println(">>>>> " + saveRequest.xmlText());
        
        Tsd[] tsda = saveRequest.getSaveTestingSessionData().getTsdArray();
        for(int i=0;i<tsda.length;i++) {
		    Tsd tsd = tsda[i];
		    String rosterId = tsd.getLsid().substring(0, tsd.getLsid().indexOf(":"));
		    
		    saveResponse.addNewTsd();
	        saveResponse.getTsd().setLsid(tsd.getLsid());
	        saveResponse.getTsd().setScid(tsd.getScid());
	        saveResponse.getTsd().setStatus(WriteToAuditFile.Tsd.Status.OK); 
        }
	    
        //System.out.println("<<<<< " + responseDocument.xmlText());
		return responseDocument.xmlText();
	}

	private String uploadAuditFile(String xml) {
		return null;
	}

	private String feedback(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	private String save(HttpServletResponse response, String xml) throws XmlException, IOException, ClassNotFoundException {
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
		AdssvcRequest saveRequest = document.getAdssvcRequest();
		AdssvcResponseDocument responseDocument = AdssvcResponseDocument.Factory.newInstance();
        SaveTestingSessionData saveResponse = responseDocument.addNewAdssvcResponse().addNewSaveTestingSessionData();

        //System.out.println(">>>>> " + saveRequest.xmlText());
        
        Tsd[] tsda = saveRequest.getSaveTestingSessionData().getTsdArray();
        for(int i=0;i<tsda.length;i++) {
		    Tsd tsd = tsda[i];
		    String rosterId = tsd.getLsid().substring(0, tsd.getLsid().indexOf(":"));
		    
		    saveResponse.addNewTsd();
	        saveResponse.getTsdArray(i).setLsid(tsd.getLsid());
	        saveResponse.getTsdArray(i).setScid(tsd.getScid());
	        saveResponse.getTsdArray(i).setMseq(tsd.getMseq());
	        saveResponse.getTsdArray(i).setStatus(Status.OK);
		    
		    if(tsd.getIstArray() != null && tsd.getIstArray().length > 0) {
		    	oasSink.putItemResponse(rosterId, tsd);

		    }
		    
		    if(tsd.getLsvArray() != null && tsd.getLsvArray().length > 0) {
		    	// subtest events
		    }
		    
		    if(tsd.getLevArray() != null && tsd.getLevArray().length > 0) {
		    	if(tsd.getLevArray()[0].getE() == null || !LmsEventType.TERMINATED.equals(tsd.getLevArray()[0].getE())) {
			    	try {
			    		// test events
				    	NextSco nextSco = saveResponse.getTsdArray(i).addNewNextSco();
				    	Manifest manifest = oasSource.getManifest(rosterId);
				    	ManifestData[] manifestData = manifest.getManifest();
				    	int nextScoIndex = 0;
				    	for(int j=0;j<manifestData.length;j++) {
				    		if(manifestData[j].getId() == Integer.parseInt(tsd.getScid())) {
				    			nextScoIndex = j+1;
				    			break;
				    		}
				    	}
				    	if(nextScoIndex < manifestData.length) {
		                	nextSco.setId(String.valueOf(manifestData[nextScoIndex].getId()));
				    	}
			    	} catch (Exception e) {
			    		// do nothing
			    	}
	                // TODO: place roster on queue for RDBMS persistence
	                TestDeliveryContextListener.enqueueRoster(rosterId);
		    	}
		    }
		    
		    
        }
	    
        // TODO: implement correlation, sequence and subtest/roster status checks for security
        //System.out.println("<<<<< " + responseDocument.xmlText());
		return responseDocument.xmlText();
	}

	private String login(String xml) throws XmlException, IOException, ClassNotFoundException {
		TmssvcRequestDocument document = TmssvcRequestDocument.Factory.parse(xml);
		LoginRequest lr = document.getTmssvcRequest().getLoginRequest();
		StudentCredentials creds = new StudentCredentials();
		if(lr.getUserName() == null || lr.getUserName().trim().length() < 1) {
			creds.setUsername("PT-STUDENT1460604");//lr.getUserName());
			creds.setPassword("dime79");//lr.getPassword());
			creds.setAccesscode("ptest1");//lr.getAccessCode());
		} else {
			creds.setUsername(lr.getUserName());
			creds.setPassword(lr.getPassword());
			creds.setAccesscode(lr.getAccessCode());
		}
		RosterData rd = oasSource.getRosterData(creds);
		// TODO: handle restart case by populating restart data from Cassandra-stored item responses
		return rd.getLoginDocument().xmlText();
	}
	
	public String getSubtest(String xml) throws XmlException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
	{
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
		AdssvcRequest request = document.getAdssvcRequest();
		
		int subtestId = (new Integer(request.getGetSubtest().getSubtestid())).intValue();
		String hash = request.getGetSubtest().getHash();
		String subtest = adsSource.getSubtest(subtestId, hash);
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
			adsSink.putSubtest(subtestId, hash, subtest);
		}
		return subtest;
	}
	
	public String downloadItem(String xml) throws XmlException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
	{
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
		AdssvcRequest request = document.getAdssvcRequest();
		
		int itemId = (new Integer(request.getDownloadItem().getItemid())).intValue();
		String hash = request.getDownloadItem().getHash();
		String subtest = adsSource.getItem(itemId, hash);
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
			adsSink.putItem(itemId, hash, subtest);
		}
		return subtest;
	}

	private String getMethod(HttpServletRequest request) {
    	String URI = request.getRequestURI();
    	String result = URI.substring(URI.lastIndexOf("/") + 1);
		if(result.startsWith(ServletUtils.SAVE_METHOD)) {
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
