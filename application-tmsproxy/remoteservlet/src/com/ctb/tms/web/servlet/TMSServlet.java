package com.ctb.tms.web.servlet;

import java.io.IOException;
import java.math.BigInteger;
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
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;

import org.apache.log4j.Logger;

import com.bea.xml.XmlException;
import com.ctb.tdc.web.utils.ServletUtils;
import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestData;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.ADSNoSQLSink;
import com.ctb.tms.nosql.ADSNoSQLSource;
import com.ctb.tms.nosql.NoSQLStorageFactory;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.ctb.tms.rdb.ADSRDBSink;
import com.ctb.tms.rdb.ADSRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.ctb.tms.util.Constants;

public class TMSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	OASNoSQLSource oasSource = NoSQLStorageFactory.getOASSource();
	OASNoSQLSink oasSink = NoSQLStorageFactory.getOASSink();
	ADSNoSQLSource adsSource = NoSQLStorageFactory.getADSSource();
	ADSNoSQLSink adsSink = NoSQLStorageFactory.getADSSink();
	
	ADSRDBSource adsDBSource = RDBStorageFactory.getADSSource();
	
	static Logger logger = Logger.getLogger(TMSServlet.class);

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
			//logger.debug("***** Remote servlet request: " + xml);
			
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

        //logger.debug(">>>>> " + saveRequest.xmlText());
        
        AdssvcRequestDocument.AdssvcRequest.WriteToAuditFile.Tsd tsd = saveRequest.getWriteToAuditFile().getTsd();
        String rosterId = tsd.getLsid().substring(0, tsd.getLsid().indexOf(":"));
		    
	    saveResponse.addNewTsd();
        saveResponse.getTsd().setLsid(tsd.getLsid());
        saveResponse.getTsd().setScid(tsd.getScid());
        saveResponse.getTsd().setStatus(WriteToAuditFile.Tsd.Status.OK); 
	    
        //logger.debug("<<<<< " + responseDocument.xmlText());
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
        
        Tsd[] tsda = saveRequest.getSaveTestingSessionData().getTsdArray();
        for(int i=0;i<tsda.length;i++) {
		    Tsd tsd = tsda[i];
		    if(tsd.getLsid() != null && !(tsd.getLsid().length() < 1) && !"undefined".equals(tsd.getLsid())) {
			    String rosterId = tsd.getLsid().substring(0, tsd.getLsid().indexOf(":"));
			    
			    saveResponse.addNewTsd();
		        saveResponse.getTsdArray(i).setLsid(tsd.getLsid());
		        saveResponse.getTsdArray(i).setScid(tsd.getScid());
		        saveResponse.getTsdArray(i).setMseq(tsd.getMseq());
		        saveResponse.getTsdArray(i).setStatus(Status.OK);
			    
	    		Manifest manifest = oasSource.getManifest(rosterId);
		    	ManifestData[] manifestData = manifest.getManifest();
		    	int nextScoIndex = 0;
		    	int j;
		    	for(j=0;j<manifestData.length;j++) {
		    		if(manifestData[j].getId() == Integer.parseInt(tsd.getScid())) {
		    			nextScoIndex = j+1;
		    			break;
		    		}
		    	}
		        
			    if(tsd.getIstArray() != null && tsd.getIstArray().length > 0) {
			    	// keep IP status if we're receiving heartbeats or responses
			    	manifestData[j].setCompletionStatus("IP");
			    	// response events
			    	oasSink.putItemResponse(rosterId, tsd);
			    }
			    
			    if(tsd.getLsvArray() != null && tsd.getLsvArray().length > 0) {
			    	// test events
			    }
			    
			    if(tsd.getLevArray() != null && tsd.getLevArray().length > 0) {
			    	LmsEventType.Enum eventType = tsd.getLevArray()[0].getE();
	    			//logger.debug("***** Got subtest event type: " + eventType.toString());
			    	if(tsd.getLevArray()[0].getE() == null || !LmsEventType.TERMINATED.equals(eventType)) {
				    	try {
				    		if(LmsEventType.LMS_INITIALIZE.equals(eventType)) {
				    			manifestData[j].setCompletionStatus("IP");
				    		} else if(LmsEventType.STU_PAUSE.equals(eventType)) {
				    			manifestData[j].setCompletionStatus("SP");
				    		} else if(LmsEventType.STU_RESUME.equals(eventType)) {
				    			manifestData[j].setCompletionStatus("IP");
				    		} else if(LmsEventType.STU_STOP.equals(eventType)) {
				    			manifestData[j].setCompletionStatus("IS");
				    		} else if(LmsEventType.LMS_FINISH.equals(eventType)) {
				    			manifestData[j].setCompletionStatus("CO");
						    	NextSco nextSco = saveResponse.getTsdArray(i).addNewNextSco();
						    	
						    	if(nextScoIndex < manifestData.length) {
				                	nextSco.setId(String.valueOf(manifestData[nextScoIndex].getId()));
						    	}
				    		}
				    	} catch (Exception e) {
				    		e.printStackTrace();
				    	}
		                // Cache write-behind will handle response persistence
		                //TestDeliveryContextListener.enqueueRoster(rosterId);
			    	}
			    }
			    // always update manifest to override interrupter via write-behind if still receiving events
	    		manifest.setManifest(manifestData);
	    		oasSink.putManifestData(rosterId, manifest);
		    }
        }
	    
        // TODO: implement correlation, sequence and subtest/roster status checks for security
        // TODO: update roster status, lastMseq, restartNumber, start/end times, etc. on test events
		return responseDocument.xmlText();
	}

	private String login(String xml) throws XmlException, IOException, ClassNotFoundException, SQLException {
		TmssvcRequestDocument document = TmssvcRequestDocument.Factory.parse(xml);
		LoginRequest lr = document.getTmssvcRequest().getLoginRequest();
		StudentCredentials creds = new StudentCredentials();
		if(lr.getUserName() == null || lr.getUserName().trim().length() < 1) {
			creds.setUsername("PT-STUDENT1459034");//lr.getUserName());
			creds.setPassword("dime79");//lr.getPassword());
			creds.setAccesscode("ptest1");//lr.getAccessCode());
		} else {
			creds.setUsername(lr.getUserName());
			creds.setPassword(lr.getPassword());
			creds.setAccesscode(lr.getAccessCode());
		}
		RosterData rd = oasSource.getRosterData(creds);
		String testRosterId = String.valueOf(rd.getAuthData().getTestRosterId());
		Manifest manifest = oasSource.getManifest(testRosterId);
		if(manifest == null) {
			manifest = rd.getManifest();
			oasSink.putManifestData(testRosterId, manifest);
		} else {
			rd.setManifest(manifest);
		}
		TmssvcResponseDocument response = rd.getLoginDocument();
		LoginResponse loginResponse = response.getTmssvcResponse().getLoginResponse();
		BigInteger restart = loginResponse.getRestartNumber();
		if(restart == null) restart = BigInteger.valueOf(0);
		int restartCount = restart.intValue();
		logger.debug("Restart count: " + restartCount);
		if(restartCount > 0) {
			ManifestData[] manifesta = manifest.getManifest();
			boolean gotRestart = false;
	        for(int i=0; i<manifesta.length ;i++) {
	            if(!gotRestart && (manifesta[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS) || 
	            					manifesta[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS) ||
	            					manifesta[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS) ||
	            					manifesta[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS))) {
                	ConsolidatedRestartData restartData = loginResponse.addNewConsolidatedRestartData();
                	Tsd[] irt = oasSource.getItemResponses(testRosterId);
                	ItemResponseData [] ird = RosterData.generateItemResponseData(manifesta[i], irt);
                    RosterData.generateRestartData(loginResponse, manifesta[i], ird, restartData);
                    gotRestart = true;
                }
	        }
	        if(gotRestart) {
	        	loginResponse.setRestartFlag(true);
	        }
		}
		loginResponse.setRestartNumber(BigInteger.valueOf(restartCount + 1));
		oasSink.putRosterData(creds, rd);
		return response.xmlText();
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
				conn = adsDBSource.getADSConnection();
				subtest = adsDBSource.getSubtest(conn, subtestId, hash);
				if("true".equals(RDBStorageFactory.copytosink)) {
					ADSRDBSink adsDBSink = RDBStorageFactory.getADSSink();
					Connection sinkConn = adsDBSink.getADSConnection();
					adsDBSink.putSubtest(sinkConn, subtestId, subtest);
					sinkConn.commit();
					sinkConn.close();
				}
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
		String item = adsSource.getItem(itemId, hash);
		if(item == null) {
			Connection conn = null;
			try {
				conn = adsDBSource.getADSConnection();
				item = adsDBSource.getItem(conn, itemId, hash);
				if("true".equals(RDBStorageFactory.copytosink)) {
					ADSRDBSink adsDBSink = RDBStorageFactory.getADSSink();
					Connection sinkConn = adsDBSink.getADSConnection();
					adsDBSink.putItem(sinkConn, itemId, item);
					sinkConn.commit();
					sinkConn.close();
				}
			} finally {
				if(conn != null) {
					conn.close();
				}
			}
			adsSink.putItem(itemId, hash, item);
		}
		return item;
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
        //logger.debug(result);
    	return result;
	}

}
