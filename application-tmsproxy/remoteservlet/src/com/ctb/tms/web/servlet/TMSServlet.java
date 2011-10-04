package com.ctb.tms.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Lsv.CmiCore;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Lsv.CmiCore.Exit;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Lsv.ExtCore;
import noNamespace.AdssvcResponseDocument;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.CompleteTutorial;
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
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.values.XmlValueDisconnectedException;

import com.ctb.tdc.web.utils.ContentFile;
import com.ctb.tdc.web.utils.ServletUtils;
import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestData;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.exception.testDelivery.InvalidCorrelationIdException;
import com.ctb.tms.exception.testDelivery.InvalidTestRosterIdException;
import com.ctb.tms.nosql.ADSNoSQLSink;
import com.ctb.tms.nosql.ADSNoSQLSource;
import com.ctb.tms.nosql.NoSQLStorageFactory;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.ctb.tms.rdb.ADSRDBSink;
import com.ctb.tms.rdb.ADSRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.ctb.tms.util.Constants;
import com.ctb.tms.util.TabeLocatorUtils;
import com.ctb.tms.util.TabeLocatorUtils.RecommendedSubtestLevel;
import com.ctb.tms.util.TabeLocatorUtils.RosterSubtestStatus;
import com.ctb.tms.web.listener.TestDeliveryContextListener;
import com.ctb.tms.web.listener.TestDeliveryContextListener.ScoringMessage;

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
			logger.debug("***** Remote servlet request: " + xml);
			
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
	        else if (method != null && method.startsWith(ServletUtils.COMPLETE_TUTORIAL_METHOD))
	            result = completeTutorial(xml);
	        else if(method.toLowerCase().indexOf("mp3") >= 0)
	        	getMp3(request, response);
	        else if (method != null && method.startsWith(ServletUtils.GET_STATUS_METHOD)) {
	        	result = verifySettings(xml);
	        }
			else {
	            result = ServletUtils.ERROR;   
	        }
			
	        // return response to client
	        if (result != null) {
	        	logger.debug("***** response: " + result);
	        	ServletUtils.writeResponse(response, result);
	        }
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = ServletUtils.ERROR;
			ServletUtils.writeResponse(response, result);
		}
	}
	
	private String verifySettings(String xml) {
		XmlOptions xmlOptions = new XmlOptions(); 
        xmlOptions = xmlOptions.setUnsynchronized();
		AdssvcResponseDocument responseDocument = AdssvcResponseDocument.Factory.newInstance(xmlOptions);
        TmsStatus status = responseDocument.addNewAdssvcResponse().addNewTmsStatus();
        status.setStatus(TmsStatus.Status.OK);
    	return responseDocument.xmlText();
	}
	
	private String completeTutorial(String xml) throws XmlException {
    	XmlOptions xmlOptions = new XmlOptions(); 
        xmlOptions = xmlOptions.setUnsynchronized();
        AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml, xmlOptions);
		AdssvcRequest saveRequest = document.getAdssvcRequest();
        AdssvcResponseDocument response = AdssvcResponseDocument.Factory.newInstance(xmlOptions);
        CompleteTutorial saveResponse = response.addNewAdssvcResponse().addNewCompleteTutorial();
        
        String lsid = saveRequest.getCompleteTutorial().getLsid();
        String testRosterId = "-1";
        String accessCode = null;
        if(lsid.indexOf(":") >= 0) {
            testRosterId = lsid.substring(0, lsid.indexOf(":"));
            accessCode = lsid.substring(lsid.indexOf(":")+1,lsid.length());
        }
        int mSeq = saveRequest.getCompleteTutorial().getMseq().intValue();
        saveResponse.setLsid(lsid);
        saveResponse.setMseq(new BigInteger(String.valueOf(mSeq)));
        saveResponse.setStatus(noNamespace.AdssvcResponseDocument.AdssvcResponse.CompleteTutorial.Status.OK);
        try {
            // make sure we have a usable test roster id
            if(testRosterId == null || testRosterId.trim().equals("") || testRosterId.trim().equals("-1")) {
                throw new InvalidTestRosterIdException();
            } else {
                try {
                    Integer.parseInt(testRosterId);
                } catch (Exception e) {
                    throw new InvalidTestRosterIdException();
                }
            }
            
            Manifest manifest = oasSource.getManifest(testRosterId, accessCode);
            
            if (manifest.getTutorialTaken()==null) {
                manifest.setTutorialTaken("TRUE");
                oasSink.putManifest(testRosterId, accessCode, manifest);
            }
        } catch (InvalidTestRosterIdException itre) {
            saveResponse.setStatus(noNamespace.AdssvcResponseDocument.AdssvcResponse.CompleteTutorial.Status.INVALID_LSID);
        } catch (Exception tde) {
            saveResponse.setStatus(noNamespace.AdssvcResponseDocument.AdssvcResponse.CompleteTutorial.Status.OTHER_ERROR);
        }
        return response.xmlText();
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
    	XmlOptions xmlOptions = new XmlOptions(); 
        xmlOptions = xmlOptions.setUnsynchronized();
    	AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml, xmlOptions);
		AdssvcRequest saveRequest = document.getAdssvcRequest();
		AdssvcResponseDocument responseDocument = AdssvcResponseDocument.Factory.newInstance(xmlOptions);
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
		XmlOptions xmlOptions = new XmlOptions(); 
        xmlOptions = xmlOptions.setUnsynchronized();
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml, xmlOptions);
		AdssvcRequest saveRequest = document.getAdssvcRequest();

        StudentFeedbackDataDocument response = StudentFeedbackDataDocument.Factory.newInstance(xmlOptions);
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
            feedbackResponse.getLms().getScoArray()[i].getLsv().getCmiCore().setScoreAbility(new BigDecimal(feedback[i].getAbilityScore()));
            feedbackResponse.getLms().getScoArray()[i].getLsv().getCmiCore().setScoreSem(new BigDecimal(feedback[i].getSemScore()));
            feedbackResponse.getLms().getScoArray()[i].getLsv().getCmiCore().setScoreObjective(feedback[i].getObjectiveScore());
            feedbackResponse.getLms().getScoArray()[i].getLsv().addNewExtCore().setNumberOfUnscoredItems(new BigInteger(String.valueOf(feedback[i].getUnscored())));
        }
        return response.xmlText();
	}

	private String save(HttpServletResponse response, String xml) throws XmlException, IOException, ClassNotFoundException, InvalidCorrelationIdException, SQLException {
		XmlOptions xmlOptions = new XmlOptions(); 
        xmlOptions = xmlOptions.setUnsynchronized();
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml, xmlOptions);
		AdssvcRequest saveRequest = document.getAdssvcRequest();
		AdssvcResponseDocument responseDocument = AdssvcResponseDocument.Factory.newInstance(xmlOptions);
        SaveTestingSessionData saveResponse = responseDocument.addNewAdssvcResponse().addNewSaveTestingSessionData();
        
        Tsd[] tsda = saveRequest.getSaveTestingSessionData().getTsdArray();
        for(int i=0;i<tsda.length;i++) {
		    Tsd tsd = tsda[i];
		    logger.debug("TMSServlet: save tsd: " + tsd);
		    if(tsd.getLsid() != null && !(tsd.getLsid().length() < 1) && !"undefined".equals(tsd.getLsid())) {
			    String rosterId = tsd.getLsid().substring(0, tsd.getLsid().indexOf(":"));
			    String accessCode = tsd.getLsid().substring(tsd.getLsid().indexOf(":") + 1, tsd.getLsid().length()).toUpperCase();
			    
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
		    		if(!"TERMINATOR".equals(tsd.getScid()) && (manifestData[j].getId() == Integer.parseInt(tsd.getScid()))) {
		    			nextScoIndex = j+1;
		    			// TODO (complete): fix next subtest selection for TABE auto-locator
		    			break;
		    		}
		    	}
		    	
		    	int rosterCid = manifest.getRosterCorrelationId();
		    	int thisCid = tsd.getCid().intValue();
		    	logger.debug("Cached CID: " + rosterCid + ", this message CID: " + thisCid);
		    	if(rosterCid != thisCid) {
		    		responseDocument = AdssvcResponseDocument.Factory.newInstance(xmlOptions);
		            saveResponse = responseDocument.addNewAdssvcResponse().addNewSaveTestingSessionData();
		            noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData.Tsd errorTsd = saveResponse.addNewTsd();
		            errorTsd.setStatus(Status.INVALID_CID);
		            errorTsd.addNewError();
		            errorTsd.getError().setMethod("save_testing_session_data");
		            errorTsd.getError().setStatus("invalid_cid");
		            errorTsd.getError().setErrorElement(tsd.toString());
		            manifest.setRosterCompletionStatus("IN");
		            manifestData[j].setCompletionStatus("IN");
		            oasSink.putManifest(rosterId, accessCode, manifest);
		            return responseDocument.xmlText();
		    	}
		        
		    	manifest.setRosterLastMseq(tsd.getMseq().intValue());
		    	
			    if(tsd.getIstArray() != null && tsd.getIstArray().length > 0) {
			    	// keep IP status if we're receiving heartbeats or responses
			    	manifestData[j].setCompletionStatus("IP");
			    	// response events
			    	oasSink.putItemResponse(rosterId, tsd);
			    	logger.debug("TMSServlet: save: cached response for roster " + rosterId + ", message " + tsd.getMseq() + ": " + tsd.xmlText()); 
			    }
			    
			    if(tsd.getLsvArray() != null && tsd.getLsvArray().length > 0) {
			    	// test events
			    	Lsv[] lsva = tsd.getLsvArray();
			    	// TODO (complete): capture subtest raw scores against manifest record, persist to backing store
			    	int raw = -1;
		            int max = -1;
		            int unscored = -1;
		            double ability = 0;
                    double sem = 0;
                    String obj = null;                    
		            boolean timeout = false;
		            for(int k=0;k<lsva.length;k++) {
		                Lsv lsv = (Lsv) lsva[k];
		                CmiCore cmi = lsv.getCmiCore();
		                if(cmi != null) {
		                	Exit.Enum exit = cmi.getExit();
		                    if(exit != null) {
		                        timeout = exit.equals(Exit.TIME_OUT);
		                    }
		                    // collect subtest score stuff here, put in SISS
		                    BigDecimal cmiraw = cmi.getScoreRaw();
		                    BigDecimal cmimax = cmi.getScoreMax();
		                    BigDecimal cmiability = new BigDecimal(0);
		                    BigDecimal cmisem =  new BigDecimal(0);
		                    String cmiobjetive = "";
		                    
		                    if(cmi.getScoreAbility() != null)
		                    	cmiability = cmi.getScoreAbility();
		                    
		                    if(cmi.getScoreSem() != null)
		                    	cmisem = cmi.getScoreSem();
		                    
		                    if(cmi.getScoreObjective() != null)
		                    	cmiobjetive = cmi.getScoreObjective(); 
		                    
		                    if(cmiraw != null) {
		                        raw = cmiraw.intValue();
		                        max = cmimax.intValue();
		                    }
		                    if(cmiability != null) {
	                            ability = cmiability.doubleValue();
	                            sem = cmisem.doubleValue();
	                            obj = cmiobjetive.toString();
	                        }
		                }
		                ExtCore ext = lsv.getExtCore();
		                if(ext != null) {
		                	BigInteger extunscored = ext.getNumberOfUnscoredItems();
		                	if(extunscored != null) {
		                		unscored = extunscored.intValue();
		                	}
		                }
		            }
		            if(raw > -1 && max > -1 && unscored > -1) {
		                manifestData[j].setRawScore(raw);
		                manifestData[j].setMaxScore(max);
		                manifestData[j].setUnscored(unscored);
		            }
		            if(ability > 0){
		            	manifestData[j].setAbilityScore(ability);
		            	manifestData[j].setSemScore(sem);
		            	manifestData[j].setObjectiveScore(obj);
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
				    			manifestData[j].setStartTime(System.currentTimeMillis());
				    			manifest.setRosterCorrelationId(tsd.getCid().intValue());
				    			updateCID(rosterId, tsd.getCid().intValue(), accessCode);
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
				    			manifestData[j].setEndTime(System.currentTimeMillis());
				    			if(j < manifestData.length && ("TB".equals(manifestData[j].getProduct()) && manifestData.length > 8 && "L".equals(manifestData[j].getLevel()))) {
				    				// we just completed a locator subtest of a single-TAC auto-located TABE assessment
				    	    		handleTabeLocator(rosterId);
				    	    		manifest = oasSource.getManifest(rosterId, accessCode);
				    	    		manifestData = manifest.getManifest();
				    	    	}
						    	if(nextScoIndex < manifestData.length) {
						    		NextSco nextSco = saveResponse.getTsdArray(i).addNewNextSco();
				                	nextSco.setId(String.valueOf(manifestData[nextScoIndex].getId()));
				                	logger.debug("Selected next sco: " + manifestData[nextScoIndex].getId());
						    	} else {
						    		logger.debug("Selected next sco index " + nextScoIndex + " is greater than manifest length: " + manifestData.length);
						    	}
				    		}
				    	} catch (Exception e) {
				    		e.printStackTrace();
				    	}
			    	} else if (LmsEventType.TERMINATED.equals(eventType)) {
			    		if(j < manifestData.length && (("TL".equals(manifestData[j].getProduct()) || "TB".equals(manifestData[j].getProduct())) && "L".equals(manifestData[j].getLevel()))) {
		    				// we just completed the locator assessment
		    	    		handleTabeLocator(rosterId);
		    	    		manifest = oasSource.getManifest(rosterId, accessCode);
		    	    		manifestData = manifest.getManifest();
				    	}
			    		manifest.setRosterEndTime(new Timestamp(System.currentTimeMillis()));
			    		/*boolean allComplete = true;
			    		for(int n=0;n<manifestData.length;n++) {
			    			if(!"CO".equals(manifestData[n].getCompletionStatus())) {
			    				allComplete = false;
			    				break;
			    			}
			    		}
			    		if(allComplete) {
			    			manifest.setRosterCompletionStatus("CO");
			    		} else {*/
			    			manifest.setRosterCompletionStatus("IS");
			    		//}
			    		if("T".equals(manifestData[0].getScorable())) {
			    			TestDeliveryContextListener.enqueueRoster(new ScoringMessage(System.currentTimeMillis(), rosterId));
			    			logger.info("TMSServlet: save: sent scoring message for roster " + rosterId);
			            }
			    	}
			    }
			    // always update manifest to override interrupter via write-behind if still receiving events
	    		manifest.setManifest(manifestData);
	    		if(manifest.getRosterCompletionStatus() == null) {
	    			manifest.setRosterCompletionStatus("IP");
	    		}
	    		oasSink.putManifest(rosterId, accessCode, manifest);
	    		logger.info("TMSServlet: save: updated manifest for roster " + rosterId);
		    }
        }
	    
        // TODO (complete): implement correlation, sequence and subtest/roster status checks for security
        // TODO (complete): update roster status, lastMseq, restartNumber, start/end times, etc. on test events
		return responseDocument.xmlText();
	}
	
	private void handleTabeLocator(String testRosterId) throws SQLException, IOException, ClassNotFoundException {
        logger.debug("##### handleTabeLocator: In handleTabeLocator");
		RosterSubtestStatus [] locatorSubtests = null;
        ArrayList rssList = new ArrayList();
        Manifest[] manifesta = oasSource.getAllManifests(testRosterId);
        logger.debug("##### handleTabeLocator: found " + manifesta.length + " manifests for roster " + testRosterId);
        for(int i=0;i<manifesta.length;i++) {
        	Manifest manifest = manifesta[i];
        	ManifestData[] mda = manifest.getManifest();
        	logger.debug("##### handleTabeLocator: manifest " + i + " length: " + mda.length);
        	for(int j=0;j<mda.length;j++) {
        		ManifestData md = mda[j];
        		if("L".equals(md.getLevel())) {
        			logger.debug("##### handleTabeLocator: md " + i + "-" + j + " is locator");
        			RosterSubtestStatus rss = new RosterSubtestStatus();
        			rss.setItemSetId(md.getId());
        			rss.setItemSetName(md.getTitle());
        			rss.setSubtestCompletionStatus(md.getCompletionStatus());
        			rss.setRawScore(md.getRawScore());
        			rssList.add(rss);
        			logger.debug("##### handleTabeLocator: added rss: " + rss.getItemSetName() + ":" + rss.getSubtestCompletionStatus() + ":" + rss.getRawScore());
        		}
        	}
        }
        locatorSubtests = (RosterSubtestStatus[]) rssList.toArray(new RosterSubtestStatus[0]);
        HashMap recommendedMap = TabeLocatorUtils.calculateRecommendSubtestLevel(locatorSubtests);
        logger.debug("##### handleTabeLocator: calculated " + recommendedMap.keySet().size() + " recommended levels");
        for(int i=0;i<manifesta.length;i++) {
        	Manifest manifest = manifesta[i];
        	ManifestData[] mda = manifest.getManifest();
        	ArrayList newmanifest = new ArrayList();
        	for(int j=0;j<mda.length;j++) {
        		ManifestData md = mda[j];
        		String subtestName = mda[j].getTitle().replaceAll(" Locator ", " ").replaceAll(" Sample Questions", "").replaceAll(" Sample Question", "").trim();
        		logger.debug("##### handleTabeLocator: checking recommended level for " + subtestName);
        		RecommendedSubtestLevel rsl = (RecommendedSubtestLevel) recommendedMap.get(subtestName.trim());
        		if(rsl != null) {
	        		if("L".equals(md.getLevel())) {
	        			md.setRecommendedLevel(rsl.getRecommendedLevel());
	        			newmanifest.add(md);
	        			logger.debug("##### handleTabeLocator: set recommended level for locator subtest: " + md.getId());
	        		} else {
	        			if (rsl.getRecommendedLevel().equals(md.getLevel())) {
	        				newmanifest.add(md);
	        				logger.debug("##### handleTabeLocator: found recommended subtest, id: " + md.getId());
	        			}
	        		}
        		} else {
        			logger.debug("##### handleTabeLocator: no level in map for " + subtestName);
        			// no recommendation for this content area yet
        			newmanifest.add(md);
        		}
        	}
        	manifest.setManifest((ManifestData[]) newmanifest.toArray(new ManifestData[0]));
        }
        oasSink.putAllManifests(testRosterId, manifesta);
    }
	
	private void updateCID(String testRosterId, int cid, String currentAccessCode) throws IOException, ClassNotFoundException {
		// necessary to kick out same student using different access code . . .
		Manifest[] allManifests = oasSource.getAllManifests(testRosterId);
		logger.debug("Found " + allManifests.length + " manifests for roster " + testRosterId);
		for(int m=0;m<allManifests.length;m++) {
			allManifests[m].setRosterCorrelationId(cid);
			if("IP".equals(allManifests[m].getRosterCompletionStatus()) && !currentAccessCode.equals(allManifests[m].getAccessCode())) {
				ManifestData[] mda = allManifests[m].getManifest();
				for(int n=0;n<mda.length;n++) {
					if("IP".equals(mda[n].getCompletionStatus())) {
						mda[n].setCompletionStatus("IN");
					}
				}
				allManifests[m].setRosterCompletionStatus("IN");
			}
			logger.debug("Set CID: " + cid + ", for manifest: " + allManifests[m].getAccessCode());
		}
		oasSink.putAllManifests(testRosterId, allManifests);
	}

	private String login(String xml) throws XmlException, IOException, ClassNotFoundException, SQLException {
		XmlOptions xmlOptions = new XmlOptions(); 
        xmlOptions = xmlOptions.setUnsynchronized();
		TmssvcRequestDocument document = TmssvcRequestDocument.Factory.parse(xml, xmlOptions);
		LoginRequest lr = document.getTmssvcRequest().getLoginRequest();
		StudentCredentials creds = new StudentCredentials();
		creds.setUsername(lr.getUserName());
		creds.setPassword(lr.getPassword());
		creds.setAccesscode(lr.getAccessCode().toUpperCase());
		
		RosterData rd = oasSource.getRosterData(creds);
		if(rd == null) {
			TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.AUTHENTICATION_FAILURE_STATUS);
            return response.xmlText();
		}
		String testRosterId = String.valueOf(rd.getAuthData().getTestRosterId());
		Manifest manifest = oasSource.getManifest(testRosterId, creds.getAccesscode());
		if(manifest == null || manifest.getManifest().length < 1) {
			TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.AUTHENTICATION_FAILURE_STATUS);
            return response.xmlText();
		}
		
		ManifestData md = manifest.getManifest()[0];
		if(("TB".equals(md.getProduct()) && manifest.getManifest().length == 8) && !"L".equals(md.getLevel())) {
			// we're in a non-locator section of a multi-TAC auto-located TABE assessment
    		// manifest hasn't been shortened, must not have completed relevant locator subtest
			TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.LOCATOR_SUBTEST_NOT_COMPLETED_STATUS);
            return response.xmlText();
    	}

		TmssvcResponseDocument response = rd.getLoginDocument();
		LoginResponse loginResponse = response.getTmssvcResponse().getLoginResponse();
       	Sco[] scoa = new Sco[0];
       	if (loginResponse.getManifest() != null) {
       		scoa = loginResponse.getManifest().getScoArray();
       	}
       	logger.debug("Initial manifest size: " + scoa.length);
       	/*LinkedHashMap scomap = new LinkedHashMap(scoa.length);
       	for (int h=0;h<scoa.length;h++) {
       		scomap.put(scoa[h].getId(), scoa[h]);
       		logger.debug("Added Sco " + scoa[h].getId() + " to scomap.");
       	}*/
		BigInteger restart = loginResponse.getRestartNumber();
		if(restart == null) restart = BigInteger.valueOf(0);
		int restartCount = restart.intValue();
		logger.debug("Restart count: " + restartCount);
		
		int thisCid = Integer.parseInt(lr.getCid());
		manifest.setRosterCorrelationId(thisCid);
		if(restartCount > 0) {
			// try to optimize by only setting cid on other manifests if they've been accessed - this may be risky
			updateCID(testRosterId, thisCid, creds.getAccesscode());
		}

		ManifestData[] manifesta = manifest.getManifest();
		ArrayList newmanifest = new ArrayList();
		boolean gotRestart = false;
        for(int i=0; i<manifesta.length ;i++) {
            if(restartCount > 0 && !gotRestart && (manifesta[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS) || 
            					manifesta[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS) ||
            					manifesta[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS) ||
            					manifesta[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS))) {        	
            	Tsd[] irt = null;
            	ConsolidatedRestartData restartData = null;
            	irt = oasSource.getItemResponses(testRosterId);
            	logger.info("TMSServlet: found " + irt.length + " responses in cache.");
            	boolean responsesInCache = (irt != null && irt.length > 0);
            	ConsolidatedRestartData[] crda = loginResponse.getConsolidatedRestartDataArray();
            	if(crda != null && crda.length > 0) {
	            	restartData = loginResponse.getConsolidatedRestartDataArray(0);
	            	boolean responsesInRD = (restartData.getTsdArray() != null && restartData.getTsdArray().length > 0);
                	if (!responsesInCache && responsesInRD) {
                		irt = convertTsdType(restartData.getTsdArray(0));
                		for(int j=0;j<irt.length;j++) {
	                    	oasSink.putItemResponse(testRosterId, irt[j]);
	                    }
                	}
            	}
            	ItemResponseData [] ird = RosterData.generateItemResponseData(manifesta[i], irt);
            	if(loginResponse.getConsolidatedRestartDataArray() == null || loginResponse.getConsolidatedRestartDataArray().length == 0) {
            		loginResponse.addNewConsolidatedRestartData();
            	}
            	loginResponse.setConsolidatedRestartDataArray(0, ConsolidatedRestartData.Factory.newInstance(xmlOptions));
            	restartData = loginResponse.getConsolidatedRestartDataArray(0);
            	RosterData.generateRestartData(loginResponse, manifesta[i], ird, restartData);
                gotRestart = true;
                logger.info("TMSServlet: login: generated restart data for roster " + testRosterId + ", found " + ird.length + " responses");
            } 
            if (manifesta[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS)) {
            	//scomap.remove(String.valueOf(manifesta[i].getId()));
            	//logger.debug("found completed sco: " + String.valueOf(manifesta[i].getId()));
            	int g;
            	for(g=0;g<scoa.length;g++) {
            		try {
	            		//logger.debug("rd id: " + scoa[g].getId() + ", manifest id: " + String.valueOf(manifesta[i].getId()));
	            		if(scoa[g].getId().equals(String.valueOf(manifesta[i].getId()))) {
	            			break;
	            		}
            		} catch (XmlValueDisconnectedException xe) {
            			break;
            		}
            	}
            	if(g<scoa.length) {
	            	//logger.debug("removing Sco " + g + " from manifest");
	            	loginResponse.getManifest().removeSco(g);
	            	logger.debug("removed Sco " + manifesta[i].getId() + " from scomap.");
            	}
            } else {
            	newmanifest.add(manifesta[i]);
            }
        }
        if(newmanifest.size() < 1) {
			response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_COMPLETED_STATUS);
            return response.xmlText();
		} else {
			manifest.setManifest((ManifestData[])newmanifest.toArray(new ManifestData[0]));
		}
        //loginResponse.getManifest().setScoArray((Sco[])scomap.values().toArray(new Sco[0]));
        //logger.debug("Final manifest: " + loginResponse.getManifest().xmlText());
        if(gotRestart) {
        	loginResponse.setRestartFlag(true);
        }

        // TODO (complete): handle random distractor seed
		if (rd.getAuthData().getRandomDistractorSeedNumber() != null) {
			 loginResponse.setRandomDistractorSeedNumber(new BigInteger(String.valueOf( rd.getAuthData().getRandomDistractorSeedNumber())));
		 }  else {
			 if ("Y".equals(manifest.getManifest()[0].getRandomDistractorStatus())) {
				 Integer seed = manifest.getRandomDistractorSeed();
				 if(seed == null) {
					 seed = generateRandomNumber();
					 manifest.setRandomDistractorSeed(seed);
				 }
				 rd.getAuthData().setRandomDistractorSeedNumber(seed);
				 loginResponse.setRandomDistractorSeedNumber(BigInteger.valueOf(seed));
			 }
		 }
		
		if(manifest.getRosterStartTime() == null) {
			manifest.setRosterStartTime(new Timestamp(System.currentTimeMillis()));
		}
		manifest.setRosterCompletionStatus("IP");
		manifest.setStudentName(rd.getAuthData().getStudentFirstName() + " " + rd.getAuthData().getStudentLastName());
		String result = response.xmlText();
		int newRestartCount = restartCount + 1;
		manifest.setRosterRestartNumber(newRestartCount);
		loginResponse.setRestartNumber(BigInteger.valueOf(newRestartCount));
		rd.getAuthData().setRestartNumber(newRestartCount);
		if(loginResponse.getTutorial() != null) {
			if(manifest.getTutorialTaken() == null) {
				manifest.setTutorialTaken(0==loginResponse.getTutorial().getDeliverTutorial().intValue()?"TRUE":null);
			} else {
				int tut = "TRUE".equals(manifest.getTutorialTaken())?0:1;
				loginResponse.getTutorial().setDeliverTutorial(BigInteger.valueOf(tut));
			}
		}
		oasSink.putManifest(testRosterId, creds.getAccesscode(), manifest);
		oasSink.putRosterData(creds, rd);
		
		logger.debug(response.xmlText());
		
		return result;
	}
	
	private static Integer generateRandomNumber () {
		final String NUM_ARRAY   = "1234567890";
		String alphaNumArray = NUM_ARRAY;
		int index = 0;
		Random rnd = new Random();
		boolean validRandom = false;
		String seed = "";
		while(!validRandom) {
			for(int i = 0; i < 3; i++) {
				index = rnd.nextInt();
				if (index < 0) {
					index = index * -1;
				}
				// make sure the index is a value within the length of our array
				if(index != 0) {
					index = index % alphaNumArray.length();
				}
				seed = seed.concat(String.valueOf(alphaNumArray.charAt(index)));
			}
			if (isNumOdd(seed)) {
				validRandom = true;
				if(verifyContainsCharFrom(NUM_ARRAY,seed)) {
					validRandom = true;
				}
			} else {
				seed = "";
			}
		}
		return Integer.valueOf(seed);
	}
	
	private static boolean verifyContainsCharFrom(String charArray,String seed) {
		boolean verified = false;
		int j = 0;
		while(!verified && (j < seed.length())) {
			if(charArray.indexOf(String.valueOf(seed.charAt(j))) != -1) {
				verified = true;
			}
			j++;
		}
		return verified;
	}
    
    private static boolean isNumOdd(String seed) {

		return Integer.valueOf(String.valueOf(seed.charAt(seed.length() - 1))).
				intValue() % 2 == 0 ? false:true;
	}
	
	private Tsd[] convertTsdType(ConsolidatedRestartData.Tsd tsd) {
		//return tsd.changeType(Tsd.type);
		Tsd[] newtsda = new Tsd[tsd.getIstArray().length];
		ConsolidatedRestartData.Tsd.Ist[] ista = tsd.getIstArray();
		for(int i=0;i<ista.length;i++) {
			XmlOptions xmlOptions = new XmlOptions(); 
	        xmlOptions = xmlOptions.setUnsynchronized();
			Tsd newtsd = Tsd.Factory.newInstance(xmlOptions);
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
		XmlOptions xmlOptions = new XmlOptions(); 
        xmlOptions = xmlOptions.setUnsynchronized();
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml, xmlOptions);
		AdssvcRequest request = document.getAdssvcRequest();
		
		int subtestId = (new Integer(request.getGetSubtest().getSubtestid())).intValue();
		String hash = request.getGetSubtest().getHash();
		String subtest = adsSource.getSubtest(subtestId, hash);
		// TODO: get from Coherence read-through.
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
		XmlOptions xmlOptions = new XmlOptions(); 
        xmlOptions = xmlOptions.setUnsynchronized();
		AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml, xmlOptions);
		AdssvcRequest request = document.getAdssvcRequest();
		
		int itemId = (new Integer(request.getDownloadItem().getItemid())).intValue();
		String hash = request.getDownloadItem().getHash();
		String item = adsSource.getItem(itemId, hash);
		// TODO: get from Coherence read-through.
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
    	logger.debug(uri);
    	String result = uri.substring(uri.lastIndexOf("/") + 1);
		if(result.startsWith(ServletUtils.SAVE_METHOD)) {
			String requestXML = request.getParameter("requestXML");
			if(requestXML.indexOf("adssvc_request") >= 0) {
				if (requestXML.indexOf("get_subtest") >= 0) 
					result = ServletUtils.GET_SUBTEST_METHOD;
				else if (requestXML.indexOf("download_item") >= 0) 
                	result = ServletUtils.DOWNLOAD_ITEM_METHOD;
				else if (requestXML.indexOf("complete_tutorial") >= 0)
					result = ServletUtils.COMPLETE_TUTORIAL_METHOD;
			}
		}       	
        //logger.debug(result);
    	return result;
	}

}
