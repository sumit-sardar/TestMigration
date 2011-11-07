package com.ctb.tms.bean.login;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData;
import noNamespace.BaseType;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ast;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Ov;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Rv;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import com.ctb.tms.exception.testDelivery.AuthenticationFailureException;
import com.ctb.tms.exception.testDelivery.KeyEnteredResponsesException;
import com.ctb.tms.exception.testDelivery.OutsideTestWindowException;
import com.ctb.tms.exception.testDelivery.TestSessionCompletedException;
import com.ctb.tms.exception.testDelivery.TestSessionNotScheduledException;
import com.ctb.tms.util.Constants;
import com.ctb.tms.util.DateUtils;
import com.tangosol.coherence.component.util.Collections;

public class RosterData implements Serializable {
	TmssvcResponseDocument document;
	AuthenticationData authData;
	//Manifest manifest;
	private boolean replicate = true;
	
	static Logger logger = Logger.getLogger(RosterData.class);
	
    public void setReplicate(boolean replicate) {
    	this.replicate = replicate;
    }
	
	public TmssvcResponseDocument getDocument() {
		return this.document;
	}
	
	public TmssvcResponseDocument getLoginDocument() {
		XmlOptions xmlOptions = new XmlOptions(); 
		xmlOptions.setCharacterEncoding("UTF-8");
        xmlOptions.setUnsynchronized();
		TmssvcResponseDocument response = this.document;
		try {
			// were credentials correct?
	        if(authData == null) 
	            throw new AuthenticationFailureException();
	        // has someone already done key entry?
	        if(authData.getCaptureMethod() != null && !authData.getCaptureMethod().equals(Constants.RosterCaptureMethod.CAPTURE_METHOD_ONLINE))
	            throw new KeyEnteredResponsesException();  
	                               
	        // are we outside the test window?
	        Date now = new Date(System.currentTimeMillis());
	        now = DateUtils.getAdjustedDate(now, TimeZone.getDefault().getID(), "GMT", now);
	
	        Date windowStartDateTime = (Date) authData.getWindowStartDate().clone();
	        windowStartDateTime.setHours(authData.getDailyStartTime().getHours());
	        windowStartDateTime.setMinutes(authData.getDailyStartTime().getMinutes());
	        windowStartDateTime.setSeconds(authData.getDailyStartTime().getSeconds());
	
	        Date windowEndDateTime = (Date) authData.getWindowEndDate().clone();
	        windowEndDateTime.setHours(authData.getDailyEndTime().getHours());
	        windowEndDateTime.setMinutes(authData.getDailyEndTime().getMinutes());
	        windowEndDateTime.setSeconds(authData.getDailyEndTime().getSeconds());
	        
	        TimeZone timeZone = TimeZone.getTimeZone(authData.getTimeZone());
	        int startDateOffset = timeZone.getOffset(windowStartDateTime.getTime());
	        int endDateOffset = timeZone.getOffset(windowEndDateTime.getTime());
	        int nowOffset = timeZone.getOffset(now.getTime());
	        
	        windowStartDateTime = new Date(windowStartDateTime.getTime() + startDateOffset - nowOffset);
	        windowEndDateTime = new Date(windowEndDateTime.getTime() + endDateOffset - nowOffset);
	
	        boolean dateBefore = now.compareTo(windowStartDateTime) < 0;
	        boolean dateAfter = now.compareTo(windowEndDateTime) > 0;
	
	        boolean timeBefore = DateUtils.timeBefore(now, windowStartDateTime) &&
	            !(DateUtils.timeBefore(windowEndDateTime, windowStartDateTime) && DateUtils.timeBefore(now, windowEndDateTime));
	        boolean timeAfter = DateUtils.timeAfter(now, windowEndDateTime) &&
	            !(DateUtils.timeAfter(windowStartDateTime, windowEndDateTime) && DateUtils.timeAfter(now, windowStartDateTime));

	        // does the roster have appropriate completion status?
	        String statusCode = authData.getRosterTestCompletionStatus();
	        if(statusCode.equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS))
	            throw new TestSessionCompletedException();
	        if(statusCode.equals(Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS))
	            // do nothing on IP status, allow zero-time restart
	        	//throw new TestSessionInProgressException();
	        	statusCode = Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS;
	        	authData.setRosterTestCompletionStatus(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS);
	        if( dateBefore || dateAfter || timeBefore || timeAfter)
	            throw new OutsideTestWindowException();
	        if(!statusCode.equals(Constants.StudentTestCompletionStatus.SCHEDULED_STATUS) && 
	            !statusCode.equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS) &&
	            !statusCode.equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS))
	            throw new TestSessionNotScheduledException();
		} catch (AuthenticationFailureException afe) {
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.AUTHENTICATION_FAILURE_STATUS); 
        } catch (KeyEnteredResponsesException afe) {
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.KEY_ENTERED_RESPONSES_STATUS); 
        } catch (OutsideTestWindowException afe) {
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.OUTSIDE_TEST_WINDOW_STATUS); 
        } catch (TestSessionCompletedException afe) {
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_COMPLETED_STATUS); 
        } catch (TestSessionNotScheduledException afe) {
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_NOT_SCHEDULED_OR_INTERRUPTED_STATUS); 
        } catch (Exception e) {
        	e.printStackTrace();
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.INTERNAL_SERVER_ERROR_STATUS);
        }
        /*ConsolidatedRestartData restartData = response.getTmssvcResponse().getLoginResponse().getConsolidatedRestartData();
        if(restartData != null) {
	        Tsd[] restartManifest = restartData.getTsdArray();
	        if (restartManifest == null || restartManifest.length < 1) {
	        	response.getTmssvcResponse().getLoginResponse().setConsolidatedRestartData(null);
	        }
        }*/
		return response;
	}
	public void setDocument(TmssvcResponseDocument document) {
		this.document = document;
	}
	public AuthenticationData getAuthData() {
		return authData;
	}
	public void setAuthData(AuthenticationData authData) {
		this.authData = authData;
	}

	/*public Manifest getManifest() {
		return manifest;
	}

	public void setManifest(Manifest manifest) {
		this.manifest = manifest;
	}*/
	
	private static ItemResponseData [] sortItemResponseData(ItemResponseData [] ird) {
		HashMap<Integer, ItemResponseData> sortedIrd = new HashMap<Integer, ItemResponseData>(ird.length);
		for(int i=0;i<ird.length;i++) {
			sortedIrd.put(new Integer(ird[i].getResponseSeqNum()), ird[i]);
		}
		Integer [] keys = sortedIrd.keySet().toArray(new Integer[0]);
		Arrays.sort(keys);
		ird = new ItemResponseData [keys.length];
		for(int i=0;i<keys.length;i++) {
			ird[i] = sortedIrd.get(keys[i]);
		}
		return ird;
	}
	
	public static void generateRestartData(LoginResponse loginResponse, ManifestData manifestData, ItemResponseData [] itemResponseData, ConsolidatedRestartData restartData) throws SQLException {
		itemResponseData = sortItemResponseData(itemResponseData);
		Tsd tsd = restartData.addNewTsd();        
		tsd.setScid(String.valueOf(manifestData.getId()));
		tsd.setLsid(loginResponse.getLsid());
		if (manifestData.getScratchpadContentStr()== null) manifestData.setScratchpadContentStr("");
		tsd.addSp(manifestData.getScratchpadContentStr());
		Ast ast = tsd.addNewAst();
		int maxRSN = 0;
		int totalDur = 0;
		for(int i=0;i<itemResponseData.length;i++) {
			ItemResponseData data = itemResponseData[i];
			tsd.addNewIst();
			Ist ist = tsd.getIstArray(i);
			ist.setIid(data.getItemId());
			ist.setEid(""+data.getEid());
			ist.setCst(Ist.Cst.UNKNOWN);
			ist.setMrk("T".equals(data.getStudentMarked())?"1":"0");
			ist.setDur(data.getResponseElapsedTime());
			ist.setMseq(BigInteger.valueOf(data.getResponseSeqNum()));
			totalDur = totalDur + data.getResponseElapsedTime();
			Rv rv = ist.addNewRv();
			if ("SR".equals(data.getItemType())) {
				rv.setT(BaseType.IDENTIFIER);
				rv.setV(data.getResponse());
			}
			else { 
				rv.setT(BaseType.STRING);
				String crResponse = "";
				String crResponseClob = data.getConstructedResponse();
				if (crResponseClob != null && crResponseClob.length() > 0) {
					crResponse = crResponseClob.substring(1, crResponseClob.length());
				}
				rv.setV(crResponse);
			}
			rv.setN("RESPONSE");
	
			Ov ov = ist.addNewOv();
			ov.setN("SCORE");
			ov.setT(BaseType.INTEGER);
	
			if ("SR".equals(data.getItemType()))
				ov.setV(""+data.getScore());
			else
				ov.setV("");
	
			if(data.getResponseSeqNum() > maxRSN) {
				ast.setCurEid(""+itemResponseData[i].getEid());
				maxRSN = data.getResponseSeqNum();
			}
		}
		manifestData.setTotalTime(totalDur);
		int remSec = (manifestData.getScoDurationMinutes() * 60) - manifestData.getTotalTime();
		ast.setRemSec(Float.parseFloat(String.valueOf(remSec)));
	}
	
	public static ItemResponseData[] generateItemResponseData(ManifestData manifest, ItemResponseWrapper[] tsda) {
		HashMap irdMap = new HashMap(tsda.length);
		HashMap itemMap = new HashMap(tsda.length);
		for(int i=0;i<tsda.length;i++) {
			logger.debug("generateItemResponseData: Tsd " + i);
			noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd tsd = tsda[i].getTsd();
			if(manifest.getId() == Integer.parseInt(tsd.getScid())) {
				noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist[] ista = tsd.getIstArray();
				//for(int j=0;j<ista.length;j++) {	
					//logger.debug("generateItemResponseData: Ist " + j);
					SaveTestingSessionData.Tsd.Ist ist = ista[0];
					BigInteger mapMseq = (BigInteger) itemMap.get(ist.getIid());
					if(mapMseq == null || tsd.getMseq().intValue() > mapMseq.intValue()) {
						itemMap.put(ist.getIid(), tsd.getMseq());
						//   if(ist != null && ist.getRvArray(0) != null && ist.getRvArray(0).getVArray(0) != null) {
				        if(ist != null && ist.getRvArray() != null && ist.getRvArray().length >0 ) {
				            if( ist.getRvArray(0).getVArray() != null && ist.getRvArray(0).getVArray().length >0){
				                if(ist.getRvArray(0).getVArray(0) != null){
				                    BaseType.Enum responseType = ist.getRvArray(0).getT();
				                    String xmlResponse = ist.getRvArray(0).getVArray(0);
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
				                    ItemResponseData ird = new ItemResponseData();
			                    	ird.setEid(Integer.parseInt(ist.getEid()));
			                    	ird.setItemId(ist.getIid());
			                    	ird.setResponse(response);
			                    	String score = "0";
			                    	if(ist.getOvArray() != null && ist.getOvArray().length > 0 && ist.getOvArray(0) != null && ist.getOvArray(0).getVArray() != null && ist.getOvArray(0).getVArray().length > 0 && ist.getOvArray(0).getVArray(0) != null) {
			                    		score = ist.getOvArray(0).getVArray(0);
			                    	}
			                    	if(score.trim().equals("")) score = "0";
			                    	ird.setScore(Integer.parseInt(score));
			                    	ird.setResponseElapsedTime((int)ist.getDur());
			                    	ird.setResponseSeqNum(tsd.getMseq().intValue());
			                    	ird.setStudentMarked(studentMarked);
			                    	ird.setConstructedResponse(response);
			                    	// TODO: fix this
			                    	ird.setItemType("SR");
			                    	irdMap.put(ird.getItemId(), ird);
			                    	logger.debug("RosterData: added restart item response record " + ird.getResponseSeqNum());
				                 }
				            } else { 
				                String response = "";                   
				                String studentMarked = ist.getMrk() ? "T" : "F";
				                ItemResponseData ird = new ItemResponseData();
		                    	ird.setEid(Integer.parseInt(ist.getEid()));
		                    	ird.setItemId(ist.getIid());
		                    	ird.setResponse(response);
		                    	ird.setResponseElapsedTime((int)ist.getDur());
		                    	ird.setResponseSeqNum(tsd.getMseq().intValue());
		                    	ird.setStudentMarked(studentMarked);
		                    	ird.setConstructedResponse(response);
		                    	// TODO: fix this
		                    	ird.setItemType("SR");
		                    	irdMap.put(ird.getItemId(), ird);
		                    	logger.debug("RosterData: added restart item response record " + ird.getResponseSeqNum());
				            }       
				        }
					}
				//}
			}
		}
		return (ItemResponseData[]) irdMap.values().toArray(new ItemResponseData[0]);
	}
	
	public boolean doReplicate() {
		return this.replicate;
	}
}
