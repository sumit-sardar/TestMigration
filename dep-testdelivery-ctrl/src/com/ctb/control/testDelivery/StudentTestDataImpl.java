package com.ctb.control.testDelivery; 

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.naming.InitialContext;

import noNamespace.AdssvcRequestDocument;
import noNamespace.AdssvcResponseDocument;
import noNamespace.BaseType;
import noNamespace.LmsEventType;
import noNamespace.StudentFeedbackDataDocument;
import noNamespace.AdssvcRequestDocument.AdssvcRequest;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.GetFeedbackData.Lms.Sco;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ast;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Lev;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Lsv;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist.Rv;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Lsv.CmiCore.Exit;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.CompleteTutorial;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData.Tsd.NextSco;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData.Tsd.Status;
import noNamespace.StudentFeedbackDataDocument.StudentFeedbackData;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testDelivery.login.ItemResponseData;
import com.ctb.bean.testDelivery.login.ManifestData;
import com.ctb.bean.testDelivery.studentTestData.RecommendedSubtestLevel;
import com.ctb.bean.testDelivery.studentTestData.RosterSubtestFeedback;
import com.ctb.bean.testDelivery.studentTestData.RosterSubtestStatus;
import com.ctb.bean.testDelivery.studentTestData.StudentTutorialStatus;
import com.ctb.control.jms.QueueSend;
import com.ctb.exception.testDelivery.InvalidCorrelationIdException;
import com.ctb.exception.testDelivery.InvalidItemResponseException;
import com.ctb.exception.testDelivery.InvalidItemSetIdException;
import com.ctb.exception.testDelivery.InvalidMseqException;
import com.ctb.exception.testDelivery.InvalidSubtestEventException;
import com.ctb.exception.testDelivery.InvalidTestRosterIdException;
import com.ctb.exception.testDelivery.MissingCorrelationIdException;
import com.ctb.exception.testDelivery.TestDeliveryException;
import com.ctb.util.OASLogger;
import com.ctb.util.SimpleCache;
import com.ctb.util.testDelivery.Constants;
import com.ctb.util.testDelivery.TabeLocatorUtils;

/**
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation(isTransient=true)
public class StudentTestDataImpl implements StudentTestData
{ 
	private static final String TMS_PER_INSTANCE_DUPE_CHECK = "TMS_PER_INSTANCE_DUPE_CHECK";
	private static final String TMS_PER_INSTANCE_SP_CHECK = "TMS_PER_INSTANCE_SP_CHECK";
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.AuthenticateStudent authenticator;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Product product;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.TestRoster testRoster;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.jms.ScoreStudent scorer;

    static final long serialVersionUID = 1L;
    
    private static final String CACHE_TYPE_SPEECH = "CACHE_TYPE_SPEECH";
    private String jndiFactory = "";
    private String jmsFactory = "";
    private String jmsURL = "";
    private String jmsQueue = "";
    private String jmsPrincipal = "";
    private String jmsCredentials = "";
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.SaveStudentTestData saver;
    
     /**
     * @common:operation
     */
    public AdssvcResponseDocument save(AdssvcRequestDocument document, boolean responseQueue)
    {
        AdssvcRequest saveRequest = document.getAdssvcRequest();
       // OASLogger.getLogger("TestDelivery").debug(saveRequest.toString());
        AdssvcResponseDocument response = AdssvcResponseDocument.Factory.newInstance();
        SaveTestingSessionData saveResponse = response.addNewAdssvcResponse().addNewSaveTestingSessionData();
        Tsd [] tsda = saveRequest.getSaveTestingSessionData().getTsdArray();
        if(tsda != null) {
            int i = 0;
            // sort incoming tsd elements by mseq for each roster before processing
            HashMap tsdMap = new HashMap();
            for(int k=0;k<tsda.length;k++) {
                Integer lsid = new Integer(-1);
                try {
                    lsid = Integer.valueOf(tsda[k].getLsid().substring(0, tsda[k].getLsid().indexOf(":")));
                } catch (Exception e) {
                    //do nothing here, throw lsid exception below if we can't get a good test roster id
                    lsid = new Integer (-1);
                }
                Integer mseq = new Integer(tsda[k].getMseq().intValue());
                HashMap mseqMap = (HashMap) tsdMap.get(lsid);
                if(mseqMap == null) mseqMap = new HashMap();
                mseqMap.put(mseq, tsda[k]);
                tsdMap.put(lsid, mseqMap);
            }
            Set keys = tsdMap.keySet();
            Iterator iter = keys.iterator();
            while (iter.hasNext()) {
                ArrayList keyList = new ArrayList();
                HashMap mseqMap = (HashMap) tsdMap.get((Integer) iter.next());
                Set mseqKeys = mseqMap.keySet();
                Iterator mseqIter = mseqKeys.iterator();
                while (mseqIter.hasNext()) {
                    keyList.add(mseqIter.next());
                }
                Collections.sort(keyList);
                Iterator keyIter = keyList.iterator();
                while(keyIter.hasNext()) {
                    Tsd tsd = (Tsd) mseqMap.get(keyIter.next());
                    String lsid = tsd.getLsid();
                    String testRosterId = "-1";
                    if(lsid.indexOf(":") >= 0) {
                        testRosterId = lsid.substring(0, lsid.indexOf(":"));
                    }
                    String itemSetId = tsd.getScid();
                    int mSeq = tsd.getMseq().intValue();
                    saveResponse.addNewTsd();
                    saveResponse.getTsdArray(i).setLsid(lsid);
                    saveResponse.getTsdArray(i).setScid(String.valueOf(itemSetId));
                    saveResponse.getTsdArray(i).setMseq(tsd.getMseq());
                    saveResponse.getTsdArray(i).setStatus(Status.OK);
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
                        RosterSubtestStatus [] statusList = saver.getRosterSubtestStatus(Integer.parseInt(testRosterId));
                        if(responseQueue) {
                            OASLogger.getLogger("TestDelivery").debug("message from tsd queue");
                            processItemResponseEvents(testRosterId, statusList, itemSetId, tsd.getIstArray(), mSeq, 0, false);
                            processTestEvents(testRosterId, itemSetId, tsd.getLevArray(), tsd.getLsvArray(), statusList);
                        } else {
                            if(statusList.length < 1) {
                                // couldn't find siss records for this roster id
                                OASLogger.getLogger("TestDelivery").debug("startSubtest: no SISS records for roster: " + testRosterId);
                                throw new InvalidTestRosterIdException();
                            }
                            int lastMseq = statusList[0].getLastMseq();                        
                            if(tsd.getMseq().intValue() > lastMseq) {
                                OASLogger.getLogger("TestDelivery").debug("message from lms queue");
                                processItemResponseEvents(testRosterId, statusList, itemSetId, tsd.getIstArray(), mSeq, 0, false);
                                processSubtestEvents(testRosterId, statusList, itemSetId, tsd.getLevArray(), tsd.getLsvArray(), tsd.getMseq().intValue(), null);
                            } else {
                                OASLogger.getLogger("TestDelivery").debug("ignoring message from lms queue, this mseq: " + tsd.getMseq().intValue() + " is less than last mseq: " + lastMseq);
                            }
                        }
                    } catch (InvalidTestRosterIdException itre) {
                        handleError(tsd, saveResponse.getTsdArray(i), Status.INVALID_LSID, "invalid_lsid");
                    } catch (InvalidItemSetIdException iise) {
                        handleError(tsd, saveResponse.getTsdArray(i), Status.INVALID_SCID, "invalid_scid");
                    } catch (InvalidSubtestEventException isee) {
                        handleError(tsd, saveResponse.getTsdArray(i), Status.INVALID_LEV, "invalid_lev");
                    } catch (InvalidItemResponseException iire) {
                        handleError(tsd, saveResponse.getTsdArray(i), Status.INVALID_IST, "invalid_ist");
                    } catch (Exception tde) {
                        handleError(tsd, saveResponse.getTsdArray(i), Status.OTHER_ERROR, "other_error");
                    }
                    OASLogger.getLogger("TestDelivery").debug(saveResponse.getTsdArray(i).toString());
                    i++;
                }
            }     
        }
        return response;
    }
    
    /**
     * @common:operation
     */
    public StudentFeedbackDataDocument feedback(AdssvcRequestDocument document) throws Exception
    {
        AdssvcRequest saveRequest = document.getAdssvcRequest();
        OASLogger.getLogger("TestDelivery").debug(saveRequest.toString());

        StudentFeedbackDataDocument response = StudentFeedbackDataDocument.Factory.newInstance();
        StudentFeedbackData feedbackResponse = response.addNewStudentFeedbackData();
        
        String lsid = saveRequest.getGetFeedbackData().getLsid();
        int testRosterId = Integer.parseInt(lsid.substring(0, lsid.indexOf(":")));
        
        if(saveRequest.getGetFeedbackData().getLms() != null) {
            Sco [] scoa = saveRequest.getGetFeedbackData().getLms().getScoArray();
            for(int j=0;j<scoa.length;j++) {
                Sco sco = scoa[j];            
                noNamespace.AdssvcRequestDocument.AdssvcRequest.GetFeedbackData.Lms.Sco.Lsv [] lsva = sco.getLsvArray();
                if(lsva != null) {
                    int raw = -1;
                    int max = -1;
                    int unscored = -1;
                    for(int i=0;i<lsva.length;i++) {
                        // collect subtest score stuff here, put in SISS
                    	 noNamespace.AdssvcRequestDocument.AdssvcRequest.GetFeedbackData.Lms.Sco.Lsv lsv = lsva[i];
                        if(lsv.getCmiCore() != null) {
                            if(lsv.getCmiCore().getScoreRaw() != null) {
                                raw = lsv.getCmiCore().getScoreRaw().intValue();
                                max = lsv.getCmiCore().getScoreMax().intValue();
                            }
                        }
                        if(lsv.getExtCore() != null) {
                            unscored = lsv.getExtCore().getNumberOfUnscoredItems();
                        }
                    }
                    if(raw > -1 && max > -1 && unscored > -1) {
                        storeSubtestRawScore(testRosterId, Integer.parseInt(sco.getScid()), raw, max, unscored);
                    }
                }
            }
        }
        
        RosterSubtestFeedback [] feedback = saver.getSubtestFeedbackForRoster(new Integer(testRosterId));
        if(feedback != null && feedback.length > 0) {
            feedbackResponse.addNewTestingSessionData().setStudentName(feedback[0].getStudentName());
            feedbackResponse.addNewTitle().setId(String.valueOf(feedback[0].getTestId()));
            feedbackResponse.getTitle().setName(feedback[0].getTestTitle());
            feedbackResponse.setLsid(lsid);
            feedbackResponse.setStatus("OK");
            feedbackResponse.addNewLms();
            for(int i=0;i<feedback.length;i++) {
                feedbackResponse.getTitle().addNewSco().setId(String.valueOf(feedback[i].getSubtestId()));
                feedbackResponse.getTitle().getScoArray()[i].setTitle(feedback[i].getSubtestTitle());
                feedbackResponse.getTitle().getScoArray()[i].setSeq(String.valueOf(feedback[i].getSequence()));
                feedbackResponse.getLms().addNewSco().setScid(String.valueOf(feedback[i].getSubtestId()));
                feedbackResponse.getLms().getScoArray()[i].addNewLsv().addNewCmiCore().setScoreRaw(new BigDecimal(feedback[i].getRawScore()));
                feedbackResponse.getLms().getScoArray()[i].getLsv().getCmiCore().setScoreMax(new BigDecimal(feedback[i].getMaxScore()));
                feedbackResponse.getLms().getScoArray()[i].getLsv().addNewExtCore().setNumberOfUnscoredItems(new BigInteger(String.valueOf(feedback[i].getUnscored())));
            }
        } else {
            throw new InvalidTestRosterIdException();
        }
        return response;
    }

    /**
     * @common:operation
     */
    public AdssvcResponseDocument ctbSave(AdssvcRequestDocument document)
    {
        AdssvcRequest saveRequest = document.getAdssvcRequest();
        OASLogger.getLogger("TestDelivery").debug(saveRequest.toString());
        AdssvcResponseDocument response = AdssvcResponseDocument.Factory.newInstance();
        SaveTestingSessionData saveResponse = response.addNewAdssvcResponse().addNewSaveTestingSessionData();
        
        Tsd [] tsda = saveRequest.getSaveTestingSessionData().getTsdArray();
        if(tsda != null) {
            Tsd tsd = tsda[0];
            String lsid = tsd.getLsid();
            String testRosterId = "-1";
            String accessCode = null;
            if(lsid.indexOf(":") >= 0) {
                testRosterId = lsid.substring(0, lsid.indexOf(":"));
                accessCode = lsid.substring(lsid.indexOf(":")+1,lsid.length());
            }
            String itemSetId = tsd.getScid();
            int mSeq = tsd.getMseq().intValue();
            //ISTEP2010CR001 : For save speed value of the speech
            String ttsSpeedValue = tsd.getTtsSpeedValue();
            saveResponse.addNewTsd();
            saveResponse.getTsdArray(0).setLsid(lsid);
            saveResponse.getTsdArray(0).setScid(String.valueOf(itemSetId));
            saveResponse.getTsdArray(0).setMseq(tsd.getMseq());
            saveResponse.getTsdArray(0).setStatus(Status.OK);
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
                
                RosterSubtestStatus [] statusList = saver.getRosterSubtestStatus(Integer.parseInt(testRosterId));
                OASLogger.getLogger("TestDelivery").debug("message from tsd queue");
                if(statusList.length < 1) {
                    // couldn't find siss records for this roster id
                    OASLogger.getLogger("TestDelivery").debug("ctbSave: no SISS records for roster: " + testRosterId);
                    throw new InvalidTestRosterIdException();
                }

                int lastMseq = statusList[0].getLastMseq();                        
                if(tsd.getMseq().intValue() > lastMseq) {
                    OASLogger.getLogger("TestDelivery").debug("message from lms queue");
                    if (tsd.getCid() == null) 
                        throw new MissingCorrelationIdException();

                    // process Heartbeat
                    processHeartbeat(Integer.parseInt(testRosterId), tsd.getAstArray(), tsd.getMseq().intValue());
                    processItemResponseEvents(testRosterId, statusList, itemSetId, tsd.getIstArray(), mSeq, tsd.getCid().intValue(), true);
                    int nextSubtestId = processSubtestEvents(testRosterId, statusList, itemSetId, tsd.getLevArray(), tsd.getLsvArray(), tsd.getMseq().intValue(), accessCode);
                    processTestEvents(testRosterId, itemSetId, tsd.getLevArray(), tsd.getLsvArray(), statusList);
                    if (nextSubtestId >0) {
                        NextSco nextSco = saveResponse.getTsdArray(0).addNewNextSco();
                        nextSco.setId(""+nextSubtestId);
                    }
                } else {
                	OASLogger.getLogger("TestDelivery").info("Out of sequence message from lms queue, this mseq: " + tsd.getMseq().intValue() + " is less than last mseq: " + lastMseq);
                	Ist [] ista = tsd.getIstArray();
                	if(ista != null && ista.length > 0) {
                		OASLogger.getLogger("TestDelivery").info("Handling out of sequence item response message for roster: " + testRosterId);
                		if (tsd.getCid() == null) throw new MissingCorrelationIdException();
                		processItemResponseEvents(testRosterId, statusList, itemSetId, tsd.getIstArray(), mSeq, tsd.getCid().intValue(), true);
                	}	
                }
                
                //ISTEP2010CR001 : For to save value of tts speed
                if (ttsSpeedValue != null && !ttsSpeedValue.trim().equals("")) {                    
                    
                    String cachettsSpeedValue = (String) SimpleCache.checkCache(CACHE_TYPE_SPEECH, ""+testRosterId, ""+testRosterId);
                            
                    if (cachettsSpeedValue != null ) {
                        
                        if (!ttsSpeedValue.equals(cachettsSpeedValue)) {
                        
                            saver.updateStudentTTSspeedValue(new Integer(testRosterId),ttsSpeedValue);
                         
                        }
                         
                    } else {
                        
                        saver.updateStudentTTSspeedValue (new Integer(
                                        testRosterId),ttsSpeedValue);                        
                    }
                
                    SimpleCache.cacheResult(CACHE_TYPE_SPEECH, "" + testRosterId, ttsSpeedValue, ""+testRosterId);
                                OASLogger.getLogger("TestDelivery").debug("******* save to cache "+
                                        CACHE_TYPE_SPEECH+testRosterId);                                                  
                  
                }
                //end of change
                
                    
            } catch (InvalidTestRosterIdException itre) {
                handleError(tsd, saveResponse.getTsdArray(0), Status.INVALID_LSID, "invalid_lsid");
            } catch (InvalidItemSetIdException iise) {
                handleError(tsd, saveResponse.getTsdArray(0), Status.INVALID_SCID, "invalid_scid");
            } catch (InvalidSubtestEventException isee) {
                handleError(tsd, saveResponse.getTsdArray(0), Status.INVALID_LEV, "invalid_lev");
            } catch (InvalidItemResponseException iire) {
                handleError(tsd, saveResponse.getTsdArray(0), Status.INVALID_IST, "invalid_ist");
            } catch (MissingCorrelationIdException mcie) {
                handleError(tsd, saveResponse.getTsdArray(0), Status.INVALID_CID, "missing_cid");
            } catch (InvalidCorrelationIdException icie) {
                handleError(tsd, saveResponse.getTsdArray(0), Status.INVALID_CID, "invalid_cid");
            } catch (InvalidMseqException ime) {
                handleError(tsd, saveResponse.getTsdArray(0), Status.INVALID_MSEQ, "invalid_mseq");
            } catch (Exception tde) {
            	tde.printStackTrace();
                handleError(tsd, saveResponse.getTsdArray(0), Status.OTHER_ERROR, "other_error");
            }
            OASLogger.getLogger("TestDelivery").debug(saveResponse.getTsdArray(0).toString());
        }     
        return response;
    }
    
   /* private boolean duplicateResponseExists(int testRosterId, int mseq, Ist [] ista){
    	boolean result = false;
    	try{
	    	for(int j=0;j<ista.length;j++) {
	    		result = false;
	            Ist ist = ista[j];
	            if(ist != null && ist.getRvArray() != null && ist.getRvArray().length >0 ) {
	                if( ist.getRvArray(0).getVArray() != null && ist.getRvArray(0).getVArray().length >0){
	                    if(ist.getRvArray(0).getVArray(0) != null){
	                        BaseType.Enum responseType = ist.getRvArray(0).getT();
	                        String xmlResponse = ist.getRvArray(0).getVArray(0).xmlText();
	                        String response = "";
	                        String studentMarked = ist.getMrk() ? "T" : "F";
	                        if(xmlResponse != null && xmlResponse.length() > 0) {
	                            // strip xml
	                            int start = xmlResponse.indexOf(">");
	                            if(start >= 0) {
	                                response = xmlResponse.substring(start + 1);
	                                int end = response.lastIndexOf("</");
	                                if(end != -1)
	                                    response = response.substring(0, end);
	                            } else {
	                                response = xmlResponse;
	                            }
	                            // strip CDATA
	                            start = response.indexOf("[CDATA[");
	                            if(start >= 0) {
	                                response = response.substring(start + 7);
	                                int end = response.lastIndexOf("]]");
	                                if(end != -1)
	                                    response = response.substring(0, end);
	                            }
	                        }
	                        ItemResponseData ir = saver.getItemResponseForRosterAndMseq(testRosterId, mseq);
	                        if(ir.getItemId().equals(ist.getIid()) &&
	                           ((ir.getResponse() == null && response.equals("")) || ir.getResponse().equals(response)) &&
	                           ir.getResponseElapsedTime() == ist.getDur() ) {
	                        	result = true;
	                        }
	                     }
	                }else{ 
	                    String response = "";                   
	                    String studentMarked = ist.getMrk() ? "T" : "F";                    
	                    ItemResponseData ir = saver.getItemResponseForRosterAndMseq(testRosterId, mseq);
                        if(ir.getItemId().equals(ist.getIid()) &&
                           ir.getResponse() == null &&
                           ir.getResponseElapsedTime() == ist.getDur() ) {
                        	result = true;
                        }
	                }       
	            }
	    	}
    	} catch (SQLException se) {
    		OASLogger.getLogger("TestDelivery").error("Failure while testing for duplicate message for roster " + testRosterId + ", mseq " + mseq);
    	}
        return result;
    } */
    
    /**
     * @common:operation
     */
    public AdssvcResponseDocument ctbCompleteTutorial(AdssvcRequestDocument document)
    {
        AdssvcRequest saveRequest = document.getAdssvcRequest();
        OASLogger.getLogger("TestDelivery").debug(saveRequest.toString());
        AdssvcResponseDocument response = AdssvcResponseDocument.Factory.newInstance();
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
            
            StudentTutorialStatus  studentTutorialStatus = saver.getStudentTutorialStatus(Integer.parseInt(testRosterId));
            if (studentTutorialStatus.getCompletionStatus()==null)
                saver.storeStudentTutorialStatus(studentTutorialStatus.getProductId(), studentTutorialStatus.getStudentId(), "CO");
                
        } catch (InvalidTestRosterIdException itre) {
            saveResponse.setStatus(noNamespace.AdssvcResponseDocument.AdssvcResponse.CompleteTutorial.Status.INVALID_LSID);
        } catch (Exception tde) {
            saveResponse.setStatus(noNamespace.AdssvcResponseDocument.AdssvcResponse.CompleteTutorial.Status.OTHER_ERROR);
        }
        OASLogger.getLogger("TestDelivery").debug(saveResponse.toString());
        return response;
    }
    
    /**
     * @common:operation
     */
    public StudentFeedbackDataDocument ctbFeedback(AdssvcRequestDocument document) throws Exception
    {
        AdssvcRequest saveRequest = document.getAdssvcRequest();
        OASLogger.getLogger("TestDelivery").debug(saveRequest.toString());

        StudentFeedbackDataDocument response = StudentFeedbackDataDocument.Factory.newInstance();
        StudentFeedbackData feedbackResponse = response.addNewStudentFeedbackData();
        
        String lsid = saveRequest.getGetFeedbackData().getLsid();
        int testRosterId = Integer.parseInt(lsid.substring(0, lsid.indexOf(":")));
        
        RosterSubtestFeedback [] feedback = saver.getSubtestFeedbackForRoster(new Integer(testRosterId));
        if(feedback != null && feedback.length > 0) {
            feedbackResponse.addNewTestingSessionData().setStudentName(feedback[0].getStudentName());
            feedbackResponse.addNewTitle().setId(String.valueOf(feedback[0].getTestId()));
            feedbackResponse.getTitle().setName(feedback[0].getTestTitle());
            feedbackResponse.setLsid(lsid);
            feedbackResponse.setStatus("OK");
            feedbackResponse.addNewLms();
            for(int i=0;i<feedback.length;i++) {
                feedbackResponse.getTitle().addNewSco().setId(String.valueOf(feedback[i].getSubtestId()));
                feedbackResponse.getTitle().getScoArray()[i].setTitle(feedback[i].getSubtestTitle());
                feedbackResponse.getTitle().getScoArray()[i].setSeq(String.valueOf(feedback[i].getSequence()));
                feedbackResponse.getLms().addNewSco().setScid(String.valueOf(feedback[i].getSubtestId()));
                feedbackResponse.getLms().getScoArray()[i].addNewLsv().addNewCmiCore().setScoreRaw(new BigDecimal(feedback[i].getRawScore()));
                feedbackResponse.getLms().getScoArray()[i].getLsv().getCmiCore().setScoreMax(new BigDecimal(feedback[i].getMaxScore()));
                feedbackResponse.getLms().getScoArray()[i].getLsv().addNewExtCore().setNumberOfUnscoredItems(new BigInteger(String.valueOf(feedback[i].getUnscored())));
            }
        } else {
            throw new InvalidTestRosterIdException();
        }
        return response;
    }
    
    
    private void handleError(Tsd requestTsd, noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData.Tsd errorTsd, noNamespace.AdssvcResponseDocument.AdssvcResponse.SaveTestingSessionData.Tsd.Status.Enum status, String message) {
        errorTsd.setStatus(status);
        errorTsd.addNewError();
        errorTsd.getError().setMethod("save_testing_session_data");
        errorTsd.getError().setStatus(message);
        Ist [] istArray = requestTsd.getIstArray();
        for (int i=0; istArray != null && i < istArray.length; i++) {
            if (istArray[i] != null)
                istArray[i].setRvArray(new Rv[0]);
        }
        errorTsd.getError().setErrorElement(requestTsd.toString());
        OASLogger.getLogger("TestDelivery").info("TMS error: " + message);
    }
    
    
    private void processTestEvents(String testRosterId, String itemSetId, Lev [] leva, Lsv [] lsva,  RosterSubtestStatus [] statusList) throws TestDeliveryException, InvalidTestRosterIdException, InvalidItemSetIdException, InvalidSubtestEventException {
        if(leva != null) {
            for(int j=0;j<leva.length;j++) {
                Lev lev = leva[j];
                if (lev.getE().equals(LmsEventType.TERMINATED)) {
                    OASLogger.getLogger("TestDelivery").debug("handling tsd terminated message");
                    // send the scoring message
                    boolean allSubtestsComplete = true;
                    for(int i=0;i<statusList.length;i++) {
                        if(!statusList[i].getSubtestCompletionStatus().equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS)) {
                            allSubtestsComplete = false;
                        }
                    }
                    if(!allSubtestsComplete && isScorableItemSet(Integer.valueOf(itemSetId).intValue())) {
                        // old Weblogic 8.1 JMS call
                    	// scorer.sendObjectMessage(new Integer(testRosterId));
                    	try{
                    		// new Weblogic 10.3 JMS call
                    		invokeScoring(new Integer(testRosterId));
                    	} catch (Exception se) {
                    		OASLogger.getLogger("TestDelivery").info("TMS error: " + se.getMessage());
                		}
                    }
                }
            }
        }
    }
    
    private  int processSubtestEvents(String testRosterId, RosterSubtestStatus [] statusList, String itemSetId, Lev [] leva, Lsv [] lsva, int mseq, String accessCode) throws TestDeliveryException, InvalidTestRosterIdException, InvalidItemSetIdException, InvalidSubtestEventException {
        boolean timeout = false;
        int nextSubtestId = -1;
        if(lsva != null) {
            int raw = -1;
            int max = -1;
            int unscored = -1;
            for(int i=0;i<lsva.length;i++) {
                Lsv lsv = lsva[i];
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
                storeSubtestRawScore(Integer.parseInt(testRosterId), Integer.parseInt(itemSetId), raw, max, unscored);
            }
        }
        if(itemSetId.indexOf("TERMINATOR") >= 0) {
            if(leva != null) {
                for(int j=0;j<leva.length;j++) {
                    Lev lev = leva[j];
                    // ignore all subtest events for terminator sco
                    if (lev.getE().equals(LmsEventType.TERMINATED)) {
                        OASLogger.getLogger("TestDelivery").debug("handling lms terminated message");
                        stopTest(Integer.parseInt(testRosterId), statusList, mseq, -1);
                    } else if (lev.getE().equals(LmsEventType.SYS_DISCONNECT)) {
                        OASLogger.getLogger("TestDelivery").debug("handling lms sys_disconnect message");
                        interruptTest(Integer.parseInt(testRosterId), -1, true, mseq, statusList);
                    } else {
                        OASLogger.getLogger("TestDelivery").debug("ignoring lms " + lev.getE() + " message for TERMINATOR");
                    }
                }
            }
        } else if(itemSetId.indexOf("STUDENT_FEEDBACK") >= 0) {
            if(leva != null) {
                for(int j=0;j<leva.length;j++) {
                    Lev lev = leva[j];
                    // ignore all events for student feedback sco
                    if (lev.getE() != null && lev.getE().equals(LmsEventType.TERMINATED)) {
                        OASLogger.getLogger("TestDelivery").debug("handling lms terminated message");
                        stopTest(Integer.parseInt(testRosterId), statusList, mseq, -1);
                    } else if (lev.getE().equals(LmsEventType.SYS_DISCONNECT)) {
                        OASLogger.getLogger("TestDelivery").debug("handling lms sys_disconnect message");
                        //call stopTest for this case
                        //When TMS gets a disconnect message for "STUDENT_FEEDBACK" sco, it should set roster status to "CO".
                        //interruptTest(Integer.parseInt(testRosterId), -1, lev.getEdt().getTimeInMillis(), true, mseq);
                        stopTest(Integer.parseInt(testRosterId), statusList, mseq, -1);
                    } else {
                        OASLogger.getLogger("TestDelivery").debug("ignoring lms " + lev.getE() + " message for STUDENT_FEEDBACK");
                    }
                }
            }
        } else if(itemSetId.indexOf("TEST_SESSION") >= 0) {
            if(leva != null) {
                for(int j=0;j<leva.length;j++) {
                    Lev lev = leva[j];
                    // ignore all events for generic sco
                    if (lev.getE() != null && lev.getE().equals(LmsEventType.TERMINATED)) {
                        OASLogger.getLogger("TestDelivery").debug("handling lms terminated message");
                        stopTest(Integer.parseInt(testRosterId), statusList, mseq, -1);
                    } else if (lev.getE().equals(LmsEventType.SYS_DISCONNECT)) {
                        OASLogger.getLogger("TestDelivery").debug("handling lms sys_disconnect message");
                        interruptTest(Integer.parseInt(testRosterId), -1, true, mseq, statusList);
                    } else {
                        OASLogger.getLogger("TestDelivery").debug("ignoring lms " + lev.getE() + " message for TEST_SESSION");
                    }                
                }
            }
        } else if(leva != null) {
            for(int j=0;j<leva.length;j++) {
                Lev lev = leva[j];
                if (lev.getE().equals(LmsEventType.LMS_INITIALIZE)) {
                    OASLogger.getLogger("TestDelivery").debug("handling lms initialize message");
                    startSubtest(Integer.parseInt(testRosterId), statusList, Integer.parseInt(itemSetId), mseq);
                } else if (lev.getE().equals(LmsEventType.LMS_FINISH)) {
                    OASLogger.getLogger("TestDelivery").debug("handling lms finish message");
                    nextSubtestId = stopSubtest(Integer.parseInt(testRosterId), statusList, Integer.parseInt(itemSetId), timeout, mseq, accessCode);
                } else if (lev.getE().equals(LmsEventType.TERMINATED)) {
                    OASLogger.getLogger("TestDelivery").debug("handling lms terminated message");
                    stopTest(Integer.parseInt(testRosterId), statusList, mseq, Integer.parseInt(itemSetId));
                } else if (lev.getE().equals(LmsEventType.STU_STOP)) {
                    OASLogger.getLogger("TestDelivery").debug("handling lms stu_stop message");
                    stopTest(Integer.parseInt(testRosterId), statusList, mseq, Integer.parseInt(itemSetId));
                } else if (lev.getE().equals(LmsEventType.STU_PAUSE)) {
                    OASLogger.getLogger("TestDelivery").debug("handling lms stu_pause message");
                    interruptTest(Integer.parseInt(testRosterId), Integer.parseInt(itemSetId), false, mseq, statusList);
                } else if (lev.getE().equals(LmsEventType.STU_RESUME)) {
                    OASLogger.getLogger("TestDelivery").debug("handling lms stu_resume message");
                    restartTest(Integer.parseInt(testRosterId), Integer.parseInt(itemSetId), mseq);
                } else if (lev.getE().equals(LmsEventType.SYS_DISCONNECT)) {
                    OASLogger.getLogger("TestDelivery").debug("handling lms sys_disconnect message");
                    interruptTest(Integer.parseInt(testRosterId), Integer.parseInt(itemSetId), true, mseq, statusList);
                } else {
                    OASLogger.getLogger("TestDelivery").debug("unknown event type from lms queue: " + lev.getE().toString());
                    throw new TestDeliveryException();
                }
            }
        }
        return nextSubtestId;
    }
    
    private void processItemResponseEvents(String testRosterId, RosterSubtestStatus [] statusList, String itemSetId, Ist [] ista, int mSeq, int cid, boolean isCTB) throws InvalidTestRosterIdException, InvalidItemSetIdException, InvalidItemResponseException, InvalidSubtestEventException, InvalidCorrelationIdException {
        String cacheArg = null;
    	if(ista != null && ista.length > 0) {
            if(isCTB) {
                //validate correlation
                Integer cidInRoster = saver.getCorrelationIdForRoster(Integer.valueOf(testRosterId));
                
                if (cidInRoster == null)
                    saver.setCorrelationIdForRoster(Integer.valueOf(testRosterId), new Integer(cid));
                else if (cidInRoster.intValue() != cid)
                    throw new InvalidCorrelationIdException();
                
                // must check subtest status now that response events originate from client, to prevent spoofing
                boolean foundItemSet = false;
                for(int i=0;i<statusList.length;i++) {
                    if(statusList[i].getItemSetId() == Integer.parseInt(itemSetId)) {
                         foundItemSet = true;
                         if(!(statusList[i].getTestCompletionStatus().equals(Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS) ||
                              statusList[i].getTestCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS))) {
                            // this subtest is not in progress, can't persist responses
                            OASLogger.getLogger("TestDelivery").debug("startSubtest: no scheduled SISS records for roster: " + testRosterId);
                            throw new InvalidSubtestEventException();
                        }
                    }
                }
                if(!foundItemSet) {
                    // couldn't find siss record for this item set id, student is not scheduled for specified subtest
                    OASLogger.getLogger("TestDelivery").debug("startSubtest: specified subtest (" + itemSetId + ") not in manifest for roster " + testRosterId);
                    throw new InvalidItemSetIdException();
                }
            }

            for(int j=0;j<ista.length;j++) {
                Ist ist = ista[j];
             //   if(ist != null && ist.getRvArray(0) != null && ist.getRvArray(0).getVArray(0) != null) {
                if(ist != null && ist.getRvArray() != null && ist.getRvArray().length >0 ) {
                    if( ist.getRvArray(0).getVArray() != null && ist.getRvArray(0).getVArray().length >0){
                        if(ist.getRvArray(0).getVArray(0) != null){
                            BaseType.Enum responseType = ist.getRvArray(0).getT();
                            String xmlResponse = ist.getRvArray(0).getVArray(0).xmlText();
                            String response = "";
                            String studentMarked = ist.getMrk() ? "T" : "F";
                            if(xmlResponse != null && xmlResponse.length() > 0) {
                                // strip xml
                                int start = xmlResponse.indexOf(">");
                                if(start >= 0) {
                                    response = xmlResponse.substring(start + 1);
                                    int end = response.lastIndexOf("</");
                                    if(end != -1)
                                        response = response.substring(0, end);
                                } else {
                                    response = xmlResponse;
                                }
                                // strip CDATA
                                start = response.indexOf("[CDATA[");
                                if(start >= 0) {
                                    response = response.substring(start + 7);
                                    int end = response.lastIndexOf("]]");
                                    if(end != -1)
                                        response = response.substring(0, end);
                                }
                            }
                            if(responseType.equals(BaseType.IDENTIFIER)) {
                                storeResponse(Integer.parseInt(testRosterId), Integer.parseInt(itemSetId), ist.getIid(), response, ist.getDur(), null, mSeq, isCTB, studentMarked);
                                cacheArg = testRosterId + ":" + itemSetId + ":" + ist.getIid() + ":" + response + ":" + ist.getDur() + ":" + null + ":" + mSeq + ":" + studentMarked;
                            } else if(responseType.equals(BaseType.STRING)) {
                                storeCRResponse(Integer.parseInt(testRosterId), Integer.parseInt(itemSetId), ist.getIid(), response, ist.getDur(), null, mSeq, isCTB, studentMarked);
                                cacheArg = testRosterId + ":" + itemSetId + ":" + ist.getIid() + ":" + null + ":" + ist.getDur() + ":" + null + ":" + mSeq + ":" + studentMarked;
                            }
                         }
                    }else{ 
                        String response = "";                   
                        String studentMarked = ist.getMrk() ? "T" : "F";                    
                        storeResponse(Integer.parseInt(testRosterId), Integer.parseInt(itemSetId), ist.getIid(), response, ist.getDur(), null, mSeq, isCTB, studentMarked);                                          
                    }       
                }
                
                if (ist != null 
                                && ist.getIscArray() != null 
                                && ist.getIscArray().length > 0 ) {
                    /*
                     * save scratchpad content
                     * for loop is added just to keep it safe.
                     */
                    String sp = null;
                    for (int i = 0; i < ist.getIscArray().length; i++) {
                        sp = ist.getIscArray()[i].getSp();
                        if (sp != null) {
                            int start = sp.indexOf("[CDATA[");
                            if (start >= 0) {
                                sp = sp.substring(start + 7);
                                int end = sp.lastIndexOf("]]");
                                if (end != -1)
                                    sp = sp.substring(0, end);
                            }
                        }
                    }
                    if(sp != null && !"".equals(sp.trim())) {
                    	//Object priorResponse = null;
                        //if(cacheArg != null) {
                        //	priorResponse = SimpleCache.checkCache(TMS_PER_INSTANCE_SP_CHECK, cacheArg, testRosterId); 
                        //}
                        //if(priorResponse == null) {
                        	storeScratchpadContent(Integer.parseInt(testRosterId), Integer.parseInt(itemSetId), sp);
                        //	SimpleCache.cacheResult(TMS_PER_INSTANCE_SP_CHECK, cacheArg, cacheArg, testRosterId);
                        //}
                    }

                }
            }
        }
    }
    
    private void startSubtest(int testRosterId, RosterSubtestStatus [] statusList, int itemSetId, int mSeq) throws TestDeliveryException, InvalidTestRosterIdException, InvalidItemSetIdException, InvalidSubtestEventException {
        try {
            boolean foundItemSet = false;
            for(int i=0;i<statusList.length;i++) {
                if(statusList[i].getItemSetId() == itemSetId) {
                     foundItemSet = true;
                     if(!statusList[i].getSubtestCompletionStatus().equals(Constants.StudentTestCompletionStatus.SCHEDULED_STATUS) &&
                        !statusList[i].getSubtestCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS) &&
                        !statusList[i].getSubtestCompletionStatus().equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS)) {
                        // this subtest is not in a startable state
                        OASLogger.getLogger("TestDelivery").debug("startSubtest: no scheduled SISS records for roster: " + testRosterId);
                        throw new InvalidSubtestEventException();
                    }
                }
            }
            if(!foundItemSet) {
                // couldn't find siss record for this item set id
                OASLogger.getLogger("TestDelivery").debug("startSubtest: specified subtest (" + itemSetId + ") not in manifest for roster " + testRosterId);
                throw new InvalidItemSetIdException();
            }
            // request looks good, start the subtest
            OASLogger.getLogger("TestDelivery").debug("startSubtest: starting subtest (" + itemSetId + ") for roster " + testRosterId);
            saver.startSubtest(testRosterId, itemSetId, new Date());
            
            //also update test roster here.
            saver.updateTestRosterTimeStampWithMseq(testRosterId, new Date(), mSeq);
        } catch (SQLException se) {
            // unknown problem, throw generic invalid request exception
            // TO-DO: this is probably inadequate, need more informative error
            se.printStackTrace();
            throw new TestDeliveryException();
        }
    }
    
    private int stopSubtest(int testRosterId, RosterSubtestStatus [] statusList, int itemSetId, boolean timeout, int mSeq, String accessCode) throws TestDeliveryException, InvalidTestRosterIdException, InvalidItemSetIdException, InvalidSubtestEventException {
        try {
            boolean foundItemSet = false;
            boolean allSubtestsComplete = true;
            for(int i=0;i<statusList.length;i++) {
                if(statusList[i].getItemSetId() == itemSetId) {
                     foundItemSet = true;
                     if(!statusList[i].getSubtestCompletionStatus().equals(Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS)) {
                        // this subtest is not in a stoppable state
                        OASLogger.getLogger("TestDelivery").debug("stopSubtest: specified subtest (" + itemSetId + ") not in progress for roster " + testRosterId);
                        throw new InvalidSubtestEventException();
                    } else {
                       statusList[i].setSubtestCompletionStatus(Constants.StudentTestCompletionStatus.COMPLETED_STATUS); 
                    }
                }
                if(!statusList[i].getSubtestCompletionStatus().equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS)) {
                    allSubtestsComplete = false;
                }
            }
            if(!foundItemSet) {
                // couldn't find siss record for this item set id
                OASLogger.getLogger("TestDelivery").debug("stopSubtest: specified subtest (" + itemSetId + ") not in manifest for roster " + testRosterId);
                throw new InvalidItemSetIdException();
            }
            
            // request looks good, stop the subtest
            OASLogger.getLogger("TestDelivery").debug("stopSubtest: stopping subtest (" + itemSetId + ") for roster " + testRosterId);
            saver.stopSubtest(testRosterId, itemSetId, new Date(), timeout ? "T" : "F");

            handleTabeLocator(testRosterId, itemSetId);
            int nextId = getNextSubtestId(testRosterId, itemSetId, accessCode);
            
            //also update test roster here.
            saver.updateTestRosterTimeStampWithMseq(testRosterId, new Date(), mSeq);
            
            // if all subtests are complete, score the roster
            if(allSubtestsComplete && isScorableItemSet(itemSetId)) {
                // old Weblogic 8.1 JMS call
            	//scorer.sendObjectMessage(new Integer(testRosterId));
            	try{
            		// new Weblogic 10.3 JMS call
            		invokeScoring(new Integer(testRosterId));
            	} catch (Exception se) {
            		OASLogger.getLogger("TestDelivery").info("TMS error: " + se.getMessage());
        		}
            }
            try {
            	SimpleCache.clearUserCache(String.valueOf(testRosterId));
            } catch (Exception e) {
            	OASLogger.getLogger("TestDelivery").info("Error clearing dupe cache for roster " + testRosterId + ". Message: " + e.getMessage());
            }
            return nextId;
        } catch (SQLException se) {
            // unknown problem, throw generic invalid request exception
            // TO-DO: this is probably inadequate, need more informative error
            se.printStackTrace();
            throw new TestDeliveryException();
        }
    }
    
    private static boolean isScorableItemSet(int itemSetId) {
    	return
    	itemSetId == 272434 ||
    	itemSetId == 272443 ||
    	itemSetId == 272452 ||
    	itemSetId == 272461 ||
    	itemSetId == 272470 ||
    	itemSetId == 272479 ||
    	itemSetId == 272488 ||
    	itemSetId == 272498 ||
    	itemSetId == 272507 ||
    	itemSetId == 272516 ||
    	itemSetId == 272525 ||
    	itemSetId == 272534 ||
    	itemSetId == 272543 ||
    	itemSetId == 272552 ||
    	itemSetId == 272436 ||
    	itemSetId == 272445 ||
    	itemSetId == 272454 ||
    	itemSetId == 272463 ||
    	itemSetId == 272472 ||
    	itemSetId == 272481 ||
    	itemSetId == 272490 ||
    	itemSetId == 272500 ||
    	itemSetId == 272509 ||
    	itemSetId == 272518 ||
    	itemSetId == 272527 ||
    	itemSetId == 272536 ||
    	itemSetId == 272545 ||
    	itemSetId == 272554 ||
    	itemSetId == 272440 ||
    	itemSetId == 272449 ||
    	itemSetId == 272458 ||
    	itemSetId == 272467 ||
    	itemSetId == 272476 ||
    	itemSetId == 272485 ||
    	itemSetId == 272494 ||
    	itemSetId == 272504 ||
    	itemSetId == 272513 ||
    	itemSetId == 272522 ||
    	itemSetId == 272531 ||
    	itemSetId == 272540 ||
    	itemSetId == 272549 ||
    	itemSetId == 272558 ||
    	itemSetId == 272438 ||
    	itemSetId == 272447 ||
    	itemSetId == 272456 ||
    	itemSetId == 272465 ||
    	itemSetId == 272474 ||
    	itemSetId == 272483 ||
    	itemSetId == 272492 ||
    	itemSetId == 272502 ||
    	itemSetId == 272511 ||
    	itemSetId == 272520 ||
    	itemSetId == 272529 ||
    	itemSetId == 272538 ||
    	itemSetId == 272547 ||
    	itemSetId == 272556 ||
    	itemSetId == 272306 ||
    	itemSetId == 272315 ||
    	itemSetId == 272324 ||
    	itemSetId == 272333 ||
    	itemSetId == 272342 ||
    	itemSetId == 272351 ||
    	itemSetId == 272360 ||
    	itemSetId == 272370 ||
    	itemSetId == 272379 ||
    	itemSetId == 272388 ||
    	itemSetId == 272397 ||
    	itemSetId == 272406 ||
    	itemSetId == 272415 ||
    	itemSetId == 272424 ||
    	itemSetId == 272308 ||
    	itemSetId == 272317 ||
    	itemSetId == 272326 ||
    	itemSetId == 272335 ||
    	itemSetId == 272344 ||
    	itemSetId == 272353 ||
    	itemSetId == 272362 ||
    	itemSetId == 272372 ||
    	itemSetId == 272381 ||
    	itemSetId == 272390 ||
    	itemSetId == 272399 ||
    	itemSetId == 272408 ||
    	itemSetId == 272417 ||
    	itemSetId == 272426 ||
    	itemSetId == 272310 ||
    	itemSetId == 272319 ||
    	itemSetId == 272328 ||
    	itemSetId == 272337 ||
    	itemSetId == 272346 ||
    	itemSetId == 272355 ||
    	itemSetId == 272364 ||
    	itemSetId == 272374 ||
    	itemSetId == 272383 ||
    	itemSetId == 272392 ||
    	itemSetId == 272401 ||
    	itemSetId == 272410 ||
    	itemSetId == 272419 ||
    	itemSetId == 272428 ||
    	itemSetId == 272312 ||
    	itemSetId == 272321 ||
    	itemSetId == 272330 ||
    	itemSetId == 272339 ||
    	itemSetId == 272348 ||
    	itemSetId == 272357 ||
    	itemSetId == 272366 ||
    	itemSetId == 272376 ||
    	itemSetId == 272385 ||
    	itemSetId == 272394 ||
    	itemSetId == 272403 ||
    	itemSetId == 272412 ||
    	itemSetId == 272421 ||
    	itemSetId == 272430 ||
    	itemSetId == 273982 ||
    	itemSetId == 273967 ||
    	itemSetId == 273969 ||
    	itemSetId == 273984 ||
    	itemSetId == 273971 ||
    	itemSetId == 273986 ||
    	itemSetId == 273988 ||
    	itemSetId == 273973 ||
    	itemSetId == 272223 ||
    	itemSetId == 272225 ||
    	itemSetId == 272227 ||
    	itemSetId == 272229 ||
    	itemSetId == 273904 ||
    	itemSetId == 273914 ||
    	itemSetId == 273917 ||
    	itemSetId == 273919 ||
    	itemSetId == 273908 ||
    	itemSetId == 273921 ||
    	itemSetId == 273924 ||
    	itemSetId == 273926 ||
    	itemSetId == 273890 ||
    	itemSetId == 273896 ||
    	itemSetId == 273899 ||
    	itemSetId == 273941 ||
    	itemSetId == 273912 ||
    	itemSetId == 273928 ||
    	itemSetId == 273931 ||
    	itemSetId == 273933 ||
    	itemSetId == 273894 ||
    	itemSetId == 273943 ||
    	itemSetId == 273946 ||
    	itemSetId == 273948 ||
    	itemSetId == 273952 ||
    	itemSetId == 273954 ||
    	itemSetId == 273957 ||
    	itemSetId == 273959 ||
    	itemSetId == 273937 ||
    	itemSetId == 273939 ||
    	itemSetId == 273962 ||
    	itemSetId == 273964 ||
    	itemSetId == 272222 ||
    	itemSetId == 272224 ||
    	itemSetId == 272226 ||
    	itemSetId == 272228 ||
    	itemSetId == 272305 ||
    	itemSetId == 272307 ||
    	itemSetId == 272309 ||
    	itemSetId == 272311 ||
    	itemSetId == 272314 ||
    	itemSetId == 272316 ||
    	itemSetId == 272318 ||
    	itemSetId == 272320 ||
    	itemSetId == 272323 ||
    	itemSetId == 272325 ||
    	itemSetId == 272327 ||
    	itemSetId == 272329 ||
    	itemSetId == 272332 ||
    	itemSetId == 272334 ||
    	itemSetId == 272336 ||
    	itemSetId == 272338 ||
    	itemSetId == 272341 ||
    	itemSetId == 272343 ||
    	itemSetId == 272345 ||
    	itemSetId == 272347 ||
    	itemSetId == 272350 ||
    	itemSetId == 272352 ||
    	itemSetId == 272354 ||
    	itemSetId == 272356 ||
    	itemSetId == 272359 ||
    	itemSetId == 272361 ||
    	itemSetId == 272363 ||
    	itemSetId == 272365 ||
    	itemSetId == 272369 ||
    	itemSetId == 272371 ||
    	itemSetId == 272373 ||
    	itemSetId == 272375 ||
    	itemSetId == 272378 ||
    	itemSetId == 272380 ||
    	itemSetId == 272382 ||
    	itemSetId == 272384 ||
    	itemSetId == 272387 ||
    	itemSetId == 272389 ||
    	itemSetId == 272391 ||
    	itemSetId == 272393 ||
    	itemSetId == 272396 ||
    	itemSetId == 272398 ||
    	itemSetId == 272400 ||
    	itemSetId == 272402 ||
    	itemSetId == 272405 ||
    	itemSetId == 272407 ||
    	itemSetId == 272409 ||
    	itemSetId == 272411 ||
    	itemSetId == 272414 ||
    	itemSetId == 272416 ||
    	itemSetId == 272418 ||
    	itemSetId == 272420 ||
    	itemSetId == 272423 ||
    	itemSetId == 272425 ||
    	itemSetId == 272427 ||
    	itemSetId == 272429 ||
    	itemSetId == 272433 ||
    	itemSetId == 272435 ||
    	itemSetId == 272437 ||
    	itemSetId == 272439 ||
    	itemSetId == 272442 ||
    	itemSetId == 272444 ||
    	itemSetId == 272446 ||
    	itemSetId == 272448 ||
    	itemSetId == 272451 ||
    	itemSetId == 272453 ||
    	itemSetId == 272455 ||
    	itemSetId == 272457 ||
    	itemSetId == 272460 ||
    	itemSetId == 272462 ||
    	itemSetId == 272464 ||
    	itemSetId == 272466 ||
    	itemSetId == 272469 ||
    	itemSetId == 272471 ||
    	itemSetId == 272473 ||
    	itemSetId == 272475 ||
    	itemSetId == 272478 ||
    	itemSetId == 272480 ||
    	itemSetId == 272482 ||
    	itemSetId == 272484 ||
    	itemSetId == 272487 ||
    	itemSetId == 272489 ||
    	itemSetId == 272491 ||
    	itemSetId == 272493 ||
    	itemSetId == 272497 ||
    	itemSetId == 272499 ||
    	itemSetId == 272501 ||
    	itemSetId == 272503 ||
    	itemSetId == 272506 ||
    	itemSetId == 272508 ||
    	itemSetId == 272510 ||
    	itemSetId == 272512 ||
    	itemSetId == 272515 ||
    	itemSetId == 272517 ||
    	itemSetId == 272519 ||
    	itemSetId == 272521 ||
    	itemSetId == 272524 ||
    	itemSetId == 272526 ||
    	itemSetId == 272528 ||
    	itemSetId == 272530 ||
    	itemSetId == 272533 ||
    	itemSetId == 272535 ||
    	itemSetId == 272537 ||
    	itemSetId == 272539 ||
    	itemSetId == 272542 ||
    	itemSetId == 272544 ||
    	itemSetId == 272546 ||
    	itemSetId == 272548 ||
    	itemSetId == 272551 ||
    	itemSetId == 272553 ||
    	itemSetId == 272555 ||
    	itemSetId == 272557 ||
    	itemSetId == 272592 ||
    	itemSetId == 273889 ||
    	itemSetId == 273893 ||
    	itemSetId == 273898 ||
    	itemSetId == 273903 ||
    	itemSetId == 273907 ||
    	itemSetId == 273911 ||
    	itemSetId == 273916 ||
    	itemSetId == 273923 ||
    	itemSetId == 273930 ||
    	itemSetId == 273936 ||
    	itemSetId == 273945 ||
    	itemSetId == 273951 ||
    	itemSetId == 273956 ||
    	itemSetId == 273961;
    }
    
    private void invokeScoring(Integer testRosterId) throws Exception 
    {
		getResourceValue();
	    InitialContext ic = QueueSend.getInitialContext(jndiFactory,jmsURL,jmsPrincipal,jmsCredentials);
	    QueueSend qs = new QueueSend();
	    qs.init(ic, jmsFactory, jmsQueue);
	    qs.readAndSend(qs,testRosterId);
	    qs.close();
	    ic.close();
	  }
    
    private void getResourceValue() throws Exception {
	    ResourceBundle rb = ResourceBundle.getBundle("security");
	    jndiFactory = rb.getString("jndiFactory");
	    jmsFactory = rb.getString("jmsFactory");
	    jmsURL = rb.getString("jmsURL");
	    jmsQueue = rb.getString("jmsQueue");
	    jmsPrincipal = rb.getString("jmsPrincipal");
	    jmsCredentials = rb.getString("jmsCredentials");
    }
    
    private void handleTabeLocator(int testRosterId, int itemSetId) throws SQLException {
        RosterElement roster = testRoster.getRosterElement(new Integer(testRosterId));
        TestProduct testProduct = product.getProductForTestAdmin(roster.getTestAdminId());
        boolean isTabe = false;
        if ("TB".equals(testProduct.getProductType()) 
            || "TL".equals(testProduct.getProductType()))
            isTabe = true;
        if (isTabe) {
            RosterSubtestStatus [] locatorSubtests = saver.getLocatorRosterSubtestStatus(testRosterId);
            boolean hasLocator = false;
            if (locatorSubtests!= null && locatorSubtests.length >0)
                hasLocator = true;
            if (hasLocator) {
                boolean foundInLocator=false;
                for (int i =0; i < locatorSubtests.length && !foundInLocator; i++)
                {
                    if ( locatorSubtests[i].getItemSetId() == itemSetId) {
                        foundInLocator = true;
                        List recommendedList = TabeLocatorUtils.calculateRecommendSubtestLevel(locatorSubtests);
                        Iterator iterator = recommendedList.iterator();
                        while (iterator.hasNext()) {
                            RecommendedSubtestLevel recommended = (RecommendedSubtestLevel) iterator.next();
                            saver.updateRecommendedLevelForSubtestCompletionStatus(testRosterId, recommended.getItemSetId(), recommended.getRecommendedLevel());
                            saver.deleteUnrecommendedSubtests(testRosterId, recommended.getRecommendedItemSetName()+"%", recommended.getRecommendedLevel());
                        }
                    }
                        
                }
            }
        }
        
    }
    
    private int getNextSubtestId(int testRosterId, int itemSetId, String accessCode) throws SQLException {
        ManifestData [] manifestData = authenticator.getManifest(testRosterId, accessCode);
        for (int i = 0; i<manifestData.length; i++) {
            if (manifestData[i].getId() == itemSetId) {
            	int j = i+1;
                while(j < manifestData.length){
                	if(!"CO".equals(manifestData[j].getCompletionStatus()))
	                        return manifestData[j].getId();
                	j++;
            	}
                return -1;
            }
        }
        return -1;
    }
    
    
    private void stopTest(int testRosterId, RosterSubtestStatus [] statusList, int mseq, int itemSetId) throws TestDeliveryException, InvalidTestRosterIdException, InvalidSubtestEventException {
        try {
            String status = Constants.StudentTestCompletionStatus.COMPLETED_STATUS;
            for(int i=0;i<statusList.length;i++) {
                if(!statusList[i].getSubtestCompletionStatus().equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS)) {
                    // student stopped test
                    status = Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS;
                }
            }
            // request looks good, stop the test
            OASLogger.getLogger("TestDelivery").debug("stopTest: stopping Test with new status: " + status + " for roster " + testRosterId);
            saver.stopTest(testRosterId, new Date(), status, mseq);
            if(itemSetId != -1) {
                saver.updateSubtestCompletionStatus(testRosterId, itemSetId, status, Constants.StudentTestCompletionStatus.COMPLETED_STATUS);
            }
            
        } catch (SQLException se) {
            // unknown problem, throw generic invalid request exception
            // TO-DO: this is probably inadequate, need more informative error
            se.printStackTrace();
            throw new TestDeliveryException();
        }
    }
    
    private void restartTest(int testRosterId, int itemSetId, int mseq) throws TestDeliveryException, InvalidTestRosterIdException, InvalidSubtestEventException {
        try {
            RosterSubtestStatus [] statusList = saver.getRosterSubtestStatus(testRosterId);
            if(statusList.length < 1) {
                // couldn't find siss records for this roster id
                OASLogger.getLogger("TestDelivery").debug("restartTest: no SISS records for roster: " + testRosterId);
                throw new InvalidTestRosterIdException();
            }
            boolean validRestart = false;
            for(int i=0;i<statusList.length;i++) {
                String status = statusList[i].getTestCompletionStatus();
                if(!(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS.equals(status) || 
                     Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS.equals(status) ||
                     Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS.equals(status))) {
                    OASLogger.getLogger("TestDelivery").debug("restartTest: roster status is not IN, IS, or SP for roster " + testRosterId);
                    throw new TestDeliveryException();
                }
                if(!statusList[i].getSubtestCompletionStatus().equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS)) {
                    // test isn't finished
                    validRestart = true;
                }
                if(statusList[i].getItemSetId() == itemSetId) {
                    OASLogger.getLogger("TestDelivery").debug("restartTest: restarting subtest " + itemSetId + " only with new status: " + Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS + " for roster " + testRosterId);
                    saver.updateSubtestCompletionStatus(testRosterId, itemSetId, Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS, Constants.StudentTestCompletionStatus.COMPLETED_STATUS);
                }
            }
            if(!validRestart) {
                OASLogger.getLogger("TestDelivery").debug("restartTest: all subtests already completed for roster " + testRosterId);
                throw new TestDeliveryException();
            }
            // request looks good, restart the test
            saver.setRosterCompletionStatus(testRosterId, Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS, new Date(), mseq);
        } catch (SQLException se) {
            // unknown problem, throw generic invalid request exception
            // TO-DO: this is probably inadequate, need more informative error
            se.printStackTrace();
            throw new TestDeliveryException();
        }
    }
    
    private void interruptTest(int testRosterId, int itemSetId, boolean systemStop, int mseq, RosterSubtestStatus [] statusList) throws TestDeliveryException, InvalidTestRosterIdException, InvalidSubtestEventException {
        try {
            if(systemStop) {
                // network interruption
                String status = saver.getRosterCompletionStatus(testRosterId);
                String adminStatus = saver.getTestAdminStatus(testRosterId);
                if(!Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS.equals(status)) {
                    OASLogger.getLogger("TestDelivery").debug("interruptTest: interrupting Test with new status: " + Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS + " for roster " + testRosterId);
                    if(itemSetId != -1) {
                        saver.updateSubtestCompletionStatus(testRosterId, itemSetId, Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS, Constants.StudentTestCompletionStatus.COMPLETED_STATUS);
                    }
                    if(!Constants.TestAdminStatus.PAST.equals(adminStatus)) {
                        //check to see if all subtest completed
                        //if true, set roster status as COMPLETED_STATUS
                        //if not, set roster status as SYSTEM_STOP_STATUS
                        String newStatus = Constants.StudentTestCompletionStatus.COMPLETED_STATUS;
                        for(int i=0;i<statusList.length;i++) {
                            if(!statusList[i].getSubtestCompletionStatus().equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS)) {
                                // student stopped test
                                newStatus = Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS;
                            }
                        }
                        OASLogger.getLogger("TestDelivery").debug("interruptTest: update test roster with new status: " + newStatus + " for roster " + testRosterId);
                        saver.setRosterCompletionStatus(testRosterId, newStatus, new Date(), mseq);
                        // score student if they're interrupted after completing all subtests (eg. on feedback screen)
                        if(Constants.StudentTestCompletionStatus.COMPLETED_STATUS.equals(newStatus)) {
                            // old Weblogic 8.1 JMS call
                        	// scorer.sendObjectMessage(new Integer(testRosterId));
                        	try{
                        		// new Weblogic 10.3 JMS call
                        		invokeScoring(new Integer(testRosterId));
                        	} catch (Exception se) {
                        		OASLogger.getLogger("TestDelivery").info("TMS error: " + se.getMessage());
                    		}
                        }
                    } else {
                        saver.setRosterCompletionStatus(testRosterId, Constants.StudentTestCompletionStatus.INCOMPLETE_STATUS, new Date(), mseq);
                    }
                } else {
                    OASLogger.getLogger("TestDelivery").debug("interruptTest: NOT interrupting test, leaving current status: " + status + " for roster " + testRosterId);
                }
            } else {
                // pause, only update subtest status
                OASLogger.getLogger("TestDelivery").debug("interruptTest: interrupting subtest " + itemSetId + " only with new status: " + Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS + " for roster " + testRosterId);
                if(itemSetId != -1) {
                    saver.updateSubtestCompletionStatus(testRosterId, itemSetId, Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS, Constants.StudentTestCompletionStatus.COMPLETED_STATUS);
                }
                saver.setRosterCompletionStatus(testRosterId, Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS, new Date(), mseq);
            }
        } catch (SQLException se) {
            // unknown problem, throw generic invalid request exception
            // TO-DO: this is probably inadequate, need more informative error
            se.printStackTrace();
            throw new TestDeliveryException();
        }
    }
    
    private void storeResponse(int testRosterId, int itemSetId, String itemId, String response, float elapsedTime, String answerChoiceId, int mSeq, boolean isCTB, String studentMarked) throws InvalidTestRosterIdException, InvalidItemSetIdException, InvalidItemResponseException {
        try {
            if (isCTB) {
            	//String cacheArg = testRosterId + ":" + itemSetId + ":" + itemId + ":" + response + ":" + elapsedTime + ":" + answerChoiceId + ":" + mSeq + ":" + studentMarked;
                //Object priorResponse = SimpleCache.checkCache(TMS_PER_INSTANCE_DUPE_CHECK, cacheArg, String.valueOf(testRosterId));
                //if(priorResponse == null) {
                	saver.storeResponseWithMseq(testRosterId, itemSetId, itemId, response, elapsedTime, answerChoiceId, mSeq, studentMarked);
                //	SimpleCache.cacheResult(TMS_PER_INSTANCE_DUPE_CHECK, cacheArg, cacheArg, String.valueOf(testRosterId));
                //} else {
               // 	OASLogger.getLogger("TestDelivery").info("Found duplicate message in per-instance cache for testRosterId=" + testRosterId + " mSeq=" + mSeq);
               // }
            } else {
                saver.storeResponseSTG(testRosterId, itemSetId, itemId, response, elapsedTime, answerChoiceId);
            }
        } catch (SQLException se) {
        	if(se.getMessage().indexOf("unique constraint (OAS.XAK1ITEM_RESPONSE) violated") >= 0) {
        		// this is a duplicate message, ignore it
        		OASLogger.getLogger("TestDelivery").warning("Duplicate message in storeResponse() with testRosterId="+testRosterId
                        +" itemSetId="+itemSetId+" itemId="+itemId+" response="+response+" elapsedTime="+elapsedTime
                        +" answerChoiceId="+answerChoiceId+" mSeq="+mSeq+" studentMarked="+studentMarked);
        	} else {
	            OASLogger.getLogger("TestDelivery").error("Exception caught in storeResponse() with testRosterId="+testRosterId
	                +" itemSetId="+itemSetId+" itemId="+itemId+" response="+response+" elapsedTime="+elapsedTime
	                +" answerChoiceId="+answerChoiceId+" mSeq="+mSeq+" studentMarked="+studentMarked);
	            se.printStackTrace();
	            throw new InvalidItemResponseException();
        	}
        }
    }
    
    private void storeSubtestRawScore(int testRosterId, int itemSetId, int score, int max, int unscored) throws InvalidTestRosterIdException, InvalidItemSetIdException, InvalidItemResponseException {
        try {
            saver.storeSubtestRawScore(testRosterId, itemSetId, score, max, unscored);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new InvalidItemSetIdException();
        }
    }
        
    private void storeCRResponse(int testRosterId, int itemSetId, String itemId, String response, float elapsedTime, String answerChoiceId, int mSeq, boolean isCTB, String studentMarked) throws InvalidTestRosterIdException, InvalidItemSetIdException, InvalidItemResponseException {
        try {
            storeResponse(testRosterId, itemSetId, itemId, null, elapsedTime, null, mSeq, isCTB, studentMarked);
            if (isCTB) {
/*  no decode to match stg path
                try {
                    response = URLDecoder.decode(response, "UTF-8");
                }
                catch (UnsupportedEncodingException uee) {
                    uee.printStackTrace();
                }
*/                
                saver.deleteCRResponse(testRosterId, itemSetId, itemId);
                saver.storeCRResponse(testRosterId, itemSetId, itemId, response);
            }
            else {
                saver.deleteCRResponseSTG(testRosterId, itemSetId, itemId);
                saver.storeCRResponseSTG(testRosterId, itemSetId, itemId, response);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            throw new InvalidItemResponseException();
        }
    }
    
    private void processHeartbeat(int testRosterId, Ast [] asta, int mseq) 
    {
        if (asta != null && asta.length > 0)
        {
            saver.updateTestRosterTimeStampWithMseq(testRosterId, new Date(), mseq);
        }
    }
    
    private void storeScratchpadContent(int testRosterId, 
                                        int itemSetId, 
                                        String scratchpad) {
        saver.saveSubtestScratchpadContents(testRosterId, itemSetId, scratchpad);
    }
} 
