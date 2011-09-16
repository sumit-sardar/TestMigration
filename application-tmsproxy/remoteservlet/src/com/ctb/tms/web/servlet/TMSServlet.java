package com.ctb.tms.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noNamespace.AdssvcRequestDocument;
import noNamespace.AdssvcRequestDocument.AdssvcRequest;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist.Rv;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Lsv;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Lsv.CmiCore.Exit;
import noNamespace.AdssvcResponseDocument;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData.Tsd.NextSco;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData.Tsd.Status;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.TmsStatus;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.WriteToAuditFile;
import noNamespace.LmsEventType;
import noNamespace.StudentFeedbackDataDocument;
import noNamespace.StudentFeedbackDataDocument.StudentFeedbackData;
import noNamespace.TmssvcRequestDocument;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.LoginRequest;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;

import com.ctb.tdc.web.utils.ContentFile;
import com.ctb.tdc.web.utils.ServletUtils;
import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestData;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.exception.testDelivery.InvalidCorrelationIdException;
import com.ctb.tms.nosql.ADSNoSQLSink;
import com.ctb.tms.nosql.ADSNoSQLSource;
import com.ctb.tms.nosql.NoSQLStorageFactory;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.ctb.tms.rdb.ADSRDBSink;
import com.ctb.tms.rdb.ADSRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.ctb.tms.util.Constants;
import com.ctb.tms.util.JMSUtils;

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
	        else if(method.toLowerCase().indexOf("mp3") >= 0)
	        	getMp3(request, response);
	        else if (method != null && method.startsWith(ServletUtils.VERIFY_SETTINGS_METHOD)) {
	        	result = verifySettings(xml);
	        }
			else {
	            result = ServletUtils.ERROR;   
	        }
			
	        // return response to client
	        if (result != null) {
	        	ServletUtils.writeResponse(response, result);
	        }
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = ServletUtils.ERROR;
			ServletUtils.writeResponse(response, result);
		}
	}
	
	private String verifySettings(String xml) {
		AdssvcResponseDocument responseDocument = AdssvcResponseDocument.Factory.newInstance();
        TmsStatus status = responseDocument.addNewAdssvcResponse().addNewTmsStatus();
        status.setStatus(TmsStatus.Status.OK);
    	return responseDocument.xmlText();
	}
	
	private void getMp3(HttpServletRequest request, HttpServletResponse response) throws IOException
    {   
		byte [] musicFile = null;
		response.setContentType("audio/mpeg3");
		
		try {
			String musicId = request.getParameter("musicId");
			String resource = "/resources/audio" + musicId + ".mp3";
			InputStream is = getServletContext().getResourceAsStream(resource);
		    musicFile = ContentFile.readFromStream(is);
			OutputStream stream = response.getOutputStream();
    		stream.write(musicFile);
    		response.flushBuffer();
    		stream.close();
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

	private String feedback(String xml) throws XmlException, IOException, ClassNotFoundException {
		// TODO (complete): implement feedback response
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
		AdssvcRequest saveRequest = document.getAdssvcRequest();

        StudentFeedbackDataDocument response = StudentFeedbackDataDocument.Factory.newInstance();
        StudentFeedbackData feedbackResponse = response.addNewStudentFeedbackData();
        
        String lsid = saveRequest.getGetFeedbackData().getLsid();
        String testRosterId = lsid.substring(0, lsid.indexOf(":"));
        String accessCode = lsid.substring(lsid.indexOf(":") + 1, lsid.length());
        
        Manifest manifest = oasSource.getManifest(testRosterId, accessCode);
    	ManifestData[] feedback = manifest.getManifest();

        feedbackResponse.addNewTestingSessionData().setStudentName(manifest.getStudentName());
        feedbackResponse.addNewTitle().setId(String.valueOf(feedback[0].getScoParentId()));
        feedbackResponse.getTitle().setName(feedback[0].getTestTitle());
        feedbackResponse.setLsid(lsid);
        feedbackResponse.setStatus("OK");
        feedbackResponse.addNewLms();
        for(int i=0;i<feedback.length;i++) {
            feedbackResponse.getTitle().addNewSco().setId(String.valueOf(feedback[i].getId()));
            feedbackResponse.getTitle().getScoArray()[i].setTitle(feedback[i].getTitle());
            feedbackResponse.getTitle().getScoArray()[i].setSeq(String.valueOf(feedback[i].getScoOrder()));
            feedbackResponse.getLms().addNewSco().setScid(String.valueOf(feedback[i].getId()));
            feedbackResponse.getLms().getScoArray()[i].addNewLsv().addNewCmiCore().setScoreRaw(new BigDecimal(feedback[i].getRawScore()));
            feedbackResponse.getLms().getScoArray()[i].getLsv().getCmiCore().setScoreMax(new BigDecimal(feedback[i].getMaxScore()));
            feedbackResponse.getLms().getScoArray()[i].getLsv().addNewExtCore().setNumberOfUnscoredItems(new BigInteger(String.valueOf(feedback[i].getUnscored())));
        }
        return response.xmlText();
	}

	private String save(HttpServletResponse response, String xml) throws XmlException, IOException, ClassNotFoundException, InvalidCorrelationIdException {
		//logger.debug(xml);
		
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
		AdssvcRequest saveRequest = document.getAdssvcRequest();
		AdssvcResponseDocument responseDocument = AdssvcResponseDocument.Factory.newInstance();
        SaveTestingSessionData saveResponse = responseDocument.addNewAdssvcResponse().addNewSaveTestingSessionData();
        
        Tsd[] tsda = saveRequest.getSaveTestingSessionData().getTsdArray();
        for(int i=0;i<tsda.length;i++) {
		    Tsd tsd = tsda[i];
		    if(tsd.getLsid() != null && !(tsd.getLsid().length() < 1) && !"undefined".equals(tsd.getLsid())) {
			    String rosterId = tsd.getLsid().substring(0, tsd.getLsid().indexOf(":"));
			    String accessCode = tsd.getLsid().substring(tsd.getLsid().indexOf(":") + 1, tsd.getLsid().length());
			    
			    saveResponse.addNewTsd();
		        saveResponse.getTsdArray(i).setLsid(tsd.getLsid());
		        saveResponse.getTsdArray(i).setScid(tsd.getScid());
		        saveResponse.getTsdArray(i).setMseq(tsd.getMseq());
		        saveResponse.getTsdArray(i).setStatus(Status.OK);
			    
	    		Manifest manifest = oasSource.getManifest(rosterId, accessCode);
	    		if(manifest.getRosterCompletionStatus() == null) {
	    			manifest.setRosterCompletionStatus("IP");
	    		}
		    	ManifestData[] manifestData = manifest.getManifest();
		    	int nextScoIndex = 0;
		    	int j;
		    	for(j=0;j<manifestData.length;j++) {
		    		if(manifestData[j].getId() == Integer.parseInt(tsd.getScid())) {
		    			nextScoIndex = j+1;
		    			// TODO: fix next subtest selection for TABE auto-locator
		    			break;
		    		}
		    	}
		        
		    	manifest.setRosterLastMseq(tsd.getMseq().intValue());
		    	
			    if(tsd.getIstArray() != null && tsd.getIstArray().length > 0) {
			    	int rosterCid = manifest.getRosterCorrelationId();
			    	int thisCid = tsd.getCid().intValue();
			    	logger.debug("Cached CID: " + rosterCid + ", this message CID: " + thisCid);
			    	if(rosterCid != 0 && rosterCid != thisCid) {
			    		responseDocument = AdssvcResponseDocument.Factory.newInstance();
			            saveResponse = responseDocument.addNewAdssvcResponse().addNewSaveTestingSessionData();
			            noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData.Tsd errorTsd = saveResponse.addNewTsd();
			            errorTsd.setStatus(Status.INVALID_CID);
			            errorTsd.addNewError();
			            errorTsd.getError().setMethod("save_testing_session_data");
			            errorTsd.getError().setStatus("invalid_cid");
			            errorTsd.getError().setErrorElement(tsd.toString());
			            return responseDocument.xmlText();
			    	} else if(rosterCid == 0) {
			    		rosterCid = thisCid;
			    		manifest.setRosterCorrelationId(thisCid);
			    	}
			    	// keep IP status if we're receiving heartbeats or responses
			    	manifestData[j].setCompletionStatus("IP");
			    	// response events
			    	oasSink.putItemResponse(rosterId, tsd);
			    	logger.info("TMSServlet: save: cached response for roster " + rosterId + ", message " + tsd.getMseq()); 
			    }
			    
			    if(tsd.getLsvArray() != null && tsd.getLsvArray().length > 0) {
			    	// test events
			    	Lsv[] lsva = tsd.getLsvArray();
			    	// TODO (complete): capture subtest raw scores against manifest record, persist to backing store
			    	int raw = -1;
		            int max = -1;
		            int unscored = -1;
		            boolean timeout = false;
		            for(int k=0;k<lsva.length;i++) {
		                Lsv lsv = lsva[k];
		                if(lsv.getCmiCore() != null) {
		                    if(lsv.getCmiCore().getExit() != null) {
		                        timeout = lsv.getCmiCore().getExit().equals(Exit.TIME_OUT);
		                    }
		                    // collect subtest score stuff here, put in SISS
		                    if(lsv.getCmiCore().getScoreRaw() != null) {
		                        raw = lsv.getCmiCore().getScoreRaw().intValue();
		                        max = lsv.getCmiCore().getScoreMax().intValue();
		                    }
		                }
		                if(lsv.getExtCore() != null && lsv.getExtCore().getNumberOfUnscoredItems() != null) {
		                    unscored = lsv.getExtCore().getNumberOfUnscoredItems().intValue();
		                }
		            }
		            if(raw > -1 && max > -1 && unscored > -1) {
		                manifestData[j].setRawScore(raw);
		                manifestData[j].setMaxScore(max);
		                manifestData[j].setUnscored(unscored);
		            }
			    }
			    
			    if(tsd.getLevArray() != null && tsd.getLevArray().length > 0) {
			    	LmsEventType.Enum eventType = tsd.getLevArray()[0].getE();
	    			//logger.debug("***** Got subtest event type: " + eventType.toString());
			    	if(tsd.getLevArray()[0].getE() == null || !LmsEventType.TERMINATED.equals(eventType)) {
				    	try {
				    		if(LmsEventType.LMS_INITIALIZE.equals(eventType)) {
				    			manifestData[j].setCompletionStatus("IP");
				    			manifest.setRosterCompletionStatus("IP");
				    			manifest.setRosterCorrelationId(tsd.getCid().intValue());
				    		} else if(LmsEventType.STU_PAUSE.equals(eventType)) {
				    			manifestData[j].setCompletionStatus("SP");
				    			manifest.setRosterCompletionStatus("SP");
				    		} else if(LmsEventType.STU_RESUME.equals(eventType)) {
				    			manifestData[j].setCompletionStatus("IP");
				    			manifest.setRosterCompletionStatus("IP");
				    		} else if(LmsEventType.STU_STOP.equals(eventType)) {
				    			manifestData[j].setCompletionStatus("IS");
				    			manifest.setRosterCompletionStatus("IS");
				    		} else if(LmsEventType.LMS_FINISH.equals(eventType)) {
				    			manifestData[j].setCompletionStatus("CO");
				    			manifest.setRosterCompletionStatus("CO");
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
			    	} else if (LmsEventType.TERMINATED.equals(eventType)) {
			    		manifest.setRosterEndTime(new Date(System.currentTimeMillis()));
			    		if("T".equals(manifestData[j].getScorable())) {
			    			JMSUtils.sendMessage(rosterId);
			    			logger.info("TMSServlet: save: sent scoring message for roster " + rosterId);
			            }
			    	}
			    }
			    // always update manifest to override interrupter via write-behind if still receiving events
	    		manifest.setManifest(manifestData);
	    		oasSink.putManifestData(rosterId, accessCode, manifest);
	    		logger.info("TMSServlet: save: updated manifest for roster " + rosterId);
		    }
        }
	    
        // TODO (complete): implement correlation, sequence and subtest/roster status checks for security
        // TODO (complete): update roster status, lastMseq, restartNumber, start/end times, etc. on test events
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
		Manifest manifest = oasSource.getManifest(testRosterId, creds.getAccesscode());
		/*if(manifest == null) {
			manifest = rd.getManifest();
		} else {
			rd.setManifest(manifest);
		}*/
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
	            	Tsd[] irt = null;
	            	ConsolidatedRestartData restartData = loginResponse.getConsolidatedRestartData();
	            	irt = oasSource.getItemResponses(testRosterId);
	            	boolean responsesInCache = (irt != null && irt.length > 0);
	            	boolean responsesInRD = (restartData.getTsdArray() != null && restartData.getTsdArray().length > 0);
                	if (!responsesInCache && responsesInRD) {
                		irt = convertTsdType(restartData.getTsdArray(0));
                		for(int j=0;j<irt.length;j++) {
	                    	oasSink.putItemResponse(testRosterId, irt[j]);
	                    }
                	}
                	ItemResponseData [] ird = RosterData.generateItemResponseData(manifesta[i], irt);
                	loginResponse.setConsolidatedRestartData(ConsolidatedRestartData.Factory.newInstance());
                	restartData = loginResponse.getConsolidatedRestartData();
                	RosterData.generateRestartData(loginResponse, manifesta[i], ird, restartData);
                    gotRestart = true;
                    logger.info("TMSServlet: login: generated restart data for roster " + testRosterId + ", found " + ird.length + " responses");
                }
	        }
	        if(gotRestart) {
	        	loginResponse.setRestartFlag(true);
	        }
		}
		int newRestartCount = restartCount + 1;
		loginResponse.setRestartNumber(BigInteger.valueOf(newRestartCount));
        // TODO (complete): handle random distractor seed
		loginResponse.setRandomDistractorSeedNumber(BigInteger.valueOf(manifest.getRandomDistractorSeed()));
		manifest.setRosterRestartNumber(newRestartCount);
		if(manifest.getRosterStartTime() == null) {
			manifest.setRosterStartTime(new Date(System.currentTimeMillis()));
		}
		manifest.setRosterCompletionStatus("IP");
		manifest.setRosterCorrelationId(0);
		manifest.setStudentName(rd.getAuthData().getStudentFirstName() + " " + rd.getAuthData().getStudentLastName());
		oasSink.putManifestData(testRosterId, creds.getAccesscode(), manifest);
		oasSink.putRosterData(creds, rd);
		
		//logger.debug(response.xmlText());
		
		return response.xmlText();
	}
	
	private Tsd[] convertTsdType(ConsolidatedRestartData.Tsd tsd) {
		//return tsd.changeType(Tsd.type);
		Tsd[] newtsda = new Tsd[tsd.getIstArray().length];
		ConsolidatedRestartData.Tsd.Ist[] ista = tsd.getIstArray();
		for(int i=0;i<ista.length;i++) {
			Tsd newtsd = Tsd.Factory.newInstance();
			newtsd.setScid(tsd.getScid());
			newtsd.setLsid(tsd.getLsid());
			ConsolidatedRestartData.Tsd.Ist ist = ista[i];
			Ist newist = newtsd.addNewIst();
			newist.setIid(ist.getIid());
			newist.setDur(ist.getDur());
			newist.setMrk(ist.getMrk().equals("T")?true:false);
			newist.setEid(ist.getEid());
			newtsd.setMseq(ist.getMseq());
			ConsolidatedRestartData.Tsd.Ist.Rv[] rva = ist.getRvArray();
			for(int j=0;j<rva.length;j++) {
				ConsolidatedRestartData.Tsd.Ist.Rv rv = rva[j];
				Rv newrv = newist.addNewRv();
				newrv.addV(rv.getV());
			}
			newtsda[i] = newtsd;
			//logger.debug("convertTsdType: added response " + ist.getMseq());
		}
		return newtsda;
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
    	String uri = request.getRequestURI();
    	String result = uri.substring(uri.lastIndexOf("/") + 1);
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
